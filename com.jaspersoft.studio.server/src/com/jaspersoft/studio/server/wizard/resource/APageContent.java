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
package com.jaspersoft.studio.server.wizard.resource;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.utils.IPageCompleteListener;
import com.jaspersoft.studio.wizards.AWizardPage;

public abstract class APageContent implements IPageCompleteListener {
	protected AWizardPage page;
	protected ANode pnode;
	protected MResource res;
	protected DataBindingContext bindingContext;

	public APageContent(ANode parent, MResource resource) {
		this(parent, resource, new DataBindingContext());
	}

	public APageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		this.res = resource;
		this.pnode = parent;
		this.bindingContext = bindingContext;
	}

	protected IConnection getWsClient() {
		IConnection con = res.getWsClient();
		if (con == null) {
			if (pnode instanceof MResource)
				con = ((MResource) pnode).getWsClient();
			else if (pnode instanceof MServerProfile)
				con = ((MServerProfile) pnode).getWsClient();
		}
		return con;
	}

	public void setBindingContext(DataBindingContext bindingContext) {
		this.bindingContext = bindingContext;
		rebind();
	}

	protected abstract void rebind();

	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	public abstract String getName();

	public abstract String getPageName();

	public abstract Control createContent(Composite parent);

	public static IWizardPage[] getPages(MResource res, APageContent... rcontent) {
		if (res.getValue() != null && res.getValue().getIsNew()) {
			IWizardPage[] pages = new IWizardPage[rcontent.length];
			for (int i = 0; i < pages.length; i++)
				pages[i] = new NewResourcePage(rcontent[i]);
			return pages;
		}
		return new IWizardPage[] { new EditResourcePage(rcontent) };
	}

	public void setPage(AWizardPage page) {
		this.page = page;
	}

	private boolean isPageComplete = true;

	public boolean isPageComplete() {
		return isPageComplete;
	}

	@Override
	public void pageCompleted(boolean completed) {
		setPageComplete(completed);
	}

	public void setPageComplete(boolean complete) {
		this.isPageComplete = complete;
		page.setPageComplete(complete);
	}

	public String getHelpContext() {
		return null;
	}

	public void dispose() {

	}
}
