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
package com.jaspersoft.studio.server.wizard.resource.page;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.utils.IPageCompleteListener;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.server.wizard.resource.page.selector.ASelector;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDataType;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorLov;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorQuery;
import com.jaspersoft.studio.utils.UIUtil;

public class InputControlPageContent extends APageContent implements IPageCompleteListener {

	public InputControlPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public InputControlPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.ice"; //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return Messages.RDInputControlPage_inputcontroltableitem;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		new Label(composite, SWT.NONE);

		Composite cmp = new Composite(composite, SWT.NONE);
		cmp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cmp.setLayout(new RowLayout());
		cmp.setBackground(parent.getBackground());

		bmand = new Button(cmp, SWT.CHECK);
		bmand.setText(Messages.RDInputControlPage_mandatory);

		bread = new Button(cmp, SWT.CHECK);
		bread.setText(Messages.RDInputControlPage_readonly);

		bvisible = new Button(cmp, SWT.CHECK);
		bvisible.setText(Messages.RDInputControlPage_visible);

		UIUtil.createLabel(composite, Messages.RDInputControlPage_type);

		ctype = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		ctype.setItems(new String[] { Messages.InputControlPageContent_boolean, Messages.InputControlPageContent_singleValue, Messages.RDInputControlPage_singlselectlistofvalues,
				Messages.RDInputControlPage_singleselectlovradio, Messages.RDInputControlPage_multiselectlov, Messages.RDInputControlPage_multiselectlovradio, Messages.RDInputControlPage_singlselectquery,
				Messages.RDInputControlPage_singleselectqueryradio, Messages.RDInputControlPage_multiselectquery, Messages.RDInputControlPage_multiselectquerycheckbox });

		stackComposite = new Composite(composite, SWT.NONE);
		stackLayout = new StackLayout();
		stackComposite.setLayout(stackLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		stackComposite.setLayoutData(gd);

		cvalue = new Composite(stackComposite, SWT.NONE);
		createSingleValue(stackComposite);
		createLOV(stackComposite);
		createQuery(stackComposite);

		ctype.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleTypeChanged(ctype, stackLayout);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		rebind();
		return composite;
	}

	@Override
	protected void rebind() {
		bindingContext.bindValue(SWTObservables.observeSingleSelectionIndex(ctype), PojoObservables.observeValue(getProxy(res.getValue()), "controlType")); //$NON-NLS-1$

		bindingContext.bindValue(SWTObservables.observeSelection(bmand), PojoObservables.observeValue(res.getValue(), "mandatory")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bread), PojoObservables.observeValue(res.getValue(), "readOnly")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bvisible), PojoObservables.observeValue(res.getValue(), "visible")); //$NON-NLS-1$
		if (tvalue != null)
			bindingContext.bindValue(SWTObservables.observeText(tvalue, SWT.Modify), PojoObservables.observeValue(res.getValue(), "queryValueColumn")); //$NON-NLS-1$

		handleTypeChanged(ctype, stackLayout);
	}

	protected void handleTypeChanged(Combo ctype, StackLayout stackLayout) {
		ASelector newSelector = null;
		int s = ctype.getSelectionIndex();
		if (cSelector != null)
			cSelector.removePageCompleteListener(this);
		// cSelector = null;
		if (s < 1) {
			stackLayout.topControl = cvalue;
			res.getValue().getChildren().clear();
		} else {
			if (s < 2) {
				stackLayout.topControl = csinglevalue;
				newSelector = sDataType;
			} else if (s < 6) {
				stackLayout.topControl = clov;
				newSelector = sLov;
			} else {
				stackLayout.topControl = cquery;
				newSelector = sQuery;
			}
			newSelector.addPageCompleteListener(this);
			setPageComplete(newSelector.isPageComplete());
		}
		if (newSelector != cSelector) {
			if (cSelector != null && newSelector != null) {
				cleanResource();
				newSelector.resetResource();
			}
			cSelector = newSelector;
		}
		stackComposite.layout();
	}

	private void cleanResource() {
		ResourceDescriptor r = res.getValue();
		r.getChildren().clear();
	}

	@Override
	public void pageCompleted(boolean completed) {
		setPageComplete(cSelector.isPageComplete());
	}

	private ASelector cSelector;
	private SelectorDataType sDataType;
	private SelectorLov sLov;
	private SelectorQuery sQuery;

	private Composite stackComposite;
	private Composite cvalue;
	private Group clov;
	private Group csinglevalue;
	private TabFolder cquery;

	protected void createSingleValue(Composite composite) {
		csinglevalue = new Group(composite, SWT.NONE);
		csinglevalue.setText(Messages.RDInputControlPage_datatype);
		csinglevalue.setLayout(new GridLayout(3, false));

		sDataType = new SelectorDataType();
		sDataType.createControls(csinglevalue, pnode, res);
	}

	protected void createLOV(Composite composite) {
		clov = new Group(composite, SWT.NONE);
		clov.setText(Messages.RDInputControlPage_lov);
		clov.setLayout(new GridLayout(3, false));

		sLov = new SelectorLov();
		sLov.createControls(clov, pnode, res);
	}

	protected void createQuery(Composite composite) {
		cquery = new TabFolder(composite, SWT.NONE);

		TabItem item = new TabItem(cquery, SWT.NONE);
		item.setText(Messages.RDInputControlPage_queryresource);

		Composite cmp = new Composite(cquery, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		item.setControl(cmp);

		sQuery = new SelectorQuery();
		sQuery.createControls(cmp, pnode, res);

		item = new TabItem(cquery, SWT.NONE);
		item.setText(Messages.RDInputControlPage_valueandvisiblecolumns);

		cmp = new Composite(cquery, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		item.setControl(cmp);

		UIUtil.createLabel(cmp, Messages.RDInputControlPage_valuecolumn);

		tvalue = new Text(cmp, SWT.BORDER);
		tvalue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		cmp = new Composite(cmp, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		cmp.setLayoutData(gd);

		qvct = new QueryVisibleColumnsTable(cmp, res.getValue(), this, sQuery);

		tvalue.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				pageCompleted(sQuery.isPageComplete());
				// page.setPageComplete(sQuery.isPageComplete());
			}
		});

	}

	@Override
	public void dispose() {
		if (cSelector != null)
			cSelector.removePageCompleteListener(this);
		super.dispose();
	}

	private ShiftMapProxy getProxy(ResourceDescriptor rd) {
		proxy.setResourceDescriptor(rd);
		return proxy;
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.editInputControl";
	}

	private ShiftMapProxy proxy = new ShiftMapProxy();
	private QueryVisibleColumnsTable qvct;
	private Button bmand;
	private Button bread;
	private Button bvisible;
	private Combo ctype;
	private Text tvalue;
	private StackLayout stackLayout;

	class ShiftMapProxy {
		private ResourceDescriptor rd;
		private final int[] shift = new int[] { ResourceDescriptor.IC_TYPE_BOOLEAN, ResourceDescriptor.IC_TYPE_SINGLE_VALUE, ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES,
				ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO, ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES, ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX,
				ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY, ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO, ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY,
				ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX };

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
