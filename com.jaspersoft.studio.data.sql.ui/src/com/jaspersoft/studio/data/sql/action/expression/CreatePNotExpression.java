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

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.dialogs.EditPNotExpressionDialog;
import com.jaspersoft.studio.data.sql.model.MSQLRoot;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.MHaving;
import com.jaspersoft.studio.data.sql.model.query.MWhere;
import com.jaspersoft.studio.data.sql.model.query.expression.AMExpression;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpressionGroup;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpressionPNot;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class CreatePNotExpression extends AAction {

	public CreatePNotExpression(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super("Add $P!{} Ex&pression", treeViewer);
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
		MExpressionPNot mexpr = null;
		if (sel instanceof AMExpression)
			mexpr = run(null, (AMExpression<?>) sel);
		else if (isInSelect(sel))
			mexpr = run(null, (ANode) sel, -1);

		showDialog(mexpr);
	}

	public void run(Collection<MSQLColumn> nodes) {
		Object sel = selection[0];
		MExpressionPNot mexpr = null;
		if (sel instanceof AMExpression<?>)
			mexpr = run(null, (AMExpression<?>) sel);
		else if (isInSelect(sel))
			mexpr = run(null, (ANode) sel, -1);
		showDialog(mexpr);
	}

	public void run(ANode node, JRDesignParameter prm) {
		MExpressionPNot mexpr = run(prm, node, -1);
		showDialog(mexpr);
	}

	protected void showDialog(MExpressionPNot mexpr) {
		EditPNotExpressionDialog dialog = new EditPNotExpressionDialog(UIUtils.getShell());
		dialog.setValue(mexpr);
		if (dialog.open() == Dialog.OK) {
			mexpr.setValue(dialog.getValue());
			selectInTree(mexpr);
		} else {
			ANode p = mexpr.getParent();
			p.removeChild(mexpr);
			selectInTree(p);
		}
	}

	protected MExpressionPNot run(JRDesignParameter node, AMExpression<?> mtable) {
		ANode mfrom = mtable.getParent();
		return run(node, mfrom, mfrom.getChildren().indexOf(mtable) + 1);
	}

	public MExpressionPNot run(JRDesignParameter value, ANode select, int index) {
		if (value == null) {
			INode n = select.getRoot();
			if (n instanceof MSQLRoot) {
				MSQLRoot mroot = (MSQLRoot) n;
				JRDesignDataset ds = mroot.getValue();
				if (ds != null && !ds.getParametersList().isEmpty())
					value = (JRDesignParameter) ds.getParametersList().get(ds.getParametersList().size() - 1);
			}
		}
		return new MExpressionPNot(select, value, index);
	}

}
