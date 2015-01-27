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
package com.jaspersoft.studio.editor.preview;

import java.util.Map;

import com.jaspersoft.studio.editor.preview.actions.RunStopAction;

/**
 * This interface should be implemented by whose clients who want to contribute
 * to the extension-point <code>com.jaspersoft.studio.previewModeInfo</code>.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface PreviewModeDetails {
	
	/** Extension point id information */
	String EXTENSION_POINT_ID = "previewModeInfo";

	/** Constant for Local Preview Mode */
	String PREVIEW_MODE_LOCAL = RunStopAction.MODERUN_LOCAL;

	/** Constant for Jive Preview Mode */
	String PREVIEW_MODE_JIVE = RunStopAction.MODERUN_JIVE;

	/**
	 * @return the id of the preview mode to which this details apply to
	 */
	String getPreviewModeID();

	/**
	 * @return a map of properties that should be set in the specified preview mode ({@link #getPreviewModeID()}).
	 */
	Map<String, String> getPreviewModeProperties();
	
}
