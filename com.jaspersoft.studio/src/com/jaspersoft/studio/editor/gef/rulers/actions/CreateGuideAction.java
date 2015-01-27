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
package com.jaspersoft.studio.editor.gef.rulers.actions;

import java.util.Arrays;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.ui.rulers.GuideEditPart;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.jaspersoft.studio.editor.gef.rulers.component.JDRulerEditPart;
import com.jaspersoft.studio.editor.gef.rulers.component.JDRulerFigure;

public class CreateGuideAction extends Action {

	private EditPartViewer viewer;

	/**
	 * Constructor
	 * 
	 * @param ruler
	 *          the viewer for the ruler on which the guide is to be created
	 */
	public CreateGuideAction(EditPartViewer ruler) {
		super(GEFMessages.Create_Guide_Label);
		viewer = ruler;
		setToolTipText(GEFMessages.Create_Guide_Tooltip);
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		JDRulerEditPart rulerEditPart = (JDRulerEditPart) viewer.getRootEditPart().getChildren().get(0);
		RulerProvider provider = rulerEditPart.getRulerProvider();

		// Determine where the guide should be created
		int[] positions = provider.getGuidePositions();
		Arrays.sort(positions);
		int index = 0;
		int newPosition = GuideEditPart.MIN_DISTANCE_BW_GUIDES + 1;
		int desiredDifference = (GuideEditPart.MIN_DISTANCE_BW_GUIDES * 2) + 1;
		boolean found = positions.length > 0 && positions[0] > desiredDifference;
		while (index < positions.length - 1 && !found) {
			if (positions[index + 1] - positions[index] > desiredDifference) {
				newPosition += positions[index];
				found = true;
			}
			index++;
		}
		if (!found && positions.length > 0)
			newPosition += positions[positions.length - 1];
		PositionDialog dlg = new PositionDialog(UIUtils.getShell(), newPosition);
		if (dlg.open() == Window.OK)
			newPosition = dlg.getPosition();

		JDRulerFigure rf = rulerEditPart.getRulerFigure();
		newPosition += rf.isHorizontal() ? rf.getHoffset() : rf.getVoffset();

		// Create the guide and reveal it
		viewer.getEditDomain().getCommandStack().execute(provider.getCreateGuideCommand(newPosition));
		viewer.reveal((EditPart) viewer.getEditPartRegistry().get(provider.getGuideAt(newPosition)));
	}

	private final class PositionDialog extends FormDialog {
		private int w;

		private PositionDialog(Shell shell, int w) {
			super(shell);
			this.w = w;
		}

		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText("Guide Position");
		}

		@Override
		protected void createFormContent(IManagedForm mform) {
			mform.getForm().setText("Guide Position");

			FormToolkit toolkit = mform.getToolkit();

			mform.getForm().getBody().setLayout(new GridLayout(4, false));

			toolkit.createLabel(mform.getForm().getBody(), "Guide Position"); //$NON-NLS-1$
			final Spinner width = new Spinner(mform.getForm().getBody(), SWT.BORDER);
			width.setValues(w, 0, Integer.MAX_VALUE, 0, 1, 10);
			width.setToolTipText("Guide Position");
			width.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					w = width.getSelection();
				}
			});
		}

		public int getPosition() {
			return w;
		}

	}
}
