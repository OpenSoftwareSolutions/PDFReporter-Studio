package com.jaspersoft.studio.server.container;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.eclipse.classpath.container.IClasspathContainerFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class JRSClasspathContainerFactory implements IClasspathContainerFactory {

	@Override
	public void createJRClasspathContainer(IProgressMonitor monitor, List<IClasspathEntry> centries, IJavaProject javaProject) throws JavaModelException {
		JRSClasspathContainer classpathContainer = new JRSClasspathContainer(null, javaProject);
		JavaCore.setClasspathContainer(JRSClasspathContainer.ID, new IJavaProject[] { javaProject }, new IClasspathContainer[] { classpathContainer }, monitor);
		centries.add(JavaCore.newContainerEntry(JRSClasspathContainer.ID, true));
		javaProject.setRawClasspath(centries.toArray(new IClasspathEntry[centries.size()]), monitor);
	}

	@Override
	public Set<Path> isRemovable() {
		Set<Path> set = new HashSet<Path>();
		set.add(JRSClasspathContainer.ID);
		return set;
	}

}
