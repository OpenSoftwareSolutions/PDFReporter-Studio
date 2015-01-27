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
package com.jaspersoft.studio.style.view.text;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.base.JRBoxPen;
import net.sf.jasperreports.engine.type.LineStyleEnum;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.java2d.J2DLightweightSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.combomenu.ComboItem;
import com.jaspersoft.studio.property.combomenu.ComboItemAction;
import com.jaspersoft.studio.property.combomenu.ComboMenuViewer;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.section.graphic.LineBoxDrawer;
import com.jaspersoft.studio.property.section.graphic.LineBoxDrawer.Location;
import com.jaspersoft.studio.property.section.widgets.SPRWPopUpCombo;
import com.jaspersoft.studio.swt.widgets.ColorStyledText;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.UIUtil;

/**
 * Controls to define the borders of a TextStyle element
 * 
 * @author Orlandin Marco
 */
public class BordersWidget extends Composite {


	/**
	 * Toolbutton to set all the borders
	 */
	private ToolItem allBorder;
	
	/**
	 * Toolbutton to remove all the borders
	 */
	private ToolItem noneBorder;
	
	/**
	 * Toolbutton to set the up and bottom borders
	 */
	private ToolItem upDownBorder;
	
	/**
	 * Toolbutton to set the left and right borders
	 */
	private ToolItem leftRightBorder;
	
	/**
	 * Drawer for the borders linebox
	 */
	private LineBoxDrawer bd;
	
	/**
	 * The filed used to set\show the lineWidth
	 */
	private Spinner lineWidth;
	
	/**
	 * The Combo popup used to set\show the linestyle
	 */
	private ComboMenuViewer lineStyle;
	
	/**
	 * Checkbox to know if all the padding are at the same value
	 */
	private Button checkBoxPadding;
	
	/**
	 * Spinner for the left padding or for the general padding when 
	 * the checkbox is selected
	 */
	private Spinner paddingLeft;
	
	/**
	 * Spinner for the right padding
	 */
	private Spinner paddingRight;
	
	/**
	 * Spinner for the bottom padding
	 */
	private Spinner paddingBottom;
	
	/**
	 * Spinner for the top padding
	 */
	private Spinner paddingTop;
	
	/**
	 * Border figure rectangle
	 */
	private RectangleFigure borderPreview;
	
	/**
	 * Border figure canvas
	 */
	private Canvas square;
	
	/**
	 * Group section where the controls regarding the border are placed
	 */
	private Group rightPanel;
	
	/**
	 * The widget used to set\show the line color
	 */
	private ColorStyledText lineColor;
	
	/**
	 * The element modified
	 */
	private TextStyle element;
	
	/**
	 * Selection lisners called when a control is used
	 */
	private List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();

	/**
	 * Create the control
	 * 
	 * @param parent parent control
	 * @param style style of the container
	 * @param element element modified
	 */
	public BordersWidget(Composite parent, int style, TextStyle element) {
		super(parent, style);
		this.element = element;
		setLayout(new GridLayout(1,true));
				
		createPaddingPanel(this);
		
		rightPanel = new Group(this, SWT.NONE);
		rightPanel.setText(Messages.common_borders);
		rightPanel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		rightPanel.setLayout(new GridLayout(2,false));
	
		createBorderPreview(rightPanel);
		
		createStyle(rightPanel);
		
		Composite toolBarLayout = new Composite(rightPanel, SWT.NONE);
		toolBarLayout.setLayout(new GridLayout(1,false));
		GridData toolBardGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false);
		toolBardGridData.horizontalSpan = 2;
		toolBardGridData.widthHint = 200;
		toolBardGridData.horizontalIndent = 5;
		toolBarLayout.setLayoutData(toolBardGridData);
		Label textLabel = new Label(toolBarLayout,SWT.NONE);
		textLabel.setText(Messages.BordersSection_Default_Label);
		ToolBar toolBar = new ToolBar(toolBarLayout, SWT.FLAT | SWT.WRAP);
		createButtons(toolBar);

		allBorder.setSelection(false);
		noneBorder.setSelection(false);
		leftRightBorder.setSelection(false);
		upDownBorder.setSelection(false);
	}
	
	/**
	 * Add a selection listener, all the selection listener are 
	 * called when a control is used
	 * 
	 * @param listener added listener
	 */
	public void addSelectionListener(SelectionListener listener){
		selectionListeners.add(listener);
	}


	/**
	 * Create the canvas box where the borders will be represented and could be edited
	 * @param composite the composite where the canvas will be placed
	 */
	private void createBorderPreview(Composite composite) {
		square = new Canvas(composite, SWT.BORDER | SWT.NO_REDRAW_RESIZE);
		square.setBackground(ColorConstants.white);
		//The mouse down may select a border and the mouse up refresh the painting area
		square.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				updateRightPanel();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		GridData gd = new GridData(GridData.FILL_VERTICAL | GridData.HORIZONTAL_ALIGN_CENTER
				| GridData.VERTICAL_ALIGN_CENTER);
		gd.widthHint = 120;
		gd.heightHint = 120;
		square.setLayoutData(gd);

		LightweightSystem lws = new J2DLightweightSystem();
		lws.setControl(square);
		bd = new LineBoxDrawer(null, square);
		borderPreview = new LineBoxRectangle(bd,element.getBorders());
		square.setToolTipText(Messages.BordersSection_preview_ToolTip);
		lws.setContents(borderPreview);
	}
	
	/**
	 * Enable or disable the spinner for the directional padding when the checkbox 
	 * to set the same value in every spinner is selected
	 */
	private void checkBoxValueChange(){
		if (checkBoxPadding.getSelection()){
			paddingRight.setEnabled(false);
			paddingTop.setEnabled(false);
			paddingBottom.setEnabled(false);
		} else {
			paddingRight.setEnabled(true);
			paddingTop.setEnabled(true);
			paddingBottom.setEnabled(true);						
		}
	}
	
	/**
	 * Create the padding group and the control in it
	 * @param parent the composite where the group will be placed
	 */
	private void createPaddingPanel(Composite parent){
		Group composite = new Group(parent, SWT.NONE);
		composite.setText(Messages.BordersSection_Padding_Box_Title);
		
		GridLayout layout = new GridLayout(4, false);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		GridData gd = new GridData();
		gd.horizontalSpan = 4;
		
		checkBoxPadding = new Button(composite, SWT.CHECK | SWT.FLAT | Window.getDefaultOrientation());
		checkBoxPadding.setText(Messages.BordersSection_Same_Padding_Value_Check);
		checkBoxPadding.setLayoutData(gd);
		checkBoxPadding.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				checkBoxValueChange();
				changePropertyPadding();
				callListeners(checkBoxPadding, JRBaseLineBox.PROPERTY_PADDING);
			}
		});
		
		new CLabel(composite, SWT.RIGHT).setText(Messages.BordersSection_Left_Label);
		
		paddingLeft = new Spinner(composite, SWT.BORDER | SWT.FLAT);
		paddingLeft.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		paddingLeft.setToolTipText(Messages.BordersSection_padding_tool_tip);
		paddingLeft.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changePropertyPadding();
				callListeners(paddingLeft, JRBaseLineBox.PROPERTY_LEFT_PADDING);
			}
		});
		
		new CLabel(composite, SWT.RIGHT).setText(Messages.common_right); 
		
		paddingRight = new Spinner(composite, SWT.BORDER | SWT.FLAT);
		paddingRight.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		paddingRight.setToolTipText(Messages.BordersSection_padding_tool_tip);
		paddingRight.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changePropertyPadding();
				callListeners(paddingRight, JRBaseLineBox.PROPERTY_RIGHT_PADDING);
			}
		});
		
		new CLabel(composite, SWT.RIGHT).setText(Messages.BordersSection_Top_Label); 
		
		paddingTop = new Spinner(composite, SWT.BORDER | SWT.FLAT);
		paddingTop.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		paddingTop.setToolTipText(Messages.BordersSection_padding_tool_tip);
		paddingTop.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changePropertyPadding();
				callListeners(paddingTop, JRBaseLineBox.PROPERTY_TOP_PADDING);
			}
		});
		
		new CLabel(composite, SWT.RIGHT).setText(Messages.common_bottom); 
		
		paddingBottom = new Spinner(composite, SWT.BORDER | SWT.FLAT);
		paddingBottom.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		paddingBottom.setToolTipText(Messages.BordersSection_padding_tool_tip);
		paddingBottom.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changePropertyPadding();
				callListeners(paddingBottom, JRBaseLineBox.PROPERTY_BOTTOM_PADDING);
			}
		});
	}

	/**
	 * Create the controls to define the line style, width and color
	 * 
	 * @param parent composite where the control will be placed
	 * @return composite that contains all the controls
	 */
	private Control createStyle(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		new CLabel(composite, SWT.RIGHT).setText(Messages.common_pen_color + ":"); //$NON-NLS-1$
		lineColor = new ColorStyledText(composite);
		lineColor.setColor(AlfaRGB.getFullyOpaque(new RGB(0, 0, 0)));
		lineColor.addListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changeProperty(JRBasePen.PROPERTY_LINE_COLOR, lineColor.getColor());	
				callListeners(lineColor.getPaintArea(), JRBasePen.PROPERTY_LINE_COLOR);
			}
		});

		new CLabel(composite, SWT.NONE).setText(Messages.common_pen_style + ":"); //$NON-NLS-1$

		createLineStyle(composite);

		new CLabel(composite, SWT.RIGHT).setText( Messages.common_pen_width + ":"); //$NON-NLS-1$

		lineWidth = new Spinner(composite, SWT.BORDER | SWT.FLAT);
		lineWidth.setValues(0, 0, 5000, 1, 1, 1);
		lineWidth.setToolTipText(Messages.BordersSection_width_tool_tip);
		lineWidth.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selection = lineWidth.getSelection();
				int digits = lineWidth.getDigits();
				Float newValue = new Float(selection / Math.pow(10, digits));
				changeProperty(JRBasePen.PROPERTY_LINE_WIDTH, newValue);
				callListeners(lineWidth, JRBasePen.PROPERTY_LINE_WIDTH);
			}
		});
		

		return composite;
	}
	
	/**
	 * Convert a line style enum to an integer
	 * 
	 * @param style the line style enum
	 * @return an integer from 1 to 4
	 */
	private int lineStyletoInt(LineStyleEnum style){
		if (style.equals(LineStyleEnum.SOLID)) return 1;
		if (style.equals(LineStyleEnum.DASHED)) return 2;
		if (style.equals(LineStyleEnum.DOTTED)) return 3;
		if (style.equals(LineStyleEnum.DOUBLE)) return 4;
		return 0;
	}
	
	/**
	 * Convert an integer to a line style 
	 * 
	 * @param value an integer value from 1 to 4
	 * @return a line style enum
	 */
	private LineStyleEnum intToLinestyleEnum(int value){
		if (value == 1) return LineStyleEnum.SOLID;
		if (value == 2) return LineStyleEnum.DASHED;
		if (value == 3) return LineStyleEnum.DOTTED;
		if (value == 4) return LineStyleEnum.DOUBLE;
		return null;
	}
	
	/**
	 * Create the line style combo popup
	 * @param prop properties associated to the combo popup
	 * @param composite parent where the combo will be placed
	 */
	private void createLineStyle(final Composite composite) {
		List<ComboItem> itemsList = new ArrayList<ComboItem>();
		//A blank space is added at the end of the string to compensate the size enlargement because a selected element is in bold
		itemsList.add(new ComboItem("Inherited ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/inherited.png"),0, NullEnum.INHERITED, null));
		itemsList.add(new ComboItem("Solid line ", true, ResourceManager.getImage(this.getClass(), "/icons/resources/line-solid.png"),1, LineStyleEnum.SOLID, new Integer(LineStyleEnum.SOLID.getValue() + 1)));
		itemsList.add(new ComboItem("Dashed line ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/line-dashed.png"),2, LineStyleEnum.DASHED, new Integer(LineStyleEnum.DASHED.getValue() + 1)));
		itemsList.add(new ComboItem("Dotted line ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/line-dotted.png"),3, LineStyleEnum.DOTTED, new Integer(LineStyleEnum.DOTTED.getValue() + 1)));
		itemsList.add(new ComboItem("Double line ", true,  ResourceManager.getImage(this.getClass(), "/icons/resources/line-double.png"),4, LineStyleEnum.DOUBLE, new Integer(LineStyleEnum.DOUBLE.getValue() + 1)));
		//Creating the combo popup
		lineStyle = new ComboMenuViewer(composite, SWT.NORMAL, SPRWPopUpCombo.getLongest(itemsList));
		lineStyle.setItems(itemsList);
		lineStyle.addSelectionListener(new ComboItemAction() {
				/**
				 * The action to execute when an entry is selected
				 */
				@Override
				public void exec() {
						changeProperty(JRBasePen.PROPERTY_LINE_STYLE, lineStyle.getSelectionValue() != null ? (Integer)lineStyle.getSelectionValue() : null);			
						callListeners(lineStyle.getControl(), JRBasePen.PROPERTY_LINE_STYLE);
				}
		});
		lineStyle.select(1);
	}

	/**
	 * Print the toolbar button and add the listener to them
	 * @param toolBar
	 */
	private void createButtons(ToolBar toolBar) {
		
		noneBorder = new ToolItem(toolBar, SWT.PUSH);
		noneBorder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				AlfaRGB beforeSelectionColor = lineColor.getColor();// lastColorSelected;
				int selection = lineWidth.getSelection();
				Object beforeSelectionStyle = lineStyle.getSelectionValue();
				bd.setBorderSelected(Location.LEFT);
				bd.setBorderSelected(Location.RIGHT);
				bd.setBorderSelected(Location.TOP);
				bd.setBorderSelected(Location.BOTTOM);
				AlfaRGB color = AlfaRGB.getFullyOpaque(new RGB(0,0,0));
				Float newValue = new Float(0);
				changeProperty(JRBasePen.PROPERTY_LINE_STYLE, 1);
				changeProperty(JRBasePen.PROPERTY_LINE_COLOR, color);
				changeProperty(JRBasePen.PROPERTY_LINE_WIDTH, newValue);
				bd.unselectAll();
				//The selection action change the displayed values, so i need to restore them after the unselect
				lineStyle.select((Integer)beforeSelectionStyle);
				lineWidth.setSelection(selection);
				lineColor.setColor(beforeSelectionColor);
				callListeners(noneBorder.getParent(), JRBasePen.PROPERTY_LINE_STYLE);
			}
		});
		noneBorder.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/border.png")); //$NON-NLS-1$
		noneBorder.setToolTipText(Messages.BordersSection_No_Borders);
		
		allBorder = new ToolItem(toolBar, SWT.PUSH);
		allBorder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				bd.setBorderSelected(Location.LEFT);
				bd.setBorderSelected(Location.RIGHT);
				bd.setBorderSelected(Location.TOP);
				bd.setBorderSelected(Location.BOTTOM);
				int selection = lineWidth.getSelection();
				int digits = lineWidth.getDigits();
				Float newValue = new Float(selection / Math.pow(10, digits));
				AlfaRGB color = lineColor.getColor();
				Object style = lineStyle.getSelectionValue();
				changeProperty(JRBasePen.PROPERTY_LINE_STYLE, style);
				changeProperty(JRBasePen.PROPERTY_LINE_COLOR, color);
				changeProperty(JRBasePen.PROPERTY_LINE_WIDTH, newValue);
				bd.unselectAll();
				callListeners(allBorder.getParent(), JRBasePen.PROPERTY_LINE_STYLE);
			}
		});
		allBorder.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/border-outside.png")); //$NON-NLS-1$
		allBorder.setToolTipText(Messages.BordersSection_all_borders_tool_tip);
		
		leftRightBorder = new ToolItem(toolBar, SWT.PUSH);
		leftRightBorder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				AlfaRGB beforeSelectionColor = lineColor.getColor();
				int selection = lineWidth.getSelection();
				int digits = lineWidth.getDigits();
				Float beforeSelectionWidth = new Float(selection / Math.pow(10, digits));
				Object beforeSelectionStyle = lineStyle.getSelectionValue();
				bd.setBorderSelected(Location.LEFT,false);
				bd.setBorderSelected(Location.RIGHT,false);
				bd.setBorderSelected(Location.TOP);
				bd.setBorderSelected(Location.BOTTOM);
				Float newValue = new Float(0);
				changeProperty(JRBasePen.PROPERTY_LINE_STYLE, 1);
				changeProperty(JRBasePen.PROPERTY_LINE_COLOR, new AlfaRGB(new RGB(0,0,0),255));
				changeProperty(JRBasePen.PROPERTY_LINE_WIDTH, newValue);
				bd.setBorderSelected(Location.LEFT);
				bd.setBorderSelected(Location.RIGHT);
				bd.setBorderSelected(Location.TOP,false);
				bd.setBorderSelected(Location.BOTTOM,false);
				changeProperty(JRBasePen.PROPERTY_LINE_STYLE, beforeSelectionStyle);
				changeProperty(JRBasePen.PROPERTY_LINE_COLOR, beforeSelectionColor);
				changeProperty(JRBasePen.PROPERTY_LINE_WIDTH, beforeSelectionWidth);
				bd.unselectAll();
				lineColor.setColor(beforeSelectionColor);
				lineWidth.setSelection(selection);
				lineStyle.select((Integer)beforeSelectionStyle);
				callListeners(leftRightBorder.getParent(), JRBasePen.PROPERTY_LINE_STYLE);
			}
		});
		leftRightBorder.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/border-right-left.png")); //$NON-NLS-1$
		leftRightBorder.setToolTipText(Messages.BordersSection_Left_Right_Borders);
		
		upDownBorder = new ToolItem(toolBar, SWT.PUSH);
		upDownBorder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				AlfaRGB beforeSelectionColor = lineColor.getColor();
				int selection = lineWidth.getSelection();
				int digits = lineWidth.getDigits();
				Float beforeSelectionWidth = new Float(selection / Math.pow(10, digits));
				Object beforeSelectionStyle = lineStyle.getSelectionValue();
				bd.setBorderSelected(Location.LEFT);
				bd.setBorderSelected(Location.RIGHT);
				bd.setBorderSelected(Location.TOP,false);
				bd.setBorderSelected(Location.BOTTOM,false);
				Float newValue = new Float(0);
				changeProperty(JRBasePen.PROPERTY_LINE_STYLE, 1);
				changeProperty(JRBasePen.PROPERTY_LINE_COLOR, new AlfaRGB(new RGB(0,0,0), 255));
				changeProperty(JRBasePen.PROPERTY_LINE_WIDTH, newValue);
				bd.setBorderSelected(Location.LEFT,false);
				bd.setBorderSelected(Location.RIGHT,false);
				bd.setBorderSelected(Location.TOP);
				bd.setBorderSelected(Location.BOTTOM);;
				changeProperty(JRBasePen.PROPERTY_LINE_STYLE, beforeSelectionStyle);
				changeProperty(JRBasePen.PROPERTY_LINE_COLOR, beforeSelectionColor);
				changeProperty(JRBasePen.PROPERTY_LINE_WIDTH, beforeSelectionWidth);
				bd.unselectAll();
				lineColor.setColor(beforeSelectionColor);
				lineWidth.setSelection(selection);
				lineStyle.select((Integer)beforeSelectionStyle);
				callListeners(upDownBorder.getParent(), JRBasePen.PROPERTY_LINE_STYLE);
			}
		});
		upDownBorder.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/border-top-bottom.png")); //$NON-NLS-1$
		upDownBorder.setToolTipText(Messages.BordersSection_Top_Bottom_Borders);
	}
	
	/**
	 * Change the property of a linepen
	 * @param property the property name
	 * @param newValue the new value
	 */
	public void changeProperty(String property, Object newValue) {
		//it's a change of a border attribute
		boolean areAllUnselected = bd.areAllUnslected(); 
		if (areAllUnselected || bd.isBottomSelected()){
			setLinePenAttribute(element.getBorders().getBottomPen(), property, newValue);
		}
		if(areAllUnselected || bd.isTopSelected()){
			setLinePenAttribute(element.getBorders().getTopPen(), property, newValue);
		}
		if(areAllUnselected || bd.isLeftSelected()){
			setLinePenAttribute(element.getBorders().getLeftPen(), property, newValue);
		}
		if(areAllUnselected || bd.isRightSelected()){
			setLinePenAttribute(element.getBorders().getRightPen(), property, newValue);
		}
		bd.refresh();
	}
	
	/**
	 * Set a property of a JRBoxPen element
	 * 
	 * @param pen the element
	 * @param property the id of the property to set
	 * @param value the value of the property
	 */
	private void setLinePenAttribute(JRBoxPen pen, String property, Object value){
		if (JRBasePen.PROPERTY_LINE_STYLE.equals(property)) pen.setLineStyle(intToLinestyleEnum((Integer)value));
		else if (JRBasePen.PROPERTY_LINE_COLOR.equals(property)) {
			AlfaRGB argbColor = (AlfaRGB) value;
			pen.setLineColor(new Color(argbColor.getRgb().red,argbColor.getRgb().green,argbColor.getRgb().blue,argbColor.getAlfa()));
		} else if (JRBasePen.PROPERTY_LINE_WIDTH.equals(property)) pen.setLineWidth((Float)value); 
	}
	
	/**
	 * Convert a border location to the corresponding line pen location
	 * @param loc location of a border
	 * @return a line pen location
	 */
	private JRBoxPen locationToLine(Location loc){
		JRLineBox borders = element.getBorders();
		if (loc == Location.TOP) return borders.getTopPen();
		else if (loc == Location.BOTTOM) return borders.getBottomPen();
		else if (loc == Location.RIGHT) return borders.getRightPen();
		else return borders.getLeftPen();
	}
	
	/**
	 * Update the value on the right panel with the selected line data
	 */
	private void updateRightPanel(){
		JRLineBox borders = element.getBorders();
		if (borders != null) {
			if (bd.getLastSelected() != null && bd.getLastSelected().getSelected()) {
				refreshLinePen(locationToLine(bd.getLastSelected().getLocation()));
			}
			else if (bd.isTopSelected()) {
				refreshLinePen(borders.getTopPen());
			}
			else if (bd.isBottomSelected()) {
				refreshLinePen(borders.getBottomPen());
			}
			else if (bd.isLeftSelected()) {
				refreshLinePen(borders.getLeftPen());
			}
			else if (bd.isRightSelected()) {
				refreshLinePen(borders.getRightPen());
			}
			else {
				//No border is selected, set the control to the default value
				lineColor.setColor(AlfaRGB.getFullyOpaque(new RGB(0, 0, 0)));
				lineWidth.setValues(0, 0, 5000, 1, 1, 1);
				lineStyle.select(1);
			}
		}
	}
	
	/**
	 * Refresh the right panel with the values of the selected border. If more than one border is selected 
	 * will be shown the values of one of them, choose as follow:
	 * -The last border selected
	 * -If the last border selected was deselected the first from the other SELECTED borders will be choose. The border
	 * examination order is TOP,BOTTOM,LEFT,RIGHT
	 * -If no border is selected will default values will be used
	 */
	public void refresh() {
		refreshPadding();
		if (square != null)
			square.redraw();
	}

	/**
	 * Return the padding value
	 * @param padding and object representing the padding value, could be null
	 * @return and integer version  of the padding value if it isn't null, otherwise false
	 */
	private Integer getPaddingValue(Object padding){
		return (padding != null ? (Integer)padding : 0);
	}
	
	/**
	 * Refresh the padding information
	 */
	public void refreshPadding() {
		JRLineBox box = element.getBorders();
		if (box.getOwnPadding() != null){
			checkBoxPadding.setSelection(true);
			Integer value = box.getPadding();
			paddingTop.setSelection(value);
			paddingBottom.setSelection(value);
			paddingLeft.setSelection(value);
			paddingRight.setSelection(value);
		} else {
			checkBoxPadding.setSelection(false);
			paddingTop.setSelection(getPaddingValue(box.getOwnTopPadding()));
			paddingBottom.setSelection(getPaddingValue(box.getOwnBottomPadding()));
			paddingLeft.setSelection(getPaddingValue(box.getOwnLeftPadding()));
			paddingRight.setSelection(getPaddingValue(box.getOwnRightPadding()));
		}
		checkBoxValueChange();
	}

	
	/**
	 * Change the padding property of the style
	 */
	public void changePropertyPadding(){
		JRLineBox box = element.getBorders();
		if (checkBoxPadding.getSelection()){
			box.setPadding(paddingLeft.getSelection());
			box.setBottomPadding(null);
			box.setLeftPadding(null);
			box.setRightPadding(null);
			box.setTopPadding(null);
		} else {
			box.setPadding(null);
			box.setBottomPadding(paddingBottom.getSelection());
			box.setLeftPadding(paddingLeft.getSelection());
			box.setRightPadding(paddingRight.getSelection());
			box.setTopPadding(paddingTop.getSelection());
		}
	}
	
	/**
	 * Call all the registered selection listener 
	 * 
	 * @param widget widget where the selection action is happened
	 * @param property property cahnged by the action
	 */
	private void callListeners(Control widget, String property){
		 Event e = new Event();
		 e.widget = widget;
		 SelectionEvent sEvent = new SelectionEvent(e);
		 sEvent.data = property;
		 for(SelectionListener listener : selectionListeners){
			 listener.widgetSelected(sEvent);
		 }
	}

	/**
	 * Update the right panel with the value of a linepen, but only if it's visible
	 * @param lb
	 * @param property
	 */
	public void refreshLinePen(JRBoxPen lp) {
		if (lp != null) {
			Float propertyValue = lp.getOwnLineWidth() != null ? lp.getOwnLineWidth() : 0;
			if (propertyValue>0){
				//Set the border data only if it is visible
				if (lineWidth != null && !lineWidth.isDisposed()) {
					UIUtil.setSpinnerSelection(lineWidth, null, (int) ((propertyValue == null) ? 0 : propertyValue.doubleValue()
							* Math.pow(10, 1)));
				}
	
				if (lineStyle != null && !isDisposed()) {
					int ls = lp.getOwnLineStyleValue() != null ? lineStyletoInt(lp.getOwnLineStyleValue()): 0;
					lineStyle.select(ls);
				}
				Color awtColor = lp.getOwnLineColor();
				AlfaRGB backcolor = awtColor != null ? new AlfaRGB(new RGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue()), awtColor.getAlpha()) : AlfaRGB.getFullyOpaque(new RGB(0,0,0));
				if (lineColor != null){
					lineColor.setColor(backcolor);
				}
			}
		}
	}
	

}
