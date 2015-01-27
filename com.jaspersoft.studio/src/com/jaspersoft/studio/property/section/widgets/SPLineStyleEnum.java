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

import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.LineStyleEnum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.property.combomenu.ComboItem;
import com.jaspersoft.studio.property.combomenu.ComboItemAction;
import com.jaspersoft.studio.property.combomenu.ComboMenuViewer;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * Reperesent the lineStyle of an element as a combo popup element.
 * @author Orlandin Marco
 *
 */
public class SPLineStyleEnum {
	
	/**
	 * The combo popup
	 */
	ComboMenuViewer combo;

	public SPLineStyleEnum(Composite parent, AbstractSection section, String property) {
		createComponent(parent, section, property);
	}
	
	/**
	 * Return the selected value into the combo popup
	 * @return In this case it's an integer representing the index of the selected item
	 */
	public Object getSelectedValue(){
		return combo.getSelectionValue();
	}

	/**
	 * Create the component and initialize the combo popup with the necessary value
	 * @param parent composite where the combo popup is palced
	 * @param section section that contains the property that this combo will change when an entry is selected
	 * @param property id of the property that this combo will change when an entry is selected
	 */
	public void createComponent(Composite parent, final AbstractSection section, final String property) {
		Composite composite = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.marginLeft = 0;
		composite.setLayout(layout);
		//Creating the list of entry
		List<ComboItem> itemsList = new ArrayList<ComboItem>();
		//A blank space is added at the end of the string to compensate the size enlargement because a selected element is in bold
		itemsList.add(new ComboItem("Inherited ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/inherited.png"),0, NullEnum.INHERITED, null));
		itemsList.add(new ComboItem("Solid line ", true, ResourceManager.getImage(this.getClass(), "/icons/resources/line-solid.png"),1, LineStyleEnum.SOLID, new Integer(LineStyleEnum.SOLID.getValue() + 1)));
		itemsList.add(new ComboItem("Dashed line ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/line-dashed.png"),2, LineStyleEnum.DASHED, new Integer(LineStyleEnum.DASHED.getValue() + 1)));
		itemsList.add(new ComboItem("Dotted line ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/line-dotted.png"),3, LineStyleEnum.DOTTED, new Integer(LineStyleEnum.DOTTED.getValue() + 1)));
		itemsList.add(new ComboItem("Double line ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/line-double.png"),4, LineStyleEnum.DOUBLE, new Integer(LineStyleEnum.DOUBLE.getValue() + 1)));
		//Creating the combo popup
		combo = new ComboMenuViewer(composite, SWT.NORMAL, SPRWPopUpCombo.getLongest(itemsList));
		combo.setItems(itemsList);
		combo.addSelectionListener(new ComboItemAction() {
				/**
				 * The action to execute when an entry is selected
				 */
				@Override
				public void exec() {
						propertyChange(section,JRBasePen.PROPERTY_LINE_STYLE, combo.getSelectionValue() != null ? (Integer)combo.getSelectionValue() : null);			
				}
		});
	}
	
	/**
	 * Set the contextual help for the control
     * @param href uri to open when the help is requested
	 */
	public void setHelp(String href){
		combo.setHelp(href);
	}

	public void propertyChange(AbstractSection section, String property, Integer value) {
		section.changeProperty(property, value);
	}

	/**
	 * Set the selected index of the popup combo
	 * @param b index
	 */
	public void setData(Integer b) {
		combo.select(b);
	}
	
	public Control getControl(){
		return combo.getControl();
	}
}
