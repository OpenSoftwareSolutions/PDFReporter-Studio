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
package com.jaspersoft.studio.server.publish;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.model.AMJrxmlContainer;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.plugin.IPublishContributor;
import com.jaspersoft.studio.server.publish.imp.ImpDataAdapter;
import com.jaspersoft.studio.server.publish.imp.ImpImage;
import com.jaspersoft.studio.server.publish.imp.ImpInputControls;
import com.jaspersoft.studio.server.publish.imp.ImpResourceBundle;
import com.jaspersoft.studio.server.publish.imp.ImpStyleTemplate;
import com.jaspersoft.studio.server.publish.imp.ImpSubreport;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.utils.JRXMLUtils;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JrxmlPublishContributor implements IPublishContributor {
	private JasperReportsConfiguration jrConfig;
	private String version;

	public void publishJrxml(AMJrxmlContainer mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, String version) throws Exception {
		init(mrunit.getJasperConfiguration(), version);
		publishJrxml(mrunit, monitor, jasper, fileset, file);
		if (mrunit instanceof MReportUnit && ResourceDescriptorUtil.isReportMain(file))
			publishParameters((MReportUnit) mrunit, monitor, jasper);
	}

	public void publishParameters(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper) throws Exception {
		impIC.publish(mrunit, monitor, jasper, jrConfig);
		Activator.getExtManager().publishParameters(mrunit, monitor, jasper);
	}

	private void publishJrxml(AMJrxmlContainer mres, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file) throws Exception {
		if (monitor.isCanceled())
			return;
		MReportUnit mrunit = null;
		if (mres instanceof MReportUnit)
			mrunit = (MReportUnit) mres;
		else if (mres.getParent() instanceof MReportUnit)
			mrunit = (MReportUnit) mres.getParent();

		if (mrunit != null) {
			List<JRDesignElement> elements = ModelUtils.getAllElements(jasper);
			for (JRDesignElement ele : elements) {
				if (ele instanceof JRDesignImage)
					publishImage(mrunit, monitor, jasper, fileset, file, ele, version);
				else if (ele instanceof JRDesignSubreport) {
					publishSubreport(mrunit, monitor, jasper, fileset, file, ele, version);
				} else {
					publishElement(mrunit, monitor, jasper, fileset, file, ele, version);
				}
			}
			publishDataAdapters(mrunit, monitor, jasper, fileset, file, version);
			publishBundles(mrunit, monitor, jasper, fileset, file, version);
			publishTemplates(mrunit, monitor, jasper, fileset, file, version);
		}
		// here extend and give possibility to contribute to plugins
		Activator.getExtManager().publishJrxml(mres, monitor, jasper, fileset, file, version);
	}

	protected void publishSubreport(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, JRDesignElement ele, String version) throws Exception {
		MJrxml fres = (MJrxml) impSRP.publish(jasper, ele, mrunit, monitor, fileset, file);
		if (fres == null)
			return;

		IFile[] fs = root.findFilesForLocationURI(fres.getFile().toURI());
		if (fs != null && fs.length > 0) {
			InputStream jrxmlInputStream = JRXMLUtils.getJRXMLInputStream(jrConfig, fs[0].getContents(), fs[0].getFileExtension(), fs[0].getCharset(true), version);
			InputSource is = new InputSource(new InputStreamReader(jrxmlInputStream, "UTF-8"));
			JasperDesign jrd = new JRXmlLoader(jrConfig, JasperReportsConfiguration.getJRXMLDigester()).loadXML(is);
			if (jrd != null) {
				fres.setJd(jrd);
				publishJrxml(fres, monitor, jrd, fileset, fs[0]);
				File f = FileUtils.createTempFile("jrsres", ".jrxml");
				FileUtils.writeFile(f, JRXmlWriterHelper.writeReport(jrConfig, jrd, version));
				fres.setFile(f);
			}
		}
	}

	protected void publishElement(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, JRDesignElement ele, String version) throws Exception {
	}

	protected void publishImage(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, JRDesignElement ele, String version) throws Exception {
		impImg.publish(jasper, ele, mrunit, monitor, fileset, file);
	}

	protected void publishTemplates(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, String version) throws Exception {
		for (JRReportTemplate rt : jasper.getTemplatesList()) {
			impStyle.publish(jasper, rt, mrunit, monitor, fileset, file);
		}
	}

	protected void publishDataAdapters(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, String version) throws Exception {
		List<JRDataset> ds = new ArrayList<JRDataset>();
		ds.add(jasper.getMainDataset());
		List<JRDataset> datasetsList = jasper.getDatasetsList();
		if (datasetsList != null && !datasetsList.isEmpty())
			ds.addAll(datasetsList);
		boolean syncDA = mrunit.getWsClient().getServerProfile().isSyncDA();
		for (JRDataset d : ds) {
			String dapath = d.getPropertiesMap().getProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION);
			if (syncDA && Misc.isNullOrEmpty(dapath)) {
				String name = d.getPropertiesMap().getProperty(DataQueryAdapters.DEFAULT_DATAADAPTER);
				if (!Misc.isNullOrEmpty(name)) {
					ADataAdapterStorage storage = DataAdapterManager.getProjectStorage(((IFile) jrConfig.get(FileUtils.KEY_FILE)).getProject());
					for (DataAdapterDescriptor dad : storage.getDataAdapterDescriptors()) {
						if (dad.getDataAdapter().getName().equals(name)) {
							dapath = storage.getUrl(dad).toString();
							break;
						}
					}
				}
			}
			if (Misc.isNullOrEmpty(dapath))
				continue;

			impDa.publish((JRDesignDataset) d, dapath, mrunit, monitor, fileset, file);
		}
	}

	protected void publishBundles(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, String version) throws Exception {
		List<JRDataset> ds = new ArrayList<JRDataset>();
		ds.add(jasper.getMainDataset());
		List<JRDataset> datasetsList = jasper.getDatasetsList();
		if (datasetsList != null && !datasetsList.isEmpty())
			ds.addAll(datasetsList);
		for (JRDataset d : ds) {
			String dapath = d.getResourceBundle();
			if (dapath == null || dapath.isEmpty())
				continue;
			impBundle.publish(jrConfig, jasper, dapath, mrunit, monitor, fileset, file);
			for (Locale l : Locale.getAvailableLocales())
				impBundle.publish(jrConfig, jasper, dapath + "_" + l.toString(), mrunit, monitor, fileset, file);
		}
	}

	private void init(JasperReportsConfiguration jrConfig, String version) {
		this.jrConfig = jrConfig;
		this.version = version;
		impDa = new ImpDataAdapter(jrConfig);
		impBundle = new ImpResourceBundle(jrConfig);
		impStyle = new ImpStyleTemplate(jrConfig);
		impImg = new ImpImage(jrConfig);
		impSRP = new ImpSubreport(jrConfig);
		impIC = new ImpInputControls(jrConfig);
	}

	private IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	private ImpResourceBundle impBundle;
	private ImpDataAdapter impDa;
	private ImpStyleTemplate impStyle;
	private ImpImage impImg;
	private ImpSubreport impSRP;
	private ImpInputControls impIC;

}
