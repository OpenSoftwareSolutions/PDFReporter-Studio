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
package com.jaspersoft.studio.server.wizard.resource;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MReference;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.wizard.resource.page.AddResourcePage;
import com.jaspersoft.studio.server.wizard.resource.page.ResourceDescriptorPage;

public class AddResourceWizard extends Wizard {
	private AddResourcePage page0;

	public AddResourceWizard(ANode parent, boolean nested) {
		this(parent);
		setNested(nested);
	}

	public AddResourceWizard(ANode parent) {
		super();
		setWindowTitle(Messages.AddResourceWizard_windowtitle);
		setNeedsProgressMonitor(true);
		this.parent = parent;
	}

	private boolean skipFirstPage = false;
	private boolean nested = false;

	public void setNested(boolean nested) {
		this.nested = nested;
	}

	public void setSkipFirstPage(boolean skipFirstPage) {
		this.skipFirstPage = skipFirstPage;
	}

	@Override
	public IWizardPage getStartingPage() {
		if (skipFirstPage && page0 != null)
			return getNextPage(page0);
		return super.getStartingPage();
	}

	private boolean dsonly = false;

	public void setOnlyDatasource(boolean dsonly) {
		this.dsonly = dsonly;
	}

	private boolean ruOnly = false;

	public void setOnlyReportUnit(boolean ruOnly) {
		this.ruOnly = ruOnly;
	}

	private boolean monOnly = false;

	public void setMondrianOnly(boolean monOnly) {
		this.monOnly = monOnly;
	}

	private boolean olapOnly = false;

	public void setOlapOnly(boolean olapOnly) {
		this.olapOnly = olapOnly;
	}

	@Override
	public void addPages() {
		page0 = new AddResourcePage(parent);
		page0.setOnlyDatasource(dsonly);
		page0.setOnlyReportUnit(ruOnly);
		page0.setMondrianOnly(monOnly);
		page0.setOlapOnly(olapOnly);
		addPage(page0);

		addPage(new ResourceDescriptorPage());
	}

	private ResourceFactory rfactory = new ResourceFactory();
	private Map<Class<? extends MResource>, IWizardPage[]> pagemap = new HashMap<Class<? extends MResource>, IWizardPage[]>();

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == page0) {
			MResource r = page0.getResource();
			if (r != null) {
				int size = getPageCount();
				try {
					Field f = Wizard.class.getDeclaredField("pages");
					f.setAccessible(true); // FIXME, REALLY UGLY :( BUT IT'S
					// FASTER
					List<IWizardPage> wpages = (List<IWizardPage>) f.get(this);
					for (int i = 1; i < size; i++) {
						wpages.remove(1);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				IWizardPage[] rpage = pagemap.get(r.getClass());
				if (rpage == null) {
					rpage = rfactory.getResourcePage(parent, r);
					if (rpage != null)
						pagemap.put(r.getClass(), rpage);
				}
				if (rpage != null) {
					IWizardPage firstpage = null;
					for (IWizardPage p : rpage) {
						if (getPage(p.getName()) == null) {
							addPage(p);
							if (firstpage == null)
								firstpage = p;
						}
					}
					return firstpage;
				}
				return null;
			}
		}
		return super.getNextPage(page);
	}

	@Override
	public void dispose() {
		super.dispose();
		for (IWizardPage[] pages : pagemap.values())
			for (IWizardPage p : pages)
				p.dispose();
	}

	private ANode parent;

	public MResource getResource() {
		return page0.getResource();
	}

	@Override
	public boolean performFinish() {
		if (nested)
			return true;
		try {
			getContainer().run(false, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
					File tmpfile = null;
					try {
						MResource resource = getResource();
						if (parent instanceof MReportUnit && (resource instanceof MReference || resource.getValue().getIsReference())) {
							MReportUnit mrunit = (MReportUnit) parent;
							ResourceDescriptor runit = mrunit.getValue();
							runit.getChildren().add(resource.getValue());
							WSClientHelper.saveResource(mrunit, monitor);
						} else {
							resource.setParent(parent, -1);
							WSClientHelper.saveResource(resource, monitor);
						}
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					} finally {
						if (tmpfile != null)
							tmpfile.delete();
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			UIUtils.showError(e.getCause());
			return false;
		} catch (InterruptedException e) {
			UIUtils.showError(e);
			return false;
		}
		return true;
	}

	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage() == page0)
			return false;
		return super.canFinish();
	}

}
