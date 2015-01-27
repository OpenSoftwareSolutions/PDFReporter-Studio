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
package com.jaspersoft.studio.preferences.editor.pages;

import java.util.StringTokenizer;

public class Pages {
	private Integer page;

	public Integer getPage() {
		return page;
	}

	public Integer getFrom() {
		return from;
	}

	public Integer getTo() {
		return to;
	}

	private Integer from;
	private Integer to;

	public Pages parseString(String key) {
		if (key.equals("all")) {
		} else if (key.contains(";")) {
			StringTokenizer st = new StringTokenizer(key, ";");
			from = new Integer(0);
			to = new Integer(0);
			try {
				from = new Integer(st.nextToken());
				to = new Integer(st.nextToken());
			} catch (NumberFormatException e) {
			}
			if (to < from)
				to = from;
		} else {
			try {
				page = new Integer(key);
			} catch (NumberFormatException e) {
				page = new Integer(0);
			}
		}
		return this;
	}
}
