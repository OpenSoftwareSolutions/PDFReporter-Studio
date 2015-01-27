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
package com.jaspersoft.studio.data.sql.impl;

import com.jaspersoft.studio.data.sql.Between;
import com.jaspersoft.studio.data.sql.Col;
import com.jaspersoft.studio.data.sql.ColumnFull;
import com.jaspersoft.studio.data.sql.ColumnOperand;
import com.jaspersoft.studio.data.sql.ColumnOrAlias;
import com.jaspersoft.studio.data.sql.Comparison;
import com.jaspersoft.studio.data.sql.Concat;
import com.jaspersoft.studio.data.sql.DbObjectName;
import com.jaspersoft.studio.data.sql.DbObjectNameAll;
import com.jaspersoft.studio.data.sql.Div;
import com.jaspersoft.studio.data.sql.ExpOperand;
import com.jaspersoft.studio.data.sql.ExprGroup;
import com.jaspersoft.studio.data.sql.FromTable;
import com.jaspersoft.studio.data.sql.FromTableJoin;
import com.jaspersoft.studio.data.sql.FullExpression;
import com.jaspersoft.studio.data.sql.GroupByColumnFull;
import com.jaspersoft.studio.data.sql.InOper;
import com.jaspersoft.studio.data.sql.JRParameter;
import com.jaspersoft.studio.data.sql.Like;
import com.jaspersoft.studio.data.sql.LikeOperand;
import com.jaspersoft.studio.data.sql.Minus;
import com.jaspersoft.studio.data.sql.Model;
import com.jaspersoft.studio.data.sql.OpFList;
import com.jaspersoft.studio.data.sql.OpFunction;
import com.jaspersoft.studio.data.sql.OpFunctionArg;
import com.jaspersoft.studio.data.sql.OpFunctionArgAgregate;
import com.jaspersoft.studio.data.sql.OpFunctionArgOperand;
import com.jaspersoft.studio.data.sql.OpFunctionCast;
import com.jaspersoft.studio.data.sql.OpList;
import com.jaspersoft.studio.data.sql.Operand;
import com.jaspersoft.studio.data.sql.OperandList;
import com.jaspersoft.studio.data.sql.Operands;
import com.jaspersoft.studio.data.sql.OrColumn;
import com.jaspersoft.studio.data.sql.OrExpr;
import com.jaspersoft.studio.data.sql.OrGroupByColumn;
import com.jaspersoft.studio.data.sql.OrOrderByColumn;
import com.jaspersoft.studio.data.sql.OrTable;
import com.jaspersoft.studio.data.sql.OrderByColumnFull;
import com.jaspersoft.studio.data.sql.POperand;
import com.jaspersoft.studio.data.sql.Plus;
import com.jaspersoft.studio.data.sql.Prms;
import com.jaspersoft.studio.data.sql.SQLCaseOperand;
import com.jaspersoft.studio.data.sql.SQLCaseWhens;
import com.jaspersoft.studio.data.sql.ScalarOperand;
import com.jaspersoft.studio.data.sql.Select;
import com.jaspersoft.studio.data.sql.SelectQuery;
import com.jaspersoft.studio.data.sql.SelectSubSet;
import com.jaspersoft.studio.data.sql.SqlCaseWhen;
import com.jaspersoft.studio.data.sql.SqlFactory;
import com.jaspersoft.studio.data.sql.SqlPackage;
import com.jaspersoft.studio.data.sql.Star;
import com.jaspersoft.studio.data.sql.SubQueryOperand;
import com.jaspersoft.studio.data.sql.TableFull;
import com.jaspersoft.studio.data.sql.TableOrAlias;
import com.jaspersoft.studio.data.sql.WhenList;
import com.jaspersoft.studio.data.sql.XExpr;
import com.jaspersoft.studio.data.sql.XFunction;
import com.jaspersoft.studio.data.sql.tbls;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SqlPackageImpl extends EPackageImpl implements SqlPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass selectQueryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass selectSubSetEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass selectEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass orColumnEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass columnOrAliasEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass columnFullEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass orTableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass fromTableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass fromTableJoinEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tableOrAliasEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tableFullEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dbObjectNameAllEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dbObjectNameEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass orOrderByColumnEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass orderByColumnFullEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass orGroupByColumnEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass groupByColumnFullEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass orExprEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass fullExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass exprGroupEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass xExprEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass prmsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jrParameterEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass comparisonEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass likeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass likeOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass betweenEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass inOperEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass operandListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass operandsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass operandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass opFunctionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass opFunctionArgEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass opFunctionArgOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass opFunctionCastEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass opFunctionArgAgregateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass pOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass columnOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass subQueryOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass scalarOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sqlCaseOperandEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sqlCaseWhensEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sqlCaseWhenEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass colEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tblsEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass opListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass plusEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass minusEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass starEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass divEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass concatEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass opFListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass whenListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum xFunctionEEnum = null;

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
   * @see com.jaspersoft.studio.data.sql.SqlPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SqlPackageImpl()
  {
    super(eNS_URI, SqlFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link SqlPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static SqlPackage init()
  {
    if (isInited) return (SqlPackage)EPackage.Registry.INSTANCE.getEPackage(SqlPackage.eNS_URI);

    // Obtain or create and register package
    SqlPackageImpl theSqlPackage = (SqlPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SqlPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SqlPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theSqlPackage.createPackageContents();

    // Initialize created meta-data
    theSqlPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theSqlPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(SqlPackage.eNS_URI, theSqlPackage);
    return theSqlPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getModel()
  {
    return modelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_Query()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModel_OrderByEntry()
  {
    return (EReference)modelEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSelectQuery()
  {
    return selectQueryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSelectSubSet()
  {
    return selectSubSetEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSelectSubSet_Op()
  {
    return (EAttribute)selectSubSetEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSelectSubSet_All()
  {
    return (EAttribute)selectSubSetEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelectSubSet_Query()
  {
    return (EReference)selectSubSetEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSelect()
  {
    return selectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelect_Op()
  {
    return (EReference)selectEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSelect_Select()
  {
    return (EAttribute)selectEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelect_Cols()
  {
    return (EReference)selectEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelect_Tbl()
  {
    return (EReference)selectEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelect_WhereExpression()
  {
    return (EReference)selectEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelect_GroupByEntry()
  {
    return (EReference)selectEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSelect_HavingEntry()
  {
    return (EReference)selectEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOrColumn()
  {
    return orColumnEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOrColumn_Entries()
  {
    return (EReference)orColumnEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getColumnOrAlias()
  {
    return columnOrAliasEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getColumnOrAlias_Ce()
  {
    return (EReference)columnOrAliasEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getColumnOrAlias_Alias()
  {
    return (EAttribute)columnOrAliasEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getColumnOrAlias_ColAlias()
  {
    return (EReference)columnOrAliasEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getColumnOrAlias_AllCols()
  {
    return (EAttribute)columnOrAliasEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getColumnOrAlias_DbAllCols()
  {
    return (EReference)columnOrAliasEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getColumnFull()
  {
    return columnFullEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOrTable()
  {
    return orTableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOrTable_Entries()
  {
    return (EReference)orTableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFromTable()
  {
    return fromTableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFromTable_Table()
  {
    return (EReference)fromTableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFromTable_Fjoin()
  {
    return (EReference)fromTableEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFromTableJoin()
  {
    return fromTableJoinEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFromTableJoin_Join()
  {
    return (EAttribute)fromTableJoinEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFromTableJoin_OnTable()
  {
    return (EReference)fromTableJoinEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFromTableJoin_JoinExpr()
  {
    return (EReference)fromTableJoinEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTableOrAlias()
  {
    return tableOrAliasEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTableOrAlias_Tfull()
  {
    return (EReference)tableOrAliasEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTableOrAlias_Sq()
  {
    return (EReference)tableOrAliasEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTableOrAlias_Alias()
  {
    return (EAttribute)tableOrAliasEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTableOrAlias_TblAlias()
  {
    return (EReference)tableOrAliasEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTableFull()
  {
    return tableFullEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDbObjectNameAll()
  {
    return dbObjectNameAllEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDbObjectNameAll_Dbname()
  {
    return (EAttribute)dbObjectNameAllEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDbObjectName()
  {
    return dbObjectNameEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDbObjectName_Dbname()
  {
    return (EAttribute)dbObjectNameEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOrOrderByColumn()
  {
    return orOrderByColumnEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOrOrderByColumn_Entries()
  {
    return (EReference)orOrderByColumnEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOrderByColumnFull()
  {
    return orderByColumnFullEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOrderByColumnFull_ColOrder()
  {
    return (EReference)orderByColumnFullEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOrderByColumnFull_ColOrderInt()
  {
    return (EAttribute)orderByColumnFullEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOrderByColumnFull_Direction()
  {
    return (EAttribute)orderByColumnFullEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOrGroupByColumn()
  {
    return orGroupByColumnEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOrGroupByColumn_Entries()
  {
    return (EReference)orGroupByColumnEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getGroupByColumnFull()
  {
    return groupByColumnFullEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGroupByColumnFull_ColGrBy()
  {
    return (EReference)groupByColumnFullEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOrExpr()
  {
    return orExprEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOrExpr_Entries()
  {
    return (EReference)orExprEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFullExpression()
  {
    return fullExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFullExpression_C()
  {
    return (EAttribute)fullExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Efrag()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFullExpression_NotPrm()
  {
    return (EAttribute)fullExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Expgroup()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Exp()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Xexp()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Op1()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFullExpression_Isnull()
  {
    return (EAttribute)fullExpressionEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_In()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Between()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(9);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Like()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(10);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFullExpression_Comp()
  {
    return (EReference)fullExpressionEClass.getEStructuralFeatures().get(11);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExprGroup()
  {
    return exprGroupEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getExprGroup_Expr()
  {
    return (EReference)exprGroupEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getXExpr()
  {
    return xExprEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getXExpr_Xf()
  {
    return (EAttribute)xExprEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getXExpr_Col()
  {
    return (EReference)xExprEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getXExpr_Prm()
  {
    return (EReference)xExprEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPrms()
  {
    return prmsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPrms_Entries()
  {
    return (EReference)prmsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRParameter()
  {
    return jrParameterEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRParameter_Jrprm()
  {
    return (EAttribute)jrParameterEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getComparison()
  {
    return comparisonEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getComparison_Operator()
  {
    return (EAttribute)comparisonEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getComparison_SubOperator()
  {
    return (EAttribute)comparisonEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getComparison_Op2()
  {
    return (EReference)comparisonEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLike()
  {
    return likeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLike_OpLike()
  {
    return (EAttribute)likeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getLike_Op2()
  {
    return (EReference)likeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLikeOperand()
  {
    return likeOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLikeOperand_Op2()
  {
    return (EAttribute)likeOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getLikeOperand_Fop2()
  {
    return (EReference)likeOperandEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getLikeOperand_Fcast()
  {
    return (EReference)likeOperandEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBetween()
  {
    return betweenEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBetween_OpBetween()
  {
    return (EAttribute)betweenEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBetween_Op2()
  {
    return (EReference)betweenEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBetween_Op3()
  {
    return (EReference)betweenEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInOper()
  {
    return inOperEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getInOper_Op()
  {
    return (EAttribute)inOperEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInOper_Subquery()
  {
    return (EReference)inOperEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInOper_OpList()
  {
    return (EReference)inOperEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOperandList()
  {
    return operandListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOperands()
  {
    return operandsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperands_Op1()
  {
    return (EReference)operandsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperands_Left()
  {
    return (EReference)operandsEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperands_Right()
  {
    return (EReference)operandsEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOperand()
  {
    return operandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Column()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Xop()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Subq()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Fcast()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Func()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Sqlcase()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Param()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Eparam()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperand_Scalar()
  {
    return (EReference)operandEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOpFunction()
  {
    return opFunctionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOpFunction_Fname()
  {
    return (EAttribute)opFunctionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOpFunction_Args()
  {
    return (EReference)opFunctionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOpFunctionArg()
  {
    return opFunctionArgEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOpFunctionArgOperand()
  {
    return opFunctionArgOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOpFunctionArgOperand_Op()
  {
    return (EReference)opFunctionArgOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOpFunctionCast()
  {
    return opFunctionCastEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOpFunctionCast_Op()
  {
    return (EReference)opFunctionCastEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOpFunctionCast_Type()
  {
    return (EAttribute)opFunctionCastEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOpFunctionCast_P()
  {
    return (EAttribute)opFunctionCastEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOpFunctionCast_P2()
  {
    return (EAttribute)opFunctionCastEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOpFunctionArgAgregate()
  {
    return opFunctionArgAgregateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPOperand()
  {
    return pOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPOperand_Prm()
  {
    return (EAttribute)pOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExpOperand()
  {
    return expOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getExpOperand_Prm()
  {
    return (EAttribute)expOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getColumnOperand()
  {
    return columnOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getColumnOperand_Cfull()
  {
    return (EReference)columnOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSubQueryOperand()
  {
    return subQueryOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSubQueryOperand_Sel()
  {
    return (EReference)subQueryOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getScalarOperand()
  {
    return scalarOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScalarOperand_Soint()
  {
    return (EAttribute)scalarOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScalarOperand_Sostr()
  {
    return (EAttribute)scalarOperandEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScalarOperand_Sodbl()
  {
    return (EAttribute)scalarOperandEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScalarOperand_Sodate()
  {
    return (EAttribute)scalarOperandEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScalarOperand_Sotime()
  {
    return (EAttribute)scalarOperandEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScalarOperand_Sodt()
  {
    return (EAttribute)scalarOperandEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSQLCaseOperand()
  {
    return sqlCaseOperandEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSQLCaseOperand_Expr()
  {
    return (EReference)sqlCaseOperandEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSQLCaseOperand_When()
  {
    return (EReference)sqlCaseOperandEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSQLCaseWhens()
  {
    return sqlCaseWhensEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSqlCaseWhen()
  {
    return sqlCaseWhenEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSqlCaseWhen_Expr()
  {
    return (EReference)sqlCaseWhenEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSqlCaseWhen_Texp()
  {
    return (EReference)sqlCaseWhenEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSqlCaseWhen_Eexp()
  {
    return (EReference)sqlCaseWhenEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCol()
  {
    return colEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCol_Entries()
  {
    return (EReference)colEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass gettbls()
  {
    return tblsEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference gettbls_Entries()
  {
    return (EReference)tblsEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOpList()
  {
    return opListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOpList_Entries()
  {
    return (EReference)opListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPlus()
  {
    return plusEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMinus()
  {
    return minusEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStar()
  {
    return starEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDiv()
  {
    return divEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConcat()
  {
    return concatEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOpFList()
  {
    return opFListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOpFList_Entries()
  {
    return (EReference)opFListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWhenList()
  {
    return whenListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWhenList_Entries()
  {
    return (EReference)whenListEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getXFunction()
  {
    return xFunctionEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SqlFactory getSqlFactory()
  {
    return (SqlFactory)getEFactoryInstance();
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
    modelEClass = createEClass(MODEL);
    createEReference(modelEClass, MODEL__QUERY);
    createEReference(modelEClass, MODEL__ORDER_BY_ENTRY);

    selectQueryEClass = createEClass(SELECT_QUERY);

    selectSubSetEClass = createEClass(SELECT_SUB_SET);
    createEAttribute(selectSubSetEClass, SELECT_SUB_SET__OP);
    createEAttribute(selectSubSetEClass, SELECT_SUB_SET__ALL);
    createEReference(selectSubSetEClass, SELECT_SUB_SET__QUERY);

    selectEClass = createEClass(SELECT);
    createEReference(selectEClass, SELECT__OP);
    createEAttribute(selectEClass, SELECT__SELECT);
    createEReference(selectEClass, SELECT__COLS);
    createEReference(selectEClass, SELECT__TBL);
    createEReference(selectEClass, SELECT__WHERE_EXPRESSION);
    createEReference(selectEClass, SELECT__GROUP_BY_ENTRY);
    createEReference(selectEClass, SELECT__HAVING_ENTRY);

    orColumnEClass = createEClass(OR_COLUMN);
    createEReference(orColumnEClass, OR_COLUMN__ENTRIES);

    columnOrAliasEClass = createEClass(COLUMN_OR_ALIAS);
    createEReference(columnOrAliasEClass, COLUMN_OR_ALIAS__CE);
    createEAttribute(columnOrAliasEClass, COLUMN_OR_ALIAS__ALIAS);
    createEReference(columnOrAliasEClass, COLUMN_OR_ALIAS__COL_ALIAS);
    createEAttribute(columnOrAliasEClass, COLUMN_OR_ALIAS__ALL_COLS);
    createEReference(columnOrAliasEClass, COLUMN_OR_ALIAS__DB_ALL_COLS);

    columnFullEClass = createEClass(COLUMN_FULL);

    orTableEClass = createEClass(OR_TABLE);
    createEReference(orTableEClass, OR_TABLE__ENTRIES);

    fromTableEClass = createEClass(FROM_TABLE);
    createEReference(fromTableEClass, FROM_TABLE__TABLE);
    createEReference(fromTableEClass, FROM_TABLE__FJOIN);

    fromTableJoinEClass = createEClass(FROM_TABLE_JOIN);
    createEAttribute(fromTableJoinEClass, FROM_TABLE_JOIN__JOIN);
    createEReference(fromTableJoinEClass, FROM_TABLE_JOIN__ON_TABLE);
    createEReference(fromTableJoinEClass, FROM_TABLE_JOIN__JOIN_EXPR);

    tableOrAliasEClass = createEClass(TABLE_OR_ALIAS);
    createEReference(tableOrAliasEClass, TABLE_OR_ALIAS__TFULL);
    createEReference(tableOrAliasEClass, TABLE_OR_ALIAS__SQ);
    createEAttribute(tableOrAliasEClass, TABLE_OR_ALIAS__ALIAS);
    createEReference(tableOrAliasEClass, TABLE_OR_ALIAS__TBL_ALIAS);

    tableFullEClass = createEClass(TABLE_FULL);

    dbObjectNameAllEClass = createEClass(DB_OBJECT_NAME_ALL);
    createEAttribute(dbObjectNameAllEClass, DB_OBJECT_NAME_ALL__DBNAME);

    dbObjectNameEClass = createEClass(DB_OBJECT_NAME);
    createEAttribute(dbObjectNameEClass, DB_OBJECT_NAME__DBNAME);

    orOrderByColumnEClass = createEClass(OR_ORDER_BY_COLUMN);
    createEReference(orOrderByColumnEClass, OR_ORDER_BY_COLUMN__ENTRIES);

    orderByColumnFullEClass = createEClass(ORDER_BY_COLUMN_FULL);
    createEReference(orderByColumnFullEClass, ORDER_BY_COLUMN_FULL__COL_ORDER);
    createEAttribute(orderByColumnFullEClass, ORDER_BY_COLUMN_FULL__COL_ORDER_INT);
    createEAttribute(orderByColumnFullEClass, ORDER_BY_COLUMN_FULL__DIRECTION);

    orGroupByColumnEClass = createEClass(OR_GROUP_BY_COLUMN);
    createEReference(orGroupByColumnEClass, OR_GROUP_BY_COLUMN__ENTRIES);

    groupByColumnFullEClass = createEClass(GROUP_BY_COLUMN_FULL);
    createEReference(groupByColumnFullEClass, GROUP_BY_COLUMN_FULL__COL_GR_BY);

    orExprEClass = createEClass(OR_EXPR);
    createEReference(orExprEClass, OR_EXPR__ENTRIES);

    fullExpressionEClass = createEClass(FULL_EXPRESSION);
    createEAttribute(fullExpressionEClass, FULL_EXPRESSION__C);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__EFRAG);
    createEAttribute(fullExpressionEClass, FULL_EXPRESSION__NOT_PRM);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__EXPGROUP);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__EXP);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__XEXP);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__OP1);
    createEAttribute(fullExpressionEClass, FULL_EXPRESSION__ISNULL);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__IN);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__BETWEEN);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__LIKE);
    createEReference(fullExpressionEClass, FULL_EXPRESSION__COMP);

    exprGroupEClass = createEClass(EXPR_GROUP);
    createEReference(exprGroupEClass, EXPR_GROUP__EXPR);

    xExprEClass = createEClass(XEXPR);
    createEAttribute(xExprEClass, XEXPR__XF);
    createEReference(xExprEClass, XEXPR__COL);
    createEReference(xExprEClass, XEXPR__PRM);

    prmsEClass = createEClass(PRMS);
    createEReference(prmsEClass, PRMS__ENTRIES);

    jrParameterEClass = createEClass(JR_PARAMETER);
    createEAttribute(jrParameterEClass, JR_PARAMETER__JRPRM);

    comparisonEClass = createEClass(COMPARISON);
    createEAttribute(comparisonEClass, COMPARISON__OPERATOR);
    createEAttribute(comparisonEClass, COMPARISON__SUB_OPERATOR);
    createEReference(comparisonEClass, COMPARISON__OP2);

    likeEClass = createEClass(LIKE);
    createEAttribute(likeEClass, LIKE__OP_LIKE);
    createEReference(likeEClass, LIKE__OP2);

    likeOperandEClass = createEClass(LIKE_OPERAND);
    createEAttribute(likeOperandEClass, LIKE_OPERAND__OP2);
    createEReference(likeOperandEClass, LIKE_OPERAND__FOP2);
    createEReference(likeOperandEClass, LIKE_OPERAND__FCAST);

    betweenEClass = createEClass(BETWEEN);
    createEAttribute(betweenEClass, BETWEEN__OP_BETWEEN);
    createEReference(betweenEClass, BETWEEN__OP2);
    createEReference(betweenEClass, BETWEEN__OP3);

    inOperEClass = createEClass(IN_OPER);
    createEAttribute(inOperEClass, IN_OPER__OP);
    createEReference(inOperEClass, IN_OPER__SUBQUERY);
    createEReference(inOperEClass, IN_OPER__OP_LIST);

    operandListEClass = createEClass(OPERAND_LIST);

    operandsEClass = createEClass(OPERANDS);
    createEReference(operandsEClass, OPERANDS__OP1);
    createEReference(operandsEClass, OPERANDS__LEFT);
    createEReference(operandsEClass, OPERANDS__RIGHT);

    operandEClass = createEClass(OPERAND);
    createEReference(operandEClass, OPERAND__COLUMN);
    createEReference(operandEClass, OPERAND__XOP);
    createEReference(operandEClass, OPERAND__SUBQ);
    createEReference(operandEClass, OPERAND__FCAST);
    createEReference(operandEClass, OPERAND__FUNC);
    createEReference(operandEClass, OPERAND__SQLCASE);
    createEReference(operandEClass, OPERAND__PARAM);
    createEReference(operandEClass, OPERAND__EPARAM);
    createEReference(operandEClass, OPERAND__SCALAR);

    opFunctionEClass = createEClass(OP_FUNCTION);
    createEAttribute(opFunctionEClass, OP_FUNCTION__FNAME);
    createEReference(opFunctionEClass, OP_FUNCTION__ARGS);

    opFunctionArgEClass = createEClass(OP_FUNCTION_ARG);

    opFunctionArgOperandEClass = createEClass(OP_FUNCTION_ARG_OPERAND);
    createEReference(opFunctionArgOperandEClass, OP_FUNCTION_ARG_OPERAND__OP);

    opFunctionCastEClass = createEClass(OP_FUNCTION_CAST);
    createEReference(opFunctionCastEClass, OP_FUNCTION_CAST__OP);
    createEAttribute(opFunctionCastEClass, OP_FUNCTION_CAST__TYPE);
    createEAttribute(opFunctionCastEClass, OP_FUNCTION_CAST__P);
    createEAttribute(opFunctionCastEClass, OP_FUNCTION_CAST__P2);

    opFunctionArgAgregateEClass = createEClass(OP_FUNCTION_ARG_AGREGATE);

    pOperandEClass = createEClass(POPERAND);
    createEAttribute(pOperandEClass, POPERAND__PRM);

    expOperandEClass = createEClass(EXP_OPERAND);
    createEAttribute(expOperandEClass, EXP_OPERAND__PRM);

    columnOperandEClass = createEClass(COLUMN_OPERAND);
    createEReference(columnOperandEClass, COLUMN_OPERAND__CFULL);

    subQueryOperandEClass = createEClass(SUB_QUERY_OPERAND);
    createEReference(subQueryOperandEClass, SUB_QUERY_OPERAND__SEL);

    scalarOperandEClass = createEClass(SCALAR_OPERAND);
    createEAttribute(scalarOperandEClass, SCALAR_OPERAND__SOINT);
    createEAttribute(scalarOperandEClass, SCALAR_OPERAND__SOSTR);
    createEAttribute(scalarOperandEClass, SCALAR_OPERAND__SODBL);
    createEAttribute(scalarOperandEClass, SCALAR_OPERAND__SODATE);
    createEAttribute(scalarOperandEClass, SCALAR_OPERAND__SOTIME);
    createEAttribute(scalarOperandEClass, SCALAR_OPERAND__SODT);

    sqlCaseOperandEClass = createEClass(SQL_CASE_OPERAND);
    createEReference(sqlCaseOperandEClass, SQL_CASE_OPERAND__EXPR);
    createEReference(sqlCaseOperandEClass, SQL_CASE_OPERAND__WHEN);

    sqlCaseWhensEClass = createEClass(SQL_CASE_WHENS);

    sqlCaseWhenEClass = createEClass(SQL_CASE_WHEN);
    createEReference(sqlCaseWhenEClass, SQL_CASE_WHEN__EXPR);
    createEReference(sqlCaseWhenEClass, SQL_CASE_WHEN__TEXP);
    createEReference(sqlCaseWhenEClass, SQL_CASE_WHEN__EEXP);

    colEClass = createEClass(COL);
    createEReference(colEClass, COL__ENTRIES);

    tblsEClass = createEClass(TBLS);
    createEReference(tblsEClass, TBLS__ENTRIES);

    opListEClass = createEClass(OP_LIST);
    createEReference(opListEClass, OP_LIST__ENTRIES);

    plusEClass = createEClass(PLUS);

    minusEClass = createEClass(MINUS);

    starEClass = createEClass(STAR);

    divEClass = createEClass(DIV);

    concatEClass = createEClass(CONCAT);

    opFListEClass = createEClass(OP_FLIST);
    createEReference(opFListEClass, OP_FLIST__ENTRIES);

    whenListEClass = createEClass(WHEN_LIST);
    createEReference(whenListEClass, WHEN_LIST__ENTRIES);

    // Create enums
    xFunctionEEnum = createEEnum(XFUNCTION);
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

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    selectEClass.getESuperTypes().add(this.getSelectQuery());
    columnOrAliasEClass.getESuperTypes().add(this.getOrColumn());
    fromTableEClass.getESuperTypes().add(this.getOrTable());
    dbObjectNameEClass.getESuperTypes().add(this.getColumnFull());
    dbObjectNameEClass.getESuperTypes().add(this.getTableFull());
    orderByColumnFullEClass.getESuperTypes().add(this.getOrOrderByColumn());
    groupByColumnFullEClass.getESuperTypes().add(this.getOrGroupByColumn());
    fullExpressionEClass.getESuperTypes().add(this.getOrExpr());
    jrParameterEClass.getESuperTypes().add(this.getPrms());
    operandsEClass.getESuperTypes().add(this.getOpFunctionArgAgregate());
    opFunctionArgOperandEClass.getESuperTypes().add(this.getOpFunctionArg());
    scalarOperandEClass.getESuperTypes().add(this.getOperandList());
    sqlCaseWhenEClass.getESuperTypes().add(this.getSQLCaseWhens());
    colEClass.getESuperTypes().add(this.getColumnFull());
    tblsEClass.getESuperTypes().add(this.getTableFull());
    opListEClass.getESuperTypes().add(this.getOperandList());
    plusEClass.getESuperTypes().add(this.getOperands());
    minusEClass.getESuperTypes().add(this.getOperands());
    starEClass.getESuperTypes().add(this.getOperands());
    divEClass.getESuperTypes().add(this.getOperands());
    concatEClass.getESuperTypes().add(this.getOperands());
    opFListEClass.getESuperTypes().add(this.getOpFunctionArg());
    whenListEClass.getESuperTypes().add(this.getSQLCaseWhens());

    // Initialize classes and features; add operations and parameters
    initEClass(modelEClass, Model.class, "Model", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getModel_Query(), this.getSelectQuery(), null, "query", null, 0, 1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModel_OrderByEntry(), this.getOrOrderByColumn(), null, "orderByEntry", null, 0, 1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(selectQueryEClass, SelectQuery.class, "SelectQuery", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(selectSubSetEClass, SelectSubSet.class, "SelectSubSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSelectSubSet_Op(), ecorePackage.getEString(), "op", null, 0, 1, SelectSubSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSelectSubSet_All(), ecorePackage.getEString(), "all", null, 0, 1, SelectSubSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelectSubSet_Query(), this.getSelect(), null, "query", null, 0, 1, SelectSubSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(selectEClass, Select.class, "Select", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSelect_Op(), this.getSelectSubSet(), null, "op", null, 0, -1, Select.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSelect_Select(), ecorePackage.getEString(), "select", null, 0, 1, Select.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelect_Cols(), this.getOrColumn(), null, "cols", null, 0, 1, Select.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelect_Tbl(), this.getOrTable(), null, "tbl", null, 0, 1, Select.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelect_WhereExpression(), this.getOrExpr(), null, "whereExpression", null, 0, 1, Select.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelect_GroupByEntry(), this.getOrGroupByColumn(), null, "groupByEntry", null, 0, 1, Select.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSelect_HavingEntry(), this.getOrExpr(), null, "havingEntry", null, 0, 1, Select.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(orColumnEClass, OrColumn.class, "OrColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOrColumn_Entries(), this.getColumnOrAlias(), null, "entries", null, 0, -1, OrColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(columnOrAliasEClass, ColumnOrAlias.class, "ColumnOrAlias", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getColumnOrAlias_Ce(), this.getOperands(), null, "ce", null, 0, 1, ColumnOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getColumnOrAlias_Alias(), ecorePackage.getEString(), "alias", null, 0, 1, ColumnOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getColumnOrAlias_ColAlias(), this.getDbObjectName(), null, "colAlias", null, 0, 1, ColumnOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getColumnOrAlias_AllCols(), ecorePackage.getEString(), "allCols", null, 0, 1, ColumnOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getColumnOrAlias_DbAllCols(), this.getDbObjectNameAll(), null, "dbAllCols", null, 0, 1, ColumnOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(columnFullEClass, ColumnFull.class, "ColumnFull", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(orTableEClass, OrTable.class, "OrTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOrTable_Entries(), this.getFromTable(), null, "entries", null, 0, -1, OrTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(fromTableEClass, FromTable.class, "FromTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getFromTable_Table(), this.getTableOrAlias(), null, "table", null, 0, 1, FromTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFromTable_Fjoin(), this.getFromTableJoin(), null, "fjoin", null, 0, -1, FromTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(fromTableJoinEClass, FromTableJoin.class, "FromTableJoin", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFromTableJoin_Join(), ecorePackage.getEString(), "join", null, 0, 1, FromTableJoin.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFromTableJoin_OnTable(), this.getTableOrAlias(), null, "onTable", null, 0, 1, FromTableJoin.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFromTableJoin_JoinExpr(), this.getOrExpr(), null, "joinExpr", null, 0, 1, FromTableJoin.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tableOrAliasEClass, TableOrAlias.class, "TableOrAlias", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTableOrAlias_Tfull(), this.getTableFull(), null, "tfull", null, 0, 1, TableOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTableOrAlias_Sq(), this.getSubQueryOperand(), null, "sq", null, 0, 1, TableOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTableOrAlias_Alias(), ecorePackage.getEString(), "alias", null, 0, 1, TableOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTableOrAlias_TblAlias(), this.getDbObjectName(), null, "tblAlias", null, 0, 1, TableOrAlias.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tableFullEClass, TableFull.class, "TableFull", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(dbObjectNameAllEClass, DbObjectNameAll.class, "DbObjectNameAll", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDbObjectNameAll_Dbname(), ecorePackage.getEString(), "dbname", null, 0, 1, DbObjectNameAll.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dbObjectNameEClass, DbObjectName.class, "DbObjectName", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDbObjectName_Dbname(), ecorePackage.getEString(), "dbname", null, 0, 1, DbObjectName.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(orOrderByColumnEClass, OrOrderByColumn.class, "OrOrderByColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOrOrderByColumn_Entries(), this.getOrderByColumnFull(), null, "entries", null, 0, -1, OrOrderByColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(orderByColumnFullEClass, OrderByColumnFull.class, "OrderByColumnFull", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOrderByColumnFull_ColOrder(), this.getColumnFull(), null, "colOrder", null, 0, 1, OrderByColumnFull.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOrderByColumnFull_ColOrderInt(), ecorePackage.getEInt(), "colOrderInt", null, 0, 1, OrderByColumnFull.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOrderByColumnFull_Direction(), ecorePackage.getEString(), "direction", null, 0, 1, OrderByColumnFull.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(orGroupByColumnEClass, OrGroupByColumn.class, "OrGroupByColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOrGroupByColumn_Entries(), this.getGroupByColumnFull(), null, "entries", null, 0, -1, OrGroupByColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(groupByColumnFullEClass, GroupByColumnFull.class, "GroupByColumnFull", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGroupByColumnFull_ColGrBy(), this.getColumnFull(), null, "colGrBy", null, 0, 1, GroupByColumnFull.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(orExprEClass, OrExpr.class, "OrExpr", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOrExpr_Entries(), this.getFullExpression(), null, "entries", null, 0, -1, OrExpr.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(fullExpressionEClass, FullExpression.class, "FullExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getFullExpression_C(), ecorePackage.getEString(), "c", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Efrag(), this.getFullExpression(), null, "efrag", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFullExpression_NotPrm(), ecorePackage.getEString(), "notPrm", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Expgroup(), this.getExprGroup(), null, "expgroup", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Exp(), this.getFullExpression(), null, "exp", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Xexp(), this.getXExpr(), null, "xexp", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Op1(), this.getOperands(), null, "op1", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFullExpression_Isnull(), ecorePackage.getEString(), "isnull", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_In(), this.getInOper(), null, "in", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Between(), this.getBetween(), null, "between", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Like(), this.getLike(), null, "like", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFullExpression_Comp(), this.getComparison(), null, "comp", null, 0, 1, FullExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(exprGroupEClass, ExprGroup.class, "ExprGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getExprGroup_Expr(), this.getOrExpr(), null, "expr", null, 0, 1, ExprGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(xExprEClass, XExpr.class, "XExpr", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getXExpr_Xf(), this.getXFunction(), "xf", null, 0, 1, XExpr.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getXExpr_Col(), this.getOperands(), null, "col", null, 0, 1, XExpr.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getXExpr_Prm(), this.getPrms(), null, "prm", null, 0, 1, XExpr.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(prmsEClass, Prms.class, "Prms", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getPrms_Entries(), this.getJRParameter(), null, "entries", null, 0, -1, Prms.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jrParameterEClass, JRParameter.class, "JRParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJRParameter_Jrprm(), ecorePackage.getEString(), "jrprm", null, 0, 1, JRParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(comparisonEClass, Comparison.class, "Comparison", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getComparison_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, Comparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getComparison_SubOperator(), ecorePackage.getEString(), "subOperator", null, 0, 1, Comparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getComparison_Op2(), this.getOperands(), null, "op2", null, 0, 1, Comparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(likeEClass, Like.class, "Like", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLike_OpLike(), ecorePackage.getEString(), "opLike", null, 0, 1, Like.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getLike_Op2(), this.getLikeOperand(), null, "op2", null, 0, 1, Like.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(likeOperandEClass, LikeOperand.class, "LikeOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLikeOperand_Op2(), ecorePackage.getEString(), "op2", null, 0, 1, LikeOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getLikeOperand_Fop2(), this.getOpFunction(), null, "fop2", null, 0, 1, LikeOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getLikeOperand_Fcast(), this.getOpFunctionCast(), null, "fcast", null, 0, 1, LikeOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(betweenEClass, Between.class, "Between", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBetween_OpBetween(), ecorePackage.getEString(), "opBetween", null, 0, 1, Between.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getBetween_Op2(), this.getOperands(), null, "op2", null, 0, 1, Between.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getBetween_Op3(), this.getOperands(), null, "op3", null, 0, 1, Between.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(inOperEClass, InOper.class, "InOper", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getInOper_Op(), ecorePackage.getEString(), "op", null, 0, 1, InOper.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInOper_Subquery(), this.getSubQueryOperand(), null, "subquery", null, 0, 1, InOper.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInOper_OpList(), this.getOperandList(), null, "opList", null, 0, 1, InOper.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(operandListEClass, OperandList.class, "OperandList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(operandsEClass, Operands.class, "Operands", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOperands_Op1(), this.getOperand(), null, "op1", null, 0, 1, Operands.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperands_Left(), this.getOperands(), null, "left", null, 0, 1, Operands.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperands_Right(), this.getOperand(), null, "right", null, 0, 1, Operands.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(operandEClass, Operand.class, "Operand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOperand_Column(), this.getColumnOperand(), null, "column", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Xop(), this.getOperand(), null, "xop", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Subq(), this.getSubQueryOperand(), null, "subq", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Fcast(), this.getOpFunctionCast(), null, "fcast", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Func(), this.getOpFunction(), null, "func", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Sqlcase(), this.getSQLCaseOperand(), null, "sqlcase", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Param(), this.getPOperand(), null, "param", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Eparam(), this.getExpOperand(), null, "eparam", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOperand_Scalar(), this.getScalarOperand(), null, "scalar", null, 0, 1, Operand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(opFunctionEClass, OpFunction.class, "OpFunction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getOpFunction_Fname(), ecorePackage.getEString(), "fname", null, 0, 1, OpFunction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getOpFunction_Args(), this.getOpFunctionArg(), null, "args", null, 0, 1, OpFunction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(opFunctionArgEClass, OpFunctionArg.class, "OpFunctionArg", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(opFunctionArgOperandEClass, OpFunctionArgOperand.class, "OpFunctionArgOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOpFunctionArgOperand_Op(), this.getOpFunctionArgAgregate(), null, "op", null, 0, 1, OpFunctionArgOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(opFunctionCastEClass, OpFunctionCast.class, "OpFunctionCast", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOpFunctionCast_Op(), this.getOperands(), null, "op", null, 0, 1, OpFunctionCast.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOpFunctionCast_Type(), ecorePackage.getEString(), "type", null, 0, 1, OpFunctionCast.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOpFunctionCast_P(), ecorePackage.getEInt(), "p", null, 0, 1, OpFunctionCast.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getOpFunctionCast_P2(), ecorePackage.getEInt(), "p2", null, 0, 1, OpFunctionCast.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(opFunctionArgAgregateEClass, OpFunctionArgAgregate.class, "OpFunctionArgAgregate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(pOperandEClass, POperand.class, "POperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getPOperand_Prm(), ecorePackage.getEString(), "prm", null, 0, 1, POperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(expOperandEClass, ExpOperand.class, "ExpOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getExpOperand_Prm(), ecorePackage.getEString(), "prm", null, 0, 1, ExpOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(columnOperandEClass, ColumnOperand.class, "ColumnOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getColumnOperand_Cfull(), this.getColumnFull(), null, "cfull", null, 0, 1, ColumnOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(subQueryOperandEClass, SubQueryOperand.class, "SubQueryOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSubQueryOperand_Sel(), this.getSelectQuery(), null, "sel", null, 0, 1, SubQueryOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(scalarOperandEClass, ScalarOperand.class, "ScalarOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getScalarOperand_Soint(), ecorePackage.getEInt(), "soint", null, 0, 1, ScalarOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScalarOperand_Sostr(), ecorePackage.getEString(), "sostr", null, 0, 1, ScalarOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScalarOperand_Sodbl(), ecorePackage.getEBigDecimal(), "sodbl", null, 0, 1, ScalarOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScalarOperand_Sodate(), ecorePackage.getEDate(), "sodate", null, 0, 1, ScalarOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScalarOperand_Sotime(), ecorePackage.getEDate(), "sotime", null, 0, 1, ScalarOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScalarOperand_Sodt(), ecorePackage.getEDate(), "sodt", null, 0, 1, ScalarOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(sqlCaseOperandEClass, SQLCaseOperand.class, "SQLCaseOperand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSQLCaseOperand_Expr(), this.getOrExpr(), null, "expr", null, 0, 1, SQLCaseOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSQLCaseOperand_When(), this.getSQLCaseWhens(), null, "when", null, 0, 1, SQLCaseOperand.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(sqlCaseWhensEClass, SQLCaseWhens.class, "SQLCaseWhens", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(sqlCaseWhenEClass, SqlCaseWhen.class, "SqlCaseWhen", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSqlCaseWhen_Expr(), this.getOrExpr(), null, "expr", null, 0, 1, SqlCaseWhen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSqlCaseWhen_Texp(), this.getOperands(), null, "texp", null, 0, 1, SqlCaseWhen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSqlCaseWhen_Eexp(), this.getOperands(), null, "eexp", null, 0, 1, SqlCaseWhen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(colEClass, Col.class, "Col", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getCol_Entries(), this.getDbObjectName(), null, "entries", null, 0, -1, Col.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tblsEClass, tbls.class, "tbls", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(gettbls_Entries(), this.getDbObjectName(), null, "entries", null, 0, -1, tbls.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(opListEClass, OpList.class, "OpList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOpList_Entries(), this.getScalarOperand(), null, "entries", null, 0, -1, OpList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(plusEClass, Plus.class, "Plus", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(minusEClass, Minus.class, "Minus", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(starEClass, Star.class, "Star", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(divEClass, Div.class, "Div", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(concatEClass, Concat.class, "Concat", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(opFListEClass, OpFList.class, "OpFList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getOpFList_Entries(), this.getOpFunctionArgOperand(), null, "entries", null, 0, -1, OpFList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(whenListEClass, WhenList.class, "WhenList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getWhenList_Entries(), this.getSqlCaseWhen(), null, "entries", null, 0, -1, WhenList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(xFunctionEEnum, XFunction.class, "XFunction");
    addEEnumLiteral(xFunctionEEnum, XFunction.XIN);
    addEEnumLiteral(xFunctionEEnum, XFunction.XNOTIN);
    addEEnumLiteral(xFunctionEEnum, XFunction.XEQ);
    addEEnumLiteral(xFunctionEEnum, XFunction.XNOTEQ);
    addEEnumLiteral(xFunctionEEnum, XFunction.XLS);
    addEEnumLiteral(xFunctionEEnum, XFunction.XGT);
    addEEnumLiteral(xFunctionEEnum, XFunction.XLSR);
    addEEnumLiteral(xFunctionEEnum, XFunction.XGTL);
    addEEnumLiteral(xFunctionEEnum, XFunction.XBWN);
    addEEnumLiteral(xFunctionEEnum, XFunction.XBWNC);
    addEEnumLiteral(xFunctionEEnum, XFunction.XBWNL);
    addEEnumLiteral(xFunctionEEnum, XFunction.XBWNR);

    // Create resource
    createResource(eNS_URI);
  }

} //SqlPackageImpl
