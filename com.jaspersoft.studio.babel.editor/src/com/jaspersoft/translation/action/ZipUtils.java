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
package com.jaspersoft.translation.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * offers the method to zip a file or a folder
 */
public class ZipUtils {

	/**
	 * Zip function zip all files and folders
	 * 
	 * @param srcFolder source folder
	 * @param destZipFile destination file, must contain the filename.zip
	 * @return true if the operation was sucessfull, otherwise false
	 */
	public boolean zipFiles(String srcFolder, String destZipFile) {
		boolean result = false;
		try {
			zipFolder(srcFolder, destZipFile);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Zip function zip all files and folders
	 * 
	 * @param srcFolder source folder
	 * @param destZipFile destination file, must contain the filename.zip
	 */
	private void zipFolder(String srcFolder, String destZipFile)
			throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		
		//create the output stream to zip file result
		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);
		
		//add the folder to the zip
		addFolderToZip("", srcFolder, zip);
		
		// close the zip objects
		zip.flush();
		zip.close();
	}

	/**
	 * recursively add files to the zip files
	 * 
	 * @param flag true if the file added is an empty folder, false otherwise
	 */
	private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws Exception {
		//create the file object for inputs
		File folder = new File(srcFile);

		//if the folder is empty add empty folder to the Zip file
		if (flag == true) {
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
		} else { 
			//if the current name is directory, recursively traverse it to
			//get the files
			if (folder.isDirectory()) {
				//if folder is not empty
				addFolderToZip(path, srcFile, zip);
			} else {
				//write the file to the output
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					//Write the Result
					zip.write(buf, 0, len);
				}
				in.close();
			}
		}
	}

	/**
	 * add folder to the zip file
	 */
	private void addFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		//check the empty folder
		if (folder.list().length == 0) {
			addFileToZip(path, srcFolder, zip, true);
		} else {
			//list the files in the folder
			for (String fileName : folder.list()) {
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
				} else {
					addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
				}
			}
		}
	}
}
