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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.builder.jdt.JDTUtils;
import net.sf.jasperreports.eclipse.util.ResourcePreferences;

import org.eclipse.core.internal.resources.ProjectPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.internal.dialogs.PropertyDialog;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.PreferenceInitializer;

public abstract class FieldEditorOverlayPage extends FieldEditorPreferencePage implements IWorkbenchPropertyPage,
		IWorkbenchPreferencePage {
	public static final String RESOURCE = "resource";

	private static final String PROJECT = "project";

	public static final String USERESOURCESETTINGS = "useResourceSettings"; //$NON-NLS-1$

	// Stores all created field editors
	private List<FieldEditor> editors = new ArrayList<FieldEditor>();

	// Stores owning element of properties
	private IAdaptable element;

	// Additional buttons for property pages
	private Button useWorkspaceSettingsButton, useProjectSettingsButton, useResourceSettingsButton, confWkspButton,
			confPrjButton;

	// Overlay preference store for property pages
	private ScopedPreferenceStore overlayStore;

	// The image descriptor of this pages title image
	private ImageDescriptor image;

	// Cache for page id
	private String pageId;

	private Composite selectionComposite;

	/**
	 * Constructor
	 * 
	 * @param style
	 *          - layout style
	 */
	public FieldEditorOverlayPage(int style) {
		super(style);
	}

	/**
	 * Constructor
	 * 
	 * @param title
	 *          - title string
	 * @param style
	 *          - layout style
	 */
	public FieldEditorOverlayPage(String title, int style) {
		super(title, style);
	}

	/**
	 * Constructor
	 * 
	 * @param title
	 *          - title string
	 * @param image
	 *          - title image
	 * @param style
	 *          - layout style
	 */
	public FieldEditorOverlayPage(String title, ImageDescriptor image, int style) {
		super(title, image, style);
		this.image = image;
	}

	/**
	 * Returns the id of the current preference page as defined in plugin.xml Subclasses must implement.
	 * 
	 * @return - the qualifier
	 */
	protected abstract String getPageId();

	/**
	 * Receives the object that owns the properties shown in this property page.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
	 */
	public void setElement(IAdaptable element) {
		this.element = element;
	}

	/**
	 * Delivers the object that owns the properties shown in this property page.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
	 */
	public IAdaptable getElement() {
		return element;
	}

	/**
	 * Returns true if this instance represents a property page
	 * 
	 * @return - true for property pages, false for preference pages
	 */
	public boolean isPropertyPage() {
		return getElement() != null;
	}

	public boolean isResourcePage() {
		return JDTUtils.isOrCanAdaptTo(getElement(), IFile.class);
	}

	/**
	 * We override the addField method. This allows us to store each field editor added by subclasses in a list for later
	 * processing.
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#addField(org.eclipse.jface.preference.FieldEditor)
	 */
	protected void addField(FieldEditor editor) {
		editors.add(editor);
		super.addField(editor);
	}

	/**
	 * We override the createControl method. In case of property pages we create a new PropertyStore as local preference
	 * store. After all control have been create, we enable/disable these controls.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createControl()
	 */
	public void createControl(Composite parent) {
		// Special treatment for property pages
		if (isPropertyPage()) {
			// Cache the page id
			pageId = JaspersoftStudioPlugin.getUniqueIdentifier();// getPageId();
			// Create an overlay preference store and fill it with properties
			overlayStore = JaspersoftStudioPlugin.getInstance().getPreferenceStore(getResource(), pageId);
			// overlayStore = new PropertyStore((IResource) getElement(), super.getPreferenceStore(), pageId);
			// Set overlay store as current preference store
			PreferenceInitializer.initDefaultProperties(overlayStore);
		}
		super.createControl(parent);
		// Update state of all subclass controls
		if (isPropertyPage())
			updateFieldEditors();
	}

	protected IResource getResource() {
		IResource resource = null;
		if (getElement() instanceof IResource)
			resource = (IResource) getElement();
		else if (getElement() instanceof IFileEditorInput)
			resource = ((IFileEditorInput) getElement()).getFile();
		else
			resource = (IResource) getElement().getAdapter(IResource.class);
		return resource;
	}

	/**
	 * We override the createContents method. In case of property pages we insert two radio buttons at the top of the
	 * page.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		if (isPropertyPage())
			createSelectionGroup(parent);
		return super.createContents(parent);
	}

	/**
	 * Creates and initializes a selection group with two choice buttons and one push button.
	 * 
	 * @param parent
	 *          - the parent composite
	 */
	private void createSelectionGroup(Composite parent) {
		selectionComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		selectionComposite.setLayout(layout);

		useWorkspaceSettingsButton = createRadioButton(selectionComposite, Messages.FieldEditorOverlayPage_2);
		if (isPropertyPage()) {
			confWkspButton = new Button(selectionComposite, SWT.PUSH);
			confWkspButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			confWkspButton.setText(Messages.FieldEditorOverlayPage_4);
			confWkspButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					configureWorkspaceSettings(null);
				}
			});
		}
		useProjectSettingsButton = createRadioButton(selectionComposite, Messages.FieldEditorOverlayPage_3);
		if (isResourcePage()) {

			confPrjButton = new Button(selectionComposite, SWT.PUSH);
			confPrjButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			confPrjButton.setText("Configure Project Settings");
			confPrjButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					configureWorkspaceSettings(((IFile) element).getProject());
				}
			});

			useResourceSettingsButton = createRadioButton(selectionComposite, "Use File Settings");
		}

		// Set workspace/project radio buttons
		setupWPREnabled();
	}

	protected void setupWPREnabled() {
		try {
			useWorkspaceSettingsButton.setSelection(false);
			useProjectSettingsButton.setSelection(false);
			if (useResourceSettingsButton != null)
				useResourceSettingsButton.setSelection(false);

			confWkspButton.setEnabled(false);
			if (confPrjButton != null)
				confPrjButton.setEnabled(false);

			IResource r = getResource();
			String use = r != null ? r.getPersistentProperty(new QualifiedName(pageId, USERESOURCESETTINGS)) : "";
			if (PROJECT.equals(use)) {
				useProjectSettingsButton.setSelection(true);
				if (confPrjButton != null)
					confPrjButton.setEnabled(false);
			} else if (RESOURCE.equals(use)) {
				if (useResourceSettingsButton != null)
					useResourceSettingsButton.setSelection(true);
			} else {
				useWorkspaceSettingsButton.setSelection(true);
				confWkspButton.setEnabled(true);
			}
		} catch (CoreException e) {
			useWorkspaceSettingsButton.setSelection(true);
		}
	}

	/**
	 * Convenience method creating a radio button
	 * 
	 * @param parent
	 *          - the parent composite
	 * @param label
	 *          - the button label
	 * @return - the new button
	 */
	private Button createRadioButton(Composite parent, String label) {
		final Button button = new Button(parent, SWT.RADIO);
		button.setText(label);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				confWkspButton.setEnabled(button == useWorkspaceSettingsButton);
				if (confPrjButton != null)
					confPrjButton.setEnabled(button == useProjectSettingsButton);
				updateFieldEditors();
				FieldEditorOverlayPage.super.performDefaults();
			}
		});
		return button;
	}

	/**
	 * Returns in case of property pages the overlay store, in case of preference pages the standard preference store
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		if (getElement() != null) {
			IResource r = getResource();
			if (useProjectSettingsButton != null && useProjectSettingsButton.getSelection()) {
				if (r instanceof IFile)
					r = r.getProject();
				return JaspersoftStudioPlugin.getInstance().getPreferenceStore(r, pageId);
			} else if (useResourceSettingsButton != null && useResourceSettingsButton.getSelection())
				return JaspersoftStudioPlugin.getInstance().getPreferenceStore(r, pageId);
		}
		return super.getPreferenceStore();
	}

	/*
	 * Enables or disables the field editors and buttons of this page
	 */
	private void updateFieldEditors() {
		initialize();
		// We iterate through all field editors
		// setupWPREnabled();
		boolean enabled = false;
		if (isResourcePage())
			enabled = useResourceSettingsButton.getSelection();
		else if (isPropertyPage())
			enabled = useProjectSettingsButton.getSelection();

		enableComposite(getFieldEditorParent(), enabled);
	}

	private void enableComposite(Composite parent, boolean enabled) {
		for (Control c : parent.getChildren()) {
			c.setEnabled(enabled);
			if (c instanceof Composite)
				enableComposite((Composite) c, enabled);
		}
	}

	/**
	 * We override the performOk method. In case of property pages we copy the values in the overlay store into the
	 * property values of the selected project. We also save the state of the radio buttons.
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean result = super.performOk();
		if (result && isPropertyPage()) {
			IResource resource = JDTUtils.getAdaptedObject(getElement(), IResource.class);
			if (resource != null) {
				try {
					String value = "workspace";
					if (useProjectSettingsButton.getSelection())
						value = PROJECT;
					if (useResourceSettingsButton != null && useResourceSettingsButton.getSelection())
						value = RESOURCE;
					resource.setPersistentProperty(new QualifiedName(pageId, USERESOURCESETTINGS), value);

					for (IEclipsePreferences ep : overlayStore.getPreferenceNodes(true)) {
						try {
							if (useResourceSettingsButton != null && !useResourceSettingsButton.getSelection()
									&& ep instanceof ResourcePreferences) {
								ep.clear();
							}
							if (!useProjectSettingsButton.getSelection() && ep instanceof ProjectPreferences) {
								ep.clear();
							}
							ep.flush();
						} catch (BackingStoreException e) {
							JaspersoftStudioPlugin.getInstance().logError("An error occurred while try to store back preferences", e);
						}
					}
				} catch (CoreException e) {
					JaspersoftStudioPlugin.getInstance().logError("An error occurred while try to store back preferences", e);
				}
			}
		}
		return result;
	}

	/**
	 * We override the performDefaults method. In case of property pages we switch back to the workspace settings and
	 * disable the field editors.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		if (isPropertyPage()) {
			useWorkspaceSettingsButton.setSelection(true);
			useProjectSettingsButton.setSelection(false);
			if (useResourceSettingsButton != null)
				useResourceSettingsButton.setSelection(false);
			confWkspButton.setEnabled(true);
			if (confPrjButton != null)
				confPrjButton.setEnabled(false);
			updateFieldEditors();
		}
		super.performDefaults();
	}

	/**
	 * Creates a new preferences page and opens it
	 * 
	 * @see com.bdaum.SpellChecker.preferences.SpellCheckerPreferencePage#configureWorkspaceSettings()
	 */
	protected void configureWorkspaceSettings(IProject project) {
		try {
			// create a new instance of the current class
			FieldEditorOverlayPage page = (FieldEditorOverlayPage) this.getClass().newInstance();
			page.setTitle(getTitle());
			page.setImageDescriptor(image);
			page.setElement(project);
			// and show it
			showPreferencePage(pageId, page, project);
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
	protected void showPreferencePage(String id, IPreferencePage page, final IProject project) {
		final IPreferenceNode targetNode = new PreferenceNode(id, page);
		PreferenceManager manager = new PreferenceManager();
		manager.addToRoot(targetNode);
		Shell shell = getControl().getShell();
		final PreferenceDialog dialog = project == null ? new PreferenceDialog(shell, manager) : new PropertyDialog(shell,
				manager, new StructuredSelection(project));
		BusyIndicator.showWhile(getControl().getDisplay(), new Runnable() {
			public void run() {
				dialog.create();
				dialog.setMessage(targetNode.getLabelText());
				dialog.open();
			}
		});
	}
}
