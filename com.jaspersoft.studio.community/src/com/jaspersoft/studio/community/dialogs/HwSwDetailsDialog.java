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
package com.jaspersoft.studio.community.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.community.messages.Messages;
import com.jaspersoft.studio.community.utils.CommunityAPIUtils;

/**
 * Dialog that is supposed to show the Hardware and Software information
 * gathered from the system.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class HwSwDetailsDialog extends Dialog {

	private Object result;
	private Shell shell;
	private int locationY;
	private int locationX;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public HwSwDetailsDialog(Shell parent, int style) {
		super(parent, style);
		setText(Messages.HwSwDetailsDialog_Title);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.setLocation(locationX, locationY);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(600, 400);
		shell.setText(getText());
		shell.setLayout(new FillLayout());	
		
		StyledText text = new StyledText(shell, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		FontData currFontData = text.getFont().getFontData()[0];
		text.setFont(ResourceManager.getFont(
				currFontData.getName(), (int) currFontData.height + 2, currFontData.getStyle()));
		text.setText(CommunityAPIUtils.getHardwareSoftwareInfo());
		text.addLineStyleListener(new BoldLineStyle());
		text.setIndent(5);
	}
	
	/*
	 * LineStyler for putting in bold the title of each line.
	 */
	private class BoldLineStyle implements LineStyleListener {

		@Override
		public void lineGetStyle(LineStyleEvent event) {
			List<StyleRange> styles = new ArrayList<StyleRange>();

			for (int i = 0, n = event.lineText.length(); i < n; i++) {
				if (event.lineText.charAt(i) == ':') {
					styles.add(new StyleRange(event.lineOffset, i, null, null,
							SWT.BOLD));
					break;
				}
			}
			event.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
		}

	}

	/**
	 * Sets the preferred location for the dialog.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {
		this.locationX = x;
		this.locationY = y;
	}
}
