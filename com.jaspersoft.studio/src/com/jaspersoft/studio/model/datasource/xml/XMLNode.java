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
package com.jaspersoft.studio.model.datasource.xml;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.model.ANode;

/**
 * ANode representing an XML document node.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class XMLNode extends ANode {

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private String nodeName;
	private String xpathExpression;

	public ImageDescriptor getImagePath() {
		return ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/resources/element_obj.gif");
	}

	public String getDisplayText() {
		return this.nodeName;
	}

	public String getName(){
		return this.nodeName;
	}
	
	public void setName(String nodeName){
		this.nodeName=nodeName;
	}
	
	public String getXPathExpression(){
		return this.xpathExpression;
	}
	
	public void setXPathExpression(String xpathExp){
		this.xpathExpression=xpathExp;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if(adapter==JRDesignField.class || adapter==JRField.class){
			JRDesignField field=new JRDesignField();
			field.setName(nodeName);
			field.setDescription(xpathExpression);
			field.setValueClass(String.class);
			return field;
		}
		return super.getAdapter(adapter);
	}
	
}
