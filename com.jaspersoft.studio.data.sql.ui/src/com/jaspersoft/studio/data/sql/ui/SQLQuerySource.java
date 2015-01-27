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
package com.jaspersoft.studio.data.sql.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.jasperreports.engine.JRParameter;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.MarginPainter;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledTextDropTargetEffect;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.editor.XtextSourceViewer;
import org.eclipse.xtext.ui.editor.embedded.EmbeddedEditor;
import org.eclipse.xtext.ui.editor.embedded.EmbeddedEditorFactory;
import org.eclipse.xtext.ui.editor.embedded.EmbeddedEditorModelAccess;
import org.eclipse.xtext.ui.editor.embedded.IEditedResourceProvider;
import org.eclipse.xtext.ui.editor.model.XtextDocument;

import com.google.inject.Injector;
import com.jaspersoft.studio.data.sql.Activator;
import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.model.AMSQLObject;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.dnd.NodeTransfer;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SQLQuerySource {
	private static final Color SRC_MARGINS_COLOR = SWTResourceManager.getColor(220, 220, 220);
	private SQLQueryDesigner designer;

	public SQLQuerySource(SQLQueryDesigner designer) {
		this.designer = designer;
	}

	private Injector getInjector() {
		return Activator.getInstance().getInjector(Activator.COM_JASPERSOFT_STUDIO_DATA_SQL);
	}

	private XtextSourceViewer viewer;

	public Control createSource(Composite parent) {
		Composite cmp = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		cmp.setLayout(layout);

		IEditedResourceProvider resourceProvider = new IEditedResourceProvider() {
			public XtextResource createResource() {
				Injector injector = getInjector();

				XtextResourceSet rs = injector.getInstance(XtextResourceSet.class);
				rs.setClasspathURIContext(getClass());

				IResourceFactory resourceFactory = injector.getInstance(IResourceFactory.class);
				org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createURI("website/My2.website");
				XtextResource resource = (XtextResource) resourceFactory.createResource(uri);
				rs.getResources().add(resource);

				EcoreUtil.resolveAll(resource);

				if (!resource.getErrors().isEmpty()) {
					// handle error?
				}
				return resource;
			}
		};

		Injector injector = getInjector();
		EmbeddedEditorFactory factory = injector.getInstance(EmbeddedEditorFactory.class);
		embeddedEditor = factory.newEditor(resourceProvider).showErrorAndWarningAnnotations().withParent(cmp);// .showErrorAndWarningAnnotations()
		EmbeddedEditorModelAccess partialEditorModelAccess = embeddedEditor.createPartialEditor();

		viewer = embeddedEditor.getViewer();

		LineNumberRulerColumn lnrc = new LineNumberRulerColumn();
		viewer.addVerticalRulerColumn(lnrc);
		viewer.showAnnotations(true);
		try {
			Method m = SourceViewer.class.getDeclaredMethod("getVerticalRuler");
			m.setAccessible(true);
			IVerticalRuler ivr = (IVerticalRuler) m.invoke(embeddedEditor.getViewer());
			if (ivr instanceof CompositeRuler) {
				CompositeRuler cr = (CompositeRuler) ivr;
				cr.getControl().setBackground(SRC_MARGINS_COLOR);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		MarginPainter fMarginPainter = new MarginPainter(viewer);
		fMarginPainter.setMarginRulerColumn(0);
		fMarginPainter.setMarginRulerColor(SRC_MARGINS_COLOR);

		viewer.addPainter(fMarginPainter);
		viewer.getDocument().addDocumentListener(new IDocumentListener() {

			@Override
			public void documentChanged(DocumentEvent event) {
				designer.doSourceTextChanged();
				if (designer.getActiveEditor() == SQLQuerySource.this)
					setDirty(true);
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {

			}
		});

		DropTarget target = new DropTarget(viewer.getTextWidget(), DND.DROP_MOVE | DND.DROP_COPY);
		target.setTransfer(new Transfer[] { NodeTransfer.getInstance(), TemplateTransfer.getInstance(), PluginTransfer.getInstance() });
		target.addDropListener(new StyledTextDropTargetEffect(viewer.getTextWidget()) {
			@Override
			public void drop(DropTargetEvent event) {
				Object obj = event.data;
				if (obj.getClass().isArray()) {
					Object[] arr = (Object[]) obj;
					if (arr.length > 0)
						obj = arr[0];
				}
				if (obj instanceof AMSQLObject) {
					performCustomDropOperations((AMSQLObject) obj);
					StringBuffer oldText = new StringBuffer(getQuery());
					oldText.insert(viewer.getTextWidget().getCaretOffset(), " " + ((AMSQLObject) obj).toSQLString() + " ");
					viewer.getDocument().set(oldText.toString());
				} else if (obj instanceof JRParameter) {
					StringBuffer oldText = new StringBuffer(getQuery());

					oldText.insert(viewer.getTextWidget().getCaretOffset(), " $P{" + ((JRParameter) obj).getName() + "} ");
					viewer.getDocument().set(oldText.toString());
				}
			}

			/*
			 * Should perform some custom drop operations here depending on the
			 * specific type of AMSQLObject.
			 */
			private void performCustomDropOperations(AMSQLObject obj) {
				// TODO for Slavic - Bugzilla #34318: TEMPORARY FIX THAT YOU SHOULD
				// REVIEW
				// Forcing the loading of the tables information so the user can use
				// smoothly
				// the graphical editor (Diagram Tab) without NPE.
				if (obj instanceof MSqlTable) {
					designer.getDbMetadata().loadTable((MSqlTable) obj);
				}
			}
		});
		viewer.getTextWidget().setData(SQLQueryDesigner.SQLQUERYDESIGNER, designer);
		return cmp;
	}

	// @Inject
	// private ToggleSLCommentAction.Factory toggleSLCommentActionFactory;

	// protected void createActions() {
	// if
	// (embeddedEditor.getConfiguration().getContentFormatter(embeddedEditor.getViewer())
	// != null) {
	//			Action action = new TextOperationAction(XtextUIMessages.getResourceBundle(), "Format.", embeddedEditor, ISourceViewer.FORMAT); //$NON-NLS-1$
	// action.setActionDefinitionId(JasperReportsPlugin.PLUGIN_ID +
	// ".FormatAction");
	//			//		setAction("Format", action); //$NON-NLS-1$
	//			//		markAsStateDependentAction("Format", true); //$NON-NLS-1$
	//			//		markAsSelectionDependentAction("Format", true); //$NON-NLS-1$
	// //
	// }
	//		ToggleSLCommentAction action = toggleSLCommentActionFactory.create(XtextUIMessages.getResourceBundle(), "ToggleComment.", this); //$NON-NLS-1$
	// action.setActionDefinitionId(JasperReportsPlugin.PLUGIN_ID +
	// ".ToggleCommentAction");
	//		//		setAction("ToggleComment", action); //$NON-NLS-1$
	//		//		markAsStateDependentAction("ToggleComment", true); //$NON-NLS-1$
	// // markAsSelectionDependentAction("ToggleComment", true);
	// configureToggleCommentAction(action);
	// //
	// // actioncontributor.contributeActions(this);
	// }

	// protected void configureToggleCommentAction(ToggleSLCommentAction action) {
	// ISourceViewer sourceViewer = embeddedEditor.getViewer();
	// SourceViewerConfiguration configuration =
	// embeddedEditor.getConfiguration();
	// action.configure(sourceViewer, configuration);
	// }

	public String getQuery() {
		return viewer.getDocument().get();
	}

	public void setQuery(String txt) {
		viewer.getDocument().set(txt);
		setDirty(false);
	}

	public void dispose() {

	}

	private boolean isDirty = false;
	private EmbeddedEditor embeddedEditor;

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public XtextDocument getXTextDocument() {
		return (XtextDocument) viewer.getDocument();
	}

	public void setupFont(JasperReportsConfiguration jConfig) {
		// viewer.getTextWidget().setFont(FontUtils.getEditorsFont(jConfig));
	}
}
