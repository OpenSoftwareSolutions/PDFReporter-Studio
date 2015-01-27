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
package com.jaspersoft.studio.property.descriptor.expression;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import com.jaspersoft.studio.utils.Misc;

public class ExprUtil {
	public static <T extends JRExpression> T setValues(T e, Object value) {
		return setValues(e, value, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends JRExpression> T setValues(T e, Object value, String valueClassName) {
		if (value == null)
			return null;
		String text = "";
		JRDesignExpression expr = (JRDesignExpression) e;
		expr = createExpression(expr);
		if (value instanceof JRDesignExpression) {
			return checkEmpty((T) value);
		} else if (value instanceof String) {
			text = (String) value;
		}
		expr.setText(text);
		if (valueClassName != null)
			expr.setValueClassName(valueClassName);
		return checkEmpty((T) expr);
	}

	private static JRDesignExpression createExpression(JRDesignExpression expr) {
		if (expr == null) {
			expr = new JRDesignExpression();
		}
		return expr;
	}

	public static String getExpressionText(JRExpression jrExpression) {
		if (jrExpression != null)
			return Misc.nvl(jrExpression.getText(), "");
		return "";
	}

	private static <T extends JRExpression> T checkEmpty(T e) {
		if (e.getText().trim().isEmpty())
			return null;
		return e;
	}

	public static JRExpression getExpression(JRExpression jrExpression) {
		return jrExpression;
	}

	/**
	 * Create a JRDesignExpression by specifying the expression text.
	 * 
	 * @param text
	 *          - The text of the expression, or null for empty expressions
	 * 
	 * @return a new JRDesignExpression
	 */
	public static JRDesignExpression createExpression(String text) {
		return createExpression(text, (String) null);
	}

	/**
	 * Create a JRDesignExpression by specifying the expression text. An optional value class name can be used as
	 * expression class. Value class name should be specified only if strictly required, since it is deprecated by
	 * JasperReports.
	 * 
	 * @param text
	 *          - The text of the expression, or null for empty expressions
	 * @param valueClassName
	 *          - A string representing the class returned by the expression
	 * 
	 * @return a new JRDesignExpression
	 */
	@SuppressWarnings("deprecation")
	public static JRDesignExpression createExpression(String text, String valueClassName) {
		JRDesignExpression exp = new JRDesignExpression();

		if (text != null) {
			exp.setText(text);
		}

		if (valueClassName != null) {
			exp.setValueClassName(valueClassName);
		}

		return exp;

	}

	/**
	 * Create a JRDesignExpression by specifying the expression text. An optional value class can be used as expression
	 * class. Value class should be specified only if strictly required, since it is deprecated by JasperReports.
	 * 
	 * @param text
	 *          - An expression, or null if the expression is empty.
	 * @param valueClass
	 *          - A value class, or null for default
	 * 
	 * @return a new JRDesignExpression
	 */
	public static JRDesignExpression createExpression(String text, Class valueClass) {
		String valueClassName = null;
		if (valueClass != null) {
			valueClassName = valueClass.getName();
			if (valueClass.isArray())
				valueClassName = null;
			else if (valueClass.isArray())
				valueClassName = null;
			else if (valueClass.isPrimitive())
				valueClassName = null;
		}
		return createExpression(text, valueClassName);
	}

	public static JRExpression clone(JRExpression expr) {
		if (expr == null)
			return null;
		return (JRExpression) expr.clone();
	}
}
