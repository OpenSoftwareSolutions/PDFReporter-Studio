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
package com.jaspersoft.studio.property.infoList;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * create a scrollable composite that emulate a list of elements. the elements can be selected and an action can be
 * associated with the double click. every element is a composite with inside a label for the title and a styled text
 * for the description
 * 
 * @author Orlandin Marco
 * 
 */
public class SelectableComposite extends ScrolledComposite {

	/**
	 * list of elements displayed
	 */
	private List<ElementDescription> items;

	/**
	 * composite of the selected element
	 */
	private Composite selectedComposite;
	
	/**
	 * List of the clickable composites associated with each ElementDescriptor
	 */
	private List<Composite> compositeList;

	/**
	 * color used as background for the unselected elements
	 */
	private Color unselectedColor = SWTResourceManager.getColor(255, 255, 225);

	/**
	 * color used as background for the selected element
	 */
	private Color selectedColor = SWTResourceManager.getColor(51, 153, 255);

	/**
	 * adapter used to define what action must be done when the user double click an element
	 */
	private SelectionAdapter mouseDoubleClick;

	/**
	 * listener that handle the selection and the double click on a list element composite
	 */
	private MouseListener compositeMouseAction = new MouseListener() {

		@Override
		public void mouseUp(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
			widgetSelected(e.getSource());
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			widgetSelected(e.getSource());
			if (mouseDoubleClick != null) {
				Event baseEvent = new Event();
				baseEvent.data = selectedComposite.getData();
				baseEvent.item = selectedComposite;
				baseEvent.widget = e.widget;
				SelectionEvent event = new SelectionEvent(baseEvent);
				mouseDoubleClick.widgetSelected(event);
			} else
				widgetSelected(((Control) e.getSource()).getParent());
			setScrolledFocus();
		}
	};

	private void widgetSelected(Object source) {
		if (source.getClass().equals(Composite.class)) {
			if (source != selectedComposite) {
				if (selectedComposite != null) {
					selectedComposite.setBackground(unselectedColor);
					setChildrenColor(selectedComposite, unselectedColor);
				}
				selectedComposite = (Composite) source;
				selectedComposite.setBackground(selectedColor);
				setChildrenColor(selectedComposite, selectedColor);
				setScrolledFocus();
			}
		} else
			widgetSelected(((Control) source).getParent());
	}
	
	/**
	 * Listener to move the selected element up and down when the up and 
	 * down keys are pressed
	 */
	private Listener arrowListener = new Listener() {
		
		/**
		 * Return the index of the actually selected element (there
		 * must be a selected element or it will return the first element)
		 */
		private int getActualSelectionIndex(){
			int index = 0;
			if (selectedComposite == null) return 0;
			for(ElementDescription item : items){
				if (selectedComposite.getData() == item){
					break;
				} else {
					index++;
				}
			}
			return index;
		}
		
		@Override
		public void handleEvent(Event event) {
			if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN){
				int selectionIndex = getActualSelectionIndex();
				int newSelectionIndex = -1;
				if (event.keyCode == SWT.ARROW_UP && selectionIndex > 0 ){
					newSelectionIndex = selectionIndex-1;
				} else if (event.keyCode == SWT.ARROW_DOWN && selectionIndex < (items.size()-1)){
					newSelectionIndex =  selectionIndex+1;
				}
				if (newSelectionIndex != -1){
					ElementDescription elementToSelect = items.get(newSelectionIndex);
					//Search the next element
					for(Composite comp : compositeList){
						if (comp.getData() == elementToSelect){
							//Element found, deselect the old one if any
							if (selectedComposite != null) {
								selectedComposite.setBackground(unselectedColor);
								setChildrenColor(selectedComposite, unselectedColor);
							}
							//Select the new element and exit from the for
							selectedComposite = comp;
							showControl(selectedComposite);
							selectedComposite.setBackground(selectedColor);
							setChildrenColor(selectedComposite, selectedColor);
							setScrolledFocus();
							break;
						}
					}
				}
			}
		}
	};

	public SelectableComposite(Composite parent) {
		super(parent, SWT.V_SCROLL);
		items = null;
		setLayout(new GridLayout(1, false));
		selectedComposite = null;
		mouseDoubleClick = null;
		this.getVerticalBar().setIncrement(5);
	}

	/**
	 * Return if the content of the composite has already been set
	 * 
	 * @return true if the content of the composite has been set, false otherwise
	 */
	public boolean isItemSetted() {
		return items != null;
	}

	/**
	 * set the content of the composite
	 * 
	 * @param itemsthe
	 *          list of elements to display
	 */
	public void setItems(List<ElementDescription> items) {
		this.items = items;
		createItems();
	}

	/**
	 * set the listener called when the user double click an element of the list. inside the data of the event you will
	 * find the ElementDescription that has generated the double clicked element of the list
	 * 
	 * @param listener
	 */
	public void SetDoubleClickListener(SelectionAdapter listener) {
		this.mouseDoubleClick = listener;
	}

	/**
	 * return the actually selected element
	 * 
	 * @return the ElementDescription of the actually selected element or null if none
	 */
	public ElementDescription getSelectedElement() {
		if (selectedComposite != null)
			return (ElementDescription) selectedComposite.getData();
		return null;
	}

	/**
	 * Force the focus on the scrollable composite
	 */
	private void setScrolledFocus() {
		this.forceFocus();
	}

	/**
	 * set the background of a composite and of all its children
	 * 
	 * @param parent
	 * @param color
	 */
	private void setChildrenColor(Composite parent, Color color) {
		for (Control child : parent.getChildren()) {
			child.setBackground(color);
		}
	}

	private void createItems() {
		final Composite mainComposite = new Composite(this, SWT.NONE);
		GridLayout mainCompLayout = new GridLayout(1, false);
		mainCompLayout.verticalSpacing = 0;
		mainCompLayout.horizontalSpacing = 0;
		mainCompLayout.marginWidth = 0;
		mainCompLayout.marginHeight = 0;
		mainComposite.setLayout(mainCompLayout);
		setContent(mainComposite);
		mainComposite.setRedraw(false);
		compositeList = new ArrayList<Composite>();
		for (ElementDescription item : items) {
			Composite comp = new Composite(mainComposite, SWT.BORDER);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			GridLayout containerLayout = new GridLayout(1, false);

			comp.setLayout(containerLayout);
			comp.setData(item);
			comp.setBackground(unselectedColor);

			Label titleLabel = new Label(comp, SWT.NONE);
			titleLabel.setFont(SWTResourceManager.getBoldFont(titleLabel.getFont()));

			titleLabel.setText(item.getName());
			titleLabel.addMouseListener(compositeMouseAction);

			createDescription(item.getDescription(), comp, item.getStyles());

			comp.addMouseListener(compositeMouseAction);
			setChildrenColor(comp, unselectedColor);
			compositeList.add(comp);
		}
		mainComposite.setRedraw(true);
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Point size = getSize();
				mainComposite.setSize(mainComposite.computeSize(size.x, SWT.DEFAULT));
			}

		});
		
		PlatformUI.getWorkbench().getDisplay().addFilter(org.eclipse.swt.SWT.KeyDown, arrowListener);
	}

	private void createDescription(String text, Composite comp, StyleRange[] styles) {
		final StyledText descLabel = new StyledText(comp, SWT.MULTI | SWT.WRAP);
		descLabel.setRightMargin(10);
		descLabel.setEditable(false);
		descLabel.setText(text);
		descLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		descLabel.addMouseListener(compositeMouseAction);
		if (styles != null && styles.length > 0)
			descLabel.setStyleRanges(styles);
		descLabel.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				setScrolledFocus();
				descLabel.setSelection(0, 0);
			}

		});
	}

	@Override
	public void dispose() {
		super.dispose();
		PlatformUI.getWorkbench().getDisplay().removeFilter(org.eclipse.swt.SWT.KeyDown, arrowListener);
		selectedColor.dispose();
		unselectedColor.dispose();
	}

}
