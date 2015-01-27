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
package com.jaspersoft.studio.utils.compatibility;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISources;

/**
 * This class contains some methods of the {@link org.eclipse.ui.handlers.HandlerUtil} class available
 * only in Eclipse version after the 3.6.x.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class HandlerUtil {

	/**
	 * Return the input of the active editor.
	 * 
	 * @param event
	 *            The execution event that contains the application context
	 * @return the input of the active editor, or <code>null</code>.
	 * @since 3.7
	 */
	public static IEditorInput getActiveEditorInput(ExecutionEvent event) {
		Object o = org.eclipse.ui.handlers.HandlerUtil.getVariable(event, ISources.ACTIVE_EDITOR_INPUT_NAME);
		if (o instanceof IEditorInput) {
			return (IEditorInput) o;
		}
		return null;
	}
	
}
