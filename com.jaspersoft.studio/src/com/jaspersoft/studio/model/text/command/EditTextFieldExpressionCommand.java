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
package com.jaspersoft.studio.model.text.command;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRCloneUtils;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.property.descriptor.expression.dialog.JRExpressionEditor;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Command that allows to edit the expression associated to a {@link JRDesignTextField} element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class EditTextFieldExpressionCommand extends Command {

	private JasperDesign jasperDesign;
	private MTextField textField;
	private JRDesignExpression originalExpression;
	private JRDesignExpression newExpression;
	private boolean isExpressionChanged;
	
	public EditTextFieldExpressionCommand(MTextField textField) {
		this.textField = textField;
		this.jasperDesign = this.textField.getJasperDesign();
		this.originalExpression = (JRDesignExpression) this.textField.getPropertyValue(JRDesignTextField.PROPERTY_EXPRESSION);
		this.isExpressionChanged = false;
	}
	
	@Override
	public boolean canExecute() {
		return (textField!=null && jasperDesign!=null);
	}
	
	@Override
	public void execute() {
		textField.setPropertyValue(JRDesignTextField.PROPERTY_EXPRESSION, newExpression);
	}
	
	public int showDialog() {
		if(!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
			JRExpressionEditor wizard = new JRExpressionEditor();
			wizard.setValue(JRCloneUtils.nullSafeClone(originalExpression));
			ExpressionContext ec = ModelUtils.getElementExpressionContext((JRDesignTextField) textField.getValue(), textField);
			wizard.setExpressionContext(ec);

			WizardDialog dialog = ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(UIUtils.getShell(), wizard);
			if (dialog.open() == Dialog.OK) {
				isExpressionChanged = true;
				newExpression=wizard.getValue();
				return Window.OK;
			}
		}
		isExpressionChanged=false;
		return Window.CANCEL;
	}
	
	@Override
	public boolean canUndo() {
		// we can not simply rely on the original and new expression
		// values, because null is a good one
		return isExpressionChanged;
	}
	
	@Override
	public void undo() {
		textField.setPropertyValue(JRDesignTextField.PROPERTY_EXPRESSION, originalExpression);
	}
	
	@Override
	public String getLabel() {
		return "Change Text Field Expression";
	}
}
