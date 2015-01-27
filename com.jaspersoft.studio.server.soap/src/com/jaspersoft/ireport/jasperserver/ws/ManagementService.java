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
package com.jaspersoft.ireport.jasperserver.ws;

public interface ManagementService extends java.rmi.Remote {
    public java.lang.String runReport(java.lang.String requestXmlString) throws java.rmi.RemoteException;
    public java.lang.String put(java.lang.String requestXmlString) throws java.rmi.RemoteException;
    public java.lang.String get(java.lang.String requestXmlString) throws java.rmi.RemoteException;
    public java.lang.String list(java.lang.String requestXmlString) throws java.rmi.RemoteException;
    public java.lang.String delete(java.lang.String requestXmlString) throws java.rmi.RemoteException;
    public java.lang.String copy(java.lang.String requestXmlString) throws java.rmi.RemoteException;
    public java.lang.String move(java.lang.String requestXmlString) throws java.rmi.RemoteException;
}
