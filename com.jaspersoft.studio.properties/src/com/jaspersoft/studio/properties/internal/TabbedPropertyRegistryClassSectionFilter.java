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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.jaspersoft.studio.properties.view.ISectionDescriptor;
import com.jaspersoft.studio.properties.view.ITypeMapper;

/**
 * Provides a section filtering mechanism where the selection is an
 * IStructuredSelection and filtering is based on class.
 * 
 * @author Anthony Hunter
 */
public class TabbedPropertyRegistryClassSectionFilter {

	private ITypeMapper typeMapper = null;

	/**
	 * Constructor for TabbedPropertyRegistryClassSectionFilter
	 * 
	 * @param typeMapper
	 *            the type mapper.
	 */
	public TabbedPropertyRegistryClassSectionFilter(ITypeMapper typeMapper) {
		super();
		this.typeMapper = typeMapper;
	}

	/**
	 * Verifies if the property section extension represented by sectionElement
	 * applies to the given input.
	 * 
	 * @param descriptor
	 *            the section descriptor.
	 * @param selection
	 *            the selection.
	 * @return <code>true</code> if this section applies to the current
	 *         selection.
	 */
	public boolean appliesToSelection(ISectionDescriptor descriptor,
			ISelection selection) {

		if (selection instanceof IStructuredSelection
				&& selection.isEmpty() == false) {

			if (descriptor.getEnablesFor() != ISectionDescriptor.ENABLES_FOR_ANY
					&& ((IStructuredSelection) selection).size() != descriptor
							.getEnablesFor()) {
				/**
				 * enablesFor does not match the size of the selection, do not
				 * display section.
				 */
				return false;
			}

			IFilter filter = descriptor.getFilter();

			if (filter != null) {
				for (Iterator<?> i = ((IStructuredSelection) selection)
						.iterator(); i.hasNext();) {
					Object object = i.next();

					if (filter != null && filter.select(object) == false) {
						/**
						 * filter fails so section does not apply to the
						 * selection, do not display section.
						 */
						return false;
					}
				}
				/**
				 * filter passes for all objects in the selection.
				 */
				return true;
			}

			Set<Class<?>> effectiveTypes = new HashSet<Class<?>>();

			for (Iterator<?> i = ((IStructuredSelection) selection).iterator(); i
					.hasNext();) {

				Object object = i.next();

				Class<?> remapType = object.getClass();
				if (typeMapper != null) {
					remapType = typeMapper.mapType(object);
				}

				if (effectiveTypes.add(remapType)) {

					// the effective types of the selection
					if (appliesToEffectiveType(descriptor, remapType) == false) {
						return false;
					}
				}
			}
		} else {
			/* Bug 245690 selection is not a IStructuredSelection */
			if (descriptor.getFilter() != null) {
				return descriptor.getFilter().select(selection);
			}
		}

		return true;
	}

	private boolean appliesToEffectiveType(ISectionDescriptor descriptor,
			Class<?> inputClass) {

		List<String> classTypes = getClassTypes(inputClass);

		List<String> sectionInputTypes = descriptor.getInputTypes();
		for (String type : sectionInputTypes) {
			if (classTypes.contains(type)) {
				// found a match
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the classes and interfaces the given target class
	 * extends/implements.
	 */
	protected List<String> getClassTypes(Class<?> target) {
		List<String> result = new ArrayList<String>();
		// add classes
		List<Class<?>> classes = computeClassOrder(target);
		for (Class<?> i : classes) {
			result.add(i.getName());
		}
		// add interfaces
		result.addAll(computeInterfaceOrder(classes));
		return result;
	}

	private List<Class<?>> computeClassOrder(Class<?> target) {
		List<Class<?>> result = new ArrayList<Class<?>>(4);
		Class<?> clazz = target;
		while (clazz != null) {
			result.add(clazz);
			clazz = clazz.getSuperclass();
		}
		return result;
	}

	private List<String> computeInterfaceOrder(List<Class<?>> classes) {
		List<String> result = new ArrayList<String>(4);
		Map<Class<?>, Class<?>> seen = new HashMap<Class<?>, Class<?>>(4);
		for (Class<?> iter : classes) {
			Class<?>[] interfaces = iter.getInterfaces();
			internalComputeInterfaceOrder(interfaces, result, seen);
		}
		return result;
	}

	private void internalComputeInterfaceOrder(Class<?>[] interfaces,
			List<String> result, Map<Class<?>, Class<?>> seen) {
		List<Class<?>> newInterfaces = new ArrayList<Class<?>>(seen.size());
		for (int i = 0; i < interfaces.length; i++) {
			Class<?> interfac = interfaces[i];
			if (seen.get(interfac) == null) {
				result.add(interfac.getName());
				seen.put(interfac, interfac);
				newInterfaces.add(interfac);
			}
		}
		for (Class<?> iter : newInterfaces) {
			internalComputeInterfaceOrder(iter.getInterfaces(), result, seen);
		}
	}
}
