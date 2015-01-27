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
package com.jaspersoft.studio.components.chart.editor;

import java.util.List;

import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;

import com.jaspersoft.studio.components.chart.editor.action.ExportJar;
import com.jaspersoft.studio.components.chart.editor.part.ChartThemeEditPartFactory;
import com.jaspersoft.studio.components.chart.model.theme.MChartThemeSettings;
import com.jaspersoft.studio.editor.AContextMenuProvider;
import com.jaspersoft.studio.editor.AGraphicEditor;
import com.jaspersoft.studio.editor.outline.JDReportOutlineView;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class CrosstabEditor.
 * 
 * @author Chicu Veaceslav
 */
public class ChartThemeEditor extends AGraphicEditor {

	public ChartThemeEditor(JasperReportsConfiguration jrContext) {
		super(jrContext);
	}

	@Override
	protected EditPartFactory createEditParFactory() {
		return new ChartThemeEditPartFactory();
	}

	protected JDReportOutlineView createOutline(TreeViewer viewer) {
		outlinePage = new JDReportOutlineView(this, viewer) {
			protected void initActions(ActionRegistry registry, IActionBars bars) {
			}

			protected org.eclipse.gef.ContextMenuProvider getMenuContentProvider() {
				return createContextMenuProvider(getViewer());
			}
		};
		// outlinePage.setEditPartFactory(new StyleTreeEditPartFactory());
		return outlinePage;
	}

	@Override
	protected AContextMenuProvider createContextMenuProvider(
			EditPartViewer graphicalViewer) {
		return new AContextMenuProvider(graphicalViewer, getActionRegistry());
	}

	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		List<String> selectionActions = getSelectionActions();
		IAction action = new ExportJar(this);
		registry.registerAction(action);
		selectionActions.add(ExportJar.ID);
	}

	public ChartThemeSettings getChartThemeSettings() {
		return ((MChartThemeSettings) getModel().getChildren().get(0))
				.getValue();
	}

}
