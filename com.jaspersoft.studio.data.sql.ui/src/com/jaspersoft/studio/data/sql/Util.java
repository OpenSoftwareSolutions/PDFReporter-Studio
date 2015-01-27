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
package com.jaspersoft.studio.data.sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.data.sql.model.AMSQLObject;
import com.jaspersoft.studio.data.sql.model.ISubQuery;
import com.jaspersoft.studio.data.sql.model.MDBObjects;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlSchema;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.AMQueryObject;
import com.jaspersoft.studio.data.sql.model.query.MHaving;
import com.jaspersoft.studio.data.sql.model.query.MUnion;
import com.jaspersoft.studio.data.sql.model.query.MWhere;
import com.jaspersoft.studio.data.sql.model.query.expression.AMExpression;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupBy;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.operand.AOperand;
import com.jaspersoft.studio.data.sql.model.query.operand.FieldOperand;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.model.query.subquery.MQueryTable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.utils.Misc;

public class Util {
	public static boolean columnExists(MSQLColumn c, MDBObjects orderBy, List<MSqlTable> tables) {
		if (!tables.contains(c.getParent()))
			return true;
		for (INode n : orderBy.getChildren())
			if (n.getValue().equals(c))
				return true;
		return false;
	}

	public static List<AMSQLObject> getUsedColumns(ANode sel) {
		List<AMSQLObject> list = new ArrayList<AMSQLObject>();
		MDBObjects mgb = null;
		if (sel instanceof MDBObjects)
			mgb = (MDBObjects) sel;
		else if (sel instanceof AMQueryObject)
			mgb = (MDBObjects) sel.getParent();
		for (INode n : mgb.getChildren())
			if (n instanceof MSelectColumn)
				list.add((AMSQLObject) n.getValue());
		return list;
	}

	public static List<MSqlTable> getTables(ANode sel) {
		List<MSqlTable> list = new ArrayList<MSqlTable>();
		ANode r = getQueryRoot(sel);
		for (INode n : r.getChildren()) {
			if (n instanceof MFrom) {
				for (INode t : n.getChildren()) {
					list.add((MSqlTable) t.getValue());
					if (!t.getChildren().isEmpty()) {
						for (INode jt : t.getChildren())
							list.add((MSqlTable) jt.getValue());
					}
				}
				break;
			}
		}
		return list;
	}

	public static ANode getQueryRoot(ANode n) {
		ANode root = n;
		while (root != null && !(root instanceof MRoot || root instanceof MUnion || root instanceof ISubQuery || (root instanceof MFromTable && root.getValue() instanceof MQueryTable)))
			root = root.getParent();
		return root;
	}

	public static List<MFromTable> getFromTables(ANode sel) {
		List<MFromTable> list = new ArrayList<MFromTable>();
		ANode r = getQueryRoot(sel);
		for (INode n : r.getChildren()) {
			if (n instanceof MFrom) {
				for (INode t : n.getChildren()) {
					list.add((MFromTable) t);
					if (!t.getChildren().isEmpty()) {
						for (INode jt : t.getChildren())
							if (jt instanceof MFromTable)
								list.add((MFromTable) jt);
					}
				}
				break;
			}
		}
		return list;
	}

	public static MRoot getRoot(MSQLColumn mcol, AMExpression<?> mexpr) {
		if (mcol != null)
			return (MRoot) mcol.getRoot();
		ModelVisitor<INode> mv = new ModelVisitor<INode>(mexpr.getRoot()) {
			@Override
			public boolean visit(INode n) {
				if (n instanceof AMQueryObject && n.getValue() instanceof ANode) {
					ANode v = (ANode) ((AMQueryObject<?>) n).getValue();
					setObject(v.getRoot());
					return false;
				}
				return true;
			}
		};
		return (MRoot) mv.getObject();
	}

	public static ANode getOldNode(ANode target, final ANode src) {
		ModelVisitor<ANode> mv = new ModelVisitor<ANode>(target.getRoot()) {
			@Override
			public boolean visit(INode n) {
				if (n instanceof AMExpression && src instanceof AMExpression)
					;// System.out.println(((AMExpression<?>) n).getId() + " --- " +
						// ((AMExpression<?>) src).getId());
				if (src != n && src.equals(n)) {
					setObject((ANode) n);
					stop();
				}
				return true;
			}
		};
		return mv.getObject();
	}

	@SuppressWarnings("unchecked")
	public static <T extends AMKeyword> T getKeyword(ANode msc, Class<T> clazz) {
		for (INode n : getQueryRoot(msc).getChildren())
			if (clazz.isInstance(n))
				return (T) n;
		return null;
	}

	public static List<ANode> getAllNodes(Object data) {
		List<ANode> nodes = new ArrayList<ANode>();
		if (data.getClass().isArray()) {
			Object[] ar = (Object[]) data;
			for (Object obj : ar)
				if (obj instanceof ANode)
					nodes.add((ANode) obj);
		} else if (data instanceof ANode)
			nodes.add((ANode) data);
		return nodes;
	}

	public static void filterTables(List<ANode> node, Set<MSqlTable> tables, Set<MSQLColumn> cols, Set<ANode> others) {
		for (ANode n : node) {
			if (n instanceof MSqlTable)
				tables.add((MSqlTable) n);
			else if (n instanceof MSQLColumn)
				cols.add((MSQLColumn) n);
			else
				others.add(n);
		}
	}

	public static void cleanTableVersions(final MFromTable newTbl, final MFromTable oldTbl) {
		new ModelVisitor<Object>(newTbl.getRoot()) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MSelectColumn && ((MSelectColumn) n).getMFromTable().equals(oldTbl)) {
					((MSelectColumn) n).setMFromTable(newTbl);
					return false;
				}
				if (n instanceof MGroupByColumn && ((MGroupByColumn) n).getMFromTable().equals(oldTbl)) {
					((MGroupByColumn) n).setMFromTable(newTbl);
					return false;
				}
				if (n instanceof MOrderByColumn && ((MOrderByColumn) n).getMFromTable().equals(oldTbl)) {
					((MOrderByColumn) n).setMFromTable(newTbl);
					return false;
				}
				if (n instanceof AMExpression) {
					for (AOperand op : ((AMExpression<?>) n).getOperands()) {
						if (op instanceof FieldOperand && ((FieldOperand) op).getFromTable().equals(oldTbl)) {
							((FieldOperand) op).setFromTable(newTbl);
						}
					}
					return false;
				}
				return true;
			}

		};
	}

	public static void refreshTables(MRoot rmeta, final MRoot rquery, final SQLQueryDesigner designer) {
		List<MSqlTable> oldTables = getTables(rquery);
		Set<MSqlTable> newTables = new HashSet<MSqlTable>();
		for (MSqlTable mt : oldTables) {
			MSqlTable newTbl = getTable(rmeta, mt, designer);
			if (newTbl != null)
				newTables.add(newTbl);
		}
		for (MSqlTable t : newTables)
			replaceTable(rquery, t);
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				designer.setRefreshMetadata(true);
				rquery.getPropertyChangeSupport().firePropertyChange("tablesupdated", false, true);
				designer.setRefreshMetadata(false);
			}
		});

	}

	public static void replaceTable(MRoot rquery, final MSqlTable mtable) {
		final String sqlTable = mtable.toSQLString();
		new ModelVisitor<MSqlTable>(rquery) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MFromTable)
					if (((MFromTable) n).getValue().toSQLString().equals(sqlTable))
						n.setValue(mtable);
				if (n instanceof MSelectColumn) {
					MSQLColumn mc = getColumn(((MSelectColumn) n).getMFromTable(), ((MSelectColumn) n).getValue());
					if (mc != null)
						n.setValue(mc);
					return false;
				}
				if (n instanceof MGroupByColumn) {
					MSQLColumn mc = getColumn(((MGroupByColumn) n).getMFromTable(), ((MGroupByColumn) n).getValue());
					if (mc != null)
						n.setValue(mc);
					return false;
				}
				if (n instanceof MOrderByColumn) {
					MSQLColumn mc = getColumn(((MOrderByColumn) n).getMFromTable(), ((MOrderByColumn) n).getValue());
					if (mc != null)
						n.setValue(mc);
					return false;
				}
				if (n instanceof AMExpression) {
					for (AOperand op : ((AMExpression<?>) n).getOperands()) {
						if (op instanceof FieldOperand) {
							MSQLColumn mc = getColumn(((FieldOperand) op).getFromTable(), ((FieldOperand) op).getMColumn());
							if (mc != null)
								((FieldOperand) op).setColumn(mc);
						}
					}
					return false;
				}
				return true;
			}

			private MSQLColumn getColumn(MFromTable mtable, MSQLColumn old) {
				if (mtable != null && mtable.getValue() != null && old != null && mtable.getValue().equals(old.getParent())) {
					for (INode n : mtable.getChildren()) {
						if (n.equals(old))
							return (MSQLColumn) n;
					}
				}
				return null;
			}
		};
	}

	public static MSqlTable getTable(MRoot rmeta, final MSqlTable mt, final SQLQueryDesigner designer) {
		MSqlSchema msch = null;
		if (mt.getParent() != null) {
			if (mt.getParent() instanceof MSqlSchema)
				msch = (MSqlSchema) mt.getParent();
			else if (mt.getParent().getParent() != null && mt.getParent().getParent() instanceof MSqlSchema)
				msch = (MSqlSchema) mt.getParent().getParent();
		}
		final String schema = msch != null ? msch.getValue() : "";
		return new ModelVisitor<MSqlTable>(rmeta) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MSqlSchema) {
					MSqlSchema mschema = (MSqlSchema) n;
					if (!mschema.getValue().equalsIgnoreCase(schema) || mschema.isNotInMetadata())
						return false;
					else
						designer.getDbMetadata().loadSchema(mschema);
				} else if (n instanceof MSqlTable) {
					if (((MSqlTable) n).getValue().equalsIgnoreCase(mt.getValue())) {
						designer.getDbMetadata().loadTable((MSqlTable) n);
						setObject((MSqlTable) n);
						stop();
					}
				}
				return true;
			}
		}.getObject();
	}

	public static MSqlTable getTable(MRoot rmeta, MSqlTable mtable) {
		return getTable(rmeta, null, mtable.getSchema().getValue(), mtable.getValue());
	}

	public static MSqlTable getTable(MRoot rmeta, final String cat, final String schema, final String table) {
		ModelVisitor<MSqlTable> v = new ModelVisitor<MSqlTable>(rmeta) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof MSqlSchema)
					return Misc.nvl(((MSqlSchema) n).getValue()).equals(Misc.nvl(schema));
				if (n instanceof MSqlTable) {
					if (n.getValue().equals(table)) {
						setObject((MSqlTable) n);
						stop();
					}
				}
				return true;
			}
		};
		return v.getObject();
	}

	public static void removeFrom(List<?> col, int from) {
		for (int i = col.size() - 1; i > from; i--)
			col.remove(i);
	}

	public static void copySubQuery(MFromTable mftj, MFromTable mtbl) {
		if (mftj.getValue() instanceof MQueryTable) {
			List<INode> children = new ArrayList<INode>(mftj.getChildren());
			for (INode n : children) {
				if (n instanceof MUnion || n instanceof MSelect || n instanceof MFrom || n instanceof MWhere || n instanceof MGroupBy || n instanceof MHaving)
					mtbl.addChild((ANode) n);
			}
		}
	}

	public static MSelect createSelect(ANode parent) {
		MSelect ms = new MSelect(parent);
		new MFrom(parent);
		new MWhere(parent);
		new MGroupBy(parent);
		new MHaving(parent);
		return ms;
	}
}
