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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.properties.messages.Messages;
import com.jaspersoft.studio.properties.view.ISection;
import com.jaspersoft.studio.properties.view.TabContents;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetWidgetFactory;


/**
 * Display a search bar into the top of a property sheet page. This bar can be used 
 * to search and highlight a specific property. The bar is obtained with a text with 
 * the autocomplete function. at the right of the text there is a fake button painted
 * using a canvas. A click of this button can manually open the autocomplete dialog, event 
 * if the user is not typing anything.
 * 
 * @author Orlandin Marco
 *
 */
public class TabbedPropertySearch extends Composite {
	
	/**
	 * Text where the name of the property can be typed or selected from the available proposed by the autocomplete
	 */
	private Text textArea;
	
	/**
	 * The page of the property sheet
	 */
	private TabbedPropertySheetPage page;
	
	/**
	 * Factory widget, used to build stuff
	 */
	private TabbedPropertySheetWidgetFactory factory;
	
	/**
	 * The last element selected by the user
	 */
	private Object lastSelectedElement = null;
	
	/**
	 * Keep the properties list of the last selected element, until the selection dosen't change with an element
	 * with a different type. Maybe could be improved by storing all the created properties when they are builded
	 */
	private PropertiesContainer cachedProperties = null;
	
	/**
	 * Color used for the arrow of the fake button
	 */
	private static final Color arrowColor = SWTResourceManager.getColor(0,0,0);
	
	/**
	 * The autocomplete object for the text element
	 */
	private ManualyOpenedAutocomplete autocomplete;
	
	/**
	 * The height of the text element and of the canvas with the arrow painted
	 */
	private int widgetHeight = 18;
	
	/**
	 * Action executed when an element from the autocomplete is selected
	 */
	private IContentProposalListener proposalListener = new IContentProposalListener(){
		@Override
		public void proposalAccepted(IContentProposal proposal) {
			if (proposal instanceof PropertyContentProposal){
				PropertyContentProposal propertyProposal = (PropertyContentProposal)proposal;
				selectElement(propertyProposal.getPropertyId(),propertyProposal.getSectionType());
			}
		}
		
	};
	

	/**
	 * Constructor for TabbedPropertySearch.
	 * 
	 * @param parent the parent composite.
	 * @param page the page where the control is placed
	 */
	public TabbedPropertySearch(Composite parent, TabbedPropertySheetPage page) {
		super(parent, SWT.NO_FOCUS);
		this.page = page;
		this.factory = page.getWidgetFactory();

		this.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				drawTitleBackground(e);
			}
		});

		factory.getColors().initializeSectionToolBarColors();
		setBackground(factory.getColors().getBackground());
		setForeground(factory.getColors().getForeground());

		FormLayout layout = new FormLayout();
		layout.marginWidth = 1;
		layout.marginHeight = 0;
		setLayout(layout);
		
		Composite containerComp = new Composite(this,SWT.BORDER);
		GridLayout containerLayout = new GridLayout(2,false);
		containerLayout.marginWidth = 0;
		containerLayout.marginHeight = 0;
		containerLayout.horizontalSpacing = 0;
		containerLayout.verticalSpacing = 0;
		containerComp.setLayout(containerLayout);
		
		//Create the text area
		createTextArea(containerComp);
		
		//Create the arrow button 
		createFakeButton(containerComp);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		containerComp.setLayoutData(data);
	}
	
	/**
	 * Create the Text area control 
	 * 
	 * @param containerComp  container where the control is placed
	 */
	private void createTextArea(Composite containerComp){
		textArea = new Text(containerComp, SWT.NONE);
		textArea.setForeground(factory.getColors().getColor(IFormColors.TITLE));
		textArea.setText(Messages.TabbedPropertySearch_searchPropertyLabel);
		
		//Focus listener, to populate the combo when it is selected
		textArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textArea.getText().equals(Messages.TabbedPropertySearch_searchPropertyLabel)){
					textArea.setText("");
				}
				updateAutocompleteContent();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (textArea.getText().isEmpty()) 
					textArea.setText(Messages.TabbedPropertySearch_searchPropertyLabel); 
			}
		});
		
		textArea.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				//when all the text is deleted then the autocomplete dialog is opened showing every choice
				if (e.keyCode == SWT.BS && textArea.getText().isEmpty() 
						&& !autocomplete.isProposalOpened()) autocomplete.openProposalPopup();	
				//The down arrow open the autocomplete dialog is opened 
				if (e.keyCode == SWT.ARROW_DOWN && !autocomplete.isProposalOpened()) autocomplete.openProposalPopup();
				//When the return key is pressed the element with the same name of the typed one is selected without open the autocomplete
				if (e.keyCode == SWT.CR && !autocomplete.isProposalOpened() && cachedProperties != null){
					String searchedString = textArea.getText().toLowerCase();
					for(int i=0;i<cachedProperties.getSize();i++){
						PropertyContainer actualContainer = cachedProperties.getPrperties()[i];
						String actualString = actualContainer.getName().toLowerCase();
						if (actualString.equals(searchedString)){
							checkSelection(actualContainer.getId());
							return;
						}
					}
				}
			}
		});
		
		GridData textData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		textData.heightHint = widgetHeight;
		textArea.setLayoutData(textData);
	}
	
	/**
	 * Create a fake arrow button that can be clicked to open or close 
	 * manually the autocomplete dialog
	 * 
	 * @param containerComp container where the button will be placed
	 */
	private void createFakeButton(Composite containerComp){
		Canvas openIcon = new Canvas(containerComp, SWT.NONE);
		openIcon.setBackground(SWTResourceManager.getColor(255,255,255));
		openIcon.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Color oldBackground = e.gc.getBackground();
				e.gc.fillRectangle(0,0,e.width,e.height);
				e.gc.setBackground(arrowColor);
				e.gc.setAntialias(SWT.ON);
				int oddX_offset = e.width % 2 == 0 ? 0 : 1;  
				int y_offset = 7;
				int x_offset = 4;
				int x1 = x_offset-oddX_offset;
				int y1 = y_offset;
				int x2 = e.width-x_offset;
				int y2 = y_offset;
				int x3 = (e.width-oddX_offset)/2;
				int y3 = e.height-y_offset;
				e.gc.fillPolygon(new int[]{x1,y1,x2,y2,x3,y3});
				e.gc.setAntialias(SWT.DEFAULT);
				e.gc.setBackground(oldBackground);
			}
		});
		
		openIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (textArea.getText().equals(Messages.TabbedPropertySearch_searchPropertyLabel)){
					textArea.setText("");
				}
				updateAutocompleteContent();
				if (autocomplete.isProposalOpened()) autocomplete.closeProposalPopup();
				else autocomplete.openProposalPopup();
			}
		});
		GridData iconData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		iconData.widthHint = 15;
		iconData.heightHint = widgetHeight;
		openIcon.setLayoutData(iconData);
				
	}
	
	/**
	 * Method called to initialize or update the autocomplete set of elements
	 */
	private void updateAutocompleteContent(){
		if (autocomplete == null) {
			PropertiesContainer properties = getAllProperties();
			autocomplete = new ManualyOpenedAutocomplete(textArea, new TextContentAdapter(), properties);
			autocomplete.addProposalSelectedListener(proposalListener);
		} else {
			Object actualSelectedElement = getSelectedElement();
			if (lastSelectedElement == null || (actualSelectedElement != null && !actualSelectedElement.getClass().equals(lastSelectedElement.getClass()))){
				PropertiesContainer properties = getAllProperties();
				autocomplete.setProposals(properties);
			}
		}
	}
	
	/**
	 * Return all the properties for the selected element. The property are cached and not 
	 * recalculated until the selection maintain the same type
	 * 
	 * @return a PropertiesContainer with all the properties name and relative ids for the 
	 * selected element type
	 */
	private PropertiesContainer getAllProperties(){
		//Check if i have the properties for the element in the cache
		if (lastSelectedElement == null || cachedProperties == null){
			//I haven't build a cache yet, need to create it
			cachedProperties = createElements();
			lastSelectedElement = getSelectedElement();
		} else {
			//Maybe i have already the element cached
			Object actualSelectedElement = getSelectedElement();
			if (actualSelectedElement == null){
				cachedProperties = new PropertiesContainer();
				lastSelectedElement = null;
			} else if (!actualSelectedElement.getClass().equals(lastSelectedElement.getClass())){
				//The cache was build for an element with different type\properties, i need to rebuild it
				cachedProperties = createElements();
				lastSelectedElement = actualSelectedElement;
			}
		}
		textArea.setText(""); //$NON-NLS-1$
		return cachedProperties;
	}
	
	
	/**
	 * Get the element actually selected
	 */
	private Object getSelectedElement(){
		return page.getSelectedObject();
	}
	
	/**
	 * Create a PropertiesContainer containing all the selectable properties for 
	 * the actually selected element type. The property are also ordered into 
	 * a lexicographic way
	 * 
	 */
	private PropertiesContainer createElements(){
		List<PropertyContainer> listToOrder = new ArrayList<PropertyContainer>();
		List<TabContents> lst = page.getCurrentTabs();
		
		for(TabContents actualContents : lst){
			for(ISection section : actualContents.getSections()){
				if (section instanceof IWidgetsProviderSection){
					IWidgetsProviderSection attributesSection = (IWidgetsProviderSection)section;
					List<Object> providedProperties = attributesSection.getHandledProperties();
					for(Object property : providedProperties){
						WidgetDescriptor desc = attributesSection.getPropertyInfo(property);
						listToOrder.add(new PropertyContainer(desc.getName(), property, attributesSection.getClass()));
					}
				}
			}
		}
		Collections.sort(listToOrder);
		return new PropertiesContainer(listToOrder.toArray(new PropertyContainer[listToOrder.size()]));
	}
	
	
	/**
	 * Method called when there is a selection event. 
	 * 
	 * @param id the id of the element to select
	 */
	private void checkSelection(Object id){
		if (id != null) selectElement(id);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		arrowColor.dispose();
	}

	
	/**
	 * Select the properties in the property sheet page with a specific id
	 * 
	 * @param id the id of the property
	 */
	private void selectElement(Object id){
		List<TabContents> lst = page.getCurrentTabs();
		for(TabContents actualContents : lst){
			for(ISection section : actualContents.getSections()){
				if (section instanceof IWidgetsProviderSection){
					IWidgetsProviderSection actualSection = ((IWidgetsProviderSection)section);
					//search the section that contains the property
					if (actualSection.getHandledProperties().contains(id)){
						//Select the section, it will also create it
						page.setSelection(actualContents);
						//Expand the properties expandable composite, if it is inside one of it
						actualSection.expandForProperty(id);
						//Get the widget from the section and highlight it for 2000ms
						IHighlightPropertyWidget widget = actualSection.getWidgetForProperty(id);
						if (widget != null) {
							Control highLightedControl = widget.getControlToBorder();
							if (highLightedControl != null) highLightedControl.forceFocus();
							widget.highLightWidget(2000);
						}
						return;
					}
				}
			}
		}
	}
	
	/**
	 * Select the properties in the property sheet page with a specific id
	 * 
	 * @param id the id of the property
	 */
	private void selectElement(Object id, Class<?> sectionType){
		if (sectionType == null) {
			checkSelection(id);
			return;
		}
		List<TabContents> lst = page.getCurrentTabs();
		for(TabContents actualContents : lst){
			for(ISection section : actualContents.getSections()){
				if (section instanceof IWidgetsProviderSection){
					IWidgetsProviderSection actualSection = ((IWidgetsProviderSection)section);
					//search the section that contains the property
					if (actualSection.getClass().equals(sectionType) && actualSection.getHandledProperties().contains(id)){
						//Select the section, it will also create it
						page.setSelection(actualContents);
						//Expand the properties expandable composite, if it is inside one of it
						actualSection.expandForProperty(id);
						//Get the widget from the section and highlight it for 2000ms
						IHighlightPropertyWidget widget = actualSection.getWidgetForProperty(id);
						if (widget != null) widget.highLightWidget(2000);
						return;
					}
				}
			}
		}
	}
	
	/**
	 * @param e
	 */
	protected void drawTitleBackground(PaintEvent e) {
		if (factory.getColors() == null) return;
		Rectangle bounds = getClientArea();
		Color bg = factory.getColors().getColor(IFormColors.H_GRADIENT_END);
		Color gbg = factory.getColors().getColor(IFormColors.H_GRADIENT_START);
		GC gc = e.gc;
		gc.setForeground(bg);
		gc.setBackground(gbg);
		gc.fillGradientRectangle(bounds.x, bounds.y, bounds.width,
				bounds.height, true);
		// background bottom separator
		gc.setForeground(factory.getColors().getColor(
				IFormColors.H_BOTTOM_KEYLINE1));
		gc.drawLine(bounds.x, bounds.height - 2, bounds.x + bounds.width - 1,
				bounds.height - 2);
		gc.setForeground(factory.getColors().getColor(
				IFormColors.H_BOTTOM_KEYLINE2));
		gc.drawLine(bounds.x, bounds.height - 1, bounds.x + bounds.width - 1,
				bounds.height - 1);
	}

}
