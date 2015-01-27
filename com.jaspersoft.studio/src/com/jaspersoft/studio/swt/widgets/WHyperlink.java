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
package com.jaspersoft.studio.swt.widgets;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.design.JRDesignHyperlinkParameter;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.jface.dialogs.ElementWithValueExpressionDialog;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Hyperlink widget re-usable in custom wizards/dialogs/panels.
 * It allows the editing of a {@link JRDesignHyperlink} instance element.
 * <p>
 * After creating the widget instance the clients must always invoke the 
 * {@link #setHyperlink(JRDesignHyperlink)} method in order to modify an
 * existing {@link JRDesignHyperlink} instance.
 *  
 * @author mrabbi
 *
 */
public class WHyperlink extends Composite implements IExpressionContextSetter {
	
	private JRDesignHyperlink hyperlink;
	private ExpressionContext expContext;
	private boolean init=false;
	
	// Widgets
	private WTextExpression referenceExpr;
	private WTextExpression anchorExpr;
	private WTextExpression pageExpr;
	private WTextExpression tooltipExpr;
	private Combo comboHyperlinkTarget;
	private Combo comboHyperlinkType;
	private TableViewer tableViewerHyperlinkParameters;
	private Button btnAddHyperlinkParam;
	private Button btnModifyHyperlinkParam;
	private Button btnRemoveHyperlinkParam;
	private TabFolder tabFolder;
	private TabItem tbtmReference;
	private TabItem tbtmAnchor;
	private TabItem tbtmPage;
	private TabItem tbtmHyperlinkParameters;
	private TabItem tbtmTooltip;
	private Composite tooltipContent;
	private Composite hyperlinkParamsContent;
	private Composite anchorContent;
	private Composite referenceContent;
	private Composite pageContent;
//	private TabItem tbtmWhenExpression;
//	private Composite whenExprContent;
//	private WTextExpression whenExpr;
	
	// Enumeration combo stuff
	private static String[] linkTargetItems=new String[]{
		HyperlinkTargetEnum.SELF.getName(),
		HyperlinkTargetEnum.BLANK.getName(),
		HyperlinkTargetEnum.TOP.getName(),
		HyperlinkTargetEnum.PARENT.getName(),};
	private static String[] linkTypeItems;
	
	static {
		ArrayList<HyperlinkTypeEnum> filteredTypes = new ArrayList<HyperlinkTypeEnum>(2);
		filteredTypes.add(HyperlinkTypeEnum.CUSTOM);	// Will be used automatically when user write a custom entry
		filteredTypes.add(HyperlinkTypeEnum.NULL);		// Makes no much sense into this widget
		List<String> alltypes=ModelUtils.getHyperlinkTypeNames4Widget(filteredTypes);		
		linkTypeItems=alltypes.toArray(new String[alltypes.size()]);
	}
	

	/**
	 * Create the hyperlink widget.
	 * 
	 * @param parent
	 * @param style
	 */
	public WHyperlink(Composite parent, int style) {
		super(parent, style);
		init=true;
		setLayout(new GridLayout(2, false));
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		CLabel lblHyperlinkTarget = new CLabel(this, SWT.NONE);
		lblHyperlinkTarget.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHyperlinkTarget.setText(Messages.WHyperlink_TargetLbl);
		
		comboHyperlinkTarget = new Combo(this, SWT.NONE);
		GridData gd_comboHyperlinkTarget = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_comboHyperlinkTarget.widthHint = 195;
		comboHyperlinkTarget.setLayoutData(gd_comboHyperlinkTarget);
		comboHyperlinkTarget.setItems(linkTargetItems);
		comboHyperlinkTarget.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				HyperlinkTargetEnum selectedTarget=HyperlinkTargetEnum.getByName(comboHyperlinkTarget.getText());
				if(selectedTarget==null){
					selectedTarget=HyperlinkTargetEnum.CUSTOM;
				}
				
				if(!init){
					hyperlink.setHyperlinkTarget(selectedTarget);
					hyperlink.setLinkTarget(comboHyperlinkTarget.getText());
				}
			}
		});
			
		CLabel lblHyperlinkType = new CLabel(this, SWT.NONE);
		lblHyperlinkType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHyperlinkType.setText(Messages.WHyperlink_TypeLbl);
		
		comboHyperlinkType = new Combo(this, SWT.NONE);
		GridData gd_comboHyperlinkType = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_comboHyperlinkType.widthHint = 195;
		comboHyperlinkType.setLayoutData(gd_comboHyperlinkType);
		comboHyperlinkType.setItems(linkTypeItems);
		comboHyperlinkType.addModifyListener(new ModifyListener() {
			private HyperlinkTypeEnum previousSelected=HyperlinkTypeEnum.NULL;
			
			public void modifyText(ModifyEvent e) {
				HyperlinkTypeEnum selectedType=HyperlinkTypeEnum.getByName(comboHyperlinkType.getText());
				if(selectedType==null){
					selectedType=HyperlinkTypeEnum.CUSTOM;
				}

				if(selectedType!=previousSelected){
					refreshSubTabs(selectedType);
				}
				previousSelected=selectedType;
				
				if(!init){
					hyperlink.setLinkType(comboHyperlinkType.getText());
					if(selectedType!=HyperlinkTypeEnum.CUSTOM){
						// No setting operation, otherwise it will produce an Exception
						// with the following message: "Custom hyperlink types cannot be specified using the byte constant".
						hyperlink.setHyperlinkType(selectedType);
					}
				}
			}

		});	
		
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		// Reference tab
		tbtmReference = new TabItem(tabFolder, SWT.NONE);
		tbtmReference.setText(Messages.WHyperlink_ReferenceTab);
		referenceContent = new Composite(tabFolder, SWT.NONE);
		tbtmReference.setControl(referenceContent);
		referenceContent.setLayout(new GridLayout(1,true));
		
		referenceExpr = new WTextExpression(referenceContent, SWT.NONE, Messages.WHyperlink_ReferenceExprLbl, WTextExpression.LABEL_ON_TOP){
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				if(!init){
					hyperlink.setHyperlinkReferenceExpression(exp);
				}
			}
		};
		referenceExpr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true));
		
		// Anchor tab
		tbtmAnchor = new TabItem(tabFolder, SWT.NONE);
		tbtmAnchor.setText(Messages.WHyperlink_AnchorTab);
		anchorContent = new Composite(tabFolder, SWT.NONE);
		tbtmAnchor.setControl(anchorContent);
		anchorContent.setLayout(new GridLayout(1,true));
		
		anchorExpr = new WTextExpression(anchorContent, SWT.NONE, Messages.WHyperlink_AnchorExprLbl, WTextExpression.LABEL_ON_TOP){
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				if(!init){
					hyperlink.setHyperlinkAnchorExpression(exp);
				}
			}
		};
		anchorExpr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// Page tab
		tbtmPage = new TabItem(tabFolder, SWT.NONE);
		tbtmPage.setText(Messages.WHyperlink_PageTab);
		pageContent = new Composite(tabFolder, SWT.NONE);
		tbtmPage.setControl(pageContent);
		pageContent.setLayout(new GridLayout(1,true));
		
		pageExpr = new WTextExpression(pageContent, SWT.NONE,Messages.WHyperlink_PageExprLbl, WTextExpression.LABEL_ON_TOP){
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				if(!init){
					hyperlink.setHyperlinkPageExpression(exp);
				}
			}
		};
		pageExpr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		// Parameters tab
		tbtmHyperlinkParameters = new TabItem(tabFolder, SWT.NONE);
		tbtmHyperlinkParameters.setText(Messages.WHyperlink_ParametersTab);
		hyperlinkParamsContent = new Composite(tabFolder, SWT.NONE);
		tbtmHyperlinkParameters.setControl(hyperlinkParamsContent);
		hyperlinkParamsContent.setLayout(new GridLayout(3,false));
		
		Composite cmpHyperlinkParamsTableViewer=new Composite(hyperlinkParamsContent, SWT.NONE);
		cmpHyperlinkParamsTableViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		TableColumnLayout tl_hyperlinkParamsTableViewer = new TableColumnLayout();
		cmpHyperlinkParamsTableViewer.setLayout(tl_hyperlinkParamsTableViewer);
		
		tableViewerHyperlinkParameters = new TableViewer(cmpHyperlinkParamsTableViewer, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableHyperlinkParameters = tableViewerHyperlinkParameters.getTable();
		tableHyperlinkParameters.setHeaderVisible(true);
		tableHyperlinkParameters.setLinesVisible(true);
		
		TableViewerColumn tblclmnHyperlinkParamName = new TableViewerColumn(tableViewerHyperlinkParameters, SWT.NONE);
		tblclmnHyperlinkParamName.getColumn().setText(Messages.WHyperlink_ParameterNameCol);
		tblclmnHyperlinkParamName.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if (element!=null){
					return ((JRDesignHyperlinkParameter)element).getName();
				}
				return null;
			}
		});
		tl_hyperlinkParamsTableViewer.setColumnData(tblclmnHyperlinkParamName.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		
		TableViewerColumn tblclmnHyperlinkParamExp = new TableViewerColumn(tableViewerHyperlinkParameters, SWT.NONE);
		tblclmnHyperlinkParamExp.getColumn().setText(Messages.WHyperlink_ParameterExprCol);
		tblclmnHyperlinkParamExp.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element!=null){
					JRExpression valueExpression = ((JRDesignHyperlinkParameter)element).getValueExpression();
					if (valueExpression!=null){
						return valueExpression.getText();
					}
				}
				return super.getText(element);
			}
		});
		tl_hyperlinkParamsTableViewer.setColumnData(tblclmnHyperlinkParamExp.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		
		tableViewerHyperlinkParameters.setContentProvider(new ArrayContentProvider());
		tableViewerHyperlinkParameters.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				modifySelectedParameter();			
			}
		});
		tableViewerHyperlinkParameters.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// Enables the modify and remove buttons disabled at startup
				btnModifyHyperlinkParam.setEnabled(true);
				btnRemoveHyperlinkParam.setEnabled(true);
			}
		});
		
		btnAddHyperlinkParam = new Button(hyperlinkParamsContent, SWT.NONE);
		btnAddHyperlinkParam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		btnAddHyperlinkParam.setText(Messages.WHyperlink_AddBtn);
		btnAddHyperlinkParam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewParameter();
			}
		});
		
		btnModifyHyperlinkParam = new Button(hyperlinkParamsContent, SWT.NONE);
		btnModifyHyperlinkParam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		btnModifyHyperlinkParam.setText(Messages.WHyperlink_ModifyBtn);
		btnModifyHyperlinkParam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modifySelectedParameter();
			}
		});
		btnModifyHyperlinkParam.setEnabled(false);
		
		btnRemoveHyperlinkParam = new Button(hyperlinkParamsContent, SWT.NONE);
		btnRemoveHyperlinkParam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		btnRemoveHyperlinkParam.setText(Messages.WHyperlink_RemoveBtn);
		btnRemoveHyperlinkParam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelectedParameter();
				btnModifyHyperlinkParam.setEnabled(false);
				btnRemoveHyperlinkParam.setEnabled(false);
			}
		});
		btnRemoveHyperlinkParam.setEnabled(false);

		// Tooltip tab
		tbtmTooltip = new TabItem(tabFolder, SWT.NONE);
		tbtmTooltip.setText(Messages.WHyperlink_TooltipTab);
		tooltipContent = new Composite(tabFolder, SWT.NONE);
		tbtmTooltip.setControl(tooltipContent);
		tooltipContent.setLayout(new GridLayout(1, true));
		
		tooltipExpr = new WTextExpression(tooltipContent, SWT.NONE,Messages.WHyperlink_TooltipExprLbl, WTextExpression.LABEL_ON_TOP){
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				if(!init){
					hyperlink.setHyperlinkTooltipExpression(exp);
				}
			}
		};
		tooltipExpr.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
//		tbtmWhenExpression = new TabItem(tabFolder, SWT.NONE);
//		tbtmWhenExpression.setText("When Expression");
//		whenExprContent = new Composite(tabFolder, SWT.NONE);
//		tbtmWhenExpression.setControl(whenExprContent);
//		whenExprContent.setLayout(new GridLayout(1, true));
//		whenExpr = new WTextExpression(whenExprContent, SWT.NONE,"Hyperlink When Expression", WTextExpression.LABEL_ON_TOP){
//			@Override
//			public void setExpression(JRDesignExpression exp) {
//				super.setExpression(exp);
//				if(!init){
//					hyperlink.setHyperlinkWhenExpression(exp);
//				}
//			}
//		};
//		whenExpr.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			
		if(hyperlink==null){
			hyperlink=new JRDesignHyperlink();
			// Default values
			hyperlink.setHyperlinkType(HyperlinkTypeEnum.NONE);
			hyperlink.setLinkType("None"); //$NON-NLS-1$
			hyperlink.setHyperlinkTarget(HyperlinkTargetEnum.SELF);
			hyperlink.setLinkTarget("Self"); //$NON-NLS-1$
		}
		refreshWidgetsContent();
		
		init=false;
	}
	
	/*
	 * Refresh widgets content using the hyperlink object information.
	 */
	private void refreshWidgetsContent(){
		Assert.isNotNull(hyperlink);
		
		// Hyperlink target
		String linkTarget = hyperlink.getLinkTarget();
		if(linkTarget!=null && !linkTarget.isEmpty()){
			comboHyperlinkTarget.setText(linkTarget);
		}
		
		// Hyperlink type
		String linkType = hyperlink.getLinkType();
		if(linkType!=null && !linkType.isEmpty()){
			comboHyperlinkType.setText(linkType);
		}
		
		// Hyperlink reference
		referenceExpr.setExpression((JRDesignExpression)hyperlink.getHyperlinkReferenceExpression());
			
		// Hyperlink anchor
		anchorExpr.setExpression((JRDesignExpression)hyperlink.getHyperlinkAnchorExpression());
		
		// Hyperlink page
		pageExpr.setExpression((JRDesignExpression)hyperlink.getHyperlinkPageExpression());
		
		// Hyperlink parameters
		if(hyperlink.getHyperlinkParameters()!=null){
			tableViewerHyperlinkParameters.setInput(hyperlink.getHyperlinkParameters());
		}
		
		// Hyperlink tooltip
		tooltipExpr.setExpression((JRDesignExpression)hyperlink.getHyperlinkTooltipExpression());
		
//		// Hyperlink when expression
//		whenExpr.setExpression((JRDesignExpression)hyperlink.getHyperlinkWhenExpression());
	}
	
	/*
	 * Enables/Disables the widgets inside the different tabs
	 * depending on the current selected hyperlink type.
	 */
	private void refreshSubTabs(HyperlinkTypeEnum selectedType) {
		// NOTE: Tabitems can not be hide/shown programmatically.
		// To "simulate" the behaviour the tabitem must be disposed and then
		// recreated and the #setControl method must be invoked.
		// Their content widgets, in fact have the tabfolder as parent.
		
		//Dispose all tabitem less the tooltip always shown
		tbtmReference.dispose();
		tbtmAnchor.dispose();
		tbtmPage.dispose();
		tbtmHyperlinkParameters.dispose();
		
		// And recreates only the necessary ones
		switch (selectedType) {
			case CUSTOM:
				tbtmHyperlinkParameters = new TabItem(tabFolder, SWT.NONE, tabFolder.getItemCount()-1);
				tbtmHyperlinkParameters.setText(Messages.WHyperlink_ParametersTab);
				tbtmHyperlinkParameters.setControl(hyperlinkParamsContent);
				break;
			case LOCAL_ANCHOR:
				tbtmAnchor=new TabItem(tabFolder, SWT.NONE,tabFolder.getItemCount()-1);
				tbtmAnchor.setText(Messages.WHyperlink_AnchorTab);
				tbtmAnchor.setControl(anchorContent);
				break;
			case LOCAL_PAGE:
				tbtmPage=new TabItem(tabFolder, SWT.NONE,tabFolder.getItemCount()-1);
				tbtmPage.setText(Messages.WHyperlink_PageTab);
				tbtmPage.setControl(pageContent);
				break;
			case NONE:
				break;
			case REFERENCE:
				tbtmReference=new TabItem(tabFolder, SWT.NONE,tabFolder.getItemCount()-1);
				tbtmReference.setText(Messages.WHyperlink_ReferenceTab);
				tbtmReference.setControl(referenceContent);
				break;
			case REMOTE_ANCHOR:
				tbtmReference=new TabItem(tabFolder, SWT.NONE,tabFolder.getItemCount()-1);
				tbtmReference.setText(Messages.WHyperlink_ReferenceTab);
				tbtmReference.setControl(referenceContent);
				tbtmAnchor=new TabItem(tabFolder, SWT.NONE,tabFolder.getItemCount()-1);
				tbtmAnchor.setText(Messages.WHyperlink_AnchorTab);
				tbtmAnchor.setControl(anchorContent);				
				break;
			case REMOTE_PAGE:
				tbtmReference=new TabItem(tabFolder, SWT.NONE,tabFolder.getItemCount()-1);
				tbtmReference.setText(Messages.WHyperlink_ReferenceTab);
				tbtmReference.setControl(referenceContent);
				tbtmPage=new TabItem(tabFolder, SWT.NONE,tabFolder.getItemCount()-1);
				tbtmPage.setText(Messages.WHyperlink_PageTab);
				tbtmPage.setControl(pageContent);
				break;
		}
		
		tabFolder.setSelection(0);
//		resetHyperlinkDataAndTabsContent();
	}
	
	/**
	 * Sets the new hyperlink object and updates the UI content according.
	 * Hyperlink can not be null.
	 * 
	 * @param hyperlink the new {@link JRDesignHyperlink} instance to set
	 */
	public void setHyperlink(JRDesignHyperlink hyperlink){
		Assert.isNotNull(hyperlink);
		this.hyperlink=hyperlink;
		refreshWidgetsContent();
	}

//	/*
//	 * Resets the content information of the different tabs.
//	 * Should be used when changing the hyperlink type, so that 
//	 * the data inside the controls are "fresh new".
//	 *
//	 * Also the element information are "cleaned".
//	 */
//	private void resetHyperlinkDataAndTabsContent(){
//		// Hyperlink Reference
//		referenceExpr.setExpression(null);
//		hyperlink.setHyperlinkReferenceExpression(null);
//		
//		// Hyperlink Anchor
//		anchorExpr.setExpression(null);
//		hyperlink.setHyperlinkAnchorExpression(null);
//		
//		// Hyperlink Page
//		pageExpr.setExpression(null);
//		hyperlink.setHyperlinkPageExpression(null);
//
//		// Hyperlink Tooltip 
//		tooltipExpr.setExpression(null);
//		hyperlink.setHyperlinkTooltipExpression(null);
//		
//		// Hyperlink Parameters
//		JRHyperlinkParameter[] oldParams = hyperlink.getHyperlinkParameters();
//		if (oldParams!=null){
//			for (JRHyperlinkParameter p : oldParams){
//				hyperlink.removeHyperlinkParameter(p);
//			}
//		}
//		tableViewerHyperlinkParameters.getTable().clearAll();
//		btnModifyHyperlinkParam.setEnabled(false);
//		btnRemoveHyperlinkParam.setEnabled(false);
//	}
	
	/*
	 * Shows the dialog for the creation of a new hyperlink parameter.
	 */
	private void addNewParameter() {
		ElementWithValueExpressionDialog dialog=new ElementWithValueExpressionDialog(Messages.WHyperlink_AddParameterDialogTitle, Messages.WHyperlink_AddParameterDialogLbl1, Messages.WHyperlink_AddParameterDialogLbl2, null, null, getShell());
		dialog.setExpressionContext(expContext);
		if(dialog.open()==Window.OK){
			JRDesignHyperlinkParameter linkParam=new JRDesignHyperlinkParameter();
			linkParam.setName(dialog.getElementName());
			linkParam.setValueExpression(dialog.getElementValueExpression());
			hyperlink.addHyperlinkParameter(linkParam);
			tableViewerHyperlinkParameters.setInput(hyperlink.getHyperlinkParametersList());
		}
	}
	
	/*
	 * Shows the dialog for the modification of the currently selected hyperlink parameter.
	 */
	private void modifySelectedParameter() {
		Object selObject=((IStructuredSelection)tableViewerHyperlinkParameters.getSelection()).getFirstElement();
		if(selObject!=null && selObject instanceof JRDesignHyperlinkParameter){
			JRDesignHyperlinkParameter selectedParam=(JRDesignHyperlinkParameter)selObject;
			ElementWithValueExpressionDialog dialog=new ElementWithValueExpressionDialog(Messages.WHyperlink_ModifyParameterDialogTitle, Messages.WHyperlink_ModifyParameterDialogLbl1, Messages.WHyperlink_ModifyParameterDialogLbl2, selectedParam.getName(), (JRDesignExpression)selectedParam.getValueExpression(), getShell());
			dialog.setExpressionContext(expContext);
			if(dialog.open()==Window.OK){
				selectedParam.setName(dialog.getElementName());
				selectedParam.setValueExpression(dialog.getElementValueExpression());
				tableViewerHyperlinkParameters.setInput(hyperlink.getHyperlinkParametersList());
			}
		}
	}
	
	/*
	 * Remove the currently selected hyperlink paramter.
	 */
	private void removeSelectedParameter() {
		Object selObject=((IStructuredSelection)tableViewerHyperlinkParameters.getSelection()).getFirstElement();
		if(selObject!=null && selObject instanceof JRDesignHyperlinkParameter){
			hyperlink.removeHyperlinkParameter((JRDesignHyperlinkParameter)selObject);
			tableViewerHyperlinkParameters.setInput(hyperlink.getHyperlinkParametersList());
		}
	}
	
	/**
	 * Returns the actual {@link JRDesignHyperlink} instance handled by the widget. 
	 * 
	 * @return the current hyperlink
	 */
	public JRDesignHyperlink getHyperlink(){
		if(this.hyperlink.getHyperlinkTypeValue()==null){
			return null;			
		}
		
		// Adjust the hyperlink object depending on the hyperlink type,
		// resetting all useless properties that may be dirty due to editing.
		boolean clearParameters=false;
		switch (this.hyperlink.getHyperlinkTypeValue()) {
			case CUSTOM:
				this.hyperlink.setHyperlinkAnchorExpression(null);
				this.hyperlink.setHyperlinkPageExpression(null);
				this.hyperlink.setHyperlinkReferenceExpression(null);
				break;
			case LOCAL_ANCHOR:
				this.hyperlink.setHyperlinkPageExpression(null);
				this.hyperlink.setHyperlinkReferenceExpression(null);
				clearParameters=true;
				break;
			case LOCAL_PAGE:
				this.hyperlink.setHyperlinkAnchorExpression(null);
				this.hyperlink.setHyperlinkReferenceExpression(null);
				clearParameters=true;
				break;
			case REMOTE_ANCHOR:
				this.hyperlink.setHyperlinkPageExpression(null);
				clearParameters=true;
				break;
			case REMOTE_PAGE:
				this.hyperlink.setHyperlinkAnchorExpression(null);
				clearParameters=true;
				break;
			case NONE:
				this.hyperlink.setHyperlinkAnchorExpression(null);
				this.hyperlink.setHyperlinkPageExpression(null);
				this.hyperlink.setHyperlinkReferenceExpression(null);
				clearParameters=true;
				break;
			case REFERENCE: 
				this.hyperlink.setHyperlinkAnchorExpression(null);
				this.hyperlink.setHyperlinkPageExpression(null);
				clearParameters=true;
				break;
			default:
				return null;
		}
		
		if(clearParameters){
			List<JRHyperlinkParameter> allParameters = this.hyperlink.getHyperlinkParametersList();
			if(allParameters!=null){
				for(JRHyperlinkParameter p : allParameters){
					this.hyperlink.removeHyperlinkParameter(p);
				}
			}
			allParameters.clear();
		}
		
		return this.hyperlink;
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext=expContext;
		referenceExpr.setExpressionContext(expContext);
		anchorExpr.setExpressionContext(expContext);
		pageExpr.setExpressionContext(expContext);
		tooltipExpr.setExpressionContext(expContext);
//		whenExpr.setExpressionContext(expContext);
	}
	
}
