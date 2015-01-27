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
package com.jaspersoft.hadoop.hbase.adapter;



/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseDataAdapterImpl implements HBaseDataAdapter {
	private String zookeeperQuorum = "localhost";

	private String zookeeperClientPort = "2181";

	private String name = "HBaseDataAdapter";

	public String getZookeeperQuorum() {
		return zookeeperQuorum;
	}

	public void setZookeeperQuorum(String zookeeperQuorum) {
		this.zookeeperQuorum = zookeeperQuorum;
	}

	public String getZookeeperClientPort() {
		return zookeeperClientPort;
	}

	public void setZookeeperClientPort(String zookeeperClientPort) {
		this.zookeeperClientPort = zookeeperClientPort;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
