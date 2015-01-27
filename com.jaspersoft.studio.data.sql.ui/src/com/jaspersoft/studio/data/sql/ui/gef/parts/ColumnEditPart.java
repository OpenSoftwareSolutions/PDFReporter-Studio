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

import net.sf.jasperreports.eclipse.JasperReportsPlugin;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.ActionFactory;
import com.jaspersoft.studio.data.sql.action.select.CreateColumn;
import com.jaspersoft.studio.data.sql.action.select.DeleteColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.ui.gef.SQLQueryDiagram;
import com.jaspersoft.studio.data.sql.ui.gef.figures.ColumnFigure;

public class ColumnEditPart extends AbstractGraphicalEditPart {

	private String colname;
	private MSelectColumn mSelCol;
	private ActionFactory afactory;
	private MSelect mselect;

	@Override
	protected IFigure createFigure() {
		colname = getModel().getValue();
		Image image = null;
		ImageDescriptor imgd = getModel().getImagePath();
		if (imgd != null)
			image = JasperReportsPlugin.getDefault().getImage(imgd);
		SQLQueryDesigner designer = (SQLQueryDesigner) getViewer().getProperty(SQLQueryDiagram.SQLQUERYDIAGRAM);
		afactory = designer.getOutline().getAfactory();
		final MFromTable tblModel = ColumnEditPart.this.getParent().getModel();
		mselect = Util.getKeyword(tblModel, MSelect.class);
		ColumnFigure cbfig = new ColumnFigure(colname, image) {
			@Override
			protected void handleSelectionChanged() {
				super.handleSelectionChanged();
				if (isRefreshing)
					return;
				if (isSelected()) {
					CreateColumn ct = afactory.getAction(CreateColumn.class);
					if (ct.calculateEnabled(new Object[] { mselect }))
						ct.run(ColumnEditPart.this.getModel(), tblModel);
				} else {
					if (ColumnEditPart.this.getParent().isAllstar()) {
						// get all columns from tables
						// remove *
						boolean tmp = isRefreshing;
						isRefreshing = true;
						setSelected(true);
						isRefreshing = tmp;
						return;
					}
					DeleteColumn dc = afactory.getAction(DeleteColumn.class);
					if (dc.calculateEnabled(new Object[] { mSelCol }))
						dc.run();
				}
				refreshVisuals();
			}
		};
		cbfig.setToolTip(new Label(getModel().getToolTip()));
		return cbfig;
	}

	@Override
	public TableEditPart getParent() {
		return (TableEditPart) super.getParent();
	}

	@Override
	public ColumnFigure getFigure() {
		return (ColumnFigure) super.getFigure();
	}

	private boolean isRefreshing = false;

	@Override
	protected void refreshVisuals() {
		isRefreshing = true;
		if (getParent().isAllstar())
			getFigure().setSelected(true);
		else {
			mSelCol = getParent().getColumnMap().get(colname);
			getFigure().setSelected(mSelCol != null);
		}
		isRefreshing = false;
	}

	public MSelectColumn getmSelectColumn() {
		return mSelCol;
	}

	@Override
	public MSQLColumn getModel() {
		return (MSQLColumn) super.getModel();
	}

	@Override
	protected void createEditPolicies() {
		// installEditPolicy(EditPolicy.LAYOUT_ROLE, new
		// ColumnLayoutEditPolicy());
		// installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new
		// TableNodeEditPolicy());
	}

}
