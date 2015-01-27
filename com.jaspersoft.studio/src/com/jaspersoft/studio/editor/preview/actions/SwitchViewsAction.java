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
package com.jaspersoft.studio.editor.preview.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jaspersoft.studio.editor.preview.MultiPageContainer;
import com.jaspersoft.studio.editor.preview.view.AViewsFactory;
import com.jaspersoft.studio.messages.Messages;

public class SwitchViewsAction extends Action implements IMenuCreator {
	public static final String SEPARATOR = "SEPARATOR"; //$NON-NLS-1$
	private MultiPageContainer container;
	protected String view;
	private boolean changeName = true;
	private AViewsFactory viewFactory;

	public SwitchViewsAction(MultiPageContainer container, String view, boolean changeName, AViewsFactory viewFactory) {
		super(view, AS_DROP_DOWN_MENU);
		setToolTipText(Messages.SwitchViewsAction_actionTooltip);
		setMenuCreator(this);
		this.viewFactory = viewFactory;
		this.container = container;
		this.view = view;
		this.changeName = changeName;
	}

	private Menu listMenu;

	public void dispose() {
		if (listMenu != null)
			listMenu.dispose();
	}

	public Menu getMenu(Control parent) {
		if (listMenu != null)
			listMenu.dispose();
		listMenu = new Menu(parent);

		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem menuItem = (MenuItem) e.getSource();
				menuItem.setSelection(true);
				view = (String) menuItem.getData("view.key"); //$NON-NLS-1$
				run();
			}
		};

		for (String key : container.getKeys()) {
			if (key.startsWith(SEPARATOR)) {
				new MenuItem(listMenu, SWT.SEPARATOR);
			} else {
				MenuItem m1 = new MenuItem(listMenu, SWT.RADIO);
				m1.setText(viewFactory.getLabel(key));
				m1.addSelectionListener(listener);
				m1.setData("view.key", key); //$NON-NLS-1$
			}
		}
		return listMenu;
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

	@Override
	public void run() {
		if (view != null) {
			if (changeName)
				setText(viewFactory.getLabel(view));
			container.switchView(view);
			container.afterSwitchView();
		}
	}
}
