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

import java.io.File;
import java.net.URI;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.ExpressionUtil;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.AWizardNode;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.JSSWizardSelectionPage;
import com.jaspersoft.studio.wizards.ReportNewWizard;

public class NewSubreportPage extends JSSWizardSelectionPage implements IExpressionContextSetter {

	private Button radioButtonUseReport;
	private WTextExpression subreportExpressionEditor;
	private Button useReportB;
	private Button radioButtonNewReport;
	private ExpressionContext expContext;

	public static final int NEW_REPORT = 0;
	public static final int EXISTING_REPORT = 1;
	public static final int NO_REPORT = 2;

	public static final String SUBREPORT_PARAMETERS = "SUBREPORT_PARAMETERS"; //$NON-NLS-1$

	private int selectedOption = -1;
	private JRDesignExpression selectedSubreportExpression = null;

	public JRDesignExpression getSelectedSubreportExpression() {
		return selectedSubreportExpression;
	}

	private java.io.File selectedFile = null;

	public java.io.File getSelectedFile() {
		return selectedFile;
	}

	protected NewSubreportPage() {
		super("newsubreportpage"); //$NON-NLS-1$
		setTitle(Messages.common_subreport);
		setDescription(Messages.WizardNewSubreportPage_description);
		setImageDescriptor(MSubreport.getIconDescriptor().getIcon32());
	}

	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_SUBREPORT_SOURCE;
	}

	private MSubreport subreport;

	public MSubreport getSubreport() {
		return subreport;
	}

	public void setSubreport(MSubreport subreport) {
		this.subreport = subreport;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new FormLayout());

		radioButtonNewReport = new Button(composite, SWT.RADIO);
		FormData fd_newReport = new FormData();
		fd_newReport.top = new FormAttachment(0, 5);
		fd_newReport.left = new FormAttachment(0, 5);
		radioButtonNewReport.setLayoutData(fd_newReport);
		radioButtonNewReport.setText(Messages.NewSubreportPage_newReport_text);

		radioButtonUseReport = new Button(composite, SWT.RADIO);
		FormData fd_useReport = new FormData();
		fd_useReport.top = new FormAttachment(radioButtonNewReport, 20);
		fd_useReport.left = new FormAttachment(radioButtonNewReport, 0, SWT.LEFT);
		radioButtonUseReport.setLayoutData(fd_useReport);
		radioButtonUseReport.setText(Messages.NewSubreportPage_useReport_text);
		radioButtonUseReport.setSelection(true);
		setPageComplete(false);

		subreportExpressionEditor = new WTextExpression(composite, SWT.NONE) {
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				handleDataChanged();
			}
		};

		FormData fd_subreportExpressionEditor = new FormData();
		fd_subreportExpressionEditor.top = new FormAttachment(radioButtonUseReport, 6);
		fd_subreportExpressionEditor.right = new FormAttachment(100, -10);
		fd_subreportExpressionEditor.left = new FormAttachment(0, 27);
		subreportExpressionEditor.setLayoutData(fd_subreportExpressionEditor);

		if (expContext != null) {
			subreportExpressionEditor.setExpressionContext(expContext);
		}

		subreportExpressionEditor.addModifyListener(new ExpressionModifiedListener() {

			@Override
			public void expressionModified(ExpressionModifiedEvent event) {
				handleExpressionModified();
			}
		});

		radioButtonNewReport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setUseReportEnabled();
				if (radioButtonNewReport.getSelection()) {
					setSelectedNode(new AWizardNode() {
						public IWizard createWizard() {
							IWizard pwizard = NewSubreportPage.this.getWizard();
							ReportNewWizard w = new ReportNewWizard(pwizard, pwizard.getNextPage(NewSubreportPage.this));
							IWorkbench bench = PlatformUI.getWorkbench();
							IWorkbenchPage page = bench.getActiveWorkbenchWindow().getActivePage();
							w.init(bench, (IStructuredSelection) page.getSelection());
							return w;
						}
					});
					setPageComplete(true);
				}
			}
		});
		radioButtonUseReport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedNode(null);
				setUseReportEnabled();
				setPageComplete(!(subreportExpressionEditor.getExpression() == null || subreportExpressionEditor
						.getExpression().getText().isEmpty()));
			}
		});

		Button empty = new Button(composite, SWT.RADIO);
		Label label = new Label(subreportExpressionEditor, SWT.NONE);
		label.setLayoutData(new FormData());
		FormData fd_empty = new FormData();
		fd_empty.top = new FormAttachment(subreportExpressionEditor, 52);
		fd_empty.left = new FormAttachment(radioButtonNewReport, 0, SWT.LEFT);
		empty.setLayoutData(fd_empty);
		empty.setText(Messages.NewSubreportPage_empty_text);
		empty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedNode(null);
				setUseReportEnabled();
				setPageComplete(true);
				handleDataChanged();
			}
		});

		handleDataChanged();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "Jaspersoft.wizard");//$NON-NLS-1$

		useReportB = new Button(composite, SWT.PUSH);
		fd_subreportExpressionEditor.bottom = new FormAttachment(100, -168);
		FormData fd_useReportB = new FormData();
		fd_useReportB.top = new FormAttachment(subreportExpressionEditor, 6);
		fd_useReportB.right = new FormAttachment(subreportExpressionEditor, 0, SWT.RIGHT);
		useReportB.setLayoutData(fd_useReportB);
		useReportB.setText(Messages.NewSubreportPage_useReportB_text);
		useReportB.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				FilteredResourcesSelectionDialog wizard = new FilteredResourcesSelectionDialog(Display.getCurrent()
						.getActiveShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE) {

					@Override
					protected void configureShell(org.eclipse.swt.widgets.Shell shell) {
						super.configureShell(shell);
						PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, ContextHelpIDs.WIZARD_SUBREPORT_FILE);
					};
				};

				wizard.setInitialPattern("*.jrxml");//$NON-NLS-1$
				if (wizard.open() == Dialog.OK) {
					handleFileSelected((IFile) wizard.getFirstResult());

					// setUpSubreport((IFile) wizard.getFirstResult(), null);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void setUseReportEnabled() {
		boolean enabled = radioButtonUseReport.getSelection();
		subreportExpressionEditor.setEnabled(enabled);
		useReportB.setEnabled(enabled);
		handleDataChanged();
	}

	/**
	 * Set the selected file and update the expression
	 * 
	 * @param file
	 */
	protected void handleFileSelected(IFile file) {
		if (file == null) {
			setSelectedFile(null);
			return;
		}

		setSelectedFile(file.getFullPath().toFile());

		IFile contextfile = (IFile) ((JSSWizard) getWizard()).getConfig().get(FileUtils.KEY_FILE);

		String filepath = null;
		if (contextfile != null && file.getProject().equals(contextfile.getProject())) {
			filepath = file.getProjectRelativePath().toPortableString().replaceAll(file.getProject().getName() + "/", ""); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			filepath = file.getRawLocationURI().toASCIIString();
		}

		selectedSubreportExpression = new JRDesignExpression();

		if (filepath.toLowerCase().endsWith(".jrxml")) { //$NON-NLS-1$
			filepath = filepath.substring(0, filepath.lastIndexOf(".")) + ".jasper"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		selectedSubreportExpression.setText("\"" + filepath + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		subreportExpressionEditor.setExpression(selectedSubreportExpression);

		storeSettings();
	}

	/**
	 * Set the selected expression, and check if it is also a valid file...
	 */
	protected void handleExpressionModified() {

		setSelectedFile(null);
		JRDesignExpression exp = subreportExpressionEditor.getExpression();
		selectedSubreportExpression = exp;

		String fpath = ExpressionUtil.eval(exp, ((JSSWizard) getWizard()).getConfig());

		IFile contextfile = (IFile) ((JSSWizard) getWizard()).getConfig().get(FileUtils.KEY_FILE);
		try {
			IFile file = contextfile.getParent().getFile(new Path(fpath));
			if (file.exists()) {
				setSelectedFile(file.getFullPath().toFile());
			}
		} catch (Exception ex) {
		}

		if (selectedFile == null) {
			try {
				File f = new File(new URI(fpath));
				if (f.exists()) {
					setSelectedFile(f);
				}
			} catch (Exception ex) {
			}

		}

		storeSettings();

	}

	/*
	 * public void setUpSubreport(IFile file, JasperDesign newjd) { if (file == null) return; JRDesignExpression jre = new
	 * JRDesignExpression(); IFile contextfile = (IFile)
	 * subreport.getJasperConfiguration().get(IEditorContributor.KEY_FILE); String filepath = null; if (contextfile !=
	 * null && file.getProject().equals(contextfile.getProject())) { filepath =
	 * file.getProjectRelativePath().toPortableString().replaceAll(file.getProject().getName() + "/", ""); } else {
	 * filepath = file.getRawLocationURI().toASCIIString(); }
	 * 
	 * filepath = filepath.replaceAll(".jrxml", ".jasper"); jre.setText("\"" + filepath + "\"");//$NON-NLS-1$
	 * //$NON-NLS-2$ subreport.setPropertyValue(JRDesignSubreport.PROPERTY_EXPRESSION, jre);
	 * 
	 * JRDesignSubreport s = (JRDesignSubreport) subreport.getValue(); if (newjd == null) { InputStream in = null; try {
	 * in = file.getContents(); InputSource is = new InputSource(new InputStreamReader(in, "UTF-8"));
	 * 
	 * newjd = new JRXmlLoader(JasperReportsConfiguration.getJRXMLDigester()).loadXML(is); } catch (JRException e) {
	 * UIUtils.showError(e); } catch (CoreException e) { UIUtils.showError(e); } catch (UnsupportedEncodingException e) {
	 * UIUtils.showError(e); } catch (ParserConfigurationException e) { UIUtils.showError(e); } catch (SAXException e) {
	 * UIUtils.showError(e); } finally { if (in != null) try { in.close(); } catch (IOException e) { UIUtils.showError(e);
	 * } } } if (newjd != null) { List<JRParameter> prms = newjd.getParametersList(); for (JRParameter p : prms) { if
	 * (!p.isSystemDefined()) { JRDesignSubreportParameter sp = new JRDesignSubreportParameter(); sp.setName(p.getName());
	 * 
	 * JRDesignExpression jrep = new JRDesignExpression(); jrep.setText(""); sp.setExpression(jrep);
	 * 
	 * try { s.addParameter(sp); } catch (JRException e) { e.printStackTrace(); } } }
	 * 
	 * subreportExpressionEditor.setExpression(jre); } }
	 */

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
		if (subreportExpressionEditor != null) {
			subreportExpressionEditor.setExpressionContext(this.expContext);
		}
	}

	public void handleDataChanged() {
		setErrorMessage(null);
		setMessage(Messages.WizardNewSubreportPage_description);
		if (radioButtonUseReport.getSelection()) {
			boolean complete = !(subreportExpressionEditor.getExpression() == null || subreportExpressionEditor
					.getExpression().getText().isEmpty());
			if (!complete) {
				setErrorMessage(Messages.NewSubreportPage_pageError);
			}
			setPageComplete(complete);
		}

		storeSettings();
		fireChangeEvent();
	}

	/**
	 * Saves the local variables which hold the information provided by the user.
	 * 
	 */
	public void storeSettings() {
		selectedOption = 0;

		if (radioButtonNewReport.getSelection())
			selectedOption = NEW_REPORT;
		else if (radioButtonUseReport.getSelection()) {
			selectedOption = EXISTING_REPORT;
		} else {
			selectedOption = NO_REPORT;
		}
	}

	public int getSelectedOption() {
		return selectedOption;
	}

	private void setSelectedFile(File f) {
		if (f == selectedFile)
			return;
		if (f != null && selectedFile != null && f.equals(selectedFile))
			return;

		selectedFile = f;

		getSettings().remove(SUBREPORT_PARAMETERS);

		if (f != null) {
			// Let's extract the parameters from this file...
			if (!f.exists()) {
				// Change the extension to see if there is the source...
				String fname = f.getName();
				if (fname.endsWith(".jasper")) { //$NON-NLS-1$
					fname = fname.substring(0, fname.lastIndexOf(".")) + ".jrxml"; //$NON-NLS-1$ //$NON-NLS-2$

				} else if (fname.endsWith(".jrxml")) { //$NON-NLS-1$
					fname = fname.substring(0, fname.lastIndexOf(".")) + "..jasper"; //$NON-NLS-1$ //$NON-NLS-2$
				}

				f = new File(f.getParent(), fname);
			}

			if (f.exists()) {
				try {
					JasperReportsConfiguration jConfig = ((JSSWizard) getWizard()).getConfig();
					if (f.getName().endsWith(".jasper")) { //$NON-NLS-1$
						JasperReport report = (JasperReport) JRLoader.loadObject(jConfig, f);
						getSettings().put(SUBREPORT_PARAMETERS, report.getParameters());
					} else if (f.getName().endsWith(".jrxml")) { //$NON-NLS-1$
						JasperDesign jd = JRXmlLoader.load(jConfig, f);
						getSettings().put(SUBREPORT_PARAMETERS, jd.getParameters());
					}

				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

		}

		System.out.println("Set parameters to: " + getSettings().get(SUBREPORT_PARAMETERS)); //$NON-NLS-1$
	}

}
