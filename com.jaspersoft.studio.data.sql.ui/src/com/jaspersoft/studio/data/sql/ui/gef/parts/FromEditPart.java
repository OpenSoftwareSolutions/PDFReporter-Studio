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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.data.sql.QueryWriter;
import com.jaspersoft.studio.data.sql.model.ISubQuery;
import com.jaspersoft.studio.data.sql.model.query.MUnion;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.data.sql.ui.gef.layout.GraphLayoutManager;
import com.jaspersoft.studio.data.sql.ui.gef.policy.FromContainerEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.NoSelectionEditPolicy;
import com.jaspersoft.studio.model.AMapElement;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.Misc;

public class FromEditPart extends AbstractGraphicalEditPart {

	private static final Insets INSETS = new Insets(10, 10, 10, 10);

	@Override
	protected IFigure createFigure() {
		RoundedRectangle fig = new RoundedRectangle() {
			@Override
			public Insets getInsets() {
				return INSETS;
			}
		};
		fig.setLayoutManager(new FromLayout());
		fig.setBackgroundColor(SWTResourceManager.getColor(248, 248, 255));
		fig.setOpaque(true);
		return fig;
	}

	@Override
	public MFrom getModel() {
		return (MFrom) super.getModel();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new FromContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy() {

			@Override
			protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
				Rectangle r = (Rectangle) constraint;
				if (child instanceof TableEditPart) {
					SetValueCommand cmd = new SetValueCommand();
					cmd.setPropertyId(MFromTable.PROP_X);
					cmd.setPropertyValue(new Point(Math.max(0, r.x), Math.max(0, r.y)));
					cmd.setTarget(((TableEditPart) child).getModel());
					return cmd;
				}
				return super.createChangeConstraintCommand(request, child, constraint);
			}

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}

			@Override
			protected EditPolicy createChildEditPolicy(EditPart child) {
				if (child instanceof TableEditPart)
					return new NoSelectionEditPolicy();
				return super.createChildEditPolicy(child);
			}

			@Override
			protected Command createChangeConstraintCommand(EditPart child,
					Object constraint) {
				return createChangeConstraintCommand(null, child, constraint);
			}
		});
	}

	@Override
	protected List<?> getModelChildren() {
		final List<ANode> list = new ArrayList<ANode>();
		final INode mfrom = (INode) getModel();
		new ModelVisitor<ANode>(mfrom) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MUnion || n instanceof ISubQuery)
					return false;
				if (n instanceof MFromTable) {
					if (n.getValue() instanceof MQueryTable)
						return false;
					else
						list.add((ANode) n);
					if (((MFromTable) n).getPropertyActualValue(MFromTable.PROP_X) == null)
						layout = false;
				}
				return true;
			}
		};
		// look for subquery return MFrom ...
		new ModelVisitor<ANode>(mfrom.getParent()) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MFrom && n != mfrom) {
					list.add((ANode) n);
					return false;
				}
				return true;
			}
		};
		return list;
	}

	protected void refreshVisuals() {
		RoundedRectangle f = (RoundedRectangle) getFigure();
		setupLayoutManager();
		MFrom mfrom = getModel();
		getFigure().setToolTip(new Label(QueryWriter.writeSubQuery(mfrom.getParent())));

		AbstractGraphicalEditPart parent = (AbstractGraphicalEditPart) getParent();
		Point location = f.getLocation();
		if (mfrom.getPropertyActualValue(MFromTable.PROP_X) != null)
			location.x = (Integer) mfrom.getPropertyValue(MFromTable.PROP_X);
		if (mfrom.getPropertyActualValue(MFromTable.PROP_Y) != null)
			location.y = (Integer) mfrom.getPropertyValue(MFromTable.PROP_Y);
		parent.setLayoutConstraint(this, f, new Rectangle(location.x, location.y, -1, -1));
	}

	private boolean layout = false;
	private boolean isRunning = false;

	protected void setupLayoutManager() {
		if (layout)
			return;
		layout = true;
		if (!isRunning) {
			isRunning = true;
			if (Misc.nvl(new ModelVisitor<Boolean>(getModel()) {
				@Override
				public boolean visit(INode n) {
					if (n instanceof AMapElement && ((AMapElement) n).getPropertyActualValue(MFromTable.PROP_X) == null) {
						setObject(Boolean.FALSE);
						stop();
					}
					return false;
				}
			}.getObject(), Boolean.TRUE)) {
				getFigure().setLayoutManager(new FromLayout());
				isRunning = false;
			} else {
				getFigure().setLayoutManager(new GraphLayoutManager(this));
				UIUtils.getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						Map<AMapElement, Rectangle> map = new HashMap<AMapElement, Rectangle>();
						for (Object p : getChildren()) {
							if (p instanceof TableEditPart) {
								TableEditPart aep = (TableEditPart) p;
								Rectangle b = aep.getFigure().getBounds();
								map.put(aep.getModel(), b);
							}
						}
						for (AMapElement key : map.keySet()) {
							Rectangle b = map.get(key);
							key.setNoEvents(true);
							key.setPropertyValue(MFromTable.PROP_X, b.x);
							key.setPropertyValue(MFromTable.PROP_Y, b.y);
							key.setNoEvents(false);
						}
						getFigure().setLayoutManager(new FromLayout());
						// getFigure().setSize(400, 400);
						isRunning = false;
					}
				});
			}
		}
	}

	class FromLayout extends XYLayout {

		protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
			container.validate();
			List<IFigure> children = container.getChildren();
			Rectangle result = new Rectangle().setLocation(container.getClientArea().getLocation());
			for (IFigure c : children)
				result.union(c.getBounds());
			Insets ins = container.getInsets();
			result.resize(ins.getWidth(), ins.getHeight());
			return result.getSize();
		}
	}
}
