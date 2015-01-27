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
package com.jaspersoft.studio.components.section.name;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.jasperreports.engine.base.JRBaseElement;

import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Represent a subeditor that can change its name according with the name
 * of the managed model
 * 
 * @author Orlandin Marco
 *
 */
public abstract class NamedSubeditor extends AbstractVisualEditor {

	/**
	 * Listneer to update the editor name when some properties of the managed
	 * element changes
	 */
	protected PropertyChangeListener mListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			setupPartName();
		}
	};
	
	public NamedSubeditor(JasperReportsConfiguration jrContext) {
		super(jrContext);
	}
	
	/**
	 * When the model is set on the editor the listener is added to it and removed from the old one
	 * (if present)
	 */
	@Override
	public void setModel(INode model) {
		INode oldModel = getModel();
		if (oldModel != null && oldModel instanceof MRoot && oldModel.getChildren().size() > 0) {
			INode n = oldModel.getChildren().get(0);
			n.getPropertyChangeSupport().removePropertyChangeListener(mListener);
		}
		super.setModel(model);
		if (model != null && model instanceof MRoot && model.getChildren().size() > 0) {
			INode n = model.getChildren().get(0);
			n.getPropertyChangeSupport().addPropertyChangeListener(mListener);
		}
		setupPartName();
	}
	
	/**
	 * Update the name of the editor, if it is present on the element uses that one, otherwise
	 * a default name for the editor is used
	 */
	protected void setupPartName() {
		ANode node = getEditedNode();
		if (node != null && node.getValue() instanceof JRBaseElement) {
			JRBaseElement el = (JRBaseElement)node.getValue();
			String name = el.getPropertiesMap().getProperty(NameSection.getNamePropertyId(node));
			if (!Misc.isNullOrEmpty(name)) {
				setPartName(name);
				return;
			}
		}
		setPartName(getDefaultPartName());
	}
	
	/**
	 * Return the default name for the editor, when the managed model
	 * has not a name
	 * 
	 * @return a name for the editor
	 */
	public abstract String getDefaultPartName();
	
	/**
	 * Return the node edited inside the editor
	 * 
	 * @return node edited inside the editor
	 */
	public abstract ANode getEditedNode();
	
}
