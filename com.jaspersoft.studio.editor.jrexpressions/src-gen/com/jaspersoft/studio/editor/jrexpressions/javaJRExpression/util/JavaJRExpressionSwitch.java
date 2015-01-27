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
package com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.common.types.JvmTypeReference;

import com.jaspersoft.studio.editor.jrexpressions.compatibility.Switch;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayInitializer;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BooleanLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CharLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRExpressionModel;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmGenericArrayTypeReference;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmLowerBound;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmUpperBound;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmWildcardTypeReference;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.NullLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage
 * @generated
 */
public class JavaJRExpressionSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static JavaJRExpressionPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaJRExpressionSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = JavaJRExpressionPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case JavaJRExpressionPackage.JR_EXPRESSION_MODEL:
      {
        JRExpressionModel jrExpressionModel = (JRExpressionModel)theEObject;
        T result = caseJRExpressionModel(jrExpressionModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JASPER_REPORTS_EXPRESSION:
      {
        JasperReportsExpression jasperReportsExpression = (JasperReportsExpression)theEObject;
        T result = caseJasperReportsExpression(jasperReportsExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.TYPE:
      {
        Type type = (Type)theEObject;
        T result = caseType(type);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.ARRAY_INITIALIZER:
      {
        ArrayInitializer arrayInitializer = (ArrayInitializer)theEObject;
        T result = caseArrayInitializer(arrayInitializer);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.METHOD_INVOCATION:
      {
        MethodInvocation methodInvocation = (MethodInvocation)theEObject;
        T result = caseMethodInvocation(methodInvocation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.FULL_METHOD_NAME:
      {
        FullMethodName fullMethodName = (FullMethodName)theEObject;
        T result = caseFullMethodName(fullMethodName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.ARGUMENTS:
      {
        Arguments arguments = (Arguments)theEObject;
        T result = caseArguments(arguments);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.EXPRESSION_LIST:
      {
        ExpressionList expressionList = (ExpressionList)theEObject;
        T result = caseExpressionList(expressionList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JVM_PARAMETERIZED_TYPE_REFERENCE:
      {
        JvmParameterizedTypeReference jvmParameterizedTypeReference = (JvmParameterizedTypeReference)theEObject;
        T result = caseJvmParameterizedTypeReference(jvmParameterizedTypeReference);
        if (result == null) result = caseJvmTypeReference(jvmParameterizedTypeReference);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JVM_WILDCARD_TYPE_REFERENCE:
      {
        JvmWildcardTypeReference jvmWildcardTypeReference = (JvmWildcardTypeReference)theEObject;
        T result = caseJvmWildcardTypeReference(jvmWildcardTypeReference);
        if (result == null) result = caseJvmTypeReference(jvmWildcardTypeReference);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JVM_UPPER_BOUND:
      {
        JvmUpperBound jvmUpperBound = (JvmUpperBound)theEObject;
        T result = caseJvmUpperBound(jvmUpperBound);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JVM_LOWER_BOUND:
      {
        JvmLowerBound jvmLowerBound = (JvmLowerBound)theEObject;
        T result = caseJvmLowerBound(jvmLowerBound);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.TEST_EXPRESSION:
      {
        TestExpression testExpression = (TestExpression)theEObject;
        T result = caseTestExpression(testExpression);
        if (result == null) result = caseJasperReportsExpression(testExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.BINARY_EXPRESSION:
      {
        BinaryExpression binaryExpression = (BinaryExpression)theEObject;
        T result = caseBinaryExpression(binaryExpression);
        if (result == null) result = caseJasperReportsExpression(binaryExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.STATIC_FIELD:
      {
        StaticField staticField = (StaticField)theEObject;
        T result = caseStaticField(staticField);
        if (result == null) result = caseJasperReportsExpression(staticField);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JR_FIELD_OBJ:
      {
        JRFieldObj jrFieldObj = (JRFieldObj)theEObject;
        T result = caseJRFieldObj(jrFieldObj);
        if (result == null) result = caseJasperReportsExpression(jrFieldObj);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JR_PARAMETER_OBJ:
      {
        JRParameterObj jrParameterObj = (JRParameterObj)theEObject;
        T result = caseJRParameterObj(jrParameterObj);
        if (result == null) result = caseJasperReportsExpression(jrParameterObj);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JR_VARIABLE_OBJ:
      {
        JRVariableObj jrVariableObj = (JRVariableObj)theEObject;
        T result = caseJRVariableObj(jrVariableObj);
        if (result == null) result = caseJasperReportsExpression(jrVariableObj);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JR_RESOURCE_BUNDLE_KEY_OBJ:
      {
        JRResourceBundleKeyObj jrResourceBundleKeyObj = (JRResourceBundleKeyObj)theEObject;
        T result = caseJRResourceBundleKeyObj(jrResourceBundleKeyObj);
        if (result == null) result = caseJasperReportsExpression(jrResourceBundleKeyObj);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.METHODS_EXPRESSION:
      {
        MethodsExpression methodsExpression = (MethodsExpression)theEObject;
        T result = caseMethodsExpression(methodsExpression);
        if (result == null) result = caseJasperReportsExpression(methodsExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.INT_LITERAL:
      {
        IntLiteral intLiteral = (IntLiteral)theEObject;
        T result = caseIntLiteral(intLiteral);
        if (result == null) result = caseJasperReportsExpression(intLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.LONG_LITERAL:
      {
        LongLiteral longLiteral = (LongLiteral)theEObject;
        T result = caseLongLiteral(longLiteral);
        if (result == null) result = caseJasperReportsExpression(longLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.FLOAT_LITERAL:
      {
        FloatLiteral floatLiteral = (FloatLiteral)theEObject;
        T result = caseFloatLiteral(floatLiteral);
        if (result == null) result = caseJasperReportsExpression(floatLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.DOUBLE_LITERAL:
      {
        DoubleLiteral doubleLiteral = (DoubleLiteral)theEObject;
        T result = caseDoubleLiteral(doubleLiteral);
        if (result == null) result = caseJasperReportsExpression(doubleLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.CHAR_LITERAL:
      {
        CharLiteral charLiteral = (CharLiteral)theEObject;
        T result = caseCharLiteral(charLiteral);
        if (result == null) result = caseJasperReportsExpression(charLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.STRING_LITERAL:
      {
        StringLiteral stringLiteral = (StringLiteral)theEObject;
        T result = caseStringLiteral(stringLiteral);
        if (result == null) result = caseJasperReportsExpression(stringLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.BOOLEAN_LITERAL:
      {
        BooleanLiteral booleanLiteral = (BooleanLiteral)theEObject;
        T result = caseBooleanLiteral(booleanLiteral);
        if (result == null) result = caseJasperReportsExpression(booleanLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.NULL_LITERAL:
      {
        NullLiteral nullLiteral = (NullLiteral)theEObject;
        T result = caseNullLiteral(nullLiteral);
        if (result == null) result = caseJasperReportsExpression(nullLiteral);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.CASTED_EXPRESSION:
      {
        CastedExpression castedExpression = (CastedExpression)theEObject;
        T result = caseCastedExpression(castedExpression);
        if (result == null) result = caseJasperReportsExpression(castedExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.ARRAY_CREATOR:
      {
        ArrayCreator arrayCreator = (ArrayCreator)theEObject;
        T result = caseArrayCreator(arrayCreator);
        if (result == null) result = caseJasperReportsExpression(arrayCreator);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case JavaJRExpressionPackage.JVM_GENERIC_ARRAY_TYPE_REFERENCE:
      {
        JvmGenericArrayTypeReference jvmGenericArrayTypeReference = (JvmGenericArrayTypeReference)theEObject;
        T result = caseJvmGenericArrayTypeReference(jvmGenericArrayTypeReference);
        if (result == null) result = caseJvmTypeReference(jvmGenericArrayTypeReference);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>JR Expression Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>JR Expression Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJRExpressionModel(JRExpressionModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jasper Reports Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jasper Reports Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJasperReportsExpression(JasperReportsExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseType(Type object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Array Initializer</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Array Initializer</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArrayInitializer(ArrayInitializer object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Method Invocation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Method Invocation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMethodInvocation(MethodInvocation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Full Method Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Full Method Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFullMethodName(FullMethodName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Arguments</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Arguments</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArguments(Arguments object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expression List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expression List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpressionList(ExpressionList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Parameterized Type Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Parameterized Type Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmParameterizedTypeReference(JvmParameterizedTypeReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Wildcard Type Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Wildcard Type Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmWildcardTypeReference(JvmWildcardTypeReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Upper Bound</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Upper Bound</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmUpperBound(JvmUpperBound object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Lower Bound</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Lower Bound</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmLowerBound(JvmLowerBound object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Test Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Test Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTestExpression(TestExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBinaryExpression(BinaryExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Static Field</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Static Field</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStaticField(StaticField object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>JR Field Obj</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>JR Field Obj</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJRFieldObj(JRFieldObj object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>JR Parameter Obj</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>JR Parameter Obj</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJRParameterObj(JRParameterObj object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>JR Variable Obj</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>JR Variable Obj</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJRVariableObj(JRVariableObj object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>JR Resource Bundle Key Obj</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>JR Resource Bundle Key Obj</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJRResourceBundleKeyObj(JRResourceBundleKeyObj object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Methods Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Methods Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMethodsExpression(MethodsExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Int Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Int Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIntLiteral(IntLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Long Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Long Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLongLiteral(LongLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Float Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Float Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFloatLiteral(FloatLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Double Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Double Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDoubleLiteral(DoubleLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Char Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Char Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCharLiteral(CharLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringLiteral(StringLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Boolean Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Boolean Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBooleanLiteral(BooleanLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Null Literal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Null Literal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNullLiteral(NullLiteral object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Casted Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Casted Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCastedExpression(CastedExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Array Creator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Array Creator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseArrayCreator(ArrayCreator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Generic Array Type Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Generic Array Type Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmGenericArrayTypeReference(JvmGenericArrayTypeReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Jvm Type Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Jvm Type Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJvmTypeReference(JvmTypeReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //JavaJRExpressionSwitch
