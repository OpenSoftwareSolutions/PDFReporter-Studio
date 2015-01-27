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
package com.jaspersoft.hbase.deserialize.impl;

import java.util.regex.Pattern;

import com.jaspersoft.hbase.deserialize.Deserializer;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class ShellDeserializer implements Deserializer {

    private Pattern longPattern = Pattern.compile("-?[0-9]+");

    private Pattern doublePattern = Pattern.compile("-?[0-9]+\\.[0-9]+");

    @Override
    public String deserializeColumnFamily(byte[] columnFamily) {
        if (columnFamily == null) {
            return null;
        }
        return new String(columnFamily);
    }

    @Override
    public String deserializeQualifier(byte[] qualifier) {
        if (qualifier == null) {
            return null;
        }
        return new String(qualifier);
    }

    @Override
    public Object deserializeValue(String tableName, String columnFamily, String qualifier, byte[] value) {
        if (value == null) {
            return null;
        }
        String rawValue = new String(value);
        if (longPattern.matcher(rawValue).matches()) {
            return Long.parseLong(rawValue);
        } else if (doublePattern.matcher(rawValue).matches()) {
            return Double.parseDouble(rawValue);
        }
        return rawValue;
    }

    @Override
    public byte[] serializeColumnFamily(String columnFamily) {
        if (columnFamily == null) {
            return null;
        }
        return columnFamily.getBytes();
    }

    @Override
    public byte[] serializeQualifier(String qualifier) {
        if (qualifier == null) {
            return null;
        }
        return qualifier.getBytes();
    }

    @Override
    public Object deserializeRowId(byte[] rowID) {
        if (rowID == null) {
            return null;
        }
        return new String(rowID);
    }

    @Override
    public byte[] serializeRowId(String rowID) {
        if (rowID == null) {
            return new byte[] {};
        }
        return rowID.getBytes();
    }

    @Override
    public byte[] serializeValue(String tableName, String columnFamily, String qualifier, Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value).getBytes();
    }
}
