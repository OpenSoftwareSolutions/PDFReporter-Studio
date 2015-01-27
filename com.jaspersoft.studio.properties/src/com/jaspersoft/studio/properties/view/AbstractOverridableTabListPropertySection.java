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

/**
 * An abstract implementation of a section in a tab that overrides the tabs that
 * are provided by the tabbed property registry with a new list of tabs.
 * 
 * @author Anthony Hunter
 * @since 3.4
 */
public class AbstractOverridableTabListPropertySection
	extends AbstractPropertySection
	implements IOverridableTabList {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.IOverridableTabList#getTabs()
	 */
	public ITabItem[] getTabs() {
		return new ITabItem[] {};
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.IOverridableTabList#selectTab(int)
	 */
	public void selectTab(int tab) {
		/* no default implementation */
	}
}
