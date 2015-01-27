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
package com.jaspersoft.studio.properties.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.properties.view.ITabItem;

/**
 * Shows the list of tabs in the tabbed property sheet page.
 * 
 */
public class TabbedPropertyList {

	protected static final int NONE = -1;

	private int selectedElementIndex = NONE;

	private ToolBarManager toolBarManager;

	private CoolItem coolItem;

	private CoolBar cBar;
	private Menu chevronMenu;

	/**
	 * Constructor for TabbedPropertyList.
	 * 
	 * @param parent
	 *            the parent widget.
	 * @param factory
	 *            the widget factory.
	 */
	public TabbedPropertyList(Composite parent) {
		cBar = new CoolBar(parent, SWT.HORIZONTAL);
		cBar.setLocked(true);
		coolItem = new CoolItem(cBar, SWT.DROP_DOWN | SWT.NO_FOCUS);
		ToolBar toolBar = new ToolBar(cBar, SWT.FLAT | SWT.RIGHT | SWT.NO_FOCUS);
		toolBarManager = new ToolBarManager(toolBar);

		coolItem.setControl(toolBar);
		coolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == SWT.ARROW) {
					CoolItem item = (CoolItem) event.widget;
					Rectangle itemBounds = item.getBounds();
					Point pt = cBar.toDisplay(new Point(itemBounds.x,
							itemBounds.y));
					itemBounds.x = pt.x;
					itemBounds.y = pt.y;
					ToolBar bar = (ToolBar) item.getControl();
					ToolItem[] tools = bar.getItems();

					int i = 0;
					while (i < tools.length) {
						Rectangle toolBounds = tools[i].getBounds();
						pt = bar.toDisplay(new Point(toolBounds.x, toolBounds.y));
						toolBounds.x = pt.x;
						toolBounds.y = pt.y;

						/*
						 * Figure out the visible portion of the tool by looking
						 * at the intersection of the tool bounds with the cool
						 * item bounds.
						 */
						Rectangle intersection = itemBounds
								.intersection(toolBounds);

						/*
						 * If the tool is not completely within the cool item
						 * bounds, then it is partially hidden, and all
						 * remaining tools are completely hidden.
						 */
						if (!intersection.equals(toolBounds))
							break;
						i++;
					}

					/*
					 * Create a menu with items for each of the completely
					 * hidden buttons.
					 */
					if (chevronMenu != null)
						chevronMenu.dispose();
					chevronMenu = new Menu(cBar);
					for (int j = i; j < tools.length; j++) {
						final ToolItem titem = tools[j];

						MenuItem menuItem = new MenuItem(chevronMenu, SWT.PUSH);

						menuItem.setText(titem.getText());
						menuItem.setImage(titem.getImage());
						menuItem.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e) {
								if (titem.getData() instanceof ActionContributionItem) {
									((ActionContributionItem) titem.getData())
											.getAction().run();
								}
							}
						});

					}

					/*
					 * Drop down the menu below the chevron, with the left edges
					 * aligned.
					 */
					pt = cBar.toDisplay(new Point(event.x, event.y));
					chevronMenu.setLocation(pt.x, pt.y);
					chevronMenu.setVisible(true);
				}
			}
		});
		calcSize(coolItem);
	}

	public Composite getControl() {
		return cBar;
	}

	private void calcSize(CoolItem item) {
		toolBarManager.update(true);
	}

	/**
	 * Returns the zero-relative index of the item which is currently selected
	 * in the receiver, or -1 if no item is selected.
	 * 
	 * @return the index of the selected item
	 */
	public int getSelectionIndex() {
		return selectedElementIndex;
	}

	/**
	 * Removes all elements from this list.
	 */
	public void removeAll() {
		toolBarManager.removeAll();
		toolBarManager.update(true);

		selectedElementIndex = NONE;
	}

	/**
	 * Sets the new list elements.
	 * 
	 * @param children
	 */
	public void setElements(Object[] children) {
		removeAll();
		for (int i = 0; i < children.length; i++) {
			ITabItem tabItem = (ITabItem) children[i];
			ActionContributionItem aci = new ActionContributionItem(
					new TabAction(tabItem));
			aci.setMode(ActionContributionItem.MODE_FORCE_TEXT);
			toolBarManager.add(aci);
		}
		calcSize(coolItem);
	}

	/**
	 * Selects one of the elements in the list.
	 * 
	 * @param index
	 *            the index of the element to select.
	 */
	protected void select(int index) {
		if (getSelectionIndex() == index){
			IContributionItem item = toolBarManager.getItems()[index];
			//Code to keep the button pressed when it is clicked more than once
			if (item instanceof ActionContributionItem){
				ActionContributionItem aItem = (ActionContributionItem)item;
				aItem.getAction().setChecked(true);
			}
			return;
		}
		deselectAll();
		IContributionItem[] items = toolBarManager.getItems();
		if (index >= 0 && index < items.length) {
			((ActionContributionItem) items[index]).getAction()
					.setChecked(true);
			selectedElementIndex = index;
			cBar.notifyListeners(SWT.Selection, new Event());
		}
	}

	/**
	 * Deselects all the elements in the list.
	 */
	public void deselectAll() {
		IContributionItem[] items = toolBarManager.getItems();
		for (IContributionItem i : items)
			((ActionContributionItem) i).getAction().setChecked(false);
		selectedElementIndex = NONE;
	}

	class TabAction extends Action {
		public TabAction(ITabItem tabItem) {
			super(tabItem.getText(), IAction.AS_CHECK_BOX);
			setImageDescriptor(tabItem.getImage());
		}

		@Override
		public void run() {
			IContributionItem[] items = toolBarManager.getItems();
			for (int i = 0; i < items.length; i++) {
				ActionContributionItem item = (ActionContributionItem) items[i];
				if (item.getAction() == this) {
					select(i);

					/*
					 * We set focus to the tabbed property composite so that
					 * focus is moved to the appropriate widget in the section.
					 */
					Composite tabbedPropertyComposite = cBar.getParent();
					while (!(tabbedPropertyComposite instanceof TabbedPropertyComposite)) {
						tabbedPropertyComposite = tabbedPropertyComposite
								.getParent();
					}
					tabbedPropertyComposite.setFocus();
				}
			}

		}
	}

}
