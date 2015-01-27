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

import java.io.FileInputStream;
import java.io.InputStream;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;

import com.jaspersoft.studio.data.storage.FileDataAdapterStorage;

public class MatchDataAdapter implements IEditorMatchingStrategy {

	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		InputStream in = null;
		IPath path = null;
		try {

			if (input instanceof FileStoreEditorInput) {
				FileStoreEditorInput fsei = (FileStoreEditorInput) input;
				path = new Path(fsei.getURI().getPath());
			} else if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				path = file.getRawLocation();
			}
			if (!path.getFileExtension().equals("xml"))
				return false;

			IPath refpath = null;
			IEditorInput editorRefInput = editorRef.getEditorInput();
			if (editorRefInput instanceof FileStoreEditorInput) {
				FileStoreEditorInput fsei = (FileStoreEditorInput) editorRefInput;
				refpath = new Path(fsei.getURI().getPath());
			} else if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				refpath = file.getRawLocation();
			}
			if (!path.toOSString().equals(refpath.toOSString()))
				return false;

			return FileDataAdapterStorage.readDataADapter(new FileInputStream(path.toOSString()), null) != null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtils.closeStream(in);
		}
		return false;
	}

}
