/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.utils.parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;

import com.jaspersoft.studio.utils.ExpressionUtil;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ParameterUtil {

	public static void setParameters(JasperReportsConfiguration jConfig, JRDataset dataset, Map<String, Object> inmap) {
		Map<String, Object> map = jConfig.getJRParameters();
		for (JRParameter p : dataset.getParameters()) {
			if (!p.isSystemDefined()) {
				if (map != null && p.isForPrompting()) {
					Object val = map.get(p.getName());
					if (val != null) {
						inmap.put(p.getName(), val);
						continue;
					}
				}

				// if (p.getDefaultValueExpression() != null) {
				// try {
				// Object val = p.getValueClass().newInstance();
				// inmap.put(p.getName(), val);
				// } catch (InstantiationException e) {
				// inmap.put(p.getName(), getDefaultInstance(p, jConfig, dataset));
				// } catch (IllegalAccessException e) {
				// inmap.put(p.getName(), getDefaultInstance(p, jConfig, dataset));
				// }
				// } else {
				// Even if no default value expression was specified, tries
				// to provide a default value based on class type of the parameter.
				inmap.put(p.getName(), getDefaultInstance(p, jConfig, dataset));
				// }
			}
		}
	}

	private static Object getDefaultInstance(JRParameter p, JasperReportsConfiguration jConfig, JRDataset dataset) {
		if (p.getDefaultValueExpression() != null) {
			return ExpressionUtil.cachedExpressionEvaluation(p.getDefaultValueExpression(), jConfig,
					(JRDesignDataset) dataset);
		} else {
			try {
				return p.getValueClass().newInstance();
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}

		if (p.getValueClass().isAssignableFrom(Integer.class))
			return new Integer(0);
		if (p.getValueClass().isAssignableFrom(Byte.class))
			return new Byte("0");
		if (p.getValueClass().isAssignableFrom(Short.class))
			return new Short("0");
		if (p.getValueClass().isAssignableFrom(Float.class))
			return new Float(0);
		if (p.getValueClass().isAssignableFrom(Long.class))
			return new Long(0);
		if (p.getValueClass().isAssignableFrom(Double.class))
			return new Double(0);
		if (p.getValueClass().isAssignableFrom(List.class))
			return new ArrayList();
		if (p.getValueClass().isAssignableFrom(Set.class))
			return new HashSet();
		if (p.getValueClass().isAssignableFrom(Map.class))
			return new HashMap();
		if (p.getValueClass().isAssignableFrom(Collection.class))
			return new ArrayList();

		return null;
	}

	public static Map<String, JRValueParameter> convertMap(Map<String, ?> inmap, JRDataset dataset) {
		Map<String, JRValueParameter> outmap = new HashMap<String, JRValueParameter>();
		for (String key : inmap.keySet())
			outmap.put(key, new SimpleValueParameter(inmap.get(key)));
		for (JRParameter p : dataset.getParameters()) {
			SimpleValueParameter svp = new SimpleValueParameter(inmap.get(p.getName()));
			if (inmap.get(p.getName()) == null)
				svp.setValueClass(p.getValueClass());
			outmap.put(p.getName(), svp);
		}
		outmap.put(JRParameter.REPORT_PARAMETERS_MAP, new SimpleValueParameter(inmap));
		return outmap;
	}
}
