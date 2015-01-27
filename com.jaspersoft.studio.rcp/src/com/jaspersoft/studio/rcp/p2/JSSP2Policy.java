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
package com.jaspersoft.studio.rcp.p2;

import org.eclipse.equinox.p2.engine.query.UserVisibleRootQuery;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.ui.Policy;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Point;

import com.jaspersoft.studio.rcp.Activator;

/**
 * JSSP2Policy defines the Jaspersoft Studio policies for the p2 UI. 
 * The policy is registered as an OSGi service when the JSS bundle starts.
 * 
 * @author Massimo Rabbi (mrabb@users.sourceforge.net)
 */
public class JSSP2Policy extends Policy {
	
	public JSSP2Policy() {
		super();
		setRepositoriesVisible(false);
	}

	public void updateForPreferences() {
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		setRepositoriesVisible(prefs
				.getBoolean(PreferenceConstants.REPOSITORIES_VISIBLE));
		setRestartPolicy(prefs.getInt(PreferenceConstants.RESTART_POLICY));
		setShowLatestVersionsOnly(prefs
				.getBoolean(PreferenceConstants.SHOW_LATEST_VERSION_ONLY));
		setGroupByCategory(prefs
				.getBoolean(PreferenceConstants.AVAILABLE_GROUP_BY_CATEGORY));
		setShowDrilldownRequirements(prefs
				.getBoolean(PreferenceConstants.SHOW_DRILLDOWN_REQUIREMENTS));
		setFilterOnEnv(prefs.getBoolean(PreferenceConstants.FILTER_ON_ENV));
		setUpdateWizardStyle(prefs.getInt(PreferenceConstants.UPDATE_WIZARD_STYLE));
		int preferredWidth = prefs.getInt(PreferenceConstants.UPDATE_DETAILS_WIDTH);
		int preferredHeight = prefs.getInt(PreferenceConstants.UPDATE_DETAILS_HEIGHT);
		setUpdateDetailsPreferredSize(new Point(preferredWidth, preferredHeight));		
		
		if (prefs.getBoolean(PreferenceConstants.AVAILABLE_SHOW_ALL_BUNDLES))
			setVisibleAvailableIUQuery(QueryUtil.ALL_UNITS);
		else
			setVisibleAvailableIUQuery(QueryUtil.createIUGroupQuery());
		if (prefs.getBoolean(PreferenceConstants.INSTALLED_SHOW_ALL_BUNDLES))
			setVisibleAvailableIUQuery(QueryUtil.ALL_UNITS);
		else
			setVisibleAvailableIUQuery(new UserVisibleRootQuery());
	}
}
