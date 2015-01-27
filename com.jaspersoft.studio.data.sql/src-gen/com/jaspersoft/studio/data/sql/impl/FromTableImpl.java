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

import com.jaspersoft.studio.data.sql.FromTable;
import com.jaspersoft.studio.data.sql.FromTableJoin;
import com.jaspersoft.studio.data.sql.SqlPackage;
import com.jaspersoft.studio.data.sql.TableOrAlias;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>From Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FromTableImpl#getTable <em>Table</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FromTableImpl#getFjoin <em>Fjoin</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FromTableImpl extends OrTableImpl implements FromTable
{
  /**
   * The cached value of the '{@link #getTable() <em>Table</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTable()
   * @generated
   * @ordered
   */
  protected TableOrAlias table;

  /**
   * The cached value of the '{@link #getFjoin() <em>Fjoin</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFjoin()
   * @generated
   * @ordered
   */
  protected EList<FromTableJoin> fjoin;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FromTableImpl()
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
    return SqlPackage.Literals.FROM_TABLE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TableOrAlias getTable()
  {
    return table;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTable(TableOrAlias newTable, NotificationChain msgs)
  {
    TableOrAlias oldTable = table;
    table = newTable;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FROM_TABLE__TABLE, oldTable, newTable);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTable(TableOrAlias newTable)
  {
    if (newTable != table)
    {
      NotificationChain msgs = null;
      if (table != null)
        msgs = ((InternalEObject)table).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FROM_TABLE__TABLE, null, msgs);
      if (newTable != null)
        msgs = ((InternalEObject)newTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FROM_TABLE__TABLE, null, msgs);
      msgs = basicSetTable(newTable, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FROM_TABLE__TABLE, newTable, newTable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<FromTableJoin> getFjoin()
  {
    if (fjoin == null)
    {
      fjoin = new EObjectContainmentEList<FromTableJoin>(FromTableJoin.class, this, SqlPackage.FROM_TABLE__FJOIN);
    }
    return fjoin;
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
      case SqlPackage.FROM_TABLE__TABLE:
        return basicSetTable(null, msgs);
      case SqlPackage.FROM_TABLE__FJOIN:
        return ((InternalEList<?>)getFjoin()).basicRemove(otherEnd, msgs);
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
      case SqlPackage.FROM_TABLE__TABLE:
        return getTable();
      case SqlPackage.FROM_TABLE__FJOIN:
        return getFjoin();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SqlPackage.FROM_TABLE__TABLE:
        setTable((TableOrAlias)newValue);
        return;
      case SqlPackage.FROM_TABLE__FJOIN:
        getFjoin().clear();
        getFjoin().addAll((Collection<? extends FromTableJoin>)newValue);
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
      case SqlPackage.FROM_TABLE__TABLE:
        setTable((TableOrAlias)null);
        return;
      case SqlPackage.FROM_TABLE__FJOIN:
        getFjoin().clear();
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
      case SqlPackage.FROM_TABLE__TABLE:
        return table != null;
      case SqlPackage.FROM_TABLE__FJOIN:
        return fjoin != null && !fjoin.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //FromTableImpl
