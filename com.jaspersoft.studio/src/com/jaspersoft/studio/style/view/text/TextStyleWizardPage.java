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

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceCache;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;
import com.jaspersoft.studio.property.color.chooser.ColorDialog;
import com.jaspersoft.studio.property.descriptor.color.ColorLabelProvider;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Wizard page that offers the controls to edit or create a textual style
 * 
 * @author Orlandin Marco
 *
 */
public class TextStyleWizardPage extends JSSHelpWizardPage {

	private TextStyle element = null;
	
	private Combo fontName;
	
	private Combo fontSize;
	
	private ToolItem bold;
	
	private ToolItem italic;
	
	private ToolItem underline;
	
	private ToolItem strikeTrought;
	
	private ToolItem leftHAlignment;
	
	private ToolItem rightHAlignment;
	
	private ToolItem centerHAlignment;
	
	private ToolItem justifiedHAlignment;
	
	private ToolItem topVAlignment;
	
	private ToolItem bottomVAlignment;
	
	private ToolItem middleVAlignment;
	
	private Text description;
	
	private ToolItem rotationNone;
	
	private ToolItem rotationUpsideDown;
	
	private ToolItem rotationLeft;
	
	private ToolItem rotationRight;
	
	private BordersWidget bordersWidget;
	
	private ToolItem forecolor;
	
	private ToolItem backcolor;
	
	private Button transparent;
	
	private Composite preview;
	
	private ColorLabelProvider colorLabelProvider = new ColorLabelProvider(null);
	
	private ResourceCache imagesCache = new ResourceCache();
	
	private boolean settingData = false;
	
	private Point previewDefaultSize = new Point(400, 50);
	
	private ModifyListener valueModifyListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			refreshEvent();
		}
	};
	
	private SelectionAdapter valueSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			refreshEvent();
		};
	};
	
	private SelectionAdapter disableAllOtherButtons = new SelectionAdapter() {
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			ToolItem selectedItem = (ToolItem)e.widget;
			for(ToolItem item : selectedItem.getParent().getItems()){
				if (item != selectedItem) item.setSelection(false);
			}
			refreshEvent();
		};
	};
	
	
	private void refreshEvent(){
		synchronized (this) {
			if (!settingData && !fontSize.getText().isEmpty()){
				updateStyle();
				refreshPreview();
			}
		}
	}
	
	protected TextStyleWizardPage(String pageName) {
		super(pageName);
		setTitle(Messages.TextStyleWizardPage_pageTitle);
		setDescription(Messages.TextStyleWizardPage_pageDescription);
	}
	
	protected TextStyleWizardPage(String pageName, TextStyle templateToOpen) {
		this(pageName);
		if (templateToOpen != null){
			element = templateToOpen.clone();
			String text = element.getDescription();
			if (text == null || text.isEmpty()) text = Messages.TextStyleView_sampleText; 
			element.setDescription(text);
		}
	}
	
	public void setElement(TextStyle element){
		if (element != null){
			this.element = element.clone();
			String text = this.element.getDescription();
			if (text == null || text.isEmpty()) text = Messages.TextStyleView_sampleText; 
			this.element.setDescription(text);
			setData();
		}
	}
	
	@Override
	public void createControl(Composite parent) {
		if (element == null)	element = createDefaultElement();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite descriptionArea = new Composite(container, SWT.NONE);
		descriptionArea.setLayout(new GridLayout(2,false));
		descriptionArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,2, 1));
		new Label(descriptionArea,SWT.NONE).setText(Messages.TextStyleView_sampleText);
		description = new Text(descriptionArea, SWT.BORDER);
		description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		description.addModifyListener(valueModifyListener);
		
		Composite leftArea = new Composite(container, SWT.NONE);
		leftArea.setLayout(new GridLayout(1,false));
		leftArea.setLayoutData(new GridData(GridData.FILL_BOTH));		
		
		Group fontAttributes = new Group(leftArea, SWT.NONE);
		fontAttributes.setLayout(new GridLayout(1,false));
		fontAttributes.setLayoutData(new GridData(GridData.FILL_BOTH));
		fontAttributes.setText(Messages.common_font);
		createFontSection(fontAttributes);
		createAlignments(fontAttributes);
		createRotation(fontAttributes);
		
		Group colorAttributes = new Group(leftArea, SWT.NONE);
		colorAttributes.setLayout(new GridLayout(1,false));
		colorAttributes.setLayoutData(new GridData(GridData.FILL_BOTH));
		colorAttributes.setText(Messages.ColorsSection_colorSectionTitle);
		createColors(colorAttributes);
		
		Composite rightArea = new Composite(container, SWT.NONE);
		rightArea.setLayout(new GridLayout(1,false));
		rightArea.setLayoutData(new GridData(GridData.FILL_BOTH));		

		bordersWidget = new BordersWidget(rightArea, SWT.NONE, element);
		bordersWidget.setLayoutData(new GridData(GridData.FILL_BOTH));
		bordersWidget.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				refreshPreview();
			};
		});
		
		Group previewArea = new Group(container, SWT.NONE);
		previewArea.setText(Messages.TextStyleWizardPage_previewLabel);
		previewArea.setLayout(new GridLayout(1,false));
		previewArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,2, 1));
		preview = new Composite(previewArea, SWT.NONE);
		GridData previewData = new GridData();
		previewData.widthHint = previewDefaultSize.x;
		previewData.heightHint = previewDefaultSize.y;
		previewData.horizontalAlignment = SWT.CENTER;
		previewData.verticalAlignment = SWT.FILL;
		previewData.grabExcessHorizontalSpace = true;
		previewData.grabExcessVerticalSpace = true;
		preview.setLayoutData(previewData);
		previewArea.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				refreshPreview();
			}
		});
		
		setControl(container);		
		setData();
	}
	
	private void refreshPreview(){
		Rectangle bounds = preview.getBounds();
		int width = bounds.width;
		int height = bounds.height;
		if (width < 1 || height <1) {
			width = previewDefaultSize.x;
			height = previewDefaultSize.y;
		}
		preview.setBackgroundImage(imagesCache.getImage(PreviewGenerator.generatePreview(element, width, height, preview.getBackground().getRGB())));
	}
	
	@Override
	public void dispose() {
		updateStyle();
		super.dispose();
		colorLabelProvider.dispose();
		imagesCache.dispose();
	}
	
	private void updateStyle(){
		JRFont font = element.getFont();
		font.setBold(bold.getSelection());
		font.setItalic(italic.getSelection());
		font.setUnderline(underline.getSelection());
		font.setStrikeThrough(strikeTrought.getSelection());
		font.setFontName(fontName.getText());
		String fontSizeText = fontSize.getText();
		float size = Float.valueOf(fontSizeText);
		font.setFontSize(size);
		
		element.setHorizontalAlignmen(getHorizonltalAlignment());
		element.setVerticalAlignmen(getVerticalAlignment());
		element.setRotation(getRotation());
		
		element.setBackGround((AlfaRGB)backcolor.getData());
		element.setForeGround((AlfaRGB)forecolor.getData());
		element.setTransparent(transparent.getSelection());
		
		element.setDescription(description.getText());
	}
	
	private void createFontSection(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		container.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		fontName = new Combo(container, SWT.NONE);
		fontName.setItems(FontUtils.stringToItems(getFontNames()));
		fontName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fontName.addModifyListener(valueModifyListener);
		fontSize = new Combo(container, SWT.NONE);
		fontSize.setItems(ModelUtils.FONT_SIZES);
		fontSize.addModifyListener(valueModifyListener);
		Composite styleButtons = new Composite(container, SWT.NONE);
		styleButtons.setLayout(new GridLayout(1,false));
		ToolBar bar = new ToolBar(styleButtons, SWT.NONE);
		bold = new ToolItem(bar, SWT.CHECK);
		bold.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/edit-bold.png")); //$NON-NLS-1$
		bold.addSelectionListener(valueSelectionListener);
		italic = new ToolItem(bar, SWT.CHECK);
		italic.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/edit-italic.png")); //$NON-NLS-1$
		italic.addSelectionListener(valueSelectionListener);
		underline = new ToolItem(bar, SWT.CHECK);
		underline.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/edit-underline.png")); //$NON-NLS-1$
		underline.addSelectionListener(valueSelectionListener);
		strikeTrought = new ToolItem(bar, SWT.CHECK);
		strikeTrought.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/edit-strike.png")); //$NON-NLS-1$
		strikeTrought.addSelectionListener(valueSelectionListener);
	}
	
	private void createAlignments(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3,false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(container, SWT.NONE).setText(Messages.AlignSection_common_align);
		
		ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		leftHAlignment =  new ToolItem(toolBar, SWT.CHECK);
		leftHAlignment.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/left_align.gif")); //$NON-NLS-1$
		leftHAlignment.setToolTipText(Messages.TextStyleWizardPage_alignLeftTooltip);
		leftHAlignment.addSelectionListener(disableAllOtherButtons);
		
		centerHAlignment =  new ToolItem(toolBar, SWT.CHECK);
		centerHAlignment.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/center_align.gif")); //$NON-NLS-1$
		centerHAlignment.setToolTipText(Messages.TextStyleWizardPage_alignCenterTooltip);
		centerHAlignment.addSelectionListener(disableAllOtherButtons);
		
		rightHAlignment =  new ToolItem(toolBar, SWT.CHECK);
		rightHAlignment.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/right_align.gif")); //$NON-NLS-1$
		rightHAlignment.setToolTipText(Messages.TextStyleWizardPage_alignRightTooltip);
		rightHAlignment.addSelectionListener(disableAllOtherButtons);
		
		justifiedHAlignment =  new ToolItem(toolBar, SWT.CHECK);
		justifiedHAlignment.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/justified_align.gif")); //$NON-NLS-1$
		justifiedHAlignment.setToolTipText(Messages.TextStyleWizardPage_justifyHTooltip);
		justifiedHAlignment.addSelectionListener(disableAllOtherButtons);
		
		toolBar = new ToolBar(container, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		topVAlignment =  new ToolItem(toolBar, SWT.CHECK);
		topVAlignment.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/formatting/edit-vertical-alignment-top.png")); //$NON-NLS-1$
		topVAlignment.setToolTipText(Messages.TextStyleWizardPage_alignTopTooltip);
		topVAlignment.addSelectionListener(disableAllOtherButtons);
		
		middleVAlignment =  new ToolItem(toolBar, SWT.CHECK);
		middleVAlignment.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/formatting/edit-vertical-alignment-middle.png")); //$NON-NLS-1$
		middleVAlignment.setToolTipText(Messages.TextStyleWizardPage_alignMiddleTooltip);
		middleVAlignment.addSelectionListener(disableAllOtherButtons);
		
		bottomVAlignment =  new ToolItem(toolBar, SWT.CHECK);
		bottomVAlignment.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/formatting/edit-vertical-alignment.png")); //$NON-NLS-1$
		bottomVAlignment.setToolTipText(Messages.TextStyleWizardPage_alignBottomTooltip);
		bottomVAlignment.addSelectionListener(disableAllOtherButtons);
	}
	
	private void createRotation(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(container, SWT.NONE).setText(Messages.common_rotation);
		
		ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		rotationNone =  new ToolItem(toolBar, SWT.CHECK);
		rotationNone.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/text-direction-none.png")); //$NON-NLS-1$
		rotationNone.setToolTipText(Messages.TextStyleWizardPage_rotationNoneTooltip);
		rotationNone.addSelectionListener(disableAllOtherButtons);
		
		rotationLeft =  new ToolItem(toolBar, SWT.CHECK);
		rotationLeft.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/text-direction-left.png")); //$NON-NLS-1$
		rotationLeft.setToolTipText(Messages.TextStyleWizardPage_rotationLeftTooltip);
		rotationLeft.addSelectionListener(disableAllOtherButtons);
		
		rotationRight =  new ToolItem(toolBar, SWT.CHECK);
		rotationRight.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/text-direction-right.png")); //$NON-NLS-1$
		rotationRight.setToolTipText(Messages.TextStyleWizardPage_rotationRightTooltip);
		rotationRight.addSelectionListener(disableAllOtherButtons);
		
		rotationUpsideDown =  new ToolItem(toolBar, SWT.CHECK);
		rotationUpsideDown.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/text-direction-updown.png")); //$NON-NLS-1$
		rotationUpsideDown.setToolTipText(Messages.TextStyleWizardPage_upsideDownTooltip);
		rotationUpsideDown.addSelectionListener(disableAllOtherButtons);
	}
	
	private void createColors(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4,false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(container, SWT.NONE).setText(Messages.common_forecolor);
		ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.WRAP | SWT.LEFT);
		toolBar.setBackground(parent.getBackground());
		forecolor = createColorButton(toolBar);
		toolBar.pack();
		new Label(container, SWT.NONE).setText(Messages.common_backcolor);
		toolBar = new ToolBar(container, SWT.FLAT | SWT.WRAP | SWT.LEFT);
		toolBar.setBackground(parent.getBackground());
		backcolor = createColorButton(toolBar);
		
		transparent = new Button(container, SWT.CHECK);
		transparent.setText(Messages.common_opaque);
		transparent.addSelectionListener(valueSelectionListener);
	}
	
	private HorizontalAlignEnum getHorizonltalAlignment(){
		if (rightHAlignment.getSelection()) return HorizontalAlignEnum.RIGHT;
		else if (centerHAlignment.getSelection()) return HorizontalAlignEnum.CENTER;
		else if (justifiedHAlignment.getSelection()) return HorizontalAlignEnum.JUSTIFIED;
		else return HorizontalAlignEnum.LEFT;
	}
	
	private void setHorizonltalAlignment(){
		HorizontalAlignEnum align = element.getHorizontalAlignmen();
		if (HorizontalAlignEnum.RIGHT.equals(align)) rightHAlignment.setSelection(true);
		else if (HorizontalAlignEnum.CENTER.equals(align)) centerHAlignment.setSelection(true);
		else if (HorizontalAlignEnum.JUSTIFIED.equals(align)) justifiedHAlignment.setSelection(true);
		else  leftHAlignment.setSelection(true);
	}
	
	private VerticalAlignEnum getVerticalAlignment(){
		if (middleVAlignment.getSelection()) return VerticalAlignEnum.MIDDLE;
		else if (bottomVAlignment.getSelection()) return VerticalAlignEnum.BOTTOM;
		else return VerticalAlignEnum.TOP;
	}
	
	private void setVerticalAlignment(){
		VerticalAlignEnum align = element.getVerticalAlignmen();
		if (VerticalAlignEnum.MIDDLE.equals(align)) middleVAlignment.setSelection(true);
		else if (VerticalAlignEnum.BOTTOM.equals(align)) bottomVAlignment.setSelection(true);
		else topVAlignment.setSelection(true);
	}
	
	private RotationEnum getRotation(){
		if (rotationLeft.getSelection()) return RotationEnum.LEFT;
		else if (rotationRight.getSelection()) return RotationEnum.RIGHT;
		else if (rotationUpsideDown.getSelection()) return RotationEnum.UPSIDE_DOWN;
		else return RotationEnum.NONE;
	}
	
	private void setRotation(){
		RotationEnum align = element.getRotation();
		if (RotationEnum.LEFT.equals(align)) rotationLeft.setSelection(true);
		else if (RotationEnum.RIGHT.equals(align)) rotationRight.setSelection(true);
		else if (RotationEnum.UPSIDE_DOWN.equals(align)) rotationUpsideDown.setSelection(true);
		else rotationNone.setSelection(true);
	}
	
	private void setData(){
		synchronized (this) {
			settingData = true;
			JRFont font = element.getFont();
			fontName.setText(font.getOwnFontName());
			fontSize.setText(String.valueOf(font.getOwnFontsize()));
			bold.setSelection(font.isOwnBold());
			italic.setSelection(font.isOwnItalic());
			underline.setSelection(font.isOwnUnderline());
			strikeTrought.setSelection(font.isOwnStrikeThrough());
			description.setText(element.getDescription());
			setHorizonltalAlignment();
			setVerticalAlignment();
			setRotation();
			setColors();
			transparent.setSelection(element.isTransparent());
			bordersWidget.refresh();
			settingData = false;
		}
	}
	
	private void setColors(){
		AlfaRGB foreColor = element.getForeGround();
		forecolor.setData(foreColor);
		forecolor.setImage(colorLabelProvider.getImage(foreColor));
		AlfaRGB backColor = element.getBackGround();
		backcolor.setData(backColor);
		backcolor.setImage(colorLabelProvider.getImage(backColor));
	}
	
	private static java.util.List<String[]> getFontNames() {
		java.util.List<String[]> classes = new ArrayList<String[]>();
		java.util.List<String> elements = new ArrayList<String>();
		classes.add(elements.toArray(new String[elements.size()]));
		elements = new ArrayList<String>();
		String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			elements.add(name);
		}
		classes.add(elements.toArray(new String[elements.size()]));
		return classes;
	}
		
	private TextStyle createDefaultElement(){
		TextStyle defaultTextStyle = new TextStyle();
		defaultTextStyle.setBackGround(AlfaRGB.getFullyOpaque(new RGB(255,255,255)));
		defaultTextStyle.setForeGround(AlfaRGB.getFullyOpaque(new RGB(0,0,0)));
		defaultTextStyle.setHorizontalAlignmen(HorizontalAlignEnum.LEFT);
		defaultTextStyle.setVerticalAlignmen(VerticalAlignEnum.TOP);
		defaultTextStyle.setTransparent(true);
		defaultTextStyle.setRotation(RotationEnum.NONE);
		
		JRBaseFont font = new JRBaseFont();
		font.setFontName("Arial"); //$NON-NLS-1$
		font.setFontSize(8f);
		font.setBold(false);
		font.setItalic(false);
		font.setUnderline(false);
		font.setStrikeThrough(false);
		defaultTextStyle.setFont(font);

		
		JRBaseLineBox box = new JRBaseLineBox(null);
		box.setPadding(null);
		box.setTopPadding(0);
		box.setBottomPadding(0);
		box.setLeftPadding(0);
		box.setRightPadding(0);

		defaultTextStyle.setBorders(box);
		defaultTextStyle.setDescription(Messages.TextStyleView_sampleText);
		
		return defaultTextStyle;
	}
	
	private ToolItem createColorButton(ToolBar toolBar){
		final ToolItem button = new ToolItem(toolBar, SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(button.getParent().getShell());
				cd.setText(Messages.TextStyleWizardPage_colorLabel);
				AlfaRGB rgb = (AlfaRGB) button.getData();
				cd.setRGB(rgb == null ? null : rgb);
				AlfaRGB newColor = cd.openAlfaRGB();
				if (newColor != null) {
					button.setData(newColor);
					button.setImage(colorLabelProvider.getImage(newColor));
					updateStyle();
					refreshPreview();
				}
			}
		});	
		return button;
	}
	
	public TextStyle getStyle(){
		return element;
	}

	@Override
	protected String getContextName() {
		return null;
	}

}
