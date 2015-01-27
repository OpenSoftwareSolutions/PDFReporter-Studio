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
package com.jaspersoft.studio.editor.style.command;

import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.model.style.MStyleTemplateReference;
import com.jaspersoft.studio.model.style.MStylesTemplate;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateStyleTemplateReferenceCommand extends Command {

	private JRTemplateReference jrTemplate;

	private JRSimpleTemplate jrDesign;

	private int index;

	/**
	 * Instantiates a new creates the style template command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateStyleTemplateReferenceCommand(MStylesTemplate destNode, MStyleTemplateReference srcNode, int index) {
		super();
		this.jrDesign = (JRSimpleTemplate) destNode.getValue();
		this.index = index;
		if (srcNode != null && srcNode.getValue() != null)
			this.jrTemplate = (JRTemplateReference) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrTemplate != null) {
			if (index < 0 || index > jrDesign.getIncludedTemplatesList().size())
				jrDesign.addIncludedTemplate(jrTemplate);
			else
				jrDesign.addIncludedTemplate(index, jrTemplate);
		}
	}

	private void createObject() {
		if (jrTemplate == null) {
			FilteredResourcesSelectionDialog fd = new FilteredResourcesSelectionDialog(Display.getCurrent().getActiveShell(),
					false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
			fd.setInitialPattern("*.jrtx");//$NON-NLS-1$
			if (fd.open() == Dialog.OK) {
				IFile file = (IFile) fd.getFirstResult();

				this.jrTemplate = MStyleTemplateReference.createJRTemplate();
				jrTemplate.setLocation(file.getProjectRelativePath().toPortableString());

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrDesign.removeIncludedTemplate(jrTemplate);
	}
}
