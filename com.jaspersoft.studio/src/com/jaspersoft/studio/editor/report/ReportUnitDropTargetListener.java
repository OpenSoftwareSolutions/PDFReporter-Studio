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
import java.util.List;

import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JRDesignHyperlinkParameter;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.dnd.DropTargetEvent;

import com.jaspersoft.studio.editor.gef.parts.ImageFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.text.TextFieldFigureEditPart;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog.ParameterDTO;

/**
 * Listener for the drop of a report unit into an element, put the unit informations
 * into the hyperlink section
 * 
 * @author Orlandin Marco
 *
 */
public class ReportUnitDropTargetListener extends AbstractTransferDropTargetListener{
	
	
	public ReportUnitDropTargetListener(EditPartViewer viewer){
		super(viewer, UnitTransfer.getInstance());
	}

	@Override
	protected void updateTargetRequest() {
	}
	

	/**
	 * Updates the target EditPart.
	 */
	protected void updateTargetEditPart() {
		setTargetEditPart(calculateTargetEditPart());
	}
	
	/**
	 * Override of the leave because the for some reason
	 * SWT call it before the drop action when the mouse button is 
	 * released. and this normally call the unload (removed from the override)
	 * that set the target to null
	 */
	public void dragLeave(DropTargetEvent event) {
		setCurrentEvent(event);
	}
	
	/**
	 * Generate a designhyperling parameter form the name of the parameter and the expression value treated 
	 * as a string
	 * 
	 * @param name name of the parameter
	 * @param expressionValue the string value to insert into the parameter, sicne it is a textual value quotes are 
	 * automatically added
	 * @return a designhyperling parameter
	 */
	private static JRDesignHyperlinkParameter generateParameter(String name, String expressionValue){
		JRDesignHyperlinkParameter newParam = new JRDesignHyperlinkParameter();
    newParam.setName(name);
    newParam.setValueExpression(new JRDesignExpression("\""+expressionValue+"\""));
    return newParam;
	}
	
	/**
	 * Drop action, get a list of string from the event, where the first position it the name of the unit and the other
	 * are the parameters of the unit, using this set the parameter inside the element model
	 */
	@Override
	protected void handleDrop() {
		if (getTargetEditPart() instanceof TextFieldFigureEditPart || getTargetEditPart() instanceof ImageFigureEditPart){
		  APropertyNode textField = (APropertyNode)getTargetEditPart().getModel();
		  if (textField != null){
		  	ParameterDTO parameters = (ParameterDTO)textField.getPropertyValue(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS);
		    if (parameters.getValue()== null){
		    	parameters.setValue(new JRHyperlinkParameter[0]);
		    }
		    //JRHyperlinkParameter[] params = parameters.getValue();
		    List<JRHyperlinkParameter> newParams = new ArrayList<JRHyperlinkParameter>();
		    //newParams.addAll(Arrays.asList(params));
		    String[] dropParameters = (String[])getCurrentEvent().data;
		    
		    newParams.add(generateParameter("_report", dropParameters[0]));
		    for(int i=1; i<dropParameters.length;i++){
		    	newParams.add(generateParameter(dropParameters[i], ""));
		    }
		    parameters.setValue(newParams.toArray(new JRHyperlinkParameter[newParams.size()]));
		    textField.setPropertyValue(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, parameters);
		    textField.setPropertyValue(JRDesignHyperlink.PROPERTY_LINK_TYPE, "ReportExecution");
		  }
		}
	}

	/**
	 * Find the edit part to return using the mouse cursor actual position, and return it. But if and only if 
	 * it is a part that support the hyperlink section, otherwise it return null.
	 * 
	 * @return a reference to an editpart under the mouse curso, if any and if it support the hyperlink section, otherwise 
	 * null;
	 */
	protected EditPart calculateTargetEditPart() {
		EditPart ep = getViewer().findObjectAt(getDropLocation());
		if (ep instanceof TextFieldFigureEditPart || ep instanceof ImageFigureEditPart) {
			return ep;
		}
		return null;
	}
}
