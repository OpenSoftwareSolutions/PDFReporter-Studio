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
package com.jaspersoft.studio.property.section.widgets;

import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignFont;
import net.sf.jasperreports.engine.util.JRStyleResolver;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.jface.IntegerCellEditorValidator;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.preferences.fonts.FontsPreferencePage;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;
import com.jaspersoft.studio.property.descriptor.combo.FontNamePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * This class implement the subsection into the cart property tab, for the font name is used a standard combo.
 * 
 * @author Chicu Veaceslav & Orlandin Marco
 * 
 */
public class SPFont extends ASPropertyWidget {
	private final class PreferenceListener implements IPropertyChangeListener {

		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			if (event.getProperty().equals(FontsPreferencePage.FPP_FONT_LIST)) {
				if (parentNode != null)
					fontName.setItems(parentNode.getJasperConfiguration().getFontList());
			}
		}
	}

	private PreferenceListener preferenceListener;

	/**
	 * The combo popup with the font names
	 */
	private Combo fontName;

	/**
	 * The combo with the font size
	 */
	private Combo fontSize;

	/**
	 * Buttom for the attribute bold
	 */
	private ToolItem boldButton;

	/**
	 * Button for the attribute italic
	 */
	private ToolItem italicButton;

	/**
	 * Button for the attribute underline
	 */
	private ToolItem underlineButton;

	/**
	 * Button for the attribute striketrought
	 */
	private ToolItem strikeTroughtButton;

	/**
	 * Flag to check if the font name was already been inserted into the combo popup
	 */
	private boolean itemsSetted;

	/**
	 * Font model
	 */
	private MFont mfont;

	/**
	 * Node represented
	 */
	private APropertyNode parentNode;

	/**
	 * Composite where the control will be placed
	 */
	private Composite group;

	public SPFont(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
		preferenceListener = new PreferenceListener();
		JaspersoftStudioPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(preferenceListener);
		itemsSetted = false;
	}

	@Override
	public Control getControl() {
		return group.getParent();
	}

	/**
	 * Property change action
	 * 
	 * @param section
	 * @param property
	 * @param value
	 * @param pd
	 */
	public void propertyChange(AbstractSection section, String property, String value, FontNamePropertyDescriptor pd) {
		changeProperty(section, pDescriptor.getId(), pd.getId(), value);
	}

	/**
	 * The increment\decrement font size button, adapted to the chart font structure
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	private class SPChartButtom extends SPButton {

		/**
		 * The type of font represented (title, legend, subtitle)
		 */
		private String fontNameProperty;

		public SPChartButtom(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor,
				APropertyNode fontValue, String fontNameProperty) {
			super(parent, section, pDescriptor, fontValue);
			this.fontNameProperty = fontNameProperty;
		}

		/**
		 * The ovverrided version first change the font into a temp. mfont item, the use this object to replace the font
		 * size inside the chart model
		 */
		@Override
		protected void createCommand(boolean increment) {
			Object fontSizeString = fontSize.getText();
			Integer newValue = 2;
			if (fontSizeString != null && fontSizeString.toString().length() > 0) {
				newValue = Integer.valueOf(fontSizeString.toString());
				Integer plus = null;
				if (increment)
					plus = Math.round((new Float(newValue) / 100) * SPButton.factor) + 1;
				else
					plus = Math.round((new Float(newValue) / 100) * -SPButton.factor) - 1;
				if ((newValue + plus) > 99)
					newValue = 99;
				else if ((newValue + plus) > 0)
					newValue += plus;
				section.changePropertyOn(JRBaseFont.PROPERTY_FONT_SIZE, newValue.toString(), mfont);
				section.changePropertyOn(fontNameProperty, new MFont((JRFont) mfont.getValue()), parentNode);
			}
		}
	}

	/**
	 * Given a combo and and a string return the index of the string in the combo
	 * 
	 * @param combo
	 * @param searchedString
	 * @return the index of the string in the combo, or 0 if the string is not found
	 */
	private int indexOf(Combo combo, String searchedString) {
		String[] elements = combo.getItems();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].equals(searchedString)) {
				return i;
			}
		}
		return 0;
	}

	protected void createComponent(Composite parent) {
		mfont = new MFont(new JRDesignFont(null));
		mfont.setJasperConfiguration(section.getElement().getJasperConfiguration());
		group = section.getWidgetFactory().createSection(parent, pDescriptor.getDisplayName(), true, 3);

		final FontNamePropertyDescriptor pd = (FontNamePropertyDescriptor) mfont
				.getPropertyDescriptor(JRBaseStyle.PROPERTY_FONT_NAME);
		fontName = new Combo(group, SWT.NONE);
		fontName.setToolTipText(pd.getDescription());
		fontName.addModifyListener(new ModifyListener() {

			private int time = 0;

			public void modifyText(ModifyEvent e) {
				if (e.time - time > 100) {
					String value = fontName.getText();
					if (!value.equals(FontUtils.separator))
						propertyChange(section, JRBaseFont.PROPERTY_FONT_NAME, value, pd);
					else
						fontName.select(indexOf(fontName, (String) mfont.getPropertyActualValue(JRBaseFont.PROPERTY_FONT_NAME)));
					int stringLength = fontName.getText().length();
					fontName.setSelection(new Point(stringLength, stringLength));
				}
				time = e.time;
			}
		});

		final RWComboBoxPropertyDescriptor pd1 = (RWComboBoxPropertyDescriptor) mfont
				.getPropertyDescriptor(JRBaseStyle.PROPERTY_FONT_SIZE);

		Composite fontSizeLayout = new Composite(group, SWT.NONE);
		GridData fontSizeData = new GridData();
		fontSizeData.widthHint = 65;
		fontSizeData.minimumWidth = 65;
		fontSizeLayout.setLayout(new GridLayout(1, false));
		fontSizeLayout.setLayoutData(fontSizeData);
		fontSize = section.getWidgetFactory().createCombo(fontSizeLayout, SWT.FLAT);
		fontSize.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fontSize.setItems(pd1.getItems());
		fontSize.addModifyListener(new ModifyListener() {
			private int time = 0;

			public void modifyText(ModifyEvent e) {
				if (e.time - time > 100) {
					String value = fontSize.getText();
					if (IntegerCellEditorValidator.instance().isValid(value) == null)
						changeProperty(section, pDescriptor.getId(), pd1.getId(), value);
					int stringLength = fontSize.getText().length();
					fontSize.setSelection(new Point(stringLength, stringLength));
				}
				time = e.time;
			}
		});
		fontSize.setToolTipText(pd1.getDescription());

		/*
		 * Button to increment\decrment the font size
		 */
		new SPChartButtom(group, section, pd1, mfont, pDescriptor.getId().toString());

		ToolBar toolBar = new ToolBar(group, SWT.FLAT | SWT.WRAP | SWT.LEFT);
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		toolBar.setLayoutData(gd);

		boldButton = createItem(toolBar, JRBaseStyle.PROPERTY_BOLD, "icons/resources/edit-bold.png");

		italicButton = createItem(toolBar, JRBaseStyle.PROPERTY_ITALIC, "icons/resources/edit-italic.png");

		underlineButton = createItem(toolBar, JRBaseStyle.PROPERTY_UNDERLINE, "icons/resources/edit-underline.png");

		strikeTroughtButton = createItem(toolBar, JRBaseStyle.PROPERTY_STRIKE_THROUGH, "icons/resources/edit-strike.png");
	}

	/**
	 * Create a tool bar button
	 * 
	 * @param toolBar
	 *          the parent tool bar
	 * @param id
	 *          the id of the property changed by the button press
	 * @param image
	 *          the image of the tool button
	 * @return the created tool button
	 */
	private ToolItem createItem(ToolBar toolBar, Object id, String image) {
		final IPropertyDescriptor ipd = mfont.getPropertyDescriptor(id);

		final ToolItem item = new ToolItem(toolBar, SWT.CHECK);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changeProperty(section, pDescriptor.getId(), ipd.getId(), new Boolean(item.getSelection()));
			}
		});
		item.setImage(JaspersoftStudioPlugin.getInstance().getImage(image)); //$NON-NLS-1$
		item.setToolTipText(ipd.getDescription());
		return item;
	}

	private void changeProperty(AbstractSection section, Object property, Object prop, Object value) {
		section.changePropertyOn(prop, value, mfont);
		if (property != null && parentNode != null)
			section.changePropertyOn(property, new MFont((JRFont) mfont.getValue()), parentNode);
	}

	/**
	 * Set the font name, the font size and the font attribute in the respective controls
	 */
	public void setData(APropertyNode pnode, Object value) {
		this.parentNode = pnode;
		this.mfont = (MFont) value;
		if (mfont != null) {

			JRFont fontValue = (JRFont) mfont.getValue();

			if (!itemsSetted) {
				fontName.setItems(parentNode.getJasperConfiguration().getFontList());
				itemsSetted = true;
			}
			String strfontname = JRStyleResolver.getFontName(fontValue);
			fontName.setText(strfontname);

			String strfontsize = Integer.toString(JRStyleResolver.getFontSize(fontValue));
			fontSize.setText(strfontsize != null ? strfontsize : "");

			Boolean b = JRStyleResolver.isBold(fontValue);
			boldButton.setSelection(b != null ? b.booleanValue() : false);
			b = JRStyleResolver.isItalic(fontValue);
			italicButton.setSelection(b != null ? b.booleanValue() : false);
			b = JRStyleResolver.isUnderline(fontValue);
			underlineButton.setSelection(b != null ? b.booleanValue() : false);
			b = JRStyleResolver.isStrikeThrough(fontValue);
			strikeTroughtButton.setSelection(b != null ? b.booleanValue() : false);
		}
	}
}
