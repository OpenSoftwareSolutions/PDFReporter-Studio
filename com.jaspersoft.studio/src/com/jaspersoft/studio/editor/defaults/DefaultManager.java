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
package com.jaspersoft.studio.editor.defaults;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Default manager that keep loaded the actual default template set (if present) 
 * and offer the methods to load another template set or apply it to the new
 * elements. Use the static INSTANCE method to access to the default manager
 * 
 * @author Orlandin Marco
 *
 */
public class DefaultManager {

	/**
	 * The Default Manager is global and it is accessible only trough this class
	 */
	public static final DefaultManager INSTANCE = new DefaultManager();
	
	/**
	 * The properties file
	 */
	private Preferences prefs = PropertiesHelper.INSTANCE_SCOPE.getNode(JaspersoftStudioPlugin.getUniqueIdentifier());
	
	/**
	 * Key used in the application properties file to store the list of template set and the actual selected one
	 */
	private static final String DEFAULT_KEY = "defaultValues";
	
	/**
	 * List of the available template sets. Each item is the absolute path to the template set file
	 */
	private List<String> availableDefaults;
	
	/**
	 * The path to the selected template set file, it's null if no file is selected
	 */
	private String actualDefault = null;
	
	/**
	 * The root of the model of the selected template set file, it's null if no file is selected
	 */
	private INode defaultReport = null;
	
	/**
	 * The jasperconfiguration of the selected template set file, it's null if no file is selected
	 */
	private JasperReportsConfiguration defaultConfig = null;
	
	/**
	 * Map to keep cached each default model in the selected template with it's type. In this 
	 * way it's easy to find if there is a template for a specific type and provide an object
	 * that offers the method to apply the model to the new element
	 */
	private HashMap<Class<?>, MGraphicElement> selectedDefaultsMap;
	
	/**
	 * Resource listener used to see when a resource is deleted and update the template 
	 * list if the resource was a template set
	 */
	IResourceChangeListener resourceDeletedListener = new IResourceChangeListener() {
		
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			List<IFile> resourcesDeleted = new ArrayList<IFile>();
			if (event.getType() == IResourceChangeEvent.POST_CHANGE){
				iterateResourceDelta(event.getDelta(), resourcesDeleted);
				for(IFile resource : resourcesDeleted){
					String resourceString = resource.getRawLocation().toOSString();
					availableDefaults.remove(resourceString);
					if (resourceString.equals(actualDefault)){
						actualDefault = null;
						defaultReport = null;
						defaultConfig = null;
					}
				}
				savePreferences();
			}
		}
	};
	
	/**
	 * Constructor, since it's private the class can be only accessed by the INSTANCE method
	 */
	private DefaultManager(){
		initializeDefaultManager();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceDeletedListener);
	};
	
	/**
	 * Save inside the application properties file the list of all the defined template sets
	 * as an xml string
	 */
	private void savePreferences(){
		String xmlData = getXMLData();
		prefs.put(DEFAULT_KEY, xmlData);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the xml string with the definition of all the current template sets
	 * 
	 * @return xml definition
	 */
	private String getXMLData() {
		String result = "<defaultReports>";
		for(String defaultReport : availableDefaults){
			result+="<report path=\""+ defaultReport + "\" default=\"";
			result+=defaultReport.equals(actualDefault) + "\"/>";
		}
		result += "</defaultReports>";
		return result;
	}
	
	/**
	 * Load the list of available template sets and if it is defined load the model
	 * of the selected template set
	 */
	private void initializeDefaultManager(){
		loadPreferences();
		if (actualDefault != null){
				loadDefaultModel();
		}
	}
	
	/**
	 * Load the list of the template set from the preferences file. If one of them has the default 
	 * attribute set to true then it will be the selected default. If more of them has the attribute
	 * to true only the last one will be the default. A template set is added to the available ones
	 * only if it's file exist
	 */
	private void loadPreferences(){
		String defaults = prefs.get(DEFAULT_KEY, "");
		availableDefaults = new ArrayList<String>();
		if (!defaults.isEmpty()){
			try {
				Document document = JRXmlUtils.parse(new InputSource(new StringReader(defaults)));
				NodeList adapterNodes = document.getDocumentElement().getChildNodes();
				for (int i = 0; i < adapterNodes.getLength(); ++i) {
					Node adapterNode = adapterNodes.item(i);
					if (adapterNode.getNodeType() == Node.ELEMENT_NODE) {
						String path = adapterNode.getAttributes().getNamedItem("path").getNodeValue(); //$NON-NLS-1$
						String defaultReport = adapterNode.getAttributes().getNamedItem("default").getNodeValue(); //$NON-NLS-1$
						//Check file existence
						if (new File(path).exists()){
							availableDefaults.add(path);
							if (Boolean.parseBoolean(defaultReport)) actualDefault = path;
						}
					}
				}
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load the current default template set, creating it's model structure
	 */
	private void loadDefaultModel() {
		InputStream in = null;
		selectedDefaultsMap = new HashMap<Class<?>, MGraphicElement>();
		defaultReport = null;
		try {
			File defaultFile = new File(actualDefault);
			if (defaultFile.exists()){
				in = new ByteArrayInputStream(FileUtils.readFileToByteArray(defaultFile));
				JasperReportsConfiguration jConfig = getDefaultJRConfig();
				JasperDesign jd = new JRXmlLoader(jConfig, JasperReportsConfiguration.getJRXMLDigester()).loadXML(in);
				jConfig.setJasperDesign(jd);
				defaultReport = ReportFactory.createReport(jConfig);
				defaultReport.getChildren().get(0).setValue(jd);
				defaultConfig = jConfig;
				setElementsType();
			}
		} catch (Exception e) {
			actualDefault = null;
			defaultReport = null;
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {}
		}
	}
	
	/**
	 * Recursive method called when some resource changes, it search for deleted resources inside
	 * the delta hierarchy
	 * 
	 * @param delta actual level of the delta hierarchy
	 * @param deleteResources the list of deleted resources actually found
	 */
	private void iterateResourceDelta(IResourceDelta delta, List<IFile> deleteResources){
		if (delta.getKind() == IResourceDelta.REMOVED && delta.getResource() instanceof IFile){
			deleteResources.add((IFile)delta.getResource());
		}
		for(IResourceDelta affectedResource : delta.getAffectedChildren()){
			iterateResourceDelta(affectedResource, deleteResources);
		}
	}
	
	/**
	 * Return a JasperReports configuration for the selected Template Set 
	 * 
	 * @param the file to the JRXML of the template set
	 * @return a not null jasperreports configuration
	 */
	private JasperReportsConfiguration getDefaultJRConfig() {
		return new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), null);
	}
	
	/**
	 * Inspect the loaded model to search the detail band and all it's content to the type cache map
	 */
	private void setElementsType(){
		//Search for the detail band
		if (defaultReport == null || defaultReport.getChildren().isEmpty() || !(defaultReport instanceof MRoot)) return; 
		for(INode node : defaultReport.getChildren().get(0).getChildren()){
			if (node instanceof MBand){
				MBand band = (MBand)node;
				if (band.getBandType() == BandTypeEnum.DETAIL){
					addAll(band);
					break;
				}
			}
		}
	}
	
	/**
	 * Recursive method to add all nodes inside the current parent to the the type cache map.
	 * The key is the node type and the value is the node itself
	 * 
	 * @param parent the current parent
	 */
	private void addAll(INode parent){
		for(INode node : parent.getChildren()){
			if (node instanceof MGraphicElement){
				selectedDefaultsMap.put(node.getClass(), (MGraphicElement)node);
			}
			addAll(node);
		}
	}
	
	/**
	 * Return if there is a default selected
	 * 
	 * @return true if there is a default selected, false otherwise
	 */
	public boolean hasDefault(){
		return actualDefault != null && defaultReport != null;
	}
	
	/**
	 * Apply a template of a specific type to an element. The element and 
	 * the class type must have a compatible type. If there is not a defined
	 * template for the requested type then it dosen't do anything
	 * 
	 * @param modelType the type of the request template model
	 * @param element the element to which the template will be applied. Typically the
	 * model type is the ANode that contains (or will contain) the element 
	 */
	public void applyDefault(Class<?> modelType, JRElement element){
		if (DefaultManager.INSTANCE.hasDefault()){
			MGraphicElement defaultSetter = selectedDefaultsMap.get(modelType);
			if (defaultSetter != null){
				defaultSetter.trasnferProperties(element);
			}
		}
	}
	
	/**
	 * Get the value of a property of the template of a specified type. If the
	 * type is not available, or the property is not found or there isn't a 
	 * template set selected the it return null
	 * 
	 * @param modelType the type of the template
	 * @param propertyId the name of the property
	 * @return the value of the property
	 */
	public Object getDefaultPropertiesValue(Class<?> modelType, String propertyId){
		if (hasDefault()){
			MGraphicElement container = selectedDefaultsMap.get(modelType);
			if (container != null){
				return container.getPropertyValue(propertyId);
			}
		}
		return null;
	}
	
	/**
	 * Set the current default template set. It will be loaded 
	 * and the preferences will be saved if valid
	 * 
	 * @param path the absolute path to the template set file
	 */
	public void setDefaultFile(String path){
		String oldDefault = actualDefault;
		INode oldDefaultReport = defaultReport;
		JasperReportsConfiguration oldConfig = defaultConfig;
		HashMap<Class<?>, MGraphicElement> oldDefaultMap = selectedDefaultsMap;
		actualDefault = path;
		try {
			loadDefaultModel();
			savePreferences();
		} catch (Exception e) {
			actualDefault = oldDefault;
			defaultReport = oldDefaultReport;
			defaultConfig = oldConfig;
			selectedDefaultsMap = oldDefaultMap;
			e.printStackTrace();
		}
	}
	
	/**
	 * Unset the any selected template set. It will be loaded and
	 * the preference saved
	 */
	public void unsetDefaultFile(){
		actualDefault = null;
		defaultReport = null;
		defaultConfig = null;
		savePreferences();
	}
	
	/**
	 * Check if a template set is the currently default one
	 * 
	 * @param path the absolute path to the template set file that you 
	 * want to check
	 */
	public boolean isCurrentDefault(String path){
		return path.equals(actualDefault);
	}
	
	/**
	 * Add a new template set
	 * 
	 * @param path the path to the template set file 
	 * @param isCurrentDefault true if the added template set should also be used as default, 
	 * false otherwise
	 */
	public void addDefaultFile(String path, boolean isCurrentDefault){
		boolean invalidDefault = false;
		availableDefaults.add(path);
		if (isCurrentDefault){
			actualDefault = path;
			try {
				loadDefaultModel();
			} catch (Exception e) {
				availableDefaults.remove(path);
				invalidDefault = true;
				actualDefault = null;
				defaultReport = null;
				defaultConfig = null;
				e.printStackTrace();
			}
		}
		if (!invalidDefault){
			savePreferences();
		}
	}
	
	/**
	 * Return a list of all the available template set. The list 
	 * is calculated only when it is requested
	 * 
	 * @return a list of string where each element is an absolute path to a 
	 * template set
	 */
	public List<String> getAvailableElements(){
		if (availableDefaults == null){
			initializeDefaultManager();
		} 
		return availableDefaults;
	}
	
	/**
	 * Recursive method to search a jr type inside the current parent and if it is
	 * found remove the element with that type and return its parent
	 * 
	 * @param element use the type of this element to search another element of the same type in the children of the current parent
	 * @param actualNode the current parent
	 * @return the parent of the removed node if found, null otherwise
	 */
	private INode searchAndRemoveOld(JRDesignElement element, INode actualNode){
		for(INode node : actualNode.getChildren()){
			if (node.getValue() != null && node.getValue().getClass().equals(element.getClass()) && actualNode.getValue() instanceof JRDesignElementGroup){
				//found must delete and return the parent
				Object parent = actualNode.getValue();
				((JRDesignElementGroup)parent).removeElement((JRDesignElement)node.getValue());
				return actualNode;
			} else {
				INode subResult = searchAndRemoveOld(element, node);
				if (subResult != null) return subResult;
			}
		}
		return null;
	}
	
	/**
	 * Return the detail band of the actually selected template set
	 * 
	 * @return the model of the detail band or null if it can't be found
	 */
	private INode searchDetailBand(){
		MReport mReport = (MReport)defaultReport.getChildren().get(0);
		for(INode child : mReport.getChildren()){
			if (child instanceof MBand && child.getValue() != null){
				MBand band = (MBand)child;
				if (band.getBandType() == BandTypeEnum.DETAIL) return band;
			}
		}
		return null;
	}
	
	/**
	 * Add an element to the current selected template set. The element is 
	 * added inside the detail band and if there was an element of the same 
	 * type of the added one the old one is removed
	 * 
	 * @param element element to add
	 */
	private void addNewElement(JRDesignElement element){
		INode parent = searchDetailBand();
		Assert.isNotNull(parent, "The Template Set report must have a detail band");
		if (parent != null){
			searchAndRemoveOld(element, parent);
			((JRDesignElementGroup)parent.getValue()).addElement(element);
		}
	}
	
	/**
	 * Add an element to the current selected template set. The element is 
	 * added inside the detail band and if there was an element of the same 
	 * type of the added one the old one is removed. The element is added always
	 * in position 0,0 of the detail band. If the template set hasn't a detail band
	 * an error is shown
	 * The template set file is updated automatically and if there isn't a selected
	 * default template set this method dosen't do anything 
	 * 
	 * @param element element to add
	 */
	public void addElementToCurrentDefault(MGraphicElement element, boolean copyAttributesFromStyles){
		if (hasDefault()){
			JRDesignElement newElement = (JRDesignElement)element.getValue().clone();
			if (copyAttributesFromStyles) CustomStyleResolver.copyInheritedAttributes(element, newElement);
			newElement.setX(0);
			newElement.setY(0);
			selectedDefaultsMap.put(element.getClass(), (MGraphicElement)ReportFactory.createNode(null, newElement, 0));
			addNewElement(newElement);
			//Update the template file
			UIUtils.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					try {
						IFile reportFile = getFileFromPath(actualDefault);
						String xml = model2xml(JRXmlWriterHelper.LAST_VERSION);
						reportFile.setContents(new ByteArrayInputStream(xml.getBytes("UTF-8")), IFile.KEEP_HISTORY | IFile.FORCE,  new NullProgressMonitor());
					} catch (Throwable e) {
						UIUtils.showError(e);
					}
				}
			});
		}
	}
	
	/**
	 * Get an IFile form an absolute path
	 * 
	 * @param pathString the absolute path in the filesystem
	 * @return the IFile in the workspace
	 */
	private IFile getFileFromPath(String pathString){
		IPath path = new Path(pathString);
		IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
		IFile reportFile = 	workspace.getRoot().getFileForLocation(path);
		return reportFile;
	}
	
	/**
	 * Generate the xml definition of the current selected template set
	 * report
	 * 
	 * @param version version of the jrxml
	 * @return an ml string
	 */
	private String model2xml(String version) {
		String xml = null;
		try {
			JasperDesign report = null;
			MReport mReport = (MReport)defaultReport.getChildren().get(0);
			if (mReport != null) {
				report = mReport.getJasperDesign();
			}
			xml = JRXmlWriterHelper.writeReport(defaultConfig, report, "UTF-8", version);
		} catch (Throwable e) {
			UIUtils.showError(e);
		}
		return xml;
	}
	

	/**
	 * The name of the current template set selected
	 * 
	 * @return the name of the selected template set or an empty string
	 * if no template set is selected
	 */
	public String getDefaultName(){
		if (hasDefault()){
			return new File(actualDefault).getName();
		}
		return "";
	}
	
	/**
	 * Remove a template set from the available template sets list
	 * and save the new list on the preferences
	 * 
	 * @param path the path of the template set to remove
	 */
	public void removeDefaultFile(String path){
		if (isCurrentDefault(path)) unsetDefaultFile();
		for(String defaults : availableDefaults){
			if (path.equals(defaults)){
				availableDefaults.remove(defaults);
				savePreferences();
				break;
			}
		}
		
	}
	
	/**
	 * Reload the current default from it's file in the workspace
	 * Used to refresh the default when the file is modified manually
	 */
	public void reloadCurrentDefault(){
		if (actualDefault != null){
			loadDefaultModel();
		}
	}
	
}
