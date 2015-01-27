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
/**
 */
package com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Test Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TestExpressionImpl#getCondition <em>Condition</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TestExpressionImpl#getTrueStatement <em>True Statement</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TestExpressionImpl#getFalseStatement <em>False Statement</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TestExpressionImpl extends JasperReportsExpressionImpl implements TestExpression
{
  /**
   * The cached value of the '{@link #getCondition() <em>Condition</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCondition()
   * @generated
   * @ordered
   */
  protected JasperReportsExpression condition;

  /**
   * The cached value of the '{@link #getTrueStatement() <em>True Statement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTrueStatement()
   * @generated
   * @ordered
   */
  protected JasperReportsExpression trueStatement;

  /**
   * The cached value of the '{@link #getFalseStatement() <em>False Statement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFalseStatement()
   * @generated
   * @ordered
   */
  protected JasperReportsExpression falseStatement;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TestExpressionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return JavaJRExpressionPackage.Literals.TEST_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JasperReportsExpression getCondition()
  {
    return condition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCondition(JasperReportsExpression newCondition, NotificationChain msgs)
  {
    JasperReportsExpression oldCondition = condition;
    condition = newCondition;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION, oldCondition, newCondition);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCondition(JasperReportsExpression newCondition)
  {
    if (newCondition != condition)
    {
      NotificationChain msgs = null;
      if (condition != null)
        msgs = ((InternalEObject)condition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION, null, msgs);
      if (newCondition != null)
        msgs = ((InternalEObject)newCondition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION, null, msgs);
      msgs = basicSetCondition(newCondition, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION, newCondition, newCondition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JasperReportsExpression getTrueStatement()
  {
    return trueStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTrueStatement(JasperReportsExpression newTrueStatement, NotificationChain msgs)
  {
    JasperReportsExpression oldTrueStatement = trueStatement;
    trueStatement = newTrueStatement;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT, oldTrueStatement, newTrueStatement);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTrueStatement(JasperReportsExpression newTrueStatement)
  {
    if (newTrueStatement != trueStatement)
    {
      NotificationChain msgs = null;
      if (trueStatement != null)
        msgs = ((InternalEObject)trueStatement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT, null, msgs);
      if (newTrueStatement != null)
        msgs = ((InternalEObject)newTrueStatement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT, null, msgs);
      msgs = basicSetTrueStatement(newTrueStatement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT, newTrueStatement, newTrueStatement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JasperReportsExpression getFalseStatement()
  {
    return falseStatement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFalseStatement(JasperReportsExpression newFalseStatement, NotificationChain msgs)
  {
    JasperReportsExpression oldFalseStatement = falseStatement;
    falseStatement = newFalseStatement;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT, oldFalseStatement, newFalseStatement);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFalseStatement(JasperReportsExpression newFalseStatement)
  {
    if (newFalseStatement != falseStatement)
    {
      NotificationChain msgs = null;
      if (falseStatement != null)
        msgs = ((InternalEObject)falseStatement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT, null, msgs);
      if (newFalseStatement != null)
        msgs = ((InternalEObject)newFalseStatement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT, null, msgs);
      msgs = basicSetFalseStatement(newFalseStatement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT, newFalseStatement, newFalseStatement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION:
        return basicSetCondition(null, msgs);
      case JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT:
        return basicSetTrueStatement(null, msgs);
      case JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT:
        return basicSetFalseStatement(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION:
        return getCondition();
      case JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT:
        return getTrueStatement();
      case JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT:
        return getFalseStatement();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION:
        setCondition((JasperReportsExpression)newValue);
        return;
      case JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT:
        setTrueStatement((JasperReportsExpression)newValue);
        return;
      case JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT:
        setFalseStatement((JasperReportsExpression)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION:
        setCondition((JasperReportsExpression)null);
        return;
      case JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT:
        setTrueStatement((JasperReportsExpression)null);
        return;
      case JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT:
        setFalseStatement((JasperReportsExpression)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case JavaJRExpressionPackage.TEST_EXPRESSION__CONDITION:
        return condition != null;
      case JavaJRExpressionPackage.TEST_EXPRESSION__TRUE_STATEMENT:
        return trueStatement != null;
      case JavaJRExpressionPackage.TEST_EXPRESSION__FALSE_STATEMENT:
        return falseStatement != null;
    }
    return super.eIsSet(featureID);
  }

} //TestExpressionImpl
