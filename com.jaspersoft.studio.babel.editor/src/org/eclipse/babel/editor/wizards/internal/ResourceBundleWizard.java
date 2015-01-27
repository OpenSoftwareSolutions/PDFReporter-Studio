/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Matthias Lettmayer - added setBundleId() + setDefaultPath() (fixed issue 60)
 ******************************************************************************/
package org.eclipse.babel.editor.wizards.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.babel.editor.preferences.MsgEditorPreferences;
import org.eclipse.babel.editor.wizards.IResourceBundleWizard;
import org.eclipse.babel.messages.Messages;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * This is the main wizard class for creating a new set of ResourceBundle
 * properties files. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one or several files with the extension
 * "properties".
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class ResourceBundleWizard extends Wizard implements INewWizard,
        IResourceBundleWizard {
    private ResourceBundleNewWizardPage page;
    private ISelection selection;

    private String defaultRbName = "";
    private String defaultPath = "";

    /**
     * Constructor for ResourceBundleWizard.
     */
    public ResourceBundleWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
     * Adding the page to the wizard.
     */

    public void addPages() {
        page = new ResourceBundleNewWizardPage(selection, defaultPath,
                defaultRbName);
        addPage(page);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We
     * will create an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {
        final String containerName = page.getContainerName();
        final String baseName = page.getFileName();
        final String[] locales = page.getLocaleStrings();
        if (!folderExists(containerName)) {
            // show choosedialog
            String message = Messages.editor_wiz_createfolder;
            message = String.format(message, containerName);
            if (!MessageDialog.openConfirm(getShell(), "Create Folder", message)) { //$NON-NLS-1$
                return false;
            }

        }
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor)
                    throws InvocationTargetException {
                try {
                    monitor.worked(1);
                    monitor.setTaskName(Messages.editor_wiz_creating);
                    IFile file = null;
                    for (int i = 0; i < locales.length; i++) {
                        String fileName = baseName;
                        if (locales[i]
                                .equals(ResourceBundleNewWizardPage.DEFAULT_LOCALE)) {
                            fileName += ".properties"; //$NON-NLS-1$
                        } else {
                            fileName += "_" + locales[i] //$NON-NLS-1$
                                    + ".properties"; //$NON-NLS-1$
                        }
                        file = createFile(containerName, fileName, monitor);
                    }
                    if (file == null) { // file creation failed
                        MessageDialog.openError(getShell(),
                                "Error", "Error creating file"); //$NON-NLS-1$
                        throwCoreException("File \"" + containerName + baseName + "\" could not be created"); //$NON-NLS-1$
                    }
                    final IFile lastFile = file;
                    getShell().getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            IWorkbenchPage wbPage = PlatformUI.getWorkbench()
                                    .getActiveWorkbenchWindow().getActivePage();
                            try {
                                IDE.openEditor(wbPage, lastFile, true);
                            } catch (PartInitException e) {
                            }
                        }
                    });
                    monitor.worked(1);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(),
                    "Error", realException.getLocalizedMessage()); //$NON-NLS-1$
            return false;
        }
        return true;
    }

    /*
     * Checks if the input folder existsS
     */
    /* default */boolean folderExists(String path) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(path));
        if (resource == null) {
            return false;
        } else {
            return resource.exists();
        }
    }

    /*
     * The worker method. It will find the container, create the file if missing
     * or just replace its contents, and open the editor on the newly created
     * file. Will also create the parent folders of the file if they do not
     * exist.
     */
    /* default */IFile createFile(String containerName, String fileName,
            IProgressMonitor monitor) throws CoreException {

        monitor.beginTask(Messages.editor_wiz_creating + fileName, 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        Path containerNamePath = new Path(containerName);
        IResource resource = root.findMember(containerNamePath);
        if (resource == null) {
            if (!createFolder(containerNamePath, root, monitor)) {
                MessageDialog.openError(
                                getShell(),
                                "Error",
                                String.format(Messages.editor_wiz_error_couldnotcreatefolder, containerName)); 
                return null;
            }
        } else if (!resource.exists() || !(resource instanceof IContainer)) {
            //throwCoreException("Container \"" + containerName  //$NON-NLS-1$
            //        + "\" does not exist."); //$NON-NLS-1$
            if (!createFolder(containerNamePath, root, monitor)) {
                MessageDialog
                        .openError(
                                getShell(),
                                "Error",
                                String.format(Messages.editor_wiz_error_couldnotcreatefolder, containerName));
                return null;
            }
        }

        IContainer container = (IContainer) root.findMember(containerNamePath);
        if (container == null) {
            MessageDialog
                    .openError(
                            getShell(),
                            "Error",
                            String.format(Messages.editor_wiz_error_couldnotcreatefolder, containerName));
            return null;
        }
        final IFile file = container.getFile(new Path(fileName));
        try {
            InputStream stream = openContentStream();
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
        }
        return file;
    }

    /*
     * Recursively creates all missing folders
     */
    protected boolean createFolder(Path folderPath, IWorkspaceRoot root,
            IProgressMonitor monitor) {
        IResource baseResource = root.findMember(folderPath);
        if (!(baseResource == null)) {
            if (baseResource.exists()) {
                return true;
            }
        } else { // if folder does not exist
            if (folderPath.segmentCount() <= 1) {
                return true;
            }
            Path oneSegmentLess = (Path) folderPath.removeLastSegments(1); // get
                                                                           // parent
            if (createFolder(oneSegmentLess, root, monitor)) { // create parent
                                                               // folder
                IResource resource = root.findMember(oneSegmentLess);
                if (resource == null) {
                    return false; // resource is null
                } else if (!resource.exists()
                        || !(resource instanceof IContainer)) {
                    return false; // resource does not exist
                }
                final IFolder folder = root.getFolder(folderPath);
                try {
                    folder.create(true, true, monitor);
                } catch (CoreException e) {
                    return false;
                }
                return true;
            } else {
                return false; // could not create parent folder of the input
                              // path
            }
        }
        return true;
    }

    /*
     * We will initialize file contents with a sample text.
     */
    private InputStream openContentStream() {
        String contents = ""; //$NON-NLS-1$
        if (MsgEditorPreferences.getSerializerConfig().isShowSupportEnabled()) {
            // contents = PropertiesGenerator.GENERATED_BY;
        }
        return new ByteArrayInputStream(contents.getBytes());
    }

    private synchronized void throwCoreException(String message)
            throws CoreException {
        IStatus status = new Status(IStatus.ERROR, "org.eclipse.babel.editor", //$NON-NLS-1$
                IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize
     * from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection structSelection) {
        this.selection = structSelection;
    }

    public void setBundleId(String rbName) {
        defaultRbName = rbName;
    }

    public void setDefaultPath(String pathName) {
        defaultPath = pathName;
    }
}