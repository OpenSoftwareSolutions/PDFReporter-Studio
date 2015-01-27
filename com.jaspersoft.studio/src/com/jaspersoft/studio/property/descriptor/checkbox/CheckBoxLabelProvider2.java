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
package com.jaspersoft.studio.property.descriptor.checkbox;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceCache;
import org.eclipse.wb.swt.SWTResourceManager;

public class CheckBoxLabelProvider2 extends LabelProvider {

	private static final String CHECKED_KEY = "CHECKED"; //$NON-NLS-1$
	private static final String UNCHECK_KEY = "UNCHECKED"; //$NON-NLS-1$
	
	private ResourceCache cache = new ResourceCache();

	public CheckBoxLabelProvider2() {
		if (JFaceResources.getImageRegistry().getDescriptor(CHECKED_KEY) == null) {
			JFaceResources.getImageRegistry().put(UNCHECK_KEY, makeShot(false));
			JFaceResources.getImageRegistry().put(CHECKED_KEY, makeShot(true));
		}
	}

	private Image makeShot(boolean type) {
		// Hopefully no platform uses exactly this color because we'll make
		// it transparent in the image.
		Display display = Display.getCurrent();
		Color greenScreen = SWTResourceManager.getColor(222, 223, 224);

		Shell shell = new Shell(display.getActiveShell(), SWT.NO_TRIM);

		// otherwise we have a default gray color
		shell.setBackground(greenScreen);

		Button button = new Button(shell, SWT.CHECK);
		button.setBackground(greenScreen);
		button.setSelection(type);

		// otherwise an image is located in a corner
		button.setLocation(1, 1);
		Point bsize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		// otherwise an image is stretched by width
		bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
		bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
		button.setSize(bsize);
		shell.setSize(bsize);

		Image image = new Image(display, bsize.x, bsize.y);
		shell.open();
		GC gc = new GC(shell);
		try {
			gc.copyArea(image, 0, 0);
		} finally {
			gc.dispose();
			shell.close();
		}

		ImageData imageData = image.getImageData();
		imageData.transparentPixel = imageData.palette.getPixel(greenScreen.getRGB());
		image.dispose();
		Image resultImage = cache.getImage(imageData);
		return resultImage;
	}

	public Image getImage(Object element) {
		if (element instanceof Boolean && ((Boolean) element).booleanValue()) {
			return JFaceResources.getImageRegistry().get(CHECKED_KEY);
		} else {
			return JFaceResources.getImageRegistry().get(UNCHECK_KEY);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		cache.dispose();
	}

}
