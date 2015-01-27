/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.data.reader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.eclipse.builder.JasperReportCompiler;
import net.sf.jasperreports.eclipse.builder.jdt.JRErrorHandler;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.resources.IFile;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.adapter.DataAdapterParameterContributorFactory;
import com.jaspersoft.studio.editor.preview.view.control.ReportControler;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Reader class for generic dataset.
 * <p>
 * Example of usage: data preview in dataset dialog.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class DatasetReader {

	// Data adapter descriptor
	private DataAdapterDescriptor dataAdapterDesc;
	// List of columns that will be used for data preview
	private List<String> columns;
	// Allows to get parameters, fields, query (language and text)
	// and other properties
	private JRDesignDataset designDataset;
	// Max records num
	private int maxRecords = -1;
	// Listeners for dataset reading
	// They will receive event notification for example by a scriptlet
	private List<DatasetReaderListener> listeners;
	// Status flag that lets you know if a report is running
	private boolean running;

	public DatasetReader() {
		listeners = new ArrayList<DatasetReaderListener>();
	}

	/**
	 * Executes the task for the dataset reading task.
	 * <p>
	 * The following sequential steps are performed:
	 * <ol>
	 * <li>loading the jasper design for the custom data preview report;</li>
	 * <li>setting the query information and report language</li>
	 * <li>replacing properties map</li>
	 * <li>adding the standard parameters and custom ones</li>
	 * <li>adding the fields</li>
	 * <li>compiling the report obtaining a jasper report object</li>
	 * <li>setting the parameters (including data adapter contributed ones) map</li>
	 * <li>filling the report</li>
	 * </ol>
	 * 
	 * @param jConfig
	 *          the configuration instance
	 */
	public void start(JasperReportsConfiguration jConfig) {
		InputStream is = null;
		// Temporary replace the class loader to get the "report" one.
		// This is necessary for example to load JDBC drivers or additional
		// classes that are in the classpath of the JasperReports project
		// containing the report.
		ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(jConfig.getClassLoader());
		try {
			running = true;
			// 1. Load JD from custom data preview report
			String reportLocation = JaspersoftStudioPlugin.getInstance().getFileLocation(
					DataPreviewScriptlet.PREVIEW_REPORT_PATH);
			is = new FileInputStream(reportLocation);
			JasperDesign dataJD = JRXmlLoader.load(jConfig, is);

			// 2. Set query information
			JRDesignQuery query = new JRDesignQuery();
			query.setLanguage(designDataset.getQuery().getLanguage());
			query.setText(designDataset.getQuery().getText());
			dataJD.setQuery(query);
			// and the report language to the actual report one
			dataJD.setLanguage(jConfig.getJasperDesign().getLanguage());
			dataJD.setFilterExpression(designDataset.getFilterExpression());
			// 3. Replace properties map
			ModelUtils.replacePropertiesMap(jConfig.getJasperDesign().getPropertiesMap(), dataJD.getPropertiesMap());

			// 4. Set "standard" parameters
			List<JRParameter> parametersList = designDataset.getParametersList();
			for (JRParameter param : parametersList) {
				if (param.isSystemDefined())
					continue;
				if (dataJD.getParametersMap().containsKey(param.getName())) {
					dataJD.removeParameter(param.getName());
				}
				dataJD.addParameter(param);
			}
			// and add the custom ones
			JRDesignParameter pColumns = new JRDesignParameter();
			pColumns.setName(DataPreviewScriptlet.PARAM_COLUMNS);
			pColumns.setValueClass(List.class);
			dataJD.addParameter(pColumns);
			JRDesignParameter pListeners = new JRDesignParameter();
			pListeners.setName(DataPreviewScriptlet.PARAM_LISTENERS);
			pListeners.setValueClass(List.class);
			dataJD.addParameter(pListeners);

			// 5. Add the fields
			if (columns.size() > 0) {
				// Clear "dirty" fields
				dataJD.getFieldsList().clear();
				dataJD.getFieldsMap().clear();
				//
				JRField[] fields = designDataset.getFields();
				for (JRField f : fields) {
					dataJD.addField(f);
				}
			}

			// 6. Compile report
			JasperReport jrobj = null;
			IFile f = (IFile) jConfig.get(FileUtils.KEY_FILE);
			if (f != null) {
				JasperReportCompiler compiler = new JasperReportCompiler();
				compiler.setErrorHandler(new JRErrorHandler(f));
				compiler.setProject(f.getProject());
				jrobj = compiler.compileReport(jConfig, dataJD);
			} else
				jrobj = JasperCompileManager.getInstance(jConfig).compile(dataJD);

			// 7. Prepare parameters
			Map<String, Object> hm = new HashMap<String, Object>();
			if (jConfig.getJRParameters() != null) {
				// add also prompting parameters that may have previously
				// set during a preview phase. This way we can get a more
				// likely "default" value.
				hm.putAll(jConfig.getJRParameters());
			}
			hm = ReportControler.resetParameters(hm, jConfig);
			hm.put(DataPreviewScriptlet.PARAM_COLUMNS, columns);
			hm.put(DataPreviewScriptlet.PARAM_LISTENERS, listeners);
			if (maxRecords > 0) {
				hm.put(JRDesignParameter.REPORT_MAX_COUNT, maxRecords);
			} else {
				hm.remove(JRDesignParameter.REPORT_MAX_COUNT);
			}

			// 8. Contribute parameters from the data adapter
			if (dataAdapterDesc != null)
				jConfig.put(DataAdapterParameterContributorFactory.PARAMETER_DATA_ADAPTER, dataAdapterDesc.getDataAdapter());
			DataAdapterService das = null;
			try {
				das = DataAdapterServiceUtil.getInstance(jConfig).getService(dataAdapterDesc.getDataAdapter());

				das.contributeParameters(hm);

				ModelUtils.replacePropertiesMap(designDataset.getPropertiesMap(), jrobj.getMainDataset().getPropertiesMap());

				JaspersoftStudioPlugin.getExtensionManager().onRun(jConfig, jrobj, hm);

				// 9. Fill the report
				JasperFillManager.getInstance(jConfig).fill(jrobj, hm);
			} finally {
				if (das != null)
					das.dispose();
			}
		} catch (DataPreviewInterruptedException e) {
			e.printStackTrace();
			// DO NOTHING
			// This exception should occur only when
			// a stop on the reading has been invoked.
		} catch (Exception e) {
			UIUtils.showError(e);
			// UIUtils.showError(Messages.DatasetReader_GenericErrorMsg, e);
		} finally {
			FileUtils.closeStream(is);
			running = false;
			for (DatasetReaderListener l : listeners) {
				l.finished();
			}
			Thread.currentThread().setContextClassLoader(originalClassLoader);
		}
	}

	/**
	 * Ends the dataset reading task.
	 */
	public void stop() {
		if (running) {
			for (DatasetReaderListener l : listeners) {
				// Invalidating the listener will cause the running scriptlet
				// to launch a JRScriptletException, that will abort the running report.
				l.invalidate();
			}
		}
	}

	/* Getters and setters */

	public DataAdapterDescriptor getDataAdapterDescriptor() {
		return dataAdapterDesc;
	}

	public void setDataAdapterDescriptor(DataAdapterDescriptor dataAdapterDesc) {
		this.dataAdapterDesc = dataAdapterDesc;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public JRDesignDataset getDesignDataset() {
		return designDataset;
	}

	public void setDesignDataset(JRDesignDataset designDataset) {
		this.designDataset = designDataset;
	}

	public int getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	/* Listener methods */

	/**
	 * Adds a new {@link DatasetReaderListener} to the list of listeners that will be notified when a read event on the
	 * dataset occurs.
	 * 
	 * @param listener
	 *          the listener to add
	 */
	public void addDatasetReaderListener(DatasetReaderListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the specified {@link DatasetReaderListener} from the list of listeners that will be notified when a read
	 * event on the dataset occurs.
	 * 
	 * @param listener
	 *          the listener to remove
	 */
	public void removeDatasetReaderListener(DatasetReaderListener listener) {
		listeners.remove(listener);
	}

	/**
	 * @return <code>true</code> if the dataset reader is running, <code>false</code> otherwise
	 */
	public boolean isRunning() {
		return this.running;
	}

}
