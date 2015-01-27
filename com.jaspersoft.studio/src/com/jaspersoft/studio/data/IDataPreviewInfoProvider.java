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

import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * This interface should be implemented by those classes that 
 * are supposed to provide information to deal with data preview.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface IDataPreviewInfoProvider {

	/**
	 * @return the jasper reports configuration
	 */
	JasperReportsConfiguration getJasperReportsConfig();
	
	/**
	 * @return the data adapter currently set
	 */
	DataAdapterDescriptor getDataAdapterDescriptor();

	/**
	 * @return the design dataset
	 */
	JRDesignDataset getDesignDataset();
	
	/**
	 * @return the list of fields currently selected for preview
	 */
	List<JRDesignField> getFieldsForPreview();
	
}
