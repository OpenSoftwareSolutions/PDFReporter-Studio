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
package net.sf.jasperreports.eclipse.builder.jdt;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;

public class CompilationUnitResult {
	private Set<Method> resolvedMethods;
	private Set<Method> missingMethods;
	public IProblem[] problems;

	public boolean hasMissingMethods() {
		return missingMethods != null && !missingMethods.isEmpty();
	}

	public Set<Method> getMissingMethods() {
		return missingMethods;
	}

	public void addMissingMethod(Method missingMethod) {
		if (resolvedMethods == null || !resolvedMethods.contains(missingMethod)) {
			if (missingMethods == null)
				missingMethods = new HashSet<Method>();
			missingMethods.add(missingMethod);
		}
	}

	public IProblem[] getProblems() {
		return problems;
	}

	public void setProblems(IProblem[] problems) {
		this.problems = problems;
	}

	public void resolveMissingMethods() {
		if (hasMissingMethods()) {
			if (resolvedMethods == null)
				resolvedMethods = new HashSet<Method>();
			resolvedMethods.addAll(missingMethods);
		}
	}

	public void reset() {
		missingMethods = null;
		problems = null;
	}
}
