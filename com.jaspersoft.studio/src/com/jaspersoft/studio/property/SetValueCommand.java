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

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.utils.SelectionHelper;

/*
 * The Class SetValueCommand.
 */
public class SetValueCommand extends Command {

	/** The property value. */
	protected Object propertyValue;

	/** The property name. */
	protected Object propertyName;

	/** The undo value. */
	protected Object undoValue;

	/** The reset on undo. */
	protected boolean resetOnUndo;

	/** The target. */
	protected IPropertySource target;

	/**
	 * Instantiates a new sets the value command.
	 */
	public SetValueCommand() {
		super(""); //$NON-NLS-1$
	}

	/**
	 * Instantiates a new sets the value command.
	 * 
	 * @param propLabel
	 *          the prop label
	 */
	public SetValueCommand(String propLabel) {
		super(MessageFormat.format(Messages.SetValueCommand_set_zero_property, new Object[] { propLabel }).trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return true;
	}

	/**
	 * Gets the target.
	 * 
	 * @return the target
	 */
	public IPropertySource getTarget() {
		((ANode) target).setValue(targetValue);
		return target;
	}

	private Object targetValue;

	/**
	 * Sets the target.
	 * 
	 * @param aTarget
	 *          the new target
	 */
	public void setTarget(IPropertySource aTarget) {
		target = aTarget;
		targetValue = ((ANode) aTarget).getValue();
	}

	/**
	 * Sets the property id.
	 * 
	 * @param pName
	 *          the new property id
	 */
	public void setPropertyId(Object pName) {
		propertyName = pName;
	}

	/**
	 * Sets the property value.
	 * 
	 * @param val
	 *          the new property value
	 */
	public void setPropertyValue(Object val) {
		propertyValue = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		undoValue = getTarget().getPropertyValue(propertyName);
		getTarget().setPropertyValue(propertyName, propertyValue);

		if (commands == null){
			List<Command> commandsList = JaspersoftStudioPlugin.getPostSetValueManager().postSetValue(target, propertyName, propertyValue, undoValue);
			ANode startingNode = null;
			//try to found the root node of the report
			if (target instanceof ANode) startingNode = (ANode)target;
			else startingNode = SelectionHelper.getOpenedRoot();
			//Copy the contributed post descriptors inside the jss compound command
			commands = new JSSCompoundCommand(JSSCompoundCommand.getMainNode(startingNode));
			for(Command c : commandsList){
				commands.add(c);
			}
		}
		if (commands != null)
			commands.execute();
	}

	/**
	 * For the post set descriptor also a JSScompound command is used,
	 * to disable useless refresh operation 
	 */
	private JSSCompoundCommand commands;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (commands != null){
			commands.undo();
		}
		if (resetOnUndo)
			getTarget().resetPropertyValue(propertyName);
		else
			getTarget().setPropertyValue(propertyName, undoValue);
	}

}
