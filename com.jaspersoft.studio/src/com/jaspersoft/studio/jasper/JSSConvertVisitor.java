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
package com.jaspersoft.studio.jasper;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.base.JRBasePrintFrame;
import net.sf.jasperreports.engine.convert.ConvertVisitor;
import net.sf.jasperreports.engine.convert.ReportConverter;

public class JSSConvertVisitor extends ConvertVisitor {

	public JSSConvertVisitor(ReportConverter reportConverter) {
		super(reportConverter);
	}

	public JSSConvertVisitor(ReportConverter reportConverter, JRBasePrintFrame parentFrame) {
		super(reportConverter, parentFrame);
	}

	@Override
	public void visitChart(JRChart chart) {
		JRPrintElement printImage = ChartConverter.getInstance().convert(reportConverter, chart);
		addElement(parentFrame, printImage);
		addContour(reportConverter, parentFrame, printImage);
	}

	@Override
	public void visitImage(JRImage image) {
		JRPrintElement printImage = ImageConverter.getInstance().convert(reportConverter, image);
		addElement(parentFrame, printImage);
		addContour(reportConverter, parentFrame, printImage);
	}
}
