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
package com.jaspersoft.studio.property;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.messages.MessagesByKeys;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.properties.view.ITypeMapper;
/*
 * Label provider for the title bar for the tabbed property sheet page.
 * 
 */
public class ElementLabelProvider extends LabelProvider {

	private ITypeMapper typeMapper;

	/**
	 * constructor.
	 */
	public ElementLabelProvider() {
		super();
		typeMapper = new ElementTypeMapper();
	}

	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object objects) {
		if (objects == null || objects.equals(StructuredSelection.EMPTY))
			return null;
		final boolean multiple[] = { false };
		Object object = getObject(objects, multiple);
		IIconDescriptor icon = null;
		if (object == null)
			icon = MGraphicElement.getIconDescriptor();
		else {
			ANode element = null;
			if (object instanceof EditPart) {
				element = (ANode) ((EditPart) object).getModel();
			} else if (object instanceof ANode) {
				element = (ANode) object;
			}
			if (element != null) {
				try {
					icon = (IIconDescriptor) element.getClass().getMethod("getIconDescriptor", new Class<?>[] {}) //$NON-NLS-1$
							.invoke(null, new Object[] {});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		if (icon != null)
			return JaspersoftStudioPlugin.getInstance().getImage(icon.getIcon16());
		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object objects) {
		if (objects == null || objects.equals(StructuredSelection.EMPTY)) {
			return Messages.ElementLabelProvider_no_items_selected;
		}
		final boolean multiple[] = { false };
		final Object object = getObject(objects, multiple);

		if (object == null || (objects instanceof IStructuredSelection && ((IStructuredSelection) objects).size() > 1)) {
			return ((IStructuredSelection) objects).size() + " " + Messages.ElementLabelProvider_items_selected; //$NON-NLS-1$
		} else {
			String name = typeMapper.mapType(object).getName();
			if (object instanceof EditPart) {
				ANode element = (ANode) ((EditPart) object).getModel();
				// Look for a custom title for the property sheet page (if possible)
				if(element instanceof APropertyNode && 
						((APropertyNode)element).getCustomPropertyTitle()!=null){
					return ((APropertyNode)element).getCustomPropertyTitle();
				}
				String str = MessagesByKeys.getString(name.substring(name.lastIndexOf('.') + 2));
				String displayText = element.getDisplayText();
				if(displayText!=null){
					displayText=displayText.replaceAll("(\\r|\\n)+", " ");//$NON-NLS-1$ //$NON-NLS-2$
					if(displayText.length() > 30)
						displayText = displayText.substring(0, 30)+"...";//$NON-NLS-1$
					return str + ": " + displayText; //$NON-NLS-1$
				}
			}
			return MessagesByKeys.getString(name.substring(name.lastIndexOf('.') + 1));
		}
	}

	/**
	 * Determine if a multiple object selection has been passed to the label provider. If the objects is a
	 * IStructuredSelection, see if all the objects in the selection are the same and if so, we want to provide labels for
	 * the common selected element.
	 * 
	 * @param objects
	 *          a single object or a IStructuredSelection.
	 * @param multiple
	 *          first element in the array is true if there is multiple unequal selected elements in a
	 *          IStructuredSelection.
	 * @return the object to get labels for.
	 */
	private Object getObject(Object objects, boolean multiple[]) {
		Assert.isNotNull(objects);
		Object object = null;
		if (objects instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) objects;
			object = selection.getFirstElement();
			if (selection.size() == 1) {
				// one element selected
				multiple[0] = false;
				return object;
			}
			// multiple elements selected
			multiple[0] = true;
			Class<?> firstClass = typeMapper.mapType(object);
			// determine if all the objects in the selection are the same type
			if (selection.size() > 1) {
				for (Iterator<?> i = selection.iterator(); i.hasNext();) {
					Object next = i.next();
					Class<?> nextClass = typeMapper.mapType(next);
					if (!nextClass.equals(firstClass)) {
						// two elements not equal == multiple selected unequal
						multiple[0] = false;
						object = null;
						break;
					}
				}
			}
		} else {
			multiple[0] = false;
			object = objects;
		}
		return object;
	}

}
