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
package com.jaspersoft.studio.properties.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.properties.IPropertySheetPage;

import com.jaspersoft.studio.properties.internal.TabbedPropertyComposite;
import com.jaspersoft.studio.properties.internal.TabbedPropertyComposite.TabState;
import com.jaspersoft.studio.properties.internal.TabbedPropertyRegistry;
import com.jaspersoft.studio.properties.internal.TabbedPropertyRegistryFactory;
import com.jaspersoft.studio.properties.internal.TabbedPropertyTitle;
import com.jaspersoft.studio.properties.internal.TabbedPropertyViewer;

/**
 * A property sheet page that provides a tabbed UI. It's is based on the 
 * TabbedPropertySheetPage made by Anthony Hunter inside eclipse, but with some 
 * optimization to be faster with Jaspersoft Studio.
 * 
 * @author Anthony Hunter & Orlandin Marco
 */
public class TabbedPropertySheetPage extends Page implements IPropertySheetPage {

	/**
	 * Composite where the controls are shown
	 */
	private TabbedPropertyComposite tabbedPropertyComposite;

	/**
	 * Utility to create graphical widgets
	 */
	private TabbedPropertySheetWidgetFactory widgetFactory;

	private ITabbedPropertySheetPageContributor contributor;

	private TabbedPropertyRegistry registry;

	/**
	 * The currently active contributor id, which may not match the contributor id
	 * from the workbench part that created this instance.
	 */
	private String currentContributorId;

	/**
	 * The provider of the tab contents to show
	 */
	protected IStructuredContentProvider tabListContentProvider;

	/**
	 * The current selection
	 */
	private ISelection currentSelection;

	/**
	 * The viewer for the tab contents
	 */
	private TabbedPropertyViewer tabbedPropertyViewer;

	/**
	 * The current tab shown
	 */
	private TabContents currentTab;

	/**
	 * Map to cache every tabcontents to its descriptor
	 */
	private Map<ITabDescriptor, TabContents> descriptorToTab;

	/**
	 * Map to cache every tabcontents with the composite where it is shown
	 */
	private Map<TabContents, Composite> tabToComposite;
	
	/**
	 * Map of the last tab selected for each element
	 */
	private Map<Object, String> lastSelectedTabForElement;

	/**
	 * Flag to show or not the title bar
	 */
	private boolean hasTitleBar;

	/**
	 * SelectionChangedListener for the ListViewer.
	 */
	class SelectionChangedListener implements ISelectionChangedListener {

		/**
		 * Shows the tab associated with the selection.
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			if (!tabbedPropertyComposite.isDisposed()) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				
				TabContents tab = null;
				ITabDescriptor descriptor = (ITabDescriptor) selection.getFirstElement();
				if (currentTab != null) currentTab.aboutToBeHidden();
				if (descriptor != null) {
					tab = getTab(descriptor);
					if (tabbedPropertyViewer != null && tabbedPropertyViewer.getInput() != null) {
						// force widgets to be resized
						tab.setInput(tabbedPropertyViewer.getWorkbenchPart(), (ISelection) tabbedPropertyViewer.getInput());
						TabState state = tabbedPropertyComposite.showTabContents(descriptor);
						if (state == TabState.TAB_NOT_DEFINED){
							//Tab not defined, it need to be created
							Composite tabComposite = createTabComposite(descriptor);
							tabToComposite.put(tab, tabComposite);
							tab.createControls(tabComposite, TabbedPropertySheetPage.this);
							tabbedPropertyComposite.showTabContents(descriptor);
						}
						
						// store tab selection
						storeCurrentTabSelection(descriptor);
						tab.refresh();
						currentTab = tab;
						currentTab.aboutToBeShown();
						if (state != TabState.TAB_ALREADY_VISIBLE) {
							//The layout is done only if the tab was not visible
							tabbedPropertyComposite.layout();
							tabbedPropertyComposite.updatePageMinimumSize();
						}
					}
				}
			}
		}
	}
	
	/**
	 * create a new tabbed property sheet page.
	 * 
	 * @param tabbedPropertySheetPageContributor the tabbed property sheet page contributor.
	 * @param showTitleBar  boolean indicating if the title bar should be shown
	 */
	public TabbedPropertySheetPage(ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor, boolean showTitleBar) {
		hasTitleBar = showTitleBar;
		contributor = tabbedPropertySheetPageContributor;
		currentContributorId = contributor.getContributorId();
		tabToComposite = new HashMap<TabContents, Composite>();
		descriptorToTab = new HashMap<ITabDescriptor, TabContents>();
		lastSelectedTabForElement = new HashMap<Object, String>();
		validateRegistry();
	}
	
	/**
	 * create tab from the descriptor if necessary. then the 
	 * tab is cached to make faster the next request
	 * 
	 * @param descriptor a tab descriptor
	 * @return a TabContents created from the descriptor
	 */
	private TabContents getTab(ITabDescriptor descriptor){
		// can not cache based on the id - tabs may have the same id,
		// but different section depending on the selection
		TabContents tab = descriptorToTab.get(descriptor);
		if (tab == null){
			tab = descriptor.createTab();
			descriptorToTab.put(descriptor, tab);
		}
		return tab;
	}

	/**
	 * Take a TabContents and search a TabDescriptor for that tab
	 */
	private ITabDescriptor getTabDescriptor(TabContents tab) {
		for (ITabDescriptor key : descriptorToTab.keySet()) {
			if (descriptorToTab.get(key) == tab)
				return key;
		}
		return null;
	}

	/**
	 * Change the selected tab with the one passed by parameter
	 */
	public void setSelection(TabContents tab) {
		tabbedPropertyViewer.setSelectionToWidget(getTabDescriptor(tab).getId(),0);
	}

	/**
	 * Create the page control
	 * 
	 * @param parent parent of the page
	 */
	public void createControl(Composite parent) {
		widgetFactory = new TabbedPropertySheetWidgetFactory(this);
		tabbedPropertyComposite = new TabbedPropertyComposite(parent, this, hasTitleBar);

		tabbedPropertyComposite.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				/*
				 * Check the page height when the composite area is resized because the
				 * column layout could be changed
				 */
				tabbedPropertyComposite.updatePageMinimumSize();
			}
		});

		PlatformUI.getWorkbench().getHelpSystem().setHelp(tabbedPropertyComposite, "com.jaspersoft.studio.doc.view_properties");

		widgetFactory.paintBordersFor(tabbedPropertyComposite);
		tabbedPropertyComposite.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		tabbedPropertyComposite.setLayoutData(formData);

		tabbedPropertyViewer = new TabbedPropertyViewer(tabbedPropertyComposite.getList());
		tabbedPropertyViewer.setContentProvider(tabListContentProvider);
		tabbedPropertyViewer.addSelectionChangedListener(new SelectionChangedListener());

		
		if (hasTitleBar && registry == null) {
			initContributor(currentContributorId);
		}
	}

	public TabbedPropertyComposite getTabbedPropertyComposite() {
		return tabbedPropertyComposite;
	}

	/**
	 * Initialize the contributor with the provided contributor id.
	 * 
	 * @param contributorId the contributor id.
	 */
	private void initContributor(String contributorId) {
		registry = TabbedPropertyRegistryFactory.getInstance().createRegistry(contributor);
		tabListContentProvider = getTabListContentProvider();
		hasTitleBar = hasTitleBar && registry.getLabelProvider() != null;
		if (tabbedPropertyViewer != null) {
			tabbedPropertyViewer.setContentProvider(tabListContentProvider);
		}
	}

	/**
	 * Gets the tab list content provider for the contributor.
	 * 
	 * @return the tab list content provider for the contributor.
	 */
	protected IStructuredContentProvider getTabListContentProvider() {
		return registry.getTabListContentProvider();
	}

	/**
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		if (widgetFactory != null) {
			widgetFactory.dispose();
			widgetFactory = null;
		}
		

		if (registry != null) {
			TabbedPropertyRegistryFactory.getInstance().disposeRegistry(contributor);
			registry = null;
		}
		

		contributor = null;
		currentContributorId = null;
		currentSelection = null;
		disposeTabs();
	}
	
	/**
	 * Dispose all the tabs and their controls
	 */
	private void disposeTabs() {
		for (Entry<TabContents, Composite> pair : tabToComposite.entrySet()) {
			Composite composite = pair.getValue();
			pair.getKey().dispose();
			if (composite != null) {
				composite.dispose();
			}
		}
		tabToComposite.clear();
		descriptorToTab.clear();
	}


	/**
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() {
		return tabbedPropertyComposite;
	}

	/**
	 * @see org.eclipse.ui.part.IPage#setActionBars(org.eclipse.ui.IActionBars)
	 */
	public void setActionBars(IActionBars actionBars) {
		// Override the undo and redo global action handlers
		// to use the contributor action handlers
		IActionBars partActionBars = null;
		if (contributor instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart) contributor;
			partActionBars = editorPart.getEditorSite().getActionBars();
		} else if (contributor instanceof IViewPart) {
			IViewPart viewPart = (IViewPart) contributor;
			partActionBars = viewPart.getViewSite().getActionBars();
		}

		if (partActionBars != null) {
			IAction action = partActionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
			if (action != null) {
				actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), action);
			}
			action = partActionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
			if (action != null) {
				actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), action);
			}
		}
	}

	/**
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() {
		getControl().setFocus();
	}

	/**
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!(selection instanceof IStructuredSelection))
			selection = new StructuredSelection();
		setInput(part, selection);
	}

	/**
	 * Stores the current tab label in the selection queue. Tab labels are used to
	 * carry the tab context from one input object to another. The queue specifies
	 * the selection priority. So if the first tab in the queue is not available
	 * for the input we try the second tab and so on. If none of the tabs are
	 * available we default to the first tab available for the input.
	 */
	private void storeCurrentTabSelection(ITabDescriptor tab) {
		lastSelectedTabForElement.put(getSelectedObject(), tab.getId());
	}

	/**
	 * Return the first element of the current selection or null if it is not available. 
	 * 
	 * @return the first element of the selection or null if it's empty or not a structured selection
	 */
	public Object getSelectedObject(){
		if (currentSelection instanceof IStructuredSelection) return ((IStructuredSelection)currentSelection).getFirstElement();
		else return null;
	}


	/**
	 * Helper method for creating property tab composites.
	 * 
	 * @return the property tab composite.
	 */
	private Composite createTabComposite(ITabDescriptor tab) {
		if (tab.getSectionDescriptors().size()>1){
			Composite result = widgetFactory.createComposite(tabbedPropertyComposite.createTabContents(tab), SWT.NO_FOCUS);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			result.setLayout(layout);
			result.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			return result;
		} else {
			return tabbedPropertyComposite.createTabContents(tab);
		}
	}
	

	private void setInput(IWorkbenchPart part, ISelection selection) {
		if (selection.equals(currentSelection)) {
			return;
		}
		this.currentSelection = selection;	

		// update tabs list
		tabbedPropertyViewer.setInput(part, currentSelection);
		String selectedTabIndex = lastSelectedTabForElement.get(getSelectedObject());
		if (tabbedPropertyViewer.getElements().size()>0){
			tabbedPropertyComposite.showEmptyPage(false);
			tabbedPropertyViewer.setSelectionToWidget(selectedTabIndex, 0);
			refreshTitleBar();
		} else {
			tabbedPropertyComposite.showEmptyPage(true);
		}
	}

	/**
	 * Get the currently active tab.
	 * 
	 * @return the currently active tab.
	 * @since 3.4
	 */
	public TabContents getCurrentTab() {
		return currentTab;
	}


	/**
	 * Get the widget factory.
	 * 
	 * @return the widget factory.
	 */
	public TabbedPropertySheetWidgetFactory getWidgetFactory() {
		return widgetFactory;
	}

	/**
	 * Update the title bar of the contributor has a label provider.
	 */
	private void refreshTitleBar() {
		if (hasTitleBar && registry != null) {
			TabbedPropertyTitle title = tabbedPropertyComposite.getTitle();
			if (currentTab == null) {
				/**
				 * No tabs are shown so hide the title bar, otherwise you see
				 * "No properties available" and a title bar for the selection.
				 */
				title.setTitle(null, null);
			} else {
				String text = registry.getLabelProvider().getText(currentSelection);
				Image image = registry.getLabelProvider().getImage(currentSelection);
				title.setTitle(text, image);
			}
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		refreshTitleBar();
	}


	/**
	 * The workbench part creates this instance of the TabbedPropertySheetPage and
	 * implements ITabbedPropertySheetPageContributor which is unique contributor
	 * id. This unique contributor id is used to load a registry with the
	 * extension point This id matches the registry.
	 * <p>
	 * It is possible for elements in a selection to implement
	 * ITabbedPropertySheetPageContributor to provide a different contributor id
	 * and thus a differenent registry.
	 * 
	 * @param selection
	 *          the current selection in the active workbench part.
	 */
	private void validateRegistry() {
		/**
		 * All the elements in the selection implement a new contributor id, so use
		 * that id.
		 */
		initContributor(currentContributorId);
		overrideActionBars();
	}

	/**
	 * Override the action bars for the selection based contributor.
	 */
	private void overrideActionBars() {
		if (registry.getActionProvider() != null) {
			IActionProvider actionProvider = registry.getActionProvider();
			actionProvider.setActionBars(contributor, getSite().getActionBars());
		}
	}

	/**
	 * Return a list of all the available TabContents actually visible
	 */
	public List<TabContents> getCurrentTabs() {
		List<TabContents> result = new ArrayList<TabContents>();
		for(ITabDescriptor desc : tabbedPropertyViewer.getElements()){
			result.add(getTab(desc));
		}
		return result;
	}
}
