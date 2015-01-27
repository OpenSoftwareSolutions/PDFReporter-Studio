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
package com.jaspersoft.studio.server.dnd;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MJar;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MRDataAdapter;
import com.jaspersoft.studio.server.model.MRFont;
import com.jaspersoft.studio.server.model.MRImage;
import com.jaspersoft.studio.server.model.MRStyleTemplate;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.MResourceBundle;
import com.jaspersoft.studio.server.model.MXmlFile;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.utils.ImageUtils;
import com.jaspersoft.studio.utils.Misc;

/**
 * Helper class to manage DND operations related to the Server Repository.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public final class RepositoryDNDHelper {

	private static final String JRML_EXTENSION = "jrxml"; //$NON-NLS-1$
	private static final String XML_EXTENSION = "xml"; //$NON-NLS-1$
	private static final String DATA_ADAPTER_EXTENSION = "xml"; //$NON-NLS-1$
	private static final String RESOURCE_BUNDLE_EXTENSION = "properties"; //$NON-NLS-1$
	private static final String JAR_EXTENSION = "jar"; //$NON-NLS-1$
	private static final String FONT_EXTENSION = "ttf"; //$NON-NLS-1$
	private static final String TEMPLATE_EXTENSION = "jrtx"; //$NON-NLS-1$
	private static final List<String> ALLOWED_EXTENSIONS;

	static {
		ALLOWED_EXTENSIONS = new ArrayList<String>();
		ALLOWED_EXTENSIONS.add(JRML_EXTENSION);
		ALLOWED_EXTENSIONS.add(XML_EXTENSION);
		ALLOWED_EXTENSIONS.add(DATA_ADAPTER_EXTENSION);
		ALLOWED_EXTENSIONS.add(RESOURCE_BUNDLE_EXTENSION);
		ALLOWED_EXTENSIONS.add(JAR_EXTENSION);
		ALLOWED_EXTENSIONS.add(FONT_EXTENSION);
		ALLOWED_EXTENSIONS.add(TEMPLATE_EXTENSION);
		ALLOWED_EXTENSIONS.addAll(ImageUtils.getAllowedImageFileExtensions());
	}

	private RepositoryDNDHelper() {
		// Prevent instantiation
	}

	/**
	 * Checks if the specified extension is allowed for the DROP operation on the
	 * JRS tree
	 * 
	 * @param extension
	 *          file extension
	 * @return <code>true</code> if the element can be dropped, <code>false</code>
	 *         otherwise
	 */
	public static boolean isDropOperationAllowed(String extension) {
		Assert.isNotNull(extension);
		return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
	}

	public static void performDropOperation(final MResource targetParentResource, final String fullFilename) {
		final File file = new File(fullFilename);
		final String suggestedId = FilenameUtils.removeExtension(file.getName());
		final String suggestedName = FilenameUtils.removeExtension(file.getName());
		final String fileExt = Misc.nvl(FilenameUtils.getExtension(fullFilename)).toLowerCase();

		try {
			ProgressMonitorDialog pm = new ProgressMonitorDialog(UIUtils.getShell());
			pm.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						monitor.beginTask(NLS.bind(Messages.RepositoryDNDHelper_SavingResourceTask, fullFilename), IProgressMonitor.UNKNOWN);
						// Gets a list of all siblings of the future resource
						// This will allow to compute correct ID and NAME information for
						// the
						// ResourceDescriptor
						List<ResourceDescriptor> childrenDescriptors = WSClientHelper.listFolder(targetParentResource, WSClientHelper.getClient(monitor, targetParentResource), targetParentResource.getValue()
								.getUriString(), new NullProgressMonitor(), 0);
						// Create the ResourceDescriptor depending on this kind (use file
						// extension)
						ResourceDescriptor newRD = getResourceDescriptor(targetParentResource, fileExt);
						// Update the NAME and ID for the ResourceDescriptor
						ResourceDescriptorUtil.setProposedResourceDescriptorIDAndName(childrenDescriptors, newRD, suggestedId, suggestedName);
						// Create and save the resource
						final AFileResource fileResource = createNewFileResource(targetParentResource, newRD, fileExt);
						fileResource.setFile(file);

						monitor.setTaskName(NLS.bind(Messages.RepositoryDNDHelper_SavingResourceTask, fullFilename));
						WSClientHelper.saveResource(fileResource, monitor);
					} catch (Throwable e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}

			});
		} catch (Exception e) {
			UIUtils.showError(e);
		}
	}

	/*
	 * Creates a FileResource instance for the specific resource kind.
	 */
	private static AFileResource createNewFileResource(MResource targetParentResource, ResourceDescriptor rd, String fileExt) {
		// Image file
		if (ImageUtils.hasValidFileImageExtension(fileExt)) {
			return new MRImage(targetParentResource, rd, -1);
		}
		// Jrxml file
		if (JRML_EXTENSION.equals(fileExt)) {
			return new MJrxml(targetParentResource, rd, -1);
		}
		// XML file
		if (XML_EXTENSION.equals(fileExt)) {
			return new MXmlFile(targetParentResource, rd, -1);
		}
		// Data Adapter file
		if (DATA_ADAPTER_EXTENSION.equals(fileExt)) {
			return new MRDataAdapter(targetParentResource, rd, -1);
		}
		// Resource bundle file
		if (RESOURCE_BUNDLE_EXTENSION.equals(fileExt)) {
			return new MResourceBundle(targetParentResource, rd, -1);
		}
		// Jar file
		if (JAR_EXTENSION.equals(fileExt)) {
			return new MJar(targetParentResource, rd, -1);
		}
		// Font file
		if (FONT_EXTENSION.equals(fileExt)) {
			return new MRFont(targetParentResource, rd, -1);
		}
		// Style Template file
		if (TEMPLATE_EXTENSION.equals(fileExt)) {
			return new MRStyleTemplate(targetParentResource, rd, -1);
		}
		throw new UnsupportedOperationException(MessageFormat.format(Messages.RepositoryDNDHelper_NewFileResourceErrMsg, new Object[] { fileExt }));
	}

	/*
	 * Creates a ResourceDescriptor instance for the specific resource kind.
	 */
	private static ResourceDescriptor getResourceDescriptor(MResource targetParentResource, String fileExt) {
		// Image file
		if (ImageUtils.hasValidFileImageExtension(fileExt)) {
			return MRImage.createDescriptor(targetParentResource);
		}
		// Jrxml file
		if (JRML_EXTENSION.equals(fileExt)) {
			return MJrxml.createDescriptor(targetParentResource);
		}
		// XML file
		if (XML_EXTENSION.equals(fileExt)) {
			return MXmlFile.createDescriptor(targetParentResource);
		}
		// Data Adapter file
		if (DATA_ADAPTER_EXTENSION.equals(fileExt)) {
			return MRDataAdapter.createDescriptor(targetParentResource);
		}
		// Resource bundle file
		if (RESOURCE_BUNDLE_EXTENSION.equals(fileExt)) {
			return MResourceBundle.createDescriptor(targetParentResource);
		}
		// Jar file
		if (JAR_EXTENSION.equals(fileExt)) {
			return MJar.createDescriptor(targetParentResource);
		}
		// Font file
		if (FONT_EXTENSION.equals(fileExt)) {
			return MRFont.createDescriptor(targetParentResource);
		}
		// Style Template file
		if (TEMPLATE_EXTENSION.equals(fileExt)) {
			return MRStyleTemplate.createDescriptor(targetParentResource);
		}
		throw new UnsupportedOperationException(MessageFormat.format(Messages.RepositoryDNDHelper_NewResourceDescriptorErrMsg, new Object[] { fileExt }));
	}
}
