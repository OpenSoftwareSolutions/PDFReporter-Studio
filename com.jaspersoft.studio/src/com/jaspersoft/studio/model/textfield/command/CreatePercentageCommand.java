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
package com.jaspersoft.studio.model.textfield.command;

import java.math.BigDecimal;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.model.textfield.MPercentage;
import com.jaspersoft.studio.model.textfield.command.wizard.PercentageWizard;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 * @author Giulio Toffoli	06/07/11 21.59	Fixed the way the variable name is created, the generated expression and textfield evaluation time

 */
public class CreatePercentageCommand extends CreateElementCommand {

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreatePercentageCommand(MElementGroup destNode, MPercentage srcNode, int index) {
		super(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreatePercentageCommand(MFrame destNode, MPercentage srcNode, int index) {
		super(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreatePercentageCommand(MBand destNode, MPercentage srcNode, int index) {
		super(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param position
	 *          the position
	 * @param index
	 *          the index
	 */
	public CreatePercentageCommand(ANode destNode, MPercentage srcNode, Rectangle position, int index) {
		super(destNode, srcNode, position, index);
	}

	/**
	 * Creates the object.
	 */
	@Override
	protected void createObject() {
		if (jrElement == null) {
			JRCloneable field = null;
			ResetTypeEnum rtype = ResetTypeEnum.REPORT;
			JRGroup group = null;

			PercentageWizard wizard = new PercentageWizard();
			WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
			wizard.init(jasperDesign);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				field = wizard.getField();
				rtype = wizard.getResetType();
				group = wizard.getGroup();

				super.createObject();
				if (field != null) {
					JRDesignTextField tf = (JRDesignTextField) jrElement;
					
					
					// Create the expressions based of the reset type selected by the user...
					JRDesignVariable variable = null;
					try {
						if (field instanceof JRField)
						{
							variable = createVariable(((JRField) field).getName(),
									((JRField) field).getValueClassName(), rtype, group);
							
						}
						else if (field instanceof JRVariable)
						{
							variable = createVariable(((JRVariable) field).getName(),
									((JRVariable) field).getValueClassName(), rtype, group);
						}
						
						if (variable == null)
						{
							return; // we don't want to continue in this case...
						}
						
						jasperDesign.addVariable(variable);
						
					} catch (Exception e) {
						UIUtils.showError(e);
						
					}
					
					JRDesignExpression expression = new JRDesignExpression();
					if (field instanceof JRField)
						expression.setText(createExpression(((JRField) field).getName(), variable.getName(), ((JRField) field).getValueClass()));
					if (field instanceof JRVariable)
						expression.setText(createExpression(((JRVariable) field).getName(), variable.getName(), ((JRVariable) field).getValueClass()));
					tf.setExpression(expression);
					tf.setPattern("#,##0.00%");
					
					// Set the evaluation time of this textfield to AUTO
					tf.setEvaluationTime( net.sf.jasperreports.engine.type.EvaluationTimeEnum.AUTO);

					
				}
			}
		}
	}

	private String createExpression(String name, String vname, Class<?> clazz) {
		if (clazz.isAssignableFrom(Integer.class))
			return "new Double($F{" + name + "}.intValue() / $V{" + vname + "}.intValue())";
		if (clazz.isAssignableFrom(Byte.class))
			return "new Double($F{" + name + "}.byteValue() / $V{" + vname + "}.byteValue())";
		if (clazz.isAssignableFrom(Short.class))
			return "new Double($F{" + name + "}.shortValue() / $V{" + vname + "}.shortValue())";
		if (clazz.isAssignableFrom(Float.class))
			return "new Float($F{" + name + "}.floatValue() / $V{" + vname + "}.floatValue())";
		if (clazz.isAssignableFrom(Double.class))
			return "new Double($F{" + name + "}.doubleValue() / $V{" + vname + "}.doubleValue())";
		if (clazz.isAssignableFrom(BigDecimal.class))
			return "$F{" + name + "} / $V{" + vname + "}";

		return "";
	}

	public static boolean isNumber(Class<?> clazz) {
		if (clazz.isAssignableFrom(Integer.class))
			return true;
		if (clazz.isAssignableFrom(Byte.class))
			return true;
		if (clazz.isAssignableFrom(Short.class))
			return true;
		if (clazz.isAssignableFrom(Float.class))
			return true;
		if (clazz.isAssignableFrom(Double.class))
			return true;
		if (clazz.isAssignableFrom(BigDecimal.class))
			return true;

		return false;
	}

	private JRDesignVariable jrVariable;

	private JRDesignVariable createVariable(String name, String clazz, ResetTypeEnum rtype, JRGroup group)
			throws Exception {
		jrVariable = new JRDesignVariable();
		jrVariable.setCalculation(CalculationEnum.SUM);
		
		String vname = name + "_SUM";
		int i=0;
		while (jasperDesign.getVariablesMap().containsKey(vname))
		{
			i++;
			vname = name + "_" + i + "_SUM";
		}
		
		jrVariable.setName(vname);
		jrVariable.setResetType(rtype);
		if (rtype.equals(ResetTypeEnum.GROUP))
			jrVariable.setResetGroup(group);

		jrVariable.setValueClassName(clazz);

		JRDesignExpression jre = new JRDesignExpression();
		jre.setText("$F{" + name + "}");
		jrVariable.setExpression(jre);

		return jrVariable;
	}

}
