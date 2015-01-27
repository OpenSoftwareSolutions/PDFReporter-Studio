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
package com.jaspersoft.studio.editor.report;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignScriptlet;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.IJROBjectEditor;
import com.jaspersoft.studio.editor.part.MultiPageToolbarEditorPart;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.style.StyleTemplateFactory;
import com.jaspersoft.studio.plugin.ExtensionManager;
import com.jaspersoft.studio.properties.view.ITabbedPropertySheetPageContributor;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.utils.ExpressionUtil;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class ReportContainer.
 * 
 * @author Chicu Veaceslav
 */
public class ReportContainer extends MultiPageToolbarEditorPart implements ITabbedPropertySheetPageContributor, IJROBjectEditor, CachedSelectionProvider {

	/**
	 * Key used to save, retrieve the selection cache from the jasper reprots configuration
	 */
	public static final String SELECTION_CACHE_KEY = "SELECTION_CACHE_PROVIDER";
	
	/**
	 * Property used by an element to ask to the container to check if for that element there is an editor opened and in
	 * that case close it. The property change event must have the old value set with the JRelement that it is requesting
	 * the editor closing
	 */
	public static final String CLOSE_EDITOR_PROPERTY = "closeElementEditor";
	
	/** 
	 * The model. 
	 */
	private INode model = null;

	/** 
	 * The editors. 
	 */
	private List<AbstractVisualEditor> editors = new ArrayList<AbstractVisualEditor>();

	
	/**
	 * The selection cache used by all the editors in this container (report editor and eventually its subeditors)
	 * The selection cache is passed to the subeditors trough the jasper configuration. The cached is stored
	 * when this container is created and can be retrieved with the SELECTION_CACHE_KEY
	 */
	private CommonSelectionCacheProvider selectionCache;

	/** The parent. */
	private EditorPart parent;
	private PropertyChangeSupport propertyChangeSupport;
	private JasperReportsConfiguration jrContext;

	public PropertyChangeSupport getPropertyChangeSupport() {
		if (propertyChangeSupport == null)
			propertyChangeSupport = new PropertyChangeSupport(this);
		return propertyChangeSupport;
	}

	/**
	 * Instantiates a new report container.
	 * 
	 * @param parent
	 *          the parent
	 */
	public ReportContainer(EditorPart parent, JasperReportsConfiguration jrContext) {
		this.parent = parent;
		this.jrContext = jrContext;
		this.selectionCache = new CommonSelectionCacheProvider();
		//Store the selection cache
		jrContext.put(SELECTION_CACHE_KEY, selectionCache);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#getActiveEditor()
	 */
	@Override
	public IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	@Override
	protected void createPages() {
		try {
			reportEditor = new ReportEditor(jrContext);
			int index = addPage(reportEditor, getEditorInput());
			setPageText(index, Messages.common_main_report);
			setPageImage(index, reportEditor.getPartImage());
			editors.add(reportEditor);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.common_error_creating_nested_visual_editor, null,
					e.getStatus());
		}
		getEditorSite().getActionBarContributor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		for (AbstractVisualEditor editor : editors) {
			editor.doSave(monitor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		for (AbstractVisualEditor editor : editors) {
			editor.doSaveAs();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	private PropertyChangeListener modelListener = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent evt) {

			if (evt.getPropertyName().equals(CLOSE_EDITOR_PROPERTY)) {
				AbstractVisualEditor obj = ccMap.get(evt.getOldValue());
				if (obj != null)
					removeEditorPage(evt, obj);
			} else if (evt.getNewValue() != null && evt.getOldValue() == null) {
				// createEditorPage(evt.getNewValue());
			} else if (evt.getNewValue() == null && evt.getOldValue() != null) {
				AbstractVisualEditor obj = ccMap.get(evt.getOldValue());
				if (obj != null)
					removeEditorPage(evt, obj);
			}
			getPropertyChangeSupport().firePropertyChange(evt);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					firePropertyChange(ISaveablePart.PROP_DIRTY);
				}
			});
		}

	};

	/**
	 * Sets the model.
	 * 
	 * @param model
	 *          the new model
	 */
	public void setModel(INode model) {
		if (this.model != null && this.model.getChildren() != null && !this.model.getChildren().isEmpty())
			this.model.getChildren().get(0).getPropertyChangeSupport().removePropertyChangeListener(modelListener);
		if (model != null && model.getChildren() != null && !model.getChildren().isEmpty())
			model.getChildren().get(0).getPropertyChangeSupport().addPropertyChangeListener(modelListener);
		this.model = model;
		updateVisualView();
	}

	private Map<Object, AbstractVisualEditor> ccMap = new HashMap<Object, AbstractVisualEditor>();

	private ExtensionManager m = JaspersoftStudioPlugin.getExtensionManager();

	private AbstractVisualEditor createEditorPage(Object obj) {
		AbstractVisualEditor ave = ccMap.get(obj);
		try {
			if (ave == null) {
				JasperDesign jd = getModel().getJasperDesign();
				MRoot root = new MRoot(null, jd);
				MPage rep = new MPage(root, jd);
				rep.setJasperConfiguration(jrContext);
				ANode node = m.createNode(rep, obj, -1);

				ave = m.getEditor(obj, jrContext);
				if (ave != null) {
					ave.getEditDomain().setCommandStack(reportEditor.getEditDomain().getCommandStack());
					//Necessary to create element with the drag and drop inside a subeditor
					ave.getEditDomain().setPaletteViewer(reportEditor.getEditDomain().getPaletteViewer());
					
					final int index = addPage(ave, getEditorInput());

					editors.add(ave);
					ccMap.put(node.getValue(), ave);
					ave.setModel(root);
					setPageText(index, ave.getPartName());
					setPageImage(index, ave.getPartImage());

					rep.getPropertyChangeSupport().addPropertyChangeListener(modelListener);

					ave.addPropertyListener(titleListener);
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return ave;
	}

	private IPropertyListener titleListener = new IPropertyListener() {

		@Override
		public void propertyChanged(Object source, int propId) {
			if (propId == IWorkbenchPart.PROP_TITLE) {
				int ind = editors.indexOf((AbstractVisualEditor) source);
				setPageText(ind, ((AbstractVisualEditor) source).getPartName());
			}
		}
	};

	private void removeEditorPage(PropertyChangeEvent evt, AbstractVisualEditor ave) {
		if (ave.getModel() != null && modelListener != null)
			ave.getModel().getPropertyChangeSupport().addPropertyChangeListener(modelListener);
		ave.setModel(null);
		ave.removePropertyListener(titleListener);
		int ind = editors.indexOf(ave);
		if (ind >= 0 && ind < getPageCount())
			removePage(ind);
		editors.remove(ind);
		if (evt != null) {
			ccMap.remove(evt.getOldValue());
		} else {
			Object okey = null;
			for (Object key : ccMap.keySet()) {
				AbstractVisualEditor value = ccMap.get(key);
				if (value != null && value == ave) {
					okey = key;
					break;
				}
			}
			ccMap.remove(okey);
		}
		ave.dispose();
	}

	/**
	 * Update visual view.
	 */
	public void updateVisualView() {
		if (!editors.isEmpty()) {
			editors.get(0).setModel(this.model);
			while (editors.size() > 1) {
				AbstractVisualEditor ave = editors.get(1);
				removeEditorPage(null, ave);
			}
			setActiveEditor(editors.get(0));
		}
		for (AbstractVisualEditor ave : editors) {
			ave.setModel(this.model);
		}

		// AbstractVisualEditor ave = getMainEditor();
		// if (ave != null)
		// ave.setModel(this.model);
	}

	public AbstractVisualEditor getMainEditor() {
		if (editors != null && !editors.isEmpty())
			return editors.get(0);
		return null;
	}

	/**
	 * Check if there are subeditors opened
	 * 
	 * @return true if there are subeditors opened, false otherwise
	 */
	public boolean hasSubeditorOpened() {
		return (ccMap != null && !ccMap.isEmpty());
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public INode getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
		if (type == IPropertySource.class)
			return getPropertySheetPage();
		if (type == IPropertySource2.class)
			return getPropertySheetPage();
		if (type == IPropertySheetPage.class)
			return getPropertySheetPage();
		return super.getAdapter(type);
	}

	/** The property sheet page. */
	private IPropertySheetPage propertySheetPage;

	private ReportEditor reportEditor;

	/**
	 * Gets the property sheet page.
	 * 
	 * @return the property sheet page
	 */
	public IPropertySheetPage getPropertySheetPage() {
		// if (propertySheetPage == null)
		propertySheetPage = new TabbedPropertySheetPage(ReportContainer.this, true);

		return propertySheetPage;
	}
	
	public CommonSelectionCacheProvider getSelectionCache(){
		return selectionCache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor#getContributorId()
	 */
	public String getContributorId() {
		return "com.jaspersoft.studio.editor.report.ReportContainer"; //$NON-NLS-1$
	}
	
	/**
	 * Create a fake command to force the refresh of the editor and outline panels, this override
	 * the disable refresh flag, so calling this the editor area is always updated
	 */
	protected void refreshVisuals(INode report){
			 if (report != null){
				 PropertyChangeEvent event = new PropertyChangeEvent(report.getJasperDesign(), JSSCompoundCommand.REFRESH_UI_EVENT, null, null);
				 report.getPropertyChangeSupport().firePropertyChange(event);
			 }
	}
	
	/**
	 * Get the object that is modified by a subeditor. It calculated searching the last node 
	 * of the mpage of the subeditor (since the first are the styles, the dataset...)
	 * 
	 * @param searchNode the starting node
	 * @return the node modified by the subeditor
	 */
	private INode getInnerModel(INode searchNode){
		INode actualNode = searchNode;
		if (actualNode instanceof MRoot && actualNode.getChildren().size() >0){
			return getInnerModel(actualNode.getChildren().get(0));
		} else if (actualNode instanceof MPage && actualNode.getChildren().size()>0){
			return actualNode.getChildren().get(actualNode.getChildren().size()-1);
		}
		return actualNode;
	}

	@Override
	protected void postPageChange(int newPageIndex, int oldPageIndex) {
		AbstractVisualEditor activeEditor = editors.get(newPageIndex);
		//request the rapaint of the element on the main editor node when switching between the subeditors, supposing they were modified in the subeditor
		if (oldPageIndex > 0){
			AbstractVisualEditor oldEditor = editors.get(oldPageIndex);
			INode subModel = getInnerModel(oldEditor.getModel());
			((JRChangeEventsSupport)subModel.getValue()).getEventSupport().firePropertyChange(MGraphicElement.FORCE_GRAPHICAL_REFRESH, null, null);
		}
		IEditorActionBarContributor contributor = parent.getEditorSite().getActionBarContributor();
		if (contributor != null && contributor instanceof MultiPageEditorActionBarContributor) {

			((MultiPageEditorActionBarContributor) contributor).setActivePage(activeEditor);
		}
	}

	public void openEditor(Object obj, ANode node) {
		if (getEditorInput() instanceof FileEditorInput) {
			if (obj instanceof JRDesignReportTemplate || obj instanceof JRSimpleTemplate || obj instanceof JRStyle
					|| obj instanceof JRConditionalStyle || obj instanceof JRTemplateReference) {
				StyleTemplateFactory.openEditor(obj, getEditorInput(), node);
				return;
			}
			if (obj instanceof JRDesignSubreport) {
				if (getEditorInput() instanceof FileEditorInput) {
					JRDesignSubreport s = (JRDesignSubreport) obj;
					if (s.getExpression() != null) {
						String path = ExpressionUtil.eval(s.getExpression(), jrContext);
						if (path != null) {
							String fpath = path.replaceAll(".jasper", ".jrxml");
							try {
								RepositoryUtil.getInstance(jrContext).getBytesFromLocation(path.replaceAll(".jasper", ".jrxml"));

							} catch (JRException e) {
								e.printStackTrace();
								try {
									RepositoryUtil.getInstance(jrContext).getBytesFromLocation(path);
									if (!UIUtils.showConfirmation("Subreport File",
											String.format("File %s does not exists, do you want to open %s?", fpath, path)))
										return;
									fpath = path;
								} catch (JRException e1) {
									UIUtils.showError(e1);
									return;
								}
							}
							SelectionHelper.openEditor((FileEditorInput) getEditorInput(), fpath);
						}
					}
				}
				return;
			}
			if (obj instanceof JRDesignImage) {
				if (getEditorInput() instanceof FileEditorInput) {
					JRDesignImage s = (JRDesignImage) obj;
					if (s.getExpression() != null)
						SelectionHelper.openEditor((FileEditorInput) getEditorInput(),
								ExpressionUtil.eval(s.getExpression(), jrContext));
				}
				return;
			}
		}
		if (obj instanceof JRDesignScriptlet) {
			String str = ((JRDesignScriptlet) obj).getValueClassName();
			IProject prj = ((FileEditorInput) getEditorInput()).getFile().getProject();
			IJavaProject javaProject = JavaCore.create(prj);
			if (javaProject != null)
				try {
					IType type = javaProject.findType(str);
					if (type != null)
						JavaUI.openInEditor(type);
				} catch (PartInitException e) {
					e.printStackTrace();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}

		}
		if (obj instanceof JasperDesign) {
			setActivePage(0);
		} else {
			AbstractVisualEditor ave = createEditorPage(obj);
			if (ave != null) {
				/**
				 * If was created another editor with inside an mpage the i save the parent of the current node inside the page.
				 * Doing this it is always possible from a node get its real parent and go back into the hierarchy. This
				 * information need only to be saved here since when an element change parent all the open editors for the
				 * element are closed
				 */
				if (ave.getModel().getChildren().size() > 0 && ave.getModel().getChildren().get(0) instanceof MPage) {
					MPage pageElement = (MPage) ave.getModel().getChildren().get(0);
					pageElement.setRealParent(node.getParent());
				}
				if (getActiveEditor() != ave) {
					int index = editors.indexOf(ave);
					if (index > 0 && index <= editors.size() - 1) {
						setActivePage(index);
						final Composite prnt = getContainer().getParent();
						final Point size = prnt.getSize();
						prnt.getParent().setSize(size.x - 2, size.y - 2);
						UIUtils.getDisplay().asyncExec(new Runnable() {
							@Override
							public void run() {
								prnt.getParent().setSize(size.x, size.y);
							}
						});

						// if (obj instanceof JRDesignElement)
						// SelectionHelper.setSelection((JRDesignElement) obj, true);
						// ave.getGraphicalViewer().setSelection(new StructuredSelection(obj));
					}
				}
			}
		}
	}
}
