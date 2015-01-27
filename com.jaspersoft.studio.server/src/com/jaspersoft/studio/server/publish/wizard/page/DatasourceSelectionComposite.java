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
package com.jaspersoft.studio.server.publish.wizard.page;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MRDatasource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.properties.dialog.RepositoryDialog;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.restv2.WsTypes;
import com.jaspersoft.studio.server.wizard.find.FindResourceJob;
import com.jaspersoft.studio.server.wizard.resource.AddResourceWizard;
import com.jaspersoft.studio.server.wizard.resource.ResourceWizard;
import com.jaspersoft.studio.server.wizard.resource.page.selector.ASelector;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource.SelectionType;
import com.jaspersoft.studio.utils.Misc;

/**
 * This widget is used to select the data source that will be associated to a
 * report unit when created in the remote JasperServer repository.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class DatasourceSelectionComposite extends Composite {
	private boolean mandatory = false;
	private MResource res;
	private ANode parent;

	// Widgets stuff
	private Text textLocalDS;
	private Text textDSFromRepo;
	private Button btnSelectDSFromRepo;
	private Button btnSelectLocalDS;
	private Button rbDSFromRepo;
	private Button rbLocalDS;
	private Button rbNoDS;
	private List<DatasourceSelectionListener> dsListeners;
	private boolean isConfiguringPage;
	private String[] excludeTypes;

	public void setExcludeTypes(String[] excludeTypes) {
		this.excludeTypes = excludeTypes;
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DatasourceSelectionComposite(Composite parent, int style, boolean mandatory, String[] excludeTypes) {
		super(parent, style);
		this.mandatory = mandatory;
		this.excludeTypes = excludeTypes;
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		rbDSFromRepo = new Button(this, SWT.RADIO);
		rbDSFromRepo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setEnabled(SelectorDatasource.SelectionType.REMOTE_DATASOURCE);
			}
		});
		rbDSFromRepo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		rbDSFromRepo.setText(Messages.DatasourceSelectionComposite_FromRepository);

		textDSFromRepo = new Text(this, SWT.BORDER);
		textDSFromRepo.setEnabled(false);
		textDSFromRepo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		btnSelectDSFromRepo = new Button(this, SWT.PUSH);
		btnSelectDSFromRepo.setText("...");
		btnSelectDSFromRepo.setEnabled(false);
		btnSelectDSFromRepo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectDatasourceFromRepository();
			}
		});

		rbLocalDS = new Button(this, SWT.RADIO);
		rbLocalDS.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setEnabled(SelectorDatasource.SelectionType.LOCAL_DATASOURCE);
			}
		});
		rbLocalDS.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		rbLocalDS.setText(Messages.DatasourceSelectionComposite_LocalDatasource);

		textLocalDS = new Text(this, SWT.BORDER);
		textLocalDS.setEnabled(false);
		textLocalDS.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		btnSelectLocalDS = new Button(this, SWT.PUSH);
		btnSelectLocalDS.setEnabled(false);
		btnSelectLocalDS.setText("...");
		btnSelectLocalDS.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectLocalDatasource();
			}
		});
		if (!mandatory) {
			rbNoDS = new Button(this, SWT.RADIO);
			rbNoDS.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			rbNoDS.setSelection(true);
			rbNoDS.setText(Messages.DatasourceSelectionComposite_NoDatasource);
			rbNoDS.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					removeDatasource(res);
					setEnabled(SelectorDatasource.SelectionType.NO_DATASOURCE);
				}
			});
		}
	}

	/**
	 * Configures the information needed to correctly use the datasource selection
	 * widget.
	 * 
	 * @param parent
	 *          the parent anode from which retrieve a {@link MServerProfile}
	 * @param resource
	 *          the resource for which we are configuring the datasource
	 */
	public void configurePage(ANode parent, MResource resource) {
		isConfiguringPage = true;
		this.parent = parent;
		this.res = resource;

		ResourceDescriptor r = SelectorDatasource.getDatasource(res.getValue());
		if (r != null) {
			if (r.getIsReference())
				setEnabled(SelectorDatasource.SelectionType.REMOTE_DATASOURCE);
			else
				setEnabled(SelectorDatasource.SelectionType.LOCAL_DATASOURCE);
		} else
			setEnabled(SelectorDatasource.SelectionType.NO_DATASOURCE);
		isConfiguringPage = false;
	}

	/*
	 * Enables (and resets) the internal status of the widget depending on the
	 * type of datasource we are creating/modifying.
	 */
	private void setEnabled(SelectorDatasource.SelectionType type) {
		// Disable and reset all the widgets
		btnSelectDSFromRepo.setEnabled(false);
		textDSFromRepo.setEnabled(false);
		textDSFromRepo.setText(""); //$NON-NLS-1$

		btnSelectLocalDS.setEnabled(false);
		textLocalDS.setEnabled(false);
		textLocalDS.setText(""); //$NON-NLS-1$

		rbDSFromRepo.setSelection(false);
		rbLocalDS.setSelection(false);
		if (rbNoDS != null)
			rbNoDS.setSelection(false);

		// Enable and check all the resource related information
		ResourceDescriptor r = SelectorDatasource.getDatasource(res.getValue());
		switch (type) {
		case REMOTE_DATASOURCE:
			rbDSFromRepo.setSelection(true);
			btnSelectDSFromRepo.setEnabled(true);
			// textDSFromRepo.setEnabled(true);
			if (isConfiguringPage && r != null)
				textDSFromRepo.setText(Misc.nvl(r.getReferenceUri()));
			break;
		case LOCAL_DATASOURCE:
			rbLocalDS.setSelection(true);
			btnSelectLocalDS.setEnabled(true);
			// textLocalDS.setEnabled(true);
			if (isConfiguringPage && r != null)
				textLocalDS.setText(Misc.nvl(r.getName()));
			break;
		case NO_DATASOURCE:
			if (rbNoDS != null)
				rbNoDS.setSelection(true);
			else {
				setEnabled(SelectionType.REMOTE_DATASOURCE);
				return;
			}
			break;
		}
		notifyDatasourceSelectionChanged();
	}

	/*
	 * Performs the selection of a local datasource. Shows a dialog where the user
	 * can choose the right one.
	 */
	private void selectLocalDatasource() {
		ResourceDescriptor runit = res.getValue();
		ResourceDescriptor ref = SelectorDatasource.getDatasource(runit);

		if (ASelector.isReference(ref))
			ref = null;
		if (ref == null && res.getValue().getWsType().equals(WsTypes.INST().toRestType(ResourceDescriptor.TYPE_DOMAIN_TOPICS))) {
			ref = MRDatasource.createDescriptor(null);
			ref.setName("SemanticLayerDataSource");
			ref.setLabel("SemanticLayerDataSource");
			ref.setWsType(ResourceDescriptor.TYPE_DATASOURCE_DOMAIN1);
		}
		if (ref == null) {
			AddResourceWizard wizard = new AddResourceWizard(res, true);
			wizard.setOnlyDatasource(true);
			WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
			dialog.create();
			if (dialog.open() != Dialog.OK)
				return;
			ref = wizard.getResource().getValue();

			ref.setIsReference(false);
			ref.setParentFolder(runit.getParentFolder() + "/" + runit.getName() + "_files"); //$NON-NLS-1$
			// ref.setWsType(ResourceDescriptor.TYPE_DATASOURCE);
			ref.setUriString(ref.getParentFolder() + "/" + ref.getName());//$NON-NLS-1$

			SelectorDatasource.replaceDatasource(res, ref);
		} else {
			MResource r = ResourceFactory.getResource(null, ASelector.cloneResource(ref), -1);
			ResourceWizard wizard = new ResourceWizard(parent, r, true, true);
			WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
			dialog.create();
			if (dialog.open() != Dialog.OK)
				return;
			ASelector.copyFields(r.getValue(), ref);
		}
		textLocalDS.setText(Misc.nvl(ref.getUriString()));
		notifyDatasourceSelectionChanged();
	}

	/*
	 * Performs the selection of a datasource from a remote repository. Shows a
	 * dialog where the user can choose the right one.
	 */
	private void selectDatasourceFromRepository() {
		// N.B: remember we need to pass a fresh new MServerProfile info in
		// order
		// to avoid problem of refreshing (children/parent relationship changes)
		// due to tree viewer node expansion...
		MServerProfile msp = ServerManager.getMServerProfileCopy((MServerProfile) parent.getRoot());
		if (msp.isSupported(Feature.SEARCHREPOSITORY)) {
			String[] dsArray = WsTypes.INST().getDatasourcesArray();
			if (res.getValue().getWsType().equals(ResourceDescriptor.TYPE_DOMAIN_TOPICS))
				dsArray = new String[] { WsTypes.INST().toRestType(ResourceDescriptor.TYPE_DATASOURCE_DOMAIN) };
			String[] exclude = null;
			if (excludeTypes != null && excludeTypes.length > 0) {
				exclude = new String[excludeTypes.length];
				for (int i = 0; i < excludeTypes.length; i++)
					exclude[i] = WsTypes.INST().toRestType(excludeTypes[i]);
			}

			ResourceDescriptor rd = FindResourceJob.doFindResource(msp, dsArray, exclude);
			if (rd != null)
				setResource(res, rd);
		} else {
			RepositoryDialog rd = new RepositoryDialog(UIUtils.getShell(), msp) {
				@Override
				public boolean isResourceCompatible(MResource r) {
					String type = r.getValue().getWsType();
					if (excludeTypes != null)
						for (String t : excludeTypes)
							if (type.equals(t))
								return false;
					return SelectorDatasource.isDatasource(r.getValue());
				}
			};
			if (rd.open() == Dialog.OK) {
				MResource rs = rd.getResource();
				if (rs != null)
					setResource(res, rs.getValue());
			}
		}
		notifyDatasourceSelectionChanged();
	}

	private void setResource(MResource res, ResourceDescriptor rd) {
		ResourceDescriptor runit = res.getValue();
		try {
			rd = WSClientHelper.getResource(new NullProgressMonitor(), parent, rd);
			rd.setIsReference(true);
			rd.setReferenceUri(rd.getUriString());
			rd.setParentFolder(runit.getParentFolder() + "/" + runit.getName() + "_files"); //$NON-NLS-1$ //$NON-NLS-2$
			rd.setWsType(rd.getWsType());
			rd.setUriString(rd.getParentFolder() + "/" + rd.getName());//$NON-NLS-1$
			SelectorDatasource.replaceDatasource(res, rd);
			rd.setDirty(false);

			textDSFromRepo.setText(rd.getReferenceUri());
		} catch (Exception e1) {
			UIUtils.showError(e1);
		}
	}

	/*
	 * Remove a previous existing datasource from the MResource instance
	 * specified.
	 */
	private void removeDatasource(final MResource res) {
		ResourceDescriptor rdel = SelectorDatasource.getDatasource(res.getValue());
		if (rdel != null)
			res.getValue().getChildren().remove(rdel);
	}

	/**
	 * @return <code>true</code> if a valid alternative for dataset information is
	 *         selected, <code>false</code> otherwise
	 */
	public boolean isDatasourceSelectionValid() {
		return (rbNoDS != null && rbNoDS.getSelection()) || !textDSFromRepo.getText().trim().isEmpty() || !textLocalDS.getText().trim().isEmpty();
	}

	public void addDatasourceSelectionListener(DatasourceSelectionListener l) {
		if (dsListeners == null) {
			dsListeners = new ArrayList<DatasourceSelectionListener>(1);
		}
		dsListeners.add(l);
	}

	public void removeDatasourceSelectionListener(DatasourceSelectionListener l) {
		if (dsListeners != null) {
			dsListeners.remove(l);
		}
	}

	private void notifyDatasourceSelectionChanged() {
		if (dsListeners != null) {
			for (DatasourceSelectionListener l : dsListeners) {
				l.datasourceSelectionChanged();
			}
		}
	}
}
