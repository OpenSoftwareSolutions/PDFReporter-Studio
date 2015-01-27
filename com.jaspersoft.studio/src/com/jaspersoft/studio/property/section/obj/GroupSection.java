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
package com.jaspersoft.studio.property.section.obj;

import java.text.MessageFormat;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.MBandGroupHeader;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

public class GroupSection extends AbstractSection {
	private ASPropertyWidget nameWidget;

	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, Messages.GroupSection_SectionTitle, false, 2);

		nameWidget = createWidget4Property(parent, JRDesignGroup.PROPERTY_NAME);

		createWidget4Property(parent, JRDesignGroup.PROPERTY_EXPRESSION);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignGroup.PROPERTY_NAME, Messages.common_name);
		addProvidedProperties(JRDesignGroup.PROPERTY_EXPRESSION, Messages.common_expression);
	}

	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MBandGroupHeader)
			return ((MBandGroupHeader) md).getMGroup();
		if (md instanceof MBandGroupFooter)
			return ((MBandGroupFooter) md).getMGroup();
		return md;
	}
	
	/**
	 * Check if the property changed is the name and in this case check that the 
	 * new name is different from any existing group. If it is different the change
	 * is done, otherwise a warning message is shown and the original name is 
	 * restored
	 */
	@Override
	public boolean changeProperty(Object property, Object newValue) {
		if (JRDesignGroup.PROPERTY_NAME.equals(property)){
			JasperDesign jd = getElement().getJasperConfiguration().getJasperDesign();
			String oldName = getElement().getPropertyValue(JRDesignGroup.PROPERTY_NAME).toString();
			//If the new name is equals to the actual one the there is no need to change
			if (oldName.equals(newValue)) return true;
			if (jd != null && jd.getGroupsMap().get(newValue) != null) {
				nameWidget.setData(getElement(), oldName);
				String message = MessageFormat.format(Messages.GroupSection_SameNameErrorMsg, new Object[] { newValue });
				MessageDialog dialog = new MessageDialog(UIUtils.getShell(), Messages.GroupSection_SameNameErrorTitle, null,
						message, MessageDialog.WARNING, new String[] { "Ok"}, 0); //$NON-NLS-1$
				dialog.open();
				return false;
			}
		}
		return super.changeProperty(property, newValue);
	}

}
