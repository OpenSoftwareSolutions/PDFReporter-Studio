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
package com.jaspersoft.studio.editor.preview.stats;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactory;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactoryContext;

public class RecordCountScriptletFactory implements ScriptletFactory {

	public RecordCountScriptletFactory() {
	}

	private List<JRAbstractScriptlet> list;

	public List<JRAbstractScriptlet> getScriplets(ScriptletFactoryContext context) throws JRException {
		if (list == null) {
			list = new ArrayList<JRAbstractScriptlet>();
		}
		list.add(new RecordCountScriptlet());
		return list;
	}

	public int getRecordCount() {
		int count = 0;
		if (list != null)
			for (JRAbstractScriptlet s : list)
				if (s instanceof RecordCountScriptlet)
					count += ((RecordCountScriptlet) s).getCounter();
		return count;
	}
}
