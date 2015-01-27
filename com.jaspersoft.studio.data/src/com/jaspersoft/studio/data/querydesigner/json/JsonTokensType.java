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
package com.jaspersoft.studio.data.querydesigner.json;


/**
 * Enumeration for different types of Json Query tokens.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public enum JsonTokensType {
	TEXT(true),KEYWORD(true),QUOTED_LITERAL(true),NUMBER(true),
	SYMBOL(true),EOF(false),EOL(false),SPACE(false),OTHER(true),
	JRPARAMETER(true),JRVARIABLE(true),JRFIELD(true);

	private boolean hasColor;
	
	private JsonTokensType(boolean hasColor) {
		this.hasColor=hasColor;
	}
	
	public static int getColoredTokensNum(){
		int num=0;
		for (JsonTokensType t : values()){
			if(t.hasColor){
				num++;
			}
		}
		return num;
	}
}
