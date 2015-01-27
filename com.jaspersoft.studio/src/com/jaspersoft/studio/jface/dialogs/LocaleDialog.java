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
package com.jaspersoft.studio.jface.dialogs;

import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.WLocaleList;

public class LocaleDialog extends Dialog {
	
	private CTabFolder tabFolder;
	private CTabItem tbtmPredefinedLocales;
	private CTabItem tbtmCustomLocale;
	private WLocaleList wLocaleList;
	private Text textCustomLanguageCode;
	private Text textCustomCountryCode;
	private Text textCustomVariantCode;
	private Button buttonDefaultLocale;
	private Locale locale = null;

	/**
	 * Create the LocaleDialog.
	 * @param parentShell
	 * @param locale
	 */
	public LocaleDialog(Shell parentShell, Locale locale) {
		super(parentShell);
		this.locale = locale;
	}
	
	/**
	 * Configure Shell attributes like setText
	 */
	@Override
	protected void configureShell(Shell shell) {
    super.configureShell(shell);
    shell.setText(Messages.LocaleDialog_0);
  }

	/**
	 * Create contents of the LocaleDialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		// UI elements
		Composite container = (Composite) super.createDialogArea(parent);
		FillLayout fl_container = new FillLayout(SWT.HORIZONTAL);
		fl_container.marginWidth = 5;
		fl_container.marginHeight = 5;
		container.setLayout(fl_container);
		
		tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		tbtmPredefinedLocales = new CTabItem(tabFolder, SWT.NONE);
		tbtmPredefinedLocales.setText(Messages.LocaleDialog_1);
		
		Composite compositePredefinedLocales = new Composite(tabFolder, SWT.NONE);
		tbtmPredefinedLocales.setControl(compositePredefinedLocales);
		FillLayout fl_compositePredefinedLocales = new FillLayout(SWT.HORIZONTAL);
		fl_compositePredefinedLocales.marginWidth = 5;
		fl_compositePredefinedLocales.marginHeight = 5;
		compositePredefinedLocales.setLayout(fl_compositePredefinedLocales);
		
		wLocaleList = new WLocaleList(compositePredefinedLocales, SWT.NONE);
		
		tbtmCustomLocale = new CTabItem(tabFolder, SWT.NONE);
		tbtmCustomLocale.setText(Messages.LocaleDialog_2);
		
		Composite compositeCustomLocale = new Composite(tabFolder, SWT.NONE);
		tbtmCustomLocale.setControl(compositeCustomLocale);
		GridLayout gl_compositeCustomLocale = new GridLayout(3, false);
		compositeCustomLocale.setLayout(gl_compositeCustomLocale);
		
		Label lblNewLabel = new Label(compositeCustomLocale, SWT.NONE);
		lblNewLabel.setText(Messages.LocaleDialog_3 + Messages.LocaleDialog_4);
		
		textCustomLanguageCode = new Text(compositeCustomLocale, SWT.BORDER);
		GridData gd_txtCustomLanguageCode = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtCustomLanguageCode.minimumWidth = 100;
		textCustomLanguageCode.setLayoutData(gd_txtCustomLanguageCode);
		
		Label lblNewLabel_1 = new Label(compositeCustomLocale, SWT.NONE);
		lblNewLabel_1.setText(Messages.LocaleDialog_5);
		
		Label lblNewLabel_2 = new Label(compositeCustomLocale, SWT.NONE);
		lblNewLabel_2.setText(Messages.LocaleDialog_6);
		
		textCustomCountryCode = new Text(compositeCustomLocale, SWT.BORDER);
		GridData gd_textCustomCountryCode = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textCustomCountryCode.minimumWidth = 100;
		textCustomCountryCode.setLayoutData(gd_textCustomCountryCode);
		
		Label lblNewLabel_3 = new Label(compositeCustomLocale, SWT.NONE);
		lblNewLabel_3.setText(Messages.LocaleDialog_7);
		
		Label lblNewLabel_4 = new Label(compositeCustomLocale, SWT.NONE);
		lblNewLabel_4.setText(Messages.LocaleDialog_8);
		
		textCustomVariantCode = new Text(compositeCustomLocale, SWT.BORDER);
		GridData gd_textCustomVariantCode = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textCustomVariantCode.minimumWidth = 100;
		textCustomVariantCode.setLayoutData(gd_textCustomVariantCode);
		
		Label lblNewLabel_5 = new Label(compositeCustomLocale, SWT.NONE);
		lblNewLabel_5.setText(Messages.LocaleDialog_9);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		new Label(compositeCustomLocale, SWT.NONE);
		
		Label lblNewLabel_6 = new Label(compositeCustomLocale, SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		lblNewLabel_6.setText(Messages.LocaleDialog_10 + Messages.LocaleDialog_11);
		
		// UI elements listeners
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				CTabItem cTabItem = (CTabItem)e.item;
				if (cTabItem == tbtmPredefinedLocales) {
					getButton(OK).setEnabled(true);
				} else {
					getButton(OK).setEnabled(false);
				}
			}
		});
		
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				okPressed();
			}
		};
		wLocaleList.setListSelectionListener(selectionAdapter);
		
		// init UI elements values
		initElements();
		
		return container;
	}
	
  private void initElements() {
		
		if (locale != null) {
			
			// if locale is predefined...
			if (wLocaleList.contains(locale)) {
				
				// set selection to predefined locale
				wLocaleList.setSelection(locale);
				
				// set selection to predefined locales tab
				tabFolder.setSelection(0);
				
			} else { // ...if locale is custom
				
			  String language = locale.getLanguage();
				String country = locale.getCountry();
				String variant = locale.getVariant();
					
				if (language != null && language.length() > 0) {
					textCustomLanguageCode.setText(language);
				}
				if (country != null && country.length() > 0) {
					textCustomCountryCode.setText(country);
				}
				if (variant != null && variant.length() > 0) {
					textCustomVariantCode.setText(variant);
				}
				
			  // set selection to custom locale tab
				tabFolder.setSelection(1);
			}
			
		} else {
			
		  // set selection to system default locale
			wLocaleList.setSelection(Locale.getDefault());
			
		  // set selection to predefined locales tab
			tabFolder.setSelection(0);
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		buttonDefaultLocale = createButton(parent, 2, Messages.LocaleDialog_12, false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		
		buttonDefaultLocale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				wLocaleList.setSelection(Locale.getDefault());
			  
				// set selection to predefined locales tab
				tabFolder.setSelection(0);
				
				okPressed();
			}
		});
		
		textCustomLanguageCode.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				
				getButton(OK).setEnabled(isCustomLocaleValid());
			}
		});
		
		textCustomCountryCode.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				
				getButton(OK).setEnabled(isCustomLocaleValid());
			}
		});
		
		textCustomVariantCode.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				
				getButton(OK).setEnabled(isCustomLocaleValid());
			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	@Override
	protected void okPressed() {
		
		CTabItem tabItem = tabFolder.getSelection();
		
		// set a predefined locale...
		if (tabItem.equals(tbtmPredefinedLocales)) {
			
			locale = wLocaleList.getSelectedLocale();
			
		} else { // ...set a custom locale
			
			String language = textCustomLanguageCode.getText();
			String country = textCustomCountryCode.getText();
			String variant = textCustomVariantCode.getText();
			
			if (language != null && language.trim().length() > 0) {
				if (country != null && language.trim().length() > 0) {
					if (variant != null && variant.trim().length() > 0) {
						locale = new Locale(language, country, variant);
					} else {
						locale = new Locale(language, country);
					}
				} else {
					locale = new Locale(language);
				}
			}
			// language is a mandatory field. If it is not filled,
			// just return the selected predefined locale. 
			else { 
				locale = wLocaleList.getSelectedLocale();
			}
		}
		super.okPressed();
	}
	
	/**
	 * This method check the validity of a custom locale. Rules are:
	 * <br>- Language is the only required field
	 * <br>- If you fill Country, Language must be filled
	 * <br>- If you fill Variant, both Language and Country must be filled
	 * @return true or false
	 */
	private boolean isCustomLocaleValid() {
		
		String language = textCustomLanguageCode.getText();
		String country = textCustomCountryCode.getText();
		String variant = textCustomVariantCode.getText();
		
		/*if ( (language != null && language.trim().length() > 0)
				 ||
			   (
			  	((language != null && language.trim().length() > 0))
			  	&&((country != null && country.trim().length() > 0))
			   )
			   ||
			   (
					((language != null && language.trim().length() > 0))
					&&((country != null && country.trim().length() > 0))
					&&((variant != null && variant.trim().length() > 0))
				 )
			 ) {
			return true;
		}	else {
			return false;
		}*/
		
		if (variant != null && variant.trim().length() > 0) {
			if (country != null && country.trim().length() > 0) {
				if (language != null && language.trim().length() > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	// GETTERS AND SETTERS
	/**
	 * Return the locale created by the LocaleDialog.
	 * @return locale
	 */
	public Locale getLocale() {
		return locale;
	}
}
