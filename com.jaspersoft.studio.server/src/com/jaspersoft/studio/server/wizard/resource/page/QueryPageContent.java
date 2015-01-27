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
package com.jaspersoft.studio.server.wizard.resource.page;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.UIUtil;

public class QueryPageContent extends APageContent {

	public QueryPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public QueryPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	private boolean showLangs = true;
	private static Text tsql;
	private static Combo clang;
	private static QProxy proxy;

	public QueryPageContent(ANode parent, MResource resource, boolean showLangs) {
		super(parent, resource);
		this.showLangs = showLangs;
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.query";
	}

	@Override
	public String getName() {
		return Messages.RDQueryPage_textquery;
	}

	public Control createContent(Composite parent) {
		Control createContentComposite = createContentComposite(parent, bindingContext, res.getValue(), res, showLangs);
		rebind();
		return createContentComposite;
	}

	public static Control createContentComposite(Composite parent, DataBindingContext bindingContext, ResourceDescriptor r, MResource res) {
		return createContentComposite(parent, bindingContext, r, res);
	}

	public static Control createContentComposite(Composite parent, DataBindingContext bindingContext, ResourceDescriptor r, MResource res, boolean showLangs) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		if (showLangs) {
			UIUtil.createLabel(composite, Messages.RDQueryPage_language);

			clang = new Combo(composite, SWT.BORDER);

			clang.setItems(new String[] { "sql", "hql", "domain", "sl", "HiveQL", "MongoDbQuery" });
			// clang.setItems(ModelUtils.getQueryLanguages(res.getJasperConfiguration()));

		}
		UIUtil.createLabel(composite, Messages.RDQueryPage_query);

		tsql = new Text(composite, SWT.BORDER | SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 100;
		gd.widthHint = 400;
		tsql.setLayoutData(gd);

		return composite;
	}

	@Override
	protected void rebind() {
		ResourceDescriptor r = res.getValue();
		if (clang != null && !clang.isDisposed())
			bindingContext.bindValue(SWTObservables.observeText(clang), PojoObservables.observeValue(getProxy(r), "language")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(tsql, SWT.Modify), PojoObservables.observeValue(r, "sql")); //$NON-NLS-1$
	}

	private static QProxy getProxy(ResourceDescriptor rd) {
		if (proxy == null)
			proxy = new QProxy();
		proxy.setResourceDescriptor(rd);
		return proxy;
	}

	public static class QProxy {
		private ResourceDescriptor rd;

		public void setResourceDescriptor(ResourceDescriptor rd) {
			this.rd = rd;
		}

		public void setLanguage(String lang) {
			lang = ModelUtils.getLanguage(lang);
			rd.setResourceProperty(ResourceDescriptor.PROP_QUERY_LANGUAGE, lang);
		}

		public String getLanguage() {
			return rd.getResourcePropertyValue(ResourceDescriptor.PROP_QUERY_LANGUAGE);
		}
	}
}
