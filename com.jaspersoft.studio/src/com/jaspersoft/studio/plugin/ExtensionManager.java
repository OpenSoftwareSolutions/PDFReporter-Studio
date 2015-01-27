/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.WorkbenchPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.editor.IEditorContributor;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionEditorSupportFactory;
import com.jaspersoft.studio.editor.preview.PreviewModeDetails;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.repository.IRepositoryViewProvider;
import com.jaspersoft.studio.style.view.TemplateViewProvider;
import com.jaspersoft.studio.templates.TemplateProvider;
import com.jaspersoft.studio.utils.AContributorAction;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ExtensionManager {

	private static Map<Class<?>, IComponentFactory> factoryByNodeType = new HashMap<Class<?>, IComponentFactory>();

	public void init() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "components"); //$NON-NLS-1$ 
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IComponentFactory) {
					IComponentFactory compFactory = (IComponentFactory) o;
					nodeFactory.add(compFactory);
					for (Class<?> cl : compFactory.getKnownClasses()) {
						factoryByNodeType.put(cl, compFactory);
					}
				}
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}

		// List all the extensions that provide a DataAdapterFactory
		config = Platform.getExtensionRegistry().getConfigurationElementsFor(JaspersoftStudioPlugin.PLUGIN_ID,
				"dataAdapters"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof DataAdapterFactory)
					DataAdapterManager.addDataAdapterFactory((DataAdapterFactory) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}

		config = Platform.getExtensionRegistry().getConfigurationElementsFor(JaspersoftStudioPlugin.PLUGIN_ID,
				"editorLifecycle"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IEditorContributor)
					eContributor.add((IEditorContributor) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}

		DataAdapterManager.getPreferencesStorage();
	}

	public List<IRepositoryViewProvider> getRepositoryProviders() {
		List<IRepositoryViewProvider> paletteGroup = new ArrayList<IRepositoryViewProvider>();
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "repositoryview"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IRepositoryViewProvider)
					paletteGroup.add((IRepositoryViewProvider) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return paletteGroup;
	}

	/**
	 * Returns the support factory for the expression editor.
	 * 
	 * <p>
	 * The method seeks for a custom support factory or a default one (fallback).
	 * 
	 * @return the contributed support factory, null <code>otherwise</code>
	 */
	public IExpressionEditorSupportFactory getExpressionEditorSupportFactory() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "expressionEditorSupport"); //$NON-NLS-1$ 
		IExpressionEditorSupportFactory defaultFactory = null;
		boolean defaultFound = false;
		boolean overrideFound = true;
		for (IConfigurationElement el : config) {
			if (!defaultFound && "false".equals(el.getAttribute("override"))) {
				Object defaultSupportClazz;
				try {
					defaultSupportClazz = el.createExecutableExtension("class");
					if (defaultSupportClazz instanceof IExpressionEditorSupportFactory) {
						defaultFactory = (IExpressionEditorSupportFactory) defaultSupportClazz;
					}
				} catch (CoreException e) {
					JaspersoftStudioPlugin
							.getInstance()
							.getLog()
							.log(
									new Status(IStatus.ERROR, JaspersoftStudioPlugin.PLUGIN_ID,
											"An error occurred while trying to create the new class.", e));
				}
			} else {
				if (!overrideFound && "true".equals(el.getAttribute("override"))) {
					overrideFound = true;
					Object overrideClazz;
					try {
						overrideClazz = el.createExecutableExtension("class");
						if (overrideClazz instanceof IExpressionEditorSupportFactory) {
							return (IExpressionEditorSupportFactory) overrideClazz;
						}
					} catch (CoreException e) {
						JaspersoftStudioPlugin
								.getInstance()
								.getLog()
								.log(
										new Status(IStatus.ERROR, JaspersoftStudioPlugin.PLUGIN_ID,
												"An error occurred while trying to create the new class.", e));
					}
				}
			}
		}

		return defaultFactory;
	}

	/**
	 * Returns the list of contributed template provider, on the extension point templateProviderSupport
	 * 
	 * @return the list of contributed template provider, it can be empty but not null
	 */
	public List<TemplateProvider> getTemplateProviders() {

		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "templateProviderSupport"); //$NON-NLS-1$ 

		ArrayList<TemplateProvider> providersList = new ArrayList<TemplateProvider>();
		for (IConfigurationElement el : config) {

			Object defaultSupportClazz;
			try {
				defaultSupportClazz = el.createExecutableExtension("providerClass");
				if (defaultSupportClazz instanceof TemplateProvider) {
					providersList.add((TemplateProvider) defaultSupportClazz);
				}
			} catch (CoreException e) {
				JaspersoftStudioPlugin
						.getInstance()
						.getLog()
						.log(
								new Status(IStatus.ERROR, JaspersoftStudioPlugin.PLUGIN_ID,
										"An error occurred while trying to create the new class.", e));
			}
		}
		return providersList;
	}

	/**
	 * A list of the contributed Tab to visualize a series of Template Styles
	 */
	private ArrayList<TemplateViewProvider> stylesViewList = null;

	/**
	 * Return a list of the contributed Tab to visualize a series of Template Styles. The read styles are cached after the
	 * first time they are red
	 * 
	 * @return a list of TemplateViewProvider
	 */
	public List<TemplateViewProvider> getStylesViewProvider() {
		if (stylesViewList == null) {
			IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
					JaspersoftStudioPlugin.PLUGIN_ID, "stylesViewContributor");
			stylesViewList = new ArrayList<TemplateViewProvider>();
			for (IConfigurationElement el : config) {

				Object defaultSupportClazz;
				try {
					defaultSupportClazz = el.createExecutableExtension("providerClass");
					if (defaultSupportClazz instanceof TemplateViewProvider) {
						stylesViewList.add((TemplateViewProvider) defaultSupportClazz);
					}
				} catch (CoreException e) {
					JaspersoftStudioPlugin
							.getInstance()
							.getLog()
							.log(
									new Status(IStatus.ERROR, JaspersoftStudioPlugin.PLUGIN_ID,
											"An error occurred while trying to create the new class.", e));
				}
			}
		}
		return stylesViewList;
	}

	public List<PaletteGroup> getPaletteGroups() {
		List<PaletteGroup> paletteGroup = new ArrayList<PaletteGroup>();
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "palette"); //$NON-NLS-1$ 
		for (IConfigurationElement e : config) {
			PaletteGroup p = new PaletteGroup();
			p.setId(e.getAttribute("id")); //$NON-NLS-1$
			p.setName(e.getAttribute("Name")); //$NON-NLS-1$
			p.setImage(e.getAttribute("image")); //$NON-NLS-1$
			p.setAfterGroup(e.getAttribute("afterGroup")); //$NON-NLS-1$
			paletteGroup.add(p);
		}
		return paletteGroup;
	}

	public Map<String, List<PaletteEntry>> getPaletteEntries() {
		Map<String, List<PaletteEntry>> map = new HashMap<String, List<PaletteEntry>>();
		for (IComponentFactory f : nodeFactory) {
			IPaletteContributor ipc = f.getPaletteEntries();
			if (ipc != null) {
				Map<String, List<PaletteEntry>> paletteEntries = ipc.getPaletteEntries();
				if (paletteEntries != null) {
					for (String key : paletteEntries.keySet()) {
						List<PaletteEntry> ol = map.get(key);
						if (ol == null)
							map.put(key, paletteEntries.get(key));
						else
							ol.addAll(paletteEntries.get(key));
					}
				}
			}
		}
		return map;
	}

	private List<IComponentFactory> nodeFactory = new ArrayList<IComponentFactory>();

	public ANode createNode(ANode parent, Object jrObject, int newIndex) {
		for (IComponentFactory f : nodeFactory) {
			ANode n = f.createNode(parent, jrObject, newIndex);
			if (n != null)
				return n;
		}
		return null;
	}

	public List<?> getChildren4Element(Object jrObject) {
		for (IComponentFactory f : nodeFactory) {
			List<?> lst = f.getChildren4Element(jrObject);
			if (lst != null && !lst.isEmpty())
				return lst;
		}
		return null;
	}

	public Command getStretchToContent(ANode node) {
		for (IComponentFactory f : getPrioritizedFactoryList(node)) {
			Command c = f.getStretchToContent(node);
			if (c != null)
				return c;
		}
		return null;
	}

	private List<IComponentFactory> getPrioritizedFactoryList(Object obj) {
		if (obj != null) {
			IComponentFactory selectedFactory = factoryByNodeType.get(obj.getClass());
			if (selectedFactory != null) {
				List<IComponentFactory> copyLst = new ArrayList<IComponentFactory>(nodeFactory.size());
				copyLst.addAll(nodeFactory);
				copyLst.remove(selectedFactory);
				copyLst.add(0, selectedFactory);
				return copyLst;
			}
		}
		return nodeFactory;
	}

	public Command getCreateCommand(ANode parent, ANode child, Rectangle location, int newIndex) {
		for (IComponentFactory f : getPrioritizedFactoryList(child)) {
			Command c = f.getCreateCommand(parent, child, location, newIndex);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	public Command getDeleteCommand(ANode parent, ANode child) {
		for (IComponentFactory f : getPrioritizedFactoryList(child)) {
			Command c = f.getDeleteCommand(parent, child);
			if (c != null)
				return c;
		}
		return null;
	}

	public Command getReorderCommand(ANode parent, ANode child, int newIndex) {
		for (IComponentFactory f : getPrioritizedFactoryList(child)) {
			Command c = f.getReorderCommand(child, parent, newIndex);
			if (c != null)
				return c;
		}
		return null;
	}

	public Command getOrphanCommand(ANode parent, ANode child) {
		for (IComponentFactory f : getPrioritizedFactoryList(child)) {
			Command c = f.getOrphanCommand(parent, child);
			if (c != null)
				return c;
		}
		return null;
	}

	public IFigure createFigure(ANode node) {
		for (IComponentFactory f : getPrioritizedFactoryList(node)) {
			IFigure c = f.createFigure(node);
			if (c != null)
				return c;
		}
		return null;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		for (IComponentFactory f : getPrioritizedFactoryList(model)) {
			EditPart c = f.createEditPart(context, model);
			if (c != null)
				return c;
		}
		return null;
	}

	public List<Action> getActions(WorkbenchPart part) {
		List<Action> lst = new ArrayList<Action>();
		for (IComponentFactory f : nodeFactory) {
			List<Action> l = f.getActions(part);
			if (l != null && !l.isEmpty())
				lst.addAll(l);
		}
		return lst;
	}

	public List<String> getActionIDs() {
		List<String> lst = new ArrayList<String>();
		for (IComponentFactory f : nodeFactory) {
			List<String> l = f.getActionsID();
			if (l != null && !l.isEmpty())
				lst.addAll(l);
		}
		return lst;
	}

	public AbstractVisualEditor getEditor(Object parent, JasperReportsConfiguration jrContext) {
		for (IComponentFactory f : nodeFactory) {
			AbstractVisualEditor n = f.getEditor(parent, jrContext);
			if (n != null)
				return n;
		}
		return null;
	}

	private List<IEditorContributor> eContributor = new ArrayList<IEditorContributor>();

	public void onLoad(JasperDesign jd, EditorPart editor) {
		for (IEditorContributor f : eContributor)
			f.onLoad(jd, editor);
	}

	public void onSave(JasperReportsConfiguration jrConfig, IProgressMonitor monitor) {
		for (IEditorContributor f : eContributor)
			f.onSave(jrConfig, monitor);
	}

	public String getTitleToolTip(JasperReportsConfiguration jrConfig, String tooltip) {
		for (IEditorContributor f : eContributor) {
			String s = f.getTitleToolTip(jrConfig, tooltip);
			if (s != null && !s.isEmpty())
				tooltip = s;
		}
		return tooltip;
	}

	public void onRun(JasperReportsConfiguration jrConfig, JasperReport jr, Map<String, Object> params) {
		for (IEditorContributor f : eContributor)
			f.onRun(jrConfig, jr, params);
	}

	public List<AContributorAction> getActions() {
		List<AContributorAction> list = new ArrayList<AContributorAction>();
		for (IEditorContributor f : eContributor) {
			AContributorAction[] actions = f.getActions();
			for (AContributorAction a : actions)
				list.add(a);
		}
		return list;
	}

	public ExpressionContext getExpressionContext4Element(Object jrObject) {
		for (IComponentFactory f : nodeFactory) {
			ExpressionContext exprContext = f.getElementExpressionContext(jrObject);
			if (exprContext != null)
				return exprContext;
		}
		return null;
	}

	/**
	 * Looks for contributions related to the specified preview mode ID.
	 * 
	 * @param previewModeID
	 *          the preview mode identifier
	 * @return the list of contributed information
	 */
	public List<PreviewModeDetails> getAllPreviewModeDetails(String previewModeID) {
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, PreviewModeDetails.EXTENSION_POINT_ID); //$NON-NLS-1$
		List<PreviewModeDetails> allDetails = new ArrayList<PreviewModeDetails>();
		for (IConfigurationElement ce : elements) {
			if (previewModeID.equals(ce.getAttribute("modeID")) || previewModeID == null) {
				Object clazz;
				try {
					clazz = ce.createExecutableExtension("class");
					if (clazz instanceof PreviewModeDetails) {
						allDetails.add((PreviewModeDetails) clazz);
					}
				} catch (CoreException e) {
					JaspersoftStudioPlugin.getInstance().logError("An error occurred while trying to create the new class.", e);
				}
			}
		}
		return allDetails;
	}

	/**
	 * @return all the contributions for all the possible preview modes.
	 */
	public List<PreviewModeDetails> getAllPreviewModeDetails() {
		return getAllPreviewModeDetails(null);
	}
}
