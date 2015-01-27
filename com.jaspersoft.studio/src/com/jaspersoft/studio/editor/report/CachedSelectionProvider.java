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
package com.jaspersoft.studio.editor.report;

/**
 * Interface to implement to provide a selection cache. The selection cache
 * can be used from the action (actually both toolbar and contextual) to cache
 * similar request of a subset of the selection and improve the performances
 * 
 * @author Orlandin Marco
 *
 */
public interface CachedSelectionProvider {

	/**
	 * Return the common selection provider
	 * 
	 * @return a not null selection provider. Remember that the elements inside the cache
	 * must be updated with the selectionChange method when the selection change
	 */
	public CommonSelectionCacheProvider getSelectionCache();
	
}
