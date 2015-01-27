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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.HandleBounds;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.gef.commands.SetPageConstraintCommand;
import com.jaspersoft.studio.editor.gef.figures.ReportPageFigure;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.ColoredRectangle;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.FigureSelectionEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.PageLayoutEditPolicy;
import com.jaspersoft.studio.editor.outline.OutlineTreeEditPartFactory;
import com.jaspersoft.studio.editor.outline.editpolicy.CloseSubeditorDeletePolicy;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;

public class FrameFigureEditPart extends FigureEditPart implements IContainer {

	/**
	 * Color of the feedback when an element is dragged into the frame
	 */
	public static Color addElementColor = new Color(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), 128);

	/**
	 * figure of the target feedback
	 */
	private RectangleFigure targetFeedback;

	@Override
	public MFrame getModel() {
		return (MFrame) super.getModel();
	}

	/**
	 * True if the frame figure has a target figure feedback set, otherwise false
	 * 
	 * @return
	 */
	public boolean hasTargetFeedBack() {
		return targetFeedback != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// super.createEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new CloseSubeditorDeletePolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new FigureSelectionEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new PageLayoutEditPolicy() {

			@Override
			protected Command getCreateCommand(ANode parent, Object obj, Rectangle constraint, int index) {
				if (parent instanceof MPage)
					parent = getModel();
				Rectangle b = getModel().getBounds();
				int x = constraint.x - b.x - ReportPageFigure.PAGE_BORDER.left;
				int y = constraint.y - b.y - ReportPageFigure.PAGE_BORDER.top;
				constraint = new Rectangle(x, y, constraint.width, constraint.height);
				return super.getCreateCommand(parent, obj, constraint, index);
			}

			@Override
			protected Command createAddCommand(EditPart child, Object constraint) {
				Rectangle rect = (Rectangle) constraint;
				if (child.getModel() instanceof MGraphicElement) {
					MGraphicElement cmodel = (MGraphicElement) child.getModel();
					//if the parent of the child its a frame, then its a SetPageConstraintCommand because
					//i'm moving the element inside the frame
					if (cmodel.getParent() instanceof MFrame && cmodel.getParent() == getModel()) {
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
						JSSCompoundCommand c = new JSSCompoundCommand(getModel());
						// Without this an element it's one point up when placed into a frame
						rect.y++;
						c.add(OutlineTreeEditPartFactory.getOrphanCommand(cmodel.getParent(), cmodel));
						c.add(new CreateElementCommand(getModel(), cmodel, rect, -1));
						return c;
					}
				} else {
					return super.createChangeConstraintCommand(child, constraint);
				}
			}

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
				if (targetFeedback == null) {
					targetFeedback = new ColoredRectangle(addElementColor, 2.0f);
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
}
