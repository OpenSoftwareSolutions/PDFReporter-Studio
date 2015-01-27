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
package com.jaspersoft.studio.jface.dialogs;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.progress.WorkbenchJob;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.ImageUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Dialog proposed when an image needs to be selected.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class ImageSelectionDialog extends Dialog {
	// Image preview job information
	private static final int IMAGE_PREVIEW_JOB_DELAY = 500;
	private ImagePreviewJob imagePreviewJob;
	// Expression that will be associated to the image element
	private String imageExpressionText;
	// All widgets stuff
	private Text txtResourcePath;
	private Text txtFilesystemPath;
	private Text txtURL;
	private Button btnWorkspaceResource;
	private Button btnAbsolutePath;
	private Button btnNoImage;
	private Button btnUrlRemote;
	private Button btnCustomExpression;
	private StackLayout grpOptionsLayout;
	private Composite cmpWorkspaceResourceSelection;
	private Composite cmpFilesystemResourceSelection;
	private Composite cmpNoImage;
	private Composite cmpCustomExpression;
	private Composite cmpURL;
	private Composite cmpImgPreview;
	private Composite cmpNoImgPreview;
	private Group grpOptions;
	private Label imagePreview;
	private Group grpImagePreview;
	private StackLayout grpImagePreviewLayout;
	private Label lblImageSize;
	private Label lblImageDimension;
	private WTextExpression customExpression;
	private JRDesignExpression jrImgExpression;

	private JasperReportsConfiguration jConfig;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ImageSelectionDialog(Shell parentShell) {
		super(parentShell);
		imagePreviewJob = new ImagePreviewJob();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(getDialogTitle());
	}

	/**
	 * @return the title for the dialog
	 */
	protected String getDialogTitle() {
		return "Select an image";
	}

	/**
	 * Returns an array of strings containing the title for the modes section, plus the title of every mode.
	 * <p>
	 * 
	 * Default implementation would return 6 strings, including 1 title and the following 5 modes:
	 * <ol>
	 * <li>workspace resource;</li>
	 * <li>absolute path in filesystem;</li>
	 * <li>URL;</li>
	 * <li>no image;</li>
	 * <li>custom expression</li>
	 * </ol>
	 * 
	 * @return the title and labels for the group of modes
	 */
	protected String[] getImageModesAndHeaderTitles() {
		return new String[] { "Image selection mode", "Workspace resource (an element inside the workspace)",
				"Absolute Path in the filesystem (use only for quick testing, never use in real reports)",
				"URL (a remote URL referring to an image, will be the expression value)",
				"No image (no image reference will be set)",
				"Custom expression (enter an expression for the image using the expression editor)" };
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		String[] imageModesAndHeaderTitles = getImageModesAndHeaderTitles();

		Group grpImageSelectionMode = new Group(container, SWT.NONE);
		grpImageSelectionMode.setText(imageModesAndHeaderTitles[0]);
		grpImageSelectionMode.setLayout(new GridLayout(1, false));
		grpImageSelectionMode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		btnWorkspaceResource = new Button(grpImageSelectionMode, SWT.RADIO);
		btnWorkspaceResource.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeImageSelectionMode(cmpWorkspaceResourceSelection);
			}
		});
		btnWorkspaceResource.setText(imageModesAndHeaderTitles[1]);

		btnAbsolutePath = new Button(grpImageSelectionMode, SWT.RADIO);
		btnAbsolutePath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeImageSelectionMode(cmpFilesystemResourceSelection);
			}
		});
		btnAbsolutePath.setText(imageModesAndHeaderTitles[2]);

		btnUrlRemote = new Button(grpImageSelectionMode, SWT.RADIO);
		btnUrlRemote.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeImageSelectionMode(cmpURL);
			}
		});
		btnUrlRemote.setText(imageModesAndHeaderTitles[3]);

		btnNoImage = new Button(grpImageSelectionMode, SWT.RADIO);
		btnNoImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeImageSelectionMode(cmpNoImage);
			}
		});
		btnNoImage.setText(imageModesAndHeaderTitles[4]);

		btnCustomExpression = new Button(grpImageSelectionMode, SWT.RADIO);
		btnCustomExpression.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeImageSelectionMode(cmpCustomExpression);
			}
		});
		btnCustomExpression.setText(imageModesAndHeaderTitles[5]);

		createOptionsPanel(container);

		createImagePreviewPanel(container);

		// As default no image radio button selected
		btnNoImage.setSelection(true);
		changeImageSelectionMode(this.cmpNoImage);

		return area;
	}

	/*
	 * Creates the panel with the different options container. A stack layout will be used.
	 */
	private void createOptionsPanel(Composite container) {
		grpOptions = new Group(container, SWT.NONE);
		grpOptions.setText(Messages.ImageSelectionDialog_OptionsGroupTitle);
		grpOptionsLayout = new StackLayout();
		grpOptions.setLayout(grpOptionsLayout);
		grpOptions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		createWSSelectionContainer();
		createFSSelectionContainer();
		createNoImageContainer();
		createCustomExprContainer();
		createURLOptionsContainer();
	}

	/*
	 * Creates the composite container for the workspace image selection.
	 */
	private void createWSSelectionContainer() {
		cmpWorkspaceResourceSelection = new Composite(grpOptions, SWT.NONE);
		cmpWorkspaceResourceSelection.setLayout(new GridLayout(2, false));

		Label lblSelectImageFromWS = new Label(cmpWorkspaceResourceSelection, SWT.NONE);
		lblSelectImageFromWS.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblSelectImageFromWS.setText(Messages.ImageSelectionDialog_SelectImgFromWS);

		txtResourcePath = new Text(cmpWorkspaceResourceSelection, SWT.BORDER);
		txtResourcePath.setEnabled(false);
		txtResourcePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnSelectWsRes = new Button(cmpWorkspaceResourceSelection, SWT.NONE);
		btnSelectWsRes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectImageFromWorkspace();
			}
		});
		btnSelectWsRes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelectWsRes.setText(Messages.ImageSelectionDialog_Browse);
	}

	/*
	 * Creates the composite container for the filesystem image selection.
	 */
	private void createFSSelectionContainer() {
		cmpFilesystemResourceSelection = new Composite(grpOptions, SWT.NONE);
		cmpFilesystemResourceSelection.setLayout(new GridLayout(2, false));

		Label lblSelectImageFromFilesystem = new Label(cmpFilesystemResourceSelection, SWT.NONE);
		lblSelectImageFromFilesystem.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblSelectImageFromFilesystem.setText(Messages.ImageSelectionDialog_SelectImgFromFS);

		txtFilesystemPath = new Text(cmpFilesystemResourceSelection, SWT.BORDER);
		txtFilesystemPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFilesystemPath.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				loadImagePreview();
			}
		});

		Button btnSelectFilesystemRes = new Button(cmpFilesystemResourceSelection, SWT.NONE);
		btnSelectFilesystemRes.setText(Messages.ImageSelectionDialog_Browse);
		btnSelectFilesystemRes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectImageFromFilesystem();
			}
		});
	}

	/*
	 * Creates the empty composite for no image selection.
	 */
	private void createNoImageContainer() {
		cmpNoImage = new Composite(grpOptions, SWT.NONE);
	}

	/*
	 * Creates the composite container for the custom expression editing.
	 */
	private void createCustomExprContainer() {
		cmpCustomExpression = new Composite(grpOptions, SWT.NONE);
		GridLayout cmpCustomExpressionlayout = new GridLayout();
		cmpCustomExpression.setLayout(cmpCustomExpressionlayout);

		customExpression = new WTextExpression(cmpCustomExpression, SWT.NONE,
				Messages.ImageSelectionDialog_EnterExpression, WTextExpression.LABEL_ON_TOP) {
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				// Keep in synch the expression modification in the widget
				// with the final image expression.
				jrImgExpression = exp;
			}
		};
		customExpression.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/*
	 * Create the composite container for the URL image selection.
	 */
	private void createURLOptionsContainer() {
		cmpURL = new Composite(grpOptions, SWT.NONE);
		cmpURL.setLayout(new GridLayout(1, false));

		Label lblNewLabel = new Label(cmpURL, SWT.NONE);
		lblNewLabel.setText(Messages.ImageSelectionDialog_EnterURL);

		txtURL = new Text(cmpURL, SWT.BORDER);
		txtURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtURL.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				loadImagePreview();
			}
		});
	}

	/*
	 * Create the image preview panel.
	 */
	private void createImagePreviewPanel(Composite container) {
		grpImagePreview = new Group(container, SWT.NONE);
		grpImagePreviewLayout = new StackLayout();
		grpImagePreview.setLayout(grpImagePreviewLayout);
		grpImagePreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		grpImagePreview.setText(Messages.ImageSelectionDialog_ImagePreviewGroupTitle);

		cmpImgPreview = new Composite(grpImagePreview, SWT.NONE);
		cmpImgPreview.setLayout(new GridLayout(1, false));

		lblImageDimension = new Label(cmpImgPreview, SWT.NONE);
		lblImageDimension.setText(Messages.ImageSelectionDialog_Dimension);
		lblImageDimension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblImageSize = new Label(cmpImgPreview, SWT.NONE);
		lblImageSize.setText(Messages.ImageSelectionDialog_Size);
		lblImageSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		imagePreview = new Label(cmpImgPreview, SWT.NONE);
		imagePreview.setText("IMAGE HERE"); //$NON-NLS-1$
		imagePreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		cmpNoImgPreview = new Composite(grpImagePreview, SWT.NONE);
		cmpNoImgPreview.setLayout(new GridLayout(1, false));

		Label lblNoPreviewAvailable = new Label(cmpNoImgPreview, SWT.NONE);
		lblNoPreviewAvailable.setText(Messages.ImageSelectionDialog_NoPreviewAvailable);
		lblNoPreviewAvailable.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lblNoPreviewAvailable.setAlignment(SWT.CENTER);
	}

	/*
	 * When a new image selection mode is selected, shows the dedicated options panel and hide the image preview one.
	 */
	private void changeImageSelectionMode(Control newTopControl) {
		// Resets previous info on the image expression
		imageExpressionText = null;
		// Resets widgets
		txtResourcePath.setText(""); //$NON-NLS-1$
		txtFilesystemPath.setText(""); //$NON-NLS-1$
		txtURL.setText(""); //$NON-NLS-1$
		customExpression.setExpression(null);
		// Shows no preview panel and hide the image preview one
		Image currImgPreview = imagePreview.getImage();
		if (currImgPreview != null)
			currImgPreview.dispose();
		grpImagePreviewLayout.topControl = cmpNoImgPreview;
		grpImagePreview.layout();
		// Change the top control for the options panel
		grpOptionsLayout.topControl = newTopControl;
		grpOptions.layout();
	}

	/*
	 * Popup the dialog to select the image from workspace.
	 */
	private void selectImageFromWorkspace() {
		FilteredResourcesSelectionDialog fd = new FilteredResourcesSelectionDialog(Display.getCurrent().getActiveShell(),
				false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
		fd.setInitialPattern("*.png");//$NON-NLS-1$
		if (fd.open() == Dialog.OK) {
			IFile file = (IFile) fd.getFirstResult();
			IFile contextfile = (IFile) jConfig.get(FileUtils.KEY_FILE);
			String filepath = null;
			if (contextfile != null && file.getProject().equals(contextfile.getProject()))
				filepath = file.getProjectRelativePath().toPortableString().replaceAll(file.getProject().getName() + "/", ""); //$NON-NLS-1$ //$NON-NLS-2$
			else
				filepath = file.getRawLocationURI().toASCIIString();
			txtResourcePath.setText(filepath);
			try {
				IFileStore imgFileStore = EFS.getStore(file.getLocationURI());
				loadImagePreview(file.getLocation().toOSString(), imgFileStore);
				// Change the standard separator with an universal one
				imageExpressionText = file.getLocation().toOSString()
						.replace(System.getProperty("file.separator").charAt(0), '/');
			} catch (CoreException e) {
				UIUtils.showError(e);
			}
		} else {
			// no image selected
			txtResourcePath.setText(""); //$NON-NLS-1$
			grpImagePreviewLayout.topControl = cmpNoImgPreview;
			grpImagePreview.layout();
		}
	}

	/*
	 * Popup the dialog to select the image from the filesystem.
	 */
	private void selectImageFromFilesystem() {
		FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		fd.setFilterPath(root.getLocation().toOSString());
		fd.setFilterExtensions(new String[] { "*.png", "*.jpeg; *.jpg", "*.gif", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String selection = fd.open();
		if (selection != null) {
			// After the text modification the image preview job will be invoked...
			txtFilesystemPath.setText(selection);
		}
	}

	/*
	 * Loads the preview image panel with the specified image file information.
	 */
	private void loadImagePreview(String imgLocation, IFileStore imgFileStore) {
		Image oldPreviewImg = imagePreview.getImage();
		Image originalImg = null;
		try {
			originalImg = new Image(getShell().getDisplay(), imgLocation);
		} catch (SWTException ex) {
			// Unable to load the image (most cases not valid location)
			// Only catch exception...and show no image preview panel
			grpImagePreviewLayout.topControl = cmpNoImgPreview;
			grpImagePreview.layout();
		}
		if (originalImg != null) {
			int imgHeight = originalImg.getImageData().height;
			int imgWidth = originalImg.getImageData().width;
			String sizeInfo = Messages.ImageSelectionDialog_NoSizeInfoAvailable;
			sizeInfo = (DecimalFormat.getNumberInstance().format(imgFileStore.fetchInfo().getLength()))
					+ Messages.ImageSelectionDialog_bytes;

			// Gets a resized image for the preview area
			Image resizedImg = ImageUtils.resize(originalImg, Math.min(imgWidth, 200), Math.min(imgHeight, 200));
			imagePreview.setImage(resizedImg);
			lblImageDimension.setText(Messages.ImageSelectionDialog_Dimension + imgWidth + "x" + imgHeight + "px"); //$NON-NLS-2$ //$NON-NLS-3$
			lblImageSize.setText(Messages.ImageSelectionDialog_Size + sizeInfo);
			grpImagePreviewLayout.topControl = cmpImgPreview;
			grpImagePreview.layout(true);

			// Dispose unused images
			if (originalImg != null) {
				originalImg.dispose();
			}
			if (oldPreviewImg != null) {
				oldPreviewImg.dispose();
			}
		}
	}

	/*
	 * Loads the preview image panel with the specified remote URL information.
	 */
	private void loadPreviewRemoteImage(String imageURLText) {
		Image oldPreviewImg = imagePreview.getImage();
		HttpURLConnection con = null;
		InputStream imageIS = null;
		try {
			URL imageURL = new URL(imageURLText);
			con = (HttpURLConnection) imageURL.openConnection();
			imageIS = con.getInputStream();
			int imageLength = con.getContentLength();
			Image remoteImg = new Image(getShell().getDisplay(), imageIS);

			String sizeInfo = Messages.ImageSelectionDialog_NoSizeInfoAvailable;
			sizeInfo = (DecimalFormat.getNumberInstance().format(imageLength)) + Messages.ImageSelectionDialog_bytes;
			// Gets a resized image for the preview area
			int imgWidth = remoteImg.getImageData().width;
			int imgHeight = remoteImg.getImageData().height;
			Image resizedImg = ImageUtils.resize(remoteImg, Math.min(imgWidth, 200), Math.min(imgHeight, 200));
			imagePreview.setImage(resizedImg);
			lblImageDimension.setText(Messages.ImageSelectionDialog_Dimension + imgWidth + "x" + imgHeight + "px"); //$NON-NLS-2$ //$NON-NLS-3$
			lblImageSize.setText(Messages.ImageSelectionDialog_Size + sizeInfo);
			grpImagePreviewLayout.topControl = cmpImgPreview;
			grpImagePreview.layout(true);

			// Dispose unused images
			if (remoteImg != null) {
				remoteImg.dispose();
			}
			if (oldPreviewImg != null) {
				oldPreviewImg.dispose();
			}
		} catch (Exception e) {
			grpImagePreviewLayout.topControl = cmpNoImgPreview;
			grpImagePreview.layout();
		} finally {
			FileUtils.closeStream(imageIS);
		}
	}

	/*
	 * Cancels a possible existing image preview job and schedules a new one.
	 */
	private void loadImagePreview() {
		// TODO: we should add a check for allowed image extensions.
		imagePreviewJob.cancel();
		imagePreviewJob.schedule(IMAGE_PREVIEW_JOB_DELAY);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(640, 640);
	}

	/**
	 * Configure required information for the correct dialog functioning.
	 * 
	 * @param jConfig
	 */
	public void configureDialog(JasperReportsConfiguration jConfig) {
		this.jConfig = jConfig;
	}

	@Override
	public int open() {
		if (jConfig == null) {
			throw new RuntimeException(Messages.ImageSelectionDialog_Error);
		}
		return super.open();
	}

	/*
	 * Job that is responsible to load an image from an URL or a local filesystem path.
	 */
	private final class ImagePreviewJob extends WorkbenchJob {

		public ImagePreviewJob() {
			super(Messages.ImageSelectionDialog_JobImgPreview);
			setSystem(true);
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			if (ImageSelectionDialog.this.getDialogArea() != null && !ImageSelectionDialog.this.getDialogArea().isDisposed()) {
				monitor.beginTask(Messages.ImageSelectionDialog_JobImgPreviewRetrieving, IProgressMonitor.UNKNOWN);
				if (btnAbsolutePath.getSelection()) {
					// filesystem path...
					String imagePath = txtFilesystemPath.getText();
					IFileStore imgFileStore = EFS.getLocalFileSystem().getStore(new Path(imagePath));
					loadImagePreview(imagePath, imgFileStore);
					// Change the standard separator with an universal one
					imageExpressionText = imagePath.replace(System.getProperty("file.separator").charAt(0), '/');
				} else if (btnUrlRemote.getSelection()) {
					// URL
					String imageURLText = txtURL.getText();
					imageExpressionText = imageURLText;
					loadPreviewRemoteImage(imageURLText);
				}
				monitor.done();
				return Status.OK_STATUS;
			} else {
				return Status.CANCEL_STATUS;
			}
		}

	}

	@Override
	public boolean close() {
		if (imagePreviewJob != null) {
			imagePreviewJob.cancel();
			imagePreviewJob = null;
		}
		return super.close();
	}

	@Override
	protected void okPressed() {
		// Updates the expression that will be associated to the image element.
		// Covers all cases except the custom expression one because
		// it is already kept in synch.
		if (!btnCustomExpression.getSelection()) {
			if (imageExpressionText != null) {
				jrImgExpression = new JRDesignExpression();
				if (imageExpressionText.endsWith(".svg")) //$NON-NLS-1$
					jrImgExpression
							.setText("net.sf.jasperreports.renderers.BatikRenderer.getInstanceFromLocation($P{JASPER_REPORTS_CONTEXT}, \"" + imageExpressionText + "\")");//$NON-NLS-1$ //$NON-NLS-2$
				else
					jrImgExpression.setText("\"" + imageExpressionText + "\"");//$NON-NLS-1$ //$NON-NLS-2$
			} else {
				jrImgExpression = null;
			}
		}

		super.okPressed();
	}

	public JRDesignExpression getImageExpression() {
		return jrImgExpression;
	}
}
