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
package com.jaspersoft.studio.server.wizard.resource.page.datasource;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.UIUtil;

public class DatasourceCustomPageContent extends APageContent {
	private class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			ResourceProperty rp = (ResourceProperty) element;
			switch (columnIndex) {
			case 0:
				return Misc.nvl(rp.getName());
			case 1:
				return Misc.nvl(rp.getValue());
			}
			return ""; //$NON-NLS-1$
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	public DatasourceCustomPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public DatasourceCustomPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.datasource.custom";
	}

	@Override
	public String getName() {
		return Messages.RDDatasourceJNDIPage_DatasourceTabItem;
	}

	private TableViewer tviewer;
	private Text srvName;

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(composite, "Service Class");

		srvName = new Text(composite, SWT.BORDER);
		srvName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (res.isSupported(Feature.DATASOURCENAME)) {
			UIUtil.createLabel(composite, "Data Source Name");

			final Text dsName = new Text(composite, SWT.BORDER);
			dsName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		Composite cmp = new Composite(composite, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		cmp.setLayoutData(gd);

		Table wtable = new Table(cmp, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 100;
		wtable.setLayoutData(gd);
		wtable.setHeaderVisible(true);
		wtable.setLinesVisible(true);

		TableColumn[] col = new TableColumn[2];
		col[0] = new TableColumn(wtable, SWT.NONE);
		col[0].setText(com.jaspersoft.studio.messages.Messages.common_name);

		col[1] = new TableColumn(wtable, SWT.NONE);
		col[1].setText(com.jaspersoft.studio.messages.Messages.DefaultDataAdapterEditorComposite_valueLabel);

		for (TableColumn tc : col)
			tc.pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, false));
		tlayout.addColumnData(new ColumnWeightData(50, false));
		wtable.setLayout(tlayout);

		tviewer = new TableViewer(wtable);
		tviewer.setContentProvider(new ListContentProvider());
		tviewer.setLabelProvider(new TLabelProvider());
		attachCellEditors(tviewer, wtable);
		UIUtil.setViewerCellEditingOnDblClick(tviewer);

		Composite bGroup = new Composite(cmp, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		final ResourceDescriptor rd = res.getValue();
		new NewButton() {
			@Override
			protected void afterElementAdded(Object selement) {
				rd.setResourceProperty((ResourceProperty) selement);
			}
		}.createNewButtons(bGroup, tviewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				return new ResourceProperty("name", "value");
			}
		});
		final DeleteButton delb = new DeleteButton() {
			@Override
			protected void afterElementDeleted(Object element) {
				rd.removeResourceProperty(((ResourceProperty) element).getName());
			}
		};
		delb.createDeleteButton(bGroup, tviewer);

		ResourceProperty rp = rd.getProperty(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_PROPERTY_MAP);
		if (rp == null) {
			rp = new ResourceProperty(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_PROPERTY_MAP);
			rp.setProperties(new ArrayList<ResourceProperty>());
			rd.setResourceProperty(rp);
		}
		rebind();
		tviewer.setInput(rp.getProperties());
		return composite;
	}

	@Override
	protected void rebind() {
		ResourceDescriptor rd = res.getValue();
		bindingContext.bindValue(SWTObservables.observeText(srvName, SWT.Modify), PojoObservables.observeValue(rd, "serviceClass")); //$NON-NLS-1$

	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("NAME")) //$NON-NLS-1$
					return true;
				if (property.equals("VALUE")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				ResourceProperty prop = (ResourceProperty) element;
				if ("NAME".equals(property)) //$NON-NLS-1$
					return Misc.nvl(prop.getName());
				if ("VALUE".equals(property)) //$NON-NLS-1$
					return Misc.nvl(prop.getValue());
				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				ResourceProperty rp = (ResourceProperty) tableItem.getData();
				if ("NAME".equals(property)) { //$NON-NLS-1$
					ResourceDescriptor rd = res.getValue();
					rd.removeResourceProperty(rp);
					rp.setName((String) value);
					rd.setResourceProperty(rp);
				} else if ("VALUE".equals(property)) { //$NON-NLS-1$
					rp.setValue((String) value);
				}
				tviewer.update(element, new String[] { property });
				tviewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new TextCellEditor(parent) });
		viewer.setColumnProperties(new String[] { "NAME", "VALUE" }); //$NON-NLS-1$ //$NON-NLS-2$ 
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.adapter_jndi";
	}
}
