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
package com.jaspersoft.studio.data.sql.ui.gef.parts;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.table.EditTableJoin;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.IQueryString;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.from.TableJoin;
import com.jaspersoft.studio.data.sql.ui.gef.SQLQueryDiagram;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;

public class RelationshipPart extends AbstractConnectionEditPart {

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
	}

	@Override
	public TableJoin getModel() {
		return (TableJoin) super.getModel();
	};

	@Override
	public void performRequest(Request req) {
		if (RequestConstants.REQ_OPEN.equals(req.getType())) {
			SQLQueryDesigner designer = (SQLQueryDesigner) getViewer().getProperty(SQLQueryDiagram.SQLQUERYDIAGRAM);
			EditTableJoin ct = designer.getOutline().getAfactory().getAction(EditTableJoin.class);
			if (ct.calculateEnabled(new Object[] { getModel().getJoinTable() }))
				ct.run();
		}
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		PolylineConnection conn = (PolylineConnection) super.createFigure();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		return conn;
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		MFromTableJoin joinTable = getModel().getJoinTable();
		final StringBuffer tt = new StringBuffer();
		tt.append(getModel().getFromTable().getToolTip()).append(" ").append(joinTable.getToolTip());
		new ModelVisitor<Object>(joinTable) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof IQueryString)
					tt.append(((IQueryString) n).toSQLString());
				return true;
			}
		};
		PolylineConnection f = (PolylineConnection) getFigure();
		f.setToolTip(new Label(tt.toString()));
		// f.setLayoutManager(new DelegatingLayout());
		//
		// Label label = new Label("middle");
		// f.add(label, new ConnectionLocator(f, ConnectionLocator.MIDDLE));

		if (joinTable.getJoin().equals(AMKeyword.INNER_JOIN)) {
			f.setTargetDecoration(getInnerDecoration());
			f.setSourceDecoration(getInnerDecoration());
		} else if (joinTable.getJoin().equals(AMKeyword.LEFT_OUTER_JOIN)) {
			f.setTargetDecoration(getOuterDecoration());
			f.setSourceDecoration(getInnerDecoration());
		} else if (joinTable.getJoin().equals(AMKeyword.RIGHT_OUTER_JOIN)) {
			f.setTargetDecoration(getInnerDecoration());
			f.setSourceDecoration(getOuterDecoration());
		} else if (joinTable.getJoin().equals(AMKeyword.FULL_OUTER_JOIN)) {
			f.setTargetDecoration(getOuterDecoration());
			f.setSourceDecoration(getOuterDecoration());
		} else if (joinTable.getJoin().equals(AMKeyword.CROSS_JOIN)) {
			f.setTargetDecoration(getCrossDecoration());
			f.setSourceDecoration(getCrossDecoration());
		}
	}

	private RotatableDecoration getInnerDecoration() {
		PolygonDecoration srcpd = new PolygonDecoration();
		srcpd.setTemplate(JOINDECORATOR);
		srcpd.setBackgroundColor(ColorConstants.black);
		return srcpd;
	}

	private RotatableDecoration getOuterDecoration() {
		PolygonDecoration srcpd = new PolygonDecoration();
		srcpd.setTemplate(JOINDECORATOR);
		srcpd.setBackgroundColor(ColorConstants.white);
		return srcpd;
	}

	private RotatableDecoration getCrossDecoration() {
		PolygonDecoration srcpd = new PolygonDecoration();
		srcpd.setTemplate(JOINDECORATOR);
		srcpd.setBackgroundColor(ColorConstants.lightGray);
		return srcpd;
	}

	public static final PointList JOINDECORATOR = new PointList();

	static {
		JOINDECORATOR.addPoint(0, 0);
		JOINDECORATOR.addPoint(-1, 2);
		JOINDECORATOR.addPoint(-2, 0);
		JOINDECORATOR.addPoint(-1, -2);
		JOINDECORATOR.addPoint(0, 0);
	}
}
