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

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.base.JRBaseFont;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * A combo menu that could be used to represent a font
 * 
 * @author Orlandin Marco
 * 
 */
public class SPFontNameCombo extends ASPropertyWidget {

	/**
	 * The combo popup
	 */
	protected Combo combo;

	/**
	 * True if the combo popup was already initialized with the data, false otherwise
	 */
	protected boolean dataSetted;

	/**
	 * String used in the combobox to print a separator
	 */
	private static String separator = "__________________";

	public SPFontNameCombo(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
		dataSetted = false;
	}

	/**
	 * Given a combo and and a string return the index of the string in the combo
	 * 
	 * @param combo
	 * @param searchedString
	 * @return the index of the string in the combo, or 0 if the string is not found
	 */
	private int indexOf(Combo combo, String searchedString) {
		String[] elements = combo.getItems();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].equals(searchedString)) {
				return i;
			}
		}
		return 0;
	}
	
	public static List<String[]> getFontNames() {
		java.util.List<String[]> classes = new ArrayList<String[]>();
		java.util.List<String> elements = new ArrayList<String>();
		classes.add(elements.toArray(new String[elements.size()]));
		elements = new ArrayList<String>();
		String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			elements.add(name);
		}
		classes.add(elements.toArray(new String[elements.size()]));
		return classes;
	}

	/**
	 * Set the data of the combo popup, and if it wasn't initialized the fonts will be added
	 */
	@Override
	public void setData(final APropertyNode pnode, Object b) {
		if (pnode != null) {
			combo.setEnabled(pnode.isEditable());
			if (!dataSetted) {
				if (pnode.getJasperConfiguration() != null) combo.setItems(pnode.getJasperConfiguration().getFontList());
				else FontUtils.stringToItems(getFontNames());
				combo.addModifyListener(new ModifyListener() {

					private int time = 0;

					public void modifyText(ModifyEvent e) {
						if (e.time - time > 100) {
							String value = combo.getText();
							if (!value.equals(separator))
								propertyChange(section, JRBaseFont.PROPERTY_FONT_NAME, combo.getText());
							else
								combo.select(indexOf(combo, (String) pnode.getPropertyActualValue(JRBaseFont.PROPERTY_FONT_NAME)));
							int stringLength = combo.getText().length();
							combo.setSelection(new Point(stringLength, stringLength));
						}
						time = e.time;
					}
				});
				dataSetted = true;
			}
			if (b != null) combo.setText(b.toString());
		}
	}

	public void propertyChange(AbstractSection section, String property, String value) {
		section.changeProperty(property, value);
	}

	@Override
	protected void createComponent(Composite parent) {
		if (combo == null) {
			combo = new Combo(parent, SWT.NONE);
		}
	}

	@Override
	public Control getControl() {
		return combo;
	}

}
