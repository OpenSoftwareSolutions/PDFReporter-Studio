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
package com.jaspersoft.studio.property.section.report.action;

import java.util.List;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.gef.parts.ReportPageEditPart;
import com.jaspersoft.studio.editor.gef.parts.band.BandEditPart;
import com.jaspersoft.studio.editor.report.ReportEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.property.SetValueCommand;

public class PageRemoveMarginsAction extends ACachedSelectionAction {
	public static final String ID = "pageRemoveMarginsAction"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param diagramViewer
	 *          the GraphicalViewer whose grid enablement and visibility properties are to be toggled
	 */
	public PageRemoveMarginsAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	/**
	 * Initializes this action's text and images.
	 */
	protected void init() {
		super.init();
		setText(Messages.PageRemoveMarginsAction_actionName);
		setToolTipText(Messages.PageRemoveMarginsAction_actionTooltip);
		setId(ID);
		setEnabled(false);
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		ReportEditor part = (ReportEditor) getWorkbenchPart();
		MReport n = (MReport) part.getModel().getChildren().get(0);
		JasperDesign jd = n.getJasperDesign();

		JSSCompoundCommand c = new JSSCompoundCommand(getText(), n);
		c.add(createResetCommand(n, JasperDesign.PROPERTY_LEFT_MARGIN, 0));
		c.add(createResetCommand(n, JasperDesign.PROPERTY_RIGHT_MARGIN, 0));
		c.add(createResetCommand(n, JasperDesign.PROPERTY_TOP_MARGIN, 0));
		c.add(createResetCommand(n, JasperDesign.PROPERTY_BOTTOM_MARGIN, 0));

		int w = jd.getPageWidth() - jd.getLeftMargin() - jd.getRightMargin();
		int h = jd.getPageHeight() - jd.getTopMargin() - jd.getBottomMargin();

		c.add(createResetCommand(n, JasperDesign.PROPERTY_PAGE_WIDTH, w));
		c.add(createResetCommand(n, JasperDesign.PROPERTY_PAGE_HEIGHT, h));

		execute(c);
	}

	private Command createResetCommand(APropertyNode n, Object prop, int value) {
		SetValueCommand cmd = new SetValueCommand();
		cmd.setPropertyId(prop);
		cmd.setPropertyValue(value);
		cmd.setTarget(n);
		return cmd;
	}

	@Override
	protected boolean calculateEnabled() {
		if (getSelectedObjects().size()>1) return false;
		
		List<Object> pageEditPart = editor.getSelectionCache().getSelectionPartForType(ReportPageEditPart.class);
		if (pageEditPart.size() > 0) return true;
		
		List<Object> bandEditParts = editor.getSelectionCache().getSelectionPartForType(BandEditPart.class);
		if (bandEditParts.size() > 0) return true;

		return false;
	}
}
