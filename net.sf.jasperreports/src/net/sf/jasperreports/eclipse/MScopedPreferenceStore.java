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
package net.sf.jasperreports.eclipse;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class MScopedPreferenceStore extends ScopedPreferenceStore {

	public MScopedPreferenceStore(IScopeContext context, String qualifier) {
		super(context, qualifier);
	}

	@Override
	public IEclipsePreferences[] getPreferenceNodes(boolean includeDefault) {
		return super.getPreferenceNodes(withDefault);
	}

	private boolean withDefault = true;

	public void setWithDefault(boolean withDefault) {
		this.withDefault = withDefault;
	}
}
