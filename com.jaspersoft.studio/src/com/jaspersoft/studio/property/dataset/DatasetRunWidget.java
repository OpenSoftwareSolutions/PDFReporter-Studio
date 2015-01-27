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
package com.jaspersoft.studio.property.dataset;

import java.sql.Connection;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;

public class DatasetRunWidget implements IExpressionContextSetter{
	public static final String[] ITEMS = new String[] { Messages.WizardConnectionPage_noconnection_text,
			Messages.WizardConnectionPage_connection_text, Messages.WizardConnectionPage_datasource_text,
			Messages.WizardConnectionPage_mainreport_text, Messages.WizardConnectionPage_empty_connection_text };
	private JRDesignDatasetRun datasetrun;

	public DatasetRunWidget(Composite parent) {
		createControl(parent);
	}

	public void setData(JRDesignDatasetRun datasetrun) {
		this.datasetrun = datasetrun;
		if (datasetrun != null) {
			if (datasetrun.getConnectionExpression() == null && datasetrun.getDataSourceExpression() == null) {
				cnExpr.setEnabled(false);
				cmb.select(0);
			} else if (datasetrun.getConnectionExpression() != null) {
				cnExpr.setEnabled(true);
				cnExpr.bindObject(datasetrun, "ConnectionExpression");
				cmb.select(1);
			} else if (datasetrun.getDataSourceExpression() != null) {
				cnExpr.setEnabled(true);
				cnExpr.bindObject(datasetrun, "DataSourceExpression");
				cmb.select(2);
			}
		}
	}

	public void setEnabled(boolean enabled) {
		if (enabled)
			layout.topControl = dsRunComposite;
		else
			layout.topControl = emptyComposite;
		control.layout();
	}

	public Control getControl() {
		return control;
	}

	private Composite control;
	private StackLayout layout;
	private Composite emptyComposite;
	private Composite dsRunComposite;

	private ExpressionWidget cnExpr;
	private Combo cmb;
	private ExpressionContext expContext;

	public void createControl(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		control = cmp;
		layout = new StackLayout();
		cmp.setLayout(layout);

		emptyComposite = new Composite(cmp, SWT.NONE);

		dsRunComposite = new Composite(cmp, SWT.NONE);
		dsRunComposite.setLayout(new GridLayout(3, false));

		cmb = new Combo(dsRunComposite, SWT.READ_ONLY | SWT.BORDER | SWT.SINGLE);
		cmb.setItems(ITEMS);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		cmb.setLayoutData(gd);

		cnExpr = new ExpressionWidget(dsRunComposite, "");

		layout.topControl = dsRunComposite;
		cmp.layout();

		cmb.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int sel = cmb.getSelectionIndex();
				switch (sel) {
				case 0:
					setNoConnection();
					break;
				case 1:
					setConnection(""); //$NON-NLS-1$
					break;
				case 2:
					setDatasource("");//$NON-NLS-1$
					break;
				case 3:
					setConnection("$P{REPORT_CONNECTION}"); //$NON-NLS-1$
					break;
				case 4:
					setDatasource("new net.sf.jasperreports.engine.JREmptyDataSource()");//$NON-NLS-1$
					break;

				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private void setNoConnection() {
		if (datasetrun != null) {
			datasetrun.setConnectionExpression(null);
			datasetrun.setDataSourceExpression(null);
			setData(datasetrun);
		}
	}

	private void setDatasource(String exTxt) {
		if (datasetrun != null) {
			JRDesignExpression jde = (JRDesignExpression) datasetrun.getDataSourceExpression();
			if (jde == null)
				jde = new JRDesignExpression();
			jde.setValueClass(JRDataSource.class);
			jde.setText(exTxt);
			datasetrun.setConnectionExpression(null);
			datasetrun.setDataSourceExpression(jde);
			setData(datasetrun);
		}
	}

	private void setConnection(String exTxt) {
		if (datasetrun != null) {
			JRDesignExpression jde = (JRDesignExpression) datasetrun.getConnectionExpression();
			if (jde == null)
				jde = new JRDesignExpression();
			jde.setValueClass(Connection.class);
			jde.setText(exTxt);
			datasetrun.setConnectionExpression(jde);
			datasetrun.setDataSourceExpression(null);
			setData(datasetrun);
		}
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext=expContext;
		if(cnExpr!=null){
			cnExpr.setExpressionContext(this.expContext);
		}
	}
	
}
