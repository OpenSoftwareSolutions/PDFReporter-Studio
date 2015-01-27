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
 * WSObjectPermission.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 09, 2010 (01:02:43 CEST) WSDL2Java emitter.
 */

package com.jaspersoft.ireport.jasperserver.ws.permissions;

public class WSObjectPermission  implements java.io.Serializable {
    private java.lang.String uri;

    private java.lang.Object permissionRecipient;

    private int permissionMask;

    public WSObjectPermission() {
    }

    public WSObjectPermission(
           java.lang.String uri,
           java.lang.Object permissionRecipient,
           int permissionMask) {
           this.uri = uri;
           this.permissionRecipient = permissionRecipient;
           this.permissionMask = permissionMask;
    }


    /**
     * Gets the uri value for this WSObjectPermission.
     * 
     * @return uri
     */
    public java.lang.String getUri() {
        return uri;
    }


    /**
     * Sets the uri value for this WSObjectPermission.
     * 
     * @param uri
     */
    public void setUri(java.lang.String uri) {
        this.uri = uri;
    }


    /**
     * Gets the permissionRecipient value for this WSObjectPermission.
     * 
     * @return permissionRecipient
     */
    public java.lang.Object getPermissionRecipient() {
        return permissionRecipient;
    }


    /**
     * Sets the permissionRecipient value for this WSObjectPermission.
     * 
     * @param permissionRecipient
     */
    public void setPermissionRecipient(java.lang.Object permissionRecipient) {
        this.permissionRecipient = permissionRecipient;
    }


    /**
     * Gets the permissionMask value for this WSObjectPermission.
     * 
     * @return permissionMask
     */
    public int getPermissionMask() {
        return permissionMask;
    }


    /**
     * Sets the permissionMask value for this WSObjectPermission.
     * 
     * @param permissionMask
     */
    public void setPermissionMask(int permissionMask) {
        this.permissionMask = permissionMask;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WSObjectPermission)) return false;
        WSObjectPermission other = (WSObjectPermission) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.uri==null && other.getUri()==null) || 
             (this.uri!=null &&
              this.uri.equals(other.getUri()))) &&
            ((this.permissionRecipient==null && other.getPermissionRecipient()==null) || 
             (this.permissionRecipient!=null &&
              this.permissionRecipient.equals(other.getPermissionRecipient()))) &&
            this.permissionMask == other.getPermissionMask();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getUri() != null) {
            _hashCode += getUri().hashCode();
        }
        if (getPermissionRecipient() != null) {
            _hashCode += getPermissionRecipient().hashCode();
        }
        _hashCode += getPermissionMask();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WSObjectPermission.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "WSObjectPermission"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uri");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uri"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("permissionRecipient");
        elemField.setXmlName(new javax.xml.namespace.QName("", "permissionRecipient"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("permissionMask");
        elemField.setXmlName(new javax.xml.namespace.QName("", "permissionMask"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
