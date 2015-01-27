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

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRAbstractJavaCompiler;
import net.sf.jasperreports.engine.design.JRClassGenerator;
import net.sf.jasperreports.engine.design.JRCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRJavacCompiler;
import net.sf.jasperreports.engine.design.JRSourceCompileTask;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.functions.FunctionsUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRJdtCompiler.java 6152 2013-05-09 10:48:25Z teodord $
 */
public class JRJdtCompiler extends JRAbstractJavaCompiler {
	private static final String JDT_PROPERTIES_PREFIX = "org.eclipse.jdt.core.";
	private static final IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();
	public static final Log log = LogFactory.getLog(JRJdtCompiler.class);

	private final ClassLoader classLoader;

	/**
	 * 
	 */
	public JRJdtCompiler(JasperReportsContext jasperReportsContext) {
		super(jasperReportsContext, false);
		classLoader = getClassLoader();
	}

	/**
	 * @deprecated Replaced by {@link #JRJdtCompiler(JasperReportsContext)}.
	 */
	public JRJdtCompiler() {
		this(DefaultJasperReportsContext.getInstance());
	}

	/**
	 *
	 */
	protected String compileUnits(final JRCompilationUnit[] units, String classpath, File tempDirFile) {
		final INameEnvironment env = getNameEnvironment(units);

		final IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());

		final CompilerRequestor requestor = getCompilerRequestor(units);

		final Compiler compiler = new Compiler(env, policy, getJdtSettings(), requestor, problemFactory);

		do {
			CompilationUnit[] compilationUnits = requestor.processCompilationUnits();

			compiler.compile(compilationUnits);
		} while (requestor.hasMissingMethods());

		requestor.processProblems();

		return requestor.getFormattedProblems();
	}

	/**
	 * 
	 */
	protected INameEnvironment getNameEnvironment(final JRCompilationUnit[] units) {
		return new NameEnvironement(this, units);
	}

	/**
	 *
	 */
	protected CompilerRequestor getCompilerRequestor(final JRCompilationUnit[] units) {
		return new CompilerRequestor(jasperReportsContext, this, units);
	}

	private Map<String, String> settings;

	protected Map<String, String> getJdtSettings() {
		if (settings == null) {
			settings = new HashMap<String, String>();
			settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
			settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
			settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
			// if (ctxt.getOptions().getJavaEncoding() != null)
			// {
			// settings.put(CompilerOptions.OPTION_Encoding,
			// ctxt.getOptions().getJavaEncoding());
			// }
			// if (ctxt.getOptions().getClassDebugInfo())
			// {
			// settings.put(CompilerOptions.OPTION_LocalVariableAttribute,
			// CompilerOptions.GENERATE);
			// }

			List<JRPropertiesUtil.PropertySuffix> properties = JRPropertiesUtil.getInstance(jasperReportsContext).getProperties(JDT_PROPERTIES_PREFIX);
			for (Iterator<JRPropertiesUtil.PropertySuffix> it = properties.iterator(); it.hasNext();) {
				JRPropertiesUtil.PropertySuffix property = it.next();
				String propVal = property.getValue();
				if (propVal != null && propVal.length() > 0) {
					settings.put(property.getKey(), propVal);
				}
			}

			Properties systemProps = System.getProperties();
			for (Enumeration<String> it = (Enumeration<String>) systemProps.propertyNames(); it.hasMoreElements();) {
				String propName = it.nextElement();
				if (propName.startsWith(JDT_PROPERTIES_PREFIX)) {
					String propVal = systemProps.getProperty(propName);
					if (propVal != null && propVal.length() > 0) {
						settings.put(propName, propVal);
					}
				}
			}
		}
		return settings;
	}

	/**
	 *
	 */
	private ClassLoader getClassLoader() {
		ClassLoader clsLoader = Thread.currentThread().getContextClassLoader();
		if (clsLoader != null) {
			try {
				Class.forName(JRJdtCompiler.class.getName(), true, clsLoader);
			} catch (ClassNotFoundException e) {
				clsLoader = null;
				// if (log.isWarnEnabled())
				// log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRJdtCompiler class. Using JRJdtCompiler.class.getClassLoader() instead.");
			}
		}
		if (clsLoader == null)
			clsLoader = JRClassLoader.class.getClassLoader();
		return clsLoader;
	}

	protected InputStream getResource(String resourceName) {
		return classLoader.getResourceAsStream(resourceName);
	}

	protected Class<?> loadClass(String className) throws ClassNotFoundException {
		return classLoader.loadClass(className);
	}

	protected void checkLanguage(String language) throws JRException {
		if (!JRReport.LANGUAGE_JAVA.equals(language))
			throw new JRException("Language \"" + language + "\" not supported by this report compiler.\n" + "Expecting \"java\" instead.");
	}

	protected JRCompilationUnit recreateCompileUnit(JRCompilationUnit compilationUnit, Set<Method> missingMethods) {
		String unitName = compilationUnit.getName();

		JRSourceCompileTask sourceTask = compilationUnit.getCompileTask();
		JRCompilationSourceCode sourceCode = JRClassGenerator.modifySource(sourceTask, missingMethods, compilationUnit.getSourceCode());

		File sourceFile = compilationUnit.getSourceFile();
		File saveSourceDir = sourceFile == null ? null : sourceFile.getParentFile();
		sourceFile = getSourceFile(saveSourceDir, unitName, sourceCode);

		return new JRCompilationUnit(unitName, sourceCode, sourceFile, compilationUnit.getExpressions(), sourceTask);
	}

	protected JRCompilationSourceCode generateSourceCode(JRSourceCompileTask sourceTask) throws JRException {
		return JRClassGenerator.generateClass(sourceTask);
	}

	protected String getSourceFileName(String unitName) {
		return unitName + ".java";
	}

	protected String getCompilerClass() {
		return JRJavacCompiler.class.getName();
	}

	/**
	 * 
	 */
	public static class CompilerRequestor implements ICompilerRequestor {
		private final JasperReportsContext jasperReportsContext;
		protected final JRJdtCompiler compiler;
		protected final JRCompilationUnit[] units;
		protected final CompilationUnitResult[] unitResults;

		public CompilerRequestor(final JasperReportsContext jasperReportsContext, final JRJdtCompiler compiler, final JRCompilationUnit[] units) {
			this.jasperReportsContext = jasperReportsContext;
			this.compiler = compiler;
			this.units = units;
			this.unitResults = new CompilationUnitResult[units.length];

			reset();
		}

		public void acceptResult(CompilationResult result) {
			String className = ((CompilationUnit) result.getCompilationUnit()).className;

			int classIdx;
			for (classIdx = 0; classIdx < units.length; ++classIdx) {
				if (className.equals(units[classIdx].getName())) {
					break;
				}
			}

			if (result.hasErrors()) {
				// IProblem[] problems = result.getErrors();
				IProblem[] problems = getJavaCompilationErrors(result);

				unitResults[classIdx].problems = problems;

				String sourceCode = units[classIdx].getSourceCode();

				for (int i = 0; i < problems.length; i++) {
					IProblem problem = problems[i];

					if (IProblem.UndefinedMethod == problem.getID()) {
						if (problem.getSourceStart() >= 0 && problem.getSourceEnd() >= 0) {
							String methodName = sourceCode.substring(problem.getSourceStart(), problem.getSourceEnd() + 1);

							Method method = FunctionsUtil.getInstance(jasperReportsContext).getMethod4Function(methodName);
							if (method != null) {
								unitResults[classIdx].addMissingMethod(method);
								// continue;
							}
						}
					}
				}
			} else {
				ClassFile[] resultClassFiles = result.getClassFiles();
				for (int i = 0; i < resultClassFiles.length; i++) {
					units[classIdx].setCompileData(resultClassFiles[i].getBytes());
				}
			}
		}

		/**
		 * 
		 */
		public void processProblems() {
			// nothing to do here
		}

		/**
		 * 
		 */
		public String getFormattedProblems() {
			StringBuffer problemBuffer = new StringBuffer();

			for (int u = 0; u < units.length; u++) {
				String sourceCode = units[u].getSourceCode();

				IProblem[] problems = unitResults[u].problems;

				if (problems != null && problems.length > 0) {
					for (int i = 0; i < problems.length; i++) {
						IProblem problem = problems[i];

						problemBuffer.append(i + 1);
						problemBuffer.append(". ");
						problemBuffer.append(problem.getMessage());

						if (problem.getSourceStart() >= 0 && problem.getSourceEnd() >= 0) {
							int problemStartIndex = sourceCode.lastIndexOf("\n", problem.getSourceStart()) + 1;
							int problemEndIndex = sourceCode.indexOf("\n", problem.getSourceEnd());
							if (problemEndIndex < 0) {
								problemEndIndex = sourceCode.length();
							}

							problemBuffer.append("\n");
							problemBuffer.append(sourceCode.substring(problemStartIndex, problemEndIndex));
							problemBuffer.append("\n");
							for (int j = problemStartIndex; j < problem.getSourceStart(); j++) {
								problemBuffer.append(" ");
							}
							if (problem.getSourceStart() == problem.getSourceEnd()) {
								problemBuffer.append("^");
							} else {
								problemBuffer.append("<");
								for (int j = problem.getSourceStart() + 1; j < problem.getSourceEnd(); j++) {
									problemBuffer.append("-");
								}
								problemBuffer.append(">");
							}

							problemBuffer.append("\n");
						}
					}

					problemBuffer.append(problems.length);
					problemBuffer.append(" errors\n");
				}
			}

			return problemBuffer.length() > 0 ? problemBuffer.toString() : null;
		}

		/**
		 * 
		 */
		public boolean hasMissingMethods() {
			for (CompilationUnitResult unitResult : unitResults) {
				if (unitResult.hasMissingMethods()) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 
		 */
		public CompilationUnit[] processCompilationUnits() {
			final CompilationUnit[] compilationUnits = new CompilationUnit[units.length];

			for (int i = 0; i < compilationUnits.length; i++) {
				if (unitResults[i].hasMissingMethods()) {
					units[i] = compiler.recreateCompileUnit(units[i], unitResults[i].getMissingMethods());
					unitResults[i].resolveMissingMethods();
				}

				compilationUnits[i] = new CompilationUnit(units[i].getSourceCode(), units[i].getName());
			}

			reset();

			return compilationUnits;
		}

		/**
		 * 
		 */
		protected void reset() {
			for (int i = 0; i < unitResults.length; i++) {
				if (unitResults[i] == null) {
					unitResults[i] = new CompilationUnitResult();
				} else
					unitResults[i].reset();
			}
		}

		/**
		 * 
		 */
		protected IProblem[] getJavaCompilationErrors(CompilationResult result) {
			return result.getErrors();
			//
			// try {
			// Method getErrorsMethod = result.getClass().getMethod("getErrors",
			// (Class[]) null);
			// return (IProblem[]) getErrorsMethod.invoke(result, (Object[]) null);
			// } catch (SecurityException e) {
			// throw new JRRuntimeException("Error resolving JDT methods", e);
			// } catch (NoSuchMethodException e) {
			// throw new JRRuntimeException("Error resolving JDT methods", e);
			// } catch (IllegalArgumentException e) {
			// throw new JRRuntimeException("Error invoking JDT methods", e);
			// } catch (IllegalAccessException e) {
			// throw new JRRuntimeException("Error invoking JDT methods", e);
			// } catch (InvocationTargetException e) {
			// throw new JRRuntimeException("Error invoking JDT methods", e);
			// }
		}
	}

}
