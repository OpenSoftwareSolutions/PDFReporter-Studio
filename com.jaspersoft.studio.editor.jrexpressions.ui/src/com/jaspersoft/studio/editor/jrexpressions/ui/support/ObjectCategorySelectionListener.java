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
package com.jaspersoft.studio.editor.jrexpressions.ui.support;

import com.jaspersoft.studio.editor.jrexpressions.ui.support.java.JavaExpressionEditorComposite;


/**
 * Classes which implement this interface provide a method
 * that deals with the events that are generated when an
 * {@link ObjectCategoryItem} is selected.
 * 
 * <p>
 * Example: classic scenario is the selection of an object
 * category item inside a tree. 
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see JavaExpressionEditorComposite Listener usage example
 *
 */
public interface ObjectCategorySelectionListener {
	
	/**
	 * Sent when a category item is selected.
	 * 
	 * @param event the event containing the information about the category selection
	 */
	void select(ObjectCategorySelectionEvent event);
}
