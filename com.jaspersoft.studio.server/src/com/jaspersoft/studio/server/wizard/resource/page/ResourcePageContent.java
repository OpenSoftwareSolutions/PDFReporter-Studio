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

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.ui.validator.EmptyStringValidator;
import net.sf.jasperreports.eclipse.ui.validator.IDStringValidator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.restv2.DiffFields;
import com.jaspersoft.studio.server.wizard.permission.PermissionDialog;
import com.jaspersoft.studio.server.wizard.permission.PermissionWizard;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.UIUtil;

public class ResourcePageContent extends APageContent {

	private Text tname;
	private Text tid;
	private Text tudate;
	private Proxy proxy;
	private Text tparent;
	private Text tdesc;
	private Text tcdate;
	private Text ttype;
	private Button bisRef;
	private Button bPerm;

	public ResourcePageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public ResourcePageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.resource"; //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return Messages.AResourcePage_title;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));

		UIUtil.createLabel(composite, Messages.AResourcePage_parentfolder);
		tparent = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		tparent.setLayoutData(gd);
		// tparent.setEnabled(false);

		UIUtil.createLabel(composite, Messages.AResourcePage_type);
		ttype = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		ttype.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// ttype.setEnabled(false);

		bisRef = new Button(composite, SWT.CHECK);
		bisRef.setText(Messages.ResourcePageContent_isReference);
		bisRef.setEnabled(false);

		if (res.getWsClient().isSupported(Feature.PERMISSION)) {
			bPerm = new Button(composite, SWT.PUSH);
			bPerm.setText("Permissions");
			if (res.getValue().getIsNew())
				bPerm.setEnabled(false);
			else
				bPerm.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						PermissionWizard wizard = new PermissionWizard(res);
						PermissionDialog dialog = new PermissionDialog(UIUtils.getShell(), wizard);
						dialog.addApplyListener(wizard);
						dialog.open();
					}
				});
		} else {
			gd = new GridData();
			gd.horizontalSpan = 2;
			bisRef.setLayoutData(gd);
		}

		UIUtil.createLabel(composite, Messages.AResourcePage_creationdate);
		tcdate = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		tcdate.setLayoutData(gd);
		// tcdate.setEnabled(false);

		ResourceDescriptor rd = res.getValue();
		proxy = new Proxy(rd);
		if (res.isSupported(Feature.UPDATEDATE)) {
			UIUtil.createLabel(composite, Messages.ResourcePageContent_UpdateDate);
			tudate = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			tudate.setLayoutData(gd);
		}

		UIUtil.createSeparator(composite, 4);

		UIUtil.createLabel(composite, Messages.AResourcePage_name);
		tname = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.widthHint = 500;
		tname.setLayoutData(gd);

		UIUtil.createLabel(composite, Messages.AResourcePage_id);
		tid = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.widthHint = 500;
		tid.setLayoutData(gd);

		UIUtil.createLabel(composite, Messages.AResourcePage_description);
		tdesc = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 100;
		gd.widthHint = 500;
		gd.horizontalSpan = 3;
		tdesc.setLayoutData(gd);

		tid.setEditable(rd.getIsNew());
		if (rd.getIsNew()) {
			rd.setName(rd.getLabel());
			tname.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					tid.setText(IDStringValidator.safeChar(Misc.nvl(tname.getText())));
				}
			});
		}
		rebind();

		tname.setFocus();
		return composite;
	}

	@Override
	protected void rebind() {
		ResourceDescriptor rd = res.getValue();
		if (tudate != null)
			bindingContext.bindValue(SWTObservables.observeText(tudate, SWT.NONE), PojoObservables.observeValue(proxy, "updateDate")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(tparent, SWT.NONE), PojoObservables.observeValue(proxy, "parentFolder")); //$NON-NLS-1$
		IConnection c = res.getWsClient();
		final Format f = (c != null ? c.getTimestampFormat() : DateFormat.getTimeInstance());

		IConverter t2mConv = new Converter(String.class, Date.class) {

			public Object convert(Object fromObject) {
				try {
					if (fromObject != null && fromObject instanceof String && !((String) fromObject).isEmpty())
						return f.parseObject((String) fromObject);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		IConverter m2tConv = new Converter(Date.class, String.class) {

			public Object convert(Object fromObject) {
				if (fromObject == null)
					return "";
				return f.format(fromObject);
			}
		};
		bindingContext.bindValue(SWTObservables.observeText(tcdate, SWT.NONE), PojoObservables.observeValue(rd, "creationDate"), new UpdateValueStrategy().setConverter(t2mConv),
				new UpdateValueStrategy().setConverter(m2tConv));

		bindingContext.bindValue(SWTObservables.observeText(ttype, SWT.NONE), PojoObservables.observeValue(rd, "wsType")); //$NON-NLS-1$ 
		bindingContext.bindValue(SWTObservables.observeSelection(bisRef), PojoObservables.observeValue(rd, "isReference")); //$NON-NLS-1$ 
		bindingContext.bindValue(SWTObservables.observeText(tid, SWT.Modify), PojoObservables.observeValue(rd, "name"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new IDStringValidator()), null);

		bindingContext.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(rd, "label"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new EmptyStringValidator()), null);
		bindingContext.bindValue(SWTObservables.observeText(tdesc, SWT.Modify), PojoObservables.observeValue(rd, "description")); //$NON-NLS-1$
		bindingContext.updateTargets();
//		bindingContext.updateModels();
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.editResource"; //$NON-NLS-1$
	}

	@Override
	public boolean isPageComplete() {
		if (tid.getText().trim().isEmpty() || tname.getText().trim().isEmpty()) {
			return false;
		}
		return super.isPageComplete();
	}

	class Proxy {
		private ResourceDescriptor rd;

		public Proxy(ResourceDescriptor rd) {
			this.rd = rd;
		}

		public ResourceDescriptor getResourceDescriptor() {
			return rd;
		}

		public String getUpdateDate() {
			return DiffFields.getSoapValue(rd, DiffFields.UPDATEDATE);
		}

		public void setUpdateDate(String name) {
		}

		public String getParentFolder() {
			String p = rd.getParentFolder();
			if (Misc.isNullOrEmpty(p))
				p = "/";
			return p;
		}

		public void setParentFolder(String p) {
			rd.setParentFolder(p);
		}
	}
}
