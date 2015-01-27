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
package com.jaspersoft.studio.model.band.command;

import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignSection;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.band.MBand;
/*/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateBandDetailCommand extends Command {
	
	/** The jr band. */
	private JRDesignBand jrBand;
	
	/** The jr design section. */
	private JRDesignSection jrDesignSection;
	
	/** The index. */
	private int index = -1;

	/**
	 * Instantiates a new creates the band detail command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public CreateBandDetailCommand(MBand destNode, MBand srcNode) {
		super();
		this.jrDesignSection = (JRDesignSection) destNode.getJasperDesign().getDetailSection();
		if (srcNode != null && srcNode.getValue() != null)
			this.jrBand = (JRDesignBand) srcNode.getValue();
	}
	
	
	public CreateBandDetailCommand(MBand destNode, MBand srcNode, int index) {
		this(destNode,srcNode);
		this.index = index;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrBand == null) {
			this.jrBand = MBand.createJRBand();
		}
		if (jrBand != null) {
			if (index < 0 || index > jrDesignSection.getBandsList().size())
				jrDesignSection.addBand(jrBand);
			else
				jrDesignSection.addBand(index, jrBand);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		index = jrDesignSection.getBandsList().indexOf(jrBand);
		jrDesignSection.removeBand(jrBand);
	}
}
