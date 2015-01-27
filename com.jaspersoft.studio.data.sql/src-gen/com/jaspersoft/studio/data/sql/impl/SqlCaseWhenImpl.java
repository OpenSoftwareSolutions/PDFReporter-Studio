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
package com.jaspersoft.studio.data.sql.impl;

import com.jaspersoft.studio.data.sql.Operands;
import com.jaspersoft.studio.data.sql.OrExpr;
import com.jaspersoft.studio.data.sql.SqlCaseWhen;
import com.jaspersoft.studio.data.sql.SqlPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Case When</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.SqlCaseWhenImpl#getExpr <em>Expr</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.SqlCaseWhenImpl#getTexp <em>Texp</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.SqlCaseWhenImpl#getEexp <em>Eexp</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SqlCaseWhenImpl extends SQLCaseWhensImpl implements SqlCaseWhen
{
  /**
   * The cached value of the '{@link #getExpr() <em>Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpr()
   * @generated
   * @ordered
   */
  protected OrExpr expr;

  /**
   * The cached value of the '{@link #getTexp() <em>Texp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTexp()
   * @generated
   * @ordered
   */
  protected Operands texp;

  /**
   * The cached value of the '{@link #getEexp() <em>Eexp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEexp()
   * @generated
   * @ordered
   */
  protected Operands eexp;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SqlCaseWhenImpl()
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
    return SqlPackage.Literals.SQL_CASE_WHEN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OrExpr getExpr()
  {
    return expr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExpr(OrExpr newExpr, NotificationChain msgs)
  {
    OrExpr oldExpr = expr;
    expr = newExpr;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.SQL_CASE_WHEN__EXPR, oldExpr, newExpr);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExpr(OrExpr newExpr)
  {
    if (newExpr != expr)
    {
      NotificationChain msgs = null;
      if (expr != null)
        msgs = ((InternalEObject)expr).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.SQL_CASE_WHEN__EXPR, null, msgs);
      if (newExpr != null)
        msgs = ((InternalEObject)newExpr).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.SQL_CASE_WHEN__EXPR, null, msgs);
      msgs = basicSetExpr(newExpr, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.SQL_CASE_WHEN__EXPR, newExpr, newExpr));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operands getTexp()
  {
    return texp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTexp(Operands newTexp, NotificationChain msgs)
  {
    Operands oldTexp = texp;
    texp = newTexp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.SQL_CASE_WHEN__TEXP, oldTexp, newTexp);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTexp(Operands newTexp)
  {
    if (newTexp != texp)
    {
      NotificationChain msgs = null;
      if (texp != null)
        msgs = ((InternalEObject)texp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.SQL_CASE_WHEN__TEXP, null, msgs);
      if (newTexp != null)
        msgs = ((InternalEObject)newTexp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.SQL_CASE_WHEN__TEXP, null, msgs);
      msgs = basicSetTexp(newTexp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.SQL_CASE_WHEN__TEXP, newTexp, newTexp));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operands getEexp()
  {
    return eexp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEexp(Operands newEexp, NotificationChain msgs)
  {
    Operands oldEexp = eexp;
    eexp = newEexp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.SQL_CASE_WHEN__EEXP, oldEexp, newEexp);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEexp(Operands newEexp)
  {
    if (newEexp != eexp)
    {
      NotificationChain msgs = null;
      if (eexp != null)
        msgs = ((InternalEObject)eexp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.SQL_CASE_WHEN__EEXP, null, msgs);
      if (newEexp != null)
        msgs = ((InternalEObject)newEexp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.SQL_CASE_WHEN__EEXP, null, msgs);
      msgs = basicSetEexp(newEexp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.SQL_CASE_WHEN__EEXP, newEexp, newEexp));
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
      case SqlPackage.SQL_CASE_WHEN__EXPR:
        return basicSetExpr(null, msgs);
      case SqlPackage.SQL_CASE_WHEN__TEXP:
        return basicSetTexp(null, msgs);
      case SqlPackage.SQL_CASE_WHEN__EEXP:
        return basicSetEexp(null, msgs);
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
      case SqlPackage.SQL_CASE_WHEN__EXPR:
        return getExpr();
      case SqlPackage.SQL_CASE_WHEN__TEXP:
        return getTexp();
      case SqlPackage.SQL_CASE_WHEN__EEXP:
        return getEexp();
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
      case SqlPackage.SQL_CASE_WHEN__EXPR:
        setExpr((OrExpr)newValue);
        return;
      case SqlPackage.SQL_CASE_WHEN__TEXP:
        setTexp((Operands)newValue);
        return;
      case SqlPackage.SQL_CASE_WHEN__EEXP:
        setEexp((Operands)newValue);
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
      case SqlPackage.SQL_CASE_WHEN__EXPR:
        setExpr((OrExpr)null);
        return;
      case SqlPackage.SQL_CASE_WHEN__TEXP:
        setTexp((Operands)null);
        return;
      case SqlPackage.SQL_CASE_WHEN__EEXP:
        setEexp((Operands)null);
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
      case SqlPackage.SQL_CASE_WHEN__EXPR:
        return expr != null;
      case SqlPackage.SQL_CASE_WHEN__TEXP:
        return texp != null;
      case SqlPackage.SQL_CASE_WHEN__EEXP:
        return eexp != null;
    }
    return super.eIsSet(featureID);
  }

} //SqlCaseWhenImpl
