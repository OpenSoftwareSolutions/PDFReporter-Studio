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
package com.jaspersoft.studio.wizards.functions;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

import net.sf.jasperreports.eclipse.builder.jdt.JDTUtils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;

import com.jaspersoft.studio.utils.Misc;

/**
 * Utility class for the tasks to be performed during the new functions library creation.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class FunctionsLibraryGenerationUtil {

	private static final String TEMPLATES_LOCATION_PREFIX = "com/jaspersoft/studio/wizards/functions/templates/";
	private static final String FUNCTION_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "FunctionClass.vm";
	private static final String CATEGORY_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "CategoryClass.vm";
	private static final String LIBRARY_MESSAGES_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "SampleFunctionsMessages.vm";
	private static final String JR_EXTENSION_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "JRExtension.vm";
	private static final String SAMPLE_JRXML_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "SampleFunctionsReport.vm";
	
	private static final String JR_MESSAGES_PROPERTIES = "jasperreports_messages.properties";
	private static final String JR_EXTENSION_PROPERTIES = "jasperreports_extension.properties";
	private static final String SAMPLE_JRXML = "SampleFunctionsReport.jrxml";
	
	private IJavaProject javaProject;
	private IPackageFragmentRoot packageFragmentRoot;
	private IPackageFragment packageFragment;
	private IProgressMonitor monitor;
	private VelocityEngine ve;
	private String libraryName;
	private String packageName;
	
	public FunctionsLibraryGenerationUtil(GenerationInfo config, VelocityEngine ve, IProgressMonitor monitor){
		this.ve = ve;
		this.monitor = monitor;
		this.javaProject = config.getJavaProject();
		this.packageFragmentRoot = config.getPackageFragmentRoot();
		this.packageFragment = config.getPackageFragment();
		this.libraryName = config.getLibraryName();
		this.packageName = config.getPackageName();
	}

	/**
	 * Creates the new functions library class.
	 * 
	 * @param categories the list of categories to which the functions will belong to
	 * @param createSampleMethods specifies if the samples methods should be created
	 * @throws CoreException
	 */
	public void createFunctionClass(
			List<String> categories, boolean createSampleMethods) throws CoreException {
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", packageName);
		functionContext.put("categoriesList", categories);
		functionContext.put("functionLibraryName", libraryName);
		functionContext.put("printSampleMethods", createSampleMethods);

		Template functionTemplate = ve.getTemplate(FUNCTION_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		
		ICompilationUnit functionCU = packageFragment.createCompilationUnit(libraryName+".java", fsw.toString(), true, monitor);
		JDTUtils.formatUnitSourceCode(functionCU, monitor);
		functionCU.save(monitor, true);
		
		// Then let's create/update the internationalization properties file for the functions. 
		// It might be the same of the category or not.
		// We need it only if sample functions creation is enable.
		if(createSampleMethods) {
			IPath packagePath = packageFragment.getPath().makeRelativeTo(javaProject.getPath());
			IFile functionsLibraryI18nPropertiesFile = javaProject.getProject().getFile(packagePath.append(JR_MESSAGES_PROPERTIES));
			Template functionsLibraryI18nTemplate = ve.getTemplate(LIBRARY_MESSAGES_TEMPLATE_LOCATION);
			VelocityContext functionsLibContext = new VelocityContext();
			String functionsLibraryClass = libraryName;
			if(!Misc.isNullOrEmpty(packageName)){
				functionsLibraryClass=packageName+"."+libraryName;
			}
			functionsLibContext.put("functionsLibraryClass", functionsLibraryClass);
			StringWriter flibsw = new StringWriter();
			functionsLibraryI18nTemplate.merge(functionsLibContext, flibsw);
			if(functionsLibraryI18nPropertiesFile.exists()) {
				functionsLibraryI18nPropertiesFile.appendContents(new ByteArrayInputStream(("\n"+flibsw.toString()).getBytes()), IResource.FORCE, monitor);
			} else {
				functionsLibraryI18nPropertiesFile.create(new ByteArrayInputStream(flibsw.toString().getBytes()), IResource.FORCE, monitor);
			}
		}		
	}
	
	/**
	 * Creates if needed the category to which the new functions library will belong to.
	 * It also creates/updates the internationalization file.
	 * 
	 * @param categoryClass the full qualified name of the category class to create
	 * @param categoryLabel the label of the category
	 * @param categoryDescription the description of the category
	 * @throws CoreException
	 */
	public void createCategoryClass(String categoryClass, String categoryLabel, String categoryDescription) throws CoreException{
		// Create category class (pay attention it could exist)
		int lastDotIdx = categoryClass.lastIndexOf('.');
		String categoryPackage = "";
		if(lastDotIdx!=-1){
			categoryPackage=categoryClass.substring(0, lastDotIdx);
		}
		String categoryClassName = categoryClass.substring(lastDotIdx+1);
		IType categoryClassType = javaProject.findType(categoryClass);
		IPath categoryPackagePath = null;
		boolean categoryExists = categoryClassType != null;
		if(!categoryExists) {
			// New category we should create it
			VelocityContext categoryContext = new VelocityContext();
			categoryContext.put("categoryPackage", categoryPackage);
			categoryContext.put("categoryClassName", categoryClassName);
			Template categoryTemplate = ve.getTemplate(CATEGORY_CLASS_TEMPLATE_LOCATION);
			StringWriter csw = new StringWriter();
			categoryTemplate.merge( categoryContext, csw );
			
			ICompilationUnit categoryCU = packageFragment.createCompilationUnit(categoryClassName+".java", csw.toString(), true, monitor);
			JDTUtils.formatUnitSourceCode(categoryCU, monitor);
			categoryCU.save(monitor,true);
			categoryPackagePath = packageFragment.getPath().makeRelativeTo(javaProject.getPath());
		} else {
			categoryPackagePath = categoryClassType.getPath().makeRelativeTo(javaProject.getPath());;
		}
		
		// We should create the internationalization properties file
		// Let's first create category one, or update if it exists
		if(!categoryExists) {
			IFile categoryI18nPropertiesFile = javaProject.getProject().getFile(categoryPackagePath.append(JR_MESSAGES_PROPERTIES));
			StringBuffer sb = new StringBuffer("\n");
			sb.append(categoryClass).append(".").append("name").append("=").append(categoryLabel).append("\n");
			sb.append(categoryClass).append(".").append("description").append("=").append(categoryDescription);
			if(categoryI18nPropertiesFile.exists()){
				categoryI18nPropertiesFile.appendContents(new ByteArrayInputStream(sb.toString().getBytes()), IResource.FORCE, monitor);
			}
			else {
				categoryI18nPropertiesFile.create(new ByteArrayInputStream(sb.toString().getBytes()), IResource.FORCE, monitor);
			}
		}		
	}
	
	/**
	 * Creates or updates the JasperReports Extension file needed to add the new functions library.
	 * <p>
	 * 
	 * If the file already exists it appends the new library information.
	 * 
	 * @throws CoreException
	 */
	public void createJasperReportsExtensionFile() throws CoreException {
		// We should create at source folder level the properties file jasperreports_extension.properties
		// If it exists we should append the information.
		IPath srcFolderPath = packageFragmentRoot.getPath().makeRelativeTo(javaProject.getPath());
		IFile extensionsFile = javaProject.getProject().getFile(srcFolderPath.append(JR_EXTENSION_PROPERTIES));
		String libraryClass = libraryName;
		if(!Misc.isNullOrEmpty(packageName)){
			libraryClass=packageName+"."+libraryName;
		}
		if(extensionsFile.exists()) {
			// we should append the new library information
			String newLibrary = "\nnet.sf.jasperreports.extension.functions." + libraryName.toLowerCase() + "=" + libraryClass ;
			extensionsFile.appendContents(new ByteArrayInputStream(newLibrary.getBytes()), IResource.FORCE, monitor);
		}
		else {
			// use the template
			Template extensionTemplate = ve.getTemplate(JR_EXTENSION_TEMPLATE_LOCATION);			
			VelocityContext extensionContext = new VelocityContext();
			extensionContext.put("libraryName", libraryName.toLowerCase());
			extensionContext.put("libraryClass", libraryClass);
			StringWriter extsw = new StringWriter();
			extensionTemplate.merge(extensionContext, extsw);
			extensionsFile.create(new ByteArrayInputStream(extsw.toString().getBytes()), IResource.FORCE, monitor);	
		}
	}

	/**
	 * Creates the sample JRXML file that uses the sample methods of the newly created 
	 * library of functions.
	 * @throws CoreException 
	 */
	public void createSampleJRXML() throws CoreException {
		IFile sampleJRXMLFile = javaProject.getProject().getFile(SAMPLE_JRXML);
		if(!sampleJRXMLFile.exists()){
			// For now we don't need to specify anything in the context
			// It will simply "copy" the JRXML content
			VelocityContext sampleJRXMLContext = new VelocityContext();
			Template categoryTemplate = ve.getTemplate(SAMPLE_JRXML_TEMPLATE_LOCATION);
			StringWriter jrxmlsw = new StringWriter();
			categoryTemplate.merge( sampleJRXMLContext, jrxmlsw );
			sampleJRXMLFile.create(new ByteArrayInputStream(jrxmlsw.toString().getBytes()), IResource.FORCE, monitor);
		}
	}
}
