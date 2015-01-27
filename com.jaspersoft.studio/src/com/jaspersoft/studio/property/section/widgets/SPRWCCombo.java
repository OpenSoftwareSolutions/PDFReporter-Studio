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
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.wb.swt.ResourceCache;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.combo.RWCComboPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.Misc;

public class SPRWCCombo extends ASPropertyWidget {
	
	protected CCombo combo;
	
	private ResourceCache cache = new ResourceCache();

	private static class ComboAction extends Action {
		/**
		 * element that this entry represent
		 */

		/**
		 * Create a new entry for the menu
		 * 
		 * @param name
		 *          Name of the entry
		 * @param style
		 *          Style of the entry
		 * @param item
		 *          element that this entry represent
		 */
		public ComboAction(String name, int style) {
			super(name, style);
		}

	}

	public SPRWCCombo(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return combo;
	}

	private boolean refresh = false;

	protected void createComponent(Composite parent) {
		combo = new CCombo(parent, SWT.FLAT);
		setNewItems((RWCComboPropertyDescriptor) pDescriptor);
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (refresh)
					return;
				if (combo.getSelectionIndex() >= 0) {
					section.changeProperty(pDescriptor.getId(), combo.getItem(combo.getSelectionIndex()));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		combo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (refresh)
					return;
				section.changeProperty(pDescriptor.getId(), combo.getText());
			}
		});
		combo.setToolTipText(pDescriptor.getDescription());
		combo.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
					cache.dispose();
			}
		});
	}

	protected APropertyNode pnode;

	public void setData(APropertyNode pnode, Object b) {
		this.pnode = pnode;
		refresh = true;
		final RWCComboPropertyDescriptor pd = (RWCComboPropertyDescriptor) pDescriptor;

		String str = (String) b;
		String[] items = combo.getItems();
		int selection = 0;
		for (int i = 0; i < items.length; i++) {
			if (Misc.compare(items[i], str, pd.isCaseSensitive())) {
				selection = i;
				break;
			}
		}
		combo.select(selection);
		if (selection == 0 && pd.getItems().length > 0) {
			str = Misc.nvl(str);
			combo.setText(str);
		}
		int stringLength = combo.getText().length();

		combo.setSelection(new Point(stringLength, stringLength));
		refresh = false;
	}

	private Image createImage(final String fontName) {

		Image stringImage = cache.getImage(fontName);
		if (stringImage == null){
			Display display = Display.getCurrent();
			Color TRANSPARENT_COLOR = display.getSystemColor(SWT.COLOR_WHITE);
			Color DRAWING_COLOR = display.getSystemColor(SWT.COLOR_BLACK);
			PaletteData paletteData = new PaletteData(new RGB[] { TRANSPARENT_COLOR.getRGB(), DRAWING_COLOR.getRGB() });
			ImageData imageData = new ImageData(55, 15, 4, paletteData);
			imageData.transparentPixel = 0; // index of the palette
	
			stringImage = new Image(display, imageData);
			GC stringGc = new GC(stringImage);
			try {
				stringGc.setForeground(DRAWING_COLOR);
				stringGc.setBackground(TRANSPARENT_COLOR);
				stringGc.setFont(cache.getFont(fontName, 10, 0));
				stringGc.drawText("Sample", 0, 0);
			} finally {
				stringGc.dispose();
				cache.storeImage(fontName, stringImage);
			}
		}
		return stringImage;
	}

	
	public void setNewItems(final RWCComboPropertyDescriptor pd) {
		MenuManager manager = new MenuManager("#PopUpMenu");
		for (String element : pd.getItems()) {
			ComboAction action = new ComboAction(element, SWT.NONE);
			action.setImageDescriptor(ImageDescriptor.createFromImage(createImage(element)));
			manager.add(action);
			manager.add(new Separator());
		}
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end"));
		// }
		// });
		Menu menu = manager.createContextMenu(combo);
		combo.setMenu(menu);
		// combo.setItems(pd.getItems());
	}
}
