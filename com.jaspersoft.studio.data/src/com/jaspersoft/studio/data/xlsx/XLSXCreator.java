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
package com.jaspersoft.studio.data.xlsx;

import com.jaspersoft.studio.data.xls.XLSCreator;

/**
 * Creator to build a JSS XLSX data adapter from the xml definition of an iReport XLSX 
 * data adapter. The XLS and XLSX are identical as XML definition, so this creator redefine
 * only the id
 * 
 * @author Orlandin Marco
 */
public class XLSXCreator extends XLSCreator {

	@Override
	public String getID() {
		return "com.jaspersoft.ireport.designer.connection.JRXlsxDataSourceConnection";
	}
}
