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

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.property.descriptor.expression.dialog.JRExpressionEditor;

public class ExpressionWidget {
	private String label;
	private ExpressionContext exprContext;

	public ExpressionWidget(Composite parent, String label) {
		this.label = label;
		createControl(parent);
		if (label != null)
			expLabel.setText(label);
	}

	public void setEnabled(boolean enabled) {
		expButton.setEnabled(enabled);
		expText.setEnabled(enabled);
	}

	private void createControl(Composite parent) {
		if (label != null)
			expLabel = new Label(parent, SWT.NONE);

		expText = new Text(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 100;
		expText.setLayoutData(gd);
		expText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setExpressionText(expText.getText(), null);
			}
		});

		expButton = new Button(parent, SWT.PUSH | SWT.FLAT);
		expButton.setText("...");
		expButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if(!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
					JRExpressionEditor wizard = new JRExpressionEditor();
					wizard.setValue(expression);
					wizard.setExpressionContext(exprContext);
					WizardDialog dialog = ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(Display.getDefault().getActiveShell(), wizard);
					if (dialog.open() == Dialog.OK) {
						JRDesignExpression exprTmp = wizard.getValue();
						if(exprTmp!=null){
							setExpressionText(exprTmp.getText(), exprTmp.getValueClassName());
						}
						else{
							setExpression(exprTmp);
						}
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private JRDesignExpression expression;
	private Text expText;
	private Label expLabel;

	public void setExpression(JRDesignExpression exp) {
		this.expression = exp;
		setOnParent(exp);
		
		if (exp != null && exp.getText() != null) {
			
			if (!exp.getText().equals(expText.getText()))
			{
				expText.setText(exp.getText());
				expText.setToolTipText(expText.getText());
			}
		}
		else{
			expText.setText("");
			expText.setToolTipText("");
		}
	}

	protected void setOnParent(JRDesignExpression exp) {
		try {
			if (obj != null)
				obj.getClass().getMethod("set" + property, new Class[] { JRExpression.class })
						.invoke(obj, new Object[] { exp });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private boolean isModMode = false;

	private void setExpressionText(String exptxt, String valueClass) {
		if (!isModMode) {
			isModMode = true;
			try {
				if (exptxt != null && !exptxt.isEmpty()) {
					if (expression == null)
						expression = new JRDesignExpression();
					expression.setText(exptxt);
				} else {
					expression = null;
				}
				if (valueClass != null && expression != null)
					expression.setValueClassName(valueClass);
				setExpression(expression);
			} finally {
				isModMode = false;
			}
		}
	}

	private Object obj;
	private String property;
	private Button expButton;

	public void bindObject(Object obj, String property) {
		this.obj = obj;
		this.property = property;
		try {
			JRDesignExpression expr = null;
			if (obj != null)
				expr = (JRDesignExpression) obj.getClass().getMethod("get" + property, new Class[0]).invoke(obj, new Object[0]);
			setExpression(expr);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public void setExpressionContext(ExpressionContext exprContext) {
		this.exprContext=exprContext;
	}

	public boolean isEnabled() {
		return expButton.isEnabled() && expText.isEnabled();
	}
}
