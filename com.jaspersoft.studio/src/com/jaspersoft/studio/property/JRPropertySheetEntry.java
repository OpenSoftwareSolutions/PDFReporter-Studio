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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.ForwardUndoCompoundCommand;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertySheetEntry;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxLabelProvider;

/*
 * /* The Class JRPropertySheetEntry.
 */
public class JRPropertySheetEntry extends org.eclipse.ui.views.properties.PropertySheetEntry {

	/** The listener. */
	private PropertyChangeListener listener;

	/** The command stack listener. */
	private CommandStackListener commandStackListener;

	/** The model. */
	private ANode model;

	/** The stack. */
	private CommandStack stack;

	/**
	 * Instantiates a new jR property sheet entry.
	 * 
	 * @param stack the stack
	 * @param model the model
	 */
	public JRPropertySheetEntry(CommandStack stack, ANode model) {
		this(stack, model, true);
	}
	
	/**
	 * Instantiates a new jR property sheet entry.
	 * 
	 * @param stack the stack
	 * @param model the model
	 * @param addListener flag to add or not the listener on the command stack to refresh all the entries
	 */
	public JRPropertySheetEntry(CommandStack stack, ANode model, boolean addListener) {
		super();
		setCommandStack(stack, addListener);
		setModel(model);
	}

	/**
	 * Sets the model.
	 * 
	 * @param model
	 *          the new model
	 */
	public void setModel(ANode model) {
		if (listener != null && this.model != null)
			this.model.getPropertyChangeSupport().removePropertyChangeListener(listener);
		if (model != null) {
			if (listener == null) {
				listener = new PropertyChangeListener() {

					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getSource() instanceof IPropertySource)
							JRPropertySheetEntry.this.setValues(new Object[] { evt.getSource() });
					}
				};
			}
			model.getPropertyChangeSupport().addPropertyChangeListener(listener);
		}
		this.model = model;
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public ANode getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.PropertySheetEntry#setValues(java.lang.Object[])
	 */
	@Override
	public void setValues(Object[] objects) {
		if (objects.length > 0) { // TODO WORK WITH COLLECTION
			if (objects[0] instanceof EditPart) {
				if (((EditPart) objects[0]).getModel() instanceof ANode)
					setModel((ANode) ((EditPart) objects[0]).getModel());
			}
		} else
			setModel(null);

		if (objects.length == 0) {
			editValue = null;
		} else {
			// set the first value object as the entry's value
			Object newValue = objects[0];

			// see if we should convert the value to an editable value
			IPropertySource source = getPropertySource(newValue);
			if (source != null) {
				newValue = source.getEditableValue();
			}
			editValue = newValue;
		}
		super.setValues(objects);
	}

	/**
	 * The child dosen't have the listener on the stack because the listener
	 * refresh always from root to all the children, so one listener on the root
	 * is the only one needed
	 */
	protected JRPropertySheetEntry createChildEntry() {
		return new JRPropertySheetEntry(stack, model, false);
	}

	/**
	 * Sets the command stack.
	 * 
	 * @param stack the new commands stack
	 * @param addListener flag to add or not the listener on the command stack to refresh all the entries
	 */
	void setCommandStack(CommandStack stack, boolean addListener) {
		this.stack = stack;
		if (addListener){
			//First remove any previous listener
			if (commandStackListener != null){
				stack.removeCommandStackListener(commandStackListener);
				commandStackListener = null;
			}
			//Then create and add the new one
			commandStackListener = new CommandStackListener() {
				public void commandStackChanged(EventObject e) {
					refreshFromRoot();
				}
			};
			stack.addCommandStackListener(commandStackListener);
		}
	}

	/**
	 * Gets the command stack.
	 * 
	 * @return the command stack
	 */
	public CommandStack getCommandStack() {
		// only the root has, and is listening too, the command stack
		if (getParent() != null)
			return ((JRPropertySheetEntry) getParent()).getCommandStack();
		return stack;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.PropertySheetEntry#dispose()
	 */
	@Override
	public void dispose() {
		if (stack != null)
			stack.removeCommandStackListener(commandStackListener);
		if (getModel() != null)
			getModel().getPropertyChangeSupport().removePropertyChangeListener(listener);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.PropertySheetEntry#resetPropertyValue()
	 */
	public void resetPropertyValue() {
		JSSCompoundCommand cc = new JSSCompoundCommand(null);
		ResetValueCommand restoreCmd;

		if (getParent() == null)
			// root does not have a default value
			return;

		// Use our parent's values to reset our values.
		boolean change = false;
		Object[] objects = getParent().getValues();
		for (int i = 0; i < objects.length; i++) {
			IPropertySource source = getPropertySource(objects[i]);
			if (source.isPropertySet(getDescriptor().getId())) {
				// source.resetPropertyValue(getDescriptor()getId());
				restoreCmd = new ResetValueCommand();
				restoreCmd.setTarget(source);
				restoreCmd.setPropertyId(getDescriptor().getId());
				cc.add(restoreCmd);
				cc.setReferenceNodeIfNull(source);
				change = true;
			}
		}
		if (change) {
			getCommandStack().execute(cc);
			refreshFromRoot();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.PropertySheetEntry#valueChanged(org.eclipse.ui.views.properties.PropertySheetEntry)
	 */
	protected void valueChanged(PropertySheetEntry child) {
		// StructuredSelection selections =
		// (StructuredSelection)SelectionHelper.getActiveJRXMLEditor().getSite().getSelectionProvider().getSelection();

		valueChanged((JRPropertySheetEntry) child, new ForwardUndoCompoundCommand(), Arrays.asList(getValues()));
	}

	boolean isRefresh = false;

	/**
	 * Value changed.
	 * 
	 * @param child
	 *          the child
	 * @param command
	 *          the command
	 * @param selections
	 *          the actually selected elements
	 */
	void valueChanged(JRPropertySheetEntry child, final CompoundCommand command, List<?> selections) {
		if (!isRefresh && child.getValues().length > 0) {
			isRefresh = true;
			// The value and the property is the same for all the selected elements, so i take it from the first one
			// propertysheet
			Object newval = child.getValues()[0];
			Object propid = child.getDescriptor().getId();
			List<Object> remainingSelection = new ArrayList<Object>(selections);
			for (Object obj : selections) {
				Object rawModel = null;
				if (obj instanceof EditPart)
					rawModel = ((EditPart) obj).getModel();
				else if (obj instanceof ANode)
					rawModel = obj;
				if (rawModel != null && rawModel instanceof APropertyNode) {
					APropertyNode aNode = (APropertyNode) rawModel;
					IPropertySource propertySource = getPropertySource(aNode);
					Object oldval = aNode.getPropertyValue(propid);
					if (newval instanceof Command) {
						command.add((Command) newval);
						continue;
					}
					if (!(oldval instanceof INode)) {
						if (oldval != null && newval != null && oldval.equals(newval))
							continue;
						if (oldval == null && newval == null)
							continue;
					}
					SetValueCommand setCommand = new SetValueCommand(child.getDisplayName());
					setCommand.setTarget(propertySource);
					setCommand.setPropertyId(propid);
					setCommand.setPropertyValue(newval);
					command.add(setCommand);
					remainingSelection.remove(obj);
				}
			}

			// inform our parent
			if (getParent() != null) {
				((JRPropertySheetEntry) getParent()).valueChanged(this, command, remainingSelection);
				isRefresh = false;
			} else {
				// I am the root entry
				Display.getCurrent().asyncExec(new Runnable() {

					public void run() {
						stack.execute(command);
						isRefresh = false;
					}
				});
			}
		}
	}

	@Override
	public String getValueAsString() {
		ILabelProvider provider = getDescriptor().getLabelProvider();
		if (provider instanceof CheckBoxLabelProvider) {
			return provider.getText(editValue);//$NON-NLS-1$
		}
		if (provider == null) {
			return editValue.toString();
		}
		String text = provider.getText(editValue);
		if (text == null) {
			return "";//$NON-NLS-1$
		}
		return text;
	}

	private Object editValue;

}
