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
package com.jaspersoft.studio.editor.style;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.figures.ImageFigure;
import com.jaspersoft.studio.editor.gef.figures.StaticTextFigure;
import com.jaspersoft.studio.editor.gef.figures.borders.CornerBorder;
import com.jaspersoft.studio.editor.gef.figures.borders.ElementLineBorder;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.style.editpolicy.ElementEditPolicy;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.model.text.MStaticText;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;

public class StyleEditPart extends FigureEditPart {

	private StaticTextFigure textF;
	private ImageFigure imageF;
	private JRDesignStaticText textE;
	private MStaticText textModel;
	private JRDesignImage imageE;
	private MImage imageModel;
	private GridData gd;
	
	public StyleEditPart() {
		super();
	}

	@Override
	protected IFigure createFigure() {
		RectangleFigure rf = new RectangleFigure();
		rf.setBorder(new LineBorder(ColorConstants.lightGray));
		GridLayout lm = new GridLayout(2, false);
		lm.marginHeight = 20;
		lm.marginWidth = 20;
		lm.horizontalSpacing = 20;
		rf.setLayoutManager(lm);

		MStyle st = (MStyle) getModel();
		JRStyle style = (JRStyle) st.getValue();
		
		textModel = new MStaticText();
		textE = new JRDesignStaticText();
		textE.setX(20);
		textE.setY(20);
		textE.setWidth(200);
		textE.setHeight(100);
		textE.setText(getStylePartText(style.getName()));
		textE.setStyle(style);
		textModel.setValue(textE);

		imageModel = new MImage();
		imageE = new JRDesignImage(null);
		imageE.setX(textE.getX() * 2 + textE.getWidth());
		imageE.setY(textE.getY());
		imageE.setWidth(100);
		imageE.setHeight(textE.getHeight());
		imageE.setStyle(style);
		imageModel.setValue(imageE);

		rf.setSize(textE.getX() * 3 + textE.getWidth() + imageE.getWidth(), textE.getY() * 2 + textE.getHeight());
		
		textF = new StaticTextFigure(textModel);
		textF.setJRElement(textE, drawVisitor);

		imageF = new ImageFigure(imageModel);
		imageF.setJRElement(imageE, drawVisitor);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = textE.getHeight() + 20;
		gd.widthHint = textE.getWidth() + 5;
		lm.setConstraint(textF, gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = imageE.getHeight() + 20;
		gd.widthHint = imageE.getWidth() + 5;
		lm.setConstraint(imageF, gd);

		rf.add(textF);
		rf.add(imageF);
		setPrefsBorder(rf);
		
		//Event to refresh the figure when a style attributed is changed
		st.getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if (JRDesignStyle.PROPERTY_NAME.equals(arg0.getPropertyName())){
					textE.setText(getStylePartText(arg0.getNewValue() != null ? arg0.getNewValue() : ""));
				}
				imageModel.setChangedProperty(true);
				textModel.setChangedProperty(true);
				if (getParent() != null) {
					refresh();
				} else {
					//This edit part was removed, delete the listener for that editpart from the model
					getModel().getPropertyChangeSupport().removePropertyChangeListener(this);
				}
			}
		});
		
		return rf;
	}
	
	/**
	 * Return the composed string to use as name for the edit part
	 * 
	 * @param styleName the name of the style
	 * @return text to draw inside the style edit part
	 */
	private String getStylePartText(Object styleName){
		return MessageFormat.format(Messages.StyleEditPart_styleTemplatePrefix, new Object[]{styleName});
	}

	public void setPrefsBorder(IFigure rect) {
		String pref = Platform.getPreferencesService().getString(JaspersoftStudioPlugin.getUniqueIdentifier(),
				DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_STYLE, "rectangle", null); //$NON-NLS-1$

		if (pref.equals("rectangle")) { //$NON-NLS-1$
			imageF.setBorder(new ElementLineBorder(ColorConstants.black));
			textF.setBorder(new ElementLineBorder(ColorConstants.black));
		} else {
			imageF.setBorder(new CornerBorder(ColorConstants.black, 5));
			textF.setBorder(new CornerBorder(ColorConstants.black, 5));
		}
	}

	@Override
	protected void setupFigure(IFigure rect) {
		LayoutManager lm = rect.getParent().getLayoutManager();
		Rectangle b = rect.getBounds();
		if (gd == null) {
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.heightHint = b.height;
			gd.widthHint = b.width;
			lm.setConstraint(rect, gd);
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementEditPolicy());
	}

}
