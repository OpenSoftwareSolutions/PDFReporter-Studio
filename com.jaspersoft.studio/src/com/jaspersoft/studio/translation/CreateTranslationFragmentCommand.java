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
package com.jaspersoft.studio.translation;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardDialog;
import org.osgi.framework.Bundle;

import com.jaspersoft.studio.translation.wizard.GenerateFragmentWizard;
import com.jaspersoft.translation.action.ProvideSelectedTranslation;
import com.jaspersoft.translation.resources.TranslationInformation;

/**
 * Command used to open the wizard to export a translation project as 
 * one or more fragments that can be used from jaspersoft studio
 * 
 * @author Orlandin Marco
 *
 */
public class CreateTranslationFragmentCommand implements IHandler {
	
	/**
	 * If for some plugin it is not possible to recover the actual version, maybe because
	 * the user is generated the fragment also for a pro but using a community version, then
	 * is used as base version the one of the main plugin, since it is always available and the
	 * version of the other plugins are aligned
	 */
	private static String baseVersion = Platform.getBundle("com.jaspersoft.studio").getHeaders().get("Bundle-Version").toString();
	
	/**
	 * Generate an extended translation information starting from a standard 
	 * Translation information, a version and a vender name. The extended translation
	 * provide some additional informations like the host plugin for the translation, 
	 * its minimum version and so on.
	 * 
	 * @param baseInfo TranslationInformation used as base
	 * @param qualifiedVersion version of the plugin\fragment that will be generated from the extended translation
	 * @param vendorName producer of the plugin\fragment that will be generated from the extended version
	 * @return
	 */
	public static ExtendedTranslationInformation generateExtendedInfo(TranslationInformation baseInfo, String qualifiedVersion, String vendorName){
		ExtendedTranslationInformation extendedInfo = new ExtendedTranslationInformation(baseInfo);
		String qualifiedName = baseInfo.getPluginName()+"_translation";
		Bundle bundle = Platform.getBundle(baseInfo.getPluginName()); 
		String version = "";
		if (bundle != null) version = bundle.getHeaders().get("Bundle-Version").toString();
		else version = new String(baseVersion);
		version = version.replaceAll("\\.qualifier", "");
		extendedInfo.setBundleVersion(qualifiedVersion);
		extendedInfo.setBundleProducer(vendorName);
		extendedInfo.setBundleName(qualifiedName);
		extendedInfo.setHostPluginName(baseInfo.getPluginName());
		extendedInfo.setHostPluginVersion(version);
		return extendedInfo;
	}
	
	/**
	 * Convert a list of TranslateInormation into a list of the same size of ExtendedTranslationInformation.
	 * The version for the extended translation is calculated with the actual date\time and the vendor name
	 * is the name of the actual user
	 * 
	 * @param baseInfos a not list of TranslateInormation, that will be converted into extended information
	 * @return a not null list of ExtendedTranslateInormation, with the same size of the input list
	 */
	private List<ExtendedTranslationInformation> generateExtendedInfos(List<TranslationInformation> baseInfos){
		String qualifiedVersion = FragmentCreationUtil.generateQualifier();
		String vendorName = System.getProperty("user.name"); 
		List<ExtendedTranslationInformation> result = new ArrayList<ExtendedTranslationInformation>();
		for(TranslationInformation baseInfo : baseInfos){
			result.add(generateExtendedInfo(baseInfo, qualifiedVersion, vendorName));
		}
		return result;
	}

	/**
	 * Open the wizard to create and export the translation fragments
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProvideSelectedTranslation translationCommand = new ProvideSelectedTranslation();
		List<TranslationInformation> translations = translationCommand.execute();
		List<ExtendedTranslationInformation> extendedInfo = generateExtendedInfos(translations);
		
		GenerateFragmentWizard wizard = new GenerateFragmentWizard(extendedInfo);
		WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
		dialog.create();
		UIUtils.resizeAndCenterShell(dialog.getShell(), 600,  600);
		dialog.open();
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {}
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}
	
}
