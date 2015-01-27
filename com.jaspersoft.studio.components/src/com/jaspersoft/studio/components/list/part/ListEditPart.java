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
package com.jaspersoft.studio.components.list.part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.callout.CalloutEditPart;
import com.jaspersoft.studio.callout.command.CalloutSetConstraintCommand;
import com.jaspersoft.studio.callout.pin.PinEditPart;
import com.jaspersoft.studio.callout.pin.command.PinSetConstraintCommand;
import com.jaspersoft.studio.compatibility.ToolUtilitiesCompatibility;
import com.jaspersoft.studio.components.SubEditorEditPartTracker;
import com.jaspersoft.studio.components.list.ListComponentFactory;
import com.jaspersoft.studio.components.list.figure.ListFigure;
import com.jaspersoft.studio.components.list.model.MList;
import com.jaspersoft.studio.editor.action.create.CreateElementAction;
import com.jaspersoft.studio.editor.gef.commands.SetPageConstraintCommand;
import com.jaspersoft.studio.editor.gef.parts.EditableFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.SnapToGeometryThreshold;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.FigurePageLayoutEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.FigureSelectionEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.SearchParentDragTracker;
import com.jaspersoft.studio.editor.outline.OutlineTreeEditPartFactory;
import com.jaspersoft.studio.editor.outline.editpolicy.CloseSubeditorDeletePolicy;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;

/**
 * 
 * @author Chicu Veaceslav & Orlandin Marco
 * 
 */
public class ListEditPart extends EditableFigureEditPart {

	/**
	 * Create a ListFigure and initialize it. The listfigure is the type of the
	 * figure of this edit part
	 */
	protected IFigure createFigure() {
		ListFigure rect = new ListFigure((MList)getModel());
		setupListFigure(rect);
		setPrefsBorder(rect);
		setupFigure(rect);
		figure = rect;
		return rect;
	}

	/**
	 * Set in the list figure the size of the cell
	 * 
	 * @param rect
	 *            a list figure
	 */
	protected void setupListFigure(IFigure rect) {
		MList model = (MList) getModel();

		ListFigure lfig = (ListFigure) rect;

		Integer h = (Integer) model.getPropertyValue(MList.PREFIX
				+ DesignListContents.PROPERTY_HEIGHT);
		if (h == null)
			h = (Integer) model
					.getPropertyValue(JRDesignElement.PROPERTY_HEIGHT);
		lfig.setCellHeight(h);

		Integer w = (Integer) (model.getPropertyValue(MList.PREFIX
				+ DesignListContents.PROPERTY_WIDTH));
		if (w == null)
			w = (Integer) model
					.getPropertyValue(JRDesignElement.PROPERTY_WIDTH);
		lfig.setCellWidth(w);
	}

	/**
	 * Reset the size of the cell in the list figure and refresh the element
	 */
	@Override
	public void refreshVisuals() {
		setupListFigure(getFigure());
		super.refreshVisuals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new CloseSubeditorDeletePolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new FigurePageLayoutEditPolicy() {

					@Override
					protected Command getCreateCommand(CreateRequest request) {
						Rectangle constraint = (Rectangle) getConstraintFor(request);

						if (request.getNewObject() instanceof CreateElementAction) {
							CreateElementAction action = (CreateElementAction) request
									.getNewObject();
							action.dropInto(getHost().getModel(),
									constraint.getCopy(), -1);
							action.run();
							return action.getCommand();
						} else if (request.getNewObject() instanceof MGraphicElement) {
							return OutlineTreeEditPartFactory.getCreateCommand((ANode) getHost().getModel(),(ANode) request.getNewObject(),constraint.getCopy(), -1);
						} else if (request.getNewObject() instanceof Collection<?>) {
							JSSCompoundCommand cmd = new JSSCompoundCommand(null);
							Collection<?> c = (Collection<?>) request.getNewObject();
							for (Object obj : c) {
								if (obj instanceof ANode) {
									ANode aObj = (ANode) obj;
									cmd.setReferenceNodeIfNull(aObj);
									cmd.add(OutlineTreeEditPartFactory .getCreateCommand((ANode) getHost().getModel(), aObj, constraint.getCopy(), -1));
								}
							}
							return cmd;
						}
						return null;
					}

					@Override
					protected Command createAddCommand(EditPart child,
							Object constraint) {
						Rectangle rect = (Rectangle) constraint;
						if (child.getModel() instanceof MGraphicElement) {
							MGraphicElement cmodel = (MGraphicElement) child
									.getModel();
							if (cmodel.getParent() instanceof MList) {
								MList cparent = (MList) cmodel.getParent();
								if (cparent == getModel()) {
									Rectangle r = cmodel.getBounds();
									SetPageConstraintCommand cmd = new SetPageConstraintCommand();
									JRDesignElement jde = (JRDesignElement) cmodel
											.getValue();
									int x = r.x + rect.x - jde.getX() + 2;
									int y = r.y + rect.y - jde.getY() + 2;
									rect.setLocation(x, y);
									cmd.setContext(
											(ANode) getHost().getModel(),
											(ANode) child.getModel(), rect);

									return cmd;
								}
							} else {
								JSSCompoundCommand c = new JSSCompoundCommand(cmodel);
								c.add(OutlineTreeEditPartFactory.getOrphanCommand(cmodel.getParent(),cmodel));
								c.add(new CreateElementCommand((MList) getModel(), cmodel, rect, -1));
								return c;
							}
						} else if (child instanceof CalloutEditPart) {
							return new CalloutSetConstraintCommand(
									((CalloutEditPart) child).getModel(),
									adaptConstraint(constraint));
						} else if (child instanceof PinEditPart) {
							return new PinSetConstraintCommand(
									((PinEditPart) child).getModel(),
									adaptConstraint(constraint));

						} else {
							return super.createChangeConstraintCommand(child,
									constraint);
						}
						return null;
					}

				});
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new FigureSelectionEditPolicy());
	}

	@Override
	public void performRequest(Request req) {
		if (RequestConstants.REQ_OPEN.equals(req.getType())) {
			MList model = (MList) getModel();
			Command c = ListComponentFactory.INST().getStretchToContent(model);
			if (c != null)
				getViewer().getEditDomain().getCommandStack().execute(c);
		}
		super.performRequest(req);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if (key == SnapToHelper.class) {
			List<SnapToHelper> snapStrategies = new ArrayList<SnapToHelper>();
			Boolean val = jConfig.getPropertyBoolean(
					RulersGridPreferencePage.P_PAGE_RULERGRID_SHOWRULER,
					Boolean.TRUE);
			Boolean stg = jConfig.getPropertyBoolean(
					RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGUIDES,
					Boolean.TRUE);
			if (val.booleanValue() && stg != null && stg.booleanValue())
				snapStrategies.add(new SnapToGuides(this));
			val = jConfig.getPropertyBoolean(
					RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGEOMETRY,
					Boolean.TRUE);
			if (val.booleanValue()) {

				SnapToGeometryThreshold snapper = new SnapToGeometryThreshold(
						this);
				snapper.setThreshold(2.0);
				snapStrategies.add(snapper);
			}
			val = jConfig.getPropertyBoolean(
					RulersGridPreferencePage.P_PAGE_RULERGRID_SNAPTOGRID,
					Boolean.TRUE);
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
	public DragTracker getDragTracker(Request request) {
		if (ToolUtilitiesCompatibility.isSubeditorMainElement(this)) return new SubEditorEditPartTracker(this);
		else return new SearchParentDragTracker(this);
	}

}
