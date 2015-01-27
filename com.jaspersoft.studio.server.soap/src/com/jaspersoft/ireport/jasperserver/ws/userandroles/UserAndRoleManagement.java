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
/**
 * UserAndRoleManagement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 09, 2010 (01:02:43 CEST) WSDL2Java emitter.
 */

package com.jaspersoft.ireport.jasperserver.ws.userandroles;

public interface UserAndRoleManagement extends java.rmi.Remote {
    public com.jaspersoft.ireport.jasperserver.ws.WSUser[] findUsers(com.jaspersoft.ireport.jasperserver.ws.userandroles.WSUserSearchCriteria criteria) throws java.rmi.RemoteException;
    public com.jaspersoft.ireport.jasperserver.ws.WSUser putUser(com.jaspersoft.ireport.jasperserver.ws.WSUser user) throws java.rmi.RemoteException;
    public void deleteUser(com.jaspersoft.ireport.jasperserver.ws.WSUser user) throws java.rmi.RemoteException;
    public com.jaspersoft.ireport.jasperserver.ws.WSRole[] findRoles(com.jaspersoft.ireport.jasperserver.ws.userandroles.WSRoleSearchCriteria criteria) throws java.rmi.RemoteException;
    public com.jaspersoft.ireport.jasperserver.ws.WSRole putRole(com.jaspersoft.ireport.jasperserver.ws.WSRole role) throws java.rmi.RemoteException;
    public com.jaspersoft.ireport.jasperserver.ws.WSRole updateRoleName(com.jaspersoft.ireport.jasperserver.ws.WSRole oldRole, java.lang.String newName) throws java.rmi.RemoteException;
    public void deleteRole(com.jaspersoft.ireport.jasperserver.ws.WSRole role) throws java.rmi.RemoteException;
}
