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
package com.jaspersoft.studio.data.sql;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.jaspersoft.studio.data.sql.SqlPackage
 * @generated
 */
public interface SqlFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SqlFactory eINSTANCE = com.jaspersoft.studio.data.sql.impl.SqlFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Model</em>'.
   * @generated
   */
  Model createModel();

  /**
   * Returns a new object of class '<em>Select Query</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Select Query</em>'.
   * @generated
   */
  SelectQuery createSelectQuery();

  /**
   * Returns a new object of class '<em>Select Sub Set</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Select Sub Set</em>'.
   * @generated
   */
  SelectSubSet createSelectSubSet();

  /**
   * Returns a new object of class '<em>Select</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Select</em>'.
   * @generated
   */
  Select createSelect();

  /**
   * Returns a new object of class '<em>Or Column</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Or Column</em>'.
   * @generated
   */
  OrColumn createOrColumn();

  /**
   * Returns a new object of class '<em>Column Or Alias</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Column Or Alias</em>'.
   * @generated
   */
  ColumnOrAlias createColumnOrAlias();

  /**
   * Returns a new object of class '<em>Column Full</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Column Full</em>'.
   * @generated
   */
  ColumnFull createColumnFull();

  /**
   * Returns a new object of class '<em>Or Table</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Or Table</em>'.
   * @generated
   */
  OrTable createOrTable();

  /**
   * Returns a new object of class '<em>From Table</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>From Table</em>'.
   * @generated
   */
  FromTable createFromTable();

  /**
   * Returns a new object of class '<em>From Table Join</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>From Table Join</em>'.
   * @generated
   */
  FromTableJoin createFromTableJoin();

  /**
   * Returns a new object of class '<em>Table Or Alias</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Table Or Alias</em>'.
   * @generated
   */
  TableOrAlias createTableOrAlias();

  /**
   * Returns a new object of class '<em>Table Full</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Table Full</em>'.
   * @generated
   */
  TableFull createTableFull();

  /**
   * Returns a new object of class '<em>Db Object Name All</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Db Object Name All</em>'.
   * @generated
   */
  DbObjectNameAll createDbObjectNameAll();

  /**
   * Returns a new object of class '<em>Db Object Name</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Db Object Name</em>'.
   * @generated
   */
  DbObjectName createDbObjectName();

  /**
   * Returns a new object of class '<em>Or Order By Column</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Or Order By Column</em>'.
   * @generated
   */
  OrOrderByColumn createOrOrderByColumn();

  /**
   * Returns a new object of class '<em>Order By Column Full</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Order By Column Full</em>'.
   * @generated
   */
  OrderByColumnFull createOrderByColumnFull();

  /**
   * Returns a new object of class '<em>Or Group By Column</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Or Group By Column</em>'.
   * @generated
   */
  OrGroupByColumn createOrGroupByColumn();

  /**
   * Returns a new object of class '<em>Group By Column Full</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Group By Column Full</em>'.
   * @generated
   */
  GroupByColumnFull createGroupByColumnFull();

  /**
   * Returns a new object of class '<em>Or Expr</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Or Expr</em>'.
   * @generated
   */
  OrExpr createOrExpr();

  /**
   * Returns a new object of class '<em>Full Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Full Expression</em>'.
   * @generated
   */
  FullExpression createFullExpression();

  /**
   * Returns a new object of class '<em>Expr Group</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expr Group</em>'.
   * @generated
   */
  ExprGroup createExprGroup();

  /**
   * Returns a new object of class '<em>XExpr</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>XExpr</em>'.
   * @generated
   */
  XExpr createXExpr();

  /**
   * Returns a new object of class '<em>Prms</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Prms</em>'.
   * @generated
   */
  Prms createPrms();

  /**
   * Returns a new object of class '<em>JR Parameter</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>JR Parameter</em>'.
   * @generated
   */
  JRParameter createJRParameter();

  /**
   * Returns a new object of class '<em>Comparison</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Comparison</em>'.
   * @generated
   */
  Comparison createComparison();

  /**
   * Returns a new object of class '<em>Like</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Like</em>'.
   * @generated
   */
  Like createLike();

  /**
   * Returns a new object of class '<em>Like Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Like Operand</em>'.
   * @generated
   */
  LikeOperand createLikeOperand();

  /**
   * Returns a new object of class '<em>Between</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Between</em>'.
   * @generated
   */
  Between createBetween();

  /**
   * Returns a new object of class '<em>In Oper</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>In Oper</em>'.
   * @generated
   */
  InOper createInOper();

  /**
   * Returns a new object of class '<em>Operand List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operand List</em>'.
   * @generated
   */
  OperandList createOperandList();

  /**
   * Returns a new object of class '<em>Operands</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operands</em>'.
   * @generated
   */
  Operands createOperands();

  /**
   * Returns a new object of class '<em>Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operand</em>'.
   * @generated
   */
  Operand createOperand();

  /**
   * Returns a new object of class '<em>Op Function</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Op Function</em>'.
   * @generated
   */
  OpFunction createOpFunction();

  /**
   * Returns a new object of class '<em>Op Function Arg</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Op Function Arg</em>'.
   * @generated
   */
  OpFunctionArg createOpFunctionArg();

  /**
   * Returns a new object of class '<em>Op Function Arg Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Op Function Arg Operand</em>'.
   * @generated
   */
  OpFunctionArgOperand createOpFunctionArgOperand();

  /**
   * Returns a new object of class '<em>Op Function Cast</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Op Function Cast</em>'.
   * @generated
   */
  OpFunctionCast createOpFunctionCast();

  /**
   * Returns a new object of class '<em>Op Function Arg Agregate</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Op Function Arg Agregate</em>'.
   * @generated
   */
  OpFunctionArgAgregate createOpFunctionArgAgregate();

  /**
   * Returns a new object of class '<em>POperand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>POperand</em>'.
   * @generated
   */
  POperand createPOperand();

  /**
   * Returns a new object of class '<em>Exp Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Exp Operand</em>'.
   * @generated
   */
  ExpOperand createExpOperand();

  /**
   * Returns a new object of class '<em>Column Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Column Operand</em>'.
   * @generated
   */
  ColumnOperand createColumnOperand();

  /**
   * Returns a new object of class '<em>Sub Query Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Sub Query Operand</em>'.
   * @generated
   */
  SubQueryOperand createSubQueryOperand();

  /**
   * Returns a new object of class '<em>Scalar Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Scalar Operand</em>'.
   * @generated
   */
  ScalarOperand createScalarOperand();

  /**
   * Returns a new object of class '<em>SQL Case Operand</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>SQL Case Operand</em>'.
   * @generated
   */
  SQLCaseOperand createSQLCaseOperand();

  /**
   * Returns a new object of class '<em>SQL Case Whens</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>SQL Case Whens</em>'.
   * @generated
   */
  SQLCaseWhens createSQLCaseWhens();

  /**
   * Returns a new object of class '<em>Case When</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Case When</em>'.
   * @generated
   */
  SqlCaseWhen createSqlCaseWhen();

  /**
   * Returns a new object of class '<em>Col</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Col</em>'.
   * @generated
   */
  Col createCol();

  /**
   * Returns a new object of class '<em>tbls</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>tbls</em>'.
   * @generated
   */
  tbls createtbls();

  /**
   * Returns a new object of class '<em>Op List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Op List</em>'.
   * @generated
   */
  OpList createOpList();

  /**
   * Returns a new object of class '<em>Plus</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Plus</em>'.
   * @generated
   */
  Plus createPlus();

  /**
   * Returns a new object of class '<em>Minus</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Minus</em>'.
   * @generated
   */
  Minus createMinus();

  /**
   * Returns a new object of class '<em>Star</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Star</em>'.
   * @generated
   */
  Star createStar();

  /**
   * Returns a new object of class '<em>Div</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Div</em>'.
   * @generated
   */
  Div createDiv();

  /**
   * Returns a new object of class '<em>Concat</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Concat</em>'.
   * @generated
   */
  Concat createConcat();

  /**
   * Returns a new object of class '<em>Op FList</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Op FList</em>'.
   * @generated
   */
  OpFList createOpFList();

  /**
   * Returns a new object of class '<em>When List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>When List</em>'.
   * @generated
   */
  WhenList createWhenList();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  SqlPackage getSqlPackage();

} //SqlFactory
