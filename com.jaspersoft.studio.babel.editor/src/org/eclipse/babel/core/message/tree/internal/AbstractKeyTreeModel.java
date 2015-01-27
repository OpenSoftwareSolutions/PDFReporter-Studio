/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Matthias Lettmayer - fixed bug in returnNodeWithKey()
 ******************************************************************************/
package org.eclipse.babel.core.message.tree.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.internal.MessagesBundleGroupAdapter;
import org.eclipse.babel.core.message.tree.IAbstractKeyTreeModel;
import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.IKeyTreeVisitor;

/**
 * Hierarchical representation of all keys making up a
 * {@link MessagesBundleGroup}.
 * 
 * Key tree model, using a delimiter to separate key sections into nodes. For
 * instance, a dot (.) delimiter on the following key...
 * <p>
 * <code>person.address.street</code>
 * <P>
 * ... will result in the following node hierarchy:
 * <p>
 * 
 * <pre>
 * person
 *     address
 *         street
 * </pre>
 * 
 * @author Pascal Essiembre
 */
public class AbstractKeyTreeModel implements IAbstractKeyTreeModel {

    private List<IKeyTreeModelListener> listeners = new ArrayList<IKeyTreeModelListener>();
    private Comparator<IKeyTreeNode> comparator;

    private KeyTreeNode rootNode = new KeyTreeNode(null, null, null, null);

    private String delimiter;
    private MessagesBundleGroup messagesBundleGroup;

    protected static final KeyTreeNode[] EMPTY_NODES = new KeyTreeNode[] {};

    /**
     * Defaults to ".".
     */
    public AbstractKeyTreeModel(MessagesBundleGroup messagesBundleGroup) {
        this(messagesBundleGroup, "."); //$NON-NLS-1$
    }
    
    public void dispose(){
    	listeners.clear();
    }

    /**
     * Constructor.
     * 
     * @param messagesBundleGroup
     *            {@link MessagesBundleGroup} instance
     * @param delimiter
     *            key section delimiter
     */
    public AbstractKeyTreeModel(MessagesBundleGroup messagesBundleGroup,
            String delimiter) {
        super();
        this.messagesBundleGroup = messagesBundleGroup;
        this.delimiter = delimiter;
        createTree();

        messagesBundleGroup
                .addMessagesBundleGroupListener(new MessagesBundleGroupAdapter() {
                    public void keyAdded(String key) {
                        createTreeNodes(key);
                    }

                    public void keyRemoved(String key) {
                        removeTreeNodes(key);
                    }
                });
    }

    /**
     * Adds a key tree model listener.
     * 
     * @param listener
     *            key tree model listener
     */
    public void addKeyTreeModelListener(IKeyTreeModelListener listener) {
        listeners.add(0, listener);
    }

    /**
     * Removes a key tree model listener.
     * 
     * @param listener
     *            key tree model listener
     */
    public void removeKeyTreeModelListener(IKeyTreeModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify all listeners that a node was added.
     * 
     * @param node
     *            added node
     */
    protected void fireNodeAdded(KeyTreeNode node) {
        for (IKeyTreeModelListener listener : listeners) {
            listener.nodeAdded(node);
        }
    }

    /**
     * Notify all listeners that a node was removed.
     * 
     * @param node
     *            removed node
     */
    protected void fireNodeRemoved(KeyTreeNode node) {
        for (IKeyTreeModelListener listener : listeners) {
            listener.nodeRemoved(node);
        }
    }

    /**
     * Gets all nodes on a branch, starting (and including) with parent node.
     * This has the same effect of calling <code>getChildren(KeyTreeNode)</code>
     * recursively on all children.
     * 
     * @param parentNode
     *            root of a branch
     * @return all nodes on a branch
     */
    // TODO inline and remove this method.
    public KeyTreeNode[] getBranch(KeyTreeNode parentNode) {
        return parentNode.getBranch().toArray(new KeyTreeNode[] {});
    }

    /**
     * Accepts the visitor, visiting the given node argument, along with all its
     * children. Passing a <code>null</code> node will walk the entire tree.
     * 
     * @param visitor
     *            the object to visit
     * @param node
     *            the starting key tree node
     */
    public void accept(IKeyTreeVisitor visitor, IKeyTreeNode node) {
        if (node == null) {
            return;
        }

        if (node != null) {
            visitor.visitKeyTreeNode(node);
        }
        IKeyTreeNode[] nodes = getChildren(node);
        for (int i = 0; i < nodes.length; i++) {
            accept(visitor, nodes[i]);
        }
    }

    /**
     * Gets the child nodes of a given key tree node.
     * 
     * @param node
     *            the node from which to get children
     * @return child nodes
     */
    public IKeyTreeNode[] getChildren(IKeyTreeNode node) {
        if (node == null) {
            return null;
        }

        IKeyTreeNode[] nodes = node.getChildren();
        if (getComparator() != null) {
            Arrays.sort(nodes, getComparator());
        }
        return nodes;
    }

    /**
     * Gets the comparator.
     * 
     * @return the comparator
     */
    public Comparator<IKeyTreeNode> getComparator() {
        return comparator;
    }

    /**
     * Sets the node comparator for sorting sibling nodes.
     * 
     * @param comparator
     *            node comparator
     */
    public void setComparator(Comparator<IKeyTreeNode> comparator) {
        this.comparator = comparator;
    }

    /**
     * Depth first for the first leaf node that is not filtered. It makes the
     * entire branch not not filtered
     * 
     * @param filter
     *            The leaf filter.
     * @param node
     * @return true if this node or one of its descendant is in the filter (ie
     *         is displayed)
     */
    public boolean isBranchFiltered(IKeyTreeNodeLeafFilter filter,
            IKeyTreeNode node) {
        if (!((KeyTreeNode) node).hasChildren()) {
            return filter.isFilteredLeaf(node);
        } else {
            // depth first:
            for (IKeyTreeNode childNode : ((KeyTreeNode) node)
                    .getChildrenInternal()) {
                if (isBranchFiltered(filter, childNode)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the delimiter.
     * 
     * @return delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the delimiter.
     * 
     * @param delimiter
     *            delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Gets the key tree root nodes.
     * 
     * @return key tree root nodes
     */
    public IKeyTreeNode[] getRootNodes() {
        return getChildren(rootNode);
    }

    public IKeyTreeNode getRootNode() {
        return rootNode;
    }

    /**
     * Gets the parent node of the given node.
     * 
     * @param node
     *            node from which to get parent
     * @return parent node
     */
    public IKeyTreeNode getParent(IKeyTreeNode node) {
        return node.getParent();
    }

    /**
     * Gets the messages bundle group that this key tree represents.
     * 
     * @return messages bundle group
     */
    public MessagesBundleGroup getMessagesBundleGroup() {
        // TODO consider moving this method (and part of constructor) to super
        return messagesBundleGroup;
    }

    private void createTree() {
        rootNode = new KeyTreeNode(null, null, null, messagesBundleGroup);
        String[] keys = messagesBundleGroup.getMessageKeys();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            createTreeNodes(key);
        }
    }

    private void createTreeNodes(String bundleKey) {
        StringTokenizer tokens = new StringTokenizer(bundleKey, delimiter);
        KeyTreeNode node = rootNode;
        String bundleKeyPart = ""; //$NON-NLS-1$
        while (tokens.hasMoreTokens()) {
            String name = tokens.nextToken();
            bundleKeyPart += name;
            KeyTreeNode child = (KeyTreeNode) node.getChild(name);
            if (child == null) {
                child = new KeyTreeNode(node, name, bundleKeyPart,
                        messagesBundleGroup);
                fireNodeAdded(child);
            }
            bundleKeyPart += delimiter;
            node = child;
        }
        node.setUsedAsKey();
    }

    private void removeTreeNodes(String bundleKey) {
        if (bundleKey == null) {
            return;
        }
        StringTokenizer tokens = new StringTokenizer(bundleKey, delimiter);
        KeyTreeNode node = rootNode;
        while (tokens.hasMoreTokens()) {
            String name = tokens.nextToken();
            node = (KeyTreeNode) node.getChild(name);
            if (node == null) {
                System.err
                        .println("No RegEx node matching bundleKey to remove"); //$NON-NLS-1$
                return;
            }
        }
        KeyTreeNode parentNode = (KeyTreeNode) node.getParent();
        parentNode.removeChild(node);
        fireNodeRemoved(node);
        while (parentNode != rootNode) {
            if (!parentNode.hasChildren()
                    && !messagesBundleGroup.isMessageKey(parentNode
                            .getMessageKey())) {
                ((KeyTreeNode) parentNode.getParent()).removeChild(parentNode);
                fireNodeRemoved(parentNode);
            }
            parentNode = (KeyTreeNode) parentNode.getParent();
        }
    }

    public interface IKeyTreeNodeLeafFilter {
        /**
         * @param leafNode
         *            A leaf node. Must not be called if the node has children
         * @return true if this node should be filtered.
         */
        boolean isFilteredLeaf(IKeyTreeNode leafNode);
    }

    public IKeyTreeNode getChild(String key) {
        return returnNodeWithKey(key, rootNode);
    }

    private IKeyTreeNode returnNodeWithKey(String key, IKeyTreeNode node) {

        if (!key.equals(node.getMessageKey())) {
            for (IKeyTreeNode n : node.getChildren()) {
                IKeyTreeNode returnNode = returnNodeWithKey(key, n);
                if (returnNode == null) {
                    continue;
                } else {
                    return returnNode;
                }
            }
        } else {
            return node;
        }
        return null;
    }
}
