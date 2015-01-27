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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.rest.client.RemoteAdmin;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.log4j.Logger;

import com.jaspersoft.hadoop.hbase.connection.HBaseConnection;
import com.jaspersoft.hbase.deserialize.impl.DefaultDeserializer;

/**
 * Imports a relational table into a HBase table
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseImporter {
	private static final Logger logger = Logger.getLogger(HBaseImporter.class);

	private Connection connection;

	private Statement statement;

	private String tableName;

	private HBaseConnection hbaseConnection;

	public HBaseImporter(String tableName) throws IOException, ClassNotFoundException {
		Properties settings = new Properties();
		settings.load(getClass().getClassLoader().getResourceAsStream("HBaseImporter.properties"));
		createConnection(settings);
		this.tableName = tableName;
		hbaseConnection = new HBaseConnection(settings.getProperty("com.jaspersoft.hbase.zookeeperQuorum"), settings.getProperty("com.jaspersoft.hbase.zookeeperClientPort"),
				HBaseImporter.class.getClassLoader());
	}

	private void createConnection(Properties settings) throws ClassNotFoundException {
		Class.forName(settings.getProperty("com.jaspersoft.hbase.driver", null));
		try {
			connection = DriverManager.getConnection(settings.getProperty("com.jaspersoft.hbase.jdbcURL", null), settings.getProperty("com.jaspersoft.hbase.user", null),
					settings.getProperty("com.jaspersoft.hbase.password", null));
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Import the records of the selected table
	 */
	public void importRecords() throws IOException, InterruptedException {
		RemoteAdmin remoteAdmin = new RemoteAdmin(hbaseConnection.getClient(), new Configuration());
		if (remoteAdmin.isTableAvailable(tableName)) {
			logger.info("Table \"" + tableName + "\" exists, droping it");
			remoteAdmin.deleteTable(tableName);
		}
		logger.info("Creating new table \"" + tableName + "\"");

		ResultSet resultSet = null;
		Map<Integer, byte[]> columnNamesMap = new HashMap<Integer, byte[]>();

		try {
			resultSet = statement.executeQuery("SELECT * FROM " + tableName);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount(), index;
			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			HColumnDescriptor columnDescriptor = new HColumnDescriptor(HBaseDataSource.COLUMN_FAMILY);
			tableDescriptor.addFamily(columnDescriptor);
			for (index = 1; index <= columnCount; index++) {
				columnNamesMap.put(index, metaData.getColumnName(index).getBytes());
			}
			remoteAdmin.createTable(tableDescriptor);
			RemoteHTable table = new RemoteHTable(hbaseConnection.getClient(), tableName);
			Put rowPut;
			Object value;
			List<Put> rowPutsList = new ArrayList<Put>();
			long timestamp = System.currentTimeMillis();
			while (resultSet.next()) {
				rowPut = new Put(String.valueOf(timestamp++).getBytes());
				for (index = 1; index <= columnCount; index++) {
					value = resultSet.getObject(index);
					if (value == null) {
						continue;
					}
					rowPut.add(HBaseDataSource.COLUMN_FAMILY, columnNamesMap.get(index), convertObjectToByteArray(value));
				}
				rowPutsList.add(rowPut);
			}
			logger.info("Puts list created, adding in batch");
			table.put(rowPutsList);
			table.flushCommits();
			table.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Close the used resources
	 */
	public void shutdown() {
		try {
			connection.close();
			hbaseConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the imported records
	 */
	public void validate() throws IOException {
		logger.info("Validating the records");
		RemoteHTable table = new RemoteHTable(hbaseConnection.getClient(), tableName);
		Scan scan = new Scan();
		ResultScanner scanner = table.getScanner(scan);
		Result result;
		int count = 5;
		logger.info("The first " + count + " will be displayed");
		DefaultDeserializer deserializer = new DefaultDeserializer();
		try {
			while ((result = scanner.next()) != null && count-- != 0) {
				printMap(result.getMap(), " ", deserializer);
			}
		} finally {
			scanner.close();
			table.close();
		}
	}

	@SuppressWarnings("unchecked")
	private void printMap(NavigableMap<?, ?> map, String indent, DefaultDeserializer deserializer) {
		Object value;
		indent += " ";
		for (Object key : map.keySet()) {
			System.out.println(indent + ((key instanceof byte[]) ? new String((byte[]) key) : String.valueOf(key)));
			value = map.get(key);
			if (value instanceof NavigableMap<?, ?>) {
				printMap((NavigableMap<Object, Object>) value, indent, deserializer);
			} else {
				System.out.println(indent + " " + (value instanceof byte[] ? deserializer.deserializeValue(tableName, "", "", (byte[]) value) : value));
			}
		}
	}

	public static byte[] convertObjectToByteArray(Object value) {
		if (value == null) {
			return null;
		}
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		byte[] result = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(value);
			result = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		HBaseImporter importer = null;
		try {
			importer = new HBaseImporter("accounts");
			importer.importRecords();
			importer.validate();
		} finally {
			if (importer != null) {
				importer.shutdown();
			}
		}
	}
}
