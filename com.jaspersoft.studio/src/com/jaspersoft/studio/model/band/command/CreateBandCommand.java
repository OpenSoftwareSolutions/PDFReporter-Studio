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
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.band.MBand;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateBandCommand extends Command {

	/** The band type. */
	private BandTypeEnum bandType;

	/** The jr element. */
	private JRDesignBand jrElement;

	/** The jr design. */
	private JasperDesign jrDesign;

	/**
	 * Instantiates a new creates the band command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public CreateBandCommand(MBand destNode, MBand srcNode) {
		super();
		this.bandType = destNode.getBandType();
		this.jrDesign = destNode.getJasperDesign();
	}

	/**
	 * Creates the object.
	 */
	private void createObject() {
		if (jrElement == null) {
			jrElement = MBand.createJRBand();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrElement != null) {
			switch (bandType) {
			case TITLE:
				jrDesign.setTitle(jrElement);
				break;
			case PAGE_HEADER:
				jrDesign.setPageHeader(jrElement);
				break;
			case COLUMN_HEADER:
				jrDesign.setColumnHeader(jrElement);
				break;
			case COLUMN_FOOTER:
				jrDesign.setColumnFooter(jrElement);
				break;
			case PAGE_FOOTER:
				jrDesign.setPageFooter(jrElement);
				break;
			case LAST_PAGE_FOOTER:
				jrDesign.setLastPageFooter(jrElement);
				break;
			case SUMMARY:
				jrDesign.setSummary(jrElement);
				break;
			case BACKGROUND:
				jrDesign.setBackground(jrElement);
				break;
			case NO_DATA:
				jrDesign.setNoData(jrElement);
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		switch (bandType) {
		case TITLE:
			jrDesign.setTitle(null);
			break;
		case PAGE_HEADER:
			jrDesign.setPageHeader(null);
			break;
		case COLUMN_HEADER:
			jrDesign.setColumnHeader(null);
			break;
		case COLUMN_FOOTER:
			jrDesign.setColumnFooter(null);
			break;
		case PAGE_FOOTER:
			jrDesign.setPageFooter(null);
			break;
		case LAST_PAGE_FOOTER:
			jrDesign.setLastPageFooter(null);
			break;
		case SUMMARY:
			jrDesign.setSummary(null);
			break;
		case BACKGROUND:
			jrDesign.setBackground(null);
			break;
		case NO_DATA:
			jrDesign.setNoData(null);
			break;
		}
	}

}
