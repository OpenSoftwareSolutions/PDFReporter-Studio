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
package com.jaspersoft.studio.editor.action.image;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.action.IGlobalAction;
import com.jaspersoft.studio.jface.dialogs.ImageSelectionDialog;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.image.MImage;

public class ChangeImageExpression  extends ACachedSelectionAction implements IGlobalAction {

	private MImage imageModel;
	
	private final static String ID = "ActionImageChangeExpression";
	
	public ChangeImageExpression(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(Messages.ImageContributionItem_actionName);
		setToolTipText(Messages.ImageContributionItem_actionName);
		loadImageModel();
	}
	
	private void loadImageModel(){
		imageModel = null;
		List<Object> images = editor.getSelectionCache().getSelectionModelForType(MImage.class); 
		if (!images.isEmpty()){
			imageModel = (MImage) images.get(0);
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return (imageModel != null);
	}
	
	@Override
	protected void setSelection(ISelection selection) {
		super.setSelection(selection);
		loadImageModel();
	}
	
	public static void setImageExpression(MImage imageModel){
		if (imageModel != null){
			ImageSelectionDialog d=new ImageSelectionDialog(UIUtils.getShell());
			d.configureDialog(imageModel.getJasperConfiguration());
			if(d.open()==Window.OK) {
				JRDesignExpression imageExpression = d.getImageExpression();
				if(imageExpression==null){
					// No image selected => remove property
					imageModel.setPropertyValue(JRDesignImage.PROPERTY_EXPRESSION, "");
				}
				else {
					imageModel.setPropertyValue(JRDesignImage.PROPERTY_EXPRESSION, imageExpression.getText());
				}
			}
		}
	}
	
	@Override
	public void run() {
		setImageExpression(imageModel);
	}

}
