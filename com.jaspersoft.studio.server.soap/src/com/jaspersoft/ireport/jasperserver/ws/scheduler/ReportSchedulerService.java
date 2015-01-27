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
 * ReportSchedulerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 09, 2010 (01:02:43 CEST) WSDL2Java emitter.
 */

package com.jaspersoft.ireport.jasperserver.ws.scheduler;

public interface ReportSchedulerService extends javax.xml.rpc.Service {
    public java.lang.String getReportSchedulerAddress();

    public com.jaspersoft.ireport.jasperserver.ws.scheduler.ReportScheduler getReportScheduler() throws javax.xml.rpc.ServiceException;

    public com.jaspersoft.ireport.jasperserver.ws.scheduler.ReportScheduler getReportScheduler(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
