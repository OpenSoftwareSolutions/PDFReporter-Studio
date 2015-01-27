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
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.ui.validator.EmptyStringValidator;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.properties.dialog.RepositoryDialog;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.restv2.WsTypes;
import com.jaspersoft.studio.server.wizard.find.FindResourceJob;
import com.jaspersoft.studio.server.wizard.resource.APageContent;

public class ReferencePageContent extends APageContent {

	public ReferencePageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public ReferencePageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.reference";
	}

	@Override
	public String getName() {
		return Messages.RDReferencePage_textreference;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 0;
		composite.setLayout(layout);

		com.jaspersoft.studio.utils.UIUtil.createLabel(composite, Messages.RDReferencePage_referencedesc + " ");

		trefuri = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		trefuri.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		trefuri.setEnabled(false);

		Button bbrowse = new Button(composite, SWT.ARROW | SWT.DOWN);
		bbrowse.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				MServerProfile msp = ServerManager.getMServerProfileCopy((MServerProfile) pnode.getRoot());
				ResourceDescriptor resrd = res.getValue();
				if (res.isSupported(Feature.SEARCHREPOSITORY)) {
					List<String> restTypes = WsTypes.INST().getRestTypes();
					ResourceDescriptor rd = FindResourceJob.doFindResource(msp, restTypes.toArray(new String[restTypes.size()]), new String[] { ResourceMediaType.FOLDER_CLIENT_TYPE,
							ResourceMediaType.REPORT_UNIT_CLIENT_TYPE, ResourceMediaType.DOMAIN_TOPIC_TYPE });
					if (rd != null) {
						resrd.setReferenceUri(rd.getUriString());
						resrd.setIsReference(true);
						resrd.setUriString(rd.getUriString());
						resrd.setWsType(rd.getWsType());
						ref = rd;
						rebind();
						bindingContext.updateTargets();
					}
				} else {
					RepositoryDialog rd = new RepositoryDialog(UIUtils.getShell(), msp) {

						@Override
						public boolean isResourceCompatible(MResource r) {
							return !(r instanceof MFolder || r instanceof MReportUnit);
							// ResourceFactory.isFileResourceType(r.getValue());
						}

					};
					if (rd.open() == Dialog.OK) {
						MResource rs = rd.getResource();
						if (rs != null) {
							resrd.setReferenceUri(rs.getValue().getUriString());
							loadReference(resrd);
							bindingContext.updateTargets();
						}
					}
				}
			}

		});

		Composite cmp = new Composite(composite, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		cmp.setLayoutData(gd);

		com.jaspersoft.studio.utils.UIUtil.createLabel(cmp, Messages.RDReferencePage_parentfolder);
		tparent = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		tparent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tparent.setEnabled(false);

		com.jaspersoft.studio.utils.UIUtil.createLabel(cmp, Messages.RDReferencePage_type);
		ttype = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		ttype.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ttype.setEnabled(false);

		com.jaspersoft.studio.utils.UIUtil.createLabel(cmp, Messages.RDReferencePage_creationdate);
		tcdate = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		tcdate.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tcdate.setEnabled(false);

		com.jaspersoft.studio.utils.UIUtil.createSeparator(cmp, 2);

		com.jaspersoft.studio.utils.UIUtil.createLabel(cmp, Messages.RDReferencePage_refid);
		tid = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		tid.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tid.setEnabled(false);

		com.jaspersoft.studio.utils.UIUtil.createLabel(cmp, Messages.RDReferencePage_refname);
		tname = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tname.setEnabled(false);

		com.jaspersoft.studio.utils.UIUtil.createLabel(cmp, Messages.RDReferencePage_refdesc);
		tdesc = new Text(cmp, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 100;
		tdesc.setLayoutData(gd);
		tdesc.setEnabled(false);

		loadReference(res.getValue());
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {
		Object[] bds = bindingContext.getBindings().toArray();
		for (Object obj : bds) {
			Binding b = (Binding) obj;
			bindingContext.removeBinding(b);
			b.dispose();
		}
		bindingContext.bindValue(SWTObservables.observeText(trefuri, SWT.NONE), PojoObservables.observeValue(res.getValue(), "referenceUri")); //$NON-NLS-1$
		if (ref != null) {
			bindingContext.bindValue(SWTObservables.observeText(tparent, SWT.NONE), PojoObservables.observeValue(ref, "parentFolder")); //$NON-NLS-1$

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
					return f.format(fromObject);
				}
			};

			bindingContext.bindValue(SWTObservables.observeText(tcdate, SWT.NONE), PojoObservables.observeValue(ref, "creationDate"), new UpdateValueStrategy().setConverter(t2mConv),
					new UpdateValueStrategy().setConverter(m2tConv));

			bindingContext.bindValue(SWTObservables.observeText(ttype, SWT.NONE), PojoObservables.observeValue(ref, "wsType")); //$NON-NLS-1$

			bindingContext.bindValue(SWTObservables.observeText(tid, SWT.Modify), PojoObservables.observeValue(ref, "name"), //$NON-NLS-1$
					new UpdateValueStrategy().setAfterConvertValidator(new EmptyStringValidator()), null);

			bindingContext.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(ref, "label"), //$NON-NLS-1$
					new UpdateValueStrategy().setAfterConvertValidator(new EmptyStringValidator()), null);
			bindingContext.bindValue(SWTObservables.observeText(tdesc, SWT.Modify), PojoObservables.observeValue(ref, "description")); //$NON-NLS-1$
		}
		bindingContext.updateTargets();
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete() && ref != null;
	}

	private ResourceDescriptor ref;
	private Text trefuri;
	private Text tparent;
	private Text ttype;
	private Text tcdate;
	private Text tid;
	private Text tname;
	private Text tdesc;

	private void loadReference(ResourceDescriptor resrd) {
		try {
			ref = WSClientHelper.getReference(new NullProgressMonitor(), pnode, resrd);
			if (ref != null) {
				rebind();
				bindingContext.updateTargets();
				if (ResourceFactory.isFileResourceType(ref))
					res.getValue().setWsType(ResourceDescriptor.TYPE_REFERENCE);
				else
					res.getValue().setWsType(ref.getWsType());
				setPageComplete(isPageComplete());
			}

		} catch (Exception e) {
			UIUtils.showError(e);
		}
	}
}
