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
package com.jaspersoft.studio.properties.internal;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.properties.Activator;
import com.jaspersoft.studio.properties.TabbedPropertyViewStatusCodes;
import com.jaspersoft.studio.properties.messages.Messages;
import com.jaspersoft.studio.properties.view.AbstractTabDescriptor;
import com.jaspersoft.studio.properties.view.IActionProvider;
import com.jaspersoft.studio.properties.view.ISectionDescriptor;
import com.jaspersoft.studio.properties.view.ISectionDescriptorProvider;
import com.jaspersoft.studio.properties.view.ITabDescriptor;
import com.jaspersoft.studio.properties.view.ITabDescriptorProvider;
import com.jaspersoft.studio.properties.view.ITypeMapper;

/**
 * Provides information about the tabbed property extension points. Each tabbed
 * property registry is associated with a unique contributor ID.
 * 
 * @author Anthony Hunter
 */
public class TabbedPropertyRegistry {

	private final static String NO_TAB_ERROR = Messages.TabbedPropertyRegistry_Non_existing_tab;

	private final static String CONTRIBUTOR_ERROR = Messages.TabbedPropertyRegistry_contributor_error;

	private final static String TAB_ERROR = Messages.TabDescriptor_Tab_unknown_category;

	// extension point constants
	private static final String EXTPT_CONTRIBUTOR = "propertyContributor"; //$NON-NLS-1$

	private static final String EXTPT_TABS = "propertyTabs"; //$NON-NLS-1$

	private static final String EXTPT_SECTIONS = "propertySections"; //$NON-NLS-1$

	private static final String ELEMENT_TAB = "propertyTab"; //$NON-NLS-1$

	private static final String ELEMENT_SECTION = "propertySection"; //$NON-NLS-1$

	private static final String ELEMENT_PROPERTY_CATEGORY = "propertyCategory"; //$NON-NLS-1$

	private static final String ATT_CATEGORY = "category"; //$NON-NLS-1$

	private static final String ATT_CONTRIBUTOR_ID = "contributorId"; //$NON-NLS-1$

	private static final String ATT_TYPE_MAPPER = "typeMapper"; //$NON-NLS-1$

	private static final String ATT_LABEL_PROVIDER = "labelProvider"; //$NON-NLS-1$

	private static final String ATT_ACTION_PROVIDER = "actionProvider"; //$NON-NLS-1$

	private static final String ATT_SECTION_DESCRIPTOR_PROVIDER = "sectionDescriptorProvider"; //$NON-NLS-1$

	private static final String ATT_TAB_DESCRIPTOR_PROVIDER = "tabDescriptorProvider"; //$NON-NLS-1$

	private static final String TOP = "top"; //$NON-NLS-1$

	protected String contributorId;

	protected IConfigurationElement contributorConfigurationElement;

	protected List<String> propertyCategories;

	protected ILabelProvider labelProvider;

	protected IActionProvider actionProvider;

	protected ITypeMapper typeMapper;

	protected ISectionDescriptorProvider sectionDescriptorProvider;

	protected ITabDescriptorProvider tabDescriptorProvider;

	protected ITabDescriptor[] tabDescriptors;

	protected static final AbstractTabDescriptor[] EMPTY_DESCRIPTOR_ARRAY = new TabDescriptor[0];

	/**
	 * There is one details registry for each contributor type.
	 */
	protected TabbedPropertyRegistry(String id) {
		this.contributorId = id;
		this.propertyCategories = new ArrayList<String>();
		IConfigurationElement[] extensions = getConfigurationElements(EXTPT_CONTRIBUTOR);
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement configurationElement = extensions[i];
			String contributor = configurationElement
					.getAttribute(ATT_CONTRIBUTOR_ID);
			if (contributor == null || !id.equals(contributor)) {
				continue;
			}
			this.contributorConfigurationElement = configurationElement;
			try {
				if (configurationElement.getAttribute(ATT_LABEL_PROVIDER) != null) {
					labelProvider = (ILabelProvider) configurationElement
							.createExecutableExtension(ATT_LABEL_PROVIDER);
				}
				if (configurationElement.getAttribute(ATT_ACTION_PROVIDER) != null) {
					actionProvider = (IActionProvider) configurationElement
							.createExecutableExtension(ATT_ACTION_PROVIDER);
				}
				if (configurationElement.getAttribute(ATT_TYPE_MAPPER) != null) {
					typeMapper = (ITypeMapper) configurationElement
							.createExecutableExtension(ATT_TYPE_MAPPER);
				}
				if (configurationElement
						.getAttribute(ATT_SECTION_DESCRIPTOR_PROVIDER) != null) {
					sectionDescriptorProvider = (ISectionDescriptorProvider) configurationElement
							.createExecutableExtension(ATT_SECTION_DESCRIPTOR_PROVIDER);
				}
				if (configurationElement
						.getAttribute(ATT_TAB_DESCRIPTOR_PROVIDER) != null) {
					tabDescriptorProvider = (ITabDescriptorProvider) configurationElement
							.createExecutableExtension(ATT_TAB_DESCRIPTOR_PROVIDER);
				}
			} catch (CoreException exception) {
				handleConfigurationError(id, exception);
			}
			addPropertyCategories(configurationElement);
		}
		if (propertyCategories == null || contributorId == null
				|| contributorConfigurationElement == null) {
			handleConfigurationError(id, null);
			this.contributorId = null;
		}
	}

	/**
	 * Gets the categories that are valid for this contributor.
	 * 
	 * @param configurationElement
	 *            the configuration element for this contributor.
	 */
	private void addPropertyCategories(
			IConfigurationElement configurationElement) {
		IConfigurationElement[] elements = configurationElement
				.getChildren(ELEMENT_PROPERTY_CATEGORY);
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			propertyCategories.add(element.getAttribute(ATT_CATEGORY));
		}
	}

	/**
	 * Handle the error when an issue is found loading from the configuration
	 * element.
	 * 
	 * @param id
	 *            the configuration id.
	 * @param exception
	 *            an optional CoreException
	 */
	private void handleConfigurationError(String id, CoreException exception) {
		String message = MessageFormat.format(CONTRIBUTOR_ERROR,
				new Object[] { id });
		IStatus status = new Status(IStatus.ERROR, Activator.getDefault()
				.getBundle().getSymbolicName(),
				TabbedPropertyViewStatusCodes.CONTRIBUTOR_ERROR, message,
				exception);
		Activator.getDefault().getLog().log(status);
	}

	/**
	 * Reads property section extensions. Returns all section descriptors for
	 * the current contributor id or an empty array if none is found.
	 */
	protected ISectionDescriptor[] readSectionDescriptors() {
		List<ISectionDescriptor> result = new ArrayList<ISectionDescriptor>();
		IConfigurationElement[] extensions = getConfigurationElements(EXTPT_SECTIONS);
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement extension = extensions[i];
			IConfigurationElement[] sections = extension
					.getChildren(ELEMENT_SECTION);
			for (int j = 0; j < sections.length; j++) {
				IConfigurationElement section = sections[j];
				ISectionDescriptor descriptor = new SectionDescriptor(section,
						typeMapper);
				result.add(descriptor);
			}
		}
		return (ISectionDescriptor[]) result
				.toArray(new ISectionDescriptor[result.size()]);
	}

	/**
	 * Returns the configuration elements targeted for the given extension point
	 * and the current contributor id. The elements are also sorted by plugin
	 * prerequisite order.
	 */
	protected IConfigurationElement[] getConfigurationElements(
			String extensionPointId) {
		if (contributorId == null) {
			return new IConfigurationElement[0];
		}
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(
						Activator.getDefault().getBundle().getSymbolicName(),
						extensionPointId);
		IConfigurationElement[] extensions = point.getConfigurationElements();
		List<IConfigurationElement> unordered = new ArrayList<IConfigurationElement>(
				extensions.length);
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement extension = extensions[i];
			if (!extension.getName().equals(extensionPointId)) {
				continue;
			}
			String contributor = extension.getAttribute(ATT_CONTRIBUTOR_ID);
			if (!contributorId.equals(contributor)) {
				continue;
			}
			unordered.add(extension);
		}
		return (IConfigurationElement[]) unordered
				.toArray(new IConfigurationElement[unordered.size()]);
	}

	/**
	 * Returns the index of the given element in the array.
	 */
	private int getIndex(Object[] array, Object target) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(target)) {
				return i;
			}
		}
		return -1; // should never happen
	}

	/**
	 * Returns all section descriptors for the provided selection.
	 * 
	 * @param part
	 *            the workbench part containing the selection
	 * @param selection
	 *            the current selection.
	 * @return all section descriptors.
	 */
	public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part,
			ISelection selection) {
		if (selection == null || selection.isEmpty()) {
			return EMPTY_DESCRIPTOR_ARRAY;
		}

		ITabDescriptor[] allDescriptors = null;
		if (tabDescriptorProvider == null) {
			allDescriptors = getAllTabDescriptors();
		} else {
			allDescriptors = tabDescriptorProvider.getTabDescriptors(part,
					selection);
		}

		ITabDescriptor[] result = filterTabDescriptors(allDescriptors, part,
				selection);
		return result;
	}

	/**
	 * Filters out the tab descriptors that do not have any sections for the
	 * given input.
	 */
	protected ITabDescriptor[] filterTabDescriptors(
			ITabDescriptor[] descriptors, IWorkbenchPart part,
			ISelection selection) {
		List<ITabDescriptor> result = new ArrayList<ITabDescriptor>();
		for (int i = 0; i < descriptors.length; i++) {
			ITabDescriptor descriptor = adaptDescriptorFor(descriptors[i],
					part, selection);
			if (!descriptor.getSectionDescriptors().isEmpty()) {
				result.add(descriptor);
			}
		}
		if (result.size() == 0) {
			return EMPTY_DESCRIPTOR_ARRAY;
		}
		return (ITabDescriptor[]) result.toArray(new ITabDescriptor[result
				.size()]);
	}

	/**
	 * Given a property tab descriptor remove all its section descriptors that
	 * do not apply to the given input object.
	 */
	protected ITabDescriptor adaptDescriptorFor(ITabDescriptor target,
			IWorkbenchPart part, ISelection selection) {
		List<ISectionDescriptor> filteredSectionDescriptors = new ArrayList<ISectionDescriptor>();
		List<ISectionDescriptor> descriptors = target.getSectionDescriptors();
		for (ISectionDescriptor descriptor : descriptors) {
			if (descriptor.appliesTo(part, selection)) {
				filteredSectionDescriptors.add(descriptor);
			}
		}
		AbstractTabDescriptor result = (AbstractTabDescriptor) ((AbstractTabDescriptor) target)
				.clone();
		result.setSectionDescriptors(filteredSectionDescriptors);
		return result;
	}

	/**
	 * Reads property tab extensions. Returns all tab descriptors for the
	 * current contributor id or an empty array if none is found.
	 */
	protected ITabDescriptor[] getAllTabDescriptors() {
		if (tabDescriptors == null) {
			List<ITabDescriptor> temp = readTabDescriptors();
			populateWithSectionDescriptors(temp);
			temp = sortTabDescriptorsByCategory(temp);
			temp = sortTabDescriptorsByAfterTab(temp);
			tabDescriptors = (TabDescriptor[]) temp
					.toArray(new TabDescriptor[temp.size()]);
		}
		return tabDescriptors;
	}

	/**
	 * Reads property tab extensions. Returns all tab descriptors for the
	 * current contributor id or an empty list if none is found.
	 */
	protected List<ITabDescriptor> readTabDescriptors() {
		List<ITabDescriptor> result = new ArrayList<ITabDescriptor>();
		IConfigurationElement[] extensions = getConfigurationElements(EXTPT_TABS);
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement extension = extensions[i];
			IConfigurationElement[] tabs = extension.getChildren(ELEMENT_TAB);
			for (int j = 0; j < tabs.length; j++) {
				IConfigurationElement tab = tabs[j];
				TabDescriptor descriptor = new TabDescriptor(tab);
				if (getIndex(propertyCategories.toArray(),
						descriptor.getCategory()) == -1) {
					/* tab descriptor has unknown category */
					handleTabError(tab, descriptor.getCategory() == null ? "" //$NON-NLS-1$
							: descriptor.getCategory());
				} else {
					result.add(descriptor);
				}
			}
		}
		return result;
	}

	/**
	 * Populates the given tab descriptors with section descriptors.
	 */
	protected void populateWithSectionDescriptors(
			List<ITabDescriptor> aTabDescriptors) {
		ISectionDescriptor[] sections = null;
		if (sectionDescriptorProvider != null) {
			sections = sectionDescriptorProvider.getSectionDescriptors();
		} else {
			sections = readSectionDescriptors();
		}
		for (int i = 0; i < sections.length; i++) {
			ISectionDescriptor section = sections[i];
			appendToTabDescriptor(section, aTabDescriptors);
		}
	}

	/**
	 * Appends the given section to a tab from the list.
	 */
	protected void appendToTabDescriptor(ISectionDescriptor section,
			List<ITabDescriptor> aTabDescriptors) {
		for (ITabDescriptor tab : aTabDescriptors) {
			if (((TabDescriptor) tab).append(section)) {
				return;
			}
		}
		// could not append the section to any of the existing tabs - log error
		String message = MessageFormat.format(NO_TAB_ERROR, new Object[] {
				section.getId(), section.getTargetTab() });
		IStatus status = new Status(IStatus.ERROR, Activator.getDefault()
				.getBundle().getSymbolicName(),
				TabbedPropertyViewStatusCodes.NO_TAB_ERROR, message, null);
		Activator.getDefault().getLog().log(status);
	}

	/**
	 * Sorts the tab descriptors in the given list according to category.
	 */
	protected List<ITabDescriptor> sortTabDescriptorsByCategory(
			List<ITabDescriptor> descriptors) {
		Collections.sort(descriptors, new Comparator<ITabDescriptor>() {

			public int compare(ITabDescriptor arg0, ITabDescriptor arg1) {
				TabDescriptor one = (TabDescriptor) arg0;
				TabDescriptor two = (TabDescriptor) arg1;
				String categoryOne = one.getCategory();
				String categoryTwo = two.getCategory();
				int categoryOnePosition = getIndex(
						propertyCategories.toArray(), categoryOne);
				int categoryTwoPosition = getIndex(
						propertyCategories.toArray(), categoryTwo);
				return categoryOnePosition - categoryTwoPosition;
			}
		});
		return descriptors;
	}

	/**
	 * Sorts the tab descriptors in the given list according to afterTab.
	 */
	protected List<ITabDescriptor> sortTabDescriptorsByAfterTab(
			List<ITabDescriptor> tabs) {
		if (tabs.size() == 0 || propertyCategories == null) {
			return tabs;
		}
		List<ITabDescriptor> sorted = new ArrayList<ITabDescriptor>();
		int categoryIndex = 0;
		for (int i = 0; i < propertyCategories.size(); i++) {
			List<ITabDescriptor> categoryList = new ArrayList<ITabDescriptor>();
			String category = (String) propertyCategories.get(i);
			int topOfCategory = categoryIndex;
			int endOfCategory = categoryIndex;
			while (endOfCategory < tabs.size()
					&& ((TabDescriptor) tabs.get(endOfCategory)).getCategory()
							.equals(category)) {
				endOfCategory++;
			}
			for (int j = topOfCategory; j < endOfCategory; j++) {
				TabDescriptor tab = (TabDescriptor) tabs.get(j);
				if (tab.getAfterTab().equals(TOP)) {
					categoryList.add(0, tabs.get(j));
				} else {
					categoryList.add(tabs.get(j));
				}
			}
			Collections.sort(categoryList, new Comparator<ITabDescriptor>() {

				public int compare(ITabDescriptor arg0, ITabDescriptor arg1) {
					TabDescriptor one = (TabDescriptor) arg0;
					TabDescriptor two = (TabDescriptor) arg1;
					if (two.getAfterTab().equals(one.getId())) {
						return -1;
					} else if (one.getAfterTab().equals(two.getId())) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			for (int j = 0; j < categoryList.size(); j++) {
				sorted.add(categoryList.get(j));
			}
			categoryIndex = endOfCategory;
		}
		return sorted;
	}

	/**
	 * Gets the type mapper for the contributor.
	 * 
	 * @return the type mapper for the contributor.
	 */
	public ITypeMapper getTypeMapper() {
		return typeMapper;
	}

	/**
	 * Gets the label provider for the contributor.
	 * 
	 * @return the label provider for the contributor.
	 */
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	/**
	 * Gets the action provider for the contributor.
	 * 
	 * @return the action provider for the contributor.
	 */
	public IActionProvider getActionProvider() {
		return actionProvider;
	}

	/**
	 * Gets the tab list content provider for the contributor.
	 * 
	 * @return the tab list content provider for the contributor.
	 */
	public IStructuredContentProvider getTabListContentProvider() {
		return new TabListContentProvider(this);
	}

	/**
	 * Handle the tab error when an issue is found loading from the
	 * configuration element.
	 * 
	 * @param configurationElement
	 *            the configuration element
	 */
	private void handleTabError(IConfigurationElement configurationElement,
			String category) {
		String pluginId = configurationElement.getDeclaringExtension()
				.getNamespaceIdentifier();
		String message = MessageFormat.format(TAB_ERROR, new Object[] {
				pluginId, category });
		IStatus status = new Status(IStatus.ERROR, pluginId,
				TabbedPropertyViewStatusCodes.TAB_ERROR, message, null);
		Activator.getDefault().getLog().log(status);
	}

	/**
	 * Disposes this registry.
	 * 
	 * @since 3.7
	 */
	public void dispose() {
		if (labelProvider != null) {
			labelProvider.dispose();
			labelProvider = null;
		}

		if (tabDescriptors != null) {
			for (int i = 0; i < tabDescriptors.length; i++) {
				if (tabDescriptors[i] instanceof TabDescriptor)
					((TabDescriptor) tabDescriptors[i]).dispose();
			}
		}
	}
}
