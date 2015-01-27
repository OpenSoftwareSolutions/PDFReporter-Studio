package com.jaspersoft.studio.server.action.resource;

import net.sf.jasperreports.eclipse.ui.ATitledDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;

public class PasteDialog extends ATitledDialog {
	public static final int REPLACE = 0;
	public static final int SKIP = 1;
	public static final int COPY = 2;

	protected PasteDialog(Shell parentShell, MResource res) {
		super(parentShell);
		setTitle(Messages.PasteDialog_0 + res.getValue().getUriString());
		setDescription(Messages.PasteDialog_1);
		setDefaultSize(450, 300);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);

		Button bSkip = new Button(cmp, SWT.PUSH);
		bSkip.setText(Messages.PasteDialog_2);
		bSkip.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bSkip.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				choise = SKIP;
				close();
			}
		});

		Button bReplace = new Button(cmp, SWT.PUSH);
		bReplace.setText(Messages.PasteDialog_3);
		bReplace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bReplace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				choise = REPLACE;
				close();
			}
		});

		// Button bCreate = new Button(cmp, SWT.PUSH);
		// bCreate.setText(Messages.PasteDialog_4);
		// bCreate.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// bCreate.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// choise = COPY;
		// close();
		// }
		// });

		final Button bAll = new Button(cmp, SWT.CHECK);
		bAll.setText(Messages.PasteDialog_5);
		bAll.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				forall = bAll.getSelection();
			}
		});

		return cmp;
	}

	private boolean forall = false;

	public boolean getForAll() {
		return forall;
	}

	private int choise = 0;

	public int getChoise() {
		return choise;
	}

}
