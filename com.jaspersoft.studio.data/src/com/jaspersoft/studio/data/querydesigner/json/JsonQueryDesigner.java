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
package com.jaspersoft.studio.data.querydesigner.json;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.data.json.JsonDataAdapter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.progress.WorkbenchJob;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.designer.AQueryDesignerContainer;
import com.jaspersoft.studio.data.designer.tree.NodeBoldStyledLabelProvider;
import com.jaspersoft.studio.data.designer.tree.TreeBasedQueryDesigner;
import com.jaspersoft.studio.data.messages.Messages;
import com.jaspersoft.studio.dnd.NodeDragListener;
import com.jaspersoft.studio.dnd.NodeTransfer;
import com.jaspersoft.studio.model.datasource.json.JsonSupportNode;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

/**
 * Json query designer that provides a basic syntax highlighting support, plus a
 * tree viewer where the json file is visualized.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class JsonQueryDesigner extends TreeBasedQueryDesigner {

	private static final int JOB_DELAY = 300;
	private JsonDataManager jsonDataManager;
	private DecorateTreeViewerJob decorateJob;
	private NodeBoldStyledLabelProvider<JsonSupportNode> treeLabelProvider;
	private JsonLineStyler lineStyler;
	private Composite toolbarComposite;

	public JsonQueryDesigner() {
		super();
		this.jsonDataManager = new JsonDataManager();
		this.lineStyler = new JsonLineStyler();
		this.decorateJob = new DecorateTreeViewerJob();
		this.treeLabelProvider = new NodeBoldStyledLabelProvider<JsonSupportNode>();
	}

	@Override
	public Control createToolbar(Composite parent) {
		if (showAdditionalInfo()) {
			toolbarComposite = new Composite(parent, SWT.NONE);
			toolbarComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			toolbarComposite.setLayout(layout);
			toolbarComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			Button btn = new Button(toolbarComposite, SWT.PUSH);
			btn.setText(Messages.JsonQueryDesigner_ReadFieldsBtn);
			btn.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
			btn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					container.doGetFields();
				}

			});
			return toolbarComposite;
		} else {
			return null;
		}
	}

	@Override
	public Control getToolbarControl() {
		return this.toolbarComposite;
	}

	@Override
	public Control createControl(Composite parent) {
		Control createdControl = super.createControl(parent);
		queryTextArea.addLineStyleListener(lineStyler);
		return createdControl;
	}

	@Override
	protected void createTreeViewer(Composite parent) {
		super.createTreeViewer(parent);
		if (showAdditionalInfo()) {
			addDragSupport();
			createContextualMenu();
		}
		addDoubleClickSupport();
	}

	@Override
	protected IBaseLabelProvider getTreeLabelProvider() {
		return this.treeLabelProvider;
	}

	@Override
	protected IContentProvider getTreeContentProvider() {
		return new JsonTreeContentProvider();
	}

	@Override
	protected void decorateTreeUsingQueryText() {
		if (jsonDataManager.getJsonSupportModel() != null) {
			decorateJob.cancel();
			decorateJob.schedule(JOB_DELAY);
		}
	}

	private boolean showAdditionalInfo() {
		return container.getContainerType() == AQueryDesignerContainer.CONTAINER_WITH_INFO_TABLES;
	}

	/*
	 * Adds support for generating the query expression, using the current
	 * selected node as input.
	 */
	private void addDoubleClickSupport() {
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection s = (TreeSelection) treeViewer.getSelection();
				if (s.getFirstElement() instanceof JsonSupportNode) {
					JsonSupportNode jsonNode = (JsonSupportNode) s
							.getFirstElement();
					String queryExpression = jsonDataManager
							.getQueryExpression(null, jsonNode);
					queryTextArea.setText(queryExpression);
				}
			}
		});
	}

	@Override
	protected void refreshTreeViewerContent(final DataAdapterDescriptor da) {
		if (!isRefreshing) {
			this.container.getQueryStatus().showInfo(""); //$NON-NLS-1$
			if (da != null && da.getDataAdapter() instanceof JsonDataAdapter) {
				treeViewer.setInput(JsonTreeCustomStatus.LOADING_JSON);

				try {
					JsonQueryDesigner.this.run(true, true,
							new IRunnableWithProgress() {

								@Override
								public void run(IProgressMonitor monitor)
										throws InvocationTargetException,
										InterruptedException {
									monitor.beginTask(
											Messages.JsonQueryDesigner_Task, -1);
									String fileName = ((JsonDataAdapter) da
											.getDataAdapter()).getFileName();
									try {
										jsonDataManager
												.loadJsonDataFile(fileName);
										Display.getDefault().asyncExec(
												new Runnable() {
													@Override
													public void run() {
														treeViewer
																.setInput(jsonDataManager
																		.getJsonSupportModel());
														treeViewer
																.expandToLevel(2);
														decorateTreeUsingQueryText();
														isRefreshing = false;
													}
												});
									} catch (Exception e) {
										JsonQueryDesigner.this.container
												.getQueryStatus().showError(e);
										Display.getDefault().asyncExec(
												new Runnable() {
													@Override
													public void run() {
														treeViewer.getTree()
																.removeAll();
														treeViewer
																.setInput(JsonTreeCustomStatus.ERROR_LOADING_JSON);
														isRefreshing = false;
													}
												});
									} finally {
										monitor.done();
									}
								}
							});
				} catch (Exception ex) {
					JsonQueryDesigner.this.container.getQueryStatus()
							.showError(ex);
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							treeViewer.getTree().removeAll();
							treeViewer
									.setInput(JsonTreeCustomStatus.ERROR_LOADING_JSON);
							isRefreshing = false;
						}
					});
				}
			} else {
				treeViewer.getTree().removeAll();
				treeViewer.setInput(JsonTreeCustomStatus.FILE_NOT_FOUND);
				isRefreshing = false;
			}
		}
	}

	/*
	 * Adds drag support to the Json tree viewer.
	 */
	private void addDragSupport() {
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { NodeTransfer.getInstance(),
				PluginTransfer.getInstance() };
		treeViewer.addDragSupport(ops, transfers, new NodeDragListener(
				treeViewer) {
			@Override
			public void dragStart(DragSourceEvent event) {
				TreeSelection s = (TreeSelection) treeViewer.getSelection();
				if (s.getFirstElement() instanceof JsonSupportNode) {
					JsonSupportNode jsonNode = (JsonSupportNode) s
							.getFirstElement();
					jsonNode.setExpression(jsonDataManager.getQueryExpression(
							queryTextArea.getText(), jsonNode));
					event.doit = !s.isEmpty();
				} else {
					event.doit = false;
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				if (!event.doit)
					return;
			}
		});
	}

	/*
	 * Creates the contextual menu for the tree representing the Json document.
	 */
	private void createContextualMenu() {
		Menu contextMenu = new Menu(treeViewer.getTree());
		final MenuItem setRecordNodeItem = new MenuItem(contextMenu, SWT.PUSH);
		setRecordNodeItem.setText(Messages.JsonQueryDesigner_ItemSetRecordNode);
		setRecordNodeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object sel = ((IStructuredSelection) treeViewer.getSelection())
						.getFirstElement();
				if (sel instanceof JsonSupportNode) {
					String queryExpression = jsonDataManager
							.getQueryExpression(null, (JsonSupportNode) sel);
					queryTextArea.setText(queryExpression);
				}
			}
		});
		new MenuItem(contextMenu, SWT.SEPARATOR);
		final MenuItem addNodeAsFieldItem1 = new MenuItem(contextMenu, SWT.PUSH);
		addNodeAsFieldItem1.setText(Messages.JsonQueryDesigner_ItemAddNode);
		addNodeAsFieldItem1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object sel = ((IStructuredSelection) treeViewer.getSelection())
						.getFirstElement();
				if (sel instanceof JsonSupportNode) {
					String queryExpression = jsonDataManager
							.getQueryExpression(queryTextArea.getText(),
									(JsonSupportNode) sel);
					((JsonSupportNode) sel).setExpression(queryExpression);
					createField((JsonSupportNode) sel);
				}
			}
		});
		final MenuItem addNodeAsFieldItem2 = new MenuItem(contextMenu, SWT.PUSH);
		addNodeAsFieldItem2
				.setText(Messages.JsonQueryDesigner_ItemAddNodeAbsolute);
		addNodeAsFieldItem2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object sel = ((IStructuredSelection) treeViewer.getSelection())
						.getFirstElement();
				if (sel instanceof JsonSupportNode) {
					String queryExpression = jsonDataManager
							.getQueryExpression(null, (JsonSupportNode) sel);
					((JsonSupportNode) sel).setExpression(queryExpression);
					createField((JsonSupportNode) sel);
				}
			}
		});
		new MenuItem(contextMenu, SWT.SEPARATOR);
		final MenuItem expandAllItem = new MenuItem(contextMenu, SWT.PUSH);
		expandAllItem.setText(Messages.JsonQueryDesigner_ItemExpandAll);
		expandAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeViewer.expandAll();
			}
		});
		final MenuItem collapseAllItem = new MenuItem(contextMenu, SWT.PUSH);
		collapseAllItem.setText(Messages.JsonQueryDesigner_ItemCollapseAll);
		collapseAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeViewer.collapseAll();
			}
		});
		final MenuItem resetRefreshDocItem = new MenuItem(contextMenu, SWT.PUSH);
		resetRefreshDocItem
				.setText(Messages.JsonQueryDesigner_ItemResetRefresh);
		resetRefreshDocItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTreeViewerContent(container.getDataAdapter());
			}
		});
		treeViewer.getTree().setMenu(contextMenu);

		contextMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuShown(MenuEvent e) {
				Object selEl = ((IStructuredSelection) treeViewer
						.getSelection()).getFirstElement();
				if (selEl instanceof JsonSupportNode) {
					addNodeAsFieldItem1.setEnabled(true);
					addNodeAsFieldItem2.setEnabled(true);
					setRecordNodeItem.setEnabled(true);
				} else {
					setRecordNodeItem.setEnabled(false);
					addNodeAsFieldItem1.setEnabled(false);
					addNodeAsFieldItem2.setEnabled(false);
				}
			}

			@Override
			public void menuHidden(MenuEvent e) {

			}
		});
	}

	/*
	 * Job that is responsible to update the treeviewer presentation depending
	 * on the nodes selected by the Json query.
	 */
	private final class DecorateTreeViewerJob extends WorkbenchJob {

		public DecorateTreeViewerJob() {
			super(Messages.JsonQueryDesigner_Job);
			setSystem(true);
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			if (control != null && !control.isDisposed()) {
				monitor.beginTask(Messages.JsonQueryDesigner_JobTask,
						IProgressMonitor.UNKNOWN);
				String query = queryTextArea.getText();
				treeLabelProvider.setSelectedNodes(jsonDataManager
						.getSelectableNodes(query));
				treeViewer.refresh();
				monitor.done();
				return Status.OK_STATUS;
			} else {
				return Status.CANCEL_STATUS;
			}
		}

	}

	@Override
	public void dispose() {
		if (decorateJob != null) {
			decorateJob.cancel();
			decorateJob = null;
		}
		super.dispose();
	}

	@Override
	public String getContextHelpId() {
		return ContextHelpIDs.WIZARD_QUERY_DIALOG;
	}

}
