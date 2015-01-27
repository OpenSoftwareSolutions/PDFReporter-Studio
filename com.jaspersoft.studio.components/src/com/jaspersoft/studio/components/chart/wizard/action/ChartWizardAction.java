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
package com.jaspersoft.studio.components.chart.wizard.action;

import java.util.List;

import net.sf.jasperreports.engine.JRChart;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.components.chart.model.command.EditChartCommand;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.frame.MFrame;

public class ChartWizardAction extends ACachedSelectionAction {
	public static final String ID = "charteditaction"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *            The part for this action
	 */
	public ChartWizardAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
	}

	@Override
	public Command createCommand() {
		List<Object> charts = editor.getSelectionCache().getSelectionModelForType(MChart.class);
		if (charts.isEmpty())
			return null;
		
		for (Object chart : charts) {
				MChart n = (MChart) chart;
				if (n.getValue().getChartType() == JRChart.CHART_TYPE_MULTI_AXIS)
					continue;
				INode parent = n.getParent();
				if (parent instanceof MFrame) return new EditChartCommand((MFrame) parent, n);
				if (parent instanceof MBand) return new EditChartCommand((MBand) parent, n);
				if (parent instanceof MElementGroup) return new EditChartCommand((MElementGroup) parent, n);
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
		setId(ChartWizardAction.ID);
		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED));
		setEnabled(false);
	}
}
