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
package com.jaspersoft.studio.editor;

import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.plugin.ExtensionManager;

public abstract class AEditPartFactory implements EditPartFactory {
	protected JSSDrawVisitor drawVisitor;
	protected JasperDesign jDesign;
	protected JasperReportsContext jrContext;

	public JSSDrawVisitor getDrawVisitor(ANode model) {
		if (model == null)
			return null;
		JasperDesign tjd = model.getJasperDesign();
		if (tjd != jDesign) {
			jDesign = tjd;
			drawVisitor = new JSSDrawVisitor(new ReportConverter(jrContext, jDesign, true), null);
		}
		if (drawVisitor == null)
			drawVisitor = new JSSDrawVisitor(new ReportConverter(jrContext, jDesign, true), null);
		drawVisitor.setClip(false);
		return drawVisitor;
	}

	protected void initContext(EditPart context, Object model) {
		if (model != null && model instanceof ANode)
			jrContext = ((ANode) model).getJasperConfiguration();
		if (jrContext == null && context != null) {
			EditPartViewer gv = context.getViewer();
			Object prop = gv.getProperty("JRCONTEXT");
			if (prop != null && prop instanceof JasperReportsContext) {
				jrContext = (JasperReportsContext) prop;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		initContext(context, model);

		ExtensionManager m = JaspersoftStudioPlugin.getExtensionManager();
		EditPart editPart = m.createEditPart(context, model);
		if (editPart == null)
			editPart = createEditPart(model);
		if (editPart != null) {
			editPart.setModel(model);
			if (editPart instanceof FigureEditPart)
				((FigureEditPart) editPart).setDrawVisitor(getDrawVisitor((ANode) model));
		}
		return editPart;
	}

	protected abstract EditPart createEditPart(Object model);
}
