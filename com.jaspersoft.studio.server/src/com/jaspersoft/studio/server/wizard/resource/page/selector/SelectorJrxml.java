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
package com.jaspersoft.studio.server.wizard.resource.page.selector;

import java.io.File;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.properties.dialog.RepositoryDialog;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.wizard.find.FindResourceJob;
import com.jaspersoft.studio.utils.Misc;

public class SelectorJrxml {
	private Button brRepo;
	private Text jsRefDS;
	private Button brLocal;
	private Text jsLocDS;
	private Button bLoc;
	private Button bRef;
	private MResource res;

	public void createControls(Composite cmp, final ANode parent, final MResource res) {
		this.res = res;

		Composite composite = new Composite(cmp, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		composite.setLayoutData(gd);
		composite.setLayout(new GridLayout(2, false));

		brRepo = new Button(composite, SWT.RADIO);
		brRepo.setText("Select from Repository");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		brRepo.setLayoutData(gd);
		brRepo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (brRepo.getSelection())
					setEnabled(0, false);
			}
		});

		jsRefDS = new Text(composite, SWT.BORDER);
		jsRefDS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jsRefDS.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireSelectionChanged();
			}
		});

		bRef = new Button(composite, SWT.PUSH);
		bRef.setText("Browse");
		bRef.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// N.B: remember we need to pass a fresh new MServerProfile info
				// in order
				// to avoid problem of refreshing (children/parent relationship
				// changes)
				// due to tree viewer node expansion...
				MServerProfile msp = ServerManager.getMServerProfileCopy((MServerProfile) parent.getRoot());
				if (res.isSupported(Feature.SEARCHREPOSITORY)) {
					ResourceDescriptor rd = FindResourceJob.doFindResource(msp, new String[] { ResourceMediaType.FILE_CLIENT_TYPE }, null);
					if (rd != null)
						setRemoteResource(res, rd, parent);
				} else {
					RepositoryDialog rd = new RepositoryDialog(UIUtils.getShell(), msp) {
						@Override
						public boolean isResourceCompatible(MResource r) {
							return r instanceof MJrxml;
						}
					};
					if (rd.open() == Dialog.OK) {
						MResource rs = rd.getResource();
						if (rs != null)
							setRemoteResource(res, rs.getValue(), parent);
					}
				}
			}
		});

		brLocal = new Button(composite, SWT.RADIO);
		brLocal.setText("Local Resource");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		brLocal.setLayoutData(gd);
		brLocal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (brLocal.getSelection())
					setEnabled(1, false);
			}
		});

		jsLocDS = new Text(composite, SWT.BORDER);
		jsLocDS.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jsLocDS.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fireSelectionChanged();
			}
		});

		bLoc = new Button(composite, SWT.PUSH);
		bLoc.setText("Browse");
		bLoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FilteredResourcesSelectionDialog wizard = new FilteredResourcesSelectionDialog(UIUtils.getShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
				wizard.setInitialPattern("*.jrxml");//$NON-NLS-1$
				if (wizard.open() == Dialog.OK) {
					ResourceDescriptor jrxmlDescriptor = new ResourceDescriptor();
					jrxmlDescriptor.setName("main_jrxml");
					jrxmlDescriptor.setLabel("Main Jrxml");
					jrxmlDescriptor.setWsType(ResourceDescriptor.TYPE_JRXML);
					jrxmlDescriptor.setIsNew(true);
					jrxmlDescriptor.setMainReport(true);
					jrxmlDescriptor.setIsReference(false);
					jrxmlDescriptor.setHasData(true);

					replaceMainReport(res, jrxmlDescriptor);
					fireSelectionChanged();

					resfile = new File(((IFile) wizard.getFirstResult()).getLocationURI());
					((AFileResource) res).setFile(resfile);

					jsLocDS.setText(resfile.getAbsolutePath());
				}
			}
		});
		ResourceDescriptor r = getMainReport(res.getValue());
		if (r != null) {
			if (r.getIsReference()) {
				rrepo = r;
				setEnabled(0, true);
			} else {
				rlocal = r;
				setEnabled(1, true);
			}
		} else
			setEnabled(1, true);
	}

	private void setRemoteResource(MResource res, ResourceDescriptor rd, ANode parent) {
		ResourceDescriptor runit = res.getValue();
		try {
			rd = WSClientHelper.getResource(new NullProgressMonitor(), parent, rd);
			rd.setIsReference(true);
			rd.setMainReport(true);
			rd.setReferenceUri(rd.getUriString());
			rd.setParentFolder(runit.getParentFolder() + "/" + runit.getName() + "_files");
			rd.setWsType(ResourceDescriptor.TYPE_JRXML);
			rd.setUriString(rd.getParentFolder() + "/" + rd.getName());
			replaceMainReport(res, rd);
			rd.setDirty(false);
			fireSelectionChanged();

			jsRefDS.setText(rd.getUriString());
		} catch (Exception e1) {
			UIUtils.showError(e1);
		}
	}

	private SelectionListener listener;

	public void addSelectionListener(SelectionListener listener) {
		this.listener = listener;
	}

	public static void replaceMainReport(final MResource res, ResourceDescriptor rd) {
		ResourceDescriptor rdel = getMainReport(res.getValue());
		if (rdel != null) {
			int index = res.getValue().getChildren().indexOf(rdel);
			if (index >= 0)
				res.getValue().getChildren().remove(index);
		}
		if (rd != null)
			res.getValue().getChildren().add(rd);
	}

	public static ResourceDescriptor getMainReport(ResourceDescriptor ru) {
		for (Object obj : ru.getChildren()) {
			ResourceDescriptor r = (ResourceDescriptor) obj;
			if (r.getWsType().equals(ResourceDescriptor.TYPE_JRXML) && r.isMainReport()) {
				return r;
			}
		}
		return null;
	}

	private ResourceDescriptor rlocal;
	private ResourceDescriptor rrepo;
	private File resfile;

	private void setEnabled(int pos, boolean init) {
		bRef.setEnabled(false);
		jsRefDS.setEnabled(false);

		bLoc.setEnabled(false);
		jsLocDS.setEnabled(false);

		brRepo.setSelection(false);
		brLocal.setSelection(false);

		jsRefDS.setText("");
		jsLocDS.setText("");

		ResourceDescriptor r = getMainReport(res.getValue());
		AFileResource aFileResource = (AFileResource) res;
		File file = aFileResource.getFile();
		switch (pos) {
		case 0:
			if (!init) {
				rlocal = r;
				resfile = file;
				replaceMainReport(res, rrepo);
				aFileResource.setFile(null);
			}
			bRef.setEnabled(true);
			brRepo.setSelection(true);
			jsRefDS.setEnabled(true);
			if (rrepo != null)
				jsRefDS.setText(Misc.nvl(rrepo.getReferenceUri()));
			break;
		case 1:
			if (!init) {
				rrepo = r;
				replaceMainReport(res, rlocal);
			}

			brLocal.setSelection(true);
			bLoc.setEnabled(true);
			jsLocDS.setEnabled(true);
			if (rlocal != null) {
				if (resfile != null) {
					jsLocDS.setText(resfile.getAbsolutePath());
					aFileResource.setFile(resfile);
				} else
					jsLocDS.setText(Misc.nvl(rlocal.getName()));
			}
			break;
		}

	}

	private void fireSelectionChanged() {
		if (listener != null)
			listener.widgetSelected(null);
	}

	/**
	 * Checks if a valid JRXML reference is currently selected.
	 * 
	 * @return <code>true</code> if a valid jrxml reference is selected,
	 *         <code>false</code> otherwise
	 */
	public boolean isJrxmlSelected() {
		return (!jsRefDS.getText().trim().isEmpty() || !jsLocDS.getText().trim().isEmpty());
	}

}
