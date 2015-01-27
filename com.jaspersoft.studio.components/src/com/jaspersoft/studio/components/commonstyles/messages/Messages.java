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
package com.jaspersoft.studio.components.commonstyles.messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.jaspersoft.studio.components.commonstyles.messages.messages"; //$NON-NLS-1$
	public static String CommonViewProvider_createStyleLabel;
	public static String CommonViewProvider_createStyleToolButton;
	public static String CommonViewProvider_deleteStyleLabel;
	public static String CommonViewProvider_deleteStyleQuestionText;
	public static String CommonViewProvider_deleteStyleQuestionTitle;
	public static String CommonViewProvider_deleteStyleToolButton;
	public static String CommonViewProvider_editStyleLabel;
	public static String CommonViewProvider_editStyleToolButton;
	public static String CommonViewProvider_exportStylesToolTip;
	public static String CommonViewProvider_finishLabel;
	public static String ExportDialog_fileErrorMessage;
	public static String ExportDialog_fileErrorTitle;
	public static String ImportDialog_deselectAllButton;
	public static String ImportDialog_dialogLabel;
	public static String ImportDialog_dialogName;
	public static String ImportDialog_pathLabel;
	public static String ImportDialog_selectAllButton;
	public static String ImportExportDialog_browseButtonText;
	public static String ImportExportDialog_destinationText;
	public static String ImportExportDialog_labelText;
	public static String ImportExportDialog_titleText;
	public static String ImportExportDialog_warningText;
	public static String ImportExportDialog_warningTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
