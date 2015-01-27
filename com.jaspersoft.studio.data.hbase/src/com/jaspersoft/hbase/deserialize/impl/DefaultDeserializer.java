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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jaspersoft.hbase.deserialize.Deserializer;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class DefaultDeserializer implements Deserializer {

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
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(value);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public byte[] serializeValue(String tableName, String columnFamily, String qualifier, Object value) {
        if (value == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            return byteArrayOutputStream.toByteArray();
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
        return null;
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
}
