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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionFactory
 * @model kind="package"
 * @generated
 */
public interface JavaJRExpressionPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "javaJRExpression";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.jaspersoft.com/studio/editor/jrexpressions/JavaJRExpression";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "javaJRExpression";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  JavaJRExpressionPackage eINSTANCE = com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl.init();

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRExpressionModelImpl <em>JR Expression Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRExpressionModelImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRExpressionModel()
   * @generated
   */
  int JR_EXPRESSION_MODEL = 0;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_EXPRESSION_MODEL__EXPRESSION = 0;

  /**
   * The number of structural features of the '<em>JR Expression Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_EXPRESSION_MODEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JasperReportsExpressionImpl <em>Jasper Reports Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JasperReportsExpressionImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJasperReportsExpression()
   * @generated
   */
  int JASPER_REPORTS_EXPRESSION = 1;

  /**
   * The number of structural features of the '<em>Jasper Reports Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JASPER_REPORTS_EXPRESSION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TypeImpl <em>Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TypeImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getType()
   * @generated
   */
  int TYPE = 2;

  /**
   * The feature id for the '<em><b>Primitive Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE__PRIMITIVE_TYPE = 0;

  /**
   * The feature id for the '<em><b>Jvm Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE__JVM_TYPE = 1;

  /**
   * The number of structural features of the '<em>Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayInitializerImpl <em>Array Initializer</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayInitializerImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getArrayInitializer()
   * @generated
   */
  int ARRAY_INITIALIZER = 3;

  /**
   * The feature id for the '<em><b>Initialization</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_INITIALIZER__INITIALIZATION = 0;

  /**
   * The number of structural features of the '<em>Array Initializer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_INITIALIZER_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodInvocationImpl <em>Method Invocation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodInvocationImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getMethodInvocation()
   * @generated
   */
  int METHOD_INVOCATION = 4;

  /**
   * The feature id for the '<em><b>Fully Qualified Method Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME = 0;

  /**
   * The feature id for the '<em><b>Args</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_INVOCATION__ARGS = 1;

  /**
   * The number of structural features of the '<em>Method Invocation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHOD_INVOCATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FullMethodNameImpl <em>Full Method Name</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FullMethodNameImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getFullMethodName()
   * @generated
   */
  int FULL_METHOD_NAME = 5;

  /**
   * The feature id for the '<em><b>Prefix QMN</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FULL_METHOD_NAME__PREFIX_QMN = 0;

  /**
   * The feature id for the '<em><b>Dots</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FULL_METHOD_NAME__DOTS = 1;

  /**
   * The feature id for the '<em><b>Method Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FULL_METHOD_NAME__METHOD_NAME = 2;

  /**
   * The number of structural features of the '<em>Full Method Name</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FULL_METHOD_NAME_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArgumentsImpl <em>Arguments</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArgumentsImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getArguments()
   * @generated
   */
  int ARGUMENTS = 6;

  /**
   * The feature id for the '<em><b>Expr Lst</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARGUMENTS__EXPR_LST = 0;

  /**
   * The number of structural features of the '<em>Arguments</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARGUMENTS_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ExpressionListImpl <em>Expression List</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ExpressionListImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getExpressionList()
   * @generated
   */
  int EXPRESSION_LIST = 7;

  /**
   * The feature id for the '<em><b>Expressions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_LIST__EXPRESSIONS = 0;

  /**
   * The feature id for the '<em><b>Commas</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_LIST__COMMAS = 1;

  /**
   * The number of structural features of the '<em>Expression List</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXPRESSION_LIST_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmParameterizedTypeReferenceImpl <em>Jvm Parameterized Type Reference</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmParameterizedTypeReferenceImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmParameterizedTypeReference()
   * @generated
   */
  int JVM_PARAMETERIZED_TYPE_REFERENCE = 8;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_PARAMETERIZED_TYPE_REFERENCE__ARGUMENTS = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Jvm Parameterized Type Reference</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_PARAMETERIZED_TYPE_REFERENCE_FEATURE_COUNT = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmWildcardTypeReferenceImpl <em>Jvm Wildcard Type Reference</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmWildcardTypeReferenceImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmWildcardTypeReference()
   * @generated
   */
  int JVM_WILDCARD_TYPE_REFERENCE = 9;

  /**
   * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_WILDCARD_TYPE_REFERENCE__CONSTRAINTS = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Jvm Wildcard Type Reference</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_WILDCARD_TYPE_REFERENCE_FEATURE_COUNT = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmUpperBoundImpl <em>Jvm Upper Bound</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmUpperBoundImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmUpperBound()
   * @generated
   */
  int JVM_UPPER_BOUND = 10;

  /**
   * The feature id for the '<em><b>Type Reference</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_UPPER_BOUND__TYPE_REFERENCE = 0;

  /**
   * The number of structural features of the '<em>Jvm Upper Bound</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_UPPER_BOUND_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmLowerBoundImpl <em>Jvm Lower Bound</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmLowerBoundImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmLowerBound()
   * @generated
   */
  int JVM_LOWER_BOUND = 11;

  /**
   * The feature id for the '<em><b>Type Reference</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_LOWER_BOUND__TYPE_REFERENCE = 0;

  /**
   * The number of structural features of the '<em>Jvm Lower Bound</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_LOWER_BOUND_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TestExpressionImpl <em>Test Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TestExpressionImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getTestExpression()
   * @generated
   */
  int TEST_EXPRESSION = 12;

  /**
   * The feature id for the '<em><b>Condition</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEST_EXPRESSION__CONDITION = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>True Statement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEST_EXPRESSION__TRUE_STATEMENT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>False Statement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEST_EXPRESSION__FALSE_STATEMENT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Test Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEST_EXPRESSION_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BinaryExpressionImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getBinaryExpression()
   * @generated
   */
  int BINARY_EXPRESSION = 13;

  /**
   * The feature id for the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__LEFT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Op</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__OP = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Right</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION__RIGHT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Binary Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINARY_EXPRESSION_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StaticFieldImpl <em>Static Field</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StaticFieldImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getStaticField()
   * @generated
   */
  int STATIC_FIELD = 14;

  /**
   * The feature id for the '<em><b>Prefix QMN</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATIC_FIELD__PREFIX_QMN = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Dots</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATIC_FIELD__DOTS = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Field Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATIC_FIELD__FIELD_NAME = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Static Field</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATIC_FIELD_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRFieldObjImpl <em>JR Field Obj</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRFieldObjImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRFieldObj()
   * @generated
   */
  int JR_FIELD_OBJ = 15;

  /**
   * The feature id for the '<em><b>Braced Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_FIELD_OBJ__BRACED_IDENTIFIER = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>JR Field Obj</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_FIELD_OBJ_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRParameterObjImpl <em>JR Parameter Obj</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRParameterObjImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRParameterObj()
   * @generated
   */
  int JR_PARAMETER_OBJ = 16;

  /**
   * The feature id for the '<em><b>Braced Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_PARAMETER_OBJ__BRACED_IDENTIFIER = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>JR Parameter Obj</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_PARAMETER_OBJ_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRVariableObjImpl <em>JR Variable Obj</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRVariableObjImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRVariableObj()
   * @generated
   */
  int JR_VARIABLE_OBJ = 17;

  /**
   * The feature id for the '<em><b>Braced Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_VARIABLE_OBJ__BRACED_IDENTIFIER = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>JR Variable Obj</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_VARIABLE_OBJ_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRResourceBundleKeyObjImpl <em>JR Resource Bundle Key Obj</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRResourceBundleKeyObjImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRResourceBundleKeyObj()
   * @generated
   */
  int JR_RESOURCE_BUNDLE_KEY_OBJ = 18;

  /**
   * The feature id for the '<em><b>Braced Identifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_RESOURCE_BUNDLE_KEY_OBJ__BRACED_IDENTIFIER = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>JR Resource Bundle Key Obj</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JR_RESOURCE_BUNDLE_KEY_OBJ_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl <em>Methods Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getMethodsExpression()
   * @generated
   */
  int METHODS_EXPRESSION = 19;

  /**
   * The feature id for the '<em><b>Include Object Instatiation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Method Invocations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHODS_EXPRESSION__METHOD_INVOCATIONS = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Object Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHODS_EXPRESSION__OBJECT_EXPRESSION = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Array Indexes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHODS_EXPRESSION__ARRAY_INDEXES = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Methods Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METHODS_EXPRESSION_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.IntLiteralImpl <em>Int Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.IntLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getIntLiteral()
   * @generated
   */
  int INT_LITERAL = 20;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INT_LITERAL__VALUE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Int Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INT_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.LongLiteralImpl <em>Long Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.LongLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getLongLiteral()
   * @generated
   */
  int LONG_LITERAL = 21;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_LITERAL__VALUE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Long Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LONG_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FloatLiteralImpl <em>Float Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FloatLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getFloatLiteral()
   * @generated
   */
  int FLOAT_LITERAL = 22;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FLOAT_LITERAL__VALUE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Float Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FLOAT_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.DoubleLiteralImpl <em>Double Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.DoubleLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getDoubleLiteral()
   * @generated
   */
  int DOUBLE_LITERAL = 23;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_LITERAL__VALUE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Double Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOUBLE_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CharLiteralImpl <em>Char Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CharLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getCharLiteral()
   * @generated
   */
  int CHAR_LITERAL = 24;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHAR_LITERAL__VALUE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Char Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CHAR_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StringLiteralImpl <em>String Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StringLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getStringLiteral()
   * @generated
   */
  int STRING_LITERAL = 25;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_LITERAL__VALUE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>String Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BooleanLiteralImpl <em>Boolean Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BooleanLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getBooleanLiteral()
   * @generated
   */
  int BOOLEAN_LITERAL = 26;

  /**
   * The feature id for the '<em><b>Is True</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN_LITERAL__IS_TRUE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Boolean Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BOOLEAN_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.NullLiteralImpl <em>Null Literal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.NullLiteralImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getNullLiteral()
   * @generated
   */
  int NULL_LITERAL = 27;

  /**
   * The number of structural features of the '<em>Null Literal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NULL_LITERAL_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CastedExpressionImpl <em>Casted Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CastedExpressionImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getCastedExpression()
   * @generated
   */
  int CASTED_EXPRESSION = 28;

  /**
   * The feature id for the '<em><b>Cast Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASTED_EXPRESSION__CAST_TYPE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Casted Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASTED_EXPRESSION__CASTED_EXPR = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Casted Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CASTED_EXPRESSION_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayCreatorImpl <em>Array Creator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayCreatorImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getArrayCreator()
   * @generated
   */
  int ARRAY_CREATOR = 29;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_CREATOR__TYPE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Size</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_CREATOR__SIZE = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Initialization</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_CREATOR__INITIALIZATION = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Array Creator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_CREATOR_FEATURE_COUNT = JASPER_REPORTS_EXPRESSION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmGenericArrayTypeReferenceImpl <em>Jvm Generic Array Type Reference</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmGenericArrayTypeReferenceImpl
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmGenericArrayTypeReference()
   * @generated
   */
  int JVM_GENERIC_ARRAY_TYPE_REFERENCE = 30;

  /**
   * The feature id for the '<em><b>Component Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_GENERIC_ARRAY_TYPE_REFERENCE__COMPONENT_TYPE = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Jvm Generic Array Type Reference</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JVM_GENERIC_ARRAY_TYPE_REFERENCE_FEATURE_COUNT = TypesPackage.JVM_TYPE_REFERENCE_FEATURE_COUNT + 1;


  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRExpressionModel <em>JR Expression Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>JR Expression Model</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRExpressionModel
   * @generated
   */
  EClass getJRExpressionModel();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRExpressionModel#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRExpressionModel#getExpression()
   * @see #getJRExpressionModel()
   * @generated
   */
  EReference getJRExpressionModel_Expression();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression <em>Jasper Reports Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Jasper Reports Expression</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression
   * @generated
   */
  EClass getJasperReportsExpression();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type
   * @generated
   */
  EClass getType();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type#isPrimitiveType <em>Primitive Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Primitive Type</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type#isPrimitiveType()
   * @see #getType()
   * @generated
   */
  EAttribute getType_PrimitiveType();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type#getJvmType <em>Jvm Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Jvm Type</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type#getJvmType()
   * @see #getType()
   * @generated
   */
  EReference getType_JvmType();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayInitializer <em>Array Initializer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Initializer</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayInitializer
   * @generated
   */
  EClass getArrayInitializer();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayInitializer#getInitialization <em>Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Initialization</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayInitializer#getInitialization()
   * @see #getArrayInitializer()
   * @generated
   */
  EReference getArrayInitializer_Initialization();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation <em>Method Invocation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Method Invocation</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation
   * @generated
   */
  EClass getMethodInvocation();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation#getFullyQualifiedMethodName <em>Fully Qualified Method Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Fully Qualified Method Name</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation#getFullyQualifiedMethodName()
   * @see #getMethodInvocation()
   * @generated
   */
  EReference getMethodInvocation_FullyQualifiedMethodName();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation#getArgs <em>Args</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Args</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation#getArgs()
   * @see #getMethodInvocation()
   * @generated
   */
  EReference getMethodInvocation_Args();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName <em>Full Method Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Full Method Name</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName
   * @generated
   */
  EClass getFullMethodName();

  /**
   * Returns the meta object for the attribute list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName#getPrefixQMN <em>Prefix QMN</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Prefix QMN</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName#getPrefixQMN()
   * @see #getFullMethodName()
   * @generated
   */
  EAttribute getFullMethodName_PrefixQMN();

  /**
   * Returns the meta object for the attribute list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName#getDots <em>Dots</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Dots</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName#getDots()
   * @see #getFullMethodName()
   * @generated
   */
  EAttribute getFullMethodName_Dots();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName#getMethodName <em>Method Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Method Name</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName#getMethodName()
   * @see #getFullMethodName()
   * @generated
   */
  EAttribute getFullMethodName_MethodName();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments <em>Arguments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Arguments</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments
   * @generated
   */
  EClass getArguments();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments#getExprLst <em>Expr Lst</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expr Lst</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments#getExprLst()
   * @see #getArguments()
   * @generated
   */
  EReference getArguments_ExprLst();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList <em>Expression List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Expression List</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList
   * @generated
   */
  EClass getExpressionList();

  /**
   * Returns the meta object for the containment reference list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList#getExpressions <em>Expressions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Expressions</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList#getExpressions()
   * @see #getExpressionList()
   * @generated
   */
  EReference getExpressionList_Expressions();

  /**
   * Returns the meta object for the attribute list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList#getCommas <em>Commas</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Commas</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList#getCommas()
   * @see #getExpressionList()
   * @generated
   */
  EAttribute getExpressionList_Commas();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference <em>Jvm Parameterized Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Jvm Parameterized Type Reference</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference
   * @generated
   */
  EClass getJvmParameterizedTypeReference();

  /**
   * Returns the meta object for the reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference#getType()
   * @see #getJvmParameterizedTypeReference()
   * @generated
   */
  EReference getJvmParameterizedTypeReference_Type();

  /**
   * Returns the meta object for the containment reference list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference#getArguments <em>Arguments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Arguments</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference#getArguments()
   * @see #getJvmParameterizedTypeReference()
   * @generated
   */
  EReference getJvmParameterizedTypeReference_Arguments();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmWildcardTypeReference <em>Jvm Wildcard Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Jvm Wildcard Type Reference</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmWildcardTypeReference
   * @generated
   */
  EClass getJvmWildcardTypeReference();

  /**
   * Returns the meta object for the containment reference list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmWildcardTypeReference#getConstraints <em>Constraints</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Constraints</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmWildcardTypeReference#getConstraints()
   * @see #getJvmWildcardTypeReference()
   * @generated
   */
  EReference getJvmWildcardTypeReference_Constraints();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmUpperBound <em>Jvm Upper Bound</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Jvm Upper Bound</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmUpperBound
   * @generated
   */
  EClass getJvmUpperBound();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmUpperBound#getTypeReference <em>Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type Reference</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmUpperBound#getTypeReference()
   * @see #getJvmUpperBound()
   * @generated
   */
  EReference getJvmUpperBound_TypeReference();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmLowerBound <em>Jvm Lower Bound</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Jvm Lower Bound</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmLowerBound
   * @generated
   */
  EClass getJvmLowerBound();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmLowerBound#getTypeReference <em>Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type Reference</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmLowerBound#getTypeReference()
   * @see #getJvmLowerBound()
   * @generated
   */
  EReference getJvmLowerBound_TypeReference();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression <em>Test Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Test Expression</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression
   * @generated
   */
  EClass getTestExpression();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression#getCondition <em>Condition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Condition</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression#getCondition()
   * @see #getTestExpression()
   * @generated
   */
  EReference getTestExpression_Condition();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression#getTrueStatement <em>True Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>True Statement</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression#getTrueStatement()
   * @see #getTestExpression()
   * @generated
   */
  EReference getTestExpression_TrueStatement();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression#getFalseStatement <em>False Statement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>False Statement</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression#getFalseStatement()
   * @see #getTestExpression()
   * @generated
   */
  EReference getTestExpression_FalseStatement();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression <em>Binary Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binary Expression</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression
   * @generated
   */
  EClass getBinaryExpression();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression#getLeft <em>Left</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression#getLeft()
   * @see #getBinaryExpression()
   * @generated
   */
  EReference getBinaryExpression_Left();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression#getOp <em>Op</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Op</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression#getOp()
   * @see #getBinaryExpression()
   * @generated
   */
  EAttribute getBinaryExpression_Op();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression#getRight <em>Right</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Right</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression#getRight()
   * @see #getBinaryExpression()
   * @generated
   */
  EReference getBinaryExpression_Right();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField <em>Static Field</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Static Field</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField
   * @generated
   */
  EClass getStaticField();

  /**
   * Returns the meta object for the attribute list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField#getPrefixQMN <em>Prefix QMN</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Prefix QMN</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField#getPrefixQMN()
   * @see #getStaticField()
   * @generated
   */
  EAttribute getStaticField_PrefixQMN();

  /**
   * Returns the meta object for the attribute list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField#getDots <em>Dots</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Dots</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField#getDots()
   * @see #getStaticField()
   * @generated
   */
  EAttribute getStaticField_Dots();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField#getFieldName <em>Field Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Field Name</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField#getFieldName()
   * @see #getStaticField()
   * @generated
   */
  EAttribute getStaticField_FieldName();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj <em>JR Field Obj</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>JR Field Obj</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj
   * @generated
   */
  EClass getJRFieldObj();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj#getBracedIdentifier <em>Braced Identifier</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Braced Identifier</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj#getBracedIdentifier()
   * @see #getJRFieldObj()
   * @generated
   */
  EAttribute getJRFieldObj_BracedIdentifier();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj <em>JR Parameter Obj</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>JR Parameter Obj</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj
   * @generated
   */
  EClass getJRParameterObj();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj#getBracedIdentifier <em>Braced Identifier</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Braced Identifier</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj#getBracedIdentifier()
   * @see #getJRParameterObj()
   * @generated
   */
  EAttribute getJRParameterObj_BracedIdentifier();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj <em>JR Variable Obj</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>JR Variable Obj</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj
   * @generated
   */
  EClass getJRVariableObj();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj#getBracedIdentifier <em>Braced Identifier</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Braced Identifier</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj#getBracedIdentifier()
   * @see #getJRVariableObj()
   * @generated
   */
  EAttribute getJRVariableObj_BracedIdentifier();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj <em>JR Resource Bundle Key Obj</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>JR Resource Bundle Key Obj</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj
   * @generated
   */
  EClass getJRResourceBundleKeyObj();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj#getBracedIdentifier <em>Braced Identifier</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Braced Identifier</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj#getBracedIdentifier()
   * @see #getJRResourceBundleKeyObj()
   * @generated
   */
  EAttribute getJRResourceBundleKeyObj_BracedIdentifier();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression <em>Methods Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Methods Expression</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression
   * @generated
   */
  EClass getMethodsExpression();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#isIncludeObjectInstatiation <em>Include Object Instatiation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Include Object Instatiation</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#isIncludeObjectInstatiation()
   * @see #getMethodsExpression()
   * @generated
   */
  EAttribute getMethodsExpression_IncludeObjectInstatiation();

  /**
   * Returns the meta object for the containment reference list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getMethodInvocations <em>Method Invocations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Method Invocations</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getMethodInvocations()
   * @see #getMethodsExpression()
   * @generated
   */
  EReference getMethodsExpression_MethodInvocations();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getObjectExpression <em>Object Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Object Expression</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getObjectExpression()
   * @see #getMethodsExpression()
   * @generated
   */
  EReference getMethodsExpression_ObjectExpression();

  /**
   * Returns the meta object for the containment reference list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getArrayIndexes <em>Array Indexes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Array Indexes</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression#getArrayIndexes()
   * @see #getMethodsExpression()
   * @generated
   */
  EReference getMethodsExpression_ArrayIndexes();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral <em>Int Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Int Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral
   * @generated
   */
  EClass getIntLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral#getValue()
   * @see #getIntLiteral()
   * @generated
   */
  EAttribute getIntLiteral_Value();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral <em>Long Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Long Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral
   * @generated
   */
  EClass getLongLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral#getValue()
   * @see #getLongLiteral()
   * @generated
   */
  EAttribute getLongLiteral_Value();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral <em>Float Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Float Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral
   * @generated
   */
  EClass getFloatLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral#getValue()
   * @see #getFloatLiteral()
   * @generated
   */
  EAttribute getFloatLiteral_Value();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral <em>Double Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Double Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral
   * @generated
   */
  EClass getDoubleLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral#getValue()
   * @see #getDoubleLiteral()
   * @generated
   */
  EAttribute getDoubleLiteral_Value();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CharLiteral <em>Char Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Char Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CharLiteral
   * @generated
   */
  EClass getCharLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CharLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CharLiteral#getValue()
   * @see #getCharLiteral()
   * @generated
   */
  EAttribute getCharLiteral_Value();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral <em>String Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral
   * @generated
   */
  EClass getStringLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral#getValue()
   * @see #getStringLiteral()
   * @generated
   */
  EAttribute getStringLiteral_Value();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BooleanLiteral <em>Boolean Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Boolean Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BooleanLiteral
   * @generated
   */
  EClass getBooleanLiteral();

  /**
   * Returns the meta object for the attribute '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BooleanLiteral#isIsTrue <em>Is True</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Is True</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BooleanLiteral#isIsTrue()
   * @see #getBooleanLiteral()
   * @generated
   */
  EAttribute getBooleanLiteral_IsTrue();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.NullLiteral <em>Null Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Null Literal</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.NullLiteral
   * @generated
   */
  EClass getNullLiteral();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression <em>Casted Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Casted Expression</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression
   * @generated
   */
  EClass getCastedExpression();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression#getCastType <em>Cast Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Cast Type</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression#getCastType()
   * @see #getCastedExpression()
   * @generated
   */
  EReference getCastedExpression_CastType();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression#getCastedExpr <em>Casted Expr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Casted Expr</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression#getCastedExpr()
   * @see #getCastedExpression()
   * @generated
   */
  EReference getCastedExpression_CastedExpr();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator <em>Array Creator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Creator</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator
   * @generated
   */
  EClass getArrayCreator();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator#getType()
   * @see #getArrayCreator()
   * @generated
   */
  EReference getArrayCreator_Type();

  /**
   * Returns the meta object for the containment reference list '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator#getSize <em>Size</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Size</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator#getSize()
   * @see #getArrayCreator()
   * @generated
   */
  EReference getArrayCreator_Size();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator#getInitialization <em>Initialization</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Initialization</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator#getInitialization()
   * @see #getArrayCreator()
   * @generated
   */
  EReference getArrayCreator_Initialization();

  /**
   * Returns the meta object for class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmGenericArrayTypeReference <em>Jvm Generic Array Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Jvm Generic Array Type Reference</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmGenericArrayTypeReference
   * @generated
   */
  EClass getJvmGenericArrayTypeReference();

  /**
   * Returns the meta object for the containment reference '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmGenericArrayTypeReference#getComponentType <em>Component Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Component Type</em>'.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmGenericArrayTypeReference#getComponentType()
   * @see #getJvmGenericArrayTypeReference()
   * @generated
   */
  EReference getJvmGenericArrayTypeReference_ComponentType();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  JavaJRExpressionFactory getJavaJRExpressionFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRExpressionModelImpl <em>JR Expression Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRExpressionModelImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRExpressionModel()
     * @generated
     */
    EClass JR_EXPRESSION_MODEL = eINSTANCE.getJRExpressionModel();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JR_EXPRESSION_MODEL__EXPRESSION = eINSTANCE.getJRExpressionModel_Expression();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JasperReportsExpressionImpl <em>Jasper Reports Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JasperReportsExpressionImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJasperReportsExpression()
     * @generated
     */
    EClass JASPER_REPORTS_EXPRESSION = eINSTANCE.getJasperReportsExpression();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TypeImpl <em>Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TypeImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getType()
     * @generated
     */
    EClass TYPE = eINSTANCE.getType();

    /**
     * The meta object literal for the '<em><b>Primitive Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TYPE__PRIMITIVE_TYPE = eINSTANCE.getType_PrimitiveType();

    /**
     * The meta object literal for the '<em><b>Jvm Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TYPE__JVM_TYPE = eINSTANCE.getType_JvmType();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayInitializerImpl <em>Array Initializer</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayInitializerImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getArrayInitializer()
     * @generated
     */
    EClass ARRAY_INITIALIZER = eINSTANCE.getArrayInitializer();

    /**
     * The meta object literal for the '<em><b>Initialization</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_INITIALIZER__INITIALIZATION = eINSTANCE.getArrayInitializer_Initialization();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodInvocationImpl <em>Method Invocation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodInvocationImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getMethodInvocation()
     * @generated
     */
    EClass METHOD_INVOCATION = eINSTANCE.getMethodInvocation();

    /**
     * The meta object literal for the '<em><b>Fully Qualified Method Name</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME = eINSTANCE.getMethodInvocation_FullyQualifiedMethodName();

    /**
     * The meta object literal for the '<em><b>Args</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHOD_INVOCATION__ARGS = eINSTANCE.getMethodInvocation_Args();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FullMethodNameImpl <em>Full Method Name</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FullMethodNameImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getFullMethodName()
     * @generated
     */
    EClass FULL_METHOD_NAME = eINSTANCE.getFullMethodName();

    /**
     * The meta object literal for the '<em><b>Prefix QMN</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FULL_METHOD_NAME__PREFIX_QMN = eINSTANCE.getFullMethodName_PrefixQMN();

    /**
     * The meta object literal for the '<em><b>Dots</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FULL_METHOD_NAME__DOTS = eINSTANCE.getFullMethodName_Dots();

    /**
     * The meta object literal for the '<em><b>Method Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FULL_METHOD_NAME__METHOD_NAME = eINSTANCE.getFullMethodName_MethodName();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArgumentsImpl <em>Arguments</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArgumentsImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getArguments()
     * @generated
     */
    EClass ARGUMENTS = eINSTANCE.getArguments();

    /**
     * The meta object literal for the '<em><b>Expr Lst</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARGUMENTS__EXPR_LST = eINSTANCE.getArguments_ExprLst();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ExpressionListImpl <em>Expression List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ExpressionListImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getExpressionList()
     * @generated
     */
    EClass EXPRESSION_LIST = eINSTANCE.getExpressionList();

    /**
     * The meta object literal for the '<em><b>Expressions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXPRESSION_LIST__EXPRESSIONS = eINSTANCE.getExpressionList_Expressions();

    /**
     * The meta object literal for the '<em><b>Commas</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXPRESSION_LIST__COMMAS = eINSTANCE.getExpressionList_Commas();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmParameterizedTypeReferenceImpl <em>Jvm Parameterized Type Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmParameterizedTypeReferenceImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmParameterizedTypeReference()
     * @generated
     */
    EClass JVM_PARAMETERIZED_TYPE_REFERENCE = eINSTANCE.getJvmParameterizedTypeReference();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE = eINSTANCE.getJvmParameterizedTypeReference_Type();

    /**
     * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JVM_PARAMETERIZED_TYPE_REFERENCE__ARGUMENTS = eINSTANCE.getJvmParameterizedTypeReference_Arguments();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmWildcardTypeReferenceImpl <em>Jvm Wildcard Type Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmWildcardTypeReferenceImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmWildcardTypeReference()
     * @generated
     */
    EClass JVM_WILDCARD_TYPE_REFERENCE = eINSTANCE.getJvmWildcardTypeReference();

    /**
     * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JVM_WILDCARD_TYPE_REFERENCE__CONSTRAINTS = eINSTANCE.getJvmWildcardTypeReference_Constraints();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmUpperBoundImpl <em>Jvm Upper Bound</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmUpperBoundImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmUpperBound()
     * @generated
     */
    EClass JVM_UPPER_BOUND = eINSTANCE.getJvmUpperBound();

    /**
     * The meta object literal for the '<em><b>Type Reference</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JVM_UPPER_BOUND__TYPE_REFERENCE = eINSTANCE.getJvmUpperBound_TypeReference();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmLowerBoundImpl <em>Jvm Lower Bound</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmLowerBoundImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmLowerBound()
     * @generated
     */
    EClass JVM_LOWER_BOUND = eINSTANCE.getJvmLowerBound();

    /**
     * The meta object literal for the '<em><b>Type Reference</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JVM_LOWER_BOUND__TYPE_REFERENCE = eINSTANCE.getJvmLowerBound_TypeReference();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TestExpressionImpl <em>Test Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.TestExpressionImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getTestExpression()
     * @generated
     */
    EClass TEST_EXPRESSION = eINSTANCE.getTestExpression();

    /**
     * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TEST_EXPRESSION__CONDITION = eINSTANCE.getTestExpression_Condition();

    /**
     * The meta object literal for the '<em><b>True Statement</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TEST_EXPRESSION__TRUE_STATEMENT = eINSTANCE.getTestExpression_TrueStatement();

    /**
     * The meta object literal for the '<em><b>False Statement</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TEST_EXPRESSION__FALSE_STATEMENT = eINSTANCE.getTestExpression_FalseStatement();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BinaryExpressionImpl <em>Binary Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BinaryExpressionImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getBinaryExpression()
     * @generated
     */
    EClass BINARY_EXPRESSION = eINSTANCE.getBinaryExpression();

    /**
     * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINARY_EXPRESSION__LEFT = eINSTANCE.getBinaryExpression_Left();

    /**
     * The meta object literal for the '<em><b>Op</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINARY_EXPRESSION__OP = eINSTANCE.getBinaryExpression_Op();

    /**
     * The meta object literal for the '<em><b>Right</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINARY_EXPRESSION__RIGHT = eINSTANCE.getBinaryExpression_Right();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StaticFieldImpl <em>Static Field</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StaticFieldImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getStaticField()
     * @generated
     */
    EClass STATIC_FIELD = eINSTANCE.getStaticField();

    /**
     * The meta object literal for the '<em><b>Prefix QMN</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATIC_FIELD__PREFIX_QMN = eINSTANCE.getStaticField_PrefixQMN();

    /**
     * The meta object literal for the '<em><b>Dots</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATIC_FIELD__DOTS = eINSTANCE.getStaticField_Dots();

    /**
     * The meta object literal for the '<em><b>Field Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATIC_FIELD__FIELD_NAME = eINSTANCE.getStaticField_FieldName();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRFieldObjImpl <em>JR Field Obj</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRFieldObjImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRFieldObj()
     * @generated
     */
    EClass JR_FIELD_OBJ = eINSTANCE.getJRFieldObj();

    /**
     * The meta object literal for the '<em><b>Braced Identifier</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JR_FIELD_OBJ__BRACED_IDENTIFIER = eINSTANCE.getJRFieldObj_BracedIdentifier();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRParameterObjImpl <em>JR Parameter Obj</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRParameterObjImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRParameterObj()
     * @generated
     */
    EClass JR_PARAMETER_OBJ = eINSTANCE.getJRParameterObj();

    /**
     * The meta object literal for the '<em><b>Braced Identifier</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JR_PARAMETER_OBJ__BRACED_IDENTIFIER = eINSTANCE.getJRParameterObj_BracedIdentifier();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRVariableObjImpl <em>JR Variable Obj</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRVariableObjImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRVariableObj()
     * @generated
     */
    EClass JR_VARIABLE_OBJ = eINSTANCE.getJRVariableObj();

    /**
     * The meta object literal for the '<em><b>Braced Identifier</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JR_VARIABLE_OBJ__BRACED_IDENTIFIER = eINSTANCE.getJRVariableObj_BracedIdentifier();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRResourceBundleKeyObjImpl <em>JR Resource Bundle Key Obj</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JRResourceBundleKeyObjImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJRResourceBundleKeyObj()
     * @generated
     */
    EClass JR_RESOURCE_BUNDLE_KEY_OBJ = eINSTANCE.getJRResourceBundleKeyObj();

    /**
     * The meta object literal for the '<em><b>Braced Identifier</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JR_RESOURCE_BUNDLE_KEY_OBJ__BRACED_IDENTIFIER = eINSTANCE.getJRResourceBundleKeyObj_BracedIdentifier();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl <em>Methods Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getMethodsExpression()
     * @generated
     */
    EClass METHODS_EXPRESSION = eINSTANCE.getMethodsExpression();

    /**
     * The meta object literal for the '<em><b>Include Object Instatiation</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION = eINSTANCE.getMethodsExpression_IncludeObjectInstatiation();

    /**
     * The meta object literal for the '<em><b>Method Invocations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHODS_EXPRESSION__METHOD_INVOCATIONS = eINSTANCE.getMethodsExpression_MethodInvocations();

    /**
     * The meta object literal for the '<em><b>Object Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHODS_EXPRESSION__OBJECT_EXPRESSION = eINSTANCE.getMethodsExpression_ObjectExpression();

    /**
     * The meta object literal for the '<em><b>Array Indexes</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METHODS_EXPRESSION__ARRAY_INDEXES = eINSTANCE.getMethodsExpression_ArrayIndexes();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.IntLiteralImpl <em>Int Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.IntLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getIntLiteral()
     * @generated
     */
    EClass INT_LITERAL = eINSTANCE.getIntLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INT_LITERAL__VALUE = eINSTANCE.getIntLiteral_Value();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.LongLiteralImpl <em>Long Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.LongLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getLongLiteral()
     * @generated
     */
    EClass LONG_LITERAL = eINSTANCE.getLongLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LONG_LITERAL__VALUE = eINSTANCE.getLongLiteral_Value();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FloatLiteralImpl <em>Float Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.FloatLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getFloatLiteral()
     * @generated
     */
    EClass FLOAT_LITERAL = eINSTANCE.getFloatLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FLOAT_LITERAL__VALUE = eINSTANCE.getFloatLiteral_Value();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.DoubleLiteralImpl <em>Double Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.DoubleLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getDoubleLiteral()
     * @generated
     */
    EClass DOUBLE_LITERAL = eINSTANCE.getDoubleLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOUBLE_LITERAL__VALUE = eINSTANCE.getDoubleLiteral_Value();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CharLiteralImpl <em>Char Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CharLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getCharLiteral()
     * @generated
     */
    EClass CHAR_LITERAL = eINSTANCE.getCharLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CHAR_LITERAL__VALUE = eINSTANCE.getCharLiteral_Value();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StringLiteralImpl <em>String Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.StringLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getStringLiteral()
     * @generated
     */
    EClass STRING_LITERAL = eINSTANCE.getStringLiteral();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_LITERAL__VALUE = eINSTANCE.getStringLiteral_Value();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BooleanLiteralImpl <em>Boolean Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.BooleanLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getBooleanLiteral()
     * @generated
     */
    EClass BOOLEAN_LITERAL = eINSTANCE.getBooleanLiteral();

    /**
     * The meta object literal for the '<em><b>Is True</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BOOLEAN_LITERAL__IS_TRUE = eINSTANCE.getBooleanLiteral_IsTrue();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.NullLiteralImpl <em>Null Literal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.NullLiteralImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getNullLiteral()
     * @generated
     */
    EClass NULL_LITERAL = eINSTANCE.getNullLiteral();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CastedExpressionImpl <em>Casted Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.CastedExpressionImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getCastedExpression()
     * @generated
     */
    EClass CASTED_EXPRESSION = eINSTANCE.getCastedExpression();

    /**
     * The meta object literal for the '<em><b>Cast Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASTED_EXPRESSION__CAST_TYPE = eINSTANCE.getCastedExpression_CastType();

    /**
     * The meta object literal for the '<em><b>Casted Expr</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CASTED_EXPRESSION__CASTED_EXPR = eINSTANCE.getCastedExpression_CastedExpr();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayCreatorImpl <em>Array Creator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.ArrayCreatorImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getArrayCreator()
     * @generated
     */
    EClass ARRAY_CREATOR = eINSTANCE.getArrayCreator();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_CREATOR__TYPE = eINSTANCE.getArrayCreator_Type();

    /**
     * The meta object literal for the '<em><b>Size</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_CREATOR__SIZE = eINSTANCE.getArrayCreator_Size();

    /**
     * The meta object literal for the '<em><b>Initialization</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_CREATOR__INITIALIZATION = eINSTANCE.getArrayCreator_Initialization();

    /**
     * The meta object literal for the '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmGenericArrayTypeReferenceImpl <em>Jvm Generic Array Type Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JvmGenericArrayTypeReferenceImpl
     * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.JavaJRExpressionPackageImpl#getJvmGenericArrayTypeReference()
     * @generated
     */
    EClass JVM_GENERIC_ARRAY_TYPE_REFERENCE = eINSTANCE.getJvmGenericArrayTypeReference();

    /**
     * The meta object literal for the '<em><b>Component Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JVM_GENERIC_ARRAY_TYPE_REFERENCE__COMPONENT_TYPE = eINSTANCE.getJvmGenericArrayTypeReference_ComponentType();

  }

} //JavaJRExpressionPackage
