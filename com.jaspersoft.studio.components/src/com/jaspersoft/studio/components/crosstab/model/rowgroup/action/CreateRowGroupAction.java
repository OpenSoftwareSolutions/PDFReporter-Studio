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
package com.jaspersoft.studio.components.crosstab.model.rowgroup.action;

import java.util.ArrayList;

import net.sf.jasperreports.engine.design.JRDesignStyle;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.dialog.ApplyCrosstabStyleAction;
import com.jaspersoft.studio.components.crosstab.model.rowgroup.MRowGroup;
import com.jaspersoft.studio.editor.outline.actions.ACreateAction;
import com.jaspersoft.studio.editor.palette.JDPaletteCreationFactory;
/*
 * The Class CreateGroupAction.
 */
public class CreateRowGroupAction extends ACreateAction {

	/** The Constant ID. */
	public static final String ID = "create_rowgroup"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public CreateRowGroupAction(IWorkbenchPart part) {
		super(part);
		setCreationFactory(new JDPaletteCreationFactory(MRowGroup.class));
	}
	
	@Override
	public void run() {
		super.run();
		MCrosstab crosstab = null;
		Object selected = getSelectedObjects().get(0);
		if (selected instanceof EditPart) {
			EditPart part = (EditPart) selected;
			if (part.getModel() instanceof MCrosstab) crosstab = (MCrosstab)part.getModel();
		} 
		if (crosstab != null){
			ApplyCrosstabStyleAction applyStyle = new ApplyCrosstabStyleAction(new ArrayList<JRDesignStyle>(), crosstab.getValue());
			applyStyle.rebuildStylesFromCrosstab();
			applyStyle.applayStyle(crosstab.getJasperDesign());
		}
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.CreateRowGroupAction_create_row_group);
		setToolTipText(Messages.CreateRowGroupAction_create_row_group_tool_tip);
		setId(CreateRowGroupAction.ID);
		setImageDescriptor(Activator.getDefault().getImageDescriptor("icons/add-crosstabrows-16.png"));
		setDisabledImageDescriptor(Activator.getDefault().getImageDescriptor("icons/add-crosstabrows-16.png"));
		setEnabled(false);
	}

}
