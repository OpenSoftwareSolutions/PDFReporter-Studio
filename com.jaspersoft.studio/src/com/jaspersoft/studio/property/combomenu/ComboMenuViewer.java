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
package com.jaspersoft.studio.property.combomenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jaspersoft.studio.help.HelpSystem;

/**
 * Class that manage the Combo Popup, create the popup manu and execture the action. the combo popup want to imitate a
 * combobox where a series of element are listed, but the selection of one item is done using a popup menu, opened by a
 * left click on the combobox
 * 
 * @author Orlandin Marco
 * 
 */
public class ComboMenuViewer implements IMenuProvider {

	/**
	 * Style bit: Create handle control and drop-down widget with default behaviours, i.e. showing text, showing image,
	 * using menu as drop-down widget.
	 */
	public static final int NORMAL = ComboButton.NORMAL;

	/**
	 * Style bit: Don't show text.
	 */
	public static final int NO_TEXT = ComboButton.NO_TEXT;

	/**
	 * Style bit: Don't show image.
	 */
	public static final int NO_IMAGE = ComboButton.NO_IMAGE;

	/**
	 * List of registered actions that will be executed when an element is selected
	 */
	private List<ComboItemAction> listeners;

	/**
	 * Button that made the popup menu appears
	 */
	private ComboButton dropDownHandle;

	/**
	 * List of the items inside the popup menu
	 */
	private List<ComboItem> elementList = new ArrayList<ComboItem>();

	/**
	 * The popup menu
	 */
	private Menu popupMenu = null;

	/**
	 * Last element selected in the menu
	 */
	private ComboItem selectedItem = null;
	
	/**
	 * Flag used to disable the update of the image and of the text inside the combo button. This 
	 * is done to make the selection happen but not to show it in the button, and assigning it a static
	 * Appearance
	 */
	private boolean disableSelectedItemUpdate = false;
	
	/**
	 * Disable the emphasis effect on the last selected item when set to true
	 */
	private boolean disableSelectedItemEmphasis = false;

	/**
	 * Represent the action associated to every element of the menu, so it represent an entry in the menu
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	private class ComboAction extends SelectionAdapter {
		/**
		 * element that this entry represent
		 */
		private ComboItem item;

		/**
		 * Text value of this item
		 */
		private String name;

		/**
		 * Listener for this item
		 */
		private List<ComboItemAction> listeners;

		/**
		 * Image of the item
		 */
		private Image descriptor;

		/**
		 * Create a new entry for the menu
		 * 
		 * @param name
		 *          Name of the entry
		 * @param style
		 *          Style of the entry
		 * @param item
		 *          element that this entry represent
		 * @param Image
		 *          of the item
		 */
		public ComboAction(String name, List<ComboItemAction> listeners, ComboItem item, Image descriptor) {
			this.name = name;
			this.listeners = listeners;
			this.item = item;
			this.descriptor = descriptor;
		}

		/**
		 * Return the image for this item
		 * 
		 * @return image for the item, could be null
		 */
		public Image getImageDescriptor() {
			return descriptor;
		}

		/**
		 * Text value for this item
		 * 
		 * @return
		 */
		public String getText() {
			return name;
		}

		/**
		 * Return the element associated to this entry
		 * 
		 * @return
		 */
		public ComboItem getItem() {
			return item;
		}

		/**
		 * Selection event
		 */
		public void widgetSelected(SelectionEvent event) {
			if (!disableSelectedItemUpdate){
				dropDownHandle.setText(getText());
				dropDownHandle.setImage(getImageDescriptor());
			}
			selectedItem = getItem();
			for (ComboItemAction listener : this.listeners) {
				listener.exec();
			}
		}

	}

	/**
	 * Constructs a new instance of this class given its parent and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *          a composite control which will be the parent of the new instance (cannot be null)
	 * @param style
	 *          the style of control to construct
	 * 
	 * @see #NORNAL
	 * @see #NO_TEXT
	 * @see #NO_IMAGE
	 * @see #FILTERED
	 */
	public ComboMenuViewer(Composite parent, int style, String biggerString) {
		dropDownHandle = new ComboButton(parent, style, biggerString, this);
		listeners = new ArrayList<ComboItemAction>();
		dropDownHandle.addOpenListener(new IOpenListener() {
			public void open(OpenEvent event) {
				openPopup();
			}
		});
	}
	
	/**
	 * Used to disable the update of the image and of the text inside the combo button. This 
	 * is done to make the selection happen but not to show it in the button, and assigning it a static
	 * Appearance
	 * 
	 * @param value true if the refresh of the button appearance should be disabled on selection, otherwise true. The
	 * default value is false
	 */
	public void disableSelectedItemUpdate(boolean value){
		disableSelectedItemUpdate = value;
	}

	/**
	 * Disable the emphasis effect on the last selected item
	 * 
	 * @param value true if the emphasis on the last selected element should be disabled, otherwise false. By
	 * default it is false
	 */
	public void disableSelectedEmphasis(boolean value){
		disableSelectedItemEmphasis = value;
	}
	
	/**
	 * Add a new action to execute when an element form the popup menu is selected
	 * 
	 * @param listener
	 *          the action to execute when the element is selected
	 */
	public void addSelectionListener(ComboItemAction listener) {
		listeners.add(listener);
	}

	/**
	 * Set the tooltip text of the combo box
	 * 
	 * @param text
	 *          the text
	 */
	public void setToolTipText(String text) {
		dropDownHandle.getControl().setToolTipText(text);
	}

	/**
	 * Used to check if the popup menu has element inside it
	 * 
	 * @return true if there are entry, otherwise false
	 */
	protected boolean hasNoElement() {
		return elementList.isEmpty();
	}

	/**
	 * Return the number of entry in the popup menu
	 * 
	 * @return number of entry
	 */
	protected int getItemCount() {
		return elementList.size();
	}

	public int getWidth() {
		return dropDownHandle.getWidth();
	}

	/**
	 * Return the index of the selected item in the combo
	 * 
	 * @return
	 */
	public int getSelectionIndex() {
		if (hasNoElement())
			return 0;
		return indexForElement(selectedItem);
	}

	/**
	 * Return the value of the selected item
	 * 
	 * @return the value type depends on how the ComboItem that define this voice is defined
	 */
	public Object getSelectionValue() {
		if (hasNoElement())
			return null;
		return getSelectedItem().getValue();
	}

	/**
	 * Return the item at a specific position
	 * 
	 * @param position
	 * @return
	 */
	public ComboItem getItemAtPosition(int position) {
		return elementList.get(position);
	}

	/**
	 * Return the last item selected
	 * 
	 * @return
	 */
	public ComboItem getSelectedItem() {
		return selectedItem;
	}

	/**
	 * Return the index in the list of a specific item
	 * 
	 * @param element
	 *          the item
	 * @return the index of the item
	 */
	protected int indexForElement(ComboItem element) {
		return elementList.indexOf(element);
	}

	/**
	 * Close the popupmenu when the input change
	 * 
	 * @param input
	 * @param oldInput
	 */
	protected void inputChanged(Object input, Object oldInput) {
		closePopup();
	}

	/**
	 * Return the control of the combobox
	 * 
	 * @return A reference to the combobox control
	 */
	public Control getControl() {
		return dropDownHandle.getControl();
	}

	/**
	 * Open the popup menu from a menu manager instantiated
	 */
	protected void openPopup() {
		openPopupMenu(getPopup());
	}

	/**
	 * Return the previous created popup menu, if it was never created before then it will be created and returned
	 * 
	 * @return a menu
	 */
	protected Menu getPopup() {
		if (popupMenu == null) {
			popupMenu = createPopupMenu();
		}
		return popupMenu;
	}

	/**
	 * Open the popoup menu inside the menumanger and place it under the combobox
	 * 
	 * @param menuManager
	 */
	protected void openPopupMenu(Menu menu) {
		if (menu != null && !menu.isDisposed()) {
			if (menu.isVisible()) {
				menu.setVisible(false);
			} else {
				locatePopupMenu(menu);
				setSelectionToMenu(menu);
				menu.setVisible(true);
			}
		}
	}

	/**
	 * Create a new menumanger
	 * 
	 * @return
	 */
	protected Menu createPopupMenu() {
		Menu newMenu = new Menu(getControl());
		// When the menu is hidden i remove the focus from it, so the hover events continue to work
		newMenu.addMenuListener(new MenuListener() {

			@Override
			public void menuShown(MenuEvent e) {
			}

			@Override
			public void menuHidden(MenuEvent e) {
				dropDownHandle.getControl().getParent().getParent().setFocus();
			}
		});
		refreshPopupMenu(newMenu);
		return newMenu;
	}

	/**
	 * Set the elements inside the menu manager
	 * 
	 * @param newItems
	 *          an array of element
	 */
	public void setItems(ComboItem[] newItems) {
		elementList = new ArrayList<ComboItem>(Arrays.asList(newItems));
		createPopupMenu();
	}

	/**
	 * Set the elements inside the menu manager
	 * 
	 * @param newItems
	 *          a list of element
	 */
	public void setItems(List<ComboItem> newItems) {
		elementList = newItems;
		createPopupMenu();
	}

	/**
	 * Set the contextual help for this control. The help will be set in the button and also in the contextual menu
	 * 
	 * @param href
	 *          uri to open when the help is requested
	 */
	public void setHelp(String href) {
		HelpSystem.setHelp(dropDownHandle.getControl(), href);
		HelpProvider provider = new HelpProvider(getPopup());
		provider.setHelp(href);
	}

	/**
	 * Refresh the popup menu deleting the old entry and creating the updated one
	 * 
	 * @param menuManager
	 */
	protected void refreshPopupMenu(Menu newMenu) {
		// menuManager.removeAll();
		// The elements will be sorted in ascending order
		Collections.sort(elementList, new Comparator<ComboItem>() {
			@Override
			public int compare(ComboItem o1, ComboItem o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		// Add the new elements
		for (ComboItem element : elementList) {
			if (element.isSeparator()) {
				new MenuItem(newMenu, SWT.SEPARATOR);
			} else {
				MenuItem item = new MenuItem(newMenu, SWT.PUSH);
				String text = element.getText();
				item.setText(text);
				item.setImage(element.getImage());
				ComboAction action = new ComboAction(text, new ArrayList<ComboItemAction>(listeners), element,
						element.getImage());
				item.addSelectionListener(action);
			}
		}
		setSelectionToMenu(newMenu);
	}

	/**
	 * Set the menu in the right location, under the combobox
	 * 
	 * @param menu
	 */
	protected void locatePopupMenu(Menu menu) {
		Rectangle r;
		if (getControl() instanceof Composite) {
			r = ((Composite) getControl()).getClientArea();
		} else {
			r = getControl().getBounds();
			r.x = r.y = 0;
		}
		Point loc = getControl().toDisplay(r.x, r.y);
		loc.y += r.height;
		menu.setLocation(loc);
	}

	/**
	 * Close the popoup menu
	 */
	protected void closePopup() {
		if (popupMenu != null) {
			if (popupMenu != null && !popupMenu.isDisposed()) {
				popupMenu.setVisible(false);
			}
		}
	}

	/**
	 * Set the actual item selected in the menu (the selected item has a bold like text)
	 * 
	 * @param menuManager
	 */
	protected void setSelectionToMenu(Menu menu) {
		if (!disableSelectedItemEmphasis){
			int index = getSelectionIndex();
			if (menu != null && !menu.isDisposed()) {
				if (index < 0 || index >= menu.getItemCount()) {
					menu.setDefaultItem(null);
				} else {
					menu.setDefaultItem(menu.getItem(index));
				}
			}
		}
	}

	/**
	 * Set as selected the element with a specific index
	 * 
	 * @param index
	 *          the index of the element to select
	 */
	public void select(int index) {
		if (index >= 0 && index < elementList.size()) {
			selectedItem = elementList.get(index);
			dropDownHandle.setText(selectedItem.getText());
			dropDownHandle.setImage(selectedItem.getImage());
		}
	}
	
	/**
	 * Show in the button the image and the text of a specific item
	 * 
	 * @param item item from where the image and the text to show in the button are taken.
	 * Can not be null and it can be an element external to the items list.
	 */
	public void select(ComboItem item) {
		dropDownHandle.setText(item.getText());
		dropDownHandle.setImage(item.getImage());
	}

	/**
	 * Set only the text of the selected item
	 * 
	 * @param text
	 */
	public void setText(String text) {
		dropDownHandle.setText(text);
	}

	/**
	 * Dispose the element
	 * 
	 * @param event
	 */
	protected void handleDispose(DisposeEvent event) {
		closePopup();
		if (popupMenu != null) {
			popupMenu.dispose();
			popupMenu = null;
		}
	}

	/**
	 * Set the combo enabled or disabled
	 * 
	 * @param enabled
	 *          true if enabled, otherwise false
	 */
	public void setEnabled(boolean enabled) {
		dropDownHandle.setEnabled(enabled);
	}

	/**
	 * Check if the combobox is enabled
	 * 
	 * @return true if it is enabled, otherwise false
	 */
	public boolean isEnabled() {
		return dropDownHandle.isEnabled();
	}

	/**
	 * Check if the menu is visible
	 * 
	 * @return true if the popup menu is visible, otherwise false
	 */
	public boolean isDropDownVisible() {
		return popupMenu != null && !popupMenu.isDisposed() && popupMenu.isVisible();
	}

	@Override
	public Menu getMenu() {
		return getPopup();
	}

}
