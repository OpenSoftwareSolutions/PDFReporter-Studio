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
package com.jaspersoft.studio.community.issues;

import com.jaspersoft.studio.community.requests.IssueRequest;
import com.jaspersoft.studio.community.utils.CommunityAPIUtils;

/**
 * Body content for an new tracker issue request.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see IssueRequest
 *
 */
public class IssueBody {
	
	private String bodyContent;
	
	public IssueBody(String bodyContent){
		this.bodyContent = bodyContent;
	}
	
	public String asJsonString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\"body\":{"); //$NON-NLS-1$
		sb.append("\"und\":["); //$NON-NLS-1$
		sb.append("{"); //$NON-NLS-1$
		sb.append("\"value\": \"").append(CommunityAPIUtils.jsonStringSanitize(bodyContent)).append("\","); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("\"format\": \"plain_text_html\""); //$NON-NLS-1$
		sb.append("}"); //$NON-NLS-1$
		sb.append("]"); //$NON-NLS-1$
		sb.append("}"); //$NON-NLS-1$
		return sb.toString();
	}
}
