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
package com.jaspersoft.studio.editor.style;

import java.util.List;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;

import com.jaspersoft.studio.editor.AGraphicEditor;
import com.jaspersoft.studio.editor.outline.JDReportOutlineView;
import com.jaspersoft.studio.editor.outline.actions.CreateStyleAction;
import com.jaspersoft.studio.editor.outline.actions.CreateStyleTemplateReferenceAction;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class CrosstabEditor.
 * 
 * @author Chicu Veaceslav
 */
public class StyleTemplateEditor extends AGraphicEditor {

	public StyleTemplateEditor(JasperReportsConfiguration jrContext) {
		super(jrContext);
	}

	@Override
	protected EditPartFactory createEditParFactory() {
		return new StyleEditPartFactory();
	}

	protected JDReportOutlineView createOutline(TreeViewer viewer) {
		outlinePage = new JDReportOutlineView(this, viewer) {
			protected void initActions(ActionRegistry registry, IActionBars bars) {
				String id = CreateStyleTemplateReferenceAction.ID;
				bars.setGlobalActionHandler(id, registry.getAction(id));

				id = CreateStyleAction.ID;
				bars.setGlobalActionHandler(id, registry.getAction(id));
			}

			protected org.eclipse.gef.ContextMenuProvider getMenuContentProvider() {
				return createContextMenuProvider(getViewer());
			}
		};
		outlinePage.setEditPartFactory(new StyleTreeEditPartFactory());
		return outlinePage;
	}

	protected ContextMenuProvider createContextMenuProvider(EditPartViewer graphicalViewer) {
		return new AppStyleContextMenuProvider(graphicalViewer, getActionRegistry());
	}

	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		List<String> selectionActions = getSelectionActions();
		IAction action = new CreateStyleAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateStyleAction.ID);

		action = new CreateStyleTemplateReferenceAction(this);
		registry.registerAction(action);
		selectionActions.add(CreateStyleTemplateReferenceAction.ID);

		// ------------
		action = new DirectEditAction(this);
		registry.registerAction(action);
		selectionActions.add(action.getId());

	}

}
