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
package com.jaspersoft.studio.properties.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Message Bundle class for the tabbed properties view plug-in.
 * 
 * @author Anthony Hunter
 * 
 */
public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "com.jaspersoft.studio.properties.messages.messages";//$NON-NLS-1$

	/**
	 * Constructor for TabbedPropertyMessages.
	 */
	private Messages() {
		// private constructor
	}

	/**
	 * Message when a property section extension is in error.
	 */
	public static String SectionDescriptor_Section_error;

	/**
	 * Message when a property section extension causes a class not found
	 * exception.
	 */
	public static String SectionDescriptor_class_not_found_error;

	/**
	 * Message when a property tab extension is in error.
	 */
	public static String TabDescriptor_Tab_error;

	/**
	 * Message when a property tab extension has an unknown category.
	 */
	public static String TabDescriptor_Tab_unknown_category;

	/**
	 * Message when a non existing tab is found in a property section extension.
	 */
	public static String TabbedPropertyRegistry_Non_existing_tab;

	/**
	 * Message when a property contributor extension is in error.
	 */
	public static String TabbedPropertyRegistry_contributor_error;

	public static String TabbedPropertySearch_searchPropertyLabel;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
