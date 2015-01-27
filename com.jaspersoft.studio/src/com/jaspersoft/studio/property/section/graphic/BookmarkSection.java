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
package com.jaspersoft.studio.property.section.graphic;

import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignTextField;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

/**
 * This class provide the controls to define the the anchor name expression
 * and the bookmark level of and element that support it
 * 
 * 
 * @author Orlandin Marco
 *
 */
public class BookmarkSection extends AbstractSection {
	
	private ExpandableComposite section;
	
	/**
	 * Return the key of the property that define the anchor name. The property can
	 * have multiple value depending to the type of the selected element
	 * 
	 * @return key of the property that define the anchor name, null if there isn't an anchor property for the element
	 */
	public String getAnchorNameProperty(){
		if (getElement() instanceof MImage) return JRDesignImage.PROPERTY_ANCHOR_NAME_EXPRESSION;
		else return JRDesignTextField.PROPERTY_ANCHOR_NAME_EXPRESSION;
	}
	
	/**
	 * Return the key of the property that define the bookmark level. The property can
	 * have multiple value depending to the type of the selected element
	 * 
	 * @return key of the property that define the bookmark level
	 */
	public String getBookmarkLevelProperty(){
		if (getElement() instanceof MImage) return JRDesignImage.PROPERTY_BOOKMARK_LEVEL;
		else return JRDesignTextField.PROPERTY_BOOKMARK_LEVEL;
	}
	
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		String anchorNameProperty = getAnchorNameProperty();
		String bookmarkLevelProperty = getBookmarkLevelProperty();
		if (bookmarkLevelProperty != null || anchorNameProperty != null){
			parent = getWidgetFactory().createSection(parent, Messages.BookmarkSection_bookmarkSectionTitle, true, 2);
			section = (ExpandableComposite)parent.getParent();
			parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			if (anchorNameProperty != null) {
				ASPropertyWidget exp = createWidget4Property(parent, anchorNameProperty, true);  
				exp.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}
			if (bookmarkLevelProperty != null){
				createWidget4Property(parent,bookmarkLevelProperty, true);  
				section.setExpanded(false);
			}
		}
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignTextField.PROPERTY_ANCHOR_NAME_EXPRESSION, Messages.MTextField_anchorNameLabel);
		addProvidedProperties(JRDesignTextField.PROPERTY_BOOKMARK_LEVEL, Messages.MTextField_bookmarkLevelLabel);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) {
			section.setExpanded(true);
		}
	}
}
