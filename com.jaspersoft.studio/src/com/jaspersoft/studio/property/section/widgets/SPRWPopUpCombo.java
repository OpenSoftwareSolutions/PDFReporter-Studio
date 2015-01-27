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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.combomenu.ComboItem;
import com.jaspersoft.studio.property.combomenu.ComboItemAction;
import com.jaspersoft.studio.property.combomenu.ComboMenuViewer;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * The widget for a popup combo box
 * @author Orlandin Marco
 *
 */
public class SPRWPopUpCombo extends ASPropertyWidget {
	
	/**
	 * The combo item
	 */
	protected ComboMenuViewer combo;

	/**
	 * The list of entry in the popup menu
	 */
	protected List<ComboItem> items = null;
		
	/**
	 * Create a new widget
	 * @param parent parent of the widget
	 * @param section section where the command will be executed
	 * @param pDescriptor descriptor of the property of this item
	 * @param items List of entry in the popup menu
	 */
	public SPRWPopUpCombo(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor, List<ComboItem> items) {
		super(parent, section, pDescriptor);
		this.items = items;
		createComponent(parent);
	}

	@Override
	public Control getControl() {
		if (combo!=null)
			return combo.getControl();
		else return null;
	}

	public void setData(APropertyNode pnode, Object b) {
		combo.setEnabled(pnode.isEditable());
		int index = 0;
		if (b != null)
			index = ((Number) b).intValue();
		combo.select(index);
	}
	
	/**
	 * Return the longest text in the list of entry
	 * @param itemList a list of entry
	 * @return the longest label
	 */
	public static String getLongest(List<ComboItem> itemList){
		String longest = "";
		for(ComboItem item : itemList)
			if (longest.length()<item.getText().length()){
				longest = item.getText();
			}
		return longest;
	}

	protected void createComponent(Composite parent) {
		if (items != null){
			combo = new ComboMenuViewer(parent, SWT.NORMAL, getLongest(items));
			combo.setItems(items);
			combo.addSelectionListener(new ComboItemAction() {
					@Override
					public void exec() {
							section.changeProperty(pDescriptor.getId(), combo.getSelectionValue());			
					}
			});
			combo.setToolTipText(pDescriptor.getDescription());
			getControl().addFocusListener(focusListener);
		}
	}

}
