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

import org.eclipse.babel.core.message.IMessagesBundleGroup;

public interface IKeyTreeNode {

    /**
     * Returns the key of the corresponding Resource-Bundle entry.
     * 
     * @return The key of the Resource-Bundle entry
     */
    String getMessageKey();

    /**
     * Returns the set of Resource-Bundle entries of the next deeper hierarchy
     * level that share the represented entry as their common parent.
     * 
     * @return The direct child Resource-Bundle entries
     */
    IKeyTreeNode[] getChildren();

    /**
     * The represented Resource-Bundle entry's id without the prefix defined by
     * the entry's parent.
     * 
     * @return The Resource-Bundle entry's display name.
     */
    String getName();

    /**
     * Returns the set of Resource-Bundle entries from all deeper hierarchy
     * levels that share the represented entry as their common parent.
     * 
     * @return All child Resource-Bundle entries
     */
    // Collection<? extends IKeyTreeItem> getNestedChildren();

    /**
     * Returns whether this Resource-Bundle entry is visible under the given
     * filter expression.
     * 
     * @param filter
     *            The filter expression
     * @return True if the filter expression matches the represented
     *         Resource-Bundle entry
     */
    // boolean applyFilter(String filter);

    /**
     * The Resource-Bundle entries parent.
     * 
     * @return The parent Resource-Bundle entry
     */
    IKeyTreeNode getParent();

    /**
     * The Resource-Bundles key representation.
     * 
     * @return The Resource-Bundle reference, if known
     */
    IMessagesBundleGroup getMessagesBundleGroup();

    boolean isUsedAsKey();

    void setParent(IKeyTreeNode parentNode);

    void addChild(IKeyTreeNode childNode);

}
