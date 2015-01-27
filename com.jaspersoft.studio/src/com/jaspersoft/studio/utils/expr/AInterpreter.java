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

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JRFiller;
import net.sf.jasperreports.engine.util.JRResourcesUtil;
import net.sf.jasperreports.engine.util.JRStringUtil;

import org.apache.commons.lang.LocaleUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AInterpreter {
	protected IJavaProject javaProject;
	protected JRDesignDataset dataset;
	protected JasperDesign jasperDesign;
	protected JasperReportsConfiguration jConfig;
	protected ClassLoader classLoader;

	public void prepareExpressionEvaluator(JRDesignDataset dataset, JasperDesign jasperDesign,
			JasperReportsConfiguration jConfig) throws Exception {
		this.dataset = dataset;
		this.jasperDesign = jasperDesign;
		this.jConfig = jConfig;
		try {
			IFile file = (IFile) jConfig.get(FileUtils.KEY_FILE);
			if (file != null) {
				IProject project = file.getProject();
				if (project.getNature(JavaCore.NATURE_ID) != null)
					javaProject = JavaCore.create(project);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		classLoader = jConfig.getClassLoader();
		if (classLoader == null) {
			if (jasperDesign != null)
				classLoader = jasperDesign.getClass().getClassLoader();
			else
				classLoader = Thread.currentThread().getContextClassLoader();
		}
	}

	protected abstract Object eval(String expression) throws Exception;

	protected abstract void set(String key, Object val) throws Exception;

	protected abstract Object get(String key) throws Exception;

	public Object interpretExpression(String expression) {
		try {
			if (dataset != null)
				expression = prepareExpression(expression, 0);
			return eval(expression);
		} catch (Throwable e) {
			System.out.println("Expression: " + expression);
			e.printStackTrace();
		}
		return null;
	}

	private Set<String> literals = new HashSet<String>();

	protected String prepareExpression(String expression, int recursion) throws Exception {
		while (expression.indexOf("$P{") >= 0) {
			String pname = Misc.extract(expression, "$P{", "}");
			JRParameter pr = null;
			pr = dataset.getParametersMap().get(pname);
			// for (JRParameter p : dataset.getParametersList()) {
			// if (p.getName().equals(pname)) {
			// pr = p;
			// break;
			// }
			// }
			if (pr == null)
				throw new JRException("Paramater $P{" + pname + "} does not exists in the dataset");
			String pnameLiteral = getLiteral(pname);
			expression = Misc.strReplace(pnameLiteral, "$P{" + pname + "}", expression);

			if (!literals.contains(pnameLiteral))
				recursiveInterpreter(recursion, pr);
		}
		
		//Try to evaluate the variable
		while (expression.indexOf("$V{") >= 0) {
			String vname = Misc.extract(expression, "$V{", "}");
			JRVariable vr = null;
			vr = dataset.getVariablesMap().get(vname);
			if (vr == null)
				throw new JRException("Variable $V{" + vname + "} does not exists in the dataset");
			String pnameLiteral = getVariableLiteral(vname);
			expression = Misc.strReplace(pnameLiteral, "$V{" + vname + "}", expression);

			if (!literals.contains(pnameLiteral))
				recursiveInterpreter(recursion, vr);
		}
		
		
		while (expression.indexOf("$R{") >= 0) {
			String pname = Misc.extract(expression, "$R{", "}");
			String baseName = getBundleName();
			if (!baseName.isEmpty()) {
				ResourceBundle rb = getResourceBundle();
				if (rb != null)
					baseName = Misc.nvl(rb.getString(pname));
			}
			expression = Misc.strReplace("\"" + baseName + "\"", "$R{" + pname + "}", expression);
		}
		return expression;
	}

	protected Object recursiveInterpreter(int recursion, JRParameter prm) throws Exception {
		++recursion;
		String pliteral = getLiteral(prm.getName());
		if (literals.contains(pliteral))
			return get(pliteral);
		if (prm.getName().equals("JASPER_REPORTS_CONTEXT"))
			return setValue(jConfig, pliteral);
		JRExpression exp = prm.getDefaultValueExpression();
		if (recursion > 100 || exp == null || Misc.isNullOrEmpty(exp.getText()))
			return getNull(pliteral, prm);
		return setValue(eval(prepareExpression(exp.getText(), recursion)), pliteral);
	}
	
	protected Object recursiveInterpreter(int recursion, JRVariable vrb) throws Exception {
		++recursion;
		String pliteral = getVariableLiteral(vrb.getName());
		if (literals.contains(pliteral))
			return get(pliteral);
		JRExpression exp = vrb.getInitialValueExpression();
		if (recursion > 100 || exp == null || Misc.isNullOrEmpty(exp.getText())){
			if (vrb.getValueClass().equals(String.class)) return setValue("", pliteral);
			else return setValue(null, pliteral);
		}
		return setValue(eval(prepareExpression(exp.getText(), recursion)), pliteral);
	}

	private Object getNull(String pliteral, JRParameter prm) throws Exception {
		if (isConvertNullParams() && prm.getValueClass().equals(String.class))
			return setValue("", pliteral);
		return setValue(null, pliteral);
	}

	private Object setValue(Object v, String literal) throws Exception {
		set(literal, v);
		literals.add(literal);
		return v;
	}

	private String getLiteral(String pname) {
		return "param_" + JRStringUtil.escapeJavaStringLiteral(pname).replace(".", "_");
	}
	
	private String getVariableLiteral(String vname) {
		return "var_" + JRStringUtil.escapeJavaStringLiteral(vname).replace(".", "_");
	}

	private boolean convertNullParams = false;

	/**
	 * @return the convertNullParams
	 */
	public boolean isConvertNullParams() {
		return convertNullParams;
	}

	/**
	 * @param convertNullParams
	 *          the convertNullParams to set
	 */
	public void setConvertNullParams(boolean convertNullParams) {
		this.convertNullParams = convertNullParams;
	}

	private ResourceBundle rb;

	protected ResourceBundle getResourceBundle() {
		if (rb == null)
			rb = JRResourcesUtil.loadResourceBundle(getBundleName(), getLocale(), jConfig.getClassLoader());
		return rb;
	}

	private String bundleName;

	protected String getBundleName() {
		if (bundleName != null)
			return bundleName;
		bundleName = dataset.getResourceBundle();
		if (bundleName == null)
			bundleName = jasperDesign.getMainDataset().getResourceBundle();
		if (Misc.isNullOrEmpty(bundleName))
			bundleName = "";
		return bundleName;
	}

	private Locale locale;

	protected Locale getLocale() {
		if (locale != null)
			return locale;
		locale = Locale.getDefault();
		Object obj = null;
		if (jConfig.getJRParameters() != null) {
			obj = jConfig.getJRParameters().get(JRParameter.REPORT_LOCALE);
			if (obj == null) {
				String str = jConfig.getProperty(JRFiller.PROPERTY_DEFAULT_LOCALE);
				if (str != null)
					obj = LocaleUtils.toLocale(str);
			}
		}
		if (obj != null && obj instanceof Locale)
			locale = (Locale) obj;
		return locale;
	}
}
