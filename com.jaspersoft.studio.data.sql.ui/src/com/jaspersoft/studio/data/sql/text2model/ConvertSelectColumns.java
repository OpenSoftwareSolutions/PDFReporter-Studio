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
package com.jaspersoft.studio.data.sql.text2model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.jaspersoft.studio.data.sql.ColumnFull;
import com.jaspersoft.studio.data.sql.ColumnOperand;
import com.jaspersoft.studio.data.sql.ColumnOrAlias;
import com.jaspersoft.studio.data.sql.Concat;
import com.jaspersoft.studio.data.sql.Div;
import com.jaspersoft.studio.data.sql.ExpOperand;
import com.jaspersoft.studio.data.sql.Minus;
import com.jaspersoft.studio.data.sql.OpFunction;
import com.jaspersoft.studio.data.sql.OpFunctionArg;
import com.jaspersoft.studio.data.sql.OpFunctionCast;
import com.jaspersoft.studio.data.sql.Operand;
import com.jaspersoft.studio.data.sql.Operands;
import com.jaspersoft.studio.data.sql.OrColumn;
import com.jaspersoft.studio.data.sql.POperand;
import com.jaspersoft.studio.data.sql.Plus;
import com.jaspersoft.studio.data.sql.SQLCaseOperand;
import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.ScalarOperand;
import com.jaspersoft.studio.data.sql.SqlCaseWhen;
import com.jaspersoft.studio.data.sql.Star;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.impl.DbObjectNameImpl;
import com.jaspersoft.studio.data.sql.impl.OperandImpl;
import com.jaspersoft.studio.data.sql.impl.OperandsImpl;
import com.jaspersoft.studio.data.sql.impl.OrColumnImpl;
import com.jaspersoft.studio.data.sql.impl.SelectImpl;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.AMQueryAliased;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectExpression;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectSubQuery;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.KeyValue;
import com.jaspersoft.studio.utils.Misc;

public class ConvertSelectColumns {
	public static void convertSelectColumns(SQLQueryDesigner designer, ANode qroot, OrColumn cols) {
		if (cols == null)
			return;
		if (cols instanceof ColumnOrAlias)
			doColumns(designer, qroot, Util.getKeyword(qroot, MSelect.class), (ColumnOrAlias) cols);
		else if (cols instanceof OrColumnImpl) {
			MSelect msel = Util.getKeyword(qroot, MSelect.class);
			for (ColumnOrAlias fcol : cols.getEntries())
				doColumns(designer, qroot, msel, fcol);
		}
	}

	private static void doColumns(SQLQueryDesigner designer, ANode qroot, MSelect msel, ColumnOrAlias fcol) {
		try {
			if (fcol.getAllCols() != null)
				new MSelectExpression(msel, "*");
			else if (fcol.getDbAllCols() != null) {
				new MSelectExpression(msel, fcol.getDbAllCols().getDbname() + ".*");
			} else {
				Operands ce = fcol.getCe();
				if (ce != null) {
					if (ce instanceof OperandImpl)
						setupAlias(getMSelectColumn(designer, qroot, (OperandImpl) ce, msel), fcol);
					else if (ce instanceof OperandsImpl) {
						AMQueryAliased<?> mscol = null;
						if (ce.getOp1() != null && ce.getLeft() == null && ce.getRight() == null)
							mscol = getMSelectColumn(designer, qroot, (OperandImpl) ce.getOp1(), msel);
						else {
							mscol = getColumnUnknown(msel, "");
							mscol.setValue(operands2String(designer, qroot, mscol, ce, msel));
						}
						setupAlias(mscol, fcol);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static String operands2String(SQLQueryDesigner designer, ANode qroot, ANode parent, Operands ops, MSelect msel) {
		Operand op = ops.getOp1();
		if (op == null && ops.getLeft() != null)
			op = ops.getLeft().getOp1();

		String str = operand2String(designer, qroot, parent, op, msel);
		if (ops instanceof Plus)
			str += " + ";
		else if (ops instanceof Minus)
			str += " - ";
		else if (ops instanceof Star)
			str += " * ";
		else if (ops instanceof Div)
			str += " / ";
		else if (ops instanceof Concat)
			str += " || ";

		if (ops.getRight() != null)
			str += operand2String(designer, qroot, parent, ops.getRight(), msel);
		return str;
	}

	private static AMQueryAliased<?> getMSelectColumn(SQLQueryDesigner designer, ANode qroot, OperandImpl op, MSelect msel) {
		AMQueryAliased<?> mscol = null;
		if (op.getSubq() != null) {
			mscol = new MSelectSubQuery(msel);
			Util.createSelect(mscol);
			Text2Model.convertSelect(designer, mscol, (SelectImpl) op.getSubq().getSel());
		} else if (op.getColumn() != null)
			mscol = getColumn(msel, op.getColumn().getCfull());
		else if (op.getFunc() != null) {
			mscol = getColumnUnknown(msel, "");
			mscol.setValue(getFunctionString(designer, qroot, mscol, op.getFunc(), msel));
		} else if (op.getFcast() != null) {
			mscol = getColumnUnknown(msel, "");
			mscol.setValue(getFunctionString(designer, qroot, mscol, op.getFcast(), msel));
		} else if (op.getParam() != null)
			mscol = getColumnUnknown(msel, op.getParam().getPrm());
		else if (op.getEparam() != null)
			mscol = getColumnUnknown(msel, op.getEparam().getPrm());
		else if (op.getScalar() != null)
			mscol = getColumnUnknown(msel, getScalarString(op.getScalar()));
		else if (op.getSqlcase() != null) {
			mscol = getColumnUnknown(msel, "");
			mscol.setValue(case2string(designer, qroot, mscol, op.getSqlcase(), msel));
		} else if (op.getXop() != null)
			mscol = getMSelectColumn(designer, qroot, (OperandImpl) op.getXop(), msel);
		return mscol;
	}

	protected static String case2string(SQLQueryDesigner designer, ANode qroot, ANode parent, SQLCaseOperand scase, MSelect msel) {
		String res = "CASE ";
		if (scase.getExpr() != null)
			res += " " + ConvertExpression.convertExpression2String(designer, qroot, parent, scase.getExpr());
		if (scase.getWhen() != null) {
			for (EObject eobj : scase.getWhen().eContents()) {
				if (eobj instanceof SqlCaseWhen) {
					SqlCaseWhen scasewhen = (SqlCaseWhen) eobj;
					res += "\nWHEN ";
					if (scasewhen.getExpr() != null)
						res += " " + ConvertExpression.convertExpression2String(designer, qroot, parent, scasewhen.getExpr());
					if (scasewhen.getTexp() != null)
						res += "THEN " + operands2String(designer, qroot, parent, scasewhen.getTexp(), msel);
					if (scasewhen.getEexp() != null)
						res += "ELSE " + operands2String(designer, qroot, parent, scasewhen.getEexp(), msel);
				}
			}
		}
		parent.removeChildren();
		return res + "\nEND";
	}

	protected static String operand2String(SQLQueryDesigner designer, ANode qroot, ANode parent, Operand oper, MSelect msel) {
		// if (oper.getSubq() != null) {
		// MSelectSubQuery qroot = new MSelectSubQuery(msel);
		// Util.createSelect(qroot);
		// Text2Model.convertSelect(designer, qroot, (SelectImpl)
		// oper.getSubq().getSel());
		// } else
		if (oper.getColumn() != null)
			return getColumn(oper.getColumn().getCfull(), msel);
		if (oper.getFunc() != null)
			return getFunctionString(designer, qroot, parent, oper.getFunc(), msel);
		if (oper.getFcast() != null)
			return getFunctionString(designer, qroot, parent, oper.getFcast(), msel);
		if (oper.getParam() != null)
			return oper.getParam().getPrm();
		if (oper.getEparam() != null)
			return oper.getEparam().getPrm();
		if (oper.getScalar() != null)
			return getScalarString(oper.getScalar());
		if (oper.getSqlcase() != null)
			return case2string(designer, qroot, parent, oper.getSqlcase(), msel);
		if (oper.getXop() != null)
			return operand2String(designer, qroot, parent, oper.getXop(), msel);
		return "";
	}

	public static String getFunctionString(SQLQueryDesigner designer, ANode qroot, ANode parent, OpFunction f, MSelect msel) {
		String sargs = " ";
		OpFunctionArg args = f.getArgs();
		if (args != null) {
			String sep = "";
			for (EObject eobj : args.eContents()) {
				sargs += sep;
				if (eobj instanceof OperandImpl)
					sargs += operand2String(designer, qroot, parent, (OperandImpl) eobj, msel);
				else if (eobj instanceof ColumnOperand)
					sargs += getColumn(((ColumnOperand) eobj).getCfull(), msel);
				else if (eobj instanceof POperand)
					sargs += ((POperand) eobj).getPrm();
				else if (eobj instanceof ExpOperand)
					sargs += ((ExpOperand) eobj).getPrm();
				else if (eobj instanceof ScalarOperand)
					sargs += eobj.toString();
				else if (eobj instanceof Operands)
					sargs += operands2String(designer, qroot, parent, (Operands) eobj, msel);
				sep = ",";
			}
		}
		return f.getFname() + sargs + ")";
	}

	public static String getFunctionString(SQLQueryDesigner designer, ANode qroot, ANode parent, OpFunctionCast f, MSelect msel) {
		String sargs = "";

		EObject eobj = f.getOp();
		if (eobj instanceof OperandImpl)
			sargs += operand2String(designer, qroot, parent, (OperandImpl) eobj, msel);
		else if (eobj instanceof ColumnOperand)
			sargs += getColumn(((ColumnOperand) eobj).getCfull(), msel);
		else if (eobj instanceof POperand)
			sargs += ((POperand) eobj).getPrm();
		else if (eobj instanceof ExpOperand)
			sargs += ((ExpOperand) eobj).getPrm();
		else if (eobj instanceof ScalarOperand)
			sargs += eobj.toString();
		else if (eobj instanceof Operands)
			sargs += operands2String(designer, qroot, parent, (Operands) eobj, msel);

		if (f.getType() != null)
			sargs += " AS " + f.getType();
		if (f.getP() > 0) {
			sargs += "(" + f.getP();
			if (f.getP2() > 0)
				sargs += "," + f.getP2();
			sargs += ")";
		}
		return "CAST(" + sargs + ")";
	}

	private static String getScalarString(ScalarOperand sc) {
		if (sc.getSodate() != null)
			return sc.getSodate().toString();
		if (sc.getSodbl() != null)
			return sc.getSodbl().toString();
		if (sc.getSodt() != null)
			return sc.getSodt().toString();
		if (sc.getSostr() != null)
			return sc.getSostr();
		if (sc.getSotime() != null)
			return sc.getSotime().toString();
		return Integer.toString(sc.getSoint());
	}

	private static void setupAlias(AMQueryAliased<?> mscol, ColumnOrAlias fcol) {
		if (mscol == null)
			return;
		mscol.setAliasKeyword(Misc.nvl(fcol.getAlias(), " "));
		if (fcol.getColAlias() != null)
			mscol.setAlias(fcol.getColAlias().getDbname());
	}

	private static String getColumn(ColumnFull tf, MSelect msel) {
		EList<EObject> eContents = tf.eContents();
		String column = null;
		if (tf instanceof DbObjectNameImpl)
			column = ((DbObjectNameImpl) tf).getDbname();
		else
			column = ConvertUtil.getDbObjectName(eContents, 1);
		String table = ConvertUtil.getDbObjectName(eContents, 2);
		String schema = ConvertUtil.getDbObjectName(eContents, 3);
		// String catalog = getDbObjectName(eContents, 3);
		ConvertUtil.findColumn(msel, schema, table, column);
		if (table != null)
			column = table + "." + column;
		if (schema != null)
			column = schema + "." + column;
		return column;
	}

	private static AMQueryAliased<?> getColumn(MSelect msel, ColumnFull tf) {
		EList<EObject> eContents = tf.eContents();
		String column = null;
		if (tf instanceof DbObjectNameImpl)
			column = ((DbObjectNameImpl) tf).getDbname();
		else
			column = ConvertUtil.getDbObjectName(eContents, 1);
		String table = ConvertUtil.getDbObjectName(eContents, 2);
		String schema = ConvertUtil.getDbObjectName(eContents, 3);
		// String catalog = getDbObjectName(eContents, 3);
		MSelectColumn msqlt = findColumn(msel, schema, table, column);
		if (msqlt == null)
			return getColumnUnknown(msel, column);
		return msqlt;
	}

	private static MSelectExpression getColumnUnknown(MSelect msel, String column) {
		return new MSelectExpression(msel, column);
	}

	private static MSelectColumn findColumn(final MSelect msel, final String schema, final String table, final String column) {
		KeyValue<MSQLColumn, MFromTable> kv = ConvertUtil.findColumn(msel, schema, table, column);
		if (kv != null)
			return new MSelectColumn(msel, kv.key, kv.value);
		return null;
	}

}
