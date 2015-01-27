/*******************************************************************************
 * Copyright (c) 2012 Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexej Strelzow - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.tree;

import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.jface.viewers.TreeViewer;

public interface IKeyTreeContributor {

    void contribute(final TreeViewer treeViewer);

    IKeyTreeNode getKeyTreeNode(String key);

    IKeyTreeNode[] getRootKeyItems();
}
