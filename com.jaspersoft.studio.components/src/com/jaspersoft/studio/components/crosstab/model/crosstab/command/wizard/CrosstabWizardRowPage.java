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

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.wizards.JSSWizard;

public class CrosstabWizardRowPage extends CrosstabWizardColumnPage {
	private static final String F_CALCULATION = "CALCULATION";
	private static final String F_TOTALPOSITION = "TOTALPOSITION";
	private static final String F_ORDER = "ORDER";
	private static final String F_NAME = "NAME";

	private final class TRowLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			Wrapper w = (Wrapper) element;
			String oldExpText = w.getOldExpText();
			switch (columnIndex) {
			case 0:
				if (oldExpText.startsWith("$F{")) //$NON-NLS-1$
					return JaspersoftStudioPlugin.getInstance().getImage(MField
							.getIconDescriptor().getIcon16());
				if (oldExpText.startsWith("$P{")) //$NON-NLS-1$
					return JaspersoftStudioPlugin.getInstance().getImage(MParameter
							.getIconDescriptor().getIcon16());
				if (oldExpText.startsWith("$V{")) //$NON-NLS-1$
					return JaspersoftStudioPlugin.getInstance().getImage(MVariable
							.getIconDescriptor().getIcon16());
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			Wrapper w = (Wrapper) element;
			JRDesignCrosstabRowGroup m = (JRDesignCrosstabRowGroup) w
					.getValue();
			JRCrosstabBucket bucket = m.getBucket();

			switch (columnIndex) {
			case 0:
				return w.getLabel();
			case 1:
				return bucket.getOrderValue().getName();
			case 2:
				return m.getTotalPositionValue().getName();
			case 3:
				return w.getCalculation().getName();
			}
			return ""; //$NON-NLS-1$
		}
	}

	protected CrosstabWizardRowPage() {
		this("crosstabrowpage"); //$NON-NLS-1$
	}

	protected CrosstabWizardRowPage(String pagename) {
		super(pagename);
		setTitle(Messages.CrosstabWizardRowPage_rows);
		setImageDescriptor(
				Activator.getDefault().getImageDescriptor("icons/wizard_rows.png"));//$NON-NLS-1$
		setDescription(Messages.CrosstabWizardRowPage_description);
		setPageComplete(false);
	}

	/**
	 * Set the label provider, which is an instance of the local class
	 * TLabelProvider. This implementation deals with Row Groups.
	 * 
	 * @see com.jaspersoft.studio.components.crosstab.model.crosstab.command.wizard.CrosstabWizardColumnPage#setLabelProvider(org.eclipse.jface.viewers.TableViewer)
	 * 
	 * @param
	 * 
	 * @return
	 */
	@Override
	protected void setLabelProvider(TableViewer tableViewer) {
		tableViewer.setLabelProvider(new TRowLabelProvider());
	}

	@Override
	protected void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals(F_ORDER)) //$NON-NLS-1$
					return true;
				if (property.equals(F_TOTALPOSITION)) //$NON-NLS-1$
					return true;
				if (property.equals(F_CALCULATION)) { //$NON-NLS-1$
					Wrapper w = (Wrapper) element;
					JRDesignCrosstabRowGroup rg = (JRDesignCrosstabRowGroup) w
							.getValue();
					if (Date.class.isAssignableFrom(rg.getBucket()
							.getValueClass()))
						return true;
				}
				return false;
			}

			public Object getValue(Object element, String property) {
				Wrapper w = (Wrapper) element;
				JRDesignCrosstabRowGroup prop = (JRDesignCrosstabRowGroup) w
						.getValue();
				if (F_NAME.equals(property)) //$NON-NLS-1$
					return ((TRowLabelProvider) viewer.getLabelProvider())
							.getColumnText(element, 1);

				if (F_ORDER.equals(property)) //$NON-NLS-1$
					return EnumHelper.getValue(
							prop.getBucket().getOrderValue(), 1, false);

				if (F_TOTALPOSITION.equals(property)) //$NON-NLS-1$
					return EnumHelper.getValue(prop.getTotalPositionValue(), 0,
							false);
				if (F_CALCULATION.equals(property)) //$NON-NLS-1$
					return w.getCalculation().getValue();

				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				setErrorMessage(null);
				setMessage(getDescription());
				Wrapper w = (Wrapper) tableItem.getData();
				JRDesignCrosstabRowGroup data = (JRDesignCrosstabRowGroup) w
						.getValue();
				JRDesignCrosstabBucket bucket = (JRDesignCrosstabBucket) data
						.getBucket();
				if (F_ORDER.equals(property)) { //$NON-NLS-1$
					bucket.setOrder((SortOrderEnum) EnumHelper.getSetValue(
							SortOrderEnum.values(), value, 1, false));
				} else if (F_TOTALPOSITION.equals(property)) { //$NON-NLS-1$
					data.setTotalPosition((CrosstabTotalPositionEnum) EnumHelper
							.getSetValue(CrosstabTotalPositionEnum.values(),
									value, 0, false));
				} else if (F_CALCULATION.equals(property)) { //$NON-NLS-1$
					AgregationFunctionEnum function = AgregationFunctionEnum
							.getByValue((Integer) value);
					w.setCalculation(function);
					CrosstabWizard.setBucketExpression(bucket,
							w.getOldExpText(), function);
				}
				viewer.update(element, new String[] { property });
				viewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] {
				new TextCellEditor(parent),
				new ComboBoxCellEditor(parent, EnumHelper.getEnumNames(
						SortOrderEnum.values(), NullEnum.NOTNULL)),
				new ComboBoxCellEditor(parent, EnumHelper.getEnumNames(
						CrosstabTotalPositionEnum.values(), NullEnum.NOTNULL)),
				new ComboBoxCellEditor(parent, AgregationFunctionEnum
						.getStringValues()) });
		viewer.setColumnProperties(new String[] { F_NAME, F_ORDER,
				F_TOTALPOSITION, F_CALCULATION });
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
					.getAvailableRowGroups());
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

			settings.put(CrosstabWizard.CROSSTAB_ROWS, getSelectedFields());
			setPageComplete(!(getSelectedFields()==null || getSelectedFields().isEmpty()));
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

		String objName = ((TRowLabelProvider) rightTView.getLabelProvider())
				.getColumnText(object, 0);
		for (Object obj : fields) {
			if (((TRowLabelProvider) leftTView.getLabelProvider())
					.getColumnText(obj, 0).equals(objName)) {
				return obj;
			}
		}
		return null;
	}
}
