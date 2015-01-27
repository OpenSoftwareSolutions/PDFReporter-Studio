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
package com.jaspersoft.studio.data.ejbql;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.messages.Messages;
import com.jaspersoft.studio.data.querydesigner.ejbql.EJBQLLineStyler;
import com.jaspersoft.studio.data.ui.SimpleQueryWizardDataEditorComposite;

/**
 * Editor composite for the EJBQL query language. This is supposed to used by
 * {@link EjbqlDataAdapterDescriptor}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 */
public class EjbqlWizardDataEditorComposite extends SimpleQueryWizardDataEditorComposite {

	private EJBQLLineStyler lineStyler = new EJBQLLineStyler();

	public EjbqlWizardDataEditorComposite(Composite parent, WizardPage page, DataAdapterDescriptor dataAdapterDescriptor) {
		super(parent, page, dataAdapterDescriptor, "ejbql"); //$NON-NLS-1$ 
		setTitle(Messages.EjbqlWizardDataEditorComposite_Title);
		styledText.addLineStyleListener(lineStyler);
		styledText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				lineStyler.parseBlockComments(styledText.getText());
			}
		});
	}

}
