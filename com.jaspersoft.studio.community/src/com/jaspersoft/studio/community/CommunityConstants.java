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
package com.jaspersoft.studio.community;

/**
 * Constants related to the Community Site REST API.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public final class CommunityConstants {

	public static final String LOGIN_URL = "http://community.jaspersoft.com/community-api/user/login"; //$NON-NLS-1$
	public static final String FILE_UPLOAD_URL = "http://community.jaspersoft.com/community-api/file.json"; //$NON-NLS-1$
	public static final String ISSUE_CREATION_URL = "http://community.jaspersoft.com/community-api/node.json"; //$NON-NLS-1$
	public static final String JSON_CONTENT_TYPE = "application/json"; //$NON-NLS-1$
	public static final String REQUEST_CHARSET = "UTF-8"; //$NON-NLS-1$
	public static final String SECURE_PREFSTORE_PATHNAME = "jaspersoft_community_site"; //$NON-NLS-1$
	public static final int JSSPROJECT_COMMUNITY_ID = 496;
	public static final String NODE_CONTENT_URL_PREFIX = "http://community.jaspersoft.com/community-api/node/"; //$NON-NLS-1$
	public static final String ISSUE_SUBMISSION_WIZARD_IMG = "resources/images/softwareBug.png"; //$//$NON-NLS-1$
	public static final String NEW_COMMUNITY_TRACKER_ACCOUNT_URL = "http://community.jaspersoft.com/modal_forms/nojs/login"; //$//$NON-NLS-1$
	
	private CommunityConstants(){
		// prevent instantiation...
	}
}
