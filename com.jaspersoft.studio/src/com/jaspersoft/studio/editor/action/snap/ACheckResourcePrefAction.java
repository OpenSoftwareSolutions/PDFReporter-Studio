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
package com.jaspersoft.studio.editor.action.snap;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class ACheckResourcePrefAction extends AResourcePreferenceAction {

	/**
	 * Constructor
	 * 
	 * @param diagramViewer
	 *          the GraphicalViewer whose grid enablement and visibility properties are to be toggled
	 */
	public ACheckResourcePrefAction(String text, JasperReportsConfiguration jrConfig) {
		super(text, jrConfig, AS_CHECK_BOX);
	}

	/**
	 * @see org.eclipse.jface.action.IAction#isChecked()
	 */
	public boolean isChecked() {
		String p = getProperty();
		return jrConfig.getPropertyBooleanDef(p, false);
	};

	@Override
	protected void doRun() throws Exception {
		getStore().setValue(getProperty(), Boolean.toString(!isChecked()));
	}

	protected abstract String getProperty();
}
