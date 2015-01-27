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
package com.jaspersoft.studio.editor.preview.input;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.messages.Messages;

public class ImageInput extends ADataInput {
	private Button btn;

	public boolean isForType(Class<?> valueClass) {
		return Image.class.isAssignableFrom(valueClass);
	}

	@Override
	public void createInput(Composite parent, final IParameter param, final Map<String, Object> params) {
		super.createInput(parent, param, params);
		if (isForType(param.getValueClass())) {
			btn = new Button(parent, SWT.NONE);
			btn.setText(Messages.ImageInput_selectimage);
			btn.setToolTipText(param.getDescription());
			btn.addFocusListener(focusListener);
			btn.setAlignment(SWT.LEFT);
			GridData gd = new GridData();
			gd.heightHint = 70;
			gd.widthHint = 300;
			gd.horizontalIndent = 8;
			btn.setLayoutData(gd);
			btn.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					FilteredResourcesSelectionDialog fd = new FilteredResourcesSelectionDialog(Display.getCurrent()
							.getActiveShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
					fd.setInitialPattern("*.png");//$NON-NLS-1$
					if (fd.open() == Dialog.OK) {
						IFile file = (IFile) fd.getFirstResult();
						Image image;
						try {
							image = ImageIO.read(file.getContents());
							updateModel(image);
							setButtonImage(btn, image);
						} catch (Exception e1) {
							UIUtils.showError(e1);
						}
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
			updateInput();
			setNullable(param, btn);
		}
	}

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null && value instanceof Image)
			setButtonImage(btn, (Image) value);
		setDecoratorNullable(param);
	}

	public static void setButtonImage(final Button txt, Image image) {
		org.eclipse.swt.graphics.Image img = new org.eclipse.swt.graphics.Image(txt.getDisplay(), convertAWTImageToSWT(
				image).scaledTo(50, 50));

		txt.setImage(img);
	}

	public static ImageData convertAWTImageToSWT(Image image) {
		if (image == null) {
			throw new IllegalArgumentException(Messages.ImageInput_nullimage);
		}
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		if (w == -1 || h == -1) {
			return null;
		}
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return convertToSWT(bi);
	}

	/**
	 * Converts a buffered image to SWT <code>ImageData</code>.
	 * 
	 * @param bufferedImage
	 *          the buffered image (<code>null</code> not permitted).
	 * 
	 * @return The image data.
	 */
	public static ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
					colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(),
					palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(),
					palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

}
