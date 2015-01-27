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
package net.sf.jasperreports.samples.wizards;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.wizard.project.JRProjectPage;
import net.sf.jasperreports.eclipse.wizard.project.JRProjectWizard;
import net.sf.jasperreports.samples.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class SampleNewWizard extends JRProjectWizard {

	/**
	 * Constructor for SampleNewWizard.
	 */
	public SampleNewWizard() {
		super();
		setWindowTitle("JasperReports Samples");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		init(workbench, selection, new JRProjectPage());
	}

	public void init(IWorkbench workbench, IStructuredSelection selection, JRProjectPage page) {
		step1 = page;
		step1.setTitle("JasperReports Samples");
		step1.setDescription("Create a new project with JasperReports Samples");
		step1.setName("JasperReportsSamples");
		addPage(step1);
	}

	@Override
	protected void createProject(IProgressMonitor monitor, IProject prj) throws CoreException, JavaModelException {
		Set<String> cpaths = new HashSet<String>();
		Set<String> lpaths = new HashSet<String>();
		super.createProject(monitor, prj);
		File copyto = prj.getLocation().toFile();
		Set<URL> paths = Activator.getSamplesManager().getURLs();
		Enumeration<?> en = Activator.getDefault().getBundle().findEntries("resources", "*", true); //$NON-NLS-1$ //$NON-NLS-2$
		while (en.hasMoreElements())
			paths.add((URL) en.nextElement());
		en = JasperReportsPlugin.getDefault().getBundle().findEntries("lib", "*.jar", true); //$NON-NLS-1$ //$NON-NLS-2$
		while (en.hasMoreElements())
			paths.add((URL) en.nextElement());
		for (URL url : paths) {
			OutputStream out = null;
			InputStream in = null;
			if (url.getFile().endsWith(".zip")) {
				File zip = null;
				try {
					in = new BufferedInputStream(url.openStream(), 1024);
					zip = File.createTempFile("arc", ".zip", copyto);
					out = new BufferedOutputStream(new FileOutputStream(zip));
					FileUtils.copyInputStream(in, out);
					unpackArchive(zip, copyto, monitor, cpaths, lpaths);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					FileUtils.closeStream(in);
					FileUtils.closeStream(out);
					if (zip != null)
						zip.delete();
				}
			} else {
				String path = url.getPath();
				File file = new File(copyto, File.separator + path);
				new File(file.getParent()).mkdirs();
				String fname = file.getName();
				try {
					org.apache.commons.io.FileUtils.copyURLToFile(url, file);
					if (file.getParentFile().getName().equals("src"))
						cpaths.add(path.substring(0, path.lastIndexOf("/")));
					if (file.getParentFile().getParentFile().getName().equals("lib") && (fname.endsWith(".jar") || fname.endsWith(".zip")))
						lpaths.add(path);
					if (file.getParentFile().getName().equals("lib") && (fname.endsWith(".jar") || fname.endsWith(".zip")))
						lpaths.add(path);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			if (monitor.isCanceled())
				return;
		}
		IJavaProject project = JavaCore.create(prj);
		addSourceFolders(cpaths, project, monitor);
		addLibraries(lpaths, project, monitor);
		prj.refreshLocal(IProject.DEPTH_INFINITE, monitor);
		prj.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		prj.close(monitor);
		prj.open(monitor);
	}

	public static void unpackArchive(File theFile, File targetDir, IProgressMonitor monitor, Set<String> cpaths, Set<String> lpaths) {
		byte[] buffer = new byte[1024];
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(new FileInputStream(theFile));

			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				File file = new File(targetDir, File.separator + ze.getName());

				new File(file.getParent()).mkdirs();
				String fname = file.getName();
				if (ze.isDirectory()) {
					if (fname.equals("src"))
						cpaths.add(ze.getName());
				} else {
					FileOutputStream fos = new FileOutputStream(file);
					int len;
					while ((len = zis.read(buffer)) > 0)
						fos.write(buffer, 0, len);

					fos.close();
				}
				if (file.getParentFile().getName().equals("lib") && (fname.endsWith(".jar") || fname.endsWith(".zip")))
					lpaths.add(ze.getName());
				if (monitor.isCanceled()) {
					zis.close();
					return;
				}
				ze = zis.getNextEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				zis.closeEntry();
				zis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void addLibraries(Set<String> lpaths, IJavaProject project, IProgressMonitor monitor) {
		if (lpaths.isEmpty())
			return;
		try {
			IClasspathEntry[] cpentries = project.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[cpentries.length + lpaths.size()];
			System.arraycopy(cpentries, 0, newEntries, 0, cpentries.length);
			int i = cpentries.length;
			for (String cp : lpaths) {
				newEntries[i] = JavaCore.newLibraryEntry(project.getProject().getFile(cp).getFullPath(), null, null);
				i++;
				if (monitor.isCanceled())
					return;
			}
			project.setRawClasspath(newEntries, monitor);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

	}

	private static void addSourceFolders(Set<String> cpaths, IJavaProject project, IProgressMonitor monitor) {
		if (cpaths.isEmpty())
			return;
		try {
			IClasspathEntry[] cpentries = project.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[cpentries.length + cpaths.size()];
			System.arraycopy(cpentries, 0, newEntries, 0, cpentries.length);
			int i = cpentries.length;
			for (String cp : cpaths) {
				newEntries[i] = JavaCore.newSourceEntry(project.getProject().getFile(cp).getFullPath());
				i++;
				if (monitor.isCanceled())
					return;
			}
			project.setRawClasspath(newEntries, monitor);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

	}

}
