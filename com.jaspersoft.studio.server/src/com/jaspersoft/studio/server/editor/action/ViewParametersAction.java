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
package com.jaspersoft.studio.server.editor.action;

import com.jaspersoft.studio.editor.preview.MultiPageContainer;
import com.jaspersoft.studio.editor.preview.actions.ASwitchAction;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.server.editor.ReportRunControler;

public class ViewParametersAction extends ASwitchAction {

	public ViewParametersAction(MultiPageContainer container) {
		super(container, ReportRunControler.FORM_PARAMETERS);
		setImageDescriptor(MParameter.getIconDescriptor().getIcon16());
		setToolTipText(MParameter.getIconDescriptor().getToolTip());
	}
}
