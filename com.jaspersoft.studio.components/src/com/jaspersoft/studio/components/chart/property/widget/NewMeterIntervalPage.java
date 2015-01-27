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

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.ColorStyledText;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * 
 * Page where the user can define the attributes of a series for the Meter Chart
 * 
 * @author Orlandin Marco
 *
 */
public class NewMeterIntervalPage extends JSSHelpWizardPage {

	/**
	 * Text area where the user type the series name
	 */
	private Text label;
	
	/**
	 * Widget used to show the actually selected color
	 */
	private ColorStyledText backgroundColor;
	
	/**
	 * Widget used to input the low expression
	 */
	private WTextExpression lowExpression;
	
	/**
	 * Widget used to input the high expression
	 */
	private WTextExpression highExpression;
	
	/**
	 * Field where the series name is stored
	 */
	private String labelValue;
	
	/**
	 * Field where the background color is stored
	 */
	private AlfaRGB backgroundValue;
	
	/**
	 * Field where the low expression value is stored
	 */
	private JRDesignExpression lowExpressionValue;
	
	
	/**
	 * Field where the high expression is stored
	 */
	private JRDesignExpression highExpressionValue;
	
	protected NewMeterIntervalPage() {
		super("Meter Page"); //$NON-NLS-1$
		setTitle(Messages.NewMeterIntervalPage_1);
	}
	
	/**
	 * Listener called when an expression is changed, validate the page and store the fields
	 */
	private ExpressionModifiedListener expModified = new ExpressionModifiedListener() {
		
		@Override
		public void expressionModified(ExpressionModifiedEvent event) {
			updateStatus();
		}
	};
	
	/**
	 * Listener called when the series name or color changes, validate the page and store the fields
	 */
	private ModifyListener fieldsModified = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			updateStatus();	
		}
	};

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		new Label(container, SWT.NONE).setText(Messages.NewMeterIntervalPage_2);
		label = new Text(container, SWT.BORDER);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText(Messages.NewMeterIntervalPage_3);
		new Label(container, SWT.NONE).setText(Messages.NewMeterIntervalPage_4);
		backgroundColor = new ColorStyledText(container);
		backgroundColor.setColor(AlfaRGB.getFullyOpaque(new RGB(255, 255,255)),false);
		new Label(container, SWT.NONE).setText(Messages.NewMeterIntervalPage_5);
		lowExpression = new WTextExpression(container, SWT.NONE);
		lowExpression.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lowExpression.setExpression(new JRDesignExpression("\"CHANGE ME\"")); //$NON-NLS-1$
		new Label(container, SWT.NONE).setText(Messages.NewMeterIntervalPage_7);
		highExpression = new WTextExpression(container, SWT.NONE);
		highExpression.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		highExpression.setExpression(new JRDesignExpression("\"CHANGE ME\"")); //$NON-NLS-1$
		setControl(container);
		updateStatus();
		backgroundColor.addListener(fieldsModified);
		lowExpression.addModifyListener(expModified);
		highExpression.addModifyListener(expModified);
		label.addModifyListener(fieldsModified);
		UIUtils.resizeAndCenterShell(getShell(), 600, 400);
	}
	
	/**
	 * Return a not null name of the series 
	 * 
	 * @return a not null string, can be empty
	 */
	public String getLabel(){
		return labelValue;
	}
	
	/**
	 * Return an expression for the high value
	 * 
	 * @return a not null expression
	 */
	public JRDesignExpression getHighExpresion(){
		return highExpressionValue;
	}
	
	/**
	 * Return an expression for the low value
	 * 
	 * @return a not null expression
	 */
	public JRDesignExpression getLowExpression(){
		return lowExpressionValue;
	}
	
	/**
	 * Return the background color and its transparency
	 * 
	 * @return the background color of the series
	 */
	public AlfaRGB getBackgroundColor(){
		return backgroundValue;
	}

	/**
	 * Update the status of the page and save the fields. To have a valid status the two expression
	 * must be not empty
	 */
	protected void updateStatus() {
		labelValue = label.getText();
		lowExpressionValue = lowExpression.getExpression();
		highExpressionValue = highExpression.getExpression();
		backgroundValue = backgroundColor.getColor();
		if (highExpression.getExpression() == null || highExpression.getExpression().getText().isEmpty()) {
			setMessage(Messages.NewMeterIntervalPage_9, IMessageProvider.ERROR);
			setPageComplete(false);
		} else if (lowExpression.getExpression() == null || lowExpression.getExpression().getText().isEmpty()) {
			setMessage(Messages.NewMeterIntervalPage_10, IMessageProvider.ERROR);
			setPageComplete(false);
		} else {
			setMessage(Messages.NewMeterIntervalPage_11, IMessageProvider.INFORMATION);
			setPageComplete(true);
		}
	}
	
	@Override
	protected String getContextName() {
		return "com.jaspersoft.studio.doc.createMeterSeries"; //$NON-NLS-1$
	}

}
