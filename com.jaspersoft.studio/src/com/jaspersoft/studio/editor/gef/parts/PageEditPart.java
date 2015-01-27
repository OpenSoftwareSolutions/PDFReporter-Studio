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
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.callout.CalloutEditPart;
import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.callout.pin.PinEditPart;
import com.jaspersoft.studio.editor.gef.figures.APageFigure;
import com.jaspersoft.studio.editor.gef.figures.ContainerPageFigure;
import com.jaspersoft.studio.editor.gef.figures.borders.ShadowBorder;
import com.jaspersoft.studio.editor.gef.figures.borders.SimpleShadowBorder;
import com.jaspersoft.studio.editor.gef.figures.layers.GridLayer;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.JSSSnapFeedBackPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.PageLayoutEditPolicy;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class PageEditPart.
 * 
 * @author Chicu Veaceslav
 */
public class PageEditPart extends AJDEditPart implements PropertyChangeListener {
	private PreferenceListener preferenceListener;

	private final class PreferenceListener implements IPropertyChangeListener {

		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			handlePreferencesChanged(event);
		}
	}

	protected void handlePreferencesChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
		String p = event.getProperty();
		if (p.equals(DesignerPreferencePage.P_PAGE_DESIGN_BORDER_STYLE))
			setPrefsBorder(getFigure());
		if (p.equals(RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWRULER)) {
			Boolean val = jConfig.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWRULER, Boolean.TRUE);
			getViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, val);
		} else if (p.equals(RulersGridPreferencePage.P_PAGE_GRID_COLOR)
				|| p.equals(RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWGRID)
				|| p.equals(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEY)
				|| p.equals(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEX)) {
			refreshGridLayer();
		}
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				refreshVisuals();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if (key == SnapToHelper.class) {
			List<SnapToHelper> snapStrategies = new ArrayList<SnapToHelper>();
			Boolean val = jConfig.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWRULER, Boolean.TRUE);
			Boolean stg = jConfig.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGUIDES, Boolean.TRUE);
			if (val.booleanValue() && stg != null && stg.booleanValue())
				snapStrategies.add(new SnapToGuides(this));
			val = jConfig.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGEOMETRY, Boolean.TRUE);
			if (val.booleanValue()) {
				SnapToGeometryThreshold snapper = new SnapToGeometryThreshold(this);
				snapper.setThreshold(6.0);
				snapStrategies.add(snapper);
			}
			val = jConfig.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGRID, Boolean.TRUE);
			if (val.booleanValue())
				snapStrategies.add(new SnapToGrid(this));

			if (snapStrategies.size() == 0)
				return null;
			if (snapStrategies.size() == 1)
				return snapStrategies.get(0);

			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++)
				ss[i] = snapStrategies.get(i);
			return new CompoundSnapToHelper(ss);
		}
		return super.getAdapter(key);
	}

	@Override
	public void activate() {
		super.activate();
		ANode node = (ANode) getModel();
		node.getPropertyChangeSupport().addPropertyChangeListener((PropertyChangeListener) this);

		preferenceListener = new PreferenceListener();
		JaspersoftStudioPlugin.getInstance().addPreferenceListener(preferenceListener);
	}

	@Override
	public void deactivate() {
		ANode node = (ANode) getModel();
		node.getPropertyChangeSupport().removePropertyChangeListener((PropertyChangeListener) this);
		if (preferenceListener != null)
			JaspersoftStudioPlugin.getInstance().removePreferenceListener(preferenceListener);
		super.deactivate();
	}

	protected JasperReportsConfiguration jConfig;

	protected void setPrefsBorder(IFigure rect) {
		if (jConfig == null)
			jConfig = ((APropertyNode) getModel()).getJasperConfiguration();
		String pref = jConfig.getProperty(DesignerPreferencePage.P_PAGE_DESIGN_BORDER_STYLE,
				DesignerPreferencePage.DEFAULT_BORDERSTYLE); //$NON-NLS-1$

		if (pref.equals(DesignerPreferencePage.DEFAULT_BORDERSTYLE)) //$NON-NLS-1$
			rect.setBorder(new ShadowBorder());
		else
			rect.setBorder(new SimpleShadowBorder());
	}

	/**
	 * Updates the {@link GridLayer grid} based on properties set on the {@link #getViewer() graphical viewer}:
	 * {@link SnapToGrid#PROPERTY_GRID_VISIBLE}, {@link SnapToGrid#PROPERTY_GRID_SPACING}, and
	 * {@link SnapToGrid#PROPERTY_GRID_ORIGIN}.
	 * <p>
	 * This method is invoked initially when the GridLayer is created, and when any of the above-mentioned properties are
	 * changed on the viewer.
	 */
	protected void refreshGridLayer() {
		if (jConfig != null) {
			boolean visible = jConfig.getPropertyBoolean(RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWGRID, true);
			GridLayer grid = ((APageFigure) getFigure()).getGrid();
			grid.setOrigin((Point) getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ORIGIN));

			int x = jConfig.getPropertyInteger(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEX, 10);
			int y = jConfig.getPropertyInteger(RulersGridPreferencePage.P_PAGE_RULERGRID_GRIDSPACEY, 10);

			grid.setSpacing(new Dimension(x, y));
			grid.setVisible(visible);
			getViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(x, y));

			String mcolor = jConfig.getProperty(RulersGridPreferencePage.P_PAGE_GRID_COLOR,
					RulersGridPreferencePage.DEFAULT_GRIDCOLOR);
			Color fg = SWTResourceManager.getColor(StringConverter.asRGB(mcolor));
			grid.setForegroundColor(fg);
		}
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#register()
	 */
	protected void register() {
		super.register();
		getViewer().addPropertyChangeListener(gridListener);
		refreshGridLayer();
	}

	/**
	 * @see AbstractEditPart#unregister()
	 */
	protected void unregister() {
		getViewer().removePropertyChangeListener(gridListener);
		super.unregister();
	}

	private PropertyChangeListener gridListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			String property = evt.getPropertyName();
			if (property.equals(SnapToGrid.PROPERTY_GRID_ORIGIN))
				refreshGridLayer();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModel()
	 */
	@Override
	public Object getModel() {
		Object model = super.getModel();
		if (model instanceof MRoot)
			return ((MRoot) model).getChildren().get(0);
		return model;
	}

	/**
	 * Gets the page.
	 * 
	 * @return the page
	 */
	public ANode getPage() {
		return (ANode) getModel();
	}

	/**
	 * Gets the jasper design.
	 * 
	 * @return the jasper design
	 */
	public JasperDesign getJasperDesign() {
		return getPage().getJasperDesign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		APageFigure figure = newPageFigure();
		setPrefsBorder(figure);
		setupPageFigure(figure);
		// get current display...
		figure.setOpaque(false);
		figure.setBackgroundColor(ColorConstants.white);
		figure.setLayoutManager(new XYLayout());
		updateRullers();
		return figure;
	}

	protected APageFigure newPageFigure() {
		return new ContainerPageFigure(true, this);
	}

	/**
	 * Setup page figure.
	 * 
	 * @param jd
	 *          the jasper design
	 * @param figure2
	 *          the figure2
	 */
	protected void setupPageFigure(APageFigure figure2) {
		JasperDesign jd = getJasperDesign();
		int w = 2000;// jd.getPageWidth() + 20;
		int h = 5000;// designHeight + 10;

		figure2.setSize(w, h);

		getViewer().setProperty("RULER_HOFFSET", APageFigure.PAGE_BORDER.left); //$NON-NLS-1$
		getViewer().setProperty("RULER_VOFFSET", APageFigure.PAGE_BORDER.top); //$NON-NLS-1$
		getViewer().setProperty("RULER_HEND", jd.getPageWidth()); //$NON-NLS-1$
		getViewer().setProperty("RULER_VEND", jd.getPageHeight() - APageFigure.PAGE_BORDER.top); //$NON-NLS-1$

		getViewer().setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN,
				new Point(APageFigure.PAGE_BORDER.left, APageFigure.PAGE_BORDER.top));

		setupPagePreferences(figure2);
	}

	protected void setupPagePreferences(APageFigure figure2) {
		String mcolor = jConfig.getProperty(DesignerPreferencePage.P_PAGE_BACKGROUND,
				DesignerPreferencePage.DEFAULT_PAGE_BACKGROUND);
		((APageFigure) figure2).setPageBackground(SWTResourceManager.getColor(StringConverter.asRGB(mcolor)));
	}

	public void updateRullers() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new PageLayoutEditPolicy());
		installEditPolicy("Snap Feedback", new JSSSnapFeedBackPolicy()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return getRoot().getDragTracker(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List<Object> getModelChildren() {
		final List<Object> list = new ArrayList<Object>();
		new ModelVisitor<Object>(getPage()) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MCallout) {
					list.add(n);
					for (INode node : n.getChildren())
						list.add(node);
				} else if (n instanceof IGraphicElement && n.getValue() != null)
					list.add(n);

				return true;
			}
		};
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#addChildVisual(org.eclipse.gef.EditPart, int)
	 */
	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (childEditPart instanceof IContainerPart) {
			IFigure layer = getLayer(MainDesignerRootEditPart.SECTIONS_LAYER);
			if (layer != null)
				layer.add(((AbstractGraphicalEditPart) childEditPart).getFigure());
		} else if (childEditPart instanceof FigureEditPart || childEditPart instanceof CalloutEditPart
				|| childEditPart instanceof PinEditPart) {
			IFigure layer = getLayer(MainDesignerRootEditPart.ELEMENTS_LAYER);
			if (layer != null)
				layer.add(((AJDEditPart) childEditPart).getFigure());
		}
		super.addChildVisual(childEditPart, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	public void refreshVisuals() {
		APageFigure figure2 = (APageFigure) getFigure();
		if (Display.getCurrent() != null) {
			setupPageFigure(figure2);
			for (Object i : getChildren()) {
				if (i instanceof EditPart)
					((EditPart) i).refresh();
			}
			/*
			 * ((GridLayout) figure2.getLayoutManager()).marginHeight = jasperDesign.getTopMargin();
			 * figure2.getLayoutManager().layout(figure2);
			 */
			figure2.repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent arg0) {
		refreshChildren();
		refreshVisuals();
	}

}
