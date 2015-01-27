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
package com.jaspersoft.studio.data.sql.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.expression.ChangeOperator;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpression;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpressionGroup;
import com.jaspersoft.studio.data.sql.action.expression.CreatePNotExpression;
import com.jaspersoft.studio.data.sql.action.expression.CreateWhereFromColumn;
import com.jaspersoft.studio.data.sql.action.expression.CreateXExpression;
import com.jaspersoft.studio.data.sql.action.expression.EditExpression;
import com.jaspersoft.studio.data.sql.action.groupby.CreateGroupByColumn;
import com.jaspersoft.studio.data.sql.action.groupby.CreateGroupByFromColumn;
import com.jaspersoft.studio.data.sql.action.order.CreateOrderByColumn;
import com.jaspersoft.studio.data.sql.action.order.CreateOrderByFromColumn;
import com.jaspersoft.studio.data.sql.action.order.OrderByDesc;
import com.jaspersoft.studio.data.sql.action.select.CreateColumn;
import com.jaspersoft.studio.data.sql.action.select.CreateHavingFromColumn;
import com.jaspersoft.studio.data.sql.action.select.CreateSelectExpression;
import com.jaspersoft.studio.data.sql.action.select.CreateSubSelect;
import com.jaspersoft.studio.data.sql.action.select.DeleteColumn;
import com.jaspersoft.studio.data.sql.action.select.EditColumn;
import com.jaspersoft.studio.data.sql.action.select.SelectDistinct;
import com.jaspersoft.studio.data.sql.action.table.CreateSubQueryTable;
import com.jaspersoft.studio.data.sql.action.table.CreateTable;
import com.jaspersoft.studio.data.sql.action.table.DeleteTable;
import com.jaspersoft.studio.data.sql.action.table.DeleteTableJoin;
import com.jaspersoft.studio.data.sql.action.table.EditTable;
import com.jaspersoft.studio.data.sql.action.table.EditTableJoin;
import com.jaspersoft.studio.data.sql.action.table.JoinTable;
import com.jaspersoft.studio.data.sql.action.union.ChangeSetOperator;
import com.jaspersoft.studio.data.sql.action.union.CreateUnion;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.MUnion;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpression;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpressionGroup;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpressionX;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectSubQuery;

public class ActionFactory {
	private List<AAction> actions = new ArrayList<AAction>();

	public ActionFactory(SQLQueryDesigner designer, TreeViewer treeViewer) {

		actions.add(new SelectDistinct(treeViewer));
		actions.add(new CreateWhereFromColumn(designer, treeViewer));
		actions.add(new CreateGroupByFromColumn(treeViewer));
		actions.add(new CreateHavingFromColumn(designer, treeViewer));
		actions.add(new CreateOrderByFromColumn(treeViewer));
		actions.add(null);
		actions.add(new CreateColumn(designer, treeViewer));
		actions.add(new CreateSelectExpression(treeViewer));
		actions.add(new CreateSubSelect(treeViewer));
		actions.add(null);
		actions.add(new EditColumn(treeViewer));
		actions.add(null);
		actions.add(new DeleteColumn(designer, treeViewer));
		actions.add(new DeleteAction<MSelectSubQuery>(designer, treeViewer, "Sub Query", MSelectSubQuery.class));
		actions.add(null);

		actions.add(new JoinTable(designer, treeViewer));
		actions.add(new CreateTable(designer, treeViewer));
		actions.add(new CreateSubQueryTable(designer, treeViewer));
		actions.add(null);
		actions.add(new EditTableJoin(designer, treeViewer));
		actions.add(new EditTable(treeViewer));
		actions.add(null);
		actions.add(new DeleteTableJoin(treeViewer));
		actions.add(new DeleteTable(designer, treeViewer));
		actions.add(null);

		actions.add(new CreateGroupByColumn(designer, treeViewer));
		actions.add(null);
		actions.add(new DeleteAction<MGroupByColumn>(designer, treeViewer, "Column", MGroupByColumn.class));
		actions.add(null);

		actions.add(new CreateOrderByColumn(designer, treeViewer));
		actions.add(null);
		actions.add(new OrderByDesc(treeViewer));
		actions.add(null);
		actions.add(new DeleteAction<MOrderByColumn>(designer, treeViewer, "Column", MOrderByColumn.class));

		actions.add(null);
		actions.add(new CreateExpressionGroup(treeViewer));
		actions.add(new CreateExpression(designer, treeViewer));
		actions.add(new CreateXExpression(designer, treeViewer));
		actions.add(new CreatePNotExpression(designer, treeViewer));
		actions.add(new ChangeOperator(treeViewer));
		actions.add(null);
		actions.add(new CreateUnion(treeViewer));
		actions.add(null);
		actions.add(new ChangeSetOperator(AMKeyword.SET_OPERATOR_UNION, treeViewer));
		actions.add(new ChangeSetOperator(AMKeyword.SET_OPERATOR_UNION_ALL, treeViewer));
		actions.add(new ChangeSetOperator(AMKeyword.SET_OPERATOR_EXCEPT, treeViewer));
		actions.add(new ChangeSetOperator(AMKeyword.SET_OPERATOR_INTERSECT, treeViewer));
		actions.add(new ChangeSetOperator(AMKeyword.SET_OPERATOR_MINUS, treeViewer));
		actions.add(null);
		actions.add(new EditExpression(treeViewer));
		actions.add(null);
		actions.add(new DeleteAction<MUnion>(designer, treeViewer, "Union", MUnion.class));
		actions.add(null);
		actions.add(new DeleteAction<MExpression>(designer, treeViewer, "Expression", MExpression.class));
		actions.add(new DeleteAction<MExpressionX>(designer, treeViewer, "Expression $X{}", MExpressionX.class));
		actions.add(new DeleteAction<MExpressionGroup>(designer, treeViewer, "Expression Group", MExpressionGroup.class));
	}

	@SuppressWarnings("unchecked")
	public <T extends AAction> T getAction(Class<T> aclass) {
		for (AAction act : actions) {
			if (act != null && aclass.isAssignableFrom(act.getClass()))
				return (T) act;
		}
		return null;
	}

	public List<AAction> getActions() {
		return actions;
	}

	public void fillMenu(Object[] selection, IMenuManager menu) {
		for (AAction act : actions) {
			if (act == null)
				menu.add(new org.eclipse.jface.action.Separator());
			else if (act.calculateEnabled(selection))
				menu.add(act);
		}
	}

	public List<DeleteAction<?>> getDeleteActions(Object[] selection) {
		List<DeleteAction<?>> res = new ArrayList<DeleteAction<?>>();
		for (AAction act : actions) {
			if (act != null && act instanceof DeleteAction && act.calculateEnabled(selection))
				res.add((DeleteAction<?>) act);
		}
		return res;
	}
}
