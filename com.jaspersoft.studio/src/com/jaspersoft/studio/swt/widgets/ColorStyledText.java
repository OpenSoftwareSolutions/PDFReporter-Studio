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
package com.jaspersoft.studio.swt.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.color.chooser.ColorDialog;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorLabelProvider;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * This class is used to paint a "Color TextBox", a label where a color is expressed in hex value, with a representation
 * of that color on the left and a button to select a color from a SO dependent window. The color can be changed also
 * editing the textual hex value
 * 
 * @author Orlandin Marco
 * 
 */
public class ColorStyledText {

	private static final int LINECOLOR_PREVIEW_WIDTH = 16;
	private static final int LINECOLOR_PREVIEW_HEIGHT = 16;

	/**
	 * The color represented
	 */
	private AlfaRGB color = null;

	/**
	 * The text area
	 */
	private StyledText textArea;

	/**
	 * Last valid textual value inserted for the color
	 */
	private String lastValidText;

	/**
	 * The buttons
	 */
	private Label lineColor;

	/**
	 * Listener called when the color is edited with the button or by editing it's textual value
	 */
	private List<ModifyListener> listener;

	/**
	 * Flag used to disable the call of the events into listener
	 */
	private boolean raiseEvents = true;

	/**
	 * Provider to convert a RGB color into an image
	 */
	private ColorLabelProvider provider;

	/**
	 * Guard that block the modify event when another is already going
	 */
	private boolean refreshingGuard;

	/**
	 * Area where the component is placed
	 */
	private Composite paintArea;
	
	/**
	 * Flag used to know if show or not the controls to define the color alpha
	 */
	private boolean disableAlphaSelection = false;
	
	/**
	 * Flag to set the control enabled or disabled
	 */
	private boolean enabled = true;

	/**
	 * Class that handle the editing of the textual value of the color, if the textual value is in the expected format the
	 * new color will be used and the change will be notified to the handler, otherwise the old color will be taken
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	private class EditListener implements ModifyListener {

		public EditListener() {
		}

		@Override
		public void modifyText(ModifyEvent e) {
			// Check if there are others modifyEvent going
			if (!refreshingGuard) {
				refreshingGuard = true;
				String text = textArea.getText();
				// Convert the text into color only if there are exactly seven chars, a # symbol followed by
				// three pair of hex values
				RGB newColor = null;
				try {
					if (text.startsWith("[") && text.endsWith("]")) {
						int index1 = text.indexOf("[") + 1;
						int index2 = text.indexOf(",");
						int index3 = text.indexOf(",", index2 + 1);
						int index4 = text.indexOf("]");
						int redCompontent = Integer.valueOf(text.substring(index1, index2));
						int greenCompontent = Integer.valueOf(text.substring(index2 + 1, index3));
						int blueCompontent = Integer.valueOf(text.substring(index3 + 1, index4));
						newColor = new RGB(redCompontent, greenCompontent, blueCompontent);
					} else if (text.startsWith("#") && text.length() == 7) {
						newColor = new RGB(Integer.valueOf(text.substring(1, 3), 16), Integer.valueOf(text.substring(3, 5), 16), Integer.valueOf(text.substring(5, 7), 16));
					} else if (!text.startsWith("#") && text.length() == 6) {
						newColor = new RGB(Integer.valueOf(text.substring(0, 2), 16), Integer.valueOf(text.substring(2, 4), 16), Integer.valueOf(text.substring(4, 6), 16));
					}
				} catch (NumberFormatException ex) {
				} catch (IllegalArgumentException ex) {
				}
				// If the color has been changed and the event flag is open then fire the events
				if (newColor != null) {
					if (color != null) color = new AlfaRGB(newColor, color.getAlfa());
					else color = AlfaRGB.getFullyOpaque(newColor);
					textArea.setText(getHexFromRGB(color.getRgb()));
					if (lineColor != null) {
						lineColor.setImage(provider.getImage(color, LINECOLOR_PREVIEW_WIDTH, LINECOLOR_PREVIEW_HEIGHT));
					}
					if (raiseEvents)
						for (ModifyListener element : listener) {
							element.modifyText(e);
						}
				}
				refreshingGuard = false;
			}
		}
	}

	public void setBackground(Color color) {
		paintArea.setBackground(color);
	}

	public void setLayoutData(Object data) {
		paintArea.setLayoutData(data);
	}
	
	/**
	 * When the color dialog is opened to select the color 
	 * this flag is used to determinate if the control 
	 * to define the alpha should be shown.
	 * 
	 * @param value true if the control to change the alpha should not be shown
	 * otherwise false
	 */
	public void DisableAlphaSelection(boolean value){
		disableAlphaSelection = value;
	}

	/**
	 * Check if a keycode, a code associated to a keybord's key, is a number
	 * 
	 * @param keyCode
	 *          the key code
	 * @return true if the code is of a number, false otherwise
	 */
	public static boolean isNumber(int keyCode) {
		return (keyCode >= 48 && keyCode <= 57);
	}

	/**
	 * Check if a keycode, a code associated to a keybord's key, is a character
	 * 
	 * @param keyCode
	 *          the key code
	 * @return true if the code is of a character, false otherwise
	 */
	public static boolean isCharachter(int keyCode) {
		return (keyCode >= 97 && keyCode <= 122);
	}

	/**
	 * Check if a keycode, a code associated to a keybord's key, is alphanumeric
	 * 
	 * @param keyCode
	 *          the key code
	 * @return true if the code is of a alphanumeric, false otherwise
	 */
	public static boolean isAlphanumeric(int keyCode) {
		return isNumber(keyCode) || isCharachter(keyCode);
	}

	/**
	 * Construct the element
	 * 
	 * @param parent
	 *          the composite where the the element will be placed
	 */
	public ColorStyledText(Composite parent) {
		refreshingGuard = false;
		listener = new ArrayList<ModifyListener>();
		provider = new ColorLabelProvider(NullEnum.NULL);
		paintArea = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(2, false);
		paintArea.setLayout(layout);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 0;
		layout.marginHeight = 1;
		layout.marginWidth = 1;

		createButton();

		// Paint the text area
		GridData textData = new GridData();
		textData.verticalAlignment = SWT.CENTER;
		textData.horizontalAlignment = SWT.LEFT;
		textArea = new StyledText(paintArea, SWT.SINGLE);
		textArea.setBackground(paintArea.getBackground());
		// When the text area is disposed also the actual color is disposed as well
		textArea.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				disposeProvider();
			}
		});
		textArea.setLayoutData(textData);
		textArea.setAlignment(SWT.LEFT);
		textArea.addModifyListener(new EditListener());
		textArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	/**
	 * Center to the screen the passed shell
	 * @param shell
	 */
	private Shell centeredShell(Shell shell){
		Shell result = new Shell(shell);
		Rectangle bounds = result.getDisplay().getBounds();
		Rectangle rect = result.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		result.setLocation(x, y);
		return result;
	}

	/**
	 * Create the button to open the dialog of selection color. The button has painted inside a preview of the color
	 */
	private void createButton() {
		// Paint the button
		GridData lineColorData = new GridData();
		lineColorData.heightHint = LINECOLOR_PREVIEW_HEIGHT;
		lineColorData.widthHint = LINECOLOR_PREVIEW_WIDTH;
		lineColorData.verticalAlignment = SWT.FILL;
		lineColorData.horizontalAlignment = SWT.CENTER;
		lineColor = new Label(paintArea, SWT.NONE);
		lineColor.setLayoutData(lineColorData);
		lineColor.setToolTipText(Messages.ColorStyledText_LineColor_ToolTip);

		// Open the color selection window when the button is pushed
		lineColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (enabled){
					ColorDialog cd = new ColorDialog(centeredShell(paintArea.getShell()));
					cd.setText(Messages.common_line_color);
					if (getColor() != null) cd.setRGB(getColor());
					AlfaRGB newColor = null;
					if (disableAlphaSelection) {
						RGB rgbColor = cd.openRGB();
						if (rgbColor != null) newColor = AlfaRGB.getFullyOpaque(rgbColor);
					} else newColor = cd.openAlfaRGB();
					if (newColor != null) {
						setColor(newColor, true);
					}
				}
			}
		});
	}

	/**
	 * Set the contexutal help for the element
	 * 
	 * @param href
	 *          link to the element of the documentation relative to this element
	 */
	public void setHelp(String href) {
		HelpSystem.setHelp(textArea, href);
	}

	private String leftPadWithZero(String baseString) {
		return StringUtils.leftPad(baseString, 2, "0"); //$NON-NLS-1$
	}

	/**
	 * Return the hexadecimal representation of a color
	 * 
	 * @param color
	 *          The color
	 * @return The color hexadecimal representation
	 */
	private String getHexFromRGB(RGB color) {
		int r = color.red;
		int g = color.green;
		int b = color.blue;
		String s = leftPadWithZero(Integer.toHexString(r)) + leftPadWithZero(Integer.toHexString(g))
				+ leftPadWithZero(Integer.toHexString(b));
		return "#".concat(s.toUpperCase()); //$NON-NLS-1$ 
	}

	/**
	 * Set the color of element, either it's representation and it's textual value
	 * 
	 * @param newColor
	 *          the new color
	 * @param callListener
	 *          true to call the edit listener after the editing
	 */
	public void setColor(AlfaRGB newColor, boolean callListener) {
		raiseEvents = callListener;
		// dispose the old color before to create the new one
		color = newColor;
		lastValidText = getHexFromRGB(color.getRgb());
		textArea.setText(lastValidText);
		raiseEvents = true;
	}

	/**
	 * dispose the color label provider
	 */
	private void disposeProvider() {
		if (provider != null)
			provider.dispose();
	}

	/**
	 * Set the color of element, either it's representation and it's textual value. this method dosen't call the edit
	 * listeners
	 * 
	 * @param newColor
	 *          the new color
	 */
	public void setColor(AlfaRGB newColor) {
		setColor(newColor, false);
	}


	/**
	 * Add a new listener for the editing of the color
	 * 
	 * @param listener
	 *          the new listener
	 */
	public void addListener(ModifyListener listener) {
		this.listener.add(listener);
	}

	/**
	 * Return the actual color
	 * 
	 * @return the color in RGB format
	 */
	public AlfaRGB getColor() {
		return color;
	}

	/**
	 * Return the paint area
	 * 
	 * @return composite where all the elements that compose the widget are placed
	 */
	public Composite getPaintArea() {
		return paintArea;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
		textArea.setEnabled(enabled);
	}

}
