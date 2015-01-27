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
import com.jaspersoft.studio.data.sql.GroupByColumnFull;
import com.jaspersoft.studio.data.sql.SqlPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group By Column Full</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.GroupByColumnFullImpl#getColGrBy <em>Col Gr By</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GroupByColumnFullImpl extends OrGroupByColumnImpl implements GroupByColumnFull
{
  /**
   * The cached value of the '{@link #getColGrBy() <em>Col Gr By</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColGrBy()
   * @generated
   * @ordered
   */
  protected ColumnFull colGrBy;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GroupByColumnFullImpl()
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
    return SqlPackage.Literals.GROUP_BY_COLUMN_FULL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColumnFull getColGrBy()
  {
    return colGrBy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetColGrBy(ColumnFull newColGrBy, NotificationChain msgs)
  {
    ColumnFull oldColGrBy = colGrBy;
    colGrBy = newColGrBy;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY, oldColGrBy, newColGrBy);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColGrBy(ColumnFull newColGrBy)
  {
    if (newColGrBy != colGrBy)
    {
      NotificationChain msgs = null;
      if (colGrBy != null)
        msgs = ((InternalEObject)colGrBy).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY, null, msgs);
      if (newColGrBy != null)
        msgs = ((InternalEObject)newColGrBy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY, null, msgs);
      msgs = basicSetColGrBy(newColGrBy, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY, newColGrBy, newColGrBy));
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
      case SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY:
        return basicSetColGrBy(null, msgs);
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
      case SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY:
        return getColGrBy();
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
      case SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY:
        setColGrBy((ColumnFull)newValue);
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
      case SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY:
        setColGrBy((ColumnFull)null);
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
      case SqlPackage.GROUP_BY_COLUMN_FULL__COL_GR_BY:
        return colGrBy != null;
    }
    return super.eIsSet(featureID);
  }

} //GroupByColumnFullImpl
