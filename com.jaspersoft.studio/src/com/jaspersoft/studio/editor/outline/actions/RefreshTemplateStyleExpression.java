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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.ExternalStylesManager;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MStyleTemplate;

/**
 * Action to reload an external template style, this will also re-evaluate its expression
 * 
 * @author Orlandin Marco
 *
 */
public class RefreshTemplateStyleExpression extends ACachedSelectionAction {

	/** The Constant ID. */
	public static final String ID = "refresh_template_style_expression"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public RefreshTemplateStyleExpression(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.RefreshTemplateStyleExpression_title);
		setToolTipText(Messages.RefreshTemplateStyleExpression_tooltip);
		setId(RefreshTemplateStyleExpression.ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/refresh_style_action.png")); //$NON-NLS-1$
		setEnabled(false);
	}

	/**
	 * Enable only if there is at least one style that can be exported
	 */
	@Override
	protected boolean calculateEnabled() {
		return !getSelectedStyles().isEmpty();
	}

	@Override
	public void run() {
		List<MStyleTemplate> templates = getSelectedStyles();
		for(MStyleTemplate template : templates){
			ExternalStylesManager.refreshStyle(template);
			//Need to manually refresh the child nodes
			template.refreshChildren();
		}
	}
	
	/**
	 * Return the list of all the selected Template styles. 
	 * 
	 * @return a not null list of MStyleTemplate
	 */
	private List<MStyleTemplate> getSelectedStyles(){
		List<Object> templates = editor.getSelectionCache().getSelectionModelForType(MStyleTemplate.class);
		List<MStyleTemplate> result = new ArrayList<MStyleTemplate>();
		for (Object template : templates){
			result.add((MStyleTemplate)template);
		}
		return result;
	}
}
