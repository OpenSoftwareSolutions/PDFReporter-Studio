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
package com.jaspersoft.studio.components.map.model.marker.dialog;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.marker.MarkersDTO;

public class MarkerEditor extends Wizard {
	private MarkersDTO value;
	private MarkerPage page0;

	public MarkersDTO getValue() {
		if (page0 != null)
			return page0.getValue();
		return value;
	}

	public void setValue(MarkersDTO value) {
		if (page0 != null)
			page0.setValue(value);
		this.value = value;
	}

	public MarkerEditor() {
		super();
		setWindowTitle(Messages.MarkerEditor_Title);
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		page0 = new MarkerPage("mapmarkers"); //$NON-NLS-1$
		page0.setValue(value);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
