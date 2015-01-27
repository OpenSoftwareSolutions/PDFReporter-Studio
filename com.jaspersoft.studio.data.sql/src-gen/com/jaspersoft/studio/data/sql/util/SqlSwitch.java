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
package com.jaspersoft.studio.data.sql.util;

import com.jaspersoft.studio.data.sql.*;
import com.jaspersoft.studio.data.sql.compatibility.Switch;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
 

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
 * @see com.jaspersoft.studio.data.sql.SqlPackage
 * @generated
 */
public class SqlSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static SqlPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SqlSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = SqlPackage.eINSTANCE;
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
      case SqlPackage.MODEL:
      {
        Model model = (Model)theEObject;
        T result = caseModel(model);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SELECT_QUERY:
      {
        SelectQuery selectQuery = (SelectQuery)theEObject;
        T result = caseSelectQuery(selectQuery);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SELECT_SUB_SET:
      {
        SelectSubSet selectSubSet = (SelectSubSet)theEObject;
        T result = caseSelectSubSet(selectSubSet);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SELECT:
      {
        Select select = (Select)theEObject;
        T result = caseSelect(select);
        if (result == null) result = caseSelectQuery(select);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OR_COLUMN:
      {
        OrColumn orColumn = (OrColumn)theEObject;
        T result = caseOrColumn(orColumn);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.COLUMN_OR_ALIAS:
      {
        ColumnOrAlias columnOrAlias = (ColumnOrAlias)theEObject;
        T result = caseColumnOrAlias(columnOrAlias);
        if (result == null) result = caseOrColumn(columnOrAlias);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.COLUMN_FULL:
      {
        ColumnFull columnFull = (ColumnFull)theEObject;
        T result = caseColumnFull(columnFull);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OR_TABLE:
      {
        OrTable orTable = (OrTable)theEObject;
        T result = caseOrTable(orTable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.FROM_TABLE:
      {
        FromTable fromTable = (FromTable)theEObject;
        T result = caseFromTable(fromTable);
        if (result == null) result = caseOrTable(fromTable);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.FROM_TABLE_JOIN:
      {
        FromTableJoin fromTableJoin = (FromTableJoin)theEObject;
        T result = caseFromTableJoin(fromTableJoin);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.TABLE_OR_ALIAS:
      {
        TableOrAlias tableOrAlias = (TableOrAlias)theEObject;
        T result = caseTableOrAlias(tableOrAlias);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.TABLE_FULL:
      {
        TableFull tableFull = (TableFull)theEObject;
        T result = caseTableFull(tableFull);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.DB_OBJECT_NAME_ALL:
      {
        DbObjectNameAll dbObjectNameAll = (DbObjectNameAll)theEObject;
        T result = caseDbObjectNameAll(dbObjectNameAll);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.DB_OBJECT_NAME:
      {
        DbObjectName dbObjectName = (DbObjectName)theEObject;
        T result = caseDbObjectName(dbObjectName);
        if (result == null) result = caseColumnFull(dbObjectName);
        if (result == null) result = caseTableFull(dbObjectName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OR_ORDER_BY_COLUMN:
      {
        OrOrderByColumn orOrderByColumn = (OrOrderByColumn)theEObject;
        T result = caseOrOrderByColumn(orOrderByColumn);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.ORDER_BY_COLUMN_FULL:
      {
        OrderByColumnFull orderByColumnFull = (OrderByColumnFull)theEObject;
        T result = caseOrderByColumnFull(orderByColumnFull);
        if (result == null) result = caseOrOrderByColumn(orderByColumnFull);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OR_GROUP_BY_COLUMN:
      {
        OrGroupByColumn orGroupByColumn = (OrGroupByColumn)theEObject;
        T result = caseOrGroupByColumn(orGroupByColumn);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.GROUP_BY_COLUMN_FULL:
      {
        GroupByColumnFull groupByColumnFull = (GroupByColumnFull)theEObject;
        T result = caseGroupByColumnFull(groupByColumnFull);
        if (result == null) result = caseOrGroupByColumn(groupByColumnFull);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OR_EXPR:
      {
        OrExpr orExpr = (OrExpr)theEObject;
        T result = caseOrExpr(orExpr);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.FULL_EXPRESSION:
      {
        FullExpression fullExpression = (FullExpression)theEObject;
        T result = caseFullExpression(fullExpression);
        if (result == null) result = caseOrExpr(fullExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.EXPR_GROUP:
      {
        ExprGroup exprGroup = (ExprGroup)theEObject;
        T result = caseExprGroup(exprGroup);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.XEXPR:
      {
        XExpr xExpr = (XExpr)theEObject;
        T result = caseXExpr(xExpr);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.PRMS:
      {
        Prms prms = (Prms)theEObject;
        T result = casePrms(prms);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.JR_PARAMETER:
      {
        JRParameter jrParameter = (JRParameter)theEObject;
        T result = caseJRParameter(jrParameter);
        if (result == null) result = casePrms(jrParameter);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.COMPARISON:
      {
        Comparison comparison = (Comparison)theEObject;
        T result = caseComparison(comparison);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.LIKE:
      {
        Like like = (Like)theEObject;
        T result = caseLike(like);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.LIKE_OPERAND:
      {
        LikeOperand likeOperand = (LikeOperand)theEObject;
        T result = caseLikeOperand(likeOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.BETWEEN:
      {
        Between between = (Between)theEObject;
        T result = caseBetween(between);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.IN_OPER:
      {
        InOper inOper = (InOper)theEObject;
        T result = caseInOper(inOper);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OPERAND_LIST:
      {
        OperandList operandList = (OperandList)theEObject;
        T result = caseOperandList(operandList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OPERANDS:
      {
        Operands operands = (Operands)theEObject;
        T result = caseOperands(operands);
        if (result == null) result = caseOpFunctionArgAgregate(operands);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OPERAND:
      {
        Operand operand = (Operand)theEObject;
        T result = caseOperand(operand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OP_FUNCTION:
      {
        OpFunction opFunction = (OpFunction)theEObject;
        T result = caseOpFunction(opFunction);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OP_FUNCTION_ARG:
      {
        OpFunctionArg opFunctionArg = (OpFunctionArg)theEObject;
        T result = caseOpFunctionArg(opFunctionArg);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OP_FUNCTION_ARG_OPERAND:
      {
        OpFunctionArgOperand opFunctionArgOperand = (OpFunctionArgOperand)theEObject;
        T result = caseOpFunctionArgOperand(opFunctionArgOperand);
        if (result == null) result = caseOpFunctionArg(opFunctionArgOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OP_FUNCTION_CAST:
      {
        OpFunctionCast opFunctionCast = (OpFunctionCast)theEObject;
        T result = caseOpFunctionCast(opFunctionCast);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OP_FUNCTION_ARG_AGREGATE:
      {
        OpFunctionArgAgregate opFunctionArgAgregate = (OpFunctionArgAgregate)theEObject;
        T result = caseOpFunctionArgAgregate(opFunctionArgAgregate);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.POPERAND:
      {
        POperand pOperand = (POperand)theEObject;
        T result = casePOperand(pOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.EXP_OPERAND:
      {
        ExpOperand expOperand = (ExpOperand)theEObject;
        T result = caseExpOperand(expOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.COLUMN_OPERAND:
      {
        ColumnOperand columnOperand = (ColumnOperand)theEObject;
        T result = caseColumnOperand(columnOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SUB_QUERY_OPERAND:
      {
        SubQueryOperand subQueryOperand = (SubQueryOperand)theEObject;
        T result = caseSubQueryOperand(subQueryOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SCALAR_OPERAND:
      {
        ScalarOperand scalarOperand = (ScalarOperand)theEObject;
        T result = caseScalarOperand(scalarOperand);
        if (result == null) result = caseOperandList(scalarOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SQL_CASE_OPERAND:
      {
        SQLCaseOperand sqlCaseOperand = (SQLCaseOperand)theEObject;
        T result = caseSQLCaseOperand(sqlCaseOperand);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SQL_CASE_WHENS:
      {
        SQLCaseWhens sqlCaseWhens = (SQLCaseWhens)theEObject;
        T result = caseSQLCaseWhens(sqlCaseWhens);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.SQL_CASE_WHEN:
      {
        SqlCaseWhen sqlCaseWhen = (SqlCaseWhen)theEObject;
        T result = caseSqlCaseWhen(sqlCaseWhen);
        if (result == null) result = caseSQLCaseWhens(sqlCaseWhen);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.COL:
      {
        Col col = (Col)theEObject;
        T result = caseCol(col);
        if (result == null) result = caseColumnFull(col);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.TBLS:
      {
        tbls tbls = (tbls)theEObject;
        T result = casetbls(tbls);
        if (result == null) result = caseTableFull(tbls);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OP_LIST:
      {
        OpList opList = (OpList)theEObject;
        T result = caseOpList(opList);
        if (result == null) result = caseOperandList(opList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.PLUS:
      {
        Plus plus = (Plus)theEObject;
        T result = casePlus(plus);
        if (result == null) result = caseOperands(plus);
        if (result == null) result = caseOpFunctionArgAgregate(plus);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.MINUS:
      {
        Minus minus = (Minus)theEObject;
        T result = caseMinus(minus);
        if (result == null) result = caseOperands(minus);
        if (result == null) result = caseOpFunctionArgAgregate(minus);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.STAR:
      {
        Star star = (Star)theEObject;
        T result = caseStar(star);
        if (result == null) result = caseOperands(star);
        if (result == null) result = caseOpFunctionArgAgregate(star);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.DIV:
      {
        Div div = (Div)theEObject;
        T result = caseDiv(div);
        if (result == null) result = caseOperands(div);
        if (result == null) result = caseOpFunctionArgAgregate(div);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.CONCAT:
      {
        Concat concat = (Concat)theEObject;
        T result = caseConcat(concat);
        if (result == null) result = caseOperands(concat);
        if (result == null) result = caseOpFunctionArgAgregate(concat);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.OP_FLIST:
      {
        OpFList opFList = (OpFList)theEObject;
        T result = caseOpFList(opFList);
        if (result == null) result = caseOpFunctionArg(opFList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case SqlPackage.WHEN_LIST:
      {
        WhenList whenList = (WhenList)theEObject;
        T result = caseWhenList(whenList);
        if (result == null) result = caseSQLCaseWhens(whenList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModel(Model object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Select Query</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Select Query</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSelectQuery(SelectQuery object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Select Sub Set</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Select Sub Set</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSelectSubSet(SelectSubSet object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Select</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Select</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSelect(Select object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Or Column</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Or Column</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrColumn(OrColumn object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Column Or Alias</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Column Or Alias</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseColumnOrAlias(ColumnOrAlias object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Column Full</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Column Full</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseColumnFull(ColumnFull object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Or Table</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Or Table</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrTable(OrTable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>From Table</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>From Table</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFromTable(FromTable object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>From Table Join</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>From Table Join</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFromTableJoin(FromTableJoin object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Table Or Alias</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Table Or Alias</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTableOrAlias(TableOrAlias object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Table Full</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Table Full</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTableFull(TableFull object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Db Object Name All</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Db Object Name All</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDbObjectNameAll(DbObjectNameAll object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Db Object Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Db Object Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDbObjectName(DbObjectName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Or Order By Column</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Or Order By Column</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrOrderByColumn(OrOrderByColumn object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Order By Column Full</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Order By Column Full</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrderByColumnFull(OrderByColumnFull object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Or Group By Column</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Or Group By Column</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrGroupByColumn(OrGroupByColumn object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Group By Column Full</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Group By Column Full</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseGroupByColumnFull(GroupByColumnFull object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Or Expr</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Or Expr</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOrExpr(OrExpr object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Full Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Full Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseFullExpression(FullExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Expr Group</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Expr Group</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExprGroup(ExprGroup object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>XExpr</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>XExpr</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseXExpr(XExpr object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Prms</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Prms</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePrms(Prms object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>JR Parameter</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>JR Parameter</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseJRParameter(JRParameter object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Comparison</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Comparison</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseComparison(Comparison object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Like</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Like</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLike(Like object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Like Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Like Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLikeOperand(LikeOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Between</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Between</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseBetween(Between object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>In Oper</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>In Oper</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInOper(InOper object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operand List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operand List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperandList(OperandList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operands</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operands</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperands(Operands object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOperand(Operand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Op Function</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Op Function</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOpFunction(OpFunction object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Op Function Arg</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Op Function Arg</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOpFunctionArg(OpFunctionArg object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Op Function Arg Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Op Function Arg Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOpFunctionArgOperand(OpFunctionArgOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Op Function Cast</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Op Function Cast</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOpFunctionCast(OpFunctionCast object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Op Function Arg Agregate</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Op Function Arg Agregate</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOpFunctionArgAgregate(OpFunctionArgAgregate object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>POperand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>POperand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePOperand(POperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Exp Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Exp Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseExpOperand(ExpOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Column Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Column Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseColumnOperand(ColumnOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Sub Query Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Sub Query Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSubQueryOperand(SubQueryOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Scalar Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Scalar Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseScalarOperand(ScalarOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SQL Case Operand</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SQL Case Operand</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSQLCaseOperand(SQLCaseOperand object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>SQL Case Whens</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>SQL Case Whens</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSQLCaseWhens(SQLCaseWhens object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Case When</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Case When</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSqlCaseWhen(SqlCaseWhen object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Col</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Col</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCol(Col object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>tbls</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>tbls</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casetbls(tbls object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Op List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Op List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOpList(OpList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Plus</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Plus</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePlus(Plus object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Minus</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Minus</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMinus(Minus object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Star</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Star</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStar(Star object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Div</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Div</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDiv(Div object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Concat</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Concat</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseConcat(Concat object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Op FList</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Op FList</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOpFList(OpFList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>When List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>When List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWhenList(WhenList object)
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

} //SqlSwitch
