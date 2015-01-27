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
package com.jaspersoft.studio.server.properties.dialog;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelUtil;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;
import com.jaspersoft.studio.server.ServerProvider;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.action.resource.RefreshResourcesAction;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;

public abstract class RepositoryDialog extends Dialog {

	public RepositoryDialog(Shell parentShell, INode root) {
		super(parentShell);
		this.root = root;
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.SHELL_TRIM);
	}

	private INode root;

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("JasperServer Repository Browser");
	}

	private MResource resource;
	private Text tname;
	private Text ttype;

	public MResource getResource() {
		return resource;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(2, false));

		final TreeViewer treeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		gd.minimumHeight = 300;
		gd.minimumWidth = 400;
		treeViewer.getTree().setLayoutData(gd);
		treeViewer.setContentProvider(new ReportTreeContetProvider());
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());

		ColumnViewerToolTipSupport.enableFor(treeViewer);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection ts = (TreeSelection) event.getSelection();
				Object obj = ts.getFirstElement();
				if (obj instanceof MResource) {
					MResource mres = (MResource) obj;
					boolean resCompatible = isResourceCompatible(mres);
					getButton(IDialogConstants.OK_ID).setEnabled(resCompatible);
					if (resCompatible)
						setResource((MResource) obj);
				}
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			private RefreshResourcesAction refreshAction;

			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection ts = (TreeSelection) treeViewer.getSelection();
				Object el = ts.getFirstElement();
				if (el instanceof MResource) {
					MResource mres = (MResource) el;
					boolean resCompatible = isResourceCompatible(mres);
					if (resCompatible) {
						okPressed();
						return;
					}
					if (mres instanceof MFolder) {
						if (treeViewer.getExpandedState(el))
							treeViewer.collapseToLevel(el, 1);
						else {
							if (refreshAction == null)
								refreshAction = new RefreshResourcesAction(treeViewer);
							if (refreshAction.isEnabled())
								refreshAction.run();
							treeViewer.expandToLevel(el, 1);
						}
					}
				}
			}
		});
		treeViewer.addTreeListener(new ITreeViewerListener() {

			private ServerProvider serverProvider;

			public void treeExpanded(TreeExpansionEvent event) {
				if (serverProvider == null)
					serverProvider = new ServerProvider();
				serverProvider.handleTreeEvent(event);
			}

			public void treeCollapsed(TreeExpansionEvent event) {

			}
		});

		new Label(composite, SWT.NONE).setText("Resource Name");

		tname = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(composite, SWT.NONE).setText("Resource Type");

		ttype = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		ttype.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Job job = new Job("Building report") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Looking into repository", IProgressMonitor.UNKNOWN);
				try {
					MServerProfile msp = null;
					if (root instanceof MServerProfile)
						msp = (MServerProfile) root;
					else if (root instanceof MResource)
						msp = (MServerProfile) ((MResource) root).getRoot();
					if (ModelUtil.isEmpty(msp))
						WSClientHelper.connectGetData(msp, monitor);
					UIUtils.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							treeViewer.setInput(root);
						}
					});
				} catch (Exception e) {
					UIUtils.showError(e);
					return Status.CANCEL_STATUS;
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.schedule();

		return composite;
	}

	public abstract boolean isResourceCompatible(MResource r);

	private void setResource(MResource res) {
		this.resource = res;
		tname.setText(res.getValue().getUriString());
		ttype.setText(res.getValue().getWsType());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
}
