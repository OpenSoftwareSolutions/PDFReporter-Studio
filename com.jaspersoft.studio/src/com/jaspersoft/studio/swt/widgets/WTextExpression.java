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
package com.jaspersoft.studio.swt.widgets;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.property.descriptor.expression.dialog.JRExpressionEditor;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.UIUtil;

/**
 * Expression widget re-usable in custom dialogs and wizards. The text of the expression is represented inside the
 * textbox. The button enables the use of the expression editor (shown in a separate dialog). An additional label can be
 * specified, and based upon the <code>showMode</code> flag of the constructor it will be drawn on top of the textbox
 * and button, or on the their left.
 * <p>
 * 
 * <b>ADDITIONAL NOTE</b>: the widget has a default internal layout of type {@link FormLayout}.
 * <p>
 * 
 * <b>EXPRESSION MODIFICATIONS</b>: to add custom behavior when an expression is modified/set you can either create a
 * sub-class of the {@link WTextExpression} one, overriding the {@link #setExpression(JRDesignExpression)} method:
 * 
 * <pre>
 * // ...
 * WTextExpression myExpression = new WTextExpression(container, SWT.NONE, &quot;My expression&quot;, WTextExpression.LABEL_ON_TOP) {
 * 	&#064;Override
 * 	public void setExpression(JRDesignExpression exp) {
 * 		super.setExpression(exp);
 * 		// YOUR CUSTOM CODE HERE...
 * 	}
 * };
 * // ...
 * </pre>
 * 
 * or adding a new {@link ExpressionModifiedListener} via {@link #addModifyListener(ExpressionModifiedListener)} method.
 * 
 * @author mrabbi
 * 
 */
public class WTextExpression extends Composite implements IExpressionContextSetter {

	/** No label specified */
	public static final int LABEL_NONE = 0x0000;
	/** Label painted on the left of the expression box */
	public static final int LABEL_ON_LEFT = 0x0001;
	/** Label painted on top of the expression box and button */
	public static final int LABEL_ON_TOP = 0x0002;
	/** Number of lines for the text expression widget */
	public static final int TEXT_LINE_NUMBERS = 3;

	public static final String BUTTON_ICON_PATH = "icons/resources/expressionedit-16.png"; //$NON-NLS-1$
	private int customTextLinesNumber = -1;
	private ExpressionContext expContext;
	private int oldpos = 0;
	private boolean isRefreshing = false;

	// Widgets
	private JRDesignExpression expression;
	private Text textExpression;
	private Button btnEditExpression;
	private Label label;

	// Expression modify listeners
	private List<ExpressionModifiedListener> listeners = new ArrayList<ExpressionModifiedListener>();

	/**
	 * Creates the new widget made only by a textbox and a button.
	 * 
	 * @param parent
	 *          parent composite
	 * @param style
	 *          widget style
	 */
	public WTextExpression(Composite parent, int style) {
		this(parent, style, null, LABEL_NONE, -1);
	}

	/**
	 * Creates the new widget made only by a textbox and a button.
	 * 
	 * @param parent
	 *          parent composite
	 * @param style
	 *          widget style
	 * @param number
	 *          of text lines to show
	 */
	public WTextExpression(Composite parent, int style, int linesNum) {
		this(parent, style, null, LABEL_NONE, linesNum);
	}

	/**
	 * Creates the new widget depending on the specified flag <code>showMode</code> and using the <code>textLabel</code>
	 * as additional input.
	 * <p>
	 * Please note that if <code>textLabel</code> is <code>null</code> or <code>showMode</code> uses the default value of
	 * <code>LABEL_NONE</code>, the label is not created.
	 * 
	 * @param parent
	 *          parent composite
	 * @param style
	 *          widget style
	 * @param textLabel
	 *          the information label associated to the widget
	 * @param showMode
	 *          flag to specify the label position
	 * 
	 */
	public WTextExpression(Composite parent, int style, String textLabel, int showMode) {
		this(parent, style, textLabel, showMode, -1);
	}

	/**
	 * Creates the new widget depending on the specified flag <code>showMode</code> and using the <code>textLabel</code>
	 * as additional input. The number of lines is used for the height hint computation of the text widget that will
	 * contain the expression. A valid value must be greater than zero, otherwise the default value (
	 * {@link #TEXT_LINE_NUMBERS}) is used.
	 * <p>
	 * Please note that if <code>textLabel</code> is <code>null</code> or <code>showMode</code> uses the default value of
	 * <code>LABEL_NONE</code>, the label is not created.
	 * 
	 * @param parent
	 *          parent composite
	 * @param style
	 *          widget style
	 * @param textLabel
	 *          the information label associated to the widget
	 * @param showMode
	 *          flag to specify the label position
	 * @param number
	 *          of text lines to show
	 * 
	 */
	public WTextExpression(Composite parent, int style, String textLabel, int showMode, int linesNum) {
		super(parent, style);
		this.customTextLinesNumber = linesNum;
		setLayout(new FormLayout());

		if (textLabel != null && (showMode == LABEL_ON_LEFT || showMode == LABEL_ON_TOP)) {
			// Create the needed label
			label = new Label(this, SWT.NONE);
			label.setText(textLabel);
		} else {
			showMode = LABEL_NONE;
		}

		textExpression = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		textExpression.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!isRefreshing) {
					String text = textExpression.getText();
					oldpos = textExpression.getCaretPosition();
					if (text.isEmpty()) {
						setExpression(null);
					} else {
						setExpression(new JRDesignExpression(text));
					}
				}
			}
		});

		btnEditExpression = new Button(this, SWT.FLAT);
		btnEditExpression.setImage(JaspersoftStudioPlugin.getInstance().getImage(BUTTON_ICON_PATH));
		btnEditExpression.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if(!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
					JRExpressionEditor wizard = new JRExpressionEditor();
					wizard.setValue(expression);
					wizard.setExpressionContext(expContext);
					WizardDialog dialog = ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(getShell(), wizard);
					if (dialog.open() == Dialog.OK) {
						JRDesignExpression value = wizard.getValue();
						setExpression(value);
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		configureWidgetsLayoutData(showMode);

	}

	/*
	 * Sets the layout data information for the custom widget controls.
	 */
	private void configureWidgetsLayoutData(int showMode) {
		int heightHint = UIUtil.getCharHeight(textExpression);
		if (showMode == LABEL_ON_LEFT) {
			// Configuration with label on left
			FormData fd_label = new FormData();
			fd_label.top = new FormAttachment(0, 3);
			fd_label.left = new FormAttachment(0);
			label.setLayoutData(fd_label);

			FormData fd_btnEditExpression = new FormData();
			fd_btnEditExpression.top = new FormAttachment(0);
			fd_btnEditExpression.right = new FormAttachment(100);
			btnEditExpression.setLayoutData(fd_btnEditExpression);

			FormData fd_textExpression = new FormData();
			fd_textExpression.bottom = new FormAttachment(100);
			fd_textExpression.top = new FormAttachment(label, -3, SWT.TOP);
			fd_textExpression.right = new FormAttachment(btnEditExpression, -5, SWT.LEFT);
			fd_textExpression.left = new FormAttachment(label, 5);
			fd_textExpression.height = heightHint;
			textExpression.setLayoutData(fd_textExpression);
		} else if (showMode == LABEL_ON_TOP) {
			// Configuration with label on top
			FormData fd_label = new FormData();
			fd_label.left = new FormAttachment(0);
			fd_label.right = new FormAttachment(100);
			fd_label.top = new FormAttachment(0);
			label.setLayoutData(fd_label);

			FormData fd_btnEditExpression = new FormData();
			fd_btnEditExpression.top = new FormAttachment(label, 5);
			fd_btnEditExpression.right = new FormAttachment(label, 0, SWT.RIGHT);
			btnEditExpression.setLayoutData(fd_btnEditExpression);

			FormData fd_textExpression = new FormData();
			fd_textExpression.top = new FormAttachment(label, 5);
			fd_textExpression.right = new FormAttachment(btnEditExpression, -5);
			fd_textExpression.bottom = new FormAttachment(100);
			fd_textExpression.left = new FormAttachment(0);
			fd_textExpression.height = heightHint;
			textExpression.setLayoutData(fd_textExpression);
		} else {
			// Standard configuration
			final FormData fd_textExpression = new FormData();
			fd_textExpression.bottom = new FormAttachment(100);
			fd_textExpression.top = new FormAttachment(0);
			fd_textExpression.left = new FormAttachment(0);
			fd_textExpression.right = new FormAttachment(btnEditExpression, -5);
			fd_textExpression.height = heightHint;
			textExpression.setLayoutData(fd_textExpression);

			FormData fd_btnEditExpression = new FormData();
			fd_btnEditExpression.right = new FormAttachment(100);
			fd_btnEditExpression.top = new FormAttachment(0);
			btnEditExpression.setLayoutData(fd_btnEditExpression);
			fd_textExpression.width = textExpression.getBounds().width /2;
			addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					fd_textExpression.width = textExpression.getBounds().width /2;
					layout();
				}
			});
		}
	}

	/**
	 * Sets the expression for the widget.
	 * 
	 * @param exp
	 *          the expression to set
	 */
	public void setExpression(JRDesignExpression exp) {
		isRefreshing = true;
		this.expression = exp;

		// PAY ATTENTION: Checks are needed in order to avoid notification
		// loop due to the modifyEvent raised after a setText call.
		if (exp != null && !exp.getText().equals(textExpression.getText())) {
			textExpression.setText(exp.getText());
			textExpression.setToolTipText(exp.getText());
			if (exp.getText().length() >= oldpos)
				textExpression.setSelection(oldpos, oldpos);

		} else if (exp == null && !textExpression.getText().isEmpty()) {
			textExpression.setText(""); //$NON-NLS-1$
			textExpression.setToolTipText(""); //$NON-NLS-1$
		}

		// Notifies the listeners of the new expression
		fireModifyEvent();

		isRefreshing = false;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.textExpression.setEnabled(enabled);
		this.btnEditExpression.setEnabled(enabled);
		if (this.label != null) {
			this.label.setEnabled(enabled);
		}
	}

	/**
	 * Returns the currently set expression.
	 * 
	 * @return the {@link JRDesignExpression} instance set
	 */
	public JRDesignExpression getExpression() {
		return this.expression;
	}

	/**
	 * Gets the currently set number of lines for the widget. This value is used for the calculation of the text
	 * expression height hint. Default value is {@value #TEXT_LINE_NUMBERS}.
	 * 
	 * @return the number of lines
	 */
	protected int getTextLinesNumber() {
		if (customTextLinesNumber > 0)
			return customTextLinesNumber;
		return TEXT_LINE_NUMBERS;
	}

	/**
	 * Returns the text contained inside the widget text-box that represents the actual {@link JRDesignExpression}
	 * instance.
	 * 
	 * @return the text representation of the expression, an empty string if the expression is <code>null</code>
	 */
	public String getText() {
		return this.expression == null ? "" : Misc.nvl(this.getExpression().getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.editor.expression.IExpressionContextSetter#setExpressionContext(com.jaspersoft.studio.editor
	 * .expression.ExpressionContext)
	 */
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}
	
	/**
	 * @return the expression context currently set, <code>null</code> if none
	 */
	public ExpressionContext getExpressionContext() {
		return this.expContext;
	}

	/**
	 * Adds a new listener that will be notified of any expression change/notification.
	 * 
	 * @param ml
	 *          the new {@link ExpressionModifiedListener} to add
	 */
	public void addModifyListener(ExpressionModifiedListener ml) {
		listeners.add(ml);
	}

	/**
	 * Removes an {@link ExpressionModifiedListener} instance.
	 * 
	 * @param ml
	 *          the {@link ExpressionModifiedListener} instance to be removed
	 */
	public void removeModifyListener(ExpressionModifiedListener ml) {
		listeners.remove(ml);
	}

	public Text getTextControl() {
		return textExpression;
	}

	/*
	 * Notifies the listeners of the expression change.
	 */
	private void fireModifyEvent() {
		ExpressionModifiedEvent event = new ExpressionModifiedEvent(this);
		event.modifiedExpression = this.expression;
		for (ExpressionModifiedListener ml : listeners)
			ml.expressionModified(event);
	}

	@Override
	public void dispose() {
		// Remove modify listeners
		Object[] listenersArray = listeners.toArray();
		for (Object l : listenersArray) {
			removeModifyListener((ExpressionModifiedListener) l);
		}
		listeners.clear();
		listeners = null;
		super.dispose();
	}

}
