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
package com.jaspersoft.studio.data.designer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AQueryDesignerContainer {
	public static final int CONTAINER_WITH_INFO_TABLES = 0x02;
	public static final int CONTAINER_WITH_NO_TABLES = 0x01;

	protected AQueryStatus qStatus;

	protected abstract void createStatusBar(Composite comp);

	public AQueryStatus getQueryStatus() {
		return qStatus;
	}

	public abstract void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable)
			throws InvocationTargetException, InterruptedException;

	public abstract void setFields(List<JRDesignField> fields);

	public abstract void setParameters(List<JRDesignParameter> params);

	public abstract DataAdapterDescriptor getDataAdapter();

	public void doGetFields() {
		try {
			run(true, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					doGetFields(monitor);
				}
			});
		} catch (InvocationTargetException ex) {
			getQueryStatus().showError(ex.getTargetException());
		} catch (InterruptedException ex) {
			getQueryStatus().showError(ex);
		}
	}

	protected void doGetFields(IProgressMonitor monitor) {
	}

	public abstract int getContainerType();

	protected JasperReportsConfiguration jConfig;

	public JasperReportsConfiguration getjConfig() {
		return jConfig;
	}

	public abstract List<JRDesignField> getCurrentFields();
}
