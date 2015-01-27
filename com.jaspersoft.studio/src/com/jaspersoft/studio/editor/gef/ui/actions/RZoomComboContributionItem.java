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
package com.jaspersoft.studio.editor.gef.ui.actions;

import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IPartService;
/*
 * The Class RZoomComboContributionItem.
 * 
 * @author Chicu Veaceslav
 */
public class RZoomComboContributionItem extends ZoomComboContributionItem {
	
	/** The combo. */
	private Combo combo;

	/**
	 * Instantiates a new r zoom combo contribution item.
	 * 
	 * @param partService
	 *          the part service
	 */
	public RZoomComboContributionItem(IPartService partService) {
		super(partService);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.ZoomComboContributionItem#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		combo = (Combo) super.createControl(parent);
		return combo;
	}

	/**
	 * Sets the enabled.
	 * 
	 * @param enabled
	 *          the new enabled
	 */
	public void setEnabled(boolean enabled) {
		if (combo != null && !combo.isDisposed())
			combo.setEnabled(enabled);
	}

}
