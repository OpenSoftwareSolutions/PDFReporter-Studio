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
package com.jaspersoft.studio.components.table.figure;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.commonstyles.CommonViewProvider;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle.BorderStyleEnum;
import com.jaspersoft.studio.components.table.model.table.command.wizard.TableStyleWizard;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.style.view.TemplateStyleView;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * Extension to show inside a gallery a list of TableStyle that can be drag and
 * dropped on a Table to apply them
 * 
 * @author Orlandin Marco
 * 
 */
public class TableStyleView extends CommonViewProvider {

	/**
	 * Height of every image in the gallery
	 */
	private static final int GALLERY_HEIGHT = 100;

	/**
	 * Width of every image in the gallery
	 */
	private static final int GALLERY_WIDTH = 100;

	/**
	 * The gallery root item
	 */
	private GalleryItem tableGroup;

	/**
	 * Create a gallery with inside all the table styles with their previews
	 */
	@Override
	public void createControls(Composite parent) {
		super.createControls(parent, GALLERY_WIDTH, GALLERY_HEIGHT, Messages.TableStyleView_labelText);
		addDragSupport();
	}

	public List<TemplateStyle> getStylesList() {
		List<TemplateStyle> result = new ArrayList<TemplateStyle>();
		Collection<TemplateStyle> savedStyles = TemplateStyleView.getTemplateStylesStorage().getStylesDescriptors();
		for (TemplateStyle style : savedStyles)
			if (style instanceof TableStyle)
				result.add(style);
		return result;
	}

	/**
	 * Open the TableStyle wizard to create a style
	 */
	protected void doCreate() {
		TableStyleWizard wizard = new TableStyleWizard(true, null);
		WizardDialog dialog = getEditorDialog(wizard);
		if (dialog.open() == Dialog.OK) {
			TableStyle newStyle = wizard.getTableStyle();
			TemplateStyleView.getTemplateStylesStorage().addStyle(newStyle);
			// getItem(newStyle, tableGroup);
			// checkedGallery.redraw();
		}
	}

	/**
	 * Open the style dialog to edit the selected Template Style
	 */
	@Override
	protected void doEdit() {
		GalleryItem selectedItem = checkedGallery.getSelection()[0];
		TemplateStyle oldStyle = (TemplateStyle) selectedItem.getData();
		TableStyleWizard wizard = new TableStyleWizard(true, oldStyle);
		WizardDialog dialog = getEditorDialog(wizard);
		if (dialog.open() == Dialog.OK) {
			TableStyle newStyle = wizard.getTableStyle();
			TemplateStyleView.getTemplateStylesStorage().editStyle(oldStyle, newStyle);
			// updateItem(newStyle, selectedItem);
			// checkedGallery.redraw();
		}
	}

	/**
	 * The name of the tab
	 * 
	 * @return a string that will be used as title of the tab
	 */
	@Override
	public String getTabName() {
		return "Table Styles"; //$NON-NLS-1$
	}

	/**
	 * Called when the styles need to be inserted in the gallery. Here are passed
	 * all the template styles read from the properties file, so only the one with
	 * type TableStyle will be shown
	 * 
	 * @param styles
	 *          a list of all the TemplateStyles read from the properties file
	 */
	@Override
	public void fillStyles(Collection<TemplateStyle> styles) {
		tableGroup = new GalleryItem(checkedGallery, SWT.NONE);
		checkedGallery.setRedraw(false);
		for (TemplateStyle style : styles)
			if (style instanceof TableStyle)
				getItem(style, tableGroup);
		checkedGallery.setRedraw(true);
	}

	/**
	 * Add the drag support
	 */
	private void addDragSupport() {
		int operations = DND.DROP_MOVE;
		final Transfer[] types = new Transfer[] { TableRestrictedTransferType.getInstance() };
		DragSource source = new DragSource(checkedGallery, operations);
		source.setTransfer(types);
		source.addDragListener(new StyleDragListener());
	}

	/**
	 * Build a preview image of a Table
	 * 
	 * @param style
	 *          the style of the table
	 * @return a preview SWT image of the table
	 */
	public Image generatePreviewFigure(final TemplateStyle style) {
		String key = "tableTemplates_" + style.toString(); //$NON-NLS-1$
		Image image = ResourceManager.getImage(key);
		if (image == null && style instanceof TableStyle) {
			TableStyle tableStyle = (TableStyle) style;
			image = new Image(null, new org.eclipse.swt.graphics.Rectangle(0, 0, GALLERY_WIDTH, GALLERY_HEIGHT));
			GC graphics = new GC(image);
			try {
				int y = 1;
				int x = 1;
				int w = GALLERY_WIDTH - 6;
				int h = GALLERY_HEIGHT - 6;
				int rowHeight = h / 7;

				// Draw the shadow
				Rectangle bounds = new Rectangle(x, y, w, h);
				fillRoundRectangleDropShadow(graphics, bounds, 6, 4, 4);

				Rectangle row_bounds = new Rectangle(x, y + rowHeight * 2, w, rowHeight);
				Display disp = PlatformUI.getWorkbench().getDisplay();
				
				//Draw the detail
				AlfaRGB alfaRGB = style.getColor(TableStyle.STANDARD_COLOR_DETAIL);
				graphics.setAlpha(alfaRGB.getAlfa());
				Color swtColorDetail = new Color(disp, alfaRGB.getRgb());
				graphics.setBackground(swtColorDetail);
				graphics.fillRectangle(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);
				row_bounds = new Rectangle(x, y + rowHeight * 3, w, rowHeight);
			
				RGB c = null;
				Color swtColor = null;
				if (tableStyle.hasAlternateColor()) {
					alfaRGB =  style.getColor(TableStyle.COLOR_DETAIL);
					graphics.setAlpha(alfaRGB.getAlfa());
					c = alfaRGB.getRgb();
					swtColor = new Color(disp, c);
					graphics.setBackground(swtColor);
				}
				graphics.fillRectangle(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);
				row_bounds = new Rectangle(x, y + rowHeight * 4, w, rowHeight);
				if (swtColor != null)
					swtColor.dispose();
				graphics.setBackground(swtColorDetail);
				graphics.fillRectangle(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);
				swtColorDetail.dispose();

				// TABLE HEADER
				row_bounds = new Rectangle(x, y + rowHeight * 0, w, rowHeight);
				alfaRGB = style.getColor(TableStyle.COLOR_TABLE_HEADER);
				c = alfaRGB.getRgb();
				graphics.setAlpha(alfaRGB.getAlfa());
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);

				// TABLE FOOTER
				row_bounds = new Rectangle(x, y + rowHeight * 6, w, rowHeight);
				swtColor.dispose();
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);

				// COLUMN HEADER
				row_bounds = new Rectangle(x, y + rowHeight * 1, w, rowHeight);
				swtColor.dispose();
				alfaRGB = style.getColor(TableStyle.COLOR_COL_HEADER);
				c = alfaRGB.getRgb();
				graphics.setAlpha(alfaRGB.getAlfa());
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);

				// COLUMN FOOTER
				row_bounds = new Rectangle(x, y + rowHeight * 5, w, rowHeight);
				swtColor.dispose();
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);
				swtColor.dispose();

				alfaRGB = tableStyle.getRGBBorderColor();
				c = alfaRGB.getRgb();
				graphics.setAlpha(alfaRGB.getAlfa());
				swtColor = new Color(disp, c);
				graphics.setForeground(swtColor);
				// Draw border...
				for (int i = 0; i < 8; ++i) {
					graphics.drawLine(x, y + rowHeight * i, x + w, y + rowHeight * i);
				}

				h = rowHeight * 7;
				if (tableStyle.getBorderStyle() == BorderStyleEnum.FULL) {
					for (int i = 0; i < 3; ++i) {
						graphics.drawLine(x + (i * (w / 3)), y, x + (i * (w / 3)), y + h);
					}
					graphics.drawLine(x + w, y, x + w, y + h - 1);
				}
				if (tableStyle.getBorderStyle() == BorderStyleEnum.ONLY_HORIZONTAL) {
					graphics.drawLine(x, y, x, y + h);
					graphics.drawLine(x + w, y, x + w, y + h - 1);
				}
				swtColor.dispose();
			} finally {
				graphics.dispose();
			}
			ResourceManager.addImage(key, image);

		}
		return image;
	}

	/**
	 * Return the drop listener to handle the drag and drop of an element from the
	 * tab to the editor, it can be null if the drag operation is not wanted
	 * 
	 * @param viewer
	 *          the viewer of the editor
	 * @return the drop listener that will be added to the editor, it will handle
	 *         the drag of a tablestyle on a table
	 */
	@Override
	public AbstractTransferDropTargetListener getDropListener(EditPartViewer viewer) {
		return new TableStyleTransferDropListener(viewer);
	}

	/**
	 * Return an empty table style that can be used to build a real TableStyle
	 * starting from the XML reperesentation of a table Style
	 */
	@Override
	public TemplateStyle getBuilder() {
		return new TableStyle();
	}

	/**
	 * Return the icon image that will be used on the tab
	 * 
	 * @return and SWT icon
	 */
	@Override
	public Image getTabImage() {
		Image image = ResourceManager.getImage("table-style-16"); //$NON-NLS-1$
		if (image == null) {
			image = Activator.getDefault().getImageDescriptor("icons/table-style-16.png").createImage(); //$NON-NLS-1$
			ResourceManager.addImage("table-style-16", image); //$NON-NLS-1$
		}
		return image;
	}

	@Override
	public List<TemplateStyle> getStylesList(List<TemplateStyle> mixedList) {
		List<TemplateStyle> result = new ArrayList<TemplateStyle>();
		for (TemplateStyle style : mixedList) {
			if (style instanceof TableStyle)
				result.add(style);
		}
		return result;
	}

	@Override
	public void notifyChange(PropertyChangeEvent e) {
		if (e.getNewValue() instanceof TableStyle) {
			checkedGallery.clearAll();
			tableGroup = new GalleryItem(checkedGallery, SWT.NONE);
			checkedGallery.setRedraw(false);
			for (TemplateStyle style : getStylesList())
				if (style instanceof TableStyle)
					getItem(style, tableGroup);
			checkedGallery.setRedraw(true);
		}
	}
}
