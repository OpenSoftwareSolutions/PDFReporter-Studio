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
package com.jaspersoft.studio.server.wizard.pages;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.ui.validator.EmptyStringValidator;
import net.sf.jasperreports.eclipse.ui.validator.NotEmptyIFolderValidator;
import net.sf.jasperreports.util.CastorUtil;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.compatibility.dialog.VersionCombo;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.preferences.CASListFieldEditor;
import com.jaspersoft.studio.server.preferences.CASPreferencePage;
import com.jaspersoft.studio.server.preferences.SSOServer;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.Version;
import com.jaspersoft.studio.server.secret.JRServerSecretsProvider;
import com.jaspersoft.studio.server.wizard.validator.URLValidator;
import com.jaspersoft.studio.swt.widgets.WLocale;
import com.jaspersoft.studio.swt.widgets.WSecretText;
import com.jaspersoft.studio.swt.widgets.WTimeZone;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.UIUtil;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.WizardEndingStateListener;

public class ServerProfilePage extends WizardPage implements WizardEndingStateListener {
	private MServerProfile sprofile;
	private WSecretText tpass;
	private Text tuser;
	private Text ttimeout;
	private Text lpath;
	private Button bchunked;
	private Combo bmime;
	private Button bdaterange;
	private Button bUseSoap;
	private Button bSyncDA;
	private Button blpath;
	private VersionCombo cversion;
	private DataBindingContext dbc;
	private Text txtInfo;
	private WLocale loc;
	private WTimeZone tz;
	private Button bSSO;
	private Combo ccas;

	public ServerProfilePage(MServerProfile sprofile) {
		super("serverprofilepage"); //$NON-NLS-1$
		setTitle(Messages.ServerProfilePage_1);
		setDescription(Messages.ServerProfilePage_2);
		this.sprofile = sprofile;
	}

	public void createControl(final Composite parent) {
		dbc = new DataBindingContext();
		WizardPageSupport.create(this, dbc);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		setControl(composite);

		new Label(composite, SWT.NONE).setText(Messages.ServerProfilePage_3);
		Text tname = new Text(composite, SWT.BORDER);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gd);

		new Label(composite, SWT.NONE).setText(Messages.ServerProfilePage_4);

		Text turl = new Text(composite, SWT.BORDER);
		turl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Group gr = new Group(composite, SWT.NONE);
		gr.setText(Messages.ServerProfilePage_8);
		gr.setLayout(new GridLayout(2, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gr.setLayoutData(gd);

		new Label(gr, SWT.NONE).setText(Messages.ServerProfilePage_9);
		Text torg = new Text(gr, SWT.BORDER);
		torg.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createCredentials(gr);

		final Section expcmp = new Section(composite, ExpandableComposite.TREE_NODE);
		expcmp.setTitleBarForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		UIUtil.setBold(expcmp);
		expcmp.setText(Messages.ServerProfilePage_advancedsettings);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		expcmp.setLayoutData(gd);
		expcmp.setExpanded(false);

		CTabFolder tabFolder = new CTabFolder(expcmp, SWT.BOTTOM);
		expcmp.setClient(tabFolder);

		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.ServerProfilePage_0);
		bptab.setControl(createAdvancedSettings(tabFolder));

		bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.ServerProfilePage_5);
		bptab.setControl(createInfo(tabFolder));

		tabFolder.setSelection(0);

		expcmp.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				UIUtils.relayoutDialog(getShell(), 0, -1);
			}
		});
		ServerProfile value = sprofile.getValue();
		Proxy proxy = new Proxy(value);
		dbc.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(value, "name"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new EmptyStringValidator() {
					@Override
					public IStatus validate(Object value) {
						IStatus s = super.validate(value);
						if (s.equals(Status.OK_STATUS) && !ServerManager.isUniqueName(sprofile, (String) value)) {
							return ValidationStatus.warning(Messages.ServerProfilePage_13);
						}
						return s;
					}
				}), null);
		dbc.bindValue(SWTObservables.observeText(turl, SWT.Modify), PojoObservables.observeValue(proxy, "url"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new URLValidator()), null);
		dbc.bindValue(SWTObservables.observeText(lpath, SWT.Modify), PojoObservables.observeValue(proxy, "projectPath"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new NotEmptyIFolderValidator()), null);
		dbc.bindValue(SWTObservables.observeText(torg, SWT.Modify), PojoObservables.observeValue(value, "organisation")); //$NON-NLS-1$
		dbc.bindValue(SWTObservables.observeText(tuser, SWT.Modify), PojoObservables.observeValue(value, "user"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new UsernameValidator()), null);
		dbc.bindValue(SWTObservables.observeText(tpass, SWT.Modify), PojoObservables.observeValue(value, "pass")); //$NON-NLS-1$

		dbc.bindValue(SWTObservables.observeText(ttimeout, SWT.Modify), PojoObservables.observeValue(value, "timeout")); //$NON-NLS-1$

		dbc.bindValue(SWTObservables.observeSelection(bchunked), PojoObservables.observeValue(value, "chunked")); //$NON-NLS-1$
		dbc.bindValue(SWTObservables.observeText(bmime), PojoObservables.observeValue(proxy, "mime")); //$NON-NLS-1$
		dbc.bindValue(SWTObservables.observeText(loc.getCombo()), PojoObservables.observeValue(value, "locale")); //$NON-NLS-1$
		dbc.bindValue(SWTObservables.observeText(tz.getCombo()), PojoObservables.observeValue(value, "timeZone")); //$NON-NLS-1$

		dbc.bindValue(SWTObservables.observeSelection(bdaterange), PojoObservables.observeValue(value, "supportsDateRanges")); //$NON-NLS-1$
		dbc.bindValue(SWTObservables.observeSelection(bUseSoap), PojoObservables.observeValue(value, "useOnlySOAP")); //$NON-NLS-1$
		dbc.bindValue(SWTObservables.observeSelection(bSyncDA), PojoObservables.observeValue(value, "syncDA")); //$NON-NLS-1$
		dbc.bindValue(SWTObservables.observeSelection(bSSO), PojoObservables.observeValue(value, "useSSO")); //$NON-NLS-1$

		dbc.bindValue(SWTObservables.observeText(cversion.getControl()), PojoObservables.observeValue(proxy, "jrVersion")); //$NON-NLS-1$

		tpass.loadSecret(JRServerSecretsProvider.SECRET_NODE_ID, Misc.nvl(sprofile.getValue().getPass()));

		showServerInfo();
	}

	protected void createCredentials(Group gr) {
		cmpCredential = new Composite(gr, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		cmpCredential.setLayoutData(gd);
		stackLayout = new StackLayout();
		stackLayout.marginWidth = 0;
		cmpCredential.setLayout(stackLayout);

		cmpUP = new Composite(cmpCredential, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		cmpUP.setLayout(layout);

		new Label(cmpUP, SWT.NONE).setText(Messages.ServerProfilePage_10);
		tuser = new Text(cmpUP, SWT.BORDER);
		tuser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tuser.setTextLimit(100);

		new Label(cmpUP, SWT.NONE).setText(Messages.ServerProfilePage_11);
		tpass = new WSecretText(cmpUP, SWT.BORDER | SWT.PASSWORD);
		tpass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		cmpCAS = new Composite(cmpCredential, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		cmpCAS.setLayout(layout);

		new Label(cmpCAS, SWT.NONE).setText("SSO Server");
		ccas = new Combo(cmpCAS, SWT.READ_ONLY | SWT.SINGLE | SWT.BORDER);

		String v = null;
		v = JasperReportsConfiguration.getDefaultInstance().getPrefStore().getString(CASPreferencePage.CAS);
		for (String line : v.split("\n")) {
			if (line.isEmpty())
				continue;
			try {
				SSOServer srv = (SSOServer) CastorUtil.read(new ByteArrayInputStream(Base64.decodeBase64(line)), CASListFieldEditor.mapping);
				ssoservers.add(srv);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		final ServerProfile value = sprofile.getValue();
		String[] items = new String[ssoservers.size()];
		int sel = 0;
		for (int i = 0; i < ssoservers.size(); i++) {
			SSOServer srv = ssoservers.get(i);
			items[i] = srv.getUrl();
			if (srv.getUuid().equals(value.getSsoUuid()))
				sel = i;
		}
		ccas.setItems(items);
		ccas.select(sel);
		if (sel >= 0 && sel < ssoservers.size())
			value.setSsoUuid(ssoservers.get(sel).getUuid());
		ccas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int ind = ccas.getSelectionIndex();
				if (ind >= 0 && ind < ssoservers.size())
					value.setSsoUuid(ssoservers.get(ind).getUuid());
			}
		});

		if (value.isUseSSO())
			stackLayout.topControl = cmpCAS;
		else
			stackLayout.topControl = cmpUP;
	}

	private List<SSOServer> ssoservers = new ArrayList<SSOServer>();
	private Composite cmpUP;
	private Composite cmpCAS;
	private StackLayout stackLayout;
	private Composite cmpCredential;

	private Composite createAdvancedSettings(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(3, false));

		new Label(cmp, SWT.NONE).setText(Messages.ServerProfilePage_jrversion);

		cversion = new VersionCombo(cmp);
		cversion.setVersion(JRXmlWriterHelper.LAST_VERSION);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		cversion.getControl().setLayoutData(gd);

		new Label(cmp, SWT.NONE).setText(Messages.ServerProfilePage_connectiontimeout);

		ttimeout = new Text(cmp, SWT.BORDER);
		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.widthHint = 100;
		ttimeout.setLayoutData(gd);

		bchunked = new Button(cmp, SWT.CHECK);
		bchunked.setText(Messages.ServerProfilePage_chunkedrequest);

		bdaterange = new Button(cmp, SWT.CHECK);
		bdaterange.setText(Messages.ServerProfilePage_daterangeexpression);
		gd = new GridData();
		gd.horizontalSpan = 2;
		bdaterange.setLayoutData(gd);

		bUseSoap = new Button(cmp, SWT.CHECK);
		bUseSoap.setText(Messages.ServerProfilePage_6);
		// gd = new GridData();
		// gd.horizontalSpan = 3;
		// bUseSoap.setLayoutData(gd);

		bSyncDA = new Button(cmp, SWT.CHECK);
		bSyncDA.setText(Messages.ServerProfilePage_14);
		bSyncDA.setToolTipText(Messages.ServerProfilePage_15);
		gd = new GridData();
		gd.horizontalSpan = 2;
		bSyncDA.setLayoutData(gd);

		bSSO = new Button(cmp, SWT.CHECK);
		bSSO.setText(Messages.ServerProfilePage_18);
		bSSO.setToolTipText(Messages.ServerProfilePage_20);
		gd = new GridData();
		gd.horizontalSpan = 3;
		bSSO.setLayoutData(gd);
		bSSO.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (bSSO.getSelection())
					stackLayout.topControl = cmpCAS;
				else
					stackLayout.topControl = cmpUP;
				cmpCredential.layout();
			}
		});

		String ttip = Messages.ServerProfilePage_7;
		Label lbl = new Label(cmp, SWT.NONE);
		lbl.setText(Messages.ServerProfilePage_12);
		lbl.setToolTipText(ttip);

		bmime = new Combo(cmp, SWT.READ_ONLY);
		bmime.setItems(new String[] { "MIME", "DIME" }); //$NON-NLS-1$ //$NON-NLS-2$
		bmime.setToolTipText(ttip);
		gd = new GridData();
		gd.horizontalSpan = 2;
		bmime.setLayoutData(gd);

		ttip = Messages.ServerProfilePage_16;

		lbl = new Label(cmp, SWT.NONE);
		lbl.setText(Messages.ServerProfilePage_17);
		lbl.setToolTipText(ttip);

		lpath = new Text(cmp, SWT.BORDER);
		lpath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lpath.setToolTipText(ttip);

		blpath = new Button(cmp, SWT.PUSH);
		blpath.setText("..."); //$NON-NLS-1$
		blpath.setToolTipText(ttip);

		blpath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ContainerSelectionDialog csd = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), true, Messages.ServerProfilePage_19);
				if (csd.open() == Dialog.OK) {
					Object[] selection = csd.getResult();
					if (selection != null && selection.length > 0 && selection[0] instanceof Path) {
						sprofile.setProjectPath(((Path) selection[0]).toPortableString());
						dbc.updateTargets();
					}
				}
			}
		});

		lbl = new Label(cmp, SWT.NONE);
		lbl.setText(Messages.ServerProfilePage_21);

		loc = new WLocale(cmp, SWT.BORDER);
		gd = new GridData();
		gd.horizontalSpan = 2;
		loc.setLayoutData(gd);

		lbl = new Label(cmp, SWT.NONE);
		lbl.setText(Messages.ServerProfilePage_22);

		tz = new WTimeZone(cmp, SWT.BORDER);
		gd = new GridData();
		gd.horizontalSpan = 2;
		tz.setLayoutData(gd);

		return cmp;
	}

	private Composite createInfo(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout());

		txtInfo = new Text(cmp, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txtInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtInfo.setBackground(cmp.getBackground());

		return cmp;
	}

	public void showServerInfo() {
		try {
			txtInfo.setText(sprofile.getConnectionInfo());
			// dbc.updateTargets();
			IConnection c = sprofile.getWsClient();
			if (c != null) {
				ServerInfo si = c.getServerInfo(null);
				// cversion.getControl().setEnabled(Version.isEstimated(si));
				bdaterange.setEnabled(!Version.isDateRangeSupported(si));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Proxy {
		private ServerProfile sp;

		public Proxy(ServerProfile sp) {
			this.sp = sp;
		}

		public void setUrl(String url) {
			sp.setUrl(Misc.nvl(url).trim());
		}

		public String getUrl() {
			return sp.getUrl();
		}

		public void setJrVersion(String v) {
			sp.setJrVersion(VersionCombo.getJrVersion(v));
		}

		public String getJrVersion() {
			return VersionCombo.getLabelVersion(sp.getJrVersion());
		}

		public void setProjectPath(String projectPath) {
			sprofile.setProjectPath(projectPath);
		}

		public String getProjectPath() {
			return sp.getProjectPath();
		}

		public void setMime(String v) {
			sp.setMime(v.equals("MIME")); //$NON-NLS-1$
		}

		public String getMime() {
			return sp.isMime() ? "MIME" : "DIME"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void performHelp() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelp("com.jaspersoft.studio.doc.jaspersoftserver"); //$NON-NLS-1$
	}

	@Override
	public void performFinishInvoked() {
		if (JaspersoftStudioPlugin.shouldUseSecureStorage()) {
			tpass.persistSecret();
			sprofile.getValue().setPass(tpass.getUUIDKey());
		}
	}

	@Override
	public void performCancelInvoked() {

	}

}
