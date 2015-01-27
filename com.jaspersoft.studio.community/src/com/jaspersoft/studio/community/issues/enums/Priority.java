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
 * Enumeration representing the issue priority.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public enum Priority {
	None(10,Messages.Priority_None),
	Low(20,Messages.Priority_Low),
	Normal(30,Messages.Priority_Normal),
	High(40,Messages.Priority_High),
	Urgent(50,Messages.Priority_Urgent),
	Immediate(60,Messages.Priority_Immediate);
	
	public static final String FIELD_NAME = "field_bug_priority"; //$NON-NLS-1$
	private Integer value;
	private String text;
	
	private Priority(Integer value,String text){
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
