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
package com.jaspersoft.studio.components.crosstab.model.measure.command;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.model.measure.MMeasure;
import com.jaspersoft.studio.components.crosstab.model.measure.MMeasures;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteMeasureCommand extends Command {

	/** The jr dataset. */
	private JRDesignCrosstab jrCrosstab;

	/** The jr parameter. */
	private JRDesignCrosstabMeasure jrMeasure;

	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete parameter command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteMeasureCommand(MMeasures destNode, MMeasure srcNode) {
		super();
		this.jrCrosstab = (JRDesignCrosstab) destNode.getValue();
		this.jrMeasure = (JRDesignCrosstabMeasure) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrCrosstab.getMesuresList().indexOf(jrMeasure);
		jrCrosstab.removeMeasure(jrMeasure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrCrosstab == null || jrMeasure == null)
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
		try {
			if (elementPosition >= 0 && elementPosition < jrCrosstab.getMesuresList().size())
				jrCrosstab.addMeasure(elementPosition, jrMeasure);
			else
				jrCrosstab.addMeasure(jrMeasure);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
