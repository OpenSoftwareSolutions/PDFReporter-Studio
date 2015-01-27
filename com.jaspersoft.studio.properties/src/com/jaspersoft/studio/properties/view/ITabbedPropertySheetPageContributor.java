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
 * Interface for a workbench part to contribute content to the tabbed property
 * view.
 * <p>
 * It is expected that the contributor ID is unique for a configuration of tabs
 * and sections. Editors and views can share a configuration by sharing a
 * contributor ID. Editors and views cannot share tabs and sections from
 * multiple contributors.
 * </p>
 * <p>
 * As a workaround, if all the elements in a structured selection implement
 * ITabbedPropertySheetPageContributor and they all return the same unique
 * contributor ID, then that configuration of tabs and sections will be used by
 * the tabbed property view for that selection.
 * </p>
 * 
 * @author Anthony Hunter
 */
public interface ITabbedPropertySheetPageContributor {

	/**
	 * Returns the contributor ID for the tabbed property sheet page.
	 * 
	 * @return the contributor ID for the tabbed property sheet page.
	 */
	public String getContributorId();
}
