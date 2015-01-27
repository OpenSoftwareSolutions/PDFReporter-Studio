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
package com.jaspersoft.studio.server.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.MScopedPreferenceStore;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.repo.FileRepositoryService;
import net.sf.jasperreports.repo.RepositoryService;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.part.EditorPart;

import com.jaspersoft.studio.editor.IEditorContributor;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.export.AExporter;
import com.jaspersoft.studio.server.publish.action.JrxmlPublishAction;
import com.jaspersoft.studio.server.publish.wizard.SaveConfirmationDialog;
import com.jaspersoft.studio.utils.AContributorAction;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JSSFileRepositoryService;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JRSEditorContributor implements IEditorContributor {

	public void onLoad(final JasperDesign jd, final EditorPart editor) {
		if (!(editor instanceof JrxmlEditor))
			return;
		String prop = jd.getProperty(AExporter.PROP_SERVERURL);
		if (prop == null)
			return;
		JrxmlEditor jEditor = (JrxmlEditor) editor;
		JasperReportsConfiguration jConfig = jEditor.getJrContext(null);
		JSSFileRepositoryService repService = jConfig.getFileRepositoryService();
		List<RepositoryService> rservices = repService.getRepositoryServices();
		List<RepositoryService> toDel = new ArrayList<RepositoryService>();
		for (RepositoryService rs : rservices)
			if (rs instanceof JRSRepositoryService) {
				toDel.add(rs);
				FileRepositoryService frs = ((JRSRepositoryService) rs).getFileRepositoryService();
				if (frs != null)
					toDel.add(frs);
			}
		rservices.removeAll(toDel);
		rservices.add(new JRSRepositoryService(repService, jConfig));
	}

	public static final String KEY_PUBLISH2JSS = "PUBLISH2JSS";
	public static final String KEY_PUBLISH2JSS_SILENT = "PUBLISH2JSS.SILENT";

	public void onSave(JasperReportsContext jrConfig, IProgressMonitor monitor) {
		JasperReportsConfiguration jConfig = (JasperReportsConfiguration) jrConfig;
		JasperDesign jd = jConfig.getJasperDesign();

		String prop = getServerURL(jd, (IFile) jrConfig.getValue(FileUtils.KEY_FILE));
		if (prop == null)
			return;
		MScopedPreferenceStore pStore = (MScopedPreferenceStore) jConfig.getPrefStore();
		pStore.setWithDefault(false);
		String sRun = Misc.nullIfEmpty(pStore.getString(KEY_PUBLISH2JSS));
		String sAllways = Misc.nullIfEmpty(pStore.getString(KEY_PUBLISH2JSS_SILENT));
		pStore.setWithDefault(true);

		boolean run = sRun == null ? true : Boolean.parseBoolean(sRun);
		boolean allways = sAllways == null ? true : Boolean.parseBoolean(sAllways);
		if (allways) {
			SaveConfirmationDialog dialog = new SaveConfirmationDialog(UIUtils.getShell());
			run = (dialog.open() == Dialog.OK);
			pStore.setValue(KEY_PUBLISH2JSS_SILENT, Boolean.toString(!dialog.getAllways()));
		}

		pStore.setValue(KEY_PUBLISH2JSS, Boolean.toString(run));

		// jConfig.put(KEY_PUBLISH2JSS, run);
		// jConfig.put(KEY_PUBLISH2JSS_SILENT, dialog.getAllways());

		if (run) {
			JrxmlPublishAction action = getAction(monitor, jConfig);
			action.setSilent(run);
			action.run();
		}
	}

	public static String getServerURL(JasperDesign jd, IFile f) {
		String prop = jd.getProperty(AExporter.PROP_SERVERURL);
		if (prop == null && f != null) {
			try {
				prop = f.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, AExporter.PROP_SERVERURL));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	protected static JrxmlPublishAction getAction(IProgressMonitor monitor, JasperReportsConfiguration jrConfig) {
		JrxmlPublishAction publishAction = new JrxmlPublishAction(1, monitor);
		publishAction.setJrConfig(jrConfig);
		return publishAction;
	}

	public void onRun(JasperReportsConfiguration jrConfig, JasperReport jr, Map<String, Object> params) {
		for (JRParameter p : jr.getParameters()) {
			// look if there are JRS built-in parameters, set server value, for this
			// connection
			// cache all of this, preference to do this ?
		}
	}

	public AContributorAction[] getActions() {
		return new AContributorAction[] { new JrxmlPublishAction() };
	}

	@Override
	public String getTitleToolTip(JasperReportsContext jrConfig, String toolTip) {
		String s = toolTip;
		JasperDesign jd = ((JasperReportsConfiguration) jrConfig).getJasperDesign();
		if (jd != null) {
			String p = jd.getProperty(AExporter.PROP_SERVERURL);
			if (p != null)
				s += "\nServer: " + p;
			p = jd.getProperty(AExporter.PROP_REPORTUNIT);
			if (p != null)
				s += "\nReport Unit: " + p;
			p = jd.getProperty(AExporter.PROP_REPORTRESOURCE);
			if (p != null)
				s += "\nResource name: " + p;
		}
		return s;
	}

}
