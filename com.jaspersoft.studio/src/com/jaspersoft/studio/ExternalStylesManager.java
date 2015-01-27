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
package com.jaspersoft.studio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.style.MStyleTemplate;
import com.jaspersoft.studio.model.style.StyleTemplateFactory;
import com.jaspersoft.studio.utils.ExpressionUtil;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Class that caches the external styles to improve the performance when resolving their names
 * 
 * @author Orlandin Marco
 *
 */
public class ExternalStylesManager {

	/**
	 * Map of the cached styles, the key is the absolute path to the template file
	 */
	private static HashMap<String, List<JRStyle>> externalStylesCache = new HashMap<String, List<JRStyle>>();
	

	/**
	 * Listener called when a file is saved
	 */
	private static IResourceChangeListener resourceChangeListenr = new IResourceChangeListener(){
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			HashSet<String> removedStyles = new HashSet<String>();
			if (event.getDelta() != null){
				removeStyle(event.getDelta().getAffectedChildren(), removedStyles);
				//if (event.getType() == IResourceChangeEvent.PRE_CLOSE) removeReport(event.getDelta().getAffectedChildren());
				refreshStyles(removedStyles);
			}
		}	
	};
	
	/**
	 * Listener used to know when an editor is closed and if it has an interpreter saved than it can be removed
	 * 
	 */
	/*private static IPartListener documentClosedListener = new IPartListener() {
		
		@Override
		public void partClosed(IWorkbenchPart part) {
			if (part instanceof JrxmlEditor){
				JrxmlEditor editor = (JrxmlEditor)part;
				ExpressionUtil.removeCachedInterpreter(editor.getModel().getJasperDesign().getMainDesignDataset());
			}	
		}
		
		@Override
		public void partOpened(IWorkbenchPart part) {}
		
		@Override
		public void partDeactivated(IWorkbenchPart part) {}
		
		@Override
		public void partBroughtToTop(IWorkbenchPart part) {}
		
		@Override
		public void partActivated(IWorkbenchPart part) {}
	};*/
	
	/**
	 * Initialize the appropriate listener
	 */
	public static void initListeners(){
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListenr,
				IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.POST_CHANGE);
		
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(documentClosedListener);
	}
	
	/**
	 * Notify to the opened jrxml editors to refresh the styles
	 * 
	 * @param removedStyles name of the Styles that were updated in the cache
	 */
	private static void refreshStyles(HashSet<String> changedStyles){
		IWorkbenchWindow activeWorkbenchWindow = JaspersoftStudioPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null || activeWorkbenchWindow.getPages() == null) return;
		for(IWorkbenchPage page : activeWorkbenchWindow.getPages()){
			IEditorReference[] openEditors = page.getEditorReferences();
			for(IEditorReference editor : openEditors){
				IWorkbenchPart part = editor.getPart(false);
				if (part instanceof JrxmlEditor){
					JrxmlEditor jrxmlEditor = (JrxmlEditor)part;
					jrxmlEditor.refreshExternalStyles(changedStyles);
				}
			}
		}
	}
	
	/**
	 * When a jrtx file is saved search if it is in the cache and in case it is update
	 * 
	 * @param editedResources resources saved
	 * @param removedStylesName Map where the name of the update jrstyle are stored
	 */
	private static void removeStyle(IResourceDelta[] editedResources, HashSet<String> removedStylesName){
		for(IResourceDelta resource : editedResources){
			if (resource.getAffectedChildren().length>0) {
				removeStyle(resource.getAffectedChildren(), removedStylesName);
			}
			IPath rawLocation = resource.getResource().getRawLocation();
			if (rawLocation != null){
				String key = rawLocation.toOSString();
				List<JRStyle> removedElement = externalStylesCache.remove(key);
				if (removedElement != null){
					ArrayList<JRStyle> cachedStyles = new ArrayList<JRStyle>();
					StyleTemplateFactory.getStylesReference(key, cachedStyles, new HashSet<File>());
					externalStylesCache.put(key, cachedStyles);
					for(JRStyle style : removedElement)
						removedStylesName.add(style.getName());
				}
			}
		}
	}
	
	/**
	 * When a close action  is done, if it was of a report remove eventually its interpreter 
	 * from the cache
	 * 
	 */
	/*private static void removeReport(IResourceDelta[] editedResources){
		for(IResourceDelta resource : editedResources){
			if (resource.getAffectedChildren().length>0) {
				removeReport(resource.getAffectedChildren());
			}
			IPath rawLocation = resource.getResource().getRawLocation();
			if (rawLocation != null){
				String key = rawLocation.toOSString();
				ExpressionUtil.removeCachedInterpreter(key);
			}
		}
	}*/
	
	/**
	 * Map of the expression that was already attempt to evaluate, but since their evaluation
	 * failed they are marked as not valuable, so the next evaluation will be skipped
	 */
	private static HashSet<String> notEvaluableExpressions = new HashSet<String>();
	
	/**
	 * Key when it is raised an event of style not found
	 */
	public static final String STYLE_NOT_FOUND_EVENT = "templateReferenceNotFound";
	
	/**
	 * Key when it is raised an event of style found
	 */
	public static final String STYLE_FOUND_EVENT = "templateReferenceFound";
	
	/**
	 * Fire an event of style found or not found
	 * 
	 * @param event the text of the event, should be STYLE_NOT_FOUND_EVENT or STYLE_FOUND_EVENT
	 * @param element JRelement of the template style
	 */
	private static void fireEvent(String event, JRReportTemplate element){
		if (element instanceof JRChangeEventsSupport){
			JRChangeEventsSupport eventElement = (JRChangeEventsSupport)element;
			eventElement.getEventSupport().firePropertyChange(event, null, null);
		}
	}
	
	/**
	 * Check if a style reference expression is valuable or not 
	 * 
	 * @param projectPath the path of the project where the style is defined
	 * @param expression the text of the expression
	 * @param true if the expression can be evaluated (because it was already evaluated without errors or
	 * because it was never evaluated) false otherwise (during the last attempt to evaluate the expression an
	 * error happen)
	 */
	public static boolean isNotValuable(String projectPath, String expression){
		return (notEvaluableExpressions.contains(projectPath + "." + expression));
	}
	
	/**
	 * Check if a style reference expression is valuable or not 
	 * 
	 * @param template the model that contains the reference information
	 * @param true if the expression can be evaluated (because it was already evaluated without errors or
	 * because it was never evaluated) false otherwise (during the last attempt to evaluate the expression an
	 * error happen)
	 */
	public static boolean isNotValuable(MStyleTemplate template){
		JasperReportsConfiguration jConf = template.getJasperConfiguration();
		IFile project = (IFile) jConf.get(FileUtils.KEY_FILE);
		String projectPath = project.getLocation().toPortableString();
		
		JRDesignReportTemplate jrTemplate = (JRDesignReportTemplate) template.getValue();
		String expression =  jrTemplate.getSourceExpression().getText();
		return (notEvaluableExpressions.contains(projectPath + "." + expression));
	}
	
	/**
	 * Add a new expression of a template style to the not valuable expressions
	 * 
	 * @param projectPath the path of the project where the style is defined
	 * @param expression the text of the expression
	 */
	public static void addNotValuableExpression(String projectPath, String expression){
		notEvaluableExpressions.add(projectPath + "." + expression);
	}
	
	/**
	 * Reload a style, ignoring if it expression was already evaluated before
	 * 
	 * @param template a template style element, the value inside the model must be an
	 * instance of JRDesignReportTemplate
	 */
	public static void refreshStyle(ANode template){
		JasperReportsConfiguration jConf = template.getJasperConfiguration();
		IFile project = (IFile) jConf.get(FileUtils.KEY_FILE);
		String projectPath = project.getLocation().toPortableString();
		
		JRDesignReportTemplate jrTemplate = (JRDesignReportTemplate) template.getValue();
		String expression =  jrTemplate.getSourceExpression().getText();
		
		notEvaluableExpressions.remove(projectPath + "." + expression);
		//Recalculate the style overwriting the cache
		String evaluatedExpression = evaluateStyleExpression(jrTemplate, project, jConf);
		if (evaluatedExpression != null) {
			File styleFile = StyleTemplateFactory.getFile(evaluatedExpression, project);
			if (styleFile != null) {
				String key = styleFile.getAbsolutePath();
				List<JRStyle> cachedStyles = new ArrayList<JRStyle>();
				StyleTemplateFactory.getStylesReference(project, evaluatedExpression, cachedStyles, new HashSet<File>());
				externalStylesCache.put(key, cachedStyles);
				fireEvent(STYLE_FOUND_EVENT, jrTemplate);
			} else {
				JRExpression styleExpression = jrTemplate.getSourceExpression();
				String expString = styleExpression != null ? styleExpression.getText() : "";
				addNotValuableExpression(projectPath, expString);
				fireEvent(STYLE_NOT_FOUND_EVENT, jrTemplate);
			}
		}
	}
	
	/**
	 * Resolve an expression and return the reference to the style or null if it can not be resolve
	 * 
	 * @param styleExpression expression of the external style
	 * @param project project of the report
	 * @param jConfig Configuration of the report to evaluate the expression
	 * @return path of the style of null if the expression can't be resolved
	 */
	public static String evaluateStyleExpression(JRReportTemplate style, IFile project, JasperReportsConfiguration jConfig){	
		String evaluatedExpression = null;
		String projectPath = project.getLocation().toPortableString();
		JRExpression styleExpression = style.getSourceExpression();
		String expString = styleExpression != null ? styleExpression.getText() : "";
		try{
			//Check first if there are previous failed attempt to evaluate the expression
			if (!isNotValuable(projectPath, expString)){
				evaluatedExpression =  ExpressionUtil.cachedExpressionEvaluation(styleExpression, jConfig); 
				if (evaluatedExpression == null){
					//The expression is not valuable, add it to the map
					addNotValuableExpression(projectPath, expString);
					fireEvent(STYLE_NOT_FOUND_EVENT, style);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			//The expression is not valuable, add it to the map
			addNotValuableExpression(projectPath, expString);
			fireEvent(STYLE_NOT_FOUND_EVENT, style);
		}
		return evaluatedExpression;
	}
	
	
	/**
	 * If the expression of an external style can be resolved then return all the jrstyle defined inside
	 * otherwise return an empty list
	 * 
	 * @param styleExpression expression of the external style
	 * @param project project of the report
	 * @param jConfig Configuration of the report to evaluate the expression
	 * @return Not null list of styles inside the external style associated with the project and expression
	 */
	public static List<JRStyle> getStyles(JRReportTemplate style, IFile project, JasperReportsConfiguration jConfig) {
		String evaluatedExpression = evaluateStyleExpression(style, project, jConfig);
		if (evaluatedExpression != null) {
			File styleFile = StyleTemplateFactory.getFile(evaluatedExpression, project);
			if (styleFile != null) {
				String key = styleFile.getAbsolutePath();
				List<JRStyle> cachedStyles = externalStylesCache.get(key);
				if (cachedStyles == null) {
					cachedStyles = new ArrayList<JRStyle>();
					StyleTemplateFactory.getStylesReference(project, evaluatedExpression, cachedStyles, new HashSet<File>());
					externalStylesCache.put(key, cachedStyles);
				}
				fireEvent(STYLE_FOUND_EVENT, style);
				return cachedStyles;
			} else {
				String projectPath = project.getLocation().toPortableString();
				JRExpression styleExpression = style.getSourceExpression();
				String expString = styleExpression != null ? styleExpression.getText() : "";
				addNotValuableExpression(projectPath, expString);
				fireEvent(STYLE_NOT_FOUND_EVENT, style);
			}
		}
		return new ArrayList<JRStyle>();
	}
	
	/**
	 * Given a list of JRStyles and a styles name search inside the list a JRStyle
	 * with that name and return it
	 * 
	 * @param jrStylesList list where the style is searched
	 * @param searchedName name of the searched style
	 * @return the style in the list with the requested name, or null if it can't be found
	 */
	private static JRStyle searchStyleInList(List<JRStyle> jrStylesList, String searchedName){
		for(JRStyle style : jrStylesList){
			if (searchedName.equals(style.getName())){
				return style;
			}
		}
		return null;
	}
	
	/**
	 * Search in all the external styles template of a report a style with a specific name
	 * and return it. If it can't be found it return null, and if there are more styles in 
	 * different templates with the searched name then the first one found is returned
	 * 
	 * @param styleName the name of the style searched
	 * @param jConfig jasper configuration of the report 
	 * @return a JRStyle reference of the searched style or null if it can't be found between 
	 * the defined external styles
	 */
	public static JRStyle getExternalStyle(String styleName, JasperReportsConfiguration jConfig){
		JasperDesign design = jConfig.getJasperDesign();
		if (design != null){
			IFile project = (IFile) jConfig.get(FileUtils.KEY_FILE);
			for (JRReportTemplate template : design.getTemplatesList()){
				List<JRStyle> loadedStyles = getStyles(template, project, jConfig);
				JRStyle searchedStyle = searchStyleInList(loadedStyles, styleName);
				if (searchedStyle != null) return searchedStyle;
			}
		}
		return null;
	}

}
