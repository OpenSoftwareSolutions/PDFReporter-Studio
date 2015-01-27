/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Reiterer - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.core.message.tree;

public interface IAbstractKeyTreeModel {

    IKeyTreeNode[] getChildren(IKeyTreeNode node);

    IKeyTreeNode getChild(String key);

    IKeyTreeNode[] getRootNodes();

    IKeyTreeNode getRootNode();

    IKeyTreeNode getParent(IKeyTreeNode node);

    void accept(IKeyTreeVisitor visitor, IKeyTreeNode node);

}
