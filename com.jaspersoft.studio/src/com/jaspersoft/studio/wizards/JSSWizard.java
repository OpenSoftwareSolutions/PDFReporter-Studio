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
package com.jaspersoft.studio.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class JSSWizard extends Wizard implements JSSWizardPageChangeListener {

	/**
	 * The key to identify the JasperReports configuration inside the wizard settings.
	 * 
	 * @see getConfig() is a shortcut to quickly get the configuration. By default, there is always one configuration in
	 *      the wizard, but subclasses may force the use of a local configuration.
	 */
	public static final String JASPERREPORTS_CONFIGURATION = "jasperreports_configuration"; //$NON-NLS-1$

	/**
	 * Key to retrieve the path of the directory in which resides the file being edited (or the location in which we are
	 * working in order to get a reference to a project).
	 * 
	 * The key points to an element of type IPath
	 * 
	 */
	public static final String FILE_PATH = "file_path";

	/**
	 * Key to retrieve the name of the file being edited
	 */
	public static final String FILE_NAME = "file_name";

	private IWizard parentWizard;
	private IWizardPage fallbackPage;
	private List<IWizard> childWizards = new ArrayList<IWizard>();

	private Map<String, Object> settings = new HashMap<String, Object>();

	/**
	 * Allows pages to share keyed objects for general porpuses.
	 * 
	 * @return
	 */
	public Map<String, Object> getSettings() {

		if (parentWizard != null && parentWizard instanceof JSSWizard) {
			return ((JSSWizard) parentWizard).getSettings();
		}

		return settings;
	}

	/**
	 * Allows pages to share keyed objects for general porpuses.
	 * 
	 * @return
	 */
	public void setSettings(Map<String, Object> map) {
		this.settings = map;
	}

	public JSSWizard() {
		super();
		setForcePreviousAndNextButtons(true);
	}

	public JSSWizard(IWizard parentWizard, IWizardPage fallbackPage) {
		this();
		this.parentWizard = parentWizard;
		this.fallbackPage = fallbackPage;
	}

	public IWizardPage hasNextPage(IWizardPage page) {
		IWizardPage wpage = getNextPage(page);
		if (wpage == null && parentWizard != null && fallbackPage != null) {
			return fallbackPage;
		}
		return wpage;
	}

	public IWizard getParentWizard() {
		return this.parentWizard;
	}

	protected void addChild(IWizard w) {
		if (!childWizards.contains(w))
			childWizards.add(w);
	}
	
	/**
	 * Return the list of the child wizards
	 */
	protected List<IWizard> getChildWizards(){
		return childWizards;
	}
	
	@Override
	/*
	 * (non-Javadoc) Method declared on IWizard. The default behavior is to return the page that was added to this wizard
	 * after the given page.
	 */
	public IWizardPage getNextPage(IWizardPage page) {

		IWizardPage nextPage = null;
		int index = wizardPages.indexOf(page);
		if (index >= 0 && index < wizardPages.size() - 1) {

			nextPage = (IWizardPage) wizardPages.get(index + 1);
		}

		// last page or page not found
		if (nextPage == null && parentWizard != null && fallbackPage != null) {
			if (parentWizard instanceof JSSWizard)
				((JSSWizard) parentWizard).addChild(this);
			nextPage = fallbackPage;
		}

		return nextPage;
	}

	@Override
	public boolean performFinish() {
		for (IWizard w : childWizards)
			w.performFinish();
		return true;
	}

	public void setConfig(JasperReportsConfiguration config) {

		if (config == null) {
			getSettings().remove(JASPERREPORTS_CONFIGURATION);
		} else {
			getSettings().put(JASPERREPORTS_CONFIGURATION, config);
		}
	}

	public JasperReportsConfiguration getConfig() {
		return (JasperReportsConfiguration) getSettings().get(JASPERREPORTS_CONFIGURATION);
	}

	public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) {
		try {
			getContainer().run(true, true, runnable);
		} catch (InvocationTargetException e) {
			UIUtils.showError(e.getCause());
		} catch (InterruptedException e) {
			UIUtils.showError(e.getCause());
		}
	}

	/**
	 * 
	 * @deprecated Use setConfig() instead
	 * @param jConfig
	 */
	public void init(JasperReportsConfiguration jConfig) {
		setConfig(jConfig);
	}

	private List<Command> commands;

	public void removeCommand(Command command) {
		if (parentWizard != null && parentWizard instanceof JSSWizard)
			((JSSWizard) parentWizard).removeCommand(command);
		if (commands != null)
			commands.remove(command);
	}

	public void addCommand(Command command) {
		if (parentWizard != null && parentWizard instanceof JSSWizard)
			((JSSWizard) parentWizard).addCommand(command);
		if (commands == null)
			commands = new ArrayList<Command>();
		commands.add(command);
	}

	public List<Command> getCommands() {
		return commands;
	}

	/**
	 * This wizard's list of pages (element type: <code>IWizardPage</code>).
	 */
	private List<IWizardPage> wizardPages = new ArrayList<IWizardPage>();

	/**
	 * Adds a new page to this wizard. The page is inserted at the end of the page list.
	 * 
	 * @param page
	 *          the new page
	 */
	public void addPage(IWizardPage page) {
		wizardPages.add(page);
		page.setWizard(this);

		if (page instanceof JSSWizardPage) {
			((JSSWizardPage) page).addChangeListener(this);

			IWizard wiz = this.getParentWizard();
			while (wiz != null && wiz instanceof JSSWizard) {
				JSSWizard parentw = (JSSWizard) wiz;
				((JSSWizardPage) page).addChangeListener(parentw);
				wiz = parentw.getParentWizard();
			}
		}

	}

	/**
	 * Adds a new page to this wizard. The page is inserted at the end of the page list.
	 * 
	 * @param page
	 *          the new page
	 */
	public void addPage(int index, IWizardPage page) {
		wizardPages.add(index, page);
		page.setWizard(this);

		if (page instanceof JSSWizardPage) {
			((JSSWizardPage) page).addChangeListener(this);
		}

		if (getContainer() != null && getContainer().getCurrentPage() != null) {
			getContainer().updateButtons();
		}
	}

	public void removePage(IWizardPage page) {
		// Check if the page to remove is the current one, in that case it is not possible to remove that page...
		if (getContainer() != null && getContainer().getCurrentPage() == page)
			return;

		if (page instanceof JSSWizardPage) {
			((JSSWizardPage) page).removeChangeListener(this);
		}

		wizardPages.remove(page);

		// Update the buttons if the page has been removed...
		if (getContainer() != null && getContainer().getCurrentPage() != null) {
			getContainer().updateButtons();
		}
	}

	/**
	 * The <code>Wizard</code> implementation of this <code>IWizard</code> method does nothing. Subclasses should extend
	 * if extra pages need to be added before the wizard opens. New pages should be added by calling <code>addPage</code>.
	 */
	public void addPages() {
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean canFinish() {
		// Default implementation is to check if all pages are complete.
		for (int i = 0; i < wizardPages.size(); i++) {
			if (!((IWizardPage) wizardPages.get(i)).isPageComplete()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * The <code>Wizard</code> implementation of this <code>IWizard</code> method creates all the pages controls using
	 * <code>IDialogPage.createControl</code>. Subclasses should reimplement this method if they want to delay creating
	 * one or more of the pages lazily. The framework ensures that the contents of a page will be created before
	 * attempting to show it.
	 */
	public void createPageControls(Composite pageContainer) {
		// the default behavior is to create all the pages controls
		for (int i = 0; i < wizardPages.size(); i++) {
			IWizardPage page = (IWizardPage) wizardPages.get(i);
			page.createControl(pageContainer);
			// page is responsible for ensuring the created control is
			// accessable
			// via getControl.
			Assert.isNotNull(page.getControl());
		}
	}

	/**
	 * The <code>Wizard</code> implementation of this <code>IWizard</code> method disposes all the pages controls using
	 * <code>DialogPage.dispose</code>. Subclasses should extend this method if the wizard instance maintains addition SWT
	 * resource that need to be disposed.
	 */
	public void dispose() {

		// notify pages
		for (int i = 0; i < wizardPages.size(); i++) {
			try {
				((IWizardPage) wizardPages.get(i)).dispose();
			} catch (Exception e) {
				Status status = new Status(IStatus.ERROR, Policy.JFACE, IStatus.ERROR, e.getMessage(), e);
				Policy.getLog().log(status);
			}
		}
		super.dispose();
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public IWizardPage getPage(String name) {
		for (int i = 0; i < wizardPages.size(); i++) {
			IWizardPage page = (IWizardPage) wizardPages.get(i);
			String pageName = page.getName();
			if (pageName.equals(name)) {
				return page;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public int getPageCount() {
		return wizardPages.size();
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public IWizardPage[] getPages() {
		return (IWizardPage[]) wizardPages.toArray(new IWizardPage[wizardPages.size()]);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public List<IWizardPage> getPageList() {
		return wizardPages;
	}

	/*
	 * (non-Javadoc) Method declared on IWizard. The default behavior is to return the page that was added to this wizard
	 * before the given page.
	 */
	public IWizardPage getPreviousPage(IWizardPage page) {
		int index = wizardPages.indexOf(page);
		if (index == 0 || index == -1) {
			// first page or page not found
			return null;
		}
		return (IWizardPage) wizardPages.get(index - 1);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard. By default this is the first page inserted into the wizard.
	 */
	public IWizardPage getStartingPage() {
		if (wizardPages.size() == 0) {
			return null;
		}
		return (IWizardPage) wizardPages.get(0);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean needsPreviousAndNextButtons() {
		return super.needsPreviousAndNextButtons() || wizardPages.size() > 1;
	}

	public void rearrangeSteps() {
		// Update the buttons if the page has been removed...
		if (getContainer() != null && getContainer().getCurrentPage() != null) {
			getContainer().updateButtons();
		}
	}

	/**
	 * When a JSS page UI changes, we may get notified with this method.
	 */
	@Override
	public void pageChanged(JSSWizardPageChangeEvent event) {
	}

}
