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
package com.jaspersoft.studio.editor.action;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.report.CommonSelectionCacheProvider;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.property.SetValueCommand;

/**
 * 
 * Abstract class to implement to define a selection action that caches
 * the command and the selection set to provide better performance
 *
 */
public abstract class ACachedSelectionAction extends SetWorkbenchAction {

	protected boolean fresh = false;
	protected boolean freshChecked = false;
	
	/**
	 * The cached command
	 */
	protected Command command;
	
	protected boolean ischecked = false;
	
	/**
	 * Editor that provide the common selection provider, class that cache the actual
	 * selection and all the request for a subselection and stuff
	 */
	protected CachedSelectionProvider editor = null;
	
	public ACachedSelectionAction(IWorkbenchPart part) {
		super(part);
		editor = (CachedSelectionProvider) part;
	}

	public ACachedSelectionAction(IWorkbenchPart part, int style) {
		super(part, style);
		editor = (CachedSelectionProvider) part;
	}

	@Override
	protected void handleSelectionChanged() {
		fresh = false;
		freshChecked = false;
		if (editor != null){
			editor.getSelectionCache().selectionChanged(getSelection());
		}
		super.handleSelectionChanged();
	}

	@Override
	public void run() {
		if (fresh && command != null){
			execute(command);
		}
	}

	@Override
	protected boolean calculateEnabled() {
		if (!fresh)
			command = createCommand();
		fresh = true;
		return command != null && command.canExecute();
	}

	protected Command createCommand() {
		List<Object> mGraphElements = editor.getSelectionCache().getSelectionModelForType(MGraphicElement.class);
		for(Object obj : mGraphElements){
			JSSCompoundCommand cmd = new JSSCompoundCommand((ANode)obj);
			cmd.add(new SetValueCommand());
			return cmd;
		}
		return null;
	}
	
	/**
	 * Verifies that there is only one EditPart selected
	 * referring to a model object of the allowed class types.
	 * 
	 * @param classes the allowed type(s) for the model object 
	 * @return <code>true</code> the single model object is instance of
	 * 					one of the allowed types,<code>false</code> otherwise
	 */
	public boolean checkSingleSelectedObject(Class<?>...classes) {
		if (getSelectedObjects().size() != 1) return false;
		CommonSelectionCacheProvider cache = editor.getSelectionCache();
		for (Class<?> clazz : classes) {
			if (!cache.getSelectionModelForType(clazz).isEmpty())
				return true;
		}
		return false;
	}
	
	/**
	 * Verifies that all the currently selected objects are EditParts
	 * referring to model objects of the allowed class types.
	 * 
	 * @param classes the allowed type(s) for the model objects 
	 * @return <code>true</code> all model objects are instances of 
	 * 					one of the allowed types,<code>false</code> otherwise
	 */
	public boolean checkAllSelectedObjects(Class<?> searchedClass){
		List<Object> elements = editor.getSelectionCache().getSelectionModelForType(searchedClass);
		return (!elements.isEmpty() && elements.size() == getSelectedObjects().size());
	}
	
	@Override
	public void setWorkbenchPart(IWorkbenchPart part) {
		super.setWorkbenchPart(part);
		if (part instanceof CachedSelectionProvider){
			fresh = false;
			editor = (CachedSelectionProvider) part;
		}
	}
}
