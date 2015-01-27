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
package com.jaspersoft.studio.property.section.widgets;

import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.inputhistory.InputHistoryCache;

public class SPExpression extends AHistorySPropertyWidget implements IExpressionContextSetter {
	private WTextExpression expr;

	public SPExpression(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return expr;
	}

	@Override
	protected Text getTextControl() {
		return expr.getTextControl();
	}
	
	@Override
	public Control getControlToBorder() {
		return getTextControl();
	}
	
	protected void createComponent(Composite parent) {
		expr = new WTextExpression(parent, SWT.NONE, 1);
		expr.addModifyListener(new ExpressionModifiedListener() {
			@Override
			public void expressionModified(ExpressionModifiedEvent event) {
				JRDesignExpression exp = expr.getExpression();
				section.changeProperty(pDescriptor.getId(), exp != null ? exp.clone() : null);
			}
		});
		if (parent.getLayout() instanceof GridLayout) {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			expr.setLayoutData(gd);
		}
		expr.getTextControl().addFocusListener(focusListener);
		autocomplete = new AutoCompleteField(expr.getTextControl(), new TextContentAdapter(),
				InputHistoryCache.get(getHistoryKey()));
	}

	public void setData(APropertyNode pnode, Object b) {
		expr.setExpression((JRDesignExpression) b);
		JRDesignElement designEl = null;
		if (pnode.getValue() instanceof JRDesignElement) {
			designEl = (JRDesignElement) pnode.getValue();
		}
		// Try to get an expression context for the node if any
		Object expContextAdapter = pnode.getAdapter(ExpressionContext.class);
		if(expContextAdapter!=null){
			expr.setExpressionContext((ExpressionContext)expContextAdapter);
		}
		else{
			expr.setExpressionContext(ModelUtils.getElementExpressionContext(designEl, pnode));
		}
	}

	public void setEnabled(boolean enabled) {
		expr.setEnabled(enabled);
	}

	public void setExpressionContext(ExpressionContext exprContext) {
		expr.setExpressionContext(exprContext);
	}

}
