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
package com.jaspersoft.studio.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessagesByKeys {
	
	private static final String MESSAGES_BY_KEYS = "com.jaspersoft.studio.messages.messagesbykeys";
	private static final ResourceBundle RB_MESSAGES_BY_KEYS = ResourceBundle.getBundle(MESSAGES_BY_KEYS);
	
	private MessagesByKeys(){
	}

	public static String getString(String key) {
		try {
			return RB_MESSAGES_BY_KEYS.getString(key.toLowerCase());
		} catch (MissingResourceException e) {
			return key;
		}
	}
	
	public static boolean hasTranslation(String key) {
		return RB_MESSAGES_BY_KEYS.containsKey(key);
	}
	
}
