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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.action.IGlobalAction;
import com.jaspersoft.studio.editor.action.snap.SizeGridAction;
import com.jaspersoft.studio.editor.action.snap.SnapToGridAction;
import com.jaspersoft.studio.editor.action.snap.SnapToGuidesAction;
import com.jaspersoft.studio.editor.gef.ui.actions.RZoomComboContributionItem;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.report.ReportContainer;
import com.jaspersoft.studio.editor.report.SelectionChangedListener;
import com.jaspersoft.studio.editor.xml.XMLEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.toolbars.CommonToolbarHandler;

/*
 * Manages the installation/deinstallation of global actions for multi-page editors. Responsible for the redirection of
 * global actions to the active editor. Multi-page contributor replaces the contributors for the individual editors in
 * the multi-page editor.
 * 
 * @author Chicu Veaceslav
 */
public class JrxmlEditorContributor extends MultiPageEditorActionBarContributor {

	/** The global action keys. */
	private List<String> globalActionKeys = new ArrayList<String>();

	/** The retarget actions. */
	private List<RetargetAction> retargetActions = new ArrayList<RetargetAction>();

	/** The registry. */
	private ActionRegistry registry = new ActionRegistry();

	/** The zoom combo. */
	private RZoomComboContributionItem zoomCombo;
	
	/**
	 * The selection listener that will update the toolbar when a selection change
	 */
	private SelectionChangedListener selectionListener;

	/**
	 * Creates a multi-page contributor.
	 */
	public JrxmlEditorContributor() {
		super();
	}

	/**
	 * Initialization.
	 * 
	 * @param bars
	 *          the bars
	 */
	public void init(IActionBars bars) {
		buildActions(bars);
		declareGlobalActionKeys();
		super.init(bars);
		getPage().addPartListener(partListener);
	}

	private PartListener partListener = new PartListener();

	/**
	 * Builds the actions.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions(IActionBars bars) {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		// addRetargetAction(new PrintAction());

		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());

		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY,
				Messages.JrxmlEditorContributor_show_ruler, IAction.AS_CHECK_BOX));
		addRetargetAction(new RetargetAction(SnapToGuidesAction.ID, Messages.common_snap_to_guides, IAction.AS_CHECK_BOX));

		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, Messages.common_show_grid,
				IAction.AS_CHECK_BOX));
		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY, Messages.common_snap_to_geometry,
				IAction.AS_CHECK_BOX));
		addRetargetAction(new RetargetAction(SnapToGridAction.ID, Messages.common_snap_to_grid, IAction.AS_CHECK_BOX));
		addRetargetAction(new RetargetAction(SizeGridAction.ID, Messages.JrxmlEditorContributor_grid_size));

		List<RetargetAction> ractions = JaspersoftStudioPlugin.getDecoratorManager().buildMenuActions();
		for (RetargetAction r : ractions)
			addRetargetAction(r);
	}

	/**
	 * Adds the retarded actions.
	 * 
	 * @param action
	 *          The action to add
	 */
	public void addRetargetAction(RetargetAction action) {
		addAction(action);
		retargetActions.add(action);
		getPage().addPartListener(action);
		addGlobalActionKey(action.getId());
	}

	private List<String> glRetargetAction = new ArrayList<String>();

	protected void addGlobaRetargetAction(RetargetAction action) {
		addRetargetAction(action);
		glRetargetAction.add(action.getId());
	}

	/**
	 * Adds global action key.
	 * 
	 * @param key
	 *          The key to add
	 */
	protected void addGlobalActionKey(String key) {
		globalActionKeys.add(key);
	}

	/**
	 * Adds to action registry an action.
	 * 
	 * @param action
	 *          The action to add
	 */
	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}

	/**
	 * Gets the registry.
	 * 
	 * @return ActionRegistry The registry
	 */
	protected ActionRegistry getActionRegistry() {
		return registry;
	}

	/**
	 * Declares the global action keys.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.PRINT.getId());
		addGlobalActionKey(ActionFactory.CUT.getId());
		addGlobalActionKey(ActionFactory.COPY.getId());
		addGlobalActionKey(ActionFactory.PASTE.getId());
		addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
		addGlobalActionKey(ActionFactory.DELETE.getId());
	}

	/**
	 * Gets the action.
	 * 
	 * @param id
	 *          the id
	 * @return the action
	 */
	protected IAction getAction(String id) {
		if (getActionRegistry() != null)
			return getActionRegistry().getAction(id);
		return null;
	}

	private IEditorPart lastEditor;

	/**
	 * Sets the page to active status.
	 * 
	 * @param activeEditor
	 *          The active editor
	 */
	public void setActivePage(IEditorPart activeEditor) {
		if (lastEditor instanceof CachedSelectionProvider && selectionListener != null) {
			((CachedSelectionProvider)lastEditor).getSelectionCache().removeSelectionChangeListener(selectionListener);
		}
		IActionBars bars = getActionBars();
		removeZoom(bars.getToolBarManager());
		bars.clearGlobalActionHandlers();
		lastEditor = activeEditor;
		addGlobal(bars);
		if (activeEditor instanceof XMLEditor) {
			if (textEditorContributor == null) {
				textEditorContributor = new TextEditorActionContributor();
				textEditorContributor.init(bars, activeEditor.getSite().getPage());
			}
			textEditorContributor.setActiveEditor(activeEditor);
		} else if (activeEditor instanceof ReportContainer || activeEditor instanceof AbstractVisualEditor) {
			// NO LONGER AVAILABLE IN GLOBAL TOOLBAR SINCE
			// THEY WILL BE VISIBLE IN THE ReportContainer toolbar.
			// addZoom(bars.getToolBarManager());
			if (activeEditor instanceof AbstractVisualEditor) {
				GraphicalViewer graphicalViewer = ((AbstractVisualEditor) activeEditor).getGraphicalViewer();
				ZoomManager property = (ZoomManager) graphicalViewer.getProperty(ZoomManager.class.toString());
				if (property != null)
					zoomCombo.setZoomManager(property);
			}
			ActionRegistry registry = (ActionRegistry) activeEditor.getAdapter(ActionRegistry.class);
			if (registry != null) {
				for (String id : globalActionKeys)
					bars.setGlobalActionHandler(id, registry.getAction(id));
				for (Iterator<IAction> it = registry.getActions(); it.hasNext();) {
					IAction action = it.next();
					if (action instanceof IGlobalAction)
						bars.setGlobalActionHandler(action.getId(), action);
				}
			}
			((CachedSelectionProvider)lastEditor).getSelectionCache().addSelectionChangeListener(getSelectionChangeListener(registry));
		} else if (activeEditor instanceof PreviewJRPrint) {
			ActionRegistry registry = (ActionRegistry) activeEditor.getAdapter(ActionRegistry.class);
			if (registry != null) {
				for (String id : globalActionKeys)
					bars.setGlobalActionHandler(id, registry.getAction(id));
				for (Iterator<IAction> it = registry.getActions(); it.hasNext();) {
					IAction action = it.next();
					if (action instanceof IGlobalAction)
						bars.setGlobalActionHandler(action.getId(), action);
				}
			}
		}
		bars.updateActionBars();
	}

	public IEditorPart getLastEditor() {
		return lastEditor;
	}

	/**
	 * Return a selection listener that will request the update of the toolbar
	 * when the selection changes
	 */
	private SelectionChangedListener getSelectionChangeListener(ActionRegistry registry) {
		if (selectionListener == null){
			selectionListener = new SelectionChangedListener() {	
				@Override
				public void selectionChanged() {
					UIUtils.getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							if (getPage() != null && getPage().getActiveEditor() != null){
								CommonToolbarHandler.updateSelection(getPage().getActiveEditor(), getActionBars());
							}
						}
					});
				}
			};
		}
		return selectionListener;
	}

	private final class PartListener implements IPartListener2 {
		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			IEditorPart activeEditor = partRef.getPage().getActiveEditor();
			if (activeEditor instanceof JrxmlEditor) {
				activeEditor = ((JrxmlEditor) activeEditor).getActiveEditor();
			}
			setActivePage(activeEditor);
		}

		@Override
		public void partBroughtToTop(IWorkbenchPartReference partRef) {;
		}

		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			if (lastEditor instanceof CachedSelectionProvider && selectionListener != null) {
				((CachedSelectionProvider)lastEditor).getSelectionCache().selectionChanged(StructuredSelection.EMPTY);
			}
		}

		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partOpened(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partHidden(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partVisible(IWorkbenchPartReference partRef) {
		}

		@Override
		public void partInputChanged(IWorkbenchPartReference partRef) {
		}
	}

	/**
	 * Returns the action registed with the given text editor.
	 * 
	 * @param editor
	 *          the editor
	 * @param actionID
	 *          the action id
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(ITextEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}

	/**
	 * Adds the undo and redo items to the toolbar.
	 * 
	 * @param tbm
	 *          The IToolBarManager
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager tbm) {
		tbm.add(getAction(ActionFactory.UNDO.getId()));
		tbm.add(getAction(ActionFactory.REDO.getId()));
		tbm.add(new Separator());

		addZoom(tbm);
	}

	private void addZoom(IToolBarManager tbm) {
		tbm.add(getAction(GEFActionConstants.ZOOM_IN));
		tbm.add(getAction(GEFActionConstants.ZOOM_OUT));
		if (zoomCombo == null)
			zoomCombo = new RZoomComboContributionItem(getPage());
		zoomCombo.setEnabled(true);
		tbm.add(zoomCombo);
		tbm.update(true);
	}

	private void removeZoom(IToolBarManager tbm) {
		tbm.remove(GEFActionConstants.ZOOM_IN);
		tbm.remove(GEFActionConstants.ZOOM_OUT);
		tbm.remove(zoomCombo.getId());
		zoomCombo.setEnabled(false);
		tbm.update(true);
	}

	private void addGlobal(IActionBars bars) {
		IToolBarManager tbm = bars.getToolBarManager();
		for (String s : glRetargetAction) {
			tbm.remove(s);
			// bars.setGlobalActionHandler(s, getAction(s));
			tbm.add(getAction(s));
		}
		tbm.update(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(org.eclipse.jface.action.IMenuManager)
	 */
	public void contributeToMenu(IMenuManager manager) {
		super.contributeToMenu(manager);

		MenuManager viewMenu = new MenuManager(Messages.JrxmlEditorContributor_view);
		viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
		viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
		viewMenu.add(new Separator());
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
		viewMenu.add(getAction(SnapToGuidesAction.ID));
		viewMenu.add(new Separator());
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		viewMenu.add(getAction(SnapToGridAction.ID));
		viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
		viewMenu.add(getAction(SizeGridAction.ID));

		JaspersoftStudioPlugin.getDecoratorManager().contribute2Menu(getActionRegistry(), viewMenu);

		manager.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
	}

	private TextEditorActionContributor textEditorContributor = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.EditorActionBarContributor#contributeToStatusLine(org.eclipse.jface.action.IStatusLineManager)
	 */
	@Override
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		super.contributeToStatusLine(statusLineManager);
		if (textEditorContributor != null)
			textEditorContributor.contributeToStatusLine(statusLineManager);

		statusLineManager.setMessage(""); //$NON-NLS-1$
	}

	/**
	 * Disposes the contributor. Removes all {@link RetargetAction}s that were {@link org.eclipse.ui.IPartListener}s on
	 * the {@link org.eclipse.ui.IWorkbenchPage} and disposes them. Also disposes the action registry.
	 * <P>
	 * Subclasses may extend this method to perform additional cleanup.
	 * 
	 * @see org.eclipse.ui.part.EditorActionBarContributor#dispose()
	 */
	public void dispose() {
		getPage().removePartListener(partListener);
		for (RetargetAction action : retargetActions) {
			getPage().removePartListener(action);
			action.dispose();
		}
		registry.dispose();
		retargetActions = null;
		registry = null;
	}

}
