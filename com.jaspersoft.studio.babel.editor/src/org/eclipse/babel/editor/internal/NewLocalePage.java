/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.internal;

import java.util.Locale;

import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.util.FileUtils;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.editor.widgets.LocaleSelector;
import org.eclipse.babel.messages.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * Page for adding a new locale (new localized properties file).
 */
public class NewLocalePage extends Composite {

    private Font fontBoldBig = UIUtils.createFont(this, SWT.BOLD, 5);
    private Font fontBold = UIUtils.createFont(this, SWT.BOLD, 1);
    
    /**
     * Constructor.
     * @param parent parent component.
     * @param resourceManager resource manager 
     */
    public NewLocalePage(final Composite parent, final MessagesEditor editor) {
        super(parent, SWT.NONE);
        
        setLayout(new GridLayout());

        Composite block = new Composite(this, SWT.NONE);
        block.setLayout(new GridLayout());
        
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.verticalAlignment = GridData.CENTER;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        block.setLayoutData(gridData);
        
        // Title label
        Label label = new Label(block, SWT.NONE);
        label.setText(Messages.editor_new_title);
        label.setFont(fontBoldBig);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        label.setLayoutData(gridData);

        // Locale selector
        final LocaleSelector localeSelector =  new LocaleSelector(block);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        localeSelector.setLayoutData(gridData);
        localeSelector.selectLocale(Locale.getDefault());
        
        // Create button
        Button createButton = new Button(block, SWT.NULL);
        createButton.setText(Messages.editor_new_create);
        createButton.setFont(fontBold);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.CENTER;
        createButton.setLayoutData(gridData);
		createButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				final Locale newLocale = localeSelector.getSelectedLocale();
				// add local to bundleGroup
				MessagesBundleGroup bundleGroup = editor.getBundleGroup();
				// exists local already?
				boolean existsLocal = false;
				Locale[] locales = bundleGroup.getLocales();
				for (Locale locale : locales) {
					if (locale == null) {
						if (newLocale == null) {
							existsLocal = true;
							break;
						}
					} else if (locale.equals(newLocale)) {
						existsLocal = true;
						break;
					}
				}
				if (!existsLocal){
					IMessagesBundle source =  !bundleGroup.getMessagesBundles().isEmpty() ?  bundleGroup.getMessagesBundles().iterator().next() : null;
					IMessagesBundle copy = bundleGroup.copyMessagesBundle(newLocale, source);
					FileUtils.writeToFile(copy);
					editor.reloadDisplayedContents(true);
				}
			}
		});
		this.layout();
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose() {
        fontBold.dispose();
        fontBoldBig.dispose();
        super.dispose();
    }
}
