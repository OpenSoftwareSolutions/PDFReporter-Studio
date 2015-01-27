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
package com.jaspersoft.studio.dnd;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.MDataAdapter;
import com.jaspersoft.studio.messages.Messages;

/**
 * Implementation of a {@link TransferDragSourceListener} that is supposed to handle
 * the drag operation of an {@link MDataAdapter} node from the Repository View.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class DataAdapterDragSourceListener implements TransferDragSourceListener {
	
	private static final String ENCODING = "UTF-8"; //$NON-NLS-1$
	private static final String DATA_ADAPTER_FILE_PREFIX = "dataAdapter"; //$NON-NLS-1$
	private static final String DATA_ADAPTER_FILE_EXT = ".xml";  //$NON-NLS-1$
	private String[] dataAdapterFilesLocations=new String[0];

	@Override
	public void dragStart(DragSourceEvent event) {
		event.doit = !getDragSelection(event).isEmpty();	
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		if(getTransfer().isSupportedType(event.dataType)){
			List<MDataAdapter> dataAdaptersSelected = getDragSelection(event);
			List<String> locations = new ArrayList<String>(dataAdaptersSelected.size());
			for(MDataAdapter da : dataAdaptersSelected){
				locations.add(createTemDataAdapterFile(da));
			}
			dataAdapterFilesLocations = locations.toArray(new String[locations.size()]);
			event.data = dataAdapterFilesLocations;
		}
		else {
			dataAdapterFilesLocations = new String[0];
			event.data = null;
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		for(String daFileLocation : dataAdapterFilesLocations){
			FileUtils.deleteQuietly(new File(daFileLocation));
		}
	}

	@Override
	public Transfer getTransfer() {
		return FileTransfer.getInstance();
	}
	
	/*
	 * Gets the list of data adapters selected. 
	 */
	private List<MDataAdapter> getDragSelection(DragSourceEvent event){
		if(event.getSource() instanceof DragSource){
			List<MDataAdapter> dataAdapters = new ArrayList<MDataAdapter>();
			Control control = ((DragSource)event.getSource()).getControl();
			if(control instanceof Tree && 
					((Tree) control).getSelection().length>0){
				for(TreeItem tItem : ((Tree) control).getSelection()){
					Object data = tItem.getData();
					if(data instanceof MDataAdapter){
						dataAdapters.add((MDataAdapter) data);
					}
				}
				return dataAdapters;				
			}
		}
		return new ArrayList<MDataAdapter>(0);
	}

	/*
	 * Creates a temporary file for the specified data adapter.
	 */
	private String createTemDataAdapterFile(final MDataAdapter dataAdapter) {
		try {
			File tempDirectory = FileUtils.getTempDirectory();
			String tempDataAdapterFilePath = tempDirectory.getAbsolutePath() + "/" //$NON-NLS-1$
					+ StringUtils.deleteWhitespace(dataAdapter.getDisplayText()) + DATA_ADAPTER_FILE_EXT;
			File tempDataAdapterFile = new File(tempDataAdapterFilePath);
			if (tempDataAdapterFile.exists()) {
				// fallback solution
				tempDataAdapterFile = File.createTempFile(DATA_ADAPTER_FILE_PREFIX, DATA_ADAPTER_FILE_EXT);
			}
			String xml = DataAdapterManager.toDataAdapterFile(dataAdapter.getValue());
			FileUtils.writeByteArrayToFile(tempDataAdapterFile, xml.getBytes(ENCODING));
			return tempDataAdapterFile.getAbsolutePath();
		} catch (UnsupportedEncodingException e) {
			JaspersoftStudioPlugin.getInstance().logError(
					NLS.bind(Messages.DataAdapterDragSourceListener_EncondingErrorMsg, ENCODING), e);
		} catch (IOException e) {
			JaspersoftStudioPlugin.getInstance().logError(Messages.DataAdapterDragSourceListener_IOErrorMsg, e);
		}
		return null;
	}
	
}
