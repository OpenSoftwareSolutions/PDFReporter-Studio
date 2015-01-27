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
package com.jaspersoft.studio.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.swt.widgets.Composite;

public abstract class ADataAdapterComposite extends Composite {
	protected DataBindingContext bindingContext;

	public final static String PREFIX = "com.jaspersoft.studio.doc.";

	public ADataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {
		super(parent, style);
		this.jrContext = jrContext;
		bindingContext = new DataBindingContext();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public String getHelpContextId() {
		return PREFIX.concat("dataAdapters_wizard_list");
	}

	public void removeBindings() {
		IObservableList bindings = bindingContext.getBindings();
		for (Object o : bindings) {
			bindingContext.removeBinding((Binding) o);
		}
	}

	public void removeDirtyListenersToContext() {
		IObservableList bindings = bindingContext.getBindings();
		for (Object o : bindings) {
			Binding b = (Binding) o;
			b.getTarget().removeChangeListener(listener);
		}
	}

	public void addDirtyListenersToContext() {
		IObservableList bindings = bindingContext.getBindings();
		for (Object o : bindings) {
			Binding b = (Binding) o;
			b.getTarget().addChangeListener(listener);
		}
	}

	@Override
	public void dispose() {
		IObservableList bindings = bindingContext.getBindings();
		for (Object o : bindings) {
			Binding b = (Binding) o;
			b.getTarget().removeChangeListener(listener);
		}
		super.dispose();
	}

	protected IChangeListener listener = new IChangeListener() {

		public void handleChange(ChangeEvent event) {
			pchangesuport.firePropertyChange("dirty", false, true);
		}
	};
	protected DataAdapterDescriptor dataAdapterDesc;

	public void setDataAdapter(DataAdapterDescriptor dataAdapterDesc) {
		this.dataAdapterDesc = dataAdapterDesc;
		DataAdapter dataAdapter = dataAdapterDesc.getDataAdapter();

		removeDirtyListenersToContext();
		removeBindings();

		bindWidgets(dataAdapter);

		// bindingContext.updateTargets();

		addDirtyListenersToContext();
	}

	private JasperReportsContext jrContext;

	public JasperReportsContext getJrContext() {
		return jrContext;
	}

	protected abstract void bindWidgets(DataAdapter dataAdapter);

	public abstract DataAdapterDescriptor getDataAdapter();

	protected PropertyChangeSupport pchangesuport = new PropertyChangeSupport(this);

	public void addModifyListener(PropertyChangeListener listener) {
		pchangesuport.addPropertyChangeListener(listener);
	}

	public void removeModifyListener(PropertyChangeListener listener) {
		pchangesuport.removePropertyChangeListener(listener);
	}

	/**
	 * This generic method should be used by clients in order to perform additional custom updates involving the UI
	 * components.
	 */
	public void performAdditionalUpdates() {
		// Default: do nothing - subclasses should override if needed
	}

}
