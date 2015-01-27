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
package com.jaspersoft.studio.data.sql.action.expression;

import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.action.table.CreateTable;
import com.jaspersoft.studio.data.sql.dialogs.EditExpressionDialog;
import com.jaspersoft.studio.data.sql.model.enums.Operator;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.query.MHaving;
import com.jaspersoft.studio.data.sql.model.query.MWhere;
import com.jaspersoft.studio.data.sql.model.query.expression.AMExpression;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpression;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpressionGroup;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.operand.FieldOperand;
import com.jaspersoft.studio.data.sql.model.query.operand.ParameterPOperand;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.widgets.Factory;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class CreateExpression extends AAction {
	private CreateTable ct;
	private SQLQueryDesigner designer;

	public CreateExpression(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super("Add E&xpression", treeViewer);
		this.designer = designer;
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && isInSelect(selection[0]);
	}

	public static boolean isInSelect(Object element) {
		return element instanceof MWhere || element instanceof MHaving || element instanceof AMExpression || element instanceof MFromTableJoin || element instanceof MExpressionGroup;
	}

	@Override
	public void run() {
		Object sel = selection[0];
		MExpression mexpr = null;
		if (sel instanceof AMExpression)
			mexpr = run(null, (AMExpression<?>) sel);
		else if (isInSelect(sel))
			mexpr = run(null, (ANode) sel, -1);
		mexpr.getOperands().add(Factory.getDefaultOperand(mexpr));
		mexpr.getOperands().add(Factory.getDefaultOperand(mexpr));
		showDialog(mexpr);
	}

	public void run(List<JRDesignParameter> prms) {
		Object sel = selection[0];
		MExpression mexpr = null;
		List<MFromTable> tbls = Util.getFromTables((ANode) sel);
		for (JRParameter t : prms) {
			if (sel instanceof AMExpression)
				mexpr = run(t, (AMExpression<?>) sel);
			else if (isInSelect(sel))
				mexpr = run(t, (ANode) sel, -1);
			sel = mexpr;
			mexpr.getOperands().add(Factory.getDefaultOperand(mexpr));
			ParameterPOperand pop = new ParameterPOperand(mexpr);
			pop.setJrParameter((JRDesignParameter) t);
			mexpr.getOperands().add(pop);
		}
		showDialog(mexpr);
	}

	public void run(Collection<MSQLColumn> nodes) {
		Object sel = selection[0];
		MExpression mexpr = null;
		List<MFromTable> tbls = Util.getFromTables((ANode) sel);
		for (MSQLColumn t : nodes) {
			MSqlTable tbl = (MSqlTable) t.getParent();
			MFromTable mftable = null;
			for (MFromTable ft : tbls) {
				if (ft.getValue().equals(tbl)) {
					mftable = ft;
					break;
				}
			}
			if (mftable == null) {
				if (ct == null)
					ct = new CreateTable(designer, treeViewer);
				ANode r = Util.getQueryRoot((ANode) sel);
				for (INode n : r.getChildren()) {
					if (n instanceof MFrom) {
						mftable = ct.run(tbl, (MFrom) n, -1);
						break;
					}
				}
			}
			if (sel instanceof AMExpression)
				mexpr = run(t, (AMExpression<?>) sel);
			else if (isInSelect(sel))
				mexpr = run(t, (ANode) sel, -1);
			sel = mexpr;
			mexpr.getOperands().add(new FieldOperand(t, mftable, mexpr));
			mexpr.getOperands().add(Factory.getDefaultOperand(mexpr));
		}
		showDialog(mexpr);
	}

	public void run(ANode node, MSelectColumn selcol) {
		MExpression mexpr = run(selcol.getValue(), node, -1);
		mexpr.getOperands().add(new FieldOperand(selcol.getValue(), selcol.getMFromTable(), mexpr));
		mexpr.getOperands().add(Factory.getDefaultOperand(mexpr));
		showDialog(mexpr);
	}

	protected void showDialog(MExpression mexpr) {
		EditExpressionDialog dialog = new EditExpressionDialog(Display.getDefault().getActiveShell());
		dialog.setValue(mexpr);
		if (dialog.open() == Dialog.OK) {
			mexpr.setOperator(Operator.getOperator((dialog.getOperator())));
			mexpr.setPrevCond(dialog.getPrevcond());
			mexpr.setOperands(dialog.getOperands());
			selectInTree(mexpr);
		} else {
			ANode p = mexpr.getParent();
			p.removeChild(mexpr);
			selectInTree(p);
		}
	}

	protected MExpression run(Object node, AMExpression<?> mtable) {
		ANode mfrom = mtable.getParent();
		return run(node, mfrom, mfrom.getChildren().indexOf(mtable) + 1);
	}

	public MExpression run(Object node, ANode select, int index) {
		return new MExpression(select, node, index);
	}

}
