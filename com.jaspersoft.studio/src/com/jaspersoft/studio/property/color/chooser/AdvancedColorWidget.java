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

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.ImageUtils;

/**
 * A composite that offer advanced controls to choose a color like the selection of 
 * a transparency, the selection of the color between many visualization...
 * 
 * @author Orlandin Marco
 *
 */
public class AdvancedColorWidget extends Composite implements IColorProvider{

	/**
	 * Widget where is shown the actual color space and the user can choose a color from that with a mouse click
	 */
	private ColorsSelectorWidget colorsSelector;
	
	/**
	 * Preview area where is shown the selected color, if provided it is also compared with the and old color
	 * we are replacing
	 */
	private ColorPreviewWidget previewComposite = null;
	
	/**
	 * Control to define the alpha value by sliding a cursor
	 */
	private Scale alphaSlider = null;
	
	/**
	 * Control to define the alpha value by a numeric spinner
	 */
	private Spinner alphaText = null;
	
	/**
	 * Numeric value that represent the alpha transparency of the new color
	 */
	private int alpha = 255;
	
	/**
	 * List of radio buttons used to have only one button selected at time
	 */
	private List<Button> radioList = new ArrayList<Button>();
	
	/**
	 * Numeric spinner to define the hue of a color
	 */
	private Spinner hue = null;
	
	/**
	 * Numeric spinner to define the saturation of a color
	 */
	private Spinner saturation = null;
	
	/**
	 * Numeric spinner to define the brightness of a color
	 */
	private Spinner brightness = null;
	
	/**
	 * Numeric spinner to define the red component of a color
	 */
	private Spinner red = null;
	
	/**
	 * Numeric spinner to define the green component of a color
	 */
	private Spinner green = null;
	
	/**
	 * Numeric spinner to define the blue component of a color
	 */
	private Spinner blue = null;
	
	/**
	 * Textual field where the user can define a color like a hex number
	 */
	private Text hex = null;
	
	/**
	 * Button to press to acquire a color from the screen
	 */
	private Button pickColorButton;
	
	/**
	 * Boolean guard to avoid the start of the modify listener when 
	 * the textual\spinner fields are updated
	 */
	private Boolean modfiedGuard = true;

	/**
	 * Font used inside the pick color button when it is pressed
	 */
	private Font buttonFont = ResourceManager.getFont("Arial",9,SWT.BOLD); //$NON-NLS-1$
	
  /** 
   * timer interval for checking color under the mouse cursor
   * 
   */
  private static final int TIMER_INTERVAL = 50;
  
  /**
   * Flag used to hide the alpha slider controls
   */
  private boolean hideSliderBar = false;
  
  /**
   * Boolean flag to know if there is an acquiring action by the color picker
   */
	private boolean isAcquiring = false;
  
	/**
	 * Color that we are changing from the color dialog, used to compare the new color 
	 * to the old one. If it is null it is not shown
	 */
	private RGB oldColor = null;
	
	/**
	 * The actually selected color
	 */
	private AlfaRGB newColor = null;
	
  /**
   * Thread that at every time interval check the color under the mouse 
   * position on the screen and use it for the color selected by the user
   * 
   * @author Orlandin Marco
   *
   */
	private class ColorPickerThreadClass implements Runnable {
		
		/**
		 * Boolean flag that is used to terminate the thread
		 */
		private Boolean stopThread = false;
		
		/**
		 * Check if the thread should stop
		 * 
		 * @return true if the thread should terminate false if he must
		 * continue its cycle of color picking
		 */
		public boolean getStop(){
			synchronized (this) {
				return stopThread;
			}
		}
		
		/**
		 * Set the termination status of the thread
		 * 
		 * @param value true if the thread should terminate false if he must
		 * continue its cycle of color picking
		 */
		private void setStop(boolean value){
			synchronized (this) {
				stopThread = value;
			}
		}
		
		/**
		 * If the stop guard is false the thread read the color under the mouse position,
		 * update the actual color and will re-execute himself after a time interval, otherwise
		 * its run will be terminated
		 */
	  @Override
	  public void run() {
	  	if (!getStop()) {
	  		checkColorPicker();
	  		Display.getCurrent().timerExec(TIMER_INTERVAL, this);
	  	}
	  }
	};
	
	 /**
   * Thread used to read the color under the mouse cursor
   */
	private ColorPickerThreadClass colorPickerThread = new ColorPickerThreadClass();
	

	/**
	 * Modify listener used when a textual control or a numeric one is modified by the user
	 */
	private ModifyListener valueModifedListener = new ModifyListener(){
		
		@Override
		public void modifyText(ModifyEvent e) {
			synchronized (modfiedGuard) {
				if (modfiedGuard){
					//since one modification throw a chain of modification on the other control we need to consider only the first one
					modfiedGuard = false;
					hue.getParent().getParent().setRedraw(false);
					if (e.widget == hue || e.widget == saturation || e.widget == brightness){
						float h = (float)hue.getSelection();
						float s = (float)saturation.getSelection()/100;
						float b = (float)brightness.getSelection()/100;
						colorsSelector.setSelectedColor(h, s, b, false);
						updateText(h,s,b, e.widget);
					} else if (e.widget == red || e.widget == green || e.widget == blue){
						RGB rgbColor = new RGB(red.getSelection(),green.getSelection(),blue.getSelection());
						colorsSelector.setSelectedColor(rgbColor, false);
						updateText(rgbColor, rgbColor.getHSB(), e.widget);
					} else if (e.widget == hex){
						AlfaRGB rgbColor = hexParser(hex.getText());
						if (rgbColor != null){
							colorsSelector.setSelectedColor(rgbColor.getRgb(), false);
							//The alpha widget is optional so we need to check if it is present
							if (alphaText != null) alphaText.setSelection(rgbColor.getAlfa());
							updateText(rgbColor.getRgb(), rgbColor.getRgb().getHSB(), e.widget);
						}
					}
					hue.getParent().getParent().setRedraw(true);
					modfiedGuard = true;
				}
			}
		}
		
	};
	
	/**
	 * Listener used to stop the color picking thread when the space key is pressed
	 */
	private Listener spaceKeyListener = new Listener() {
		
		public void handleEvent(Event e) {	
			if (e.keyCode == UIUtils.SWT_SPACE){
				if (!isAcquiring){
					isAcquiring = true;
					colorPickerThread.setStop(false);
					Display.getCurrent().timerExec(TIMER_INTERVAL, colorPickerThread);
	        pickColorButton.setImage(null);
	        pickColorButton.setText(Messages.ColorDialog_stopPickingActionText);
	        pickColorButton.setEnabled(false);
	        pickColorButton.setLayoutData(new GridData(GridData.FILL_BOTH));
	        pickColorButton.getParent().layout(true, true);
				} else {
					stopPickerThread();
					isAcquiring = false;
				}
			} 
		}
	};
	
	/**
	 * Create the controls
	 * 
	 * @param parent parent of the controls
	 * @param style style of the main composite
	 * @param oldColor old color, used to compare it into a preview area with the selected color
	 * @param showAlpha true if the controls to set the alpha value of the new color should be displayed, false
	 * otherwise
	 */
	public AdvancedColorWidget(Composite parent, int style, AlfaRGB oldColor, boolean showAlpha) {
		 super(parent, style);
		 
		 if (oldColor != null){
			 this.oldColor = oldColor.getRgb();
			 this.alpha = oldColor.getAlfa();
		 }
		 hideSliderBar = !showAlpha;
		 
		 setLayout(new GridLayout(2,false));
		 colorsSelector = new ColorsSelectorWidget(this, SWT.NONE, new HueBasedSelector());
		 GridData rectangleData = new GridData(GridData.FILL_BOTH);
		 rectangleData.widthHint = 250;
		 rectangleData.heightHint = 150;
		 colorsSelector.setLayoutData(rectangleData);
		 if (this.oldColor != null) colorsSelector.setSelectedColor(this.oldColor,false);
		 else colorsSelector.setSelectedColor(new RGB(255,255,255), false);
		 
		 colorsSelector.addSelectionListener(new SelectionAdapter() {
			 	@Override
				public void widgetSelected(SelectionEvent e) {
			 		updateText();
				}
		 });
		 
		 Composite righSide = new Composite(this, SWT.NONE);
		 righSide.setLayout(new GridLayout(1,false));
		 righSide.setLayoutData(new GridData(GridData.FILL_BOTH));
		 
		 previewComposite = new ColorPreviewWidget(righSide, SWT.NONE);
		 if (oldColor != null) {
				previewComposite.setOldColor(this.oldColor, alpha);
		 }
		 previewComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		 //Create the color picker button
		 createColorPicker(previewComposite.getAdditionalComponentarea());
		 
		 
		 //Create the alpha slider and textual controls
		 if (!hideSliderBar) createSlider(righSide);
		 //Create the textual\numeric controls
		 createTextArea(righSide);
		 updateText();
		 
		 PlatformUI.getWorkbench().getDisplay().addFilter(org.eclipse.swt.SWT.KeyDown, spaceKeyListener);
	}
	
	
	/**
	 * Get the alpha rgb of the actually selected color
	 * 
	 * @return the actually selected color
	 */
	public AlfaRGB getSelectedColor(){
		return newColor;
	}
	
	/**
	 * Convert the text into a alfa RGB color, but only if there are exactly seven chars, a # symbol followed by three pair of hex values
	 * or if it a sequence of 3 integers separated by comma (read as rgb) or if it is a sequence of 4 integers separated by comma (read as rgba)
	 * 
	 * @param text a text representing a color as HexDecimal value, rgb value or alfa rgb value
	 * @return an alfa RGB color. If the color is provided as hex or as rgb the alpha is 255
	 */
	private AlfaRGB hexParser(String text){
		AlfaRGB newColor = null;
		try {
			if (text.startsWith("#") && text.length() == 7) { //$NON-NLS-1$
				newColor = AlfaRGB.getFullyOpaque(new RGB(Integer.valueOf(text.substring(1, 3), 16), Integer.valueOf(text.substring(3, 5), 16), Integer.valueOf(text.substring(5, 7), 16)));
			} else if (!text.startsWith("#") && text.length() == 6) { //$NON-NLS-1$
				newColor = AlfaRGB.getFullyOpaque(new RGB(Integer.valueOf(text.substring(0, 2), 16), Integer.valueOf(text.substring(2, 4), 16), Integer.valueOf(text.substring(4, 6), 16)));
			} else {
				String[] components = text.split(",");
				int[] resultComp = new int[]{0,0,0,255};
				boolean colsedBars = (text.contains("[") && text.contains("]")) || (text.contains("(") && text.contains(")")) || (text.contains("{") && text.contains("}")) || (!text.contains("{") && !text.contains("[") && !text.contains("("));
				if (components.length>2 && colsedBars){
					for(int i=0; i<components.length && i<4; i++){
						String component = components[i].replaceAll("[^\\d]", "");;
						resultComp[i] = Integer.valueOf(component);
					}
					newColor = new AlfaRGB(new RGB(resultComp[0],resultComp[1],resultComp[2]), resultComp[3]);
				}
			}
		} catch (NumberFormatException ex) {
		} catch (IllegalArgumentException ex) {
		}
		return newColor;
	}
	

	/**
	 * Return the hexadecimal representation of a color
	 * 
	 * @param color The color
	 * @return The color hexadecimal representation
	 */
	private String getHexFromRGB(RGB color) {
		int r = color.red;
		int g = color.green;
		int b = color.blue;
		String s = Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
		return "#" + StringUtils.rightPad(s, 6, "0").toUpperCase(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Refresh the text area with the values of the color inside the color selection widget.
	 * It will also update the color preview area
	 */
	private void updateText(){
		updateText(colorsSelector.getSelectedColorRGB(), colorsSelector.getSelectedColorHSB(), null);
	}
	
	/**
	 * Refresh the text area with passed color
	 * It will also update the color preview area
	 * 
	 * @param color the rgb of the new color
	 * @param hsb an array of float that represent the hsb values of the color
	 * @param source the widget that raised the event, to avoid a selection text problem
	 * on some platforms the text that has raised the event must not be set, otherwise it 
	 * will be automatically selected. It this value is null then all the widget will be set
	 */
	private void updateText(RGB color, float[] hsb, Widget source){
		synchronized (modfiedGuard) {
			modfiedGuard = false;
			hue.getParent().getParent().setRedraw(false);
			if (red != source) red.setSelection(color.red);
			if (green != source) green.setSelection(color.green);
			if (blue != source) blue.setSelection(color.blue);
			hex.setText(getHexFromRGB(color));
			int h = Math.round(hsb[0]);
			int s = Math.round(hsb[1]*100);
			int b = Math.round(hsb[2]*100);
			if (hue != source) hue.setSelection(h);
			if (saturation != source) saturation.setSelection(s);
			if (brightness != source) brightness.setSelection(b);
			hue.getParent().getParent().setRedraw(true);
			updatePreview();
			modfiedGuard = true;
		}
	}
	
	/**
	 * Update the preview area with the color inside the selector widget
	 */
	private void updatePreview(){
		newColor = new AlfaRGB(colorsSelector.getSelectedColorRGB(), alpha);
		previewComposite.setNewColor(colorsSelector.getSelectedColorRGB(), alpha);
	}
	
	/**
	 * Refresh the text area with passed color
	 * It will also update the color preview area
	 * 
	 * @param fh hue of the color
	 * @param fs saturation of the color
	 * @param fb brightness of the color
	 * @param source the widget that raised the event, to avoid a selection text problem
	 * on some platforms the text that has raised the event must not be set, otherwise it 
	 * will be automatically selected. It this value is null then all the widget will be set
	 */
	private void updateText(float fh, float fs, float fb, Widget source){
		synchronized (modfiedGuard) {
			modfiedGuard = false;
			hue.getParent().getParent().setRedraw(false);
			int h = Math.round(fh);
			int s = Math.round(fs*100);
			int b = Math.round(fb*100);
			if (hue != source) hue.setSelection(h);
			if (saturation != source) saturation.setSelection(s);
			if (brightness != source) brightness.setSelection(b);
			RGB color = new RGB(fh,fs,fb);
			if (red != source) red.setSelection(color.red);
			if (green != source) green.setSelection(color.green);
			if (blue != source) blue.setSelection(color.blue);
			hex.setText(getHexFromRGB(color));
			hue.getParent().getParent().setRedraw(true);
			updatePreview();
			modfiedGuard = true;
		}
	}
	
	/**
	 * Create the color picker button control, to press to start a color picking
	 * 
	 * @param parent where the control will be palced
	 */
	private void createColorPicker(Composite parent){
		 pickColorButton = new Button(parent, SWT.WRAP);
		 pickColorButton.setToolTipText(Messages.ColorDialog_pickerButtonTooltip);
		 pickColorButton.setFont(buttonFont);
		 pickColorButton.setAlignment(SWT.CENTER);
		 GridData buttonData = new GridData();
		 buttonData.widthHint = 50;
		 buttonData.heightHint = 50;
		 buttonData.verticalAlignment = SWT.CENTER;
		 pickColorButton.setLayoutData(buttonData);
		 pickColorButton.setText("");
		 pickColorButton.addPaintListener(new PaintListener() {
       public void paintControl(PaintEvent e) {
        if (pickColorButton.getText().isEmpty()) {
        	int size = 24;
        	int x = (e.width/2) - (size/2);
        	int y = (e.height/2) - (size/2);
        	e.gc.setAntialias(SWT.ON);
        	e.gc.drawImage(getPickerImage(size),x,y);
        }
       }
		 });
		 //When the button is pressed the color picking thread is started,
	   //the button is disabled and its content is changed to tell the user
	   //how to stop the picking thread
		 pickColorButton.addSelectionListener(new SelectionAdapter() {
			 @Override
			public void widgetSelected(SelectionEvent e) {
				isAcquiring = true;
				colorPickerThread.setStop(false);
				Display.getCurrent().timerExec(TIMER_INTERVAL, colorPickerThread);
        pickColorButton.setImage(null);
        pickColorButton.setText(Messages.ColorDialog_stopPickingActionText);
        pickColorButton.setEnabled(false);
        pickColorButton.setLayoutData(new GridData(GridData.FILL_BOTH));
        pickColorButton.getParent().layout(true, true);
       // getShell().forceFocus();
			}
		 });
	}
	
	/**
	 * Return the image of the color picker button and also cache it and removed 
	 * when the application is closed.
	 * The image can be requested of a specific size. But the width 
	 * must be the same of the height
	 * 
	 * @param size height and width of the image
	 * @return the image
	 */
  private Image getPickerImage(int size){
  	String key = "pickerIcon"+String.valueOf(size); //$NON-NLS-1$
  	Image result = ResourceManager.getImage(key);
  	if (result == null){
  		result = ImageUtils.resize(ResourceManager.getPluginImage(JaspersoftStudioPlugin.PLUGIN_ID, "/icons/resources/picker.gif"), size, size); //$NON-NLS-1$
  		ImageData data = result.getImageData();
  		result.dispose();
  		int whitePixel = data.palette.getPixel(new RGB(255,255,255));
  		data.transparentPixel = whitePixel;
  		result = new Image(Display.getCurrent(), data);
  		ResourceManager.addImage(key, result);
  	}
  	return result;
  }
	
	/**
	 * Create the slider and textual controls to define the alpha
	 * 
	 * @param parent parent of the controls
	 */
	private void createSlider(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		new Label(container, SWT.NONE).setText(Messages.ColorDialog_transparencyLabel);
		alphaSlider = new Scale(container, SWT.HORIZONTAL);
		alphaSlider.setMaximum(0);
		alphaSlider.setMaximum(255);
		alphaSlider.setSelection(alpha);
		alphaSlider.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		alphaText = new Spinner(container, SWT.BORDER);
		alphaText.setMinimum(0);
		alphaText.setMaximum(255);
		alphaText.setSelection(alpha);
		//When the alpha slider change value update also the preview composite and the alpha textual value
		alphaSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				alphaText.setSelection(alphaSlider.getSelection());
				alpha = alphaSlider.getSelection();
				updatePreview();
			}
		});
		//When the alpha text field change value update also the preview composite and the alpha slider value
		alphaText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				alphaSlider.setSelection(alphaText.getSelection());
				alpha = alphaSlider.getSelection();
				updatePreview();
			}
		});
	}
	
	/**
	 * Create the area where all the text fields describing the color values
	 * are placed
	 * 
	 * @param parent parent of the area
	 */
	private void createTextArea(Composite parent){
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite leftPart = new Composite(container, SWT.NONE);
		leftPart.setLayout(new GridLayout(3,false));
		leftPart.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite rightPart = new Composite(container, SWT.NONE);
		rightPart.setLayout(new GridLayout(3,false));
		rightPart.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		hue = createRadio(leftPart, "H:", "\u030A", new HueBasedSelector(), true, 0, 360); //$NON-NLS-1$ //$NON-NLS-2$
		saturation = createRadio(leftPart, "S:", "%", new SaturationBasedSelector(), false, 0, 100); //$NON-NLS-1$ //$NON-NLS-2$
		brightness = createRadio(leftPart, "B:", "%", new BrightnessBasedSelector(), false, 0, 100); //$NON-NLS-1$ //$NON-NLS-2$
		red = createRadio(rightPart, "R:", " ", new RedBasedSelector(), false, 0, 255); //$NON-NLS-1$ //$NON-NLS-2$
		green = createRadio(rightPart, "G:", " ", new GreenBasedSelector(), false, 0, 255); //$NON-NLS-1$ //$NON-NLS-2$
		blue = createRadio(rightPart, "B:", " ", new BluBasedSelector(), false, 0, 255); //$NON-NLS-1$ //$NON-NLS-2$
		hex = createText(leftPart, "Hex:", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Create a single text area with a label before and after it
	 * 
	 * @param parent parent of the text area
	 * @param title content of a label placed before the text area
	 * @param suffix content of a label placed after the text area
	 * @return the text area created
	 */
	private Text createText(Composite parent, String title, String suffix){
		new Label(parent, SWT.NONE).setText(title);
		Text actualText = new Text(parent, SWT.BORDER);
		actualText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (suffix != null) new Label(parent, SWT.NONE).setText(suffix);
		else new Label(parent, SWT.NONE);
		actualText.addModifyListener(valueModifedListener);
		return actualText;
	}

	/**
	 * Create a radio button followed by a spinner with a maximum and a minimum value. At the radio 
	 * button can be associated a governor. This object define how the color picker area is painted.
	 * This is done because when a button is selected its governor is set into the color picker widget
	 * changing the color space. When a radio button created with this method is selected all the other
	 * are deselected.
	 * 
	 * @param parent parent of the controls
	 * @param title content of a label placed as text of the radio button
	 * @param suffix content of a label placed after the spinner
	 * @param governor the governor that is loaded in the color picker widget when the button is selected
	 * @param defaultEnabled true if the radio button is enabled by default, false otherwise
	 * @param min min int value for the spinner
	 * @param max max int value for the spinner
	 * @return the spinner created
	 */
	private Spinner createRadio(Composite parent, String title, String suffix, IWidgetGovernor governor, boolean defaultEnabled, int min, int max){
		final Button radio = new Button(parent, SWT.RADIO);
		radioList.add(radio);
		radio.setText(title);
		radio.setData(governor);
		radio.setSelection(defaultEnabled);
		radio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (radio.getSelection()){
					disableAllRadioExceptOne(radio);
					colorsSelector.setGovernor((IWidgetGovernor)radio.getData());
				}
			}
		
		});
		Spinner actualText = new Spinner(parent, SWT.BORDER);
		actualText.setMinimum(min);
		actualText.setMaximum(max);
		actualText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (suffix != null) new Label(parent, SWT.NONE).setText(suffix);
		else new Label(parent, SWT.NONE);
		actualText.addModifyListener(valueModifedListener);
		return actualText;
	}
	
	/**
	 * Disable all the radio buttons except the one passed as parameter
	 * 
	 * @param excludedRadio reference to the radio button that will not be disabled
	 */
	private void disableAllRadioExceptOne(Button excludedRadio){
		for(Button radio : radioList){
			if (radio.getSelection() && radio != excludedRadio){
				radio.setSelection(false);
			}
		}
	}
	
	/**
	 * Read the color under the mouse position and set it as the current color. This 
	 * is done only if JSS or one of its components are focused. It is necessary to control
	 * this dialog and its content because it modal, so every other component of JSS can not be focused
	 */
  private void checkColorPicker() {
		if (!isDisposed() && (getShell().isFocusControl() || checkControlFocused(getShell().getChildren()))){
	  	Robot robot;
			try {
				robot = new Robot();
				Point pos = Display.getCurrent().getCursorLocation();
				java.awt.Color color = robot.getPixelColor(pos.x, pos.y);
				RGB rgbColor = new RGB(color.getRed(), color.getGreen(), color.getBlue());
				colorsSelector.setSelectedColor(rgbColor, false);
				updateText(rgbColor, rgbColor.getHSB(), null);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
  }
  
  /**
   * Check if the passed controls or one of its children is focused 
   * 
   * @param controls 
   * @return true if a control in the searching set is focused, false if none 
   * control is focused
   */
	private boolean checkControlFocused(Control[] controls){
		for(Control control : controls){
			if (control.isFocusControl()) return true;
			if (control instanceof Composite){
				Composite comp = (Composite)control;
				boolean childrenFocused = checkControlFocused(comp.getChildren());
				if (childrenFocused) return true;
			}
		}
		return false;
	}
  
	/**
	 * Set the flag to close the color picker thread, remove the key listener
	 * used to know when the thread should be stopped and restore the status 
	 * of the button to restart the thread (so the user can restart it manually).
	 */
  private void stopPickerThread(){
		colorPickerThread.setStop(true);
    if (!pickColorButton.isDisposed()){
	    pickColorButton.setText(""); //$NON-NLS-1$
	    pickColorButton.setEnabled(true);
			GridData buttonData = new GridData();
			buttonData.widthHint = 50;
		  buttonData.heightHint = 50;
	    //pickColorButton.setImage(getPickerImage(24));
			pickColorButton.setLayoutData(buttonData);
			pickColorButton.getParent().layout(true, true);
    }
	}
  
  
  /**
   * Dispose the control and if it is still active stop the thread
   */
  @Override
  public void dispose() {
  	super.dispose();
  	PlatformUI.getWorkbench().getDisplay().removeFilter(org.eclipse.swt.SWT.KeyDown, spaceKeyListener);
		stopPickerThread();
  }

}
