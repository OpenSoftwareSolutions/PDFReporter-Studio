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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.xtext.common.types.TypesPackage;

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
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionFactory;
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
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JavaJRExpressionPackageImpl extends EPackageImpl implements JavaJRExpressionPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jrExpressionModelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jasperReportsExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass typeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass arrayInitializerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass methodInvocationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass fullMethodNameEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass argumentsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expressionListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jvmParameterizedTypeReferenceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jvmWildcardTypeReferenceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jvmUpperBoundEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jvmLowerBoundEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass testExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass binaryExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass staticFieldEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jrFieldObjEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jrParameterObjEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jrVariableObjEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jrResourceBundleKeyObjEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass methodsExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass intLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass longLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass floatLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass doubleLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass charLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass booleanLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass nullLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass castedExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass arrayCreatorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jvmGenericArrayTypeReferenceEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private JavaJRExpressionPackageImpl()
  {
    super(eNS_URI, JavaJRExpressionFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link JavaJRExpressionPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static JavaJRExpressionPackage init()
  {
    if (isInited) return (JavaJRExpressionPackage)EPackage.Registry.INSTANCE.getEPackage(JavaJRExpressionPackage.eNS_URI);

    // Obtain or create and register package
    JavaJRExpressionPackageImpl theJavaJRExpressionPackage = (JavaJRExpressionPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof JavaJRExpressionPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new JavaJRExpressionPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    TypesPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theJavaJRExpressionPackage.createPackageContents();

    // Initialize created meta-data
    theJavaJRExpressionPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theJavaJRExpressionPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(JavaJRExpressionPackage.eNS_URI, theJavaJRExpressionPackage);
    return theJavaJRExpressionPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRExpressionModel()
  {
    return jrExpressionModelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJRExpressionModel_Expression()
  {
    return (EReference)jrExpressionModelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJasperReportsExpression()
  {
    return jasperReportsExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getType()
  {
    return typeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getType_PrimitiveType()
  {
    return (EAttribute)typeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getType_JvmType()
  {
    return (EReference)typeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getArrayInitializer()
  {
    return arrayInitializerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getArrayInitializer_Initialization()
  {
    return (EReference)arrayInitializerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMethodInvocation()
  {
    return methodInvocationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMethodInvocation_FullyQualifiedMethodName()
  {
    return (EReference)methodInvocationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMethodInvocation_Args()
  {
    return (EReference)methodInvocationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFullMethodName()
  {
    return fullMethodNameEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFullMethodName_PrefixQMN()
  {
    return (EAttribute)fullMethodNameEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFullMethodName_Dots()
  {
    return (EAttribute)fullMethodNameEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFullMethodName_MethodName()
  {
    return (EAttribute)fullMethodNameEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getArguments()
  {
    return argumentsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getArguments_ExprLst()
  {
    return (EReference)argumentsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExpressionList()
  {
    return expressionListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getExpressionList_Expressions()
  {
    return (EReference)expressionListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getExpressionList_Commas()
  {
    return (EAttribute)expressionListEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJvmParameterizedTypeReference()
  {
    return jvmParameterizedTypeReferenceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJvmParameterizedTypeReference_Type()
  {
    return (EReference)jvmParameterizedTypeReferenceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJvmParameterizedTypeReference_Arguments()
  {
    return (EReference)jvmParameterizedTypeReferenceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJvmWildcardTypeReference()
  {
    return jvmWildcardTypeReferenceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJvmWildcardTypeReference_Constraints()
  {
    return (EReference)jvmWildcardTypeReferenceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJvmUpperBound()
  {
    return jvmUpperBoundEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJvmUpperBound_TypeReference()
  {
    return (EReference)jvmUpperBoundEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJvmLowerBound()
  {
    return jvmLowerBoundEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJvmLowerBound_TypeReference()
  {
    return (EReference)jvmLowerBoundEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTestExpression()
  {
    return testExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTestExpression_Condition()
  {
    return (EReference)testExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTestExpression_TrueStatement()
  {
    return (EReference)testExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTestExpression_FalseStatement()
  {
    return (EReference)testExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBinaryExpression()
  {
    return binaryExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBinaryExpression_Left()
  {
    return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBinaryExpression_Op()
  {
    return (EAttribute)binaryExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBinaryExpression_Right()
  {
    return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStaticField()
  {
    return staticFieldEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStaticField_PrefixQMN()
  {
    return (EAttribute)staticFieldEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStaticField_Dots()
  {
    return (EAttribute)staticFieldEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStaticField_FieldName()
  {
    return (EAttribute)staticFieldEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRFieldObj()
  {
    return jrFieldObjEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRFieldObj_BracedIdentifier()
  {
    return (EAttribute)jrFieldObjEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRParameterObj()
  {
    return jrParameterObjEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRParameterObj_BracedIdentifier()
  {
    return (EAttribute)jrParameterObjEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRVariableObj()
  {
    return jrVariableObjEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRVariableObj_BracedIdentifier()
  {
    return (EAttribute)jrVariableObjEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRResourceBundleKeyObj()
  {
    return jrResourceBundleKeyObjEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRResourceBundleKeyObj_BracedIdentifier()
  {
    return (EAttribute)jrResourceBundleKeyObjEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMethodsExpression()
  {
    return methodsExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMethodsExpression_IncludeObjectInstatiation()
  {
    return (EAttribute)methodsExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMethodsExpression_MethodInvocations()
  {
    return (EReference)methodsExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMethodsExpression_ObjectExpression()
  {
    return (EReference)methodsExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMethodsExpression_ArrayIndexes()
  {
    return (EReference)methodsExpressionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIntLiteral()
  {
    return intLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIntLiteral_Value()
  {
    return (EAttribute)intLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLongLiteral()
  {
    return longLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLongLiteral_Value()
  {
    return (EAttribute)longLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFloatLiteral()
  {
    return floatLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFloatLiteral_Value()
  {
    return (EAttribute)floatLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDoubleLiteral()
  {
    return doubleLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDoubleLiteral_Value()
  {
    return (EAttribute)doubleLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCharLiteral()
  {
    return charLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getCharLiteral_Value()
  {
    return (EAttribute)charLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStringLiteral()
  {
    return stringLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringLiteral_Value()
  {
    return (EAttribute)stringLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBooleanLiteral()
  {
    return booleanLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBooleanLiteral_IsTrue()
  {
    return (EAttribute)booleanLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNullLiteral()
  {
    return nullLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCastedExpression()
  {
    return castedExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCastedExpression_CastType()
  {
    return (EReference)castedExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCastedExpression_CastedExpr()
  {
    return (EReference)castedExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getArrayCreator()
  {
    return arrayCreatorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getArrayCreator_Type()
  {
    return (EReference)arrayCreatorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getArrayCreator_Size()
  {
    return (EReference)arrayCreatorEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getArrayCreator_Initialization()
  {
    return (EReference)arrayCreatorEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJvmGenericArrayTypeReference()
  {
    return jvmGenericArrayTypeReferenceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJvmGenericArrayTypeReference_ComponentType()
  {
    return (EReference)jvmGenericArrayTypeReferenceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaJRExpressionFactory getJavaJRExpressionFactory()
  {
    return (JavaJRExpressionFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    jrExpressionModelEClass = createEClass(JR_EXPRESSION_MODEL);
    createEReference(jrExpressionModelEClass, JR_EXPRESSION_MODEL__EXPRESSION);

    jasperReportsExpressionEClass = createEClass(JASPER_REPORTS_EXPRESSION);

    typeEClass = createEClass(TYPE);
    createEAttribute(typeEClass, TYPE__PRIMITIVE_TYPE);
    createEReference(typeEClass, TYPE__JVM_TYPE);

    arrayInitializerEClass = createEClass(ARRAY_INITIALIZER);
    createEReference(arrayInitializerEClass, ARRAY_INITIALIZER__INITIALIZATION);

    methodInvocationEClass = createEClass(METHOD_INVOCATION);
    createEReference(methodInvocationEClass, METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME);
    createEReference(methodInvocationEClass, METHOD_INVOCATION__ARGS);

    fullMethodNameEClass = createEClass(FULL_METHOD_NAME);
    createEAttribute(fullMethodNameEClass, FULL_METHOD_NAME__PREFIX_QMN);
    createEAttribute(fullMethodNameEClass, FULL_METHOD_NAME__DOTS);
    createEAttribute(fullMethodNameEClass, FULL_METHOD_NAME__METHOD_NAME);

    argumentsEClass = createEClass(ARGUMENTS);
    createEReference(argumentsEClass, ARGUMENTS__EXPR_LST);

    expressionListEClass = createEClass(EXPRESSION_LIST);
    createEReference(expressionListEClass, EXPRESSION_LIST__EXPRESSIONS);
    createEAttribute(expressionListEClass, EXPRESSION_LIST__COMMAS);

    jvmParameterizedTypeReferenceEClass = createEClass(JVM_PARAMETERIZED_TYPE_REFERENCE);
    createEReference(jvmParameterizedTypeReferenceEClass, JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE);
    createEReference(jvmParameterizedTypeReferenceEClass, JVM_PARAMETERIZED_TYPE_REFERENCE__ARGUMENTS);

    jvmWildcardTypeReferenceEClass = createEClass(JVM_WILDCARD_TYPE_REFERENCE);
    createEReference(jvmWildcardTypeReferenceEClass, JVM_WILDCARD_TYPE_REFERENCE__CONSTRAINTS);

    jvmUpperBoundEClass = createEClass(JVM_UPPER_BOUND);
    createEReference(jvmUpperBoundEClass, JVM_UPPER_BOUND__TYPE_REFERENCE);

    jvmLowerBoundEClass = createEClass(JVM_LOWER_BOUND);
    createEReference(jvmLowerBoundEClass, JVM_LOWER_BOUND__TYPE_REFERENCE);

    testExpressionEClass = createEClass(TEST_EXPRESSION);
    createEReference(testExpressionEClass, TEST_EXPRESSION__CONDITION);
    createEReference(testExpressionEClass, TEST_EXPRESSION__TRUE_STATEMENT);
    createEReference(testExpressionEClass, TEST_EXPRESSION__FALSE_STATEMENT);

    binaryExpressionEClass = createEClass(BINARY_EXPRESSION);
    createEReference(binaryExpressionEClass, BINARY_EXPRESSION__LEFT);
    createEAttribute(binaryExpressionEClass, BINARY_EXPRESSION__OP);
    createEReference(binaryExpressionEClass, BINARY_EXPRESSION__RIGHT);

    staticFieldEClass = createEClass(STATIC_FIELD);
    createEAttribute(staticFieldEClass, STATIC_FIELD__PREFIX_QMN);
    createEAttribute(staticFieldEClass, STATIC_FIELD__DOTS);
    createEAttribute(staticFieldEClass, STATIC_FIELD__FIELD_NAME);

    jrFieldObjEClass = createEClass(JR_FIELD_OBJ);
    createEAttribute(jrFieldObjEClass, JR_FIELD_OBJ__BRACED_IDENTIFIER);

    jrParameterObjEClass = createEClass(JR_PARAMETER_OBJ);
    createEAttribute(jrParameterObjEClass, JR_PARAMETER_OBJ__BRACED_IDENTIFIER);

    jrVariableObjEClass = createEClass(JR_VARIABLE_OBJ);
    createEAttribute(jrVariableObjEClass, JR_VARIABLE_OBJ__BRACED_IDENTIFIER);

    jrResourceBundleKeyObjEClass = createEClass(JR_RESOURCE_BUNDLE_KEY_OBJ);
    createEAttribute(jrResourceBundleKeyObjEClass, JR_RESOURCE_BUNDLE_KEY_OBJ__BRACED_IDENTIFIER);

    methodsExpressionEClass = createEClass(METHODS_EXPRESSION);
    createEAttribute(methodsExpressionEClass, METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION);
    createEReference(methodsExpressionEClass, METHODS_EXPRESSION__METHOD_INVOCATIONS);
    createEReference(methodsExpressionEClass, METHODS_EXPRESSION__OBJECT_EXPRESSION);
    createEReference(methodsExpressionEClass, METHODS_EXPRESSION__ARRAY_INDEXES);

    intLiteralEClass = createEClass(INT_LITERAL);
    createEAttribute(intLiteralEClass, INT_LITERAL__VALUE);

    longLiteralEClass = createEClass(LONG_LITERAL);
    createEAttribute(longLiteralEClass, LONG_LITERAL__VALUE);

    floatLiteralEClass = createEClass(FLOAT_LITERAL);
    createEAttribute(floatLiteralEClass, FLOAT_LITERAL__VALUE);

    doubleLiteralEClass = createEClass(DOUBLE_LITERAL);
    createEAttribute(doubleLiteralEClass, DOUBLE_LITERAL__VALUE);

    charLiteralEClass = createEClass(CHAR_LITERAL);
    createEAttribute(charLiteralEClass, CHAR_LITERAL__VALUE);

    stringLiteralEClass = createEClass(STRING_LITERAL);
    createEAttribute(stringLiteralEClass, STRING_LITERAL__VALUE);

    booleanLiteralEClass = createEClass(BOOLEAN_LITERAL);
    createEAttribute(booleanLiteralEClass, BOOLEAN_LITERAL__IS_TRUE);

    nullLiteralEClass = createEClass(NULL_LITERAL);

    castedExpressionEClass = createEClass(CASTED_EXPRESSION);
    createEReference(castedExpressionEClass, CASTED_EXPRESSION__CAST_TYPE);
    createEReference(castedExpressionEClass, CASTED_EXPRESSION__CASTED_EXPR);

    arrayCreatorEClass = createEClass(ARRAY_CREATOR);
    createEReference(arrayCreatorEClass, ARRAY_CREATOR__TYPE);
    createEReference(arrayCreatorEClass, ARRAY_CREATOR__SIZE);
    createEReference(arrayCreatorEClass, ARRAY_CREATOR__INITIALIZATION);

    jvmGenericArrayTypeReferenceEClass = createEClass(JVM_GENERIC_ARRAY_TYPE_REFERENCE);
    createEReference(jvmGenericArrayTypeReferenceEClass, JVM_GENERIC_ARRAY_TYPE_REFERENCE__COMPONENT_TYPE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    jvmParameterizedTypeReferenceEClass.getESuperTypes().add(theTypesPackage.getJvmTypeReference());
    jvmWildcardTypeReferenceEClass.getESuperTypes().add(theTypesPackage.getJvmTypeReference());
    testExpressionEClass.getESuperTypes().add(this.getJasperReportsExpression());
    binaryExpressionEClass.getESuperTypes().add(this.getJasperReportsExpression());
    staticFieldEClass.getESuperTypes().add(this.getJasperReportsExpression());
    jrFieldObjEClass.getESuperTypes().add(this.getJasperReportsExpression());
    jrParameterObjEClass.getESuperTypes().add(this.getJasperReportsExpression());
    jrVariableObjEClass.getESuperTypes().add(this.getJasperReportsExpression());
    jrResourceBundleKeyObjEClass.getESuperTypes().add(this.getJasperReportsExpression());
    methodsExpressionEClass.getESuperTypes().add(this.getJasperReportsExpression());
    intLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    longLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    floatLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    doubleLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    charLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    stringLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    booleanLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    nullLiteralEClass.getESuperTypes().add(this.getJasperReportsExpression());
    castedExpressionEClass.getESuperTypes().add(this.getJasperReportsExpression());
    arrayCreatorEClass.getESuperTypes().add(this.getJasperReportsExpression());
    jvmGenericArrayTypeReferenceEClass.getESuperTypes().add(theTypesPackage.getJvmTypeReference());

    // Initialize classes and features; add operations and parameters
    initEClass(jrExpressionModelEClass, JRExpressionModel.class, "JRExpressionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJRExpressionModel_Expression(), this.getJasperReportsExpression(), null, "expression", null, 0, 1, JRExpressionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jasperReportsExpressionEClass, JasperReportsExpression.class, "JasperReportsExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(typeEClass, Type.class, "Type", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getType_PrimitiveType(), ecorePackage.getEBoolean(), "primitiveType", null, 0, 1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getType_JvmType(), theTypesPackage.getJvmTypeReference(), null, "jvmType", null, 0, 1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(arrayInitializerEClass, ArrayInitializer.class, "ArrayInitializer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getArrayInitializer_Initialization(), this.getExpressionList(), null, "initialization", null, 0, 1, ArrayInitializer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(methodInvocationEClass, MethodInvocation.class, "MethodInvocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getMethodInvocation_FullyQualifiedMethodName(), this.getFullMethodName(), null, "fullyQualifiedMethodName", null, 0, 1, MethodInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMethodInvocation_Args(), this.getArguments(), null, "args", null, 0, 1, MethodInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(fullMethodNameEClass, FullMethodName.class, "FullMethodName", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFullMethodName_PrefixQMN(), ecorePackage.getEString(), "prefixQMN", null, 0, -1, FullMethodName.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFullMethodName_Dots(), ecorePackage.getEString(), "dots", null, 0, -1, FullMethodName.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFullMethodName_MethodName(), ecorePackage.getEString(), "methodName", null, 0, 1, FullMethodName.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(argumentsEClass, Arguments.class, "Arguments", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getArguments_ExprLst(), this.getExpressionList(), null, "exprLst", null, 0, 1, Arguments.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(expressionListEClass, ExpressionList.class, "ExpressionList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getExpressionList_Expressions(), this.getJasperReportsExpression(), null, "expressions", null, 0, -1, ExpressionList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getExpressionList_Commas(), ecorePackage.getEString(), "commas", null, 0, -1, ExpressionList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jvmParameterizedTypeReferenceEClass, JvmParameterizedTypeReference.class, "JvmParameterizedTypeReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJvmParameterizedTypeReference_Type(), theTypesPackage.getJvmType(), null, "type", null, 0, 1, JvmParameterizedTypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getJvmParameterizedTypeReference_Arguments(), theTypesPackage.getJvmTypeReference(), null, "arguments", null, 0, -1, JvmParameterizedTypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jvmWildcardTypeReferenceEClass, JvmWildcardTypeReference.class, "JvmWildcardTypeReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJvmWildcardTypeReference_Constraints(), ecorePackage.getEObject(), null, "constraints", null, 0, -1, JvmWildcardTypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jvmUpperBoundEClass, JvmUpperBound.class, "JvmUpperBound", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJvmUpperBound_TypeReference(), theTypesPackage.getJvmTypeReference(), null, "typeReference", null, 0, 1, JvmUpperBound.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jvmLowerBoundEClass, JvmLowerBound.class, "JvmLowerBound", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJvmLowerBound_TypeReference(), theTypesPackage.getJvmTypeReference(), null, "typeReference", null, 0, 1, JvmLowerBound.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(testExpressionEClass, TestExpression.class, "TestExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTestExpression_Condition(), this.getJasperReportsExpression(), null, "condition", null, 0, 1, TestExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTestExpression_TrueStatement(), this.getJasperReportsExpression(), null, "trueStatement", null, 0, 1, TestExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTestExpression_FalseStatement(), this.getJasperReportsExpression(), null, "falseStatement", null, 0, 1, TestExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(binaryExpressionEClass, BinaryExpression.class, "BinaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getBinaryExpression_Left(), this.getJasperReportsExpression(), null, "left", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBinaryExpression_Op(), ecorePackage.getEString(), "op", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getBinaryExpression_Right(), ecorePackage.getEObject(), null, "right", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(staticFieldEClass, StaticField.class, "StaticField", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStaticField_PrefixQMN(), ecorePackage.getEString(), "prefixQMN", null, 0, -1, StaticField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStaticField_Dots(), ecorePackage.getEString(), "dots", null, 0, -1, StaticField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStaticField_FieldName(), ecorePackage.getEString(), "fieldName", null, 0, 1, StaticField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jrFieldObjEClass, JRFieldObj.class, "JRFieldObj", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJRFieldObj_BracedIdentifier(), ecorePackage.getEString(), "bracedIdentifier", null, 0, 1, JRFieldObj.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jrParameterObjEClass, JRParameterObj.class, "JRParameterObj", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJRParameterObj_BracedIdentifier(), ecorePackage.getEString(), "bracedIdentifier", null, 0, 1, JRParameterObj.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jrVariableObjEClass, JRVariableObj.class, "JRVariableObj", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJRVariableObj_BracedIdentifier(), ecorePackage.getEString(), "bracedIdentifier", null, 0, 1, JRVariableObj.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jrResourceBundleKeyObjEClass, JRResourceBundleKeyObj.class, "JRResourceBundleKeyObj", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJRResourceBundleKeyObj_BracedIdentifier(), ecorePackage.getEString(), "bracedIdentifier", null, 0, 1, JRResourceBundleKeyObj.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(methodsExpressionEClass, MethodsExpression.class, "MethodsExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMethodsExpression_IncludeObjectInstatiation(), ecorePackage.getEBoolean(), "includeObjectInstatiation", null, 0, 1, MethodsExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMethodsExpression_MethodInvocations(), this.getMethodInvocation(), null, "methodInvocations", null, 0, -1, MethodsExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMethodsExpression_ObjectExpression(), this.getJasperReportsExpression(), null, "objectExpression", null, 0, 1, MethodsExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMethodsExpression_ArrayIndexes(), this.getJasperReportsExpression(), null, "arrayIndexes", null, 0, -1, MethodsExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(intLiteralEClass, IntLiteral.class, "IntLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIntLiteral_Value(), ecorePackage.getEInt(), "value", null, 0, 1, IntLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(longLiteralEClass, LongLiteral.class, "LongLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLongLiteral_Value(), ecorePackage.getELong(), "value", null, 0, 1, LongLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(floatLiteralEClass, FloatLiteral.class, "FloatLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFloatLiteral_Value(), ecorePackage.getEFloat(), "value", null, 0, 1, FloatLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(doubleLiteralEClass, DoubleLiteral.class, "DoubleLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDoubleLiteral_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, DoubleLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(charLiteralEClass, CharLiteral.class, "CharLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getCharLiteral_Value(), ecorePackage.getEChar(), "value", null, 0, 1, CharLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringLiteralEClass, StringLiteral.class, "StringLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStringLiteral_Value(), ecorePackage.getEString(), "value", null, 0, 1, StringLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(booleanLiteralEClass, BooleanLiteral.class, "BooleanLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBooleanLiteral_IsTrue(), ecorePackage.getEBoolean(), "isTrue", null, 0, 1, BooleanLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(nullLiteralEClass, NullLiteral.class, "NullLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(castedExpressionEClass, CastedExpression.class, "CastedExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCastedExpression_CastType(), this.getType(), null, "castType", null, 0, 1, CastedExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCastedExpression_CastedExpr(), this.getJasperReportsExpression(), null, "castedExpr", null, 0, 1, CastedExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(arrayCreatorEClass, ArrayCreator.class, "ArrayCreator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getArrayCreator_Type(), this.getType(), null, "type", null, 0, 1, ArrayCreator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getArrayCreator_Size(), this.getJasperReportsExpression(), null, "size", null, 0, -1, ArrayCreator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getArrayCreator_Initialization(), this.getArrayInitializer(), null, "initialization", null, 0, 1, ArrayCreator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jvmGenericArrayTypeReferenceEClass, JvmGenericArrayTypeReference.class, "JvmGenericArrayTypeReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getJvmGenericArrayTypeReference_ComponentType(), this.getJvmParameterizedTypeReference(), null, "componentType", null, 0, 1, JvmGenericArrayTypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //JavaJRExpressionPackageImpl
