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
package com.jaspersoft.studio.components.commonstyles;

import java.awt.Rectangle;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.nebula.widgets.gallery.RoundedGalleryItemRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.commonstyles.messages.Messages;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.style.view.TemplateStyleView;
import com.jaspersoft.studio.style.view.TemplateViewProvider;
import com.jaspersoft.studio.utils.IOUtils;
import com.jaspersoft.studio.wizards.JSSWizard;

/**
 * 
 * Class with some common methods to contribute a TemplateStyle view
 * 
 * @author Orlandin Marco
 *
 */
public abstract class CommonViewProvider implements TemplateViewProvider, ViewProviderInterface{
	
	/**
	 * The gallery that show the element
	 */
	protected Gallery checkedGallery;
	
	/**
	 * The common delete action
	 */
	protected MenuItem deleteAction = null;
	
	/**
	 * The common edit action
	 */
	protected MenuItem editAction = null;
	
	/**
	 * The standard create action
	 */
	protected MenuItem createAction = null;
	
	/**
	 * Toolbar button to create a template style
	 */
	private ToolItem createStyle = null;
	
	/**
	 * Toolbar button to edit a template style
	 */
	private ToolItem editStyle = null;
	
	/**
	 * Toolbar button to delete a template style
	 */
	private ToolItem deleteStyle = null;
	
	
	/**
	 * Drag listener for the drag and drop of a style from a gallery 
	 * control
	 * 
	 * @author Orlandin Marco
	 *
	 */
	protected class StyleDragListener implements DragSourceListener{
		
		public StyleDragListener(){
			
		}
		
		@Override
		public void dragStart(DragSourceEvent event) {
			dragSetData(event);
		}
		
		/**
		 * The set data method insert into the event the selected templatestyle,
		 * if any
		 */
		@Override
		public void dragSetData(DragSourceEvent event) {
			if (checkedGallery.getSelection().length>0){
				Object data = checkedGallery.getSelection()[0].getData();
				byte[] serializedData = IOUtils.writeToByteArray(data);
				event.data = serializedData;
			}
			
		}
		
		@Override
		public void dragFinished(DragSourceEvent event) {	
		}
	}
	
	/**
	 * Handler for the right click of the gallery, if the right click is outside an element then
	 * the selected element will be deselected and the delete and update actions (if present) will 
	 * be disabled
	 */
	protected class GalleryRightClick implements MouseListener {

		public GalleryRightClick(){}
		
		@Override
		public void mouseUp(MouseEvent e) {	
		}
		
		@Override
		public void mouseDown(MouseEvent e) {
			boolean allDeselected = (e.button == 3 && checkedGallery.getItem(new Point(e.x,e.y))==null);
			if (allDeselected) checkedGallery.deselectAll();
			if (deleteAction != null) deleteAction.setEnabled(!allDeselected);
			if (editAction != null) editAction.setEnabled(!allDeselected);
			updateToolBartSelection();
		}
		
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			boolean isEdit = (e.button == 1 && checkedGallery.getSelectionCount()>0);
			if (isEdit) doEdit();
		}
	}
	
	/**
	 * Create a standard contextual delete action
	 */
	protected void initializeDeleteAction(){
	    deleteAction = new MenuItem(checkedGallery.getMenu(), SWT.NONE);
	    deleteAction.setText(com.jaspersoft.studio.components.commonstyles.messages.Messages.CommonViewProvider_deleteStyleLabel);
	    deleteAction.setImage(getDeleteStyleImage());
	    deleteAction.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		doDelete();
	    		updateToolBartSelection();
	    	}
		});   
	}
	
	/**
	 * Code to execute when the delete contextual action is called
	 */
	protected void doDelete(){
		Shell actualShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		if (MessageDialog.openQuestion(actualShell, com.jaspersoft.studio.components.commonstyles.messages.Messages.CommonViewProvider_deleteStyleQuestionTitle, com.jaspersoft.studio.components.commonstyles.messages.Messages.CommonViewProvider_deleteStyleQuestionText)){
			GalleryItem selectedItem = checkedGallery.getSelection()[0];
			TemplateStyleView.getTemplateStylesStorage().removeStyle((TemplateStyle)selectedItem.getData());
			//checkedGallery.remove(selectedItem);
			//checkedGallery.redraw();
		}
	}
	
	/**
	 * Create a standard contextual delete action
	 */
	protected void initializeCreateAction(){
		createAction = new MenuItem(checkedGallery.getMenu(), SWT.NONE);
		createAction.setText(com.jaspersoft.studio.components.commonstyles.messages.Messages.CommonViewProvider_createStyleLabel);
		createAction.setImage(getNewStyleImage());
		createAction.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		doCreate();
	    		updateToolBartSelection();
	    	}
		});
	}
	
	/**
	 * Code to execute when the delete contextual action is called
	 */
	protected void doCreate(){}
	
	/**
	 * Create a standard contextual delete action
	 */
	protected void initializeEditAction(){
		editAction = new MenuItem(checkedGallery.getMenu(), SWT.NONE);
		editAction.setText(com.jaspersoft.studio.components.commonstyles.messages.Messages.CommonViewProvider_editStyleLabel);
		editAction.setImage(getEditStyleImage());
		editAction.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		doEdit();
	    		updateToolBartSelection();
	    	}
		});
	}
	
	/**
	 * Code to execute when the delete contextual action is called
	 */
	protected void doEdit(){}
	
	/**
	 * Return an image that can be used as icon for the create style action
	 * 
	 * @return an SWT image
	 * 
	 */
	public Image getNewStyleImage() {
		Image image = ResourceManager.getImage("create-style"); //$NON-NLS-1$
		if (image == null){
			image = Activator.getDefault().getImageDescriptor("icons/create-style.png").createImage(); //$NON-NLS-1$
			ResourceManager.addImage("create-style", image); //$NON-NLS-1$
		}
		return image;
	}
	
	/**
	 * Return an image that can be used as icon for the delete style action
	 * 
	 * @return an SWT image
	 * 
	 */
	public Image getDeleteStyleImage() {
		Image image = ResourceManager.getImage("delete-style"); //$NON-NLS-1$
		if (image == null){
			image = Activator.getDefault().getImageDescriptor("icons/delete_style.gif").createImage(); //$NON-NLS-1$
			ResourceManager.addImage("delete-style", image); //$NON-NLS-1$
		}
		return image;
	}
	
	/**
	 * Return an image that can be used as icon for the export styles action
	 * 
	 * @return an SWT image
	 * 
	 */
	public Image getExportStylesImage() {
		Image image = ResourceManager.getImage("export-styles"); //$NON-NLS-1$
		if (image == null){
			image = Activator.getDefault().getImageDescriptor("icons/table-export.png").createImage(); //$NON-NLS-1$
			ResourceManager.addImage("export-styles", image); //$NON-NLS-1$
		}
		return image;
	}
	
	/**
	 * Return an image that can be used as icon for the import styles action
	 * 
	 * @return an SWT image
	 * 
	 */
	public Image getImportStylesImage() {
		Image image = ResourceManager.getImage("import-styles"); //$NON-NLS-1$
		if (image == null){
			image = Activator.getDefault().getImageDescriptor("icons/table-import.png").createImage(); //$NON-NLS-1$
			ResourceManager.addImage("import-styles", image); //$NON-NLS-1$
		}
		return image;
	}
	
	/**
	 * Return an image that can be used as icon for the edit style action
	 * 
	 * @return an SWT image
	 * 
	 */
	public Image getEditStyleImage() {
		Image image = ResourceManager.getImage("edit-style"); //$NON-NLS-1$
		if (image == null){
			image = Activator.getDefault().getImageDescriptor("icons/edit-style.png").createImage(); //$NON-NLS-1$
			ResourceManager.addImage("edit-style", image); //$NON-NLS-1$
		}
		return image;
	}

	/**
	 * Create a standard dialog from a wizard. It also substitute the ok button text of the dialog with
	 * a finish text
	 * 
	 * @param wizardPage page to put inside the dialog
	 * @return a dialog that can be opened
	 */
	protected WizardDialog getEditorDialog(JSSWizard wizardPage){
			WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizardPage){
			//Ovverride this method to change the default text of the finish button with another text
			@Override
			protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
				Button button = super.createButton(parent, id, label, defaultButton);
				if (id == IDialogConstants.FINISH_ID) button.setText(Messages.CommonViewProvider_finishLabel);
				return button;
			}
		};
		return dialog;
	}
	
	/**
	 * Build a gallery item for a TemplateStyle
	 * 
	 * @param style the style
	 * @param rootItem the root of all the items
	 * @return A gallery item
	 */
	protected GalleryItem getItem(TemplateStyle style, GalleryItem rootItem) {
		GalleryItem ti = new GalleryItem(rootItem, SWT.NONE);
		String description = style.getDescription();
		ti.setText(description.isEmpty() ? " " : description); //$NON-NLS-1$
		Image previewImage = generatePreviewFigure(style);
		ti.setImage(previewImage);
		ti.setSelectedImage(previewImage);
		ti.setStandardImage(previewImage);
		ti.setData(style);
		return ti;
	}
	
	
	/**
	 * Update a gallery item for a TemplateStyle
	 * 
	 * @param style the style
	 * @param itemToUpdate the item to update
	 */
	protected void updateItem(TemplateStyle style, GalleryItem itemToUpdate) {
		String description = style.getDescription();
		itemToUpdate.setText(description.isEmpty() ? " " : description); //$NON-NLS-1$
		Image previewImage = generatePreviewFigure(style);
		itemToUpdate.setImage(previewImage);
		itemToUpdate.setSelectedImage(previewImage);
		itemToUpdate.setStandardImage(previewImage);
		itemToUpdate.setData(style);
	}
	
	/**
	 * Create a shadow effect on a Rectangle
	 * 
	 * @param gc the graphics used to design
	 * @param bounds bounds of the element where to create the shadow
	 * @param xOffset how much the shadow is translated of the x axe
	 * @param yOffset how much the shadow is translated of the y axe
	 * @param radius the radious of the shadow
	 */
	public static void fillRoundRectangleDropShadow(GC gc, Rectangle bounds, int xOffset, int yOffset, int radius) {
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		Color oldColor = gc.getBackground();
		int oldAlpha = gc.getAlpha();
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.setAlpha(0x8f / radius);

		for (int i = 0; i < radius; i++) {
			Rectangle shadowBounds = new Rectangle(bounds.x + xOffset, bounds.y + yOffset, bounds.width - i, bounds.height - i);

			gc.fillRoundRectangle(shadowBounds.x, shadowBounds.y, shadowBounds.width, shadowBounds.height, radius, radius);
		}
		gc.setBackground(oldColor);
		gc.setAlpha(oldAlpha);
	}
	
	/**
	 * Enable or disable the edit and delete toolbar buttons according to the selection
	 */
	private void updateToolBartSelection(){
		boolean selectionState = checkedGallery.getSelectionCount()>0;
		if (editStyle != null) editStyle.setEnabled(selectionState);
		if (deleteStyle != null) deleteStyle.setEnabled(selectionState);
	}
	
	/**
	 * Crate the toolbar with three button to add, edit or delete template styles
	 * @param parent the container of the toolbar
	 */
	protected void createToolBar(Composite parent){
		checkedGallery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateToolBartSelection();
			}
		});
		
		ToolBar toolBar = new ToolBar (parent, SWT.FLAT);
		createStyle = new ToolItem (toolBar, SWT.PUSH);
		createStyle.setImage (getNewStyleImage());
		createStyle.setToolTipText(Messages.CommonViewProvider_createStyleToolButton);
		createStyle.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		doCreate();
	    		updateToolBartSelection();
	    	}
		});
		
		editStyle = new ToolItem (toolBar, SWT.PUSH);
		editStyle.setImage (getEditStyleImage());
		editStyle.setToolTipText(Messages.CommonViewProvider_editStyleToolButton);
		editStyle.setEnabled(false);
		editStyle.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		doEdit();
	    		updateToolBartSelection();
	    	}
		});
		
		deleteStyle = new ToolItem (toolBar, SWT.PUSH);
		deleteStyle.setImage (getDeleteStyleImage());
		deleteStyle.setToolTipText(Messages.CommonViewProvider_deleteStyleToolButton);
		deleteStyle.setEnabled(false);
		deleteStyle.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		doDelete();
	    		updateToolBartSelection();
	    	}
		});
		
		ToolItem exportItem = new ToolItem (toolBar, SWT.PUSH);
		exportItem.setImage (getExportStylesImage());
		exportItem.setToolTipText(Messages.CommonViewProvider_exportStylesToolTip);
		final CommonViewProvider exportProvider = this;
		exportItem.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		ExportDialog dlg = new ExportDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),exportProvider);
	    		dlg.open();
	    	}
		});
		
		ToolItem importItem = new ToolItem (toolBar, SWT.PUSH);
		importItem.setImage (getImportStylesImage());
		importItem.setToolTipText("Import the styles from an XML file");
		final CommonViewProvider importProvider = this;
		importItem.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		ImportDialog dlg = new ImportDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),importProvider);
	    		dlg.open();
	    	}
		});
		
		GridData toolButtonData = new GridData();
		toolButtonData.horizontalAlignment = SWT.END;
		toolBar.setLayoutData(toolButtonData);
	}
	
	/**
	 * Create all the visible controls
	 * 
	 * @param parent the container of all the controls
	 * @param imageWidth the width of images of the gallery
	 * @param imageHeight the height of the images of the gallery
	 * @param labelText the text on the description label
	 */
	protected void createControls(Composite parent, int imageWidth, int imageHeight, String labelText) {
		Composite firstLine = new Composite(parent, SWT.NONE);
		GridLayout firstLineLayout = new GridLayout(2,false);
		firstLineLayout.verticalSpacing = 0;
		firstLineLayout.marginHeight = 0;
		firstLine.setLayout(firstLineLayout);
		GridData firstLineData = new GridData();
		firstLineData.grabExcessHorizontalSpace=true;
		firstLineData.horizontalAlignment = SWT.FILL;
		firstLine.setLayoutData(firstLineData);
		
		Label dragLabel = new Label(firstLine, SWT.NONE);
		dragLabel.setText(labelText);
		GridData labelData = new GridData();
		labelData.grabExcessHorizontalSpace = true;
		labelData.horizontalAlignment = SWT.FILL;
		dragLabel.setLayoutData(labelData);
		
		checkedGallery = new Gallery(parent, SWT.VIRTUAL | SWT.V_SCROLL | SWT.BORDER);
		final NoGroupRenderer gr = new NoGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemSize(imageWidth, imageHeight);
		gr.setAutoMargin(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		checkedGallery.setLayoutData(gd);
		checkedGallery.setGroupRenderer(gr);
		checkedGallery.enableItemsTooltip(false);
		RoundedGalleryItemRenderer ir = new RoundedGalleryItemRenderer();
		ir.setShowLabels(true);
		checkedGallery.setItemRenderer(ir);
		GridData galleryData = new GridData();
		galleryData.grabExcessHorizontalSpace = true;
		galleryData.grabExcessVerticalSpace = true;
		galleryData.horizontalAlignment = SWT.FILL;
		galleryData.verticalAlignment = SWT.FILL;
		checkedGallery.setLayoutData(galleryData);
		
	    Menu popupMenu = new Menu(checkedGallery);
	    checkedGallery.setMenu(popupMenu);
	    checkedGallery.addMouseListener(new GalleryRightClick());
	    
	    initializeCreateAction();
	    initializeEditAction();
	    initializeDeleteAction();
		createToolBar(firstLine);
	}
	
}
