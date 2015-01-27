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

import com.jaspersoft.studio.model.style.MStyleTemplate;
import com.jaspersoft.studio.model.style.MStyles;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteStyleTemplateCommand extends Command {

	/** The jr design. */
	private JasperDesign jrDesign;

	/** The jr template. */
	private JRDesignReportTemplate jrTemplate;

	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete style template command.
	 */
	public DeleteStyleTemplateCommand(MStyles destNode, MStyleTemplate srcNode) {
		super();
		this.jrDesign = srcNode.getJasperDesign();
		this.jrTemplate = (JRDesignReportTemplate) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrDesign.getTemplatesList().indexOf(jrTemplate);
		jrDesign.removeTemplate(jrTemplate);
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
		if (elementPosition < 0 || elementPosition > jrDesign.getTemplatesList().size())
			jrDesign.addTemplate(jrTemplate);
		else
			jrDesign.addTemplate(elementPosition, jrTemplate);
	}
}
