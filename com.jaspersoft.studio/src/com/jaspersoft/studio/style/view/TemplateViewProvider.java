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
package com.jaspersoft.studio.style.view;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.style.TemplateStyle;

/**
 * Interface to implement to contribute with a tab into the TemplateStyleView (you must also use the extension 
 * point)
 * 
 * @author Orlandin Marco
 *
 */
public interface TemplateViewProvider {

	/**
	 * The parent is a composite that fill the tab
	 * 
	 * @param parent a Composite where insert the controls of this extension
	 */
	public void createControls(Composite parent);
	
	/**
	 * The name of the tab
	 * 
	 * @return a string that will be used as title of the tab
	 */
	public String getTabName();
	
	/**
	 * Used to pass the styles to show inside the tab. Here are passed all the template styles read from
	 * the properties file, and the implementation of the view need to check which are the ones that it 
	 * want to show
	 * 
	 * @param styles a list of all the TemplateStyles read from the properties file
	 */
	public void fillStyles(Collection<TemplateStyle> styles);
	
	/**
	 * Return the drop listener to handle the drag and drop of an element from the tab to the editor, it can be null
	 * if the drag operation is not wanted
	 * 
	 * @param viewer the viewer of the editor
	 * @return the drop listener that will be added to the editor
	 */
	public AbstractTransferDropTargetListener getDropListener(EditPartViewer viewer);
	
	/**
	 * Return the icon image that will be used on the tab
	 * 
	 * @return and SWT icon
	 */
	public Image getTabImage();
	
	/**
	 * Return the a void instance of the TemplateType visualized from the extension. This void instance can be used 
	 * with the method buildFromXML to create a real instance to visualize from a XML serialization of the file
	 * 
	 * @return
	 */
	public TemplateStyle getBuilder();
	
	/**
	 * Method to notify a change in the styles storage
	 * 
	 * @param e the event
	 */
	public void notifyChange(PropertyChangeEvent e);
	
}
