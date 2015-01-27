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

import net.sf.jasperreports.charts.type.EdgeEnum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.combomenu.ComboItem;
import com.jaspersoft.studio.property.combomenu.ComboItemAction;
import com.jaspersoft.studio.property.combomenu.ComboMenuViewer;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * This class define a position selector as a popoup combo menu
 * @author Orlandin Marco
 *
 */
public class SPLegendAlignementEnum extends ASPropertyWidget{
	
	/**
	 * The combo popup
	 */
	ComboMenuViewer combo;

	public SPLegendAlignementEnum(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent,section,pDescriptor);
		createComponent(parent, section, pDescriptor.getId().toString());
		combo.setToolTipText(pDescriptor.getDescription());
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
		composite.setBackground(parent.getBackground());
		RowLayout layout = new RowLayout();
		layout.marginLeft = 0;
		composite.setLayout(layout);
		//Creating the list of entry
		List<ComboItem> itemsList = new ArrayList<ComboItem>();
		itemsList.add(new ComboItem(Messages.SPLegendAlignementEnum_default, true, ResourceManager.getImage(this.getClass(), "/icons/resources/blank-none.png"),0, NullEnum.UNDEFINED,  null)); //$NON-NLS-2$
		itemsList.add(new ComboItem(Messages.SPLegendAlignementEnum_top, true,  ResourceManager.getImage(this.getClass(), "/icons/resources/eclipse/align-edge-top.gif"),1, EdgeEnum.TOP,  new Integer(EdgeEnum.TOP.getValue()))); //$NON-NLS-2$
		itemsList.add(new ComboItem(Messages.SPLegendAlignementEnum_bottom, true, ResourceManager.getImage(this.getClass(), "/icons/resources/eclipse/align-edge-bottom.gif"),2, EdgeEnum.BOTTOM, new Integer(EdgeEnum.BOTTOM.getValue()))); //$NON-NLS-2$
		itemsList.add(new ComboItem(Messages.SPLegendAlignementEnum_left, true, ResourceManager.getImage(this.getClass(), "/icons/resources/eclipse/align-edge-left.gif"),3, EdgeEnum.LEFT, new Integer(EdgeEnum.LEFT.getValue()))); //$NON-NLS-2$
		itemsList.add(new ComboItem(Messages.SPLegendAlignementEnum_right, true, ResourceManager.getImage(this.getClass(), "/icons/resources/eclipse/align-edge-right.gif"),4, EdgeEnum.RIGHT, new Integer(EdgeEnum.RIGHT.getValue()))); //$NON-NLS-2$
		//Creating the combo popup
		combo = new ComboMenuViewer(composite, SWT.NORMAL, SPRWPopUpCombo.getLongest(itemsList));
		combo.setItems(itemsList);
		combo.addSelectionListener(new ComboItemAction() {
				/**
				 * The action to execute when an entry is selected
				 */
				@Override
				public void exec() {
						propertyChange(section, property, combo.getSelectionValue() != null ? (Integer)combo.getSelectionValue() : null);			
				}
		});
	}

	public void propertyChange(AbstractSection section, String property, Integer value) {
		section.changeProperty(property, value);
	}

	@Override
	protected void createComponent(Composite parent) {
	}

	@Override
	public void setData(APropertyNode pnode, Object value) {
		combo.select(value != null ? (Integer)value : 0);
	}

	@Override
	public Control getControl() {
		return combo != null ? combo.getControl() : null;
	}

}
