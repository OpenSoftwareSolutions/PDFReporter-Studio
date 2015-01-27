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
package com.jaspersoft.studio.preferences.editor.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FilePrefUtil;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.help.TableHelpListener;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.editor.table.TableFieldEditor;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

public class PropertyListFieldEditor extends TableFieldEditor {

	protected Button editButton;

	public PropertyListFieldEditor() {
		super();
	}

	public PropertyListFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, new String[] { Messages.PropertyListFieldEditor_propertyLabel,
				Messages.PropertyListFieldEditor_valueLabel }, new int[] { 200, 30 }, parent);
	}

	@Override
	protected String createList(String[][] items) {
		return ""; //$NON-NLS-1$
	}

	@Override
	protected String[][] parseString(String string) {
		return new String[0][0];
	}
	
	private class PEditDialog extends Dialog {
		
		private String pname;
		private String pvalue;

		protected PEditDialog(Shell parentShell) {
			this(parentShell,null,null);
		}
		
		protected PEditDialog(Shell parentShell, String pname, String pvalue) {
			super(parentShell);
			this.pname = pname;
			this.pvalue = pvalue;
		}
		
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			composite.setLayout(new GridLayout(2, false));
			Label label = new Label(composite, SWT.NONE);
			label.setText(Messages.PropertyListFieldEditor_newPropertyName);

			final Text text = new Text(composite, SWT.BORDER);
			text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			text.setText(Misc.nvl(pname, "net.sf.jasperreports.")); //$NON-NLS-1$
			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					pname = text.getText();
				}
			});

			label = new Label(composite, SWT.NONE);
			label.setText(Messages.PropertyListFieldEditor_newPropertyValue);

			final Text tname = new Text(composite, SWT.BORDER);
			tname.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			tname.setText(Misc.nvl(pvalue,Messages.PropertyListFieldEditor_exampleValue));
			tname.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					pvalue = tname.getText();
				}
			});
			applyDialogFont(composite);
			return composite;
		}
		
		@Override
		protected boolean isResizable() {
			return true;
		}
		
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setSize(500, 200);
			newShell.setText(Messages.PropertyListFieldEditor_newPropertyTitle);
		}

		public String getPName() {
			return this.pname;
		}

		public String getPValue() {
			return this.pvalue;
		}
		
	}
	

	@Override
	protected String[] getNewInputObject() {
		PEditDialog dialog = new PEditDialog(UIUtils.getShell());
		if (dialog.open() == Window.OK) {
			return new String[]{dialog.getPName(),dialog.getPValue()};
		}
		return null;
	}

	protected void doStore() {
		TableItem[] items = getTable().getItems();
		Properties props = new Properties();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			// getPreferenceStore().setValue(item.getText(0), item.getText(1));
			String key = item.getText(0);
			String value = item.getText(1);
			props.setProperty(key, value);
			if (key.equals("net.sf.jasperreports.default.font.name")) //$NON-NLS-1$
				JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty(key, value);
			else if (key.equals("net.sf.jasperreports.default.font.size")) //$NON-NLS-1$
				JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty(key, value);
		}
		getPreferenceStore().setValue(FilePrefUtil.NET_SF_JASPERREPORTS_JRPROPERTIES, FileUtils.getPropertyAsString(props));
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoad() {
		if (getTable() != null) {
			//			List<PropertySuffix> lst = PropertiesHelper.DPROP.getProperties(""); //$NON-NLS-1$
			// Collections.sort(lst, new PropertyComparator());
			Properties props = null;
			try {
				props = FileUtils.load(getPreferenceStore().getString(FilePrefUtil.NET_SF_JASPERREPORTS_JRPROPERTIES));
				List<String> keys = new ArrayList<String>();
				for (Object key : props.keySet())
					keys.add((String) key);
				Collections.sort(keys);

				for (String key : keys) {
					String value = props.getProperty(key);
					TableItem tableItem = new TableItem(getTable(), SWT.NONE);
					tableItem.setText(new String[] { (String) key, value });
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			// if (props != null)
			// for (PropertySuffix ps : lst) {
			// if (props.getProperty(ps.getKey()) == null) {
			// TableItem tableItem = new TableItem(getTable(), SWT.NONE);
			// tableItem.setText(new String[] { ps.getKey(), ps.getValue() });
			// }
			// }

			// TableItem[] items = table.getItems();
			// Collator collator = Collator.getInstance(Locale.getDefault());
			// for (int i = 1; i < items.length; i++) {
			// String value1 = items[i].getText(0);
			// for (int j = 0; j < i; j++) {
			// String value2 = items[j].getText(0);
			// if (collator.compare(value1, value2) < 0) {
			// String[] values = { items[i].getText(0), items[i].getText(1) };
			// items[i].dispose();
			// TableItem item = new TableItem(table, SWT.NONE, j);
			// item.setText(values);
			// items = table.getItems();
			// break;
			// }
			// }
			// }
			// Add an help listener to the table
			TableHelpListener.setTableHelp(getTable());
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoadDefault() {
		if (getTable() != null) {
			getTable().removeAll();

			List<PropertySuffix> lst = PropertiesHelper.DPROP.getProperties(""); //$NON-NLS-1$
			Collections.sort(lst, new PropertyComparator());
			for (PropertySuffix ps : lst) {

				TableItem tableItem = new TableItem(getTable(), SWT.NONE);
				tableItem.setText(new String[] { ps.getKey(), ps.getValue() });
			}
		}
	}

	@Override
	protected boolean isFieldEditable(int col, int row) {
		if (col == 0) {
			TableItem ti = table.getItem(row);
			return PropertiesHelper.DPROP.getProperty(ti.getText(0)) == null;
		}
		return super.isFieldEditable(col, row);
	}

	@Override
	protected void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, ContextHelpIDs.PREFERENCES_PROPERTIES);
	}

	@Override
	public void createSelectionListener() {
		selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Widget widget = event.widget;
				if (widget == addButton) {
					addPressed();
				} else if (widget == duplicateButton) {
					duplicatePressed();
				} else if (widget == removeButton) {
					removePressed();
				} else if (widget == editButton) {
					editPressed();
				}	else if (widget == table) {
					selectionChanged();
				}
			}
		};
	}
	
	private void editPressed() {
		int selIdx = table.getSelectionIndex();
		if(selIdx!=-1){
			TableItem item = table.getItem(selIdx);
			String pname = item.getText(0);
			String pvalue = item.getText(1);
			PEditDialog dialog = new PEditDialog(UIUtils.getShell(), pname, pvalue);
			if(dialog.open() == Window.OK) {
				String newPName = dialog.getPName();
				String newPValue = dialog.getPValue();
				if(!pname.equals(newPName)){
					// ensure no duplicates
					for(int i=0;i<table.getItemCount();i++) {
						if(i!=selIdx && newPName.equals(table.getItem(i).getText(0))) {
							MessageDialog.openError(UIUtils.getShell(), Messages.PropertyListFieldEditor_ErrTitle, Messages.PropertyListFieldEditor_ErrMsg);
							return;
						}
					}
				}
				item.setText(0,newPName);
				item.setText(1,newPValue);
			}
		}
	}
	
	protected void selectionChanged() {
		super.selectionChanged();
		int index = table.getSelectionIndex();
		int size = table.getItemCount();
		boolean isMultiSelection = table.getSelectionCount() > 1;
		if (editButton != null)
			editButton.setEnabled(!isMultiSelection && size >= 1 && index >= 0 && index < size && isEditable(index));
	}
	
	protected boolean isEditable(int row) {
		return true;
	}
	
	@Override
	protected void createButtons(Composite box) {
		addButton = createPushButton(box, Messages.common_add);
		duplicateButton = createPushButton(box, Messages.PropertyListFieldEditor_duplicateButton);
		removeButton = createPushButton(box, Messages.common_delete);
		editButton = createPushButton(box, Messages.common_edit);
	}
}
