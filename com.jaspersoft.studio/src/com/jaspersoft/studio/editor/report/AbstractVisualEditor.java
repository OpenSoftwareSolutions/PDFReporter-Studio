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
package com.jaspersoft.studio.editor.report;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.ResourceTransfer;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.callout.action.CreatePinAction;
import com.jaspersoft.studio.editor.IGraphicalEditor;
import com.jaspersoft.studio.editor.ZoomActualAction;
import com.jaspersoft.studio.editor.action.CustomDeleteAction;
import com.jaspersoft.studio.editor.action.HideElementsAction;
import com.jaspersoft.studio.editor.action.MoveDetailDownAction;
import com.jaspersoft.studio.editor.action.MoveDetailUpAction;
import com.jaspersoft.studio.editor.action.MoveGroupDownAction;
import com.jaspersoft.studio.editor.action.MoveGroupUpAction;
import com.jaspersoft.studio.editor.action.OpenEditorAction;
import com.jaspersoft.studio.editor.action.ShowPropertyViewAction;
import com.jaspersoft.studio.editor.action.align.Align2BorderAction;
import com.jaspersoft.studio.editor.action.align.Align2Element;
import com.jaspersoft.studio.editor.action.band.MaximizeContainerAction;
import com.jaspersoft.studio.editor.action.band.StretchToContentAction;
import com.jaspersoft.studio.editor.action.copy.CopyAction;
import com.jaspersoft.studio.editor.action.copy.CopyFormatAction;
import com.jaspersoft.studio.editor.action.copy.CutAction;
import com.jaspersoft.studio.editor.action.copy.PasteAction;
import com.jaspersoft.studio.editor.action.copy.PasteFormatAction;
import com.jaspersoft.studio.editor.action.exporter.AddExporterPropertyAction;
import com.jaspersoft.studio.editor.action.image.ChangeImageExpression;
import com.jaspersoft.studio.editor.action.order.BringBackwardAction;
import com.jaspersoft.studio.editor.action.order.BringForwardAction;
import com.jaspersoft.studio.editor.action.order.BringToBackAction;
import com.jaspersoft.studio.editor.action.order.BringToFrontAction;
import com.jaspersoft.studio.editor.action.size.MatchSizeAction;
import com.jaspersoft.studio.editor.action.size.Size2BorderAction;
import com.jaspersoft.studio.editor.action.snap.ShowGridAction;
import com.jaspersoft.studio.editor.action.snap.ShowRullersAction;
import com.jaspersoft.studio.editor.action.snap.SizeGridAction;
import com.jaspersoft.studio.editor.action.snap.SnapToGeometryAction;
import com.jaspersoft.studio.editor.action.snap.SnapToGridAction;
import com.jaspersoft.studio.editor.action.snap.SnapToGuidesAction;
import com.jaspersoft.studio.editor.action.text.BoldAction;
import com.jaspersoft.studio.editor.action.text.ConvertStaticIntoText;
import com.jaspersoft.studio.editor.action.text.ConvertTextIntoStatic;
import com.jaspersoft.studio.editor.action.text.ItalicAction;
import com.jaspersoft.studio.editor.action.text.StrikethroughAction;
import com.jaspersoft.studio.editor.action.text.UnderlineAction;
import com.jaspersoft.studio.editor.defaults.SetDefaultsAction;
import com.jaspersoft.studio.editor.dnd.ImageResourceDropTargetListener;
import com.jaspersoft.studio.editor.dnd.ImageURLTransfer;
import com.jaspersoft.studio.editor.dnd.JSSTemplateTransferDropTargetListener;
import com.jaspersoft.studio.editor.gef.rulers.component.JDRulerComposite;
import com.jaspersoft.studio.editor.gef.ui.actions.RZoomComboContributionItem;
import com.jaspersoft.studio.editor.gef.ui.actions.ViewSettingsDropDownAction;
import com.jaspersoft.studio.editor.java2d.J2DGraphicalEditorWithFlyoutPalette;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.editor.menu.AppContextMenuProvider;
import com.jaspersoft.studio.editor.outline.JDReportOutlineView;
import com.jaspersoft.studio.editor.outline.actions.ConnectToDomainAction;
import com.jaspersoft.studio.editor.outline.actions.CreateConditionalStyleAction;
import com.jaspersoft.studio.editor.outline.actions.CreateDatasetAction;
import com.jaspersoft.studio.editor.outline.actions.CreateFieldAction;
import com.jaspersoft.studio.editor.outline.actions.CreateGroupAction;
import com.jaspersoft.studio.editor.outline.actions.CreateParameterAction;
import com.jaspersoft.studio.editor.outline.actions.CreateParameterSetAction;
import com.jaspersoft.studio.editor.outline.actions.CreateScriptletAction;
import com.jaspersoft.studio.editor.outline.actions.CreateSortFieldAction;
import com.jaspersoft.studio.editor.outline.actions.CreateStyleAction;
import com.jaspersoft.studio.editor.outline.actions.CreateStyleTemplateAction;
import com.jaspersoft.studio.editor.outline.actions.CreateVariableAction;
import com.jaspersoft.studio.editor.outline.actions.ExportStyleAsTemplateAction;
import com.jaspersoft.studio.editor.outline.actions.RefreshTemplateStyleExpression;
import com.jaspersoft.studio.editor.outline.actions.ResetStyleAction;
import com.jaspersoft.studio.editor.outline.page.MultiOutlineView;
import com.jaspersoft.studio.editor.palette.JDPaletteFactory;
import com.jaspersoft.studio.editor.part.MultiPageToolbarEditorPart;
import com.jaspersoft.studio.formatting.actions.CenterInParentAction;
import com.jaspersoft.studio.formatting.actions.DecreaseHSpaceAction;
import com.jaspersoft.studio.formatting.actions.DecreaseVSpaceAction;
import com.jaspersoft.studio.formatting.actions.EqualsHSpaceAction;
import com.jaspersoft.studio.formatting.actions.EqualsVSpaceAction;
import com.jaspersoft.studio.formatting.actions.IncreaseHSpaceAction;
import com.jaspersoft.studio.formatting.actions.IncreaseVSpaceAction;
import com.jaspersoft.studio.formatting.actions.JoinLeftAction;
import com.jaspersoft.studio.formatting.actions.JoinRightAction;
import com.jaspersoft.studio.formatting.actions.OrganizeAsTableAction;
import com.jaspersoft.studio.formatting.actions.RemoveHSpaceAction;
import com.jaspersoft.studio.formatting.actions.RemoveVSpaceAction;
import com.jaspersoft.studio.formatting.actions.SameHeightMaxAction;
import com.jaspersoft.studio.formatting.actions.SameHeightMinAction;
import com.jaspersoft.studio.formatting.actions.SameWidthMaxAction;
import com.jaspersoft.studio.formatting.actions.SameWidthMinAction;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;
import com.jaspersoft.studio.style.view.TemplateViewProvider;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class AbstractVisualEditor.
 * 
 * @author Chicu Veaceslav
 */
public abstract class AbstractVisualEditor extends J2DGraphicalEditorWithFlyoutPalette implements IAdaptable, IGraphicalEditor, CachedSelectionProvider {
	
	private Image partImage = JaspersoftStudioPlugin.getInstance().getImage(MReport.getIconDescriptor().getIcon16());
	
	private FlyoutPreferences palettePreferences;
	
	protected JasperReportsConfiguration jrContext;

	public JasperReportsConfiguration getJrContext() {
		return jrContext;
	}


	public Image getPartImage() {
		return partImage;
	}

	/**
	 * Instantiates a new abstract visual editor.
	 */
	public AbstractVisualEditor(JasperReportsConfiguration jrContext) {
		DefaultEditDomain ed = new DefaultEditDomain(this);
		setEditDomain(ed);
		this.jrContext = jrContext;
	}

	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	@Override
	public void setEditDomain(DefaultEditDomain ed) {
		super.setEditDomain(ed);
	}

	public void setPartImage(Image partImage) {
		this.partImage = partImage;
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
		// getGraphicalViewer().setRootEditPart(new MainDesignerRootEditPart());
		// if (model != null)
		getGraphicalViewer().setContents(model);
		if (outlinePage != null){
			//The outline for the current editor maybe not available because it was closed 
			//and reopened into another editor. So when we try to set its contents it is 
			//better to check if it was disposed outside and in that case recrated it.
			if (outlinePage.isDisposed()){
				//If the outline is recreated by calling the getOutlineView 
				//then the setContends it is already done so we need to do it only in the else case
				getOutlineView();
			} else outlinePage.setContents(model);
		}
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public INode getModel() {
		return model;
	}
	
	public ISelection getOutlineSelection(){
		if (outlinePage != null && !outlinePage.isDisposed()){
			return outlinePage.getSelection();
		}
		return StructuredSelection.EMPTY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getActionRegistry()
	 */
	@Override
	public ActionRegistry getActionRegistry() {
		return super.getActionRegistry();
	}

	/** The ruler comp. */
	private JDRulerComposite rulerComp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.editor.java2d.J2DGraphicalEditorWithFlyoutPalette#createGraphicalViewer(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected void createGraphicalViewer(Composite parent) {
		rulerComp = new JDRulerComposite(parent, SWT.NONE);
		super.createGraphicalViewer(rulerComp);
		rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		return JDPaletteFactory.createPalette(getIgnorePalleteElements());
	}

	protected abstract List<String> getIgnorePalleteElements();

	// FIXME: something wrong, I should not do that, order in initialisation is
	// wrong

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getGraphicalControl()
	 */
	@Override
	protected Control getGraphicalControl() {
		if (rulerComp != null)
			return rulerComp;
		return super.getGraphicalControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getSelectionSynchronizer()
	 */
	@Override
	public SelectionSynchronizer getSelectionSynchronizer() {
		return super.getSelectionSynchronizer();
	}

	/**
	 * Gets the editor.
	 * 
	 * @return the editor
	 */
	public FigureCanvas getEditor() {
		return (FigureCanvas) getGraphicalViewer().getControl();
	}

	/** The shared key handler. */
	private KeyHandler sharedKeyHandler;

	/**
	 * Gets the common key handler.
	 * 
	 * @return the common key handler
	 */
	public KeyHandler getCommonKeyHandler() {
		if (sharedKeyHandler == null) {
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
					getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		}
		return sharedKeyHandler;
	}

	/**
	 * Creates the additional actions.
	 */
	protected void createAdditionalActions() {
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		// Show Grid Action
		Boolean isGridVisible = jrContext.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWGRID, true);
		Boolean isSnapToGuides = jrContext.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGUIDES, true);
		Boolean isSnapToGrid = jrContext.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGRID, true);
		Boolean isSnapToGeometry = jrContext.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGEOMETRY,
				true);

		int gspaceX = jrContext.getPropertyInteger(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEX, 10);
		int gspaceY = jrContext.getPropertyInteger(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEY, 10);

		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, isSnapToGrid.booleanValue());
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, isGridVisible.booleanValue());
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, new Point(30, 30));
		graphicalViewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gspaceX, gspaceY));
		graphicalViewer.setProperty(SnapToGuidesAction.ID, isSnapToGuides);
		graphicalViewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, isSnapToGeometry.booleanValue());

		IAction showGrid = new ShowGridAction(jrContext);
		getActionRegistry().registerAction(showGrid);

		SnapToGridAction snapGridAction = new SnapToGridAction(jrContext);
		getActionRegistry().registerAction(snapGridAction);

		SizeGridAction sizeGridAction = new SizeGridAction(jrContext);
		getActionRegistry().registerAction(sizeGridAction);

		// snap to geometry
		IAction snapAction = new SnapToGeometryAction(jrContext);
		getActionRegistry().registerAction(snapAction);

		snapAction = new SnapToGuidesAction(jrContext);
		getActionRegistry().registerAction(snapAction);

		// show rullers
		IAction showRulers = new ShowRullersAction(jrContext);
		getActionRegistry().registerAction(showRulers);
		// zoom manager actions
		ZoomManager zoomManager = (ZoomManager) graphicalViewer.getProperty(ZoomManager.class.toString());

		getActionRegistry().registerAction(new ZoomInAction(zoomManager));
		getActionRegistry().registerAction(new ZoomOutAction(zoomManager));
		getActionRegistry().registerAction(new  ZoomActualAction(zoomManager));
		graphicalViewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);

		// set context menu
		graphicalViewer.setContextMenu(new AppContextMenuProvider(graphicalViewer, getActionRegistry()));

		graphicalViewer.setProperty("JRCONTEXT", jrContext);

		LayoutManager.addActions(getActionRegistry(), this, getSelectionActions());

		JaspersoftStudioPlugin.getDecoratorManager().registerActions(getActionRegistry(), getSelectionActions(),
				getGraphicalViewer(), this);
		JaspersoftStudioPlugin.getEditorSettingsManager().registerActions(getActionRegistry(), jrContext);
		

	}

	/**
	 * Force the refresh of the actions enablement and visibility
	 * state
	 */
	public void forceUpdateActions(){
		updateActions(getSelectionActions());
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#selectionChanged(org.eclipse.ui.IWorkbenchPart,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (isSame(part))
			updateActions(getSelectionActions());
	}

	private boolean isSame(IWorkbenchPart part) {
		if (part == getSite().getPart())
			return true;
		if (part instanceof MultiPageEditorPart) {
			Object spage = ((MultiPageEditorPart) part).getSelectedPage();
			if (spage instanceof IWorkbenchPart)
				return isSame((IWorkbenchPart) spage);
		} else if (part instanceof MultiPageToolbarEditorPart) {
			Object spage = ((MultiPageToolbarEditorPart) part).getSelectedPage();
			if (spage instanceof IWorkbenchPart)
				return isSame((IWorkbenchPart) spage);
		}
		if (part instanceof ContentOutline) {
			IContentOutlinePage outPage = (IContentOutlinePage) part.getAdapter(IContentOutlinePage.class);
			if (outPage instanceof MultiOutlineView)
				return isSame(((MultiOutlineView) outPage).getEditor());
			else if (outPage instanceof JDReportOutlineView) {
				JDReportOutlineView coPage = (JDReportOutlineView) outPage;
				return coPage == outlinePage;
			}
			// if (outPage != null)
			// return isSame(outPage);
		}
		return false;
	}

	/** The outline page. */
	protected JDReportOutlineView outlinePage;
	private EditorContributor editorContributor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
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

	protected JDReportOutlineView getOutlineView() {
		if (outlinePage == null || outlinePage.isDisposed()) {
			TreeViewer viewer = new TreeViewer();
			outlinePage = new JDReportOutlineView(this, viewer);
		}
		outlinePage.setContents(getModel());
		return outlinePage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		getEditDomain().getCommandStack().markSaveLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		initializeEditor();
	}

	@Override
	protected FlyoutPreferences getPalettePreferences() {
		// We cache the palette preferences for the open editor
		// Default implementation returns a new FlyoutPreferences object
		// every time the getPalettePreferences method is invoked.
		if (palettePreferences == null) {
			palettePreferences = super.getPalettePreferences();
			// Palette always opened
			palettePreferences.setPaletteState(FlyoutPaletteComposite.STATE_PINNED_OPEN);
		}
		return palettePreferences;
	}

	protected void initializeEditor() {
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		graphicalViewer.addDropTargetListener(new JSSTemplateTransferDropTargetListener(graphicalViewer));
		graphicalViewer.addDropTargetListener(new ReportUnitDropTargetListener(graphicalViewer));
		graphicalViewer.addDropTargetListener(new ImageResourceDropTargetListener(graphicalViewer, ResourceTransfer
				.getInstance()));
		graphicalViewer.addDropTargetListener(new ImageResourceDropTargetListener(graphicalViewer, FileTransfer
				.getInstance()));
		graphicalViewer.addDropTargetListener(new ImageResourceDropTargetListener(graphicalViewer, ImageURLTransfer
				.getInstance()));

		// Load the contributed drop providers for the contributed template styles
		List<TemplateViewProvider> dropProviders = JaspersoftStudioPlugin.getExtensionManager().getStylesViewProvider();
		for (TemplateViewProvider provider : dropProviders) {
			AbstractTransferDropTargetListener listener = provider.getDropListener(graphicalViewer);
			if (listener != null)
				graphicalViewer.addDropTargetListener(listener);
		}

		getEditorSite().getActionBarContributor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getGraphicalViewer()
	 */
	@Override
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#createPalettePage()
	 */
	@Override
	protected CustomPalettePage createPalettePage() {
		return new CustomPalettePage(getPaletteViewerProvider()) {
			@Override
			public void init(IPageSite pageSite) {
				super.init(pageSite);
				IAction copy = getActionRegistry().getAction(ActionFactory.COPY.getId());
				pageSite.getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#createPaletteViewerProvider()
	 */
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {

			@Override
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
				// Uncomment these lines if you want to set as default a palette
				// with column layout and large icons.
				// // TODO: we should replace these default suggestions not using the GEF preference
				// // store explicitly. It would be better override the PaletteViewer creation in order
				// // to have a custom PaletteViewerPreferences (#viewer.getPaletteViewerPreferences()).
				// // This way we could store the preferences in our preference store (maybe the JaspersoftStudio plugin one).
				// // For now we'll stay with this solution avoiding the user to lose previous saved preferences
				// // regarding the palette.
				// InternalGEFPlugin.getDefault().getPreferenceStore().setDefault(
				// PaletteViewerPreferences.PREFERENCE_LAYOUT, PaletteViewerPreferences.LAYOUT_COLUMNS);
				// InternalGEFPlugin.getDefault().getPreferenceStore().setDefault(
				// PaletteViewerPreferences.PREFERENCE_COLUMNS_ICON_SIZE,true);
			}

			@Override
			protected void hookPaletteViewer(PaletteViewer viewer) {
				super.hookPaletteViewer(viewer);
				final CopyTemplateAction copy = new CopyTemplateAction(AbstractVisualEditor.this);
				if (copy != null) {
					viewer.addSelectionChangedListener(copy);
				}
			}
		};
	}
	
	/**
	 * Return the selection cache extracting it from the current jr context
	 */
	public CommonSelectionCacheProvider getSelectionCache(){
		return  (CommonSelectionCacheProvider)jrContext.get(ReportContainer.SELECTION_CACHE_KEY);
	}

	/**
	 * Create the contextual action to add stuff to the datasets (fields, variables) and to create styles.
	 */
	protected void createDatasetAndStyleActions(ActionRegistry registry) {
		List<String> selectionActions = getSelectionActions();

		IAction action = new CreateFieldAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateFieldAction.ID);

		action = new CreateSortFieldAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateSortFieldAction.ID);

		action = new CreateVariableAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateVariableAction.ID);

		action = new CreateScriptletAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateScriptletAction.ID);

		action = new CreateParameterAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateParameterAction.ID);
		
		action = new CreateParameterSetAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateParameterSetAction.ID);

		action = new CreateGroupAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateGroupAction.ID);

		action = new CreateDatasetAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateDatasetAction.ID);

		action = new CreateStyleAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateStyleAction.ID);

		action = new CreateConditionalStyleAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateConditionalStyleAction.ID);

		action = new ExportStyleAsTemplateAction(this);
		registry.registerAction(action);
		selectionActions.add(ExportStyleAsTemplateAction.ID);

		action = new ResetStyleAction(this);
		registry.registerAction(action);
		selectionActions.add(ResetStyleAction.ID);

		action = new CreateStyleTemplateAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateStyleTemplateAction.ID);

		action = new RefreshTemplateStyleExpression(this);
		registry.registerAction(action);
		selectionActions.add(RefreshTemplateStyleExpression.ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();

		ActionRegistry registry = getActionRegistry();
		IAction action = new CutAction(this);
		registry.registerAction(action);
		List<String> selectionActions = getSelectionActions();
		selectionActions.add(action.getId());

		// Create the custom delete action that aggregate all the messages when more elements are deleted
		// the old default action is replaced
		CustomDeleteAction deleteAction = new CustomDeleteAction(this);
		registry.registerAction(deleteAction);

		
		action = new HideElementsAction(this,true);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		action = new HideElementsAction(this,false);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		
		action = new CopyAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new PasteAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		action = new CopyFormatAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new PasteFormatAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new SetDefaultsAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		
		action = new MatchWidthAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new MatchHeightAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		// create actions
		createEditorActions(registry);

		// ------------
		action = new DirectEditAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// ------------
		action = new BringForwardAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new BringToFrontAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new BringToBackAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new BringBackwardAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// --Create image change path action --
		action = new ChangeImageExpression(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// --Create exporter properties action --
		action = new AddExporterPropertyAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// ------------
		action = new Align2Element(this.getSite().getPart(), PositionConstants.LEFT);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2Element(this.getSite().getPart(), PositionConstants.RIGHT);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2Element(this.getSite().getPart(), PositionConstants.TOP);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2Element(this.getSite().getPart(), PositionConstants.BOTTOM);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2Element(this.getSite().getPart(), PositionConstants.CENTER);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2Element(this.getSite().getPart(), PositionConstants.MIDDLE);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// ------------
		action = new Align2BorderAction(this, PositionConstants.LEFT);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2BorderAction(this, PositionConstants.RIGHT);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2BorderAction(this, PositionConstants.TOP);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2BorderAction(this, PositionConstants.BOTTOM);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2BorderAction(this, PositionConstants.CENTER);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Align2BorderAction(this, PositionConstants.MIDDLE);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new CenterInParentAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// ---------------------

		action = new MatchSizeAction(this, MatchSizeAction.TYPE.WIDTH);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new MatchSizeAction(this, MatchSizeAction.TYPE.HEIGHT);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new MatchSizeAction(this, MatchSizeAction.TYPE.BOTH);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new SameHeightMaxAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new SameHeightMinAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new SameWidthMaxAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new SameWidthMinAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// Horizontal Spacing Actions

		action = new IncreaseHSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new DecreaseHSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new RemoveHSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new EqualsHSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// Vertical Spacing Actions

		action = new IncreaseVSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new DecreaseVSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new RemoveVSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new EqualsVSpaceAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// Join Spacing Actions

		action = new JoinRightAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new JoinLeftAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// ---------------------

		action = new Size2BorderAction(this, Size2BorderAction.WIDTH);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Size2BorderAction(this, Size2BorderAction.HEIGHT);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new Size2BorderAction(this, Size2BorderAction.BOTH);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new MaximizeContainerAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new OrganizeAsTableAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new StretchToContentAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// ------------------

		action = new ShowPropertyViewAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new BoldAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new ItalicAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new UnderlineAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new StrikethroughAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new CreatePinAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// Start of the convert action
		action = new ConvertTextIntoStatic(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new ConvertStaticIntoText(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());
		// End of the convert actions

		// Move group and detail actions
		action = new MoveGroupUpAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new MoveGroupDownAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new MoveDetailUpAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new MoveDetailDownAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new ConnectToDomainAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		// Action to open a subreport into the editor
		action = new OpenEditorAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}

	protected void createEditorActions(ActionRegistry registry) {

	}

	/**
	 * Contributes items to the specified toolbar that is supposed to be put on the top right of the current visual editor
	 * (i.e: ReportEditor, CrosstabEditor, TableEditor, ListEditor).
	 * <p>
	 * 
	 * Default behavior contributes the following items:
	 * <ul>
	 * <li>Zoom In</li>
	 * <li>Zoom Out</li>
	 * <li>Zoom Combo</li>
	 * <li>Global "View" settings drop down menu</li>
	 * </ul>
	 * 
	 * Sub-classes may want to override this method to modify the toolbar.
	 * 
	 * @param toolbarManager
	 *          the toolbar manager to be enriched
	 */
	public void contributeItemsToEditorTopToolbar(IToolBarManager toolbarManager) {
		toolbarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		toolbarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
		RZoomComboContributionItem zoomItem = new RZoomComboContributionItem(getEditorSite().getPage());
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		ZoomManager property = (ZoomManager) graphicalViewer.getProperty(ZoomManager.class.toString());
		if (property != null)
			zoomItem.setZoomManager(property);
		zoomItem.setEnabled(true);
		toolbarManager.add(zoomItem);
		toolbarManager.add(new Separator());
		// Global "View" menu items
		toolbarManager.add(new ViewSettingsDropDownAction(getActionRegistry()));
	}
	
	/**
	 * Return the main element managed by this editor, page and root are excluded
	 */
	public INode getManagedElement(){
		INode node = model;
		while(node != null && !node.getChildren().isEmpty() && (node instanceof MRoot || node instanceof MPage)){
			node = node.getChildren().get(node.getChildren().size()-1);
		}
		return node;
	}
}
