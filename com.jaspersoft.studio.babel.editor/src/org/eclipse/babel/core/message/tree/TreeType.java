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
package org.eclipse.babel.core.message.tree;

/**
 * Enum for two tree types. If a tree has the type {@link #Tree}, then it is
 * displayed as tree. E.g. following key is given: parent.child.grandchild
 * result:
 * 
 * <pre>
 * parent
 * 	child
 * 		grandchild
 * </pre>
 * 
 * If it is {@link #Flat}, it will be displayed as parent.child.grandchild.
 * 
 * @author Alexej Strelzow
 */
public enum TreeType {
    Tree, Flat
}
