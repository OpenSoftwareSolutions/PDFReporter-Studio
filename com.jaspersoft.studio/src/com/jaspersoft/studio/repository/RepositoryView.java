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
package com.jaspersoft.studio.repository;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.dnd.DelegatingDragAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.DelegatingDropAdapter;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;
import com.jaspersoft.studio.plugin.ExtensionManager;
import com.jaspersoft.studio.properties.view.ITabbedPropertySheetPageContributor;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.repository.actions.Separator;

public class RepositoryView extends ViewPart implements ITabbedPropertySheetPageContributor {
	public RepositoryView() {
		super();
	}

	private TreeViewer treeViewer;
	private PropertyChangeListener propChangeListener = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent evt) {
			if (!treeViewer.getTree().isDisposed()) {
				treeViewer.refresh(true);
				treeViewer.expandToLevel(evt.getNewValue(), 1);
			}
		}
	};

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == IPropertySource.class)
			return getPropertySheetPage();
		if (type == IPropertySource2.class)
			return getPropertySheetPage();
		if (type == IPropertySheetPage.class)
			return getPropertySheetPage();
		return super.getAdapter(type);
	}

	/** The property sheet page. */
	private IPropertySheetPage propertySheetPage;

	public IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null)
			propertySheetPage = new TabbedPropertySheetPage(this, true);
		return propertySheetPage;
	}

	public String getContributorId() {
		return getSite().getId();
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, "com.jaspersoft.studio.doc.view_repository");

		treeViewer = new TreeViewer(composite);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer.setContentProvider(new ReportTreeContetProvider());
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());
		treeViewer.setInput(getResources()); // pass a non-null that will be ignored
		treeViewer.expandToLevel(2);
		ColumnViewerToolTipSupport.enableFor(treeViewer);
		// getViewSite().setSelectionProvider(treeViewer);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						rprovs = getExtensionManager();
						for (IRepositoryViewProvider rp : rprovs) {
							rp.doubleClick(treeViewer);
						}
					}
				});
			}
		});
		treeViewer.addTreeListener(new ITreeViewerListener() {

			public void treeExpanded(TreeExpansionEvent event) {
				rprovs = getExtensionManager();
				for (IRepositoryViewProvider rp : rprovs)
					rp.handleTreeEvent(event);
			}

			public void treeCollapsed(TreeExpansionEvent event) {
			}
		});
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				for (IContributionItem ci : topToolbarManager.getItems()) {
					if (ci instanceof ActionContributionItem) {
						IAction action = ((ActionContributionItem) ci).getAction();
						action.setEnabled(action.isEnabled());
					}
				}
				// getViewSite().getActionBars().updateActionBars();

				IActionBars actionBars = getViewSite().getActionBars();
				actionBars.clearGlobalActionHandlers();

				List<IAction> alist = fillContextMenu();
				if (alist != null) {
					for (IAction act : alist) {
						if (!(act instanceof Separator))
							actionBars.setGlobalActionHandler(act.getId(), act);
					}
				}
			}
		});

		// Create menu and toolbars.
		createActions();
		createMenu();
		createToolbar();
		createContextMenu();
		hookGlobalActions();
		addDNDListeners();

		rprovs = getExtensionManager();
		for (IRepositoryViewProvider rp : rprovs) {
			rp.addPropertyChangeListener(propChangeListener);
		}
	}

	@Override
	public void dispose() {
		rprovs = getExtensionManager();
		for (IRepositoryViewProvider rp : rprovs) {
			rp.removePropertyChangeListener(propChangeListener);
		}
		super.dispose();
	}

	private List<IRepositoryViewProvider> rprovs;
	private ExtensionManager extensionManager;
	private IToolBarManager topToolbarManager;

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		init(site);
		getExtensionManager();
	}

	private void hookGlobalActions() {
		treeViewer.getControl().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				rprovs = getExtensionManager();
				for (IRepositoryViewProvider rp : rprovs) {
					rp.hookKeyEvent(treeViewer, event);
				}
			}
		});
	}

	public void createActions() {

	}

	/**
	 * Create menu.
	 */
	private void createMenu() {
	}

	/**
	 * Create toolbar.
	 */
	private void createToolbar() {
		topToolbarManager = getViewSite().getActionBars().getToolBarManager();

		rprovs = getExtensionManager();
		for (IRepositoryViewProvider rp : rprovs) {
			Action[] actions = rp.getActions(treeViewer);
			if (actions != null) {
				for (Action a : actions)
					topToolbarManager.add(a);
			}
		}
	}

	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillMenu(mgr, fillContextMenu());
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, treeViewer);
	}

	/**
	 * Add the listeners for the drag and drop
	 */
	private void addDNDListeners() {
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		List<TransferDragSourceListener> dragListeners = new ArrayList<TransferDragSourceListener>();
		List<TransferDropTargetListener> dropListeners = new ArrayList<TransferDropTargetListener>();
		for (IRepositoryViewProvider rp : rprovs) {
			List<TransferDragSourceListener> tdsl = rp.getTransferDragSourceListeners(treeViewer);
			if (tdsl != null)
				dragListeners.addAll(tdsl);
			List<TransferDropTargetListener> tdtl = rp.getTransferDropTargetListeners(treeViewer);
			if (tdtl != null)
				dropListeners.addAll(tdtl);
		}
		// In case its needed add the related delegating adapter for both drag and drop operations
		if (!dragListeners.isEmpty()) {
			DelegatingDragAdapter dragAdapter = new DelegatingDragAdapter();
			for (TransferDragSourceListener dragListener : dragListeners) {
				dragAdapter.addDragSourceListener(dragListener);
			}
			treeViewer.addDragSupport(operations, dragAdapter.getTransfers(), dragAdapter);
		}
		if (!dropListeners.isEmpty()) {
			DelegatingDropAdapter dropAdapter = new DelegatingDropAdapter();
			for (TransferDropTargetListener dropListener : dropListeners) {
				dropAdapter.addDropTargetListener(dropListener);
			}
			treeViewer.addDropSupport(operations, dropAdapter.getTransfers(), dropAdapter);
		}
	}

	private List<IAction> fillContextMenu() {
		TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		List<IAction> alist = null;
		for (int i = 0; i < p.length; i++) {
			Object obj = p[i].getLastSegment();
			if (obj instanceof ANode) {
				rprovs = getExtensionManager();
				List<IAction> tlist = new ArrayList<IAction>();
				for (IRepositoryViewProvider rp : rprovs) {
					List<IAction> t = rp.fillContextMenu(treeViewer, (ANode) obj);
					if (t != null)
						tlist.addAll(t);
				}
				if (tlist == null || tlist.isEmpty())
					return null;
				if (alist == null) {
					alist = tlist;
				} else {
					List<IAction> todelete = new ArrayList<IAction>();
					for (IAction a : alist) {
						if (!tlist.contains(a)) {
							todelete.add(a);
						}
					}
					alist.removeAll(todelete);
					if (alist.isEmpty())
						return null;
				}
			}
		}
		return alist;
	}

	private void fillMenu(IMenuManager mgr, List<IAction> alist) {
		if (alist != null) {
			for (IAction act : alist) {
				if (act instanceof Separator)
					mgr.add(new org.eclipse.jface.action.Separator());
				else {
					mgr.add(act);
				}
			}
		}
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	public ANode getResources() {
		MRoot rootNode = new MRoot(null, null);

		rprovs = getExtensionManager();
		for (IRepositoryViewProvider rp : rprovs) {
			rp.getNode(rootNode);
		}

		return rootNode;
	}

	private List<IRepositoryViewProvider> getExtensionManager() {
		if (extensionManager == null) {
			extensionManager = new ExtensionManager();
			extensionManager.init();
		}
		if (rprovs == null)
			rprovs = extensionManager.getRepositoryProviders();
		return rprovs;
	}
}
