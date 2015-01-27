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
package com.jaspersoft.studio.editor.gef.commands;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.property.SetValueCommand;

/**
 * Command to align one or more elements to another primary element
 * 
 * @author Orlandin Marco
 *
 */
public class Align2ElementCommand extends Command {
	
	/**
	 * The type of alignment
	 */
	private int alignement;
	
	/**
	 * The primary element used as reference to align the others
	 */
	private MGraphicElement primary;
	
	/**
	 * The current selection
	 */
	private List<MGraphicElement> selection;
	
	/**
	 * The list of commands that will be generated to move the other elements
	 */
	private List<SetValueCommand> setPositionCommands;

	/**
	 * Create the command 
	 * 
	 * @param alignement The type of alignment, should be one between PositionConstants.LEFT, PositionConstants.RIGHT,
	 * PositionConstants.BOTTOM, PositionConstants.TOP, PositionConstants.CENTER, PositionConstants.MIDDLE
	 * @param primary the primary element used as reference
	 * @param selection the elements to move. It can contains also the primary element but this is obviously ignored
	 * during the movments
	 */
	public Align2ElementCommand(int alignement, MGraphicElement primary, List<MGraphicElement> selection){
		super();
		this.alignement = alignement;
		this.primary = primary;
		this.selection = selection;
	}
	
	@Override
	public boolean canExecute() {
		return (primary != null && selection != null && selection.size()>1);
	}
	
	@Override
	public boolean canUndo() {
		return setPositionCommands != null;
	}

	@Override
	public void execute() {
		setPositionCommands = new ArrayList<SetValueCommand>();
		JRDesignElement designElement = primary.getValue();
		for(MGraphicElement element : selection){
			if (element != primary){
				JRDesignElement selected = element.getValue();
				switch (alignement) {
				case PositionConstants.LEFT:
					createCommand(JRDesignElement.PROPERTY_X, element, designElement.getX());
					break;
				case PositionConstants.RIGHT:
					int rightPost = designElement.getX() + designElement.getWidth() - selected.getWidth();
					createCommand(JRDesignElement.PROPERTY_X, element, rightPost);
					break;
				case PositionConstants.TOP:
					createCommand(JRDesignElement.PROPERTY_Y, element, designElement.getY());
					break;
				case PositionConstants.BOTTOM:
					int bottomPos = designElement.getY() + designElement.getHeight() - selected.getHeight();
					createCommand(JRDesignElement.PROPERTY_Y, element, bottomPos);
					break;
				case PositionConstants.CENTER:
					int centerVert = designElement.getX() + Math.abs(designElement.getWidth() - selected.getWidth())/2;
					createCommand(JRDesignElement.PROPERTY_X, element, centerVert);
					break;
				case PositionConstants.MIDDLE:
					int middle = designElement.getY() + Math.abs(designElement.getHeight() - selected.getHeight())/2;
					createCommand(JRDesignElement.PROPERTY_Y, element, middle);
					break;
				default:
					break;
				}
			}
		}
		//Execute the subcommands
		for(Command cmd : setPositionCommands){
			cmd.execute();
		}
	}

	/**
	 * Create a set value command for a specified properties and add it to the list
	 * of subcommands to execute to move the other selected elements
	 * 
	 * @param property the property to change
	 * @param destination the element to move
	 * @param value the new value of the property
	 */
	private void createCommand(String property, MGraphicElement destination, Object value){
		SetValueCommand newCommand = new SetValueCommand();
		newCommand.setTarget(destination);
		newCommand.setPropertyValue(value);
		newCommand.setPropertyId(property);
		setPositionCommands.add(newCommand);
	}
	
	@Override
	public void undo() {
		for(SetValueCommand undoCommand : setPositionCommands){
			undoCommand.undo();
		}
		setPositionCommands = null;
	}
}
