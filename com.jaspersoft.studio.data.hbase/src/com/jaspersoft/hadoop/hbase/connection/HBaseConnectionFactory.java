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

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.log4j.Logger;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseConnectionFactory implements PoolableObjectFactory<HBaseConnection> {
	private final Logger logger = Logger.getLogger(HBaseConnectionFactory.class);

	private String host;

	private String port;

	private ClassLoader classLoader;

	@Override
	public void activateObject(HBaseConnection connection) throws Exception {
		if (connection != null) {
			connection.setClassLoader(classLoader);
		}
	}

	@Override
	public void destroyObject(HBaseConnection connection) throws Exception {
		System.out.println("Factory destroy object");
		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public HBaseConnection makeObject() throws Exception {
		System.out.println("Factory make object");
		return new HBaseConnection(host, port, classLoader);
	}

	@Override
	public void passivateObject(HBaseConnection connection) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Passivate is not implemented");
		}
	}

	@Override
	public boolean validateObject(HBaseConnection connection) {
		System.out.println("Factory validate object");
		if (connection != null) {
			try {
				return host.equals(connection.getRestHost()) && port.equals(connection.getRestPort())
						&& connection.test().contains("success");
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
}
