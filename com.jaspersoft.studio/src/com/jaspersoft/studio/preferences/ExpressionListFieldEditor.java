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
package com.jaspersoft.studio.preferences;

import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.property.descriptor.expression.dialog.JRExpressionEditor;
import com.jaspersoft.studio.utils.Misc;

/**
 * Field editor for a list of user defined expressions.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ExpressionListFieldEditor extends ListEditor{
	
	public static final String EXPRESSION_SEP="\n";

	public ExpressionListFieldEditor(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String createList(String[] items) {
        StringBuffer expressionsBuff = new StringBuffer("");//$NON-NLS-1$

        for (int i = 0; i < items.length; i++) {
        	expressionsBuff.append(items[i]);
        	expressionsBuff.append(EXPRESSION_SEP);
        }
        return expressionsBuff.toString();
	}

	@Override
	protected String getNewInputObject() {
		if(!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
			JRExpressionEditor wizard = new JRExpressionEditor();
			WizardDialog dialog = 
					ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(getShell(),wizard);
			if (dialog.open() == Dialog.OK) {
				JRDesignExpression newExp = wizard.getValue();
				if(newExp!=null &&
						!Misc.nvl(newExp.getText()).equals("")){
					return newExp.getText();
				}
			}
		}
		return null;
	}

	@Override
	protected String[] parseString(String stringList) {
        StringTokenizer st = new StringTokenizer(stringList, EXPRESSION_SEP);//$NON-NLS-1$
        ArrayList<String> v = new ArrayList<String>();
        while (st.hasMoreElements()) {
            v.add((String)st.nextElement());
        }
        return v.toArray(new String[v.size()]);
	}
	
}
