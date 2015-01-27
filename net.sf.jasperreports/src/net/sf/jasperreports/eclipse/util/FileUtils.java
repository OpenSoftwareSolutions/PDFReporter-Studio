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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Properties;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.builder.JasperReportsNature;
import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;

public class FileUtils {

	public static void closeStream(Closeable stream) {
		if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static String findRelativePath(String base, String path) throws IOException {
		if (base == null)
			throw new IOException("NullOriginalPath"); //$NON-NLS-1$

		if (path == null)
			throw new IOException("NullRelativePath"); //$NON-NLS-1$

		//
		// remove ./ if present
		//
		if (path.startsWith("./")) //$NON-NLS-1$
			path = path.substring(2);

		//
		// remove any .. reference by taking off the last section/ of
		// the original path
		//
		if (path.startsWith("../")) { //$NON-NLS-1$
			int slash = base.lastIndexOf('/');
			base = base.substring(0, slash);
			path = path.substring(3);
		}

		int slash = base.lastIndexOf('/');

		if (slash < 0)
			return path;

		String dir = base.substring(0, slash + 1);
		return dir + path;
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	public static File createTempFile(String prefix, String sufix) throws IOException {
		File f = File.createTempFile(prefix, sufix);
		f.deleteOnExit();
		return f;
	}

	public static File createTempDir() throws IOException {
		return createTempDir("");
	}

	public static File createTempDir(String prefix) throws IOException {
		final File sysTempDir = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
		File newTempDir;
		final int maxAttempts = 9;
		int attemptCount = 0;
		do {
			attemptCount++;
			if (attemptCount > maxAttempts)
				throw new IOException(NLS.bind(Messages.FileUtils_ImpossibleToCreateTempDirectory, maxAttempts));
			String dirName = prefix + System.currentTimeMillis();// gUUID.randomUUID().toString();
			newTempDir = new File(sysTempDir, dirName);
		} while (newTempDir.exists());

		if (newTempDir.mkdirs()) {
			newTempDir.deleteOnExit();
			newTempDir.setWritable(true, false);
			newTempDir.setReadable(true, false);
			return newTempDir;
		} else
			throw new IOException(NLS.bind(Messages.FileUtils_UnableToCreateDirectory, newTempDir.getAbsolutePath()));
	}

	/**
	 * Recursively delete file or directory
	 * 
	 * @param fileOrDir
	 *          the file or dir to delete
	 * @return true iff all files are successfully deleted
	 */
	public static boolean recursiveDelete(File fileOrDir) {
		if (fileOrDir.isDirectory()) {
			// recursively delete contents
			for (File innerFile : fileOrDir.listFiles()) {
				if (!recursiveDelete(innerFile)) {
					return false;
				}
			}
		}

		return fileOrDir.delete();
	}

	public static byte[] getBytes(File file) throws IOException {
		return IOUtils.toByteArray(new FileInputStream(file));
		//
		// byte[] b = new byte[(int) file.length()];
		// FileInputStream in = null;
		// try {
		// in = new FileInputStream(file);
		// in.read(b);
		// } finally {
		// closeStream(in);
		// }
		// return b;
	}

	public static byte[] getBytes(IFile file) throws IOException, CoreException {
		// InputStream in = file.getContents();
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// byte[] buffer = new byte[1024];
		// while (in.read(buffer) != -1)
		// bos.write(buffer);
		// bos.flush();
		//
		// return bos.toByteArray();

		return IOUtils.toByteArray(file.getContents());

		// byte[] b = new byte[(int)
		// IDEResourceInfoUtils.getFileInfo(file.getFullPath()).getLength()];
		// InputStream in = null;
		// try {
		// in = file.getContents(true);
		// in.read(b);
		// } finally {
		// closeStream(in);
		// }
		// return b;
	}

	public static String readFileAsAString(File file) throws IOException {
		return new String(getBytesFromFile(file));
	}

	/**
	 * Returns the contents of the file in a byte array.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
			is.close();
			throw new IllegalArgumentException(Messages.FileUtils_FileTooLargeError);
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			is.close();
			throw new IOException(NLS.bind(Messages.FileUtils_UnableToReadFile, file.getName()));
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	public static File fileRenamed(File file, String strFilename, String ext) throws CoreException {
		return fileRenamed(file, strFilename, ext, true);
	}

	public static File fileRenamed(File file, String strFilename, String ext, boolean showWarning) throws CoreException {
		String fname = strFilename + ext;
		if (fname.equals(file.getAbsolutePath()))
			return file;
		deleteFileIfExists(null, fname);

		file.renameTo(new File(fname));
		if (showWarning)
			UIUtils.showWarning(NLS.bind(Messages.FileUtils_DifferentFileTypeWarning, fname));
		return new File(fname);
	}

	public static IFile fileRenamed(IFile file, String strFilename, String ext, boolean showWarning, IProgressMonitor monitor) throws CoreException {
		String fname = strFilename + ext;
		if (fname.equals(file.getFullPath().toOSString()))
			return file;
		deleteFileIfExists(monitor, fname);
		try {
			file.move(new Path(fname), true, monitor);
		} catch (Exception ex) {
			// For some reasons on windows the move is able to create the file
			// with the new name, but it is unable to remove the original one
			// (the move works like a copy with new name an remove the original,
			// instead a real rename)
			// It seems a permission issues, even if it regards only the delete
			// procedure, or more probably a "file in use" lock
			// anyway as hotfix we catch the exception and log it
			file.getLocation().toFile().delete();// <-Another try to remove the
																						// original file, probably will fail
																						// too
			JasperReportsPlugin.getDefault().logWarning("Impossibile to delete the file without extension from the temp folder", ex);
		}
		if (showWarning)
			UIUtils.showWarning(NLS.bind(Messages.FileUtils_DifferentFileTypeWarning, fname));
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource r = root.findMember(fname);
		if (r != null && r.exists() && r instanceof IFile)
			return (IFile) r;
		return file;
	}

	protected static void deleteFileIfExists(IProgressMonitor monitor, String fname) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource r = root.findMember(fname);
		if (r != null && r.exists())
			r.delete(true, monitor);
	}

	public static String readInputStreamAsString(InputStream in) throws IOException {

		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1) {
			byte b = (byte) result;
			buf.write(b);
			result = bis.read();
		}
		return buf.toString();
	}

	public static void writeFile(File f, String content) throws IOException {
		org.apache.commons.io.FileUtils.write(f, content, "UTF-8");

		// FileWriter fw = null;
		// try {
		// // fw = new FileWriter(f);
		// FileOutputStream fileStream = new FileOutputStream(f);
		// OutputStreamWriter fw = new OutputStreamWriter(fileStream, "UTF-8");
		// fw.write(content);
		// } finally {
		// if (fw != null)
		// fw.close();
		// }
	}

	public static String getPropertyAsString(Properties prop) {
		StringBuffer str = new StringBuffer();
		for (String key : prop.stringPropertyNames()) {
			str.append(key).append("=").append(stringConvert(prop.getProperty(key), false, true)) //$NON-NLS-1$
					.append("\n"); //$NON-NLS-1$
		}
		return str.toString();
	}

	public static Properties load(String propertiesString) throws IOException {
		Properties properties = new Properties();
		if (propertiesString != null)
			properties.load(new StringReader(propertiesString));
		return properties;
	}

	public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len = in.read(buffer);
		while (len >= 0) {
			out.write(buffer, 0, len);
			len = in.read(buffer);
		}
		closeStream(in);
		closeStream(out);
	}

	public static IFile getInProjectFile(URI uri, IProgressMonitor monitor) throws CoreException {
		IPath location = new Path(uri.getPath());
		IProject project = getProject(monitor);
		IFile file = project.getFile(location.lastSegment());
		file.createLink(location, IResource.REPLACE, null);

		return file;
	}

	public static IFolder getInProjectFolder(URI uri, IProgressMonitor monitor) throws CoreException {
		IPath location = new Path(uri.getPath());
		IProject project = getProject(monitor);
		IFolder file = project.getFolder(location.lastSegment());
		file.createLink(location, IResource.REPLACE, null);

		return file;
	}

	public static void prepareFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent != null && parent instanceof IFolder)
			prepareFolder((IFolder) parent, monitor);
		if (!folder.exists())
			folder.create(true, true, monitor);
	}

	protected static IProject getProject(IProgressMonitor monitor) throws CoreException, JavaModelException {
		IProject project = null;
		for (IProject prj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (prj.isOpen()) {
				if (project == null)
					project = prj;
				else if (prj.getNature(JasperReportsNature.NATURE_ID) != null)
					project = prj;
				if (project != null && project.getName().equals(DEFAULT_PROJECT))
					break;
			}
		}
		if (project == null)
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(DEFAULT_PROJECT);
		// Create a project if one doesn't exist and open it.
		if (!project.exists()) {
			project.create(monitor);
			project.open(monitor);
			ProjectUtil.createJRProject(monitor, project);
		}
		if (!project.isOpen())
			project.open(monitor);
		return project;
	}

	public static final String DEFAULT_PROJECT = "MyReports"; //$NON-NLS-1$

	public static IEditorInput checkAndConvertEditorInput(IEditorInput editorInput, IProgressMonitor monitor) throws PartInitException {
		if (editorInput instanceof FileStoreEditorInput) {
			try {
				FileStoreEditorInput fsei = (FileStoreEditorInput) editorInput;

				IFile file = getInProjectFile(fsei.getURI(), monitor);

				editorInput = new FileEditorInput(file);
			} catch (CoreException e) {
				throw new PartInitException(e.getMessage(), e);
			}
		}
		return editorInput;
	}

	/**
	 * Converts unicodes to encoded &#92;uxxxx and escapes special characters with
	 * a preceding slash
	 */
	public static String stringConvert(String theString, boolean escapeSpace, boolean escapeUnicode) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	/**
	 * Convert a nibble to a hex character
	 * 
	 * @param nibble
	 *          the nibble to convert.
	 */
	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/** A table of hex digits */
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static File findFile(IFile file, String str) {
		if (str == null || str.isEmpty())
			return null;
		IContainer parent = file.getParent();
		SimpleFileResolver fr = new SimpleFileResolver(Arrays.asList(new File[] { new File(parent.getLocationURI()), file.getRawLocation().toFile().getParentFile(),
				new File(file.getProject().getLocationURI()) }));
		fr.setResolveAbsolutePath(true);
		return fr.resolveFile(str);
	}

	public static final String KEY_FILE = "ifile";
}
