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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Simple redefinition of a selection action that permit to set the selection 
 * and have access to the calculateEnabled method
 * 
 * @author Orlandin Marco
 *
 */
public abstract class CustomSelectionAction extends ACachedSelectionAction {

	public CustomSelectionAction(IWorkbenchPart part) {
		super(part);
	}

	public CustomSelectionAction(IWorkbenchPart part, int style){
			super(part, style);
	}
	
	/**
	 * Set the selection, to force the element where the action will operate
	 */
	public void setSelection(ISelection selection){
		super.setSelection(selection);
	}
	
	/**
	 * Return if the action is enabled or not, using the calculatEnabled method.
	 * 
	 */
	public boolean canExecute(){
		return calculateEnabled();
	}
}
