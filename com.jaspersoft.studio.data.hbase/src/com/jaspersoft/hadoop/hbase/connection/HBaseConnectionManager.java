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

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.log4j.Logger;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseConnectionManager {
	private GenericObjectPool<HBaseConnection> connectionsPool;

	private Config poolConfiguration;

	private HBaseConnectionFactory connectionFactory;

	private final Logger logger = Logger.getLogger(HBaseConnectionManager.class);

	public HBaseConnectionManager() {
		connectionFactory = new HBaseConnectionFactory();
		poolConfiguration = new Config();
		poolConfiguration.testOnBorrow = true;
		poolConfiguration.testWhileIdle = true;
		poolConfiguration.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
		poolConfiguration.maxActive = 4;
		poolConfiguration.maxIdle = 2;
		poolConfiguration.minIdle = 1;
	}

	private GenericObjectPool<HBaseConnection> startConnectionsPool() {
		if (connectionsPool == null) {
			connectionsPool = new GenericObjectPool<HBaseConnection>(connectionFactory, poolConfiguration);
		}
		return connectionsPool;
	}

	public HBaseConnection borrowConnection() throws Exception {
		if (connectionsPool == null) {
			startConnectionsPool();
		}
		if (connectionsPool == null) {
			logger.error("No connection pool created");
			return null;
		}
		return connectionsPool.borrowObject();
	}

	public void returnConnection(HBaseConnection connection) {
		if (connectionsPool == null) {
			logger.error("No connection pool created");
			return;
		}
		try {
			connectionsPool.returnObject(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		if (connectionsPool != null) {
			try {
				connectionsPool.clear();
				connectionsPool.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setMaxActive(int maxActive) {
		poolConfiguration.maxActive = maxActive;
	}

	public void setMaxIdle(int maxIdle) {
		poolConfiguration.maxIdle = maxIdle;
	}

	public void setMinIdle(int minIdle) {
		poolConfiguration.minIdle = minIdle;
	}

	public void setHost(String host) {
		connectionFactory.setHost(host);
	}

	public void setPort(String port) {
		connectionFactory.setPort(port);
	}

	public void setClassLoader(ClassLoader classLoader) {
		connectionFactory.setClassLoader(classLoader);
	}
}
