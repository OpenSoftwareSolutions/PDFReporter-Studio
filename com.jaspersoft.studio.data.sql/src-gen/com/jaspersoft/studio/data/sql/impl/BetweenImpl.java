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

import com.jaspersoft.studio.data.sql.Between;
import com.jaspersoft.studio.data.sql.Operands;
import com.jaspersoft.studio.data.sql.SqlPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Between</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.BetweenImpl#getOpBetween <em>Op Between</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.BetweenImpl#getOp2 <em>Op2</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.BetweenImpl#getOp3 <em>Op3</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BetweenImpl extends MinimalEObjectImpl.Container implements Between
{
  /**
   * The default value of the '{@link #getOpBetween() <em>Op Between</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOpBetween()
   * @generated
   * @ordered
   */
  protected static final String OP_BETWEEN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOpBetween() <em>Op Between</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOpBetween()
   * @generated
   * @ordered
   */
  protected String opBetween = OP_BETWEEN_EDEFAULT;

  /**
   * The cached value of the '{@link #getOp2() <em>Op2</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp2()
   * @generated
   * @ordered
   */
  protected Operands op2;

  /**
   * The cached value of the '{@link #getOp3() <em>Op3</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp3()
   * @generated
   * @ordered
   */
  protected Operands op3;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BetweenImpl()
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
    return SqlPackage.Literals.BETWEEN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOpBetween()
  {
    return opBetween;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOpBetween(String newOpBetween)
  {
    String oldOpBetween = opBetween;
    opBetween = newOpBetween;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.BETWEEN__OP_BETWEEN, oldOpBetween, opBetween));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operands getOp2()
  {
    return op2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOp2(Operands newOp2, NotificationChain msgs)
  {
    Operands oldOp2 = op2;
    op2 = newOp2;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.BETWEEN__OP2, oldOp2, newOp2);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp2(Operands newOp2)
  {
    if (newOp2 != op2)
    {
      NotificationChain msgs = null;
      if (op2 != null)
        msgs = ((InternalEObject)op2).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.BETWEEN__OP2, null, msgs);
      if (newOp2 != null)
        msgs = ((InternalEObject)newOp2).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.BETWEEN__OP2, null, msgs);
      msgs = basicSetOp2(newOp2, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.BETWEEN__OP2, newOp2, newOp2));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operands getOp3()
  {
    return op3;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOp3(Operands newOp3, NotificationChain msgs)
  {
    Operands oldOp3 = op3;
    op3 = newOp3;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.BETWEEN__OP3, oldOp3, newOp3);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp3(Operands newOp3)
  {
    if (newOp3 != op3)
    {
      NotificationChain msgs = null;
      if (op3 != null)
        msgs = ((InternalEObject)op3).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.BETWEEN__OP3, null, msgs);
      if (newOp3 != null)
        msgs = ((InternalEObject)newOp3).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.BETWEEN__OP3, null, msgs);
      msgs = basicSetOp3(newOp3, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.BETWEEN__OP3, newOp3, newOp3));
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
      case SqlPackage.BETWEEN__OP2:
        return basicSetOp2(null, msgs);
      case SqlPackage.BETWEEN__OP3:
        return basicSetOp3(null, msgs);
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
      case SqlPackage.BETWEEN__OP_BETWEEN:
        return getOpBetween();
      case SqlPackage.BETWEEN__OP2:
        return getOp2();
      case SqlPackage.BETWEEN__OP3:
        return getOp3();
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
      case SqlPackage.BETWEEN__OP_BETWEEN:
        setOpBetween((String)newValue);
        return;
      case SqlPackage.BETWEEN__OP2:
        setOp2((Operands)newValue);
        return;
      case SqlPackage.BETWEEN__OP3:
        setOp3((Operands)newValue);
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
      case SqlPackage.BETWEEN__OP_BETWEEN:
        setOpBetween(OP_BETWEEN_EDEFAULT);
        return;
      case SqlPackage.BETWEEN__OP2:
        setOp2((Operands)null);
        return;
      case SqlPackage.BETWEEN__OP3:
        setOp3((Operands)null);
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
      case SqlPackage.BETWEEN__OP_BETWEEN:
        return OP_BETWEEN_EDEFAULT == null ? opBetween != null : !OP_BETWEEN_EDEFAULT.equals(opBetween);
      case SqlPackage.BETWEEN__OP2:
        return op2 != null;
      case SqlPackage.BETWEEN__OP3:
        return op3 != null;
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
    result.append(" (opBetween: ");
    result.append(opBetween);
    result.append(')');
    return result.toString();
  }

} //BetweenImpl
