/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.editor.outline.actions;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.report.CommonSelectionCacheProvider;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.model.parameter.MParameters;
import com.jaspersoft.studio.model.parameter.command.CreateParameterCommand;
import com.jaspersoft.studio.prm.ParameterSet;
import com.jaspersoft.studio.prm.wizard.ParameterSetWizard;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class CreateParameterAction.
 */
public class CreateParameterSetAction extends ACachedSelectionAction {

	/** The Constant ID. */
	public static final String ID = "create_parameter_set"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public CreateParameterSetAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	protected boolean calculateEnabled() {
		if (getSelectedObjects().size() != 1)
			return false;
		CommonSelectionCacheProvider cache = editor.getSelectionCache();
		List<Object> selection = cache.getSelectionModelForType(MParameters.class);
		if (!selection.isEmpty() && ((ANode) selection.get(0)).getParent() instanceof MReport)
			return true;
		return false;
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.CreateParameterSetAction_0);
		setToolTipText(Messages.CreateParameterSetAction_1);
		setId(CreateParameterSetAction.ID);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED));
		setEnabled(false);
	}

	@Override
	public void run() {
		List<Object> mGraphElements = editor.getSelectionCache().getSelectionModelForType(MParameters.class);
		for (Object obj : mGraphElements) {
			if (obj instanceof MParameters) {
				MParameters<JRDesignDataset> mPrm = (MParameters<JRDesignDataset>) obj;
				JasperReportsConfiguration jConfig = mPrm.getJasperConfiguration();
				ParameterSetWizard wizard = new ParameterSetWizard(jConfig);
				WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
				dialog.create();
				if (dialog.open() == Dialog.OK) {
					ParameterSet prmSet = wizard.getValue();
					if (prmSet == null)
						return;
					boolean override = wizard.isOverride();
					JSSCompoundCommand cmd = new JSSCompoundCommand(null);
					JRDesignDataset ds = mPrm.getValue();
					for (JRDesignParameter p : prmSet.getParameters()) {
						if (ds.getParametersMap().containsKey(p.getName())) {
							if (!override)
								continue;
							MParameter target = null;
							for (INode n : mPrm.getChildren()) {
								if (n instanceof MParameter) {
									MParameter parm = (MParameter) n;
									if (parm.getValue().getName().equals(p.getName())) {
										target = parm;
										break;
									}
								}
							}

							SetValueCommand c = new SetValueCommand();
							c.setTarget(target);
							c.setPropertyId(JRDesignParameter.PROPERTY_DESCRIPTION);
							c.setPropertyValue(p.getDescription());
							cmd.add(c);

							c = new SetValueCommand();
							c.setTarget(target);
							c.setPropertyId(JRDesignParameter.PROPERTY_VALUE_CLASS_NAME);
							c.setPropertyValue(p.getValueClassName());
							cmd.add(c);

							c = new SetValueCommand();
							c.setTarget(target);
							c.setPropertyId(JRDesignParameter.PROPERTY_NESTED_TYPE_NAME);
							c.setPropertyValue(p.getNestedTypeName());
							cmd.add(c);

							c = new SetValueCommand();
							c.setTarget(target);
							c.setPropertyId(JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION);
							c.setPropertyValue(p.getDefaultValueExpression());
							cmd.add(c);

							c = new SetValueCommand();
							c.setTarget(target);
							c.setPropertyId(JRDesignParameter.PROPERTY_FOR_PROMPTING);
							c.setPropertyValue(p.isForPrompting());
							cmd.add(c);
						} else
							cmd.add(new CreateParameterCommand(ds, p, -1));
					}
					command = cmd;
				} else
					return;
				fresh = true;
				super.run();
				break;
			}
		}
	}
}
