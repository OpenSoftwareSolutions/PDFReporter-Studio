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
package com.jaspersoft.studio.property.descriptor.classname;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.search.BasicSearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.SelectionDialog;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.ATextDialogCellEditor;
import com.jaspersoft.studio.utils.SelectionHelper;

public class ClassTypeCellEditor extends ATextDialogCellEditor {
	private List<Class<?>> classes;

	public void setClasses(List<Class<?>> classes) {
		this.classes = classes;
	}

	public ClassTypeCellEditor(Composite parent) {
		super(parent);
	}

	public ClassTypeCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		return getJavaClassDialog(cellEditorWindow.getShell(), classes);
	}

	public static String getJavaClassDialog(Shell shell, List<Class<?>> classes) {
		try {
			IJavaSearchScope searchScope = SearchEngine.createWorkspaceScope();
			if (classes != null && !classes.isEmpty()) {
				IProject prj = ((IFileEditorInput) SelectionHelper.getActiveJRXMLEditor().getEditorInput()).getFile()
						.getProject();
				if (prj != null) {
					IJavaProject jprj = JavaCore.create(prj);
					IType t;

					t = jprj.findType(classes.get(0).getName());
					// ITypeHierarchy hierarchy = t.newTypeHierarchy(new
					// NullProgressMonitor());
					// IType[] subTypes = hierarchy.getAllSubtypes(t);
					if (t != null)
						searchScope = BasicSearchEngine.createHierarchyScope(t);// (jprj, t, owner, true, true, true);

				}
			}
			// FilteredTypesSelectionDialog a = new FilteredTypesSelectionDialog();
			// a.
			// JavaModelUtil.g
			// searchScope.enclosingProjectsAndJars()[0].
			//
			// SearchEngine.createHierarchyScope(IType.);
			// IType focus = ...;
			// IJavaProject project = ...;
			// ITypeHierarchy hierarchy = focus.newTypeHierarchy(project, pm);
			// IType[] subTypes = hierarchy.getAllSubTypes(focus);
			// IJavaSearchScope scope = SearchEngine.createJavaSearchScope(subTypes);
			// SearchPattern sp = SearchPattern.createPattern("java.lang.String", IJavaSearchConstants.CLASS,
			// IJavaSearchConstants.IMPLEMENTORS, SearchPattern.R_EXACT_MATCH);
			// FilteredTypesSelectionDialog a = new FilteredTypesSelectionDialog(shell, false, new
			// ProgressMonitorDialog(shell),
			// searchScope, SearchPattern.R_EXACT_MATCH);

			// ;
			// -------------
			// IProject project; // currently selected project
			//
			// // get the java project and locate the interface type
			// JavaProject javaProject = JavaCore.create(project);
			// IType myInterface = javaProject.findType("MyInterface", "name.seller.rich");
			//
			// // get the sub types from the interface's type hierarchy
			// ITypeHierarchy hierarchy = myInterface.newTypeHierarchy(new NullProgressMonitor());
			//
			// IType[] subTypes = hierarchy.getAllSubtypes(myInterface);

			SelectionDialog dialog = JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(shell), searchScope,
					IJavaElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES, false);
			dialog.setTitle(Messages.ClassTypeCellEditor_open_type);
			dialog.setMessage(Messages.ClassTypeCellEditor_dialog_message);
			if (dialog.open() == Window.OK) {
				if (dialog.getResult() != null && dialog.getResult().length > 0) {
					IType bt = (IType) dialog.getResult()[0];
					return bt.getFullyQualifiedName();
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
