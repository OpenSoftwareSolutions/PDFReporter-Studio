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

import com.jaspersoft.studio.data.sql.LikeOperand;
import com.jaspersoft.studio.data.sql.OpFunction;
import com.jaspersoft.studio.data.sql.OpFunctionCast;
import com.jaspersoft.studio.data.sql.SqlPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Like Operand</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.LikeOperandImpl#getOp2 <em>Op2</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.LikeOperandImpl#getFop2 <em>Fop2</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.LikeOperandImpl#getFcast <em>Fcast</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LikeOperandImpl extends MinimalEObjectImpl.Container implements LikeOperand
{
  /**
   * The default value of the '{@link #getOp2() <em>Op2</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp2()
   * @generated
   * @ordered
   */
  protected static final String OP2_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOp2() <em>Op2</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp2()
   * @generated
   * @ordered
   */
  protected String op2 = OP2_EDEFAULT;

  /**
   * The cached value of the '{@link #getFop2() <em>Fop2</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFop2()
   * @generated
   * @ordered
   */
  protected OpFunction fop2;

  /**
   * The cached value of the '{@link #getFcast() <em>Fcast</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFcast()
   * @generated
   * @ordered
   */
  protected OpFunctionCast fcast;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LikeOperandImpl()
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
    return SqlPackage.Literals.LIKE_OPERAND;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOp2()
  {
    return op2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp2(String newOp2)
  {
    String oldOp2 = op2;
    op2 = newOp2;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.LIKE_OPERAND__OP2, oldOp2, op2));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OpFunction getFop2()
  {
    return fop2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFop2(OpFunction newFop2, NotificationChain msgs)
  {
    OpFunction oldFop2 = fop2;
    fop2 = newFop2;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.LIKE_OPERAND__FOP2, oldFop2, newFop2);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFop2(OpFunction newFop2)
  {
    if (newFop2 != fop2)
    {
      NotificationChain msgs = null;
      if (fop2 != null)
        msgs = ((InternalEObject)fop2).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.LIKE_OPERAND__FOP2, null, msgs);
      if (newFop2 != null)
        msgs = ((InternalEObject)newFop2).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.LIKE_OPERAND__FOP2, null, msgs);
      msgs = basicSetFop2(newFop2, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.LIKE_OPERAND__FOP2, newFop2, newFop2));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OpFunctionCast getFcast()
  {
    return fcast;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFcast(OpFunctionCast newFcast, NotificationChain msgs)
  {
    OpFunctionCast oldFcast = fcast;
    fcast = newFcast;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.LIKE_OPERAND__FCAST, oldFcast, newFcast);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFcast(OpFunctionCast newFcast)
  {
    if (newFcast != fcast)
    {
      NotificationChain msgs = null;
      if (fcast != null)
        msgs = ((InternalEObject)fcast).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.LIKE_OPERAND__FCAST, null, msgs);
      if (newFcast != null)
        msgs = ((InternalEObject)newFcast).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.LIKE_OPERAND__FCAST, null, msgs);
      msgs = basicSetFcast(newFcast, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.LIKE_OPERAND__FCAST, newFcast, newFcast));
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
      case SqlPackage.LIKE_OPERAND__FOP2:
        return basicSetFop2(null, msgs);
      case SqlPackage.LIKE_OPERAND__FCAST:
        return basicSetFcast(null, msgs);
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
      case SqlPackage.LIKE_OPERAND__OP2:
        return getOp2();
      case SqlPackage.LIKE_OPERAND__FOP2:
        return getFop2();
      case SqlPackage.LIKE_OPERAND__FCAST:
        return getFcast();
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
      case SqlPackage.LIKE_OPERAND__OP2:
        setOp2((String)newValue);
        return;
      case SqlPackage.LIKE_OPERAND__FOP2:
        setFop2((OpFunction)newValue);
        return;
      case SqlPackage.LIKE_OPERAND__FCAST:
        setFcast((OpFunctionCast)newValue);
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
      case SqlPackage.LIKE_OPERAND__OP2:
        setOp2(OP2_EDEFAULT);
        return;
      case SqlPackage.LIKE_OPERAND__FOP2:
        setFop2((OpFunction)null);
        return;
      case SqlPackage.LIKE_OPERAND__FCAST:
        setFcast((OpFunctionCast)null);
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
      case SqlPackage.LIKE_OPERAND__OP2:
        return OP2_EDEFAULT == null ? op2 != null : !OP2_EDEFAULT.equals(op2);
      case SqlPackage.LIKE_OPERAND__FOP2:
        return fop2 != null;
      case SqlPackage.LIKE_OPERAND__FCAST:
        return fcast != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (op2: ");
    result.append(op2);
    result.append(')');
    return result.toString();
  }

} //LikeOperandImpl
