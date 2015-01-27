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
package com.jaspersoft.studio.data.remotexml;

import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.xml.RemoteXmlDataAdapter;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.ADataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.messages.Messages;
import com.jaspersoft.studio.jface.dialogs.LocaleDialog;
import com.jaspersoft.studio.jface.dialogs.TimeZoneDialog;
import com.jaspersoft.studio.property.descriptor.pattern.dialog.PatternEditor;
import com.jaspersoft.studio.swt.binding.LocaleToStringConverter;
import com.jaspersoft.studio.swt.binding.String2LocaleConverter;
import com.jaspersoft.studio.swt.binding.String2TimeZoneConverter;
import com.jaspersoft.studio.swt.binding.TimeZone2StringConverter;

public class RemoteXMLDataAdapterComposite extends ADataAdapterComposite {

	private Text textFileUrl;
	private Text textDatePattern;
	private Text textNumberPattern;
	private Text textLocale;
	private Text textTimeZone;
	private Locale locale = null;
	private TimeZone timeZone = null;
	private Button supportsNamespaces;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public RemoteXMLDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {

		super(parent, style, jrContext);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel.setText(Messages.RemoteXMLDataAdapterComposite_0);

		textFileUrl = new Text(composite, SWT.BORDER);
		textFileUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		supportsNamespaces = new Button(composite, SWT.CHECK);
		supportsNamespaces.setText(Messages.RemoteXMLDataAdapterComposite_NamespacesSupport);
		supportsNamespaces.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));


		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		composite_1.setLayout(new FillLayout(SWT.VERTICAL));

		@SuppressWarnings("unused")
		Label separator = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);

		Composite composite_3 = new Composite(this, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		GridLayout gl_composite_3 = new GridLayout(3, false);
		gl_composite_3.marginWidth = 0;
		gl_composite_3.marginHeight = 0;
		composite_3.setLayout(gl_composite_3);

		Label lblNewLabel_2 = new Label(composite_3, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel_2.setText(Messages.RemoteXMLDataAdapterComposite_1);

		textDatePattern = new Text(composite_3, SWT.BORDER);
		textDatePattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button btnCreateDatePattern = new Button(composite_3, SWT.NONE);
		GridData gd_btnCreateDatePattern = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_btnCreateDatePattern.widthHint = 100;
		btnCreateDatePattern.setLayoutData(gd_btnCreateDatePattern);
		btnCreateDatePattern.setText(Messages.RemoteXMLDataAdapterComposite_2);

		Label lblNewLabel_3 = new Label(composite_3, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel_3.setText(Messages.RemoteXMLDataAdapterComposite_3);

		textNumberPattern = new Text(composite_3, SWT.BORDER);
		textNumberPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button btnCreateNumberPattern = new Button(composite_3, SWT.NONE);
		GridData gd_btnCreateNumberPattern = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_btnCreateNumberPattern.widthHint = 100;
		btnCreateNumberPattern.setLayoutData(gd_btnCreateNumberPattern);
		btnCreateNumberPattern
				.setText(Messages.RemoteXMLDataAdapterComposite_4);

		Group grpLocaleTime = new Group(this, SWT.SHADOW_NONE);
		grpLocaleTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		grpLocaleTime.setText(Messages.RemoteXMLDataAdapterComposite_5);
		GridLayout gl_grpLocaleTime = new GridLayout(3, false);
		gl_grpLocaleTime.marginWidth = 0;
		gl_grpLocaleTime.marginHeight = 0;
		grpLocaleTime.setLayout(gl_grpLocaleTime);

		Label lblNewLabel_4 = new Label(grpLocaleTime, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel_4.setText(Messages.RemoteXMLDataAdapterComposite_6);

		textLocale = new Text(grpLocaleTime, SWT.BORDER);
		textLocale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		textLocale.setEnabled(false);

		Button btnSelectLocale = new Button(grpLocaleTime, SWT.NONE);
		GridData gd_btnSelectLocale = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnSelectLocale.widthHint = 100;
		btnSelectLocale.setLayoutData(gd_btnSelectLocale);
		btnSelectLocale.setText(Messages.RemoteXMLDataAdapterComposite_7);

		Label lblNewLabel_5 = new Label(grpLocaleTime, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel_5.setText(Messages.RemoteXMLDataAdapterComposite_8);

		textTimeZone = new Text(grpLocaleTime, SWT.BORDER);
		textTimeZone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		textTimeZone.setEnabled(false);

		Button btnSelectTimeZone = new Button(grpLocaleTime, SWT.NONE);
		GridData gd_btnSelectTimeZone = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_btnSelectTimeZone.widthHint = 100;
		btnSelectTimeZone.setLayoutData(gd_btnSelectTimeZone);
		btnSelectTimeZone.setText(Messages.RemoteXMLDataAdapterComposite_9);

		// UI elements listener

		btnCreateDatePattern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PatternEditor wizard = new PatternEditor();
				wizard.setNumberPatterns(false);
				wizard.setValue(textDatePattern.getText());
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				dialog.create();

				if (dialog.open() == Dialog.OK) {
					String val = wizard.getValue();
					textDatePattern.setText(val);
				}
			}
		});

		btnCreateNumberPattern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PatternEditor wizard = new PatternEditor();
				wizard.setDatePatterns(false);
				wizard.setValue(textNumberPattern.getText());
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				dialog.create();

				if (dialog.open() == Dialog.OK) {
					String val = wizard.getValue();
					textNumberPattern.setText(val);
				}
			}
		});

		btnSelectLocale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LocaleDialog localeDialog = new LocaleDialog(getShell(), locale);
				localeDialog.create();

				if (localeDialog.open() == Dialog.OK) {
					locale = localeDialog.getLocale();
					textLocale.setText(locale.getDisplayName());
				}
			}
		});

		btnSelectTimeZone.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TimeZoneDialog timeZoneDialog = new TimeZoneDialog(getShell(),
						timeZone);
				timeZoneDialog.create();

				if (timeZoneDialog.open() == Dialog.OK) {
					timeZone = timeZoneDialog.getTimeZone();
					if (timeZone != null) {
						textTimeZone.setText(timeZone.getID());
					}
				}
			}
		});
	}

	@Override
	protected void bindWidgets(DataAdapter dataAdapter) {
		RemoteXmlDataAdapter remoteXmlDataAdapter = (RemoteXmlDataAdapter) dataAdapter;

		bindingContext.bindValue(SWTObservables.observeSelection(supportsNamespaces),
				PojoObservables
						.observeValue(dataAdapter, "namespaceAware")); //$NON-NLS-1$
		bindingContext.bindValue(
				SWTObservables.observeText(textFileUrl, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "fileName")); //$NON-NLS-1$
		bindingContext.bindValue(
				SWTObservables.observeText(textDatePattern, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "datePattern")); //$NON-NLS-1$
		bindingContext.bindValue(
				SWTObservables.observeText(textNumberPattern, SWT.Modify),
				PojoObservables.observeValue(dataAdapter, "numberPattern")); //$NON-NLS-1$

		bindingContext
				.bindValue(
						SWTObservables.observeText(textLocale, SWT.Modify),
						PojoObservables.observeValue(dataAdapter, "locale"), new UpdateValueStrategy() //$NON-NLS-1$
								.setConverter(new String2LocaleConverter()),
						new UpdateValueStrategy()
								.setConverter(new LocaleToStringConverter()));

		bindingContext
				.bindValue(
						SWTObservables.observeText(textTimeZone, SWT.Modify),
						PojoObservables.observeValue(dataAdapter, "timeZone"), new UpdateValueStrategy() //$NON-NLS-1$
								.setConverter(new String2TimeZoneConverter()),
						new UpdateValueStrategy()
								.setConverter(new TimeZone2StringConverter()));

		locale = remoteXmlDataAdapter.getLocale();
		if (locale != null) {
			textLocale.setText(locale.getDisplayName());
		} else {
			textLocale.setText(Messages.RemoteXMLDataAdapterComposite_15
					+ Locale.getDefault().getDisplayName()
					+ Messages.RemoteXMLDataAdapterComposite_16);
			locale = Locale.getDefault();
		}

		timeZone = remoteXmlDataAdapter.getTimeZone();
		if (timeZone != null) {
			textTimeZone.setText(timeZone.getID());
		} else {
			textTimeZone.setText(TimeZone.getDefault().getID());
			timeZone = TimeZone.getDefault();
		}
	}

	public DataAdapterDescriptor getDataAdapter() {
		if (dataAdapterDesc == null)
			dataAdapterDesc = new RemoteXMLDataAdapterDescriptor();

		RemoteXmlDataAdapter remoteXmlDataAdapter = (RemoteXmlDataAdapter) dataAdapterDesc
				.getDataAdapter();

		remoteXmlDataAdapter.setFileName(textFileUrl.getText());
		remoteXmlDataAdapter.setDatePattern(textDatePattern.getText());
		remoteXmlDataAdapter.setNumberPattern(textNumberPattern.getText());
		remoteXmlDataAdapter.setLocale(locale);
		remoteXmlDataAdapter.setTimeZone(timeZone);

		return dataAdapterDesc;
	}
	
	@Override
	public String getHelpContextId() {
		return PREFIX.concat("adapter_xml_remote"); //$NON-NLS-1$
	}

}
