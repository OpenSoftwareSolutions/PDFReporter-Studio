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
package com.jaspersoft.studio.editor.preview.view.report.html;

import java.util.List;

import javax.servlet.http.Cookie;

import net.sf.jasperreports.eclipse.viewer.BrowserUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.report.IURLViewable;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ABrowserViewer extends APreview implements IURLViewable {
	protected Browser browser;

	public ABrowserViewer(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		try {
			browser = BrowserUtils.getSWTBrowserWidget(composite, SWT.NONE);
			browser.setLayoutData(new GridData(GridData.FILL_BOTH));
			browser.setJavascriptEnabled(true);
		} catch (Error e) {
			e.printStackTrace();
		}
		return composite;
	}

	@Override
	public void contribute2ToolBar(IToolBarManager tmanager) {
		super.contribute2ToolBar(tmanager);
		urlBar = new URLContributionItem(Misc.nvl(url, " "));
		tmanager.add(urlBar);
		tmanager.add(new Action("", JaspersoftStudioPlugin.getInstance().getImageDescriptor(
				JaspersoftStudioPlugin.ICONS_RESOURCES_REFRESH_16_PNG)) {
			@Override
			public void run() {
				browser.refresh();
			}
		});
	}

	protected String url;
	private URLContributionItem urlBar;

	public void setURL(String url, String urlcookie, String scookie) throws Exception {
		this.url = Misc.nvl(url);
		if (urlBar != null)
			urlBar.setUrl(url);
		if (browser != null) {
			browser.clearSessions();
			if (urlcookie != null && scookie != null)
				Browser.setCookie(scookie, urlcookie);
			browser.setUrl(url);
		}
	}

	public void setURL(String url) throws Exception {
		setURL(url, null, null);
	}
}
