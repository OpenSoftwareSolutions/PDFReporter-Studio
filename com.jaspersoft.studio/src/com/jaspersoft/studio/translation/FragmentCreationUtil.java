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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import com.jaspersoft.studio.utils.VelocityUtils;
import com.jaspersoft.translation.resources.ITranslationResource;
import com.jaspersoft.translation.resources.TranslationInformation;

/**
 * Class that provides the methods to convert one or more extended translation informations
 * into a series of fragments that can be used with jaspersoft studio 
 * 
 * @author Orlandin Marco
 *
 */
public class FragmentCreationUtil {
	
	/**
	 * String used as file separator in the current os
	 */
	public final static String SEPARATOR =  System.getProperty("file.separator");
	
	/**
	 * Location of the templates to generate the manifest, build file.... of the fragments
	 */
	private static final String TEMPLATES_LOCATION_PREFIX = "com/jaspersoft/studio/translation/templates/";
	
	/**
	 * Location of the template to generate the manifest for the fragment
	 */
	private static final String MANIFEST_TEMPLATE = TEMPLATES_LOCATION_PREFIX + "manifestFile.vm";
	
	/**
	 * Location of the template to generate the build file for the fragment
	 */
	private static final String BUILD_TEMPLATE = TEMPLATES_LOCATION_PREFIX + "buildFile.vm";
	
	/**
	 * Location of the template to generate a single command for the fragment.xml of the rcp fragment
	 */
	private static final String FRAGMENT_COMMAND_TEMPLATE = TEMPLATES_LOCATION_PREFIX + "fragmentCommandFile.vm";
	
	/**
	 * Location of the template to generate the fragment.xml of the rcp fragment
	 */
	private static final String FRAGMENT_XML_FILE = TEMPLATES_LOCATION_PREFIX + "fragmentXmlFile.vm";
	
	/**
	 * Generate the manifest for a single ExtendedTranslationInformation and so for a fragment since
	 * to each ExtendedTranslationInformation correspond a fragment
	 * 
	 * @param pluginInfo The ExtendedTranslationInformation used to generate the fragment where this manifest goes
	 * @param isSingleton True if the fragment has a fragment.xml file, false otherwise
	 * @param a string that identify the language provided by the fragment, used in the bundle name
	 * @return all the text inside the manifest for the fragment
	 */
	public static String generateManifest(ExtendedTranslationInformation pluginInfo, String languagesCodes, boolean isSingleton)
	{
			VelocityEngine ve = VelocityUtils.getConfiguredVelocityEngine();
			
			VelocityContext functionContext = new VelocityContext();
			functionContext.put("hostPlugin", pluginInfo.getHostPluginName());
			functionContext.put("bundleName", pluginInfo.getBundleName()+languagesCodes);
			functionContext.put("qualifier", pluginInfo.getBundleVersion());
			functionContext.put("pluginVersion", pluginInfo.getHostPluginVersion());
			functionContext.put("vendor", pluginInfo.getBundleProducer());
			String singleton = "";
			if (isSingleton) singleton = ";singleton:=true";
			functionContext.put("singleton", singleton);
			
			Template functionTemplate = ve.getTemplate(MANIFEST_TEMPLATE);
			StringWriter fsw = new StringWriter();
			functionTemplate.merge( functionContext, fsw );
			return fsw.toString();
	}

	/**
	 * Generate the build file for a single TranslationInformation and so for a fragment since
	 * to each TranslationInformation correspond a fragment
	 * 
	 * @param pluginInfo The TranslationInformation used to generate the fragment where this build goes
	 * @param pluginDir folder where the build file will be placed
	 * @param rootFileNames list of folder and files that are in the root of the fragment and so need to be added to the build file
	 */
	public static void createBuildFile(TranslationInformation pluginInfo, File pluginDir, List<String> rootFileNames){	
		String buildInclusion = "";
		if (rootFileNames.size()>0){
			for(int i=0; i<rootFileNames.size(); i++){
				buildInclusion += ",\\\n"+rootFileNames.get(i);
			}
		}
		VelocityEngine ve = VelocityUtils.getConfiguredVelocityEngine();
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("rootFiles", buildInclusion);
		Template functionTemplate = ve.getTemplate(BUILD_TEMPLATE);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		try {
			FileUtils.writeFile(new File(pluginDir.getAbsolutePath() + SEPARATOR + "build.properties"), fsw.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the fragment.xml file to contribute to the language switch menu of the rcp plugin
	 * 
	 * @param destinationDir  destination of the fragment
	 * @param locales entries that will be contribute the switch language menu
	 */
	public static void createFragmentXml(File destinationDir, List<ImageLocale> locales){
		String commands = "";
		File iconDir = new File(destinationDir.getAbsolutePath() + SEPARATOR + "icons");
		iconDir.mkdirs();
		for(ImageLocale loc : locales){
			
			String imagePath = "";
			if (loc.getImage() != null){ 
				ImageLoader loader = new ImageLoader();
		    loader.data = new ImageData[] {loc.getImage()};
		    String imageName = loc.getLocale().toString()+".png";
		    loader.save(iconDir.getAbsolutePath() + SEPARATOR + imageName, SWT.IMAGE_PNG);
		    imagePath = "icons/"+imageName;
			}
	    
	    VelocityEngine ve = VelocityUtils.getConfiguredVelocityEngine();
			
			VelocityContext commandContext = new VelocityContext();
			commandContext.put("iconpath", imagePath);
			commandContext.put("languageParameter", loc.getLocale().toString());
			commandContext.put("languageName", loc.getLocale().getDisplayLanguage(loc.getLocale()));
			commandContext.put("languageCommandId", "com.jaspersoft.studio.switchlanguage.menus."+loc.getLocale().getDisplayLanguage(new Locale("en", "EN")));
			
			Template commandTemplate = ve.getTemplate(FRAGMENT_COMMAND_TEMPLATE);
			StringWriter fsw = new StringWriter();
			commandTemplate.merge( commandContext, fsw );
			commands += fsw.toString()+"\n";
		}
		
		// Create the fragment.xml file
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		
		VelocityContext fragmentContext = new VelocityContext();
		fragmentContext.put("commands", commands);
		Template fragmentTemplate = ve.getTemplate(FRAGMENT_XML_FILE);
		StringWriter fsw = new StringWriter();
		fragmentTemplate.merge( fragmentContext, fsw );
		try {
			FileUtils.writeFile(new File(destinationDir.getAbsolutePath() + SEPARATOR + "fragment.xml"), fsw.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Convert a series of locales into a string the can be used as identifier
	 * in the file name or in the bundle name
	 * 
	 * @param languagesProvided list of langage provided by the fragment
	 * @return identifier of the languages
	 */
	private static String getCodesFromLanguage(List<ImageLocale> languagesProvided){
		String languagesCodes = "";
		for(ImageLocale locale : languagesProvided){
			languagesCodes += "_"+locale.getLocale().toString();
		}
		return languagesCodes;
	}
	
	/**
	 * Even if the RCP plugin is not translated it is generated a fragment for it to contribute 
	 * the language switch menu
	 * 
	 * @param destinationPath destination of the fragment
	 * @param languagesProvided entries that will be contribute the switch language menu
	 */
	private static void forceCreateFragmentRcp(String destinationPath, List<ImageLocale> languagesProvided){
		String tmpDirectory = System.getProperty("java.io.tmpdir"); 
		List<String> rootFileNames = new ArrayList<String>();
		String rcpPluginName = "com.jaspersoft.studio.rcp";
		File pluginDir = new File(tmpDirectory + FragmentCreationUtil.SEPARATOR + rcpPluginName);
		if (pluginDir.exists()) JarFileUtils.delete(pluginDir);
		pluginDir.mkdirs();
		FragmentCreationUtil.createFragmentXml(pluginDir, languagesProvided);
		rootFileNames.add("fragment.xml");
		rootFileNames.add("icons/");
		
		TranslationInformation baseInfo = new TranslationInformation(rcpPluginName);
		String version = FragmentCreationUtil.generateQualifier();
		ExtendedTranslationInformation rcpPlugin = CreateTranslationFragmentCommand.generateExtendedInfo(baseInfo,version,"JSSBuilder");
		
		String languageProvidedIds = getCodesFromLanguage(languagesProvided);
		String jarName = rcpPlugin.getBundleName() + languageProvidedIds + "_" + rcpPlugin.getBundleVersion() + ".jar";
		FragmentCreationUtil.createBuildFile(rcpPlugin, pluginDir, rootFileNames);
		String manifest = FragmentCreationUtil.generateManifest(rcpPlugin, languageProvidedIds, true);
		JarFileUtils.createJar(destinationPath, pluginDir, jarName, manifest);
	}
	
	/**
	 * Generate the qualifier for a fragment using the eclipse style. 
	 * So a concatenation of the actuals year,mont,day,hour and minute
	 * 
	 * @return an eclipse style qualifier
	 */
	public static String generateQualifier(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * Split a package name into a path structure, so change every dot
	 * with the system file path separator symbol
	 * 
	 * @param name
	 * @return
	 */
	private static String getPathFromPackageName(String name){
		String[] names = name.split("\\.");
		String path = "";
		for(String packageName : names){
			path += packageName + SEPARATOR;
		}
		return path;
	}
	
	/**
	 * Create the packages structure on the folder that will be compressed into a jar 
	 * to create the fragment
	 * 
	 * @param srcFolder folder where the package structure will be created
	 * @param plugin plugin informations from where the package structure and the path of the files inside the packages are read
	 */
	private static void createPackages(File srcFolder, TranslationInformation plugin){
		for(ITranslationResource resource : plugin.getResources()){
			if (!resource.isFile()){
				String packageFolderPath = srcFolder.getAbsolutePath() + FragmentCreationUtil.SEPARATOR + getPathFromPackageName(resource.getResourceName());
				File packageFolder = new File(packageFolderPath);
				packageFolder.mkdirs();
				for(ITranslationResource packageContent : resource.getChildren()){
					if (packageContent.isFile()){
						JarFileUtils.copyFile(new File(packageContent.getResourcePath()), packageFolder);
					}
				}
			} 
		}
	}
	

	/**
	 * Create the fragments for the TranslationInformation provided (one for each translation) that will add
	 * the translation to JSS and will provide entry for the new languages on the switch language menu
	 * 
	 * @param destinationPath folder where the fragment will be placed
	 * @param translations Translations of plugins that will be converted into fragments for JSS
	 * @param languagesProvided entries that will be contribute the switch language menu
	 */
	public static void createFragment(String destinationPath, List<ExtendedTranslationInformation> translations, List<ImageLocale> languagesProvided)
	{
		boolean rcpPluginFound = false;
		String tmpDirectory = System.getProperty("java.io.tmpdir"); 
		String languageProvidedIds = getCodesFromLanguage(languagesProvided);
		for(ExtendedTranslationInformation plugin : translations){
			List<String> rootFileNames = new ArrayList<String>();
			boolean hasPackage = false;
			File pluginDir = new File(tmpDirectory + FragmentCreationUtil.SEPARATOR + plugin.getPluginName());
			if (pluginDir.exists()) JarFileUtils.delete(pluginDir);
			pluginDir.mkdirs();
			for(ITranslationResource resource : plugin.getResources()){
				if (resource.isFile()){
					rootFileNames.add(resource.getResourceName());
					JarFileUtils.copyFile(new File(resource.getResourcePath()), pluginDir);
				} else hasPackage = true;
			}
			
			if (hasPackage){
				createPackages(pluginDir, plugin);
			}
			
			String manifest = "";
			if (plugin.getHostPluginName().equals("com.jaspersoft.studio.rcp")){
				rcpPluginFound = true;
				FragmentCreationUtil.createFragmentXml(pluginDir, languagesProvided);
				rootFileNames.add("fragment.xml");
				rootFileNames.add("icons/");
				manifest = FragmentCreationUtil.generateManifest(plugin, languageProvidedIds, true);
			} else {
				manifest = FragmentCreationUtil.generateManifest(plugin, languageProvidedIds, false);
			}
			
			String jarName = plugin.getBundleName() + languageProvidedIds + "_" + plugin.getBundleVersion() + ".jar";
			FragmentCreationUtil.createBuildFile(plugin, pluginDir, rootFileNames);
			JarFileUtils.createJar(destinationPath, pluginDir, jarName, manifest);
		}
		
		if (!rcpPluginFound){
			forceCreateFragmentRcp(destinationPath, languagesProvided);
		}
	}
	
}
