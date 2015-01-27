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
package com.jaspersoft.studio.translation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Class that contains static methods to manipulate files on the file system and create Jars
 * 
 * @author Orlandin Marco
 *
 */
public class JarFileUtils {
	
	/**
	 * Copy a file on the file system from a source to a destination
	 * 
	 * @param sourceFile reference to the source file
	 * @param targetDir reference to the target folder of the copy. The copy will have
	 * the same name of the original
	 */
	public static void copyFile(File sourceFile, File targetDir) {
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		File copy = new File(targetDir.getAbsolutePath() + File.separatorChar + sourceFile.getName());
		InputStream is = null;
		if (!copy.exists()) {
			try {
				is = new FileInputStream(sourceFile);
				FileOutputStream fwr = new FileOutputStream(copy);
				copy.createNewFile();
				try {
					byte[] buff = new byte[1024];
					int read;
					while ((read = is.read(buff)) != -1) {
						fwr.write(buff, 0, read);
					}
				} finally {
					fwr.close();
				}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (is != null) is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
	}
	
	/**
	 * Delete a file or a folder from the file system. The folder 
	 * is deleted also if it is not empty. In this case its content
	 * is deleted recursively
	 * 
	 * @param file file or directory to delete
	 */
	public static void delete(File file)
	{
		 if(file.isDirectory()){
			 if(file.list().length==0){
				 file.delete();
	     }else{
	    		//list all the directory contents
	        String files[] = file.list();
	        for (String temp : files) {
	        	//construct the file structure
	        	File fileDelete = new File(file, temp);
	   	      //recursive delete
	   	      delete(fileDelete);
	        }
	   	   //check the directory again, if empty then delete it
	   	   if(file.list().length==0){
	   	  	 file.delete();
	   	   }
	     }
	   }else{
	    		//if file, then delete it
	    		file.delete();
	   }
	}
	
	/**
	 * Recursive method to add file and folder to the jar
	 * 
	 * @param source element to add to the jar
	 * @param target jar where the element is added
	 * @param basePath slice of path not included in the jar, to avoid the directory starting
	 * from the drive letter for example
	 */
	private static void add(File source, JarOutputStream target, String basePath) throws IOException
	{
	  BufferedInputStream in = null;
	  try
	  {
      String name = source.getPath().substring(basePath.length()+1).replace("\\", "/");
	    if (source.isDirectory())
	    {
	      if (!name.isEmpty())
	      {
	        if (!name.endsWith("/"))
	          name += "/";
	        JarEntry entry = new JarEntry(name);
	        entry.setTime(source.lastModified());
	        target.putNextEntry(entry);
	        target.closeEntry();
	      }
	      for (File nestedFile: source.listFiles())
	        add(nestedFile, target, basePath);
	      return;
	    }

	    JarEntry entry = new JarEntry(name);
	    entry.setTime(source.lastModified());
	    target.putNextEntry(entry);
	    in = new BufferedInputStream(new FileInputStream(source));

	    byte[] buffer = new byte[1024];
	    while (true)
	    {
	      int count = in.read(buffer);
	      if (count == -1)
	        break;
	      target.write(buffer, 0, count);
	    }
	    target.closeEntry();
	  }
	  finally
	  {
	    if (in != null)
	      in.close();
	  }
	}
	
	/**
	 * Create a jar with the content of a folder inside, and a manifest
	 * 
	 * @param destination folder where the jar will be placed
	 * @param pluginDir directory from where the content will be read to be placed inside the jar. The folder itself it is not
	 * included in the jar
	 * @param fileName name of the jar file (must have the extension)
	 * @param manifestContent manifest of the jar
	 */
	public static void createJar(String destination, File pluginDir, String fileName, String manifestContent){
		try {
			Manifest manifest = new Manifest(new ByteArrayInputStream(manifestContent.getBytes("UTF-8")));
			JarOutputStream target = new JarOutputStream(new FileOutputStream(destination + File.separatorChar + fileName), manifest);
			for(File children : pluginDir.listFiles()){
				add(children, target, pluginDir.getAbsolutePath());
			}
			target.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
