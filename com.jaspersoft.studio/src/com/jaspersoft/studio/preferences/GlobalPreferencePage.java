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
package com.jaspersoft.studio.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

public class GlobalPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String JSS_JETTY_PORT = "com.jaspersoft.studio.jetty.port"; //$NON-NLS-1$
	public static final String JSS_USE_SECURE_STORAGE = "com.jaspersoft.studio.secure.storage"; //$NON-NLS-1$
	public static final String JSS_ENABLE_INTERNAL_CONSOLE = "com.jaspersoft.studio.jss.console"; //$NON-NLS-1$

	public GlobalPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		createNoteComposite(getFont(), parent, "", Messages.GlobalPreferencePage_NoteMsg); //$NON-NLS-1$
		return contents;
	}
	
	@Override
	protected void createFieldEditors() {
		Label lbl = new Label(getFieldEditorParent(), SWT.NONE);
		lbl.setText(Messages.GlobalPreferencePage_jettyServerTitle);
		lbl.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));

		IntegerFieldEditor port = new IntegerFieldEditor(JSS_JETTY_PORT, Messages.GlobalPreferencePage_port, getFieldEditorParent());
		port.setValidRange(0, 49151);
		addField(port);
		
		Label separator = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		
		Label securityTitle = new Label(getFieldEditorParent(), SWT.NONE);
		securityTitle.setText(Messages.GlobalPreferencePage_title);
		securityTitle.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		
		BooleanFieldEditor useSecStorage = new BooleanFieldEditor(JSS_USE_SECURE_STORAGE, Messages.GlobalPreferencePage_flagDescription,getFieldEditorParent());
		addField(useSecStorage);
		
		Label separator2 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		separator2.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		
		Label debuggingTitle = new Label(getFieldEditorParent(), SWT.NONE);
		debuggingTitle.setText(Messages.GlobalPreferencePage_LoggingPrefs);
		debuggingTitle.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
		
		BooleanFieldEditor enableJSSConsole = new BooleanFieldEditor(JSS_ENABLE_INTERNAL_CONSOLE, Messages.GlobalPreferencePage_JSSConsoleFieldLabel,getFieldEditorParent());
		enableJSSConsole.getDescriptionControl(getFieldEditorParent()).setToolTipText(Messages.GlobalPreferencePage_JSSConsoleFieldTooltip);
		addField(enableJSSConsole);
		
	}
	
	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(JSS_JETTY_PORT, 0);
		store.setDefault(JSS_USE_SECURE_STORAGE, true);
		store.setDefault(JSS_ENABLE_INTERNAL_CONSOLE, false);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

}
