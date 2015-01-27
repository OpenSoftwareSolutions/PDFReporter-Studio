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
import org.eclipse.emf.ecore.util.EDataTypeEList;

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Static Field</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StaticFieldImpl#getPrefixQMN <em>Prefix QMN</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StaticFieldImpl#getDots <em>Dots</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StaticFieldImpl#getFieldName <em>Field Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StaticFieldImpl extends JasperReportsExpressionImpl implements StaticField
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
   * The default value of the '{@link #getFieldName() <em>Field Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFieldName()
   * @generated
   * @ordered
   */
  protected static final String FIELD_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFieldName() <em>Field Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFieldName()
   * @generated
   * @ordered
   */
  protected String fieldName = FIELD_NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected StaticFieldImpl()
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
    return JavaJRExpressionPackage.Literals.STATIC_FIELD;
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
      prefixQMN = new EDataTypeEList<String>(String.class, this, JavaJRExpressionPackage.STATIC_FIELD__PREFIX_QMN);
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
      dots = new EDataTypeEList<String>(String.class, this, JavaJRExpressionPackage.STATIC_FIELD__DOTS);
    }
    return dots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFieldName()
  {
    return fieldName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFieldName(String newFieldName)
  {
    String oldFieldName = fieldName;
    fieldName = newFieldName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.STATIC_FIELD__FIELD_NAME, oldFieldName, fieldName));
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
      case JavaJRExpressionPackage.STATIC_FIELD__PREFIX_QMN:
        return getPrefixQMN();
      case JavaJRExpressionPackage.STATIC_FIELD__DOTS:
        return getDots();
      case JavaJRExpressionPackage.STATIC_FIELD__FIELD_NAME:
        return getFieldName();
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
      case JavaJRExpressionPackage.STATIC_FIELD__PREFIX_QMN:
        getPrefixQMN().clear();
        getPrefixQMN().addAll((Collection<? extends String>)newValue);
        return;
      case JavaJRExpressionPackage.STATIC_FIELD__DOTS:
        getDots().clear();
        getDots().addAll((Collection<? extends String>)newValue);
        return;
      case JavaJRExpressionPackage.STATIC_FIELD__FIELD_NAME:
        setFieldName((String)newValue);
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
      case JavaJRExpressionPackage.STATIC_FIELD__PREFIX_QMN:
        getPrefixQMN().clear();
        return;
      case JavaJRExpressionPackage.STATIC_FIELD__DOTS:
        getDots().clear();
        return;
      case JavaJRExpressionPackage.STATIC_FIELD__FIELD_NAME:
        setFieldName(FIELD_NAME_EDEFAULT);
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
      case JavaJRExpressionPackage.STATIC_FIELD__PREFIX_QMN:
        return prefixQMN != null && !prefixQMN.isEmpty();
      case JavaJRExpressionPackage.STATIC_FIELD__DOTS:
        return dots != null && !dots.isEmpty();
      case JavaJRExpressionPackage.STATIC_FIELD__FIELD_NAME:
        return FIELD_NAME_EDEFAULT == null ? fieldName != null : !FIELD_NAME_EDEFAULT.equals(fieldName);
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
    result.append(", fieldName: ");
    result.append(fieldName);
    result.append(')');
    return result.toString();
  }

} //StaticFieldImpl
