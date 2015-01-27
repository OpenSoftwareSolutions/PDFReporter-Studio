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
package com.jaspersoft.studio.preferences.theme;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.utils.Misc;

/*
 * 
 */
public class ThemesPreferencePage extends FieldEditorOverlayPage {

	public static final String P_THEME_UI = "com.jaspersoft.studio.theme.ui"; //$NON-NLS-1$
	public static final String P_THEMES_UI = "com.jaspersoft.studio.themes.ui"; //$NON-NLS-1$

	public ThemesPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.ThemesPreferencePage_themsTitle);
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		Composite cmp = new Composite(getFieldEditorParent(), SWT.NONE);

		String[][] tmatrix = getThemes4Combo();

		comboeditor = new TComboEditor(P_THEME_UI, Messages.ThemesPreferencePage_themesLabel, tmatrix, cmp) {
			@Override
			protected void doLoad() {
				super.doLoad();
				setButtonsEnabled();
			}
		};
		addField(comboeditor);
		cmp.setLayout(new GridLayout(3, false));

		Composite bcmp = new Composite(cmp, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		bcmp.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		bcmp.setLayoutData(gd);

		Button bnew = new Button(bcmp, SWT.PUSH);
		bnew.setText(Messages.ThemesPreferencePage_newButton);
		bnew.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bnew.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String themes = getPreferenceStore().getString(P_THEMES_UI);
				NewDialog dialog = new NewDialog(Display.getDefault().getActiveShell());
				dialog.create();
				if (dialog.open() == Dialog.OK) {
					ATheme theme = ATheme.load(getPreferenceStore(), comboeditor.getValue());
					if (theme != null) {
						theme.setName(dialog.getThemename());
						getPreferenceStore().setValue(P_THEMES_UI, themes + ";" + dialog.getThemename()); //$NON-NLS-1$
						theme.save(getPreferenceStore());
						comboeditor.refresh(getThemes4Combo());
						comboeditor.setSelection(dialog.getThemename());
					}
				}
			}
		});

		bdel = new Button(bcmp, SWT.PUSH);
		bdel.setText(Messages.ThemesPreferencePage_removeButton);
		bdel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bdel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ATheme theme = ATheme.load(getPreferenceStore(), comboeditor.getValue());
				if (theme != null) {
					String themes = getPreferenceStore().getString(P_THEMES_UI);
					String value = ""; //$NON-NLS-1$
					String[] tms = themes.split(";"); //$NON-NLS-1$
					for (String t : tms) {
						if (t.equals(theme.getName()))
							continue;
						if (!value.isEmpty())
							value += ";"; //$NON-NLS-1$
						value += t;
					}
					getPreferenceStore().setValue(P_THEMES_UI, value);
					getPreferenceStore().setValue(P_THEME_UI, LightTheme.NAME);
					comboeditor.refresh(getThemes4Combo());
				}
			}
		});

		createThemeWidgets(getTheme(), cmp);

		getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
				if (event.getProperty().equals(P_THEME_UI)) {
					ATheme theme = ATheme.load(getPreferenceStore(), (String) event.getNewValue());
					if (theme != null)
						theme.apply(getPreferenceStore());
				}
			}
		});

		comboListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setButtonsEnabled();
			}
		};
		comboeditor.addSelectionListener(comboListener);
	}

	class NewDialog extends Dialog {
		public NewDialog(Shell shell) {
			super(shell);
		}

		private String themename = Messages.ThemesPreferencePage_themeExampleName;
		private Text tname;

		public String getThemename() {
			return themename;
		}

		public void setThemename(String themename) {
			this.themename = themename;
		}

		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(Messages.ThemesPreferencePage_newThemeDialogTitle);
		}

		@Override
		protected Control createDialogArea(Composite parent) {

			Composite cmp = new Composite(parent, SWT.NONE);
			cmp.setLayout(new GridLayout(2, false));
			cmp.setLayoutData(new GridData(GridData.FILL_BOTH));

			new Label(cmp, SWT.NONE).setText(Messages.ThemesPreferencePage_newThemeNameLabel);

			tname = new Text(cmp, SWT.BORDER);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalIndent = 16;
			tname.setLayoutData(gd);

			return cmp;
		}

		@Override
		protected Control createContents(Composite parent) {
			Control control = super.createContents(parent);

			DataBindingContext binding = new DataBindingContext();

			ControlDecoration controlDecoration = new ControlDecoration(tname, SWT.LEFT | SWT.TOP);
			controlDecoration.setDescriptionText(Messages.ThemesPreferencePage_duplicateName);
			FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
					FieldDecorationRegistry.DEC_ERROR);
			controlDecoration.setImage(fieldDecoration.getImage());

			binding.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(this, "themename"), //$NON-NLS-1$
					new UpdateValueStrategy().setAfterConvertValidator(new StringRequiredValidator(Messages.ThemesPreferencePage_enternameMessage,
							controlDecoration, getButton(IDialogConstants.OK_ID))), null);

			return control;
		}
	}

	class StringRequiredValidator implements IValidator {
		private String[] themes = getPreferenceStore().getString(P_THEMES_UI).split(";"); //$NON-NLS-1$
		private final String errorText;
		private final ControlDecoration controlDecoration;
		private Button okButton;

		public StringRequiredValidator(String errorText, ControlDecoration controlDecoration, Button okButton) {
			super();
			this.okButton = okButton;
			this.errorText = errorText;
			this.controlDecoration = controlDecoration;
		}

		public IStatus validate(Object value) {
			if (value instanceof String) {
				String text = (String) value;
				if (text.trim().length() == 0) {
					controlDecoration.show();
					okButton.setEnabled(false);
					return ValidationStatus.error(errorText);
				}
				for (String t : themes) {
					if (t.equals(value)) {
						controlDecoration.show();
						okButton.setEnabled(false);
						return ValidationStatus.error(errorText);
					}
				}
			}
			okButton.setEnabled(true);
			controlDecoration.hide();
			return Status.OK_STATUS;
		}
	}

	private void createThemeWidgets(ATheme theme, Composite cmp) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;

		tcontrol = theme.createControl(cmp, getPreferenceStore());
		tcontrol.setLayoutData(gd);

		cmp.layout(true);
	}

	private String[][] getThemes4Combo() {
		String themes = getPreferenceStore().getString(P_THEMES_UI);
		String[] tms = themes.split(";"); //$NON-NLS-1$
		String[][] tmatrix = new String[tms.length][2];
		for (int i = 0; i < tms.length; i++) {
			tmatrix[i][0] = tms[i];
			tmatrix[i][1] = tms[i];
		}
		return tmatrix;
	}

	private SelectionAdapter comboListener;

	@Override
	public void dispose() {
		if (comboeditor != null && comboListener != null)
			comboeditor.removeSelectionListener(comboListener);
		super.dispose();
	}

	private ATheme getTheme() {
		return Misc.nvl(ATheme.load(getPreferenceStore(), getPreferenceStore().getString(P_THEME_UI)), new LightTheme());
	}

	public static void getDefaults(IPreferenceStore store) {
		LightTheme lt = new LightTheme();
		lt.save(store);
		DarkTheme dt = new DarkTheme();
		dt.save(store);

		store.setDefault(P_THEMES_UI, lt.getName() + ";" + dt.getName()); //$NON-NLS-1$
		store.setDefault(P_THEME_UI, lt.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	public static final String PAGE_ID = "com.jaspersoft.studio.preferences.theme.ThemesPreferencePage.property"; //$NON-NLS-1$
	private TComboEditor comboeditor;
	private Button bdel;
	private Composite tcontrol;

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}

	private void setButtonsEnabled() {
		Composite cmp = tcontrol.getParent();
		ATheme theme = ATheme.load(getPreferenceStore(), comboeditor.getValue());
		if (theme != null) {
			bdel.setEnabled(!(theme.getName().equals(LightTheme.NAME) || theme.getName().equals(DarkTheme.NAME)));
			tcontrol.dispose();
			createThemeWidgets(theme, cmp);

		}
	}
}
