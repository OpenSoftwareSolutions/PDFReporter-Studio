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
package com.jaspersoft.studio.model.subreport.command.wizard;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.property.dataset.wizard.WizardConnectionPage;
import com.jaspersoft.studio.property.dataset.wizard.WizardDataSourcePage;
import com.jaspersoft.studio.property.descriptor.subreport.parameter.dialog.SubreportPropertyPage;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.ReportNewWizard;

public class SubreportWizard extends JSSWizard {
	private NewSubreportPage step0;
	private WizardConnectionPage step2;
	private SubreportPropertyPage step3;
	private MSubreport subreport;
//	private MDatasetRun mdataset;

	public SubreportWizard() {
		super();
		setWindowTitle(Messages.common_subreport);
	}

	@Override
	public void addPages() {
		this.subreport = new MSubreport();
		subreport.setValue(subreport.createJRElement(getConfig().getJasperDesign()));
		subreport.setPropertyValue(JRDesignSubreport.PROPERTY_CONNECTION_EXPRESSION, "$P{REPORT_CONNECTION}");

		step0 = new NewSubreportPage();
		step0.setSubreport(subreport);
		addPage(step0);

		subreport.setJasperConfiguration(getConfig());
		
		step2 = new WizardConnectionPage();
		addPage(step2);
		
		step3 = new SubreportPropertyPage();
		addPage(step3);

		// Setting up the expressions context. This is not really useful, since
		// the subreport has not been added to the report yet and it will be fallback to the default dataset.
		// FIXME: pass a proper ANode to the wizard to let the code to lookup for a more appropriate dataset.
		ExpressionContext ec = ModelUtils.getElementExpressionContext((JRDesignElement)subreport.getValue(), subreport);
		step0.setExpressionContext(ec);
		step2.setExpressionContext(ec);
		// FIXME: add support to the step3 for setting the expression context
		//step3.setExpressionContext(ec);
		
	}

	/**
	 * The getNextPage implementations does nothing, since all the logic has
	 * been moved inside each page, specifically extended for
	 * this wizard
	 * 
	 * @see com.jaspersoft.studio.wizards.JSSWizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 *
	 * @param the current page.
	 *
	 * @return the next page
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		// Nothing to do. If you change this method, please update the
		// comment.
		
		return super.getNextPage(page);
	}

	@Override
	public boolean performFinish() {
		for (IWizard w : getChildWizards())
		{
			if (w instanceof ReportNewWizard){
				((ReportNewWizard) w).getSettings().put(WizardDataSourcePage.EXTRA_PARAMETERS, step3.getValue());
			}
			w.performFinish();
		}
		return true;
	}

	/**
	 * Retutn the subreport object...
	 * 
	 * @return
	 */
	public MSubreport getSubreport() {
		
		JRSubreportParameter[] map = step3.getValue();
		
		if (map != null)
			subreport.setPropertyValue(JRDesignSubreport.PROPERTY_PARAMETERS, map);

		// Configure connection expression...
		JRDesignDatasetRun datasetRun = step2.getJRDesignDatasetRun();

		subreport.setPropertyValue(JRDesignSubreport.PROPERTY_PARAMETERS_MAP_EXPRESSION,
				datasetRun.getParametersMapExpression());
		subreport.setPropertyValue(JRDesignSubreport.PROPERTY_CONNECTION_EXPRESSION,
				datasetRun.getConnectionExpression());
		subreport.setPropertyValue(JRDesignSubreport.PROPERTY_DATASOURCE_EXPRESSION,
				datasetRun.getDataSourceExpression());
		
		// Create the subreport expression....
		if (step0.getSelectedOption() == NewSubreportPage.EXISTING_REPORT)
		{
			subreport.setPropertyValue( JRDesignSubreport.PROPERTY_EXPRESSION ,  step0.getSelectedSubreportExpression() );
		}
		else if (step0.getSelectedOption() == NewSubreportPage.NEW_REPORT)
		{
			// In this case the new report has been created by using a report wizard
			// which stores the location of a file in the
			// wizard settings...
			IPath path = (IPath) getSettings().get(JSSWizard.FILE_PATH);
			String fname = (String) getSettings().get(JSSWizard.FILE_NAME);
			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(path);
			IFile file = ((IContainer)resource).getFile(new Path(fname));
			
			IFile contextfile = (IFile) getConfig().get(FileUtils.KEY_FILE);
			
			String filepath = "";
			if (contextfile != null && file.getProject().equals(contextfile.getProject())) {
				filepath = file.getProjectRelativePath().toPortableString().replaceAll(file.getProject().getName() + "/", "");
			} else {
				filepath = file.getRawLocationURI().toASCIIString();
			}
			if (filepath.toLowerCase().endsWith(".jrxml"))
			{
				filepath = filepath.substring(0,filepath.lastIndexOf(".")) + ".jasper";
			}
			JRDesignExpression exp = new JRDesignExpression();
			exp.setText("\"" + filepath + "\""); //$NON-NLS-1$ $NON-NLS-1$
			
			/*if (map.length>0)
			{
				try {
					JasperReportsConfiguration jrContext = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), file);
					JasperDesign jd = new JRXmlLoader(JasperReportsConfiguration.getJRXMLDigester()).loadXML(new InputSource(file.getContents()));
					jrContext.setJasperDesign(jd);
					for(JRSubreportParameter param : map){
						if (!jd.getParametersMap().containsKey(param.getName())){
							JRDesignParameter newParam = new JRDesignParameter();
							newParam.setName(param.getName());
							newParam.setDefaultValueExpression(param.getExpression());
							jd.addParameter(newParam);
						}
					}
					JRXmlWriter.writeReport(jd, file.getProjectRelativePath().toPortableString(), "UTF-8");
				} catch (Exception e) {			}
			}*/
			subreport.setPropertyValue( JRDesignSubreport.PROPERTY_EXPRESSION , exp ); 
		}
		return subreport;
	}

	@Override
	public void init(JasperReportsConfiguration jd) {
		setConfig(jd);
		if (subreport != null)
			subreport.setJasperConfiguration(jd);
		//if (mdataset != null)
		//	mdataset.setJasperConfiguration(jd);
	}

	@Override
	public boolean canFinish() {
		
		if (step0.getSelectedOption() == NewSubreportPage.NEW_REPORT)
		{
			return getContainer().getCurrentPage() == step3;
		}
		return super.canFinish();
	}
	
	
}
