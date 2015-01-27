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

import com.jaspersoft.studio.data.sql.ColumnOperand;
import com.jaspersoft.studio.data.sql.ExpOperand;
import com.jaspersoft.studio.data.sql.OpFunction;
import com.jaspersoft.studio.data.sql.OpFunctionCast;
import com.jaspersoft.studio.data.sql.Operand;
import com.jaspersoft.studio.data.sql.POperand;
import com.jaspersoft.studio.data.sql.SQLCaseOperand;
import com.jaspersoft.studio.data.sql.ScalarOperand;
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
 * An implementation of the model object '<em><b>Operand</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getColumn <em>Column</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getXop <em>Xop</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getSubq <em>Subq</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getFcast <em>Fcast</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getFunc <em>Func</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getSqlcase <em>Sqlcase</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getParam <em>Param</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getEparam <em>Eparam</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.OperandImpl#getScalar <em>Scalar</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperandImpl extends MinimalEObjectImpl.Container implements Operand
{
  /**
   * The cached value of the '{@link #getColumn() <em>Column</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColumn()
   * @generated
   * @ordered
   */
  protected ColumnOperand column;

  /**
   * The cached value of the '{@link #getXop() <em>Xop</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXop()
   * @generated
   * @ordered
   */
  protected Operand xop;

  /**
   * The cached value of the '{@link #getSubq() <em>Subq</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubq()
   * @generated
   * @ordered
   */
  protected SubQueryOperand subq;

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
   * The cached value of the '{@link #getFunc() <em>Func</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFunc()
   * @generated
   * @ordered
   */
  protected OpFunction func;

  /**
   * The cached value of the '{@link #getSqlcase() <em>Sqlcase</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSqlcase()
   * @generated
   * @ordered
   */
  protected SQLCaseOperand sqlcase;

  /**
   * The cached value of the '{@link #getParam() <em>Param</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParam()
   * @generated
   * @ordered
   */
  protected POperand param;

  /**
   * The cached value of the '{@link #getEparam() <em>Eparam</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEparam()
   * @generated
   * @ordered
   */
  protected ExpOperand eparam;

  /**
   * The cached value of the '{@link #getScalar() <em>Scalar</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getScalar()
   * @generated
   * @ordered
   */
  protected ScalarOperand scalar;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OperandImpl()
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
    return SqlPackage.Literals.OPERAND;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ColumnOperand getColumn()
  {
    return column;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetColumn(ColumnOperand newColumn, NotificationChain msgs)
  {
    ColumnOperand oldColumn = column;
    column = newColumn;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__COLUMN, oldColumn, newColumn);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColumn(ColumnOperand newColumn)
  {
    if (newColumn != column)
    {
      NotificationChain msgs = null;
      if (column != null)
        msgs = ((InternalEObject)column).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__COLUMN, null, msgs);
      if (newColumn != null)
        msgs = ((InternalEObject)newColumn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__COLUMN, null, msgs);
      msgs = basicSetColumn(newColumn, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__COLUMN, newColumn, newColumn));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operand getXop()
  {
    return xop;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetXop(Operand newXop, NotificationChain msgs)
  {
    Operand oldXop = xop;
    xop = newXop;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__XOP, oldXop, newXop);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setXop(Operand newXop)
  {
    if (newXop != xop)
    {
      NotificationChain msgs = null;
      if (xop != null)
        msgs = ((InternalEObject)xop).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__XOP, null, msgs);
      if (newXop != null)
        msgs = ((InternalEObject)newXop).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__XOP, null, msgs);
      msgs = basicSetXop(newXop, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__XOP, newXop, newXop));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SubQueryOperand getSubq()
  {
    return subq;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSubq(SubQueryOperand newSubq, NotificationChain msgs)
  {
    SubQueryOperand oldSubq = subq;
    subq = newSubq;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__SUBQ, oldSubq, newSubq);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubq(SubQueryOperand newSubq)
  {
    if (newSubq != subq)
    {
      NotificationChain msgs = null;
      if (subq != null)
        msgs = ((InternalEObject)subq).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__SUBQ, null, msgs);
      if (newSubq != null)
        msgs = ((InternalEObject)newSubq).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__SUBQ, null, msgs);
      msgs = basicSetSubq(newSubq, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__SUBQ, newSubq, newSubq));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__FCAST, oldFcast, newFcast);
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
        msgs = ((InternalEObject)fcast).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__FCAST, null, msgs);
      if (newFcast != null)
        msgs = ((InternalEObject)newFcast).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__FCAST, null, msgs);
      msgs = basicSetFcast(newFcast, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__FCAST, newFcast, newFcast));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OpFunction getFunc()
  {
    return func;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFunc(OpFunction newFunc, NotificationChain msgs)
  {
    OpFunction oldFunc = func;
    func = newFunc;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__FUNC, oldFunc, newFunc);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFunc(OpFunction newFunc)
  {
    if (newFunc != func)
    {
      NotificationChain msgs = null;
      if (func != null)
        msgs = ((InternalEObject)func).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__FUNC, null, msgs);
      if (newFunc != null)
        msgs = ((InternalEObject)newFunc).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__FUNC, null, msgs);
      msgs = basicSetFunc(newFunc, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__FUNC, newFunc, newFunc));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SQLCaseOperand getSqlcase()
  {
    return sqlcase;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSqlcase(SQLCaseOperand newSqlcase, NotificationChain msgs)
  {
    SQLCaseOperand oldSqlcase = sqlcase;
    sqlcase = newSqlcase;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__SQLCASE, oldSqlcase, newSqlcase);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSqlcase(SQLCaseOperand newSqlcase)
  {
    if (newSqlcase != sqlcase)
    {
      NotificationChain msgs = null;
      if (sqlcase != null)
        msgs = ((InternalEObject)sqlcase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__SQLCASE, null, msgs);
      if (newSqlcase != null)
        msgs = ((InternalEObject)newSqlcase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__SQLCASE, null, msgs);
      msgs = basicSetSqlcase(newSqlcase, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__SQLCASE, newSqlcase, newSqlcase));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public POperand getParam()
  {
    return param;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParam(POperand newParam, NotificationChain msgs)
  {
    POperand oldParam = param;
    param = newParam;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__PARAM, oldParam, newParam);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setParam(POperand newParam)
  {
    if (newParam != param)
    {
      NotificationChain msgs = null;
      if (param != null)
        msgs = ((InternalEObject)param).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__PARAM, null, msgs);
      if (newParam != null)
        msgs = ((InternalEObject)newParam).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__PARAM, null, msgs);
      msgs = basicSetParam(newParam, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__PARAM, newParam, newParam));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpOperand getEparam()
  {
    return eparam;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEparam(ExpOperand newEparam, NotificationChain msgs)
  {
    ExpOperand oldEparam = eparam;
    eparam = newEparam;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__EPARAM, oldEparam, newEparam);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEparam(ExpOperand newEparam)
  {
    if (newEparam != eparam)
    {
      NotificationChain msgs = null;
      if (eparam != null)
        msgs = ((InternalEObject)eparam).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__EPARAM, null, msgs);
      if (newEparam != null)
        msgs = ((InternalEObject)newEparam).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__EPARAM, null, msgs);
      msgs = basicSetEparam(newEparam, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__EPARAM, newEparam, newEparam));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ScalarOperand getScalar()
  {
    return scalar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetScalar(ScalarOperand newScalar, NotificationChain msgs)
  {
    ScalarOperand oldScalar = scalar;
    scalar = newScalar;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__SCALAR, oldScalar, newScalar);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setScalar(ScalarOperand newScalar)
  {
    if (newScalar != scalar)
    {
      NotificationChain msgs = null;
      if (scalar != null)
        msgs = ((InternalEObject)scalar).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__SCALAR, null, msgs);
      if (newScalar != null)
        msgs = ((InternalEObject)newScalar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.OPERAND__SCALAR, null, msgs);
      msgs = basicSetScalar(newScalar, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.OPERAND__SCALAR, newScalar, newScalar));
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
      case SqlPackage.OPERAND__COLUMN:
        return basicSetColumn(null, msgs);
      case SqlPackage.OPERAND__XOP:
        return basicSetXop(null, msgs);
      case SqlPackage.OPERAND__SUBQ:
        return basicSetSubq(null, msgs);
      case SqlPackage.OPERAND__FCAST:
        return basicSetFcast(null, msgs);
      case SqlPackage.OPERAND__FUNC:
        return basicSetFunc(null, msgs);
      case SqlPackage.OPERAND__SQLCASE:
        return basicSetSqlcase(null, msgs);
      case SqlPackage.OPERAND__PARAM:
        return basicSetParam(null, msgs);
      case SqlPackage.OPERAND__EPARAM:
        return basicSetEparam(null, msgs);
      case SqlPackage.OPERAND__SCALAR:
        return basicSetScalar(null, msgs);
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
      case SqlPackage.OPERAND__COLUMN:
        return getColumn();
      case SqlPackage.OPERAND__XOP:
        return getXop();
      case SqlPackage.OPERAND__SUBQ:
        return getSubq();
      case SqlPackage.OPERAND__FCAST:
        return getFcast();
      case SqlPackage.OPERAND__FUNC:
        return getFunc();
      case SqlPackage.OPERAND__SQLCASE:
        return getSqlcase();
      case SqlPackage.OPERAND__PARAM:
        return getParam();
      case SqlPackage.OPERAND__EPARAM:
        return getEparam();
      case SqlPackage.OPERAND__SCALAR:
        return getScalar();
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
      case SqlPackage.OPERAND__COLUMN:
        setColumn((ColumnOperand)newValue);
        return;
      case SqlPackage.OPERAND__XOP:
        setXop((Operand)newValue);
        return;
      case SqlPackage.OPERAND__SUBQ:
        setSubq((SubQueryOperand)newValue);
        return;
      case SqlPackage.OPERAND__FCAST:
        setFcast((OpFunctionCast)newValue);
        return;
      case SqlPackage.OPERAND__FUNC:
        setFunc((OpFunction)newValue);
        return;
      case SqlPackage.OPERAND__SQLCASE:
        setSqlcase((SQLCaseOperand)newValue);
        return;
      case SqlPackage.OPERAND__PARAM:
        setParam((POperand)newValue);
        return;
      case SqlPackage.OPERAND__EPARAM:
        setEparam((ExpOperand)newValue);
        return;
      case SqlPackage.OPERAND__SCALAR:
        setScalar((ScalarOperand)newValue);
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
      case SqlPackage.OPERAND__COLUMN:
        setColumn((ColumnOperand)null);
        return;
      case SqlPackage.OPERAND__XOP:
        setXop((Operand)null);
        return;
      case SqlPackage.OPERAND__SUBQ:
        setSubq((SubQueryOperand)null);
        return;
      case SqlPackage.OPERAND__FCAST:
        setFcast((OpFunctionCast)null);
        return;
      case SqlPackage.OPERAND__FUNC:
        setFunc((OpFunction)null);
        return;
      case SqlPackage.OPERAND__SQLCASE:
        setSqlcase((SQLCaseOperand)null);
        return;
      case SqlPackage.OPERAND__PARAM:
        setParam((POperand)null);
        return;
      case SqlPackage.OPERAND__EPARAM:
        setEparam((ExpOperand)null);
        return;
      case SqlPackage.OPERAND__SCALAR:
        setScalar((ScalarOperand)null);
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
      case SqlPackage.OPERAND__COLUMN:
        return column != null;
      case SqlPackage.OPERAND__XOP:
        return xop != null;
      case SqlPackage.OPERAND__SUBQ:
        return subq != null;
      case SqlPackage.OPERAND__FCAST:
        return fcast != null;
      case SqlPackage.OPERAND__FUNC:
        return func != null;
      case SqlPackage.OPERAND__SQLCASE:
        return sqlcase != null;
      case SqlPackage.OPERAND__PARAM:
        return param != null;
      case SqlPackage.OPERAND__EPARAM:
        return eparam != null;
      case SqlPackage.OPERAND__SCALAR:
        return scalar != null;
    }
    return super.eIsSet(featureID);
  }

} //OperandImpl
