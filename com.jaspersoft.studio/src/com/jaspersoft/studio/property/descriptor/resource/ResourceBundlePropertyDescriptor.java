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
package com.jaspersoft.studio.property.descriptor.resource;

import java.util.Locale;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPResourceType;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ResourceBundleFilterDialog;

/**
 * 
 * Widget descriptor with a button to select a properties resource file and a text area where the path of the selected
 * resource is shown. From the selected filename is calculated the bundle base name. Other than this if the parent
 * folder of the file is not in the classpath it is proposed to the user to add it automatically
 * 
 * @author Orlandin Marco & Slavic
 * 
 */
public class ResourceBundlePropertyDescriptor extends NTextPropertyDescriptor {

	/**
	 * This class extends the original widget to select a resource to be used only with resource bundle. For example the
	 * selection of the file is limited to the files with .properties extension and contained in the actually opened
	 * project or in one of its dependences
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	private class SPBundleType extends SPResourceType {

		public SPBundleType(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
			super(parent, section, pDescriptor);
		}

		@Override
		protected String convertFile2Value(IFile f) {
			String fname = ResourceBundlePropertyDescriptor.this.convertFile2Value(f);
			JasperReportsConfiguration jConf = pnode.getJasperConfiguration();
			if (jConf != null) {
				IFile file = (IFile) jConf.get(FileUtils.KEY_FILE);
				if (file != null && file.getParent().equals(f.getParent()))
					return fname;
			}
			String pname = f.getParent().getProjectRelativePath().toOSString();
			if (pname.startsWith("/"))
				pname = pname.substring(1);
			if (!pname.isEmpty())
				pname += "/";
			return pname + fname;
		}

		@Override
		protected SelectionAdapter buttonPressed() {
			return new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					IJavaProject openProject = SelectionHelper.getJavaProjectFromCurrentJRXMLEditor();
					if (openProject != null) {
						ResourceBundleFilterDialog dialog = new ResourceBundleFilterDialog(ftext.getShell(), false, openProject);
						dialog.setTitle(Messages.ResourceCellEditor_open_resource);
						if (dialog.open() == Window.OK) {
							IFile file = (IFile) dialog.getFirstResult();
							if (file != null)
								handleTextChanged(section, pDescriptor.getId(), convertFile2Value(file));
						}
					}
				}
			};
		}
	};

	public ResourceBundlePropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new ResourceCellEditor(parent) {
			@Override
			protected String convertFile2Value(IFile f) {
				return ResourceBundlePropertyDescriptor.this.convertFile2Value(f);
			}
		};
		editor.setValidator(NResourceCellEditorValidator.instance());
		setValidator(NResourceCellEditorValidator.instance());
		HelpSystem.bindToHelp(this, editor.getControl());
		return editor;
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		ASPropertyWidget textWidget = new SPBundleType(parent, section, this);
		textWidget.setReadOnly(readOnly);
		return textWidget;
	}

	private String convertFile2Value(IFile f) {
		String fileName = f.getName().trim();
		int propertiesIndex = fileName.toLowerCase().lastIndexOf(".properties"); //$NON-NLS-1$
		if (propertiesIndex != -1)
			fileName = fileName.substring(0, propertiesIndex);
		return removeLocale(fileName);
	}

	/**
	 * Check if the filename has a locale as terminal part, in this case the locale is removed to get a base name
	 * 
	 * @param fileName
	 *          original filename
	 * @return filename without locale (if the filename had not a locale this is equal to the filename)
	 */
	private String removeLocale(String fileName) {
		for (Locale loc : Locale.getAvailableLocales()) {
			if (fileName.endsWith("_" + loc.toString())) //$NON-NLS-1$
				return fileName.substring(0, fileName.length() - loc.toString().length() - 1);
		}
		return fileName;
	}

}
