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
package com.jaspersoft.studio.editor.outline.actions;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.editor.palette.JDPaletteCreationFactory;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * /* The Class CreateConditionalStyleAction.
 */
public class CreateConditionalStyleAction extends ACreateAndSelectAction {

	/** The Constant ID. */
	public static final String ID = "create_conditional_style"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action	
	 */
	public CreateConditionalStyleAction(IWorkbenchPart part) {
		super(part);
		setCreationFactory(new JDPaletteCreationFactory(MConditionalStyle.class));
		setLazyEnablementCalculation(true);
	}
	
	@Override
	protected boolean calculateEnabled() {
		// Strict check on MStyle. 
		// Its subclass MConditionalStyle is not allowed.
		if(!checkAllSelectedObjects(MStyle.class) || 
				!ModelUtils.checkTypesForAllEditParModels(getSelectedObjects(),false,MConditionalStyle.class)) {
			return false;
		}
		return super.calculateEnabled();
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.CreateConditionalStyleAction_create_conditional_style);
		setToolTipText(Messages.CreateConditionalStyleAction_create_conditional_style_tool_tip);
		setId(CreateConditionalStyleAction.ID);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED));
		setEnabled(false);
	}

}
