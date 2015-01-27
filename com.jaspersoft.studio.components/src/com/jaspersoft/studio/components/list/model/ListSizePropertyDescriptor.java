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
package com.jaspersoft.studio.components.list.model;

import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.StandardListComponent;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.jaspersoft.studio.components.list.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IPropertyDescriptorWidget;
import com.jaspersoft.studio.property.section.widgets.SPGenericToolbar;

/**
 * Property descriptor to show some button to quickset the size of the list
 * cell
 * 
 * @author Orlandin Marco
 *
 */
public class ListSizePropertyDescriptor extends PropertyDescriptor  implements IPropertyDescriptorWidget{

	/**
	 * The section
	 */
	private AbstractSection section;
	
	/**
	 * Id of this descriptor
	 */
	public static final String PROPERTY_ID = "LIST_CELL_SIZE_QUICKSETTINGS"; //$NON-NLS-1$
	
	/**
	 * Listener to set the cell size to the size of the element
	 */
	private SelectionAdapter setCellToDefaultAction = new SelectionAdapter() {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			for (APropertyNode node : section.getElements()){
				if (node instanceof MList){
					MList list = (MList)node;
					int elementWidth = list.getValue().getWidth();
					int elementHeight = list.getValue().getHeight();
					section.changePropertyOn(MList.PREFIX + DesignListContents.PROPERTY_HEIGHT, elementHeight, list);
					section.changePropertyOn(MList.PREFIX + DesignListContents.PROPERTY_WIDTH, elementWidth, list);
				}
			}
			section.refresh();
		};
		
	};
	
	/**
	 * Listener to set the cell size to fit the content
	 */
	private SelectionAdapter setCellToContentAction = new SelectionAdapter() {
		
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			
			for (APropertyNode node : section.getElements()){
				if (node instanceof MList){
					MList list = (MList)node;
					StandardListComponent listComponent = (StandardListComponent)list.getValue().getComponent();
					int cellWidth = 100;
					int cellHeight = 30;
					for(JRChild child : listComponent.getContents().getChildren()){
						if (child instanceof JRElement){
							JRElement jrChild = (JRElement)child;
							int rightBound = jrChild.getX()+jrChild.getWidth();
							int downBound  = jrChild.getY()+jrChild.getHeight();
							cellWidth = Math.max(cellWidth, rightBound);
							cellHeight = Math.max(cellHeight, downBound);
						}
					}
					section.changePropertyOn(MList.PREFIX + DesignListContents.PROPERTY_HEIGHT, cellHeight, list);
					section.changePropertyOn(MList.PREFIX + DesignListContents.PROPERTY_WIDTH, cellWidth, list);	
				}
			}
			section.refresh();
		};
		
	};
	
	public ListSizePropertyDescriptor() {
		super(PROPERTY_ID, ""); //$NON-NLS-1$
	}
	
	/**
	 * Create the widget and define the button to set the cell size to the element size and to the content size
	 */
	@Override
	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		this.section = section;
		SPGenericToolbar toolbuttons = new SPGenericToolbar(parent, section, this, SPGenericToolbar.Style.buttons);
		toolbuttons.addItem(setCellToDefaultAction, Messages.ListSizePropertyDescriptor_elementFitTooltip, Messages.ListSizePropertyDescriptor_elementFitName, null);
		toolbuttons.addItem(setCellToContentAction, Messages.ListSizePropertyDescriptor_contentFitTooltip, Messages.ListSizePropertyDescriptor_contentFitName, null);
		toolbuttons.createItems();
		return toolbuttons;
	}

}
