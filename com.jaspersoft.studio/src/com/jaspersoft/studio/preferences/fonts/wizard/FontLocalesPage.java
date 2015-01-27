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
package com.jaspersoft.studio.preferences.fonts.wizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.SimpleFontFamily;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.jface.dialogs.LocaleDialog;
import com.jaspersoft.studio.preferences.editor.table.TableLabelProvider;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class FontLocalesPage extends WizardPage {
	private SimpleFontFamily fontFamily;

	public FontLocalesPage(FontFamily fontFamily) {
		super("fontlocalespage"); //$NON-NLS-1$
		setTitle("Locales");
		setDescription("Font locales mapping");
		this.fontFamily = (SimpleFontFamily) fontFamily;
	}

	@Override
	public void dispose() {
		List<String> inlist = (List<String>) tableViewer.getInput();
		HashSet<String> locales = new HashSet<String>(inlist);
		if (locales.isEmpty())
			fontFamily.setLocales(null);
		else
			fontFamily.setLocales(locales);

		super.dispose();
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		setControl(composite);

		Label lbl = new Label(composite, SWT.WRAP);
		lbl.setText("If you want to use this set of True Type Fonts only for a set of Locales, you can"
				+ "specify them here. In example you can define this family to be used with Chinese, and select"
				+ "the Chinese Locale. Subsequently you can define a new font extension like this one, with the "
				+ "same family name but specifying a set of TrueType font files to be used with the Japanese locale."
				+ "\nSpecify the Locales with wich this font should be used. Leave the list empty to support any locale.");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.widthHint = 300;
		lbl.setLayoutData(gd);

		buildTable(composite);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				LocaleDialog ld = new LocaleDialog(Display.getCurrent().getActiveShell(), null);
				if (ld.open() == Dialog.OK) {
					Locale l = ld.getLocale();
					if (fontFamily.getLocales() == null)
						fontFamily.setLocales(new HashSet<String>());
					if (!fontFamily.getLocales().contains(l)) {
						fontFamily.getLocales().add(l.toString());
						return l.toString();
					}
				}
				return null;
			}

		});

		new DeleteButton().createDeleteButton(bGroup, tableViewer);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "Jaspersoft.wizard"); //$NON-NLS-1$
	}

	private Table table;
	private TableViewer tableViewer;

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setHeaderVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		// attachCellEditors(tableViewer, table);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[1];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText("Locale");

		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();

		fillTable(table);
	}

	private void fillTable(Table table) {
		List<String> lst = new ArrayList<String>();

		Set<String> locales = fontFamily.getLocales();
		if (locales != null)
			lst.addAll(fontFamily.getLocales());
		tableViewer.setInput(lst);
	}

}
