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
package com.jaspersoft.studio.property;

import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.gef.commands.Command;

/*
 * The Class SetValueCommand.
 */
public class SetExpressionValueCommand extends Command {

	private JRDesignExpression exp;
	private String text;
	private String className;

	private String oldText;
	private String oldClassname;

	public SetExpressionValueCommand(JRDesignExpression exp, String text, String className) {
		super("");
		this.exp = exp;
		this.text = text;
		this.className = className;

		oldText = exp.getText();
		oldClassname = exp.getValueClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		exp.setText(text);
		exp.setValueClassName(className);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		exp.setText(oldText);
		exp.setValueClassName(oldClassname);
	}

}
