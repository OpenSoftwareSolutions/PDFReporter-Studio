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
package com.jaspersoft.studio.translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.babel.editor.widgets.LocaleSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.utils.ImageUtils;

/**
 * This class extend the locale selector component of the bundle editor plugin.
 * The extension proved the support to the icon for a locale. Some icon are provided
 * by default, but can be changed by the user using a button to select a supported image.
 * If the image is too big to be an icon it will be resized keeping the aspect ration
 * 
 * @author Orlandin Marco
 *
 */
public class FlagLocaleSelector extends LocaleSelector{

	/**
	 * Label where the icon image is set
	 */
	private Label flagImage;
	
	/**
	 * Button used to browse for a new icon
	 */
	private Button changeFlagImage;
	
	/**
	 * string that identify the locale associated with the image
	 */
	private String actualLocaleImage = null;
	
	public FlagLocaleSelector(Composite parent) {
		super(parent);
		selectionGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		Composite flagComposite = new Composite(selectionGroup, SWT.NONE);
		flagComposite.setLayout(new GridLayout(2,false));
		flagComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
		
		flagImage = new Label(flagComposite, SWT.NONE);
		
		changeFlagImage = new Button(flagComposite, SWT.NONE);
		changeFlagImage.setText("Set Flag Icon");
		changeFlagImage.addSelectionListener(new SelectionAdapter(){
			
			/**
			 * Open the browser and on the selection load the image, resize it if necessary
			 * and update the label
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
	        FileDialog fd = new FileDialog(UIUtils.getShell(), SWT.OPEN);
	        fd.setText("Save");
	        String[] filterExt = { "*.jpg", "*.png", ".gif" };
	        fd.setFilterExtensions(filterExt);
	        String selected = fd.open();
	        if (selected != null){
	        	try {
	        		Image loadedImage = new Image(null, new FileInputStream(new File(selected)));
	        		//Resize the image if it is too big
	        		int width = loadedImage.getImageData().width;
	        		int height = loadedImage.getImageData().height;
	        		if (width > 16){
	        			int scaleFactor = width/16;
	        			width = width/scaleFactor;
	        			height = height / scaleFactor;
	        		}
	        		if (height > 11){
	        			int scaleFactor = height/11;
	        			width = width/scaleFactor;
	        			height = height / scaleFactor;
	        		}
	        		if (width != loadedImage.getImageData().width || height != loadedImage.getImageData().height){
	        			Image biggerImage = loadedImage;
	        			loadedImage = ImageUtils.resize(loadedImage, width, height);
	        			biggerImage.dispose();
	        		}
	        		disposeOldFlagImage();
							flagImage.setImage(loadedImage);
							changeFlagImage.setText("Change flag image");
							flagImage.getParent().layout(true,true);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
	        }
			}
			
		});
		
	}
	
	private void disposeOldFlagImage(){
		Image flag = flagImage.getImage();
		if (flag != null) flag.dispose();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		disposeOldFlagImage();
	}
	
	/**
	 * Change the actually displayed image
	 * 
	 * @param image the new image
	 * @param actualLocale locale associated with the new image
	 */
	public void updateImage(ImageData image, String actualLocale){
		disposeOldFlagImage();
		if (image != null) flagImage.setImage(new Image(UIUtils.getDisplay(), image));
		else flagImage.setImage(null);
		actualLocaleImage = actualLocale;
		if (image == null) changeFlagImage.setText("Set flag image");
		else changeFlagImage.setText("Change flag image");
		flagImage.getParent().layout(true,true);
	}
	
	/**
	 * Return the actual flag image
	 * 
	 * @return the actual locale image, can be null
	 */
	public ImageData getActualImage(){
		if (flagImage.getImage() == null) return null;
		return flagImage.getImage().getImageData();
	}
	
	/**
	 * Return the language of the actual locale
	 * 
	 * @return a two chars string representing a locale language
	 */
	public String getLangText(){
		return langText.getText();
	}
	
	/**
	 * Return the country of the actual locale
	 * 
	 * @return a two chars string representing a locale country
	 */
	public String getCountryText(){
		return countryText.getText();
	}
	
	/**
	 * Return the identifier of the locale associated with the actual image
	 * 
	 * @return a locale as string
	 */
	public String getActualLocaleImage(){
		return actualLocaleImage;
	}
	
}
