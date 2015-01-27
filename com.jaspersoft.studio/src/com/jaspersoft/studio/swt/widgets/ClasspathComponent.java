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
package com.jaspersoft.studio.swt.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class ClasspathComponent {

	public class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			File file = (File) element;
			if (!file.exists())
				return JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/obj16/error_tsk.gif"); //$NON-NLS-1$
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return element.toString();
		}

	}

	private List<File> classpaths = new ArrayList<File>();

	public List<String> getClasspaths() {
		List<String> paths = new ArrayList<String>();
		if (classpaths != null)
			for (File f : classpaths)
				paths.add(f.getAbsolutePath());
		return paths;
	}

	public void setClasspaths(List<String> paths) {
		this.classpaths = new ArrayList<File>();
		for (String path : paths) {
			File file = new File(path);

			classpaths.add(file);
		}
		tviewer.setInput(classpaths);
	}

	public ClasspathComponent(Composite parent) {
		createComponent(parent);
	}

	private Control control;
	private TableViewer tviewer;
	private Table wtable;

	public Control getControl() {
		return control;
	}

	public TableViewer getViewer() {
		return tviewer;
	}

	public void createComponent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		wtable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 100;
		gd.heightHint = 100;
		wtable.setLayoutData(gd);
		wtable.setHeaderVisible(true);

		TableColumn[] col = new TableColumn[1];
		col[0] = new TableColumn(wtable, SWT.NONE);
		col[0].setText(Messages.ClasspathComponent_1);
		col[0].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		wtable.setLayout(tlayout);

		tviewer = new TableViewer(wtable);
		tviewer.setContentProvider(new ListContentProvider());
		tviewer.setLabelProvider(new TLabelProvider());

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		bGroup.setBackground(parent.getBackground());

		new NewButton() {
			protected void afterElementAdded(Object selement) {
				handleClasspathChanged();
			}
		}.createNewButtons(bGroup, tviewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN | SWT.MULTI);
				dialog.setFilterNames(new String[] { "JAR Files", "All Files (*.*)" }); //$NON-NLS-1$ //$NON-NLS-2$
				dialog.setFilterExtensions(new String[] { "*.jar", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$

				if (dialog.open() != null) {
					String dir = dialog.getFilterPath();
					String[] jars = dialog.getFileNames();
					String delimiter = System.getProperty("file.separator"); //$NON-NLS-1$
					File[] files = new File[jars.length];
					for (int i = 0; i < jars.length; i++) {
						files[i] = new File(dir + delimiter + jars[i]);
					}
					return files;
				}
				return null;
			}

		});
		new DeleteButton() {
			protected void afterElementDeleted(Object element) {
				handleClasspathChanged();
			}
		}.createDeleteButton(bGroup, tviewer);

		this.control = composite;
	}

	protected void handleClasspathChanged() {
	}
}
