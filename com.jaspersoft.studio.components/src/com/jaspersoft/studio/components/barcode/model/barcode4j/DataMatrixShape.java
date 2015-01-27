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

public class DataMatrixShape {
	public static String[] getItems() {
		return new String[] { "<" + Messages.common_default + ">", Messages.DataMatrixShape_force_none, Messages.DataMatrixShape_force_square, Messages.DataMatrixShape_force_rectangle };
	}

	public static int getPos4Shape(String pos4shape) {
		if (pos4shape != null) {
			if (pos4shape.equals("")) //$NON-NLS-1$
				return 0;
			if (pos4shape.equals("force-none")) //$NON-NLS-1$
				return 1;
			if (pos4shape.equals("force-square")) //$NON-NLS-1$
				return 2;
			if (pos4shape.equals("force-rectangle")) //$NON-NLS-1$
				return 3;
		}
		return 0;
	}

	public static String getShape4Pos(int pos) {
		switch (pos) {
		case 0:
			return ""; //$NON-NLS-1$
		case 1:
			return "force-none"; //$NON-NLS-1$
		case 2:
			return "force-square"; //$NON-NLS-1$
		case 3:
			return "force-rectangle"; //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}
}
