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

import java.util.List;

import org.eclipse.swt.SWT;
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
 * Summarizes the changes due to the refactoring operation.
 * 
 * @author Alexej Strelzow
 */
public class KeyRefactoringSummaryDialog extends KeyRefactoringDialog {

    /** Dialog Model */
    private List<String> changeSet;

    /** GUI */
    private Button okButton;

    private Label changesLabel;
    private Text changesText;

    /**
     * Constructor.
     * 
     * @param parentShell
     *            The parent's shell
     */
    public KeyRefactoringSummaryDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(Composite parent) {

        Composite dialogArea = new Composite(parent, SWT.NONE); // (Composite)
                                                                // super.createDialogArea(parent);
        final GridLayout layout = new GridLayout(1, true);
        dialogArea.setLayout(layout);
        dialogArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                1, 1));
        initLayout(dialogArea);

        return dialogArea;
    }

    /**
     * Initializes the layout
     * 
     * @param parent
     *            The parent
     */
    private void initLayout(Composite parent) {
        changesLabel = new Label(parent, SWT.NONE);
        changesLabel.setText("Changes:");

        changesText = new Text(parent, SWT.BORDER | SWT.MULTI);
        changesText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                1, 1));
        changesText.setEditable(false);
        changesText.setText(getChangeSetText());
    }

    /**
     * @return The text to display (changes)
     */
    private String getChangeSetText() {

        StringBuilder sb = new StringBuilder();

        for (String s : changeSet) {
            sb.append(s + "\r\n");
        }

        return sb.toString();
    }

    /**
     * @param changeSet
     *            The change set of the refactoring operation, which contains
     *            Resource: line number
     */
    public void setChangeSet(List<String> changeSet) {
        this.changeSet = changeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Rename summary");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void okPressed() {
        setReturnCode(OK);
        close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        super.create();
        DialogConfiguration config = getConfig();
        this.setTitle("Summary of key renaming: "
                + config.getPreselectedKey() + " -> " + config.getNewKey());
        this.setMessage("The resource bundle " + config.getPreselectedBundle()
                + " and " + changeSet.size() + " files of the project "
                + config.getProjectName() + " have been successfully modified.");
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

        okButton.setEnabled(true);
    }

}
