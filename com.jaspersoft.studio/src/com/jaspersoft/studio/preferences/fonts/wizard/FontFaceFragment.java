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

import java.io.File;

import net.sf.jasperreports.engine.fonts.SimpleFontFace;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.Misc;

public class FontFaceFragment {

	private SimpleFontFace fontFace;

	protected FontFaceFragment(SimpleFontFace fontFace) {
		this.fontFace = fontFace;
	}

	public Composite createDialogArea(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(3, false));

		Text txt = createFileField(cmp, "TrueType", "ttf"); //$NON-NLS-1$ //$NON-NLS-2$
		txt.setText(Misc.nvl(fontFace.getTtf()));
		txt.setToolTipText(Misc.nvl(fontFace.getTtf()));
		txt = createFileField(cmp, "Embedded OpenType", "eot"); //$NON-NLS-1$ //$NON-NLS-2$
		txt.setText(Misc.nvl(fontFace.getEot()));
		txt.setToolTipText(Misc.nvl(fontFace.getEot()));
		txt = createFileField(cmp, "Scalable Vector Graphics", "svg"); //$NON-NLS-1$ //$NON-NLS-2$
		txt.setText(Misc.nvl(fontFace.getSvg()));
		txt.setToolTipText(Misc.nvl(fontFace.getSvg()));
		txt = createFileField(cmp, "Web Open Font Format", "woff"); //$NON-NLS-1$ //$NON-NLS-2$
		txt.setText(Misc.nvl(fontFace.getWoff()));
		txt.setToolTipText(Misc.nvl(fontFace.getWoff()));

		new Label(cmp, SWT.NONE).setText("PDF Font Name");

		final Combo txtPdf = new Combo(cmp, SWT.BORDER);
		txtPdf.setItems(new String[] { "Courier", "Courier-Bold", "Courier-BoldOblique", "Courier-Oblique", "Helvetica",
				"Helvetica-Bold", "Helvetica-BoldOblique", "Helvetica-Oblique", "Symbol", "Times-Roman", "Times-Bold",
				"Times-BoldItalic", "Times-Italic", "ZapfDingbats", "STSong-Light", "Mhei-Medium", "MSung-Light",
				"HeiseiKakuGo-W5", "HeiseiMin-W3", "HYGoThic-Medium", "HYSMyeongJo-Medium" });
		txtPdf.setText(Misc.nvl(fontFace.getPdf()));
		txtPdf.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				fontFace.setPdf(txtPdf.getText());
			}
		});
		return cmp;
	}

	private Text createFileField(Composite composite, String name, final String type) {
		new Label(composite, SWT.NONE).setText(name + " (." + type + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		final Text txt = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		txt.setLayoutData(gd);
		txt.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String selected = txt.getText();
				if (selected.trim().isEmpty())
					selected = null;
				if (type.equals("ttf"))
					fontFace.setTtf(selected);
				else if (type.equals("eot"))
					fontFace.setEot(selected);
				else if (type.equals("svg"))
					fontFace.setSvg(selected);
				else if (type.equals("woff"))
					fontFace.setWoff(selected);
			}
		});

		Button button = new Button(composite, SWT.PUSH);
		button.setText(Messages.FontFamilyPage_browseButton);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
				fd.setText(Messages.FontFamilyPage_browseDialogTitle);
				setupLastLocation(fd);
				String font = null;
				if (type.equals("ttf"))
					font = fontFace.getTtf();
				else if (type.equals("eot"))
					font = fontFace.getEot();
				else if (type.equals("svg"))
					font = fontFace.getSvg();
				else if (type.equals("woff"))
					font = fontFace.getWoff();
				if (font != null)
					fd.setFilterPath(font.substring(0, font.lastIndexOf(File.separatorChar)));
				fd.setFilterExtensions(new String[] { "*." + type + ";*." + type.toUpperCase(), "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$  
				String selected = fd.open();
				setLastLocation(fd, selected);
				if (selected != null) {
					selected = selected.trim();
					if (type.equals("ttf"))
						fontFace.setTtf(selected);
					else if (type.equals("eot"))
						fontFace.setEot(selected);
					else if (type.equals("svg"))
						fontFace.setSvg(selected);
					else if (type.equals("woff"))
						fontFace.setWoff(selected);
					txt.setText(Misc.nvl(selected));
					txt.setToolTipText(Misc.nvl(selected));
				}
			}
		});
		return txt;
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
}
