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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.type.JREnum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.Misc;

/**
 * Can offer a generic combo to select an evaluation, reset, increment time for a components handling the presence of
 * groups. Can also provide an error message when a previously selected group is not found. The list of groups is read
 * directly by the property descriptor.
 * 
 * @author Orlandin Marco
 * 
 */
public abstract class SPGroupTypeCombo extends ASPropertyWidget {

	/**
	 * Prefix string for the group elements
	 */
	protected static final String GROUPPREFIX = "[Group] "; //$NON-NLS-1$

	/**
	 * Combo where the elements are shown
	 */
	protected Combo combo;

	/**
	 * Boolean flag to know if the first element is a missing group error message
	 */
	protected boolean hasFirstFakeElement = false;

	/**
	 * The property descriptor of the group
	 */
	protected IPropertyDescriptor gDescriptor;

	public SPGroupTypeCombo(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor,
			IPropertyDescriptor gDescriptor) {
		super(parent, section, pDescriptor);
		this.gDescriptor = gDescriptor;
	}

	@Override
	public Control getControl() {
		return combo;
	}

	public void createComponent(Composite parent) {
		combo = section.getWidgetFactory().createCombo(parent, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				// If the selected entry is the first and it is fake then don't do anything
				if (hasFirstFakeElement && combo.getSelectionIndex() == 0)
					return;
				String group = null;
				Integer et = new Integer(1);

				String str = combo.getItem(combo.getSelectionIndex());
				if (str.startsWith(GROUPPREFIX)) {
					group = str.substring(GROUPPREFIX.length());
					et = EnumHelper.getValue(getGroupEnum(), 1, false);
				} else {
					et = EnumHelper.getValue(getByName(str), 1, false);
				}
				// It is important to set first the group because the group changing dosen't trigger an event
				// so otherwise setting the type first trigger the event but the group has not been set to the
				// setData method dosen't find the group and set always the element 0.
				section.changeProperty(gDescriptor.getId(), Misc.nvl(group));
				section.changeProperty(pDescriptor.getId(), et);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		combo.setToolTipText(pDescriptor.getDescription());
	}

	@Override
	public void setData(APropertyNode pnode, Object value) {
		setData((Integer) pnode.getPropertyValue(pDescriptor.getId()),
				(String) pnode.getPropertyValue(gDescriptor.getId()), getItems());
	}

	public void setData(Integer et, String group, String[] items) {
		hasFirstFakeElement = false;
		int selection = 0;
		JREnum sel = EnumHelper.getSetValue(getEnumValues(), et, 1, false);
		boolean found = false;
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(sel.getName())) {
				selection = i;
				found = true;
				break;
			}
			if (items[i].startsWith(GROUPPREFIX) && sel.equals(getGroupEnum())) {
				if (items[i].substring(GROUPPREFIX.length()).equals(group)) {
					selection = i;
					found = true;
					break;
				}
			}
		}
		String[] comboItems = items;
		// If the group can't be found set a fake item
		if (!found && group != null && !group.isEmpty()) {
			List<String> newItems = new ArrayList<String>();
			newItems.add(MessageFormat.format(Messages.SPGroupTypeCombo_groupNotFounError, group));
			newItems.addAll(new ArrayList<String>(Arrays.asList(items)));
			comboItems = newItems.toArray(new String[newItems.size()]);
			hasFirstFakeElement = true;
		}
		combo.setItems(comboItems);
		combo.select(selection);
	}

	/**
	 * Return the items selectable on the combo. Essentially they are the elements in the enumeration. but if in the
	 * enumeration is there a value for the group elements then also the available groups will be added to the items. The
	 * available groups are read from the property descriptor
	 * 
	 * @return
	 */
	protected String[] getItems() {
		List<String> lsIncs = new ArrayList<String>();
		for (JREnum en : getEnumValues()) {
			if (en.equals(getGroupEnum())) {
				if (gDescriptor instanceof RWComboBoxPropertyDescriptor) {
					String[] groupItems = ((RWComboBoxPropertyDescriptor) gDescriptor).getItems();
					for (String gr : groupItems)
						lsIncs.add(GROUPPREFIX + gr);
				} else if (gDescriptor instanceof RComboBoxPropertyDescriptor) {
					String[] groupItems = ((RComboBoxPropertyDescriptor) gDescriptor).getItems();
					for (String gr : groupItems)
						lsIncs.add(GROUPPREFIX + gr);
				}
			} else {
				lsIncs.add(en.getName());
			}
		}
		return lsIncs.toArray(new String[lsIncs.size()]);
	}

	/**
	 * Return the enumerations selectable on this element
	 * 
	 * @return the list of enumerations that can be selected
	 */
	protected abstract JREnum[] getEnumValues();

	/**
	 * Return the group enumeration for the element
	 * 
	 * @return the enumeration corresponding to group
	 */
	protected abstract JREnum getGroupEnum();

	/**
	 * Return the enumeration value starting from it's name
	 * 
	 * @param name
	 *          string representation of the enumeration value
	 * @return the enumeration value
	 */
	protected abstract JREnum getByName(String name);

}
