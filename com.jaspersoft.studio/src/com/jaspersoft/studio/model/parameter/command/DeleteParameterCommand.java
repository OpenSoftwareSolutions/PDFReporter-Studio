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
package com.jaspersoft.studio.model.parameter.command;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import com.jaspersoft.studio.model.command.ADatasetObjectDeleteCommand;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.model.parameter.MParameters;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * /* link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteParameterCommand extends ADatasetObjectDeleteCommand{

	private JRDesignParameter jrParameter;

	/**
	 * Instantiates a new delete parameter command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteParameterCommand(MParameters<JRDesignDataset> destNode, MParameter srcNode) {
		this(srcNode.getJasperConfiguration(), destNode.getValue(), srcNode.getValue(), null);
	}

	/**
	 * @param jContext
	 * @param jrDataset
	 * @param jrParameter
	 */
	public DeleteParameterCommand(JasperReportsConfiguration jContext, JRDesignDataset jrDataset,
			JRParameter jrParameter, Boolean canceled) {
		super(canceled);
		this.jContext = jContext;
		jd = jContext.getJasperDesign();
		this.jrDataset = jrDataset;
		this.jrParameter = (JRDesignParameter) jrParameter;
		objectName = "$P{" + jrParameter.getName() + "}";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrDataset.getParametersList().indexOf(jrParameter);
		jrDataset.removeParameter(jrParameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrDataset == null || jrParameter == null)
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
		if (canceled == null || canceled)
			return;
		try {
			if (elementPosition < 0 || elementPosition > jrDataset.getParametersList().size())
				jrDataset.addParameter(jrParameter);
			else
				jrDataset.addParameter(elementPosition, jrParameter);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
