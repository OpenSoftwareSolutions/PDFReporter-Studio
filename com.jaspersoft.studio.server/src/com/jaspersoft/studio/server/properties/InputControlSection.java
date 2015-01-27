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
package com.jaspersoft.studio.server.properties;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.model.MListOfValues;
import com.jaspersoft.studio.server.model.MRQuery;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.properties.dialog.RepositoryDialog;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.wizard.find.FindResourceJob;

public class InputControlSection extends ASection {
	private Combo ctype;
	private Button bmand;
	private Button bread;
	private Button bvisible;
	private Text trefuri;
	private Button bbrowse;

	@Override
	protected void createSectionControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		AbstractSection.createLabel(parent, getWidgetFactory(), "", 120);

		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cmp.setLayout(new RowLayout());
		cmp.setBackground(parent.getBackground());

		bmand = getWidgetFactory().createButton(cmp, "Mandatory", SWT.CHECK);
		// bmand.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		bread = getWidgetFactory().createButton(cmp, "Read Only", SWT.CHECK);
		// bread.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		bvisible = getWidgetFactory().createButton(cmp, "Visible", SWT.CHECK);
		// bvisible.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		AbstractSection.createLabel(parent, getWidgetFactory(), "Type", 120);

		ctype = getWidgetFactory().createCombo(parent, SWT.BORDER | SWT.READ_ONLY);
		ctype.setItems(new String[] { "Boolean", "Single Value", "Single Select List of Values", "Single Select List of Values (Radio)", "Multi Select List of Values",
				"Multi Select List of Values (Checkbox)", "Single Select Query", "Single Select Query (Radio)", "Multi Select Query", "Multi Select Query (Checkbox)" });

		AbstractSection.createLabel(parent, getWidgetFactory(), "Referenced List of values", 120);

		cmp = new Composite(parent, SWT.NONE);
		cmp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		cmp.setLayout(layout);
		cmp.setBackground(parent.getBackground());

		trefuri = getWidgetFactory().createText(cmp, "", SWT.BORDER | SWT.READ_ONLY);
		trefuri.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		bbrowse = new Button(cmp, SWT.PUSH);
		bbrowse.setText("...");
		bbrowse.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				MServerProfile msp = ServerManager.getMServerProfileCopy((MServerProfile) res.getRoot());
				if (res.isSupported(Feature.SEARCHREPOSITORY)) {

					ResourceDescriptor rd = FindResourceJob.doFindResource(msp, new String[] { ResourceMediaType.LIST_OF_VALUES_CLIENT_TYPE, ResourceMediaType.QUERY_CLIENT_TYPE }, null);
					if (rd != null) {
						res.getValue().setReferenceUri(rd.getUriString());
						bindingContext.updateTargets();
					}
				} else {
					RepositoryDialog rd = new RepositoryDialog(UIUtils.getShell(), msp) {

						@Override
						public boolean isResourceCompatible(MResource r) {
							return (r instanceof MListOfValues) || (r instanceof MRQuery);
						}

					};
					if (rd.open() == Dialog.OK) {
						MResource rs = rd.getResource();
						if (rs != null) {
							res.getValue().setReferenceUri(rs.getValue().getUriString());
							bindingContext.updateTargets();
						}
					}
				}
			}
		});
	}

	@Override
	public void enableFields(boolean enable) {
		ctype.setEnabled(enable);
		bmand.setEnabled(enable);
		bread.setEnabled(enable);
		bvisible.setEnabled(enable);
		trefuri.setEditable(enable);
		bbrowse.setEnabled(enable);
	}

	@Override
	protected void bind() {
		bindingContext.bindValue(SWTObservables.observeSingleSelectionIndex(ctype), PojoObservables.observeValue(getProxy(res.getValue()), "controlType"));

		bindingContext.bindValue(SWTObservables.observeSelection(bmand), PojoObservables.observeValue(res.getValue(), "mandatory"));
		bindingContext.bindValue(SWTObservables.observeSelection(bread), PojoObservables.observeValue(res.getValue(), "readOnly"));
		bindingContext.bindValue(SWTObservables.observeSelection(bvisible), PojoObservables.observeValue(res.getValue(), "visible"));
	}

	private ShiftMapProxy getProxy(ResourceDescriptor rd) {
		proxy.setResourceDescriptor(rd);
		return proxy;
	}

	private ShiftMapProxy proxy = new ShiftMapProxy();

	class ShiftMapProxy {
		private ResourceDescriptor rd;
		private final int[] shift = new int[] { 1, 2, 3, 8, 6, 10, 4, 9, 7, 11 };

		public void setResourceDescriptor(ResourceDescriptor rd) {
			this.rd = rd;
		}

		public void setControlType(int type) {
			rd.setControlType((byte) shift[type]);
		}

		public int getControlType() {
			for (int i = 0; i < shift.length; i++)
				if (shift[i] == rd.getControlType())
					return i;
			return -1;
		}
	}
}
