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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.utils.VelocityUtils;

/**
 * Wizard for the new functions library creation.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class NewFunctionsLibraryWizard extends Wizard implements INewWizard {

	private FunctionsLibraryInformationPage page1;
	private AdditionalFunctionsCategoriesPage page2;
	private List<String> availableCategories;
	
	/**
	 * Wizard constructor.
	 */
	public NewFunctionsLibraryWizard() {
		super();
		setWindowTitle("New Functions Library Wizard");
		setNeedsProgressMonitor(true);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page1 = new FunctionsLibraryInformationPage();
		page1.setWizard(this);
		page1.init(selection);
		page2 = new AdditionalFunctionsCategoriesPage();
		page2.setWizard(this);
		addPage(page1);
		addPage(page2);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		// Gather information
		final String libraryName = page1.getLibraryName();
		final String sourceFolder = page1.getPackageFragmentRootText();
		final String packageName = page1.getPackageText();
		final String categoryLabel = page1.getCategoryLabel();
		final String categoryDescription = page1.getCategoryDescription();
		final String categoryClass = page1.getCategoryClass();
		final boolean createSampleMethods = page1.isCreateSampleFunctions();
		final boolean createSampleReport = page1.isCreateSampleReport();
		final List<String> allCategoriesCategories = new ArrayList<String>();
		allCategoriesCategories.add(categoryClass);
		allCategoriesCategories.addAll(page2.getAdditionalCategories());

		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						// Create the package if necessary
						IJavaProject javaProject = page1.getJavaProject();
						IPackageFragmentRoot packageFragmentRoot = page1.getPackageFragmentRoot();
						IPackageFragment pFragment = packageFragmentRoot.createPackageFragment(packageName, true, monitor);

						// Configure the Velocity Engine
						VelocityEngine ve = VelocityUtils.getConfiguredVelocityEngine();
						
						GenerationInfo config = new GenerationInfo(javaProject, packageFragmentRoot, pFragment, libraryName, packageName);
						FunctionsLibraryGenerationUtil generationUtil = new FunctionsLibraryGenerationUtil(config, ve, monitor);
						
						// Create the function class information
						generationUtil.createFunctionClass(allCategoriesCategories, createSampleMethods);
						
						// Create the category class information
						generationUtil.createCategoryClass(categoryClass, categoryLabel, categoryDescription);
						
						// Create the JR Extension information
						generationUtil.createJasperReportsExtensionFile();
						
						// Create the sample JRXML inside the root of the project
						if(createSampleReport){
							generationUtil.createSampleJRXML();
						}
					} catch (Exception e) {
						throw new InvocationTargetException(e, "An error occurred during the functions library creation");
					}
			
				}
			});
		} catch (Exception e) {
			UIUtils.showError(e);
		}

		return true;
	}
	
	public void setAvailableCategories(List<String> categories) {
		this.availableCategories = categories;
	}
	
	public List<String> getAvailableCategories() {
		return availableCategories;
	}

}
