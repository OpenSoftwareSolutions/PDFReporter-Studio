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
package com.jaspersoft.studio.editor.style.command;

import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.style.MStyleTemplateReference;
import com.jaspersoft.studio.model.style.MStylesTemplate;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteStyleTemplateCommand extends Command {

	private JRSimpleTemplate jrDesign;

	private JRTemplateReference jrTemplate;

	private int index = 0;

	/**
	 * Instantiates a new delete style template command.
	 */
	public DeleteStyleTemplateCommand(MStylesTemplate destNode, MStyleTemplateReference srcNode) {
		super();
		this.jrDesign = (JRSimpleTemplate) destNode.getValue();
		this.jrTemplate = (JRTemplateReference) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		index = jrDesign.getIncludedTemplatesList().indexOf(jrTemplate);
		jrDesign.removeIncludedTemplate(jrTemplate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrDesign == null || jrTemplate == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (index < 0 || index > jrDesign.getIncludedTemplatesList().size())
			jrDesign.addIncludedTemplate(jrTemplate);
		else
			jrDesign.addIncludedTemplate(index, jrTemplate);
	}
}
