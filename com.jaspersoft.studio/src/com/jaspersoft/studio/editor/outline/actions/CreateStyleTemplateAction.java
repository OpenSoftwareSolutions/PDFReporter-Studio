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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.editor.palette.JDPaletteCreationFactory;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MStyleTemplate;
import com.jaspersoft.studio.model.style.MStyles;

/*
 * The Class CreateStyleTemplateAction.
 */
public class CreateStyleTemplateAction extends ACreateAndSelectAction {

	/** The Constant ID. */
	public static final String ID = "create_style_template"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public CreateStyleTemplateAction(IWorkbenchPart part) {
		super(part);
		setCreationFactory(new JDPaletteCreationFactory(MStyleTemplate.class));
	}
	
	@Override
	protected boolean calculateEnabled() {
		if(!checkSingleSelectedObject(MStyles.class)){
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
		setText(Messages.CreateStyleTemplateAction_create_style_template);
		setToolTipText(Messages.CreateStyleTemplateAction_create_style_template_tool_tip);
		setId(CreateStyleTemplateAction.ID);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED));
		setEnabled(false);
	}

	@Override
	public void run() {
		super.run();
		ISelection s = getSelection();
		if (s instanceof StructuredSelection) {
			Object obj = ((StructuredSelection) s).getFirstElement();
			if (obj instanceof EditPart) {
				EditPart editPart = (EditPart) obj;
				List<?> children = editPart.getParent().getChildren();
				if (children != null && !children.isEmpty()) {
					int last = 0;
					StructuredSelection newselection = new StructuredSelection(children.get(last));
					setSelection(newselection);
					getWorkbenchPart().getSite().getSelectionProvider().setSelection(newselection);
				}
			}
		}
	}
}
