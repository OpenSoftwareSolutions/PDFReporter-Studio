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
package com.jaspersoft.studio.editor.gef.parts.band;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.callout.CalloutEditPart;
import com.jaspersoft.studio.callout.command.CalloutSetConstraintCommand;
import com.jaspersoft.studio.callout.pin.PinEditPart;
import com.jaspersoft.studio.callout.pin.command.PinSetConstraintCommand;
import com.jaspersoft.studio.editor.gef.figures.BandFigure;
import com.jaspersoft.studio.editor.gef.figures.ReportPageFigure;
import com.jaspersoft.studio.editor.gef.parts.APrefFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.FrameFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.IContainerPart;
import com.jaspersoft.studio.editor.gef.parts.ReportPageEditPart;
import com.jaspersoft.studio.editor.gef.parts.SnapToGeometryThreshold;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.ColoredRectangle;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.JSSSnapFeedBackPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.PageLayoutEditPolicy;
import com.jaspersoft.studio.editor.outline.OutlineTreeEditPartFactory;
import com.jaspersoft.studio.editor.outline.editpolicy.CloseSubeditorDeletePolicy;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.property.dataset.dialog.IDatasetDialogSupport;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * BandEditPart creates the figure for the band. The figure is actually just the bottom border of the band. This allows
 * to drag this border to resize the band. The PageEditPart sets a specific contraint for the BandEditPart elements in
 * order to make them move only vertically. The BandMoveEditPolicy is responsable for the feedback when the band is
 * dragged.
 * 
 * @author Chicu Veaceslav, Giulio Toffoli
 */
public class BandEditPart extends APrefFigureEditPart implements PropertyChangeListener, IContainerPart, IContainer,
		IDatasetDialogSupport {

	@Override
	protected void handlePreferenceChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
		if (event.getProperty().equals(DesignerPreferencePage.P_SHOW_REPORT_BAND_NAMES)) {
			setBandNameShowing(getFigure());
		} else
			super.handlePreferenceChanged(event);
	}

	@Override
	public BandFigure getFigure() {
		return (BandFigure) super.getFigure();
	}

	/**
	 * Gets the band.
	 * 
	 * @return the band
	 */
	public JRDesignBand getBand() {
		return getModel().getValue();
	}

	@Override
	public MBand getModel() {
		return (MBand) super.getModel();
	}

	public void performRequest(Request req) {
		if (RequestConstants.REQ_OPEN.equals(req.getType())) {
			int bandHeight = ModelUtils.getBandHeight(getBand());
			if (bandHeight > 0) {
				SetValueCommand cmd = new SetValueCommand();
				cmd.setTarget((IPropertySource) getModel());
				cmd.setPropertyId(JRDesignBand.PROPERTY_HEIGHT);
				cmd.setPropertyValue(bandHeight);
				getViewer().getEditDomain().getCommandStack().execute(cmd);
			}
		}
		super.performRequest(req);
	}

	/**
	 * Gets the jasper design.
	 * 
	 * @return the jasper design
	 */
	public JasperDesign getJasperDesign() {
		try {
			return ((MBand) getModel()).getJasperDesign();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * A different drag tracker will be used to allow to do a drag selection without selecting the marquee tool
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return new NotMovablePartDragTracker(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		JRDesignBand jrBand = getBand();
		BandTypeEnum bandTypeValue = jrBand.getOrigin().getBandTypeValue();
		boolean drawColumns = bandTypeValue.equals(BandTypeEnum.COLUMN_FOOTER)
				|| bandTypeValue.equals(BandTypeEnum.COLUMN_HEADER) || bandTypeValue.equals(BandTypeEnum.GROUP_FOOTER)
				|| bandTypeValue.equals(BandTypeEnum.GROUP_HEADER) || bandTypeValue.equals(BandTypeEnum.COLUMN_HEADER)
				|| bandTypeValue.equals(BandTypeEnum.DETAIL);

		BandFigure rect = new BandFigure(drawColumns, getModel());
		rect.setForegroundColor(ColorConstants.blue);
		setupBandFigure(rect);

		figure = rect;
		setBandNameShowing(rect);
		setMarginColor();
		return rect;
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
				snapper.setThreshold(2.0);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new CloseSubeditorDeletePolicy());
		installEditPolicy("Snap Feedback", new JSSSnapFeedBackPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new BandMoveEditPolicy() {
			@Override
			protected void showSelection() {
				updateRulers();
			}

		});
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new PageLayoutEditPolicy() {

			@Override
			protected Command getCreateCommand(ANode parent, Object obj, Rectangle constraint, int index) {
				Rectangle rect = ((Rectangle) constraint).getCopy();
				rect = rect.getTranslated(-ReportPageFigure.PAGE_BORDER.left, -ReportPageFigure.PAGE_BORDER.right);
				return super.getCreateCommand(parent, obj, rect, index);
			}
			

			@Override
			protected Command createAddCommand(EditPart child, Object constraint) {
				Rectangle rect = ((Rectangle) constraint).getCopy();
				rect = rect.getTranslated(-ReportPageFigure.PAGE_BORDER.left, -ReportPageFigure.PAGE_BORDER.right);
				if (child.getModel() instanceof MGraphicElement) {
					MGraphicElement cmodel = (MGraphicElement) child.getModel();
					MBand mband = getModel();
					//REMOVED CODE FOR Bug 38971. However is not good that the band itself can decide the target of the creation
					//operation. The creation is requested by the SearchParentDragTracker and it requested it on the current edit
					//part, so it want the command for the edit part where the request was done. The logic to choose the 
					//parent should be in the drag tracker, not there
					//MBand hoveredBand = ModelUtils.getBand4Point(mband.getRoot(),new Point(rect.x, rect.y));
					//if (hoveredBand != null) mband = hoveredBand;
					if (cmodel.getParent() instanceof MBand && cmodel.getParent() == mband) {
						return super.createChangeConstraintCommand(child, rect);
					} else {
						JSSCompoundCommand c = new JSSCompoundCommand(mband);

						c.add(OutlineTreeEditPartFactory.getOrphanCommand(cmodel.getParent(), cmodel));
						c.add(new CreateElementCommand(mband, cmodel, CreateElementCommand.fixLocation(rect, mband,
								cmodel.getValue()), -1));
						return c;
					}
				} else if (child instanceof CalloutEditPart) {
					return new CalloutSetConstraintCommand(((CalloutEditPart) child).getModel(), adaptConstraint(constraint));
				} else if (child instanceof PinEditPart) {
					return new PinSetConstraintCommand(((PinEditPart) child).getModel(), adaptConstraint(constraint));
				}
				return null;
			}

			private RectangleFigure targetFeedback;

			/**
			 * Show the feedback during drag and drop
			 */
			protected void showLayoutTargetFeedback(Request request) {
				super.showLayoutTargetFeedback(request);
				getLayoutTargetFeedback(request);
			}

			/**
			 * Erase the feedback from a ban when no element is dragged into it
			 */
			protected void eraseLayoutTargetFeedback(Request request) {
				super.eraseLayoutTargetFeedback(request);
				if (targetFeedback != null) {
					removeFeedback(targetFeedback);
					targetFeedback = null;
				}
			}

			/**
			 * Paint the figure to give the feedback, a blue border overlapping the band border
			 * 
			 * @param request
			 * @return feedback figure
			 */
			protected IFigure getLayoutTargetFeedback(Request request) {
				if (request.getType().equals(RequestConstants.REQ_CREATE) && request instanceof CreateRequest) {
					CreateRequest cbr = (CreateRequest) request;
					if (cbr.getNewObject() instanceof Collection<?>) {
						Collection<?> c = (Collection<?>) cbr.getNewObject();
						if (!c.isEmpty() && c.iterator().next() instanceof MStyle)
							return null;
					}
				} else if (request instanceof ChangeBoundsRequest) {
					ChangeBoundsRequest cbr = (ChangeBoundsRequest) request;
					List<EditPart> lst = cbr.getEditParts();
					for (EditPart ep : lst)
						if (((ANode) ep.getModel()).getParent() == getModel())
							return null;
				}
				if (targetFeedback == null) {
					targetFeedback = new ColoredRectangle(FrameFigureEditPart.addElementColor, 2.0f);
					targetFeedback.setFill(false);
					IFigure hostFigure = getHostFigure();
					Rectangle bounds = hostFigure.getBounds();
					if (hostFigure instanceof HandleBounds)
						bounds = ((HandleBounds) hostFigure).getHandleBounds();
					Rectangle rect = new PrecisionRectangle(bounds);
					getHostFigure().translateToAbsolute(rect);
					getFeedbackLayer().translateToRelative(rect);

					targetFeedback.setBounds(rect);
					addFeedback(targetFeedback);
				}
				return targetFeedback;
			}

		});
	}

	public EditPolicy getEditPolicy() {
		return new BandResizableEditPolicy();
	}

	@Override
	public boolean isSelectable() {
		return true;
	}

	/**
	 * Class name.
	 * 
	 * @param cls
	 *          the cls
	 * @return the string
	 */
	public static String className(Object cls) {
		String className = cls.getClass().getName();
		return className.substring(className.lastIndexOf('.') + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	public void refreshVisuals() {
		IFigure rect = getFigure();
		setupBandFigure(rect);
		if (Display.getCurrent() != null)
			rect.repaint();
	}

	private void setupBandFigure(IFigure rect) {
		JRDesignBand jrBand = getBand();
		MBand bandNode = (MBand) getModel();
		Rectangle bounds = (bandNode).getBounds();
		JasperDesign jasperDesign = getJasperDesign();
		BandFigure bfig = (BandFigure) rect;
		bfig.setMarginLeft(jasperDesign.getLeftMargin());
		bfig.setMarginRight(jasperDesign.getRightMargin());
		bfig.setColumnNumber(jasperDesign.getColumnCount());
		bfig.setColumnWidth(jasperDesign.getColumnWidth());
		bfig.setColumnSpacing(jasperDesign.getColumnSpacing());

		// int width = jasperDesign.getPageWidth() + PageFigure.PAGE_BORDER.left + 1;
		int width = jasperDesign.getPageWidth() + 1;
		int height = jrBand != null ? jrBand.getHeight() + 1 : 0;
		rect.setBounds(new Rectangle(ReportPageFigure.PAGE_BORDER.left, bounds.y + ReportPageFigure.PAGE_BORDER.top, width,
				height));
		// rect.setBounds(new Rectangle(0, bounds.y + PageFigure.PAGE_BORDER.top, width, height));

		// update tooltip and band text (shown in background)
		bfig.setToolTip(new Label(bandNode.getToolTip()));
		bfig.setBandText(bandNode.getSimpleDisplayName());

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
						if (figEditPart.getModel().getParent() == bandNode)
							figEditPart.updateRulers();
					}
				}
		}
	}

	@Override
	public void updateRulers() {
		EditPart ep = getParent();
		if (ep instanceof ReportPageEditPart)
			((ReportPageEditPart) ep).updateRullers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (getParent() != null)
			getParent().refresh();
	}

	public Object getConstraintFor(ChangeBoundsRequest request, GraphicalEditPart child) {
		if (request.getResizeDirection() == PositionConstants.SOUTH
				|| request.getResizeDirection() == PositionConstants.NORTH)
			System.out.println(" Constraint request:  " + request.getSizeDelta() + "  " + request.getResizeDirection()); //$NON-NLS-1$ //$NON-NLS-2$
		return new Rectangle(0, 0, 0, request.getSizeDelta().height);
	}

	/*
	 * Update flag for band name showing.
	 */
	private void setBandNameShowing(BandFigure figure) {
		if (jConfig == null)
			jConfig = getModel().getJasperConfiguration();
		boolean showBandName = jConfig.getPropertyBoolean(DesignerPreferencePage.P_SHOW_REPORT_BAND_NAMES, true);
		figure.setShowBandName(showBandName);
		refreshVisuals();
	}

	public Dimension getContaierSize() {
		return null;
	}

	protected void setupMarginColor() {
		((BandFigure) figure).setMarginsColor(getMarginColor());
	}
}
