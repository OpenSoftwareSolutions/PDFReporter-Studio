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
package net.sf.jasperreports.eclipse.messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "net.sf.jasperreports.eclipse.messages.messages"; //$NON-NLS-1$
	public static String AbstractJRUIPlugin_GenericErrorMsg;
	public static String BundleCommonUtils_LoggingToStdErr;
	public static String BundleCommonUtils_LoggingToStdOut;
	public static String BundleCommonUtils_MessagePrefix;
	public static String CleanTempResources_SearchBrokenLinksTask;
	public static String CleanTempResources_SearchProjectTask;
	public static String CreateStyleTemplateCommand_loadStyleError;
	public static String EmptyQueryExecuterFactoryBundle_NoFactoryClassRegistered;
	public static String EmptyStringValidator_EmptyError;
	public static String FileUtils_DifferentFileTypeWarning;
	public static String FileUtils_FileTooLargeError;
	public static String FileUtils_ImpossibleToCreateTempDirectory;
	public static String FileUtils_UnableToCreateDirectory;
	public static String FileUtils_UnableToReadFile;
	public static String IDStringValidator_EmptyError;
	public static String IDStringValidator_InvalidChars;
	public static String JasperReportCompiler_ErrorInitializationReportCompiler;
	public static String JRClasspathContainerPage_Description;
	public static String JRClasspathContainerPage_InfoText;
	public static String JRClasspathContainerPage_Title;
	public static String JRProjectPage_Description;
	public static String JRProjectPage_ErrorExistingProject;
	public static String JRProjectPage_LblName;
	public static String JRProjectPage_Title;
	public static String ResourceManager_WrongCornerDecoration;
	public static String ResourcePreferences_ErrorLoadingPreferenceFile;
	public static String ResourcePreferences_ErrPreferenceFileNotExist;
	public static String ResourcePreferences_LoadingPreferencesFile;
	public static String SWTResourceManager_WrongCornerDecoration;
	public static String ToggleNatureAction_JobName;
	public static String UIUtils_AnswerNo;
	public static String UIUtils_AnswerYes;
	public static String UIUtils_DeleteConfirmation;
	public static String UIUtils_ExceptionDetailsMsg;
	public static String UIUtils_ExceptionTitle;
	public static String UIUtils_InformationTitle;
	public static String UIUtils_ResourceDeleteConfirmationMsg;
	public static String UIUtils_Warning;
	public static String JRProjectWizard_OverwriteQuestionMsg;
	public static String JRProjectWizard_OverwriteQuestionTitle;
	public static String JRProjectWizard_title;
	public static String NotEmptyFileValidator_filenotexists;
	public static String ZoomActualSizeAction_actionName;
	public static String ZoomActualSizeAction_actionTooltip;
	public static String ZoomFitPageAction_actionName;
	public static String ZoomFitPageAction_actionTooltip;
	public static String ZoomFitPageWidthAction_actionName;
	public static String ZoomFitPageWidthAction_actionTooltip;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
