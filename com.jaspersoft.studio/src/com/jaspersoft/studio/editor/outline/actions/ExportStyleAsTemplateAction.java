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
package com.jaspersoft.studio.editor.outline.actions;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRStyle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.style.wizard.StyleTemplateImportWizard;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Action to open the wizard to export one or more JRStyle as an external template style file
 * 
 * @author Orlandin Marco
 * 
 */
public class ExportStyleAsTemplateAction extends SelectionAction {

	/** The Constant ID. */
	public static final String ID = "export_style_as_template"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public ExportStyleAsTemplateAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.ExportStyleAsTemplateAction_actionName);
		setToolTipText(Messages.ExportStyleAsTemplateAction_actionTooltip);
		setId(ExportStyleAsTemplateAction.ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/export_style.png")); //$NON-NLS-1$
		setEnabled(false);
		setLazyEnablementCalculation(true);
	}

	/**
	 * Enable only if there is at least one style that can be exported
	 */
	@Override
	protected boolean calculateEnabled() {
		return ModelUtils.checkTypesForAllEditParModels(
				getSelectedObjects(), true, new Class<?>[]{MConditionalStyle.class, MStyle.class});
	}

	@Override
	public void run() {
		StyleTemplateImportWizard importWizard = new StyleTemplateImportWizard(getSelectedStyles());
		ISelection selection = getSelection();
		StructuredSelection structured = null;
		if (selection instanceof StructuredSelection)
			structured = (StructuredSelection) selection;
		else
			structured = new StructuredSelection();
		importWizard.init(PlatformUI.getWorkbench(), structured);
		WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), importWizard);
		dialog.open();
	}

	/**
	 * Return the list of all the selected JRStyle. If a conditional style is selected then it is taken its parent.
	 * 
	 * @return a not null list of JRStyle
	 */
	private List<JRStyle> getSelectedStyles() {
		List<?> objects = getSelectedObjects();
		if (objects == null || objects.isEmpty())
			return new ArrayList<JRStyle>();
		List<JRStyle> result = new ArrayList<JRStyle>();
		for (Object obj : objects) {
			if (obj instanceof EditPart) {
				ANode n = (ANode) ((EditPart) obj).getModel();
				if (n instanceof MConditionalStyle) {
					JRStyle condStyle = (JRStyle) n.getParent().getValue();
					if (!result.contains(condStyle))
						result.add(condStyle);
				} else if (n instanceof MStyle && !result.contains(n.getValue())) {
					result.add((JRStyle) n.getValue());
				}
			}
		}
		return result;
	}
}
