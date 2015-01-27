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
 * UserAndRoleManagementServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 09, 2010 (01:02:43 CEST) WSDL2Java emitter.
 */

package com.jaspersoft.ireport.jasperserver.ws.userandroles;

public class UserAndRoleManagementServiceLocator extends org.apache.axis.client.Service implements com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagementService {

    public UserAndRoleManagementServiceLocator() {
    }


    public UserAndRoleManagementServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UserAndRoleManagementServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UserAndRoleManagementServicePort
    private java.lang.String UserAndRoleManagementServicePort_address = "http://report.airlineplus.net:8080/jasperserver/services/UserAndRoleManagementService";

    public java.lang.String getUserAndRoleManagementServicePortAddress() {
        return UserAndRoleManagementServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UserAndRoleManagementServicePortWSDDServiceName = "UserAndRoleManagementServicePort";

    public java.lang.String getUserAndRoleManagementServicePortWSDDServiceName() {
        return UserAndRoleManagementServicePortWSDDServiceName;
    }

    public void setUserAndRoleManagementServicePortWSDDServiceName(java.lang.String name) {
        UserAndRoleManagementServicePortWSDDServiceName = name;
    }

    public com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagement getUserAndRoleManagementServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UserAndRoleManagementServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUserAndRoleManagementServicePort(endpoint);
    }

    public com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagement getUserAndRoleManagementServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagementServiceSoapBindingStub _stub = new com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagementServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getUserAndRoleManagementServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUserAndRoleManagementServicePortEndpointAddress(java.lang.String address) {
        UserAndRoleManagementServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagement.class.isAssignableFrom(serviceEndpointInterface)) {
                com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagementServiceSoapBindingStub _stub = new com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagementServiceSoapBindingStub(new java.net.URL(UserAndRoleManagementServicePort_address), this);
                _stub.setPortName(getUserAndRoleManagementServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("UserAndRoleManagementServicePort".equals(inputPortName)) {
            return getUserAndRoleManagementServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "UserAndRoleManagementService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "UserAndRoleManagementServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UserAndRoleManagementServicePort".equals(portName)) {
            setUserAndRoleManagementServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
