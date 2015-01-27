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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorSupportProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

public class DataAdapterErrorDialog extends IconAndMessageDialog {

	/**
	 * The Details button.
	 */
	private Button detailsButton;

	/**
	 * The title of the dialog.
	 */
	private String title;

	/**
	 * The SWT text control that displays the error details.
	 */
	private Text text;

	/**
	 * Indicates whether the error details viewer is currently created.
	 */
	private boolean textCreated = false;

	/**
	 * The main status object.
	 */
	private IStatus status;

	/**
	 * The current clipboard. To be disposed when closing the dialog.
	 */
	private Clipboard clipboard;

	/**
	 * Creates an error dialog. Note that the dialog will have no visual
	 * representation (no widgets) until it is told to open.
	 * <p>
	 * Normally one should use <code>openError</code> to create and open one
	 * of these. This constructor is useful only if the error object being
	 * displayed contains child items <it>and </it> you need to specify a mask
	 * which will be used to filter the displaying of these children. The error
	 * dialog will only be displayed if there is at least one child status
	 * matching the mask.
	 * </p>
	 * 
	 * @param parentShell
	 *            the shell under which to create this dialog
	 * @param dialogTitle
	 *            the title to use for this dialog, or <code>null</code> to
	 *            indicate that the default title should be used
	 * @param message
	 *            the message to show in this dialog, or <code>null</code> to
	 *            indicate that the error's message should be shown as the
	 *            primary message
	 * @param status
	 *            the error to show to the user
	 * @param displayMask
	 *            the mask to use to filter the displaying of child items, as
	 *            per <code>IStatus.matches</code>
	 * @see org.eclipse.core.runtime.IStatus#matches(int)
	 */
	public DataAdapterErrorDialog(Shell parentShell, String dialogTitle, String message,
			IStatus status) {
		super(parentShell);
		this.title = dialogTitle == null ? JFaceResources
				.getString("Problem_Occurred") : //$NON-NLS-1$
				dialogTitle;
		this.message = message == null ? status.getMessage()
				: JFaceResources
						.format(
								"Reason", new Object[] { message, status.getMessage() }); //$NON-NLS-1$
		this.status = status;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog. Handles the pressing of the Ok
	 * or Details button in this dialog. If the Ok button was pressed then close
	 * this dialog. If the Details button was pressed then toggle the displaying
	 * of the error details area. Note that the Details button will only be
	 * visible if the error being displayed specifies child details.
	 */
	@Override
	protected void buttonPressed(int id) {
		if (id == IDialogConstants.DETAILS_ID) {
			// was the details button pressed?
			toggleDetailsArea();
		} else {
			super.buttonPressed(id);
		}
	}

	/*
	 * (non-Javadoc) Method declared in Window.
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Details buttons
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createDetailsButton(parent);
	}

	/**
	 * Create the area for extra error support information.
	 * 
	 * @param parent
	 */
	private void createSupportArea(Composite parent) {

		ErrorSupportProvider provider = Policy.getErrorSupportProvider();

		if (provider == null)
			return;

		Composite supportArea = new Composite(parent, SWT.NONE);
		provider.createSupportArea(supportArea, status);

		GridData supportData = new GridData(SWT.FILL, SWT.FILL, true, true);
		supportData.verticalSpan = 3;
		supportArea.setLayoutData(supportData);
		if (supportArea.getLayout() == null){
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			supportArea.setLayout(layout); // Give it a default layout if one isn't set
		}


	}

	/**
	 * Create the details button if it should be included.
	 * 
	 * @param parent
	 *            the parent composite
	 * @since 3.2
	 */
	protected void createDetailsButton(Composite parent) {
		if (shouldShowDetailsButton()) {
			detailsButton = createButton(parent, IDialogConstants.DETAILS_ID,
					IDialogConstants.SHOW_DETAILS_LABEL, false);
		}
	}

	/**
	 * This implementation of the <code>Dialog</code> framework method creates
	 * and lays out a composite. Subclasses that require a different dialog area
	 * may either override this method, or call the <code>super</code>
	 * implementation and add controls to the created composite.
	 * 
	 * Note:  Since 3.4, the created composite no longer grabs excess vertical space.
	 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=72489.
	 * If the old behavior is desired by subclasses, get the returned composite's
	 * layout data and set grabExcessVerticalSpace to true.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// Create a composite with standard margins and spacing
		// Add the messageArea to this composite so that as subclasses add widgets to the messageArea
		// and dialogArea, the number of children of parent remains fixed and with consistent layout.
		// Fixes bug #240135
		Composite composite = new Composite(parent, SWT.NONE);
		createMessageArea(composite);
		createSupportArea(parent);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.numColumns = 2;
		composite.setLayout(layout);
		GridData childData = new GridData(GridData.FILL_BOTH);
		childData.horizontalSpan = 2;
		childData.grabExcessVerticalSpace = false;
		composite.setLayoutData(childData);
		composite.setFont(parent.getFont());

		return composite;
	}

	/*
	 * @see IconAndMessageDialog#createDialogAndButtonArea(Composite)
	 */
	@Override
	protected void createDialogAndButtonArea(Composite parent) {
		super.createDialogAndButtonArea(parent);
		if (this.dialogArea instanceof Composite) {
			// Create a label if there are no children to force a smaller layout
			Composite dialogComposite = (Composite) dialogArea;
			if (dialogComposite.getChildren().length == 0) {
				new Label(dialogComposite, SWT.NULL);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IconAndMessageDialog#getImage()
	 */
	@Override
	protected Image getImage() {
		if (status != null) {
			if (status.getSeverity() == IStatus.WARNING) {
				return getWarningImage();
			}
			if (status.getSeverity() == IStatus.INFO) {
				return getInfoImage();
			}
		}
		// If it was not a warning or an error then return the error image
		return getErrorImage();
	}

	/**
	 * Create this dialog's drop-down list component.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the drop-down list component
	 */
	protected Text createDropDownList(Composite parent) {
		// create the list
		text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY );
		// fill the list
		text.setText(getStackTrace());
		
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
				| GridData.GRAB_VERTICAL);
		data.heightHint = 100; //list.getItemHeight() * LIST_ITEM_COUNT;
		data.horizontalSpan = 2;
		text.setLayoutData(data);
		text.setFont(parent.getFont());
		
		Menu copyMenu = new Menu(text);
		MenuItem copyItem = new MenuItem(copyMenu, SWT.NONE);
		copyItem.addSelectionListener(new SelectionListener() {
			/*
			 * @see SelectionListener.widgetSelected (SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				copyToClipboard();
			}

			/*
			 * @see SelectionListener.widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				copyToClipboard();
			}
		});
		copyItem.setText(JFaceResources.getString("copy")); //$NON-NLS-1$
		text.setMenu(copyMenu);
		textCreated = true;
		return text;
	}

 
	/**
	 * Toggles the unfolding of the details area. This is triggered by the user
	 * pressing the details button.
	 */
	private void toggleDetailsArea() {
		Point windowSize = getShell().getSize();
		Point oldSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		if (textCreated) {
			text.dispose();
			textCreated = false;
			detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
		} else {
			text = createDropDownList((Composite) getContents());
			detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
			getContents().getShell().layout();
		}
		Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		getShell()
				.setSize(
						new Point(windowSize.x, windowSize.y
								+ (newSize.y - oldSize.y)));
	}

	

	/**
	 * Copy the contents of the statuses to the clipboard.
	 */
	private void copyToClipboard() {
		if (clipboard != null) {
			clipboard.dispose();
		}

		clipboard = new Clipboard(text.getDisplay());
		clipboard.setContents(new Object[] { getStackTrace() },
				new Transfer[] { TextTransfer.getInstance() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#close()
	 */
	@Override
	public boolean close() {
		if (clipboard != null) {
			clipboard.dispose();
		}
		return super.close();
	}

	/**
	 * Show the details portion of the dialog if it is not already visible. This
	 * method will only work when it is invoked after the control of the dialog
	 * has been set. In other words, after the <code>createContents</code>
	 * method has been invoked and has returned the control for the content area
	 * of the dialog. Invoking the method before the content area has been set
	 * or after the dialog has been disposed will have no effect.
	 * 
	 * @since 3.1
	 */
	protected final void showDetailsArea() {
		if (!textCreated) {
			Control control = getContents();
			if (control != null && !control.isDisposed()) {
				toggleDetailsArea();
			}
		}
	}

	/**
	 * Return whether the Details button should be included. This method is
	 * invoked once when the dialog is built. By default, the Details button is
	 * only included if the status used when creating the dialog was a
	 * multi-status or if the status contains an exception. Subclasses may
	 * override.
	 * 
	 * @return whether the Details button should be included
	 * @since 3.1
	 */
	protected boolean shouldShowDetailsButton() {
		return status.getException() != null;
	}

	/**
	 * Set the status displayed by this error dialog to the given status. This
	 * only affects the status displayed by the Details list. The message, image
	 * and title should be updated by the subclass, if desired.
	 * 
	 * @param status
	 *            the status to be displayed in the details list
	 * @since 3.1
	 */
	protected final void setStatus(IStatus status) {
		if (this.status != status) {
			this.status = status;
		}

		if (textCreated) {
			text.setText( getStackTrace());
		}
	}

	
    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#isResizable()
     */
    @Override
		protected boolean isResizable() {
    	return true;
    }

    
	
	/**
	 * Instantiate a new DataAdapterErrorDialog.
	 * <p>
	 * The Dialog is not shown until the <code>open()</code> method has been called.
	 * </p>
	 * 
	 * @param parentShell
	 *            <code>Shell</code> in which the dialog is opened.
	 * @param message
	 *            The message of the Dialog.
	 * @param status
	 *            The Status with the root cause.
	 */
	public DataAdapterErrorDialog(Shell parentShell, String message, IStatus status) {
			this(parentShell, "Data Adapter Error", message, status);
	}

	/**
	 * Open an Error Dialog for an generic Exception.
	 * <p>
	 * This method will take the Exception reason from the given Exception class and
	 * display the entire stacktrace in the details.
	 * </p>
	 * <p>
	 * This Dialog is modal and will block until OK is clicked or the dialog is closed with ESC or
	 * the window close button.
	 * </P>
	 * 
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * 
	 * @param message
	 *            The message to display. If null, the message will be taken by the error.
	 *            
	 * @param ex
	 *            The <code>Exception</code> of which display error message and stacktrace.
	 */
	public static void showErrorDialog(Shell parentShell, String message, Throwable ex) {
		
		String errorMessage = (message == null) ? ex.getLocalizedMessage() : message;
		String reason = ex.getLocalizedMessage();
		
    if (reason == null || reason.length() == 0)
    {
    	reason = "No details about the error are availables.";
    }

        
    // Create the IStatus that will be displayed in the dialog
		IStatus status = new Status(IStatus.ERROR, JaspersoftStudioPlugin.getUniqueIdentifier(), reason, ex);
		
		
		// Show the dialog.
		DataAdapterErrorDialog dialog = new DataAdapterErrorDialog(parentShell, errorMessage, status);
		dialog.open();
		return;
	}

  
	private String getStackTrace() {
		
		if (status != null  && status.getException() != null)
		{
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			status.getException().printStackTrace(printWriter);
			return result.toString();
		}
		return "";
  }


}
