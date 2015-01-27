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
package com.jaspersoft.studio.components.crosstab.figure;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
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
import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.crosstab.command.wizard.CrosstabStyleWizard;
import com.jaspersoft.studio.components.crosstab.model.dialog.CrosstabStyle;
import com.jaspersoft.studio.editor.style.TemplateStyle;
import com.jaspersoft.studio.style.view.TemplateStyleView;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * Extension to show inside a gallery a list of CrosstabStyle that can be drag
 * and dropped on a Table to apply them
 * 
 * @author Orlandin Marco
 * 
 */
public class CrosstabStyleView extends CommonViewProvider {

	/**
	 * Height of every image in the gallery
	 */
	private static final int GALLERY_HEIGHT = 80;

	/**
	 * Width of every image in the gallery
	 */
	private static final int GALLERY_WIDTH = 100;

	/**
	 * The gallery root item
	 */
	private GalleryItem tableGroup;

	/**
	 * Create a gallery with inside all the crosstab styles with their previews
	 */
	@Override
	public void createControls(Composite parent) {
		super.createControls(parent, GALLERY_WIDTH, GALLERY_HEIGHT, Messages.CrosstabStyleView_0);
		addDragSupport();
	}

	protected void doCreate() {
		CrosstabStyleWizard wizard = new CrosstabStyleWizard(true, null);
		WizardDialog dialog = getEditorDialog(wizard);
		if (dialog.open() == Dialog.OK) {
			CrosstabStyle newStyle = wizard.getTableStyle();
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
		CrosstabStyleWizard wizard = new CrosstabStyleWizard(true, oldStyle);
		WizardDialog dialog = getEditorDialog(wizard);
		if (dialog.open() == Dialog.OK) {
			CrosstabStyle newStyle = wizard.getTableStyle();
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
		return "Crosstab Styles"; //$NON-NLS-1$
	}

	public List<TemplateStyle> getStylesList() {
		List<TemplateStyle> result = new ArrayList<TemplateStyle>();
		Collection<TemplateStyle> savedStyles = TemplateStyleView.getTemplateStylesStorage().getStylesDescriptors();
		for (TemplateStyle style : savedStyles)
			if (style instanceof CrosstabStyle)
				result.add(style);
		return result;
	}

	/**
	 * Called when the styles need to be inserted in the gallery. Here are passed
	 * all the template styles read from the properties file, so only the one with
	 * type CrosstabStyle will be shown
	 * 
	 * @param styles
	 *          a list of all the TemplateStyles read from the properties file
	 */
	@Override
	public void fillStyles(Collection<TemplateStyle> styles) {
		tableGroup = new GalleryItem(checkedGallery, SWT.NONE);
		checkedGallery.setRedraw(false);
		for (TemplateStyle style : styles)
			if (style instanceof CrosstabStyle)
				getItem(style, tableGroup);
		checkedGallery.setRedraw(true);
	}

	/**
	 * Build a preview image of a Crosstab
	 * 
	 * @param style
	 *          the style of the Crosstab
	 * @return a preview SWT image of the Crosstab
	 */
	@Override
	public Image generatePreviewFigure(final TemplateStyle style) {
		String key = "crosstabTemplates_" + style.toString(); //$NON-NLS-1$
		Image image = ResourceManager.getImage(key);
		if (image == null && style instanceof CrosstabStyle) {
			CrosstabStyle crosstabStyle = (CrosstabStyle) style;
			image = new Image(null, new org.eclipse.swt.graphics.Rectangle(0, 0, GALLERY_WIDTH, GALLERY_HEIGHT));
			GC graphics = new GC(image);
			try {
				int y = 1;
				int x = 1;
				int w = GALLERY_WIDTH - 6;
				int h = GALLERY_HEIGHT - 6;
				int rowHeight = h / 4;
				int rowWidth = w / 4;
				// I recalculate the total width and height with the rounded values;
				w = rowWidth * 4;
				h = rowHeight * 4;
				// Draw the shadow
				Rectangle bounds = new Rectangle(x, y, w, h);
				fillRoundRectangleDropShadow(graphics, bounds, 6, 4, 4);
				RGB c = null;
				Color swtColor;
				Display disp = PlatformUI.getWorkbench().getDisplay();
				// First square
				Rectangle firstSquare = new Rectangle(x, y, rowWidth, rowHeight);
				graphics.setBackground(checkedGallery.getBackground());
				graphics.fillRectangle(firstSquare.x, firstSquare.y, firstSquare.width, firstSquare.width);

				// Last row and column
				Rectangle lastRow = new Rectangle(x, y + rowHeight * 3, w, rowHeight);
				Rectangle lastCol = new Rectangle(x + rowWidth * 3, y, rowWidth, h);
				AlfaRGB alfaRGB =  style.getColor(CrosstabStyle.COLOR_TOTAL);
				c = alfaRGB.getRgb();
				graphics.setAlpha(alfaRGB.getAlfa());
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(lastRow.x, lastRow.y, lastRow.width, lastRow.height);
				graphics.fillRectangle(lastCol.x, lastCol.y, lastCol.width, lastCol.height);

				// column and row before the last
				Rectangle beforeLastRow = new Rectangle(x, y + rowHeight * 2, rowWidth * 3, rowHeight);
				Rectangle beforeLastCol = new Rectangle(x + rowWidth * 2, y, rowWidth, rowHeight * 3);
				swtColor.dispose();
				alfaRGB = style.getColor(CrosstabStyle.COLOR_GROUP);
				c = alfaRGB.getRgb();
				graphics.setAlpha(alfaRGB.getAlfa());
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(beforeLastRow.x, beforeLastRow.y, beforeLastRow.width, beforeLastRow.height);
				graphics.fillRectangle(beforeLastCol.x, beforeLastCol.y, beforeLastCol.width, beforeLastCol.height);

				// detail cell
				Rectangle detail = new Rectangle(x + rowWidth, y + rowHeight, rowWidth, rowHeight);
				swtColor.dispose();
				alfaRGB = style.getColor(CrosstabStyle.COLOR_DETAIL);
				c = alfaRGB.getRgb();
				graphics.setAlpha(alfaRGB.getAlfa());
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(detail.x, detail.y, detail.width, detail.height);

				// Measure cells
				Rectangle measure1 = new Rectangle(x, y + rowHeight, rowWidth, rowHeight);
				Rectangle measure2 = new Rectangle(x + rowWidth, y, rowWidth, rowHeight);
				swtColor.dispose();
				alfaRGB = style.getColor(CrosstabStyle.COLOR_MEASURES);
				c = alfaRGB.getRgb();
				graphics.setAlpha(alfaRGB.getAlfa());
				swtColor = new Color(disp, c);
				graphics.setBackground(swtColor);
				graphics.fillRectangle(measure1.x, measure1.y, measure1.width, measure1.height);
				graphics.fillRectangle(measure2.x, measure2.y, measure2.width, measure2.height);
				swtColor.dispose();
				if (crosstabStyle.isShowGrid()) {

					if (crosstabStyle.getWhiteGrid())
						graphics.setForeground(ColorConstants.white);
					else
						graphics.setForeground(ColorConstants.black);
					// Draw border...
					for (int i = 0; i < 5; i++) {
						if (i == 0)
							graphics.drawLine(x + rowWidth, y + rowHeight * i, x + w, y + rowHeight * i);
						else
							graphics.drawLine(x, y + rowHeight * i, x + w, y + rowHeight * i);
					}

					for (int i = 0; i < 5; i++) {
						if (i == 0)
							graphics.drawLine(x + rowWidth * i, y + rowHeight, x + rowWidth * i, y + h);
						else
							graphics.drawLine(x + rowWidth * i, y, x + rowWidth * i, y + h);
					}
				}
			} finally {
				graphics.dispose();
			}
			ResourceManager.addImage(key, image);
		}
		return image;
	}

	/**
	 * Add the drag support to the element of the gallery
	 */
	private void addDragSupport() {
		int operations = DND.DROP_MOVE;
		final Transfer[] types = new Transfer[] { CrosstrabRestrictedTransferType.getInstance() };
		DragSource source = new DragSource(checkedGallery, operations);
		source.setTransfer(types);
		source.addDragListener(new StyleDragListener());
	}

	/**
	 * Return the drop listener to handle the drag and drop of an element from the
	 * tab to the editor, it can be null if the drag operation is not wanted
	 * 
	 * @param viewer
	 *          the viewer of the editor
	 * @return the drop listener that will be added to the editor, it will handle
	 *         the drag of a crosstabstyle on a crosstab
	 */
	@Override
	public AbstractTransferDropTargetListener getDropListener(EditPartViewer viewer) {
		return new CrosstabStyleTransferDropListener(viewer);
	}

	/**
	 * Return an empty crosstab style that can be used to build a real
	 * CrosstabStyle starting from the XML representation of a crosstab Style
	 */
	@Override
	public TemplateStyle getBuilder() {
		return new CrosstabStyle();
	}

	/**
	 * Return the icon image that will be used on the tab
	 * 
	 * @return and SWT icon
	 */
	@Override
	public Image getTabImage() {
		Image image = ResourceManager.getImage("crosstab-style-16"); //$NON-NLS-1$
		if (image == null) {
			image = Activator.getDefault().getImageDescriptor("icons/crosstab-style-16.png").createImage(); //$NON-NLS-1$
			ResourceManager.addImage("crosstab-style-16", image); //$NON-NLS-1$
		}
		return image;
	}

	@Override
	public List<TemplateStyle> getStylesList(List<TemplateStyle> mixedList) {
		List<TemplateStyle> result = new ArrayList<TemplateStyle>();
		for (TemplateStyle style : mixedList) {
			if (style instanceof CrosstabStyle)
				result.add(style);
		}
		return result;
	}

	@Override
	public void notifyChange(PropertyChangeEvent e) {
		if (e.getNewValue() instanceof CrosstabStyle) {
			checkedGallery.clearAll();
			tableGroup = new GalleryItem(checkedGallery, SWT.NONE);
			checkedGallery.setRedraw(false);
			for (TemplateStyle style : getStylesList())
				if (style instanceof CrosstabStyle)
					getItem(style, tableGroup);
			checkedGallery.setRedraw(true);
		}
	}
}
