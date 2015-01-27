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
package com.jaspersoft.studio.server.publish.wizard.page;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.ui.validator.IDStringValidator;
import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.ServerProvider;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.action.resource.RefreshResourcesAction;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MContentResource;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.MServers;
import com.jaspersoft.studio.server.publish.PublishUtil;
import com.jaspersoft.studio.server.utils.ValidationUtils;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

public class RFileLocationPage extends JSSHelpWizardPage {
	private TreeViewer treeViewer;
	private Text ruLabel;

	private RefreshResourcesAction refreshAction;
	private JasperReportsConfiguration jConfig;
	private Text ruID;
	private Text ruDescription;

	private boolean isFillingInput;
	private boolean canSuggestID;
	private IFile file;

	public RFileLocationPage(JasperReportsConfiguration jConfig) {
		super("serverfilepublish"); //$NON-NLS-1$
		setTitle(Messages.RUnitLocationPage_title);
		setDescription("Select JasperReports Server and location where file will be published");
		this.jConfig = jConfig;
	}

	public void refreshFile() {
		file = (IFile) jConfig.get(FileUtils.KEY_FILE);
		look4SelectedUnit();
	}

	public AFileResource getSelectedNode() {
		return fileRes;
	}

	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_SELECT_SERVER;
	}

	@Override
	public boolean isPageComplete() {
		boolean isC = super.isPageComplete() && getErrorMessage() == null;
		if (isC)
			isC = isPageCompleteLogic();
		return isC;
	}

	protected boolean isPageCompleteLogic() {
		boolean isC;
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		Object firstElement = ts.getFirstElement();
		isC = firstElement instanceof AFileResource || firstElement instanceof MFolder;
		if (isC && firstElement instanceof MFolder)
			isC = getNewRunit().getParent() != null;
		return isC;
	}

	@Override
	public void setErrorMessage(String newMessage) {
		super.setErrorMessage(newMessage);
		setPageComplete(isPageComplete());
	}

	/*
	 * Perform validation checks and eventually set the error message.
	 */
	private void performPageChecks() {
		String errorMsg = null;
		errorMsg = ValidationUtils.validateName(ruID.getText());
		if (errorMsg == null)
			errorMsg = ValidationUtils.validateLabel(ruLabel.getText());
		if (errorMsg == null)
			errorMsg = ValidationUtils.validateDesc(ruDescription.getText());
		setErrorMessage(errorMsg);
		setPageComplete(errorMsg == null);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout(2, false));

		treeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		gd.heightHint = 400;
		gd.horizontalSpan = 2;
		treeViewer.getTree().setLayoutData(gd);
		treeViewer.setContentProvider(new ReportTreeContetProvider() {
			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof MFolder && fileRes != null && fileRes.getValue().getIsNew() == true) {
					MFolder node = (MFolder) parentElement;
					if (node.getChildren() != null && node.getChildren().size() > 0) {
						List<INode> children = new ArrayList<INode>();
						for (INode n : node.getChildren()) {
							if (n != fileRes)
								children.add(n);
						}
						return children.toArray();
					}
				}
				return super.getChildren(parentElement);
			}
		});
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());
		ColumnViewerToolTipSupport.enableFor(treeViewer);

		// Report Unit shown label (resource descriptor label)
		Label lblRepoUnitName = new Label(composite, SWT.NONE);
		lblRepoUnitName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		lblRepoUnitName.setText(Messages.AResourcePage_name);
		ruLabel = new Text(composite, SWT.BORDER);
		ruLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ruLabel.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isRefresh)
					return;
				isRefresh = true;
				String rtext = ruLabel.getText();
				String validationError = ValidationUtils.validateLabel(rtext);
				setErrorMessage(validationError);
				if (validationError == null) {
					ResourceDescriptor ru = getNewRunit().getValue();
					ru.setLabel(rtext);
					// suggest the ID
					if (canSuggestID) {
						ruID.setText(rtext);
						ru.setName(IDStringValidator.safeChar(rtext));
						ru.setUriString(ru.getParentFolder() + "/" + ru.getName());
					}
				}
				isRefresh = false;
			}
		});

		// Report Unit ID (resource descriptor name)
		Label lblRepoUnitID = new Label(composite, SWT.NONE);
		lblRepoUnitID.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		lblRepoUnitID.setText(Messages.AResourcePage_id);
		ruID = new Text(composite, SWT.BORDER);
		ruID.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ruID.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isRefresh)
					return;
				isRefresh = true;
				String rtext = ruID.getText();
				String validationError = ValidationUtils.validateName(rtext);
				setErrorMessage(validationError);
				if (validationError == null) {
					ResourceDescriptor ru = getNewRunit().getValue();
					ru.setName(rtext);
					ru.setUriString(ru.getParentFolder() + "/" + ru.getName());
				}
				if (!isFillingInput && validationError == null) {
					canSuggestID = false;
				} else {
					canSuggestID = true;
				}
				isRefresh = false;
			}
		});
		ruID.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				// sanitize the text for the id attribute (name)
				// of the repository resource
				e.text = IDStringValidator.safeChar(e.text);
			}
		});

		// Report Unit description
		Label lblRepoUnitDescription = new Label(composite, SWT.NONE);
		GridData descLblGD = new GridData(SWT.FILL, SWT.TOP, false, false);
		lblRepoUnitDescription.setLayoutData(descLblGD);
		lblRepoUnitDescription.setText(Messages.AResourcePage_description);
		ruDescription = new Text(composite, SWT.BORDER | SWT.MULTI);
		GridData descGD = new GridData(SWT.FILL, SWT.TOP, true, true);
		descGD.minimumHeight = 50;
		ruDescription.setLayoutData(descGD);
		ruDescription.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isRefresh)
					return;
				String rtext = ruDescription.getText();
				ResourceDescriptor ru = getNewRunit().getValue();
				ru.setDescription(rtext);
				setErrorMessage(ValidationUtils.validateDesc(rtext));
			}
		});

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection ts = (TreeSelection) event.getSelection();
				Object obj = ts.getFirstElement();
				handleSelectionChanged(obj);
			}

		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection ts = (TreeSelection) treeViewer.getSelection();
				Object el = ts.getFirstElement();
				if (el instanceof MFolder || el instanceof MServerProfile || el instanceof MReportUnit) {
					if (treeViewer.getExpandedState(el))
						treeViewer.collapseToLevel(el, 1);
					else {
						if (refreshAction == null)
							refreshAction = new RefreshResourcesAction(treeViewer);
						if (refreshAction.isEnabled())
							refreshAction.run();
						treeViewer.expandToLevel(el, 1);
					}
				}
			}
		});
		treeViewer.addTreeListener(new ITreeViewerListener() {
			private ServerProvider serverProvider;

			public void treeExpanded(final TreeExpansionEvent event) {
				if (!skipEvents) {
					try {
						getContainer().run(false, true, new IRunnableWithProgress() {

							public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
								monitor.beginTask(Messages.Publish2ServerWizard_MonitorName, IProgressMonitor.UNKNOWN);
								try {
									if (serverProvider == null)
										serverProvider = new ServerProvider();
									serverProvider.handleTreeEvent(event, monitor);
								} catch (Exception e) {
									if (e instanceof InterruptedException)
										throw (InterruptedException) e;
									else
										UIUtils.showError(e);
								} finally {
									monitor.done();
								}
							}
						});
					} catch (InvocationTargetException e) {
						UIUtils.showError(e.getCause());
					} catch (InterruptedException e) {
						UIUtils.showError(e.getCause());
					}
				}
			}

			public void treeCollapsed(TreeExpansionEvent event) {

			}
		});
		fillInput();
	}

	private AFileResource newRes;
	private AFileResource fileRes;

	private AFileResource getNewRunit() {
		if (newRes == null) {
			ResourceDescriptor rd = AFileResource.createDescriptor(null);
			rd.setWsType(ResourceDescriptor.TYPE_CONTENT_RESOURCE);
			rd.setName(null);
			PublishUtil.initResourceName(file.getName(), rd);
			rd.setLabel(file.getName());
			newRes = new MContentResource(null, rd, -1);
			newRes.setJasperConfiguration(jConfig);
		}
		return newRes;
	}

	private boolean isRefresh = false;

	protected void handleSelectionChanged(Object obj) {
		if (isRefresh)
			return;
		isRefresh = true;

		if (obj instanceof MReportUnit) {
			fileRes = getNewRunit();
			MReportUnit pfolder = (MReportUnit) obj;
			fileRes.setParent(pfolder, -1);
			ResourceDescriptor rd = fileRes.getValue();
			rd.setUriString(pfolder.getValue().getUriString() + "/" + rd.getName());
		} else if (obj instanceof MFolder) {
			fileRes = getNewRunit();
			MFolder pfolder = (MFolder) obj;
			fileRes.setParent(pfolder, -1);
			ResourceDescriptor rd = fileRes.getValue();
			rd.setUriString(pfolder.getValue().getUriString() + "/" + rd.getName());
		} else if (obj instanceof AFileResource)
			fileRes = (AFileResource) obj;
		else
			setPageComplete(false);
		if (fileRes != null) {
			ResourceDescriptor rd = fileRes.getValue();
			ruLabel.setText(Misc.nvl(rd.getLabel()));
			ruID.setText(Misc.nvl(rd.getName()));
			ruDescription.setText(Misc.nvl(rd.getDescription()));
		}
		performPageChecks();
		isRefresh = false;
	}

	private boolean skipEvents = false;
	private MServers servers;

	public void fillInput() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				isFillingInput = true;
				servers = new MServers(null);
				ServerManager.loadServerProfilesCopy(servers);
				treeViewer.setInput(servers);
				refreshFile();
				isFillingInput = false;
			}
		});
	}

	private void look4SelectedUnit() {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Looking for resource", IProgressMonitor.UNKNOWN);
					try {
						List<String[]> paths = PublishUtil.loadPath(monitor, file);
						for (String[] p : paths) {
							MServerProfile msp = null;
							for (INode n : servers.getChildren()) {
								if (n instanceof MServerProfile && ((MServerProfile) n).getValue().getUrl().equals(p[0])) {
									msp = (MServerProfile) n;
									break;
								}
							}
							if (msp != null) {
								if (selectResource(msp, p[1], monitor))
									break;
							}
						}
					} catch (Exception ce) {
						ce.printStackTrace();
					} finally {
						monitor.done();
					}
				}

				private boolean selectResource(MServerProfile msp, String uri, IProgressMonitor monitor) throws Exception {
					if (monitor.isCanceled())
						return true;
					ResourceDescriptor rd = new ResourceDescriptor();
					rd.setUriString(uri);
					final MResource mres = WSClientHelper.findSelected(monitor, rd, msp);
					if (mres == null)
						return false;
					UIUtils.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							skipEvents = true;
							treeViewer.refresh();
							treeViewer.setSelection(new StructuredSelection(mres), true);
							skipEvents = false;
						}
					});

					return true;
				}
			});
		} catch (InvocationTargetException e) {
			UIUtils.showError(e.getCause());
		} catch (InterruptedException e) {
			UIUtils.showError(e.getCause());
		}
	}
}
