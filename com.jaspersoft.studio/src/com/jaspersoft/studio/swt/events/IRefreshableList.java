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
package com.jaspersoft.studio.swt.events;

/**
 * This interface enables the "refresh list" feature for all its implementors.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface IRefreshableList {

	/**
	 * Forces the refresh of the internal graphical representation of a generic list of elements.
	 * Usually it can be a list of properties for a specific component.
	 * <p>
	 * 
	 * This could be needed, for example, when an update operation to the set of properties/items currently associated
	 * occurs and the UI must be properly redrawn.
	 */
	void refreshList();
}
