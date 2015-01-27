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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Plugin generic preference page.
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class GeneralPrefPage extends AbstractPrefPage {

    /* Preference fields. */
    private Text keyGroupSeparator;

    private Text filterLocales;

    private Button convertEncodedToUnicode;

    private Button supportNL;
    // private Button supportFragments;
    // private Button loadOnlyFragmentResources;

    private Button keyTreeHierarchical;
    private Button keyTreeExpanded;

    private Button fieldTabInserts;

    // private Button noTreeInEditor;

    private Button setupRbeNatureAutomatically;

    /**
     * Constructor.
     */
    public GeneralPrefPage() {
        super();
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage
     *      #createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {

        // IPreferenceStore prefs = getPreferenceStore();
        Composite field = null;
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));

        // Key group separator
        field = createFieldComposite(composite);
        new Label(field, SWT.NONE).setText(Messages.prefs_groupSep); 
        keyGroupSeparator = new Text(field, SWT.BORDER);
        keyGroupSeparator.setText(MsgEditorPreferences.getGroupLevelSeparator());
        // prefs.getString(MsgEditorPreferences.GROUP__LEVEL_SEPARATOR));
        keyGroupSeparator.setTextLimit(2);

        field = createFieldComposite(composite);
        Label filterLocalesLabel = new Label(field, SWT.NONE);
        filterLocalesLabel.setText(Messages.prefs_filterLocales_label);
        filterLocalesLabel.setToolTipText(Messages.prefs_filterLocales_tooltip);
        filterLocales = new Text(field, SWT.BORDER);
        filterLocales.setText(MsgEditorPreferences.getFilterLocalesStringMatcher());
        // prefs.getString(MsgEditorPreferences.GROUP__LEVEL_SEPARATOR));
        filterLocales.setTextLimit(22);
        setWidthInChars(filterLocales, 16);

        // Convert encoded to unicode?
        field = createFieldComposite(composite);
        convertEncodedToUnicode = new Button(field, SWT.CHECK);
        convertEncodedToUnicode.setSelection(MsgEditorPreferences.getUnicodeEscapeEnabled());
        // prefs.getBoolean(MsgEditorPreferences.CONVERT_ENCODED_TO_UNICODE));
        new Label(field, SWT.NONE).setText(Messages.prefs_convertEncoded);

        // Support "NL" localization structure
        field = createFieldComposite(composite);
        supportNL = new Button(field, SWT.CHECK);
        supportNL.setSelection(MsgEditorPreferences.getNLSupportEnabled());
        // prefs.getBoolean(MsgEditorPreferences.SUPPORT_NL));
        new Label(field, SWT.NONE).setText(Messages.prefs_supportNL);

        // Setup rbe validation builder on java projects automatically.
        field = createFieldComposite(composite);
        setupRbeNatureAutomatically = new Button(field, SWT.CHECK);
        setupRbeNatureAutomatically.setSelection(MsgEditorPreferences.isBuilderSetupAutomatically());
        new Label(field, SWT.NONE).setText(Messages.prefs_setupValidationBuilderAutomatically);

        // Default key tree mode (tree vs flat)
        field = createFieldComposite(composite);
        keyTreeHierarchical = new Button(field, SWT.CHECK);
        keyTreeHierarchical.setSelection(MsgEditorPreferences.getKeyTreeHierarchical());
        new Label(field, SWT.NONE).setText(Messages.prefs_keyTree_hierarchical);

        // Default key tree expand status (expanded vs collapsed)
        field = createFieldComposite(composite);
        keyTreeExpanded = new Button(field, SWT.CHECK);
        keyTreeExpanded.setSelection(MsgEditorPreferences.getKeyTreeExpanded());
        new Label(field, SWT.NONE).setText(Messages.prefs_keyTree_expanded); 

        // Default tab key behaviour in text field
        field = createFieldComposite(composite);
        fieldTabInserts = new Button(field, SWT.CHECK);
        fieldTabInserts.setSelection(MsgEditorPreferences.getFieldTabInserts());
        // prefs.getBoolean(MsgEditorPreferences.FIELD_TAB_INSERTS));
        new Label(field, SWT.NONE).setText(Messages.prefs_fieldTabInserts);

        refreshEnabledStatuses();
        return composite;
    }

    /**
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    public boolean performOk() {
        IEclipsePreferences prefs = MsgEditorPreferences.getEclipsePreferenceStore();
        prefs.put(MsgEditorPreferences.GROUP__LEVEL_SEPARATOR, keyGroupSeparator.getText());
        prefs.put(MsgEditorPreferences.FILTER_LOCALES_STRING_MATCHERS,filterLocales.getText());
        prefs.putBoolean(MsgEditorPreferences.UNICODE_UNESCAPE_ENABLED, convertEncodedToUnicode.getSelection());
        prefs.putBoolean(MsgEditorPreferences.NL_SUPPORT_ENABLED,supportNL.getSelection());
        prefs.putBoolean(MsgEditorPreferences.ADD_MSG_EDITOR_BUILDER_TO_JAVA_PROJECTS, setupRbeNatureAutomatically.getSelection());
        prefs.putBoolean(MsgEditorPreferences.KEY_TREE_HIERARCHICAL, keyTreeHierarchical.getSelection());
        prefs.putBoolean(MsgEditorPreferences.KEY_TREE_EXPANDED,  keyTreeExpanded.getSelection());
        prefs.putBoolean(MsgEditorPreferences.FIELD_TAB_INSERTS, fieldTabInserts.getSelection());
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
        IPreferenceStore prefs = getPreferenceStore();
        keyGroupSeparator.setText(prefs
                .getDefaultString(MsgEditorPreferences.GROUP__LEVEL_SEPARATOR));
        filterLocales
                .setText(prefs
                        .getDefaultString(MsgEditorPreferences.FILTER_LOCALES_STRING_MATCHERS));
        convertEncodedToUnicode
                .setSelection(prefs
                        .getDefaultBoolean(MsgEditorPreferences.UNICODE_UNESCAPE_ENABLED));
        supportNL.setSelection(prefs
                .getDefaultBoolean(MsgEditorPreferences.NL_SUPPORT_ENABLED));
        keyTreeHierarchical.setSelection(prefs
                .getDefaultBoolean(MsgEditorPreferences.KEY_TREE_HIERARCHICAL));
        keyTreeHierarchical.setSelection(prefs
                .getDefaultBoolean(MsgEditorPreferences.KEY_TREE_EXPANDED));
        fieldTabInserts.setSelection(prefs
                .getDefaultBoolean(MsgEditorPreferences.FIELD_TAB_INSERTS));
        setupRbeNatureAutomatically
                .setSelection(prefs
                        .getDefaultBoolean(MsgEditorPreferences.ADD_MSG_EDITOR_BUILDER_TO_JAVA_PROJECTS));

        refreshEnabledStatuses();
        super.performDefaults();
    }

    private void refreshEnabledStatuses() {
    }

}
