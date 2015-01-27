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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage
 * @generated
 */
public interface JavaJRExpressionFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  JavaJRExpressionFactory eINSTANCE = com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionFactoryImpl.init();

  /**
   * Returns a new object of class '<em>JR Expression Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>JR Expression Model</em>'.
   * @generated
   */
  JRExpressionModel createJRExpressionModel();

  /**
   * Returns a new object of class '<em>Jasper Reports Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Jasper Reports Expression</em>'.
   * @generated
   */
  JasperReportsExpression createJasperReportsExpression();

  /**
   * Returns a new object of class '<em>Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Type</em>'.
   * @generated
   */
  Type createType();

  /**
   * Returns a new object of class '<em>Array Initializer</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Initializer</em>'.
   * @generated
   */
  ArrayInitializer createArrayInitializer();

  /**
   * Returns a new object of class '<em>Method Invocation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Method Invocation</em>'.
   * @generated
   */
  MethodInvocation createMethodInvocation();

  /**
   * Returns a new object of class '<em>Full Method Name</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Full Method Name</em>'.
   * @generated
   */
  FullMethodName createFullMethodName();

  /**
   * Returns a new object of class '<em>Arguments</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Arguments</em>'.
   * @generated
   */
  Arguments createArguments();

  /**
   * Returns a new object of class '<em>Expression List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expression List</em>'.
   * @generated
   */
  ExpressionList createExpressionList();

  /**
   * Returns a new object of class '<em>Jvm Parameterized Type Reference</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Jvm Parameterized Type Reference</em>'.
   * @generated
   */
  JvmParameterizedTypeReference createJvmParameterizedTypeReference();

  /**
   * Returns a new object of class '<em>Jvm Wildcard Type Reference</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Jvm Wildcard Type Reference</em>'.
   * @generated
   */
  JvmWildcardTypeReference createJvmWildcardTypeReference();

  /**
   * Returns a new object of class '<em>Jvm Upper Bound</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Jvm Upper Bound</em>'.
   * @generated
   */
  JvmUpperBound createJvmUpperBound();

  /**
   * Returns a new object of class '<em>Jvm Lower Bound</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Jvm Lower Bound</em>'.
   * @generated
   */
  JvmLowerBound createJvmLowerBound();

  /**
   * Returns a new object of class '<em>Test Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Test Expression</em>'.
   * @generated
   */
  TestExpression createTestExpression();

  /**
   * Returns a new object of class '<em>Binary Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Binary Expression</em>'.
   * @generated
   */
  BinaryExpression createBinaryExpression();

  /**
   * Returns a new object of class '<em>Static Field</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Static Field</em>'.
   * @generated
   */
  StaticField createStaticField();

  /**
   * Returns a new object of class '<em>JR Field Obj</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>JR Field Obj</em>'.
   * @generated
   */
  JRFieldObj createJRFieldObj();

  /**
   * Returns a new object of class '<em>JR Parameter Obj</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>JR Parameter Obj</em>'.
   * @generated
   */
  JRParameterObj createJRParameterObj();

  /**
   * Returns a new object of class '<em>JR Variable Obj</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>JR Variable Obj</em>'.
   * @generated
   */
  JRVariableObj createJRVariableObj();

  /**
   * Returns a new object of class '<em>JR Resource Bundle Key Obj</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>JR Resource Bundle Key Obj</em>'.
   * @generated
   */
  JRResourceBundleKeyObj createJRResourceBundleKeyObj();

  /**
   * Returns a new object of class '<em>Methods Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Methods Expression</em>'.
   * @generated
   */
  MethodsExpression createMethodsExpression();

  /**
   * Returns a new object of class '<em>Int Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Int Literal</em>'.
   * @generated
   */
  IntLiteral createIntLiteral();

  /**
   * Returns a new object of class '<em>Long Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Long Literal</em>'.
   * @generated
   */
  LongLiteral createLongLiteral();

  /**
   * Returns a new object of class '<em>Float Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Float Literal</em>'.
   * @generated
   */
  FloatLiteral createFloatLiteral();

  /**
   * Returns a new object of class '<em>Double Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Double Literal</em>'.
   * @generated
   */
  DoubleLiteral createDoubleLiteral();

  /**
   * Returns a new object of class '<em>Char Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Char Literal</em>'.
   * @generated
   */
  CharLiteral createCharLiteral();

  /**
   * Returns a new object of class '<em>String Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String Literal</em>'.
   * @generated
   */
  StringLiteral createStringLiteral();

  /**
   * Returns a new object of class '<em>Boolean Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Boolean Literal</em>'.
   * @generated
   */
  BooleanLiteral createBooleanLiteral();

  /**
   * Returns a new object of class '<em>Null Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Null Literal</em>'.
   * @generated
   */
  NullLiteral createNullLiteral();

  /**
   * Returns a new object of class '<em>Casted Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Casted Expression</em>'.
   * @generated
   */
  CastedExpression createCastedExpression();

  /**
   * Returns a new object of class '<em>Array Creator</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Array Creator</em>'.
   * @generated
   */
  ArrayCreator createArrayCreator();

  /**
   * Returns a new object of class '<em>Jvm Generic Array Type Reference</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Jvm Generic Array Type Reference</em>'.
   * @generated
   */
  JvmGenericArrayTypeReference createJvmGenericArrayTypeReference();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  JavaJRExpressionPackage getJavaJRExpressionPackage();

} //JavaJRExpressionFactory
