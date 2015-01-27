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
package com.jaspersoft.studio.community.issues.enums;

import com.jaspersoft.studio.community.messages.Messages;

/**
 * Enumeration representing the issue resolution.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public enum Resolution {
	Open(10,Messages.Resolution_Open),
	Fixed(20,Messages.Resolution_Fixed),
	Reopened(30,Messages.Resolution_Reopened),
	UnableToReproduce(40,Messages.Resolution_UnableToReproduce),
	NotFixable(50,Messages.Resolution_NotFixable),
	Duplicate(60,Messages.Resolution_Duplicate),
	NoChangeRequired(70,Messages.Resolution_NoChangeRequired),
	WorksAsDesigned(75,Messages.Resolution_WorksAsDesigned),
	Suspended(80,Messages.Resolution_Suspended),
	WontFix(90,Messages.Resolution_WontFix);
	
	public static final String FIELD_NAME = "field_bug_resolution"; //$NON-NLS-1$
	private Integer value;
	private String text;
	
	private Resolution(Integer value,String text){
		this.value = value;
		this.text = text;
	}
	
	public String getText(){
		return this.text;
	}
	
	public Integer getValue() {
		return this.value;
	}
	
	public String getStringValue(){
		return Integer.toString(this.value);
	}
}
