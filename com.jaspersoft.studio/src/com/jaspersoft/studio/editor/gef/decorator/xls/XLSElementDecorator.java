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
package com.jaspersoft.studio.editor.gef.decorator.xls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.RetargetAction;

import com.jaspersoft.studio.editor.action.xls.XLSAction;
import com.jaspersoft.studio.editor.action.xls.XLSActionList;
import com.jaspersoft.studio.editor.gef.decorator.text.TextDecorator;
import com.jaspersoft.studio.editor.gef.decorator.text.TextElementDecorator;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * Define the action related to the XLS export, it extends a TextElementDecorator to print the textual tag on the
 * elements
 * 
 * @author Orlandin Marco
 * 
 */
public class XLSElementDecorator extends TextElementDecorator {

	/**
	 * The XSL contributor for the text decoration
	 */
	private XLSDecorator decorator = new XLSDecorator();

	private List<String> actionIDs;

	/**
	 * Add or remove the XSL contributor from the text element decorator
	 */
	@Override
	public void setupFigure(ComponentFigure fig, FigureEditPart editPart) {
		super.setupFigure(fig, editPart);
		TextDecorator dec = getDecorator();
		dec.removeDecorator(decorator);
		if (editPart.getjConfig().getPropertyBooleanDef(ShowXLSTagsAction.ID, false))
			dec.addDecorator(decorator);
	}

	private void registerFit(ActionRegistry registry, IWorkbenchPart part, List<String> selectionActions) {
		IAction action = new XLSAction(part, XLSAction.FIT_ROW_ID, "true", Messages.XLSElementDecorator_fitRowAction); //$NON-NLS-1$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.FIT_COL_ID, "true", Messages.XLSElementDecorator_fitColAction); //$NON-NLS-1$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSActionList(part, "XLS_Fit_None", new String[] { XLSAction.FIT_ROW_ID, XLSAction.FIT_COL_ID }, //$NON-NLS-1$
				new String[] { null, null }, Messages.XLSElementDecorator_nonAction);
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}

	private void registerAutoFilter(ActionRegistry registry, IWorkbenchPart part, List<String> selectionActions) {
		IAction action = new XLSAction(part, XLSAction.AUTOFILTER_ID.concat(XLSDecorator.START), XLSAction.AUTOFILTER_ID, XLSDecorator.START, //$NON-NLS-1$ //$NON-NLS-2$
				Messages.XLSElementDecorator_startAction);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.AUTOFILTER_ID.concat(XLSDecorator.END), XLSAction.AUTOFILTER_ID, XLSDecorator.END, Messages.XLSElementDecorator_endAction); //$NON-NLS-1$ //$NON-NLS-2$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.AUTOFILTER_ID.concat("none"), XLSAction.AUTOFILTER_ID, null, Messages.XLSElementDecorator_nonAction); //$NON-NLS-1$ //$NON-NLS-2$
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}

	private void registerBreak(ActionRegistry registry, IWorkbenchPart part, List<String> selectionActions) {
		IAction action = new XLSAction(part, XLSAction.BREAK_AFTER_ROW_ID, XLSAction.BREAK_AFTER_ROW_ID, "true", //$NON-NLS-1$
				Messages.XLSElementDecorator_breakAfterAction, new String[] { XLSAction.BREAK_BEFORE_ROW_ID });
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.BREAK_BEFORE_ROW_ID, XLSAction.BREAK_BEFORE_ROW_ID, "true", //$NON-NLS-1$
				Messages.XLSElementDecorator_breakBeforeAction, new String[] { XLSAction.BREAK_AFTER_ROW_ID });
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSActionList(part, "XSL_Break_None", new String[] { XLSAction.BREAK_AFTER_ROW_ID, //$NON-NLS-1$
				XLSAction.BREAK_BEFORE_ROW_ID }, new String[] { null, null }, Messages.XLSElementDecorator_nonAction); //$NON-NLS-1$
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}

	private void registerCellProperties(ActionRegistry registry, IWorkbenchPart part, List<String> selectionActions) {
		IAction action = new XLSAction(part, XLSAction.CELL_HIDDEN_ID, XLSAction.CELL_HIDDEN_ID, "true", Messages.XLSElementDecorator_hiddenCellAction); //$NON-NLS-1$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.CELL_LOCKED_ID, XLSAction.CELL_LOCKED_ID, "true", Messages.XLSElementDecorator_lockCellAction); //$NON-NLS-1$

		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSActionList(part, "XSL_Cell_None", //$NON-NLS-1$
				new String[] { XLSAction.CELL_HIDDEN_ID, XLSAction.CELL_LOCKED_ID }, new String[] { null, null }, Messages.XLSElementDecorator_nonAction); //$NON-NLS-1$
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}

	private void registerFreezeProperties(ActionRegistry registry, IWorkbenchPart part, List<String> selectionActions) {

		IAction action = new XLSAction(part, XLSAction.FREEZE_COL_ID.concat("Left"), XLSAction.FREEZE_COL_ID, "Left",  //$NON-NLS-1$ //$NON-NLS-2$
				Messages.XLSElementDecorator_leftAction); 
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.FREEZE_COL_ID.concat("Right"), XLSAction.FREEZE_COL_ID, "Right", Messages.XLSElementDecorator_rightAction);  //$NON-NLS-1$ //$NON-NLS-2$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.FREEZE_COL_ID.concat("None"), XLSAction.FREEZE_COL_ID, null, Messages.XLSElementDecorator_nonAction); //$NON-NLS-1$ //$NON-NLS-2$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.FREEZE_ROW_ID.concat("Top"), XLSAction.FREEZE_ROW_ID, "Top", Messages.XLSElementDecorator_topAction); //$NON-NLS-1$ //$NON-NLS-2$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.FREEZE_ROW_ID.concat("Bottom"), XLSAction.FREEZE_ROW_ID, "Bottom", Messages.XLSElementDecorator_bottomAction); //$NON-NLS-1$ //$NON-NLS-2$
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new XLSAction(part, XLSAction.FREEZE_ROW_ID.concat("None"), XLSAction.FREEZE_ROW_ID, null, Messages.XLSElementDecorator_nonAction); //$NON-NLS-1$ //$NON-NLS-2$
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}
	
	public void registerActions(ActionRegistry registry, List<String> selectionActions,	IWorkbenchPart part) {
		registerFit(registry, part, selectionActions);
		registerAutoFilter(registry, part, selectionActions);
		registerBreak(registry, part, selectionActions);
		registerCellProperties(registry, part, selectionActions);
		registerFreezeProperties(registry, part, selectionActions);
	}

	@Override
	public void registerActions(ActionRegistry registry, List<String> selectionActions, GraphicalViewer gviewer,
			AbstractVisualEditor part) {
		gviewer.setProperty(ShowXLSTagsAction.ID, true);
		IAction action = new ShowXLSTagsAction(gviewer, part.getJrContext());
		registry.registerAction(action);
		registerActions(registry, selectionActions, part);
	}
	
	public void fillContextMenu(ActionRegistry registry, IMenuManager menu){
		MenuManager submenu = new MenuManager(Messages.XLSElementDecorator_xlsTagsMenu);
		MenuManager fitMenu = new MenuManager(Messages.XLSElementDecorator_fitMenu);
		MenuManager autoFilterMenu = new MenuManager(Messages.XLSElementDecorator_autoFilterMenu);
		MenuManager breakMenu = new MenuManager(Messages.XLSElementDecorator_breakMenu);
		MenuManager freezeMenu = new MenuManager(Messages.XLSElementDecorator_freezeMenu);
		MenuManager freezeRowMenu = new MenuManager(Messages.XLSElementDecorator_rowsMenu);
		MenuManager freezeColMenu = new MenuManager(Messages.XLSElementDecorator_columnsMenu);
		MenuManager propertiesMenu = new MenuManager(Messages.XLSElementDecorator_cellPropertiesMenu);

		submenu.add(fitMenu);
		submenu.add(autoFilterMenu);
		submenu.add(breakMenu);
		submenu.add(propertiesMenu);
		freezeMenu.add(freezeRowMenu);
		freezeMenu.add(freezeColMenu);
		submenu.add(freezeMenu);

		IAction action;
		// Adding actions for the Fit
		action = registry.getAction(XLSAction.FIT_ROW_ID);
		fitMenu.add(action);
		action = registry.getAction(XLSAction.FIT_COL_ID);
		fitMenu.add(action);
		action = registry.getAction("XLS_Fit_None"); //$NON-NLS-1$
		fitMenu.add(action);

		// Adding actions for the autofilter
		action = registry.getAction(XLSAction.AUTOFILTER_ID.concat(XLSDecorator.START)); //$NON-NLS-1$
		autoFilterMenu.add(action);
		action = registry.getAction(XLSAction.AUTOFILTER_ID.concat(XLSDecorator.END)); //$NON-NLS-1$
		autoFilterMenu.add(action);
		action = registry.getAction(XLSAction.AUTOFILTER_ID.concat("none")); //$NON-NLS-1$
		autoFilterMenu.add(action);

		// Adding actions for the break
		action = registry.getAction(XLSAction.BREAK_BEFORE_ROW_ID);
		breakMenu.add(action);
		action = registry.getAction(XLSAction.BREAK_AFTER_ROW_ID);
		breakMenu.add(action);
		action = registry.getAction("XSL_Break_None"); //$NON-NLS-1$
		breakMenu.add(action);

		// Adding actions for the cell properties
		action = registry.getAction(XLSAction.CELL_HIDDEN_ID);
		propertiesMenu.add(action);
		action = registry.getAction(XLSAction.CELL_LOCKED_ID);
		propertiesMenu.add(action);
		action = registry.getAction("XSL_Cell_None"); //$NON-NLS-1$
		propertiesMenu.add(action);

		// Adding the freeze properties
		action = registry.getAction(XLSAction.FREEZE_ROW_ID.concat("Top")); //$NON-NLS-1$
		freezeRowMenu.add(action);
		action = registry.getAction(XLSAction.FREEZE_ROW_ID.concat("Bottom")); //$NON-NLS-1$
		freezeRowMenu.add(action);
		action = registry.getAction(XLSAction.FREEZE_ROW_ID.concat("None")); //$NON-NLS-1$
		freezeRowMenu.add(action);

		action = registry.getAction(XLSAction.FREEZE_COL_ID.concat("Left")); //$NON-NLS-1$
		freezeColMenu.add(action);
		action = registry.getAction(XLSAction.FREEZE_COL_ID.concat("Right")); //$NON-NLS-1$
		freezeColMenu.add(action);
		action = registry.getAction(XLSAction.FREEZE_COL_ID.concat("None")); //$NON-NLS-1$
		freezeColMenu.add(action);

		menu.add(submenu);
	}

	@Override
	public void buildContextMenu(ActionRegistry registry, EditPartViewer viewer, IMenuManager menu) {
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		if (sel.getFirstElement() instanceof EditPart) {
			EditPart ep = (EditPart) sel.getFirstElement();
			if (!(ep.getModel() instanceof MGraphicElement))
				return;
		}
		fillContextMenu(registry, menu);
	}

	@Override
	public RetargetAction[] buildMenuActions() {
		return new RetargetAction[] { new RetargetAction(ShowXLSTagsAction.ID, Messages.XLSElementDecorator_showXLSTagsLabel, IAction.AS_CHECK_BOX) };
	}

	@Override
	public void contribute2Menu(ActionRegistry registry, MenuManager menuManager) {
		menuManager.add(registry.getAction(ShowXLSTagsAction.ID));
	}

	@Override
	public List<String> getActionIDs() {
		if (actionIDs == null) {
			actionIDs = new ArrayList<String>(1);
			actionIDs.add(ShowXLSTagsAction.ID);
		}
		return actionIDs;
	}

}
