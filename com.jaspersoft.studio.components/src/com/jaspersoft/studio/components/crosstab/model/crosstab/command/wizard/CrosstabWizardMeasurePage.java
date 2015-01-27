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
package com.jaspersoft.studio.components.crosstab.model.crosstab.command.wizard;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.engine.type.CalculationEnum;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.property.dataset.wizard.WizardFieldsPage;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.wizards.JSSWizard;

public class CrosstabWizardMeasurePage extends WizardFieldsPage {
	private static final String F_CALCULATION = "CALCULATION";
	private static final String F_NAME = "NAME";

	private final class TMeasureLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			JRDesignCrosstabMeasure m = (JRDesignCrosstabMeasure) element;
			String txt = m.getValueExpression().getText();
			switch (columnIndex) {
			case 0:
				if (txt.startsWith("$F{")) //$NON-NLS-1$
					return JaspersoftStudioPlugin.getInstance().getImage(
							MField.getIconDescriptor().getIcon16());
				if (txt.startsWith("$P{")) //$NON-NLS-1$
					return JaspersoftStudioPlugin.getInstance().getImage(
							MParameter.getIconDescriptor().getIcon16());
				if (txt.startsWith("$V{")) //$NON-NLS-1$
					return JaspersoftStudioPlugin.getInstance().getImage(
							MVariable.getIconDescriptor().getIcon16());
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			JRDesignCrosstabMeasure m = (JRDesignCrosstabMeasure) element;
			String txt = m.getValueExpression().getText();
			switch (columnIndex) {
			case 0:
				return txt.substring(3, txt.length() - 1);
			case 1:
				if (m == null || m.getCalculationValue() == null)
					return "";
				return m.getCalculationValue().getName();
			}
			return ""; //$NON-NLS-1$
		}
	}

	protected CrosstabWizardMeasurePage() {
		super("crosstabmeasurepage"); //$NON-NLS-1$
		setTitle(Messages.CrosstabWizardMeasurePage_measures);
		setImageDescriptor(Activator.getDefault().getImageDescriptor(
				"icons/wizard_details.png"));//$NON-NLS-1$
		setDescription(Messages.CrosstabWizardMeasurePage_description);
		setPageComplete(false);
	}

	@Override
	protected void setLabelProvider(TableViewer tableViewer) {
		tableViewer.setLabelProvider(new TMeasureLabelProvider());
	}

	@Override
	protected void createColumns() {
		TableColumn[] col = new TableColumn[2];
		col[0] = new TableColumn(rightTable, SWT.NONE);
		col[0].setText(Messages.common_fields);
		col[0].pack();

		col[1] = new TableColumn(rightTable, SWT.NONE);
		col[1].setText(Messages.common_calculation);
		col[1].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, false));
		tlayout.addColumnData(new ColumnWeightData(50, false));
		rightTable.setLayout(tlayout);
	}

	@Override
	protected void attachCellEditors(final TableViewer viewer, Composite parent) {
		final ComboBoxCellEditor calcCombo = new ComboBoxCellEditor(parent,
				EnumHelper.getEnumNames(CalculationEnum.values(),
						NullEnum.NOTNULL));
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				JRDesignCrosstabMeasure prop = (JRDesignCrosstabMeasure) element;
				if (property.equals(F_CALCULATION)) { //$NON-NLS-1$
					String[] items = null;
					if (Date.class.isAssignableFrom(prop.getValueClass()))
						items = new String[] { CalculationEnum.COUNT.getName(),
								CalculationEnum.DISTINCT_COUNT.getName(),
								CalculationEnum.HIGHEST.getName(),
								CalculationEnum.LOWEST.getName(),
								CalculationEnum.FIRST.getName(),
								CalculationEnum.NOTHING.getName() };
					else if (Number.class.isAssignableFrom(prop.getValueClass()))
						items = EnumHelper.getEnumNames(
								CalculationEnum.values(), NullEnum.NOTNULL);
					else
						items = new String[] { CalculationEnum.COUNT.getName(),
								CalculationEnum.DISTINCT_COUNT.getName(),
								CalculationEnum.FIRST.getName(),
								CalculationEnum.NOTHING.getName() };
					calcCombo.setItems(items);
					return true;
				}
				return false;
			}

			public Object getValue(Object element, String property) {
				JRDesignCrosstabMeasure prop = (JRDesignCrosstabMeasure) element;
				if (F_NAME.equals(property))
					return ((TMeasureLabelProvider) viewer.getLabelProvider())
							.getColumnText(element, 1);

				if (F_CALCULATION.equals(property)) {
					String name = prop.getCalculationValue().getName();
					String[] items = calcCombo.getItems();
					for (int i = 0; i < items.length; i++) {
						if (items[i].equals(name))
							return i;
					}
					return 0;
				}

				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				setErrorMessage(null);
				setMessage(getDescription());
				JRDesignCrosstabMeasure data = (JRDesignCrosstabMeasure) tableItem
						.getData();
				if (F_CALCULATION.equals(property)) {
					CalculationEnum calculation = CalculationEnum
							.getByName(calcCombo.getItems()[(Integer) value]);
					data.setCalculation(calculation);
				}
				viewer.update(element, new String[] { property });
				viewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent),
				calcCombo });
		viewer.setColumnProperties(new String[] { F_NAME, F_CALCULATION }); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This procedure initialize the dialog page with the list of available
	 * objects. This implementation looks for object set in the map as
	 * DISCOVERED_FIELDS.
	 * 
	 */
	public void loadSettings() {

		if (getSettings() == null)
			return;

		if (getWizard() instanceof CrosstabWizard) {
			setAvailableFields(((CrosstabWizard) getWizard())
					.getAvailableMeasures());
		} else {
			setAvailableFields(null);
		}
	}

	/**
	 * Every time a new selection occurs, the selected fields are stored in the
	 * settings map with the key WizardDataSourcePage.DATASET_FIELDS
	 */
	public void storeSettings() {
		if (getWizard() instanceof JSSWizard && getWizard() != null) {
			Map<String, Object> settings = ((JSSWizard) getWizard())
					.getSettings();

			if (settings == null)
				return;

			settings.put(CrosstabWizard.CROSSTAB_MEASURES, getSelectedFields());
			setPageComplete(!(getSelectedFields() == null || getSelectedFields()
					.isEmpty()));
		}

	}

	/**
	 * This function checks if a particular right element is in the provided
	 * list, and which is the matching element in that list.
	 * 
	 * This implementation is based on the string value returned by left and
	 * right getText label providers on column 0
	 * 
	 * @param object
	 * @param fields
	 * @return
	 */
	protected Object findElement(Object object, List<?> fields) {

		String objName = ((TMeasureLabelProvider) rightTView.getLabelProvider())
				.getColumnText(object, 0);
		for (Object obj : fields) {
			if (((TMeasureLabelProvider) leftTView.getLabelProvider())
					.getColumnText(obj, 0).equals(objName)) {
				return obj;
			}
		}
		return null;
	}

}
