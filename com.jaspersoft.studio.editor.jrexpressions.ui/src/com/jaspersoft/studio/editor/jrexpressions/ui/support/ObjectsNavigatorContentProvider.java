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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.jaspersoft.studio.editor.jrexpressions.ui.support.java.JavaExpressionEditorComposite;


/**
 * Content provider for the navigator tree containing the categories of object
 * items.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see JavaExpressionEditorComposite Example of usage of the content
 *      provider
 * 
 */
public class ObjectsNavigatorContentProvider implements ITreeContentProvider {

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	public Object getParent(Object element) {
		return null;
	}

	public Object[] getElements(Object inputElement) {
		// The passed element will be NameIconItem representing the objects root
		// categories
		// Usually these are the root categories (most of the times a subset of
		// them)
		// - Parameters
		// - Field
		// - Variables
		// - CrossTabs
		// - Built-in functions
		// - User Defined Expressions
		// - Recent Expressions
		if (inputElement != null
				&& inputElement instanceof ObjectCategoryItem[]) {
			return (Object[]) inputElement;
		}
		return new Object[0];
	}

	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof ObjectCategoryItem) {
			Object data = ((ObjectCategoryItem) parentElement).getData();
			if (data != null && data instanceof ObjectCategoryItem[]) {
				return (ObjectCategoryItem[]) data;
			}
		}

		return new Object[0];
	}

}
