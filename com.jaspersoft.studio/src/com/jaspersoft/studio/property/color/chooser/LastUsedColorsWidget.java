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
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceCache;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * A composite that offers the controls to  choose a color from a grid
 * of samples. The colors in the last colors used in the current session. The samples have
 * a dynamic size that is adapted to the available area
 * 
 * @author Orlandin Marco
 *
 */
public class LastUsedColorsWidget extends Composite implements IColorProvider{
	
	/**
	 * List of the last used colors color RGB.
	 */
	private static List<AlfaRGB> usedColors = new ArrayList<AlfaRGB>();
	
	/**
	 * Cache of the color samples images, that will be disposed when all
	 * the other components are disposed
	 */
	private ResourceCache imagesCache = new ResourceCache();

	/**
	 * Composite where the samples are placed
	 */
	private Composite colorComposite;
	
	/**
	 * The actual selected color
	 */
	private AlfaRGB selectedColor = null;
	
	/**
	 * Preview area where is shown the selected color, if provided it is also compared with the and old color
	 * we are replacing
	 */
	private ColorPreviewWidget previewArea;
	
	/**
	 * Label where the red component of the actually selected color is placed
	 */
	private Label red;
	
	/**
	 * Label where the green component of the actually selected color is placed
	 */
	private Label green;
	
	/**
	 * Label where the blue component of the actually selected color is placed
	 */
	private Label blue;
	
	/**
	 * Label where the hue of the actually selected color is placed
	 */
	private Label hue;
	
	/**
	 * Label where the brightness of the actually selected color is placed
	 */
	private Label brightness;
	
	/**
	 * Label where the saturation of the actually selected color is placed
	 */
	private Label saturation;
	
	/**
	 * Label where the transparency of the actually selected color is placed
	 */
	private Label alpha;
	
	
	/**
	 * Listner called when a color sample is selected
	 */
	private MouseAdapter colorSelectedAdapter = new MouseAdapter() {
		
		public void mouseDown(MouseEvent e) {
			selectedColor = (AlfaRGB)e.widget.getData();
			previewArea.setNewColor(selectedColor.getRgb(), selectedColor.getAlfa());
			updateLabels();
		};
		
	};

	/**
	 * Create the controls 
	 * 
	 * @param parent parent of the controls
	 * @param style style of the main composite
	 * @param oldColor the old color, used to compare it into a preview area with the selected color
	 */
	public LastUsedColorsWidget(Composite parent, int style, AlfaRGB oldColor) {
		super(parent, style);
		this.setLayout(new GridLayout(2,false));
		selectedColor = AlfaRGB.getFullyOpaque(new RGB(0,0,0));
		colorComposite = new Composite(this, SWT.NONE);
		colorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		colorComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				//refreshColors();
				paintPad();
			}
		});
		
		Composite rightPanel = new Composite(this, SWT.NONE);
		rightPanel.setLayout(new GridLayout(1,false));
		previewArea = new ColorPreviewWidget(rightPanel, SWT.NONE);
		if (oldColor != null){
			previewArea.setOldColor(oldColor.getRgb(), oldColor.getAlfa());
		}
		previewArea.setNewColor(selectedColor.getRgb(), selectedColor.getAlfa());
		
		Composite textualInformation = new Composite(rightPanel, SWT.NONE);
		textualInformation.setLayout(new GridLayout(4,false));
		textualInformation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		red = createLabel("R:", textualInformation);
		hue = createLabel("H:", textualInformation);
		green = createLabel("G:", textualInformation);
		saturation = createLabel("S:", textualInformation);
		blue = createLabel("B:", textualInformation);
		brightness = createLabel("B:", textualInformation);
		alpha = createLabel(Messages.ColorDialog_transparencyLabel+":", textualInformation);
		updateLabels();
	}
	
	/**
	 * Return the actually selected color
	 * 
	 * @return an alfaRGB of the actually selected color. Note using this components
	 * the alpha of the color will be always fully opaque
	 */
	public AlfaRGB getSelectedColor(){
		return selectedColor;
	}
	
	/**
	 * Return true if the list of the last used colors has at least an element, otherwise false
	 * 
	 */
	public static boolean hasColors(){
		//refreshColors();
		return usedColors.size()>0;
	}
	
	/*
	
	private static int lastColorCacheSize = -1;
	
	private static void refreshColors(){
		Set<RGB> colors = ResourceManager.getStoredColors();
		if (lastColorCacheSize == -1 || lastColorCacheSize < colors.size()){
			for(RGB color : colors){
				AlfaRGB aColor = AlfaRGB.getFullyOpaque(color);
				addColor(aColor);
			}
			lastColorCacheSize =colors.size();
		}
	}
	
	*/
	
	/**
	 * Add an element to the list of the last used color
	 * 
	 * @param color
	 * @return
	 */
	public static boolean addColor(AlfaRGB color){
		if (color!= null && !usedColors.contains(color)){
			usedColors.add(0, color);
			if (usedColors.size()>256){
				usedColors.remove(usedColors.size()-1);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Update the label values with the actual selected color
	 */
	private void updateLabels(){
		RGB selectedRGB = selectedColor.getRgb();
		red.setText(String.valueOf(selectedRGB.red));
		green.setText(String.valueOf(selectedRGB.green));
		blue.setText(String.valueOf(selectedRGB.blue));
		float[] hsb = selectedRGB.getHSB();
		hue.setText(String.valueOf(Math.round(hsb[0])));
		saturation.setText(String.valueOf(Math.round(hsb[1]*100))+"%");
		brightness.setText(String.valueOf(Math.round(hsb[1]*100))+"%");
		alpha.setText(String.valueOf(selectedColor.getAlfa()));
	}

	/**
	 * Create two label, a description one and a value one on the right
	 * @param preText text for the description
	 * @param parent parent where the two label will be placed
	 * @return value label 
	 */
	private Label createLabel(String preText, Composite parent){
		new Label(parent, SWT.NONE).setText(preText);
		Label result = new Label(parent, SWT.NONE);
		result.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return result;
	}
	
	/**
	 * create the samples adapting their size from a maximum of 22x22 to a minimum of 1x1 pixels.
	 */
	private void paintPad(){
		//Clear old elements
		for(Control child : colorComposite.getChildren()){
			child.dispose();
		}
		Rectangle rect = colorComposite.getClientArea();
		int sampleSize = 22;
		int spaceBetween = 1;
		int samplesRow = 18;
		int samplesColumn = 216/samplesRow;
		boolean enoughSize = false;
		while(!enoughSize && sampleSize != 0){
			boolean enoughWidth = ((sampleSize*samplesRow) + (spaceBetween*(samplesRow-1)))<rect.width;
			boolean enoughHeight = ((sampleSize*samplesColumn) + (spaceBetween*(samplesColumn-1)))<rect.height;
			enoughSize = enoughHeight & enoughWidth;
			if (!enoughSize) sampleSize--;
		}
		GridLayout colorLayout = new GridLayout(samplesRow, true);
		colorLayout.horizontalSpacing = spaceBetween;
		colorLayout.verticalSpacing = spaceBetween;
		colorComposite.setLayout(colorLayout);
		Iterator<AlfaRGB> colorIt = usedColors.iterator();
		for(int i=0; i<samplesColumn && colorIt.hasNext(); i++){
			for(int j=0; j<samplesRow && colorIt.hasNext(); j++){
				Canvas canv = new Canvas(colorComposite, SWT.NONE);
				GridData canvData = new GridData();
				canvData.widthHint = sampleSize;
				canvData.heightHint = sampleSize;
				canv.setLayoutData(canvData);
				AlfaRGB actualColor = colorIt.next();
				canv.setBackground(imagesCache.getColor(actualColor.getRgb()));
				canv.setData(actualColor);
				canv.addMouseListener(colorSelectedAdapter);
			}
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		imagesCache.dispose();
	}
}
