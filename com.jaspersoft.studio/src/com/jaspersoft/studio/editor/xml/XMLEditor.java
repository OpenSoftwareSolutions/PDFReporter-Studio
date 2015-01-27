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
package com.jaspersoft.studio.editor.xml;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wb.swt.ColorManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.xml.outline.EditorContentOutlinePage;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class XMLEditor.
 */
public class XMLEditor extends TextEditor {

	/** The color manager. */
	private ColorManager colorManager;

	/** The action registry. */
	private ActionRegistry actionRegistry = new ActionRegistry() {
		@Override
		public org.eclipse.jface.action.IAction getAction(Object key) {
			return XMLEditor.this.getAction((String) key);
		};
	};

	/**
	 * Instantiates a new xML editor.
	 */
	public XMLEditor(JasperReportsConfiguration jrContext) {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider(jrContext));
	}
	
	/**
	 * When the editor is graphically created then add a text change listener
	 * to the viewer
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		getSourceViewer().addTextListener(new ITextListener() {
			
			@Override
			public void textChanged(TextEvent event) {
				if (outlinePage != null) {
					String text = getSourceViewer().getTextWidget().getText();
					try{
						outlinePage.setInput(text);
					}catch(Exception ex){
						
					}
				}
			}
		});
	}

	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		if (outlinePage != null)
			outlinePage.setInput(input);
	}

	@Override
	protected void editorSaved() {
		super.editorSaved();
		if (outlinePage != null)
			outlinePage.update();
	}

	@Override
	protected void createActions() {
		super.createActions();
		ResourceBundle bundle = new NodeIconDescriptor("").getResourceBundle(JaspersoftStudioPlugin.getInstance());
		setAction("ContentFormatProposal", new TextOperationAction(bundle, "ContentFormatProposal.", this,
				ISourceViewer.FORMAT));
		setAction("ContentAssistProposal", new TextOperationAction(bundle, "ContentAssistProposal.", this,
				ISourceViewer.CONTENTASSIST_PROPOSALS));
		setAction("ContentAssistTip", new TextOperationAction(bundle, "ContentAssistTip.", this,
				ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#dispose()
	 */
	@Override
	public void dispose() {
		colorManager.dispose();
		if (outlinePage != null)
			outlinePage.setInput(null);
		super.dispose();
	}

	/**
	 * Gets the action registry.
	 * 
	 * @return the action registry
	 */
	public ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	private EditorContentOutlinePage outlinePage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ActionRegistry.class)
			return getActionRegistry();
		if (IContentOutlinePage.class.equals(adapter))
			return getOutlineView();

		return super.getAdapter(adapter);
	}

	protected IContentOutlinePage getOutlineView() {
		if (outlinePage == null) {
			outlinePage = new EditorContentOutlinePage(this);
			if (getEditorInput() != null)
				outlinePage.setInput(getEditorInput());
		}
		return outlinePage;
	}

	@Override
	protected void handleEditorInputChanged() {
		super.handleEditorInputChanged();
		if (outlinePage != null) {
			outlinePage.update();
		}
	}

	@Override
	protected void installEncodingSupport() {
		super.installEncodingSupport();
		fEncodingSupport.setEncoding("UTF-8");
	}

}
