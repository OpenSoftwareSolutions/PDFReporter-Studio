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
package com.jaspersoft.studio.community.wizards;

/**
 * In this class are collected all the ids to a contextual helps used in this plugin by the wizards
 * 
 * @author Orlandin Marco
 *
 */
public final class ContextHelpIDs {
	
	private ContextHelpIDs(){
		// Private constructor for utility class
	}
	
	public static final String PREFIX = "com.jaspersoft.studio.doc.";
	
	public static final String WIZARD_ISSUE_DETAIL = PREFIX.concat("issue_detail");
	
	public static final String WIZARD_ISSUE_ATTACHMENTS = PREFIX.concat("issue_attachments");
	
	public static final String WIZARD_ISSUE_LOGIN = PREFIX.concat("issue_login");
}
