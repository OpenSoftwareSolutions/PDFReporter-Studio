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
package com.jaspersoft.studio.property.section.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * Property widget to show a series of button inside a toolbar
 * or inside a composite, depending on the style defined during the
 * creation
 * 
 * @author Orlandin Marco
 *
 */
public class SPGenericToolbar extends ASPropertyWidget {
	
	/**
	 * Style for the control, use this as argument on the
	 * constructor to have the control as a toolbar or 
	 * as a composite with a series of button
	 * 
	 * @author Orlandin Marco
	 *
	 */
	public enum Style{toolbar, buttons};
	
	/**
	 * List of the toolitem\buttons that will be created
	 */
	private List<ToolItemDescription> toolItems;
	
	/**
	 * The parent toolbar, is null if the style is buttons
	 */
	private ToolBar toolBar;

	/**
	 * The buttons container, is null if the style is toolbar
	 */
	private Composite buttonComposite;
	
	/**
	 * The current style
	 */
	private Style style;

	/**
	 * The descriptor for a button
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class ToolItemDescription{
		
		/**
		 * Listener to call when the button is pressed
		 */
		private SelectionListener listener;
		
		/**
		 * Tooltip for the button
		 */
		private String tooltip;
		
		/**
		 * Text for the button
		 */
		private String text;
		
		/**
		 * Image descriptor for the button
		 */
		private ImageDescriptor image;
		
		public ToolItemDescription(SelectionListener listener, String tooltip, String text, ImageDescriptor image){
			this.listener = listener;
			this.tooltip = tooltip;
			this.text = text;
			this.image = image;
		}
		
		public SelectionListener getListener(){
			return listener;
		}
		
		public String getTooltip(){
			return tooltip;
		}
		
		public String getText(){
			return text;
		}
		
		public ImageDescriptor getImage(){
			return image;
		}
		
	}
	
	/**
	 * Create the class and the graphical widget
	 * 
	 * @param parent the parent container
	 * @param section the section 
	 * @param pDescriptor the descriptor of the property
	 * @param style a style between toolbar and buttons
	 */
	public SPGenericToolbar(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor, Style style) {
		super(parent, section, pDescriptor);
		this.style = style;
	}
	
	/**
	 * Add a button or a toolitem (depending on the style) to the controls. All the controls added are created when the
	 * method createItems is called
	 * 
	 * @param listener listener to call when the button is pressed
	 * @param toolTip tooltip for the button
	 * @param text text for the button, it is applied only if not null
	 * @param image image for the button, it is applied only if not null
	 */
	public void addItem(SelectionListener listener, String toolTip, String text, ImageDescriptor image){
		toolItems.add(new ToolItemDescription(listener, toolTip, text, image));
	}

	@Override
	public Control getControl() {
		if (Style.toolbar.equals(style)){
			return toolBar;
		} else {
			return buttonComposite;
		}
	}

	/**
	 * Create the parent control, toolbar ro composite
	 */
	protected void createComponent(Composite parent) {
		toolItems = new ArrayList<ToolItemDescription>();
		if (Style.toolbar.equals(style)){
			toolBar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		} else {
			buttonComposite = new Composite(parent, SWT.NONE);
			RowLayout layout = new RowLayout();
			layout.marginBottom = 0;
			layout.marginLeft = 0;
			layout.marginRight = 1;
			layout.marginTop = 0;
			buttonComposite.setLayout(layout);
		}
	}
	
	/**
	 * Create all the added items
	 */
	public void createItems(){
		if (Style.toolbar.equals(style)){
			createItemsToolbar();
		} else {
			createItemsButtons();
		}
	}
	
	/**
	 * Create the items as button inside the composite
	 */
	private void createItemsButtons(){
		for(ToolItemDescription desc : toolItems){
			Button btn = new Button(buttonComposite, SWT.NONE);
			if (desc.getText() != null && !desc.getText().isEmpty()){
				btn.setText(desc.getText());
			}
			btn.setToolTipText(desc.getTooltip());
			btn.setData(desc.getListener());
			if (desc.getImage() != null){
				btn.setImage(ResourceManager.getImage(desc.getImage()));
			}
			btn.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Object data = e.widget.getData();
					if (data instanceof SelectionListener){
						((SelectionListener)data).widgetSelected(e);
					}
				}
				
			});
		}
	}
	
	/**
	 * Create the items as toolitem inside the toolbar
	 */
	private void createItemsToolbar(){
		for(ToolItemDescription desc : toolItems){
			ToolItem newItem = new ToolItem(toolBar, SWT.PUSH);
			if (desc.getText() != null && !desc.getText().isEmpty()){
				newItem.setText(desc.getText());
			}
			newItem.setToolTipText(desc.getTooltip());
			newItem.setData(desc.getListener());
			if (desc.getImage() != null){
				newItem.setImage(ResourceManager.getImage(desc.getImage()));
			}
			newItem.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Object data = e.widget.getData();
					if (data instanceof SelectionListener){
						((SelectionListener)data).widgetSelected(e);
					}
				}
				
			});
		}
	}

	public void setData(APropertyNode pnode, Object b) {
	}
}
