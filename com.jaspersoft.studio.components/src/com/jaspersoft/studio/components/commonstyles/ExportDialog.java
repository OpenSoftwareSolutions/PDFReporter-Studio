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
package com.jaspersoft.studio.components.commonstyles;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.jaspersoft.studio.components.commonstyles.messages.Messages;
import com.jaspersoft.studio.editor.style.TemplateStyle;

/**
 * Dialog to export a series of TemplateStyles to an XML file
 * 
 * @author Orlandin Marco
 *
 */
public class ExportDialog extends FormDialog {
	
	/**
	 * The body composite
	 */
	private Composite body;
	
	/**
	 * Text field used for the path
	 */
	private Text pathText;
	
	/**
	 * List of all the checkbox in the composite
	 */
	private List<Button> buttons;
	
	/**
	 * The style provider used for this import operation
	 */
	private ViewProviderInterface styleProvider;
	
	/**
	 * 
	 * Used when the Select All or the Select None button is 
	 * pressed, it select or deselect all the checkboxs associated 
	 * to a style
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class SelectItemsAdapter extends SelectionAdapter{
		
		private boolean selectionValue;
		
		/**
		 * 
		 * 
		 * @param selectAll True if the checkbox need to be selected false otherwise
		 */
		public SelectItemsAdapter(boolean selectAll){
			selectionValue = selectAll;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			for(Button check : buttons){
				check.setSelection(selectionValue);
			}
		}
		
	}
	
	/**
	 * Create an instance of the class
	 * 
	 * @param shell
	 * @param styleProvider The provider for the type of style that are exported
	 */
	public ExportDialog(Shell shell, ViewProviderInterface styleProvider) {
		super(shell);
		this.styleProvider = styleProvider;
		buttons = new ArrayList<Button>(); 
	}

	/**
	 * Set the shell title and attribute
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ImportExportDialog_titleText);
		setShellStyle(getShellStyle() | SWT.MIN | SWT.MAX | SWT.RESIZE);
	}
	
	/**
	 * Create a file dialog that can handle only xml files
	 * 
	 * @param shell
	 * @return the file or path selected using this dialog
	 */
	private String selectPathDialog(Shell shell){
		FileDialog dialog = new FileDialog (shell, SWT.SAVE);
		String [] filterNames = new String [] {"XML files"};  //$NON-NLS-1$
		String [] filterExtensions = new String [] {"*.xml;"};  //$NON-NLS-1$
		String filterPath = "/"; //$NON-NLS-1$
		String platform = SWT.getPlatform();
		if (platform.equals("win32") || platform.equals("wpf")) { //$NON-NLS-1$ //$NON-NLS-2$
			filterNames = new String [] {"XML files"};  //$NON-NLS-1$
			filterExtensions = new String [] {"*.xml;"};  //$NON-NLS-1$
			filterPath = "c:\\"; //$NON-NLS-1$
		}
		dialog.setFilterNames (filterNames);
		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath (filterPath);
		dialog.setFileName ("MyTemplates"); //$NON-NLS-1$
		return dialog.open();
	}
	
	/**
	 * Called when ok is pressed, save the selected styles as an XML file
	 */
	@Override
	protected void okPressed() {
		
		if (pathText.getText() != ""){ //$NON-NLS-1$
			final StringBuffer xmlBuffer = new StringBuffer();
			xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<templateStyles>");  //$NON-NLS-1$
			for(Button button : buttons){
				if (button.getSelection()){
					TemplateStyle style = (TemplateStyle)button.getData();
					xmlBuffer.append(style.getXMLData());
				}
			}
			xmlBuffer.append("</templateStyles>"); //$NON-NLS-1$
			try {
				java.io.File ioFile = new java.io.File(pathText.getText());
				if (ioFile.exists()) ioFile.delete();
				OutputStream file = new FileOutputStream(ioFile); 
				String xml = xmlBuffer.toString();
				file.write(xml.getBytes("UTF-8")); //$NON-NLS-1$
				file.flush();
				file.close();
				super.okPressed();
			} catch (Exception e) {
				MessageDialog.openWarning(getShell(), Messages.ExportDialog_fileErrorTitle, Messages.ExportDialog_fileErrorMessage);
			}
		} else MessageDialog.openWarning(getShell(), Messages.ImportExportDialog_warningTitle, Messages.ImportExportDialog_warningText);
	};
	
	/**
	 * Generate the composite with the previews
	 * 
	 * @param bodyParent container
	 */
	private void generateStylesComposite(Composite bodyParent){
		final ScrolledComposite scrollComposite = new ScrolledComposite(bodyParent, SWT.V_SCROLL | SWT.BORDER);
		GridData scrolledData = new GridData(SWT.FILL, SWT.FILL, true, true);
		scrolledData.heightHint = 400;
		scrolledData.widthHint = 600;
		scrollComposite.setLayout(new GridLayout(1,false));
		scrollComposite.setLayoutData(scrolledData);
		
		final Composite dataComposite = new Composite(scrollComposite, SWT.NONE);
		dataComposite.setBackground(ColorConstants.white);
		dataComposite.setLayout(new GridLayout(3,false));
		GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		dataComposite.setLayoutData(tableData);
		for(TemplateStyle style : styleProvider.getStylesList()){
			Button checkBox = new Button(dataComposite, SWT.CHECK);
			checkBox.setData(style);
			buttons.add(checkBox);
			GridData checkBoxData = new GridData();
			checkBoxData.minimumWidth = 25;
			checkBoxData.minimumWidth = 25;
			checkBoxData.horizontalAlignment = SWT.CENTER;
			checkBoxData.verticalAlignment = SWT.CENTER;
			checkBox.setLayoutData(checkBoxData);
			Label imageLabel = new Label(dataComposite, SWT.NONE);
			imageLabel.setBackground(dataComposite.getBackground());
			imageLabel.setImage(styleProvider.generatePreviewFigure(style));
			GridData imageData = new GridData();
			imageData.minimumWidth = 100;
			imageData.minimumWidth = 100;
			imageData.horizontalAlignment = SWT.CENTER;
			imageData.verticalAlignment = SWT.CENTER;
			imageLabel.setLayoutData(imageData);
			Label descriptionLabel = new Label(dataComposite, SWT.NONE);
			descriptionLabel.setText(style.getDescription());
			descriptionLabel.setBackground(dataComposite.getBackground());
			descriptionLabel.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true, false));
		}
		
		
		scrollComposite.setContent(dataComposite);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.addControlListener(new ControlAdapter() {
		      public void controlResized(ControlEvent e) {
		        Rectangle r = scrollComposite.getClientArea();
		        scrollComposite.setMinSize(dataComposite.computeSize(r.width,
		            SWT.DEFAULT));
		      }
		});
	}

	
	/**
	 * Set the root control of the wizard, and also add a listener to do the perform help action and set the context of
	 * the top control.
	 */
	protected void setHelpControl(Control newControl) {
		newControl.addListener(SWT.Help, new Listener() {
			@Override
			public void handleEvent(Event event) {
				performHelp();
			}
		});
	};

	/**
	 * Set and show the help data on the provided context
	 */
	public void performHelp() {
		String id = "com.jaspersoft.studio.doc.ExportStyleDialog";
		PlatformUI.getWorkbench().getHelpSystem().setHelp(body, id);
		PlatformUI.getWorkbench().getHelpSystem().displayHelp(id);
	};
	
	@Override
	protected void createFormContent(final IManagedForm mform) {
		body = mform.getForm().getBody();
		body.setLayout(new GridLayout(1, true));
		body.setBackground(body.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));	
		setHelpControl(body);
		
		Label bodyLabel = new Label(body, SWT.NONE);
		bodyLabel.setText(Messages.ImportExportDialog_labelText);
		
		Composite mainComposite = new Composite(body, SWT.NONE);
		mainComposite.setLayout(new GridLayout(2,false));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		generateStylesComposite(mainComposite);
		Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1,false));
		buttonComposite.setLayoutData(new GridData(SWT.LEFT,SWT.FILL,false,true));
		Button selectAll = new Button(buttonComposite, SWT.NONE);
		selectAll.setText(Messages.ImportDialog_selectAllButton);
		selectAll.addSelectionListener(new SelectItemsAdapter(true));
		selectAll.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		Button deselectAll = new Button(buttonComposite, SWT.NONE);
		deselectAll.setText(Messages.ImportDialog_deselectAllButton);
		deselectAll.addSelectionListener(new SelectItemsAdapter(false));
		deselectAll.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		Composite pathComposite = new Composite(body, SWT.NONE);
		pathComposite.setLayout(new GridLayout(3,false));
		GridData pathData = new GridData();
		pathData.grabExcessHorizontalSpace = true;
		pathData.horizontalAlignment = SWT.FILL;
		pathComposite.setLayoutData(pathData);
		Label pathLabel = new Label(pathComposite, SWT.NONE);
		pathLabel.setText(Messages.ImportExportDialog_destinationText);
		pathText = new Text(pathComposite, SWT.BORDER);
		pathText.setEditable(false);
		GridData textData = new GridData();
		textData.grabExcessHorizontalSpace = true;
		textData.horizontalAlignment = SWT.FILL;
		pathText.setLayoutData(textData);
		Button browseButton = new Button(pathComposite, SWT.NONE);
		browseButton.setText(Messages.ImportExportDialog_browseButtonText);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = selectPathDialog(getShell());
				if (path!=null) pathText.setText(path);
			}
		});
	}

	
}
