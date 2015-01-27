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
package com.jaspersoft.studio.toolbars;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.List;

import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.text.MTextElement;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Controls to change the textual attributes of the selected elements
 * 
 * @author Orlandin Marco
 *
 */
public class TextualContributionItem extends CommonToolbarHandler {
	
	/**
	 * The node actually selected which attributes are shown in the controls
	 */
	private APropertyNode showedNode = null;

	/**
	 * Main container of the controls
	 */
	private Composite controlsArea;
	
	/**
	 * Combo with the font names
	 */
	private Combo fontName;
	
	//Controls for the font size combo
	
	/**
	 * Combo with the font sizes
	 */
	private Combo fontSizeCombo;
	
	/**
	 * The combo background default color
	 */
	private Color comboBackgroundDefault;
	
	//Controls for the font size buttons
	
	/**
	 * Toolitem to increment the text size
	 */
	private ToolItem incrementButton;
	
	/**
	 * % factor for the increment\decrement
	 */
	public static Integer factor = 10;
	
	//Controls for the font style
	
	/**
	 * Toolitem to set the text to bold
	 */
	private ToolItem bold;
	
	/**
	 * Toolitem to set the text to italic
	 */
	private ToolItem italic;
	
	/**
	 * Flag to ignore the change listeners when the state is refreshing 
	 */
	private boolean refreshing = false;
	
	/**
	 * The last font list set on the combo. Set or read the font list directly from the 
	 * combo has a little overhead because the status of the widget is check (about 100ms in some cases)
	 * so it is better to compare it with this one first
	 */
	private String[] fontList = null;
	
	//Used listener
	
	/**
	 * 
	 * Listener used to update the status of the Font name, size bold and italic
	 * when they change inside the the element selected (maybe because they are 
	 * changed from the properties view)
	 */
	private PropertyChangeListener nodeChangeListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (fontSizeCombo == null || fontSizeCombo.isDisposed() || fontName.isDisposed())
				return;

			refreshing = true;
			List<Object> selection = getSelectionForType(MTextElement.class);
			if (selection.size() == 1) {
				APropertyNode node = (APropertyNode) selection.get(0);
				if (evt.getPropertyName().equals(JRDesignStyle.PROPERTY_FONT_NAME)) {
					setFontNameText(node.getPropertyActualValue(JRDesignStyle.PROPERTY_FONT_NAME));
				} else if (evt.getPropertyName().equals(JRDesignStyle.PROPERTY_FONT_SIZE)) {
					setFontSizeComboText(node.getPropertyActualValue(JRDesignStyle.PROPERTY_FONT_SIZE));
				} else if (evt.getPropertyName().equals(JRDesignStyle.PROPERTY_ITALIC)) {
					italic.setSelection((Boolean) node.getPropertyActualValue(JRDesignStyle.PROPERTY_ITALIC));
				} else if (evt.getPropertyName().equals(JRDesignStyle.PROPERTY_BOLD)) {
					bold.setSelection((Boolean) node.getPropertyActualValue(JRDesignStyle.PROPERTY_BOLD));
				}
			} else {
				setFontSizeComboText(null);
				setFontNameText(null);
				italic.setSelection(false);
				bold.setSelection(false);
				if (showedNode != null) {
					showedNode.getPropertyChangeSupport().removePropertyChangeListener(nodeChangeListener);
					showedNode = null;
				}
			}
			refreshing = false;
		}
	};
	
	/**
	 * Listener called when the on of the fontsize buttons is pressed
	 */
	private SelectionAdapter fontSizeButtonSelect = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!refreshing){
				List<Object> selection = getSelectionForType(MTextElement.class);
				if (selection.isEmpty())
					return;
				JSSCompoundCommand cc = null;
				if (e.widget == incrementButton){
					cc = createIncremenrtsCommand(true, selection);
				} else {
					cc = createIncremenrtsCommand(false, selection);
				} 				
				getCommandStack().execute(cc);
			}
		}

	};
	
	/**
	 * Listener called when the element selected in the font name combo changes
	 */
	private SelectionAdapter fontNameComboSelect = new SelectionAdapter() {
		
		public void widgetSelected(SelectionEvent e) {
			if (!refreshing){
				List<Object> selection = getSelectionForType(MTextElement.class);
				if (selection.isEmpty())
					return;
				String value = fontName.getText();
				JSSCompoundCommand cc = new JSSCompoundCommand(null);
				for(Object textElement : selection){
					Command changeValueCmd = createCommand(textElement, value, JRDesignStyle.PROPERTY_FONT_NAME);
					if (changeValueCmd != null) {
						cc.add(changeValueCmd);
						cc.setReferenceNodeIfNull(textElement);
					}
				}
				getCommandStack().execute(cc);
			}
		} 
	};
	
	
	/**
	 * Change the font size of one or more elements
	 */
	private ModifyListener fontSizeComboModify = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent e) {
			if (!refreshing){
				List<Object> selection = getSelectionForType(MTextElement.class);
				if (selection.isEmpty())
					return;
				JSSCompoundCommand cc = new JSSCompoundCommand(null);
				String text = fontSizeCombo.getText().trim();
				//If the string ends with the separator probably the user must still insert char, so don't set it 
				if (!(text.endsWith(",") || text.endsWith("."))){
					try{
						Float realValue = Float.valueOf(text.replace(",", "."));
						for (Object obj : selection) {
							Command changeValueCmd = createCommand(obj, realValue.toString(), JRDesignStyle.PROPERTY_FONT_SIZE);
							if (changeValueCmd != null) {
								cc.add(changeValueCmd);
								cc.setReferenceNodeIfNull(obj);
							}
						}
						getCommandStack().execute(cc);
						fontSizeCombo.setBackground(comboBackgroundDefault);
					} catch(NumberFormatException ex){
						//If the value is not a valid number the the background of the textarea became red
						fontSizeCombo.setBackground(ResourceManager.getColor(255, 0, 0));
					}
				}
			}
		}
	};
	
	/**
	 * Listener called when the bold or italic button is pressed
	 */
	private SelectionAdapter booleanButtonSelected = new SelectionAdapter() {
		
		public void widgetSelected(SelectionEvent e) {
			if (!refreshing){
				List<Object> selection = getSelectionForType(MTextElement.class);
				if (selection.isEmpty())
					return;
				Object value =	((ToolItem)e.widget).getSelection();
				Object property = e.widget.getData();
				JSSCompoundCommand cc = new JSSCompoundCommand(null);
				for(Object textElement : selection){
					Command changeValueCmd = createCommand(textElement, value, property);
					if (changeValueCmd != null) {
						cc.add(changeValueCmd);
						cc.setReferenceNodeIfNull(textElement);
					}
				}
				getCommandStack().execute(cc);
			}
		};
	};
	
	/**
	 * Selection listener that create the right command when a button is pushed
	 */
	private SelectionAdapter pushButtonPressed = new SelectionAdapter() {
		
	
		public void widgetSelected(SelectionEvent e) {
			if (!refreshing){
					List<Object> selection = getSelectionForType(MTextElement.class);
					if (selection.isEmpty())
						return;
					
					JSSCompoundCommand changeSizeCommands = new JSSCompoundCommand(null);
					String property = "";
					Object data = e.widget.getData();
					if (data instanceof VerticalAlignEnum) property = JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT;
					else if (data instanceof HorizontalAlignEnum) property = JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT;
					else return;
					for (Object textElement : selection){	
						changeSizeCommands.setReferenceNodeIfNull(textElement);
						Command c = createCommand(textElement, data, property);
						if (c != null) {
							changeSizeCommands.add(c);
						}
					}
					CommandStack cs = getCommandStack();
					cs.execute(changeSizeCommands);
			}
		}
	};
	

	protected Control createControl(Composite parent) {
		super.createControl(parent);
		controlsArea = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		controlsArea.setLayout(layout);
		
		fontList = null;
		fontName = new Combo(controlsArea, SWT.DROP_DOWN);
		fontName.setData(JRDesignStyle.PROPERTY_FONT_NAME);
		fontName.addSelectionListener(fontNameComboSelect);
		setAvailableFonts();
		
		fontSizeCombo = new Combo(controlsArea, SWT.DROP_DOWN);
		fontSizeCombo.setData(JRDesignStyle.PROPERTY_FONT_SIZE);
		fontSizeCombo.setItems(ModelUtils.FONT_SIZES);
		fontSizeCombo.addModifyListener(fontSizeComboModify);

		RowData data = new RowData();
		data.width = 50;
		fontSizeCombo.setLayoutData(data);
		comboBackgroundDefault = fontSizeCombo.getBackground();
		
		ToolBar sizeButtons = new ToolBar(controlsArea, SWT.FLAT | SWT.WRAP);
		incrementButton = createFontSizeButton(true, sizeButtons);
		createFontSizeButton(false, sizeButtons);
		
		//Italic and bold button

		ToolBar buttons = new ToolBar(controlsArea, SWT.FLAT | SWT.WRAP);
		
		bold = new ToolItem(buttons, SWT.CHECK);
		bold.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/font-bold.gif"));		
		bold.setToolTipText("Bold");
		bold.setData(JRDesignStyle.PROPERTY_BOLD);
		bold.addSelectionListener(booleanButtonSelected);
		bold.setWidth(25);
		
		italic = new ToolItem(buttons, SWT.CHECK);
		italic.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/font-italic.gif"));		
		italic.setToolTipText("Italic");
		italic.setData(JRDesignStyle.PROPERTY_ITALIC);
		italic.addSelectionListener(booleanButtonSelected);
		italic.setWidth(25);
		
		//Buttons to set the text alignment
		
		buttons = new ToolBar(controlsArea, SWT.FLAT | SWT.WRAP);
		
		ToolItem alignButton = new ToolItem(buttons, SWT.PUSH);
		alignButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/left_align.gif"));
		alignButton.setData(HorizontalAlignEnum.LEFT);
		alignButton.addSelectionListener(pushButtonPressed);
		
		alignButton = new ToolItem(buttons, SWT.PUSH);
		alignButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/center_align.gif"));
		alignButton.setData(HorizontalAlignEnum.CENTER);
		alignButton.addSelectionListener(pushButtonPressed);
		
		alignButton = new ToolItem(buttons, SWT.PUSH);
		alignButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/right_align.gif"));
		alignButton.setData(HorizontalAlignEnum.RIGHT);
		alignButton.addSelectionListener(pushButtonPressed);
		
		alignButton = new ToolItem(buttons, SWT.PUSH);
		alignButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/justified_align.gif"));
		alignButton.setData(HorizontalAlignEnum.JUSTIFIED);
		alignButton.addSelectionListener(pushButtonPressed);
		
		new ToolItem(buttons, SWT.SEPARATOR);
		
		alignButton = new ToolItem(buttons, SWT.PUSH);
		alignButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/formatting/edit-vertical-alignment-top.png"));
		alignButton.setData(VerticalAlignEnum.TOP);
		alignButton.addSelectionListener(pushButtonPressed);
		
		alignButton = new ToolItem(buttons, SWT.PUSH);
		alignButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/formatting/edit-vertical-alignment-middle.png"));
		alignButton.setData(VerticalAlignEnum.MIDDLE);
		alignButton.addSelectionListener(pushButtonPressed);
		
		alignButton = new ToolItem(buttons, SWT.PUSH);
		alignButton.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/formatting/edit-vertical-alignment.png"));
		alignButton.setData(VerticalAlignEnum.BOTTOM);
		alignButton.addSelectionListener(pushButtonPressed);
		
		setAllControlsData();

		return controlsArea;
	}

	
	/**
	 * Create a single button into the toolbar
	 * 
	 * @param increment true if the button should be used for increment, false otherwise
	 */
	private ToolItem createFontSizeButton(final boolean increment, ToolBar buttons){
		Image imageValue;
		String message;
		if (increment){
			imageValue = JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/edit-size-up.png");
			message = MessageFormat.format(Messages.SPButon_Size_Increment, new Object[]{factor.toString()});
		} else {
			imageValue = JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/edit-size-down.png");
			message = MessageFormat.format(Messages.SPButon_Size_Decrement, new Object[]{factor.toString()});
		}
		ToolItem button = new ToolItem(buttons, SWT.PUSH);
		button.setImage(imageValue);
		button.setToolTipText(message);		
		button.setData(JRDesignStyle.PROPERTY_FONT_SIZE);
		button.setWidth(25);
		button.addSelectionListener(fontSizeButtonSelect);
		return button;
	}
	


	/**
	 * Create a command to change the property of the element
	 * 
	 * @param model the element
	 * @param value the new value
	 * @param property the property
	 * @return the command to change the property
	 */
	protected Command createCommand(Object model, Object value, Object property) {
		if (!(model instanceof IPropertySource))
			return null;
		SetValueCommand cmd = new SetValueCommand();
		cmd.setTarget((IPropertySource) model);
		cmd.setPropertyId(property);
		cmd.setPropertyValue(value);
		return cmd;
	}
	
	/**
	 * Check if the font in the combo should be updated or not
	 * 
	 * @param newFont the new fonts
	 * @return Return true if the new font are different from the fonts previously stored
	 * false otherwise
	 */
	private boolean needFontsUpdate(String[] newFont){
		return (fontList == null || !fontList.equals(newFont));
	}

	/**
	 * Set the available fonts inside the combo for the current report
	 */
	private void setAvailableFonts(){
		List<Object> selection = getSelectionForType(MTextElement.class);
		if (selection.size() > 0){
			APropertyNode node = (APropertyNode)selection.get(0);
			String[] fonts = JaspersoftStudioPlugin.getToolItemsManager().getFonts(node.getJasperConfiguration());
			if (needFontsUpdate(fonts) &&  fontName != null && !fontName.isDisposed()) {
				fontName.setItems(fonts);
				fontList = fonts;
			}
		}
	}


	@Override
	public boolean isVisible() {
		if (!super.isVisible()) return false;
		
		List<Object> selection = getSelectionForType(MTextElement.class);
		boolean selectionValid = selection.size() > 0;
		if (selectionValid){
			setAvailableFonts();
			setAllControlsData();
		} else if (showedNode != null) {
			showedNode.getPropertyChangeSupport().removePropertyChangeListener(nodeChangeListener);
			showedNode = null;
		}
		return selectionValid;
	}
	
	/**
	 * Remove all the decimal zeros from a string. If after the remove the remaining trail char 
	 * is a . the it is also removed
	 * 
	 * @param value a string
	 * @return a string without decimal zeros at the end
	 */
	private String removeUnnecessaryZeros(String value){
		String newValue = value.replaceAll("(\\.(\\d*[1-9])?)0+", "$1");
		if (newValue.endsWith(".")) newValue = newValue.substring(0, newValue.length()-1);
		return newValue;
	}
	
	/**
	 * Set a string inside the font name combo
	 * 
	 * @param value the string
	 */
	protected void setFontNameText(Object value) {
		fontName.setText(Misc.nvl(value, "").toString());
	}

	/**
	 * When the combo text is set the unnecessary decimal zeros are removed
	 */
	protected void setFontSizeComboText(Object value) {
		if (value == null || fontSizeCombo == null || fontSizeCombo.isDisposed())
			return;
		String str = removeUnnecessaryZeros((String) value);
		String[] items = fontSizeCombo.getItems();
		int selection = -1;
		for (int i = 0; i < items.length; i++) {
			if (Misc.compare(items[i], str, false)) {
				selection = i;
				break;
			}
		}
		if (selection != -1) fontSizeCombo.select(selection);
		else fontSizeCombo.setText(Misc.nvl(str, new Integer(10)).toString());
		int stringLength = fontSizeCombo.getText().length();

		fontSizeCombo.setSelection(new Point(stringLength, stringLength));
		fontSizeCombo.getParent().layout(true);
	}
	
	/**
	 * Create the command to change the font size
	 * 
	 * @param increment true if you want to increment the font, false otherwise
	 */
	protected JSSCompoundCommand createIncremenrtsCommand(boolean increment, List<Object> models){
		JSSCompoundCommand changeSizeCommands = new JSSCompoundCommand(null);
		for (Object model : models){
			Object fontSize  = ((APropertyNode)model).getPropertyActualValue(JRBaseFont.PROPERTY_FONT_SIZE);
			Float newValue = 2.0f;
			if (fontSize != null && fontSize.toString().length()>0){
				newValue = Float.valueOf(fontSize.toString());
				Integer plus = null;
				if (increment) plus = Math.round((new Float(newValue) / 100)*factor)+1;
				else plus =  Math.round((new Float(newValue) / 100)*-factor)-1;
				if ((newValue+plus)>99) newValue = 99.0f;
				else if ((newValue+plus)>0) newValue += plus;

				Command c = createCommand(model, newValue.toString(), JRBaseFont.PROPERTY_FONT_SIZE );
				changeSizeCommands.setReferenceNodeIfNull(model);
				if (c != null) {
					changeSizeCommands.add(c);
				}
			}
		}
		return changeSizeCommands;
	}

	/**
	 * Initialize all the controls that show the state of the object with the value of the
	 * selected element
	 */
	protected void setAllControlsData(){
		if (fontSizeCombo == null || fontSizeCombo.isDisposed() || fontName.isDisposed()) return;
		refreshing = true;
		List<Object> selection = getSelectionForType(MTextElement.class);
		if (selection.size() == 1){
			APropertyNode node = (APropertyNode)selection.get(0);
			setFontSizeComboText(node.getPropertyActualValue(JRDesignStyle.PROPERTY_FONT_SIZE));
			setFontNameText(node.getPropertyActualValue(JRDesignStyle.PROPERTY_FONT_NAME));
			italic.setSelection((Boolean) node.getPropertyActualValue(JRDesignStyle.PROPERTY_ITALIC));
			bold.setSelection((Boolean) node.getPropertyActualValue(JRDesignStyle.PROPERTY_BOLD));
			
			if (showedNode != null) showedNode.getPropertyChangeSupport().removePropertyChangeListener(nodeChangeListener);
			showedNode = node;
			showedNode.getPropertyChangeSupport().addPropertyChangeListener(nodeChangeListener);
			
		} else {
			setFontSizeComboText(null);
			setFontNameText(null);
			italic.setSelection(false);
			bold.setSelection(false);
			if (showedNode != null) {
				showedNode.getPropertyChangeSupport().removePropertyChangeListener(nodeChangeListener);	
				showedNode = null;	
			}
		}
		refreshing = false;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (showedNode != null) {
			showedNode.getPropertyChangeSupport().removePropertyChangeListener(nodeChangeListener);
			showedNode = null;
		}
		if (controlsArea != null) {
			controlsArea.dispose();
			controlsArea = null;
		}
		if (comboBackgroundDefault != null){
			comboBackgroundDefault.dispose();
			comboBackgroundDefault = null;
		}
		fontSizeCombo = null;
		bold = null;
		italic = null;
		factor = 10;
		refreshing = false;
	}
}
