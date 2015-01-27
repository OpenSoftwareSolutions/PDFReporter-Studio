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

import java.text.MessageFormat;

import net.sf.jasperreports.engine.base.JRBaseFont;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * Class that implement a toolbar with two buttons to change the font size
 * @author Orlandin Marco
 *
 */
public class SPButton extends ASPropertyWidget {

	/**
	 * The buttons toolbar
	 */
	private ToolBar buttons;
	
	/**
	 * The element with the font attribute
	 */
	private APropertyNode fontValue;
	
	/**
	 * The image for the button of increment
	 */
	private Image imageValueIncrement;
	
	/**
	 * The image for the button of decrement
	 */
	private Image imageValueDecrement;
	
	/**
	 * % factor for the increment\decrement
	 */
	public static Integer factor = 10;
	
	/**
	 * Tooltip message for the increment button
	 */
	private String messageIncrement;
	
	/**
	 * Tooltip message for the decrement button
	 */
	private String messageDecrement;

	/**
	 * Crate a new button for increment or decrement of the font size
	 * @param parent parent where the button will be painted
	 * @param section section of the element
	 * @param pDescriptor descriptor of the attribute
	 * @param fontValue The element with the font attribute
	 */
	public SPButton(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor, APropertyNode fontValue){
		super(parent, section, pDescriptor);
		messageIncrement = MessageFormat.format(Messages.SPButon_Size_Increment, new Object[]{factor.toString()});
		imageValueIncrement = JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/edit-size-up.png"); 
		messageDecrement = MessageFormat.format(Messages.SPButon_Size_Decrement, new Object[]{factor.toString()}); 
		imageValueDecrement = JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/edit-size-down.png"); 
		this.fontValue = fontValue;
		createComponent(parent);
	}
	
	
	protected void createCommand(boolean increment){
		//Object fontSize = fontValue.getPropertyActualValue(JRBaseFont.PROPERTY_FONT_SIZE);
		Object fontSize = section.getElement().getPropertyValue(JRBaseFont.PROPERTY_FONT_SIZE);
		if (fontSize.equals(""))
			fontSize = fontValue.getPropertyActualValue(JRBaseFont.PROPERTY_FONT_SIZE);
		Float newValue = 2.0f;
		if (fontSize != null && fontSize.toString().length()>0){
			newValue = Float.valueOf(fontSize.toString());
			Integer plus = null;
			if (increment) plus = Math.round((new Float(newValue) / 100)*factor)+1;
			else plus =  Math.round((new Float(newValue) / 100)*-factor)-1;
			if ((newValue+plus)>99) newValue = 99.0f;
			else if ((newValue+plus)>0) newValue += plus;
			section.changeProperty(JRBaseFont.PROPERTY_FONT_SIZE, newValue.toString());
		}
	}
	
	/**
	 * Create a single button into the toolbar
	 * @param increment true if the button should be used for increment, false otherwise
	 */
	private void createButton(final boolean increment){
		Image imageValue;
		String message;
		if (increment){
			imageValue = imageValueIncrement;
			message = messageIncrement;
		} else {
			imageValue = imageValueDecrement;
			message = messageDecrement;
		}
		ToolItem button = new ToolItem(buttons, SWT.PUSH);
		button.setImage(imageValue);
		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				createCommand(increment);
			}

		});
		button.setToolTipText(message);		
	}
	
	
	@Override
	protected void createComponent(Composite parent) {
		if (fontValue != null){
			buttons = new ToolBar(parent, SWT.FLAT | SWT.WRAP);
			createButton(true);
			createButton(false);
		}
	}

	@Override
	public void setData(APropertyNode pnode, Object value) {
		buttons.setEnabled(pnode.isEditable());
	}

	@Override
	public Control getControl() {
		return buttons;
	}

}
