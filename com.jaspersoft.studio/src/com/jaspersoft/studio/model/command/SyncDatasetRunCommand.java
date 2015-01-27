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
package com.jaspersoft.studio.model.command;

import net.sf.jasperreports.engine.JRException;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.MQuery;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.utils.SyncDatasetRunParameters;

public class SyncDatasetRunCommand extends Command {
	private MDataset dataset;
	private String oldLang;
	private String newLang;

	public SyncDatasetRunCommand(MDataset target, MQuery newValue, MQuery oldValue) {
		super();
		this.dataset = target;
		this.oldLang = oldValue != null && oldValue.getValue() != null ? oldValue.getValue().getLanguage() : null;
		this.newLang = newValue != null && newValue.getValue() != null ? newValue.getValue().getLanguage() : null;
	}

	public SyncDatasetRunCommand(MQuery target, String newValue, String oldValue) {
		super();
		dataset = target.getMdataset();
		this.oldLang = oldValue;
		this.newLang = newValue;
	}

	@Override
	public void execute() {
		try {
			for (IQueryLanguageChanged exec : SyncDatasetRunParameters.changed)
				exec.syncDataset(dataset.getJasperDesign(), dataset.getValue(), oldLang, newLang);
			SyncDatasetRunParameters.syncDataset(dataset, oldLang, newLang);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void undo() {
		try {
			for (IQueryLanguageChanged exec : SyncDatasetRunParameters.changed)
				exec.syncDataset(dataset.getJasperDesign(), dataset.getValue(), newLang, oldLang);
			SyncDatasetRunParameters.syncDataset(dataset, newLang, oldLang);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

}
