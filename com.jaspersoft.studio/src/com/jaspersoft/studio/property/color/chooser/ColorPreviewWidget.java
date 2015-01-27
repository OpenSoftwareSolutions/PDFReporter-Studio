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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.messages.Messages;

/**
 * Composite where an old color is compared with a new one. If the old color is 
 * null then the composite will show only the new color
 * 
 * @author Orlandin Marco
 */
public class ColorPreviewWidget extends Composite{

	/**
	 * Reference to the image inside the previewComposite, this reference is used 
	 * to dispose the controls is disposed
	 */
	private Image actualPreviewImage = null;
	
	/**
	 * Color compared with the new one. If it is null it is not shown
	 */
	private RGB oldColor = null;
	
	/**
	 * Alpha of the color compared with the new one.
	 */
	private int alphaOldColor = 255;
	
	/**
	 * Color compared with the old one
	 */
	private RGB newColor = null;
	
	/**
	 * Alpha of the color compared with the old one.
	 */
	private int alphaNewColor = 255;
	
	/**
	 * Composite where the new and, eventually, the old color are painted
	 */
	private Composite colorComposite;
	
	/**
	 * composite where additional components can be placed right to the color preview
	 */
	private Composite additionalComponents;
	
	/**
	 * Label to identify the old color, a reference is keep to hide it 
	 * easily when there is not an old color
	 */
	private Label oldColorLabel;
	
	/**
	 * Create the preview composite
	 * 
	 * @param parent
	 * @param style
	 */
	public ColorPreviewWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2,false));
		
		Label newColorLabel = new Label(this, SWT.NONE);
		newColorLabel.setText(Messages.ColorDialog_newColorLabel);
		newColorLabel.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false));
		new Label(this, SWT.NONE);
		
		colorComposite = new Composite(this, SWT.BORDER);
		colorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	  GridData previewData = new GridData();
	  previewData.widthHint = 120;
	  previewData.heightHint = 50;
	  previewData.verticalAlignment = SWT.TOP;
	  colorComposite.setLayoutData(previewData);
		
		additionalComponents = new Composite(this, SWT.NONE);
		GridLayout additionalComponentLayout = new GridLayout(1,false);
		additionalComponentLayout.horizontalSpacing = 0;
		additionalComponentLayout.verticalSpacing = 0;
		additionalComponentLayout.marginHeight = 0;
		additionalComponentLayout.marginWidth = 0;
		additionalComponents.setLayout(additionalComponentLayout);
		additionalComponents.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		oldColorLabel = new Label(this, SWT.NONE);
		oldColorLabel.setText(Messages.ColorDialog_oldColorLabel);
		GridData oldColorLabelData = new GridData(SWT.CENTER, SWT.TOP, false, false);
		oldColorLabelData.exclude = true;
		oldColorLabel.setLayoutData(oldColorLabelData);
		oldColorLabel.setVisible(false);
		
		addControlListener(new ControlAdapter(){
			 @Override
			public void controlResized(ControlEvent e) {
				updatePreview();
			}
		 });
	}

	/**
	 * Return the composite where additional components can be placed
	 * 
	 * @return a composite right to the color preview with a grid layout 
	 * with only a column and without margins. if fill the container in both
	 * directions
	 */
	public Composite getAdditionalComponentarea(){
		return additionalComponents;
	}
	
	/**
	 * Set the new color
	 * 
	 * @param color rgb of the new color
	 * @param alphaNewColor alpha of the new color
	 */
	public void setNewColor(RGB color, int alphaNewColor){
		newColor = color;
		this.alphaNewColor = alphaNewColor;
		updatePreview();
	}
	
	/**
	 * Set the old color
	 * 
	 * @param color rgb of the old color
	 * @param alphaNewColor alpha of the old color
	 */
	public void setOldColor(RGB color, int alphaOldColor){
		oldColor = color;
		this.alphaOldColor = alphaOldColor;
		boolean labelVisible = oldColor != null;
		((GridData)oldColorLabel.getLayoutData()).exclude = !labelVisible;
		oldColorLabel.setVisible(labelVisible);
		updatePreview();
	}
	
	/**
	 * update the preview area with the set old and new color
	 */
	public void updatePreview(){
		if (newColor == null) return;
		Rectangle rect = colorComposite.getClientArea();
		ImageData imageData=new ImageData(Math.max(1, rect.width),Math.max(1, rect.height),32,new PaletteData(0xFF0000,0xFF00,0xFF));
		Image newImage = new Image(getDisplay(), imageData);
    GC gc = new GC(newImage);
    gc.setAntialias(SWT.ON);
    //If there is an old color set paint also it into the preview area to compare it with the new one,
    //otherwise print only the new one
    if (oldColor != null){
    	gc.setAlpha(255);
    	gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
      gc.fillRectangle(0, 0, imageData.width, imageData.height/2);
      gc.setAlpha(alphaNewColor);
      gc.setBackground(ResourceManager.getColor(newColor));
      gc.fillRectangle(0, 0, imageData.width, imageData.height/2);
    	gc.setAlpha(255);
    	gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
      gc.fillRectangle(0, imageData.height/2, imageData.width, imageData.height);
      gc.setAlpha(alphaOldColor);
      gc.setBackground(ResourceManager.getColor(oldColor));
      gc.fillRectangle(0, imageData.height/2, imageData.width, imageData.height);
    } else {
    	gc.setAlpha(255);
    	gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
      gc.fillRectangle(0, 0, imageData.width, imageData.height);
      gc.setAlpha(alphaNewColor);
      gc.setBackground(ResourceManager.getColor(newColor));
      gc.fillRectangle(0, 0, imageData.width, imageData.height);
    }
    if (actualPreviewImage != null){
    	actualPreviewImage.dispose();
    }
    gc.dispose();
    actualPreviewImage = newImage;
    colorComposite.setBackgroundImage(newImage);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		actualPreviewImage.dispose();
	}
}
