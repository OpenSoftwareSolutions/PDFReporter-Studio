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
package com.jaspersoft.studio.components.table.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.components.section.name.NamedSubeditor;
import com.jaspersoft.studio.components.table.action.EditTableStyleAction;
import com.jaspersoft.studio.components.table.action.RemoveTableStylesAction;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnAfterAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnBeforeAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnBeginAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnCellAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnEndAction;
import com.jaspersoft.studio.components.table.model.column.action.DeleteColumnAction;
import com.jaspersoft.studio.components.table.model.column.action.DeleteColumnCellAction;
import com.jaspersoft.studio.components.table.model.column.action.DeleteRowAction;
import com.jaspersoft.studio.components.table.model.columngroup.action.GroupColumnsAction;
import com.jaspersoft.studio.components.table.model.columngroup.action.UnGroupColumnsAction;
import com.jaspersoft.studio.editor.gef.parts.JSSGraphicalViewerKeyHandler;
import com.jaspersoft.studio.editor.gef.parts.JasperDesignEditPartFactory;
import com.jaspersoft.studio.editor.gef.parts.MainDesignerRootEditPart;
import com.jaspersoft.studio.editor.gef.rulers.ReportRuler;
import com.jaspersoft.studio.editor.gef.rulers.ReportRulerProvider;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;
import com.jaspersoft.studio.property.dataset.dialog.ContextualDatasetAction;
import com.jaspersoft.studio.property.dataset.dialog.DatasetAction;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class TableEditor.
 * 
 * @author Chicu Veaceslav
 */
public class TableEditor extends NamedSubeditor {
	public TableEditor(JasperReportsConfiguration jrContext) {
		super(jrContext);
		setPartName(getDefaultPartName());
		setPartImage(JaspersoftStudioPlugin.getInstance().getImage(MTable.getIconDescriptor().getIcon16()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().getControl().setBackground(ColorConstants.button);

		GraphicalViewer graphicalViewer = getGraphicalViewer();
		MainDesignerRootEditPart rootEditPart = new MainDesignerRootEditPart();

		graphicalViewer.setRootEditPart(rootEditPart);
		// set EditPartFactory
		graphicalViewer.setEditPartFactory(new JasperDesignEditPartFactory());

		// set rulers providers
		RulerProvider provider = new ReportRulerProvider(new ReportRuler(true, RulerProvider.UNIT_PIXELS));
		graphicalViewer.setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, provider);

		provider = new ReportRulerProvider(new ReportRuler(false, RulerProvider.UNIT_PIXELS));
		graphicalViewer.setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, provider);

		Boolean isRulerVisible = jrContext.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWRULER);

		graphicalViewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, isRulerVisible);

		createAdditionalActions();
		graphicalViewer.setKeyHandler(new JSSGraphicalViewerKeyHandler(graphicalViewer));
	}

	@Override
	protected List<String> getIgnorePalleteElements() {
		List<String> lst = new ArrayList<String>();
		lst.add("com.jaspersoft.studio.components.crosstab.model.MCrosstab"); //$NON-NLS-1$
		return lst;
	}

	@Override
	protected void createEditorActions(ActionRegistry registry) {
		createDatasetAndStyleActions(registry);

		IAction action = new CreateColumnEndAction(this);
		registry.registerAction(action);
		@SuppressWarnings("unchecked")
		List<String> selectionActions = getSelectionActions();
		selectionActions.add(CreateColumnEndAction.ID);

		action = new GroupColumnsAction(this);
		registry.registerAction(action);
		selectionActions.add(GroupColumnsAction.ID);

		action = new EditTableStyleAction(this);
		registry.registerAction(action);
		selectionActions.add(EditTableStyleAction.ID);

		action = new CreateColumnBeginAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateColumnBeginAction.ID);

		action = new CreateColumnBeforeAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateColumnBeforeAction.ID);

		action = new CreateColumnAfterAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateColumnAfterAction.ID);

		action = new RemoveTableStylesAction(this);
		registry.registerAction(action);
		selectionActions.add(RemoveTableStylesAction.ID);

		action = new UnGroupColumnsAction(this);
		registry.registerAction(action);
		selectionActions.add(UnGroupColumnsAction.ID);

		action = new CreateColumnCellAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateColumnCellAction.ID);

		action = new DeleteColumnAction(this);
		registry.registerAction(action);
		selectionActions.add(DeleteColumnAction.ID);

		action = new DeleteColumnCellAction(this);
		registry.registerAction(action);
		selectionActions.add(DeleteColumnCellAction.ID);

		action = new DeleteRowAction(this);
		registry.registerAction(action);
		selectionActions.add(DeleteRowAction.ID);

		action = new DatasetAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

		action = new ContextualDatasetAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());
	}

	@Override
	public void contributeItemsToEditorTopToolbar(IToolBarManager toolbarManager) {
		toolbarManager.add(getActionRegistry().getAction(DatasetAction.ID));
		toolbarManager.add(new Separator());
		super.contributeItemsToEditorTopToolbar(toolbarManager);
	}

	@Override
	public String getDefaultPartName() {
		return Messages.TableEditor_table;
	}

	@Override
	public ANode getEditedNode() {
		INode model = getModel();
		if (model != null) {
			ModelVisitor<MTable> mv = new ModelVisitor<MTable>(model) {

				@Override
				public boolean visit(INode n) {
					if (n instanceof MTable) {
						setObject((MTable) n);
						stop();
					}
					return true;
				}

			};
			return mv.getObject();
		}
		return null;
	}
}
