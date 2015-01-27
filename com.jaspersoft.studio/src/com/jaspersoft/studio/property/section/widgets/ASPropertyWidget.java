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


import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.internal.IHighlightPropertyWidget;
import com.jaspersoft.studio.property.combomenu.ComboButton;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.UIUtil;

public abstract class ASPropertyWidget implements IHighlightPropertyWidget {
	protected IPropertyDescriptor pDescriptor;
	protected AbstractSection section;

	public ASPropertyWidget(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		this.pDescriptor = pDescriptor;
		this.section = section;
		createComponent(parent);
		if (getControl() != null)
			setupFocusControl(pDescriptor, getControl());
	}

	protected void setupFocusControl(IPropertyDescriptor pDescriptor, Control c) {
		if (c.isEnabled()) {
			c.addFocusListener(focusListener);
			HelpSystem.bindToHelp(pDescriptor, c);
		}
		if (c instanceof Composite) {
			for (Control cc : ((Composite) c).getChildren())
				setupFocusControl(pDescriptor, cc);
		}
	}

	public void setReadOnly(boolean readonly) {
		if (getControl() != null)
			getControl().setEnabled(!readonly);
	}

	protected abstract void createComponent(Composite parent);

	public abstract void setData(APropertyNode pnode, Object value);

	public String getId() {
		return pDescriptor.getId().toString();
	}

	public String getName() {
		return pDescriptor.getDisplayName();
	}

	private CLabel label;

	public CLabel getLabel() {
		return label;
	}

	public void setLabel(CLabel label) {
		this.label = label;
	}

	protected FocusListener focusListener = new FocusListener() {

		@Override
		public void focusLost(FocusEvent e) {
			handleFocusLost();
		}

		@Override
		public void focusGained(FocusEvent e) {
			handleFocusGained();
		}
	};

	private IStatusLineManager getStatusLineManager() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

		IWorkbenchPage page = win.getActivePage();

		IWorkbenchPart part = page.getActivePart();
		if (part == null)
			return null;
		IWorkbenchPartSite site = part.getSite();
		IActionBars actionBars = null;
		if (site instanceof IEditorSite)
			actionBars = ((IEditorSite) site).getActionBars();
		else if (site instanceof IViewSite)
			actionBars = ((IViewSite) site).getActionBars();
		if (actionBars == null)
			return null;
		return actionBars.getStatusLineManager();
	}

	protected void handleFocusGained() {
		IStatusLineManager statusLineManager = getStatusLineManager();
		if (statusLineManager != null)
			statusLineManager.setMessage(pDescriptor.getDescription());
	}

	protected void handleFocusLost() {
		IStatusLineManager statusLineManager = getStatusLineManager();
		if (statusLineManager != null)
			statusLineManager.setMessage(null);
	}

	public abstract Control getControl();

	/**
	 * Since a property widget can have many controls inside it, this method return the control to which a border will be
	 * added to highlight the widget
	 * 
	 * @return control to border
	 */
	public Control getControlToBorder() {
		return getControl();
	}

	/**
	 * According to the type of the control to highlight will be returned an object that offer 
	 * the functionality to put a border on the widget or to set its background, to highlight it
	 * 
	 * @return An object that offer the functionality to highlight the widget
	 */
	 public static IHighlightControl getControlHighlight(Control control) {
		if (control.getClass().equals(Spinner.class)) return new BackgroundHighlight(control);
		if (control.getClass().equals(Text.class)) return new BackgroundHighlight(control);
		if (control.getClass().equals(ToolBar.class)) return new BackgroundHighlight(control);
		if (control.getClass().equals(Combo.class) && !((control.getStyle() & SWT.READ_ONLY) == SWT.READ_ONLY)) return new BackgroundHighlight(control);
		if (control.getClass().equals(Button.class) && ((control.getStyle() & SWT.CHECK) == SWT.CHECK)) return new BackgroundHighlight(control);
		if (control.getClass().equals(Button.class) && ((control.getStyle() & SWT.PUSH) == SWT.PUSH)) return new BorderHightLight(control, Combo.class);
		if (control.getClass().equals(ComboButton.GraphicButton.class)) return new BackgroundHighlight(control);
		if (control instanceof Composite) return new BorderHightLight(control);
		if (control instanceof Button) return new BorderHightLight(control);
		return null;
	}

	/**
	 * highlight the widget by changing its background or by drawing a border around it for a fixed (depending from the widget)
	 * amount of time
	 * 
	 */
	@Override
	public void highLightWidget(long ms) {
		// if there isn't a control defined where add the border then return
		if (getControlToBorder() == null) return;
		final IHighlightControl highLight = getControlHighlight(getControlToBorder());
		if (highLight == null) return;
		//highlight the control
		highLight.highLightControl();
		final long sleepTime = ms;
		// Create a thread to remove the paint listener after specified time
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(sleepTime);
					// It need two thread to avoid to freeze the UI during the sleep
					getControlToBorder().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							highLight.deHighLightControl();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private static int defCharWidth = -1;

	public static int getCharWidth(Control c) {
		if (defCharWidth < 0)
			defCharWidth = UIUtil.getCharWidth(c);
		return defCharWidth;
	}
	
	/**
	 * Toggle the visibility of the property widget.
	 */
	public void toggleVisibility(boolean show) {
		// widget label
		if(getLabel().getLayoutData() instanceof GridData) {
			((GridData) getLabel().getLayoutData()).exclude = !show;
		}
		getLabel().setVisible(show);
		getLabel().setEnabled(show);
		// widget control
		if(getControl().getLayoutData() instanceof GridData) {
			((GridData) getControl().getLayoutData()).exclude = !show;
		}
		getControl().setVisible(show);
		getControl().setEnabled(show);
	}
}
