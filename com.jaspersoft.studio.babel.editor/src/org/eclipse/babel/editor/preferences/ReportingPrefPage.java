/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.preferences;

import org.eclipse.babel.messages.Messages;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Plugin preference page for reporting/performance options.
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class ReportingPrefPage extends AbstractPrefPage {

    /* Preference fields. */
    private Combo reportMissingVals;
    private Combo reportDuplVals;
    private Combo reportSimVals;
    private Text reportSimPrecision;
    private Button[] reportSimValsMode = new Button[2];

    /**
     * Constructor.
     */
    public ReportingPrefPage() {
        super();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {
        IPreferenceStore prefs = getPreferenceStore();
        Composite field = null;
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));

        new Label(composite, SWT.NONE).setText(Messages.prefs_perform_intro1);
        new Label(composite, SWT.NONE).setText(Messages.prefs_perform_intro2);
        new Label(composite, SWT.NONE).setText(" "); //$NON-NLS-1$

        // Report missing values?
        field = createFieldComposite(composite);
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        field.setLayoutData(gridData);
        new Label(field, SWT.NONE).setText(Messages.prefs_perform_missingVals);
        reportMissingVals = new Combo(field, SWT.READ_ONLY);
        populateCombo(reportMissingVals,
                prefs.getInt(MsgEditorPreferences.REPORT_MISSING_VALUES_LEVEL));
        // reportMissingVals.setSelection(
        // prefs.getBoolean(MsgEditorPreferences.REPORT_MISSING_VALUES));

        // Report duplicate values?
        field = createFieldComposite(composite);
        gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        field.setLayoutData(gridData);
        new Label(field, SWT.NONE).setText(Messages.prefs_perform_duplVals);
        reportDuplVals = new Combo(field, SWT.READ_ONLY);
        populateCombo(reportDuplVals, prefs.getInt(MsgEditorPreferences.REPORT_DUPL_VALUES_LEVEL));

        // Report similar values?
        field = createFieldComposite(composite);
        gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        field.setLayoutData(gridData);

        new Label(field, SWT.NONE).setText(Messages.prefs_perform_simVals);
        reportSimVals = new Combo(field, SWT.READ_ONLY);
        populateCombo(reportSimVals, prefs.getInt(MsgEditorPreferences.REPORT_SIM_VALUES_LEVEL));
        reportSimVals.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                refreshEnabledStatuses();
            }
        });

        Composite simValModeGroup = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = indentPixels;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        simValModeGroup.setLayout(gridLayout);

        // Report similar values: word count
        reportSimValsMode[0] = new Button(simValModeGroup, SWT.RADIO);
        reportSimValsMode[0]
                .setSelection(prefs
                        .getBoolean(MsgEditorPreferences.REPORT_SIM_VALUES_WORD_COMPARE));
        new Label(simValModeGroup, SWT.NONE).setText(Messages.prefs_perform_simVals_wordCount);

        // Report similar values: Levensthein
        reportSimValsMode[1] = new Button(simValModeGroup, SWT.RADIO);
        reportSimValsMode[1]
                .setSelection(prefs
                        .getBoolean(MsgEditorPreferences.REPORT_SIM_VALUES_LEVENSTHEIN));
        new Label(simValModeGroup, SWT.NONE).setText(Messages.prefs_perform_simVals_levensthein);

        // Report similar values: precision level
        field = createFieldComposite(composite, indentPixels);
        new Label(field, SWT.NONE).setText(Messages.prefs_perform_simVals_precision);
        reportSimPrecision = new Text(field, SWT.BORDER);
        reportSimPrecision.setText(prefs
                .getString(MsgEditorPreferences.REPORT_SIM_VALUES_PRECISION));
        reportSimPrecision.setTextLimit(6);
        setWidthInChars(reportSimPrecision, 6);
        reportSimPrecision.addKeyListener(new DoubleTextValidatorKeyListener(Messages.prefs_perform_simVals_precision_error, 0, 1));

        refreshEnabledStatuses();

        return composite;
    }

    /**
     * Creates the items in the combo and select the item that matches the
     * current value.
     * 
     * @param combo
     * @param selectedLevel
     */
    private void populateCombo(Combo combo, int selectedLevel) {
        combo.add(Messages.prefs_perform_message_ignore);
        combo.add(Messages.prefs_perform_message_info);
        combo.add(Messages.prefs_perform_message_warning);
        combo.add(Messages.prefs_perform_message_error);
        combo.select(selectedLevel);
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.RIGHT;
        combo.setLayoutData(gridData);
    }

    /**
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    public boolean performOk() {
        IEclipsePreferences prefs = MsgEditorPreferences.getEclipsePreferenceStore();
        prefs.putInt(MsgEditorPreferences.REPORT_MISSING_VALUES_LEVEL, reportMissingVals.getSelectionIndex());
        prefs.putInt(MsgEditorPreferences.REPORT_DUPL_VALUES_LEVEL, reportDuplVals.getSelectionIndex());
        prefs.putInt(MsgEditorPreferences.REPORT_SIM_VALUES_LEVEL, reportSimVals.getSelectionIndex());
        prefs.putBoolean(MsgEditorPreferences.REPORT_SIM_VALUES_WORD_COMPARE,reportSimValsMode[0].getSelection());
        prefs.putBoolean(MsgEditorPreferences.REPORT_SIM_VALUES_LEVENSTHEIN, reportSimValsMode[1].getSelection());
        double precision = 0.75d;
        try{
        	double textPrecision = Double.parseDouble(reportSimPrecision.getText());
        	precision = textPrecision;
        } catch (NumberFormatException ex){}
        prefs.putDouble(MsgEditorPreferences.REPORT_SIM_VALUES_PRECISION, precision);
        try {
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
        refreshEnabledStatuses();
        return super.performOk();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        reportMissingVals.select(MsgEditorPreferences.VALIDATION_MESSAGE_ERROR);
        reportDuplVals.select(MsgEditorPreferences.VALIDATION_MESSAGE_WARNING);
        reportSimVals.select(IPreferenceStore.INT_DEFAULT_DEFAULT);
        reportSimValsMode[0].setSelection(true);
        reportSimValsMode[1].setSelection(IPreferenceStore.BOOLEAN_DEFAULT_DEFAULT);
        reportSimPrecision.setText(Double.toString(0.75d));
        refreshEnabledStatuses();
        super.performDefaults();
    }

    /* default */void refreshEnabledStatuses() {
        boolean isReportingSimilar = reportSimVals.getSelectionIndex() != MsgEditorPreferences.VALIDATION_MESSAGE_IGNORE;

        for (int i = 0; i < reportSimValsMode.length; i++) {
            reportSimValsMode[i].setEnabled(isReportingSimilar);
        }
        reportSimPrecision.setEnabled(isReportingSimilar);
    }

}
