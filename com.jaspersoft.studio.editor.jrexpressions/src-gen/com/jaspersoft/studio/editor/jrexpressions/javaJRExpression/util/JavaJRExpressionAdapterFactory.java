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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;

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
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage
 * @generated
 */
public class JavaJRExpressionAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static JavaJRExpressionPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaJRExpressionAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = JavaJRExpressionPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JavaJRExpressionSwitch<Adapter> modelSwitch =
    new JavaJRExpressionSwitch<Adapter>()
    {
      @Override
      public Adapter caseJRExpressionModel(JRExpressionModel object)
      {
        return createJRExpressionModelAdapter();
      }
      @Override
      public Adapter caseJasperReportsExpression(JasperReportsExpression object)
      {
        return createJasperReportsExpressionAdapter();
      }
      @Override
      public Adapter caseType(Type object)
      {
        return createTypeAdapter();
      }
      @Override
      public Adapter caseArrayInitializer(ArrayInitializer object)
      {
        return createArrayInitializerAdapter();
      }
      @Override
      public Adapter caseMethodInvocation(MethodInvocation object)
      {
        return createMethodInvocationAdapter();
      }
      @Override
      public Adapter caseFullMethodName(FullMethodName object)
      {
        return createFullMethodNameAdapter();
      }
      @Override
      public Adapter caseArguments(Arguments object)
      {
        return createArgumentsAdapter();
      }
      @Override
      public Adapter caseExpressionList(ExpressionList object)
      {
        return createExpressionListAdapter();
      }
      @Override
      public Adapter caseJvmParameterizedTypeReference(JvmParameterizedTypeReference object)
      {
        return createJvmParameterizedTypeReferenceAdapter();
      }
      @Override
      public Adapter caseJvmWildcardTypeReference(JvmWildcardTypeReference object)
      {
        return createJvmWildcardTypeReferenceAdapter();
      }
      @Override
      public Adapter caseJvmUpperBound(JvmUpperBound object)
      {
        return createJvmUpperBoundAdapter();
      }
      @Override
      public Adapter caseJvmLowerBound(JvmLowerBound object)
      {
        return createJvmLowerBoundAdapter();
      }
      @Override
      public Adapter caseTestExpression(TestExpression object)
      {
        return createTestExpressionAdapter();
      }
      @Override
      public Adapter caseBinaryExpression(BinaryExpression object)
      {
        return createBinaryExpressionAdapter();
      }
      @Override
      public Adapter caseStaticField(StaticField object)
      {
        return createStaticFieldAdapter();
      }
      @Override
      public Adapter caseJRFieldObj(JRFieldObj object)
      {
        return createJRFieldObjAdapter();
      }
      @Override
      public Adapter caseJRParameterObj(JRParameterObj object)
      {
        return createJRParameterObjAdapter();
      }
      @Override
      public Adapter caseJRVariableObj(JRVariableObj object)
      {
        return createJRVariableObjAdapter();
      }
      @Override
      public Adapter caseJRResourceBundleKeyObj(JRResourceBundleKeyObj object)
      {
        return createJRResourceBundleKeyObjAdapter();
      }
      @Override
      public Adapter caseMethodsExpression(MethodsExpression object)
      {
        return createMethodsExpressionAdapter();
      }
      @Override
      public Adapter caseIntLiteral(IntLiteral object)
      {
        return createIntLiteralAdapter();
      }
      @Override
      public Adapter caseLongLiteral(LongLiteral object)
      {
        return createLongLiteralAdapter();
      }
      @Override
      public Adapter caseFloatLiteral(FloatLiteral object)
      {
        return createFloatLiteralAdapter();
      }
      @Override
      public Adapter caseDoubleLiteral(DoubleLiteral object)
      {
        return createDoubleLiteralAdapter();
      }
      @Override
      public Adapter caseCharLiteral(CharLiteral object)
      {
        return createCharLiteralAdapter();
      }
      @Override
      public Adapter caseStringLiteral(StringLiteral object)
      {
        return createStringLiteralAdapter();
      }
      @Override
      public Adapter caseBooleanLiteral(BooleanLiteral object)
      {
        return createBooleanLiteralAdapter();
      }
      @Override
      public Adapter caseNullLiteral(NullLiteral object)
      {
        return createNullLiteralAdapter();
      }
      @Override
      public Adapter caseCastedExpression(CastedExpression object)
      {
        return createCastedExpressionAdapter();
      }
      @Override
      public Adapter caseArrayCreator(ArrayCreator object)
      {
        return createArrayCreatorAdapter();
      }
      @Override
      public Adapter caseJvmGenericArrayTypeReference(JvmGenericArrayTypeReference object)
      {
        return createJvmGenericArrayTypeReferenceAdapter();
      }
      @Override
      public Adapter caseJvmTypeReference(JvmTypeReference object)
      {
        return createJvmTypeReferenceAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRExpressionModel <em>JR Expression Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRExpressionModel
   * @generated
   */
  public Adapter createJRExpressionModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression <em>Jasper Reports Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression
   * @generated
   */
  public Adapter createJasperReportsExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Type
   * @generated
   */
  public Adapter createTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayInitializer <em>Array Initializer</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayInitializer
   * @generated
   */
  public Adapter createArrayInitializerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation <em>Method Invocation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation
   * @generated
   */
  public Adapter createMethodInvocationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName <em>Full Method Name</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName
   * @generated
   */
  public Adapter createFullMethodNameAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments <em>Arguments</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments
   * @generated
   */
  public Adapter createArgumentsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList <em>Expression List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList
   * @generated
   */
  public Adapter createExpressionListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference <em>Jvm Parameterized Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmParameterizedTypeReference
   * @generated
   */
  public Adapter createJvmParameterizedTypeReferenceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmWildcardTypeReference <em>Jvm Wildcard Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmWildcardTypeReference
   * @generated
   */
  public Adapter createJvmWildcardTypeReferenceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmUpperBound <em>Jvm Upper Bound</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmUpperBound
   * @generated
   */
  public Adapter createJvmUpperBoundAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmLowerBound <em>Jvm Lower Bound</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmLowerBound
   * @generated
   */
  public Adapter createJvmLowerBoundAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression <em>Test Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.TestExpression
   * @generated
   */
  public Adapter createTestExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression <em>Binary Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BinaryExpression
   * @generated
   */
  public Adapter createBinaryExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField <em>Static Field</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StaticField
   * @generated
   */
  public Adapter createStaticFieldAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj <em>JR Field Obj</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj
   * @generated
   */
  public Adapter createJRFieldObjAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj <em>JR Parameter Obj</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj
   * @generated
   */
  public Adapter createJRParameterObjAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj <em>JR Variable Obj</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj
   * @generated
   */
  public Adapter createJRVariableObjAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj <em>JR Resource Bundle Key Obj</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj
   * @generated
   */
  public Adapter createJRResourceBundleKeyObjAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression <em>Methods Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression
   * @generated
   */
  public Adapter createMethodsExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral <em>Int Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral
   * @generated
   */
  public Adapter createIntLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral <em>Long Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral
   * @generated
   */
  public Adapter createLongLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral <em>Float Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral
   * @generated
   */
  public Adapter createFloatLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral <em>Double Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral
   * @generated
   */
  public Adapter createDoubleLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CharLiteral <em>Char Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CharLiteral
   * @generated
   */
  public Adapter createCharLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral <em>String Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral
   * @generated
   */
  public Adapter createStringLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BooleanLiteral <em>Boolean Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.BooleanLiteral
   * @generated
   */
  public Adapter createBooleanLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.NullLiteral <em>Null Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.NullLiteral
   * @generated
   */
  public Adapter createNullLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression <em>Casted Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.CastedExpression
   * @generated
   */
  public Adapter createCastedExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator <em>Array Creator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ArrayCreator
   * @generated
   */
  public Adapter createArrayCreatorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmGenericArrayTypeReference <em>Jvm Generic Array Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JvmGenericArrayTypeReference
   * @generated
   */
  public Adapter createJvmGenericArrayTypeReferenceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.common.types.JvmTypeReference <em>Jvm Type Reference</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.common.types.JvmTypeReference
   * @generated
   */
  public Adapter createJvmTypeReferenceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //JavaJRExpressionAdapterFactory
