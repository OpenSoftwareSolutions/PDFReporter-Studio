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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.properties.internal.IHighlightPropertyWidget;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.graphic.ASHighlightControl;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.BackgroundHighlight;
import com.jaspersoft.studio.property.section.widgets.SPParameter;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * This class paint the controls for the hyperlink section
 * 
 * @author Orlandin Marco
 *
 */
public class HyperlinkSection extends AbstractSection {
	
	/**
	 * Element hider for the elements related to the anchor, so the label an the expression field\button
	 */
	private ElementHider anchor;
	
	/**
	 * Element hider for the elements related to the page, so the label an the expression field\button
	 */
	private ElementHider page;
	
	/**
	 * Element hider for the elements related to the reference, so the label an the expression field\button
	 */
	private ElementHider reference;
	
	/**
	 * Element hider for the elements related to the when field, so the label an the expression field\button
	 * Now not used since the when area is always visible
	 */
	@SuppressWarnings("unused")
	private ElementHider when;

	/**
	 * Element hider for the elements related to the tooltip, so the label an the expression field\button
	 */
	private ElementHider tooltip;
	
	/**
	 * Element hider for the elements related to the parameters, so the label an the expression field\button
	 */
	private ElementHider parameters;
	
	/**
	 * Widget of the anchor field (only the field to insert the data, not the label with the name of the field)
	 */
	private ASPropertyWidget anchorWidget;
	
	/**
	 * Widget of the reference field (only the field to insert the data, not the label with the name of the field)
	 */
	private ASPropertyWidget referenceWidget;
	
	/**
	 * Widget of the parameters field (only the field to insert the data, not the label with the name of the field)
	 */
	private ASPropertyWidget parametersWidget;
	
	/**
	 * Widget of the when field (only the field to insert the data, not the label with the name of the field)
	 */
	private ASPropertyWidget whenWidget;
	
	/**
	 * Widget of the tooltip field (only the field to insert the data, not the label with the name of the field)
	 */
	private ASPropertyWidget tooltipWidget;
	
	/**
	 * Widget of the page field (only the field to insert the data, not the label with the name of the field)
	 */
	private ASPropertyWidget pageWidget;
	
	/**
	 * Control of the target selection field (only the field to insert the data, not the label with the name of the field)
	 */
	private Combo targetCombo;
	
	/**
	 * Control of the type selection field (only the field to insert the data, not the label with the name of the field)
	 */
	private Combo typeCombo;
	
	/**
	 * composite where all the elements are placed
	 */
	private Composite mainComposite;
	
	/**
	 * Expandable composite where the control are placed
	 */
	private ExpandableComposite section;
	
	/**
	 * Hashmap that contains, for every hyperlink type, a list of hiders. Every one of these hider contains one or 
	 * more controls that should be visible with the type.
	 * 
	 */
	private HashMap<String, ElementHider[]> hideList = null;
	
	/**
	 * Array with the predefined target
	 */
	private static String[] linkTargetItems=new String[]{
		HyperlinkTargetEnum.SELF.getName(),
		HyperlinkTargetEnum.BLANK.getName(),
		HyperlinkTargetEnum.TOP.getName(),
		HyperlinkTargetEnum.PARENT.getName(),};
	
	/**
	 * Array that will contains the available link type values
	 */
	private static String[] linkTypeItems;
	
	/**
	 * Initialize the link type array
	 */
	static {
		ArrayList<HyperlinkTypeEnum> filteredTypes = new ArrayList<HyperlinkTypeEnum>(2);
		filteredTypes.add(HyperlinkTypeEnum.CUSTOM);	// Will be used automatically when user write a custom entry
		filteredTypes.add(HyperlinkTypeEnum.NULL);		// Makes no much sense into this widget
		List<String> alltypes=ModelUtils.getHyperlinkTypeNames4Widget(filteredTypes);		
		linkTypeItems=alltypes.toArray(new String[alltypes.size()]);
		
	}
	
	/**
	 * This class can contains an array of controls and provide the methods to easily show 
	 * or hide that controls. The controls must be inside a grid layout
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class ElementHider {
		
		/**
		 * The array of controls
		 */
		private Control[] controls;
		
		/**
		 * 
		 * @param controls the controls to show or hide, they must be into a grid layout. Since 
		 * the grid data is needed, if the control has not a grid data then a default one will be
		 * assigned
		 */
		public ElementHider(Control[] controls){
			this.controls = controls;
		}
		
		/**
		 * Set the visibility of the stored control
		 * @param visible true if the control should be visible, false otherwise
		 */
		public void setVisibility(boolean visible){
			for(Control control : controls){
				if (!control.isDisposed()) {
					if (control.getLayoutData() == null) control.setLayoutData(new GridData());
					GridData layout = (GridData)control.getLayoutData();
					layout.exclude = !visible;
					control.setVisible(visible);
				}
			}
		}
		
		/**
		 * Show all the stored controls
		 */
		public void showAll(){
			setVisibility(true);
		}
		
		/**
		 * Hide all the stored controls
		 */
		public void hideAll(){
			setVisibility(false);
		}
		
	}
	
	/**
	 * Generate a standard grid data, useful for most of the control
	 * @return a grid data with an horizontal span of two, and an horizontal filling.
	 * At every call it is returned a new instance of the grid data
	 */
	private GridData gridDataGenerator(){
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		return gd;
	}

	/**
	 * Refresh the contents of the controls, setting into them the data of the selected element
	 */
	@Override
	public void refresh() {
		setRefreshing(true);
		APropertyNode element = getElement();
		if (element != null) {
			anchorWidget.setData(element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION));
			referenceWidget.setData(element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION));
			whenWidget.setData(element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION));
			pageWidget.setData(element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION));
			tooltipWidget.setData(element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION));
			parametersWidget.setData(element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS));
			Object propertyValue = element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_LINK_TARGET);
			targetCombo.setText(propertyValue != null ? propertyValue.toString() : linkTargetItems[0]);
			propertyValue = element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_LINK_TYPE);
			String typeValue = propertyValue != null ? propertyValue.toString() : linkTypeItems[0];
			//I don't set the text on the combo if it has already the right value to avoid to raise the panel refresh
			if (!typeValue.equals(typeCombo.getText())) typeCombo.setText(typeValue);	
		}
		setRefreshing(false);
	}
	
	/**
	 * Create the hiders map
	 */
	private void createMap(){
		if (hideList == null){
			hideList = new HashMap<String, HyperlinkSection.ElementHider[]>();
			hideList.put(linkTypeItems[0], new ElementHider[]{tooltip}); // HyperlinkTypeEnum.NONE
			hideList.put(linkTypeItems[1], new ElementHider[]{tooltip, parameters, reference}); // HyperlinkTypeEnum.REFERENCE
			hideList.put(linkTypeItems[2], new ElementHider[]{tooltip, parameters, anchor}); // HyperlinkTypeEnum.LOCAL_ANCHOR
			hideList.put(linkTypeItems[3], new ElementHider[]{tooltip, parameters, page}); // HyperlinkTypeEnum.LOCAL_PAGE
			hideList.put(linkTypeItems[4], new ElementHider[]{tooltip, parameters, reference, anchor}); // HyperlinkTypeEnum.REMOTE_ANCHOR
			hideList.put(linkTypeItems[5], new ElementHider[]{tooltip, parameters, reference, page}); // HyperlinkTypeEnum.REMOTE_PAGE
			for(int i=6;i<linkTypeItems.length;i++) {
				// the contributed ones...
				hideList.put(linkTypeItems[i], new ElementHider[]{tooltip, parameters, reference, anchor, page});
			}
			hideList.put("Custom", new ElementHider[]{tooltip, parameters, reference, anchor, page}); //$NON-NLS-1$
		}
	}
	
	/**
	 * Show or hide the visible controls using the  the link type actual selection to choose which show
	 */
	private void refreshVisibleComponents(){
		ElementHider[] hiders = new ElementHider[]{anchor, page, reference, tooltip, parameters};
		for(ElementHider hider : hiders)
			hider.hideAll();
		String selectedValue = typeCombo.getText();
		if (!hideList.containsKey(selectedValue)) selectedValue = "Custom"; //$NON-NLS-1$
		ElementHider[] actualHiders = hideList.get(selectedValue);
		for(ElementHider hider : actualHiders)
			hider.showAll();
		mainComposite.layout();
	}
	
	/**
	 * Read the textual value of a combo and set it into a property inside the element
	 * 
	 * @param combo combo from where the value is read
	 * @param property name of the property to set
	 */
	private void readValueFromCombo(Combo combo, String property){
		APropertyNode element = getElement();
		if (element != null) {
			element.setPropertyValue(property, combo.getText());
			int stringLength = combo.getText ().length (); 
			//Since it is called even on the combo modify it will move the cursor on the start at every call
			//causing that every character typed reset the cursor on the beginning. This will put the cursor on the end
			combo.setSelection(new Point (stringLength, stringLength));
		}
	}
	
	private Label createLabel(Composite parent, String toolTip, String text){
		Label newLabel = new Label(parent, SWT.NONE);
		newLabel.setText(text);
		newLabel.setToolTipText(toolTip);
		return newLabel;
	}
	
	/**
	 * Set the help for the custom components (the two combos)
	 */
	private void setHelp(){
		String prefix = "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#"; //$NON-NLS-1$
		if (getElement() instanceof MImage){
			HelpSystem.setHelp(typeCombo, prefix+"image_hyperlinkType");  //$NON-NLS-1$
			HelpSystem.setHelp(targetCombo, prefix+"image_hyperlinkTarget"); //$NON-NLS-1$
		} else if (getElement() instanceof MTextField){
			HelpSystem.setHelp(typeCombo, prefix+"textField_hyperlinkType");  //$NON-NLS-1$
			HelpSystem.setHelp(targetCombo, prefix+"textField_hyperlinkTarget"); //$NON-NLS-1$
		} else {
			HelpSystem.setHelp(typeCombo, prefix+"sectionHyperlink_hyperlinkType");  //$NON-NLS-1$
			HelpSystem.setHelp(targetCombo, prefix+"sectionHyperlink_hyperlinkTarget"); //$NON-NLS-1$
		}
	}
	
	/**
	 * Create all the controls
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		
		mainComposite = getWidgetFactory().createSection(parent, Messages.HyperlinkSection_hyperlinkSectionTitle, true, 3);
		section = (ExpandableComposite)mainComposite.getParent();
		section.setExpanded(false);
		mainComposite.setLayout(new GridLayout(3, false));
		GridData parentData = new GridData(SWT.FILL,SWT.FILL, true, true);
		//the composite must have a fixed height because for some reason, when the components are hided it 
		//is resized to a smaller dimension, but when components are shown it is not resized to a bigger one
		parentData.minimumHeight = 280;
		parentData.heightHint = 280;
		section.setLayoutData(parentData);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		createLabel(mainComposite, Messages.MHyperLink_link_target_description, Messages.MHyperLink_link_target);
		targetCombo = new Combo(mainComposite, SWT.NONE);
		targetCombo.setLayoutData(gridDataGenerator()); 
		targetCombo.setItems(linkTargetItems);
		
		createLabel(mainComposite, Messages.MHyperLink_link_type_description, Messages.MHyperLink_link_type);
		typeCombo = new Combo(mainComposite, SWT.NONE);
		typeCombo.setLayoutData(gridDataGenerator()); 
		typeCombo.setItems(linkTypeItems);
		
		Label anchorLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_anchor_expression_description, Messages.MHyperLink_hyperlink_anchor_expression);
		anchorWidget = createWidget4Property(mainComposite, JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION, false);
		anchorWidget.getControl().setLayoutData(gridDataGenerator());
		anchor = new ElementHider(new Control[]{anchorLabel, anchorWidget.getControl()});
		
		Label pageLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_page_expression_description, Messages.MHyperLink_hyperlink_page_expression);
		pageWidget = createWidget4Property(mainComposite,JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION,false);  
		pageWidget.getControl().setLayoutData(gridDataGenerator());		
		page = new ElementHider(new Control[]{pageLabel, pageWidget.getControl()});
		
		Label referenceLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_reference_expression_description, Messages.MHyperLink_hyperlink_reference_expression);
		referenceWidget = createWidget4Property(mainComposite,JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION,false); 
		referenceWidget.getControl().setLayoutData(gridDataGenerator());		
		reference = new ElementHider(new Control[]{referenceLabel, referenceWidget.getControl()});
		
		Label whenLabel = createLabel(mainComposite, Messages.MHyperLink_whenexpr_desc, Messages.MHyperLink_whenexpr);
		whenWidget = createWidget4Property(mainComposite,JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION,false); 
		whenWidget.getControl().setLayoutData(gridDataGenerator());		
		when = new ElementHider(new Control[]{whenLabel, whenWidget.getControl()});
		
		Label tooltipLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_tooltip_expression_description, Messages.MHyperLink_hyperlink_tooltip_expression);
		tooltipWidget = createWidget4Property(mainComposite,JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION,false); 
		tooltipWidget.getControl().setLayoutData(gridDataGenerator());	
		tooltip = new ElementHider(new Control[]{tooltipLabel, tooltipWidget.getControl()});
		
		Label parametersLabel = createLabel(mainComposite, Messages.MHyperLink_parameters_description, Messages.common_parameters);
		parametersWidget = createWidget4Property(mainComposite,JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS,false); 
		Control button = ((SPParameter)parametersWidget).getButton();
		parameters = new ElementHider(new Control[]{parametersLabel, parametersWidget.getControl(), button});
		
		createMap();
		setHelp();
		
		targetCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				readValueFromCombo(targetCombo, JRDesignHyperlink.PROPERTY_LINK_TARGET);
			}
		});
		targetCombo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				readValueFromCombo(targetCombo, JRDesignHyperlink.PROPERTY_LINK_TARGET);
			}
		});
		typeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					refreshVisibleComponents();
					readValueFromCombo(typeCombo, JRDesignHyperlink.PROPERTY_LINK_TYPE);
			}
		});
		typeCombo.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				refreshVisibleComponents();
				readValueFromCombo(typeCombo, JRDesignHyperlink.PROPERTY_LINK_TYPE);
			}
		});
	}
	
	@Override
	public IHighlightPropertyWidget getWidgetForProperty(Object propertyId) {
		if (JRDesignHyperlink.PROPERTY_LINK_TARGET.equals(propertyId)){
			return new ASHighlightControl(targetCombo, new BackgroundHighlight(targetCombo));
		} else if (JRDesignHyperlink.PROPERTY_LINK_TYPE.equals(propertyId)){
			return new ASHighlightControl(typeCombo, new BackgroundHighlight(typeCombo));
		} else return super.getWidgetForProperty(propertyId);
	}
	
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignHyperlink.PROPERTY_LINK_TARGET, Messages.MHyperLink_link_target);
		addProvidedProperties(JRDesignHyperlink.PROPERTY_LINK_TYPE, Messages.MHyperLink_link_type);
		addProvidedProperties(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION, Messages.MHyperLink_hyperlink_anchor_expression);
		addProvidedProperties(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION, Messages.MHyperLink_hyperlink_page_expression);
		addProvidedProperties(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION, Messages.MHyperLink_hyperlink_reference_expression);
		addProvidedProperties(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION, Messages.MHyperLink_whenexpr);
		addProvidedProperties(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION, Messages.MHyperLink_hyperlink_tooltip_expression);
		addProvidedProperties(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, Messages.common_parameters);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) {
			section.setExpanded(true);
		}
	}
	
}
