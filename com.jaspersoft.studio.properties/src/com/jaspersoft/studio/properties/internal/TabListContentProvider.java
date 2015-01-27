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
package com.jaspersoft.studio.properties.internal;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchPart;

/**
 * The default implementation of the content provider for the 
 * tabbed property sheet page's list of tabs.
 * 
 * @author Anthony Hunter
 */
public class TabListContentProvider
	implements IStructuredContentProvider {
	
	protected TabbedPropertyRegistry registry;

	protected IWorkbenchPart currentPart;
	
	/**
	 * Constructor for TabListContentProvider.
	 * @param registry the tabbed property registry.
	 */
	public TabListContentProvider(TabbedPropertyRegistry registry) {
		this.registry = registry;
	}
	
	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		Assert.isTrue(inputElement instanceof ISelection);
			return registry
			.getTabDescriptors(currentPart, (ISelection) inputElement);
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		/* not used */
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.currentPart = ((TabbedPropertyViewer)viewer).getWorkbenchPart();
	}
}
