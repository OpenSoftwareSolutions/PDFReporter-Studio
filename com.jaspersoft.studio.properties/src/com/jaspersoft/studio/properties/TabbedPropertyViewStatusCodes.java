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
package com.jaspersoft.studio.properties;

/**
 * A list of status codes for this plug-in.
 * 
 * @author Anthony Hunter
 */
public final class TabbedPropertyViewStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 */
	private TabbedPropertyViewStatusCodes() {
		/* not used */
	}

	/**
	 * Status code indicating that everything is OK.
	 */
	public static final int OK = 0;

	/**
	 * Status code indicating that a tab was not found for the given tab id.
	 */
	public static final int NO_TAB_ERROR = 1;

	/**
	 * Status code indicating that issue was found loading the section extension
	 * configuration element.
	 */
	public static final int SECTION_ERROR = 2;

	/**
	 * Status code indicating that issue was found loading the tab extension
	 * configuration element.
	 */
	public static final int TAB_ERROR = 3;

	/**
	 * Status code indicating that issue was found loading the contributor
	 * extension configuration element.
	 */
	public static final int CONTRIBUTOR_ERROR = 4;
}
