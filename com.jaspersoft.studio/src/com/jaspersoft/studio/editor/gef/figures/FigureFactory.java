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
package com.jaspersoft.studio.editor.gef.figures;

import net.sf.jasperreports.engine.JRComponentElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.callout.CalloutFigure;
import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MEllipse;
import com.jaspersoft.studio.model.MLine;
import com.jaspersoft.studio.model.MRectangle;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.model.genericElement.MGenericElement;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.model.text.MStaticText;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.plugin.ExtensionManager;

/*
 * A factory for creating Figure objects.
 * 
 * @author Chicu Veaceslav
 */
public class FigureFactory {

	/**
	 * Creates a new Figure object.
	 * 
	 * @param node
	 *          the node
	 * @return the i figure
	 */
	public static IFigure createFigure(final ANode node) {
		ExtensionManager m = JaspersoftStudioPlugin.getExtensionManager();
		IFigure f = m.createFigure(node);
		if (f != null)
			return f;
		if (node instanceof MEllipse)
			return new EllipseFigure((MEllipse)node);
		if (node instanceof MRectangle)
			return new RectangleFigure((MRectangle)node);
		if (node instanceof MStaticText)
			return new StaticTextFigure((MStaticText)node);
		if (node instanceof MTextField)
			return new TextFieldFigure((MTextField)node);
		if (node instanceof MLine)
			return new LineFigure((MLine)node);
		if (node instanceof MFrame)
			return new FrameFigure((MFrame)node);
		if (node instanceof MImage)
			return new ImageFigure((MImage)node);
		if (node instanceof MSubreport)
			return new SubreportFigure((MSubreport)node);
		if (node instanceof MGenericElement)
			return new GenericElementFigure((MGenericElement)node);
		if (node.getValue() instanceof JRComponentElement)
			return new ComponentFigure();
		if (node instanceof MCallout)
			return new CalloutFigure();
		else {
			org.eclipse.draw2d.RectangleFigure rfig = new org.eclipse.draw2d.RectangleFigure();
			rfig.setLayoutManager(new XYLayout());
			return rfig;
		}
	}

}
