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
package com.jaspersoft.studio.editor.gef.parts;

import java.util.List;

import org.eclipse.gef.EditPart;

import com.jaspersoft.studio.callout.CalloutEditPart;
import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.callout.pin.MPin;
import com.jaspersoft.studio.callout.pin.PinEditPart;
import com.jaspersoft.studio.editor.AEditPartFactory;
import com.jaspersoft.studio.editor.gef.parts.band.BandEditPart;
import com.jaspersoft.studio.editor.gef.parts.text.StaticTextFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.text.TextFieldFigureEditPart;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.model.text.MStaticText;
import com.jaspersoft.studio.model.text.MTextField;

/*
 * A factory for creating JasperDesignEditPart objects.
 * 
 * @author Chicu Veaceslav
 */
public class JasperDesignEditPartFactory extends AEditPartFactory {
	@Override
	protected EditPart createEditPart(Object model) {
		EditPart editPart = null;
		if (model instanceof MRoot) {
			List<INode> children = ((MRoot) model).getChildren();
			if (children != null && !children.isEmpty() && children.get(0) instanceof MReport)
				editPart = new ReportPageEditPart();
			else
				editPart = new PageEditPart();
		} else if (model instanceof MPage)
			editPart = new PageEditPart();
		else if (model instanceof MReport)
			editPart = new ReportPageEditPart();
		else if (model instanceof MBand)
			editPart = new BandEditPart();
		else if (model instanceof MStaticText)
			editPart = new StaticTextFigureEditPart();
		else if (model instanceof MTextField)
			editPart = new TextFieldFigureEditPart();
		else if (model instanceof MSubreport)
			editPart = new SubreportFigureEditPart();
		else if (model instanceof MImage)
			editPart = new ImageFigureEditPart();
		else if (model instanceof MFrame)
			editPart = new FrameFigureEditPart();

		else if (model instanceof MCallout)
			editPart = new CalloutEditPart();
		else if (model instanceof MPin)
			editPart = new PinEditPart();
		else if (model instanceof IGraphicElement)
			editPart = new FigureEditPart();

		return editPart;
	}

}
