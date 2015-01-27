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
package com.jaspersoft.studio.utils.expr;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.launching.JavaRuntime;

import bsh.Interpreter;

import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JavaInterpreter extends AInterpreter {
	private Interpreter interpreter = null;

	@Override
	public void prepareExpressionEvaluator(JRDesignDataset dataset, JasperDesign jasperDesign,
			JasperReportsConfiguration jConfig) throws Exception {
		super.prepareExpressionEvaluator(dataset, jasperDesign, jConfig);

		interpreter = new Interpreter();
		// I need to add to the classpath the document directory...

		// Look for the JrxmlVisualView parent of the scene...
		if (classLoader != null)
			interpreter.setClassLoader(classLoader);

		// Staring patch from rp4

		interpreter.eval("String tmp;");
		try {
			if (javaProject != null) {
				String[] cpaths = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
				for (String p : cpaths) {
					if (Misc.isNullOrEmpty(p))
						continue;
					interpreter.set("tmp", p);
					interpreter.eval("addClassPath(tmp);");
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		// Add report import directives to the bsh interpreter
		interpreter.eval("import net.sf.jasperreports.engine.*;");
		interpreter.eval("import net.sf.jasperreports.engine.fill.*;");
		interpreter.eval("import java.util.*;");
		interpreter.eval("import java.math.*;");
		interpreter.eval("import java.text.*;");
		interpreter.eval("import java.io.*;");
		interpreter.eval("import java.net.*;");
		interpreter.eval("import java.util.*;");
		interpreter.eval("import net.sf.jasperreports.engine.data.*;");

		if (jasperDesign != null) {
			String[] imports = jasperDesign.getImports();
			for (int i = 0; imports != null && i < imports.length; ++i) {
				String importString = imports[i];
				if (importString.startsWith("static "))
					interpreter.eval("static import " + imports[i].substring("static ".length()) + ";");
				else
					interpreter.eval("import " + imports[i] + ";");
			}
		}
	}

	@Override
	public Object interpretExpression(String expression) {
		if (interpreter == null)
			return null;
		return super.interpretExpression(expression);
	}

	@Override
	protected Object eval(String expression) throws Exception {
		return interpreter.eval(expression);
	}

	@Override
	protected void set(String key, Object val) throws Exception {
		interpreter.set(key, val);
	}

	@Override
	protected Object get(String key) throws Exception {
		return interpreter.get(key);
	}

}
