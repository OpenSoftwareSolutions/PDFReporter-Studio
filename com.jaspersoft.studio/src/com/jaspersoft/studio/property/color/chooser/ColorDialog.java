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
package com.jaspersoft.studio.property.color.chooser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * An advanced color dialog that offers more functionalities towards the default one,
 * like the transparency and many other. It also provide a system independent way to choose the color. 
 * Different input mode are inserted into more tabs inside the dialog
 * 
 * @author Orlandin Marco
 *
 */
public class ColorDialog extends Dialog{

	/**
	 * String title of the dialog of the chooser
	 */
	private String shellTitle = null;
	
	/**
	 * Controls to define the color in an advanced mode
	 */
	private AdvancedColorWidget advancedColors = null;
	
	/**
	 * Controls to define the color from a color palette
	 */
	private WebColorsWidget webColors  = null;
	
	/**
	 * Control provider the select the previusly colors
	 */
	private LastUsedColorsWidget lastColors = null;
	
	/**
	 * List of all the available controls provider
	 */
	private List<IColorProvider> colorsWidgets = new ArrayList<IColorProvider>();
	
	/**
	 * Folder where all the controls provider are placed
	 */
	private TabFolder folder;
	
  /**
   * Flag used to hide the alpha controls
   */
  private boolean hasAlpha = true;
  
  /**
   * index of the selected tab (so the selected input method)
   */
  private int selectedTab = 0;
	
	/**
	 * Color that we are changing from the color dialog, used to compare the new color 
	 * to the old one. If it is null it is not shown
	 */
	private AlfaRGB oldColor = null;
	
	/**
	 * Construct an instance of the class
	 * 
	 * @param parent shell for the new dialog
	 */
	public ColorDialog(Shell parent){
		super(parent);
	}

	
	/**
	 * Configure the shell to set the defined title if it is not null
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		if (shellTitle != null){
			newShell.setText(shellTitle);
		}
	}
	
	/**
	 * Explicitly call the dispose of the color provider when the dialog
	 * is closed
	 */
	private void disposeControlsProvider(){
		for (IColorProvider provider : colorsWidgets){
			provider.dispose();
		}
	}
	
	/**
	 * Open the dialog an return the selected color when it is closed. If nothing
	 * is closed return null
	 * 
	 * @return an AlfaRGB color with the selected color if the dialog is closed
	 * with ok or null if it is closed with cancel
	 */
	public AlfaRGB openAlfaRGB(){
		int returnCode = super.open();
		disposeControlsProvider();
		if (returnCode == Dialog.CANCEL) return null;
		else {
			AlfaRGB newColor = colorsWidgets.get(selectedTab).getSelectedColor();
			LastUsedColorsWidget.addColor(newColor);

			return newColor;
		}
	}
	
	/**
	 * Open the dialog an return the selected color when it is closed. If nothing
	 * is closed return null. The control to define the alpha of the color are hidden.
	 * If the user try to get the alpha it return always 255
	 * 
	 * @return an RGB color with the selected color if the dialog is closed
	 * with ok or null if it is closed with cancel
	 */
	public RGB openRGB(){
		//When a simple RGB is requested hide the alpha control
		hasAlpha = false;
		int returnCode = super.open();
		disposeControlsProvider();
		if (returnCode == Dialog.CANCEL) return null;
		else {
			AlfaRGB newColor = colorsWidgets.get(selectedTab).getSelectedColor();
			LastUsedColorsWidget.addColor(newColor);
			return newColor != null ? newColor.getRgb() : null;
		}
	}
	
	/**
	 * Set the dialog title text. This must be set before open it to make it visible
	 * 
	 * @param title text to set in the dialog title
	 */
	public void setText(String title){
		shellTitle = title;
	}
	
	/**
	 * Set the actual color that will compared into the preview are with the new one
	 * 
	 * @param color rgb of the old color
	 */
	public void setRGB(RGB color){
		if (color != null){
			oldColor = new AlfaRGB(color, 255);
		}
	}
	
	/**
	 * Set the actual color that will compared into the preview are with the new one
	 * 
	 * @param color alfaRGB of the old color
	 */
	public void setRGB(AlfaRGB color){
		if (color != null){
			oldColor = color;
		}
	}
	
	
	/**
	 * Create the controls of the dialog
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		 Composite dialogArea = (Composite) super.createDialogArea(parent);
		 folder = new TabFolder(dialogArea, SWT.NONE);
		 advancedColors = new AdvancedColorWidget(folder, SWT.NONE, oldColor, hasAlpha);
		 advancedColors.setLayoutData(new GridData(GridData.FILL_BOTH));
		 TabItem tab1 = new TabItem(folder, SWT.NONE);
		 tab1.setText(Messages.ColorDialog_advancedColorsLabel);
		 tab1.setControl(advancedColors);
		 colorsWidgets.add(advancedColors);
		 
		 TabItem tab2 = new TabItem(folder, SWT.NONE);
		 tab2.setText(Messages.ColorDialog_webColorsLabel);
		 webColors = new WebColorsWidget(folder, SWT.NONE, oldColor);
		 tab2.setControl(webColors);
		 colorsWidgets.add(webColors);
		 
		 if (LastUsedColorsWidget.hasColors()){
			 lastColors = new LastUsedColorsWidget(folder, SWT.NONE, oldColor);
			 TabItem tab3 = new TabItem(folder, SWT.NONE);
			 tab3.setText(Messages.ColorDialog_lastUserdColorLabel);
			 tab3.setControl(lastColors);
			 colorsWidgets.add(lastColors);
		 }
		 
		 
		 folder.addSelectionListener(new SelectionAdapter() {
			 @Override
			public void widgetSelected(SelectionEvent e) {
				selectedTab = folder.getSelectionIndex();
			}
		 });
		 return folder;
	}
	
}
