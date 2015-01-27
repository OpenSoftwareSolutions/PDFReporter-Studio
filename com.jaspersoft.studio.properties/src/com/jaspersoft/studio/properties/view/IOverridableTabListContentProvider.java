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
package com.jaspersoft.studio.properties.view;

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * A content provider for the tabbed property sheet page's list of tabs. Used by
 * a section that overrides the tabs that are provided by the tabbed property
 * registry with a new list of tabs.
 * <p>
 * The overridable tab list is a content provider that provides both the
 * sections and the tab labels.

 * @author Anthony Hunter
 * @since 3.4
 */
public interface IOverridableTabListContentProvider extends IStructuredContentProvider {

	/**
	 * Override the tabs displayed in the tab list with a new list of tabs.
	 */
	public void overrideTabs();
}
