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
package com.jaspersoft.studio.editor.outline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ResourceTransfer;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.IGraphicalEditor;
import com.jaspersoft.studio.editor.dnd.ImageResourceDropTargetListener;
import com.jaspersoft.studio.editor.dnd.ImageURLTransfer;
import com.jaspersoft.studio.editor.dnd.JSSTemplateTransferDropTargetListener;
import com.jaspersoft.studio.editor.gef.parts.EditableFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.MainDesignerRootEditPart;
import com.jaspersoft.studio.editor.java2d.J2DLightweightSystem;
import com.jaspersoft.studio.editor.java2d.figure.JSSScrollableThumbnail;
import com.jaspersoft.studio.editor.menu.AppContextMenuProvider;
import com.jaspersoft.studio.editor.outline.part.TreeEditPart;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.editor.report.EditorContributor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IDragable;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.utils.Misc;

/*
 * The Class JDReportOutlineView.
 */
public class JDReportOutlineView extends ContentOutlinePage implements IAdaptable {

	/** The editor. */
	protected IGraphicalEditor editor;

	/** The page book. */
	private PageBook pageBook;

	/** The outline. */
	private Control outline;

	/** The overview. */
	private Canvas overview;

	/** The show overview action. */
	private IAction showOutlineAction, showOverviewAction;

	/** The Constant ID_OUTLINE. */
	public static final String ID_ACTION_OUTLINE = "showOutlineAction";

	/** The Constant ID_OVERVIEW. */
	public static final String ID_ACTION_OVERVIEW = "showOverviewAction";

	/** The thumbnail. */
	private JSSScrollableThumbnail thumbnail;

	/** The dispose listener. */
	private DisposeListener disposeListener;

	private Point mousePosition = new Point(-1, -1);

	/**
	 * On linux the click event on the arrow to expand a tree node is not catched if the tree element hasen't the focus.
	 * So we need a trick to have here the same behavior of the others operative systems
	 */
	private boolean enableFocusFix = Util.isLinux();

	/**
	 * Instantiates a new jD report outline view.
	 * 
	 * @param editor
	 *          the editor
	 * @param viewer
	 *          the viewer
	 */
	public JDReportOutlineView(IGraphicalEditor editor, EditPartViewer viewer) {
		super(viewer);
		this.editor = editor;
	}

	public IGraphicalEditor getEditor() {
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.Page#init(org.eclipse.ui.part.IPageSite)
	 */
	@Override
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		ActionRegistry registry = editor.getActionRegistry();
		IActionBars bars = pageSite.getActionBars();
		for (Iterator<IAction> it = registry.getActions(); it.hasNext();) {
			IAction ia = it.next();
			bars.setGlobalActionHandler(ia.getId(), ia);
		}

		bars.updateActionBars();
	}

	protected void initActions(ActionRegistry registry, IActionBars bars) {

	}

	protected ContextMenuProvider getMenuContentProvider() {
		return new AppContextMenuProvider(getViewer(), editor.getActionRegistry());
	}
	
	/**
	 * Check if the outline page was closed. When the outline page is closed 
	 * the control inside the viewer is disposed and removed (so this method 
	 * will always catch the getViewer().getControl() == null when the outline 
	 * was closed).
	 * 
	 * @return true if the outline was closed and its control disposed. False otherwise
	 */
	public boolean isDisposed(){
		return (getViewer() == null || getViewer().getControl() == null 
					|| getViewer().getControl().isDisposed());
	}

	/**
	 * Configure outline viewer.
	 */
	protected void configureOutlineViewer() {
		final EditPartViewer viewer = getViewer();
		viewer.setEditDomain(editor.getEditDomain());
		viewer.setEditPartFactory(getEditPartFactory());
		ContextMenuProvider provider = getMenuContentProvider();
		viewer.setContextMenu(provider);

		viewer.addDropTargetListener(new JSSTemplateTransferDropTargetListener(viewer));
		viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer) {
			@Override
			protected Object getTemplate() {
				List<Object> models = new ArrayList<Object>();
				Object obj = super.getTemplate();
				if (obj == null) {
					List<?> selection = getViewer().getSelectedEditParts();
					for (Object it : selection) {
						if (it instanceof EditPart) {
							Object model = ((EditPart) it).getModel();
							if (model instanceof IDragable) {
								models.add(model);
							}
							if (model instanceof MBand){
								BandTypeEnum bandType =((MBand)model).getBandType();
								if (BandTypeEnum.DETAIL.equals(bandType) || BandTypeEnum.GROUP_FOOTER.equals(bandType) || BandTypeEnum.GROUP_HEADER.equals(bandType)){
									models.add(model);
								}
							}
						}
					}
				}
				return models;
			}
		});
		// Add images drop listeners
		viewer.addDropTargetListener(new ImageResourceDropTargetListener(viewer, ResourceTransfer.getInstance()));
		viewer.addDropTargetListener(new ImageResourceDropTargetListener(viewer, FileTransfer.getInstance()));
		viewer.addDropTargetListener(new ImageResourceDropTargetListener(viewer, ImageURLTransfer.getInstance()));

		IPageSite site = getSite();
		site.registerContextMenu(provider.getId(), provider, site.getSelectionProvider());

		IToolBarManager tbm = site.getActionBars().getToolBarManager();
		registerToolbarAction(tbm);
		
		showPage(ID_ACTION_OUTLINE);
	}
	
	/**
	 * Create on the table manger the toolbar actions for the outline. The actions are created 
	 * only if the toolbar manager dosen't contains them already. Actually the added action are 
	 * the one the show the standard outline and the one to show the thumbnail of the report.
	 * 
	 * @param tbm the toolbar manager for the outline.
	 */
	public void registerToolbarAction(IToolBarManager tbm){
		IContributionItem items[] = tbm.getItems();
		HashSet<String> existingItems = new HashSet<String>();
		for(IContributionItem item : items){
			existingItems.add(item.getId());
		}
		
		showOutlineAction = new Action(){
			@Override
			public void run() {
				showPage(ID_ACTION_OUTLINE);
			}
		};
		showOutlineAction.setId(ID_ACTION_OUTLINE);
		showOutlineAction.setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/outline.gif")); //$NON-NLS-1$
		showOutlineAction.setToolTipText(Messages.JDReportOutlineView_show_outline_tool_tip);
		if (!existingItems.contains(ID_ACTION_OUTLINE)){
			ActionContributionItem showOutlineItem = new ActionContributionItem(showOutlineAction);
			showOutlineItem.setVisible(true);
			tbm.add(showOutlineItem);
		}
		
		showOverviewAction = new Action() {
			@Override
			public void run() {
				showPage(ID_ACTION_OVERVIEW);
			}
		};
		showOverviewAction.setId(ID_ACTION_OVERVIEW);
		showOverviewAction.setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/overview.gif")); //$NON-NLS-1$
		showOverviewAction.setToolTipText(Messages.JDReportOutlineView_show_overview_tool_tip);
		if (!existingItems.contains(ID_ACTION_OVERVIEW)){
			ActionContributionItem showOverviewItem = new ActionContributionItem(showOverviewAction);
			showOverviewItem.setVisible(true);
			tbm.add(showOverviewItem);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.ContentOutlinePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		pageBook = new PageBook(parent, SWT.NONE);

		outline = getViewer().createControl(pageBook);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(outline, "com.jaspersoft.studio.doc.view_outline");

		overview = new Canvas(pageBook, SWT.NONE);
		pageBook.showPage(outline);
		configureOutlineViewer();
		hookOutlineViewer();
		setContents(editor.getModel());
		if (outline instanceof Tree) {
			final Tree tree = (Tree) outline;

			tree.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					mousePosition.setLocation(-1, -1);
				}

				@Override
				public void focusGained(FocusEvent e) {
					if (enableFocusFix && mousePosition.x != -1) {
						EditPart part = getViewer().findObjectAt(mousePosition);
						if (part != null && part.getModel() instanceof MRoot) {
							EditPart translatedPart = getViewer().findObjectAt(new Point(mousePosition.x + 10, mousePosition.y));
							if (translatedPart != null && translatedPart.getModel() != part.getModel()) {
								TreeItem item = (TreeItem) ((TreeEditPart) translatedPart).getWidget();
								item.setExpanded(!item.getExpanded());
								tree.deselectAll();
								tree.select(item);
								tree.layout(true);

							}
						}
					}
				}
			});

			tree.addMouseListener(new MouseListener() {

				public void mouseUp(MouseEvent e) {
				}

				public void mouseDown(MouseEvent e) {

				}

				public void mouseDoubleClick(MouseEvent e) {
					if (e.getSource() instanceof Tree) {
						Tree t = (Tree) e.getSource();
						TreeItem[] ti = t.getSelection();
						if (ti != null && ti.length > 0) {
							Object obj = ti[0].getData();
							if (obj instanceof TreeEditPart && editor instanceof AbstractVisualEditor) {

								EditPart part = (EditPart) ((AbstractVisualEditor) editor).getGraphicalViewer().getEditPartRegistry()
										.get(((TreeEditPart) obj).getModel());
								if (part != null) {
									SelectionRequest request = new SelectionRequest();
									request.setType(RequestConstants.REQ_OPEN);
									part.performRequest(request);
								} else {
									TreeEditPart atep = (TreeEditPart) obj;
									if (atep.getModel() instanceof ANode) {
										EditableFigureEditPart.openEditor(((ANode) atep.getModel()).getValue(), (IEditorPart) editor,
												(ANode) atep.getModel());
									}
								}
							}
						}
					}
				}
			});

			// This listener display the tooltip text for the abbreviated nodes names
			tree.addMouseMoveListener(new MouseMoveListener() {

				public void mouseMove(MouseEvent e) {
					mousePosition.setLocation(e.x, e.y);
					EditPart part = getViewer().findObjectAt(new Point(e.x, e.y));
					Tree t = (Tree) e.getSource();
					if (part != null && part.getModel() != null && !(part.getModel() instanceof MRoot)) {
						Object model = part.getModel();
						String toolTipText = Misc.nvl(((ANode) model).getToolTip());
						String displayText = Misc.nvl(((ANode) model).getDisplayText());
						String text = "";
						if (!toolTipText.isEmpty() && !toolTipText.equals(displayText))
							text = toolTipText + "\n";
						text += displayText;
						t.setToolTipText(text);
						return;
					}
					t.setToolTipText(null);
				}

			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.Page#dispose()
	 */
	@Override
	public void dispose() {
		unhookOutlineViewer();
		if (thumbnail != null)
			thumbnail = null;
		super.dispose();
	}

	private EditorContributor editorContributor;

	private EditPartFactory editPartFactory;

	public EditPartFactory getEditPartFactory() {
		if (editPartFactory == null)
			editPartFactory = new OutlineTreeEditPartFactory();
		return editPartFactory;
	}

	public void setEditPartFactory(EditPartFactory editPartFactory) {
		this.editPartFactory = editPartFactory;
		getViewer().setEditPartFactory(getEditPartFactory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
		if (type == ZoomManager.class)
			return editor.getGraphicalViewer().getProperty(ZoomManager.class.toString());
		if (type == EditorContributor.class) {
			if (editorContributor == null)
				editorContributor = new EditorContributor(editor.getEditDomain());
			return editorContributor;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.ContentOutlinePage#getControl()
	 */
	@Override
	public Control getControl() {
		return pageBook;
	}

	/**
	 * Hook outline viewer.
	 */
	protected void hookOutlineViewer() {
		editor.getSelectionSynchronizer().addViewer(getViewer());
	}

	/**
	 * Initialize overview.
	 */
	protected void initializeOverview() {
		LightweightSystem lws = new J2DLightweightSystem(overview);

		RootEditPart rep = editor.getGraphicalViewer().getRootEditPart();
		if (rep instanceof MainDesignerRootEditPart) {
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
			thumbnail = new JSSScrollableThumbnail((Viewport) root.getFigure(), (MRoot)getViewer().getContents().getModel());
			thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);
			disposeListener = new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;
					}
				}
			};
			editor.getEditor().addDisposeListener(disposeListener);
		}
		lws.setControl(overview);
	}

	/**
	 * Sets the contents.
	 * 
	 * @param contents
	 *          the new contents
	 */
	public void setContents(Object contents) {
		if (getViewer().getEditPartFactory() != null){
			getViewer().setContents(contents);
		}
		if (outline instanceof Tree) {
			Tree tree = (Tree) outline;
			if (!tree.isDisposed() && tree.getItems() != null && tree.getItems().length > 0)
				tree.getItem(0).setExpanded(true);
		}
	}

	/**
	 * Show page.
	 * 
	 * @param id
	 *          the id
	 */
	protected void showPage(String id) {
		if (ID_ACTION_OUTLINE.equals(id)) {
			showOutlineAction.setChecked(true);
			showOverviewAction.setChecked(false);
			pageBook.showPage(outline);
			if (thumbnail != null)
				thumbnail.setVisible(false);
		} else if (ID_ACTION_OVERVIEW.equals(id)) {
			if (thumbnail == null)
				initializeOverview();
			showOutlineAction.setChecked(false);
			showOverviewAction.setChecked(true);
			pageBook.showPage(overview);
			thumbnail.setVisible(true);
		}
	}

	/**
	 * Unhook outline viewer.
	 */
	protected void unhookOutlineViewer() {
		editor.getSelectionSynchronizer().removeViewer(getViewer());
		FigureCanvas editor2 = editor.getEditor();
		if (disposeListener != null && editor2 != null && !editor2.isDisposed())
			editor2.removeDisposeListener(disposeListener);
	}

	public void setTreeSelection(ISelection s) {
		if (s != null && s instanceof StructuredSelection && outline instanceof Tree) {
			StructuredSelection sel = (StructuredSelection) s;
			List<?> sobj = sel.toList();
			List<TreeItem> toSelect = new ArrayList<TreeItem>();
			Tree tree = (Tree) outline;
			tree.getItemCount();
			checkItems(tree.getItems(), toSelect, sobj);
			if (!toSelect.isEmpty())
				tree.setSelection(toSelect.toArray(new TreeItem[toSelect.size()]));
		} else
			setSelection(s);
	}

	public void checkItems(TreeItem[] items, List<TreeItem> toSelect, List<?> sobj) {
		if (items == null)
			return;
		for (TreeItem ti : items) {
			for (Object obj : sobj) {
				if (obj != null && ti.getData() != null) {
					if (obj == ti.getData())
						toSelect.add(ti);
					else if (obj instanceof EditPart && ti.getData() instanceof EditPart) {
						if (((EditPart) obj).getModel() == ((EditPart) ti.getData()).getModel())
							toSelect.add(ti);
					}
				}
			}
			checkItems(ti.getItems(), toSelect, sobj);
		}
	}
}
