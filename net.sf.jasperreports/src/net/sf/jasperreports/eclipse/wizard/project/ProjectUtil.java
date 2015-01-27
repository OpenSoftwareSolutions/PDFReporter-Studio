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
package net.sf.jasperreports.eclipse.wizard.project;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.builder.JasperReportsNature;
import net.sf.jasperreports.eclipse.classpath.container.JRClasspathContainer;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.PreferenceConstants;

public class ProjectUtil {
	public static void createJRProject(IProgressMonitor monitor, IProject prj) throws CoreException, JavaModelException {
		addNature(prj, JavaCore.NATURE_ID, monitor);

		List<IClasspathEntry> centries = new ArrayList<IClasspathEntry>();
		IJavaProject javaProject = JavaCore.create(prj);
		IClasspathEntry[] jreClasspath = PreferenceConstants.getDefaultJRELibrary();

		IFolder folder = prj.getFolder(PreferenceConstants.getPreferenceStore().getString(PreferenceConstants.SRCBIN_BINNAME));
		folder.create(IResource.FORCE | IResource.DERIVED, true, monitor);
		folder.setDerived(true, monitor);

		prj.refreshLocal(IResource.DEPTH_INFINITE, monitor);

		centries.addAll(Arrays.asList(jreClasspath));

		addNature(prj, JasperReportsNature.NATURE_ID, monitor);

		// Path to all libraries needed
		createJRClasspathContainer(monitor, centries, javaProject);
	}

	public static void createJRClasspathContainer(IProgressMonitor monitor, IJavaProject javaProject) throws JavaModelException {
		List<IClasspathEntry> centries = new ArrayList<IClasspathEntry>();
		IClasspathEntry[] entries = javaProject.readRawClasspath();
		for (IClasspathEntry en : entries) {
			if (en.getPath().equals(JRClasspathContainer.ID))
				return;
		}
		centries.addAll(Arrays.asList(entries));

		createJRClasspathContainer(null, centries, javaProject);
	}

	public static void createJRClasspathContainer(IProgressMonitor monitor, List<IClasspathEntry> centries, IJavaProject javaProject) throws JavaModelException {
		JRClasspathContainer classpathContainer = new JRClasspathContainer(null, javaProject);
		JavaCore.setClasspathContainer(JRClasspathContainer.ID, new IJavaProject[] { javaProject }, new IClasspathContainer[] { classpathContainer }, monitor);
		centries.add(JavaCore.newContainerEntry(JRClasspathContainer.ID, true));
		javaProject.setRawClasspath(centries.toArray(new IClasspathEntry[centries.size()]), monitor);

		JasperReportsPlugin.getClasspathContainerManager().createJRClasspathContainer(monitor, centries, javaProject);
	}

	public static void addNature(IProject project, String nature, IProgressMonitor monitor) throws CoreException {
		if (!project.isOpen())
			project.open(monitor);
		if (!project.hasNature(nature)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = nature;
			description.setNatureIds(newNatures);

			project.setDescription(description, monitor);
		}
	}

	public static boolean isOpen(IProject prj) {
		return prj.exists() && prj.isOpen() && prj.isAccessible();
	}

	public static void addFileToClasspath(IProgressMonitor monitor, IFile file) throws CoreException {
		if (file.getProject().getNature(JavaCore.NATURE_ID) != null) {
			IJavaProject jprj = JavaCore.create(file.getProject());
			List<IClasspathEntry> centries = new ArrayList<IClasspathEntry>();
			IClasspathEntry[] entries = jprj.readRawClasspath();
			for (IClasspathEntry en : entries) {
				if (en.getPath().equals(file.getFullPath()))
					return;
			}
			centries.add(JavaCore.newLibraryEntry(file.getFullPath(), null, new Path("/")));
			centries.addAll(Arrays.asList(entries));
			jprj.setRawClasspath(centries.toArray(new IClasspathEntry[centries.size()]), monitor);
		}
	}

	/**
	 * Checks if a possible project could contain existing resources.
	 * 
	 * @param prjName
	 *          the project name
	 * @return
	 */
	public static boolean hasExistingContent(String prjName) {
		try {
			URI rootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocationURI();
			URI prjLocation = new URI(rootLocation.getScheme(), null, Path.fromPortableString(rootLocation.getPath()).append(prjName).toString(), null);
			IFileStore file = EFS.getStore(prjLocation);
			return file.fetchInfo().exists();
		} catch (URISyntaxException e) {
			JasperReportsPlugin.getDefault().logError(e);
		} catch (CoreException e) {
			JasperReportsPlugin.getDefault().logError(e);
		}
		return false;
	}

	/**
	 * Deletes the content of a possible project. Also the project folder is
	 * deleted.
	 * 
	 * @param prjName
	 *          the project name
	 */
	public static void deleteProjectFolder(String prjName) {
		try {
			URI rootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocationURI();
			URI prjLocation = new URI(rootLocation.getScheme(), null, Path.fromPortableString(rootLocation.getPath()).append(prjName).toString(), null);
			File prjDir = new File(prjLocation);
			if (prjDir.exists()) {
				FileUtils.deleteDirectory(prjDir);
			}
		} catch (URISyntaxException e) {
			JasperReportsPlugin.getDefault().logError(e);
		} catch (IOException e) {
			JasperReportsPlugin.getDefault().logError(e);
		}

	}

}
