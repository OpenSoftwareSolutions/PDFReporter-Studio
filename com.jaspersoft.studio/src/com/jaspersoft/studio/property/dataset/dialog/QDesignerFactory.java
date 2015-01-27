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
package com.jaspersoft.studio.property.dataset.dialog;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.IQueryDesigner;
import com.jaspersoft.studio.data.designer.AQueryDesignerContainer;
import com.jaspersoft.studio.data.designer.QueryDesigner;

public class QDesignerFactory {
	private Composite parent;
	private Composite toolbar;
	private Map<String, IQueryDesigner> languageMap = new HashMap<String, IQueryDesigner>();
	private Map<Class<? extends IQueryDesigner>, IQueryDesigner> classmap = new HashMap<Class<? extends IQueryDesigner>, IQueryDesigner>();
	private AQueryDesignerContainer dqa;
	IConfigurationElement[] config;

	public QDesignerFactory(Composite parent, Composite toolbar, AQueryDesignerContainer dqa) {
		this.parent = parent;
		this.toolbar = toolbar;
		this.dqa = dqa;
		config = Platform.getExtensionRegistry().getConfigurationElementsFor("com.jaspersoft.studio", "queryDesigner"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void dispose() {
		for (IQueryDesigner qd : languageMap.values())
			qd.dispose();
	}

	public IQueryDesigner getDesigner(String lang) {
		IQueryDesigner qd = languageMap.get(lang.toLowerCase());
		if (qd == null) {
			for (IConfigurationElement e : config) {
				try {
					if (lang.equalsIgnoreCase(e.getAttribute("language"))) {//$NON-NLS-1$
						qd = (IQueryDesigner) e.createExecutableExtension("QueryDesignerClass"); //$NON-NLS-1$
						qd.setParentContainer(dqa);
						qd = addDesigner(lang, qd);
						return qd;
					}
				} catch (CoreException ex) {
					UIUtils.showError(ex);
				}
			}
			qd = addDesigner(lang, getDefaultDesigner());
		}
		return qd;
	}

	protected QueryDesigner getDefaultDesigner() {
		QueryDesigner defaultDesigner = new QueryDesigner();
		defaultDesigner.setParentContainer(dqa);
		return defaultDesigner;
	}

	private IQueryDesigner addDesigner(String lang, IQueryDesigner qd) {
		IQueryDesigner iqd = classmap.get(qd.getClass());
		if (iqd == null) {
			iqd = qd;
			try {
				iqd.createToolbar(toolbar);
				iqd.createControl(parent);
			} catch (Exception e) {
				e.printStackTrace();
				addDesigner(lang, getDefaultDesigner());
			}
			classmap.put(qd.getClass(), iqd);
		}
		languageMap.put(lang.toLowerCase(), iqd);
		return iqd;
	}
}
