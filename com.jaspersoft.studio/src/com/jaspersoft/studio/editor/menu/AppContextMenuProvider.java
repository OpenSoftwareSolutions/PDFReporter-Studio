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
package com.jaspersoft.studio.editor.menu;

import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.callout.action.CreatePinAction;
import com.jaspersoft.studio.editor.AContextMenuProvider;
import com.jaspersoft.studio.editor.action.HideElementsAction;
import com.jaspersoft.studio.editor.action.MoveDetailDownAction;
import com.jaspersoft.studio.editor.action.MoveDetailUpAction;
import com.jaspersoft.studio.editor.action.MoveGroupDownAction;
import com.jaspersoft.studio.editor.action.MoveGroupUpAction;
import com.jaspersoft.studio.editor.action.OpenEditorAction;
import com.jaspersoft.studio.editor.action.ShowPropertyViewAction;
import com.jaspersoft.studio.editor.action.align.Align2BorderAction;
import com.jaspersoft.studio.editor.action.band.MaximizeContainerAction;
import com.jaspersoft.studio.editor.action.band.StretchToContentAction;
import com.jaspersoft.studio.editor.action.copy.CopyFormatAction;
import com.jaspersoft.studio.editor.action.copy.PasteFormatAction;
import com.jaspersoft.studio.editor.action.layout.LayoutAction;
import com.jaspersoft.studio.editor.action.order.BringBackwardAction;
import com.jaspersoft.studio.editor.action.order.BringForwardAction;
import com.jaspersoft.studio.editor.action.order.BringToBackAction;
import com.jaspersoft.studio.editor.action.order.BringToFrontAction;
import com.jaspersoft.studio.editor.action.size.MatchSizeAction;
import com.jaspersoft.studio.editor.action.size.Size2BorderAction;
import com.jaspersoft.studio.editor.action.text.ConvertStaticIntoText;
import com.jaspersoft.studio.editor.action.text.ConvertTextIntoStatic;
import com.jaspersoft.studio.editor.defaults.SetDefaultsAction;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.editor.outline.actions.ConnectToDomainAction;
import com.jaspersoft.studio.editor.outline.actions.CreateBandAction;
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
import com.jaspersoft.studio.editor.outline.actions.DeleteGroupReportAction;
import com.jaspersoft.studio.editor.outline.actions.ExportStyleAsTemplateAction;
import com.jaspersoft.studio.editor.outline.actions.RefreshTemplateStyleExpression;
import com.jaspersoft.studio.editor.outline.actions.ResetStyleAction;
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
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.plugin.ExtensionManager;
import com.jaspersoft.studio.plugin.IComponentFactory;
import com.jaspersoft.studio.property.dataset.dialog.ContextualDatasetAction;
import com.jaspersoft.studio.property.section.report.action.PageFormatAction;
import com.jaspersoft.studio.property.section.report.action.PageRemoveMarginsAction;

/*
 * The Class AppContextMenuProvider.
 */
public class AppContextMenuProvider extends AContextMenuProvider {

	/**
	 * Instantiates a new app context menu provider.
	 * 
	 * @param viewer
	 *          the viewer
	 * @param registry
	 *          the registry
	 */
	public AppContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer, registry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	@Override
	public void buildContextMenu(IMenuManager menu) {
		// Add component actions group
		menu.add(new Separator(IComponentFactory.GROUP_COMPONENT));

		GEFActionConstants.addStandardActionGroups(menu);

		IAction action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		
		action = getActionRegistry().getAction(ConnectToDomainAction.ID);
		if (action != null && action.isEnabled())
			menu.add(action);

		// ----------------------------------------

		action = getActionRegistry().getAction(ActionFactory.CUT.getId());
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);

		action = getActionRegistry().getAction(ActionFactory.COPY.getId());
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);

		action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
		
		action = getActionRegistry().getAction(CopyFormatAction.ID);
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
		
		action = getActionRegistry().getAction(PasteFormatAction.ID);
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
		
		action = getActionRegistry().getAction(SetDefaultsAction.ID);
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);

		// -----------------------------------------------------------

		action = getActionRegistry().getAction(CreateFieldAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateSortFieldAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateVariableAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateScriptletAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateParameterAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
		
		action = getActionRegistry().getAction(CreateParameterSetAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateGroupAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateDatasetAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateStyleAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateConditionalStyleAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
		
		action = getActionRegistry().getAction(ExportStyleAsTemplateAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
		
		action = getActionRegistry().getAction(RefreshTemplateStyleExpression.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
		
		action = getActionRegistry().getAction(ResetStyleAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateStyleTemplateAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreateBandAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(CreatePinAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		ExtensionManager m = JaspersoftStudioPlugin.getExtensionManager();
		List<String> lst = m.getActionIDs();
		for (String ids : lst) {
			if (ids.equals(AContextMenuProvider.SEPARATOR)) {
				menu.appendToGroup(IComponentFactory.GROUP_COMPONENT, new Separator());
				continue;
			}
			action = getActionRegistry().getAction(ids);
			if (action != null && action.isEnabled())
				menu.appendToGroup(IComponentFactory.GROUP_COMPONENT, action);
		}

		action = getActionRegistry().getAction(DeleteGroupReportAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);

		action = getActionRegistry().getAction(MoveGroupUpAction.ID);
		if (action != null && action.isEnabled())
			menu.add(action);

		action = getActionRegistry().getAction(MoveDetailUpAction.ID);
		if (action != null && action.isEnabled())
			menu.add(action);

		action = getActionRegistry().getAction(MoveDetailDownAction.ID);
		if (action != null && action.isEnabled())
			menu.add(action);

		action = getActionRegistry().getAction(MoveGroupDownAction.ID);
		if (action != null && action.isEnabled())
			menu.add(action);

		//HIDE abd SHOW BAND ACTIONS
		action = getActionRegistry().getAction(HideElementsAction.ID_VISIBLE);
		if (action != null && action.isEnabled())
			menu.add(action);
		
		action = getActionRegistry().getAction(HideElementsAction.ID_NOT_VISIBLE);
		if (action != null && action.isEnabled())
			menu.add(action);
		

		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		// position actions
		MenuManager submenu = new MenuManager(Messages.AppContextMenuProvider_order, JaspersoftStudioPlugin.getInstance()
				.getImageDescriptor("icons/eclipseapps/elcl16/bring_to_front.gif"), BringToFrontAction.ID); //$NON-NLS-1$

		action = getActionRegistry().getAction(BringToFrontAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(BringForwardAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(BringBackwardAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(BringToBackAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		menu.add(submenu);

		// Alignment Actions
		submenu = new MenuManager(Messages.AppContextMenuProvider_align_components, InternalImages.DESC_HORZ_ALIGN_LEFT,
				GEFActionConstants.ALIGN_LEFT);

		action = getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(GEFActionConstants.ALIGN_CENTER);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(GEFActionConstants.ALIGN_RIGHT);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(GEFActionConstants.ALIGN_TOP);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(GEFActionConstants.ALIGN_MIDDLE);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(GEFActionConstants.ALIGN_BOTTOM);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(JoinLeftAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(JoinRightAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		menu.add(submenu);

		// Alignment Actions
		submenu = new MenuManager(Messages.AppContextMenuProvider_align_to_container, JaspersoftStudioPlugin.getInstance()
				.getImageDescriptor("icons/resources/eclipse/align-band-left.gif"), //$NON-NLS-1$
				Align2BorderAction.ID_ALIGN_LEFT);

		action = getActionRegistry().getAction(Align2BorderAction.ID_ALIGN_CENTER);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(Align2BorderAction.ID_ALIGN_MIDDLE);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(CenterInParentAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(Align2BorderAction.ID_ALIGN_LEFT);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(Align2BorderAction.ID_ALIGN_RIGHT);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(Align2BorderAction.ID_ALIGN_TOP);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(Align2BorderAction.ID_ALIGN_BOTTOM);
		if (action.isEnabled())
			submenu.add(action);

		menu.add(submenu);

		// match size Actions
		submenu = new MenuManager(Messages.AppContextMenuProvider_size_components, InternalImages.DESC_MATCH_WIDTH,
				GEFActionConstants.MATCH_WIDTH);

		action = getActionRegistry().getAction(MatchSizeAction.ID_SIZE_WIDTH);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(SameWidthMaxAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(SameWidthMinAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(MatchSizeAction.ID_SIZE_HEIGHT);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(SameHeightMaxAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(SameHeightMinAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(MatchSizeAction.ID_SIZE_BOTH);
		if (action.isEnabled())
			submenu.add(action);

		menu.add(submenu);
		menu.add(new Separator());

		// horizontal spacing Actions
		submenu = new MenuManager(Messages.AppContextMenuProvider_horizontalSpacingSubmenu, "horizontalspacingmenu"); //$NON-NLS-2$

		action = getActionRegistry().getAction(DecreaseHSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(IncreaseHSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(EqualsHSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(RemoveHSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		menu.add(submenu);

		// vertical spacing Actions
		submenu = new MenuManager(Messages.AppContextMenuProvider_verticalSpacingSubMenu, "verticalspacingmenu"); //$NON-NLS-2$

		action = getActionRegistry().getAction(DecreaseVSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(IncreaseVSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		submenu.add(new Separator());

		action = getActionRegistry().getAction(EqualsVSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(RemoveVSpaceAction.ID);
		if (action.isEnabled())
			submenu.add(action);

		menu.add(submenu);
		menu.add(new Separator());

		// ------------------------------

		submenu = new MenuManager(Messages.AppContextMenuProvider_size_to_container, JaspersoftStudioPlugin.getInstance()
				.getImageDescriptor("icons/eclipseapps/size_to_control_width.gif"), //$NON-NLS-1$
				Size2BorderAction.ID_SIZE_WIDTH);

		action = getActionRegistry().getAction(Size2BorderAction.ID_SIZE_WIDTH);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(Size2BorderAction.ID_SIZE_HEIGHT);
		if (action.isEnabled())
			submenu.add(action);

		action = getActionRegistry().getAction(Size2BorderAction.ID_SIZE_BOTH);
		if (action.isEnabled())
			submenu.add(action);

		menu.add(submenu);

		submenu = new MenuManager(Messages.AppContextMenuProvider_arrangeInContainerMenu, JaspersoftStudioPlugin
				.getInstance().getImageDescriptor("icons/layout-6.png"), //$NON-NLS-1$
				LayoutAction.ID);

		LayoutManager.addMenu(submenu, getActionRegistry());

		menu.add(submenu);

		menu.add(new Separator());
		// ------------------------------

		action = getActionRegistry().getAction(OrganizeAsTableAction.ID);
		if (action.isEnabled())
			menu.add(action);

		action = getActionRegistry().getAction(MaximizeContainerAction.ID);
		if (action.isEnabled())
			menu.add(action);

		action = getActionRegistry().getAction(StretchToContentAction.ID);
		if (action.isEnabled())
			menu.add(action);

		menu.add(new Separator());

		action = getActionRegistry().getAction(ConvertStaticIntoText.ID);
		if (action.isEnabled())
			menu.add(action);

		// Start of the convert actions
		action = getActionRegistry().getAction(ConvertTextIntoStatic.ID);
		if (action.isEnabled())
			menu.add(action);

		// End of the convert actions
		
		//Action to open a subreport into the editor
		action = getActionRegistry().getAction(OpenEditorAction.ID);
		if (action.isEnabled())
			menu.add(action);

		menu.add(new Separator());

		JaspersoftStudioPlugin.getDecoratorManager().buildContextMenu(getActionRegistry(), getViewer(), menu);

		action = getActionRegistry().getAction(ShowPropertyViewAction.ID);
		if (action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);

		action = getActionRegistry().getAction(PageFormatAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);

		action = getActionRegistry().getAction(PageRemoveMarginsAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);

		action = getActionRegistry().getAction(ContextualDatasetAction.ID);
		if (action != null && action.isEnabled())
			menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);

	}

}
