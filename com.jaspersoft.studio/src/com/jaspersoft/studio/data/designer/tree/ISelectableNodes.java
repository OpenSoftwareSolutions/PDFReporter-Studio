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
package com.jaspersoft.studio.data.designer.tree;

import java.util.List;

import com.jaspersoft.studio.model.ANode;

/**
 * This interface is supposed to be implemented by those clients that want
 * to give the ability to support the nodes selection.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 * @param <T> node class type
 */
public interface ISelectableNodes<T extends ANode> {

	/**
	 * Returns a list of selected nodes based on the
	 * specified input query string.
	 * 
	 * @param query the query string
	 * @return the list of selected nodes
	 */
	List<T> getSelectableNodes(String query);
}
