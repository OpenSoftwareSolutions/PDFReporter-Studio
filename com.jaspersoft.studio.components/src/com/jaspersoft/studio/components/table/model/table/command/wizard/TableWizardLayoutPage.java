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
package com.jaspersoft.studio.components.table.model.table.command.wizard;



import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle.BorderStyleEnum;
import com.jaspersoft.studio.components.table.model.dialog.TableStylePreview;
import com.jaspersoft.studio.components.widgets.ColorSelectionWidget;
import com.jaspersoft.studio.components.widgets.ColorSelectionWidget.ColorInput;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.property.color.ColorSchemaGenerator;
import com.jaspersoft.studio.swt.widgets.ColorStyledText;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Step of the wizard where you can define style of the table
 * 
 * 
 * @author Orlandin Marco
 *
 */
public class TableWizardLayoutPage extends JSSHelpWizardPage {
	
	/**
	 * Table section option
	 */
	private boolean isTableHeader = true;
	private boolean isTableFooter = true;
	private boolean isColumnHeader = true;
	private boolean isColumnFooter = true;
	private boolean isGroupHeader = true;
	private boolean isGroupFooter = true;
	

	/**
	 * Checkbox for the rows alternated color
	 */
	private Button alternateColor;
	
	/**
	 * the textfield of the title
	 */
	private Text titleText = null;

	/**
	 * Widget for the border color
	 */
	private ColorStyledText borderColor;
	
	/**
	 * Style of the border of the table
	 */
	private BorderStyleEnum borderStyle;
	
	/**
	 * Table preview widget
	 */
	private TableStylePreview preview;
	
	/**
	 * Last style generated
	 */
	private TableStyle lastGeneratedStyle = null;
	
	/**
	 * Composite of the bottom area (the table section)
	 */
	private Composite bottomComposite;
	
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
	 * List of the buttons that represent the border style
	 */
	private List<ToolItem> borderStyleButtons = new ArrayList<ToolItem>();
	
	/**
	 * String used as id for the button that represent the color used in the table header cells
	 */
	private static final String tableHeadrButton="THEADER_BUTTON"; //$NON-NLS-1$
	
	/**
	 * String used as id for the button that represent the color used in the table column cells
	 */
	private static final String columnHeadrButton="CHEADER_BUTTON"; //$NON-NLS-1$
	
	/**
	 * String used as id for the button that represent the color used in the table detail cells
	 */
	private static final String detailButton="DETAIL_BUTTON"; //$NON-NLS-1$
	
	/**
	 * String used as id for the button  that represent the color used in the table detail cells, checked 
	 * the row is odd and the attribute to alternate the rows color is true
	 */
	private static final String altDetailButton = "ALT_DETAIL_BUTTON"; //$NON-NLS-1$
	
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
	
	protected TableWizardLayoutPage(boolean createTitle) {
		super("tablepage");  //$NON-NLS-1$
		setTitle(Messages.TableWizardLayoutPage_layout);
		setDescription(Messages.TableWizardLayoutPage_description);
		this.createTitle = createTitle;
	}
	
	protected TableWizardLayoutPage(){
		this(false);
	}
	
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_LAYOUT_PAGE;
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
	/**
	 * When the colors are selected using the schema\variation input method this method is called to 
	 * update the every button that represent a color with the appropriate color
	 */
	private void updateSelectedColor(){
		String colorName = selectionWidget.getSchemaSelected();
		Color color = ColorSchemaGenerator.getColor(colorName);
		ColorSchemaGenerator.SCHEMAS variantKey = selectionWidget.getVariantSelectedKey();
		AlfaRGB baseColor = new AlfaRGB(new RGB(color.getRed(), color.getGreen(), color.getBlue()), color.getAlpha());
		TableStyle tempStyle = new TableStyle(baseColor, variantKey, TableStyle.BorderStyleEnum.FULL, AlfaRGB.getFullyOpaque(ColorConstants.white.getRGB()),true);
		selectionWidget.setButtonData(tableHeadrButton, tempStyle.getColor(TableStyle.COLOR_TABLE_HEADER));
		selectionWidget.setButtonData(columnHeadrButton, tempStyle.getColor(TableStyle.COLOR_COL_HEADER));
		selectionWidget.setButtonData(altDetailButton, tempStyle.getColor(TableStyle.COLOR_DETAIL));
		selectionWidget.setButtonData(detailButton, tempStyle.getColor(TableStyle.STANDARD_COLOR_DETAIL));
	}
	
	/**
	 * Create the group with the controls for the cell colors
	 * 
	 * @param parent parent composite of the group 
	 */
	private void createCellColors(Composite parent){
		SelectionAdapter schemaSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Update first the button and the regenerate the current selection
				updateSelectedColor();
				notifyChange();	
			}
		};
		selectionWidget = new ColorSelectionWidget(parent, selectionListener, schemaSelectionAdapter, ColorSelectionWidget.STYLE.BOTTOM);
		TableStyle temp = getDefaultStyle();
		selectionWidget.addButton(tableHeadrButton, Messages.TableWizardLayoutPage_tableHeaderLabel, temp.getColor(TableStyle.COLOR_TABLE_HEADER));
		selectionWidget.addButton(columnHeadrButton, Messages.TableWizardLayoutPage_columnHeaderLabel, temp.getColor(TableStyle.COLOR_COL_HEADER));
		selectionWidget.addButton(detailButton, Messages.TableWizardLayoutPage_detailLabel, temp.getColor(TableStyle.STANDARD_COLOR_DETAIL));
		selectionWidget.addButton(altDetailButton, Messages.TableWizardLayoutPage_altDetailLabel, temp.getColor(TableStyle.COLOR_DETAIL));
		selectionWidget.createControl(Messages.TableWizardLayoutPage_schemaGroupLabel, Messages.TableWizardLayoutPage_manualColorLabel,4);
		
		//Create the checkbox to alternate the color
		alternateColor = new Button(selectionWidget.getManualComposite(), SWT.CHECK);
		alternateColor.setText(Messages.TableWizardLayoutPage_alternated_rows_label);
		GridData checkBoxData = new GridData(GridData.FILL_HORIZONTAL);
		checkBoxData.horizontalSpan = 4;
		alternateColor.setLayoutData(checkBoxData);
		alternateColor.addSelectionListener(selectionListener);
	}
	
	/**
	 * Set the borders button according to the passed BorderStyleEnum
	 * 
	 * @param loadedStyle
	 */
	private void setBorderButtons(BorderStyleEnum loadedStyle){
		borderStyle = loadedStyle;
		ToolItem buttonFull = borderStyleButtons.get(0);
		ToolItem buttonHorizontal1 = borderStyleButtons.get(1);
		ToolItem buttonHorizontal2 = borderStyleButtons.get(2);
		buttonFull.setSelection(false);
		buttonHorizontal1.setSelection(false);
		buttonHorizontal2.setSelection(false);
		if (borderStyle.equals(BorderStyleEnum.FULL)) buttonFull.setSelection(true);
		else if (borderStyle.equals(BorderStyleEnum.PARTIAL_VERTICAL)) buttonHorizontal1.setSelection(true);
		else buttonHorizontal2.setSelection(true);
	}
	
	/**
	 * Initialize the data if a template to edit was provided, so all the controls will be 
	 * initialized with the data read from that template
	 */
	private void setData(){
		if (templateToOpen instanceof TableStyle){
			TableStyle cStyle = (TableStyle)templateToOpen;
			//Set the color on the toolitem buttons
			selectionWidget.setButtonData(tableHeadrButton, templateToOpen.getColor(TableStyle.COLOR_TABLE_HEADER));
			selectionWidget.setButtonData(columnHeadrButton, templateToOpen.getColor(TableStyle.COLOR_COL_HEADER));
			selectionWidget.setButtonData(altDetailButton, templateToOpen.getColor(TableStyle.COLOR_DETAIL));
			selectionWidget.setButtonData(detailButton, templateToOpen.getColor(TableStyle.STANDARD_COLOR_DETAIL));
			//When a  template is open for the edit then the colors input method is set to manual by default
			selectionWidget.switchInputMethod(ColorInput.MANUAL);
			
			alternateColor.setSelection(cStyle.hasAlternateColor());
			borderColor.setColor(cStyle.getRGBBorderColor());
			setBorderButtons(cStyle.getBorderStyle());
			if (titleText != null) titleText.setText(cStyle.getDescription());		
		}
	}
	
	/**
	 * Return a class that identify which are the visible section of the table
	 * 
	 * @return a TemplateSections class, that contains a series of boolean flags used to know 
	 * which sections of the table are visible.
	 */
	public TableSections getVisibileSections(){
		return new TableSections(isTableHeader, isTableFooter, isColumnHeader, isColumnFooter, isGroupHeader, isGroupFooter);
	}
	
	/**
	 * Create the group with the controls for the cell borders
	 * 
	 * @param parent parent composite of the group 
	 */
	private void createCellBorders(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.TableWizardLayoutPage_cell_border_group);
		group.setLayout(new GridLayout(2,false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label firstLabel = new Label(group,SWT.NONE);
		firstLabel.setText(Messages.TableWizardLayoutPage_borders_color_label);
		Label secondLabel = new Label(group,SWT.NONE);
		secondLabel.setText(Messages.TableWizardLayoutPage_borders_style_label);
		
		borderColor = new ColorStyledText(group);
		borderColor.setColor(AlfaRGB.getFullyOpaque(ColorConstants.black.getRGB()));
		GridData borderColorData = new GridData();
		borderColorData.minimumWidth = 50;
		borderColor.setLayoutData(borderColorData);
		borderColor.setBackground(ColorConstants.white);
		
		ToolBar toolBar = new ToolBar (group, SWT.FLAT);
		
		ToolItem buttonFull = new ToolItem (toolBar, SWT.RADIO);
		buttonFull.setImage (Activator.getDefault().getImage("icons/full_borders.png"));  //$NON-NLS-1$
		borderStyleButtons.add(buttonFull);
		
		ToolItem buttonHorizontal1 = new ToolItem (toolBar, SWT.RADIO);
		buttonHorizontal1.setImage (Activator.getDefault().getImage("icons/horizontal_borders.png"));  //$NON-NLS-1$
		borderStyleButtons.add(buttonHorizontal1);
		
		ToolItem buttonHorizontal2 = new ToolItem (toolBar, SWT.RADIO);
		buttonHorizontal2.setImage (Activator.getDefault().getImage("icons/horizontal_borders2.png")); //$NON-NLS-1$
		borderStyleButtons.add(buttonHorizontal2);
		
		toolBar.pack ();
		buttonFull.setSelection(true);
		borderStyle = BorderStyleEnum.FULL;
		
		borderColor.addListener(modifyListener);
		
		buttonFull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ToolItem buttonFull = borderStyleButtons.get(0);
				ToolItem buttonHorizontal1 = borderStyleButtons.get(1);
				ToolItem buttonHorizontal2 = borderStyleButtons.get(2);
				if (!buttonFull.getSelection()){ 
					borderStyle = BorderStyleEnum.FULL;
					buttonFull.setSelection(true);
					buttonHorizontal1.setSelection(false);
					buttonHorizontal2.setSelection(false);
					notifyChange();
				}
			}
		});
		
		buttonHorizontal1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ToolItem buttonFull = borderStyleButtons.get(0);
				ToolItem buttonHorizontal1 = borderStyleButtons.get(1);
				ToolItem buttonHorizontal2 = borderStyleButtons.get(2);
				if (!buttonHorizontal1.getSelection()){ 
					borderStyle = BorderStyleEnum.PARTIAL_VERTICAL;
					buttonHorizontal1.setSelection(true);
					buttonFull.setSelection(false);
					buttonHorizontal2.setSelection(false);
					notifyChange();
				}
			}
		});
		
		buttonHorizontal2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ToolItem buttonFull = borderStyleButtons.get(0);
				ToolItem buttonHorizontal1 = borderStyleButtons.get(1);
				ToolItem buttonHorizontal2 = borderStyleButtons.get(2);
				if (!buttonHorizontal2.getSelection()){ 
					borderStyle = BorderStyleEnum.ONLY_HORIZONTAL;
					buttonHorizontal2.setSelection(true);
					buttonFull.setSelection(false);
					buttonHorizontal1.setSelection(false);
					notifyChange();
				}
			}
		});
	}
	
	/**
	 * Create the control ad the left of the preview, so the cell color and 
	 * border groups
	 * 
	 * @param parent parent of the composite
	 */
	private void createLeftCol(Composite parent){
		Composite leftCol = new Composite(parent, SWT.NONE);
		leftCol.setLayout(new GridLayout(1,false));
		createCellColors(leftCol);
		createCellBorders(leftCol);
		GridData leftPanelData = new GridData(GridData.FILL_VERTICAL);
		leftPanelData.widthHint = 300;
		leftPanelData.minimumHeight= 200;
		leftCol.setLayoutData(leftPanelData);
	}
	
	/**
	 * Recursive method to change the enable state of a control, if the control
	 * is a composite it will drill down to disable its children
	 * 
	 * @param ctrl the actual control
	 * @param enabled true if the control should be enabled, false otherwise
	 */
	public void recursiveSetEnabled(Control ctrl, boolean enabled) {
		   if (ctrl instanceof Composite) {
		      Composite comp = (Composite) ctrl;
		      for (Control c : comp.getChildren())
		         recursiveSetEnabled(c, enabled);
		   } else {
		      ctrl.setEnabled(enabled);
		   }
		}
	
	/**
	 * Enable or disable the bottom composite where are the table sections
	 * 
	 * @param enabled true if it is enabled, false otherwise
	 */
	public void setEnabledBottomPanel(boolean enabled){
		for (Control c : bottomComposite.getChildren())
	         recursiveSetEnabled(c, enabled);
	}
	
	/**
	 * Create the bottom area of the dialog, where there are the option on the section 
	 * that will be created
	 * 
	 * @param parent parent of the bottom area
	 */
	private void createBottom(Composite parent){
		bottomComposite = new Composite(parent, SWT.NONE);
		GridData bottomData = new GridData(GridData.FILL_HORIZONTAL);
		bottomData.horizontalSpan = 2;
		bottomComposite.setLayoutData(bottomData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		bottomComposite.setLayout(layout);
		

		final Button addTHeader = new Button(bottomComposite, SWT.CHECK);
		addTHeader.setText(Messages.TableWizardLayoutPage_add_table_header);
		addTHeader.setSelection(isTableHeader);
		addTHeader.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isTableHeader = addTHeader.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Button addCHeader = new Button(bottomComposite, SWT.CHECK);
		addCHeader.setText(Messages.TableWizardLayoutPage_add_column_header);
		addCHeader.setSelection(isColumnHeader);
		addCHeader.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isColumnHeader = addCHeader.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Button addGHeader = new Button(bottomComposite, SWT.CHECK);
		addGHeader.setText(Messages.TableWizardLayoutPage_add_group_header);
		addGHeader.setSelection(isGroupHeader);
		addGHeader.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isGroupHeader = addGHeader.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Button addGFooter = new Button(bottomComposite, SWT.CHECK);
		addGFooter.setText(Messages.TableWizardLayoutPage_add_group_footer);
		addGFooter.setSelection(isGroupFooter);
		addGFooter.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isGroupFooter = addGFooter.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Button addCFooter = new Button(bottomComposite, SWT.CHECK);
		addCFooter.setText(Messages.TableWizardLayoutPage_add_column_footer);
		addCFooter.setSelection(isColumnFooter);
		addCFooter.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isColumnFooter = addCFooter.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		final Button addTFooter = new Button(bottomComposite, SWT.CHECK);
		addTFooter.setText(Messages.TableWizardLayoutPage_add_table_footer);
		addTFooter.setSelection(isTableFooter);
		addTFooter.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isTableFooter = addTFooter.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}
	
	/**
	 * Get a TableStyle with the default values
	 * 
	 * @return a not null TableStyle
	 */
	public static TableStyle getDefaultStyle(){
		String firstColor = ColorSchemaGenerator.getColors().get(0);
		Color color = ColorSchemaGenerator.getColor(firstColor);
		AlfaRGB rgbColor = AlfaRGB.getFullyOpaque(new RGB(color.getRed(), color.getGreen(), color.getBlue()));
		return new TableStyle(rgbColor, ColorSchemaGenerator.SCHEMAS.DEFAULT, BorderStyleEnum.FULL, AlfaRGB.getFullyOpaque(ColorConstants.black.getRGB()), false);
	}
	
	/**
	 * Get a TableSections with the default values
	 * 
	 * @return a not null TableSections
	 */
	public static TableSections getDefaultSection(){
		return new TableSections(true, true, true, true, true, true); 
	}
	
	/**
	 * Return the last generated style for the table, that is the effective one when 
	 * the dialog was closed. If the last generated style is null (maybe because the wizard 
	 * was finished without reach the last step), the default one is provided
	 * 
	 * @return the style to apply to the table
	 */
	public TableStyle getSelectedStyle(){
		if (lastGeneratedStyle == null) {
			lastGeneratedStyle = getDefaultStyle();
		}
		return lastGeneratedStyle;
	}
	
	/**
	 * Called when some property change, rebuild the table style with the actual state of the control
	 * and request the redraw of the preview
	 */
	private void notifyChange(){
		AlfaRGB tableHeader = selectionWidget.getButtonData(tableHeadrButton);
		AlfaRGB columnHeader = selectionWidget.getButtonData(columnHeadrButton);
		AlfaRGB detail = selectionWidget.getButtonData(detailButton);
		AlfaRGB altDetail = selectionWidget.getButtonData(altDetailButton);
		lastGeneratedStyle = new TableStyle(tableHeader, columnHeader, detail, altDetail, borderStyle, borderColor.getColor(), alternateColor.getSelection());
		if (titleText != null) lastGeneratedStyle.setDescription(titleText.getText());
		preview.setTableStyle(lastGeneratedStyle);

	}
	
	/**
	 * Generate the preview area
	 * 
	 * @param parent
	 */
	private void createPreview(Composite parent){
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.TableWizardLayoutPage_style_preview_group);
		group.setLayout(new GridLayout(1,false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		preview = new TableStylePreview(group, SWT.NONE);
		preview.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	private void createTitleLabel(Composite parent){
		Composite titleComposite = new Composite(parent, SWT.NONE);
		titleComposite.setLayout(new GridLayout(2,false));
		GridData titleCompositeData = new GridData();
		titleCompositeData.horizontalSpan = 2;
		titleCompositeData.grabExcessHorizontalSpace=true;
		titleCompositeData.horizontalAlignment = SWT.FILL;
		titleComposite.setLayoutData(titleCompositeData);
		Label descriptionLabel = new Label(titleComposite, SWT.NONE);
		descriptionLabel.setText(Messages.TableWizardLayoutPage_nameLabel);
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
		
		//Creating the left col
		createLeftCol(dialog);
		
		//Creating the right preview col
		createPreview(dialog);
		
		//Create the bottom band
		createBottom(dialog);
		if (templateToOpen != null) setData();
		notifyChange();
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
	 * Return if the table has a table header
	 * 
	 * @return true if the table has a table header, false otherwise
	 */
	public boolean isTableHeader() {
		return isTableHeader;
	}

	/**
	 * Return if the table has a table footer
	 * 
	 * @return true if the table has a table footer, false otherwise
	 */
	public boolean isTableFooter() {
		return isTableFooter;
	}

	/**
	 * Return if the table has a column header
	 * 
	 * @return true if the table has a column header, false otherwise
	 */
	public boolean isColumnHeader() {
		return isColumnHeader;
	}

	/**
	 * Return if the table has a column footer
	 * 
	 * @return true if the table has a column footer, false otherwise
	 */
	public boolean isColumnFooter() {
		return isColumnFooter;
	}

	/**
	 * Return if the table has a group header
	 * 
	 * @return true if the table has a group header, false otherwise
	 */
	public boolean isGroupHeader() {
		return isGroupHeader;
	}

	/**
	 * Return if the table has a group footer
	 * 
	 * @return true if the table has a group footer, false otherwise
	 */
	public boolean isGroupFooter() {
		return isGroupFooter;
	}
}
