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
package com.jaspersoft.hadoop.hbase.connection;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.json.JSONObject;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.thrift.generated.IOError;
import org.apache.hadoop.hbase.thrift.generated.IllegalArgument;
import org.apache.hadoop.hbase.thrift.generated.TCell;
import org.apache.hadoop.hbase.thrift.generated.TRowResult;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import com.jaspersoft.hbase.deserialize.Deserializer;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class ThriftQueryWrapper {
	private final static Logger logger = Logger.getLogger(ThriftQueryWrapper.class);

	public static final String TABLE_NAME = "tableName";

	public static final String SORT_FIELDS = "sortFields";

	public static final String DESERIALIZER_CLASS = "deserializerClass";

	public static final String START_ROW = "startRow";

	public static final String END_ROW = "endRow";

	public static final String COLUMNS_LIST = "columnList";

	public static final String ID_FIELD = "idField";

	public static final String COLUMN_SEPARATOR_FIELD = "columnSeparator";

	public static final char DEFAULT_COLUMN_SEPARATOR = '|';

	public final static String DEFAULT_ID_FIELD = "_id_";

	private static final int ROW_BATCH_AMOUNT = 100;

	public String tableName;

	public String sortFields;

	public ThriftConnection connection;

	public Deserializer deserializer;

	public int scannerID;

	public Map<String, TRowResult> sortedMap;

	public String idField;

	public final static byte[] EMPTY_ARRAY = new byte[0];

	private byte[] startRow = EMPTY_ARRAY;

	private byte[] endRow = EMPTY_ARRAY;

	private List<ByteBuffer> columns;

	public Map<String, ByteBuffer> cellFields;

	public TRowResult currentResult;

	private char columnSeparator;

	private List<TRowResult> currentRowList;

	private Iterator<TRowResult> currentRowListIterator;

	public ThriftQueryWrapper(ThriftConnection connection, String queryString) throws JRException {
		this.connection = connection;
		JSONObject queryObject = null;
		try {
			queryObject = JSONObject.fromObject(queryString);
			Class<?> deserializerClass = Class.forName(queryObject.getString(DESERIALIZER_CLASS), true, connection.getClassLoader());
			deserializer = (Deserializer) deserializerClass.getConstructors()[0].newInstance();
			cellFields = new HashMap<String, ByteBuffer>();
			columns = new ArrayList<ByteBuffer>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (queryObject == null) {
			throw new JRException("No query object found");
		}
		if (deserializer == null) {
			throw new JRException("No deserializer defined");
		}
		tableName = queryObject.getString(TABLE_NAME);
		if (queryObject.containsKey(SORT_FIELDS)) {
			sortFields = queryObject.getString(SORT_FIELDS);
		}
		if (queryObject.containsKey(START_ROW)) {
			startRow = deserializer.serializeRowId(queryObject.getString(START_ROW));
		}
		if (queryObject.containsKey(END_ROW)) {
			endRow = deserializer.serializeRowId(queryObject.getString(END_ROW));
		}
		if (queryObject.containsKey(COLUMNS_LIST)) {
			String[] columnsArray = queryObject.getString(COLUMNS_LIST).split(",");
			for (int index = 0; index < columnsArray.length; index++) {
				byte[] currentColumn = deserializer.serializeColumnFamily(columnsArray[index].trim());
				byte[][] column = KeyValue.parseColumn(currentColumn);
				if (column.length == 0) {
					logger.error("Invalid column name: " + columnsArray[index]);
				} else if (column.length == 1) {
					columns.add(ByteBuffer.wrap(column[0]));
				} else if (column.length >= 2) {
					columns.add(ByteBuffer.wrap(currentColumn));
				}
			}
		}
		idField = DEFAULT_ID_FIELD;
		if (queryObject.containsKey(ID_FIELD)) {
			idField = queryObject.getString(ID_FIELD);
		}
		columnSeparator = DEFAULT_COLUMN_SEPARATOR;
		if (queryObject.containsKey(COLUMN_SEPARATOR_FIELD)) {
			columnSeparator = queryObject.getString(COLUMN_SEPARATOR_FIELD).charAt(0);
		}
		try {
			byte[] tableNameBytes = tableName.getBytes();
			Map<ByteBuffer, ByteBuffer> attributes = new HashMap<ByteBuffer, ByteBuffer>();
			if (endRow.length == 0) {
				scannerID = connection.getClient().scannerOpen(ByteBuffer.wrap(tableNameBytes), ByteBuffer.wrap(startRow), columns, attributes);
			} else {
				scannerID = connection.getClient().scannerOpenWithStop(ByteBuffer.wrap(tableNameBytes), ByteBuffer.wrap(startRow), ByteBuffer.wrap(endRow), columns, attributes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (scannerID == -1) {
			close();
			throw new JRException("Unable to create Thrift scanner");
		}
	}

	public void close() {
		if (scannerID != -1) {
			try {
				connection.getClient().scannerClose(scannerID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			scannerID = -1;
		}
	}

	public void moveNext() {
		currentResult = null;
		try {
			if (currentRowList == null || !currentRowListIterator.hasNext()) {
				logger.info("Trying to retrieve the next " + ROW_BATCH_AMOUNT + " rows");
				currentRowList = connection.getClient().scannerGetList(scannerID, ROW_BATCH_AMOUNT);
				currentRowListIterator = null;
				if (currentRowList != null && currentRowList.size() > 0) {
					currentRowListIterator = currentRowList.iterator();
				}
			}
			if (currentRowListIterator != null && currentRowListIterator.hasNext()) {
				currentResult = currentRowListIterator.next();
			}
		} catch (IOError e) {
			e.printStackTrace();
		} catch (IllegalArgument e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		processResult();
	}

	public void processResult() {
		cellFields.clear();
		if (currentResult == null) {
			return;
		}
		boolean replaceColumnSeparator = columnSeparator != KeyValue.COLUMN_FAMILY_DELIMITER;
		for (ByteBuffer key : currentResult.getColumns().keySet()) {
			String fieldName = new String(key.array());
			if (replaceColumnSeparator) {
				fieldName = fieldName.replace(KeyValue.COLUMN_FAMILY_DELIMITER, columnSeparator);
			}
			cellFields.put(fieldName, key);
		}
	}

	public Object getColumnFieldValue(String fieldName) throws JRException {
		TCell cell = currentResult.getColumns().get(cellFields.get(fieldName));
		if (cell == null) {
			logger.error("No value for field: " + fieldName);
			return null;
		}
		int lastIndex = fieldName.lastIndexOf(columnSeparator);
		if (lastIndex == -1) {
			logger.error("Column name \"" + fieldName + "\" must contain delimiter: " + columnSeparator);
			return null;
		}
		String columnFamily = fieldName.substring(0, lastIndex);
		String qualifier = fieldName.substring(lastIndex + 1, fieldName.length());
		return deserializer.deserializeValue(tableName, columnFamily, qualifier, cell.getValue());
	}
}
