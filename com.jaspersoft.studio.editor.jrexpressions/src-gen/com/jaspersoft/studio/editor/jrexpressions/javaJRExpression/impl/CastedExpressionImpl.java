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

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Casted Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CastedExpressionImpl#getCastType <em>Cast Type</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CastedExpressionImpl#getCastedExpr <em>Casted Expr</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CastedExpressionImpl extends JasperReportsExpressionImpl implements CastedExpression
{
  /**
   * The cached value of the '{@link #getCastType() <em>Cast Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCastType()
   * @generated
   * @ordered
   */
  protected Type castType;

  /**
   * The cached value of the '{@link #getCastedExpr() <em>Casted Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCastedExpr()
   * @generated
   * @ordered
   */
  protected JasperReportsExpression castedExpr;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CastedExpressionImpl()
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
    return JavaJRExpressionPackage.Literals.CASTED_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Type getCastType()
  {
    return castType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCastType(Type newCastType, NotificationChain msgs)
  {
    Type oldCastType = castType;
    castType = newCastType;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE, oldCastType, newCastType);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCastType(Type newCastType)
  {
    if (newCastType != castType)
    {
      NotificationChain msgs = null;
      if (castType != null)
        msgs = ((InternalEObject)castType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE, null, msgs);
      if (newCastType != null)
        msgs = ((InternalEObject)newCastType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE, null, msgs);
      msgs = basicSetCastType(newCastType, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE, newCastType, newCastType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JasperReportsExpression getCastedExpr()
  {
    return castedExpr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCastedExpr(JasperReportsExpression newCastedExpr, NotificationChain msgs)
  {
    JasperReportsExpression oldCastedExpr = castedExpr;
    castedExpr = newCastedExpr;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR, oldCastedExpr, newCastedExpr);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCastedExpr(JasperReportsExpression newCastedExpr)
  {
    if (newCastedExpr != castedExpr)
    {
      NotificationChain msgs = null;
      if (castedExpr != null)
        msgs = ((InternalEObject)castedExpr).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR, null, msgs);
      if (newCastedExpr != null)
        msgs = ((InternalEObject)newCastedExpr).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR, null, msgs);
      msgs = basicSetCastedExpr(newCastedExpr, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR, newCastedExpr, newCastedExpr));
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
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE:
        return basicSetCastType(null, msgs);
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR:
        return basicSetCastedExpr(null, msgs);
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
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE:
        return getCastType();
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR:
        return getCastedExpr();
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
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE:
        setCastType((Type)newValue);
        return;
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR:
        setCastedExpr((JasperReportsExpression)newValue);
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
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE:
        setCastType((Type)null);
        return;
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR:
        setCastedExpr((JasperReportsExpression)null);
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
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CAST_TYPE:
        return castType != null;
      case JavaJRExpressionPackage.CASTED_EXPRESSION__CASTED_EXPR:
        return castedExpr != null;
    }
    return super.eIsSet(featureID);
  }

} //CastedExpressionImpl
