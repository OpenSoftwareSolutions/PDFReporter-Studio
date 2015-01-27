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
package com.jaspersoft.studio.editor.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * This class can be used to filter the selection and store the result. So the 
 * following request until the selection change can get the result from the cache
 * 
 * @author Orlandin Marco
 *
 */
public class CommonSelectionCacheProvider {
	
	/**
	 * Class to keep the association for N edit part
	 * and the relative N models
	 * 
	 * @author Orlandin Marco
	 *
	 */
	public class ModelPartPair{
		
		/**
		 * List of the models inside the edit part
		 */
		private List<Object> models = new ArrayList<Object>();
		
		/**
		 * List for the edit part
		 */
		private List<EditPart> parts = new ArrayList<EditPart>();
		
		/**
		 * Add an edit part and its model to the lists
		 * 
		 * @param part a not null edit part
		 */
		public void add(EditPart part){
			models.add(part.getModel());
			parts.add(part);
		}
		
		/**
		 * Clear the stored infromation
		 */
		public void clear(){
			models.clear();
			parts.clear();
		}
	}
	
	/**
	 * When an element request a subset of the selection of a specific type the result
	 * is cached to allow for the future request until the selection changes. 
	 * In this way the request is processed once to search the elements of a specific type
	 * and any other request for the same type will be returned from the cache
	 */
	private Map<Class<?>, ModelPartPair> cachedModelTypedRequest = new HashMap<Class<?>, ModelPartPair>();
	
	/**
	 * Do the same of the cachedModelTypedRequest, but keep cached the list of edit part insted of model
	 */
	private Map<Class<?>, List<Object>> cachedEditPartTypedRequest = new HashMap<Class<?>, List<Object>>();
	
	/**
	 * List of the models inside the edit part of the last selection
	 */
	private ModelPartPair lastModelSelection = new ModelPartPair();
	
	/**
	 * The last selection
	 */
	private List<Object> lastEditPartSelection = new ArrayList<Object>();
	
	/**
	 * List of selection listeners, called when the selection changes
	 */
	private List<SelectionChangedListener> selectionChangeListeners = new ArrayList<SelectionChangedListener>();
	
	/**
	 * Flag to control how the request for a type should return a result only if all the elements 
	 * inside the selection are the requested type or a subclass of the requested type. 
	 */
	public boolean allowDishomogeneousSelection = false;
	
	/**
	 * The last selection done
	 */
	private ISelection lastSelection = null;
	
	
	/**
	 * Called when the request for a type is not in the cache. It will
	 * resolve the request by iterating the elements in the last selection,
	 * searching for the requested type. The result will be cached and returned
	 * 
	 * @param type the selected type
	 * @return a not null list of the elements of the desired type or of one
	 * of its subclasses
	 */
	private ModelPartPair createCacheForModelType(Class<?> type){
		ModelPartPair result = new ModelPartPair();
		for(EditPart part : lastModelSelection.parts){
			if (type.isInstance(part.getModel())){
				result.add(part);
			} else if (!allowDishomogeneousSelection){
				return new ModelPartPair();
			}
		}
		return result;
	}
	
	private List<Object> createCacheForEditPartType(Class<?> type){
		List<Object> result = new ArrayList<Object>();
		for(Object obj : lastEditPartSelection){
			if (type.isInstance(obj)){
				result.add(obj);
			} else if (!allowDishomogeneousSelection){
				return new ArrayList<Object>();
			}
		}
		return result;
	}
	
	/**
	 * Return a list of object of a specific type inside the selection
	 * If the allowDishomogeneousSelection is false (default value) then
	 * all the element inside the selection must be of the requested type
	 * or a subclass of it
	 * 
	 * @param type the desired type, should be a model
	 * @return a not null list of the elements of the desired type or of one
	 * of its subclasses
	 */
	public List<Object> getSelectionModelForType(Class<?> type){
		ModelPartPair cachedValue = cachedModelTypedRequest.get(type);
		if (cachedValue == null){
			cachedValue = createCacheForModelType(type);
			cachedModelTypedRequest.put(type, cachedValue);
		}
		return cachedValue.models;
	}
	
	/**
	 * Return a list of EditPart with the model object of a specific type inside the selection
	 * If the allowDishomogeneousSelection is false (default value) then
	 * all the element inside the selection must be of the requested type
	 * or a subclass of it
	 * 
	 * @param type the desired type, should be a model
	 * @return a not null list of the elements of EditPart with as relative model or of one
	 * of its subclasses
	 */
	public List<EditPart> getSelectionModelPartForType(Class<?> type){
		ModelPartPair cachedValue = cachedModelTypedRequest.get(type);
		if (cachedValue == null){
			cachedValue = createCacheForModelType(type);
			cachedModelTypedRequest.put(type, cachedValue);
		}
		return cachedValue.parts;
	}
	
	/**
	 * Return a list of object of a specific type inside the selection
	 * If the allowDishomogeneousSelection is false (default value) then
	 * all the element inside the selection must be of the requested type
	 * or a subclass of it
	 * 
	 * @param type the desired type, should be an EditPart
	 * @return a not null list of the elements of the desired type or of one
	 * of its subclasses
	 */
	public List<Object> getSelectionPartForType(Class<?> type){
		List<Object> cachedValue = cachedEditPartTypedRequest.get(type);
		if (cachedValue == null){
			cachedValue = createCacheForEditPartType(type);
			cachedEditPartTypedRequest.put(type, cachedValue);
		}
		return cachedValue;
	}
	
	/**
	 * Clear the cache maps and reprocess the edit parts
	 */
	private void reinitializeMaps(){
		IStructuredSelection sSel = (IStructuredSelection)lastSelection;
		Iterator<?> elements = sSel.iterator();
		lastModelSelection.clear();
		cachedModelTypedRequest.clear();
		lastEditPartSelection.clear();
		cachedEditPartTypedRequest.clear();
		while (elements.hasNext()) {
			Object obj = elements.next();
			if(obj instanceof EditPart) {
				EditPart editPart = (EditPart) obj;
				lastModelSelection.add(editPart);
				lastEditPartSelection.add(editPart);
			}
		}
	}
	
	/**
	 * Allow or not Dishomogeneous selection. When it is false the request for a type should return 
	 * a result only if all the elements inside the selection are the requested type or a subclass of the requested type. 
	 * This method clear the cache when this flag changes
	 * 
	 * @param isAllowed true is the Dishomogeneous selection are allowed, false otherwise
	 */
	public void setAllowingDishomogeneousSelection(boolean isAllowed){
		if (allowDishomogeneousSelection != isAllowed){
			allowDishomogeneousSelection = isAllowed;
			reinitializeMaps();
		}
	}
	
	/**
	 * Return the last selection
	 * 
	 * @return the last selection set in the cache
	 */
	public ISelection getLastRawSelection(){
		return lastSelection;
	}
	
	/**
	 * Method that must be called when the selection changes. If the selection
	 * it the same of the previous one it dosen't do anything, otherwise it 
	 * clear the cache
	 * 
	 * @param selection the new selection, must be not null and an instance of
	 * structured selection
	 */
	public void selectionChanged(ISelection selection) {
		if (!selection.equals(lastSelection) && selection instanceof IStructuredSelection){
			lastSelection = selection;
			reinitializeMaps();
			//Call the listener
			for(SelectionChangedListener listener : selectionChangeListeners){
				listener.selectionChanged();
			}
		}
	}
	
	/**
	 * Add a listener called when the selection change. The same listener can be 
	 * added only once
	 * 
	 * @param listener the listener
	 * @return true if the listener was added, false it not (because the same listener
	 * was already added before).
	 */
	public boolean addSelectionChangeListener(SelectionChangedListener listener){
		if (!selectionChangeListeners.contains(listener)){
			selectionChangeListeners.add(listener);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove a previously added listener
	 *
	 * @param listener the listener to remove
	 * @return true if the listener was found and removed, false otherwise
	 */
	public boolean removeSelectionChangeListener(SelectionChangedListener listener){
		return selectionChangeListeners.remove(listener);
	}
}
