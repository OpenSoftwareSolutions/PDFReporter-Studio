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
package com.jaspersoft.studio.property;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

public class ResetValueCommand extends Command {

	/** the property that has to be reset. */
	protected Object propertyName;

	/** the current non-default value of the property. */
	protected Object undoValue;

	/** the property source whose property has to be reset. */
	protected IPropertySource target;

	/**
	 * Default Constructor: Sets the label for the Command.
	 * 
	 * @since 3.1
	 */
	public ResetValueCommand() {
		super(Messages.ResetValueCommand_restore_default_value);
	}

	/**
	 * Returns <code>true</code> IFF:<br>
	 * 1) the target and property have been specified<br>
	 * 2) the property has a default value<br>
	 * 3) the value set for that property is not the default.
	 * 
	 * @return true, if successful
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		boolean answer = false;
		if (target != null && propertyName != null) {
			answer = target.isPropertySet(propertyName);
			if (target instanceof IPropertySource2)
				answer = answer && (((IPropertySource2) target).isPropertyResettable(propertyName));
		}
		return answer;
	}

	/**
	 * Caches the undo value and invokes redo().
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		undoValue = target.getPropertyValue(propertyName);
		if (undoValue instanceof IPropertySource)
			undoValue = ((IPropertySource) undoValue).getEditableValue();
		redo();
	}

	/**
	 * Sets the IPropertySource.
	 * 
	 * @param propSource
	 *          the IPropertySource whose property has to be reset
	 */
	public void setTarget(IPropertySource propSource) {
		target = propSource;
	}

	/**
	 * Resets the specified property on the specified IPropertySource.
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		target.resetPropertyValue(propertyName);
		if (commands == null)
			commands = JaspersoftStudioPlugin.getPostSetValueManager().postSetValue(target, propertyName,
					target.getPropertyValue(propertyName), undoValue);
		if (commands != null)
			for (Command c : commands)
				c.execute();
	}

	private List<Command> commands;

	/**
	 * Sets the property that is to be reset.
	 * 
	 * @param pName
	 *          the property to be reset
	 */
	public void setPropertyId(Object pName) {
		propertyName = pName;
	}

	/**
	 * Restores the non-default value that was reset.
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (commands != null)
			for (Command c : commands)
				c.undo();
		target.setPropertyValue(propertyName, undoValue);
	}

}
