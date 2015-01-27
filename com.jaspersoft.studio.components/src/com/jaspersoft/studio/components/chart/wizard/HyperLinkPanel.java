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
package com.jaspersoft.studio.components.chart.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
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

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.swt.widgets.WTParametersText;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * 
 * This class can create a composite with inside the widgets to edit an MHyperLink node
 * 
 * @author Orlandin Marco
 *
 */
public class HyperLinkPanel {
	
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
	private WTextExpression anchorWidget;
	
	/**
	 * Widget of the reference field (only the field to insert the data, not the label with the name of the field)
	 */
	private WTextExpression referenceWidget;
	
	/**
	 * Widget of the parameters field (only the field to insert the data, not the label with the name of the field)
	 */
	private WTParametersText parametersWidget;
	
	/**
	 * Widget of the when field (only the field to insert the data, not the label with the name of the field)
	 */
	private WTextExpression whenWidget;
	
	/**
	 * Widget of the tooltip field (only the field to insert the data, not the label with the name of the field)
	 */
	private WTextExpression tooltipWidget;
	
	/**
	 * Widget of the page field (only the field to insert the data, not the label with the name of the field)
	 */
	private WTextExpression pageWidget;
	
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
	 * The MHyperlink node managed by the controls
	 */
	private APropertyNode hyperlinkNode;
	
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
	
	
	public HyperLinkPanel(APropertyNode mHyperlink){
		this.hyperlinkNode = mHyperlink;
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
	
	public APropertyNode getElement(){
		return hyperlinkNode;
	}

	/**
	 * Refresh the contents of the controls, setting into them the data of the selected element
	 */
	public void refresh() {
		APropertyNode element = getElement();
		if (element != null) {
			setExpressionComponentData(anchorWidget, element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION));
			setExpressionComponentData(referenceWidget, element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION));
			setExpressionComponentData(whenWidget, element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION));
			setExpressionComponentData(pageWidget, element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION));
			setExpressionComponentData(tooltipWidget, element, element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION));
			parametersWidget.updateData();
			Object propertyValue = element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_LINK_TARGET);
			targetCombo.setText(propertyValue != null ? propertyValue.toString() : linkTargetItems[0]);
			propertyValue = element.getPropertyActualValue(JRDesignHyperlink.PROPERTY_LINK_TYPE);
			String typeValue = propertyValue != null ? propertyValue.toString() : linkTypeItems[0];
			//I don't set the text on the combo if it has already the right value to avoid to raise the panel refresh
			if (!typeValue.equals(typeCombo.getText())) typeCombo.setText(typeValue);
		}
	}
	
	/**
	 * Create the hiders map
	 */
	private void createMap(){
		if (hideList == null){
			hideList = new HashMap<String, HyperLinkPanel.ElementHider[]>();
			hideList.put(linkTypeItems[0], new ElementHider[]{tooltip});
			hideList.put(linkTypeItems[1], new ElementHider[]{tooltip, parameters, reference});
			hideList.put(linkTypeItems[2], new ElementHider[]{tooltip, parameters, anchor});
			hideList.put(linkTypeItems[3], new ElementHider[]{tooltip, parameters, page});
			hideList.put(linkTypeItems[4], new ElementHider[]{tooltip, parameters, reference, anchor});
			hideList.put(linkTypeItems[5], new ElementHider[]{tooltip, parameters, reference, page});
			hideList.put(linkTypeItems[6], new ElementHider[]{tooltip, parameters, reference, anchor, page});
			hideList.put("Custom", new ElementHider[]{tooltip, parameters, reference, anchor, page});
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
		if (!hideList.containsKey(selectedValue)) selectedValue = "Custom";
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
	
	/**
	 * Create a label and set its content text and its tooltip text, then return it
	 * 
	 * @param parent the parent of the label
	 * @param toolTip the tooltip of the label
	 * @param text the text inside the label
	 * @return the created label
	 */
	private Label createLabel(Composite parent, String toolTip, String text){
		Label newLabel = new Label(parent, SWT.NONE);
		newLabel.setText(text);
		newLabel.setBackground(parent.getBackground());
		newLabel.setToolTipText(toolTip);
		return newLabel;
	}
	
	/**
	 * Set the help for the custom components (the two combos)
	 */
	private void setHelp(){
		String prefix = "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#";
		if (getElement() instanceof MImage){
			HelpSystem.setHelp(typeCombo, prefix+"image_hyperlinkType"); 
			HelpSystem.setHelp(targetCombo, prefix+"image_hyperlinkTarget");
		} else if (getElement() instanceof MTextField){
			HelpSystem.setHelp(typeCombo, prefix+"textField_hyperlinkType"); 
			HelpSystem.setHelp(targetCombo, prefix+"textField_hyperlinkTarget");
		} else {
			HelpSystem.setHelp(typeCombo, prefix+"sectionHyperlink_hyperlinkType"); 
			HelpSystem.setHelp(targetCombo, prefix+"sectionHyperlink_hyperlinkTarget");
		}
	}
	
	/**
	 * Create a WTextExpression component and set the graphical appearance. This component
	 * can be used to easily input an expression
	 *  
	 * @param parent parent of the component
	 * @return the created WTextExpression component
	 */
	protected WTextExpression createExpressionComponent(Composite parent) {
		WTextExpression expr = new WTextExpression(parent, SWT.NONE, 1);
		expr.setBackground(parent.getBackground());
		if (parent.getLayout() instanceof GridLayout) {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 100;
			expr.setLayoutData(gd);
		}
		return expr;
	}
	
	/**
	 * Set the expression inside a WTextExpression component 
	 * 
	 * @param component the component
	 * @param pnode Node from where the expression context will be extracted, should be also the node that will 
	 * contains the expression
	 * @param b the JRDesignExpression expression to set inside the WTextExpression component
	 */
	private void setExpressionComponentData(WTextExpression component, APropertyNode pnode, Object b) {
		component.setExpression((JRDesignExpression) b);
		JRDesignElement designEl = null;
		if (pnode.getValue() instanceof JRDesignElement) {
			designEl = (JRDesignElement) pnode.getValue();
		}
		// Try to get an expression context for the node if any
		Object expContextAdapter = pnode.getAdapter(ExpressionContext.class);
		if(expContextAdapter!=null){
			component.setExpressionContext((ExpressionContext)expContextAdapter);
		}
		else{
			component.setExpressionContext(ModelUtils.getElementExpressionContext(designEl, pnode));
		}
	}
	
	/**
	 * Set an expression property of the element with the expression extracted from a WTextExpression widget
	 * 
	 * @param propertyId the id of the expression property inside the element
	 * @param expressionWidget the widget from where the expression value is extracted
	 */
	private void setExpressionProperty(Object propertyId, WTextExpression expressionWidget){
		JRDesignExpression exp = expressionWidget.getExpression();
		getElement().setPropertyValue(propertyId, exp != null ? exp.clone() : null);
	}
	
	/**
	 * Update the values inside the MHyperlink property node with the value defined inside the expression widgets. 
	 * The parameters are not updated here since the are update on the fly when the appropriate widget is used
	 */
	public void setAllExpressionValues(){
		setExpressionProperty(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION, anchorWidget);
		setExpressionProperty(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION, pageWidget);
		setExpressionProperty(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION, referenceWidget);
		setExpressionProperty(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION, whenWidget);
		setExpressionProperty(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION, tooltipWidget);
	}

	/**
	 * Create all the controls inside the specified parent
	 * 
	 * @param parent parent where all the controls will be created
	 */
	public void createControls(Composite parent) {
		
		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setBackground(parent.getBackground());
		GridData mainCompData = new GridData(GridData.FILL_BOTH);
		mainCompData.widthHint = 600;
		mainCompData.minimumHeight = 250;
		mainComposite.setLayoutData(mainCompData);
		mainComposite.setLayout(new GridLayout(3, false));
		
		createLabel(mainComposite, Messages.MHyperLink_link_target_description, Messages.MHyperLink_link_target);
		targetCombo = new Combo(mainComposite, SWT.NONE);
		targetCombo.setLayoutData(gridDataGenerator()); 
		targetCombo.setItems(linkTargetItems);
		
	
		createLabel(mainComposite, Messages.MHyperLink_link_type_description, Messages.MHyperLink_link_type);
		typeCombo = new Combo(mainComposite, SWT.NONE);
		typeCombo.setLayoutData(gridDataGenerator()); 
		typeCombo.setItems(linkTypeItems);
		
		Label anchorLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_anchor_expression_description, Messages.MHyperLink_hyperlink_anchor_expression);
		anchorWidget = createExpressionComponent(mainComposite);
		anchorWidget.setLayoutData(gridDataGenerator());
		anchor = new ElementHider(new Control[]{anchorLabel, anchorWidget});
		
		Label pageLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_page_expression_description, Messages.MHyperLink_hyperlink_page_expression);
		pageWidget = createExpressionComponent(mainComposite);  
		pageWidget.setLayoutData(gridDataGenerator());		
		page = new ElementHider(new Control[]{pageLabel, pageWidget});
		
		Label referenceLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_reference_expression_description, Messages.MHyperLink_hyperlink_reference_expression);
		referenceWidget = createExpressionComponent(mainComposite); 
		referenceWidget.setLayoutData(gridDataGenerator());		
		reference = new ElementHider(new Control[]{referenceLabel, referenceWidget});
		
		createLabel(mainComposite, Messages.MHyperLink_whenexpr_desc, Messages.MHyperLink_whenexpr);
		whenWidget = createExpressionComponent(mainComposite); 
		whenWidget.setLayoutData(gridDataGenerator());		
		
		Label tooltipLabel = createLabel(mainComposite, Messages.MHyperLink_hyperlink_tooltip_expression_description, Messages.MHyperLink_hyperlink_tooltip_expression);
		tooltipWidget = createExpressionComponent(mainComposite); 
		tooltipWidget.setLayoutData(gridDataGenerator());	
		tooltip = new ElementHider(new Control[]{tooltipLabel, tooltipWidget});
		
		Label parametersLabel = createLabel(mainComposite, Messages.MHyperLink_parameters_description, Messages.common_parameters);
		parametersWidget = new WTParametersText(mainComposite,JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, getElement()); 
		parametersWidget.setBackground(mainComposite.getBackground());
		parametersWidget.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		parametersWidget.setSelectionAdapter(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				parametersWidget.updateData();
			}
		});
		Control button = parametersWidget.getButton();
		parameters = new ElementHider(new Control[]{parametersLabel, parametersWidget, button});
		
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
		refresh();
	}
	
}
