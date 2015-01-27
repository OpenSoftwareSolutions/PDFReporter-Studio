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
package com.jaspersoft.studio.editor.preview.view.report;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.FillListener;

import com.jaspersoft.studio.editor.preview.stats.Statistics;

public interface IJRPrintable extends FillListener {
	public void setJRPRint(Statistics stats, JasperPrint jrprint) throws Exception;

	public void setJRPRint(Statistics stats, JasperPrint jrprint, boolean refresh) throws Exception;
}
