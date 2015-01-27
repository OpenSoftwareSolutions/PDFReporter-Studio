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
package com.jaspersoft.studio.toolbars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.SubCoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IActionBars2;
import org.eclipse.ui.IEditorPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.toolitems.ToolItemsSet;

/**
 * 
 * This class is the base to extend to create a toolbar custom control and offers
 * also the static method to update the eclipse toolbar when the selection changes
 * 
 * @author Orlandin Marco
 *
 */
public abstract class CommonToolbarHandler extends ContributionItem {
	
	/**
	 * The custom controls are dynamically loaded starting from the class specified in the 
	 * extension point. This map bind a control id to its class, to load them once
	 */
	private static HashMap<String, CommonToolbarHandler> cachedContributionElements = new HashMap<String, CommonToolbarHandler>();
	
	/**
	 * The main control created inside the toolbar
	 */
	private ToolItem mainControl = null;

	/**
	 * The editor where the elements are selected
	 */
	private IEditorPart workbenchPart = null;
	
	
	/**
	 * Method to call when the selection changes. It will update the current selection and 
	 * refresh the toolbar removing the necessary control and adding the missing one.
	 * The method is visible is used to know if a control should be visible or not
	 * 
	 * @param activeEditor the active editor where the selection is done
	 * @param selection the new selection
	 * @param bars the current action bars, where the new controls will be placed
	 */
	public static void updateSelection(final IEditorPart activeEditor, final IActionBars bars){
		//Executed inside a thread to have more responsive ui
		UIUtils.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if (bars instanceof IActionBars2 && ((IActionBars2) bars).getCoolBarManager() instanceof SubCoolBarManager) {
					ICoolBarManager cbm = (ICoolBarManager) ((SubCoolBarManager) ((IActionBars2) bars).getCoolBarManager()).getParent();
					for(ToolItemsSet toolbar : JaspersoftStudioPlugin.getToolItemsManager().getSets()){
						//if no item of a toolbar contribution is visible then all the toolbar is removed
						boolean isToolbarVisible = false;
						List<CommonToolbarHandler> visibleControls = new ArrayList<CommonToolbarHandler>();
						List<IContributionItem> notVisibleControls = new ArrayList<IContributionItem>();
						for(IConfigurationElement control : toolbar.getControlsConfiguration()){
							CommonToolbarHandler citem = createContributionItem(control);
							citem.setWorkbenchPart(activeEditor);
							if (citem.isVisible()){
								visibleControls.add(citem);
								isToolbarVisible = true;
							} else {
								notVisibleControls.add(citem);
							}
						}
						if (!isToolbarVisible){
							removeToolbar(cbm, toolbar.getId());
						} else {
							removeToolbarContribution(cbm, toolbar.getId(), notVisibleControls);
							addContributionsToCoolbar(cbm, toolbar.getId(), visibleControls);
						}
					}
					cbm.update(true);
					bars.updateActionBars();
				}
			}
		});

	}

	
	/**
	 * Returns the editor's command stack. This is done by asking the workbench part for its CommandStack via
	 * {@link org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)}.
	 * 
	 * @return the command stack
	 */
	protected CommandStack getCommandStack() {
		return (CommandStack) workbenchPart.getAdapter(CommandStack.class);
	}
	
	
	protected List<Object> getSelectionForType(Class<?> type){
		return ((CachedSelectionProvider)workbenchPart).getSelectionCache().getSelectionModelForType(type);
	}
	
	public ISelection getLastRawSelection(){
		return ((CachedSelectionProvider)workbenchPart).getSelectionCache().getLastRawSelection();
	}
	
	/**
	 * Add a list of controls to a toolbar
	 * 
	 * @param cbm2 the toolbar manager
	 * @param toolBarID the toolbar id where the control will be added
	 * @param elementsToAdd the list of the element to add
	 * @param part the current editor part, that will be stored inside the elements 
	 */
	private static void addContributionsToCoolbar(ICoolBarManager cbm2, String toolBarID,  List<CommonToolbarHandler> elementsToAdd) {
		//Get toolbar will build a bar with the reuqested if if it isn't already present
		IContributionItem item = getToolbarContributionItem(cbm2, toolBarID);
		if (item != null) {
			ToolBarContributionItem tbitem = (ToolBarContributionItem) item;
			IToolBarManager tbmanager = tbitem.getToolBarManager();
			if (tbitem.getToolBarManager() !=null){
				for(CommonToolbarHandler elementToAdd : elementsToAdd){
					String id = elementToAdd.getId();
					if (id != null && !isElementPresentInToolbar(tbitem, elementToAdd)){
						tbmanager.add(elementToAdd);
					}
				}
				tbmanager.update(true);
			}
		}
	}
	
	/**
	 * Check if a control for the toolbar is already inside the passed toolbar. The presence is verified
	 * using the element id
	 * 
	 * @param toolbar toolbar where the element is searched
	 * @param itemToCheck the searched element
	 * @return true if an element with the same id of itemToCheck is already present in toolbar, otherwise false
	 */
	private static boolean isElementPresentInToolbar(ToolBarContributionItem toolbar, CommonToolbarHandler itemToCheck){
		for(IContributionItem item : toolbar.getToolBarManager().getItems()){
			if (item.getId().equals(itemToCheck.getId())) {
				if (!item.getClass().equals(itemToCheck.getClass())){
					toolbar.getToolBarManager().remove(item);
					item.dispose();
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Build a control for the toolbar starting form the definition inside it's extension point
	 * 
	 * @param element element definition in the plugin
	 * @return the builded element if it could be build, if there are exception this return null
	 */
	private static CommonToolbarHandler createContributionItem(IConfigurationElement element) {	
		try {
			String id = element.getAttribute("id");
			CommonToolbarHandler handler = cachedContributionElements.get(id);
			if (handler == null && id != null){
				Object newObject = element.createExecutableExtension("class");
				handler = (CommonToolbarHandler)newObject;
				//Align the ID of the object with the id of it's configuration element
				handler.setId(id);
				cachedContributionElements.put(id, handler);
			}
			return handler;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * Return a toolbar inside the manager with a specific id. If the toolbar could not be found
	 * then it is builded and placed inside the manager, then it is returned
	 * 
	 * @param coolbar the toolbar manager
	 * @param toolbarId the searched toolbar id
	 * @return a toolbar with the specified id
	 */
	private static IContributionItem getToolbarContributionItem(ICoolBarManager coolbar, String toolbarId) {
		IContributionItem item = findToolbar(coolbar, toolbarId);
		if (item == null) {
			item = new ToolBarContributionItem(new ToolBarManager(), toolbarId);
			coolbar.appendToGroup("group.editor", item);
		}
		return item;
	}
	
	/**
	 * Search a toolbar with a specified id inside the manager
	 * 
	 * @param cbm2 the toolbar manager 
	 * @param tbarid the id of the searched toolbar
	 * @return the toolbar if it is found inside the manager, otherwise null
	 */
	public static IContributionItem findToolbar(ICoolBarManager cbm2, String tbarid) {
		for (IContributionItem ci : cbm2.getItems()) {
			if (ci.getId().equals(tbarid))
				return ci;
		}
		return null;
	}
	
	/**
	 * Remove a toolbar from the manager. It dosen't do nothing if the toolbar is not in the manager.
	 * If the toolbar is found before to be removed all the controls inside are disposed. The parent
	 * toolbar of the item is not removed to have it's position preserved if other elements will
	 * be added to it with a following selection. However since it's empty it isn't visible.
	 * This method it is equivalent to removeToolbarContribution with all the content of the toolbar
	 * inside the items to remove list. But since this need to done less control it has better performance
	 * and it is preferred when we just want to remove all the content of the toolbar
	 * 
	 * @param cbm2 the toolbar manager
	 * @param tbarid the id of the toolbar to remove
	 */
	public static void removeToolbar(ICoolBarManager cbm2, String tbarid) {
		IContributionItem ictb = findToolbar(cbm2, tbarid);
		if (ictb instanceof ToolBarContributionItem) {
			IToolBarManager tbmanager = ((ToolBarContributionItem) ictb).getToolBarManager();
			if (tbmanager != null){
				//the controls inside the toolbar are disposed
				for (IContributionItem ci : tbmanager.getItems()) {
					tbmanager.remove(ci);
					ci.dispose();
				}
				tbmanager.update(true);
			}
		}
	}
	
	/**
	 * Remove a list of controls from a subtoolbar inside the manager. the removed controls are disposed, the toolbar
	 * is not removed
	 * 
	 * @param cbm The toolbar manager
	 * @param toolbarId the toolbar id where the controls should be 
	 * @param itemsToRemove the list of control to remove
	 */
	public static void removeToolbarContribution(ICoolBarManager cbm, String toolbarId, List<IContributionItem> itemsToRemove){
		IContributionItem ictb = findToolbar(cbm, toolbarId);
		if (ictb instanceof ToolBarContributionItem) {
			HashSet<String> itemsToRemoveIds = new HashSet<String>();
			for(IContributionItem itemToRemove : itemsToRemove){
				itemsToRemoveIds.add(itemToRemove.getId());
			}
			IToolBarManager tbmanager = ((ToolBarContributionItem) ictb).getToolBarManager();
			if (tbmanager != null){
			for (IContributionItem ci : tbmanager.getItems()) {
					if (itemsToRemove.isEmpty()) break;
					if (itemsToRemoveIds.contains(ci.getId())){
						tbmanager.remove(ci);
						ci.dispose();
						itemsToRemoveIds.remove(ci.getId());
					}
				}
				tbmanager.update(true);
			}
		}
	}
	
	/**
	 * Check if the current control is visible by looking at the application configuration
	 * 
	 * @return true if the control toolbar is marked as visible in the configuration
	 */
	@Override
	public boolean isVisible() {
		return JaspersoftStudioPlugin.getToolItemsManager().isToolbarVisible(getId());
	}

	/**
	 * Dosen't do anything, could be used in the future
	 * 
	 * @param parent parent of the creation
	 * @return always null
	 */
	protected Control createControl(Composite parent) {
		 return null;
	}

	/**
	 * Set the current part, should be the editor where the selection is done
	 * 
	 * @param workbenchPart the editor where the actual selection was done
	 */
	private void setWorkbenchPart(IEditorPart workbenchPart) {
		this.workbenchPart = workbenchPart;
	}
	
	/**
	 * Return the editor where the actual selection was done
	 * 
	 * @return an IEditorPart
	 */
	public IEditorPart getWorkbenchPart(){
		return workbenchPart;
	}

	/**
	 * Dispose the mainControl created for this contribution item. Doing 
	 * this every controls created by the fill of this contribution item
	 * it disposed
	 * 
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (mainControl != null){
			if (!mainControl.isDisposed()){
				if (mainControl.getControl() != null){
					mainControl.getControl().dispose();
				}
				mainControl.dispose();
			}
			mainControl = null;
		}
	}
	
  /**
   * The control item implementation of this <code>IContributionItem</code>
   * method calls the <code>createControl</code> framework method to
   * create a control under the given parent, and then creates
   * a new tool item to hold it.
   * Subclasses must implement <code>createControl</code> rather than
   * overriding this method.
   */
	public final void fill(ToolBar parent, int index) {
		Control control = createControl(parent);
		if (control == null) {
			Policy.logException(new IllegalStateException("createControl(Composite) of " + getClass() //$NON-NLS-1$
					+ " returned null, cannot fill toolbar")); //$NON-NLS-1$
		} else {
			ToolItem ti = new ToolItem(parent, SWT.SEPARATOR, index);
			ti.setControl(control);
			ti.setWidth(computeWidth(control));
			mainControl = ti;
		}
	}
  
  /**
   * Computes the width of the given control which is being added
   * to a tool bar.  This is needed to determine the width of the tool bar item
   * containing the given control.
   * <p>
   * The default implementation of this framework method returns 
   * <code>control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x</code>.
   * Subclasses may override if required.
   * </p>
   *
   * @param control the control being added
   * @return the width of the control
   */
  protected int computeWidth(Control control) {
      return control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
  }
}
