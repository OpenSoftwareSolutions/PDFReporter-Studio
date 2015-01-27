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
package com.jaspersoft.studio.editor.jrexpressions.javaJRExpression;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Methods Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#isIncludeObjectInstatiation <em>Include Object Instatiation</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getMethodInvocations <em>Method Invocations</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getObjectExpression <em>Object Expression</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getArrayIndexes <em>Array Indexes</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage#getMethodsExpression()
 * @model
 * @generated
 */
public interface MethodsExpression extends JasperReportsExpression
{
  /**
   * Returns the value of the '<em><b>Include Object Instatiation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Include Object Instatiation</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Include Object Instatiation</em>' attribute.
   * @see #setIncludeObjectInstatiation(boolean)
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage#getMethodsExpression_IncludeObjectInstatiation()
   * @model
   * @generated
   */
  boolean isIncludeObjectInstatiation();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#isIncludeObjectInstatiation <em>Include Object Instatiation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Include Object Instatiation</em>' attribute.
   * @see #isIncludeObjectInstatiation()
   * @generated
   */
  void setIncludeObjectInstatiation(boolean value);

  /**
   * Returns the value of the '<em><b>Method Invocations</b></em>' containment reference list.
   * The list contents are of type {@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Method Invocations</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Method Invocations</em>' containment reference list.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage#getMethodsExpression_MethodInvocations()
   * @model containment="true"
   * @generated
   */
  EList<MethodInvocation> getMethodInvocations();

  /**
   * Returns the value of the '<em><b>Object Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Object Expression</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Object Expression</em>' containment reference.
   * @see #setObjectExpression(JasperReportsExpression)
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage#getMethodsExpression_ObjectExpression()
   * @model containment="true"
   * @generated
   */
  JasperReportsExpression getObjectExpression();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getObjectExpression <em>Object Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Object Expression</em>' containment reference.
   * @see #getObjectExpression()
   * @generated
   */
  void setObjectExpression(JasperReportsExpression value);

  /**
   * Returns the value of the '<em><b>Array Indexes</b></em>' containment reference list.
   * The list contents are of type {@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Array Indexes</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Array Indexes</em>' containment reference list.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage#getMethodsExpression_ArrayIndexes()
   * @model containment="true"
   * @generated
   */
  EList<JasperReportsExpression> getArrayIndexes();

} // MethodsExpression
