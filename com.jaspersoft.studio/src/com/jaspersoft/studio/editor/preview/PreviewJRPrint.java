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
package com.jaspersoft.studio.editor.preview;

import java.io.InputStream;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRPrintXmlLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.jaspersoft.studio.editor.action.PrintAction;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.toolbar.ATopToolBarManager;
import com.jaspersoft.studio.editor.preview.toolbar.TopToolBarManagerJRPrint;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.AViewsFactory;
import com.jaspersoft.studio.editor.preview.view.ViewsFactory;
import com.jaspersoft.studio.editor.preview.view.control.VSimpleErrorPreview;
import com.jaspersoft.studio.editor.preview.view.report.IJRPrintable;
import com.jaspersoft.studio.utils.Console;

public class PreviewJRPrint extends ABasicEditor {
	private boolean hideParameters = true;

	public void setHideParameters(boolean hideParameters) {
		this.hideParameters = hideParameters;
	}

	public boolean isHideParameters() {
		return hideParameters;
	}

	private JasperPrint jasperPrint;

	public PreviewJRPrint() {
		super(true);
	}

	public PreviewJRPrint(boolean listenresource) {
		super(listenresource);
	}

	private ActionRegistry actionRegistry;

	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ActionRegistry.class)
			return getActionRegistry();
		if (adapter == JasperPrint.class)
			return getJasperPrint();
		return super.getAdapter(adapter);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		loadJRPrint(getEditorInput());
		// getSite().getPage().addPartListener(new IPartListener2() {
		//
		// public void partVisible(IWorkbenchPartReference partRef) {
		// if (console != null) {
		// IEditorPart ceditor = getSite().getPage().getActiveEditor();
		// if (partRef.getPart(false).getClass().equals(ceditor.getClass())) {
		// if (ceditor instanceof JrxmlEditor && ((JrxmlEditor) ceditor).getActivePage() == JrxmlEditor.PAGE_PREVIEW) {
		// console.showConsole();
		// }
		// if (ceditor instanceof PreviewJRPrint)
		// console.showConsole();
		// }
		// }
		// }
		//
		// public void partOpened(IWorkbenchPartReference partRef) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void partInputChanged(IWorkbenchPartReference partRef) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void partHidden(IWorkbenchPartReference partRef) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void partDeactivated(IWorkbenchPartReference partRef) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void partClosed(IWorkbenchPartReference partRef) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// // if (console != null)
		// // console.showConsole();
		// }
		//
		// public void partActivated(IWorkbenchPartReference partRef) {
		// // if (console != null)
		// // console.showConsole();
		// }
		// });

		ActionRegistry registry = getActionRegistry();
		IAction action = new PrintAction(this);
		registry.registerAction(action);
		getEditorSite().getActionBars().setGlobalActionHandler(ActionFactory.PRINT.getId(), action);
	}

	protected void loadJRPrint(IEditorInput input) throws PartInitException {
		InputStream in = null;
		try {
			IFile file = null;
			if (input instanceof IFileEditorInput) {
				file = ((IFileEditorInput) input).getFile();
				in = file.getContents();
			} else {
				throw new PartInitException("Invalid Input: Must be IFileEditorInput or FileStoreEditorInput"); //$NON-NLS-1$
			}
			Statistics stats = new Statistics();
			if (file.getFileExtension().equals(".jrpxml")) {
				setJasperPrint(stats, JRPrintXmlLoader.load(in));
			} else {
				Object obj = JRLoader.loadObject(in);
				if (obj instanceof JasperPrint)
					setJasperPrint(stats, (JasperPrint) obj);
			}
		} catch (Exception e) {
			throw new PartInitException("Invalid Input", e);
		}
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(final Statistics stats, JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
		UIUtils.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (getDefaultViewer() instanceof IJRPrintable) {
					JasperPrint jrprint = getJasperPrint();
					if (jrprint != null) {
						getRightContainer().switchView(stats, getDefaultViewerKey());
					} else {
						// errorPreview.setMessage("Document is empty");
						getRightContainer().switchView(stats, errorPreview);
					}
				}
			}
		});
	}

	protected String currentViewer;

	public String getCurrentViewer() {
		return currentViewer;
	}

	/**
	 * Set the current preview type
	 * 
	 * @param viewerKey
	 *          key of the type to show
	 * @param refresh
	 *          flag to set if the preview should also be refreshed
	 */
	public void setCurrentViewer(String viewerKey, boolean refresh) {
		if (getViewFactory().getKeys().contains(viewerKey)) {
			currentViewer = viewerKey;
			if (refresh)
				rightContainer.switchView(currentViewer);
		}
	}

	public String getDefaultViewerKey() {
		if (currentViewer == null)
			currentViewer = ViewsFactory.VIEWER_JAVA;
		return currentViewer;
	}

	public APreview getDefaultViewer() {
		final APreview viewer = getRightContainer().getViewer(getDefaultViewerKey());
		// Display.getDefault().syncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		// if (topToolBarManager != null)
		// topToolBarManager.contributeItems(viewer);
		// }
		// });

		return viewer;
	}

	protected void afterRightSwitchView() {

	}

	protected MultiPageContainer rightContainer;

	public MultiPageContainer getRightContainer() {
		if (rightContainer == null) {
			rightContainer = new MultiPageContainer() {
				private boolean same = false;

				@Override
				public void afterSwitchView() {
					afterRightSwitchView();
				}

				public void switchView(Statistics stats, String key) {
					same = currentViewer == key;
					currentViewer = key;
					// APreview view = pmap.get(key);
					// topToolBarManager.contributeItems(view);
					// if (!switchRightView(view, stats, this))
					// return;

					super.switchView(stats, key);
				}

				@Override
				public void switchView(String key) {
					same = currentViewer == key;
					currentViewer = key;
					super.switchView(key);
				}

				@Override
				public void switchView(Statistics stats, final APreview view) {
					if (!same && !switchRightView(view, stats, this))
						return;
					super.switchView(stats, view);
					if (!same || !view.isContributed2ToolBar())
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								if (topToolBarManager != null)
									topToolBarManager.contributeItems(view);
							}
						});
					else
						topToolBarManager.refreshToolbar();
				}

				@Override
				public void dispose() {
					super.dispose();
					topToolBarManager.removeAll();
				}
			};
		}
		return rightContainer;
	}

	public boolean switchRightView(APreview view, Statistics stats, MultiPageContainer container) {
		if (view instanceof IJRPrintable) {
			try {
				((IJRPrintable) view).setJRPRint(stats, jasperPrint);
				console.setStatistics(stats);
			} catch (Exception e) {
				errorPreview.setMessage("The document is empty.");
				container.switchView(stats, errorPreview);

				getConsole().addError(e, null);
				return false;
			}
		}
		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
		getRightContainer().dispose();
	}

	protected TopToolBarManagerJRPrint topToolBarManager;

	protected TopToolBarManagerJRPrint getTopToolBarManager(Composite container) {
		if (topToolBarManager == null)
			topToolBarManager = new TopToolBarManagerJRPrint(this, container);
		return topToolBarManager;
	}

	protected ATopToolBarManager topToolBarManager1;

	protected ATopToolBarManager getTopToolBarManager1(Composite container) {
		if (topToolBarManager1 == null)
			topToolBarManager1 = new ATopToolBarManager(this, container) {

				@Override
				protected void fillToolbar(IToolBarManager tbManager) {

				}
			};
		return topToolBarManager1;
	}

	protected VSimpleErrorPreview errorPreview;

	public VSimpleErrorPreview getErrorView() {
		return errorPreview;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, "com.jaspersoft.studio.doc.editor_jrprint");

		getTopToolBarManager1(container);
		getTopToolBarManager(container);

		Composite rcmp = createRight(container);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		rcmp.setLayoutData(gd);
	}

	protected Composite createRight(Composite parent) {
		// CSashForm rightSash = new CSashForm(parent, SWT.VERTICAL);

		rightComposite = new Composite(parent, SWT.BORDER);

		StackLayout stacklayoutView = new StackLayout();
		rightComposite.setLayout(stacklayoutView);

		getRightContainer().populate(rightComposite, getViewFactory().createPreviews(rightComposite, jrContext));

		errorPreview = new VSimpleErrorPreview(rightComposite, jrContext);

		return rightComposite;
	}

	protected AViewsFactory viewFactory;

	public AViewsFactory getViewFactory() {
		if (viewFactory == null)
			viewFactory = new ViewsFactory();
		return viewFactory;
	}

	@Override
	public void setFocus() {
		if (topToolBarManager1 != null)
			topToolBarManager1.setFocus();
	}

	private boolean notRunning = true;

	public void setNotRunning(boolean norun) {
		this.notRunning = norun;

		if (topToolBarManager1 != null) {
			topToolBarManager1.refreshToolbar();
			if (norun)
				topToolBarManager1.setEnabled(true);
		}

		if (topToolBarManager != null) {
			topToolBarManager.refreshToolbar();
			if (norun)
				topToolBarManager.setEnabled(true);
		}
	}

	public boolean isNotRunning() {
		return notRunning;
	}

	private Console console;
	protected Composite rightComposite;

	public Console getConsole() {
		if (console == null) {
			console = Console.showConsole(getEditorInput().getName(), jrContext);
			// console.addErrorPreview(errorPreview);
		}
		return console;
	}

}
