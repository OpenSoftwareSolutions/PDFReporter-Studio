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
package com.jaspersoft.studio.preferences.fonts;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.util.StringUtils;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.SimpleFontExtensionHelper;
import net.sf.jasperreports.engine.fonts.SimpleFontFace;
import net.sf.jasperreports.engine.fonts.SimpleFontFamily;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.editor.table.TableFieldEditor;
import com.jaspersoft.studio.preferences.fonts.wizard.FontConfigWizard;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class FontListFieldEditor extends TableFieldEditor {

	private Button editButton;
	private Button exportButton;

	public FontListFieldEditor() {
		super();
	}

	public FontListFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, new String[] { Messages.FontListFieldEditor_fontNameLabel }, new int[] { 100 }, parent);
	}

	@Override
	protected String createList(String[][] items) {
		return SimpleFontExtensionHelper.getFontsXml(fontFamily);
	}

	List<FontFamily> fontFamily = new ArrayList<FontFamily>();

	@Override
	protected void removePressed() {
		int index = table.getSelectionIndex();
		fontFamily.remove(index);
		super.removePressed();
	}

	@Override
	protected String[][] parseString(String string) {
		String[][] res = null;
		if (string != null && !string.isEmpty()) {
			try {
				fontFamily = SimpleFontExtensionHelper.getInstance().loadFontFamilies(
						JasperReportsConfiguration.getDefaultJRConfig(), new ByteArrayInputStream(string.getBytes()));

				res = new String[fontFamily.size()][1];
				for (int i = 0; i < fontFamily.size(); i++)
					res[i][0] = fontFamily.get(i).getName();
			} catch (Exception e) {
				e.printStackTrace();
				fontFamily = new ArrayList<FontFamily>();
				res = new String[0][0];
			}
		} else {
			fontFamily = new ArrayList<FontFamily>();
			res = new String[0][0];
		}
		return res;
	}

	@Override
	protected String[] getNewInputObject() {
		// run dialog wizard
		SimpleFontFamily font2 = new SimpleFontFamily();
		font2.setName(Messages.FontListFieldEditor_newFontSuggestedName);
		FontFamily font = runDialog(font2);
		if (font != null) {
			fontFamily.add(font);
			return new String[] { font.getName() };
		}
		return null;
	}

	protected void editPressed() {
		setPresentsDefaultValue(false);
		int index = table.getSelectionIndex();
		if (index >= 0) {
			TableItem titem = table.getItem(index);
			FontFamily font = fontFamily.get(index);
			if (font != null) {
				font = runDialog((SimpleFontFamily) ((SimpleFontFamily) font).clone());
				if (font != null) {
					titem.setText(font.getName());
					fontFamily.set(index, font);
				}
			}
		}
	}

	private static String lastLocation;

	public static String setupLastLocation(FileDialog dialog) {
		if (lastLocation == null)
			lastLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		dialog.setFilterPath(lastLocation);
		return lastLocation;
	}

	public static void setLastLocation(FileDialog dialog, String selected) {
		if (!Misc.isNullOrEmpty(selected))
			lastLocation = selected.substring(0, selected.lastIndexOf(File.separatorChar));
		else if (!Misc.isNullOrEmpty(dialog.getFileName()))
			lastLocation = dialog.getFileName();
	}

	protected void exportPressed() {
		int[] selection = table.getSelectionIndices();
		if (selection != null && selection.length > 0) {
			final List<FontFamily> lst = new ArrayList<FontFamily>(selection.length);
			for (int s : selection) {
				FontFamily font = fontFamily.get(s);
				if (font instanceof JRCloneable)
					lst.add((FontFamily) ((JRCloneable) font).clone());
			}
			final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
			fd.setText(Messages.FontListFieldEditor_exportToJar);
			setupLastLocation(fd);
			fd.setFilterExtensions(new String[] { "*.jar", "*.zip" }); //$NON-NLS-1$ //$NON-NLS-2$
			final String selected = fd.open();
			setLastLocation(fd, selected);
			if (selected != null) {
				Job job = new Job(Messages.FontListFieldEditor_exportToJar) {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask(Messages.FontListFieldEditor_exportToJar, IProgressMonitor.UNKNOWN);
						try {
							exportJAR(lst, selected);

							IFile[] resource = root.findFilesForLocationURI(new File(selected).toURI());
							if (resource != null) {
								for (IFile f : resource)
									f.refreshLocal(1, monitor);
							}
						} catch (final Exception e) {
							e.printStackTrace();
							UIUtils.getDisplay().asyncExec(new Runnable() {
								public void run() {
									IStatus status = new OperationStatus(IStatus.ERROR, JaspersoftStudioPlugin.getUniqueIdentifier(), 1,
											"Error saving file.", e.getCause()); //$NON-NLS-1$
									ErrorDialog.openError(Display.getDefault().getActiveShell(), Messages.FontListFieldEditor_errorSave,
											null, status);
								}
							});
						} finally {
							monitor.done();
						}
						return Status.OK_STATUS;
					}
				};
				job.setPriority(Job.LONG);
				job.schedule();
			}
		}
	}

	private void exportJAR(List<FontFamily> lst, String selected) throws IOException, JRException {
		FileOutputStream fos = new FileOutputStream(selected);
		try {
			ZipOutputStream zipos = new java.util.zip.ZipOutputStream(fos);
			zipos.setMethod(ZipOutputStream.DEFLATED);

			String prefix = "family" + (new Date()).getTime(); //$NON-NLS-1$
			String fontXmlFile = "fonts" + prefix + ".xml"; //$NON-NLS-1$ //$NON-NLS-2$

			ZipEntry propsEntry = new ZipEntry("jasperreports_extension.properties"); //$NON-NLS-1$
			zipos.putNextEntry(propsEntry);

			PrintWriter pw = new PrintWriter(zipos);

			pw.println("net.sf.jasperreports.extension.registry.factory.fonts=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory"); //$NON-NLS-1$
			pw.println("net.sf.jasperreports.extension.simple.font.families.ireport" + prefix + "=fonts/" + fontXmlFile); //$NON-NLS-1$ //$NON-NLS-2$

			pw.flush();
			Set<String> names = new HashSet<String>();
			List<FontFamily> newfonts = new ArrayList<FontFamily>(lst.size());
			for (FontFamily f : lst) {
				writeFont2zip(names, zipos, f, (SimpleFontFace) f.getNormalFace());
				writeFont2zip(names, zipos, f, (SimpleFontFace) f.getBoldFace());
				writeFont2zip(names, zipos, f, (SimpleFontFace) f.getItalicFace());
				writeFont2zip(names, zipos, f, (SimpleFontFace) f.getBoldItalicFace());

				String pdfenc = f.getPdfEncoding();
				if (ModelUtils.getKey4PDFEncoding(pdfenc) == null) {
					pdfenc = ModelUtils.getPDFEncoding2key(pdfenc);
					((SimpleFontFamily) f).setPdfEncoding(pdfenc);
				}
				newfonts.add(f);
			}

			ZipEntry fontsXmlEntry = new ZipEntry("fonts/" + fontXmlFile); //$NON-NLS-1$
			zipos.putNextEntry(fontsXmlEntry);

			SimpleFontExtensionHelper.writeFontsXml(zipos, newfonts);

			zipos.finish();
		} finally {
			FileUtils.closeStream(fos);
		}
	}

	private void writeFont2zip(Set<String> names, ZipOutputStream zipos, FontFamily fontFamily, SimpleFontFace font)
			throws IOException {
		if (font == null)
			return;
		try {
			font.setTtf(writeFont(names, zipos, fontFamily, font, font.getTtf()));
		} catch (JRRuntimeException r) {
		}
		font.setPdf(writeFont(names, zipos, fontFamily, font, font.getPdf()));
		font.setEot(writeFont(names, zipos, fontFamily, font, font.getEot()));
		font.setSvg(writeFont(names, zipos, fontFamily, font, font.getSvg()));
		font.setWoff(writeFont(names, zipos, fontFamily, font, font.getWoff()));
	}

	private static String writeFont(Set<String> names, ZipOutputStream zipos, FontFamily fontFamily, FontFace font,
			String fontname) throws IOException {
		if (Misc.isNullOrEmpty(fontname))
			return fontname;
		File file = new File(fontname);
		if (file.exists()) {
			String name = "fonts/" + StringUtils.toPackageName(fontFamily.getName()) + "/" + file.getName(); //$NON-NLS-1$ 
			if (!names.contains(name)) {
				ZipEntry ttfZipEntry = new ZipEntry(name);
				zipos.putNextEntry(ttfZipEntry);

				FileInputStream in = new FileInputStream(fontname); // Stream to read file
				try {
					byte[] buffer = new byte[4096]; // Create a buffer for copying
					int bytesRead;
					while ((bytesRead = in.read(buffer)) != -1)
						zipos.write(buffer, 0, bytesRead);
				} finally {
					FileUtils.closeStream(in);
				}
				names.add(name);
			}
			fontname = name;
		}
		return fontname;
	}

	private FontFamily runDialog(FontFamily font) {
		FontConfigWizard wizard = new FontConfigWizard();
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		wizard.setFont(font);
		dialog.create();
		if (dialog.open() == Dialog.OK)
			return wizard.getFont();
		return null;
	}

	@Override
	protected void createButtons(Composite box) {
		super.createButtons(box);

		editButton = createPushButton(box, Messages.FontListFieldEditor_editButton);

		exportButton = createPushButton(box, Messages.FontListFieldEditor_exportButton);
	}

	public void createSelectionListener() {
		selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Widget widget = event.widget;
				if (widget == addButton) {
					addPressed();
				} else if (widget == duplicateButton) {
					duplicatePressed();
				} else if (widget == removeButton) {
					removePressed();
				} else if (widget == upButton) {
					upPressed();
				} else if (widget == downButton) {
					downPressed();
				} else if (widget == editButton) {
					editPressed();
				} else if (widget == exportButton) {
					exportPressed();
				} else if (widget == table) {
					selectionChanged();
				}
			}
		};
	}

	protected void selectionChanged() {
		super.selectionChanged();
		int index = table.getSelectionIndex();
		int size = table.getItemCount();
		if (editButton != null)
			editButton.setEnabled(size >= 1 && index >= 0 && index < size && isEditable(index));
		if (exportButton != null)
			exportButton.setEnabled(size >= 1 && index >= 0 && index < size);
	}

	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		editButton.setEnabled(enabled);
		exportButton.setEnabled(enabled);
	}

	protected boolean isEditable(int row) {
		return true;
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

	@Override
	protected boolean isFieldEditable(int col, int row) {
		return false;
	}

	@Override
	protected boolean isRemovable(int row) {
		return super.isRemovable(row);
	}

	@Override
	protected boolean isSortable(int row) {
		return false;
	}

	@Override
	protected void handleTableDoubleClick() {
		super.handleTableDoubleClick();
		editPressed();
	}
}
