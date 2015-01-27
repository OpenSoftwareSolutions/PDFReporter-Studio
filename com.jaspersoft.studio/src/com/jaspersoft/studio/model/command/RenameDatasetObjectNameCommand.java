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
package com.jaspersoft.studio.model.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGraphicalPropertiesHandler;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.utils.ModelUtils;

public class RenameDatasetObjectNameCommand extends Command {
	private String newvalue;
	private String oldvalue;
	private String type;
	private String type1;

	private JasperDesign jd;
	private JasperReportsContext jContext;
	private JRDataset dataset;
	private Set<JRDesignExpression> cexpr = new HashSet<JRDesignExpression>();
	private ANode node;

	public RenameDatasetObjectNameCommand(MField mfield, String oldvalue) {
		super();
		node = mfield;
		jd = mfield.getJasperDesign();
		jContext = mfield.getJasperConfiguration();
		dataset = ModelUtils.getDataset(mfield);
		type = "\\$F\\{";
		type1 = "$F{";
		this.newvalue = mfield.getValue().getName();
		this.oldvalue = oldvalue;
	}

	public RenameDatasetObjectNameCommand(MVariable mvar, String oldvalue) {
		super();
		node = mvar;
		jd = mvar.getJasperDesign();
		jContext = mvar.getJasperConfiguration();
		dataset = ModelUtils.getDataset(mvar);
		type = "\\$V\\{";
		type1 = "$V{";
		this.newvalue = mvar.getValue().getName();
		this.oldvalue = oldvalue;
	}

	public RenameDatasetObjectNameCommand(MParameter mparam, String oldvalue) {
		super();
		node = mparam;
		jd = mparam.getJasperDesign();
		jContext = mparam.getJasperConfiguration();
		dataset = ModelUtils.getDataset(mparam);
		type = "\\$P\\{";
		type1 = "$P{";
		this.newvalue = mparam.getValue().getName();
		this.oldvalue = oldvalue;
	}
	
	
	/**
	 * Search all the nodes that are using this styles and set the flag to tell the graphic manager
	 * to repaint them
	 * 
	 * @param childerns the children of the actual level
	 */
	private void setModelRefresh(List<INode> childerns){
		for(INode child : childerns){
			if (child instanceof IGraphicalPropertiesHandler){
				IGraphicalPropertiesHandler graphicalElement = (IGraphicalPropertiesHandler)child;
				graphicalElement.initModel();
			}
			setModelRefresh(child.getChildren());
		}
		
	}
	

	@Override
	public void execute() {
		cexpr.clear();
		JRExpressionCollector reportCollector = JRExpressionCollector.collector(jContext, jd);
		JRExpressionCollector datasetCollector = reportCollector.getCollector(dataset);
		List<JRExpression> datasetExpressions = datasetCollector.getExpressions();
		// update expressions
		boolean modelAlreadyInitialized = false;
		for (JRExpression expr : datasetExpressions) {
			String s = expr.getText();
			if (s != null && s.length() > 4 && s.contains(type1 + oldvalue + "}")) {
				//If there are changes this will assure that the model of all the elements
				//is initialized, so the elements inside containers can be refreshed
				if (!modelAlreadyInitialized) {
					setModelRefresh(node.getRoot().getChildren());
					modelAlreadyInitialized = true;
				}
				
				s = s.replaceAll(type + oldvalue + "}", type + newvalue + "}");
				JRDesignExpression dexpr = (JRDesignExpression) expr;
				dexpr.setText(s);
				cexpr.add((JRDesignExpression) expr);
			}
		}
		doSetQuery(oldvalue, newvalue);
	}

	protected void doSetQuery(String oldVal, String newVal) {
		if (type1.equals("$P{")) {
			JRDesignQuery query = (JRDesignQuery) dataset.getQuery();
			String q = query.getText();
			// replace $P{} in query
			query.setText(q.replaceAll(type + oldVal + "}", type + newVal + "}"));
		}
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public void undo() {
		for (JRDesignExpression de : cexpr) {
			de.setText(de.getText().replaceAll(newvalue, oldvalue));
		}
		doSetQuery(newvalue, oldvalue);
	}
}
