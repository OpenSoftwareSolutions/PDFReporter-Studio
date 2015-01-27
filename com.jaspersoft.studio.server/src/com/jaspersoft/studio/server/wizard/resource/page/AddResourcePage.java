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

import java.text.MessageFormat;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MDataType;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MInputControl;
import com.jaspersoft.studio.server.model.MJar;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MListOfValues;
import com.jaspersoft.studio.server.model.MRAccessGrantSchema;
import com.jaspersoft.studio.server.model.MRDashboard;
import com.jaspersoft.studio.server.model.MRDataAdapter;
import com.jaspersoft.studio.server.model.MRFont;
import com.jaspersoft.studio.server.model.MRImage;
import com.jaspersoft.studio.server.model.MRQuery;
import com.jaspersoft.studio.server.model.MRStyleTemplate;
import com.jaspersoft.studio.server.model.MReference;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MReportUnitOptions;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.MResourceBundle;
import com.jaspersoft.studio.server.model.MXmlFile;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceAWS;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceBean;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceCustom;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceDiagnostic;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceJDBC;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceJNDI;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceVDS;
import com.jaspersoft.studio.server.model.datasource.MRMondrianSchema;
import com.jaspersoft.studio.server.model.datasource.MROlapMondrianConnection;
import com.jaspersoft.studio.server.model.datasource.MROlapUnit;
import com.jaspersoft.studio.server.model.datasource.MROlapXmlaConnection;
import com.jaspersoft.studio.server.model.datasource.MRSecureMondrianConnection;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.Version;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;
import com.jaspersoft.studio.utils.Callback;

public class AddResourcePage extends WizardPage {
	private MResource resource;
	private ANode parent;

	public AddResourcePage(ANode parent) {
		super("addresource"); //$NON-NLS-1$
		setTitle(Messages.AddResourcePage_Title);
		String title = ANode.getIconDescriptor().getTitle();
		if (parent instanceof MServerProfile)
			title = MServerProfile.getIconDescriptor().getTitle();
		else if (parent instanceof MResource)
			title = ((MResource) parent).getThisIconDescriptor().getTitle();
		setDescription(MessageFormat.format(Messages.AddResourcePage_Description, title, parent.getDisplayText()));
		this.parent = parent;
	}

	public MResource getResource() {
		return resource;
	}

	public void createControl(Composite parent) {
		final TreeViewer treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 300;
		gd.widthHint = 400;
		treeViewer.getTree().setLayoutData(gd);
		treeViewer.setContentProvider(new ReportTreeContetProvider());
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());
		treeViewer.setInput(getInput());
		ColumnViewerToolTipSupport.enableFor(treeViewer);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection ts = (TreeSelection) event.getSelection();
				Object obj = ts.getFirstElement();
				if (obj != null && obj instanceof MResource) {
					resource = (MResource) obj;
				} else
					resource = null;

				setPageComplete(canFlipToNextPage());
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (canFlipToNextPage())
					getContainer().showPage(getNextPage());
				else {
					TreeSelection s = (TreeSelection) treeViewer.getSelection();
					Object fe = s.getFirstElement();
					if (treeViewer.getExpandedState(fe))
						treeViewer.collapseToLevel(fe, 1);
					else
						treeViewer.expandToLevel(fe, 1);
				}
			}
		});
		setControl(treeViewer.getControl());
		treeViewer.setSelection(new TreeSelection(new TreePath(new Object[] { resource })), true);
	}

	private boolean dsonly = false;

	public void setOnlyDatasource(boolean dsonly) {
		this.dsonly = dsonly;
	}

	private boolean ruOnly = false;

	public void setOnlyReportUnit(boolean ruOnly) {
		this.ruOnly = ruOnly;
	}

	private boolean monOnly = false;

	public void setMondrianOnly(boolean monOnly) {
		this.monOnly = monOnly;
	}

	private boolean olapOnly = false;

	public void setOlapOnly(boolean olapOnly) {
		this.olapOnly = olapOnly;
	}

	private ANode getInput() {
		MServerProfile root = new MServerProfile(null, null);
		if (parent instanceof MResource)
			root.setWsClient(((MResource) parent).getWsClient());
		if (parent instanceof MServerProfile)
			root.setWsClient(((MServerProfile) parent).getWsClient((Callback<IConnection>) null));
		if (dsonly) {
			createDatasources(root);
		} else if (ruOnly) {
			createReportUnit(root);
		} else if (monOnly) {
			createMondrian(root);
		} else if (olapOnly) {
			createOlap(root);
		} else {
			if (parent instanceof MFolder || parent instanceof MServerProfile) {
				new MFolder(root, MFolder.createDescriptor(parent), -1);
				createReportUnit(root);

				createDatasources(root);

				new MDataType(root, MDataType.createDescriptor(parent), -1);
				new MRQuery(root, MRQuery.createDescriptor(parent), -1);
				if (!isSoap(root))
					new MRDashboard(root, MRDashboard.createDescriptor(parent), -1);

				MRoot oroot = new MRoot(root, null) {
					public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

					@Override
					public String getDisplayText() {
						return Messages.AddResourcePage_0;
					}

					@Override
					public String getToolTip() {
						return Messages.AddResourcePage_1;
					}

					@Override
					public ImageDescriptor getImagePath() {
						return MROlapMondrianConnection.getIconDescriptor().getIcon16();
					}

					public INode getRoot() {
						return parent.getRoot();
					}
				};

				new MRMondrianSchema(oroot, MRMondrianSchema.createDescriptor(parent), -1);
				// createOlap(oroot);
				if (!isSoap(root))
					new MROlapUnit(oroot, MROlapUnit.createDescriptor(parent), -1);
				new MRAccessGrantSchema(root, MRAccessGrantSchema.createDescriptor(parent), -1);
			}
			new MJrxml(root, MJrxml.createDescriptor(parent), -1);
			new MInputControl(root, MInputControl.createDescriptor(parent), -1);
			new MListOfValues(root, MListOfValues.createDescriptor(parent), -1);
			new MJar(root, MJar.createDescriptor(parent), -1);
			// new MResource(root, MResource.createDescriptor(parent), -1);
			new MResourceBundle(root, MResourceBundle.createDescriptor(parent), -1);
			new MRFont(root, MRFont.createDescriptor(parent), -1);
			new MRImage(root, MRImage.createDescriptor(parent), -1);
			new MRStyleTemplate(root, MRStyleTemplate.createDescriptor(parent), -1);
			new MXmlFile(root, MXmlFile.createDescriptor(parent), -1);
			new MRDataAdapter(root, MRDataAdapter.createDescriptor(parent), -1);

			if (parent instanceof MReportUnit) {
				new MReference(root, MReference.createDescriptor(parent), -1);
				ServerInfo si = getServerInfo(root);
				if (si != null && Version.isPro(si))
					new MReportUnitOptions(root, MReportUnitOptions.createDescriptor((MReportUnit) parent), -1);
				boolean dsexists = false;
				for (INode n : parent.getChildren()) {
					if (n instanceof MResource && SelectorDatasource.isDatasource(((MResource) n).getValue())) {
						dsexists = true;
						break;
					}
				}
				if (!dsexists)
					createDatasources(root);
			}

			Activator.getExtManager().createNewResource(root, parent);

			// new MUnknown(root, MUnknown.createDescriptor(parent), -1);
		}
		new ModelVisitor<ANode>(root) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MResource)
					((MResource) n).setEditMode(true);
				return true;
			}
		};
		setResource(root);
		return root;
	}

	private void setResource(INode rt) {
		if (rt.getChildren() != null && !rt.getChildren().isEmpty()) {
			INode iNode = rt.getChildren().get(0);
			if (iNode instanceof MResource)
				resource = (MResource) iNode;
			else
				setResource(iNode);
		}
	}

	protected void createReportUnit(ANode root) {
		new MReportUnit(root, MReportUnit.createDescriptor(parent), -1);
	}

	protected void createMondrian(ANode root) {
		ServerInfo si = getServerInfo(root);
		if (si != null && Version.isPro(si))
			new MRSecureMondrianConnection(root, MRSecureMondrianConnection.createDescriptor(parent), -1);
		else
			new MROlapMondrianConnection(root, MROlapMondrianConnection.createDescriptor(parent), -1);
	}

	private ServerInfo getServerInfo(ANode n) {
		try {
			if (n instanceof MServerProfile)
				return ((MServerProfile) n).getWsClient().getServerInfo(new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (n instanceof MResource)
			return getServerInfo((ANode) n.getRoot());
		return null;
	}

	private boolean isSoap(ANode n) {
		try {
			if (n instanceof MServerProfile)
				return !((MServerProfile) n).getWsClient().isSupported(Feature.SEARCHREPOSITORY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (n instanceof MResource)
			return isSoap((ANode) n.getRoot());
		return true;
	}

	protected void createOlap(ANode root) {
		createMondrian(root);
		new MROlapXmlaConnection(root, MROlapXmlaConnection.createDescriptor(parent), -1);
	}

	protected MRoot createDatasources(ANode root) {
		MRoot mroot = new MRoot(root, null) {
			public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public String getDisplayText() {
				return Messages.AddResourcePage_2;
			}

			@Override
			public String getToolTip() {
				return Messages.AddResourcePage_3;
			}

			@Override
			public ImageDescriptor getImagePath() {
				return MRDatasourceJDBC.getIconDescriptor().getIcon16();
			}

			public INode getRoot() {
				return parent.getRoot();
			}
		};
		new MRDatasourceBean(mroot, MRDatasourceBean.createDescriptor(parent), -1);
		new MRDatasourceJDBC(mroot, MRDatasourceJDBC.createDescriptor(parent), -1);
		new MRDatasourceJNDI(mroot, MRDatasourceJNDI.createDescriptor(parent), -1);
		new MRDatasourceCustom(mroot, MRDatasourceCustom.createDescriptor(parent), -1);
		new MRDatasourceVDS(mroot, MRDatasourceVDS.createDescriptor(parent), -1);
		new MRDatasourceDiagnostic(mroot, MRDatasourceDiagnostic.createDescriptor(parent), -1);

		new MRDatasourceAWS(mroot, MRDatasourceAWS.createDescriptor(parent), -1);
		createOlap(mroot);
		Activator.getExtManager().createNewDatasource(mroot, parent);
		return mroot;
	}

	@Override
	public boolean canFlipToNextPage() {
		return resource != null;
	}
}
