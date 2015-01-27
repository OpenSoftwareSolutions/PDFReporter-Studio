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
package com.jaspersoft.studio.components.map.model.itemdata.dialog;

import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataDatasetDTO;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.jface.dialogs.EditableDatasetBaseComposite;
import com.jaspersoft.studio.model.dataset.ComponentElementDatasetAdapter;
import com.jaspersoft.studio.model.dataset.ComponentElementDatasetRunAdapter;
import com.jaspersoft.studio.model.dataset.IEditableDatasetRun;
import com.jaspersoft.studio.property.dataset.DatasetRunSelectionListener;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Dialog that allows editing the information associated to a {@link MapDataDatasetDTO} element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ElementDatasetDialog extends TitleAreaDialog {

	private String title;
	private String message;
	private JRDesignElementDataset dataset;
	private JasperReportsConfiguration jConfig;
	private ExpressionContext defaultExpressionContext;

	public ElementDatasetDialog(
			Shell parentShell,String title, String message, JRElementDataset dataset, JasperReportsConfiguration jConfig) {
		super(parentShell);
		this.title = title;
		this.message = message;
		this.dataset = (JRDesignElementDataset) dataset;
		if(this.dataset==null){
			this.dataset = new JRDesignElementDataset();
		}
		this.jConfig = jConfig;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ElementDatasetDialog_DialogTitle);
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout());
		container.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

	 	final EditableDatasetBaseComposite compositeDatasetInfo = new EditableDatasetBaseComposite(
				new ComponentElementDatasetAdapter(dataset, jConfig),container, SWT.NONE){
					@Override
					protected IEditableDatasetRun getEditableDatesetRun() {
						return new ComponentElementDatasetRunAdapter(this.getEditableDataset());
					}
		};
		compositeDatasetInfo.addDatasetRunSelectionListener(new DatasetRunSelectionListener() {
			public void selectionChanged() {
				ExpressionContext contextFromDSRun=getExpressionContextFromDSRun();
				compositeDatasetInfo.setExpressionContext(contextFromDSRun);
			}
		});
		compositeDatasetInfo.setExpressionContext(getExpressionContextFromDSRun());
		compositeDatasetInfo.setDefaultExpressionContext(defaultExpressionContext);
		
		setTitle(this.title);
		setMessage(this.message);
		
		return area;
	}
	
	public void setDefaultExpressionContext(ExpressionContext expContext){
		this.defaultExpressionContext = expContext;
	}

	private ExpressionContext getExpressionContextFromDSRun() {
		if(dataset!=null){
			JRDesignDataset ds = ModelUtils.getDesignDatasetForDatasetRun(
					jConfig.getJasperDesign(), dataset.getDatasetRun());
			return new ExpressionContext(ds,jConfig);
		}
		return null;
	}	
	
	public JRElementDataset getDataset(){
		return dataset;
	}
}
