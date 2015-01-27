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

import java.util.List;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignSection;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.band.MBandGroup;


/**
 * Move a band of a specific number of position. The number can be negative
 * to move it up or positive to move it down. It does nothing if the number of position
 * is not in the rage of the bands
 * 
 * @author Orlandin Marco
 *
 */
public class ReorderBandCommandByRelativeIndex extends Command {

	/**
	 * Flag to check if the band was moved during the execute
	 */
	private boolean bandMoved = false;
	
	/**
	 * The number of position of the movments
	 */
	private int delta;

	/** The jr band. */
	private JRDesignBand jrBand;

	/** The jr design section. */
	private JRDesignSection jrDesignSection;

	/**
	 * Instantiates a new reorder band command.
	 * 
	 * @param child
	 *          the child
	 * @param newIndex
	 *          the new index
	 */
	public ReorderBandCommandByRelativeIndex(MBandGroup child, int delta) {
		super(Messages.common_reorder_elements);

		this.delta = delta;
		this.jrDesignSection = (JRDesignSection) child.getSection();
		this.jrBand = (JRDesignBand) child.getValue();
	}

	/**
	 * Instantiates a new reorder band command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderBandCommandByRelativeIndex(MBand child, MReport parent, int delta) {
		super(Messages.common_reorder_elements);

		this.delta = delta;
		this.jrDesignSection = (JRDesignSection) parent.getJasperDesign().getDetailSection();
		this.jrBand = (JRDesignBand) child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		List<JRBand> bList = jrDesignSection.getBandsList();
		int currentPosition = bList.indexOf(jrBand);
		int newPosition = currentPosition + delta;
		
		if (newPosition >= 0 && newPosition < bList.size() && newPosition != currentPosition){
			bList.remove(jrBand);
			bandMoved = true;
			bList.add(newPosition, jrBand);
			//This event will not change the listener on the model, but only changes its position
			jrDesignSection.getEventSupport().fireIndexedPropertyChange(MReport.CHANGE_BAND_POSITION, newPosition, currentPosition, -1);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (!bandMoved) return;
		
		List<JRBand> bList = jrDesignSection.getBandsList();
		int currentPosition = bList.indexOf(jrBand);
		int newPosition = currentPosition - delta;
		
		if (newPosition >= 0 && newPosition < bList.size() && newPosition != currentPosition){
			bList.remove(jrBand);
			bList.add(newPosition, jrBand);
			//This event will not change the listener on the model, but only changes its position
			jrDesignSection.getEventSupport().fireIndexedPropertyChange(MReport.CHANGE_BAND_POSITION, newPosition, currentPosition, -1);
		}
	}

}
