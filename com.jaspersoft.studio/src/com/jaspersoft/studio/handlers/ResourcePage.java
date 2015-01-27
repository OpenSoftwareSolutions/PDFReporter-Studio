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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.templates.JrxmlTemplateBundle;
import com.jaspersoft.studio.utils.ExpressionUtil;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;
import com.jaspersoft.templates.TemplateBundle;

/**
 * 
 * This is the page of the Template Export wizard that handle the recognition of 
 * the resource used by the report and the save of them along with the report
 * 
 * @author Orlandin Marco
 *
 */
public class ResourcePage extends JSSHelpWizardPage {
	
	/**
	 * The template bundle of the exported Report
	 */
	private TemplateBundle bundle;
	
	/**
	 * Table where the exported resources are listed
	 */
	private TableViewer tableViewer;
	
	/**
	 * Text field where the exporting path is shown
	 */
	private Text pathText;
	
	/**
	 * List of file where every file points a resource that must be exported
	 */
	private List<File> resourceList;
	
	/**
	 * The configuration of the report
	 */
	private JasperReportsConfiguration jrContext;
	
	/**
	 * The file of the exported report
	 */
	private IFile reportFile;
	
	/**
	 * Path inserted in the textarea when the dialog is advanced, this is done so the path
	 * can be read even when the control are disposed
	 */
	private String pathString;
	
	/**
	 * Boolean flag, true if there are conflict on the resource names, otherwise false
	 */
	private boolean conflictResources;
		
	/**
	 * Build the class
	 * 
	 * @param reportFile file of the report exported as template
	 */
	protected ResourcePage(IFile reportFile) {
		super("exportresources"); //$NON-NLS-1$
		setTitle(Messages.ResourcePage_pageTitle);
		setDescription(Messages.ResourcePage_pageDescription);
		conflictResources = false;
		this.reportFile = reportFile;
		setPageComplete(false);
		try{
			//Build the bundle and the jasper configuration
			this.jrContext = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), reportFile);
			this.bundle = new JrxmlTemplateBundle(reportFile.getLocationURI().toURL(), true, jrContext);
			jrContext.setJasperDesign(bundle.getJasperDesign());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_EXPORTED_RESOURCES;
	}

	/**
	 * Return the jasperdesign of the exported report
	 * 
	 * @return a not null JasperDesign
	 */
	public JasperDesign getDesign(){
		return bundle.getJasperDesign();
	}
	
	/**
	 * Return a full path that represent the destination on the filesystem of the template
	 * 
	 * @return a string that represent the destination of the template, it's included also the template
	 * name and extension
	 */
	public String getDestinationPath(){
		return pathString;
	}
	
	@Override
	public IWizardPage getNextPage() {
		pathString = pathText.getText();
		return super.getNextPage();
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new GridLayout());

		Table table = new Table(composite, SWT.V_SCROLL | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gd);
		table.setHeaderVisible(true);

		TableColumn[] col = new TableColumn[1];
		col[0] = new TableColumn(table, SWT.NONE);
		col[0].setText(Messages.ResourcePage_exportedResourceLabel.trim());

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(80, false));
		table.setLayout(tlayout);

		for (TableColumn c : col)
			c.pack();

		Composite pathComposite = new Composite(composite, SWT.NONE);
		pathComposite.setLayout(new GridLayout(3,false));
		gd = new GridData(GridData.FILL_BOTH);
		pathComposite.setLayoutData(gd);
		new Label(pathComposite,SWT.NONE).setText(Messages.ResourcePage_destinationFolderLabel);
		
		pathText = new Text(pathComposite, SWT.BORDER);
		pathText.setEditable(false);
		gd = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
		pathText.setLayoutData(gd);
		
		Button browseButton = new Button(pathComposite, SWT.NONE);
		browseButton.setText(Messages.ResourcePage_browseButton);
		browseButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(UIUtils.getShell(), SWT.SAVE);
		    fd.setText(Messages.ExportImageAction_saveDialogTitle);
		    String[] filterExt = { "*.jrxml" }; //$NON-NLS-1$
		    fd.setFileName(bundle.getLabel());
		    fd.setFilterExtensions(filterExt);
		    String selected = fd.open();
		    if (selected != null){
		    	pathText.setText(selected);
		    	if (!conflictResources) setPageComplete(true);
		    }
			}
		});
		
		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setLabelProvider(new TLabelProvider());

		fillData();
	}
	

	/**
	 * Display a error message when there are conflicts between the resources, in the detail
	 * section are listed the conflicted resources
	 * 
	 * @param conflicts List of all the resource with conflicts. The list must have a even number of elements
	 * and every element in a pair position is in conflict with the file in the following position
	 */
	private void createErrorMessage(List<String> conflicts){
		String conf = ""; //$NON-NLS-1$
		for(int i=0; i<conflicts.size(); i+=2){
			conf += conflicts.get(i).concat("\n").concat(conflicts.get(i+1)).concat("\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		IStatus status = new OperationStatus(IStatus.ERROR, JasperReportsPlugin
				.getDefault().getPluginID(), OperationStatus.NOTHING_TO_REDO,
				conf, null);
		new ConflictDetailsError(
				UIUtils.getShell(),
				Messages.ResourcePage_conflictTitle,
				Messages.ResourcePage_conflictMessage,
				status, IStatus.OK | IStatus.INFO | IStatus.WARNING
						| IStatus.ERROR) {
			protected void setShellStyle(int newShellStyle) {
				super.setShellStyle(newShellStyle | SWT.SHEET);
			}
		}.open();
	}
	
	/**
	 * Read from the report the list of the resources that it use and show them into
	 * a table. Search and identify also the conflicts between the resources
	 */
	public void fillData() {
		resourceList = getResourceNames();
		HashMap<String,File> resourceMap = new HashMap<String, File>();
		List<String> conflicts = new ArrayList<String>();
		for(File actualResource : resourceList){
			if (resourceMap.containsKey(actualResource.getName())) {
				File conflictFile = resourceMap.get(actualResource.getName());
				//Avoid to identify as a conflict the same resource used in different places
				if (!conflictFile.getAbsolutePath().equals(actualResource.getAbsolutePath())){
					conflicts.add(actualResource.getAbsolutePath());
					conflicts.add(conflictFile.getAbsolutePath());
				}
			} else resourceMap.put(actualResource.getName(), actualResource);
		}
		
		List<File> exportedResource = new ArrayList<File>();
		exportedResource.add(reportFile.getLocation().toFile());
		exportedResource.addAll(resourceList);
		tableViewer.setInput(exportedResource);
		tableViewer.refresh();
		//If there are conflict show an error message and disable the next button
		if (conflicts.size()>0){
			conflictResources = true;
			createErrorMessage(conflicts);
		}
	}

	/**
	 * Label provider for the table. Since the input are a list of file this extract 
	 * from the file a string, the full path in the file system, as content of the cell
	 * 
	 * @author Orlandin Marco
	 *
	 */
	class TLabelProvider extends CellLabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			File fr = (File) element;
			return fr.getAbsolutePath();
		}

		@Override
		public void update(ViewerCell cell) {
			cell.setText(cell.getElement().toString());
		}


		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}

		public int getToolTipDisplayDelayTime(Object object) {
			return 2000;
		}

		public int getToolTipTimeDisplayed(Object object) {
			return 5000;
		}
	}
	
	/**
	 * Given a report in the workspace and a resource name
	 * it return a file to that resource
	 * 
	 * @param file file of the report
	 * @param str name of the resource
	 * @return file to the resource in the filesystem
	 */
	protected File findFile(IFile file, String str) {
		if (str == null) return null;
		SimpleFileResolver fr = new SimpleFileResolver(
				Arrays.asList(new File[] {
						new File(file.getParent().getLocationURI()),
						new File("."), //$NON-NLS-1$
						new File(file.getProject().getLocationURI()) }));
		fr.setResolveAbsolutePath(true);
		return fr.resolveFile(str);
	}
	
	/**
	 * This method check that an expression has a text of type:
	 * 
	 * "filename"
	 * 
	 * if the format is different, or if filename does not exist in
	 * the current report directory, it returns null.
	 * 
	 * @param exp
	 * @return the correct filename
	 */
	private String evalResourceName(JRExpression exp)
	{
		return ExpressionUtil.cachedExpressionEvaluation(exp, jrContext);
	}
	
	
	/**
	 * Take the jasperdesign and then convert it into an XML string
	 */
	private String model2xml() {
		String xml = null;
		try {
			JasperDesign report = bundle.getJasperDesign();
			report.removeProperty(DataQueryAdapters.DEFAULT_DATAADAPTER);
			String version = jrContext.getProperty(StudioPreferencePage.JSS_COMPATIBILITY_VERSION, JRXmlWriterHelper.LAST_VERSION);
			xml = JRXmlWriterHelper.writeReport(jrContext, report, "UTF-8", version); //$NON-NLS-1$
		} catch (Throwable e) {
			UIUtils.showError(e);
		}
		return xml;
	}

	/**
	 * Copy a file from a source to a destination
	 * 
	 * @param inputFile Source File
	 * @param outputFile Destination file
	 * @throws IOException
	 */
	private void copyFile(File inputFile, File outputFile) throws IOException{
		FileUtils.copyFile(inputFile, outputFile);
	  //Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

	}
	
	/**
	 * Save the xml of the report on the disk
	 */
	private void saveReport() {
		// Check for function library static imports (see issue #0005771)
		// It's better to put the check here instead on the JRExpressionEditor dialog close.
		// This allow for example to "fix" the report, depending on the preference setting,
		// also when simply saving the JRXML file without having edited an expression.
		JasperDesign jd = bundle.getJasperDesign();

		if (jd != null){
			ExpressionEditorSupportUtil.updateFunctionsLibraryImports(jd, jrContext);
			rebindResources();
			try {
				File destination = new File(pathText.getText());
				FileUtils.writeStringToFile(destination, model2xml(), "UTF-8"); //$NON-NLS-1$
				String destinationPath = destination.getParent()+System.getProperty("file.separator"); //$NON-NLS-1$
				for(File resource : resourceList){
					copyFile(resource, new File(destinationPath.concat(resource.getName())));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method called to export the report and its resources
	 */
  public boolean finish() {
  	saveReport();
  	return true;
  }
  
	/**
	 * Change all the report expression to a resource (like a subreport or an image) assuming that the resource is in the same folder
	 * of the report. This can be an expensive operation, since the jrxml representing the template may be loaded in order locate all 
	 * the referenced resources. 
	 * 
	 */
	private void rebindResources() {

		List<JRDesignElement> list = ModelUtils.getAllGElements(bundle.getJasperDesign());

		for (JRDesignElement el : list) {
			if (el instanceof JRDesignImage) {
				JRDesignImage im = (JRDesignImage) el;
				String res = evalResourceName(im.getExpression());
				File resFile = findFile(reportFile, res);
				if (resFile != null) im.setExpression(new JRDesignExpression(resFile.getName()));
			}

			if (el instanceof JRDesignSubreport) {
				JRDesignSubreport sr = (JRDesignSubreport) el;
				String res = evalResourceName(sr.getExpression());
				if (res.endsWith(".jasper")) { //$NON-NLS-1$
					res = res.substring(0, res.length() - ".jasper".length()) + ".jrxml"; //$NON-NLS-1$ //$NON-NLS-2$
					File resFile = findFile(reportFile, res);
					sr.setExpression(new JRDesignExpression(resFile.getName()));
				}
			}

		}
		
		List<JRReportTemplate> templates = bundle.getJasperDesign().getTemplatesList();
		for (JRReportTemplate t : templates) {
			String res = evalResourceName(t.getSourceExpression());
			if (res != null && t instanceof JRDesignReportTemplate) {
				File resFile = findFile(reportFile, res);
				((JRDesignReportTemplate)t).setSourceExpression(new JRDesignExpression(resFile.getName()));
			}
		}
	}
	
	/**
	 * Return the File to all the resources referenced by this template. This can be an expensive operation, since the
	 * jrxml representing the template may be loaded in order locate all the referenced resources. In the returned list 
	 * are avoided duplicated files to the same resource
	 * 
	 */
	public List<File> getResourceNames() {

		List<File> resourceNames = new ArrayList<File>();
		
		//This set is used to avoid duplicated resources
		HashSet<String> alredyAddedElements = new HashSet<String>();

		List<JRDesignElement> list = ModelUtils.getAllGElements(bundle.getJasperDesign());
		
		for (JRDesignElement el : list) {
			if (el instanceof JRImage) {
				JRImage im = (JRImage) el;
				String res = evalResourceName(im.getExpression());
				File resFile = findFile(reportFile, res);
				if (resFile != null && !alredyAddedElements.contains(resFile.getAbsolutePath())) {
					resourceNames.add(resFile);
					alredyAddedElements.add(resFile.getAbsolutePath());
				}
			}

			if (el instanceof JRSubreport ) {
				JRSubreport sr = (JRSubreport) el;
				String res = evalResourceName(sr.getExpression());
				if (res != null && res.endsWith(".jasper")) { //$NON-NLS-1$
					res = res.substring(0, res.length() - ".jasper".length()) + ".jrxml"; //$NON-NLS-1$ //$NON-NLS-2$
					File resFile = findFile(reportFile, res);
					if (!alredyAddedElements.contains(resFile.getAbsolutePath())) {
						resourceNames.add(resFile);
						alredyAddedElements.add(resFile.getAbsolutePath());
					}
				}
			}

		}

		List<JRReportTemplate> templates = bundle.getJasperDesign().getTemplatesList();
		for (JRReportTemplate t : templates) {
			String res = evalResourceName(t.getSourceExpression());
			File resFile = findFile(reportFile, res);
			if (resFile != null && !alredyAddedElements.contains(resFile.getAbsolutePath())) {
				resourceNames.add(resFile);
				alredyAddedElements.add(resFile.getAbsolutePath());
			}
		}

		return resourceNames;
	}

}
