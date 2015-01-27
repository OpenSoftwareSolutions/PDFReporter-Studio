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

import com.jaspersoft.studio.components.barcode.messages.Messages;

public class TextPosition {
	public static String[] getItems() {
		return new String[] { "<" + Messages.common_default + ">", Messages.TextPosition_none, Messages.common_bottom, Messages.common_top };
	}

	public static int getPos4TextPosition(String textPosition) {
		if (textPosition != null) {
			if (textPosition.equals("")) //$NON-NLS-1$
				return 0;
			if (textPosition.equals("none")) //$NON-NLS-1$
				return 1;
			if (textPosition.equals("bottom")) //$NON-NLS-1$
				return 2;
			if (textPosition.equals("top")) //$NON-NLS-1$
				return 3;
		}
		return 0;
	}

	public static String getTextPosition4Pos(int pos) {
		switch (pos) {
		case 0:
			return ""; //$NON-NLS-1$
		case 1:
			return "none"; //$NON-NLS-1$
		case 2:
			return "bottom"; //$NON-NLS-1$
		case 3:
			return "top"; //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}
}
