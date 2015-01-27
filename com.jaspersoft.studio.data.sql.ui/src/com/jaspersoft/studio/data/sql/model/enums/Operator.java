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
package com.jaspersoft.studio.data.sql.model.enums;

public enum Operator {
	LESS("<", 2), GREATER(">", 2), EQUALS("=", 2), LESSOREQUALS("<=", 2), GREATEROREQUALS(
			">=", 2), NOTEQUALS("<>", 2), LIKE("LIKE", 2), BETWEEN("BETWEEN", 3), NOTBETWEEN(
			"NOT BETWEEN", 3), IN("IN", Integer.MAX_VALUE), NOTIN("NOT IN",
			Integer.MAX_VALUE), ISNULL("IS NULL", 1), ISNOTNULL("IS NOT NULL",
			1);

	private final String sqlname;
	private final int nrOperands;
	public static final String[] operators = getOperators();

	Operator(String sqlname, int nrOperands) {
		this.sqlname = sqlname;
		this.nrOperands = nrOperands;
	}

	public static Operator getOperator(String sqlname) {
		for (int i = 0; i < values().length; ++i) {
			Operator r = values()[i];
			if (r.getSqlname().equalsIgnoreCase(sqlname))
				return r;
		}
		return EQUALS;
	}

	public String getSqlname() {
		return sqlname;
	}

	public int getNrOperands() {
		return nrOperands;
	}

	public String getFormat(Operator op) {
		if (op.getNrOperands() == 1)
			return "{0} " + op.sqlname + " ";
		if (op.getNrOperands() == 2)
			return "{0} " + op.sqlname + " {1}";
		if (op == BETWEEN)
			return "{0} BETWEEN {1} AND {2}";
		if (op == NOTBETWEEN)
			return "{0} NOT BETWEEN {1} AND {2}";
		if (op.getNrOperands() > 3)
			return "{0} " + op.getSqlname() + " ({1})";
		return "";
	}

	public static String[] getOperators() {
		Operator[] op = values();
		String[] ops = new String[op.length];
		for (int i = 0; i < op.length; i++)
			ops[i] = op[i].getSqlname();
		return ops;
	}
}
