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

import com.jaspersoft.studio.data.sql.InOper;
import com.jaspersoft.studio.data.sql.OperandList;
import com.jaspersoft.studio.data.sql.SqlPackage;
import com.jaspersoft.studio.data.sql.SubQueryOperand;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>In Oper</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.InOperImpl#getOp <em>Op</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.InOperImpl#getSubquery <em>Subquery</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.InOperImpl#getOpList <em>Op List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InOperImpl extends MinimalEObjectImpl.Container implements InOper
{
  /**
   * The default value of the '{@link #getOp() <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected static final String OP_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOp() <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected String op = OP_EDEFAULT;

  /**
   * The cached value of the '{@link #getSubquery() <em>Subquery</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubquery()
   * @generated
   * @ordered
   */
  protected SubQueryOperand subquery;

  /**
   * The cached value of the '{@link #getOpList() <em>Op List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOpList()
   * @generated
   * @ordered
   */
  protected OperandList opList;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InOperImpl()
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
    return SqlPackage.Literals.IN_OPER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOp()
  {
    return op;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp(String newOp)
  {
    String oldOp = op;
    op = newOp;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.IN_OPER__OP, oldOp, op));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SubQueryOperand getSubquery()
  {
    return subquery;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubquery(SubQueryOperand newSubquery, NotificationChain msgs)
  {
    SubQueryOperand oldSubquery = subquery;
    subquery = newSubquery;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.IN_OPER__SUBQUERY, oldSubquery, newSubquery);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubquery(SubQueryOperand newSubquery)
  {
    if (newSubquery != subquery)
    {
      NotificationChain msgs = null;
      if (subquery != null)
        msgs = ((InternalEObject)subquery).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.IN_OPER__SUBQUERY, null, msgs);
      if (newSubquery != null)
        msgs = ((InternalEObject)newSubquery).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.IN_OPER__SUBQUERY, null, msgs);
      msgs = basicSetSubquery(newSubquery, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.IN_OPER__SUBQUERY, newSubquery, newSubquery));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OperandList getOpList()
  {
    return opList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOpList(OperandList newOpList, NotificationChain msgs)
  {
    OperandList oldOpList = opList;
    opList = newOpList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.IN_OPER__OP_LIST, oldOpList, newOpList);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOpList(OperandList newOpList)
  {
    if (newOpList != opList)
    {
      NotificationChain msgs = null;
      if (opList != null)
        msgs = ((InternalEObject)opList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.IN_OPER__OP_LIST, null, msgs);
      if (newOpList != null)
        msgs = ((InternalEObject)newOpList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.IN_OPER__OP_LIST, null, msgs);
      msgs = basicSetOpList(newOpList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.IN_OPER__OP_LIST, newOpList, newOpList));
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
      case SqlPackage.IN_OPER__SUBQUERY:
        return basicSetSubquery(null, msgs);
      case SqlPackage.IN_OPER__OP_LIST:
        return basicSetOpList(null, msgs);
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
      case SqlPackage.IN_OPER__OP:
        return getOp();
      case SqlPackage.IN_OPER__SUBQUERY:
        return getSubquery();
      case SqlPackage.IN_OPER__OP_LIST:
        return getOpList();
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
      case SqlPackage.IN_OPER__OP:
        setOp((String)newValue);
        return;
      case SqlPackage.IN_OPER__SUBQUERY:
        setSubquery((SubQueryOperand)newValue);
        return;
      case SqlPackage.IN_OPER__OP_LIST:
        setOpList((OperandList)newValue);
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
      case SqlPackage.IN_OPER__OP:
        setOp(OP_EDEFAULT);
        return;
      case SqlPackage.IN_OPER__SUBQUERY:
        setSubquery((SubQueryOperand)null);
        return;
      case SqlPackage.IN_OPER__OP_LIST:
        setOpList((OperandList)null);
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
      case SqlPackage.IN_OPER__OP:
        return OP_EDEFAULT == null ? op != null : !OP_EDEFAULT.equals(op);
      case SqlPackage.IN_OPER__SUBQUERY:
        return subquery != null;
      case SqlPackage.IN_OPER__OP_LIST:
        return opList != null;
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
    result.append(" (op: ");
    result.append(op);
    result.append(')');
    return result.toString();
  }

} //InOperImpl
