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

public class ChecksumMode {
	public static String[] getItems() {
		return new String[] { "<" + Messages.common_default + ">", Messages.ChecksumMode_auto, Messages.ChecksumMode_ignore, Messages.ChecksumMode_add, Messages.ChecksumMode_check };
	}

	public static int getPos4ChecksumMode(String mode) {
		if (mode != null) {
			if (mode.equals("<default>")) //$NON-NLS-1$
				return 0;
			if (mode.equals("Auto")) //$NON-NLS-1$
				return 1;
			if (mode.equals("Ignore")) //$NON-NLS-1$
				return 2;
			if (mode.equals("Add")) //$NON-NLS-1$
				return 3;
			if (mode.equals("Check")) //$NON-NLS-1$
				return 4;
		}
		return 0;
	}

	public static String getChecksumMode4Pos(int pos) {
		switch (pos) {
		case 0:
			return "<default>"; //$NON-NLS-1$
		case 1:
			return "Auto"; //$NON-NLS-1$
		case 2:
			return "Ignore"; //$NON-NLS-1$
		case 3:
			return "Add"; //$NON-NLS-1$
		case 4:
			return "Check"; //$NON-NLS-1$
		}
		return null;
	}
}
