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
package com.jaspersoft.studio.editor.java2d;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import com.jaspersoft.studio.editor.gef.selection.JSelectionManager;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
/*
 * The Class J2DScrollingGraphicalViewer.
 */
public class J2DScrollingGraphicalViewer extends ScrollingGraphicalViewer {

	public J2DScrollingGraphicalViewer(){
		super();
		setSelectionManager(new JSelectionManager());
	}
	
	/**
	 * Internally creates a J2DLightweightSystem.
	 * 
	 * @return the lightweight system
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#createLightweightSystem()
	 */
	protected LightweightSystem createLightweightSystem() {
		return new J2DLightweightSystem();
	}
	
	/**
	 * This override avoid to autocenter the element on selection when the option
	 * is disable by the preferences
	 */
	@Override
	public void reveal(EditPart part){
		JasperReportsConfiguration jrContext = null;
		if (getContents() != null && getContents().getModel() instanceof ANode){
			jrContext = ((ANode)getContents().getModel()).getJasperConfiguration();
		}
		//Unable to get the JasperConfiguration, uses the default fallback
		if (jrContext == null){
			jrContext = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), null);
		}
		if (jrContext.getPropertyBoolean(DesignerPreferencePage.P_CENTER_SELECTION, Boolean.TRUE)) {
			super.reveal(part);
		}
	}

}
