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
 * JobRepositoryDestination.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 09, 2010 (01:02:43 CEST) WSDL2Java emitter.
 */

package com.jaspersoft.ireport.jasperserver.ws.scheduler;

public class JobRepositoryDestination  implements java.io.Serializable {
    private long id;

    private int version;

    private java.lang.String folderURI;

    private boolean sequentialFilenames;

    private boolean overwriteFiles;

    private java.lang.String outputDescription;

    private java.lang.String timestampPattern;

    public JobRepositoryDestination() {
    }

    public JobRepositoryDestination(
           long id,
           int version,
           java.lang.String folderURI,
           boolean sequentialFilenames,
           boolean overwriteFiles,
           java.lang.String outputDescription,
           java.lang.String timestampPattern) {
           this.id = id;
           this.version = version;
           this.folderURI = folderURI;
           this.sequentialFilenames = sequentialFilenames;
           this.overwriteFiles = overwriteFiles;
           this.outputDescription = outputDescription;
           this.timestampPattern = timestampPattern;
    }


    /**
     * Gets the id value for this JobRepositoryDestination.
     * 
     * @return id
     */
    public long getId() {
        return id;
    }


    /**
     * Sets the id value for this JobRepositoryDestination.
     * 
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * Gets the version value for this JobRepositoryDestination.
     * 
     * @return version
     */
    public int getVersion() {
        return version;
    }


    /**
     * Sets the version value for this JobRepositoryDestination.
     * 
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }


    /**
     * Gets the folderURI value for this JobRepositoryDestination.
     * 
     * @return folderURI
     */
    public java.lang.String getFolderURI() {
        return folderURI;
    }


    /**
     * Sets the folderURI value for this JobRepositoryDestination.
     * 
     * @param folderURI
     */
    public void setFolderURI(java.lang.String folderURI) {
        this.folderURI = folderURI;
    }


    /**
     * Gets the sequentialFilenames value for this JobRepositoryDestination.
     * 
     * @return sequentialFilenames
     */
    public boolean isSequentialFilenames() {
        return sequentialFilenames;
    }


    /**
     * Sets the sequentialFilenames value for this JobRepositoryDestination.
     * 
     * @param sequentialFilenames
     */
    public void setSequentialFilenames(boolean sequentialFilenames) {
        this.sequentialFilenames = sequentialFilenames;
    }


    /**
     * Gets the overwriteFiles value for this JobRepositoryDestination.
     * 
     * @return overwriteFiles
     */
    public boolean isOverwriteFiles() {
        return overwriteFiles;
    }


    /**
     * Sets the overwriteFiles value for this JobRepositoryDestination.
     * 
     * @param overwriteFiles
     */
    public void setOverwriteFiles(boolean overwriteFiles) {
        this.overwriteFiles = overwriteFiles;
    }


    /**
     * Gets the outputDescription value for this JobRepositoryDestination.
     * 
     * @return outputDescription
     */
    public java.lang.String getOutputDescription() {
        return outputDescription;
    }


    /**
     * Sets the outputDescription value for this JobRepositoryDestination.
     * 
     * @param outputDescription
     */
    public void setOutputDescription(java.lang.String outputDescription) {
        this.outputDescription = outputDescription;
    }


    /**
     * Gets the timestampPattern value for this JobRepositoryDestination.
     * 
     * @return timestampPattern
     */
    public java.lang.String getTimestampPattern() {
        return timestampPattern;
    }


    /**
     * Sets the timestampPattern value for this JobRepositoryDestination.
     * 
     * @param timestampPattern
     */
    public void setTimestampPattern(java.lang.String timestampPattern) {
        this.timestampPattern = timestampPattern;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobRepositoryDestination)) return false;
        JobRepositoryDestination other = (JobRepositoryDestination) obj;
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
            ((this.folderURI==null && other.getFolderURI()==null) || 
             (this.folderURI!=null &&
              this.folderURI.equals(other.getFolderURI()))) &&
            this.sequentialFilenames == other.isSequentialFilenames() &&
            this.overwriteFiles == other.isOverwriteFiles() &&
            ((this.outputDescription==null && other.getOutputDescription()==null) || 
             (this.outputDescription!=null &&
              this.outputDescription.equals(other.getOutputDescription()))) &&
            ((this.timestampPattern==null && other.getTimestampPattern()==null) || 
             (this.timestampPattern!=null &&
              this.timestampPattern.equals(other.getTimestampPattern())));
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
        if (getFolderURI() != null) {
            _hashCode += getFolderURI().hashCode();
        }
        _hashCode += (isSequentialFilenames() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isOverwriteFiles() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOutputDescription() != null) {
            _hashCode += getOutputDescription().hashCode();
        }
        if (getTimestampPattern() != null) {
            _hashCode += getTimestampPattern().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JobRepositoryDestination.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.jasperforge.org/jasperserver/ws", "JobRepositoryDestination"));
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
        elemField.setFieldName("folderURI");
        elemField.setXmlName(new javax.xml.namespace.QName("", "folderURI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequentialFilenames");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sequentialFilenames"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overwriteFiles");
        elemField.setXmlName(new javax.xml.namespace.QName("", "overwriteFiles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "outputDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestampPattern");
        elemField.setXmlName(new javax.xml.namespace.QName("", "timestampPattern"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
