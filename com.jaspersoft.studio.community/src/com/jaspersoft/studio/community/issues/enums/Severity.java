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
 * Enumeration representing the issue severity.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public enum Severity {
	Feature(10,Messages.Severity_Feature),
	Trivial(20,Messages.Severity_Trivial),
	Text(30,Messages.Severity_Text),
	Tweak(40,Messages.Severity_Tweak),
	Minor(50,Messages.Severity_Minor),
	Major(60,Messages.Severity_Major),
	Critical(70,Messages.Severity_Critical),
	Block(80,Messages.Severity_Block);
	
	public static final String FIELD_NAME = "field_bug_severity"; //$NON-NLS-1$
	private Integer value;
	private String text;
	
	private Severity(Integer value,String text){
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
