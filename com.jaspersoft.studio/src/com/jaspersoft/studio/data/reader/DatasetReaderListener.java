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
package com.jaspersoft.studio.data.reader;

/**
 * Clients that want to be notified when a new record is read 
 * from a dataset should implement this interface.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface DatasetReaderListener {
	
	/**
	 * Notifies the reading of a new record.
	 * <p>
	 * New record information are presented as a data array.
	 * Each element represents the column data for the 
	 * record just read.
	 * 
	 * @param values the record elements read
	 */
	void newRecord(Object[] values);
	
	/**
	 * Notifies the end of the dataset reading task.
	 */
	void finished();
	
	/**
	 * Checks if the listener is an valid status that allows
	 * it to receive the notification of events.
	 * 
	 * @return <code>true</code> if listener status is ok, 
	 * 				<code>false</code> otherwise
	 */
	boolean isValidStatus();
	
	/**
	 * Change the current status of the listener 
	 * in order to invalidate it.
	 */
	void invalidate();
}
