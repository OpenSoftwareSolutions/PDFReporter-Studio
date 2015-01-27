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
package com.jaspersoft.studio.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jaspersoft.studio.editor.AMultiEditor;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.preview.PreviewContainer;
import com.jaspersoft.studio.editor.preview.view.AViewsFactory;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Action to switch the preview format of the Preview area. This is not a real action since the run method is empty. It
 * instead create a separated menu with the appropriated selection listener to do the switch. However it must extend
 * action to be contributed
 * 
 * @author Orlandin Marco
 * 
 */
public class PreviewFormatDropDownAction extends Action implements IMenuCreator {

	/**
	 * Id of the action
	 */
	public static final String ID = "SettingOutputAction";

	/**
	 * The last menu generated
	 */
	private Menu menu;

	/**
	 * Configuration of the current report
	 */
	private JasperReportsConfiguration jConfig;

	private AViewsFactory viewFactory;

	/**
	 * Create the action
	 * 
	 * @param jConfig
	 *          the jasper configuration of the current report
	 */
	public PreviewFormatDropDownAction(JasperReportsConfiguration jConfig) {
		setText(Messages.ViewSettingsDropDownAction_settingsName);
		setMenuCreator(this);
		setId(ID);
		this.jConfig = jConfig;
	}

	/**
	 * Return the JRXML editor where the current editor is opened
	 * 
	 * @return a JRXMLeditor
	 */
	private JrxmlEditor getEditor() {
		return (JrxmlEditor) jConfig.get(AMultiEditor.THEEDITOR);
	}

	/**
	 * If the last menu generated is not null the it is disposed
	 */
	@Override
	public void dispose() {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}
	}

	/**
	 * Generate the contextual menu that list all the available data preview formats and when one of them is choose then
	 * it is set on the preview editor. The selected one is also highlighted inside the list
	 */
	@Override
	public Menu getMenu(Menu parent) {
		createOutputMenu(parent);
		return menu;
	}

	/**
	 * Build and return the menu for a control
	 */
	@Override
	public Menu getMenu(Control parent) {
		Menu rootMenu = new Menu(parent);
		createOutputMenu(rootMenu);
		menu = rootMenu;
		return menu;
	}

	/**
	 * Create a single menu item of the list
	 * 
	 * @param key
	 *          the key of the preview output format that this item select
	 * @param editor
	 *          The jrxml editor
	 */
	private void creteItem(final String key, final JrxmlEditor editor) {
		final MenuItem item = new MenuItem(menu, SWT.CHECK);
		item.setText(viewFactory.getLabel(key));
		item.setData("KEY", key);
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				editor.setPreviewOutput((String) item.getData("KEY"), false);
				editor.setPreviewDirty(true);
			}

		});
	}

	/**
	 * Generate the list of all the previews output format
	 * 
	 * @param parent
	 */
	private void createOutputMenu(Menu parent) {
		MenuItem root = new MenuItem(parent, SWT.CASCADE);
		menu = new Menu(parent);
		root.setMenu(menu);
		root.setText(Messages.ViewSettingsDropDownAction_previewFormatMenu);
		JrxmlEditor editor = getEditor();
		PreviewContainer preview = (PreviewContainer) editor.getEditor(JrxmlEditor.PAGE_PREVIEW);
		viewFactory = preview.getViewFactory();
		for (String key : viewFactory.getKeys()) {
			if (viewFactory.isSeparator(key)) {
				new MenuItem(menu, SWT.SEPARATOR);
			} else {
				creteItem(key, editor);
			}
		}

		menu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				String actualPreview = getEditor().getDefaultViewerKey();
				for (MenuItem item : menu.getItems()) {
					item.setSelection(item.getText().equals(actualPreview));
				}
			}
		});
	}
}
