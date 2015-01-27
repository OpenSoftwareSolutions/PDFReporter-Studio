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
package com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Full Method Name</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FullMethodNameImpl#getPrefixQMN <em>Prefix QMN</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FullMethodNameImpl#getDots <em>Dots</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FullMethodNameImpl#getMethodName <em>Method Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FullMethodNameImpl extends MinimalEObjectImpl.Container implements FullMethodName
{
  /**
   * The cached value of the '{@link #getPrefixQMN() <em>Prefix QMN</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrefixQMN()
   * @generated
   * @ordered
   */
  protected EList<String> prefixQMN;

  /**
   * The cached value of the '{@link #getDots() <em>Dots</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDots()
   * @generated
   * @ordered
   */
  protected EList<String> dots;

  /**
   * The default value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMethodName()
   * @generated
   * @ordered
   */
  protected static final String METHOD_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMethodName() <em>Method Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMethodName()
   * @generated
   * @ordered
   */
  protected String methodName = METHOD_NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FullMethodNameImpl()
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
    return JavaJRExpressionPackage.Literals.FULL_METHOD_NAME;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getPrefixQMN()
  {
    if (prefixQMN == null)
    {
      prefixQMN = new EDataTypeEList<String>(String.class, this, JavaJRExpressionPackage.FULL_METHOD_NAME__PREFIX_QMN);
    }
    return prefixQMN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getDots()
  {
    if (dots == null)
    {
      dots = new EDataTypeEList<String>(String.class, this, JavaJRExpressionPackage.FULL_METHOD_NAME__DOTS);
    }
    return dots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getMethodName()
  {
    return methodName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMethodName(String newMethodName)
  {
    String oldMethodName = methodName;
    methodName = newMethodName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.FULL_METHOD_NAME__METHOD_NAME, oldMethodName, methodName));
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
      case JavaJRExpressionPackage.FULL_METHOD_NAME__PREFIX_QMN:
        return getPrefixQMN();
      case JavaJRExpressionPackage.FULL_METHOD_NAME__DOTS:
        return getDots();
      case JavaJRExpressionPackage.FULL_METHOD_NAME__METHOD_NAME:
        return getMethodName();
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
      case JavaJRExpressionPackage.FULL_METHOD_NAME__PREFIX_QMN:
        getPrefixQMN().clear();
        getPrefixQMN().addAll((Collection<? extends String>)newValue);
        return;
      case JavaJRExpressionPackage.FULL_METHOD_NAME__DOTS:
        getDots().clear();
        getDots().addAll((Collection<? extends String>)newValue);
        return;
      case JavaJRExpressionPackage.FULL_METHOD_NAME__METHOD_NAME:
        setMethodName((String)newValue);
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
      case JavaJRExpressionPackage.FULL_METHOD_NAME__PREFIX_QMN:
        getPrefixQMN().clear();
        return;
      case JavaJRExpressionPackage.FULL_METHOD_NAME__DOTS:
        getDots().clear();
        return;
      case JavaJRExpressionPackage.FULL_METHOD_NAME__METHOD_NAME:
        setMethodName(METHOD_NAME_EDEFAULT);
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
      case JavaJRExpressionPackage.FULL_METHOD_NAME__PREFIX_QMN:
        return prefixQMN != null && !prefixQMN.isEmpty();
      case JavaJRExpressionPackage.FULL_METHOD_NAME__DOTS:
        return dots != null && !dots.isEmpty();
      case JavaJRExpressionPackage.FULL_METHOD_NAME__METHOD_NAME:
        return METHOD_NAME_EDEFAULT == null ? methodName != null : !METHOD_NAME_EDEFAULT.equals(methodName);
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
    result.append(" (prefixQMN: ");
    result.append(prefixQMN);
    result.append(", dots: ");
    result.append(dots);
    result.append(", methodName: ");
    result.append(methodName);
    result.append(')');
    return result.toString();
  }

} //FullMethodNameImpl
