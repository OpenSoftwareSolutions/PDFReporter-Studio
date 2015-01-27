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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.Misc;

/**
 * A combo property widget used only to input and show float numbers
 * 
 * 
 * @author Orlandin Marco
 *
 */
public class SPRWFloatCombo extends ASPropertyWidget {
	
	/**
	 * The combo widget
	 */
	protected Combo combo;

	/**
	 * Boolean flag to know if the widget is refreshing FIXME: should be synchornized
	 */
	private boolean refresh = false;
	
	/**
	 * The edited node
	 */
	protected APropertyNode pnode;
	
	/**
	 * The combo background default color
	 */
	private Color comboBackgroundDefault;

	public SPRWFloatCombo(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	/**
	 * Return the combo control
	 */
	@Override
	public Control getControl() {
		return combo;
	}

	protected void createComponent(Composite parent) {
		combo = new Combo(parent, SWT.FLAT);
		comboBackgroundDefault = combo.getBackground();
		if (parent.getLayout() instanceof GridLayout) {
			GridData gd = new GridData();
			gd.minimumWidth = 100;
			combo.setLayoutData(gd);
		}
		setNewItems((RWComboBoxPropertyDescriptor) pDescriptor);
		
		
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (refresh)
					return;
				if (combo.getSelectionIndex() >= 0) {
					section.changeProperty(pDescriptor.getId(), combo.getItem(combo.getSelectionIndex()));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (refresh)
					return;
				String text = combo.getText().trim();
				//If the string ends with the separator probably the user must still insert char, so don't set it 
				if (!(text.endsWith(",") || text.endsWith("."))){
					try{
						Float realValue = Float.valueOf(text.replace(",", "."));
						section.changeProperty(pDescriptor.getId(), realValue.toString());
						combo.setBackground(comboBackgroundDefault);
					} catch(NumberFormatException ex){
						//If the value is not a valid number the the background of the textarea became red
						combo.setBackground(ResourceManager.getColor(255, 0, 0));
					}
				}
			}
		});
		combo.setToolTipText(pDescriptor.getDescription());
	}
	
	/**
	 * Remove all the decimal zeros from a string. If after the remove the remaining trail char 
	 * is a . the it is also removed
	 * 
	 * @param value a string
	 * @return a string without decimal zeros at the end
	 */
	private String removeUnnecessaryZeros(String value){
		String newValue = value.replaceAll("(\\.(\\d*[1-9])?)0+", "$1");
		if (newValue.endsWith(".")) newValue = newValue.substring(0, newValue.length()-1);
		return newValue;
	}


	public void setData(APropertyNode pnode, Object b) {
		refresh = true;
		this.pnode = pnode;
		final RWComboBoxPropertyDescriptor pd = (RWComboBoxPropertyDescriptor) pDescriptor;

		String str = removeUnnecessaryZeros((String) b);
		String[] items = combo.getItems();
		int selection = -1;
		for (int i = 0; i < items.length; i++) {
			if (Misc.compare(items[i], str, pd.isCaseSensitive())) {
				selection = i;
				break;
			}
		}
		if (selection != -1) combo.select(selection);
		else combo.setText(Misc.nvl(str));
		int stringLength = combo.getText().length();

		combo.setSelection(new Point(stringLength, stringLength));
		combo.getParent().layout(true);
		combo.setEnabled(pnode.isEditable());
		refresh = false;
	}

	public void setNewItems(final RWComboBoxPropertyDescriptor pd) {
		combo.setItems(pd.getItems());
	}
}
