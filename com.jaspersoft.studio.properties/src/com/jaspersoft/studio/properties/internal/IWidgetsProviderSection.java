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

import java.util.List;

/**
 * This interface must be implemented by a section if it expose the handled properties.
 * In this way the section can be inspected to search a specific property
 * 
 * @author Orlandin Marco
 *
 */
public interface IWidgetsProviderSection {
	
	/**
	 * Return the actually selected element
	 * 
	 */
	public Object getSelectedElement();
	
	/**
	 * Return a list of all the properties managed by the section
	 * 
	 * @return a not null list of all the property ids of the managed properties
	 */
	public List<Object> getHandledProperties();
	
	/**
	 * Return a widget associated with a specific property id, the widget must implement
	 * the interface to be highlighted
	 * 
	 * @param propertyId the id of the property
	 * @return the widget to hightlight, can be null
	 */
	public IHighlightPropertyWidget getWidgetForProperty(Object propertyId);
	
	/**
	 * Return the descriptor for a specific widget
	 * 
	 * @param propertyId id of the property associated to the widget
	 * @return descriptor for the widget, could be null
	 */
	public WidgetDescriptor getPropertyInfo(Object propertyId);
	
	/**
	 * Since a section could have its element not visible (for example because 
	 * they are hidden into and expandable composite), this method is called 
	 * to make and element with a specific id visible. In the case of a expandable
	 * composite this means to expand it
	 * 
	 * @param propertyId the id of the element to unhide, if it is hidden
	 */
	public void expandForProperty(Object propertyId);
}
