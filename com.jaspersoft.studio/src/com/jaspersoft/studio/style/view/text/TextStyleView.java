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
package com.jaspersoft.studio.style.view.text;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceCache;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.style.view.TemplateStyleView;
import com.jaspersoft.studio.style.view.TemplateViewProvider;
import com.jaspersoft.studio.utils.IOUtils;
import com.jaspersoft.studio.wizards.JSSWizard;

/**
 * Viewer where all the defined textual styles are shown
 * 
 * @author Orlandin Marco
 *
 */
public class TextStyleView implements TemplateViewProvider {

	/**
	 * Container of the styles entries
	 */
	private Composite sampleComposite;

	/**
	 * Cache where the images of the style are stored and disposed at the end
	 */
	private ResourceCache resourceCache = new ResourceCache();
	
	/**
	 * Toolbar button to create a template style
	 */
	private ToolItem createStyle = null;
	
	
	@Override
	public void createControls(Composite parent) {
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
		dragLabel.setText(Messages.TextStyleView_dragMessage);
		GridData labelData = new GridData();
		labelData.grabExcessHorizontalSpace = true;
		labelData.horizontalAlignment = SWT.FILL;
		dragLabel.setLayoutData(labelData);
		
		sampleComposite = new Composite(parent, SWT.NONE);
		sampleComposite.setBackground(ResourceManager.getColor(255, 255, 255));
		GridLayout sampleLayout = new GridLayout(1,false);
		sampleLayout.verticalSpacing = 0;
		sampleComposite.setLayout(sampleLayout);
		GridData galleryData = new GridData();
		galleryData.grabExcessHorizontalSpace = true;
		galleryData.grabExcessVerticalSpace = true;
		galleryData.horizontalAlignment = SWT.FILL;
		galleryData.verticalAlignment = SWT.FILL;
		sampleComposite.setLayoutData(galleryData);
		sampleComposite.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				resourceCache.dispose();
			}
		});
		
		createToolBar(firstLine);
		sampleComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				clearAndFillContent();
			}
		});
		 Menu popupMenu = new Menu(sampleComposite);
	   MenuItem createAction = new MenuItem(popupMenu, SWT.NONE);
	   createAction.setText(Messages.TextStyleView_createLabel);
	   createAction.setImage(JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/create-style.png")); //$NON-NLS-1$
	   createAction.addSelectionListener(new SelectionAdapter() {
	   	@Override
	   	public void widgetSelected(SelectionEvent e) {
	   		doCreate();
	   	}
	   });
	   sampleComposite.setMenu(popupMenu);
	   createDropTarget(sampleComposite);
	}
	
	/**
	 * Add the drag support to move a text template on a textual element
	 * 
	 * @param control control where the drag operation where added
	 */
	private void addDragSupport(Control control) {
		int operations = DND.DROP_MOVE;
		final Transfer[] types = new Transfer[] { TextRestrictedTransferType.getInstance() };
		DragSource source = new DragSource(control, operations);
		source.setTransfer(types);
		source.addDragListener(new StyleDragListener(control));
	}
	
	/**
	 * Listener for the drag of a text style on an element
	 * 
	 * @author Orlandin Marco
	 *
	 */
	protected class StyleDragListener implements DragSourceListener{
		
		private Control draggedElement;
		
		public StyleDragListener(Control draggedElement){
			this.draggedElement = draggedElement;
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
			if (draggedElement.getData() != null){
				Object data = draggedElement.getData();
				byte[] serializedData = IOUtils.writeToByteArray(data);
				event.data = serializedData;
			}
			
		}
		
		@Override
		public void dragFinished(DragSourceEvent event) {	
		}
	}
	
	/**
	 * Add the drop operation of a MStyle on the styles list, in this way
	 * the user can create a textstyle from a JRstyle simply by dragging it
	 * on the TextStyles list
	 * 
	 * @param targetText control where the drop listener will be added
	 */
	private void createDropTarget(final Composite targetText) {
	    Transfer[] types = new Transfer[] { TemplateTransfer.getInstance()  };
	    DropTarget dropTarget = new DropTarget(targetText, DND.DROP_MOVE);
	    dropTarget.setTransfer(types);
	    dropTarget.addDropListener(new DropTargetListener() {
	    	
	    	private List<MStyle> cachedStyles = null;
	    	
	    	/**
	    	 * Return a list of MStyle elements dragged 
	    	 * 
	    	 * @param event the drop event
	    	 * @return a not null list of MStyle
	    	 */
	    	private List<MStyle> getDraggedStyles(DropTargetEvent event){
	    		List<MStyle> result = new ArrayList<MStyle>();
	    		if (event.widget instanceof DropTarget){
	    			DropTarget dropTarget = (DropTarget)event.widget;
	    			for (Transfer transfer : dropTarget.getTransfer()){
	    				if (transfer instanceof TemplateTransfer){
	    					Object droppedObject = ((TemplateTransfer)((DropTarget)event.widget).getTransfer()[0]).getTemplate();
	    					if (droppedObject instanceof List){
	    						for(Object item : (List<?>)droppedObject){
	    							if (item instanceof MStyle) result.add((MStyle)item);
	    						}
	    					}
	    				}
	    			}
	    		}
	    		return result;
	    	}

	    	/**
	    	 * When the drag enter is check if between the dragged elements there is
	    	 * exactly one MStyle, then it allow the drag operation, otherwise the 
	    	 * feedback of the operation will be an invalid drag
	    	 */
	    	@Override
	      public void dragEnter(DropTargetEvent event) {
	      	cachedStyles = null;
	      	List<MStyle> draggedStyles = getDraggedStyles(event);
	      	if (draggedStyles.size() == 1){
	      		cachedStyles = draggedStyles;
	      	} else {
		      	event.detail = DND.DROP_NONE;
		      	event.feedback = DND.FEEDBACK_NONE;
	      	}
	      }

	      public void drop(DropTargetEvent event) {
	      	if (cachedStyles != null){
	      		for(MStyle style : cachedStyles){
	      			TextStyle newStyle = new TextStyle((JRStyle)style.getValue());
	      			TextStyleWizard wizard = new TextStyleWizard(true, newStyle);
	      			WizardDialog dialog = getEditorDialog(wizard);
	      			if (dialog.open() == Dialog.OK) {
	      				newStyle = wizard.getTableStyle();
	      				TemplateStyleView.getTemplateStylesStorage().addStyle(newStyle);
	      			}
	      		}
	      	}
	      	cachedStyles = null;
	      }

	      public void dropAccept(DropTargetEvent event) {}

				@Override
				public void dragLeave(DropTargetEvent event) {}

				@Override
				public void dragOperationChanged(DropTargetEvent event) {}

				@Override
				public void dragOver(DropTargetEvent event) {	}
	    });
	  }
	
	/**
	 * Edit operation for a TextStyle, open the dialog to edit the style
	 * 
	 * @param style TextStyle to edit
	 */
	private void doEdit(TextStyle style){
		TextStyleWizard wizard = new TextStyleWizard(true, style);
		WizardDialog dialog = getEditorDialog(wizard);
		if (dialog.open() == Dialog.OK) {
			TextStyle newStyle = wizard.getTableStyle();
			TemplateStyleView.getTemplateStylesStorage().editStyle(style,newStyle);
		}
	}
	
	/**
	 * Create operation for a TextStyle, open the dialog to set the style attributes
	 * 
	 */
	private void doCreate(){
		TextStyleWizard wizard = new TextStyleWizard(true, null);
		WizardDialog dialog = getEditorDialog(wizard);
		if (dialog.open() == Dialog.OK) {
			TextStyle newStyle = wizard.getTableStyle();
			TemplateStyleView.getTemplateStylesStorage().addStyle(newStyle);
		}
	}
	
	/**
	 * Create the popoup menu with the edit, delete and new operation 
	 * 
	 * @param container control where the menu will be added
	 * @return the menu
	 */
	private Menu createPopupMenu(final Control container){
    Menu popupMenu = new Menu(container);
    MenuItem createAction = new MenuItem(popupMenu, SWT.NONE);
    createAction.setText(Messages.TextStyleView_createLabel); 
    createAction.setImage(JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/create-style.png")); //$NON-NLS-1$
    createAction.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		doCreate();
    	}
		});
    MenuItem editAction = new MenuItem(popupMenu, SWT.NONE);
    editAction.setText(Messages.TextStyleView_editLabel);
    editAction.setImage(JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/edit-style.png")); //$NON-NLS-1$
    editAction.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
				TextStyle style = (TextStyle)container.getData();
				doEdit(style);
    	}
		});
    MenuItem deleteAction = new MenuItem(popupMenu, SWT.NONE);
    deleteAction.setText(Messages.TextStyleView_deleteLabel);
    deleteAction.setImage(JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/delete_style.gif")); //$NON-NLS-1$
    deleteAction.addSelectionListener(new SelectionAdapter() 
    {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
  			TemplateStyleView.getTemplateStylesStorage().removeStyle((TemplateStyle)container.getData());
    	}
		});
    return popupMenu;
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
				if (id == IDialogConstants.FINISH_ID) button.setText("Finish"); //$NON-NLS-1$
				return button;
			}
		};
		return dialog;
	}
	
	/**
	 * Crate the toolbar 
	 * @param parent the container of the toolbar
	 */
	protected void createToolBar(Composite parent){
		
		ToolBar toolBar = new ToolBar (parent, SWT.FLAT);
		createStyle = new ToolItem (toolBar, SWT.PUSH);
		createStyle.setImage (getTabImage());
		createStyle.setToolTipText(Messages.TextStyleView_createToolTip);
		createStyle.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		doCreate();
	    	}
		});
		
		GridData toolButtonData = new GridData();
		toolButtonData.horizontalAlignment = SWT.END;
		toolBar.setLayoutData(toolButtonData);
	}

	@Override
	public AbstractTransferDropTargetListener getDropListener(EditPartViewer viewer) {
		return new TextStyleTransferDropListener(viewer);
	}

	@Override
	public Image getTabImage() {
		return JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/text-style.png"); //$NON-NLS-1$
	}

	@Override
	public TemplateStyle getBuilder() {
		return new TextStyle();
	}
	
	private void clearContent(){
		for(Control label : sampleComposite.getChildren()){
			label.dispose();
		}
	}
	
	private void clearAndFillContent(){
		clearContent();
		fillStyles(TemplateStyleView.getTemplateStylesStorage().getStylesDescriptors());
	}

	@Override
	public void notifyChange(PropertyChangeEvent e) {
		if (e.getNewValue() instanceof TextStyle) {
			clearAndFillContent();
		}
	}
	
	@Override
	public void fillStyles(Collection<TemplateStyle> styles) {
		sampleComposite.setRedraw(false);
		for(TemplateStyle style : styles){
			if (style instanceof TextStyle){
				final Composite sampleArea = new Composite(sampleComposite, SWT.BORDER);
				sampleArea.setData(style);
				addDragSupport(sampleArea);
				GridData sampleData = new GridData();
				sampleData.heightHint = 40;
				sampleData.grabExcessHorizontalSpace = true;
				sampleData.grabExcessVerticalSpace = false;
				sampleData.horizontalAlignment = SWT.FILL;
				sampleData.verticalAlignment = SWT.TOP;
				sampleArea.setLayoutData(sampleData);
				
		    sampleArea.setMenu(createPopupMenu(sampleArea));
				
				sampleArea.addControlListener(new ControlAdapter() {
					@Override
					public void controlResized(ControlEvent e) {
 						Rectangle bounds = sampleArea.getBounds();
						int width = bounds.width-4;
						int height = 40;
						if (width < 1) {
							width = sampleComposite.getBounds().x-4;
							if (width <= 0) width = 100;
						}
						TextStyle originalStyle = (TextStyle)e.widget.getData();
						TextStyle normalized = originalStyle.clone();
						String text = originalStyle.getDescription();
						if (text == null || text.isEmpty()) text = Messages.TextStyleView_sampleText; 
						//Remove the border and use a standard alignment to improve visibility
						normalized.setDescription(text);
						normalized.setBorders(null);
						normalized.setHorizontalAlignmen(HorizontalAlignEnum.LEFT);
						normalized.setVerticalAlignmen(VerticalAlignEnum.TOP);
						int fontSize = normalized.getFont().getOwnFontsize().intValue();
						ImageData previewData = PreviewGenerator.generatePreview(normalized, fontSize*text.length()+width, fontSize + height, sampleComposite.getBackground().getRGB());
						if (normalized.getFont().getOwnFontsize()>height) previewData = cropImage(previewData, 0, 0, height, width);
						sampleArea.setBackgroundImage(resourceCache.getImage(previewData));
					}
				});
				
				sampleArea.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						TextStyle style = (TextStyle)e.widget.getData();
						doEdit(style);
					}
				});
			}
		}
		sampleComposite.setRedraw(true);
		sampleComposite.layout(true,true);
	}
	
	private static ImageData cropImage(ImageData sourceImageData, int x, int y, int height, int width){
		Image sourceImage = new Image(Display.getCurrent(), sourceImageData);
    Image croppedImage = new Image(Display.getCurrent(), width, height);
    GC gc = new GC(sourceImage);
    gc.copyArea(croppedImage, x, y);
    gc.dispose();
    ImageData croppedImageData = croppedImage.getImageData();
    croppedImage.dispose();
    sourceImage.dispose();
    return croppedImageData;
	}
	
	@Override
	public String getTabName() {
		return Messages.TextStyleView_tabTitle;
	}
	

}
