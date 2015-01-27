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
package com.jaspersoft.studio.server.editor.input.query;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.editor.preview.input.ADataInput;
import com.jaspersoft.studio.editor.preview.input.IParameter;
import com.jaspersoft.studio.server.editor.input.IInput;
import com.jaspersoft.studio.server.editor.input.PResourceDescriptor;

public class QueryInput extends ADataInput {

	private IInput iinput;
	private PResourceDescriptor rdprm;

	public ResourceDescriptor getRD() {
		return rdprm.getResourceDescriptor();
	}

	public boolean isForType(Class<?> valueClass) {
		return ResourceDescriptor.class.isAssignableFrom(valueClass);
	}

	@Override
	public void createInput(Composite parent, final IParameter param,
			final Map<String, Object> params) {
		super.createInput(parent, param, params);
		rdprm = (PResourceDescriptor) param;

		if (getRD().getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY) {
			iinput = new ListInput(this, param, params);
			iinput.createControl(parent, SWT.SINGLE);
		} else if (getRD().getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO) {
			iinput = new TableInput(this, param, params);
			iinput.createControl(parent, SWT.SINGLE | SWT.RADIO);
		} else if (getRD().getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY) {
			iinput = new TableInput(this, param, params);
			iinput.createControl(parent, SWT.MULTI);
		} else if (getRD().getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_QUERY_CHECKBOX) {
			iinput = new TableInput(this, param, params);
			iinput.createControl(parent, SWT.MULTI | SWT.CHECK);
		} else
			return;

		setMandatory(param, iinput.getControl());

	}

	public void updateInput() {
		iinput.updateInput();
	}

	public void fillTable() {
		iinput.fillControl();
	}
}
