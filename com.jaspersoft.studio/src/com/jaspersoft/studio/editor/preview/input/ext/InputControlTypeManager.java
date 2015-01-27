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
package com.jaspersoft.studio.editor.preview.input.ext;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.input.IDataInput;

public class InputControlTypeManager {
	public void init() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "inputcontroltypes"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("InputControlProvider"); //$NON-NLS-1$
				if (o instanceof IInputControlTypeProvider)
					nodeFactory.add((IInputControlTypeProvider) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private List<IInputControlTypeProvider> nodeFactory = new ArrayList<IInputControlTypeProvider>();

	public List<IDataInput> getInputControlTypes() {
		List<IDataInput> tlist = new ArrayList<IDataInput>();
		for (IInputControlTypeProvider ictp : nodeFactory) {
			IDataInput[] types = ictp.getInputControlTypes();
			if (types != null && types.length > 0)
				for (IDataInput di : types)
					tlist.add(di);
		}
		return tlist;
	}
}
