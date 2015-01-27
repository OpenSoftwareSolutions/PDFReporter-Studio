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
package com.jaspersoft.studio.components.chartspider;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.components.spiderchart.SpiderChartComponent;
import net.sf.jasperreports.components.spiderchart.StandardSpiderDataset;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.part.WorkbenchPart;

import com.jaspersoft.studio.components.chart.model.series.category.MCategorySeries;
import com.jaspersoft.studio.components.chartspider.figure.SpiderChartFigure;
import com.jaspersoft.studio.components.chartspider.model.MChartSpiderDataset;
import com.jaspersoft.studio.components.chartspider.model.MSpiderChart;
import com.jaspersoft.studio.components.chartspider.model.command.CreateCategorySeriesCommand;
import com.jaspersoft.studio.components.chartspider.model.command.CreateSpiderChartCommand;
import com.jaspersoft.studio.components.chartspider.model.command.DeleteCategorySeriesCommand;
import com.jaspersoft.studio.components.chartspider.model.command.ReorderCategorySeriesCommand;
import com.jaspersoft.studio.components.chartspider.part.SpiderChartEditPart;
import com.jaspersoft.studio.components.chartspider.wizard.action.ChartSpiderWizardAction;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGroupElement;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.plugin.IComponentFactory;
import com.jaspersoft.studio.plugin.IPaletteContributor;
import com.jaspersoft.studio.plugin.PaletteContributor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SpiderChartComponentFactory implements IComponentFactory {

	private static List<Class<?>> knownClasses;
	
	static {
		knownClasses = new ArrayList<Class<?>>(1);
		knownClasses.add(MSpiderChart.class);
	}
	
	public ANode createNode(ANode parent, Object jrObject, int newIndex) {
		if (jrObject instanceof JRDesignComponentElement
				&& ((JRDesignComponentElement) jrObject).getComponent() instanceof SpiderChartComponent) {
			return new MSpiderChart(parent,
					(JRDesignComponentElement) jrObject, newIndex);
		}
		if (jrObject instanceof StandardSpiderDataset)
			return new MChartSpiderDataset(parent,
					(StandardSpiderDataset) jrObject, parent.getJasperDesign());
		if (jrObject instanceof JRDesignCategorySeries)
			return new MCategorySeries(parent,
					(JRDesignCategorySeries) jrObject, newIndex);
		return null;
	}

	public IFigure createFigure(ANode node) {
		if (node instanceof MSpiderChart)
			return new SpiderChartFigure();
		return null;
	}

	public List<?> getChildren4Element(Object jrObject) {
		List<Object> ch = new ArrayList<Object>();
		if (jrObject instanceof StandardSpiderDataset)
			ch.addAll(((StandardSpiderDataset) jrObject).getSeriesList());
		else if (jrObject instanceof JRDesignComponentElement
				&& ((JRDesignComponentElement) jrObject).getComponent() instanceof SpiderChartComponent) {
			ch.add(((SpiderChartComponent) ((JRDesignComponentElement) jrObject)
					.getComponent()).getDataset());
		}
		return ch;
	}

	public IPaletteContributor getPaletteEntries() {
		PaletteContributor pc = new PaletteContributor();
		// pc.add("com.jaspersoft.studio.components.CHARTCOMPONENTS",
		// MSpiderChart.class);
		pc.add(MSpiderChart.class);
		return pc;
	}

	public Command getCreateCommand(ANode parent, ANode child,
			Rectangle location, int newIndex) {
		if (child instanceof MSpiderChart) {
			if (parent instanceof MElementGroup)
				return new CreateSpiderChartCommand((MElementGroup) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MBand)
				return new CreateSpiderChartCommand((MBand) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MFrame)
				return new CreateSpiderChartCommand((MFrame) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MReport)
				return new CreateSpiderChartCommand(parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof IGroupElement) {
				return new CreateSpiderChartCommand(parent,
						(MGraphicElement) child, location, newIndex);
			}
		} else if (child instanceof MCategorySeries
				&& parent instanceof MChartSpiderDataset)
			return new CreateCategorySeriesCommand(
					(MChartSpiderDataset) parent, (MCategorySeries) child,
					newIndex);
		return null;
	}

	public Command getDeleteCommand(ANode parent, ANode child) {
		if (child instanceof MCategorySeries
				&& parent instanceof MChartSpiderDataset)
			return new DeleteCategorySeriesCommand(
					(MChartSpiderDataset) parent, (MCategorySeries) child);
		return null;
	}

	public Command getReorderCommand(ANode parent, ANode child, int newIndex) {
		if (child instanceof MCategorySeries
				&& parent instanceof MChartSpiderDataset)
			return new ReorderCategorySeriesCommand((MCategorySeries) child,
					(MChartSpiderDataset) parent, newIndex);
		return null;
	}

	public List<Action> getActions(WorkbenchPart part) {
		List<Action> lst = new ArrayList<Action>();
		lst.add(new ChartSpiderWizardAction(part));
		return lst;
	}

	public List<String> getActionsID() {
		List<String> lst = new ArrayList<String>();
		lst.add(ChartSpiderWizardAction.ID);
		return lst;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		if (model != null && model instanceof MSpiderChart)
			return new SpiderChartEditPart();
		return null;
	}

	public Command getOrphanCommand(ANode parent, ANode child) {
		return null;
	}

	public AbstractVisualEditor getEditor(Object node,
			JasperReportsConfiguration jrContext) {
		return null;
	}

	public ExpressionContext getElementExpressionContext(Object jrObject) {
		// FIXME - Implement this method.
		return null;
	}

	@Override
	public Command getStretchToContent(ANode node) {
		return null;
	}

	@Override
	public List<Class<?>> getKnownClasses() {
		return knownClasses;
	}
}
