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
package com.jaspersoft.studio.server.wizard.resource.page.selector;

import java.io.File;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.wizard.resource.page.AFileResourcePageContent;

public abstract class ATextFileResourcePageContent extends AFileResourcePageContent {
	public ATextFileResourcePageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public ATextFileResourcePageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	private Text txt;

	@Override
	protected void createFileTab(Composite composite) {
		txt = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		txt.setLayoutData(gd);
	}

	@Override
	protected void handleFileChange() {
		super.handleFileChange();
		UIUtils.getDisplay().asyncExec(new Runnable() {

			public void run() {
				try {
					File f = ((AFileResource) res).getFile();
					if (f == null && !res.getValue().getIsNew()) {
						f = File.createTempFile("jrsimgfile", ".properties"); //$NON-NLS-1$ //$NON-NLS-2$
						f.deleteOnExit();
						f.createNewFile();
						WSClientHelper.getResource(new NullProgressMonitor(), res.getRoot() != null ? res : pnode, res.getValue(), f);
					}
					if (f != null && f.exists())
						txt.setText(FileUtils.readFileAsAString(f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
