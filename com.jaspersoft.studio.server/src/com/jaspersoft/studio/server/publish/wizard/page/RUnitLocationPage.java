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
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MDummy;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.ServerProvider;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.action.resource.RefreshResourcesAction;
import com.jaspersoft.studio.server.export.AExporter;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.AMJrxmlContainer;
import com.jaspersoft.studio.server.model.IInputControlsContainer;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.publish.FindResources;
import com.jaspersoft.studio.server.publish.PublishUtil;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.utils.ValidationUtils;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

public class RUnitLocationPage extends JSSHelpWizardPage {
	private JasperDesign jDesign;
	private TreeViewer treeViewer;
	private Button bnRunit;
	private Text ruLabel;

	private ANode n;
	private RefreshResourcesAction refreshAction;
	private JasperReportsConfiguration jConfig;
	private Text ruID;
	private Text ruDescription;

	private boolean isFillingInput;
	private boolean canSuggestID;

	public RUnitLocationPage(JasperReportsConfiguration jConfig, JasperDesign jDesign, ANode n) {
		super("serverpublish"); //$NON-NLS-1$
		setTitle(Messages.RUnitLocationPage_title);
		setDescription(Messages.RUnitLocationPage_description);
		this.jDesign = jDesign;
		this.n = n;
		this.jConfig = jConfig;
	}

	public void setValue(JasperDesign jDesign, ANode n) {
		this.jDesign = jDesign;
		this.n = n;
		fillInput();
	}

	public AMJrxmlContainer getSelectedNode() {
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		Object obj = ts.getFirstElement();
		if (obj != null) {
			if (obj instanceof MFolder)
				return reportUnit;
			return (AMJrxmlContainer) obj;
		}
		if (n instanceof AMJrxmlContainer)
			return (AMJrxmlContainer) n;
		reportUnit.setJasperConfiguration(jConfig);
		return reportUnit;
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
		isC = firstElement instanceof MJrxml || firstElement instanceof MFolder || firstElement instanceof MReportUnit;
		if (isC && firstElement instanceof MFolder) {
			AMJrxmlContainer runit = getReportUnit();
			isC = runit instanceof AMJrxmlContainer && runit.getParent() != null;
		}
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
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		gd.heightHint = 400;
		gd.horizontalSpan = 2;
		treeViewer.getTree().setLayoutData(gd);
		treeViewer.setContentProvider(new ReportTreeContetProvider() {
			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof MResource) {
					MResource mres = (MResource) parentElement;
					if (mres instanceof MReportUnit || (mres.isSupported(Feature.INPUTCONTROLS_ORDERING) && (mres instanceof IInputControlsContainer))) {
						if (mres.getChildren() != null && mres.getChildren().size() > 0) {
							List<INode> children = new ArrayList<INode>();
							if (mres.getChildren().get(0) instanceof MDummy)
								try {
									WSClientHelper.refreshContainer(mres, new NullProgressMonitor());
								} catch (Exception e) {
									UIUtils.showError(e);
								}
							for (INode n : mres.getChildren())
								if (n instanceof MResource && !SelectorDatasource.isDatasource(((MResource) n).getValue()))
									children.add(n);
							return children.toArray();
						}
					} else if (mres instanceof MFolder && newrunit.getValue().getIsNew() == true) {
						MFolder node = (MFolder) mres;
						if (node.getChildren() != null && node.getChildren().size() > 0) {
							List<INode> children = new ArrayList<INode>();
							for (INode n : node.getChildren())
								if (n != newrunit && n != newjrxml)
									children.add(n);
							return children.toArray();
						}
					}
				}
				return super.getChildren(parentElement);
			}
		});
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());
		ColumnViewerToolTipSupport.enableFor(treeViewer);

		bnRunit = new Button(composite, SWT.CHECK);
		bnRunit.setText(Messages.RUnitLocationPage_addreportunit_button);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		bnRunit.setLayoutData(gd);
		bnRunit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = bnRunit.getSelection();
				// Enable/Disable the detail textboxes
				// ruLabel.setEnabled(selected);
				// ruID.setEnabled(selected);
				// ruDescription.setEnabled(selected);

				reportUnit = selected ? getNewRunit() : getNewJrxml();
				if (reportUnit.getParent() == null) {
					TreeSelection ts = (TreeSelection) treeViewer.getSelection();
					Object obj = ts.getFirstElement();
					if (obj instanceof ANode)
						reportUnit.setParent((ANode) obj, -1);
				}
				performPageChecks();
				setPageComplete(isPageComplete());
			}
		});

		// Report Unit shown label (resource descriptor label)
		Label lblRepoUnitName = new Label(composite, SWT.NONE);
		lblRepoUnitName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		lblRepoUnitName.setText(Messages.RUnitLocationPage_reportunitlabel);
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
		lblRepoUnitID.setText(Messages.RUnitLocationPage_lblreportunit);
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
		lblRepoUnitDescription.setText(Messages.RUnitLocationPage_reportunitdesc_label);
		ruDescription = new Text(composite, SWT.BORDER | SWT.MULTI);
		GridData descGD = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		descGD.heightHint = 50;
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
		ruDescription.setText(""); //$NON-NLS-1$

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
				if (el instanceof MFolder || el instanceof MServerProfile) {
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
					UIUtils.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							try {
								getContainer().run(true, true, new IRunnableWithProgress() {

									public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
										monitor.beginTask(Messages.Publish2ServerWizard_MonitorName, IProgressMonitor.UNKNOWN);
										try {
											if (serverProvider == null)
												serverProvider = new ServerProvider();
											Object element = event.getElement();
											boolean be = reportUnit.getParent() == element;
											serverProvider.handleTreeEvent(event, monitor);
											if (be) {
												MFolder f = (MFolder) element;
												String nm = reportUnit.getValue().getName();
												boolean isnew = true;
												for (INode n : f.getChildren()) {
													if (n instanceof MReportUnit) {
														if (((MReportUnit) n).getValue().getName().equals(nm)) {
															reportUnit = (MReportUnit) n;
															isnew = false;
															break;
														}
													} else if (n instanceof MJrxml) {
														if (((MJrxml) n).getValue().getName().equals(nm)) {
															reportUnit = (MJrxml) n;
															isnew = false;
															break;
														}
													}
												}
												if (isnew)
													reportUnit.setParent(f, -1);
											}
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
					});

				}
			}

			public void treeCollapsed(TreeExpansionEvent event) {

			}
		});
		fillInput();
	}

	private AMJrxmlContainer reportUnit;
	private MReportUnit newrunit;
	private MJrxml newjrxml;

	private MReportUnit getNewRunit() {
		if (newrunit == null) {
			ResourceDescriptor rd = MReportUnit.createDescriptor(null);
			rd.setName(null);
			rd.setResourceProperty(ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS, true);
			newrunit = new MReportUnit(null, rd, -1);
		}
		PublishUtil.initRUnitName(newrunit, jDesign);
		return newrunit;
	}

	private MJrxml getNewJrxml() {
		if (newjrxml == null) {
			ResourceDescriptor rd = MJrxml.createDescriptor(null);
			rd.setName(null);
			newjrxml = new MJrxml(null, rd, -1);
		}
		PublishUtil.initRUnitName(newjrxml, jDesign);
		return newjrxml;
	}

	private AMJrxmlContainer getReportUnit() {
		PublishUtil.initRUnitName(reportUnit, jDesign);
		return reportUnit;
	}

	private boolean isRefresh = false;

	protected void handleSelectionChanged(Object obj) {
		if (isRefresh)
			return;
		isRefresh = true;
		boolean isFolder = obj instanceof MFolder;
		bnRunit.setSelection(isFolder);
		bnRunit.setEnabled(isFolder);
		ruLabel.setEnabled(bnRunit.getSelection() && isFolder);
		ruID.setEnabled(bnRunit.getSelection() && isFolder);
		ruDescription.setEnabled(bnRunit.getSelection() && isFolder);

		reportUnit = getNewRunit();
		if (obj instanceof MReportUnit) {
			reportUnit = getNewRunit();
			reportUnit = (MReportUnit) obj;
			ruLabel.setText(Misc.nvl(reportUnit.getValue().getLabel()));
			ruID.setText(Misc.nvl(reportUnit.getValue().getName()));
			ruDescription.setText(Misc.nvl(reportUnit.getValue().getDescription()));
		} else if (obj instanceof MFolder) {
			newrunit = getNewRunit();
			newrunit.setParent((ANode) obj, -1);

			newjrxml = getNewJrxml();
			newjrxml.setParent((ANode) obj, -1);

			if (bnRunit.getSelection())
				reportUnit = newrunit;
			else
				reportUnit = newjrxml;

			ResourceDescriptor nrd = reportUnit.getValue();
			nrd.setName(ruID.getText());
			nrd.setLabel(ruLabel.getText());
			nrd.setDescription(ruDescription.getText());
			String uri = ((MFolder) obj).getValue().getUriString();
			nrd.setParentFolder(uri);
			nrd.setUriString(uri + "/" + nrd.getName()); //$NON-NLS-1$
		} else if (obj instanceof MJrxml) {
			reportUnit = getNewJrxml();
			reportUnit = (MJrxml) obj;
			ruLabel.setText(Misc.nvl(reportUnit.getValue().getLabel()));
			ruID.setText(Misc.nvl(reportUnit.getValue().getName()));
			ruDescription.setText(Misc.nvl(reportUnit.getValue().getDescription()));
		} else if (obj instanceof MResource) {
			ANode mparent = ((MResource) obj).getParent();
			treeViewer.setSelection(new StructuredSelection(mparent), true);
			handleSelectionChanged(mparent);
		} else
			setPageComplete(false);
		performPageChecks();
		isRefresh = false;
	}

	private boolean skipEvents = false;

	public void fillInput() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				isFillingInput = true;
				initIDLabel();
				if (n instanceof MServerProfile)
					look4SelectedUnit((MServerProfile) n);
				setSelectedNode();
				isFillingInput = false;
			}
		});
	}

	private void initIDLabel() {
		if (jDesign != null) {
			ruID.setText(jDesign.getName().replace(" ", "")); //$NON-NLS-1$ //$NON-NLS-2$
			ruLabel.setText(jDesign.getName());
		}
	}

	private void setSelectedNode() {
		if (n == null || treeViewer.getTree().isDisposed())
			return;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				INode root = n.getRoot();
				if (root instanceof MServerProfile)
					root = ((ANode) root.getParent()).getRoot();
				treeViewer.setInput(root);
				skipEvents = true;
				treeViewer.refresh();
				if (n != null)
					treeViewer.setSelection(new StructuredSelection(n), true);
				setPageComplete(isPageCompleteLogic());
				skipEvents = false;
				handleSelectionChanged(n);
			}
		});
	}

	private void look4SelectedUnit(final MServerProfile mres) {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					IFile file = (IFile) jConfig.get(FileUtils.KEY_FILE);
					ANode node = FindResources.findReportUnit(mres, monitor, jDesign, file);
					if (monitor.isCanceled())
						return;
					if (n != mres)
						return;
					n = node;
					try {
						if (n instanceof MReportUnit && !ResourceDescriptorUtil.isReportMain(file)) {
							MReportUnit mReportUnit = (MReportUnit) n;
							String res = jDesign.getProperty(AExporter.PROP_REPORTRESOURCE);
							if (!Misc.isNullOrEmpty(res)) {
								mReportUnit.setValue(WSClientHelper.getResource(monitor, n, mReportUnit.getValue()));
								List<ResourceDescriptor> children = mReportUnit.getValue().getChildren();
								ResourceDescriptor rd = null;
								for (ResourceDescriptor c : children) {
									if (c.getWsType().equals(ResourceDescriptor.TYPE_JRXML) && c.getUriString().equals(res)) {
										rd = c;
										break;
									}
								}
								if (rd != null) {
									n.removeChildren();
									ANode tmpn = null;
									for (ResourceDescriptor c : children) {
										MResource mr = ResourceFactory.getResource(n, c, -1);
										if (c == rd)
											tmpn = mr;
									}
									n = tmpn;
								}
							}
						}
					} catch (Exception ce) {
						ce.printStackTrace();
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
