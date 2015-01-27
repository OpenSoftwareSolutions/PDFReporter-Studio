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
package com.jaspersoft.hadoop.hbase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.log4j.Logger;

import com.jaspersoft.hadoop.hbase.query.HBaseQueryWrapper;

/**
 * An implementation of a data source that uses an empty query and parameters
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseDataSource implements JRDataSource {
	public static final String CONNECTION = "com.jaspersoft.hbase.connection";

	public static final String TABLE_NAME = "tableName";

	public static final String SORT_FIELDS = "sortFields";

	public static final String DESERIALIZER_CLASS = "deserializerClass";

	public static final String START_ROW = "startRow";

	public static final String END_ROW = "endRow";

	public static final String COLUMNS_LIST = "columnList";

	public static final String ID_FIELD = "idField";

	public static final String COLUMN_SEPARATOR_FIELD = "columnSeparator";

	public static final String QUERY_LANGUAGE = "HBaseQuery";

	public static final char DEFAULT_COLUMN_SEPARATOR = '|';

	private boolean sorted;

	private Iterator<String> recordsIterator;

	private HBaseQueryWrapper wrapper;

	private Map<String, Object> pivotFieldsMap;

	private boolean pivot;

	private Iterator<Entry<String, Object>> pivotFieldsIterator;

	private final static Logger logger = Logger.getLogger(HBaseDataSource.class);

	public static final byte[] COLUMN_FAMILY = "schema".getBytes();

	private Entry<String, Object> currentPivotEntry;

	public HBaseDataSource(HBaseQueryWrapper wrapper) {
		this.wrapper = wrapper;
		sorted = wrapper.sortedMap != null;
		if (sorted) {
			recordsIterator = wrapper.sortedMap.keySet().iterator();
		}
		pivot = wrapper.pivotPattern != null;
		if (pivot) {
			pivotFieldsMap = new HashMap<String, Object>();
			pivotFieldsIterator = pivotFieldsMap.entrySet().iterator();
			if (logger.isDebugEnabled()) {
				logger.debug("Pivot query");
			}
		}
		logger.info("New HBaseDataSource");
	}

	/**
	 * Gets the field value for the current position.
	 */
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		if (pivot) {
			if (field.getName().equals(wrapper.qualifierJrField)) {
				return currentPivotEntry.getKey();
			} else if (field.getName().equals(wrapper.valueJrField)) {
				return currentPivotEntry.getValue();
			}
		}
		return wrapper.getColumnValue(field.getName());
	}

	/**
	 * Tries to position the cursor on the next element in the data source.
	 */
	@Override
	public boolean next() throws JRException {
		boolean next = false;
		if (pivot) {
			if (!(next = pivotFieldsIterator.hasNext())) {
				pivotFieldsIterator = null;
				pivotFieldsMap.clear();
				while (next = moveNext()) {
					HBaseFieldsProvider.processFieldsForCurrentResult(wrapper, pivotFieldsMap, false);
					if (logger.isDebugEnabled()) {
						logger.debug("Pivot fields map: " + pivotFieldsMap);
					}
					if (pivotFieldsMap.size() > 0) {
						pivotFieldsIterator = pivotFieldsMap.entrySet().iterator();
						break;
					}
				}
				next = pivotFieldsIterator != null;
			}
			if (next) {
				currentPivotEntry = pivotFieldsIterator.next();
			}
		} else {
			next = moveNext();
		}
		return next;
	}

	private boolean moveNext() {
		boolean next = false;
		if (sorted) {
			if (next = recordsIterator.hasNext()) {
				wrapper.currentResult = wrapper.sortedMap.get(recordsIterator.next());
			}
		} else {
			wrapper.moveNext();
			next = wrapper.currentResult != null;
		}
		return next;
	}
}
