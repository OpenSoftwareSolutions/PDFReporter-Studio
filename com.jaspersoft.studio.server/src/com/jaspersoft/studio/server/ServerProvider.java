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
package com.jaspersoft.studio.server;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.repository.IRepositoryViewProvider;
import com.jaspersoft.studio.repository.actions.Separator;
import com.jaspersoft.studio.server.action.resource.AddResourceAction;
import com.jaspersoft.studio.server.action.resource.CopyResourceAction;
import com.jaspersoft.studio.server.action.resource.CutResourceAction;
import com.jaspersoft.studio.server.action.resource.DeleteResourceAction;
import com.jaspersoft.studio.server.action.resource.DownloadFileAction;
import com.jaspersoft.studio.server.action.resource.ExportMetadataAction;
import com.jaspersoft.studio.server.action.resource.FindResourceAction;
import com.jaspersoft.studio.server.action.resource.ImportDataSourceInJSSAction;
import com.jaspersoft.studio.server.action.resource.ImportMetadataAction;
import com.jaspersoft.studio.server.action.resource.OpenInEditorAction;
import com.jaspersoft.studio.server.action.resource.PasteResourceAction;
import com.jaspersoft.studio.server.action.resource.PasteResourceAsLinkAction;
import com.jaspersoft.studio.server.action.resource.PropertiesAction;
import com.jaspersoft.studio.server.action.resource.RefreshResourcesAction;
import com.jaspersoft.studio.server.action.resource.RunReportUnitAction;
import com.jaspersoft.studio.server.action.resource.ShowPermissionsAction;
import com.jaspersoft.studio.server.action.server.CreateServerAction;
import com.jaspersoft.studio.server.action.server.DeleteServerAction;
import com.jaspersoft.studio.server.action.server.DuplicateServerAction;
import com.jaspersoft.studio.server.action.server.EditServerAction;
import com.jaspersoft.studio.server.dnd.InputControlDragSourceListener;
import com.jaspersoft.studio.server.dnd.InputControlDropTargetListener;
import com.jaspersoft.studio.server.dnd.RepositoryFileResourceDropTargetListener;
import com.jaspersoft.studio.server.dnd.RepositoryImageDragSourceListener;
import com.jaspersoft.studio.server.dnd.UnitDragSourceListener;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MInputControl;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.MServers;
import com.jaspersoft.studio.server.protocol.Feature;

public class ServerProvider implements IRepositoryViewProvider {
	private CreateServerAction createServerAction;
	private FindResourceAction findResourceAction;
	private EditServerAction editServerAction;
	private DeleteServerAction deleteServerAction;
	private DuplicateServerAction duplicateServerAction;
	private DeleteResourceAction deleteAction;
	private RefreshResourcesAction refreshAction;

	private PropertiesAction editAction;

	private CutResourceAction cutAction;
	private CopyResourceAction copyAction;
	private PasteResourceAction pasteAction;
	private PasteResourceAsLinkAction pasteLinkAction;

	private AddResourceAction addAction;

	private RunReportUnitAction runReportUnitAction;
	private OpenInEditorAction openInEditorAction;
	private DownloadFileAction downloadFileAction;

	private ImportMetadataAction importMetadata;
	private ExportMetadataAction exportMetadata;

	private ImportDataSourceInJSSAction importDataSourceInJSSAction;
	private ShowPermissionsAction showPermissionsAction;

	public Action[] getActions(TreeViewer treeViewer) {
		createActions(treeViewer);
		return new Action[] { findResourceAction, createServerAction };
	}

	private void createActions(TreeViewer treeViewer) {
		if (createServerAction == null)
			createServerAction = new CreateServerAction(treeViewer);
		if (findResourceAction == null)
			findResourceAction = new FindResourceAction(treeViewer, this);
		if (editServerAction == null)
			editServerAction = new EditServerAction(treeViewer);
		if (deleteServerAction == null)
			deleteServerAction = new DeleteServerAction(treeViewer);
		if (duplicateServerAction == null)
			duplicateServerAction = new DuplicateServerAction(treeViewer);
		if (deleteAction == null)
			deleteAction = new DeleteResourceAction(treeViewer);
		if (refreshAction == null)
			refreshAction = new RefreshResourcesAction(treeViewer);

		if (cutAction == null)
			cutAction = new CutResourceAction(treeViewer);
		if (copyAction == null)
			copyAction = new CopyResourceAction(treeViewer);
		if (pasteAction == null)
			pasteAction = new PasteResourceAction(treeViewer);
		if (pasteLinkAction == null)
			pasteLinkAction = new PasteResourceAsLinkAction(treeViewer);

		if (addAction == null)
			addAction = new AddResourceAction(treeViewer);

		if (runReportUnitAction == null)
			runReportUnitAction = new RunReportUnitAction(treeViewer);
		if (editAction == null)
			editAction = new PropertiesAction(treeViewer);
		if (importDataSourceInJSSAction == null) {
			importDataSourceInJSSAction = new ImportDataSourceInJSSAction(treeViewer);
		}

		if (openInEditorAction == null)
			openInEditorAction = new OpenInEditorAction(treeViewer);

		if (downloadFileAction == null)
			downloadFileAction = new DownloadFileAction(treeViewer);

		if (importMetadata == null)
			importMetadata = new ImportMetadataAction(treeViewer);
		if (exportMetadata == null)
			exportMetadata = new ExportMetadataAction(treeViewer);

		if (showPermissionsAction == null)
			showPermissionsAction = new ShowPermissionsAction(treeViewer);
	}

	public List<IAction> fillContextMenu(TreeViewer treeViewer, ANode node) {
		createActions(treeViewer);
		List<IAction> lst = new ArrayList<IAction>();
		if (node instanceof MServers) {
			if (createServerAction.isEnabled())
				lst.add(createServerAction);
		} else if (node instanceof MServerProfile) {
			if (addAction.isEnabled())
				lst.add(addAction);
			lst.add(new Separator());

			if (pasteAction.isEnabled())
				lst.add(pasteAction);
			if (pasteLinkAction.isEnabled())
				lst.add(pasteLinkAction);
			lst.add(new Separator());

			if (editServerAction.isEnabled())
				lst.add(editServerAction);
			if (duplicateServerAction.isEnabled())
				lst.add(duplicateServerAction);

			lst.add(new Separator());
			if (findResourceAction.isEnabled())
				lst.add(findResourceAction);

			lst.add(new Separator());
			if (refreshAction.isEnabled())
				lst.add(refreshAction);

			lst.add(new Separator());
			if (deleteServerAction.isEnabled())
				lst.add(deleteServerAction);

			lst.add(new Separator());

			if (importMetadata.isEnabled())
				lst.add(importMetadata);
			if (exportMetadata.isEnabled())
				lst.add(exportMetadata);
		} else if (node instanceof MResource) {
			if (addAction.isEnabled() && (node instanceof MFolder || node instanceof MReportUnit))
				lst.add(addAction);
			lst.add(new Separator());

			if (((MResource) node).isInsideReportUnit() && runReportUnitAction.isEnabled())
				lst.add(runReportUnitAction);
			lst.add(new Separator());

			if (openInEditorAction.isEnabled())
				lst.add(openInEditorAction);

			if (node instanceof AFileResource && downloadFileAction.isEnabled() && !(node instanceof MReportUnit))
				lst.add(downloadFileAction);

			lst.add(new Separator());

			if (cutAction.isEnabled())
				lst.add(cutAction);
			if (copyAction.isEnabled())
				lst.add(copyAction);
			if (pasteAction.isEnabled())
				lst.add(pasteAction);
			if (pasteLinkAction.isEnabled())
				lst.add(pasteLinkAction);

			if (deleteAction.isEnabled())
				lst.add(deleteAction);
			if (importDataSourceInJSSAction.isEnabled()) {
				lst.add(importDataSourceInJSSAction);
			}

			lst.add(new Separator());

			if (findResourceAction.isEnabled())
				lst.add(findResourceAction);

			lst.add(new Separator());

			if (refreshAction.isEnabled())
				lst.add(refreshAction);
			lst.add(new Separator());

			if (editAction.isEnabled())
				lst.add(editAction);

			if (((MResource) node).getWsClient().isSupported(Feature.PERMISSION) && showPermissionsAction.isEnabled())
				lst.add(showPermissionsAction);

			lst.add(new Separator());

			if (importMetadata.isEnabled())
				lst.add(importMetadata);
			if (exportMetadata.isEnabled())
				lst.add(exportMetadata);
		}
		return lst;
	}

	public void hookKeyEvent(TreeViewer treeViewer, KeyEvent event) {
		if (event.character == SWT.DEL && event.stateMask == 0) {
			if (deleteServerAction.isEnabled())
				deleteServerAction.run();
			if (deleteAction.isEnabled())
				deleteAction.run();
		} else if (((event.stateMask & SWT.CTRL) == SWT.CTRL) && (event.keyCode == 'f'))
			if (findResourceAction.isEnabled())
				findResourceAction.run();
	}

	public void doubleClick(TreeViewer treeViewer) {
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		Object el = ts.getFirstElement();
		if (el instanceof MFolder) {
			if (treeViewer.getExpandedState(el))
				treeViewer.collapseToLevel(el, 1);
			else {
				if (refreshAction.isEnabled())
					refreshAction.run();
				treeViewer.expandToLevel(el, 1);
			}
		}
		if (el instanceof MInputControl && editAction.isEnabled()) {
			editAction.run();
			return;
		}
		if (editServerAction.isEnabled())
			editServerAction.run();
		// if (runReportUnitAction.isEnabled())
		// runReportUnitAction.run();
		else if (openInEditorAction.isEnabled())
			openInEditorAction.run();
		else if (runReportUnitAction.isEnabled())
			runReportUnitAction.run();
	}

	public ANode getNode(ANode root) {
		MServers servers = new MServers(root);

		ServerManager.loadServerProfiles(servers);

		return servers;
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		ServerManager.getPropertyChangeSupport().addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		ServerManager.getPropertyChangeSupport().removePropertyChangeListener(pcl);
	}

	public void handleTreeEvent(TreeExpansionEvent event) {
		if (event.getElement() instanceof MServerProfile)
			listServer(event);
		else if (event.getElement() instanceof MResource)
			lazyLoadResource(event);
	}

	public void handleTreeEvent(TreeExpansionEvent event, IProgressMonitor monitor) {
		if (event.getElement() instanceof MServerProfile)
			listServer(event, monitor);
		else if (event.getElement() instanceof MResource)
			lazyLoadResource(event, monitor);
	}

	private void listServer(final TreeExpansionEvent event) {
		if (skipLazyLoad)
			return;
		Job job = new Job("Refreshing tree") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IStatus status = Status.OK_STATUS;
				try {
					status = listServer(event, monitor);
				} finally {
					monitor.done();
				}
				return status;
			}
		};
		job.setPriority(Job.SHORT);
		job.setSystem(false);
		job.setUser(true);
		job.schedule();
	}

	private void lazyLoadResource(final TreeExpansionEvent event) {
		if (skipLazyLoad)
			return;
		Job job = new Job("Refreshing tree") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				return lazyLoadResource(event, monitor);
			}
		};
		job.setPriority(Job.SHORT);
		job.setSystem(false);
		job.setUser(true);
		job.schedule();
	}

	public IStatus lazyLoadResource(final TreeExpansionEvent event, IProgressMonitor monitor) {
		if (skipLazyLoad)
			return Status.OK_STATUS;
		MResource r = (MResource) event.getElement();
		try {
			WSClientHelper.refreshResource(r, monitor);
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					event.getTreeViewer().refresh(true);
				}
			});
			return Status.OK_STATUS;
		} catch (final Throwable e) {
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					event.getTreeViewer().collapseToLevel(event.getElement(), 1);
					UIUtils.showErrorDialog(e.getMessage(), e);
				}
			});
		}
		return Status.CANCEL_STATUS;
	}

	private IStatus listServer(final TreeExpansionEvent event, final IProgressMonitor monitor) {
		final TreeViewer tv = (TreeViewer) event.getTreeViewer();
		final MServerProfile r = (MServerProfile) event.getElement();
		try {
			WSClientHelper.connectGetData(r, monitor);
			UIUtils.getDisplay().asyncExec(new Runnable() {

				public void run() {
					tv.refresh(r, true);
				}
			});

			return Status.OK_STATUS;
		} catch (final Throwable e) {
			UIUtils.getDisplay().syncExec(new Runnable() {

				public void run() {
					tv.collapseToLevel(r, 1);
					if (!monitor.isCanceled())
						UIUtils.showErrorDialog(e.getMessage(), e);
				}
			});
		}
		return Status.CANCEL_STATUS;
	}

	@Override
	public List<TransferDragSourceListener> getTransferDragSourceListeners(TreeViewer treeViewer) {
		List<TransferDragSourceListener> dragListeners = new ArrayList<TransferDragSourceListener>(2);
		dragListeners.add(new RepositoryImageDragSourceListener(treeViewer));
		dragListeners.add(new UnitDragSourceListener(treeViewer));
		dragListeners.add(new InputControlDragSourceListener(treeViewer));
		return dragListeners;
	}

	@Override
	public List<TransferDropTargetListener> getTransferDropTargetListeners(TreeViewer treeViewer) {
		List<TransferDropTargetListener> dropListeners = new ArrayList<TransferDropTargetListener>(1);
		dropListeners.add(new RepositoryFileResourceDropTargetListener(FileTransfer.getInstance()));
		dropListeners.add(new InputControlDropTargetListener(treeViewer));
		return dropListeners;
	}

	private boolean skipLazyLoad = false;

	public void setSkipLazyLoad(boolean skipLazyLoad) {
		this.skipLazyLoad = skipLazyLoad;
	}
}
