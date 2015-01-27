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
package com.jaspersoft.studio.server.wizard.resource.page.datasource;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;
import com.jaspersoft.studio.property.dataset.TLabelProvider;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.filter.DatasourceVDSFilter;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.MoveT2TButtons;

public class DatasourceVDSPageContent extends APageContent {

	public DatasourceVDSPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public DatasourceVDSPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.datasource.vds";
	}

	@Override
	public String getName() {
		return Messages.RDDatasourceVDSPage_title;
	}

	protected List<Object> inFields;
	protected List<Object> outFields;

	protected Table rightTable;
	protected TableViewer rightTView;
	protected TreeViewer leftTView;

	private MoveT2TButtons mt2t = null;
	private ListOrderButtons lob = null;

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));

		leftTView = new TreeViewer(composite, SWT.MULTI | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumWidth = 400;
		leftTView.getTree().setLayoutData(gd);
		leftTView.setContentProvider(new ReportTreeContetProvider());
		leftTView.setLabelProvider(new ReportTreeLabelProvider());
		ColumnViewerToolTipSupport.enableFor(leftTView);
		leftTView.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				// TreeSelection ts = (TreeSelection) event.getSelection();
				// Object obj = ts.getFirstElement();
			}
		});
		leftTView.addTreeListener(new ITreeViewerListener() {

			public void treeExpanded(TreeExpansionEvent event) {
			}

			public void treeCollapsed(TreeExpansionEvent event) {

			}
		});

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		// -----------------------------------
		rightTable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumWidth = 400;
		rightTable.setLayoutData(gd);
		rightTable.setHeaderVisible(true);

		rightTView = new TableViewer(rightTable);
		rightTView.setContentProvider(new ListContentProvider());
		setLabelProvider(rightTView);

		createColumns();

		createOrderButtons(composite);

		fillRightTable();

		mt2t = new MoveT2TButtons() {
			@Override
			protected void handleMove(ColumnViewer leftTView, ColumnViewer rightTView) {
				StructuredSelection s = (StructuredSelection) leftTView.getSelection();
				if (!s.isEmpty()) {
					Object input = leftTView.getInput();
					if (input instanceof MServerProfile) {
						MServerProfile left = (MServerProfile) leftTView.getInput();
						List<Proxy> right = (List<Proxy>) rightTView.getInput();
						for (Object obj : s.toArray()) {
							MResource mres = (MResource) obj;
							ResourceDescriptor rd = mres.getValue();
							if (!SelectorDatasource.isDatasource(rd))
								continue;
							if (checkExists(right, rd))
								continue;
							ResourceDescriptor rdc = null;
							for (ResourceDescriptor r : oldvds) {
								if (r.getReferenceUri().equals(rd.getUriString())) {
									rdc = r;
									break;
								}
							}
							if (rdc == null) {
								rdc = new ResourceDescriptor();
								rdc.setName(rd.getName());
								rdc.setLabel(rdc.getLabel());
								rdc.setIsReference(true);
								rdc.setReferenceUri(rd.getUriString());
								rdc.setWsType(ResourceDescriptor.TYPE_DATASOURCE);
								rdc.setIsNew(true);
								rdc.setResourceProperty("PROP_DATASOURCE_SUB_DS_ID", rd.getName());//$NON-NLS-1$
							}
							right.add(new Proxy(rdc));
							res.getValue().getChildren().add(rdc);
						}
					} else {
						List<Proxy> left = (List<Proxy>) leftTView.getInput();
						for (Object obj : s.toArray()) {
							left.remove(obj);
							res.getValue().getChildren().remove(((Proxy) obj).getResourceDescriptor());
						}
					}
					leftTView.refresh();
					rightTView.refresh();
					fireChangeEvent();
				}
			}

			protected boolean checkExists(List<Proxy> right, ResourceDescriptor rd) {
				boolean exists = false;
				for (Proxy p : right) {
					if (p.getRefuri().equals(rd.getUriString())) {
						exists = true;
						break;
					}
				}
				return exists;
			}

		};
		mt2t.createButtonsShort(bGroup, leftTView, rightTView, false);

		fillLeftTable();
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {

	};

	private void fillLeftTable() {
		final MServerProfile root = ServerManager.getMServerProfileCopy((MServerProfile) pnode.getRoot());
		try {
			page.getContainer().run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						WSClientHelper.getDatasourceListTree(monitor, root, new DatasourceVDSFilter());
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								if (!leftTView.getControl().isDisposed()) {
									leftTView.setInput(root);
									leftTView.refresh();
								}
							}
						});
					} catch (Exception e) {
						UIUtils.showError(e);
					}
				}
			});
		} catch (InvocationTargetException e) {
			UIUtils.showError(e);
			leftTView.setInput(root);
		} catch (InterruptedException e) {
			UIUtils.showError(e);
			leftTView.setInput(root);
		}

	}

	private List<ResourceDescriptor> oldvds = new ArrayList<ResourceDescriptor>();

	private void fillRightTable() {
		ResourceDescriptor rd = res.getValue();
		List<Proxy> input = new ArrayList<Proxy>();
		List<ResourceDescriptor> children = rd.getChildren();
		for (ResourceDescriptor c : children) {
			if (SelectorDatasource.isDatasource(c)) {
				input.add(new Proxy(c));
				oldvds.add(c);
			}
		}
		rightTView.setInput(input);
	}

	class Proxy {
		private String refuri;
		private String name;
		private ResourceProperty alias;
		private ResourceDescriptor rd;

		public Proxy(ResourceDescriptor rd) {
			this.rd = rd;
			if (rd.getIsReference()) {
				refuri = rd.getReferenceUri();
				name = refuri.substring(refuri.lastIndexOf("/") + 1); //$NON-NLS-1$
			} else
				name = rd.getName();
			alias = ResourceDescriptorUtil.getProperty("PROP_DATASOURCE_SUB_DS_ID", rd.getProperties());
		}

		public ResourceDescriptor getResourceDescriptor() {
			return rd;
		}

		public String getName() {
			return name;
		}

		public String getAlias() {
			return alias.getValue();
		}

		public void setAlias(String name) {
			alias.setValue(name);
		}

		public String getRefuri() {
			return refuri != null ? refuri : name;
		}
	}

	protected void setLabelProvider(TableViewer tableViewer) {
		tableViewer.setLabelProvider(new TLabelProvider());
	}

	protected void createColumns() {
		ColumnViewerToolTipSupport.enableFor(rightTView, ToolTip.NO_RECREATE);
		TableColumn[] col = new TableColumn[2];
		TableViewerColumn viewerColumn = new TableViewerColumn(rightTView, SWT.NONE);
		col[0] = viewerColumn.getColumn();
		col[0].setText(Messages.RDDatasourceVDSPage_dsname);
		col[0].pack();
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Proxy p = (Proxy) element;
				return p.getName();
			}

			@Override
			public String getToolTipText(Object element) {
				Proxy p = (Proxy) element;
				return p.getRefuri();
			}
		});

		viewerColumn = new TableViewerColumn(rightTView, SWT.NONE);
		col[1] = viewerColumn.getColumn();
		col[1].setText(Messages.RDDatasourceVDSPage_dsalias);
		col[1].pack();
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Proxy p = (Proxy) element;
				return p.getAlias();
			}

			@Override
			public String getToolTipText(Object element) {
				Proxy p = (Proxy) element;
				return p.getRefuri();
			}
		});
		viewerColumn.setEditingSupport(new EditingSupport(rightTView) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(rightTView.getTable());
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected Object getValue(Object element) {
				Proxy p = (Proxy) element;
				return p.getAlias();
			}

			@Override
			protected void setValue(Object element, Object value) {
				Proxy p = (Proxy) element;
				p.setAlias((String) value);
				rightTView.refresh(element);
			}
		});

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, false));
		tlayout.addColumnData(new ColumnWeightData(50, false));
		rightTable.setLayout(tlayout);
	}

	private void createOrderButtons(Composite composite) {
		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		lob = new ListOrderButtons();

		lob.createOrderButtons(bGroup, rightTView);
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.editVirtualDatasource";
	}
}
