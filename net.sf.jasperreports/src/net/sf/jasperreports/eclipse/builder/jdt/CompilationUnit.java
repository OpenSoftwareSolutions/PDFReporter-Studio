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

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public class CompilationUnit implements ICompilationUnit {
	protected String srcCode;
	protected String className;

	public CompilationUnit(String srcCode, String className) {
		this.srcCode = srcCode;
		this.className = className;
	}

	public char[] getFileName() {
		return className.toCharArray();
	}

	public char[] getContents() {
		return srcCode.toCharArray();
	}

	public char[] getMainTypeName() {
		return className.toCharArray();
	}

	public char[][] getPackageName() {
		return new char[0][0];
	}

	public boolean ignoreOptionalProblems() {
		return false;
	}
}
