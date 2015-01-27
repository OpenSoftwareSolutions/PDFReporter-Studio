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

import com.jaspersoft.studio.data.sql.ColumnFull;
import com.jaspersoft.studio.data.sql.OrderByColumnFull;
import com.jaspersoft.studio.data.sql.SqlPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Order By Column Full</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OrderByColumnFullImpl#getColOrder <em>Col Order</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OrderByColumnFullImpl#getColOrderInt <em>Col Order Int</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OrderByColumnFullImpl#getDirection <em>Direction</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OrderByColumnFullImpl extends OrOrderByColumnImpl implements OrderByColumnFull
{
  /**
   * The cached value of the '{@link #getColOrder() <em>Col Order</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColOrder()
   * @generated
   * @ordered
   */
  protected ColumnFull colOrder;

  /**
   * The default value of the '{@link #getColOrderInt() <em>Col Order Int</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColOrderInt()
   * @generated
   * @ordered
   */
  protected static final int COL_ORDER_INT_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getColOrderInt() <em>Col Order Int</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColOrderInt()
   * @generated
   * @ordered
   */
  protected int colOrderInt = COL_ORDER_INT_EDEFAULT;

  /**
   * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirection()
   * @generated
   * @ordered
   */
  protected static final String DIRECTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirection()
   * @generated
   * @ordered
   */
  protected String direction = DIRECTION_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OrderByColumnFullImpl()
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
    return SqlPackage.Literals.ORDER_BY_COLUMN_FULL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColumnFull getColOrder()
  {
    return colOrder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetColOrder(ColumnFull newColOrder, NotificationChain msgs)
  {
    ColumnFull oldColOrder = colOrder;
    colOrder = newColOrder;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER, oldColOrder, newColOrder);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColOrder(ColumnFull newColOrder)
  {
    if (newColOrder != colOrder)
    {
      NotificationChain msgs = null;
      if (colOrder != null)
        msgs = ((InternalEObject)colOrder).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER, null, msgs);
      if (newColOrder != null)
        msgs = ((InternalEObject)newColOrder).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER, null, msgs);
      msgs = basicSetColOrder(newColOrder, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER, newColOrder, newColOrder));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getColOrderInt()
  {
    return colOrderInt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColOrderInt(int newColOrderInt)
  {
    int oldColOrderInt = colOrderInt;
    colOrderInt = newColOrderInt;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER_INT, oldColOrderInt, colOrderInt));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDirection()
  {
    return direction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDirection(String newDirection)
  {
    String oldDirection = direction;
    direction = newDirection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.ORDER_BY_COLUMN_FULL__DIRECTION, oldDirection, direction));
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
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER:
        return basicSetColOrder(null, msgs);
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
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER:
        return getColOrder();
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER_INT:
        return getColOrderInt();
      case SqlPackage.ORDER_BY_COLUMN_FULL__DIRECTION:
        return getDirection();
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
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER:
        setColOrder((ColumnFull)newValue);
        return;
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER_INT:
        setColOrderInt((Integer)newValue);
        return;
      case SqlPackage.ORDER_BY_COLUMN_FULL__DIRECTION:
        setDirection((String)newValue);
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
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER:
        setColOrder((ColumnFull)null);
        return;
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER_INT:
        setColOrderInt(COL_ORDER_INT_EDEFAULT);
        return;
      case SqlPackage.ORDER_BY_COLUMN_FULL__DIRECTION:
        setDirection(DIRECTION_EDEFAULT);
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
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER:
        return colOrder != null;
      case SqlPackage.ORDER_BY_COLUMN_FULL__COL_ORDER_INT:
        return colOrderInt != COL_ORDER_INT_EDEFAULT;
      case SqlPackage.ORDER_BY_COLUMN_FULL__DIRECTION:
        return DIRECTION_EDEFAULT == null ? direction != null : !DIRECTION_EDEFAULT.equals(direction);
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
    result.append(" (colOrderInt: ");
    result.append(colOrderInt);
    result.append(", direction: ");
    result.append(direction);
    result.append(')');
    return result.toString();
  }

} //OrderByColumnFullImpl
