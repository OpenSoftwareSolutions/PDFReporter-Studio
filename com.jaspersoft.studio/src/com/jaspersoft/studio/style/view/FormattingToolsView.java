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
package com.jaspersoft.studio.style.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.compatibility.ToolUtilitiesCompatibility;
import com.jaspersoft.studio.editor.action.align.Align2BorderAction;
import com.jaspersoft.studio.editor.action.align.Align2Element;
import com.jaspersoft.studio.editor.action.size.MatchSizeAction;
import com.jaspersoft.studio.editor.action.size.Size2BorderAction;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.report.SelectionChangedListener;
import com.jaspersoft.studio.formatting.actions.CenterInParentAction;
import com.jaspersoft.studio.formatting.actions.DecreaseHSpaceAction;
import com.jaspersoft.studio.formatting.actions.DecreaseVSpaceAction;
import com.jaspersoft.studio.formatting.actions.EqualsHSpaceAction;
import com.jaspersoft.studio.formatting.actions.EqualsVSpaceAction;
import com.jaspersoft.studio.formatting.actions.IncreaseHSpaceAction;
import com.jaspersoft.studio.formatting.actions.IncreaseVSpaceAction;
import com.jaspersoft.studio.formatting.actions.JoinLeftAction;
import com.jaspersoft.studio.formatting.actions.JoinRightAction;
import com.jaspersoft.studio.formatting.actions.OrganizeAsTableAction;
import com.jaspersoft.studio.formatting.actions.RemoveHSpaceAction;
import com.jaspersoft.studio.formatting.actions.RemoveVSpaceAction;
import com.jaspersoft.studio.formatting.actions.SameHeightMaxAction;
import com.jaspersoft.studio.formatting.actions.SameHeightMinAction;
import com.jaspersoft.studio.formatting.actions.SameWidthMaxAction;
import com.jaspersoft.studio.formatting.actions.SameWidthMinAction;
import com.jaspersoft.studio.model.MGraphicElement;

/**
 * View where a the format option are shown as buttons and can be clicked
 * to format in the appropriate way the selected elements
 * 
 * @author Orlandin Marco
 *
 */
public class FormattingToolsView extends ViewPart implements IContributedContentsView {
	
	/**
	 * It is simulate an action, but the only important thing is the isEnabled method.
	 * This is done to avoid to call the isEnabled of every format action, in order to
	 * speed up the computation
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class FakeActionEnabler extends Action{
		
		/**
		 * The id of the incapsulated action
		 */
		private String actionId;
		
		/**
		 * Minimum number of MGraphicalElements selected in order to have the isEnabled return the true value
		 */
		private int requiredElementSelected;
		
		/**
		 * 
		 * @param requiredElements Minimum number of MGraphicalElements selected in order to have the isEnabled return the true value
		 */
		public FakeActionEnabler(int requiredElements, String actionId){
			this.requiredElementSelected = requiredElements;
			this.actionId = actionId;
		}
		
		/**
		 * Return true if the number of selected MGraphicalElement is equal or greater of the element
		 * that this action require to be selected, otherwise false;
		 */
		@Override
		public boolean isEnabled() {
			return selectedGraphicalElements>=requiredElementSelected;
		}
		
		@Override
		public void run() {
			ActionRegistry registry = (ActionRegistry)getContributingPart().getAdapter(ActionRegistry.class);
			IAction action = registry.getAction(actionId);
			action.run();
		}
	}
	
	/**
	 * The scrolled composite where the main composite is placed
	 */
	private ScrolledComposite scrollComp;
	
	/**
	 * Composite where the buttons are placed
	 */
  private Composite mainContainer;
  
  /**
   * List of all the buttons that do a format action
   */
  private List<Control> controlList = new ArrayList<Control>();
  
  /**
   * Number of MGraphicalElement selected
   */
  private int selectedGraphicalElements = 0;
  
	/**
	 * Minimum width for every button
	 */
  private int buttonsMinWidth = 200;
	
  /**
   * Height for every button
   */
  private int buttonHeight = 30;
	
  /**
   * Custom layout to have all the controls fill the available area on multiple 
   * columns without leaving empty space
   * 
   * @author Orlandin Marco
   *
   */
  private class ButtonFillLayout extends Layout{

  	/**
  	 * Height of the last column (the heightest one)
  	 */
  	private int height = 0;
  	
  	/**
  	 * The size of the control is the same of the parent
  	 */
		@Override
		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			return composite.getParent().getSize();
		}

		@Override
		protected void layout(Composite composite, boolean flushCache) {
			int containerWidth = composite.getBounds().width;
			if (containerWidth<buttonsMinWidth) return;
			Control [] children = composite.getChildren ();
			int maximumColumns = containerWidth/buttonsMinWidth;
			int fillWidth = (containerWidth / maximumColumns) - 1;
			int actualRow = 0;
			int actualCol = 0;
			for (Control control : children) {
				int newX = (actualCol*fillWidth)+1;
				int newY = (actualRow*buttonHeight)+1;
				Rectangle newBounds = new Rectangle(newX, newY, fillWidth, buttonHeight);
				control.setBounds(newBounds);
				actualCol++;
				if (actualCol == maximumColumns){
					actualCol = 0;
					actualRow++;
				}
				height = newY+buttonHeight;
			}
		}
		
		public int getHeight(){
			return height;
		}
  	
  }
  
  /**
   * Selection listener called everytime something is selected
   */
  private SelectionChangedListener selectionListener = new SelectionChangedListener() {
		
		@Override
		public void selectionChanged() {
			//Initialize contend will do nothing if the buttons was already initialized
			initializeContent();	
			updateSelectedElements();
			refresh();
		}
		
	};
	
	/**
	 * Return the editor
	 */
	@Override
	public IWorkbenchPart getContributingPart() {
		  return getSite().getPage().getActiveEditor();
	}
	
	/**
	 * Refresh the button enabled state using the isEnabled of 
	 * the actions associated to everyone of them
	 */
	private void refresh(){
		mainContainer.setRedraw(false);
		for(Control control : controlList){
			IAction action = (IAction)control.getData();
			control.setEnabled(action.isEnabled());
		}
		mainContainer.setRedraw(true);
	}

	/**
	 * Check the number of actually selected MGraphicalElement. If the number is at least
	 * 2 no more elements are searched since all the format actions need at least 2 elements
	 * 
	 * @param selection the actually selected elements
	 */
	private void updateSelectedElements(){
		IWorkbenchPart editor = getContributingPart();
		selectedGraphicalElements = 0;
		if (editor instanceof CachedSelectionProvider){
			CachedSelectionProvider cachedSelEditor = (CachedSelectionProvider)editor;
			List<?> editparts = cachedSelEditor.getSelectionCache().getSelectionModelPartForType(MGraphicElement.class);
			selectedGraphicalElements = ToolUtilitiesCompatibility.getSelectionWithoutDependants(editparts).size();
		} 
	}
	

	@Override
	public void createPartControl(Composite parent) {
		scrollComp = new ScrolledComposite(parent, SWT.V_SCROLL);
		scrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		scrollComp.setLayout(new GridLayout(1,false));
		scrollComp.setExpandVertical(true);
		scrollComp.setExpandHorizontal(true);
		scrollComp.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				refreshScrolledHeight();
			}
		});
		mainContainer = new Composite(scrollComp, SWT.BORDER);
		mainContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrollComp.setContent(mainContainer);
    mainContainer.setLayout(new ButtonFillLayout());
    
		IWorkbenchPart editor = getContributingPart();
		if (editor instanceof CachedSelectionProvider){
			CachedSelectionProvider cachedSelEditor = (CachedSelectionProvider)editor;
			cachedSelEditor.getSelectionCache().addSelectionChangeListener(selectionListener);
		} 
	}
	
	/**
	 * Set the height of the scrolled composite, according to the one of its content, to 
	 * show or not the scrollbar
	 */
	private void refreshScrolledHeight(){
		if (controlList.isEmpty()) return;
		mainContainer.layout();
		int heightRequired = ((ButtonFillLayout)mainContainer.getLayout()).getHeight();
		scrollComp.setMinHeight(heightRequired+10);
	}
	
	/**
	 * Create the buttons with the associated format actions, but only if an editor is opened
	 * and only if the buttons was not already craeted. This is done at various time since someone
	 * can open the view without any editor opened, so without formatting actions available
	 */
	private void initializeContent(){
		if (controlList.isEmpty() && getContributingPart() != null){
	  	ActionRegistry registry = (ActionRegistry)getContributingPart().getAdapter(ActionRegistry.class);
	    if (registry != null){
	    	mainContainer.setRedraw(false);
	    	generateButton(registry.getAction(Align2Element.ID_ALIGN_TOP),2);
	    	generateButton(registry.getAction(Align2Element.ID_ALIGN_BOTTOM),2);
	    	generateButton(registry.getAction(Align2Element.ID_ALIGN_LEFT),2);
	    	generateButton(registry.getAction(Align2Element.ID_ALIGN_RIGHT),2);
	    	generateButton(registry.getAction(Align2Element.ID_ALIGN_CENTER),2);
	    	generateButton(registry.getAction(Align2Element.ID_ALIGN_MIDDLE),2);
	    	generateButton(registry.getAction(Align2BorderAction.ID_ALIGN_TOP),1);
	    	generateButton(registry.getAction(Align2BorderAction.ID_ALIGN_BOTTOM),1);
	    	generateButton(registry.getAction(Align2BorderAction.ID_ALIGN_LEFT),1);
	    	generateButton(registry.getAction(Align2BorderAction.ID_ALIGN_RIGHT),1);
	    	generateButton(registry.getAction(OrganizeAsTableAction.ID),1);
	    	generateButton(registry.getAction(JoinLeftAction.ID),2);
	    	generateButton(registry.getAction(JoinRightAction.ID),2);
	    	generateButton(registry.getAction(EqualsHSpaceAction.ID),2);
	    	generateButton(registry.getAction(IncreaseHSpaceAction.ID),2);
	    	generateButton(registry.getAction(DecreaseHSpaceAction.ID),2);
	    	generateButton(registry.getAction(RemoveHSpaceAction.ID),2);
	    	generateButton(registry.getAction(EqualsVSpaceAction.ID),2);
	    	generateButton(registry.getAction(IncreaseVSpaceAction.ID),2);
	    	generateButton(registry.getAction(DecreaseVSpaceAction.ID),2);
	    	generateButton(registry.getAction(RemoveVSpaceAction.ID),2);
	    	generateButton(registry.getAction(MatchSizeAction.ID_SIZE_WIDTH),2);
	    	generateButton(registry.getAction(SameWidthMinAction.ID),2);
	    	generateButton(registry.getAction(SameWidthMaxAction.ID),2);
	    	generateButton(registry.getAction(MatchSizeAction.ID_SIZE_HEIGHT),2);
	    	generateButton(registry.getAction(SameHeightMinAction.ID),2);
	    	generateButton(registry.getAction(SameHeightMaxAction.ID),2);
	    	generateButton(registry.getAction(MatchSizeAction.ID_SIZE_BOTH),2);
	    	generateButton(registry.getAction(Size2BorderAction.ID_SIZE_BOTH),1);
	    	generateButton(registry.getAction(Size2BorderAction.ID_SIZE_WIDTH),1);
	    	generateButton(registry.getAction(Size2BorderAction.ID_SIZE_HEIGHT),1);
	    	generateButton(registry.getAction(Align2BorderAction.ID_ALIGN_CENTER),1);
	    	generateButton(registry.getAction(Align2BorderAction.ID_ALIGN_MIDDLE),1);
	    	generateButton(registry.getAction(CenterInParentAction.ID),1);
	    	mainContainer.setRedraw(true);
	    	scrollComp.layout(true, true);
	    	refreshScrolledHeight();
	    }
		}
	}
	
	/**
	 * Create a button for a formatting action. Image and text of the button are
	 * taken from the action. the enable state of the button will be dependent 
	 * from the number of MGraphicalElement selected at the moment
	 * 
	 * @param action format action
	 * @param numberOfSelectedElements minimum number of selected MGraphicalElment needed
	 * to have the button enable
	 */
	private void generateButton(IAction action, int numberOfSelectedElements){
		if (action == null) return;
		Button button = new Button(mainContainer, SWT.PUSH);
		button.setText(action.getText());
		button.setImage(ResourceManager.getImage(action.getImageDescriptor()));
		button.setToolTipText(action.getToolTipText());
		FakeActionEnabler enablerAction = new FakeActionEnabler(numberOfSelectedElements, action.getId());
		button.setEnabled(enablerAction.isEnabled());
		button.setData(enablerAction);
		RowData buttonData = new RowData();
		//buttonData.width = SWT.FILL;
		button.setLayoutData(buttonData);
		controlList.add(button);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IAction action = (IAction)e.widget.getData();
				action.run();
			}
		});
	}
	
	
	@Override
	public void dispose() {
		super.dispose();
		IWorkbenchPart editor = getContributingPart();
  		if (editor instanceof CachedSelectionProvider){
			CachedSelectionProvider cachedSelEditor = (CachedSelectionProvider)editor;
			cachedSelEditor.getSelectionCache().removeSelectionChangeListener(selectionListener);
		} 
	}
	
	@Override
	public void setFocus() {
		mainContainer.setFocus();
	}
}
