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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * This widget show a square area with  aside a rectangle area. The user 
 * can click in the square area to select a  color and in the rectangle area
 * to change the colors patter inside the squared area. The colors inside the 
 * square area and rectangle area are defined from external governor
 * 
 * @author OrlandinMarco
 *
 */
public class ColorsSelectorWidget extends Composite {

	/**
	 * Image inside the square area, disposed at the end or when it is replaced
	 */
	private Image oldImage = null;
	
	/**
	 * Image inside the rectangle area, disposed at the end or when it is replaced
	 */
	private Image oldSliderImage = null;
	
	/**
	 * Last point selected by the user in the squared area
	 */
	private Point circlePosition = new Point(0, 0);
	
	/**
	 * Last point selected by the user in the rectangle area
	 */
	private int sliderPosition = 0;
	
	/**
	 * Color actually selected in the square area
	 */
	private RGB selectedColorRGB = null;
	
	/**
	 * Color actually selected in the square area as HSB (to avoid precision error both rgb and hsb are keep)
	 */
	private float[] selectedColorHSB = new float[3];
	
	/**
	 * COmposite where the square area is placed
	 */
	private Composite colorComposite;
	
	/**
	 * Composite where the rectangle area is placed
	 */
	private Composite slider;
	
	/**
	 * Governor that define the color content of both the square and rectangle areas
	 */
	private IWidgetGovernor governor;
	
	/**
	 * Width of the arrows painted in the rectangle area to show the selected point
	 */
	private int arrowWidth = 5;
	
	/**
	 * Border color of the rectangle area
	 */
	private Color borderColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER);
	
	/**
	 * Cache of the content of the rectangle area
	 */
	private ImageData cachedSlider = null;
	
	/**
	 * Cache of the content of the square area
	 */
	private ImageData cachedPad = null;
	
	/**
	 * Listeners called when the user select a point in the square area
	 */
	private List<SelectionListener> selListeners = new ArrayList<SelectionListener>();
	
	/**
	 * Create a composite with inside all the widgets
	 * 
	 * @param parent parent of the composite
	 * @param style style of the composite
	 * @param governor governor to define the content inside the square and rectangle areas
	 */
	public ColorsSelectorWidget(Composite parent, int style, IWidgetGovernor governor) {
		super(parent, style);
		this.governor = governor;
		setLayout(new GridLayout(2,false));
		
		colorComposite = new Composite(this, SWT.BORDER);
		colorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		slider = new Composite(this, SWT.NONE);
		//When the rectangle area is resized its content, and also the rectangle one are refreshed
		slider.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
	       cachedSlider = null;
	       paintSlider();
	      	//Need to set the selection mark into the right position
				 setSelectedColor(getSelectedColorRGB(),false);
			}
		});
		//When the square area is resized its content is refreshed
		colorComposite.addControlListener(new ControlAdapter() {
			@Override
      public void controlResized(ControlEvent event) {
      	cachedPad = null;
      	paintPad();
      	//Need to set the selection mark into the right position
      	setSelectedColor(getSelectedColorRGB(),false);
      }
    });
		
		GridData rangeData = new GridData(SWT.LEFT, SWT.FILL, false, true);
		rangeData.widthHint = 30;
		slider.setLayoutData(rangeData);
		//set on the square area the mouse click listener to select a color
		colorComposite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				padClicked(e);
			}

		});
		//set on the square area the mouse listener to select a color when the mouse is
		//moved with the left key pressed
		colorComposite.addMouseMoveListener(new MouseMoveListener() {	
			@Override
			public void mouseMove(MouseEvent e) {
				if ((e.stateMask & SWT.BUTTON1) != 0){
					padClicked(e);
				}
			}
		});
		//set on the rectangle area the mouse click listener to update the square area
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				sliderClicked(e);
			}
		});
		//set on the rectangle area the mouse listener to update the square area
		//when the mouse is moved with the left key pressed
		slider.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				if ((e.stateMask & SWT.BUTTON1) != 0){
					sliderClicked(e);
				}
			}
		});
	}
	
	/**
	 * Set the actually selected color using the hsb format. the selection
	 * is reflected also into the square and rectangle area, showing the 
	 * correct selected point
	 * 
	 * @param h hue
	 * @param s saturation
	 * @param b brightness
	 * @param callListener true if the selection listeners registered should be
	 * called, false otherwise
	 */
	public void setSelectedColor(float h, float s, float b, boolean callListener){
		if (h < 0 || h > 360 || s < 0 || s > 1 || b < 0 || b > 1) return;
		RGB color = new RGB(h,s,b);
		int[] relativePositions = governor.getXYSlider(new float[]{h,s,b});
		int relativeSlide = relativePositions[2];
		int newSliderPosition = getAbsoluteSliderFromRelative(relativeSlide);
		if (sliderPosition != newSliderPosition){
			sliderPosition = newSliderPosition;
			cachedPad = null;
		}
		int relativeX = relativePositions[0];
		int relativeY = relativePositions[1];
		Point newCirclePosition = new Point(getAbsoluteXFromRelative(relativeX), getAbsoluteYFromRelative(relativeY));
		if (circlePosition != newCirclePosition){
			circlePosition = new Point(getAbsoluteXFromRelative(relativeX), getAbsoluteYFromRelative(relativeY));
			cachedSlider = null;
		}
		paintSlider();
		paintPad();
		selectedColorRGB = color;
		selectedColorHSB = new float[]{h,s,b};
  	if (callListener) callSelectionListeners();
	}
	
	/**
	 * Set the actually selected color using the RGB format. the selection
	 * is reflected also into the square and rectangle area, showing the 
	 * correct selected point
	 * 
	 * @param color the color as RGB
	 * @param callListener true if the selection listeners registered should be
	 * called, false otherwise
	 */
	public void setSelectedColor(RGB color, boolean callListener){
		if (color == null) return;
		int[] relativePositions = governor.getXYSlider(color);
		int relativeSlide = relativePositions[2];
		int newSliderPosition = getAbsoluteSliderFromRelative(relativeSlide);
		if (sliderPosition != newSliderPosition){
			sliderPosition = newSliderPosition;
			cachedPad = null;
		}
		int relativeX = relativePositions[0];
		int relativeY = relativePositions[1];
		Point newCirclePosition = new Point(getAbsoluteXFromRelative(relativeX), getAbsoluteYFromRelative(relativeY));
		if (circlePosition != newCirclePosition){
			circlePosition = new Point(getAbsoluteXFromRelative(relativeX), getAbsoluteYFromRelative(relativeY));
			cachedSlider = null;
		}
		paintSlider();
		paintPad();
		selectedColorRGB = color;
		selectedColorHSB = color.getHSB();
  	if (callListener) callSelectionListeners();
	}
	
	/**
	 * Return the actual selected color as RGB
	 * 
	 * @return an RGB color
	 */
	public RGB getSelectedColorRGB(){
		return selectedColorRGB;
	}
	
	/**
	 * Return the actual selected color as HSB
	 * 
	 * @return an array of 3 float representing the hsb values of the color
	 */
	public float[] getSelectedColorHSB(){
		return selectedColorHSB;
	}
	
	/**
	 * Set a new governor and refresh the content of the square and rectangle
	 * area. Since the color under the selection change all the selection listener
	 * are called
	 * 
	 * @param newGovernor the new governor
	 */
	public void setGovernor(IWidgetGovernor newGovernor){
		this.governor = newGovernor;
  	cachedSlider = null;
  	cachedPad = null;
		paintPad();
  	paintSlider();
  	callSelectionListeners();
	}
	
	/**
	 * Set the selection point in the rectangle area with a relative value. the value
	 * must be between the range of max and min of the actual governor
	 * 
	 * @param slider
	 */
	public void setSlider(int slider){
		this.sliderPosition = slider;
		paintPad();
  	paintSlider();
	}
	
	/**
	 * Add a selection listener that will be called when the user click the square area, passing in the 
	 * event this widget and the new color selected by the user as RGB (inside the data field of the
	 * event)
	 */
	public void addSelectionListener(SelectionListener listener){
		selListeners.add(listener);
	}
	
	/**
	 * dispose the control and the last image used into the square 
	 * and rectangle area
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (oldImage != null && !oldImage.isDisposed()){
			oldImage.dispose();
		}
		if (oldSliderImage != null && !oldSliderImage.isDisposed()){
			oldSliderImage.dispose();
		}
	}
	
	/**
	 * Handler called when the square area is clicked or the mouse is moved with the 
	 * left button clicked
	 * 
	 * @param e mouse move event
	 */
	private void padClicked(MouseEvent e){
		cachedSlider = null;
		updatePosition(e);
		paintPad();
		paintSlider();
		setSelectedColorFromImage();
  	callSelectionListeners();
	}
	
	/**
	 * Handler called when the rectangle area is clicked or the mouse is moved with the 
	 * left button clicked
	 * 
	 * @param e mouse move event
	 */
	private void sliderClicked(MouseEvent e){
		cachedPad = null;
		updateSlider(e);
  	paintSlider();
		paintPad();
		setSelectedColorFromImage();
  	callSelectionListeners();
	}
	
	/**
	 * Set the actual selected color taking it from the last point clicked by
	 * the user in the square area
	 */
	private void setSelectedColorFromImage(){
		if (cachedPad != null){
			ImageData data = cachedPad;
			int pixelValue = data.getPixel(circlePosition.x, circlePosition.y);
			selectedColorRGB = data.palette.getRGB(pixelValue);
			selectedColorHSB = selectedColorRGB.getHSB();
		}
	}
	
	/**
	 * When the user click the square are this call all the register listeners, passing in the 
	 * event this widget and the new color selected by the user as RGB (inside the data field of the
	 * event)
	 */
	private void callSelectionListeners(){
		Event e = new Event();
		e.widget = this;
		SelectionEvent event = new SelectionEvent(e);
		event.data = selectedColorRGB;
		for(SelectionListener listener : selListeners){
			listener.widgetSelected(event);
		}
	}
	
	/**
	 * Called when the square area is clicked, register
	 * the click position
	 * 
	 * @param e MouseEvent
	 */
	private void updatePosition(MouseEvent e){
		Rectangle rect = colorComposite.getClientArea();
		int x = Math.max(0, e.x);
		x = Math.min(x, rect.width-1);
		int y = Math.max(0, e.y);
		y = Math.min(y, rect.height-1);
		circlePosition = new Point(x, y);
	}
	
	/**
	 * Called when the rectangle area is clicked, register
	 * the click position
	 * 
	 * @param e MouseEvent
	 */
	private void updateSlider(MouseEvent e){
		Rectangle rect = slider.getClientArea();
		int y = Math.max(0, e.y);
		y = Math.min(y, rect.height);
		sliderPosition = y;
	}
	
	/**
	 * Translate a relative x position from the governor to an x coordinate
	 * inside the square component
	 * 
	 * @param relativeX x provided from the governor
	 * @return x coordinate inside the square area
	 */
	private int getAbsoluteXFromRelative(int relativeX){
		Rectangle rect = colorComposite.getClientArea();
		if (rect.width == 0) return 0;
		int padWidth = governor.getPadMaxX() - governor.getPadMinX();
		return (relativeX * (rect.width-1))/padWidth;
	}
	
	/**
	 * Translate a relative y position from the governor to an y coordinate
	 * inside the square component
	 * 
	 * @param relativeY y provided from the governor
	 * @return y coordinate inside the square area
	 */
	private int getAbsoluteYFromRelative(int relativeY){
		Rectangle rect = colorComposite.getClientArea();
		if (rect.height == 0) return 0;
		int padHeight = governor.getPadMaxY() - governor.getPadMinY();
		return (relativeY * (rect.height-1))/padHeight;
	}
	
	/**
	 * Translate a relative y position from the governor to an y coordinate
	 * inside the rectangle component. There is no need for x since it is constant
	 * inside this area
	 * 
	 * @param relativeSlider y provided from the governor
	 * @return y coordinate inside the rectangle area
	 */
	private int getAbsoluteSliderFromRelative(int relativeSlider){
		Rectangle rect = slider.getClientArea();
		if (rect.height == 0) return 0;
		int sliderHeight = governor.getSliderMax() - governor.getSliderMin();
		return (relativeSlider * (rect.height-1))/sliderHeight;
	}

	
	/**
	 * Create the image data for the rectangle data without arrow, so it can be cached
	 * 
	 * @param rect size of the rectangle area
	 * @return ImageData that will be placed inside the rectangle area
	 */
	private ImageData createSliderImage(Rectangle rect){
	  int padWidth = governor.getPadMaxX() - governor.getPadMinX();
    int padHeight = governor.getPadMaxY() - governor.getPadMinY();
  	float padX = ((float)(circlePosition.x*padWidth))/(getPadWidth()-1);
  	float padY = ((float)(circlePosition.y*padHeight))/(getPadHeight()-1);
		ImageData image = new ImageData(Math.max(1, rect.width),Math.max(1, rect.height),32,new PaletteData(0xFF0000,0xFF00,0xFF));
		int sliderHeight = governor.getSliderMax() - governor.getSliderMin();
		
		int backGroundPixel = image.palette.getPixel(slider.getParent().getBackground().getRGB());
		for(int y = 0; y<image.height; y++){
			float actaulSlider = ((float)sliderHeight / getSliderHeight()) * y;
			RGB actualHueColor = governor.getSliderColor(Math.round(padX), Math.round(padY), Math.round(actaulSlider));
			for(int x = 0; x<image.width; x++){
				if (x > arrowWidth && x<image.width-arrowWidth)
					image.setPixel(x,y,image.palette.getPixel(actualHueColor));
				else 
					image.setPixel(x,y, backGroundPixel);
			}
		}
		return image;
	}
	
	/**
	 * Create the image data for the square data without the selection mark, so it can be cached
	 * 
	 * @param rect size of the square area
	 * @return ImageData that will be placed inside the square area
	 */
	private ImageData createPadImage(Rectangle rect){
    ImageData imageData=new ImageData(Math.max(1, rect.width),Math.max(1, rect.height),32,new PaletteData(0xFF0000,0xFF00,0xFF));
    int padWidth = governor.getPadMaxX() - governor.getPadMinX();
    int padHeight = governor.getPadMaxY() - governor.getPadMinY();
    //Calculate the actual slider
    int slider = Math.round(((float)governor.getSliderMax() / getSliderHeight())*sliderPosition);
    for(int i=0; i<imageData.width; i++){
    	for (int j=0; j<imageData.height; j++){
      	float padX = ((float)(i*padWidth))/(imageData.width-1);
      	float padY = ((float)(j*padHeight))/(imageData.height-1);
    		imageData.setPixel(i,j,imageData.palette.getPixel(governor.getPadColor(Math.round(padX), Math.round(padY), slider)));
    	}
    }
    
    return imageData;
	}
	
	
	/**
	 * Return the height of the rectangle area
	 * 
	 * @return int representing the actual height of the rectangle area
	 */
	private int getSliderHeight(){
		if (cachedSlider != null) return cachedSlider.height;
	  	Rectangle sliderRect = slider.getClientArea();
	  	return sliderRect.height;
	}
	
	/**
	 * Return the width of the square area
	 * 
	 * @return int representing the actual width of the square area
	 */
	private int getPadWidth(){
		if (cachedPad != null) return cachedPad.width;
		Rectangle padRect = colorComposite.getClientArea();
		return padRect.width;
	}
	
	/**
	 * Return the height of the square area
	 * 
	 * @return int representing the actual height of the square area
	 */
	private int getPadHeight(){
		if (cachedPad != null) return cachedPad.height;
		Rectangle padRect = colorComposite.getClientArea();
		return padRect.height;
	}
	
	/**
	 * Put the appropriate image inside the rectangle area. If the image data of 
	 * the image it is cached it will be used, otherwise a new image data is calculated
	 * and then cached. On the image is also painted the actual selected point
	 */
	private void paintSlider()
	{
		Rectangle rect = slider.getClientArea();
		if (rect.height == 0) return;
		if (cachedSlider == null) cachedSlider = createSliderImage(rect);
		
		ImageData imageData = (ImageData)cachedSlider.clone();
		Image newImage = new Image(getDisplay(), imageData);
    	GC gc = new GC(newImage);
	    gc.setAntialias(SWT.ON);
	    gc.setForeground(borderColor);
	    gc.drawRectangle(arrowWidth, 0, imageData.width-arrowWidth*2, imageData.height-1);
	    
	    gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
	    gc.fillPolygon(new int[]{0, sliderPosition-3, arrowWidth,sliderPosition,0,sliderPosition+3});
	    gc.fillPolygon(new int[]{imageData.width, sliderPosition-3, imageData.width-arrowWidth,sliderPosition,imageData.width,sliderPosition+3});
	    if (oldSliderImage != null){
	    	oldSliderImage.dispose();
	    }
	    oldSliderImage = newImage;
	    gc.dispose();
	    slider.setBackgroundImage(newImage);
	}
	
	/**
	 * Put the appropriate image inside the square area. If the image data of 
	 * the image it is cached it will be used, otherwise a new image data is calculated
	 * and then cached. On the image is also painted the actual selected point
	 */
	private void paintPad(){
		Rectangle rect = colorComposite.getClientArea();
		
		if (rect.height == 0 || rect.width == 0) return;
	  
		if (cachedPad == null) cachedPad = createPadImage(rect);
    Image newImage =  new Image(getDisplay(), (ImageData)cachedPad.clone());
    GC gc = new GC(newImage);
    if (oldImage != null){
      oldImage.dispose();
    }
    oldImage = newImage;
    if (circlePosition != null){
    	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
    	gc.setAntialias(SWT.ON);
    	gc.drawOval(circlePosition.x-2, circlePosition.y-2, 8, 8);
    	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
    	gc.drawOval(circlePosition.x-1, circlePosition.y-1, 6, 6);
    }
    gc.dispose();
    colorComposite.setBackgroundImage(newImage);
	}
}
