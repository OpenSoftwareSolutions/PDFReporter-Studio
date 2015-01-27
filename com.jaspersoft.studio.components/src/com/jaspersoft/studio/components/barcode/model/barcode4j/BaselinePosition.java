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

public class BaselinePosition {
	public static String[] getItems() {
		return new String[] { "<" + Messages.common_default + ">", Messages.common_top, Messages.common_bottom };   
	}

	public static int getPos4BaselinePosition(String mode) {
		if (mode != null) {
			if (mode.equals("<Default>"))  //$NON-NLS-1$
				return 0;
			if (mode.equals("Top"))  //$NON-NLS-1$
				return 1;
			if (mode.equals("Bottom"))  //$NON-NLS-1$
				return 2;
		}
		return 0;
	}

	public static String getBaselinePosition4Pos(int pos) {
		switch (pos) {
		case 0:
			return "<Default>";  //$NON-NLS-1$
		case 1:
			return "Top";  //$NON-NLS-1$
		case 2:
			return "Bottom";  //$NON-NLS-1$
		}
		return null;
	}
}
