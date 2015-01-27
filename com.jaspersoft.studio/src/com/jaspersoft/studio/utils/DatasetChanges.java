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
package com.jaspersoft.studio.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.jasperreports.engine.design.JRDesignDataset;

/**
 * Property change listener used by the intepreter cache utilities to 
 * discard the cache of a dataset interpreter when something in that
 * dataset change. In general there is one of this listener for dataset
 * and should be placed on the dataset and on its children.
 * 
 * @author Orlandin Marco
 *
 */
public class DatasetChanges implements PropertyChangeListener{
	
	/**
	 * The dataset or the parent dataset if the listener is placed 
	 * on a dataset child (variables, fields....). It will be also 
	 * used as key to know which element to remove from the cache
	 */
	private JRDesignDataset parentDataset;
	
	/**
	 * Create an instance of the class
	 * 
	 * @param datasetKey The dataset or the parent dataset if the listener is placed 
	 * on a dataset child (variables, fields....). It will be also 
	 * used as key to know which element to remove from the cache
	 */
	public DatasetChanges(JRDesignDataset datasetKey){
		this.parentDataset = datasetKey;
	}
	
	/**
	 * When something change the interpreter cached for this dataset
	 * is discared
	 * 
	 * FIXME: an improvement could be to check if the changed 
	 * property invalidate the intepreter
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ExpressionUtil.removeCachedInterpreter(parentDataset);
	}
	
}
