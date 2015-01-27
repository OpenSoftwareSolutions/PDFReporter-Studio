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

import com.jaspersoft.studio.data.sql.FromTableJoin;
import com.jaspersoft.studio.data.sql.OrExpr;
import com.jaspersoft.studio.data.sql.SqlPackage;
import com.jaspersoft.studio.data.sql.TableOrAlias;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>From Table Join</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FromTableJoinImpl#getJoin <em>Join</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FromTableJoinImpl#getOnTable <em>On Table</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FromTableJoinImpl#getJoinExpr <em>Join Expr</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FromTableJoinImpl extends MinimalEObjectImpl.Container implements FromTableJoin
{
  /**
   * The default value of the '{@link #getJoin() <em>Join</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getJoin()
   * @generated
   * @ordered
   */
  protected static final String JOIN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getJoin() <em>Join</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getJoin()
   * @generated
   * @ordered
   */
  protected String join = JOIN_EDEFAULT;

  /**
   * The cached value of the '{@link #getOnTable() <em>On Table</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOnTable()
   * @generated
   * @ordered
   */
  protected TableOrAlias onTable;

  /**
   * The cached value of the '{@link #getJoinExpr() <em>Join Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getJoinExpr()
   * @generated
   * @ordered
   */
  protected OrExpr joinExpr;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FromTableJoinImpl()
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
    return SqlPackage.Literals.FROM_TABLE_JOIN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getJoin()
  {
    return join;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setJoin(String newJoin)
  {
    String oldJoin = join;
    join = newJoin;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FROM_TABLE_JOIN__JOIN, oldJoin, join));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TableOrAlias getOnTable()
  {
    return onTable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOnTable(TableOrAlias newOnTable, NotificationChain msgs)
  {
    TableOrAlias oldOnTable = onTable;
    onTable = newOnTable;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FROM_TABLE_JOIN__ON_TABLE, oldOnTable, newOnTable);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOnTable(TableOrAlias newOnTable)
  {
    if (newOnTable != onTable)
    {
      NotificationChain msgs = null;
      if (onTable != null)
        msgs = ((InternalEObject)onTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FROM_TABLE_JOIN__ON_TABLE, null, msgs);
      if (newOnTable != null)
        msgs = ((InternalEObject)newOnTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FROM_TABLE_JOIN__ON_TABLE, null, msgs);
      msgs = basicSetOnTable(newOnTable, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FROM_TABLE_JOIN__ON_TABLE, newOnTable, newOnTable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OrExpr getJoinExpr()
  {
    return joinExpr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetJoinExpr(OrExpr newJoinExpr, NotificationChain msgs)
  {
    OrExpr oldJoinExpr = joinExpr;
    joinExpr = newJoinExpr;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR, oldJoinExpr, newJoinExpr);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setJoinExpr(OrExpr newJoinExpr)
  {
    if (newJoinExpr != joinExpr)
    {
      NotificationChain msgs = null;
      if (joinExpr != null)
        msgs = ((InternalEObject)joinExpr).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR, null, msgs);
      if (newJoinExpr != null)
        msgs = ((InternalEObject)newJoinExpr).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR, null, msgs);
      msgs = basicSetJoinExpr(newJoinExpr, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR, newJoinExpr, newJoinExpr));
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
      case SqlPackage.FROM_TABLE_JOIN__ON_TABLE:
        return basicSetOnTable(null, msgs);
      case SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR:
        return basicSetJoinExpr(null, msgs);
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
      case SqlPackage.FROM_TABLE_JOIN__JOIN:
        return getJoin();
      case SqlPackage.FROM_TABLE_JOIN__ON_TABLE:
        return getOnTable();
      case SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR:
        return getJoinExpr();
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
      case SqlPackage.FROM_TABLE_JOIN__JOIN:
        setJoin((String)newValue);
        return;
      case SqlPackage.FROM_TABLE_JOIN__ON_TABLE:
        setOnTable((TableOrAlias)newValue);
        return;
      case SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR:
        setJoinExpr((OrExpr)newValue);
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
      case SqlPackage.FROM_TABLE_JOIN__JOIN:
        setJoin(JOIN_EDEFAULT);
        return;
      case SqlPackage.FROM_TABLE_JOIN__ON_TABLE:
        setOnTable((TableOrAlias)null);
        return;
      case SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR:
        setJoinExpr((OrExpr)null);
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
      case SqlPackage.FROM_TABLE_JOIN__JOIN:
        return JOIN_EDEFAULT == null ? join != null : !JOIN_EDEFAULT.equals(join);
      case SqlPackage.FROM_TABLE_JOIN__ON_TABLE:
        return onTable != null;
      case SqlPackage.FROM_TABLE_JOIN__JOIN_EXPR:
        return joinExpr != null;
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
    result.append(" (join: ");
    result.append(join);
    result.append(')');
    return result.toString();
  }

} //FromTableJoinImpl
