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
package com.jaspersoft.studio.help;

import java.net.URL;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class HelpSystem {
	private HelpSystem() {
	}

	private static HelpSystem instance;

	public static HelpSystem getInstance() {
		if (instance == null)
			instance = new HelpSystem();
		return instance;
	}

	private static HelpListener helpListener;

	private HelpListener getHelpListener() {
		if (helpListener == null)
			helpListener = new HelpListener() {

				@Override
				public void helpRequested(HelpEvent e) {
					Object object = e.widget.getData(HELP_KEY);
					if (object != null && object instanceof String) {
						URL url = PlatformUI.getWorkbench().getHelpSystem().resolve((String) object, false);
						PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(url.toExternalForm());
					}
				}
			};
		return helpListener;
	}

	public static final String HELP_KEY = "org.eclipse.ui.help";//$NON-NLS-1$

	public static void setHelp(final Control control, String href) {
		if (href == null)
			return;
		control.setData(HELP_KEY, href);
		// ensure that the listener is only registered once
		final HelpListener listener = getInstance().getHelpListener();
		control.removeHelpListener(listener);
		control.addHelpListener(listener);
		control.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				control.removeHelpListener(listener);
			}
		});
	}

	public static void bindToHelp(IPropertyDescriptor pDescriptor, Control control) {
		if (pDescriptor.getHelpContextIds() != null)
			PlatformUI.getWorkbench().getHelpSystem().setHelp(control, (String) pDescriptor.getHelpContextIds());
		else if (pDescriptor instanceof IHelp)
			HelpSystem.setHelp(control, ((IHelp) pDescriptor).getHelpReference());
	}
}
