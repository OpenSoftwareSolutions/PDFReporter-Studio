/*******************************************************************************
 * Copyright (c) 2012 Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexej Strelzow - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.core.refactoring;

import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import org.eclipse.swt.widgets.Text;

/**
 * The dialog between the user and the system. System wants to know what the new
 * name of the selected key is.
 * 
 * @author Alexej Strelzow
 */
public class KeyRefactoringDialog extends TitleAreaDialog {

    private final static String REGEXP_RESOURCE_KEY = "[\\p{Alnum}\\.]*";
	
    /*** Dialog Model ***/
    private DialogConfiguration config;
    private String selectedKey = "";

    public static final String ALL_LOCALES = "All available";

    /** GUI */
    private Button okButton;
    private Button cancelButton;

    private Label projectLabel;
    private Label resourceBundleLabel;
    private Label oldKeyLabel;
    private Label newKeyLabel;
    private Label languageLabel;

    private Text oldKeyText;
    private Text newKeyText;
    private Text projectText;
    private Text resourceBundleText;
    private Text languageText;

    /**
     * Meta data for the dialog.
     * 
     * @author Alexej Strelzow
     */
    public class DialogConfiguration {

        String projectName;
        String preselectedKey;
        String preselectedBundle;

        String newKey;
        String selectedLocale;

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getPreselectedKey() {
            return preselectedKey;
        }

        public void setPreselectedKey(String preselectedKey) {
            this.preselectedKey = preselectedKey;
        }

        public String getPreselectedBundle() {
            return preselectedBundle;
        }

        public void setPreselectedBundle(String preselectedBundle) {
            this.preselectedBundle = preselectedBundle;
        }

        public String getNewKey() {
            return newKey;
        }

        public void setNewKey(String newKey) {
            this.newKey = newKey;
        }

        public String getSelectedLocale() {
            return selectedLocale;
        }

        public void setSelectedLocale(String selectedLocale) {
            this.selectedLocale = selectedLocale;
        }
    }

    /**
     * Constructor.
     * 
     * @param parentShell
     *            The parent's shell
     */
    public KeyRefactoringDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(Composite parent) {

        Composite dialogArea = (Composite) super.createDialogArea(parent);
        initLayout(dialogArea);

        return super.createDialogArea(parent);
    }

    /**
     * Initializes the layout
     * 
     * @param parent
     *            The parent
     */
    private void initLayout(Composite parent) {
        final GridLayout layout = new GridLayout(1, true);
        parent.setLayout(layout);

        GridLayout gl = new GridLayout(2, true);
        GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);

        Composite master = new Composite(parent, SWT.NONE);
        master.setLayout(gl);
        master.setLayoutData(gd);

        projectLabel = new Label(master, SWT.NONE);
        projectLabel.setText("Project:");

        projectText = new Text(master, SWT.BORDER);
        projectText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, true, 1, 1));
        projectText.setText(config.getProjectName());
        projectText.setEnabled(false);

        resourceBundleLabel = new Label(master, SWT.NONE);
        resourceBundleLabel.setText("Resource-Bundle:");

        resourceBundleText = new Text(master, SWT.BORDER);
        resourceBundleText.setLayoutData(new GridData(GridData.FILL,
                GridData.FILL, true, true, 1, 1));
        resourceBundleText.setText(config.getPreselectedBundle());
        resourceBundleText.setEnabled(false);

        languageLabel = new Label(master, SWT.NONE);
        languageLabel.setText("Language (Country):");

        languageText = new Text(master, SWT.BORDER);
        languageText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, true, 1, 1));
        languageText.setText(ALL_LOCALES);
        languageText.setEnabled(false);

        oldKeyLabel = new Label(master, SWT.NONE);
        oldKeyLabel.setText("Old key name:");

        oldKeyText = new Text(master, SWT.BORDER);
        oldKeyText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, true, 1, 1));
        oldKeyText.setText(config.getPreselectedKey());
        oldKeyText.setEnabled(false);

        newKeyLabel = new Label(master, SWT.NONE);
        newKeyLabel.setText("New key name:");

        newKeyText = new Text(master, SWT.BORDER);
        newKeyText.setText(config.getPreselectedKey());
        newKeyText.setSelection(0, newKeyText.getText().length());
        newKeyText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
                true, true, 1, 1));

        newKeyText.setFocus();

        newKeyText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                selectedKey = newKeyText.getText();
                validate();
            }
        });
    }

    /**
     * @param config
     *            Sets the config
     */
    public void setDialogConfiguration(DialogConfiguration config) {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Rename resource key");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        // TODO Auto-generated method stub
        super.create();
        this.setTitle("Rename resource key");
        this.setMessage("Please, specify the name of the new key. \r\n"
                + "The new value will automatically replace the old ones.");
    }

    /**
     * @return The config
     */
    public DialogConfiguration getConfig() {
        return this.config;
    }
    
    private boolean isValidResourceKey(String key) {
        boolean isValid = false;

        if (key != null && key.trim().length() > 0) {
            isValid = key.matches(REGEXP_RESOURCE_KEY);
        }

        return isValid;
    }
    
	private IMessagesBundleGroup getResourceBundle(String name, IProject project) {
		RBManager instance = RBManager.getInstance(project);
		return instance.getMessagesBundleGroup(name);
	}
    
	public boolean isResourceExisting(String bundleId, String key, IProject project) {
		boolean keyExists = false;
		IMessagesBundleGroup bGroup = getResourceBundle(bundleId, project);

		if (bGroup != null) {
			keyExists = bGroup.isKey(key);
		}
		return keyExists;
	}


    /**
     * Validates all inputs of the CreateResourceBundleEntryDialog
     */
    protected void validate() {
        // Check Resource-Bundle ids
        boolean keyValid = false;
        boolean keyValidChar = isValidResourceKey(selectedKey);

        String resourceBundle = config.getPreselectedBundle();

        IProject project = StandardRefactoring.getProject(config.getProjectName());

        if (!isResourceExisting(resourceBundle, selectedKey, project)) {
            keyValid = true;
        }
        // print Validation summary
        String errorMessage = null;
        if (selectedKey.trim().length() == 0) {
            errorMessage = "No resource key specified.";
        } else if (!keyValidChar) {
            errorMessage = "The specified resource key contains invalid characters.";
        } else if (!keyValid) {
            errorMessage = "The specified resource key is already existing.";
        } else {
            if (okButton != null)
                okButton.setEnabled(true);
        }

        setErrorMessage(errorMessage);
        if (okButton != null && errorMessage != null) {
            okButton.setEnabled(false);
        } else {
            this.config.setNewKey(selectedKey);
            this.config.setSelectedLocale(ALL_LOCALES);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        okButton = createButton(parent, OK, "Ok", true);
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Set return code
                setReturnCode(OK);
                close();
            }
        });

        cancelButton = createButton(parent, CANCEL, "Cancel", false);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setReturnCode(CANCEL);
                close();
            }
        });

        okButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }

}
