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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.eclipse.classpath.ClassLoaderUtil;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.util.JRLoader;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

public class NameEnvironement implements INameEnvironment {

	public JRCompilationUnit[] units;
	private final JRJdtCompiler jrJdtCompiler;

	public NameEnvironement(JRJdtCompiler jrJdtCompiler, JRCompilationUnit[] units) {
		this.jrJdtCompiler = jrJdtCompiler;
		this.units = units;

	}

	public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
		StringBuffer result = new StringBuffer();
		String sep = "";
		for (int i = 0; i < compoundTypeName.length; i++) {
			result.append(sep);
			result.append(compoundTypeName[i]);
			sep = ".";
		}
		return findType(result.toString());
	}

	public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {
		StringBuffer result = new StringBuffer();
		String sep = "";
		for (int i = 0; i < packageName.length; i++) {
			result.append(sep);
			result.append(packageName[i]);
			sep = ".";
		}
		result.append(sep);
		result.append(typeName);
		return findType(result.toString());
	}

	private int getClassIndex(String className) {
		for (int classIdx = 0; classIdx < units.length; ++classIdx) {
			if (className.equals(units[classIdx].getName()))
				return classIdx;
		}
		return -1;
	}

	private Map<String, NameEnvironmentAnswer> map = new HashMap<String, NameEnvironmentAnswer>();

	protected NameEnvironmentAnswer findType(String className) {
		NameEnvironmentAnswer ne = map.get(className);
		if (ne != null)
			return ne;
		if (ClassLoaderUtil.packages.contains(className))
			return null;
		try {
			int classIdx = getClassIndex(className);
			if (classIdx >= 0)
				ne = NameEnvironmentAnswerFactory.getCompilationUnit(new CompilationUnit(units[classIdx].getSourceCode(), className));
			else {
				String resourceName = className.replace('.', '/') + ".class";

				byte[] classBytes = getResource(resourceName);
				if (classBytes != null)
					ne = NameEnvironmentAnswerFactory.getClassFileReader(new ClassFileReader(classBytes, className.toCharArray(), true));
				// else System.out.println(className);
			}
		} catch (JRException e) {
			JRJdtCompiler.log.error("Compilation error", e);
		} catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException exc) {
			JRJdtCompiler.log.error("Compilation error", exc);
		} catch (InvocationTargetException e) {
			throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
		} catch (IllegalArgumentException e) {
			throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
		} catch (InstantiationException e) {
			throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
		} catch (IllegalAccessException e) {
			throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
		}
		map.put(className, ne);
		return ne;
	}

	protected byte[] getResource(String name) throws JRException {
		byte[] res = null;
		InputStream is = jrJdtCompiler.getResource(name);
		if (is != null) {
			try {
				res = JRLoader.loadBytes(is);
			} finally {
				FileUtils.closeStream(is);
			}
		}
		return res;
	}

	private Map<String, Boolean> pmap = new HashMap<String, Boolean>();

	protected boolean isPackage(String result) {
		Boolean b = pmap.get(result);
		if (b != null)
			return b;
		b = ClassLoaderUtil.packages.contains(result);
		if (b)
			return b;
		int classIdx = getClassIndex(result);
		if (classIdx >= 0)
			b = false;
		else {
			// b = true;
			// try {
			// b = jrJdtCompiler.loadClass(result) == null;
			// if (!b)
			// System.out.println(result);
			// } catch (ClassNotFoundException e1) {
			// // TODO Auto-generated catch block
			// // e1.printStackTrace();
			// System.out.println(result);
			// }

			String resourceName = result.replace('.', '/') + ".class";

			b = true;

			InputStream is = jrJdtCompiler.getResource(resourceName);

			if (is != null)// cannot just test for null; need to read from
			// "is" to
			// avoid bug
			{ // with sun.plugin.cache.EmptyInputStream on JRE 1.5 plugin
				try //
				// http://sourceforge.net/tracker/index.php?func=detail&aid=1478460&group_id=36382&atid=416703
				{
					b = (is.read() > 0);
				} catch (IOException e) {
					// ignore
				} finally {
					FileUtils.closeStream(is);
				}
			}
		}
		pmap.put(result, b);
		return b;
	}

	public boolean isPackage(char[][] parentPackageName, char[] packageName) {
		StringBuffer result = new StringBuffer();
		String sep = "";
		if (parentPackageName != null) {
			for (int i = 0; i < parentPackageName.length; i++) {
				result.append(sep);
				result.append(parentPackageName[i]);
				sep = ".";
			}
		}
		if (Character.isUpperCase(packageName[0]) && !isPackage(result.toString()))
			return false;
		result.append(sep);
		result.append(packageName);
		return isPackage(result.toString());
	}

	public void cleanup() {
	}
}
