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
 * JobMailNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 09, 2010 (01:02:43 CEST) WSDL2Java emitter.
 */

package com.jaspersoft.ireport.jasperserver.ws.scheduler;

public class JobMailNotification  implements java.io.Serializable {
    private long id;

    private int version;

    private java.lang.String[] toAddresses;

    private java.lang.String subject;

    private java.lang.String messageText;

    private com.jaspersoft.ireport.jasperserver.ws.scheduler.ResultSendType resultSendType;

    private boolean skipEmptyReports;

    public JobMailNotification() {
    }

    public JobMailNotification(
           long id,
           int version,
           java.lang.String[] toAddresses,
           java.lang.String subject,
           java.lang.String messageText,
           com.jaspersoft.ireport.jasperserver.ws.scheduler.ResultSendType resultSendType,
           boolean skipEmptyReports) {
           this.id = id;
           this.version = version;
           this.toAddresses = toAddresses;
           this.subject = subject;
           this.messageText = messageText;
           this.resultSendType = resultSendType;
           this.skipEmptyReports = skipEmptyReports;
    }


    /**
     * Gets the id value for this JobMailNotification.
     * 
     * @return id
     */
    public long getId() {
        return id;
    }


    /**
     * Sets the id value for this JobMailNotification.
     * 
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * Gets the version value for this JobMailNotification.
     * 
     * @return version
     */
    public int getVersion() {
        return version;
    }


    /**
     * Sets the version value for this JobMailNotification.
     * 
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }


    /**
     * Gets the toAddresses value for this JobMailNotification.
     * 
     * @return toAddresses
     */
    public java.lang.String[] getToAddresses() {
        return toAddresses;
    }


    /**
     * Sets the toAddresses value for this JobMailNotification.
     * 
     * @param toAddresses
     */
    public void setToAddresses(java.lang.String[] toAddresses) {
        this.toAddresses = toAddresses;
    }


    /**
     * Gets the subject value for this JobMailNotification.
     * 
     * @return subject
     */
    public java.lang.String getSubject() {
        return subject;
    }


    /**
     * Sets the subject value for this JobMailNotification.
     * 
     * @param subject
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }


    /**
     * Gets the messageText value for this JobMailNotification.
     * 
     * @return messageText
     */
    public java.lang.String getMessageText() {
        return messageText;
    }


    /**
     * Sets the messageText value for this JobMailNotification.
     * 
     * @param messageText
     */
    public void setMessageText(java.lang.String messageText) {
        this.messageText = messageText;
    }


    /**
     * Gets the resultSendType value for this JobMailNotification.
     * 
     * @return resultSendType
     */
    public com.jaspersoft.ireport.jasperserver.ws.scheduler.ResultSendType getResultSendType() {
        return resultSendType;
    }


    /**
     * Sets the resultSendType value for this JobMailNotification.
     * 
     * @param resultSendType
     */
    public void setResultSendType(com.jaspersoft.ireport.jasperserver.ws.scheduler.ResultSendType resultSendType) {
        this.resultSendType = resultSendType;
    }


    /**
     * Gets the skipEmptyReports value for this JobMailNotification.
     * 
     * @return skipEmptyReports
     */
    public boolean isSkipEmptyReports() {
        return skipEmptyReports;
    }


    /**
     * Sets the skipEmptyReports value for this JobMailNotification.
     * 
     * @param skipEmptyReports
     */
    public void setSkipEmptyReports(boolean skipEmptyReports) {
        this.skipEmptyReports = skipEmptyReports;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobMailNotification)) return false;
        JobMailNotification other = (JobMailNotification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            this.version == other.getVersion() &&
            ((this.toAddresses==null && other.getToAddresses()==null) || 
             (this.toAddresses!=null &&
              java.util.Arrays.equals(this.toAddresses, other.getToAddresses()))) &&
            ((this.subject==null && other.getSubject()==null) || 
             (this.subject!=null &&
              this.subject.equals(other.getSubject()))) &&
            ((this.messageText==null && other.getMessageText()==null) || 
             (this.messageText!=null &&
              this.messageText.equals(other.getMessageText()))) &&
            ((this.resultSendType==null && other.getResultSendType()==null) || 
             (this.resultSendType!=null &&
              this.resultSendType.equals(other.getResultSendType()))) &&
            this.skipEmptyReports == other.isSkipEmptyReports();
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
        _hashCode += new Long(getId()).hashCode();
        _hashCode += getVersion();
        if (getToAddresses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getToAddresses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getToAddresses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSubject() != null) {
            _hashCode += getSubject().hashCode();
        }
        if (getMessageText() != null) {
            _hashCode += getMessageText().hashCode();
        }
        if (getResultSendType() != null) {
            _hashCode += getResultSendType().hashCode();
        }
        _hashCode += (isSkipEmptyReports() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JobMailNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "JobMailNotification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("toAddresses");
        elemField.setXmlName(new javax.xml.namespace.QName("", "toAddresses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subject");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "messageText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultSendType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultSendType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "ResultSendType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skipEmptyReports");
        elemField.setXmlName(new javax.xml.namespace.QName("", "skipEmptyReports"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
