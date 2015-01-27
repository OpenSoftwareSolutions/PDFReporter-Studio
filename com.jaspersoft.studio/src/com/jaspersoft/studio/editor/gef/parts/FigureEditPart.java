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
package com.jaspersoft.studio.editor.gef.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.figures.FigureFactory;
import com.jaspersoft.studio.editor.gef.figures.ReportPageFigure;
import com.jaspersoft.studio.editor.gef.figures.borders.CornerBorder;
import com.jaspersoft.studio.editor.gef.figures.borders.ElementLineBorder;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.ElementEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.FigurePageLayoutEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.FigureSelectionEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.SearchParentDragTracker;
import com.jaspersoft.studio.editor.gef.rulers.ReportRuler;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class FigureEditPart.
 */
public class FigureEditPart extends AJDEditPart implements PropertyChangeListener, IRulerUpdatable {

	private static final String RECTANGLE = "rectangle";
	protected JSSDrawVisitor drawVisitor;

	public JSSDrawVisitor getDrawVisitor() {
		return drawVisitor;
	}

	private PreferenceListener preferenceListener;

	private final class PreferenceListener implements IPropertyChangeListener {

		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			handlePreferenceChanged(event);
		}

	}

	protected void handlePreferenceChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
		String p = event.getProperty();
		if (p.equals(DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_STYLE)
				|| p.equals(DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_COLOR)) {
			pref = null;
			setPrefsBorder(getFigure());
		} else
			refreshVisuals();
	}

	@Override
	public void activate() {
		super.activate();
		preferenceListener = new PreferenceListener();
		JaspersoftStudioPlugin.getInstance().addPreferenceListener(preferenceListener);
	}

	@Override
	public void deactivate() {
		if (preferenceListener != null)
			JaspersoftStudioPlugin.getInstance().removePreferenceListener(preferenceListener);
		super.deactivate();
	}

	public void setDrawVisitor(JSSDrawVisitor drawVisitor) {
		this.drawVisitor = drawVisitor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		ANode model = getModel();
		IFigure rect = FigureFactory.createFigure(model);
		setPrefsBorder(rect);
		setupFigure(rect);
		return rect;
	}

	/**
	 * Instead of the default drag tracker an overridden one is returned, in this way we can control the edit part
	 * targeted from a drag & drop operation, and if the target is isn't an IContainer then it's parent is returned Change
	 * by Orlandin Marco
	 */
	@Override
	public org.eclipse.gef.DragTracker getDragTracker(org.eclipse.gef.Request request) {
		return new SearchParentDragTracker(this);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new FigureSelectionEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FigurePageLayoutEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	public void refreshVisuals() {
		Shape rect = (Shape) getFigure();
		if (Display.getCurrent() != null) {
			setupFigure(rect);
			rect.invalidate();
			rect.repaint();
		}
	}

	protected JasperReportsConfiguration jConfig;

	public JasperReportsConfiguration getjConfig() {
		return jConfig;
	}

	private String pref;
	private Color fg;

	public void setPrefsBorder(IFigure rect) {
		if (pref == null) {
			if (jConfig == null)
				jConfig = ((ANode) getModel()).getJasperConfiguration();
			pref = jConfig.getProperty(DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_STYLE, RECTANGLE);
			String mcolor = jConfig.getProperty(DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_COLOR,
					DesignerPreferencePage.DEFAULT_ELEMENT_DESIGN_BORDER_COLOR);
			fg = SWTResourceManager.getColor(StringConverter.asRGB(mcolor));
		}
		if (pref.equals(RECTANGLE)) //$NON-NLS-1$
			rect.setBorder(new ElementLineBorder(fg));
		else
			rect.setBorder(new CornerBorder(fg, 5));
	}

	/**
	 * Sets the up figure.
	 * 
	 * @param rect
	 *          the new up figure
	 */
	protected void setupFigure(IFigure rect) {
		ANode model = getModel();
		rect.setToolTip(new Label(model.getToolTip()));
		if (model instanceof IGraphicElement && model.getValue() != null) {
			Rectangle bounds = ((IGraphicElement) model).getBounds();
			int x = bounds.x + ReportPageFigure.PAGE_BORDER.left;
			int y = bounds.y + ReportPageFigure.PAGE_BORDER.top;
			if (model.getValue() instanceof JRDesignElement) {
				JRDesignElement jrElement = (JRDesignElement) model.getValue();
				if (rect instanceof ComponentFigure && drawVisitor != null) {
					ComponentFigure f = (ComponentFigure) rect;
					f.setLocation(new Point(x, y));

					f.setJRElement(jrElement, drawVisitor);
				} else
					rect.setBounds(new Rectangle(x, y, jrElement.getWidth(), jrElement.getHeight()));
			} else {
				rect.setBounds(new Rectangle(x, y, bounds.width, bounds.height));
			}
		}
		if (rect instanceof ComponentFigure)
			JaspersoftStudioPlugin.getDecoratorManager().setupFigure((ComponentFigure) rect, this);
	}

	@Override
	public ANode getModel() {
		return (ANode) super.getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		refresh();
		refreshC(getModel());
		refreshVisuals();
	}

	/**
	 * Refresh c.
	 * 
	 * @param n
	 *          the n
	 */
	private void refreshC(ANode n) {
		if (n.getChildren() != null)
			for (INode node : n.getChildren()) {
				EditPart ep = (EditPart) getViewer().getEditPartRegistry().get(node);
				if (ep instanceof FigureEditPart)
					((FigureEditPart) ep).refreshVisuals();
				refreshC((ANode) node);
			}
	}

	public void updateRulers() {
		ANode model = getModel().getParent();
		if (model instanceof IGraphicElement && model.getValue() != null) {
			Rectangle bounds = ((IGraphicElement) model).getBounds();
			if (bounds != null){
				int x = bounds.x + ReportPageFigure.PAGE_BORDER.left;
				int y = bounds.y + ReportPageFigure.PAGE_BORDER.top;
	
				getViewer().setProperty(ReportRuler.PROPERTY_HOFFSET, x);
				getViewer().setProperty(ReportRuler.PROPERTY_VOFFSET, y);
				getViewer().setProperty(ReportRuler.PROPERTY_HEND, bounds.width); //$NON-NLS-1$
				getViewer().setProperty(ReportRuler.PROPERTY_VEND, bounds.height);//$NON-NLS-1$
	
				getViewer().setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, new Point(x, y));
			}
		}
	}

}
