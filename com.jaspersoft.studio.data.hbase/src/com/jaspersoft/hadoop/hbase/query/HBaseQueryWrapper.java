/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.hadoop.hbase.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sf.jasperreports.engine.JRException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.ColumnCountGetFilter;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.ColumnRangeFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.DependentColumnFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RandomRowFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SkipFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.TimestampsFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.WhileMatchFilter;
import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.log4j.Logger;

import com.jaspersoft.hadoop.hbase.HBaseDataSource;
import com.jaspersoft.hadoop.hbase.connection.HBaseConnection;
import com.jaspersoft.hbase.deserialize.Deserializer;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseQueryWrapper {
    private final static Logger logger = Logger.getLogger(HBaseQueryWrapper.class);

    public static final byte[] EMPTY_ARRAY = new byte[0];

    public final static String DEFAULT_ID_FIELD = "_id_";

    public final static String ALIAS_KEY = "alias";

    public final static String FILTER_KEY = "filter";

    public final static String ROWS_TO_PROCCESS_KEY = "rowsToProcess";

    public final static String BATCH_SIZE_KEY = "batchSize";

    public final static String QUALIFIERS_GROUP_KEY = "qualifiersGroup";

    public final static String QUALIFIERS_EXPRESSION_KEY = "qualifiersExpression";

    public final static String QUALIFIER_JR_KEY = "qualifierJrField";

    public final static String VALUE_JR_KEY = "valueJrField";

    public HBaseConnection connection;

    public Deserializer deserializer;

    public String tableName;

    public String sortFields;

    public HTableInterface table;

    public ResultScanner scanner;

    public String idField;

    public Map<String, Result> sortedMap;

    private byte[] startRow = HConstants.EMPTY_START_ROW;

    private byte[] endRow = HConstants.EMPTY_END_ROW;

    private List<byte[][]> columns;

    public Result currentResult;

    private char columnSeparator;

    private final boolean replaceColumnSeparator;

    private Filter filter;

    private Result[] currentResultArray;

    private int currentResultIndex = 0;

    private int batchSize;

    private Map<String, String> aliasToColumn;

    private Map<String, String> columnToAlias;

    private boolean hasAlias;

    public int rowsToProcess;

    public String qualifierJrField;

    public String valueJrField;

    public Pattern pivotPattern;

    public HBaseQueryWrapper(HBaseConnection connection, String queryString) throws JRException {
        this.connection = connection;
        JSONObject queryObject = null;
        try {
            queryObject = JSONObject.fromObject(queryString);
            Class<?> deserializerClass = Class.forName(queryObject.getString(HBaseDataSource.DESERIALIZER_CLASS), true,
                    connection.getClassLoader());
            deserializer = (Deserializer) deserializerClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (queryObject == null) {
            throw new JRException("No query object found");
        }
        if (deserializer == null) {
            throw new JRException("No deserializer defined");
        }

        processQuery(queryObject);
        replaceColumnSeparator = KeyValue.COLUMN_FAMILY_DELIMITER != columnSeparator;

        try {
            table = new RemoteHTable(connection.getClient(), new Configuration(false), tableName, null);
            Scan scan = null;
            if (filter != null) {
                scan = new Scan(startRow, filter);
            } else {
                scan = new Scan(startRow, endRow);
            }
            if (columns != null && columns.size() > 0) {
                for (byte[][] columnEntry : columns) {
                    if (columnEntry.length == 1) {
                        scan.addFamily(columnEntry[0]);
                    } else {
                        scan.addColumn(columnEntry[0], columnEntry[1]);
                    }
                }
            }
            scanner = table.getScanner(scan);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JRException(e.getMessage());
        }
        queryObject.clear();
        queryObject = null;
    }

    private void processQuery(JSONObject queryObject) throws JRException {
        tableName = queryObject.getString(HBaseDataSource.TABLE_NAME);
        if (queryObject.containsKey(HBaseDataSource.SORT_FIELDS)) {
            sortFields = queryObject.getString(HBaseDataSource.SORT_FIELDS);
        }
        if (queryObject.containsKey(HBaseDataSource.START_ROW)) {
            startRow = deserializer.serializeRowId(queryObject.getString(HBaseDataSource.START_ROW));
        }
        if (queryObject.containsKey(HBaseDataSource.END_ROW)) {
            endRow = deserializer.serializeRowId(queryObject.getString(HBaseDataSource.END_ROW));
        }
        if (queryObject.containsKey(HBaseDataSource.COLUMNS_LIST)) {
            columns = new ArrayList<byte[][]>();
            String[] columnsArray = queryObject.getString(HBaseDataSource.COLUMNS_LIST).split(",");
            for (int index = 0; index < columnsArray.length; index++) {
                columns.add(KeyValue.parseColumn(deserializer.serializeColumnFamily(columnsArray[index].trim())));
            }
        }
        idField = DEFAULT_ID_FIELD;
        if (queryObject.containsKey(HBaseDataSource.ID_FIELD)) {
            idField = queryObject.getString(HBaseDataSource.ID_FIELD);
        }
        columnSeparator = HBaseDataSource.DEFAULT_COLUMN_SEPARATOR;
        if (queryObject.containsKey(HBaseDataSource.COLUMN_SEPARATOR_FIELD)) {
            columnSeparator = queryObject.getString(HBaseDataSource.COLUMN_SEPARATOR_FIELD).charAt(0);
        }
        if (queryObject.containsKey(ROWS_TO_PROCCESS_KEY)) {
            Integer value = processInteger(queryObject.remove(ROWS_TO_PROCCESS_KEY));
            if (value != null) {
                rowsToProcess = value.intValue();
            }
        }
        if (rowsToProcess <= 0) {
            rowsToProcess = 10;
        }
        if (queryObject.containsKey(BATCH_SIZE_KEY)) {
            Integer value = processInteger(queryObject.remove(BATCH_SIZE_KEY));
            if (value != null) {
                batchSize = value.intValue();
            }
        }
        if (batchSize <= 0) {
            batchSize = 100;
        }

        if (queryObject.containsKey(QUALIFIERS_GROUP_KEY)) {
            JSONObject fieldsObject = queryObject.getJSONObject(QUALIFIERS_GROUP_KEY);
            Object value = fieldsObject.remove(QUALIFIERS_EXPRESSION_KEY);
            if (value == null) {
                throw new JRException(QUALIFIERS_EXPRESSION_KEY + " cannot be null");
            }
            String fieldsExpression = String.valueOf(value);
            try {
                pivotPattern = Pattern.compile(fieldsExpression, Pattern.CASE_INSENSITIVE);
                qualifierJrField = String.valueOf(fieldsObject.remove(QUALIFIER_JR_KEY));
                valueJrField = String.valueOf(fieldsObject.remove(VALUE_JR_KEY));
                if (logger.isDebugEnabled()) {
                    logger.debug("Expression: " + fieldsExpression);
                    logger.debug("qualifierJrField: " + qualifierJrField);
                    logger.debug("valueJrField: " + valueJrField);
                }
            } catch (PatternSyntaxException e) {
                e.printStackTrace();
                throw new JRException("Invalid regular expression: " + e.getMessage());
            }
        }

        if (queryObject.containsKey(FILTER_KEY)) {
            JSONObject filterObject = queryObject.getJSONObject(FILTER_KEY);
            filter = processFilter(filterObject);
            if (filter != null) {
                logger.info("Filter defined: " + filter.getClass().getName());
            }
        }

        if (hasAlias = queryObject.containsKey(ALIAS_KEY)) {
            processAlias(queryObject.getJSONObject(ALIAS_KEY));
        }
    }

    private void processAlias(JSONObject aliasObject) {
        aliasToColumn = new HashMap<String, String>();
        columnToAlias = new HashMap<String, String>();
        Iterator<?> keysIterator = aliasObject.keys();
        while (keysIterator.hasNext()) {
            String alias = String.valueOf(keysIterator.next());
            String column = aliasObject.getString(alias);
            aliasToColumn.put(alias, column);
            columnToAlias.put(column, alias);
        }
    }

    private Integer processInteger(Object value) {
        try {
            if (value instanceof Integer) {
                return (Integer) value;
            } else {
                return Integer.parseInt((String) value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Filter processFilter(JSONObject filterObject) {
        if (filterObject.size() > 1) {
            logger.warn("More than one entry in filter struct. Only the first one will be considered.");
        }
        String filterClassName = String.valueOf(filterObject.keys().next());
        filterObject = filterObject.getJSONObject(filterClassName);
        Filter filter = null;
        if ("ColumnCountGetFilter".equals(filterClassName)) {
            filter = new ColumnCountGetFilter(filterObject.getInt("n"));
        } else if ("ColumnPaginationFilter".equals(filterClassName)) {
            filter = new ColumnPaginationFilter(filterObject.getInt("limit"), filterObject.getInt("offset"));
        } else if ("ColumnPrefixFilter".equals(filterClassName)) {
            filter = new ColumnPrefixFilter(deserializer.serializeColumnFamily(filterObject.getString("prefix")));
        } else if ("ColumnRangeFilter".equals(filterClassName)) {
            filter = new ColumnRangeFilter(deserializer.serializeQualifier(filterObject.getString("minColumn")),
                    filterObject.getBoolean("minColumnInclusive"), deserializer.serializeQualifier(filterObject
                            .getString("maxColumn")), filterObject.getBoolean("maxColumnInclusive"));
        } else if ("DependentColumnFilter".equals(filterClassName)) {
            if (filterObject.containsKey("compareOp") && filterObject.containsKey("comparator")) {
                WritableByteArrayComparable comparator = processComparator(filterObject,
                        filterObject.getString("family"), filterObject.getString("qualifier"));
                if (comparator == null) {
                    return null;
                }
                filter = new DependentColumnFilter(
                        deserializer.serializeColumnFamily(filterObject.getString("family")),
                        deserializer.serializeQualifier(filterObject.getString("qualifier")),
                        filterObject.getBoolean("dropDependentColumn"), CompareOp.valueOf(filterObject
                                .getString("compareOp")), comparator);
            } else {
                filter = new DependentColumnFilter(
                        deserializer.serializeColumnFamily(filterObject.getString("family")),
                        deserializer.serializeQualifier(filterObject.getString("qualifier")),
                        filterObject.containsKey("dropDependentColumn") ? filterObject
                                .getBoolean("dropDependentColumn") : false);
            }
        } else if ("FamilyFilter".equals(filterClassName)) {
            WritableByteArrayComparable comparator = processComparator(filterObject, null, null);
            if (comparator == null) {
                return null;
            }
            filter = new FamilyFilter(CompareOp.valueOf(filterObject.getString("compareOp")), comparator);
        } else if ("FilterList".equals(filterClassName)) {
            Operator operator = null;
            List<Filter> filters = null;
            if (filterObject.containsKey("operator")) {
                operator = Operator.valueOf(filterObject.getString("operator"));
            }
            if (filterObject.containsKey("rowFilters")) {
                JSONArray rowFiltersList = filterObject.getJSONArray("rowFilters");
                filters = new ArrayList<Filter>();
                for (int index = 0; index < rowFiltersList.size(); index++) {
                    Filter nestedFilter = processFilter(rowFiltersList.getJSONObject(index));
                    if (nestedFilter != null) {
                        filters.add(nestedFilter);
                    }
                }
            }
            if (operator != null) {
                if (filters != null) {
                    filter = new FilterList(operator, filters);
                } else {
                    filter = new FilterList(operator);
                }
            } else if (filters != null) {
                filter = new FilterList(filters);
            }
        } else if ("FirstKeyOnlyFilter".equals(filterClassName)) {
            filter = new FirstKeyOnlyFilter();
        } else if ("InclusiveStopFilter".equals(filterClassName)) {
            filter = new InclusiveStopFilter(deserializer.serializeRowId(filterObject.getString("stopRowKey")));
        } else if ("KeyOnlyFilter".equals(filterClassName)) {
            filter = new KeyOnlyFilter(filterObject.getBoolean("lenAsVal"));
        } else if ("MultipleColumnPrefixFilter".equals(filterClassName)) {
            List<byte[]> prefixes = new ArrayList<byte[]>();
            JSONArray prefixesList = filterObject.getJSONArray("prefixes");
            if (prefixesList != null) {
                for (Object prefix : prefixesList) {
                    byte[] value = deserializer.serializeQualifier(String.valueOf(prefix));
                    if (value != null) {
                        prefixes.add(value);
                    }
                }
                filter = new MultipleColumnPrefixFilter(prefixes.toArray(new byte[0][0]));
            }
        } else if ("PageFilter".equals(filterClassName)) {
            filter = new PageFilter(filterObject.getLong("pageSize"));
        } else if ("PrefixFilter".equals(filterClassName)) {
            filter = new PrefixFilter(deserializer.serializeColumnFamily(filterObject.getString("prefix")));
        } else if ("QualifierFilter".equals(filterClassName)) {
            WritableByteArrayComparable comparator = processComparator(filterObject, null, null);
            if (comparator == null) {
                return null;
            }
            filter = new QualifierFilter(CompareOp.valueOf(filterObject.getString("compareOp")), comparator);
        } else if ("RandomRowFilter".equals(filterClassName)) {
            filter = new RandomRowFilter((float) filterObject.getDouble("chance"));
        } else if ("RowFilter".equals(filterClassName)) {
            WritableByteArrayComparable comparator = processComparator(filterObject, null, null);
            if (comparator == null) {
                return null;
            }
            filter = new RowFilter(CompareOp.valueOf(filterObject.getString("compareOp")), comparator);
        } else if ("ValueFilter".equals(filterClassName)) {
            WritableByteArrayComparable comparator = processComparator(filterObject, null, null);
            if (comparator == null) {
                return null;
            }
            filter = new ValueFilter(CompareOp.valueOf(filterObject.getString("compareOp")), comparator);
        } else if ("SingleColumnValueExcludeFilter".equals(filterClassName)) {
            if (filterObject.containsKey("comparator")) {
                WritableByteArrayComparable comparator = processComparator(filterObject,
                        filterObject.getString("family"), filterObject.getString("qualifier"));
                if (comparator == null) {
                    return null;
                }
                filter = new SingleColumnValueExcludeFilter(deserializer.serializeColumnFamily(filterObject
                        .getString("family")), deserializer.serializeQualifier(filterObject.getString("qualifier")),
                        CompareOp.valueOf(filterObject.getString("compareOp")), comparator);
            } else {
                filter = new SingleColumnValueExcludeFilter(deserializer.serializeColumnFamily(filterObject
                        .getString("family")), deserializer.serializeQualifier(filterObject.getString("qualifier")),
                        CompareOp.valueOf(filterObject.getString("compareOp")), deserializer.serializeValue(tableName,
                                filterObject.getString("family"), filterObject.getString("qualifier"),
                                filterObject.getString("value")));
            }
        } else if ("SingleColumnValueFilter".equals(filterClassName)) {
            if (filterObject.containsKey("comparator")) {
                WritableByteArrayComparable comparator = processComparator(filterObject,
                        filterObject.getString("family"), filterObject.getString("qualifier"));
                if (comparator == null) {
                    return null;
                }
                filter = new SingleColumnValueFilter(deserializer.serializeColumnFamily(filterObject
                        .getString("family")), deserializer.serializeQualifier(filterObject.getString("qualifier")),
                        CompareOp.valueOf(filterObject.getString("compareOp")), comparator);
            } else {
                filter = new SingleColumnValueFilter(deserializer.serializeColumnFamily(filterObject
                        .getString("family")), deserializer.serializeQualifier(filterObject.getString("qualifier")),
                        CompareOp.valueOf(filterObject.getString("compareOp")), deserializer.serializeValue(tableName,
                                filterObject.getString("family"), filterObject.getString("qualifier"),
                                filterObject.getString("value")));
            }
        } else if ("SkipFilter".equals(filterClassName)) {
            filter = new SkipFilter(processFilter(filterObject.getJSONObject("filter")));
        } else if ("WhileMatchFilter".equals(filterClassName)) {
            filter = new WhileMatchFilter(processFilter(filterObject.getJSONObject("filter")));
        } else if ("TimestampsFilter".equals(filterClassName)) {
            JSONArray timestampsList = filterObject.getJSONArray("timestamps");
            List<Long> timestamps = new ArrayList<Long>();
            for (int index = 0; index < timestampsList.size(); index++) {
                timestamps.add(timestampsList.getLong(index));
            }
            filter = new TimestampsFilter(timestamps);
        } else {
            logger.error("Unrecognized filter: " + filterClassName);
        }
        return filter;
    }

    private WritableByteArrayComparable processComparator(JSONObject filterObject, String family, String qualifier) {
        WritableByteArrayComparable comparator = null;
        JSONObject comparatorObject = filterObject.getJSONObject("comparator");
        if (comparatorObject.size() > 1) {
            logger.warn("More than one comparator defined. Only one will be used.");
        }
        String comparatorName = String.valueOf(comparatorObject.keys().next());
        comparatorObject = comparatorObject.getJSONObject(comparatorName);
        if ("BinaryComparator".equals(comparatorName)) {
            comparator = new BinaryComparator(deserializer.serializeValue(tableName, family, qualifier,
                    comparatorObject.getString("value")));
        } else if ("BinaryPrefixComparator".equals(comparatorName)) {
            comparator = new BinaryPrefixComparator(deserializer.serializeValue(tableName, family, qualifier,
                    comparatorObject.getString("value")));
        } else if ("RegexStringComparator".equals(comparatorName)) {
            comparator = new RegexStringComparator(comparatorObject.has("expr") ? comparatorObject.getString("expr")
                    : comparatorObject.getString("substr"));
        } else if ("SubstringComparator".equals(comparatorName)) {
            comparator = new SubstringComparator(comparatorObject.has("expr") ? comparatorObject.getString("expr")
                    : comparatorObject.getString("substr"));
        }
        if (comparator == null) {
            logger.error("Unknown comparator: " + comparatorName);
        }
        return comparator;
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
        if (table != null) {
            table = null;
        }
    }

    public void moveNext() {
        currentResult = null;
        try {
            if (currentResultArray == null || currentResultIndex == currentResultArray.length) {
                currentResultArray = scanner.next(batchSize);
                currentResultIndex = 0;
            }
            if (currentResultArray != null) {
                currentResult = currentResultArray[currentResultIndex++];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getColumnFromAlias(String alias) {
        if (hasAlias) {
            String column = aliasToColumn.get(alias);
            if (column != null) {
                return column;
            }
        }
        return alias;
    }

    public String getAliasForColumn(String column) {
        if (hasAlias) {
            String alias = columnToAlias.get(column);
            if (alias != null) {
                return alias;
            }
        }
        return column;
    }

    public Object getColumnValue(String columnName) throws JRException {
        byte[][] column;
        columnName = getColumnFromAlias(columnName);
        if (columnName.equals(idField)) {
            return deserializer.deserializeRowId(currentResult.getRow());
        }
        int lastIndex = columnName.lastIndexOf(columnSeparator);
        if (lastIndex == -1) {
            logger.error("Column name \"" + columnName + "\" must contain delimiter: " + columnSeparator);
            return null;
        }
        String columnFamily = columnName.substring(0, lastIndex);
        String qualifier = columnName.substring(lastIndex + 1, columnName.length());
        if (replaceColumnSeparator) {
            column = new byte[2][];
            column[0] = deserializer.serializeColumnFamily(columnFamily);
            column[1] = deserializer.serializeQualifier(qualifier);
        } else {
            column = KeyValue.parseColumn(deserializer.serializeColumnFamily(columnName));
        }
        if (column.length < 2) {
            logger.error("No value for column name: " + columnName);
            return null;
        }
        return deserializer.deserializeValue(tableName, columnFamily, qualifier,
                currentResult.getValue(column[0], column[1]));
    }

    public char getColumnSeparator() {
        return columnSeparator;
    }
}
