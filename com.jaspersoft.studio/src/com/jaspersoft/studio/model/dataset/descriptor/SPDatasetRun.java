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
package com.jaspersoft.studio.model.dataset.descriptor;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.property.dataset.DatasetRunWidgetRadio;
import com.jaspersoft.studio.property.descriptor.expression.dialog.JRExpressionEditor;
import com.jaspersoft.studio.property.descriptor.parameter.dialog.ComboParameterEditor;
import com.jaspersoft.studio.property.descriptor.parameter.dialog.ParameterDTO;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SPDatasetRun extends ASPropertyWidget {
	
	private Combo dsetCombo;

	private Button params;
	
	private Button paramMap;

	private boolean alldatasets = true;
	
	protected APropertyNode pnode;
	
	protected MDatasetRun mDataSet;

	protected DatasetRunWidgetRadio dsRunWidget;

	public SPDatasetRun(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor, boolean alldatasets) {
		this(parent, section, pDescriptor);
		this.alldatasets = alldatasets;
	}

	public SPDatasetRun(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	protected void createComponent(Composite parent) {
		dsetCombo = section.getWidgetFactory().createCombo(parent, SWT.FLAT | SWT.READ_ONLY);
		dsetCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean en = !dsetCombo.getText().equals(ModelUtils.MAIN_DATASET);
				setDatasetEnabled(en);
				changeProperty(section, pDescriptor.getId(), JRDesignDatasetRun.PROPERTY_DATASET_NAME, en ? dsetCombo.getText(): ""); //$NON-NLS-1$
			}
		});

		dsRunWidget = new DatasetRunWidgetRadio(parent) {
			@Override
			protected void setNoConnection() {
				super.setNoConnection();
				changeProperty(section, pDescriptor.getId(), JRDesignDatasetRun.PROPERTY_CONNECTION_EXPRESSION, ""); //$NON-NLS-1$
			}

			@Override
			protected void setDatasource(String exTxt) {
				if (datasetrun != null) {

					super.setDatasource(exTxt);
					JRDesignExpression jde = (JRDesignExpression) datasetrun.getDataSourceExpression();
					changeProperty(section, pDescriptor.getId(), JRDesignDatasetRun.PROPERTY_DATA_SOURCE_EXPRESSION, jde);
				}
			}

			@Override
			protected void setConnection(String exTxt) {
				if (datasetrun != null) {

					super.setConnection(exTxt);
					JRDesignExpression jde = (JRDesignExpression) datasetrun.getConnectionExpression();
					changeProperty(section, pDescriptor.getId(), JRDesignDatasetRun.PROPERTY_CONNECTION_EXPRESSION, jde);
				}
			}
		};
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		dsRunWidget.getControl().setLayoutData(gd);

		Composite c = section.getWidgetFactory().createComposite(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		c.setLayout(layout);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gd.horizontalSpan = 2;
		c.setLayoutData(gd);

		params = section.getWidgetFactory().createButton(c, Messages.SPDatasetRun_2, SWT.PUSH | SWT.FLAT);
		params.addSelectionListener(new SelectionAdapter() {

			private ParameterDTO prmDTO;

			@Override
			public void widgetSelected(SelectionEvent e) {
				JRDesignDatasetRun datasetRun = mDataSet.getValue();
				if (prmDTO == null) {
					prmDTO = new ParameterDTO();
					prmDTO.setJasperDesign(mDataSet.getJasperDesign());

				}
				prmDTO.setValue(datasetRun.getParameters());

				ComboParameterEditor wizard = new ComboParameterEditor();
				wizard.setValue(prmDTO, mDataSet);
				// get always the selected element, because the getElement of some sections (i.e. MCrosstab)
				// return something else for their tricky dirty purposes. getSelectedElement return always
				// the selected element for the section
				JasperReportsConfiguration config = section.getSelectedElement().getJasperConfiguration();
				JRDesignDataset parentDatset = ModelUtils.getFirstDatasetInHierarchy(section.getSelectedElement());
				wizard.setExpressionContext(new ExpressionContext(parentDatset, config));
				WizardDialog dialog = new WizardDialog(params.getShell(), wizard);
				dialog.create();
				if (dialog.open() == Dialog.OK) {
					prmDTO = wizard.getValue();

					changeProperty(section, pDescriptor.getId(), JRDesignDatasetRun.PROPERTY_PARAMETERS, prmDTO);
				}
			}

		});

		paramMap = section.getWidgetFactory().createButton(c, Messages.SPDatasetRun_3, SWT.PUSH | SWT.FLAT);
		paramMap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
					JRDesignDatasetRun datasetRun = mDataSet.getValue();
					JRExpressionEditor wizard = new JRExpressionEditor();
					wizard.setValue((JRDesignExpression) datasetRun.getParametersMapExpression());
					WizardDialog dialog = ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(paramMap.getShell(), wizard);
					if (dialog.open() == Dialog.OK) {
						changeProperty(section, pDescriptor.getId(), JRDesignDatasetRun.PROPERTY_PARAMETERS_MAP_EXPRESSION,
								wizard.getValue());
					}
				}
			}

		});
	}

	@Override
	public void setData(APropertyNode pnode, Object value) {
		this.pnode = pnode;
		this.mDataSet = (MDatasetRun) value;
		JasperDesign jasperDesign = pnode.getJasperDesign();
		JRDataset dataset = null;
		JRDesignDatasetRun datasetRun = null;
		if (mDataSet != null) {
			datasetRun = mDataSet.getValue();
			if (datasetRun != null) {
				String dsname = datasetRun.getDatasetName();
				dataset = jasperDesign.getDatasetMap().get(dsname);
			}
		}
		if (dataset == null)
			dataset = jasperDesign.getMainDataset();

		String[] items = ModelUtils.getDataSets(jasperDesign, alldatasets);
		int dsindex = 0;
		if (datasetRun != null) {
			for (int i = 0; i < items.length; i++) {
				if (items[i].equals(datasetRun.getDatasetName())) {
					dsindex = i;
					break;
				}
			}
		}
		dsetCombo.setItems(items);
		dsetCombo.select(dsindex);
		setDatasetEnabled(!dsetCombo.getText().equals(ModelUtils.MAIN_DATASET));
		dsRunWidget.setData(datasetRun);

	}

	@Override
	public Control getControl() {
		return null;
	}

	protected void changeProperty(AbstractSection section, Object property, Object prop, Object value) {
		JRDesignDatasetRun jDatasetRun = null;
		if (mDataSet == null && prop.equals(JRDesignDatasetRun.PROPERTY_DATASET_NAME)) {
			jDatasetRun = new JRDesignDatasetRun();
			jDatasetRun.setDatasetName((String) value);
			mDataSet = new MDatasetRun(jDatasetRun, pnode.getJasperDesign());
			mDataSet.setJasperConfiguration(pnode.getJasperConfiguration());
			dsRunWidget.setData(jDatasetRun);
			section.changeProperty(property, mDataSet);
		} else {
			jDatasetRun = (JRDesignDatasetRun) mDataSet.getValue().clone();
			// mDataSet = new MDatasetRun(jDatasetRun, pnode.getJasperDesign());
			// mDataSet.setPropertyValue(prop, value);
			// dsRunWidget.setData(jDatasetRun);

			// section.changePropertyOn(prop, value, mDataSet);
			if (property != null) {
				mDataSet.setValue(null);
				mDataSet = new MDatasetRun(jDatasetRun, pnode.getJasperDesign());
				mDataSet.setJasperConfiguration(pnode.getJasperConfiguration());
				mDataSet.setPropertyValue(prop, value);
				dsRunWidget.setData(jDatasetRun);
				section.changePropertyOn(property, mDataSet, pnode);
			}
		}
	}

	private void setDatasetEnabled(boolean enabled) {
		paramMap.setEnabled(enabled);
		params.setEnabled(enabled);
		if (!enabled)
			dsRunWidget.setData(null);
		else
			dsRunWidget.setData(mDataSet.getValue());
		dsRunWidget.setEnabled(enabled);
	}

}
