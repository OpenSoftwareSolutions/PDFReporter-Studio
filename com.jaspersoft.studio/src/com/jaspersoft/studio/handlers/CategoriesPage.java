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
package com.jaspersoft.studio.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.messages.MessagesByKeys;
import com.jaspersoft.studio.swt.events.ChangeEvent;
import com.jaspersoft.studio.swt.events.ChangeListener;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.MoveT2TButtons;
import com.jaspersoft.studio.templates.DefaultTemplateProvider;
import com.jaspersoft.studio.templates.TemplateProvider;
import com.jaspersoft.studio.wizards.BuiltInCategories;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizardPage;

/**
 * Page of the wizard used to assign the categories to an exported report as template, 
 * and to define its type
 * 
 * @author Orlandin Marco
 *
 */
public class CategoriesPage extends JSSWizardPage {

	/**
	 * List of available categories
	 */
	protected List<String> inFields;
	
	/**
	 * List of the selected categories
	 */
	protected List<String> categoriesSelected = new ArrayList<String>();

	/**
	 * Table where the selected categories are shown
	 */
	protected Table rightTable;
	
	/**
	 * Table where the available categories are shown
	 */
	private Table leftTable;
	
	/**
	 * Combo box used to select the type of the Template, the type define the engine to
	 * use to generate report from that template
	 */
	private Combo engineCombo = null;
	
	/**
	 * String of the available key for the reprot type selection
	 */
	protected String[] engineKeys;
	
	/**
	 * Set of buttons to manage the list...
	 */
	private MoveT2TButtons mt2t = null;
	
	/**
	 * Map of all the available Template provider, the key is the key provided by the provider itself
	 */
	private HashMap<String, TemplateProvider> providersMap = new HashMap<String, TemplateProvider>();
	
	protected CategoriesPage() {
		super("addcategories"); //$NON-NLS-1$
		setTitle(Messages.CategoriesPage_pageTitle);
		setDescription(Messages.CategoriesPage_pageDescription);
		
		List<String> builtInCat = BuiltInCategories.getCategoriesList();
		inFields = builtInCat.subList(1, builtInCat.size());
	}
	
	private void createTopPanel(Composite parent){
		Composite topPanel = new Composite(parent, SWT.NONE);
		topPanel.setLayout(new GridLayout(2,false));
		
		new Label(topPanel,SWT.NONE).setText(Messages.CategoriesPage_typeLabel);
		
		engineCombo = new Combo(topPanel, SWT.READ_ONLY);

		
		List<TemplateProvider>  templateProviders = new ArrayList<TemplateProvider>();
		templateProviders.add(new DefaultTemplateProvider());
		templateProviders.addAll(JaspersoftStudioPlugin.getExtensionManager().getTemplateProviders());
		
		engineKeys = new String[templateProviders.size()];
		String[] engineNames = new String[templateProviders.size()];
		for(int i=0; i<templateProviders.size(); i++){
			TemplateProvider actualProvider = templateProviders.get(i);
			engineKeys[i] = actualProvider.getProviderKey();
			providersMap.put(engineKeys[i], actualProvider);
			engineNames[i] = actualProvider.getProviderName();
		}
		
		engineCombo.setItems(engineNames);
		engineCombo.select(0);
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayout(new GridLayout(1,false));
		setControl(panel);
	
		createTopPanel(panel);
		
		
		Composite composite = new Composite(panel, SWT.NONE);	
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label categoryDescriptionsLabel = new Label(composite, SWT.NONE);
		categoryDescriptionsLabel.setText(Messages.CategoriesPage_categoriesLabel);
		categoryDescriptionsLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		

		Composite leftPanel = new Composite(composite, SWT.NONE);
		
		GridLayout leftLayout = new GridLayout(2,false);
		leftLayout.horizontalSpacing = 0;
		leftLayout.verticalSpacing = 5;
		leftLayout.marginHeight = 0;
		leftLayout.marginWidth = 0;
		
		leftPanel.setLayout(leftLayout);
		leftPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		leftTable = new Table(leftPanel, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		gd.widthHint = 300;
		leftTable.setLayoutData(gd);
		leftTable.setHeaderVisible(true);

		TableColumn[] col = new TableColumn[1];
		col[0] = new TableColumn(leftTable, SWT.NONE);
		col[0].setText(Messages.CategoriesPage_availabelCatLabel);
		col[0].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		leftTable.setLayout(tlayout);
		
		final Text customCategory = new Text(leftPanel, SWT.BORDER);
		customCategory.setText(Messages.CategoriesPage_customCatBox);
		customCategory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		TableViewer leftTView = new TableViewer(leftTable);
		leftTView.setContentProvider(new ListContentProvider());
		setLabelProvider(leftTView);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		// -----------------------------------
		rightTable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumWidth = 300;
		rightTable.setLayoutData(gd);
		rightTable.setHeaderVisible(true);

		createColumns();

		final TableViewer rightTView = new TableViewer(rightTable);
		rightTView.setContentProvider(new ListContentProvider());
		setLabelProvider(rightTView);
		
		leftTView.setInput(inFields);
		rightTView.setInput(new ArrayList<String>());
		
		Button addCustomButton = new Button(leftPanel, SWT.NONE);
		addCustomButton.setText(Messages.CategoriesPage_addButton);
		addCustomButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unchecked")
				List<String> input = (List<String>)rightTView.getInput();
				input.add(customCategory.getText());
				rightTView.refresh();
			}
		});
		
		mt2t = new MoveT2TButtons();
		mt2t.createButtons(bGroup, leftTView, rightTView);
		
		// Add listener to check for changes in the list...
		mt2t.addChangeListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event) {
					storeSettings();
			}
		});
	}
	
	public void storeSettings()
	{
		categoriesSelected.clear();
		for(TableItem item : rightTable.getItems()){
			categoriesSelected.add(item.getData().toString());
		}
	}
	
	public void finish(String reportName, String destinationPath){
		 Properties props = new Properties();
		 props.setProperty(BuiltInCategories.NAME_KEY, reportName);
		 
		 String categories = ""; //$NON-NLS-1$
		 for(String cat : categoriesSelected){
			 categories = categories.concat(cat).concat(";"); //$NON-NLS-1$
		 }
		 if (!categories.isEmpty()) {
			 categories = categories.substring(0, categories.length()-1);
			 props.setProperty(BuiltInCategories.CATEGORY_KEY, categories);
		 }
		 
		 String engine = engineCombo != null ? engineKeys[engineCombo.getSelectionIndex()] : engineKeys[0];
		 props.setProperty(BuiltInCategories.ENGINE_KEY, engine);
		 
     String path = destinationPath.substring(0, destinationPath.lastIndexOf(".jrxml")); //$NON-NLS-1$
     path = path.concat("_descriptor.properties"); //$NON-NLS-1$
     File f = new File(path);
     OutputStream out;
		try {
			out = new FileOutputStream( f );
	     props.store(out,""); //$NON-NLS-1$
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Get a JasperDesign and check if that JasperDesign can be used as Template for the selected
	 * Template type
	 * 
	 * @param design the design to check
	 * @return a List of founded error, the list is void if no error are found
	 */
	public List<String> validateWithSelectedEngine(JasperDesign design){
		String engine = engineCombo != null ? engineKeys[engineCombo.getSelectionIndex()] : engineKeys[0];
		return providersMap.get(engine).validateTemplate(design);
	}
	
	protected void createColumns() {
		TableColumn[] col;
		TableLayout tlayout;
		col = new TableColumn[1];
		col[0] = new TableColumn(rightTable, SWT.NONE);
		col[0].setText(Messages.CategoriesPage_selectedCatLabel);
		col[0].pack();

		tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		rightTable.setLayout(tlayout);
	}
	
	protected void setLabelProvider(TableViewer tableViewer) {
		tableViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				if (MessagesByKeys.hasTranslation(element.toString())) return MessagesByKeys.getString(element.toString());
				else return element.toString();
			}
		});
	}

	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_EXPORTED_CATEGORY;
	}

}
