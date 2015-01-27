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
package com.jaspersoft.studio.server.action.resource;

import java.text.MessageFormat;
import java.util.UUID;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.data.bean.BeanDataAdapter;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapter;
import net.sf.jasperreports.data.jndi.JndiDataAdapter;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.SecureStorageUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.bean.BeanDataAdapterDescriptor;
import com.jaspersoft.studio.data.jdbc.JDBCDataAdapterDescriptor;
import com.jaspersoft.studio.data.jndi.JndiDataAdapterDescriptor;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceBean;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceJDBC;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceJNDI;

/**
 * Action for importing the selected DataSource in the JRS tree as Data Adapter
 * in JSS.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class ImportDataSourceInJSSAction extends Action {
	public static final String ID = "IMPORT_DATASOURCE_IN_JSS"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	public ImportDataSourceInJSSAction(TreeViewer treeViewer) {
		super();
		setId(ID);
		setText(Messages.ImportDataSourceInJSSAction_ActionText);
		setToolTipText(Messages.ImportDataSourceInJSSAction_ActionTooltip);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(JaspersoftStudioPlugin.PLUGIN_ID, "/icons/resources/eclipse/etool16/import_wiz.gif")); //$NON-NLS-1$
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		return firstElement != null && isValidDataSource(firstElement);
	}

	@Override
	public void run() {
		final Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();

		Job job = new Job("Building report") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("", IProgressMonitor.UNKNOWN);
				try {
					if (firstElement instanceof MResource) {
						MResource mres = (MResource) firstElement;
						mres.setValue(mres.getWsClient().get(monitor, mres.getValue(), null));
						final DataAdapterDescriptor dad = importDataSourceAsDataAdapter(mres);
						UIUtils.getDisplay().syncExec(new Runnable() {

							@Override
							public void run() {
								DataAdapterManager.getPreferencesStorage().addDataAdapter("", dad);//$NON-NLS-1$

								MessageDialog.openInformation(UIUtils.getShell(), Messages.ImportDataSourceInJSSAction_OperationInfoTitle, Messages.ImportDataSourceInJSSAction_OperationInfoMsg);
							}
						});
					}
				} catch (Exception e) {
					UIUtils.showError(e);
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.schedule();
	}

	/*
	 * Right how the allowed type of DataSource are: - JDBC - Bean - JNDI
	 */
	private boolean isValidDataSource(Object element) {
		return element instanceof MRDatasourceJDBC || element instanceof MRDatasourceJNDI || element instanceof MRDatasourceBean;
	}

	/*
	 * Performs the import operation.
	 */
	private DataAdapterDescriptor importDataSourceAsDataAdapter(MResource datasource) {
		if (datasource instanceof MRDatasourceJDBC) {
			MRDatasourceJDBC jdbcDS = (MRDatasourceJDBC) datasource;
			JDBCDataAdapterDescriptor jdbcDA = new JDBCDataAdapterDescriptor();
			JdbcDataAdapter jdbcDataAdapter = (JdbcDataAdapter) jdbcDA.getDataAdapter();
			jdbcDataAdapter.setName(getValidName(jdbcDS.getValue().getLabel(), "JDBC")); //$NON-NLS-1$
			jdbcDataAdapter.setDriver(jdbcDS.getValue().getDriverClass());
			jdbcDataAdapter.setUsername(jdbcDS.getValue().getUsername());
			jdbcDataAdapter.setPassword(getPasswordValue(jdbcDS.getValue().getPassword()));
			jdbcDataAdapter.setUrl(jdbcDS.getValue().getConnectionUrl());
			jdbcDataAdapter.setSavePassword(true);
			return jdbcDA;
		}
		if (datasource instanceof MRDatasourceJNDI) {
			MRDatasourceJNDI jndiDS = (MRDatasourceJNDI) datasource;
			JndiDataAdapterDescriptor jndiDA = new JndiDataAdapterDescriptor();
			JndiDataAdapter jndiDataAdapter = (JndiDataAdapter) jndiDA.getDataAdapter();
			jndiDataAdapter.setName(getValidName(jndiDS.getValue().getLabel(), "JNDI")); //$NON-NLS-1$
			jndiDataAdapter.setDataSourceName(jndiDS.getValue().getJndiName());
			return jndiDA;
		}
		if (datasource instanceof MRDatasourceBean) {
			MRDatasourceBean beanDS = (MRDatasourceBean) datasource;
			BeanDataAdapterDescriptor beanDA = new BeanDataAdapterDescriptor();
			BeanDataAdapter beanDataAdapter = (BeanDataAdapter) beanDA.getDataAdapter();
			beanDataAdapter.setName(getValidName(beanDS.getValue().getLabel(), "Bean")); //$NON-NLS-1$
			beanDataAdapter.setFactoryClass(beanDS.getValue().getBeanName());
			beanDataAdapter.setMethodName(beanDS.getValue().getBeanMethod());
			return beanDA;
		}
		throw new RuntimeException(Messages.ImportDataSourceInJSSAction_DataSourceNotSupportedError);
	}

	/*
	 * Gets a valid name for the new data adapter being created.
	 */
	private String getValidName(String proposedName, String prefix) {
		ADataAdapterStorage prefStorage = DataAdapterManager.getPreferencesStorage();
		if (prefStorage.isDataAdapterNameValid(proposedName)) {
			return proposedName;
		} else {
			MessageFormat msgF = new MessageFormat(Messages.ImportDataSourceInJSSAction_DataAdapterNameTemplate);
			for (int i = 1; i < 1000; i++) {
				String name = msgF.format(new Object[] { prefix, (i > 1) ? "(" + i + ")" : "" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (prefStorage.isDataAdapterNameValid(name)) {
					return name;
				}
			}
			throw new RuntimeException(Messages.ImportDataSourceInJSSAction_UnableToGetNameError);
		}
	}

	/*
	 * Gets the secret storage key or the plain text password value.
	 */
	private String getPasswordValue(String passwordFieldTxt) {
		return JaspersoftStudioPlugin.shouldUseSecureStorage() ? getSecretStorageKey(passwordFieldTxt) : passwordFieldTxt;
	}

	/*
	 * Returns the key that will be used to retrieve the information from the
	 * secure preferences.
	 */
	private String getSecretStorageKey(String pass) {
		try {
			UUID uuidKey = UUID.randomUUID();
			SecureStorageUtils.saveToDefaultSecurePreferences(AbstractDataAdapterService.SECRETS_CATEGORY, uuidKey.toString(), pass);
			return uuidKey.toString();
		} catch (StorageException e) {
			Activator.getDefault().logError(Messages.Common_ErrSecurePrefStorage, e);
		}
		;
		// in case something goes wrong return the clear-text password
		// we will rely on back-compatibility
		return pass;
	}
}
