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
package com.jaspersoft.studio.components.table.model.column.command;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.model.column.MCell;

/**
 * Command to add a delta (positive or negative) to the height of a cell. If the 
 * delta is negative and it's abs is greater then the height then it is changed to 
 * the height of the cell (always with a negative sign) to avoid to have a cell with
 * height < 0
 * 
 * @author Orlandin Marco
 *
 */
public class AddCellDeltaHeightCommand extends Command{
	
	/**
	 * The old height for the undo, it is easily computable, but 
	 * for simplicity we store it
	 */
	private int oldHeight;
	
	/**
	 * The delta to add to the height of the cell
	 */
	private int newHeightDelta;
	
	/**
	 * The cell to change
	 */
	private MCell cell;
	
	/**
	 * Create an instance of the class
	 * 
	 * @param cell
	 * @param newHeight
	 */
	public AddCellDeltaHeightCommand(MCell cell, int newHeight){
		this.cell = cell;
		this.newHeightDelta = newHeight;
	}
	
	@Override
	public void execute() {
		oldHeight = cell.getCell().getHeight();
		if (newHeightDelta < 0 && cell.getCell().getHeight() < Math.abs(newHeightDelta)){
			newHeightDelta = -cell.getCell().getHeight();
		}
		cell.getCell().setHeight(cell.getCell().getHeight()+newHeightDelta);
	}
	
	@Override
	public boolean canUndo() {
		return true;
	}
	
	@Override
	public void undo() {
		cell.getCell().setHeight(oldHeight);
	}
}
