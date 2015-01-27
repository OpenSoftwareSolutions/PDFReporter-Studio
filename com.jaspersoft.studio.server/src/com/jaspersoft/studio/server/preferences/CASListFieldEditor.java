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
package com.jaspersoft.studio.server.preferences;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.eclipse.ui.ATitledDialog;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FilePrefUtil;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.util.SecureStorageUtils;
import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;
import net.sf.jasperreports.util.CastorUtil;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.help.TableHelpListener;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.editor.table.TableFieldEditor;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.server.secret.JRServerSecretsProvider;
import com.jaspersoft.studio.swt.widgets.WSecretText;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

public class CASListFieldEditor extends TableFieldEditor {

	protected Button editButton;

	public CASListFieldEditor() {
		super();
	}

	public CASListFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, new String[] { "Type", "SSO Server" }, new int[] { 50, 300 }, parent);
	}

	@Override
	protected String createList(String[][] items) {
		return ""; //$NON-NLS-1$
	}

	@Override
	protected String[][] parseString(String string) {
		return new String[0][0];
	}

	private class PEditDialog extends ATitledDialog {

		private SSOServer value;
		private WSecretText tpass;

		protected PEditDialog(Shell parentShell, SSOServer value) {
			super(parentShell);
			this.value = value;
			setTitle("SSO Server");
			setDefaultSize(500, 300);
		}

		public SSOServer getValue() {
			return value;
		}

		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			composite.setLayout(new GridLayout(2, false));

			Label label = new Label(composite, SWT.NONE);
			label.setText("Type");

			final Combo type = new Combo(composite, SWT.READ_ONLY | SWT.SINGLE);
			type.setItems(SSOTypes.getLabels());
			type.select(SSOTypes.getIndex(value.getType()));
			type.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					value.setType(SSOTypes.valueOf(type.getText()));
				}
			});

			label = new Label(composite, SWT.NONE);
			label.setText("URL");

			final Text turi = new Text(composite, SWT.BORDER);
			turi.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			turi.setText(Misc.nvl(value.getUrl()));
			turi.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					value.setUrl(turi.getText());
					getButton(IDialogConstants.OK_ID).setEnabled(!Misc.isNullOrEmpty(value.getUrl()));
				}
			});

			label = new Label(composite, SWT.NONE);
			label.setText("Username");

			final Text tname = new Text(composite, SWT.BORDER);
			tname.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			tname.setText(Misc.nvl(value.getUser()));
			tname.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					value.setUser(tname.getText());
				}
			});

			label = new Label(composite, SWT.NONE);
			label.setText("Password");

			tpass = new WSecretText(composite, SWT.BORDER | SWT.PASSWORD);
			tpass.loadSecret(JRServerSecretsProvider.SECRET_NODE_ID, Misc.nvl(value.getPassword()));
			tpass.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			tpass.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					value.setPassword(tpass.getText());
					value.setPassuuid(tpass.getUUIDKey());
				}
			});

			applyDialogFont(composite);
			UIUtils.getDisplay().asyncExec(new Runnable() {
				public void run() {
					getButton(IDialogConstants.OK_ID).setEnabled(!Misc.isNullOrEmpty(value.getUrl()));
				}
			});
			return composite;
		}
	}

	private List<SSOServer> items = new ArrayList<SSOServer>();

	@Override
	protected String[] getNewInputObject() {
		PEditDialog dialog = new PEditDialog(UIUtils.getShell(), new SSOServer());
		if (dialog.open() == Window.OK) {
			SSOServer srv = dialog.getValue();
			items.add(srv);
			return new String[] { srv.getType().name(), srv.getUrl() };
		}
		return null;
	}

	public static Mapping mapping = new Mapping();
	static {
		mapping.loadMapping(new InputSource(CASListFieldEditor.class.getResourceAsStream("/com/jaspersoft/studio/server/preferences/SSOServer.xml")));
	}

	public static Mapping getMapping() {
		return mapping;
	}

	protected void doStore() {
		String v = "";
		for (SSOServer srv : items) {
			v += Base64.encodeBase64String(CastorUtil.write(srv, mapping).getBytes()) + "\n";
			try {
				SecureStorageUtils.saveToDefaultSecurePreferences(JRServerSecretsProvider.SECRET_NODE_ID, srv.getPassuuid(), srv.getPassword());
			} catch (StorageException e) {
				e.printStackTrace();
			}
		}
		getPreferenceStore().setValue(CASPreferencePage.CAS, v);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoad() {
		if (getTable() != null) {
			items.clear();
			String v = null;
			v = getPreferenceStore().getString(CASPreferencePage.CAS);
			for (String line : v.split("\n")) {
				if (line.isEmpty())
					continue;
				try {
					SSOServer srv = (SSOServer) CastorUtil.read(new ByteArrayInputStream(Base64.decodeBase64(line)), mapping);
					items.add(srv);
					TableItem tableItem = new TableItem(getTable(), SWT.NONE);
					tableItem.setText(new String[] { srv.getType().name(), srv.getUrl() });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// Add an help listener to the table
			TableHelpListener.setTableHelp(getTable());
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoadDefault() {

	}

	@Override
	protected boolean isFieldEditable(int col, int row) {
		return false;
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
				} else if (widget == table) {
					selectionChanged();
				}
			}
		};
	}

	@Override
	protected Button createPushButton(Composite parent, String key) {
		if (key.equals(Messages.common_duplicate))
			return null;
		return super.createPushButton(parent, key);
	}

	@Override
	protected void handleTableDoubleClick() {
		editPressed();
	}

	@Override
	protected void removePressed() {
		int selIdx = table.getSelectionIndex();
		if (selIdx >= 0)
			items.remove(selIdx);
		super.removePressed();
	}

	private void editPressed() {
		int selIdx = table.getSelectionIndex();
		if (selIdx != -1) {
			TableItem item = table.getItem(selIdx);
			SSOServer srv = (SSOServer) items.get(selIdx).clone();
			PEditDialog dialog = new PEditDialog(UIUtils.getShell(), srv);
			if (dialog.open() == Window.OK) {
				srv = dialog.getValue();
				items.set(selIdx, srv);
				item.setText(0, srv.getType().name());
				item.setText(1, srv.getUrl());
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
