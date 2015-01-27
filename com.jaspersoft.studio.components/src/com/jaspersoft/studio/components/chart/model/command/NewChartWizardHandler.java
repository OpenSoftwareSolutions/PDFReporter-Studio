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
package com.jaspersoft.studio.components.chart.model.command;

import java.util.List;

import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;

import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.utils.SelectionHelper;

/**
 * Handler to place a chart into the summary band and open its wizard, and this 
 * action can be performed from a cheatsheet 
 * 
 * @author Orlandin Marco
 *
 */
public class NewChartWizardHandler extends Action {

	/**
	 * Search for a Summary band inside a list o elements
	 * @param reportElements list of elements
	 * @return summary band if found, null otherwise
	 */
	private MBand searchSummaryBand(List<INode> reportElements){
		for(INode node : reportElements)
			if (node instanceof MBand) {
				MBand band = (MBand) node;
				if (band.getBandType() == BandTypeEnum.SUMMARY) return band;
			}
		return null;
	}
	
	/**
	 * Search the summary band from the root of the document
	 * @param root root node of the document
	 * @return summary band if found, null otherwise
	 */
	private MBand getSummaryBand(INode root){
		if (root != null){
			List<INode> children = root.getChildren();
			for(INode node : children){
				if (node instanceof MReport)
					return searchSummaryBand(node.getChildren());
			}
		}
		return null;
	}
	
	/**
	 * Search the summary band of the chart and if it is found a chart will be placed inside and its wizard will 
	 * be opened
	 */
	@Override
	public void run() {
		IEditorPart activeJRXMLEditor = SelectionHelper.getActiveJRXMLEditor();
		if (activeJRXMLEditor != null && activeJRXMLEditor instanceof JrxmlEditor) {
			INode root = ((JrxmlEditor) activeJRXMLEditor).getModel();
			MBand summary = getSummaryBand(root);
			if (summary != null){
				MChart tempChart = new MChart();
				CreateChartCommand command = new CreateChartCommand(summary, tempChart, -1);
				command.execute();
			}
		}
	};
}
