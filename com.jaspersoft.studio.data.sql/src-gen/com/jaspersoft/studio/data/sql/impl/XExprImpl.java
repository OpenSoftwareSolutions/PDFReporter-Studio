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
import com.jaspersoft.studio.data.sql.Prms;
import com.jaspersoft.studio.data.sql.SqlPackage;
import com.jaspersoft.studio.data.sql.XExpr;
import com.jaspersoft.studio.data.sql.XFunction;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>XExpr</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.XExprImpl#getXf <em>Xf</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.XExprImpl#getCol <em>Col</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.XExprImpl#getPrm <em>Prm</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XExprImpl extends MinimalEObjectImpl.Container implements XExpr
{
  /**
   * The default value of the '{@link #getXf() <em>Xf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXf()
   * @generated
   * @ordered
   */
  protected static final XFunction XF_EDEFAULT = XFunction.XIN;

  /**
   * The cached value of the '{@link #getXf() <em>Xf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXf()
   * @generated
   * @ordered
   */
  protected XFunction xf = XF_EDEFAULT;

  /**
   * The cached value of the '{@link #getCol() <em>Col</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCol()
   * @generated
   * @ordered
   */
  protected Operands col;

  /**
   * The cached value of the '{@link #getPrm() <em>Prm</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrm()
   * @generated
   * @ordered
   */
  protected Prms prm;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected XExprImpl()
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
    return SqlPackage.Literals.XEXPR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XFunction getXf()
  {
    return xf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setXf(XFunction newXf)
  {
    XFunction oldXf = xf;
    xf = newXf == null ? XF_EDEFAULT : newXf;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.XEXPR__XF, oldXf, xf));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operands getCol()
  {
    return col;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCol(Operands newCol, NotificationChain msgs)
  {
    Operands oldCol = col;
    col = newCol;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.XEXPR__COL, oldCol, newCol);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCol(Operands newCol)
  {
    if (newCol != col)
    {
      NotificationChain msgs = null;
      if (col != null)
        msgs = ((InternalEObject)col).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.XEXPR__COL, null, msgs);
      if (newCol != null)
        msgs = ((InternalEObject)newCol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.XEXPR__COL, null, msgs);
      msgs = basicSetCol(newCol, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.XEXPR__COL, newCol, newCol));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Prms getPrm()
  {
    return prm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPrm(Prms newPrm, NotificationChain msgs)
  {
    Prms oldPrm = prm;
    prm = newPrm;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.XEXPR__PRM, oldPrm, newPrm);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPrm(Prms newPrm)
  {
    if (newPrm != prm)
    {
      NotificationChain msgs = null;
      if (prm != null)
        msgs = ((InternalEObject)prm).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.XEXPR__PRM, null, msgs);
      if (newPrm != null)
        msgs = ((InternalEObject)newPrm).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.XEXPR__PRM, null, msgs);
      msgs = basicSetPrm(newPrm, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.XEXPR__PRM, newPrm, newPrm));
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
      case SqlPackage.XEXPR__COL:
        return basicSetCol(null, msgs);
      case SqlPackage.XEXPR__PRM:
        return basicSetPrm(null, msgs);
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
      case SqlPackage.XEXPR__XF:
        return getXf();
      case SqlPackage.XEXPR__COL:
        return getCol();
      case SqlPackage.XEXPR__PRM:
        return getPrm();
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
      case SqlPackage.XEXPR__XF:
        setXf((XFunction)newValue);
        return;
      case SqlPackage.XEXPR__COL:
        setCol((Operands)newValue);
        return;
      case SqlPackage.XEXPR__PRM:
        setPrm((Prms)newValue);
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
      case SqlPackage.XEXPR__XF:
        setXf(XF_EDEFAULT);
        return;
      case SqlPackage.XEXPR__COL:
        setCol((Operands)null);
        return;
      case SqlPackage.XEXPR__PRM:
        setPrm((Prms)null);
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
      case SqlPackage.XEXPR__XF:
        return xf != XF_EDEFAULT;
      case SqlPackage.XEXPR__COL:
        return col != null;
      case SqlPackage.XEXPR__PRM:
        return prm != null;
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
    result.append(" (xf: ");
    result.append(xf);
    result.append(')');
    return result.toString();
  }

} //XExprImpl
