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
package com.jaspersoft.studio.components.crosstab.action;

import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.JRStyle;

import org.eclipse.gef.commands.Command;

/**
 * The command to remove the style of a crosstab, support the undo
 * 
 * @author Orlandin Marco
 *
 */
public class RemoveStyleCommand extends Command{
	
	private JRDesignCellContents cell;
	
	private JRStyle oldStyle;
	
	private String oldStyleReference;
	
	public RemoveStyleCommand(JRDesignCellContents cell){
		this.cell = cell;
		oldStyle = null;
		oldStyleReference = null;
	}
	

	@Override
	public void execute() {
		oldStyle = cell.getStyle();
		oldStyleReference = cell.getStyleNameReference();
		cell.setStyle(null);
		cell.setStyleNameReference(null);
	}
	
	@Override
	public void undo() {
		cell.setStyle(oldStyle);
		cell.setStyleNameReference(oldStyleReference);
	}
	
	/**
	 * Undo is available if the cell is not null. A null value for the style is acceptable since a null 
	 * style means no style
	 */
	@Override
	public boolean canUndo() {
		return (cell != null);
	}

}
