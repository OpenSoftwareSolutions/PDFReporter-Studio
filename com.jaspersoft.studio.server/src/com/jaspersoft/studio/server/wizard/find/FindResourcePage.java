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
package com.jaspersoft.studio.server.wizard.find;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ClientResourceLookup;
import com.jaspersoft.studio.server.AFinderUI;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.restv2.WsTypes;
import com.jaspersoft.studio.utils.Misc;

public class FindResourcePage extends WizardPage {
	private FinderUI finderUI;
	private String[] itypes;
	private String[] etypes;

	public void setFilterTypes(String[] in, String[] excl) {
		this.itypes = in;
		this.etypes = excl;

		List<String> tps = finderUI.getTypes();
		tps.clear();
		if (itypes != null)
			for (String t : itypes)
				tps.add(t);
		if (etypes != null)
			for (String t : etypes)
				if (tps.contains(t))
					tps.remove(t);
	}

	protected FindResourcePage(MServerProfile sp) {
		super("findresource"); //$NON-NLS-1$
		setTitle(Messages.FindResourcePage_1);
		setDescription(Messages.FindResourcePage_2);
		finderUI = new FinderUI(sp);
	}

	@Override
	public void createControl(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(3, false));
		setControl(cmp);

		new Label(cmp, SWT.NONE).setText(Messages.FindResourcePage_3);

		txt = new Text(cmp, SWT.BORDER);
		txt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txt.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				doSearch();
			}
		});

		Button b = new Button(cmp, SWT.PUSH);
		b.setText(Messages.FindResourcePage_4);
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSearch();
			}
		});
		if (itypes == null && etypes == null) {
			Section expcmp = new Section(cmp, ExpandableComposite.TREE_NODE);
			expcmp.setText(Messages.FindResourcePage_5);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 3;
			gd.verticalIndent = 3;
			expcmp.setLayoutData(gd);
			expcmp.setExpanded(false);

			Composite scmp = new Composite(expcmp, SWT.NONE);
			scmp.setLayout(new GridLayout(2, false));

			Composite dsCmp = new Composite(scmp, SWT.NONE);
			dsCmp.setLayout(new GridLayout(2, false));
			dsCmp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

			ball = new Button(dsCmp, SWT.CHECK);
			ball.setText(Messages.FindResourcePage_6);
			ball.setSelection(true);
			ball.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean sel = ball.getSelection();
					for (Button b : typesMap.values())
						b.setSelection(sel);
					bds.setSelection(sel);
					finderUI.getTypes().clear();
				}
			});
			Label lbl = new Label(dsCmp, SWT.SEPARATOR | SWT.HORIZONTAL);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			lbl.setLayoutData(gd);

			Map<String, String> typeNames = ResourceFactory.getTypeNames();
			for (String rtype : typeNames.keySet()) {
				if (dsTypes.contains(rtype))
					continue;
				final Button bhiden = new Button(dsCmp, SWT.CHECK);
				bhiden.setText(typeNames.get(rtype));
				bhiden.setSelection(true);
				bhiden.setToolTipText(rtype);
				typesMap.put(rtype, bhiden);
				bhiden.addSelectionListener(typeListener);
			}

			dsCmp = new Composite(scmp, SWT.NONE);
			dsCmp.setLayout(new GridLayout(2, false));
			dsCmp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

			bds = new Button(dsCmp, SWT.CHECK);
			bds.setText(Messages.FindResourcePage_7);
			bds.setSelection(true);
			gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			gd.horizontalSpan = 2;
			bds.setLayoutData(gd);
			bds.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					boolean sel = bds.getSelection();
					for (Button b : typesMap.values()) {
						String v = typesMap.inverse().get(b);
						if (v != null && dsTypes.contains(v))
							b.setSelection(sel);
					}
					if (!sel)
						ball.setSelection(false);
					setTypes();
				}
			});

			lbl = new Label(dsCmp, SWT.SEPARATOR | SWT.HORIZONTAL);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			lbl.setLayoutData(gd);

			for (String rtype : dsTypes) {
				if (!typeNames.containsKey(rtype))
					continue;
				final Button bhiden = new Button(dsCmp, SWT.CHECK);
				bhiden.setText(Misc.nvl(typeNames.get(rtype), rtype));
				bhiden.setSelection(true);
				bhiden.setToolTipText(rtype);
				typesMap.put(rtype, bhiden);
				bhiden.addSelectionListener(typeListener);
			}

			expcmp.setClient(scmp);
			expcmp.addExpansionListener(new ExpansionAdapter() {
				public void expansionStateChanged(ExpansionEvent e) {
					UIUtils.relayoutDialog(getShell(), 0, -1);
				}
			});
		}
		Composite tableComposite = new Composite(cmp, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		gd.heightHint = 300;
		gd.widthHint = 500;
		tableComposite.setLayoutData(gd);

		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		viewer = new TableViewer(tableComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.NO_RECREATE);
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
		col.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				ClientResourceLookup p = (ClientResourceLookup) cell.getElement();

				cell.setText(p.getLabel() + " : " + p.getUri()); //$NON-NLS-1$
				StyleRange myStyledRange = new StyleRange(p.getLabel().length() + 3, cell.getText().length(), Display.getCurrent().getSystemColor(SWT.COLOR_GRAY), null);
				StyleRange[] range = { myStyledRange };
				cell.setStyleRanges(range);

				cell.setImage(ResourceFactory.getIcon(p.getResourceType()));
				super.update(cell);
			}

			@Override
			public String getToolTipText(Object element) {
				ClientResourceLookup p = (ClientResourceLookup) element;
				String tt = p.getLabel();
				tt += "\n" + Messages.FindResourcePage_10 + p.getDescription(); //$NON-NLS-1$
				tt += "\n" + Messages.FindResourcePage_uri + p.getUri(); //$NON-NLS-1$
				tt += "\n" + Messages.FindResourcePage_14 + p.getResourceType(); //$NON-NLS-1$
				tt += "\n" + Messages.FindResourcePage_16 + p.getCreationDate(); //$NON-NLS-1$
				tt += "\n" + Messages.FindResourcePage_18 + p.getUpdateDate(); //$NON-NLS-1$
				return tt;
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				((FindWizardDialog) getContainer()).finishPressed();
			}
		});

		tableColumnLayout.setColumnData(col.getColumn(), new ColumnWeightData(100));
		final Table table = viewer.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(false);

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (res != null)
					try {
						int[] sel = table.getSelectionIndices();
						if (sel.length > 0)
							value = WSClientHelper.toResourceDescriptor(finderUI.getServerProfile(), res.get(sel[0]));
						if (value != null)
							setPageComplete(true);
					} catch (Exception e1) {
						UIUtils.showError(e1);
					}
			}
		});

		txt.setFocus();
		setPageComplete(false);
	}

	private BiMap<String, Button> typesMap = HashBiMap.create();
	private ResourceDescriptor value;
	private Text txt;
	private java.util.List<ClientResourceLookup> res;
	private TableViewer viewer;
	private static Set<String> dsTypes = WsTypes.INST().getDatasources();

	public ResourceDescriptor getValue() {
		return value;
	}

	@Override
	public boolean canFlipToNextPage() {
		return super.canFlipToNextPage() && getValue() != null;
	}

	private void setTypes() {
		List<String> tps = finderUI.getTypes();
		tps.clear();
		for (Button b : typesMap.values())
			if (b.getSelection())
				tps.add(typesMap.inverse().get(b));
	}

	private SelectionListener typeListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button bhiden = (Button) e.getSource();
			String type = typesMap.inverse().get(bhiden);
			if (!bhiden.getSelection()) {
				ball.setSelection(false);
				if (dsTypes.contains(type))
					bds.setSelection(false);
			}
			setTypes();
		}
	};
	private Button bds;
	private Button ball;
	private int started = 0;
	private boolean ended = true;

	class FinderUI extends AFinderUI {
		public FinderUI(MServerProfile sp) {
			super(sp);
		}

		@Override
		public void showResults(final java.util.List<ClientResourceLookup> res) {
			FindResourcePage.this.res = res;
			UIUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					setPageComplete(res != null);
					if (res != null)
						viewer.setInput(res);
					else
						viewer.setInput(Collections.EMPTY_LIST);
					value = null;
					setPageComplete(false);
					ended = true;
					started--;
					if (started > 0) {
						started = 0;
						doSearch();
					}
				}
			});
		}
	}

	private void doSearch() {
		finderUI.setText(txt.getText());
		started++;
		if (ended) {
			ended = false;
			search();
		}
	}

	private void search() {
		if (SystemUtils.IS_OS_WINDOWS)
			new Thread(new Runnable() {
				public void run() {
					try {
						WSClientHelper.findResources(new NullProgressMonitor(), finderUI);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		else
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(Messages.FindResourcePage_19, IProgressMonitor.UNKNOWN);
						try {
							WSClientHelper.findResources(monitor, finderUI);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							monitor.done();
						}
					}
				});
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	}
}
