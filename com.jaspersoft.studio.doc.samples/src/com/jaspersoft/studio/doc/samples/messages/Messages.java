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
package com.jaspersoft.studio.doc.samples.messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.jaspersoft.studio.doc.samples.messages.messages"; //$NON-NLS-1$
	public static String ImportSamplesWizardHandler_name_label;
	public static String ImportSamplesWizardHandler_plugin_exist;
	public static String ImportSamplesWizardHandler_suggested_name;
	public static String OpenReportHandler_warningmessage_text1;
	public static String OpenReportHandler_warningmessage_text2;
	public static String OpenReportHandler_warningmessage_title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
