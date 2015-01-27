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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.color.chooser.ColorDialog;
import com.jaspersoft.studio.swt.events.ColorSelectedEvent;
import com.jaspersoft.studio.swt.events.ColorSelectionListener;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

/**
 * A custom widget that lets the user pick a color using the standard {@link ColorDialog} window. The widgets is
 * composed by three parts:
 * <ul>
 * <li>a squared-box with the preview of the chosen color;</li>
 * <li>a readonly textbox with the hexadecimal representation of the color;</li>
 * <li>an icon button that enables the user to select the color.</li>
 * </ul>
 * 
 * Users can retrieve the modified information through the different getters: {@link #getSelectedColorAsAWTColor()},
 * {@link #getSelectedColorAsRGB()} and {@link #getSelectedColorAsSWTColor()}.<br>
 * Otherwise they can listen to the color changes, adding a {@link ColorSelectionListener} to this widget instance.
 * 
 * @author mrabbi
 * 
 * 
 */
public class WColorPicker extends Composite {

	private static final String BUTTON_ICON_LOCATION = "icons/resources/colorwheel-16.png"; //$NON-NLS-1$
	private static final String BUTTON_DISABLED_ICON_LOCATION = "icons/resources/colorwheel-16-disabled.png"; //$NON-NLS-1$
	private AlfaRGB selectedRGB;
	private CLabel imgColorPreview;
	private Text textColorValue;
	private List<ColorSelectionListener> colorSelectionListeners;
	private ToolItem buttonColorChoser;

	public WColorPicker(AlfaRGB preselectedRGB, Composite parent) {
		this(preselectedRGB, parent, SWT.NONE);
	}

	public WColorPicker(AlfaRGB preselectedRGB, Composite parent, int style) {
		super(parent, style);
		this.selectedRGB = preselectedRGB;
		this.colorSelectionListeners = new ArrayList<ColorSelectionListener>();
		createControl(parent);
	}

	/*
	 * Creates the widget content.
	 */
	private void createControl(Composite parent) {
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout colorChooserLayout = new GridLayout(3, false);
		colorChooserLayout.marginHeight = 0;
		colorChooserLayout.marginWidth = 0;
		this.setLayout(colorChooserLayout);

		imgColorPreview = new CLabel(this, SWT.NONE);
		GridData gridData1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		imgColorPreview.setLayoutData(gridData1);
		imgColorPreview.setImage(Colors.getSWTColorPreview(Colors.getAWT4SWTRGBColor(selectedRGB), 16, 16));

		textColorValue = new Text(this, SWT.BORDER | SWT.BORDER_SOLID | SWT.READ_ONLY);
		GC tmpGC = new GC(textColorValue);
		int charHeight = tmpGC.getFontMetrics().getHeight();
		int averageCharWidth = tmpGC.getFontMetrics().getAverageCharWidth();
		tmpGC.dispose();
		GridData gridData2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gridData2.widthHint = textColorValue.computeSize(averageCharWidth * 10, SWT.DEFAULT).x;
		gridData2.heightHint = charHeight;
		textColorValue.setLayoutData(gridData2);
		textColorValue.setText(Colors.getHexEncodedRGBColor(selectedRGB));

		ToolBar toolBar = new ToolBar(this, SWT.NONE);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		buttonColorChoser = new ToolItem(toolBar, SWT.FLAT);
		buttonColorChoser.setImage(JaspersoftStudioPlugin.getInstance().getImage(BUTTON_ICON_LOCATION));
		buttonColorChoser.setDisabledImage(JaspersoftStudioPlugin.getInstance().getImage(BUTTON_DISABLED_ICON_LOCATION));
		buttonColorChoser.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(getShell());
				cd.setText(Messages.ColorsSection_element_forecolor);
				cd.setRGB(selectedRGB);
				AlfaRGB newColor = cd.openAlfaRGB();
				if(newColor!=null){
					setColor(newColor);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	/*
	 * Notify listeners for the color change.
	 */
	private void notifyColorSelection() {
		for (ColorSelectionListener l : colorSelectionListeners) {
			ColorSelectedEvent evt = new ColorSelectedEvent(this);
			evt.selectedColor = selectedRGB;
			l.changed(evt);
		}
	}

	/**
	 * Sets the new color (as {@link RGB} value). It also notifies all listeners of the color change occurred.
	 * 
	 * @param newColor
	 *          the new RGB color to set
	 */
	public void setColor(AlfaRGB newColor) {
		// Updates color information
		selectedRGB = newColor;
		textColorValue.setText(Colors.getHexEncodedRGBColor(selectedRGB));
		updatePreviewImage(isEnabled());
		notifyColorSelection();
	}

	/**
	 * Add a new listener for color changes.
	 * 
	 * @param listener
	 *          the listener to add
	 */
	public void addColorSelectionListener(ColorSelectionListener listener) {
		this.colorSelectionListeners.add(listener);
	}

	/**
	 * Remove the specified {@link ColorSelectionListener} instance.
	 * 
	 * @param listener
	 *          the listener to be removed
	 */
	public void removeColorSelectionListener(ColorSelectionListener listener) {
		this.colorSelectionListeners.remove(listener);
	}

	/**
	 * Gets the selected color as {@link java.awt.Color} instance.
	 * 
	 * @return the AWT selected color
	 */
	public java.awt.Color getSelectedColorAsAWTColor() {
		return Colors.getAWT4SWTRGBColor(selectedRGB);
	}

	/**
	 * Gets the selected color as {@link Color} instance.
	 * <p>
	 * <b>NOTE</b>: the {@link Color} instance returned must be directly disposed by the user in order to release system
	 * resources. No automatic disposal is given.
	 * 
	 * @return the SWT selected color
	 */
	public Color getSelectedColorAsSWTColor() {
		return selectedRGB != null ? SWTResourceManager.getColor(selectedRGB.getRgb()) : null;
	}

	/**
	 * Gets the selected color as {@link RGB} instance.
	 * 
	 * @return the RGB of the selected color
	 */
	public AlfaRGB getSelectedColorAsRGB() {
		return selectedRGB;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		updatePreviewImage(enabled);
		this.imgColorPreview.setEnabled(enabled);
		this.textColorValue.setEnabled(enabled);
		this.buttonColorChoser.setEnabled(enabled);
	}

	/*
	 * Internally updates the color preview image.
	 */
	private void updatePreviewImage(boolean enabled) {
		// Remember to dispose the old image no longer needed in order to release system resources
		Image oldImage = imgColorPreview.getImage();
		if (oldImage != null) {
			oldImage.dispose();
		}
		Image newImage = Colors.getSWTColorPreview(Colors.getAWT4SWTRGBColor(selectedRGB), 16, 16);
		if (enabled) {
			imgColorPreview.setImage(newImage);
		} else {
			Image disabledImg = new Image(getDisplay(), newImage, SWT.IMAGE_DISABLE);
			imgColorPreview.setImage(disabledImg);
			newImage.dispose();
		}
	}
}
