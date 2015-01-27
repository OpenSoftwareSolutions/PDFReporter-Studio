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
package net.sf.jasperreports.eclipse.classpath;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.eclipse.classpath.container.JRClasspathContainer;
import net.sf.jasperreports.eclipse.util.FileExtension;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

public class JavaProjectClassLoader extends ClassLoader {
	private static Map<IJavaProject, JavaProjectClassLoader> map = new HashMap<IJavaProject, JavaProjectClassLoader>();

	public static ClassLoader instance(IJavaProject project) {
		return instance(project, null);
	}

	public static JavaProjectClassLoader instance(IJavaProject project, ClassLoader classLoader) {
		JavaProjectClassLoader cl = map.get(project);
		if (cl == null) {
			cl = new JavaProjectClassLoader(project, classLoader);
			map.put(project, cl);
		}

		return cl;
	}

	private IJavaProject javaProject;
	private IElementChangedListener listener;

	private JavaProjectClassLoader(IJavaProject project) {
		super();
		init(project);
	}

	private JavaProjectClassLoader(IJavaProject project, ClassLoader classLoader) {
		super(classLoader);
		init(project);
	}

	protected void init(IJavaProject project) {
		if (project == null || !project.exists() || !project.getResource().isAccessible())
			throw new IllegalArgumentException("Invalid javaProject");
		this.javaProject = project;
		getURLClassloader();
		listener = new IElementChangedListener() {

			public void elementChanged(final ElementChangedEvent event) {
				if (ignoreClasspathChanges(event))
					return;
				System.out.println("CLASSPATH CHANGED:" + event);
				// FIXME should release this classloader
				// what happend with current objects? we have 1 loader per
				// project, maybe se can filter some events? to have less
				// updates
				curlLoader = null;
				getURLClassloader();
				if (events != null)
					events.firePropertyChange("classpath", false, true);
			}
		};
		JavaCore.addElementChangedListener(listener, ElementChangedEvent.POST_CHANGE);
	}

	private boolean ignoreClasspathChanges(ElementChangedEvent event) {
		if (event.getDelta() == null && event.getDelta().getChangedChildren() == null)
			return true;
		for (IJavaElementDelta delta : event.getDelta().getChangedChildren()) {
			if (delta.getResourceDeltas() == null)
				return false;
			for (IResourceDelta rd : delta.getResourceDeltas()) {
				if (rd.getFullPath() == null)
					continue;
				String path = rd.getFullPath().getFileExtension();
				if (path != null && !(path.equalsIgnoreCase(FileExtension.JRXML) || path.equalsIgnoreCase(FileExtension.JASPER)))
					return false;
			}
		}
		return true;
	}

	private PropertyChangeSupport events;

	public void addClasspathListener(PropertyChangeListener l) {
		if (events == null)
			events = new PropertyChangeSupport(this);
		events.addPropertyChangeListener(l);
	}

	public void removeClasspathListener(PropertyChangeListener l) {
		if (events == null)
			events = new PropertyChangeSupport(this);
		events.removePropertyChangeListener(l);
	}

	@Override
	protected URL findResource(String name) {
		if (name.endsWith(".groovy"))
			return null;
		// System.out.println(name);
		if (curlLoader != null)
			return curlLoader.getResource(name);
		return null;
		// URL url = null;
		// try {
		// url = getURLClassloader().getResource(name);
		// if (url == null) {
		// IPath path = new Path(name);
		// String resourceName = path.lastSegment();
		// path =
		// path.removeFileExtension().removeLastSegments(1).makeRelative();
		//
		// IJavaElement element = javaProject.findElement(path);
		// if (element != null) {
		// if (element instanceof IPackageFragment) {
		// Object[] children = ((IPackageFragment)
		// element).getNonJavaResources();
		// if (children != null) {
		// for (Object child : children) {
		// if (child instanceof IResource) {
		// IResource res = (IResource) child;
		// if (resourceName.equals(res.getName()))
		// return res.getLocationURI().toURL();
		// } else if (child instanceof JarEntryFile) {
		// JarEntryFile jef = (JarEntryFile) child;
		// if (resourceName.equals(jef.getName()))
		// return new URL("jar:file:" + element.getPath() + "!" +
		// jef.getFullPath());
		// }
		// }
		// }
		// } else {
		// IResource resource = javaProject.getResource();
		// if (resource != null) {
		// File file =
		// resource.getLocation().append(element.getPath().makeRelativeTo(resource.getFullPath())).append(resourceName).toFile();
		// if (file.exists())
		// return file.toURI().toURL();
		// }
		// }
		// }
		// }
		// if (url != null)
		// return url;
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (JavaModelException e) {
		// e.printStackTrace();
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }
		// return url;
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		if (curlLoader != null)
			return curlLoader.getResources(name);
		// try {
		// return getURLClassloader().getResources(name);
		// if (url == null) {
		// Set<URL> urls = new HashSet<URL>();
		// IPath path = new Path(name);
		// String resourceName = path.lastSegment();
		// path =
		// path.removeFileExtension().removeLastSegments(1).makeRelative();
		//
		// IJavaElement element = javaProject.findElement(path);
		// if (element != null) {
		// IResource resource = javaProject.getResource();
		// File file =
		// resource.getLocation().append(element.getPath().makeRelativeTo(resource.getFullPath())).append(resourceName).toFile();
		// if (file.exists())
		// urls.add(file.toURI().toURL());
		//
		// Object[] children = ((IPackageFragment)
		// element).getNonJavaResources();
		// if (children != null) {
		// for (Object child : children) {
		// if (child instanceof IResource) {
		// IResource res = (IResource) child;
		// if (resourceName.equals(res.getName())) {
		// urls.add(res.getLocationURI().toURL());
		// }
		// } else if (child instanceof JarEntryFile) {
		// JarEntryFile jef = (JarEntryFile) child;
		// if (resourceName.equals(jef.getName())) {
		// String jarpath = "jar:file:" + element.getPath() + "!" +
		// jef.getFullPath();
		// urls.add(new URL(jarpath));
		// }
		// }
		// }
		// }
		// }
		// final Iterator<URL> i = urls.iterator();
		// return new Enumeration<URL>() {
		//
		// public boolean hasMoreElements() {
		// return i.hasNext();
		// }
		//
		// public URL nextElement() {
		// return i.next();
		// }
		// };
		// }
		// if (url != null)
		// return url;
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (JavaModelException e) {
		// e.printStackTrace();
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }
		return null;
	}

	private static final String PROTOCOL_PREFIX = "file:///";

	@Override
	protected Class findClass(String className) throws ClassNotFoundException {
		if (ClassLoaderUtil.packages.contains(className))
			throw new ClassNotFoundException(className);
		if (className.endsWith("GroovyEvaluator"))
			throw new ClassNotFoundException(className);
		// System.out.println(className);
		if (curlLoader != null)
			return curlLoader.loadClass(className);
		throw new ClassNotFoundException(className);
	}

	private static URL computeForURLClassLoader(String classpath) throws MalformedURLException {
		if (!classpath.endsWith("/")) {
			File file = new File(classpath);
			if (file.exists() && file.isDirectory())
				classpath = classpath.concat("/");
		}
		return new URL(PROTOCOL_PREFIX + classpath);
	}

	private static final String FILE_SCHEME = "file";
	private static boolean calcURLS = false;

	private ClassLoader getURLClassloader() {
		if (curlLoader == null) {
			try {
				if (calcURLS)
					return getParent();
				calcURLS = true;
				JRClasspathContainer jrcnt = (JRClasspathContainer) JavaCore.getClasspathContainer(JRClasspathContainer.ID, javaProject);
				List<String> jrcntpaths = null;
				if (jrcnt != null) {
					IClasspathEntry[] ces = jrcnt.getAllClasspathEntries();
					if (ces != null && ces.length > 0) {
						jrcntpaths = new ArrayList<String>();
						for (IClasspathEntry en : ces)
							jrcntpaths.add(en.getPath().toOSString());
					}
				}
				String[] classPaths = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
				Set<URL> urls = new HashSet<URL>();
				for (int i = 0; i < classPaths.length; i++)
					try {
						if (jrcntpaths != null && jrcntpaths.contains(classPaths[i]))
							continue;
						urls.add(computeForURLClassLoader(classPaths[i]));
					} catch (MalformedURLException e) {
					}

				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
				resolveClasspathEntries(urls, root, entries);

				getURLClassloader(urls.toArray(new URL[urls.size()]));
				calcURLS = false;
			} catch (JavaModelException e1) {
				e1.printStackTrace();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
		return curlLoader;
	}

	private void resolveClasspathEntries(Set<URL> urls, IWorkspaceRoot root, IClasspathEntry[] entries) throws JavaModelException {
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				IPath path = entry.getPath();
				if (path.segmentCount() >= 2) {
					IFolder sourceFolder = root.getFolder(path);
					try {
						urls.add(new URL("file:///" + sourceFolder.getRawLocation().toOSString() + "/"));
					} catch (MalformedURLException e) {
					}
				}
			} else if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				IPath sourcePath = entry.getPath();
				covertPathToUrl(javaProject.getProject(), urls, sourcePath);
				IPath sourceOutputPath = entry.getOutputLocation();
				covertPathToUrl(javaProject.getProject(), urls, sourceOutputPath);
			} else if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				if (entry.getPath().equals(JRClasspathContainer.ID))
					continue;
				IClasspathContainer cont = JavaCore.getClasspathContainer(entry.getPath(), javaProject);
				resolveClasspathEntries(urls, root, cont.getClasspathEntries());
			}
		}
	}

	private static void addUri(Set<URL> paths, URI uri) {
		try {
			File file = new File(uri);
			if (file.isDirectory())
				paths.add(new URL(uri.toString() + File.separator));
			else
				paths.add(uri.toURL());
		} catch (MalformedURLException e) {
			// ignore error
		}
	}

	private static void covertPathToUrl(IProject project, Set<URL> paths, IPath path) {
		if (path != null && project != null && path.removeFirstSegments(1) != null && project.findMember(path.removeFirstSegments(1)) != null) {

			URI uri = project.findMember(path.removeFirstSegments(1)).getRawLocationURI();

			if (uri != null) {
				String scheme = uri.getScheme();
				if (FILE_SCHEME.equalsIgnoreCase(scheme))
					addUri(paths, uri);
				else if ("sourcecontrol".equals(scheme)) {
					// special case of Rational Team Concert
					IPath sourceControlPath = project.findMember(path.removeFirstSegments(1)).getLocation();
					File sourceControlFile = sourceControlPath.toFile();
					if (sourceControlFile.exists())
						addUri(paths, sourceControlFile.toURI());
				} else {
					IPathVariableManager variableManager = ResourcesPlugin.getWorkspace().getPathVariableManager();
					addUri(paths, variableManager.resolveURI(uri));
				}
			}
		}
	}

	private URLClassLoader curlLoader;

	private synchronized ClassLoader getURLClassloader(URL[] urls) {
		if (curlLoader == null)
			curlLoader = URLClassLoader.newInstance(urls, getParent());
		return curlLoader;
	}

}
