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
package com.jaspersoft.hbase.deserialize;

/**
 * 
 * @author Eric Diaz
 * 
 */
public interface Deserializer {
    public Object deserializeRowId(byte[] rowID);

    public String deserializeColumnFamily(byte[] columnFamily);

    public String deserializeQualifier(byte[] qualifier);

    public Object deserializeValue(String tableName, String columnFamily, String qualifier, byte[] value);

    public byte[] serializeRowId(String rowID);

    public byte[] serializeColumnFamily(String columnFamily);

    public byte[] serializeQualifier(String qualifier);

    public byte[] serializeValue(String tableName, String columnFamily, String qualifier, Object value);
}
