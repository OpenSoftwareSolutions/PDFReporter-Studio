/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Clemente Lodi-Fè - fixing bugs and setting dialog defaults
 ******************************************************************************/

package org.eclipse.babel.editor.wizards.internal;

import java.util.Locale;

import org.eclipse.babel.editor.widgets.LocaleSelector;
import org.eclipse.babel.messages.Messages;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.jaspersoft.translation.wizard.HelpWizardPage;

/**
 * The "New" wizard page allows setting the container for the new bundle group
 * as well as the bundle group common base name. The page will only accept file
 * name without the extension.
 * 
 * @author Pascal Essiembre (essiembre@users.sourceforge.net)
 * @version $Author: droy $ $Revision: 1.2 $ $Date: 2012/07/18 20:13:09 $
 */
public class ResourceBundleNewWizardPage extends HelpWizardPage {

    public static final String DEFAULT_LOCALE = "[" //$NON-NLS-1$
            + Messages.editor_default //$NON-NLS-1$
            + "]"; //$NON-NLS-1$

    /**
     * contains the path of the folder in which the resource file will be
     * created
     */
    private Text containerText;
    /**
     * Contains the name of the resource file
     */
    private Text fileText;
    private ISelection selection;

    private Button addButton;
    private Button removeButton;
    /**
     * Contains all added locales
     */
    protected List bundleLocalesList;

    private LocaleSelector localeSelector;

    private String defaultPath = "";
    private String defaultRBName = "ApplicationResources";

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param selection
     *            workbench selection
     */
    public ResourceBundleNewWizardPage(ISelection selection, String defaultPath, String defaultRBName) {
        super("wizardPage");
        setTitle(Messages.editor_wiz_title);
        setDescription(Messages.editor_wiz_desc);
        this.selection = selection;

        if (!defaultPath.isEmpty())
            this.defaultPath = defaultPath;
        if (!defaultRBName.isEmpty())
            this.defaultRBName = defaultRBName;
    }
    
    public ResourceBundleNewWizardPage(ISelection selection) {
        super("wizardPage");
        setTitle(Messages.editor_wiz_title);
        setDescription(Messages.editor_wiz_desc);
        this.selection = selection;
    }


    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        layout.verticalSpacing = 20;
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        container.setLayoutData(gd);

        // Bundle name + location
        createTopComposite(container);

        // Locales
        createBottomComposite(container);

        initialize();
        dialogChanged();
        setControl(container);
    }

    /**
     * Creates the bottom part of this wizard, which is the locales to add.
     * 
     * @param parent
     *            parent container
     */
    protected void createBottomComposite(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        container.setLayoutData(gd);

        // Available locales
        createBottomAvailableLocalesComposite(container);

        // Buttons
        createBottomButtonsComposite(container);

        // Selected locales
        createBottomSelectedLocalesComposite(container);
    }

    /**
     * Creates the bottom part of this wizard where selected locales are stored.
     * 
     * @param parent
     *            parent container
     */
    private void createBottomSelectedLocalesComposite(Composite parent) {

        // Selected locales Group
        Group selectedGroup = new Group(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout = new GridLayout();
        layout.numColumns = 1;
        selectedGroup.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        selectedGroup.setLayoutData(gd);
        selectedGroup.setText(Messages.editor_wiz_selected);
        bundleLocalesList = new List(selectedGroup, SWT.READ_ONLY | SWT.MULTI  | SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        bundleLocalesList.setLayoutData(gd);
        bundleLocalesList.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                removeButton.setEnabled(bundleLocalesList.getSelectionIndices().length != 0);
                setAddButtonState();
            }
        });
        // add a single Locale so that the bundleLocalesList isn't empty on
        // startup
        bundleLocalesList.add(DEFAULT_LOCALE);
    }

    /**
     * Creates the bottom part of this wizard where buttons to add/remove
     * locales are located.
     * 
     * @param parent
     *            parent container
     */
    private void createBottomButtonsComposite(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        container.setLayoutData(gd);

        addButton = new Button(container, SWT.NULL);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        addButton.setLayoutData(gd);
        addButton.setText(Messages.editor_wiz_add);
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                bundleLocalesList.add(getSelectedLocaleAsString());
                setAddButtonState();
                dialogChanged(); // for the locale-check
            }
        });

        removeButton = new Button(container, SWT.NULL);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        removeButton.setLayoutData(gd);
        removeButton.setText(Messages.editor_wiz_remove);
        removeButton.setEnabled(false);
        removeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                bundleLocalesList.remove(bundleLocalesList
                        .getSelectionIndices());
                removeButton.setEnabled(false);
                setAddButtonState();
                dialogChanged(); // for the locale-check
            }
        });
    }

    /**
     * Creates the bottom part of this wizard where locales can be chosen or
     * created
     * 
     * @param parent
     *            parent container
     */
    private void createBottomAvailableLocalesComposite(Composite parent) {

        localeSelector = new LocaleSelector(parent);
        localeSelector.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setAddButtonState();
            }
        });
    }

    /**
     * Creates the top part of this wizard, which is the bundle name and
     * location.
     * 
     * @param parent
     *            parent container
     */
    protected void createTopComposite(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        container.setLayoutData(gd);

        // Folder
        Label label = new Label(container, SWT.NULL);
        label.setText(Messages.editor_wiz_folder);

        containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        containerText.setLayoutData(gd);
        containerText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        Button button = new Button(container, SWT.PUSH);
        button.setText(Messages.editor_wiz_browse);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleBrowse();
            }
        });

        // Bundle name
		createBundleNameArea(container);
    }
    
    protected void createBundleNameArea(Composite container){
    	Label label = new Label(container, SWT.NULL);
        label.setText(Messages.editor_wiz_bundleName);

        fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gd);
        fileText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        label = new Label(container, SWT.NULL);
        label.setText("[locale].properties"); //$NON-NLS-1$
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */
    private void initialize() {
        if (!defaultPath.isEmpty()) {
            containerText.setText(defaultPath);

        } else if (selection != null && selection.isEmpty() == false
                && selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if (ssel.size() > 1) {
                return;
            }
            Object obj = ssel.getFirstElement();
            if (obj instanceof IAdaptable) {
                IResource resource = (IResource) ((IAdaptable) obj)
                        .getAdapter(IResource.class);
                // check if selection is a file
                if (resource.getType() == IResource.FILE) {
                    resource = resource.getParent();
                }
                // fill filepath container
                containerText
                        .setText(resource.getFullPath().toPortableString());
            } else if (obj instanceof IResource) {
                // this will most likely never happen (legacy code)
                IContainer container;
                if (obj instanceof IContainer) {
                    container = (IContainer) obj;
                } else {
                    container = ((IResource) obj).getParent();
                }
                containerText.setText(container.getFullPath()
                        .toPortableString());
            }
        }

        if (fileText != null) fileText.setText(defaultRBName);
    }

    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */

    protected void handleBrowse() {
        ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false, Messages.editor_wiz_selectFolder);
        if (dialog.open() == Window.OK) {
            Object[] result = dialog.getResult();
            if (result.length == 1) {
                containerText.setText(((Path) result[0]).toOSString());
            }
        }
    }

    /**
     * Ensures that both text fields and the Locale field are set.
     */
    protected void dialogChanged() {
        String container = getContainerName();
        String fileName = getFileName();

        if (container.length() == 0) {
            updateStatus(Messages.editor_wiz_error_container, IMessageProvider.ERROR);
            return;
        }
        if (fileName.length() == 0) {
            updateStatus(Messages.editor_wiz_error_bundleName, IMessageProvider.ERROR);
            return;
        }
        int dotLoc = fileName.lastIndexOf('.');
        if (dotLoc != -1) {
            updateStatus(Messages.editor_wiz_error_extension, IMessageProvider.ERROR);
            return;
        }
        // check if at least one Locale has been added to th list
        if (bundleLocalesList.getItemCount() <= 0) {
            updateStatus(Messages.editor_wiz_error_noLocale, IMessageProvider.ERROR); 
            return;
        }
        // check if the container field contains a valid path
        // meaning: Project exists, at least one segment, valid path
        Path pathContainer = new Path(container);
        if (!pathContainer.isValidPath(container)) {
            updateStatus(Messages.editor_wiz_error_invalidpath, IMessageProvider.ERROR);
            return;
        }

        if (pathContainer.segmentCount() < 1) {
            updateStatus(Messages.editor_wiz_error_invalidpath, IMessageProvider.ERROR); 
            return;
        }

        if (!projectExists(pathContainer.segment(0))) {
            String errormessage = Messages.editor_wiz_error_projectnotexist;
            errormessage = String.format(errormessage, pathContainer.segment(0));
            updateStatus(errormessage, IMessageProvider.ERROR); 
            return;
        }

        updateStatus(null, IMessageProvider.NONE);
    }

	protected void updateStatus(String message, int messageType) {
		setMessage(message, messageType);
		setPageComplete(messageType != IMessageProvider.ERROR);
	}
	

    /**
     * Gets the container name.
     * 
     * @return container name
     */
    public String getContainerName() {
        return containerText.getText();
    }

    /**
     * Gets the file name.
     * 
     * @return file name
     */
    public String getFileName() {
        return fileText.getText();
    }

    /**
     * Sets the "add" button state.
     */
    /* default */void setAddButtonState() {
        addButton.setEnabled(bundleLocalesList
                .indexOf(getSelectedLocaleAsString()) == -1);
    }

    /**
     * Gets the user selected locales.
     * 
     * @return locales
     */
    protected String[] getLocaleStrings() {
        return bundleLocalesList.getItems();
    }

    /**
     * Gets a string representation of selected locale.
     * 
     * @return string representation of selected locale
     */
    protected String getSelectedLocaleAsString() {
        Locale selectedLocale = localeSelector.getSelectedLocale();
        if (selectedLocale != null) {
            return selectedLocale.toString();
        }
        return DEFAULT_LOCALE;
    }

    /**
     * Checks if there is a Project with the given name in the Package Explorer
     * 
     * @param projectName
     * @return
     */
     protected boolean projectExists(String projectName) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        Path containerNamePath = new Path("/" + projectName);
        IResource resource = root.findMember(containerNamePath);
        if (resource == null) {
            return false;
        }
        return resource.exists();
    }

    public void setDefaultRBName(String name) {
        defaultRBName = name;
    }

    public void setDefaultPath(String path) {
        defaultPath = path;
    }

	@Override
	protected String getContextName() {
		return null;
	}
}