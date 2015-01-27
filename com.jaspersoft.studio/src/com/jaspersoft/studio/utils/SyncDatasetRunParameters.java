/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRQueryExecuterUtils;

import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.command.IQueryLanguageChanged;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SyncDatasetRunParameters {
	private static Map<String, Object[]> bipMap = new HashMap<String, Object[]>();

	public static void addMoreParameters(String lang, Object[] params) {
		bipMap.put(lang, params);
	}

	public static List<IQueryLanguageChanged> changed = new ArrayList<IQueryLanguageChanged>();

	public static void add(IQueryLanguageChanged executer) {
		if (!changed.contains(executer))
			changed.add(executer);
	}

	public static Object[] getBuiltInParameters(JasperReportsConfiguration jConf, String mLang) throws JRException {
		JRQueryExecuterUtils qeUtils = JRQueryExecuterUtils.getInstance(jConf);
		QueryExecuterFactory qef = qeUtils.getExecuterFactory(mLang);
		if (qef != null) {
			Object[] prms = qef.getBuiltinParameters();
			if (bipMap.containsKey(mLang)) {
				Object[] params = bipMap.get(mLang);
				if (prms == null || prms.length == 0)
					return params;

				Object[] tmp = new Object[prms.length + params.length];
				System.arraycopy(prms, 0, tmp, 0, prms.length);
				System.arraycopy(params, 0, tmp, prms.length, params.length);
				return tmp;
			}
			return prms;
		}
		return null;
	}

	public static void syncDatasetRun(MDatasetRun mDsRun, String oldName, String newName) throws JRException {
		JasperDesign jd = mDsRun.getJasperDesign();
		if (jd.getMainDataset().getQuery() != null) {
			String mLang = jd.getMainDataset().getQuery().getLanguage();
			JasperReportsConfiguration jConf = mDsRun.getJasperConfiguration();
			if (jConf == null)
				return;
			String mDsName = jd.getMainDataset().getName();
			JRDesignDatasetRun dsRun = mDsRun.getValue();
			if (dsRun.getDataSourceExpression() != null)
				return;
			if (oldName != null && !mDsName.equals(newName))
				for (JRDataset ds : jd.getDatasetsList()) {
					if (ds.getPropertiesMap().containsProperty(
							DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION))
						continue;
					if (ds.getName().equals(oldName)
							&& (mLang == null || (ds.getQuery() != null && mLang.equals(ds.getQuery().getLanguage())))) {
						Object[] bprms = getBuiltInParameters(jConf, ds.getQuery().getLanguage());
						if (bprms != null)
							cleanDatasetRun(bprms, dsRun);
						break;
					}
				}
			if (newName != null && !mDsName.equals(newName))
				for (JRDataset ds : jd.getDatasetsList()) {
					if (ds.getPropertiesMap().containsProperty(
							DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION))
						continue;
					if (ds.getName().equals(newName)
							&& (mLang == null || (ds.getQuery() != null && mLang.equals(ds.getQuery().getLanguage())))) {
						Object[] bprms = getBuiltInParameters(jConf, ds.getQuery().getLanguage());
						if (bprms != null)
							setupDatasetRun(bprms, dsRun);
						break;
					}
				}
		}
	}

	public static void syncDataset(MDataset mDsRun, String oldLang, String newLang) throws JRException {
		MReport mrep = (MReport) mDsRun.getMreport();
		if (mrep == null)
			return;
		JasperDesign jd = mDsRun.getJasperDesign();
		if (jd.getMainDataset().getQuery() != null) {
			JRDesignDataset subDS = mDsRun.getValue();
			String mDsName = jd.getMainDataset().getName();
			String mLang = jd.getMainDataset().getQuery().getLanguage();
			JasperReportsConfiguration jConf = mDsRun.getJasperConfiguration();
			if (jConf == null)
				return;
			if (subDS.getPropertiesMap().containsProperty(
					DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION))
				return;
			if (subDS == jd.getMainDesignDataset() || mDsName.equals(subDS.getName())) {
				Object[] bprms = getBuiltInParameters(jConf, oldLang);
				if (bprms != null)
					for (JRDataset ds : jd.getDatasetsList()) {
						if (ds.getQuery() != null && ds.getQuery().getLanguage().equals(oldLang))
							for (JRDesignDatasetRun dr : getDatasetRun(mrep, ds))
								cleanDatasetRun(bprms, dr);
					}
				bprms = getBuiltInParameters(jConf, newLang);
				if (bprms != null)
					for (JRDataset ds : jd.getDatasetsList()) {
						if (ds.getQuery() != null && ds.getQuery().getLanguage().equals(newLang))
							for (JRDesignDatasetRun dr : getDatasetRun(mrep, ds))
								setupDatasetRun(bprms, dr);
					}
			} else {
				if (mLang == null || (oldLang != null && oldLang.equals(mLang))) {
					for (JRDesignDatasetRun dr : getDatasetRun(mrep, subDS)) {
						Object[] bprms = getBuiltInParameters(jConf, oldLang);
						if (bprms != null)
							cleanDatasetRun(bprms, dr);
					}
				}
				if (mLang == null || (newLang != null && newLang.equals(mLang))) {
					for (JRDesignDatasetRun dr : getDatasetRun(mrep, subDS)) {
						Object[] bprms = getBuiltInParameters(jConf, newLang);
						if (bprms != null)
							setupDatasetRun(bprms, dr);
					}
				}
			}
		}
	}

	private static void prepareDatasets(JasperDesign jd) {
		for (IQueryLanguageChanged qlc : changed) {
			prepareDataSet(jd, jd.getMainDesignDataset(), qlc);
			for (JRDataset ds : jd.getDatasetsList()) {
				if (ds.getPropertiesMap().containsProperty(
						DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION))
					continue;
				prepareDataSet(jd, (JRDesignDataset) ds, qlc);
			}
		}
	}

	private static void prepareDataSet(JasperDesign jd, JRDesignDataset ds, IQueryLanguageChanged qlc) {
		try {
			if (ds.getQuery() != null)
				qlc.syncDataset(jd, ds, null, ds.getQuery().getLanguage());
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public static void sync(MReport mrep) {
		try {
			JasperReportsConfiguration jConf = mrep.getJasperConfiguration();
			JasperDesign jd = mrep.getValue();

			prepareDatasets(jd);

			if (jd != null && jd.getMainDataset() != null) {
				JRQuery query = jd.getMainDataset().getQuery();
				if (query != null) {
					String mlang = query.getLanguage();
					if (mlang.equals("sql") && Misc.isNullOrEmpty(query.getText())) {
						mlang = null;
					}
					for (JRDataset subds : jd.getDatasetsList()) {
						if (subds.getPropertiesMap().containsProperty(
								DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION))
							continue;
						if (subds.getQuery() != null && (mlang == null || mlang.equals(subds.getQuery().getLanguage()))) {
							try {
								// find query executer, look if there are built-in parameters
								Object[] bprms = getBuiltInParameters(jConf, subds.getQuery().getLanguage());
								if (bprms != null) {
									// find all datasetrun that point to subdataset
									for (JRDesignDatasetRun dr : getDatasetRun(mrep, subds))
										setupDatasetRun(bprms, dr);
								}
							} catch (JRException e) {
								// it's not necessary to log this, because, it's very possible query executer does not exists for some
								// languages
								// it depends on environment, and this is not critical for the user
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Throwable e) {
			// it's not necessary to log this, errors could happen
			e.printStackTrace();
		}
	}

	public static void setupDatasetRun(Object[] bprms, JRDesignDatasetRun dr) throws JRException {
		if (dr.getDataSourceExpression() != null)
			return;
		for (int i = 0; i < bprms.length; i += 2) {
			String pname = (String) bprms[i];
			if (getParameter(dr, pname) != null)
				continue;
			JRDesignDatasetParameter prm = new JRDesignDatasetParameter();
			prm.setName(pname);
			prm.setExpression(new JRDesignExpression("$P{" + pname + "}"));
			dr.addParameter(prm);
		}
		// cleanup?
	}

	public static void cleanDatasetRun(Object[] bprms, JRDesignDatasetRun dr) throws JRException {
		if (dr.getDataSourceExpression() != null)
			return;
		for (int i = 0; i < bprms.length; i += 2) {
			String pname = (String) bprms[i];
			JRDatasetParameter p = getParameter(dr, pname);
			if (p.getExpression() != null && p.getExpression().getText() != null
					&& p.getExpression().getText().equals("$P{" + pname + "}"))
				dr.removeParameter(p);
		}
	}

	private static JRDatasetParameter getParameter(JRDesignDatasetRun dr, String name) {
		for (JRDatasetParameter p : dr.getParameters())
			if (p.getName().equals(name))
				return p;
		return null;
	}

	public static List<JRDesignDatasetRun> getDatasetRun(MReport mrep, JRDataset jDataset) {
		final String dsName = jDataset.getName();
		final List<JRDesignDatasetRun> dsRuns = new ArrayList<JRDesignDatasetRun>();
		new ModelVisitor<Object>(mrep) {

			@Override
			public boolean visit(INode n) {
				if (n instanceof IDatasetContainer) {
					List<MDatasetRun> dsRunList = ((IDatasetContainer) n).getDatasetRunList();
					if (dsRunList != null)
						for (MDatasetRun mdsrun : dsRunList) {
							JRDesignDatasetRun dsrun = mdsrun.getValue();
							if (dsrun.getDataSourceExpression() != null)
								continue;
							if (dsrun.getDatasetName() != null && dsrun.getDatasetName().equals(dsName))
								dsRuns.add(dsrun);
						}
				}
				return true;
			}
		};
		return dsRuns;
	}

}
