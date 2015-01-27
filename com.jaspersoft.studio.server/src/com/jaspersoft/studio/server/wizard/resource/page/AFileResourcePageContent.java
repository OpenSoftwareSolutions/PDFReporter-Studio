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
package com.jaspersoft.studio.server.wizard.resource.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.ui.validator.NotEmptyFileValidator;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.util.JRTypeSniffer;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.dialogs.SaveAsDialog;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.property.combomenu.ComboItem;
import com.jaspersoft.studio.property.combomenu.ComboItemAction;
import com.jaspersoft.studio.property.combomenu.ComboMenuViewer;
import com.jaspersoft.studio.property.section.widgets.SPRWPopUpCombo;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.Misc;

public abstract class AFileResourcePageContent extends APageContent {
	protected Text trefuri;

	protected static ComboItem defaultComboItem = new ComboItem(Messages.AFileResourcePageContent_upDownButtonTitle, true, Activator.getDefault().getImage("icons/up-down-arrows.png"), 0, 0, 0); //$NON-NLS-2$

	public AFileResourcePageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public AFileResourcePageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	/**
	 * Create a button to download the file resource
	 * 
	 * @param parent
	 *          parent of the button
	 */
	protected void createExportButton(Composite parent) {
		if (!res.getValue().getIsNew()) {
			Button bexport = new Button(parent, SWT.PUSH | SWT.LEFT);
			bexport.setText(Messages.AFileResourcePage_downloadfilebutton);
			bexport.setImage(Activator.getDefault().getImage("icons/drive-download.png")); //$NON-NLS-1$
			bexport.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					SaveAsDialog saveAsDialog = new SaveAsDialog(UIUtils.getShell());
					String fname = res.getValue().getName();
					if (!fname.contains(".")) //$NON-NLS-1$
						fname += "." + ((AFileResource) res).getDefaultFileExtension(); //$NON-NLS-1$
					saveAsDialog.setOriginalName(fname);
					if (saveAsDialog.open() == Dialog.OK) {
						IPath path = saveAsDialog.getResult();
						if (path != null) {
							IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
							if (file != null)
								doSaveFile(file.getLocation().toPortableString());
							try {
								file.getParent().refreshLocal(2, null);
							} catch (CoreException e1) {
								UIUtils.showError(e1);
							}
						}
					}
				}
			});
		}
	}

	/**
	 * Create a button to upload a file resource. The behavior of the open dialog
	 * is provided by the getFileDialog method
	 * 
	 * @param parent
	 *          parent of the button
	 */
	protected void createImportButton(Composite parent) {
		Button bimport = new Button(parent, SWT.PUSH | SWT.LEFT);
		bimport.setText(Messages.AFileResourcePage_uploadfile);
		bimport.setImage(Activator.getDefault().getImage("icons/drive-upload.png")); //$NON-NLS-1$
		bimport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filename = getFileDialog();
				if (filename != null)
					((AFileResource) res).setFile(new File(filename));
				handleFileChange();
			}
		});
	}

	/**
	 * Create the text area
	 * 
	 * @param parent
	 *          parent of the area
	 */
	protected void createTextArea(Composite parent) {
		trefuri = new Text(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 10;
		trefuri.setLayoutData(gd);
	}

	@Override
	protected void rebind() {
		Binding binding = bindingContext.bindValue(SWTObservables.observeText(trefuri, SWT.Modify), PojoObservables.observeValue(new FileProxy((AFileResource) res), "fileName"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new NotEmptyFileValidator()), null);
		ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT, null, new ControlDecorationUpdater());
	}

	/**
	 * Create a popup button that can be used to upload or download a file
	 * resource. This is done since sometimes there are multiple choices (like
	 * upload from FS, upload from workspace, download into the FS, download into
	 * the WS), and using buttons for everyone will be confusing and expansive in
	 * term of UI space. This instead will group all the options inside a menu.
	 * The action in the menu are provided by the method getItemsList
	 * 
	 * @param parent
	 *          parent of the control
	 */
	protected void createComboMenuButton(Composite parent) {
		List<ComboItem> itemsList = getItemsList();
		final ComboMenuViewer multipleButton = new ComboMenuViewer(parent, SWT.NORMAL, SPRWPopUpCombo.getLongest(itemsList));
		multipleButton.setItems(itemsList);
		multipleButton.addSelectionListener(new ComboItemAction() {
			/**
			 * The action to execute when an entry is selected
			 */
			@Override
			public void exec() {
				buttonSelected((Integer) multipleButton.getSelectionValue());
			}
		});
		multipleButton.disableSelectedItemUpdate(true);
		multipleButton.disableSelectedEmphasis(true);
		multipleButton.select(defaultComboItem);
	}

	/**
	 * Provide the option available with the upload\download button, by default
	 * there is a download into the workspace and an upload from the filesystem
	 * 
	 * @return a list of action that can be selected from the upload\download
	 *         button
	 */
	protected List<ComboItem> getItemsList() {
		List<ComboItem> itemsList = new ArrayList<ComboItem>();
		// The doSaveFile method require that the root of the resource is an
		// MserverProfile (this is true when we see the properties of an element but
		// not when we create a new one
		// so we hide the download option is hidden when we are creating an elemen
		if (res.getRoot() instanceof MServerProfile)
			itemsList.add(new ComboItem(Messages.AFileResourcePage_downloadfilebutton, true, Activator.getDefault().getImage("icons/drive-download.png"), 0, 0, 0)); //$NON-NLS-2$
		itemsList.add(new ComboItem(Messages.AFileResourcePageContent_uploadFromFS, true, Activator.getDefault().getImage("icons/drive-upload.png"), 1, 1, 1)); //$NON-NLS-2$
		itemsList.add(new ComboItem(Messages.JrxmlPageContent_uploadFromRepo, true, Activator.getDefault().getImage("icons/drive-upload.png"), 2, 2, 2)); //$NON-NLS-2$
		return itemsList;
	}

	/**
	 * Called when an action is selected from the upload\download button, looking
	 * at the id of the action it execute the proper code.
	 * 
	 * @param selectionValue
	 *          id of the selcted action
	 */
	protected void buttonSelected(Integer selectionValue) {
		if (selectionValue.equals(0)) {
			SaveAsDialog saveAsDialog = new SaveAsDialog(UIUtils.getShell());
			String fname = res.getValue().getName();
			if (!fname.contains(".")) //$NON-NLS-1$
				fname += "." + ((AFileResource) res).getDefaultFileExtension(); //$NON-NLS-1$
			saveAsDialog.setOriginalName(fname);
			if (saveAsDialog.open() == Dialog.OK) {
				IPath path = saveAsDialog.getResult();
				if (path != null) {
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					if (file != null)
						doSaveFile(file.getLocation().toPortableString());
					try {
						file.getParent().refreshLocal(2, null);
					} catch (CoreException e1) {
						UIUtils.showError(e1);
					}
				}
			}
		} else if (selectionValue.equals(1)) {
			String filename = getFileDialog();
			if (filename != null)
				((AFileResource) res).setFile(new File(filename));
			handleFileChange();
		} else if (selectionValue.equals(2)) {
			String filename = getResourceDialog();
			if (filename != null)
				((AFileResource) res).setFile(new File(filename));
			handleFileChange();
		}
	}

	protected String getIntialPattern() {
		String[] f = getFilter();
		if (f != null && f.length > 0)
			return f[f.length - 1];
		return ".*";
	}

	/**
	 * Return a resource by selecting it from the workspace
	 * 
	 * @return the path of the resource
	 */
	protected String getResourceDialog() {
		FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(trefuri.getShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
		dialog.setTitle("");
		dialog.setInitialPattern(getIntialPattern()); //$NON-NLS-1$
		if (dialog.open() == Window.OK) {
			IFile file = (IFile) dialog.getFirstResult();
			return file.getLocation().toPortableString();
		}
		return null;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		// createExportButton(composite);

		// createImportButton(composite);

		createTextArea(composite);

		createComboMenuButton(composite);

		createFileTab(composite);

		handleFileChange();
		rebind();
		return composite;
	}

	private class FileProxy {
		private AFileResource fres;

		public FileProxy(AFileResource fres) {
			this.fres = fres;
		}

		public String getFileName() {
			return fres.getFileName();
		}

		public void setFileName(String fileName) {
			if (Misc.isNullOrEmpty(fileName))
				fres.setFile(null);
			else
				fres.setFile(new File(fileName));
		}
	}

	protected void handleFileChange() {
		trefuri.setText(Misc.nvl(((AFileResource) res).getFileName()));
	}

	protected void createFileTab(Composite tabFolder) {

	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.editGenericFile"; //$NON-NLS-1$
	}

	protected abstract String[] getFilter();

	protected void doSaveFile(String filename) {
		if (filename != null) {
			try {
				WSClientHelper.getResource(new NullProgressMonitor(), AFileResourcePageContent.this.res, res.getValue(), filename);
				File file = new File(filename);
				int dotPos = filename.lastIndexOf("."); //$NON-NLS-1$
				String strFilename = filename.substring(0, dotPos);
				ImageTypeEnum itype = JRTypeSniffer.getImageTypeValue(FileUtils.getBytes(file));
				if (itype == ImageTypeEnum.GIF) {
					file = FileUtils.fileRenamed(file, strFilename, ".gif"); //$NON-NLS-1$
				} else if (itype == ImageTypeEnum.JPEG) {
					file = FileUtils.fileRenamed(file, strFilename, ".jpeg"); //$NON-NLS-1$
				} else if (itype == ImageTypeEnum.PNG) {
					file = FileUtils.fileRenamed(file, strFilename, ".png"); //$NON-NLS-1$
				} else if (itype == ImageTypeEnum.TIFF) {
					file = FileUtils.fileRenamed(file, strFilename, ".tiff"); //$NON-NLS-1$
				}
				((AFileResource) res).setFile(file);
			} catch (Exception e1) {
				UIUtils.showError(e1);
			}
			handleFileChange();
		}
	}

	protected String getFileDialog() {
		FileDialog fd = new FileDialog(UIUtils.getShell(), SWT.OPEN);
		fd.setFilterExtensions(getFilter());
		fd.setText(Messages.AFileResourcePage_selectresourcefile);
		String filename = fd.open();
		return filename;
	}
}
