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
package com.jaspersoft.studio.editor.preview.actions;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.MultiPageContainer;
import com.jaspersoft.studio.editor.preview.view.control.ReportControler;
import com.jaspersoft.studio.messages.Messages;

public class ViewBookmarksAction extends ASwitchAction {

	public ViewBookmarksAction(MultiPageContainer pContainer) {
		super(pContainer, ReportControler.FORM_BOOKMARKS);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/bookmark.png"));
		setToolTipText(Messages.commons_bookmarks);
	}
}
