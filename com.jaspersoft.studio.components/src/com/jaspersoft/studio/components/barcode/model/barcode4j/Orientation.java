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
package com.jaspersoft.studio.components.barcode.model.barcode4j;

import net.sf.jasperreports.components.barcode4j.BarcodeComponent;

import com.jaspersoft.studio.components.barcode.messages.Messages;

public class Orientation {
	public static String[] getItems() {
		return new String[] { Messages.Orientation_up, Messages.Orientation_left, Messages.Orientation_down, Messages.Orientation_right };
	}

	public static int getPos4Orientation(int orientation) {
		switch (orientation) {
		case BarcodeComponent.ORIENTATION_UP:
			return 0;
		case BarcodeComponent.ORIENTATION_LEFT:
			return 1;
		case BarcodeComponent.ORIENTATION_DOWN:
			return 2;
		case BarcodeComponent.ORIENTATION_RIGHT:
			return 3;
		}
		return -1;
	}

	public static int getOrientation4Pos(int pos) {
		switch (pos) {
		case 0:
			return BarcodeComponent.ORIENTATION_UP;
		case 1:
			return BarcodeComponent.ORIENTATION_LEFT;
		case 2:
			return BarcodeComponent.ORIENTATION_DOWN;
		case 3:
			return BarcodeComponent.ORIENTATION_RIGHT;
		}
		return 0;
	}
}
