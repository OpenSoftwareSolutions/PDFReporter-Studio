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
package com.jaspersoft.studio.components.chartspider.wizard.action;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chartspider.model.MSpiderChart;
import com.jaspersoft.studio.components.chartspider.model.command.EditSpiderChartCommand;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.frame.MFrame;

public class ChartSpiderWizardAction extends ACachedSelectionAction {
	public static final String ID = "chartspidereditaction"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *            The part for this action
	 */
	public ChartSpiderWizardAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}


	@Override
	public Command createCommand() {
		List<Object> spiderCharts = editor.getSelectionCache().getSelectionModelForType(MSpiderChart.class);
		
		if (spiderCharts.isEmpty())
			return null;
		
		for (Object spiderChart : spiderCharts) {
			MSpiderChart node = (MSpiderChart) spiderChart;
			INode parent = node.getParent();
			if (parent instanceof MFrame) return new EditSpiderChartCommand((MFrame) parent, node);
			if (parent instanceof MBand) return new EditSpiderChartCommand((MBand) parent, node);
			if (parent instanceof MElementGroup) return new EditSpiderChartCommand((MElementGroup) parent, node);
		}
		return null;
	}

	@Override
	public void run() {
		execute(command);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.commoneditAction);
		setToolTipText(Messages.commoneditToolTip);
		setId(ChartSpiderWizardAction.ID);
		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED));
		setEnabled(false);
	}
}
