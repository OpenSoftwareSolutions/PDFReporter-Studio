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
package com.jaspersoft.studio.swt.toolbar;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolItemContribution extends ContributionItem {
	private ToolItem toolItem;
	private int style;

	/**
	 * Creates a control contribution item with the given id.
	 * 
	 * @param id
	 *          the contribution item id
	 */
	public ToolItemContribution(String id, int style) {
		super(id);
		this.style = style;
	}

	/**
	 * Computes the width of the given control which is being added to a tool bar. This is needed to determine the width
	 * of the tool bar item containing the given control.
	 * <p>
	 * The default implementation of this framework method returns
	 * <code>control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x</code>. Subclasses may override if required.
	 * </p>
	 * 
	 * @param control
	 *          the control being added
	 * @return the width of the control
	 */
	protected int computeWidth(Control control) {
		return control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
	}

	/**
	 * The control item implementation of this <code>IContributionItem</code> method throws an exception since controls
	 * cannot be added to menus.
	 */
	public final void fill(Menu parent, int index) {
		Assert.isTrue(false, "Can't add a control to a menu");//$NON-NLS-1$
	}

	/**
	 * The control item implementation of this <code>IContributionItem</code> method calls the <code>createControl</code>
	 * framework method to create a control under the given parent, and then creates a new tool item to hold it.
	 * Subclasses must implement <code>createControl</code> rather than overriding this method.
	 */
	public final void fill(ToolBar parent, int index) {
		toolItem = new ToolItem(parent, style, index);
	}

	public ToolItem getToolItem() {
		return toolItem;
	}
}
