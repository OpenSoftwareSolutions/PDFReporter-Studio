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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>JR Parameter Obj</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRParameterObjImpl#getBracedIdentifier <em>Braced Identifier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JRParameterObjImpl extends JasperReportsExpressionImpl implements JRParameterObj
{
  /**
   * The default value of the '{@link #getBracedIdentifier() <em>Braced Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBracedIdentifier()
   * @generated
   * @ordered
   */
  protected static final String BRACED_IDENTIFIER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getBracedIdentifier() <em>Braced Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBracedIdentifier()
   * @generated
   * @ordered
   */
  protected String bracedIdentifier = BRACED_IDENTIFIER_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JRParameterObjImpl()
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
    return JavaJRExpressionPackage.Literals.JR_PARAMETER_OBJ;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getBracedIdentifier()
  {
    return bracedIdentifier;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBracedIdentifier(String newBracedIdentifier)
  {
    String oldBracedIdentifier = bracedIdentifier;
    bracedIdentifier = newBracedIdentifier;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.JR_PARAMETER_OBJ__BRACED_IDENTIFIER, oldBracedIdentifier, bracedIdentifier));
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
      case JavaJRExpressionPackage.JR_PARAMETER_OBJ__BRACED_IDENTIFIER:
        return getBracedIdentifier();
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
      case JavaJRExpressionPackage.JR_PARAMETER_OBJ__BRACED_IDENTIFIER:
        setBracedIdentifier((String)newValue);
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
      case JavaJRExpressionPackage.JR_PARAMETER_OBJ__BRACED_IDENTIFIER:
        setBracedIdentifier(BRACED_IDENTIFIER_EDEFAULT);
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
      case JavaJRExpressionPackage.JR_PARAMETER_OBJ__BRACED_IDENTIFIER:
        return BRACED_IDENTIFIER_EDEFAULT == null ? bracedIdentifier != null : !BRACED_IDENTIFIER_EDEFAULT.equals(bracedIdentifier);
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
    result.append(" (bracedIdentifier: ");
    result.append(bracedIdentifier);
    result.append(')');
    return result.toString();
  }

} //JRParameterObjImpl
