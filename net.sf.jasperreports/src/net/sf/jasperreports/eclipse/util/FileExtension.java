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
package net.sf.jasperreports.eclipse.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;

public final class FileExtension {
	public static final String JRXML = "jrxml";
	public static final String PointJRXML = "." + JRXML;
	public static final String JASPER = "jasper";
	public static final String PointJASPER = "." + JASPER;
	public static final String JRPRINT = "jrprint";

	public static final String JRCTX = "jrctx";
	public static final String PointJRCTX = "." + JRCTX;
	public static final String JRTX = "jrtx";
	public static final String PointJRTX = "." + JRTX;

	public static IFile getCompiledFile(final IFile file) {
		String name = getCompiledFileName(file.getName(), file.getFileExtension());
		return file.getParent().getFile(new Path(name));
	}

	public static String getCompiledFileName(String name) {
		String ext = null;
		int ind = name.lastIndexOf(".");
		if (ind >= 0)
			ext = name.substring(ind);
		return getCompiledFileName(name, ext);
	}

	public static String getCompiledFileName(String name, String ext) {
		if (ext != null)
			name = StringUtils.replaceAllIns(name, FileExtension.JRXML + "$", FileExtension.JASPER);
		else
			name = name + FileExtension.PointJASPER;
		return name;
	}

	public static IFile getSourceFile(final IFile file) {
		String name = file.getName();
		String ext = file.getFileExtension();
		if (ext != null)
			name = StringUtils.replaceAllIns(name, FileExtension.JASPER + "$", FileExtension.JRXML);
		else
			name = name + FileExtension.PointJRXML;
		return file.getParent().getFile(new Path(name));
	}

	private static Set<String> imgExt = new HashSet<String>();
	static {
		imgExt.add(".png");
		imgExt.add(".jpeg");
		imgExt.add(".jpg");
		imgExt.add(".jpg2000");
		imgExt.add(".bmp");
		imgExt.add(".tiff");
		imgExt.add(".gif");
		imgExt.add(".ico");
		imgExt.add(".cur");
	}

	public static boolean isImage(String fname) {
		int ind = fname.lastIndexOf(".");
		if (ind > 0)
			fname = fname.substring(ind);
		return imgExt.contains(fname);
	}

	private static Set<String> fontExt = new HashSet<String>();
	static {
		fontExt.add(".ttf");
		fontExt.add(".eot");
		fontExt.add(".wot");
	}

	public static boolean isFont(String fname) {
		int ind = fname.lastIndexOf(".");
		if (ind > 0)
			fname = fname.substring(ind);
		return fontExt.contains(fname);
	}
}
