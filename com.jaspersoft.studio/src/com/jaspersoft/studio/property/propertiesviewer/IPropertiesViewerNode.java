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
package com.jaspersoft.studio.property.propertiesviewer;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.FilteredTree;

/**
 * This interface allows to define a page for a complex properties viewer organized
 * like the Preferences dialog of Eclipse with a tree on the left and a set of
 * stacked pages on the right.
 * 
 * @author mrabbi
 *
 * @see {@link TreePropertiesViewerPanel}
 */
public interface IPropertiesViewerNode {
	
	/**
	 * Returns the unique ID of this item node.
	 * 
	 * @return the element identifier
	 */
	public String getId();
	
	/**
	 * Returns the human readable name of this set of properties.
	 * 
	 * @return a human readable string
	 */
	public String getName();
	
	/**
	 * Returns the category ID for this node.
	 * This attribute is used to build the hierarchy of the nodes.
	 * It represents the ID of the parent node. 
	 * It is <code>null</code> in case the item is a root node.
	 * 
	 * @return the category identifier
	 */
	public String getCategory();
	
	/**
	 * Returns the actual control related to the node item.
	 * It will be shown in the main are of the panel/dialog. 
	 * 
	 * @return the current node control
	 */
	public Control getControl();
	
	/**
	 * Creates the control to show when the item is selected in the tree viewer.
	 * This method should be invoked only once, otherwise it will raise an exception.
	 *  
	 * @param parent the parent composite
	 * @throws IllegalStateException if the control has already been created
	 * @see #getControl()
	 */
	public void createControl(Composite parent) throws IllegalStateException;
	
	/**
	 * Returns a collection of keywords associated to the current node item.
	 * The search operation performed using the {@link FilteredTree} can benefit
	 * from this method.
	 * <p>
	 * If not needed it MUST return an empty collection.
	 * 
	 * @return the collection of keywords associated to the item node
	 */
    public Collection<String> getNodeKeywords();
    
    /**
     * Requests the node update.
     * A common behavior can be the control update depending on the internal status.
     */
    public void update();
    
    /**
     * Returns a suitable context ID for the help system, if it exists.
     * This way we can provide a "location" sensitive help.
     *  
     * @return the help context id for this node, <code>null</code> otherwise
     */
    public String getHelpContextID();
}
