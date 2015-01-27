/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignComponentElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.part.WorkbenchPart;

import com.jaspersoft.jasperreports.customvisualization.CVComponent;
import com.jaspersoft.studio.components.customvisualization.figure.CVFigure;
import com.jaspersoft.studio.components.customvisualization.model.MCustomVisualization;
import com.jaspersoft.studio.components.customvisualization.model.command.CreateCustomVisualizationCommand;
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

/**
 * Factory for the Custom Visualization component element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class CVComponentFactory implements IComponentFactory {
	
	private static List<Class<?>> knownClasses;
	
	static {
		knownClasses = new ArrayList<Class<?>>(1);
		knownClasses.add(MCustomVisualization.class);
	}

	@Override
	public ANode createNode(ANode parent, Object jrObject, int newIndex) {
		if (jrObject instanceof JRDesignComponentElement
				&& ((JRDesignComponentElement) jrObject).getComponent() instanceof CVComponent) {
			return new MCustomVisualization(parent, (JRDesignComponentElement) jrObject,
					newIndex);
		}
		return null;

	}

	@Override
	public List<?> getChildren4Element(Object jrObject) {
		return null;
	}

	@Override
	public IPaletteContributor getPaletteEntries() {
		PaletteContributor pc = new PaletteContributor();
		pc.add(MCustomVisualization.class);
		return pc;
	}

	@Override
	public IFigure createFigure(ANode node) {
		if (node instanceof MCustomVisualization) {
			return new CVFigure();
		}
		return null;
	}

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		return null;
	}

	@Override
	public Command getStretchToContent(ANode node) {
		return null;
	}

	@Override
	public Command getCreateCommand(ANode parent, ANode child,
			Rectangle location, int newIndex) {
		if (child instanceof MCustomVisualization) {
			if (parent instanceof MElementGroup)
				return new CreateCustomVisualizationCommand((MElementGroup) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MBand)
				return new CreateCustomVisualizationCommand((MBand) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MFrame)
				return new CreateCustomVisualizationCommand((MFrame) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MReport)
				return new CreateCustomVisualizationCommand(parent, (MGraphicElement) child,
						location, newIndex);
			if (parent instanceof IGroupElement) {
				return new CreateCustomVisualizationCommand(parent, (MGraphicElement) child,
						location, newIndex);
			}
		}
		return null;
	}

	@Override
	public Command getReorderCommand(ANode parent, ANode child, int newIndex) {
		return null;
	}

	@Override
	public Command getDeleteCommand(ANode parent, ANode child) {
		return null;
	}

	@Override
	public Command getOrphanCommand(ANode parent, ANode child) {
		return null;
	}

	@Override
	public List<Action> getActions(WorkbenchPart part) {
		return null;
	}

	@Override
	public List<String> getActionsID() {
		return null;
	}

	@Override
	public AbstractVisualEditor getEditor(Object node,
			JasperReportsConfiguration jrContext) {
		return null;
	}

	@Override
	public ExpressionContext getElementExpressionContext(Object jrObject) {
		// FIXME - Implement this method.
		return null;
	}

	@Override
	public List<Class<?>> getKnownClasses() {
		return knownClasses;
	}

}
