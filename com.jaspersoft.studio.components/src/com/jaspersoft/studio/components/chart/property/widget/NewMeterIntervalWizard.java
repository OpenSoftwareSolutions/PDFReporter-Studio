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
package com.jaspersoft.studio.components.chart.property.widget;

import java.awt.Color;

import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.util.JRMeterInterval;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.RGB;

/**
 * Wizard with a single page to define a series for the Meter chart
 * 
 * @author Orlandin Marco
 *
 */
public class NewMeterIntervalWizard extends Wizard {

	/**
	 * Page where the attributes are defined
	 */
	private NewMeterIntervalPage page0;
	
	/**
	 * The series defined
	 */
	private JRMeterInterval jrm = null;
	
	@Override
	public boolean performFinish() {
		jrm = new JRMeterInterval();
		jrm.setLabel(page0.getLabel());
		RGB rgb = page0.getBackgroundColor().getRgb();
		Color color = new Color(rgb.red, rgb.green, rgb.blue);
		jrm.setBackgroundColor(color);
		jrm.setAlpha(page0.getBackgroundColor().getAlfa()/ 255.0d);
		JRDesignDataRange dataRange = new JRDesignDataRange(null);
		dataRange.setHighExpression(page0.getHighExpresion());
		dataRange.setLowExpression(page0.getLowExpression());
		jrm.setDataRange(dataRange);
		return true;
	}
	
	/**
	 * The series with the defined attributed when the wizard is completed
	 * or null if it is cancelled
	 * 
	 * @return a meter series or null
	 */
	public JRMeterInterval getMeterInterval(){
		return jrm;
	}

	
	@Override
	public void addPages() {
		page0 = new NewMeterIntervalPage();
		addPage(page0);
	}
	

}
