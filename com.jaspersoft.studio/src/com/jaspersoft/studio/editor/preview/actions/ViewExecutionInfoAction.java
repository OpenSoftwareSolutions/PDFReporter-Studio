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

import org.eclipse.jface.action.Action;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.messages.Messages;

public class ViewExecutionInfoAction extends Action {
	private PreviewJRPrint rcontainer;

	public ViewExecutionInfoAction(PreviewJRPrint rcontainer) {
		super();
		this.rcontainer = rcontainer;
		setImageDescriptor(
				JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/information-white.png")); //$NON-NLS-1$
		setToolTipText(Messages.ViewExecutionInfoAction_tooltip);
	}

	@Override
	public void run() {
		rcontainer.getConsole().showConsole();
	}
}
