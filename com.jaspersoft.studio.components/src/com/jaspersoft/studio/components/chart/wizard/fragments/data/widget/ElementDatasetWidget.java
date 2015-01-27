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
package com.jaspersoft.studio.components.chart.wizard.fragments.data.widget;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.property.dataset.DatasetRunSelectionListener;
import com.jaspersoft.studio.property.dataset.DatasetRunWidget;
import com.jaspersoft.studio.property.descriptor.expression.dialog.JRExpressionEditor;
import com.jaspersoft.studio.property.descriptor.parameter.dialog.ComboParameterEditor;
import com.jaspersoft.studio.property.descriptor.parameter.dialog.ParameterDTO;
import com.jaspersoft.studio.utils.ModelUtils;

public class ElementDatasetWidget implements IExpressionContextSetter {
	private static final String GROUPPREFIX = "[Group] "; //$NON-NLS-1$
	private JRDesignElementDataset eDataset;
	private JasperDesign jrDesign;
	private Combo dsCombo;
	private Combo cbIncrement;
	private Combo cbReset;
	private Button btnIncrement;
	private ToolItem prmItem;
	private ToolItem prmMapItem;
	private DatasetRunWidget dsRun;
	private ExpressionContext expContext;
	private List<DatasetRunSelectionListener> dsRunSelectionListeners;
	public CTabFolder ctFolder;

	public ElementDatasetWidget(Composite parent) {
		this.dsRunSelectionListeners = new ArrayList<DatasetRunSelectionListener>();
		createDataset(parent);
		bindData();
	}

	public void setDataset(JRDesignElementDataset eDataset,
			JasperDesign jrDesign) {
		this.eDataset = eDataset;
		this.jrDesign = jrDesign;
		fillData();
	}

	private void fillData() {
		final String[] ds = ModelUtils.getDataSets(jrDesign, true);
		dsCombo.setItems(ds);
		dsCombo.select(0);
		if (eDataset != null && eDataset.getDatasetRun() != null) {
			for (int i = 0; i < ds.length; i++) {
				if (ds[i].equals(eDataset.getDatasetRun().getDatasetName())) {
					dsCombo.select(i);
					break;
				}
			}
			dsRun.setData((JRDesignDatasetRun) eDataset.getDatasetRun());
			dsRun.setExpressionContext(this.expContext);
		}

		enableMainDatasetRun();
		fillIncrement();
		fillResetGroup();
		dsCombo.getParent().layout(true);
	}

	private void fillIncrement() {
		List<String> lsIncs = new ArrayList<String>();
		lsIncs.add(IncrementTypeEnum.REPORT.getName());
		lsIncs.add(IncrementTypeEnum.PAGE.getName());
		lsIncs.add(IncrementTypeEnum.COLUMN.getName());
		JRDataset jrds = getJRdataset(eDataset);
		for (JRGroup gr : jrds.getGroups())
			lsIncs.add(GROUPPREFIX + gr.getName());
		lsIncs.add(IncrementTypeEnum.NONE.getName());

		cbIncrement.setItems(lsIncs.toArray(new String[lsIncs.size()]));

		IncrementTypeEnum rst = eDataset.getIncrementTypeValue();
		String grname = eDataset.getIncrementGroup() != null ? eDataset
				.getIncrementGroup().getName() : null;
		for (int i = 0; i < lsIncs.size(); i++) {
			String rsttype = lsIncs.get(i);
			if (rst.equals(IncrementTypeEnum.GROUP)) {
				if (rsttype.startsWith(GROUPPREFIX)
						&& grname
								.equals(rsttype.substring(GROUPPREFIX.length()))) {
					cbIncrement.select(i);
					break;
				}
			} else if (rsttype.equals(rst.getName())) {
				cbIncrement.select(i);
				break;
			}
		}
	}

	private void fillResetGroup() {
		JRDataset jrds = getJRdataset(eDataset);
		List<String> lsRsts = new ArrayList<String>();
		lsRsts.add(ResetTypeEnum.REPORT.getName());
		lsRsts.add(ResetTypeEnum.COLUMN.getName());
		lsRsts.add(ResetTypeEnum.PAGE.getName());

		for (JRGroup gr : jrds.getGroups())
			lsRsts.add(GROUPPREFIX + gr.getName());
		lsRsts.add(ResetTypeEnum.NONE.getName());
		cbReset.setItems(lsRsts.toArray(new String[lsRsts.size()]));

		ResetTypeEnum rst = eDataset.getResetTypeValue();
		String grname = eDataset.getResetGroup() != null ? eDataset
				.getResetGroup().getName() : null;
		for (int i = 0; i < lsRsts.size(); i++) {
			String rsttype = lsRsts.get(i);
			if (rst.equals(ResetTypeEnum.GROUP)) {
				if (rsttype.startsWith(GROUPPREFIX)
						&& grname
								.equals(rsttype.substring(GROUPPREFIX.length()))) {
					cbReset.select(i);
					break;
				}
			} else if (rsttype.equals(rst.getName())) {
				cbReset.select(i);
				break;
			}
		}
	}

	private void bindData() {
		dsCombo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (eDataset.getIncrementTypeValue().equals(
						IncrementTypeEnum.GROUP)) {
					eDataset.setIncrementType(IncrementTypeEnum.REPORT);
					eDataset.setIncrementGroup(null);
					cbIncrement.select(0);
				}
				if (eDataset.getResetTypeValue().equals(ResetTypeEnum.GROUP)) {
					eDataset.setResetType(ResetTypeEnum.REPORT);
					eDataset.setResetGroup(null);
					cbReset.select(0);
				}
				if (dsCombo.getSelectionIndex() == 0) {
					eDataset.setDatasetRun(null);
				} else {
					JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
					datasetRun.setDatasetName(dsCombo.getText());
					eDataset.setDatasetRun(datasetRun);
				}
				dsRun.setData((JRDesignDatasetRun) eDataset.getDatasetRun());
				enableMainDatasetRun();
				notifyDatasetRunSelectionChanged();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		bindIncrementGroup();
		bindResetGroup();

		prmItem.addSelectionListener(new SelectionListener() {
			private ParameterDTO prmDTO;

			public void widgetSelected(SelectionEvent e) {
				JRDesignDatasetRun datasetRun = (JRDesignDatasetRun) eDataset
						.getDatasetRun();
				if (prmDTO == null) {
					prmDTO = new ParameterDTO();
					prmDTO.setJasperDesign(jrDesign);
					prmDTO.setValue(datasetRun.getParameters());
				}
				
				ComboParameterEditor wizard = new ComboParameterEditor();
				wizard.setValue(prmDTO, new MDatasetRun(eDataset.getDatasetRun(), jrDesign));
				wizard.setExpressionContext(expContext);
				WizardDialog dialog = new WizardDialog(btnIncrement.getShell(),
						wizard);
				dialog.create();
				if (dialog.open() == Dialog.OK) {
					prmDTO = wizard.getValue();

					for (JRDatasetParameter prm : prmDTO.getValue())
						datasetRun.removeParameter(prm);

					for (JRDatasetParameter param : prmDTO.getValue())
						try {
							datasetRun.addParameter(param);
						} catch (JRException er) {
							er.printStackTrace();
						}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		prmMapItem.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if(!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
					JRExpressionEditor wizard = new JRExpressionEditor();
					wizard.setValue((JRDesignExpression) eDataset.getDatasetRun()
							.getParametersMapExpression());
					wizard.setExpressionContext(expContext);
					WizardDialog dialog = 
							ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(btnIncrement.getShell(),wizard);
					if (dialog.open() == Dialog.OK) {
						((JRDesignDatasetRun) eDataset.getDatasetRun())
								.setParametersMapExpression(wizard.getValue());
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private void enableMainDatasetRun() {
		boolean en = dsCombo.getSelectionIndex() != 0;
		prmItem.setEnabled(en);
		prmMapItem.setEnabled(en);
		dsRun.setEnabled(en);
	}

	private void bindResetGroup() {
		cbReset.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				String newval = cbReset.getText();
				ResetTypeEnum val = ResetTypeEnum.getByName(newval);
				if (val != null) {
					eDataset.setResetType(val);
				} else {
					eDataset.setResetType(ResetTypeEnum.GROUP);
					JRDataset jrds = getJRdataset(eDataset);
					for (JRGroup gr : jrds.getGroups()) {
						if (gr.getName().equals(
								newval.substring(GROUPPREFIX.length()))) {
							eDataset.setResetGroup(gr);
							break;
						}

					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private void bindIncrementGroup() {
		cbIncrement.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				String newval = cbIncrement.getText();
				IncrementTypeEnum val = IncrementTypeEnum.getByName(newval);
				if (val != null) {
					eDataset.setIncrementType(val);
				} else {
					eDataset.setIncrementType(IncrementTypeEnum.GROUP);
					JRDataset jrds = getJRdataset(eDataset);
					for (JRGroup gr : jrds.getGroups()) {
						if (gr.getName().equals(
								newval.substring(GROUPPREFIX.length()))) {
							eDataset.setIncrementGroup(gr);
							break;
						}
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		btnIncrement.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if(!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
					JRExpressionEditor wizard = new JRExpressionEditor();
					wizard.setValue((JRDesignExpression) eDataset
							.getIncrementWhenExpression());
					// Increment when expression should rely on the dataset run
					// information.
					JRDatasetRun datasetRun = eDataset.getDatasetRun();
					JRDesignDataset dds = jrDesign.getMainDesignDataset();
					if (datasetRun != null && datasetRun.getDatasetName() != null) {
						dds = ModelUtils.getDesignDatasetByName(jrDesign,
								datasetRun.getDatasetName());
					}
					ExpressionContext ec = new ExpressionContext(dds, expContext
							.getJasperReportsConfiguration());
					wizard.setExpressionContext(ec);
					WizardDialog dialog = 
							ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(btnIncrement.getShell(),wizard);
					if (dialog.open() == Dialog.OK) {
						eDataset.setIncrementWhenExpression(wizard.getValue());
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private JRDataset getJRdataset(final JRDesignElementDataset jrDataset) {
		JRDataset jrds = jrDesign.getMainDataset();
		if (jrDataset != null && jrDataset.getDatasetRun() != null) {
			String dsname = jrDataset.getDatasetRun().getDatasetName();
			jrDesign.getDatasetMap().get(dsname);
		}
		final JRDataset jrdsfinal = jrds;
		return jrdsfinal;
	}

	public void createDataset(Composite composite) {
		Composite grDataset = new Composite(composite, SWT.NONE);
		grDataset.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grDataset.setLayout(new GridLayout(1,false));

		ctFolder = new CTabFolder(grDataset, SWT.TOP);
		ctFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		// createFields(ctFolder);
		// createData(ctFolder);
		createParametersMap(ctFolder);
		createConnection(ctFolder);
		GridData folderData = new GridData(SWT.FILL, SWT.TOP, true, false);
		folderData.heightHint = 90;
		folderData.minimumHeight = 90;
		ctFolder.setLayoutData(folderData);
		ctFolder.setSelection(0);
	}

	private void createConnection(CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.ElementDatasetWidget_tabTitle);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));

		Composite leftComposite = new Composite(composite, SWT.NONE);
		leftComposite.setLayout(new GridLayout(3, false));
		leftComposite.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.VERTICAL_ALIGN_BEGINNING));

		new Label(leftComposite, SWT.NONE).setText(Messages.ElementDatasetWidget_incrementOnLabel);
		cbIncrement = new Combo(leftComposite, SWT.BORDER | SWT.READ_ONLY
				| SWT.SINGLE);
		cbIncrement.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btnIncrement = new Button(leftComposite, SWT.PUSH);
		btnIncrement.setText("..."); //$NON-NLS-1$
		btnIncrement.setToolTipText(Messages.ElementDatasetWidget_buttonTooltip);

		new Label(leftComposite, SWT.NONE).setText(Messages.ElementDatasetWidget_resetOnLabel);
		cbReset = new Combo(leftComposite, SWT.BORDER | SWT.READ_ONLY
				| SWT.SINGLE);
		cbReset.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(leftComposite, SWT.NONE);

		dsRun = new DatasetRunWidget(composite);
		dsRun.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		bptab.setControl(composite);
	}

	// private void createFields(CTabFolder tabFolder) {
	// CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
	// bptab.setText("Fields");
	//
	// Composite composite = new Composite(tabFolder, SWT.NONE);
	// composite.setLayout(new GridLayout());
	//
	// Label lbl = new Label(composite, SWT.NONE);
	// lbl.setText("[dataset fields table here]");
	// lbl.setLayoutData(new GridData(GridData.FILL_BOTH
	// | GridData.HORIZONTAL_ALIGN_CENTER
	// | GridData.VERTICAL_ALIGN_CENTER));
	//
	// bptab.setControl(composite);
	// }

	// private void createData(CTabFolder tabFolder) {
	// CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
	// bptab.setText("Data");
	//
	// Composite composite = new Composite(tabFolder, SWT.NONE);
	//
	// bptab.setControl(composite);
	// }

	private void createParametersMap(CTabFolder ctfolder) {
		Composite composite = new Composite(ctfolder, SWT.NONE);
		GridLayout layout = new GridLayout(10, false);
		layout.verticalSpacing = 1;
		layout.marginWidth = 1;
		layout.marginTop = 1;
		layout.marginBottom = 1;
		composite.setLayout(layout);

		Label lbl = new Label(composite, SWT.NONE);
		lbl.setText(Messages.ElementDatasetWidget_datasetLabel);

		dsCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY | SWT.SINGLE);
		dsCombo.setItems(new String[] { "main dataset" }); //$NON-NLS-1$

		// Button newDataset = new Button(composite, SWT.PUSH);
		// newDataset.setText("new");

		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.HORIZONTAL
				| SWT.WRAP | SWT.RIGHT);
		prmItem = new ToolItem(toolBar, SWT.PUSH);
		prmItem.setText(Messages.ElementDatasetWidget_parametersLabel);

		prmMapItem = new ToolItem(toolBar, SWT.PUSH);
		prmMapItem.setText(Messages.ElementDatasetWidget_parametersMapLabel);

		int tabHeight = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		tabHeight = Math.max(tabHeight, ctfolder.getTabHeight());
		ctfolder.setTabHeight(tabHeight);

		ctfolder.setTopRight(composite);
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}

	public void addDatasetRunSelectionListener(
			DatasetRunSelectionListener listener) {
		dsRunSelectionListeners.add(listener);
	}

	public void removeDatasetRunSelectionListener(
			DatasetRunSelectionListener listener) {
		dsRunSelectionListeners.remove(listener);
	}

	private void notifyDatasetRunSelectionChanged() {
		for (DatasetRunSelectionListener l : dsRunSelectionListeners) {
			l.selectionChanged();
		}
	}
}
