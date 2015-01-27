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
package com.jaspersoft.studio.wizards.dataadapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.utils.ImageUtils;
import com.jaspersoft.studio.utils.VelocityUtils;

/**
 * Wizard to create the data adapter plugin project
 * 
 * @author Orlandin Marco
 *
 */
public class NewDataAdapterWizard extends Wizard implements INewWizard {

	/**
	 * Folder where the velocity templates are placed
	 */
	private static final String TEMPLATES_LOCATION_PREFIX = "com/jaspersoft/studio/wizards/dataadapter/resources/";
	
	/**
	 * Velocity template for the activator of the plugin
	 */
	private static final String ACTIVATOR_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "Activator.vm";
	
	/**
	 * Velocity template for the data adapter factory
	 */
	private static final String FACTORY_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "Factory.vm";
	
	/**
	 * Velocity template for the data adapter descriptor
	 */
	private static final String DESCRIPTOR_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "Descriptor.vm";
	
	/**
	 * Velocity template for the data adapter editor
	 */
	private static final String EDITOR_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "Editor.vm";
	
	/**
	 * Velocity template for the controls inside the editor
	 */
	private static final String COMPOSITE_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "Composite.vm";
	
	/**
	 * Velocity template for the adapter fields provider
	 */
	private static final String PROVIDER_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "FieldsProvider.vm";
	
	/**
	 * Velocity template for the adapter wizard editor
	 */
	private static final String WIZARD_EDITOR_CLASS_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "WizardEditor.vm";
	
	/**
	 * Velocity template for the plugin.xml file
	 */
	private static final String PLUGIN_TEMPLATE_LOCATION = TEMPLATES_LOCATION_PREFIX + "Plugin.vm";
	
	/**
	 * Location of the dummy data adapter library
	 */
	private static final String LIB_LOCATION = "/resources/lib/DummyDataAdapter.jar"; 
	
	/**
	 * Type of the dummy data adapter
	 */
	private static final String DATA_ADAPTER_TYPE = "DummyDataAdapter";
	
	/**
	 * Type of the implementations of the dummy data adapter
	 */
	private static final String DATA_ADAPTER_IMPL_TYPE = DATA_ADAPTER_TYPE + "Impl";
	
	/**
	 * Type of the dummy data adapter service
	 */
	private static final String DATA_ADAPTER_SERVICE_TYPE = "DummyDataAdapterService";
	
	/**
	 * Package to add to the templates to use the dummy data adapter
	 */
	private static final String DATA_ADAPTER_PACKAGE = "com.jaspersoft.adapter.*";
	
	/**
	 * Page to provide informations to the new plugin project
	 */
	private DataAdapterInformationPage page1;

	/**
	 * The velocity engine
	 */
	private VelocityEngine ve = VelocityUtils.getConfiguredVelocityEngine();;
	
	/**
	 * Wizard constructor.
	 */
	public NewDataAdapterWizard() {
		super();
		setWindowTitle("New Data Adapter Wizard");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page1 = new DataAdapterInformationPage();
		page1.setWizard(this);
		addPage(page1);
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the Activator class
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a .java file
	 */
	private String getActivatorClass(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", adapterInfo.getPackageName());
		functionContext.put("pluginId", adapterInfo.getPluginId());

		Template functionTemplate = ve.getTemplate(ACTIVATOR_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		
		return fsw.toString();
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the data adapter factory class
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a .java file
	 */
	private String getFactoryClass(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", adapterInfo.getPackageName());
		functionContext.put("factoryName", adapterInfo.getFactoryClassName());
		functionContext.put("descriptorName", adapterInfo.getDescriptorClassName());
		functionContext.put("dataAdapterFamiliarName", adapterInfo.getAdapterName());
		functionContext.put("dataAdapterDescription", adapterInfo.getAdapterDescription());
		functionContext.put("dataAdapterType", DATA_ADAPTER_TYPE);
		functionContext.put("dataAdapterImplType", DATA_ADAPTER_IMPL_TYPE);
		functionContext.put("dataAdapterService", DATA_ADAPTER_SERVICE_TYPE); 
		functionContext.put("dataAdapterPackage", DATA_ADAPTER_PACKAGE);
		functionContext.put("image", AdapterInfo.getIconImage(adapterInfo.getIconName()));

		Template functionTemplate = ve.getTemplate(FACTORY_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		return fsw.toString();
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the data adapter descriptor class
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a .java file
	 */
	private String getDescriptorClass(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", adapterInfo.getPackageName());
		functionContext.put("descriptorName", adapterInfo.getDescriptorClassName());
		functionContext.put("dataAdapterEditor", adapterInfo.getEditorClassName());
		functionContext.put("dataAdapterFieldsProvider", adapterInfo.getFieldsProviderClassName());
		functionContext.put("dataAdapterWizardEditorComposite", adapterInfo.getWizardEditorCompositeClassName());
		functionContext.put("dataAdapterPackage", DATA_ADAPTER_PACKAGE);
		functionContext.put("dataAdapterType", DATA_ADAPTER_TYPE);
		functionContext.put("dataAdapterImplType", DATA_ADAPTER_IMPL_TYPE);
		functionContext.put("image", AdapterInfo.getIconImage(adapterInfo.getIconName()));
		
		Template functionTemplate = ve.getTemplate(DESCRIPTOR_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
	
		return fsw.toString();
		
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the data adapter editor class
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a .java file
	 */
	private String getEditorClass(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", adapterInfo.getPackageName());
		functionContext.put("dataAdapterEditor", adapterInfo.getEditorClassName());
		functionContext.put("adapterComposite", adapterInfo.getCompositeClassName());
		functionContext.put("descriptorName", adapterInfo.getDescriptorClassName());

		Template functionTemplate = ve.getTemplate(EDITOR_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		
		return fsw.toString();
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the data adapter composite class
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a .java file
	 */
	private String getCompositeClass(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", adapterInfo.getPackageName());
		functionContext.put("adapterComposite", adapterInfo.getCompositeClassName());
		functionContext.put("descriptorName", adapterInfo.getDescriptorClassName());
		functionContext.put("dataAdapterType", DATA_ADAPTER_TYPE);
		functionContext.put("dataAdapterPackage", DATA_ADAPTER_PACKAGE);
		Template functionTemplate = ve.getTemplate(COMPOSITE_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		
		return fsw.toString();
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the data adapter fields provider class
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a .java file
	 */
	private String getFieldsProviderClass(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", adapterInfo.getPackageName());
		functionContext.put("dataAdapterFieldsProvider", adapterInfo.getFieldsProviderClassName());

		Template functionTemplate = ve.getTemplate(PROVIDER_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		
		return fsw.toString();
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the data adapter wizard editor class
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a .java file
	 */
	private String getWizardEditorClass(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("packageName", adapterInfo.getPackageName());
		functionContext.put("dataAdapterWizardEditorComposite", adapterInfo.getWizardEditorCompositeClassName());
		functionContext.put("image", AdapterInfo.getIconImage(adapterInfo.getIconName()));
		
		Template functionTemplate = ve.getTemplate(WIZARD_EDITOR_CLASS_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		
		return fsw.toString();
	}
	
	/**
	 * Uses the velocity engine and the template to generate the content
	 * of the plugin file
	 * 
	 * @param adapterInfo information to put the parameter inside the template
	 * @return a textual representation of a plugin.xml file
	 */
	private String getPlugin(AdapterInfo adapterInfo){
		VelocityContext functionContext = new VelocityContext();
		functionContext.put("adapterFactory", adapterInfo.getFactoryOnPluginName());

		Template functionTemplate = ve.getTemplate(PLUGIN_TEMPLATE_LOCATION);
		StringWriter fsw = new StringWriter();
		functionTemplate.merge( functionContext, fsw );
		
		return fsw.toString();
	}
	
	/**
	 * Add the adapter icon image to the project if it is available. It 
	 * also resized to a size less or equal to 16x16
	 * 
	 * @param adapterInfo The pluigin information 
	 * @param project the project
	 * @param monitor a monitor to addd the file
	 */
	private void saveImageIntoProject(AdapterInfo adapterInfo, IProject project, IProgressMonitor monitor){
		if (adapterInfo.getIconData() != null && adapterInfo.getIconName() != null){
			ImageData imgData = adapterInfo.getIconData();
			Image loadedImage = null;
			//Resize the image if it is too big
			int width = imgData.width;
			int height = imgData.height;
			if (width > 16){
				int scaleFactor = width/16;
				width = width/scaleFactor;
				height = height / scaleFactor;
			}
			if (height > 16){
				int scaleFactor = height/16;
				width = width/scaleFactor;
				height = height / scaleFactor;
			}
			if (width != imgData.width || height != imgData.height){
				Image biggerImage = new Image(null, imgData);
				loadedImage = ImageUtils.resize(biggerImage, width, height);
				biggerImage.dispose();
			} else {
				loadedImage = new Image(null, imgData);
			}
			
			String tempImagePath = System.getProperty("java.io.tmpdir") + adapterInfo.getIconName();
			File tempImage = new File(tempImagePath);
			if (tempImage.exists()){
				tempImage.delete();
			}
			
			//Save the new image
			ImageLoader loader = new ImageLoader();
		  loader.data = new ImageData[] {loadedImage.getImageData()};
		  loader.save(tempImagePath, getImageFormatFromExtension( FilenameUtils.getExtension(tempImagePath)));
			try {
				InputStream pin = new BufferedInputStream(new FileInputStream(tempImagePath));
				PluginHelper.createFile(adapterInfo.getIconName(), project.getFolder("images"), pin, monitor);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			loadedImage.dispose();
		}
	}
	
	/**
	 * Return the SWT image type of an image using it's extension 
	 * 
	 * @param extension textual extension of the image without the .
	 * @return the swt type of the extension if it is recognized, otherwise
	 * SWT.IMAGE_JPEG
	 */
	private int getImageFormatFromExtension(String extension){
		if (extension.equals("jpeg") || extension.equals("jpg")) return SWT.IMAGE_JPEG;
		if (extension.equals("bmp")) return SWT.IMAGE_BMP;
		if (extension.equals("gif")) return SWT.IMAGE_GIF;
		if (extension.equals("png")) return SWT.IMAGE_PNG;
		if (extension.equals("ico")) return SWT.IMAGE_ICO;
		return SWT.IMAGE_JPEG;
	}
	
	/**
	 * Get a folder where to put a .java file starting from the standard src folder.
	 * The subfolders are determinated by the package of the file and if not found
	 * are created
	 * 
	 * @param packageName the package of the file
	 * @param project the project
	 * @return the folder where the java file can be placed
	 */
	private IFolder getSourceFolder(String packageName, IProject project){
		IFolder src = project.getFolder("src");
		try {
			if (!src.exists()){
					src.create(true, true, new NullProgressMonitor());
			}
			String[] packageFragments = packageName.split("\\.");
			for(String packageFragment : packageFragments){
				src = src.getFolder(packageFragment);
				if (!src.exists()){
					src.create(true, true, new NullProgressMonitor());
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return src;
	}
	
	@Override
	public boolean performFinish() {
		NullProgressMonitor monitor = new NullProgressMonitor();
		monitor.setTaskName("Crating project");
		try {
			System.out.println("start");
			ArrayList<String> src = new ArrayList<String>();
			src.add("src");
			ArrayList<String> requiredBundles = new ArrayList<String>();
			requiredBundles.add("org.eclipse.ui");
			requiredBundles.add("org.eclipse.core.runtime");
			requiredBundles.add("com.jaspersoft.studio.data");
			requiredBundles.add("com.jaspersoft.studio.components");
			AdapterInfo info = page1.getAdapterInfo();
			List<File> libFiles = new ArrayList<File>();
			libFiles.add(new File(JaspersoftStudioPlugin.getInstance().getFileLocation(LIB_LOCATION)));
			IProject pluginProject = PluginHelper.createPluginProject(info, src, requiredBundles, libFiles, monitor);
			if (pluginProject != null){
				PluginHelper.createFile(AdapterInfo.getActivatorClassName(), getSourceFolder(info.getPackageName(),pluginProject), getActivatorClass(info), monitor);
				PluginHelper.createFile(info.getFactoryFileName(), getSourceFolder(info.getPackageName(),pluginProject),  getFactoryClass(info), monitor);
				PluginHelper.createFile(info.getDescriptorFileName(), getSourceFolder(info.getPackageName(),pluginProject),  getDescriptorClass(info), monitor);
				PluginHelper.createFile(info.getEditorFileName(), getSourceFolder(info.getPackageName(),pluginProject), getEditorClass(info), monitor);
				PluginHelper.createFile(info.getCompositeFileName(), getSourceFolder(info.getPackageName(),pluginProject), getCompositeClass(info), monitor);
				PluginHelper.createFile(info.getFieldsProviderFileName(), getSourceFolder(info.getPackageName(),pluginProject), getFieldsProviderClass(info), monitor);
				PluginHelper.createFile(info.getWizardEditorCompositeFileName(), getSourceFolder(info.getPackageName(),pluginProject), getWizardEditorClass(info), monitor);
				PluginHelper.createFile(AdapterInfo.getPluginName(), pluginProject, getPlugin(info), monitor);
				saveImageIntoProject(info, pluginProject, monitor);
			}
		} catch (Exception e) {
			UIUtils.showError(e);
		}
		monitor.done();
		return true;
	}

}
