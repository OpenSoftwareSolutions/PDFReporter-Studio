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
package com.jaspersoft.studio.editor;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.editor.action.ShowPropertyViewAction;
import com.jaspersoft.studio.editor.action.copy.CopyAction;
import com.jaspersoft.studio.editor.action.copy.CutAction;
import com.jaspersoft.studio.editor.action.copy.PasteAction;
import com.jaspersoft.studio.editor.gef.parts.MainDesignerRootEditPart;
import com.jaspersoft.studio.editor.java2d.J2DGraphicalEditor;
import com.jaspersoft.studio.editor.outline.JDReportOutlineView;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.report.CommonSelectionCacheProvider;
import com.jaspersoft.studio.editor.report.EditorContributor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.properties.view.ITabbedPropertySheetPageContributor;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AGraphicEditor extends J2DGraphicalEditor implements ITabbedPropertySheetPageContributor,
		IGraphicalEditor, CachedSelectionProvider  {

	protected JasperReportsConfiguration jrContext;
	
	protected CommonSelectionCacheProvider cachedSelection = new CommonSelectionCacheProvider();

	public AGraphicEditor(JasperReportsConfiguration jrContext) {
		super();
		setEditDomain(new DefaultEditDomain(this));
		this.jrContext = jrContext;
	}

	public FigureCanvas getEditor() {
		return (FigureCanvas) getGraphicalViewer().getControl();
	}

	@Override
	public SelectionSynchronizer getSelectionSynchronizer() {
		return super.getSelectionSynchronizer();
	}

	@Override
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	public String getContributorId() {
		return "com.jaspersoft.studio.editor.report.ReportContainer";// "com.jaspersoft.studio.JRtxEditor";
	}

	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	@Override
	public ActionRegistry getActionRegistry() {
		return super.getActionRegistry();
	}

	private INode model;

	/**
	 * Sets the model.
	 * 
	 * @param model
	 *          the new model
	 */
	public void setModel(INode model) {
		this.model = model;
		if (outlinePage != null)
			outlinePage.setContents(model);
		if (model != null)
			getGraphicalViewer().setContents(model.getChildren().get(0));
		else
			getGraphicalViewer().setContents(null);
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public INode getModel() {
		return model;
	}

	private EditorContributor editorContributor;

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == IPropertySource.class)
			return getPropertySheetPage();
		if (type == IPropertySheetPage.class)
			return getPropertySheetPage();
		if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		if (type == IContentOutlinePage.class) {
			return getOutlineView();
		}
		if (type == EditorContributor.class) {
			if (editorContributor == null)
				editorContributor = new EditorContributor(getEditDomain());
			return editorContributor;
		}
		return super.getAdapter(type);
	}

	/** The property sheet page. */
	private IPropertySheetPage propertySheetPage;

	/**
	 * Gets the property sheet page.
	 * 
	 * @return the property sheet page
	 */
	public IPropertySheetPage getPropertySheetPage() {
		propertySheetPage = new TabbedPropertySheetPage(this, true);
		return propertySheetPage;
	}

	@Override
	public void createPartControl(Composite parent) {
		createGraphicalViewer(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#selectionChanged(org.eclipse.ui.IWorkbenchPart,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		cachedSelection.selectionChanged(selection);
		updateActions(getSelectionActions());
	}
	
	@Override
	public CommonSelectionCacheProvider getSelectionCache() {
		return cachedSelection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer graphicalViewer = getGraphicalViewer();

		MainDesignerRootEditPart rootEditPart = new MainDesignerRootEditPart();

		graphicalViewer.setRootEditPart(rootEditPart);
		// set EditPartFactory
		graphicalViewer.setEditPartFactory(createEditParFactory());

		graphicalViewer.setKeyHandler(new GraphicalViewerKeyHandler(graphicalViewer));

		graphicalViewer.setContextMenu(createContextMenuProvider(graphicalViewer));

		ZoomManager zoomManager = (ZoomManager) graphicalViewer.getProperty(ZoomManager.class.toString());

		getActionRegistry().registerAction(new ZoomInAction(zoomManager));
		getActionRegistry().registerAction(new ZoomOutAction(zoomManager));
		getActionRegistry().registerAction(new ZoomActualAction(zoomManager));
		
		graphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
		graphicalViewer.setProperty("JRCONTEXT", jrContext);
	}

	protected abstract ContextMenuProvider createContextMenuProvider(EditPartViewer graphicalViewer);

	protected abstract EditPartFactory createEditParFactory();

	protected JDReportOutlineView outlinePage;

	protected JDReportOutlineView getOutlineView() {
		if (outlinePage == null) {
			TreeViewer viewer = new TreeViewer();
			outlinePage = createOutline(viewer);
		}
		return outlinePage;
	}

	protected abstract JDReportOutlineView createOutline(TreeViewer viewer);

	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new CutAction(this);
		registry.registerAction(action);
		List<String> selectionActions = getSelectionActions();
		selectionActions.add(action.getId());

		action = new CopyAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new PasteAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// ------------------

		action = new ShowPropertyViewAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}

	@Override
	protected void initializeGraphicalViewer() {

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		getEditDomain().getCommandStack().markSaveLocation();
	}
}
