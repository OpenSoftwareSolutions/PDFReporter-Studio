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
package com.jaspersoft.studio.server.publish.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;

import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.publish.ResourcePublishMethod;

public class SelectLocalAction extends Action {
	private MResource mres;
	private TableViewer tableViewer;

	public SelectLocalAction(TableViewer tableViewer) {
		super();
		setText("Use Local Resource");
		this.tableViewer = tableViewer;
	}

	public boolean calculateEnabled(MResource mres) {
		this.mres = mres;
		return mres.getPublishOptions().getPublishMethod() != ResourcePublishMethod.LOCAL;
	}

	@Override
	public void run() {
		mres.getPublishOptions().setPublishMethod(ResourcePublishMethod.LOCAL);
		tableViewer.refresh();
	}
}
