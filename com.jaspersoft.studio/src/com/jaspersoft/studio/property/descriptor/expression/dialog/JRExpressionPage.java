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
package com.jaspersoft.studio.property.descriptor.expression.dialog;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorComposite;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupport;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.editor.expression.ExpressionStatus;
import com.jaspersoft.studio.editor.expression.IExpressionStatusChangeListener;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.ClassType;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.RecentExpressions;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

public class JRExpressionPage extends JSSHelpWizardPage {
	private static final int SHELL_INITIAL_HEIGHT = 680;
	private static final int SHELL_INITIAL_WIDTH = 750;
	private JRDesignExpression value;
	private StyledText queryText;
	private ClassType valueType;
	private ExpressionEditorComposite contributedComposite;
	private ExpressionContext exprContext;

	public JRDesignExpression getValue() {
		if(contributedComposite!=null){
			JRDesignExpression expression = (JRDesignExpression) contributedComposite.getExpression();
			if(expression!=null && !Misc.nvl(expression.getText()).equals("")){ //$NON-NLS-1$
				RecentExpressions.addNewExpression(expression.getText());
			}
			return expression;
		}
		else{
			return value;			
		}
	}

	public void setValue(JRDesignExpression value) {
		if (value != null)
			this.value = (JRDesignExpression) value.clone();
		else
			this.value = new JRDesignExpression();
	}
	
	public void setExpressionContext(ExpressionContext exprContext){
		this.exprContext=exprContext;
		// Update the current expression context reference
		ExpressionEditorSupportUtil.setCurrentExpressionContext(this.exprContext);
	}

	protected JRExpressionPage(String pageName) {
		super(pageName);
		setTitle(Messages.common_expression_editor);
		setDescription(Messages.JRExpressionPage_description);
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_EXPRESSION_EDITOR;
	}

	public void createControl(Composite parent) {
		// Seeks for a possible contributed composite
		ExpressionEditorSupport editorSupportForReportLanguage = ExpressionEditorSupportUtil.getEditorSupport(exprContext);
		if(editorSupportForReportLanguage!=null){
			contributedComposite = editorSupportForReportLanguage.createExpressionEditorComposite(parent);
			contributedComposite.setExpressionContext(getExpressionContext());
			contributedComposite.addExpressionStatusChangeListener(new IExpressionStatusChangeListener() {
				public void statusChanged(ExpressionStatus status) {
					if(status.equals(ExpressionStatus.ERROR)){
						setErrorMessage(status.getShortDescription());
					}
					else{
						setErrorMessage(null);
					}
				}
			});
			contributedComposite.setExpression(this.value);
			setControl(contributedComposite);
			// Resize and center shell
			UIUtils.resizeAndCenterShell(getShell(), SHELL_INITIAL_WIDTH, SHELL_INITIAL_HEIGHT);
		}
		// Otherwise fallback to a generic composite
		else{
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			setControl(composite);
	
			Label lbl1 = new Label(composite, SWT.NONE);
			lbl1.setText("Value Class Name"); //$NON-NLS-1$
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			lbl1.setLayoutData(gd);
	
			valueType = new ClassType(composite, Messages.JRExpressionPage_1);
			valueType.addListener(new ModifyListener() {
	
				public void modifyText(ModifyEvent e) {
					value.setValueClassName(valueType.getClassType());
				}
			});
	
			Label lbl2 = new Label(composite, SWT.NONE);
			lbl2.setText(Messages.common_expression + ":"); //$NON-NLS-1$
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			lbl2.setLayoutData(gd);
	
			queryText = new StyledText(composite, SWT.BORDER);
			gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			queryText.setLayoutData(gd);
	
			setWidgets();
			queryText.setFocus();
			queryText.addModifyListener(new ModifyListener() {
	
				public void modifyText(ModifyEvent e) {
					value.setText(queryText.getText());
				}
			});
		}
	}

	private ExpressionContext getExpressionContext() {
		if(exprContext==null){
			// Try to get the global expression context that use the main dataset
			exprContext=ExpressionEditorSupportUtil.getReportExpressionContext();
		}
		return exprContext;
	}
	
	private void setWidgets() {
		queryText.setText(Misc.nvl(value.getText(), "")); //$NON-NLS-1$
		valueType.setClassType(value.getValueClassName());
	}

	
	
}
