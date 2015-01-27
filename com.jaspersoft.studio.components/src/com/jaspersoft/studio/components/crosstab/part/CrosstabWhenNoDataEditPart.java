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
package com.jaspersoft.studio.components.crosstab.part;

import java.util.List;

import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.crosstab.figure.CellFigure;
import com.jaspersoft.studio.components.crosstab.figure.EmptyCellFigure;
import com.jaspersoft.studio.components.crosstab.figure.WhenNoDataCellFigure;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.components.crosstab.model.cell.command.CreateElementCommand;
import com.jaspersoft.studio.components.crosstab.model.cell.command.OrphanElementCommand;
import com.jaspersoft.studio.components.crosstab.model.nodata.MCrosstabWhenNoData;
import com.jaspersoft.studio.components.crosstab.model.nodata.MCrosstabWhenNoDataCell;
import com.jaspersoft.studio.editor.gef.commands.SetPageConstraintCommand;
import com.jaspersoft.studio.editor.gef.figures.ReportPageFigure;
import com.jaspersoft.studio.editor.gef.figures.borders.ShadowBorder;
import com.jaspersoft.studio.editor.gef.figures.borders.SimpleShadowBorder;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.PageLayoutEditPolicy;
import com.jaspersoft.studio.editor.outline.editpolicy.CloseSubeditorDeletePolicy;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;

/*
 * BandEditPart creates the figure for the band. The figure is actually just the bottom border of the band. This allows
 * to drag this border to resize the band. The PageEditPart sets a specific contraint for the BandEditPart elements in
 * order to make them move only vertically. The BandMoveEditPolicy is responsable for the feedback when the band is
 * dragged.
 * 
 * @author Chicu Veaceslav, Giulio Toffoli
 * 
 */
public class CrosstabWhenNoDataEditPart extends ACrosstabCellEditPart {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		if (getModel() instanceof MCrosstabWhenNoData)
			super.createEditPolicies();
		else {
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new CloseSubeditorDeletePolicy());
			installEditPolicy(EditPolicy.LAYOUT_ROLE, new PageLayoutEditPolicy() {

				private RectangleFigure targetFeedback;

				protected void eraseLayoutTargetFeedback(Request request) {
					super.eraseLayoutTargetFeedback(request);
					if (targetFeedback != null) {
						removeFeedback(targetFeedback);
						targetFeedback = null;
					}
				}

				protected IFigure getLayoutTargetFeedback(Request request) {
					if (request instanceof ChangeBoundsRequest) {
						ChangeBoundsRequest cbr = (ChangeBoundsRequest) request;
						List<EditPart> lst = cbr.getEditParts();
						for (EditPart ep : lst) {
							if (((ANode) ep.getModel()).getParent() == getModel())
								return null;
							if (ep instanceof CrosstabCellEditPart)
								return null;
						}
					} else if (request instanceof CreateRequest && !(getModel() instanceof MCell))
						return null;
					if (targetFeedback == null) {
						targetFeedback = new RectangleFigure();
						targetFeedback.setFill(false);

						IFigure hostFigure = getHostFigure();
						Rectangle bounds = hostFigure.getBounds();
						if (hostFigure instanceof HandleBounds)
							bounds = ((HandleBounds) hostFigure).getHandleBounds();
						Rectangle rect = new PrecisionRectangle(bounds);
						getHostFigure().translateToAbsolute(rect);
						getFeedbackLayer().translateToRelative(rect);

						targetFeedback.setBounds(rect.shrink(2, 2));
						targetFeedback.setBorder(new LineBorder(ColorConstants.lightBlue, 3));
						addFeedback(targetFeedback);
					}
					return targetFeedback;
				}

				protected void showLayoutTargetFeedback(Request request) {
					super.showLayoutTargetFeedback(request);
					getLayoutTargetFeedback(request);
				}

				protected Command getCreateCommand(ANode parent, Object obj, Rectangle constraint, int index) {
					if (parent instanceof MPage)
						parent = getModel();
					Rectangle b = ((MCell) getModel()).getBounds();
					int x = constraint.x - b.x;// - ReportPageFigure.PAGE_BORDER.left;
					int y = constraint.y - b.y;// - ReportPageFigure.PAGE_BORDER.top;
					constraint = new Rectangle(x, y, constraint.width, constraint.height);

					return super.getCreateCommand(parent, obj, constraint, index);
				}

				@Override
				protected Command createAddCommand(EditPart child, Object constraint) {
					Rectangle rect = (Rectangle) constraint;
					if (child.getModel() instanceof MGraphicElement) {
						MGraphicElement cmodel = (MGraphicElement) child.getModel();
						MCell cparent = (MCell) cmodel.getParent();
						if (cparent == getModel()) {
							SetPageConstraintCommand cmd = new SetPageConstraintCommand();
							MGraphicElement model = (MGraphicElement) child.getModel();
							Rectangle r = model.getBounds();

							JRDesignElement jde = (JRDesignElement) model.getValue();
							int x = r.x + rect.x - jde.getX() + 1;
							int y = r.y + rect.y - jde.getY() + 1;
							rect.setLocation(x, y);
							cmd.setContext(getModel(), (ANode) child.getModel(), rect);

							return cmd;
						} else {
							JSSCompoundCommand c = new JSSCompoundCommand(cmodel);

							c.add(new OrphanElementCommand(cparent, cmodel));
							c.add(new CreateElementCommand((MCell) getModel(), cmodel, rect, -1));
							return c;
						}
					}
					return null;
				}

			});
		}
	}

	@Override
	public EditPolicy getEditPolicy() {
		return null;
	}

	@Override
	protected void setupFigure(IFigure rect) {
		updateContainerSize();
		IGraphicElement model = (IGraphicElement) getModel();
		rect.setToolTip(new Label(((ANode) model).getToolTip()));

		Rectangle bounds = ((IGraphicElement) model).getBounds();
		int x = bounds.x;
		int y = bounds.y;

		rect.setLocation(new Point(x, y));

		JSSDrawVisitor dv = getDrawVisitor();
		if (rect instanceof EmptyCellFigure)
			((EmptyCellFigure) rect).setJRElement(null, dv, new Dimension(bounds.width, bounds.height));
		else if (getModel() instanceof MCell)
			((CellFigure) rect).setJRElement((JRDesignCellContents) getModel().getValue(), dv);
		rect.setSize(bounds.width + ReportPageFigure.PAGE_BORDER.left + ReportPageFigure.PAGE_BORDER.right, bounds.height + 30 + ReportPageFigure.PAGE_BORDER.top + ReportPageFigure.PAGE_BORDER.bottom);
		updateRulers();

		if (getSelected() == 1)
			updateRulers();
		else {
			List<?> selected = getViewer().getSelectedEditParts();
			if (selected.isEmpty())
				updateRulers();
			else
				for (Object obj : selected) {
					if (obj instanceof FigureEditPart) {
						FigureEditPart figEditPart = (FigureEditPart) obj;
						if (figEditPart.getModel().getParent() == getModel())
							figEditPart.updateRulers();
					}
				}
		}
	}

	@Override
	protected MCrosstab getCrosstab() {
		if (getModel() instanceof MCrosstabWhenNoDataCell)
			return ((MCrosstabWhenNoDataCell) getModel()).getCrosstab();
		if (getModel() instanceof MCrosstabWhenNoData)
			return ((MCrosstabWhenNoData) getModel()).getCrosstab();
		return null;
	}

	@Override
	protected void handlePreferenceChanged(PropertyChangeEvent event) {
		if (event.getProperty().equals(DesignerPreferencePage.P_SHOW_REPORT_BAND_NAMES))
			setBandNameShowing(getFigure());
		else if (event.getProperty().equals(DesignerPreferencePage.P_PAGE_DESIGN_BORDER_STYLE))
			setPrefsBorder(getFigure());
		else
			super.handlePreferenceChanged(event);
	}

	@Override
	protected IFigure createFigure() {
		IFigure fig = super.createFigure();
		if (fig instanceof WhenNoDataCellFigure)
			setBandNameShowing(fig);
		return fig;
	}

	public void setPrefsBorder(IFigure rect) {
		if (jConfig == null)
			jConfig = getModel().getJasperConfiguration();
		String pref = jConfig.getProperty(DesignerPreferencePage.P_PAGE_DESIGN_BORDER_STYLE, "shadow");
		if (pref.equals("shadow")) //$NON-NLS-1$
			rect.setBorder(new ShadowBorder());
		else
			rect.setBorder(new SimpleShadowBorder());
	}

	private void setBandNameShowing(IFigure figure) {
		if (figure instanceof WhenNoDataCellFigure) {
			if (jConfig == null)
				jConfig = getModel().getJasperConfiguration();
			boolean showBandName = jConfig.getPropertyBoolean(DesignerPreferencePage.P_SHOW_REPORT_BAND_NAMES, true);
			((WhenNoDataCellFigure) figure).setShowBandName(showBandName);
		}
	}
}
