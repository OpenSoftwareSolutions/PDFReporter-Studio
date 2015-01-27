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
package com.jaspersoft.studio.editor.gef.util;

import org.eclipse.gef.requests.CreateRequest;

public class CreateRequestUtil {
	public static final String NEWINDEX = "newindex";

	public static int getNewIndex(CreateRequest request) {
		int index = -1;
		if (request.getExtendedData() != null && request.getExtendedData().get(NEWINDEX) != null)
			index = (Integer) request.getExtendedData().get(NEWINDEX);
		return index;
	}
}
