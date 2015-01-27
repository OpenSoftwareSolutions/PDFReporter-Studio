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
package com.jaspersoft.studio.wizards.dataadapter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

/**
 * Support methods to generate the data adapter plugin project
 * and add files and resources to it
 * 
 * @author Orlandin Marco
 *
 */
public class PluginHelper {

	/**
	 * Create a plugin project in the workspace. Project will contains
	 * the folder libs and images included also into the build file, to 
	 * allow to easily add images and libraries
	 * 
	 * @param adapterInfo the information of the plugin
	 * @param srcFolders the source folders of the project
	 * @param requiredBundles the bundles required by the project
	 * @param requiredLibs the jar libraries required by the project
	 * @param progressMonitor a progress monitor
	 * @return the generated project
	 */
	public static IProject createPluginProject(AdapterInfo adapterInfo, List<String> srcFolders, List<String> requiredBundles, List<File> requiredLibs, IProgressMonitor progressMonitor) {
		IProject project = null;
		try {
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			project = workspace.getRoot().getProject(adapterInfo.getProjectName());

	
			final IJavaProject javaProject = JavaCore.create(project);
			final IProjectDescription projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(adapterInfo.getProjectName());
			projectDescription.setLocation(null);
			project.create(projectDescription, progressMonitor);
			final List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();

			projectDescription.setNatureIds(new String[] { JavaCore.NATURE_ID, "org.eclipse.pde.PluginNature" });

			final ICommand java = projectDescription.newCommand();
			java.setBuilderName(JavaCore.BUILDER_ID);

			final ICommand manifest = projectDescription.newCommand();
			manifest.setBuilderName("org.eclipse.pde.ManifestBuilder");

			final ICommand schema = projectDescription.newCommand();
			schema.setBuilderName("org.eclipse.pde.SchemaBuilder");

			final ICommand oaw = projectDescription.newCommand();

			projectDescription.setBuildSpec(new ICommand[] { java, manifest, schema, oaw });

			project.open(progressMonitor);
			project.setDescription(projectDescription, progressMonitor);

			Collections.reverse(srcFolders);
			for (final String src : srcFolders) {
				final IFolder srcContainer = project.getFolder(src);
				if (!srcContainer.exists()) {
					srcContainer.create(false, true, progressMonitor);
				}
				final IClasspathEntry srcClasspathEntry = JavaCore.newSourceEntry(srcContainer.getFullPath());
				classpathEntries.add(0, srcClasspathEntry);
			}
			//Add the jar libraries to the project
			IFolder libsFolder = project.getFolder("libs");
			libsFolder.create(false, true, progressMonitor);
			List<IFile> externalLibs = new ArrayList<IFile>();
			for (File libFile : requiredLibs){
				if (libFile != null && libFile.exists()){
					InputStream resourceAsStream = new FileInputStream(libFile);
					PluginHelper.createFile(libFile.getName(), libsFolder, resourceAsStream, progressMonitor);
					IFile file = libsFolder.getFile(libFile.getName());
					IPath path = file.getFullPath();
					classpathEntries.add(JavaCore.newLibraryEntry(path, path, null, true));
					externalLibs.add(file);
				}
			}

			classpathEntries.add(JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5")));
			classpathEntries.add(JavaCore.newContainerEntry(new Path("org.eclipse.pde.core.requiredPlugins")));

			javaProject.setRawClasspath(classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]), progressMonitor);

			javaProject.setOutputLocation(new Path("/" + adapterInfo.getProjectName() + "/bin"), progressMonitor);
			
			ProjectUtil.createJRClasspathContainer(progressMonitor, javaProject);
			
			IFolder imagesFolder = project.getFolder("images");
			imagesFolder.create(false, true, progressMonitor);
			createManifest(adapterInfo, requiredBundles, externalLibs, progressMonitor, project);
			createBuildProps(progressMonitor, project, srcFolders);
		}
		catch (final Exception exception) {
			exception.printStackTrace();
			JaspersoftStudioPlugin.getInstance().logError(exception);
		}
		return project;
	}

	/**
	 * Add a textual file to the project
	 * 
	 * @param name the name of the file
	 * @param container the container of the file
	 * @param content the textual content of the file
	 * @param progressMonitor a progress monitor
	 * @return the added file
	 */
	public static IFile createFile(String name, IContainer container, String content, IProgressMonitor progressMonitor) {
		final IFile file = container.getFile(new Path(name));
		assertExist(file.getParent());
		try {
			final InputStream stream = new ByteArrayInputStream(content.getBytes(file.getCharset()));
			if (file.exists()) {
				file.setContents(stream, true, true, progressMonitor);
			}
			else {
				file.create(stream, true, progressMonitor);
			}
			stream.close();
		}
		catch (final Exception e) {
			JaspersoftStudioPlugin.getInstance().logError(e);
		}
		progressMonitor.worked(1);

		return file;
	}
	
	/**
	 * Add a generic file to the project
	 * 
	 * @param name the name of the file
	 * @param container the container of the file
	 * @param inputStream the input stream to the file
	 * @param progressMonitor a progress monitor
	 * @return the added file
	 */
	public static IFile createFile(String path, IContainer container, InputStream inputStream, IProgressMonitor progressMonitor) {
		final IFile file = container.getFile(new Path(path));
		try {
			if (file.exists()) {
				file.setContents(inputStream, true, true, progressMonitor);
			}
			else {
				file.create(inputStream, true, progressMonitor);
			}
			inputStream.close();
		}
		catch (final Exception e) {
			JaspersoftStudioPlugin.getInstance().logError(e);
		}
		finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				}
				catch (final IOException e) {
					JaspersoftStudioPlugin.getInstance().logError(e);
				}
			}
		}
		progressMonitor.worked(1);

		return file;
	}


	/**
	 * Create the build.properties file for the current project
	 * 
	 * @param progressMonitor the progress monitor
	 * @param project the project
	 * @param srcFolders the list of source folders
	 */
	private static void createBuildProps(IProgressMonitor progressMonitor, IProject project, List<String> srcFolders) {
		final StringBuilder bpContent = new StringBuilder("source.. = ");
		for (final Iterator<String> iterator = srcFolders.iterator(); iterator.hasNext();) {
			bpContent.append(iterator.next()).append('/');
			if (iterator.hasNext()) {
				bpContent.append(",");
			}
		}
		bpContent.append("\n");
		bpContent.append("bin.includes = plugin.xml,\\\n");
		bpContent.append("META-INF/,\\\n");
		bpContent.append("libs/,\\\n");
		bpContent.append("images/,\\\n");
		bpContent.append(".");
		createFile("build.properties", project, bpContent.toString(), progressMonitor);
	}

	/**
	 * Create the manifest file for the current project
	 * 
	 * @param adapterInfo the plugin info
	 * @param requiredBundles the bundles required by the project
	 * @param externalLibs the external libs that are in the classpath
	 * @param progressMonitor a progress monitor
	 * @param project the project
	 */
	private static void createManifest(AdapterInfo adapterInfo, List<String> requiredBundles, List<IFile> externalLibs, IProgressMonitor progressMonitor, IProject project) throws CoreException {
		final StringBuilder maniContent = new StringBuilder("Manifest-Version: 1.0\n");
		maniContent.append("Bundle-ManifestVersion: 2\n");
		maniContent.append("Bundle-Name: " + adapterInfo.getProjectName() + "\n");
		maniContent.append("Bundle-SymbolicName: " + adapterInfo.getPluginId() + "; singleton:=true\n");
		maniContent.append("Bundle-Version: 1.0.0.qualifier\n");
		maniContent.append("Bundle-Activator: "+ adapterInfo.getPackageName() +".Activator\n");
		if (requiredBundles != null && !requiredBundles.isEmpty()){
			maniContent.append("Require-Bundle: "+requiredBundles.get(0));
			for(int i= 1; i<requiredBundles.size(); i++){
				maniContent.append(",\n " + requiredBundles.get(i));
			}
			maniContent.append("\n");
		}
		maniContent.append("Bundle-ActivationPolicy: lazy\n");
		maniContent.append("Eclipse-BuddyPolicy: registered\n");
		maniContent.append("Eclipse-RegisterBuddy: com.jaspersoft.studio.data\n");		
		maniContent.append("Bundle-RequiredExecutionEnvironment: JavaSE-1.6\r\n");
		if (!externalLibs.isEmpty()){
			maniContent.append("Bundle-ClassPath: ");
			for(IFile lib : externalLibs){
				String path = "libs/"+ lib.getName();
				maniContent.append(path + ",\n");
			}
			maniContent.append(" .\n");
		}
		
		final IFolder metaInf = project.getFolder("META-INF");
		metaInf.create(false, true, progressMonitor);
		createFile("MANIFEST.MF", metaInf, maniContent.toString(), progressMonitor);
	}

	/**
	 * Check if a container exist, it it dosen't exist the it is created. Check
	 * also the ancestors and create them as well
	 * 
	 * @param c the container to check
	 */
	private static void assertExist(final IContainer c) {
		if (!c.exists()) {
			if (!c.getParent().exists()) {
				assertExist(c.getParent());
			}
			if (c instanceof IFolder) {
				try {
					((IFolder) c).create(false, true, new NullProgressMonitor());
				}
				catch (final CoreException e) {
					JaspersoftStudioPlugin.getInstance().logError(e);
				}
			}

		}

	}
}
