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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.mihalis.opal.angles.AngleSlider;

import com.jaspersoft.studio.property.section.AbstractSection;

public class SPDegree extends SPNumber {
	private AngleSlider angleSlider;

	public SPDegree(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return composite;
	}

	protected void createComponent(Composite parent) {
		composite = section.getWidgetFactory().createComposite(parent);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.wrap = true;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.center = true;
		composite.setLayout(layout);

		angleSlider = new AngleSlider(composite, SWT.NONE) {
			public void setSelection(final int selection) {
				checkWidget();
				if (selection < 0 || selection > 360) {
					SWT.error(SWT.ERROR_CANNOT_SET_SELECTION);
				}

				try {
					// FIXME ugly hack, should remove this
					Field f = AngleSlider.class.getDeclaredField("selection");
					f.setAccessible(true); // solution
					f.set(this, selection);
					// this.selection = selection;
					Event event = new Event();
					event.widget = this;

					// FIXME ugly hack, should remove this
					Method method = AngleSlider.class.getDeclaredMethod("fireSelectionListeners", Event.class);
					method.setAccessible(true);
					method.invoke(this, event);
					// fireSelectionListeners(event);
					redraw();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

		};
		angleSlider.setToolTipText(pDescriptor.getDescription());
		angleSlider.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isRefresh)
					ftext.setText("" + angleSlider.getSelection());
			}
		});

		super.createComponent(composite);
	}

	private Composite composite;

	public void setDataNumber(Number f) {
		if (numType == null)
			numType = Double.class;
		super.setDataNumber(f);
		if (f != null) {
			int degree = Math.abs(f.intValue());
			if (degree > 360)
				degree = BigDecimal.valueOf(degree).remainder(BigDecimal.valueOf(360)).intValue();

			angleSlider.setSelection(degree);
		} else
			angleSlider.setSelection(0);
	}

}
