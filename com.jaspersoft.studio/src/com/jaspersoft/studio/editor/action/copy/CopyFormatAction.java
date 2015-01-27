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
package com.jaspersoft.studio.editor.action.copy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * Action to copy the appearance of an element and that can be 
 * pasted then into one or more other elements
 * 
 * @author Orlandin Marco
 * 
 */
public class CopyFormatAction extends ACachedSelectionAction {

	/**
	 * List of the copied\pasted properties
	 */
  public static final String[] propertyNames = new String[] {
    JRBaseStyle.PROPERTY_BACKCOLOR,
    JRBaseStyle.PROPERTY_BLANK_WHEN_NULL,
    JRBaseStyle.PROPERTY_BOLD,
    JRBaseStyle.PROPERTY_FILL,
    JRBaseStyle.PROPERTY_FONT_NAME,
    JRBaseStyle.PROPERTY_FONT_SIZE,
    JRBaseStyle.PROPERTY_FORECOLOR,
    JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT,
    JRBaseStyle.PROPERTY_ITALIC,
    JRBaseStyle.PROPERTY_MARKUP,
    JRBaseStyle.PROPERTY_MODE,
    JRBaseStyle.PROPERTY_PATTERN,
    JRBaseStyle.PROPERTY_PDF_EMBEDDED,
    JRBaseStyle.PROPERTY_PDF_ENCODING,
    JRBaseStyle.PROPERTY_PDF_FONT_NAME,
    JRBaseStyle.PROPERTY_RADIUS,
    JRBaseStyle.PROPERTY_ROTATION,
    JRBaseStyle.PROPERTY_SCALE_IMAGE,
    JRBaseStyle.PROPERTY_STRIKE_THROUGH,
    JRBaseStyle.PROPERTY_UNDERLINE,
    JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT,
    JRDesignElement.PROPERTY_PARENT_STYLE
  };
  
  /**
   * Map of the values copied for every property
   */
  private static HashMap<String, Object> copiedValues = null;
  
  public static final String ID = "CopyFormatAction"; //$NON-NLS-1$
	
	public CopyFormatAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}
	
	@Override
	protected void init() {
		super.init();
		setText(Messages.CopyFormatAction_title);
		setId(ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/copy_format.png")); //$NON-NLS-1$
		setEnabled(false);
	}

	@Override
	public void run() {
		List<APropertyNode> nodes = getNodes();
		if (nodes.size() == 1){
			copiedValues = new HashMap<String, Object>();
			APropertyNode node = nodes.get(0);
			for(String property : propertyNames){
				copiedValues.put(property, node.getPropertyActualValue(property));
			}
		}
	}


	/**
	 * Return the list of APropertyNode inside the selection
	 * 
	 * @param selectedObjects the actual selection
	 * @return a not null list of APropertyNode
	 */
	protected List<APropertyNode> getNodes() {
		List<APropertyNode> result = new ArrayList<APropertyNode>();
		List<Object> graphicalElements = editor.getSelectionCache().getSelectionModelForType(MGraphicElement.class);
		if (graphicalElements.isEmpty())
			return result;
		for (Object it : graphicalElements) {
			// Before to add an element it is checked if its nested, this is done to avoid to copy twice an element because
			// it is also directly selected with also its container (ie a frame) selected
			result.add((APropertyNode)it);
		}
		return result;
	}
	
	/**
	 * The action is enabled only if there are exactly one node into the selection
	 */
	@Override
	protected boolean calculateEnabled() {
		return getNodes().size() == 1;
	}
	
	/**
	 * Check if the properties of an element were copied
	 * 
	 * @return true if the properties were copied, false otherwise
	 */
	public static boolean hasCopiedValues(){
		return copiedValues != null;
	}
	
	/**
	 * Return a value of a property copied by an element 
	 * 
	 * @param key key of the property
	 * @return value of the property, can be null
	 */
	public static Object getCopiedValues(String key){
		return copiedValues.get(key);
	}
}
