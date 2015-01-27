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
package com.jaspersoft.studio.components.crosstab.model.crosstab.command.wizard;

import java.awt.Color;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.dialog.CrosstabStyle;
import com.jaspersoft.studio.components.crosstab.model.dialog.CrosstabStylePreview;
import com.jaspersoft.studio.components.widgets.ColorSelectionWidget;
import com.jaspersoft.studio.components.widgets.ColorSelectionWidget.ColorInput;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.property.color.ColorSchemaGenerator;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

public class CrosstabWizardLayoutPage extends JSSHelpWizardPage {
	private MCrosstab crosstab;
	private boolean isAddRowTotal = true;
	private boolean isAddColTotal = true;
	
	/**
	 * Checkbox for the grid of color white
	 */
	private Button whiteGrid;
	
	
	/**
	 * Table preview widget
	 */
	private CrosstabStylePreview preview;
	
	/**
	 * Last style generated
	 */
	private CrosstabStyle lastGeneratedStyle = null;
	
	/**
	 * Checkbox to show or hide the grid
	 */
	private Button showGrid;
	
	/**
	 * the textfield of the title
	 */
	private Text titleText = null;
	
	/**
	 * True if also the title area is created, false otherwise
	 */
	private boolean createTitle;
	
	/**
	 * Template used to initialize the data of the dialog with the 
	 * same of the template, useful for edit
	 */
	private TemplateStyle templateToOpen;
	
	/**
	 * String used as id for the button that represent the color used in the total cells
	 */
	private static final String totalColor="TOTAL_COLOR"; //$NON-NLS-1$
	
	/**
	 * String used as id for the button that represent the color used in the group (subtotal) cells
	 */
	private static final String groupColor="GROUP_COLOR"; //$NON-NLS-1$
	
	/**
	 * String used as id for the button that represent the color used in the measures cells
	 */
	private static final String measuresColor="MEASURES_COLOR"; //$NON-NLS-1$
	
	/**
	 * String used as id for the button that represent the color used in the detail cells
	 */
	private static final String detailColor = "DETAIL_COLOR"; //$NON-NLS-1$
	
	/**
	 * Widget used to handle the color selection for the cells
	 */
	private ColorSelectionWidget selectionWidget;
	
	/**
	 * Listener called when a control is modified, cause the regeneration of the 
	 * lastGeneratedStyle and the update of the preview
	 */
	private ModifyListener modifyListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			notifyChange();	
		}
	};
	
	/**
	 * Listener called when a control get a selection event, cause the regeneration of the 
	 * lastGeneratedStyle and the update of the preview
	 */
	private SelectionAdapter selectionListener = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			notifyChange();
		}
	};
	

	protected CrosstabWizardLayoutPage(boolean createTitle) {
		super("crosstablayoutpage");   //$NON-NLS-1$
		setTitle(Messages.CrosstabWizardLayoutPage_layout);
		setImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/wizard_preview.png")); //$NON-NLS-1$
		setDescription(Messages.CrosstabWizardLayoutPage_description);
		this.createTitle = createTitle;
		this.templateToOpen = null;
	}
	
	protected CrosstabWizardLayoutPage() {
		this(false);
	}
	
	public boolean isAddRowTotal() {
		return isAddRowTotal;
	}

	public boolean isAddColTotal() {
		return isAddColTotal;
	}

	public void setCrosstab(MCrosstab crosstab) {
		this.crosstab = crosstab;
	}

	public MCrosstab getCrosstab() {
		return crosstab;
	}
	
	/**
	 * When the colors are selected using the schema\variation input method this method is called to 
	 * update the every button that represent a color with the appropriate color
	 */
	private void updateSelectedColor(){
		String colorName = selectionWidget.getSchemaSelected();
		Color color = ColorSchemaGenerator.getColor(colorName);
		ColorSchemaGenerator.SCHEMAS variantKey = selectionWidget.getVariantSelectedKey();
		AlfaRGB argbColor = new AlfaRGB(new RGB(color.getRed(), color.getGreen(), color.getBlue()), color.getAlpha());
		CrosstabStyle tempStyle = new CrosstabStyle(argbColor, variantKey, false);

		selectionWidget.setButtonData(totalColor, tempStyle.getColor(CrosstabStyle.COLOR_TOTAL));
		selectionWidget.setButtonData(groupColor, tempStyle.getColor(CrosstabStyle.COLOR_GROUP));
		selectionWidget.setButtonData(measuresColor, tempStyle.getColor(CrosstabStyle.COLOR_MEASURES));
		selectionWidget.setButtonData(detailColor, tempStyle.getColor(CrosstabStyle.COLOR_DETAIL));
	}
	
	
	/**
	 * Create the controls to decide the color schema of the cells
	 * 
	 * @param parent
	 */
	private void createColorGroup(Composite parent){

		
		SelectionAdapter schemaSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Update first the button and the regenerate the current selection
				updateSelectedColor();
				notifyChange();	
			}
		};
		selectionWidget = new ColorSelectionWidget(parent, selectionListener, schemaSelectionAdapter, ColorSelectionWidget.STYLE.BOTTOM);
		CrosstabStyle temp = getDefaultStyle();
		selectionWidget.addButton(totalColor, Messages.CrosstabWizardLayoutPage_totalLabel, temp.getColor(CrosstabStyle.COLOR_TOTAL));
		selectionWidget.addButton(groupColor, Messages.CrosstabWizardLayoutPage_groupLabel, temp.getColor(CrosstabStyle.COLOR_GROUP));
		selectionWidget.addButton(measuresColor, Messages.CrosstabWizardLayoutPage_measuresLabel, temp.getColor(CrosstabStyle.COLOR_MEASURES));
		selectionWidget.addButton(detailColor, Messages.CrosstabWizardLayoutPage_detailLabel, temp.getColor(CrosstabStyle.COLOR_DETAIL));
		selectionWidget.createControl(Messages.CrosstabWizardLayoutPage_schemaGroupLabel,Messages.CrosstabWizardLayoutPage_manualGroupLabel,4);
		
		Composite checkComposite = new Composite(parent, SWT.NONE);
		checkComposite.setLayout(new GridLayout(2,false));
		checkComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		whiteGrid = new Button(checkComposite, SWT.CHECK);
		whiteGrid.setText(Messages.CrosstabWizardLayoutPage_white_grid_check);
		GridData checkBoxData = new GridData(GridData.FILL_HORIZONTAL);
		checkBoxData.horizontalSpan = 2;
		whiteGrid.setLayoutData(checkBoxData);
		
		showGrid = new Button(checkComposite, SWT.CHECK);
		showGrid.setText(Messages.CrosstabWizardLayoutPage_noGrid_label);
		GridData showGridData = new GridData(GridData.FILL_HORIZONTAL);
		showGridData.horizontalSpan = 2;
		showGrid.setLayoutData(showGridData);
		showGrid.setSelection(true);

		whiteGrid.addSelectionListener(selectionListener);
		showGrid.addSelectionListener(selectionListener);
	}
	
	/**
	 * Initialize the data if a template to edit was provided, so all the controls will be 
	 * initialized with the data read from that template
	 */
	private void setData(){
		if (templateToOpen instanceof CrosstabStyle){
			CrosstabStyle cStyle = (CrosstabStyle)templateToOpen;
			//Set the color on the toolitem buttons
			selectionWidget.setButtonData(totalColor, templateToOpen.getColor(CrosstabStyle.COLOR_TOTAL));
			selectionWidget.setButtonData(groupColor, templateToOpen.getColor(CrosstabStyle.COLOR_GROUP));
			selectionWidget.setButtonData(measuresColor, templateToOpen.getColor(CrosstabStyle.COLOR_MEASURES));
			selectionWidget.setButtonData(detailColor, templateToOpen.getColor(CrosstabStyle.COLOR_DETAIL));
			//When a  template is open for the edit then the colors input method is set to manual by default
			selectionWidget.switchInputMethod(ColorInput.MANUAL);
			whiteGrid.setSelection(cStyle.getWhiteGrid());
			showGrid.setSelection(cStyle.isShowGrid());
			if (titleText != null) titleText.setText(cStyle.getDescription());		
		}
	}
	
	/**
	 * Create the checkbox to decide to show or not the total bands
	 * 
	 * @param parent
	 */
	private void createSectionsGroup(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.CrosstabWizardLayoutPage_visible_sections_group);
		group.setLayout(new GridLayout(1,false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final Button addRowTotals = new Button(group, SWT.CHECK);
		addRowTotals.setText(Messages.CrosstabWizardLayoutPage_add_row_group_totals);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		addRowTotals.setLayoutData(gd);
		addRowTotals.setSelection(true);
		addRowTotals.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isAddRowTotal = addRowTotals.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Button addColumnTotals = new Button(group, SWT.CHECK);
		addColumnTotals.setText(Messages.CrosstabWizardLayoutPage_add_column_group_totals);
		gd = new GridData();
		gd.horizontalSpan = 2;
		addColumnTotals.setLayoutData(gd);
		addColumnTotals.setSelection(true);
		addColumnTotals.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isAddColTotal = addColumnTotals.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		//FIXME: This option are not taken because these properties are read and used when the dialog 
		//is created instead of when the wizard is completed.
		group.setVisible(false);
	}
	
	/**
	 * Create the control ad the left of the preview, so the cell color and visible sections
	 * 
	 * @param parent parent of the composite
	 */
	private void createLeftCol(Composite parent){
		Composite leftCol = new Composite(parent, SWT.NONE);
		leftCol.setLayout(new GridLayout(1,false));
		createColorGroup(leftCol);
		createSectionsGroup(leftCol);
		GridData leftPanelData = new GridData(GridData.FILL_VERTICAL);
		leftPanelData.widthHint = 300;
		leftPanelData.minimumHeight= 200;
		leftCol.setLayoutData(leftPanelData);
	}
	
	/**
	 * Set a template that will be used to initialize (if possible) the 
	 * control of the wizard with the value of the template
	 * 
	 * @param template template used to initialize the value
	 */
	public void setTemplateToOpen(TemplateStyle template){
		this.templateToOpen = template;
	}
	
	
	/**
	 * Generate the preview area
	 * 
	 * @param parent
	 */
	private void createPreview(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.CrosstabWizardLayoutPage_style_preview_group);
		group.setLayout(new GridLayout(1,false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		preview = new CrosstabStylePreview(group, SWT.NONE);
		preview.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	/**
	 * Called when some property change, rebuild the crosstab style with the actual state of the control
	 * and request the redraw of the preview
	 */
	private void notifyChange(){
		AlfaRGB colorTotal = selectionWidget.getButtonData(totalColor);
		AlfaRGB colorGroup = selectionWidget.getButtonData(groupColor);
		AlfaRGB colorDetail = selectionWidget.getButtonData(detailColor);
		AlfaRGB colorMeasures = selectionWidget.getButtonData(measuresColor);
		lastGeneratedStyle = new CrosstabStyle(colorTotal, colorGroup, colorMeasures, colorDetail, whiteGrid.getSelection());
		lastGeneratedStyle.setShowGrid(showGrid.getSelection());
		if (titleText != null) lastGeneratedStyle.setDescription(titleText.getText());
		preview.setTableStyle(lastGeneratedStyle);
	}
	
	/**
	 * Return the last generated style for the crosstab, that is the effective one when 
	 * the dialog was closed. If the last generated style is null (maybe because the wizard 
	 * was finished without reach the last step), a default one is provided
	 * 
	 * @return the style to apply to the table
	 */
	public CrosstabStyle getSelectedStyle(){
		if (lastGeneratedStyle == null) {
			lastGeneratedStyle = getDefaultStyle();
		}
		return lastGeneratedStyle;
	}
	
	/**
	 * Get a CrosstabStyle with the default values
	 * 
	 * @return a not null CrosstabStyle
	 */
	public static CrosstabStyle getDefaultStyle(){
		String firstColor = ColorSchemaGenerator.getColors().get(0);
		Color color = ColorSchemaGenerator.getColor(firstColor);
		AlfaRGB rgbColor = new AlfaRGB(new RGB(color.getRed(), color.getGreen(), color.getBlue()), color.getAlpha());
		CrosstabStyle style = new CrosstabStyle(rgbColor, ColorSchemaGenerator.SCHEMAS.DEFAULT, false);
		return style;
	}
	
	/**
	 * Create the controls to input the title of the style
	 * 
	 * @param parent composite where the control will be placed
	 */
	private void createTitleLabel(Composite parent){
		Composite titleComposite = new Composite(parent, SWT.NONE);
		titleComposite.setLayout(new GridLayout(2,false));
		GridData titleCompositeData = new GridData();
		titleCompositeData.horizontalSpan = 2;
		titleCompositeData.grabExcessHorizontalSpace=true;
		titleCompositeData.horizontalAlignment = SWT.FILL;
		titleComposite.setLayoutData(titleCompositeData);
		Label descriptionLabel = new Label(titleComposite, SWT.NONE);
		descriptionLabel.setText(Messages.CrosstabWizardLayoutPage_nameLabel);
		titleText = new Text(titleComposite, SWT.BORDER);
		titleText.addModifyListener(modifyListener);
		GridData textData = new GridData();
		textData.grabExcessHorizontalSpace = true;
		textData.horizontalAlignment = SWT.FILL;
		titleText.setLayoutData(textData);
	}

	/**
	 * Create all the controls of the dialog
	 */
	public void createControl(Composite parent) {
		Composite dialog = new Composite(parent, SWT.NONE);
		GridLayout generalLayout = new GridLayout(2,false);
		dialog.setLayout(generalLayout);
		setControl(dialog);
		//Create the title
		if (createTitle) createTitleLabel(dialog);
		createLeftCol(dialog);
		createPreview(dialog);
		if (templateToOpen != null) setData();
		notifyChange();
		
	}

	/**
	 * return the ID of the contextual help to show
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.CROSSTAB_STYLES;
	}

}
