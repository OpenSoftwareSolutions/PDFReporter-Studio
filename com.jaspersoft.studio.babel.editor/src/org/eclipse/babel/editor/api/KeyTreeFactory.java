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
package org.eclipse.babel.editor.api;

import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.tree.IAbstractKeyTreeModel;
import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.internal.AbstractKeyTreeModel;

/**
 * Factory class for the tree or nodes of the tree.
 * 
 * @see IAbstractKeyTreeModel
 * @see IValuedKeyTreeNode <br>
 * <br>
 * 
 * @author Alexej Strelzow
 */
public class KeyTreeFactory {

    /**
     * @param messagesBundleGroup
     *            Input of the key tree model
     * @return The {@link IAbstractKeyTreeModel}
     */
    public static IAbstractKeyTreeModel createModel(
            IMessagesBundleGroup messagesBundleGroup) {
        return new AbstractKeyTreeModel(
                (MessagesBundleGroup) messagesBundleGroup);
    }

    /**
     * @param parent
     *            The parent node
     * @param name
     *            The name of the node
     * @param id
     *            The id of the node (messages key)
     * @param bundleGroup
     *            The {@link IMessagesBundleGroup}
     * @return A new instance of {@link IValuedKeyTreeNode}
     */
    public static IValuedKeyTreeNode createKeyTree(IKeyTreeNode parent,
            String name, String id, IMessagesBundleGroup bundleGroup) {
        return new ValuedKeyTreeNode(parent, name, id, bundleGroup);
    }

}
