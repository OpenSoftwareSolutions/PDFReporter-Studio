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

import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;

public class DatasetRunWidgetRadio implements IExpressionContextSetter {

	protected JRDesignDatasetRun datasetrun;

	protected boolean ignoreUpdates = false;

	public DatasetRunWidgetRadio(Composite parent) {
		createControl(parent);
	}

	public void setData(JRDesignDatasetRun datasetrun) {
		this.datasetrun = datasetrun;

		removeListeners();

		isReportConnection = false;
		isEmptyDatasource = false;

		isUseParentConnection = false;
		isUseConnectionExpression = false;
		isUseDatasourceExpression = false;
		isUseEmptyDatasource = false;
		isNoConnection = false;

		// connectionExpressionBox.setExpression(null);
		// datasourceExpressionBox.setExpression(null);

		if (datasetrun != null) {
			if (datasetrun.getConnectionExpression() != null) {
				// connectionExpressionBox.setExpression((JRDesignExpression) datasetrun.getConnectionExpression());
				isReportConnection = datasetrun.getConnectionExpression().getText().equals("$P{REPORT_CONNECTION}");

				isUseParentConnection = isReportConnection;
				isUseConnectionExpression = !isReportConnection;
			} else if (datasetrun.getDataSourceExpression() != null) {
				// datasourceExpressionBox.setExpression((JRDesignExpression) datasetrun.getDataSourceExpression());
				isEmptyDatasource = datasetrun.getDataSourceExpression().getText()
						.equals("new net.sf.jasperreports.engine.JREmptyDataSource()");

				isUseEmptyDatasource = isEmptyDatasource;
				isUseDatasourceExpression = !isEmptyDatasource;
			} else {
				isNoConnection = true;
			}
		} else {
			// connectionExpressionBox.setExpression(null);
			// datasourceExpressionBox.setExpression(null);
		}
		setEnabledWidgets();
		addListeners();
	}

	private boolean isReportConnection = false;
	private boolean isEmptyDatasource = false;

	private boolean isUseParentConnection = false;
	private boolean isUseConnectionExpression = false;
	private boolean isUseDatasourceExpression = false;
	private boolean isUseEmptyDatasource = false;
	private boolean isNoConnection = false;

	private void setEnabledWidgets() {
		setSelection4Widget(radioUseParentConnection, isUseParentConnection);
		setSelection4Widget(radioUseConnectionExpression, isUseConnectionExpression);
		setSelection4Widget(radioUseDatasourceExpression, isUseDatasourceExpression);
		setSelection4Widget(radioUseEmptyDatasource, isUseEmptyDatasource);
		setSelection4Widget(radioNoConnection, isNoConnection);

		setEnabled4Widget(connectionExpressionBox, isUseConnectionExpression);
		setEnabled4Widget(datasourceExpressionBox, isUseDatasourceExpression);

		if (isUseConnectionExpression || isUseParentConnection)
			connectionExpressionBox.setExpression((JRDesignExpression) datasetrun.getConnectionExpression());
		else
			connectionExpressionBox.setExpression(null);

		if (isUseDatasourceExpression || isUseEmptyDatasource)
			datasourceExpressionBox.setExpression((JRDesignExpression) datasetrun.getDataSourceExpression());
		else
			datasourceExpressionBox.setExpression(null);
	}

	private static void setEnabled4Widget(WTextExpression b, boolean value) {
		if (b.getEnabled() != value)
			b.setEnabled(value);
	}

	private static void setSelection4Widget(Button b, boolean value) {
		if (b.getSelection() != value)
			b.setSelection(value);
	}

	public Control getControl() {
		return composite;
	}

	public void setEnabled(boolean en) {
		composite.setEnabled(en);
	}

	private Composite composite;
	private WTextExpression datasourceExpressionBox;
	private WTextExpression connectionExpressionBox;
	private Button radioUseParentConnection;
	private Button radioUseConnectionExpression;
	private Button radioUseDatasourceExpression;
	private Button radioUseEmptyDatasource;
	private Button radioNoConnection;
	private Listener listener;

	public void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());

		radioUseParentConnection = new Button(composite, SWT.RADIO);
		FormData fd_sameCon = new FormData();
		fd_sameCon.left = new FormAttachment(0, 10);
		fd_sameCon.top = new FormAttachment(0, 10);
		radioUseParentConnection.setLayoutData(fd_sameCon);
		radioUseParentConnection.setText(Messages.WizardConnectionPage_mainreport_text);

		radioUseConnectionExpression = new Button(composite, SWT.RADIO);
		FormData fd_otherCon = new FormData();
		fd_otherCon.left = new FormAttachment(0, 10);
		fd_otherCon.top = new FormAttachment(radioUseParentConnection, 6);
		radioUseConnectionExpression.setLayoutData(fd_otherCon);
		radioUseConnectionExpression.setText(Messages.WizardConnectionPage_connection_text);

		connectionExpressionBox = new WTextExpression(composite, SWT.NONE);
		FormData fd_otherExpr = new FormData();
		fd_otherExpr.bottom = new FormAttachment(radioUseConnectionExpression, 69, SWT.BOTTOM);
		fd_otherExpr.left = new FormAttachment(0, 10);
		fd_otherExpr.right = new FormAttachment(100, -5);
		fd_otherExpr.top = new FormAttachment(radioUseConnectionExpression, 6);
		connectionExpressionBox.setLayoutData(fd_otherExpr);

		radioUseEmptyDatasource = new Button(composite, SWT.RADIO);
		FormData fd_emptyCon = new FormData();
		fd_emptyCon.left = new FormAttachment(0, 10);
		fd_emptyCon.top = new FormAttachment(connectionExpressionBox, 6);
		radioUseEmptyDatasource.setLayoutData(fd_emptyCon);
		radioUseEmptyDatasource.setText(Messages.WizardConnectionPage_empty_connection_text);

		radioUseDatasourceExpression = new Button(composite, SWT.RADIO);
		FormData fd_jrdCon = new FormData();
		fd_jrdCon.left = new FormAttachment(0, 10);
		fd_jrdCon.top = new FormAttachment(radioUseEmptyDatasource, 6);
		radioUseDatasourceExpression.setLayoutData(fd_jrdCon);
		radioUseDatasourceExpression.setText(Messages.WizardConnectionPage_datasource_text);

		datasourceExpressionBox = new WTextExpression(composite, SWT.NONE);
		FormData fd_dsRunExpr = new FormData();
		fd_dsRunExpr.top = new FormAttachment(radioUseDatasourceExpression, 6);
		fd_dsRunExpr.right = new FormAttachment(connectionExpressionBox, 0, SWT.RIGHT);
		fd_dsRunExpr.left = new FormAttachment(0, 10);
		datasourceExpressionBox.setLayoutData(fd_dsRunExpr);

		radioNoConnection = new Button(composite, SWT.RADIO);
		FormData fd_noCon = new FormData();
		fd_noCon.left = new FormAttachment(radioUseParentConnection, 0, SWT.LEFT);
		fd_noCon.bottom = new FormAttachment(100, 0);
		radioNoConnection.setLayoutData(fd_noCon);
		radioNoConnection.setText(Messages.WizardConnectionPage_noconnection_text);

		fd_dsRunExpr.bottom = new FormAttachment(radioNoConnection, -6, SWT.TOP);
		fd_dsRunExpr.height = 69;

		listener = new Listener() {

			public void handleEvent(Event event) {
				// if (time > 0 && event.time - time < 20)
				// return;
				// time = event.time;

				if (radioNoConnection.getSelection())
					setNoConnection();
				else if (radioUseConnectionExpression.getSelection())
					setConnection("$P{REPORT_CONNECTION} "); //$NON-NLS-1$
				else if (radioUseParentConnection.getSelection())
					setConnection("$P{REPORT_CONNECTION}"); //$NON-NLS-1$
				else if (radioUseDatasourceExpression.getSelection())
					setDatasource("new net.sf.jasperreports.engine.JREmptyDataSource() ");//$NON-NLS-1$ 
				else if (radioUseEmptyDatasource.getSelection())
					setDatasource("new net.sf.jasperreports.engine.JREmptyDataSource()");//$NON-NLS-1$ 

				connectionExpressionBox.setEnabled(radioUseConnectionExpression.getSelection());
				datasourceExpressionBox.setEnabled(radioUseDatasourceExpression.getSelection());
			}
		};

		addListeners();

		listener.handleEvent(new Event());
	}

	private ExpressionModifiedListener mlistener = new ExpressionModifiedListener() {
		@Override
		public void expressionModified(ExpressionModifiedEvent event) {
			if (connectionExpressionBox.isEnabled()) {
				JRDesignExpression exp = connectionExpressionBox.getExpression();
				if (exp != null)
					setConnection(exp.getText());
				else
					setConnection(null);
			} else {
				JRDesignExpression exp = datasourceExpressionBox.getExpression();
				if (exp != null)
					setDatasource(exp.getText());
				else
					setDatasource(null);
			}
		}
	};

	protected void removeListeners() {
		radioUseParentConnection.removeListener(SWT.Selection, listener);
		radioUseConnectionExpression.removeListener(SWT.Selection, listener);
		connectionExpressionBox.removeModifyListener(mlistener);
		radioUseDatasourceExpression.removeListener(SWT.Selection, listener);
		datasourceExpressionBox.removeModifyListener(mlistener);
		radioUseEmptyDatasource.removeListener(SWT.Selection, listener);
		radioNoConnection.removeListener(SWT.Selection, listener);
	}

	protected void addListeners() {
		radioUseParentConnection.addListener(SWT.Selection, listener);
		radioUseConnectionExpression.addListener(SWT.Selection, listener);
		connectionExpressionBox.addModifyListener(mlistener);
		radioUseDatasourceExpression.addListener(SWT.Selection, listener);
		datasourceExpressionBox.addModifyListener(mlistener);
		radioUseEmptyDatasource.addListener(SWT.Selection, listener);
		radioNoConnection.addListener(SWT.Selection, listener);
	}

	protected void setNoConnection() {
		if (datasetrun != null) {

			datasetrun.setConnectionExpression(null);
			datasetrun.setDataSourceExpression(null);

			removeListeners();
			connectionExpressionBox.setExpression(null);
			datasourceExpressionBox.setExpression(null);
			addListeners();
		}
	}

	protected void setDatasource(String exTxt) {
		if (datasetrun != null) {
			JRDesignExpression jde = (JRDesignExpression) datasetrun.getDataSourceExpression();
			if (jde == null)
				jde = new JRDesignExpression();
			jde.setText(exTxt);
			datasetrun.setConnectionExpression(null);
			datasetrun.setDataSourceExpression(jde);

			removeListeners();
			connectionExpressionBox.setExpression(null);
			datasourceExpressionBox.setExpression(jde);
			addListeners();
			// setData(datasetrun);
		}
	}

	protected void setConnection(String exTxt) {
		if (datasetrun != null) {
			JRDesignExpression jde = (JRDesignExpression) datasetrun.getConnectionExpression();
			if (jde == null)
				jde = new JRDesignExpression();
			jde.setText(exTxt);
			datasetrun.setConnectionExpression(jde);
			datasetrun.setDataSourceExpression(null);

			removeListeners();
			datasourceExpressionBox.setExpression(null);
			connectionExpressionBox.setExpression(jde);
			addListeners();
			// setData(datasetrun);
		}
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.datasourceExpressionBox.setExpressionContext(expContext);
		this.connectionExpressionBox.setExpressionContext(expContext);
	}
}
