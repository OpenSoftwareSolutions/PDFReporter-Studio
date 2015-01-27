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
package net.sf.jasperreports.eclipse.builder;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.NoSuchMechanismException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.data.AbstractClasspathAwareDataAdapterService;
import net.sf.jasperreports.eclipse.builder.jdt.JRJdtCompiler;
import net.sf.jasperreports.eclipse.builder.jdt.NameEnvironement;
import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.util.xml.SourceLocation;
import net.sf.jasperreports.eclipse.util.xml.SourceTraceDigester;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRCompiler;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRValidationException;
import net.sf.jasperreports.engine.design.JRValidationFault;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.engine.xml.JRReportSaxParserFactory;
import net.sf.jasperreports.engine.xml.JRSaxParserFactory;
import net.sf.jasperreports.engine.xml.JRXmlDigesterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.xml.sax.SAXException;

/*
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JasperReportCompiler.java 24 2007-03-09 17:50:27Z lucianc $
 */
public class JasperReportCompiler {
	private IProject project;
	private JasperReportErrorHandler errorHandler;

	private JRSaxParserFactory parserFactory = new JRReportSaxParserFactory();
	private SourceTraceDigester digester;

	public JasperReportCompiler() {
	}

	public JasperReport compileReport(JasperReportsContext jasperReportsContext, IFile jrxml) throws CoreException {
		try {
			setProject(jrxml.getProject());
			JasperDesign jasperDesign = loadJasperDesign(jasperReportsContext, jrxml);
			return compileReport(jasperReportsContext, jasperDesign);
		} catch (JRException e) {
			errorHandler.addMarker(e);
		}
		return null;
	}

	private Map<String, JRCompiler> map = new HashMap<String, JRCompiler>();
	private Map<String, Boolean> mpack = new HashMap<String, Boolean>();
	private Map<String, byte[]> mtype = new HashMap<String, byte[]>();

	public JasperReport compileReport(JasperReportsContext jasperReportsContext, JasperDesign jasperDesign) throws CoreException {
		long start = System.currentTimeMillis();
		long bcomp = start;
		JasperReport report = null;
		String language = jasperDesign.getLanguage();
		try {
			createDigester();
			if (language == null || language.isEmpty()) {
				jasperDesign.setLanguage(JRReport.LANGUAGE_GROOVY);
				language = JRReport.LANGUAGE_GROOVY;
			}
			JRCompiler compiler = map.get(language);
			if (compiler == null) {
				if (JRReport.LANGUAGE_JAVA.equals(language)) {
					compiler = new JRJdtCompiler(jasperReportsContext) {

						@Override
						protected CompilerRequestor getCompilerRequestor(JRCompilationUnit[] units) {
							return new LocalCompilerRequestor(jasperReportsContext, this, units);
						}

						@Override
						protected INameEnvironment getNameEnvironment(JRCompilationUnit[] units) {
							return new NameEnvironement(this, units) {
								@Override
								protected boolean isPackage(String result) {
									// return super.isPackage(result);
									if (result.isEmpty())
										return true;
									Boolean isPack = mpack.get(result);
									if (isPack == null) {
										// System.out.println(result);
										isPack = super.isPackage(result);
										mpack.put(result, isPack);
									}
									return isPack;
								}

								@Override
								protected byte[] getResource(String name) throws JRException {
									if (mtype.containsKey(name))
										return mtype.get(name);
									byte[] bt = super.getResource(name);
									mtype.put(name, bt);
									return bt;
								}
							};

						}
					};
				} else {
					String compilerClassName = JRPropertiesUtil.getInstance(jasperReportsContext).getProperty(JRCompiler.COMPILER_PREFIX + language);
					try {
						Class clazz = null;
						ClassLoader cl = (ClassLoader) jasperReportsContext.getValue(AbstractClasspathAwareDataAdapterService.CURRENT_CLASS_LOADER);
						if (cl != null)
							clazz = cl.loadClass(compilerClassName);
						else
							clazz = JRClassLoader.loadClassForName(compilerClassName);
						try {
							Constructor c = clazz.getDeclaredConstructor(JasperReportsContext.class, boolean.class);
							compiler = (JRCompiler) c.newInstance(jasperReportsContext, false);
						} catch (NoSuchMethodException nsme) {
							try {
								Constructor c = clazz.getDeclaredConstructor(JasperReportsContext.class);
								compiler = (JRCompiler) c.newInstance(jasperReportsContext);
							} catch (NoSuchMechanismException e1) {
								compiler = (JRCompiler) clazz.newInstance();
							}
						}
					} catch (Exception e) {
						throw new JRException(Messages.JasperReportCompiler_ErrorInitializationReportCompiler + compilerClassName, e);
					}
				}
				map.put(language, compiler);
			}
			bcomp = System.currentTimeMillis();
			report = compiler.compileReport(jasperDesign);
		} catch (JRValidationException e) {
			setValidationMarkers(e, jasperDesign);
		} catch (JRException e) {
			errorHandler.addMarker(e);
		}
		long end = System.currentTimeMillis();
		System.out.println("Compiled: " + language + " " + (bcomp - start) + " ms " + (end - start) + " ms");
		return report;
	}

	protected void setValidationMarkers(JRValidationException e, JasperDesign design) {
		for (JRValidationFault fault : e.getFaults()) {
			String message = fault.getMessage();
			SourceLocation location = null;
			Object source = fault.getSource();
			if (source != null) {
				location = digester.getLocation(source);
				if (location == null)
					message = message + " --- " + source.toString(); //$NON-NLS-1$
			}
			if (source instanceof StandardTable){
				JRDesignElement componentElement = getElementFromTable(design.getAllBands(), (StandardTable)source);
				if (componentElement != null) source = componentElement;
			}
			if (location == null && source instanceof JRDesignElement) errorHandler.addMarker(message, location, (JRDesignElement)source);
			else errorHandler.addMarker(message, location);
		}
	}
	
	private JRDesignElement getElementFromTable(JRChild[] childs, StandardTable table){	
		for(JRChild child : childs){
			if (child instanceof JRDesignComponentElement && ((JRDesignComponentElement)child).getComponent() == table) return (JRDesignElement)child;
			if (child instanceof JRElementGroup) {
				JRElementGroup group = (JRElementGroup)child;
				JRDesignElement value = getElementFromTable(group.getElements(), table);
				if (value != null) return value;
			}
		}
		return null;
	}

	protected JasperDesign loadJasperDesign(JasperReportsContext jasperReportsContext, final IFile file) throws JRException, CoreException {
		InputStream in = file.getContents();
		try {
			return new JRXmlLoader(jasperReportsContext, createDigester()).loadXML(in);
		} finally {
			FileUtils.closeStream(in);
		}
	}

	protected SourceTraceDigester createDigester() throws JRException {
		if (digester == null) {
			// FIXME reuse code from JRXmlDigesterFactory
			SAXParser parser = parserFactory.createParser();
			digester = new SourceTraceDigester(parser);
			try {
				JRXmlDigesterFactory.setComponentsInternalEntityResources(digester);
				JRXmlDigesterFactory.configureDigester(digester);
			} catch (SAXException e) {
				throw new JRException(e);
			} catch (ParserConfigurationException e) {
				throw new JRException(e);
			}
		}
		return digester;
	}

	public JasperReportErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(JasperReportErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public void setProject(IProject project) {
		if (this.project != project)
			clean();
		this.project = project;
	}

	private void clean() {
		// System.out.println("------------------------------------------");
		map = new HashMap<String, JRCompiler>();
		mtype = new HashMap<String, byte[]>();
		mpack = new HashMap<String, Boolean>();
	}

	/**
	 * 
	 */
	protected class LocalCompilerRequestor extends JRJdtCompiler.CompilerRequestor {
		private Set expressions = new HashSet();

		protected LocalCompilerRequestor(final JasperReportsContext jasperReportsContext, final JRJdtCompiler compiler, final JRCompilationUnit[] units) {
			super(jasperReportsContext, compiler, units);
		}

		@Override
		public void processProblems() {
			for (int i = 0; i < units.length; i++) {
				JRCompilationSourceCode sourceCode = units[i].getCompilationSource();
				IProblem[] problems = unitResults[i].getProblems();
				if (problems != null) {
					for (int j = 0; j < problems.length; j++) {
						IProblem problem = problems[j];
						int line = problem.getSourceLineNumber();
						JRExpression expression = sourceCode.getExpressionAtLine(line);
						if (expression == null)
							errorHandler.addMarker(problem, null);
						else if (!addExpressionError(expression))
							errorHandler.addMarker(problem, digester.getLocation(expression), expression);
					}
				}
			}
		}

		protected boolean addExpressionError(JRExpression expression) {
			boolean b = expressions.contains(expression);
			if (!b)
				expressions.add(expression);
			return b;
		}
	}
}
