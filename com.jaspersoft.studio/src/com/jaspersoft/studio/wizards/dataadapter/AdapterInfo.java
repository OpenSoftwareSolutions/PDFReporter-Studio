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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.MessageFormat;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

/**
 * Container class to store the information necessary to the creation 
 * of a new data adapter plugin project
 * 
 * @author Orlandin Marco
 *
 */
public class AdapterInfo {
	
	/**
	 * suffix to append to the name of the classes files
	 */
	private static final String JAVA_SUFFIX = ".java";
	
	/**
	 * Suffix for the data adapter factory files
	 */
	private static final String FACTORY_SUFFIX = "Factory";
	
	/**
	 * Suffix for the data adapter descriptor files
	 */
	private static final String DESCRIPTOR_SUFFIX = "Descriptor";
	
	/**
	 * Suffix for the data adapter fields provider files
	 */
	private static final String FIELDS_PROVIDER_SUFFIX = "FieldsProvider";
	
	/**
	 * Suffix for the data adapter editor files
	 */
	private static final String EDITOR_SUFFIX = "Editor";
	
	/**
	 * Suffix for the data adapter editor inside the new report wizard files
	 */
	private static final String WIZARD_EDITOR_COMPOSITE_SUFFIX = "WizardEditorComposite";
	
	/**
	 * Suffix for the data adapter swt controls files
	 */
	private static final String COMPOSITE_SUFFIX = "Composite";
	
	/**
	 * Expression to retrieve an image from the images folder inside the plugin, by giving the name
	 * of the image
	 */
	private static final String IMAGE_EXPRESSION = "Activator.getDefault().getImage(\"images/{0}\")";
	
	/**
	 * The project name
	 */
	private String projectName;
	
	/**
	 * The name of the data adapter
	 */
	private String adapterName;
	
	/**
	 * The description of the data adapter
	 */
	private String adapterDescription;

	/**
	 * The main package of the data adapter
	 */
	private String packageName;
	
	/**
	 * The name of the icon file with the extension of the data
	 * adapter
	 */
	private String iconName;
	
	/**
	 * The image data of the data adapter
	 */
	private ImageData iconData;
	
	/**
	 * The prefix string for the classes for the current data adapter
	 */
	private String classNamePrefix;
	
	/**
	 * The plugin id
	 */
	private String pluginId;
	
	/**
	 * Create this data adapter informations container
	 * 
	 * @param projectName The plugin project name
	 * @param adapterName The name of the data adapter
	 * @param adapterDescription The description of the data adapter
	 * @param packageName The main package of the data adapter
	 * @param iconPath The path of the icon file of the data adapter. If the icon dosen't exist
	 * of if the name is null then a default icon is used
	 */
	public AdapterInfo(String projectName, String adapterName, String adapterDescription, String packageName, String iconPath){
		this.projectName = projectName;
		this.adapterDescription = adapterDescription;
		this.adapterName = adapterName;
		this.packageName = packageName;
		loadImage(iconPath);
		classNamePrefix = toCamelCase(adapterName).replaceAll(" ", "");
		pluginId = packageName + "." +  classNamePrefix;
	}
	
	/**
	 * Load icon image from its absolute path. If the path is 
	 * null or the image doesen't exist load a default image
	 * 
	 * @param fullPath absolute path of the image
	 */
	private void loadImage(String fullPath){
		if (fullPath != null){
			File imageFile = new File(fullPath);
			if (imageFile.exists()){
				try {
					FileInputStream stream = new FileInputStream(imageFile);
					Image loadedImage = new Image(null, stream);
					iconName = imageFile.getName();
					iconData = loadedImage.getImageData();
					loadedImage.dispose();
					return;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}	
			}
		}
		//The image was not provided, uses a default image
		iconName = "no_image.png";
		iconData = JaspersoftStudioPlugin.getInstance().getImage("icons/resources/image-missing.png").getImageData();
	}
	
	/**
	 * Return an expression to get an image from the resource manager by it's name
	 * 
	 * @param imageName the name of the image
	 * @return the slice of code to load that image from the expression manager
	 */
	public static String getIconImage(String imageName){
		return (MessageFormat.format(IMAGE_EXPRESSION, new Object[]{imageName}));
	}
	
	public String getFactoryClassName(){
		return classNamePrefix + FACTORY_SUFFIX;
	}
	
	public String getFactoryFileName(){
		return getFactoryClassName() + JAVA_SUFFIX;
	}
	
	public String getFactoryOnPluginName(){
		return packageName + "." + getFactoryClassName();
	}
	
	public String getDescriptorClassName(){
		return classNamePrefix + DESCRIPTOR_SUFFIX;
	}
	
	public String getDescriptorFileName(){
		return getDescriptorClassName() + JAVA_SUFFIX;
	}
	
	public String getFieldsProviderClassName(){
		return classNamePrefix + FIELDS_PROVIDER_SUFFIX;
	}
	
	public String getFieldsProviderFileName(){
		return getFieldsProviderClassName() + JAVA_SUFFIX;
	}
	
	public String getEditorClassName(){
		return classNamePrefix + EDITOR_SUFFIX;
	}
	
	public String getEditorFileName(){
		return getEditorClassName() + JAVA_SUFFIX;
	}
	
	public String getWizardEditorCompositeClassName(){
		return classNamePrefix + WIZARD_EDITOR_COMPOSITE_SUFFIX;
	}
	
	public String getWizardEditorCompositeFileName(){
		return getWizardEditorCompositeClassName() + JAVA_SUFFIX;
	}
	
	public String getCompositeClassName(){
		return classNamePrefix + COMPOSITE_SUFFIX;
	}
	
	public String getCompositeFileName(){
		return getCompositeClassName() + JAVA_SUFFIX;
	}
	
	public String getProjectName(){
		return projectName;
	}
	
	public String getAdapterName(){
		return adapterName;
	}
	
	public String getAdapterDescription(){
		return adapterDescription;
	}
	
	public String getPackageName(){
		return packageName;
	}
	
	public String getIconName(){
		return iconName;
	}
	
	public ImageData getIconData(){
		return iconData;
	}
	
	public String getPluginId(){
		return  pluginId;
	}
	
	public static String getActivatorClassName(){
		return "Activator.java";
	}
	
	public static String getPluginName(){
		return "plugin.xml";
	}
	
	/**
	 * Convert a string to a camel case 
	 * 
	 * @param s the input string
	 * @return the input string in camel case
	 */
	private  static String toCamelCase(String s){
    String[] parts = s.split(" ");
    String camelCaseString = "";
    for (String part : parts){
        if(part!=null && part.trim().length()>0)
       camelCaseString = camelCaseString + toProperCase(part);
        else
            camelCaseString=camelCaseString+part+" ";   
    }
    return camelCaseString;
 }

 private static String toProperCase(String s) {
     String temp=s.trim();
     String spaces="";
     if(temp.length()!=s.length())
     {
     int startCharIndex=s.charAt(temp.indexOf(0));
     spaces=s.substring(0,startCharIndex);
     }
     temp=temp.substring(0, 1).toUpperCase() +
     spaces+temp.substring(1).toLowerCase()+" ";
     return temp;

 }
}
