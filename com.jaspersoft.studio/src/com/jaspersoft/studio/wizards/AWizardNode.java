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
package com.jaspersoft.studio.wizards;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.swt.graphics.Point;

public abstract class AWizardNode implements IWizardNode {
	protected IWizard wizard = null;

	public void dispose() {
	}

	public Point getExtent() {
		return new Point(-1, -1);
	}

	public final IWizard getWizard() {
		if (wizard == null)
			wizard = createWizard();
		return wizard;
	}

	protected abstract IWizard createWizard();

	public boolean isContentCreated() {
		return wizard != null;
	}

}
