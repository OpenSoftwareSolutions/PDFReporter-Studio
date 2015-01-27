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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class GroovyInterpretter extends AInterpreter {
	private GroovyShell interpreter;

	public void prepareExpressionEvaluator(JRDesignDataset dataset, JasperDesign jasperDesign,
			JasperReportsConfiguration jConfig) throws Exception {
		super.prepareExpressionEvaluator(dataset, jasperDesign, jConfig);

		ImportCustomizer ic = new ImportCustomizer();
		ic.addStarImports("import net.sf.jasperreports.engine.*;");
		ic.addStarImports("import net.sf.jasperreports.engine.fill.*;");
		ic.addStarImports("import java.util.*;");
		ic.addStarImports("import java.math.*;");
		ic.addStarImports("import java.text.*;");
		ic.addStarImports("import java.io.*;");
		ic.addStarImports("import java.net.*;");
		ic.addStarImports("import java.util.*;");
		ic.addStarImports("import net.sf.jasperreports.engine.data.*;");

		if (jasperDesign != null) {
			String[] imports = jasperDesign.getImports();
			for (int i = 0; imports != null && i < imports.length; ++i) {

				String importString = imports[i];
				if (importString.startsWith("static ")) {
					ic.addStaticStars("static import " + imports[i].substring("static ".length()) + ";");
				} else {
					ic.addStarImports("import " + imports[i] + ";");
				}
			}
		}

		CompilerConfiguration cc = new CompilerConfiguration();
		cc.addCompilationCustomizers(ic);
		Binding binding = new Binding();
		if (classLoader != null) {
			interpreter = new GroovyShell(classLoader, binding, cc);
		} else
			interpreter = new GroovyShell(cc);
		// I need to add to the classpath the document directory...
	}

	public Object interpretExpression(String expression) {
		if (interpreter == null)
			return null;
		return super.interpretExpression(expression);
	}

	@Override
	protected Object eval(String expression) throws Exception {
		return interpreter.evaluate(expression);
	}

	@Override
	protected void set(String key, Object val) throws Exception {
		interpreter.setVariable(key, val);
	}

	@Override
	protected Object get(String key) throws Exception {
		return interpreter.getVariable(key);
	}
}
