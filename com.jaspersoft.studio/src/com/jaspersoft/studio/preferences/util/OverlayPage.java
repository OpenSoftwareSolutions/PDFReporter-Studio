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
package com.jaspersoft.studio.preferences.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

public abstract class OverlayPage extends PropertyPage {

	public static final String USEPROJECTSETTINGS = "useProjectSettings"; //$NON-NLS-1$

	private static final String FALSE = "false"; //$NON-NLS-1$
	private static final String TRUE = "true"; //$NON-NLS-1$

	private Button useWorkspaceSettingsButton, useProjectSettingsButton, configureButton;

	private ScopedPreferenceStore overlayStore;

	private ImageDescriptor image;

	private String pageId;

	private Composite contents;

	public OverlayPage() {
		super();
	}

	public OverlayPage(String title) {
		super();
		setTitle(title);
	}

	public OverlayPage(String title, ImageDescriptor image) {
		super();
		setTitle(title);
		setImageDescriptor(image);
		this.image = image;
	}

	protected abstract String getPageId();

	public boolean isPropertyPage() {
		return getElement() != null;
	}

	protected Control createContents(Composite parent) {
		if (isPropertyPage())
			createSelectionGroup(parent);
		contents = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		contents.setLayout(layout);
		contents.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return contents;
	}

	private void createSelectionGroup(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Composite radioGroup = new Composite(comp, SWT.NONE);
		radioGroup.setLayout(new GridLayout());
		radioGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		useWorkspaceSettingsButton = createRadioButton(radioGroup, "Use_Workspace_Settings");
		useProjectSettingsButton = createRadioButton(radioGroup, " Use Project Settings");
		configureButton = new Button(comp, SWT.PUSH);
		configureButton.setText("Configure Workspace Settings");
		configureButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				configureWorkspaceSettings();
			}
		});
		try {
			String use = ((IResource) getElement()).getPersistentProperty(new QualifiedName(pageId, USEPROJECTSETTINGS));
			if (TRUE.equals(use)) {
				useProjectSettingsButton.setSelection(true);
				configureButton.setEnabled(false);
			} else
				useWorkspaceSettingsButton.setSelection(true);
		} catch (CoreException e) {
			useWorkspaceSettingsButton.setSelection(true);
		}
	}

	private Button createRadioButton(Composite parent, String label) {
		final Button button = new Button(parent, SWT.RADIO);
		button.setText(label);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				configureButton.setEnabled(button == useWorkspaceSettingsButton);
				setControlsEnabled();
			}
		});
		return button;
	}

	public void createControl(Composite parent) {
		// Special treatment for property pages
		if (isPropertyPage()) {
			// Cache the page id
			pageId = getPageId();
			// Create an overlay preference store and fill it with properties
			overlayStore = JaspersoftStudioPlugin.getInstance().getPreferenceStore((IProject) getElement(), pageId);
			// Set overlay store as current preference store
		}
		super.createControl(parent);
		// Update enablement of all subclass controls
		if (isPropertyPage())
			setControlsEnabled();
	}

	/*
	 * Returns in case of property pages the overlay store - otherwise the standard preference store
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		if (isPropertyPage())
			return overlayStore;
		return super.getPreferenceStore();
	}

	/**
	 * Enables or disables the controls of this page
	 */
	private void setControlsEnabled() {
		boolean enabled = useProjectSettingsButton.getSelection();
		setControlsEnabled(enabled);
	}

	/**
	 * Enables or disables the controls of this page Subclasses may override.
	 * 
	 * @param enabled
	 *          - true if controls shall be enabled
	 */
	protected void setControlsEnabled(boolean enabled) {
		setControlsEnabled(contents, enabled);
	}

	/**
	 * Enables or disables a tree of controls starting at the specified root. We spare tabbed notebooks and pagebooks to
	 * allow for user navigation.
	 * 
	 * @param root
	 *          - the root composite
	 * @param enabled
	 *          - true if controls shall be enabled
	 */
	private void setControlsEnabled(Composite root, boolean enabled) {
		Control[] children = root.getChildren();
		for (int i = 0; i < children.length; i++) {
			Control child = children[i];
			if (!(child instanceof CTabFolder) && !(child instanceof TabFolder) && !(child instanceof PageBook))
				child.setEnabled(enabled);
			if (child instanceof Composite)
				setControlsEnabled((Composite) child, enabled);
		}
	}

	/**
	 * We override the performOk method. In case of property pages we save the state of the radio buttons.
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean result = super.performOk();
		if (result && isPropertyPage()) {
			// Save state of radiobuttons in project properties
			IResource resource = (IResource) getElement();
			try {
				String value = (useProjectSettingsButton.getSelection()) ? TRUE : FALSE;
				resource.setPersistentProperty(new QualifiedName(pageId, USEPROJECTSETTINGS), value);
			} catch (CoreException e) {
			}
		}
		return result;
	}

	/**
	 * We override the performDefaults method. In case of property pages we switch back to the workspace settings and
	 * disable the page controls.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		if (isPropertyPage()) {
			useWorkspaceSettingsButton.setSelection(true);
			useProjectSettingsButton.setSelection(false);
			configureButton.setEnabled(true);
			setControlsEnabled();
		}
		super.performDefaults();
	}

	/**
	 * Creates a new preferences page and opens it
	 * 
	 * @see com.bdaum.SpellChecker.preferences.SpellCheckerPreferencePage#configureWorkspaceSettings()
	 */
	protected void configureWorkspaceSettings() {
		try {
			// create a new instance of the current class
			IPreferencePage page = (IPreferencePage) this.getClass().newInstance();
			page.setTitle(getTitle());
			page.setImageDescriptor(image);
			// and show it
			showPreferencePage(pageId, page);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show a single preference pages
	 * 
	 * @param id
	 *          - the preference page identification
	 * @param page
	 *          - the preference page
	 */
	protected void showPreferencePage(String id, IPreferencePage page) {
		final IPreferenceNode targetNode = new PreferenceNode(id, page);
		PreferenceManager manager = new PreferenceManager();
		manager.addToRoot(targetNode);
		final PreferenceDialog dialog = new PreferenceDialog(getControl().getShell(), manager);
		BusyIndicator.showWhile(getControl().getDisplay(), new Runnable() {
			public void run() {
				dialog.create();
				dialog.setMessage(targetNode.getLabelText());
				dialog.open();
			}
		});
	}

}
