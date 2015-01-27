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
package com.jaspersoft.studio.model.style.command;

import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MStyleTemplate;
import com.jaspersoft.studio.model.style.MStyles;
/*/*
 * The Class ReorderStyleTemplateCommand.
 */
public class ReorderStyleTemplateCommand extends Command {

	/** The new index. */
	private int oldIndex, newIndex;

	/** The jr template. */
	private JRDesignReportTemplate jrTemplate;

	/** The jr design. */
	private JasperDesign jrDesign;

	/**
	 * Instantiates a new reorder style template command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderStyleTemplateCommand(MStyleTemplate child, MStyles parent, int newIndex) {
		super(Messages.common_reorder_elements);
		this.newIndex = Math.max(0, newIndex);
		this.jrDesign = parent.getJasperDesign();
		this.jrTemplate = (JRDesignReportTemplate) child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = jrDesign.getTemplatesList().indexOf(jrTemplate);

		jrDesign.removeTemplate(jrTemplate);
		if (newIndex < 0 || newIndex > jrDesign.getTemplatesList().size())
			jrDesign.addTemplate(jrTemplate);
		else
			jrDesign.addTemplate(newIndex, jrTemplate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrDesign.removeTemplate(jrTemplate);
		if (oldIndex < 0 || oldIndex > jrDesign.getTemplatesList().size())
			jrDesign.addTemplate(jrTemplate);
		else
			jrDesign.addTemplate(oldIndex, jrTemplate);
	}

}
