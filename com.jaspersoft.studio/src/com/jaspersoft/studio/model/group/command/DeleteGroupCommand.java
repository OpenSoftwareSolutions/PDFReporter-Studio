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
package com.jaspersoft.studio.model.group.command;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.MBandGroupHeader;
import com.jaspersoft.studio.model.group.MGroup;
import com.jaspersoft.studio.model.group.MGroups;
/*/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteGroupCommand extends Command {
	
	/** The jr dataset. */
	private JRDesignDataset jrDataset;
	
	/** The jr group. */
	private JRDesignGroup jrGroup;
	
	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete group command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteGroupCommand(MReport destNode, MBandGroupFooter srcNode) {
		super();
		this.jrDataset = destNode.getJasperDesign().getMainDesignDataset();
		this.jrGroup = srcNode.getJrGroup();
	}

	/**
	 * Instantiates a new delete group command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteGroupCommand(MReport destNode, MBandGroupHeader srcNode) {
		super();
		this.jrDataset = destNode.getJasperDesign().getMainDesignDataset();
		this.jrGroup = srcNode.getJrGroup();
	}

	/**
	 * Instantiates a new delete group command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteGroupCommand(MGroups destNode, MGroup srcNode) {
		super();
		this.jrDataset = (JRDesignDataset) destNode.getValue();
		this.jrGroup = (JRDesignGroup) srcNode.getValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrDataset.getGroupsList().indexOf(jrGroup);
		jrDataset.removeGroup(jrGroup);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrDataset == null || jrGroup == null)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		try {
			if (elementPosition < 0 || elementPosition > jrDataset.getGroupsList().size())
				jrDataset.addGroup(jrGroup);
			else
				jrDataset.addGroup(elementPosition, jrGroup);
		} catch (JRException e) {
			e.printStackTrace();
		}

	}
}
