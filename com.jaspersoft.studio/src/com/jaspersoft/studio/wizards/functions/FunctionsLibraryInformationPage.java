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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.WordUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.swt.widgets.AutoCompletionHelper;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

/**
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class FunctionsLibraryInformationPage extends NewTypeWizardPage {

	private Text libraryName;
	private Text categoryClass;
	private IJavaProject selectedJavaProject;
	private Button createSampleFunctions;
	private Button createSampleJRXML;
	private List<String> existingCategories;

	private IStatus libraryNameStatus = Status.OK_STATUS;
	private IStatus categoryClassStatus = Status.OK_STATUS;
	private Text categoryLabel;
	private Text categoryDescription;

	protected FunctionsLibraryInformationPage() {
		super(0,"functionsLibraryInformationPage");
		setTitle("Functions Library Information");
		setDescription("Please enter the details for the new functions library that will be created.");
	}

	/**
	 * The wizard owning this page is responsible for calling this method with the
	 * current selection. The selection is used to initialize the fields of the wizard
	 * page.
	 *
	 * @param selection used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem= getInitialJavaElement(selection);
		if(jelem!=null){
			selectedJavaProject = jelem.getJavaProject();
		}
		initContainerPage(jelem);
		initTypePage(jelem);
	}

	@Override
	public boolean isPageComplete() {
		return 
				getErrorMessage()==null && 
				!libraryName.getText().isEmpty() &&
				!getPackageFragmentRootText().isEmpty() &&
				!categoryLabel.getText().isEmpty() &&
				!categoryClass.getText().isEmpty(); 
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		int cols = 4;
		GridLayout layout= new GridLayout();
		layout.numColumns= cols;
		composite.setLayout(layout);
		createLibraryNameControls(composite,cols);
		createContainerControls(composite, cols);
		createPackageControls(composite, cols);
		createSeparator(composite, cols);
		createCategoryLabelAndDescControls(composite,cols);
		createCategoryClassControls(composite,cols);
		createSeparator(composite, cols);
		createSamplesControls(composite,cols);
		setControl(composite);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,ContextHelpIDs.WIZARD_NEW_FUNCTIONS_LIBRARY);
	}

	// UI related methods
	
	private void createLibraryNameControls(Composite parent, int cols) {
		Label libraryNameLbl = new Label(parent, SWT.NONE);
		libraryNameLbl.setText("Library Name:");
		libraryNameLbl.setToolTipText("A class-name like identifier that will be used to generate the class file containing your functions");
		libraryNameLbl.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		libraryName = new Text(parent, SWT.BORDER);
		libraryName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
		libraryName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				libraryNameStatus = JavaConventions.validateJavaTypeName(
						libraryName.getText(), JavaCore.VERSION_1_6, JavaCore.VERSION_1_6);
				doStatusUpdate();
			}
		});
	}
	
	@Override
	protected void createContainerControls(Composite parent, int nColumns) {
		super.createContainerControls(parent, nColumns);
		setTooltipOnPrivateField(NewContainerWizardPage.class, "fContainerDialogField", "The chosen Build-Path entry");
	}

	@Override
	protected void createPackageControls(Composite parent, int nColumns) {
		super.createPackageControls(parent, nColumns);
		setTooltipOnPrivateField(NewTypeWizardPage.class, "fPackageDialogField", "The Java package where most of the generated output files will be placed");
	}
	
	private void createCategoryLabelAndDescControls(Composite parent, int cols) {
		Label categoryLabelLbl = new Label(parent, SWT.NONE);
		categoryLabelLbl.setText("Category Label:");
		categoryLabelLbl.setToolTipText("The text shown in the Expression Editor categories list");
		categoryLabelLbl.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		categoryLabel = new Text(parent, SWT.BORDER);
		categoryLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
		categoryLabel.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// Try to guess the category name
				// Capitalize the first character of each word
				// and remove the whitespaces
				String nameTxt = categoryLabel.getText();
				nameTxt = WordUtils.capitalize(nameTxt).replaceAll("\\s","");
				// suggest the class
				String txt = getPackageText();
				if(!txt.isEmpty()) txt+=".";
				categoryClass.setText(txt+nameTxt);
				doStatusUpdate();
			}
		});
		
		
		Label categoryDescriptionLbl = new Label(parent, SWT.NONE);
		categoryDescriptionLbl.setText("Category Description:");
		categoryDescriptionLbl.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		categoryDescriptionLbl.setToolTipText("Additional details regarding the category");
		categoryDescription = new Text(parent, SWT.BORDER);
		categoryDescription.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
	}
	
	private void createCategoryClassControls(Composite parent, int cols) {
		Label categoryClassLbl = new Label(parent, SWT.NONE);
		categoryClassLbl.setText("Category Class:");
		categoryClassLbl.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		categoryClassLbl.setToolTipText("The class that will represent the category. Usually automatically suggested");
		categoryClass = new Text(parent, 	SWT.BORDER);
		categoryClass.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
		categoryClass.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				categoryClassStatus = JavaConventions.validateJavaTypeName(
						categoryClass.getText(), JavaCore.VERSION_1_6, JavaCore.VERSION_1_6);
				if(categoryClassStatus.isOK()){
					// Ensure Library Class is different from Category Class
					String libraryNameTxt = libraryName.getText();
					String categoryClassTxt = categoryClass.getText();
					if(categoryClassTxt.endsWith("."+libraryNameTxt) || categoryClassTxt.equals(libraryNameTxt)) {
						categoryClassStatus = 
								new Status(IStatus.ERROR, JaspersoftStudioPlugin.PLUGIN_ID, -1, "Category class can not be the same one of the Library itself", null);
					}
				}
				doStatusUpdate();
			}
		});
		AutoCompletionHelper.enableAutoCompletion(categoryClass, getExistingCategories());
	}

	private void createSamplesControls(Composite parent, int cols) {
		Label samplesCreationQuestion = new Label(parent, SWT.NONE);
		samplesCreationQuestion.setText("Which samples information do you want to create?");
		samplesCreationQuestion.setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, false, cols, 1));
		
		Label emptyLbl = new Label(parent, SWT.NONE);
		emptyLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		
		createSampleFunctions = new Button(parent, SWT.CHECK);
		createSampleFunctions.setText("Some example methods");
		createSampleFunctions.setToolTipText("Will create some example methods in the generated function class");
		createSampleFunctions.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
		createSampleFunctions.setSelection(true);
		createSampleFunctions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// suggest also the report creation if possible
				boolean enable = createSampleFunctions.getSelection();
				createSampleJRXML.setEnabled(enable);
				createSampleJRXML.setSelection(enable);
			}
		});
		
		createSampleJRXML = new Button(parent, SWT.CHECK);
		createSampleJRXML.setText("A sample report that uses the example functions");
		createSampleJRXML.setToolTipText("Creates an example JRXML that will use the newly created sample functions");
		createSampleJRXML.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
		createSampleJRXML.setSelection(true);
	}
	
	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		doStatusUpdate();
	}
	
	private void doStatusUpdate() {
		// status of all used components
		IStatus[] status= new IStatus[] {
			libraryNameStatus,
			fContainerStatus,
			fPackageStatus,
			categoryClassStatus
		};
		// the mode severe status will be displayed
		updateStatus(status);
	}
	
	@Override
	public void setMessage(String newMessage, int newType) {
		if("OK".equals(newMessage)){
			newMessage = getDescription();
		}
		super.setMessage(newMessage, newType);
	}
	
	// Utility methods
	
	private List<String> getExistingCategories() {
		if(existingCategories==null) {
			final Set<String> categories = new TreeSet<String>();		
			if(selectedJavaProject!=null) {
				SearchRequestor requestor = new SearchRequestor() {
					@Override
					public void acceptSearchMatch(SearchMatch match) throws CoreException {
						if(match.getElement() instanceof IType) {
							String fqn = ((IType) match.getElement()).getFullyQualifiedName();
							if(!fqn.startsWith("net.sf.jasperreports.functions.standard")) {
								// avoid to propose standard functions categories
								categories.add(fqn);
							}
						}
					}
				};
				IJavaElement[] elements= new IJavaElement[] { selectedJavaProject };
				IJavaSearchScope scope= SearchEngine.createJavaSearchScope(elements);
				int matchRule= SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE;
				SearchPattern fullAnnotationPattern= SearchPattern.createPattern(
						"net.sf.jasperreports.functions.annotations.FunctionCategory", IJavaSearchConstants.ANNOTATION_TYPE, IJavaSearchConstants.ANNOTATION_TYPE_REFERENCE, matchRule);
				SearchPattern simpleNamePattern= SearchPattern.createPattern(
						"FunctionCategory", IJavaSearchConstants.ANNOTATION_TYPE, IJavaSearchConstants.ANNOTATION_TYPE_REFERENCE, matchRule);
				SearchPattern annotationsPattern= SearchPattern.createOrPattern(fullAnnotationPattern, simpleNamePattern);
				SearchParticipant[] searchParticipants= new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
				try {
					new SearchEngine().search(annotationsPattern, searchParticipants, scope, requestor,new NullProgressMonitor());
				} catch (CoreException e) {
				}
			}
			existingCategories = new ArrayList<String>(categories);
			((NewFunctionsLibraryWizard)getWizard()).setAvailableCategories(existingCategories);
		}
		return existingCategories;
	}

	// Getters
	
	/**
	 * @return the chosen name for the functions library
	 */
	public String getLibraryName() {
		return libraryName.getText();
	}
	
	/**
	 * @return <code>true</code> if the sample methods should be created, <code>false</code> otherwise
	 */
	public boolean isCreateSampleFunctions() {
		return createSampleFunctions.getSelection();
	}

	/**
	 * @return <code>true</code> if the sample report should be created, <code>false</code> otherwise
	 */
	public boolean isCreateSampleReport() {
		return createSampleJRXML.getSelection();
	}
	
	/**
	 * @return the category full qualified class name
	 */
	public String getCategoryClass() {
		return categoryClass.getText();
	}
	
	/**
	 * @return the category label
	 */
	public String getCategoryLabel() {
		return categoryLabel.getText();
	}	
	
	/**
	 * @return the category description
	 */
	public String getCategoryDescription() {
		return categoryDescription.getText();
	}
	
	@SuppressWarnings("restriction")
	private void setTooltipOnPrivateField(Class<?> targetClazz, String fieldName, String tooltip) {
		try {
			Field containerWidget = targetClazz.getDeclaredField(fieldName);
			containerWidget.setAccessible(true);
			Object obj = containerWidget.get(this);
			if(obj instanceof DialogField) {
				((DialogField)obj).getLabelControl(null).setToolTipText(tooltip);
			}
		} catch (Exception e) {
			JaspersoftStudioPlugin.getInstance().logError("Unable to set the tooltip on the label control", e);
		}
	}
}
