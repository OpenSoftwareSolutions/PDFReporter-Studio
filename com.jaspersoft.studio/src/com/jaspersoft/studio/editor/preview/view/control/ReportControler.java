/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.editor.preview.view.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jasperreports.eclipse.builder.JasperReportCompiler;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import net.sf.jasperreports.engine.fill.FillListener;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.adapter.DataAdapterParameterContributorFactory;
import com.jaspersoft.studio.editor.preview.IParametrable;
import com.jaspersoft.studio.editor.preview.PreviewContainer;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.editor.preview.actions.RunStopAction;
import com.jaspersoft.studio.editor.preview.input.BigNumericInput;
import com.jaspersoft.studio.editor.preview.input.BooleanInput;
import com.jaspersoft.studio.editor.preview.input.DateInput;
import com.jaspersoft.studio.editor.preview.input.IDataInput;
import com.jaspersoft.studio.editor.preview.input.ImageInput;
import com.jaspersoft.studio.editor.preview.input.LocaleInput;
import com.jaspersoft.studio.editor.preview.input.TextInput;
import com.jaspersoft.studio.editor.preview.input.TimeZoneInput;
import com.jaspersoft.studio.editor.preview.input.array.CollectionInput;
import com.jaspersoft.studio.editor.preview.input.map.MapInput;
import com.jaspersoft.studio.editor.preview.jive.Context;
import com.jaspersoft.studio.editor.preview.jive.JettyUtil;
import com.jaspersoft.studio.editor.preview.stats.RecordCountScriptletFactory;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.report.IJRPrintable;
import com.jaspersoft.studio.editor.preview.view.report.html.ABrowserViewer;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.execution.VirtualizerHelper;
import com.jaspersoft.studio.utils.Console;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ReportControler {

	public static final String ST_RECORDCOUNTER = "RECORDCOUNTER"; //$NON-NLS-1$

	public static final String ST_PAGECOUNT = "PAGECOUNT"; //$NON-NLS-1$

	public static final String ST_FILLINGTIME = "FILLINGTIME"; //$NON-NLS-1$

	public static final String ST_COMPILATIONTIME = "COMPILATIONTIME"; //$NON-NLS-1$
	public static final String ST_COMPILATIONTIMESUBREPORT = "COMPILATIONTIMESUBREPORT"; //$NON-NLS-1$

	public static final String ST_REPORTEXECUTIONTIME = "REPORTEXECUTIONTIME"; //$NON-NLS-1$

	public static final String FORM_SORTING = Messages.ReportControler_sortoptiontitle;
	public static final String FORM_BOOKMARKS = Messages.commons_bookmarks;
	public static final String FORM_EXPORTER = Messages.ReportControler_exportertitle;

	public static final String FORM_REPORT_PARAMETERS = Messages.ReportControler_reportparameterstitle;
	public static final String FORM_PARAMETERS = Messages.ReportControler_inputparameterstitle;

	public static List<IDataInput> inputs = new ArrayList<IDataInput>();
	static {
		inputs.add(new BooleanInput());
		inputs.add(new TextInput());
		inputs.add(new BigNumericInput());
		inputs.add(new DateInput());
		inputs.add(new LocaleInput());
		inputs.add(new TimeZoneInput());
		inputs.add(new ImageInput());
		inputs.add(new CollectionInput());
		inputs.add(new MapInput());
		List<IDataInput> ict = JaspersoftStudioPlugin.getInputControlTypeManager().getInputControlTypes();
		if (ict != null && !ict.isEmpty())
			inputs.addAll(ict);
	}

	private List<JRParameter> prompts;
	private Map<String, Object> jasperParameters;
	private LinkedHashMap<String, APreview> viewmap;
	private PreviewContainer pcontainer;
	private JasperReportsConfiguration jrContext;

	public ReportControler(PreviewContainer pcontainer, JasperReportsConfiguration jrContext) {
		this.pcontainer = pcontainer;
		setJrContext(jrContext);
	}

	public JasperReportsConfiguration getJrContext() {
		return jrContext;
	}

	public void setJrContext(JasperReportsConfiguration jrContext) {
		this.jrContext = jrContext;
		if (jrContext.getJasperDesign() != null)
			prompts = jrContext.getJasperDesign().getParametersList();
		setParameters();
		if (viewmap != null)
			fillForms();
	}

	public void resetParametersToDefault() {
		prmInput.setupDefaultValues();
		prmRepInput.setupDefaultValues();
	}

	private void setParameters() {
		jasperParameters = resetParameters(jasperParameters, jrContext);
	}

	public static Map<String, Object> resetParameters(Map<String, Object> jasperParameters,
			JasperReportsConfiguration jrContext) {
		jasperParameters = jrContext.getJRParameters();
		if (jasperParameters == null) {
			jasperParameters = new HashMap<String, Object>();
			setDefaultParameterValues(jasperParameters, jrContext);
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			List<JRParameter> prm = jrContext.getJasperDesign().getParametersList();
			for (JRParameter p : prm) {
				Object obj = jasperParameters.get(p.getName());
				if (p.getName().endsWith(JRScriptlet.SCRIPTLET_PARAMETER_NAME_SUFFIX))
					continue;
				if (p.getName().equals(JRParameter.REPORT_DATA_SOURCE))
					continue;
				if (p.getName().equals(JRParameter.REPORT_CONNECTION))
					continue;
				if (p.getName().startsWith("XML_") || p.getName().startsWith("MONDRIAN_") //$NON-NLS-1$ //$NON-NLS-1$
						|| p.getName().startsWith("XLSX_") || p.getName().startsWith("XLS_") //$NON-NLS-1$ //$NON-NLS-1$
						|| p.getName().startsWith("JSON_") || p.getName().startsWith("HIBERNATE_") //$NON-NLS-1$ //$NON-NLS-1$
						|| p.getName().startsWith("JPA_") || p.getName().startsWith("CSV_") //$NON-NLS-1$ //$NON-NLS-1$
						|| p.getName().contains("csv.source") || p.getName().startsWith("XMLA_")) //$NON-NLS-1$ //$NON-NLS-1$
					continue;
				try {
					if (obj != null && p.getValueClass().isAssignableFrom(obj.getClass()) && p.isForPrompting()) {
						map.put(p.getName(), obj);
					}
				} catch (Exception e) {
				}
			}
			jasperParameters.clear();
			jasperParameters.putAll(map);
		}
		jrContext.setJRParameters(jasperParameters);
		return jasperParameters;
	}

	private static void setDefaultParameterValues(Map<String, Object> jasperParameters,
			JasperReportsConfiguration jrContext) {
		jasperParameters.remove(JRDesignParameter.IS_IGNORE_PAGINATION);
		jasperParameters.remove(JRDesignParameter.REPORT_MAX_COUNT);
		jasperParameters.remove(JRDesignParameter.REPORT_LOCALE);
		jasperParameters.remove(JRDesignParameter.REPORT_TIME_ZONE);
	}

	public LinkedHashMap<String, APreview> createControls(Composite composite) {
		viewmap = new LinkedHashMap<String, APreview>();
		viewmap.put(FORM_PARAMETERS, new VParameters(composite, jrContext));
		viewmap.put(FORM_REPORT_PARAMETERS, new VReportParameters(composite, jrContext));
		viewmap.put(FORM_SORTING, new VSorting(composite, jrContext));
		viewmap.put(FORM_BOOKMARKS, new VBookmarks(composite, jrContext, pcontainer));
		viewmap.put(FORM_EXPORTER, new VExporter(composite, jrContext));
		return viewmap;
	}

	private VReportParameters prmRepInput;

	private void fillForms() {
		prmInput = (VParameters) viewmap.get(FORM_PARAMETERS);
		prmInput.createInputControls(prompts, jasperParameters);

		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				prmRepInput = (VReportParameters) viewmap.get(FORM_REPORT_PARAMETERS);
				prmRepInput.createInputControls(prompts, jasperParameters);

				VSorting vs = (VSorting) viewmap.get(FORM_SORTING);
				vs.setJasperReports(jrContext.getJasperDesign(), prompts, jasperParameters);
			}
		});

	}

	public void viewerChanged(APreview view) {
		VExporter vs = (VExporter) viewmap.get(FORM_EXPORTER);
		vs.setPreferencesPage(view);
	}

	private Console c;

	public void runReport() {
		VSimpleErrorPreview errorView = showErrorView(pcontainer);
		errorView.setMessage(Messages.ReportControler_generating);
		c = pcontainer.getConsole();

		c.showConsole();
		c.clearConsole();
		if (pcontainer.getMode().equals(RunStopAction.MODERUN_LOCAL))
			pcontainer.setJasperPrint(null, null);
		// jasperParameters.clear();
		fillError = null;
		stats = new Statistics();
		stats.startCount(ST_REPORTEXECUTIONTIME);
		c.addMessage(Messages.ReportControler_stats_start);

		pcontainer.setNotRunning(false);
		runJob(pcontainer);
	}

	public void finishReport(final PreviewContainer pcontainer) {
		if (compiler != null && ((JRErrorHandler) compiler.getErrorHandler()).hasErrors())
			finishNotCompiledReport();
		else
			finishCompiledReport(c, prmInput, pcontainer);
	}

	private void finishNotCompiledReport() {
		UIUtils.getDisplay().asyncExec(new Runnable() {

			public void run() {
				pcontainer.setNotRunning(true);
				if (pcontainer.getSite() instanceof MultiPageEditorSite) {
					MultiPageEditorPart mpe = ((MultiPageEditorSite) pcontainer.getSite()).getMultiPageEditor();
					IEditorPart[] editors = mpe.findEditors(mpe.getEditorInput());
					if (editors != null && editors.length > 0) {
						// Dialog, if not ..., it's not clear for the user that error happened
						UIUtils.showInformation(Messages.ReportControler_compilationerrors);

						mpe.setActiveEditor(editors[0]);
					}
				}
			}
		});
	}

	public static void finishCompiledReport(final Console c, final AVParameters prmInput, final PreviewJRPrint pcontainer) {
		UIUtils.getDisplay().syncExec(new Runnable() {

			public void run() {
				c.addMessage(Messages.ReportControler_msg_reportfinished);
				pcontainer.setNotRunning(true);
				boolean notprmfiled = !prmInput.checkFieldsFilled();
				if (notprmfiled) {
					c.addMessage(Messages.ReportControler_msg_fillparameters);
					VSimpleErrorPreview errorView = showErrorView(pcontainer);
					errorView.setMessage(Messages.ReportControler_msg_fillparameters);
					// UIUtils.showWarning(Messages.ReportControler_msg_fillparameters);
					prmInput.setFocus();
					prmInput.setDirty(true);
				}
				if (pcontainer.isHideParameters() && pcontainer instanceof IParametrable)
					((IParametrable) pcontainer).showParameters(notprmfiled);
			}
		});
	}

	private RecordCountScriptletFactory scfactory;

	private void runJob(final PreviewContainer pcontainer) {
		fillError = null;
		Job job = new Job(Messages.PreviewEditor_preview_a
				+ ": " + jrContext.getJasperDesign().getName() + Messages.PreviewEditor_preview_b) { //$NON-NLS-1$ 

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
				JasperDesign jd = null;
				try {
					Thread.currentThread().setContextClassLoader(jrContext.getClassLoader());

					setParameters();

					final IFile file = ((IFileEditorInput) pcontainer.getEditorInput()).getFile();

					monitor.beginTask(Messages.PreviewEditor_starting, IProgressMonitor.UNKNOWN);

					jd = jrContext.getJasperDesign();// ModelUtils.copyJasperDesign(jrContext.getJasperDesign());

					JasperReport jasperReport = compileJasperDesign(file, jd, monitor);

					if (jasperReport != null) {
						if (!prmInput.checkFieldsFilled())
							return Status.CANCEL_STATUS;

						setupDataAdapter(pcontainer);
						if (pcontainer.getMode().equals(RunStopAction.MODERUN_JIVE)) {
							runJive(pcontainer, file, jasperReport);
						} else {
							setupVirtualizer(jd);
							c.startMessage(Messages.ReportControler_msg_fillreports);

							setupRecordCounters();
							JaspersoftStudioPlugin.getExtensionManager().onRun(jrContext, jasperReport, jasperParameters);
							
							// We create the fillHandle to run the report based on the type of data adapter....
							AsynchronousFillHandle fh = AsynchronousFillHandle.createHandle(jrContext, jasperReport,
									new HashMap<String, Object>(jasperParameters));

							if (fillReport(fh, monitor, pcontainer) == Status.CANCEL_STATUS) {
								cancelMessage();
								return Status.CANCEL_STATUS;
							}
							doneMessage();
						}
					}
				} catch (final Throwable e) {
					errorMessage();
					showRunReport(c, pcontainer, e, jd);
				} finally {
					Thread.currentThread().setContextClassLoader(oldLoader);
					monitor.done();
					finishReport(pcontainer);
				}
				return Status.OK_STATUS;
			}

		};
		job.setPriority(Job.LONG);
		job.schedule();
	}

	protected void runJive(final PreviewContainer pcontainer, final IFile file, final JasperReport jasperReport) {
		JettyUtil.startJetty(file.getProject(), jrContext);
		UIUtils.getDisplay().syncExec(new Runnable() {

			public void run() {
				try {
					Map<String, Object> prm = new HashMap<String, Object>();

					prm.put(JettyUtil.PRM_JRPARAMETERS, jasperParameters);
					prm.put(JettyUtil.PRM_JASPERREPORT, jasperReport);

					UUID randomUUID = UUID.randomUUID();
					Context.putContext(randomUUID.toString(), prm);

					String url = JettyUtil.getURL(file, randomUUID.toString(), jrContext);
					ABrowserViewer jiveViewer = pcontainer.getJiveViewer();
					jiveViewer.setURL(url);
					pcontainer.getRightContainer().switchView(null, jiveViewer);

				} catch (Throwable e) {
					UIUtils.showError(e);
				}
			}
		});
	}

	private JasperReport compileJasperDesign(IFile file, JasperDesign jd, IProgressMonitor monitor) throws CoreException {
		// stats.startCount(ST_COMPILATIONTIMESUBREPORT);
		// CompileAction.doRun(jrContext, monitor, false);
		// stats.endCount(ST_COMPILATIONTIMESUBREPORT);
		stats.startCount(ST_COMPILATIONTIME);
		c.startMessage(Messages.ReportControler_msg_compiling);
		if (compiler == null) {
			compiler = new JasperReportCompiler();
			compiler.setErrorHandler(new JRMarkerErrorHandler(c, file));
			compiler.setProject(file.getProject());
		}
		((JRErrorHandler) compiler.getErrorHandler()).reset();
		JasperReport jasperReport = compiler.compileReport(jrContext, jd);// JasperCompileManager.getInstance(jrContext).compile(jd);
		stats.endCount(ST_COMPILATIONTIME);
		if (((JRErrorHandler) compiler.getErrorHandler()).hasErrors()) {
			UIUtils.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					VSimpleErrorPreview errorView = showErrorView(pcontainer);
					errorView.setMessage(Messages.ReportControler_compilationerrors);
				}
			});
		}
		doneMessage();
		return jasperReport;
	}

	public void cancelMessage() {
		c.addMessage(Messages.ReportControler_CANCELED);
	}

	public void doneMessage() {
		c.addMessage(Messages.ReportControler_done);
	}

	public void errorMessage() {
		c.addMessage(Messages.ReportControler_error);
	}

	private void setupVirtualizer(JasperDesign jd) {
		c.startMessage(Messages.ReportControler_msg_setvirtualizer);
		VirtualizerHelper.setVirtualizer(jd, jrContext, jasperParameters);
		doneMessage();
	}

	private void setupDataAdapter(final PreviewContainer pcontainer) throws JRException {
		c.startMessage(Messages.ReportControler_msg_setdataadapter);
		DataAdapterDescriptor daDesc = pcontainer.getDataAdapterDesc();
		if (daDesc != null)
			jasperParameters.put(DataAdapterParameterContributorFactory.PARAMETER_DATA_ADAPTER, daDesc.getDataAdapter());
		doneMessage();
	}

	public void stop() {
		if (pmonitor != null)
			pmonitor.setCanceled(true);
	}

	private IStatus fillReport(final AsynchronousFillHandle fh, IProgressMonitor monitor,
			final PreviewContainer pcontainer) throws Throwable {
		Assert.isTrue(fh != null);
		pmonitor = new SubProgressMonitor(monitor, IProgressMonitor.UNKNOWN,
				SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK);
		IStatus retstatus = Status.OK_STATUS;
		try {
			pmonitor.beginTask(Messages.PreviewEditor_fill_report, IProgressMonitor.UNKNOWN);
			fh.addFillListener((IJRPrintable) getDefaultViewer());
			PageGenerationListener pgListener = new PageGenerationListener();
			fh.addFillListener(pgListener);
			fh.addListener(pgListener);
			stats.startCount(ST_FILLINGTIME);
			fh.startFill();
			finished = true;
			while (finished && fillError == null) {
				if (pmonitor.isCanceled()) {
					fh.cancellFill();
					retstatus = Status.CANCEL_STATUS;
					break;
				}
				Thread.sleep(500);
				pmonitor.worked(10);
			}
			if (fillError != null)
				throw new JRException(fillError);
		} catch (OutOfMemoryError e) {
			pcontainer.setJasperPrint(stats, null);
			throw e;
		} catch (Throwable e) {
			handleFillException(e);
			throw e;
		} finally {
			pmonitor.done();
		}

		return retstatus;
	}

	class PageGenerationListener implements AsynchronousFilllListener, FillListener {
		JasperPrint jrPrint;
		private boolean refresh = false;

		@Override
		public void pageUpdated(JasperPrint arg0, int page) {
		}

		@Override
		public void pageGenerated(final JasperPrint arg0, final int page) {
			this.jrPrint = arg0;
			if (page == 0) {
				UIUtils.getDisplay().syncExec(new Runnable() {
					public void run() {
						refreshRightView();
					}
				});
			}
			// pcontainer.setJasperPrint(stats, arg0);
			if (refresh)
				return;
			refresh = true;
			UIUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					stats.endCount(ST_FILLINGTIME);
					stats.setValue(ST_PAGECOUNT, page);
					if (scfactory != null)
						stats.setValue(ST_RECORDCOUNTER, scfactory.getRecordCount());
					stats.endCount(ST_REPORTEXECUTIONTIME);
					c.setStatistics(stats);
					refresh = false;
				}
			});
		}

		public void reportFinished(final JasperPrint jPrint) {
			this.jrPrint = jPrint;
			finishUpdateViewer(pcontainer, jPrint);
		}

		public void reportFillError(Throwable t) {
			handleFillException(t);
		}

		public void reportCancelled() {
			if (jrPrint != null)
				finishUpdateViewer(pcontainer, jrPrint);
			c.addMessage(Messages.PreviewEditor_report_fill_canceled);
		}
	}

	private boolean finished = true;
	private Throwable fillError = null;

	private VParameters prmInput;

	private Statistics stats;

	public static final String ST_REPORTSIZE = "REPORTSIZE"; //$NON-NLS-1$

	public static final String ST_EXPORTTIME = "ST_EXPORTTIME"; //$NON-NLS-1$

	private JasperReportCompiler compiler;

	private IProgressMonitor pmonitor;

	private void handleFillException(Throwable t) {
		fillError = t;
	}

	protected void setupRecordCounters() {
		List<ScriptletFactory> extensions = new ArrayList<ScriptletFactory>();
		scfactory = new RecordCountScriptletFactory();
		extensions.add(scfactory);
		jrContext.setExtensions(ScriptletFactory.class, extensions);
	}

	private void finishUpdateViewer(final PreviewContainer pcontainer, final JasperPrint jPrint) {
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				stats.endCount(ST_FILLINGTIME);
				if (jPrint != null)
					stats.setValue(ST_PAGECOUNT, jPrint.getPages().size());
				if (scfactory != null)
					stats.setValue(ST_RECORDCOUNTER, scfactory.getRecordCount());
				stats.endCount(ST_REPORTEXECUTIONTIME);
				APreview pv = getDefaultViewer();
				if (pv instanceof IJRPrintable)
					try {
						((IJRPrintable) pv).setJRPRint(stats, jPrint, true);
						VBookmarks vs = (VBookmarks) viewmap.get(FORM_BOOKMARKS);
						vs.setJasperPrint(jPrint);
					} catch (Exception e) {
						e.printStackTrace();
					}

				pcontainer.setJasperPrint(stats, jPrint);
				c.setStatistics(stats);
				finished = false;
			}
		});
	}

	protected APreview getDefaultViewer() {
		APreview pv = pcontainer.getDefaultViewer();
		return pv;
	}

	public static void showRunReport(Console c, final PreviewJRPrint pcontainer, final Throwable e) {
		showRunReport(c, pcontainer, e, null);
	}

	public static void showRunReport(Console c, final PreviewJRPrint pcontainer, final Throwable e,
			final JasperDesign design) {
		c.addError(e, design);
		UIUtils.getDisplay().syncExec(new Runnable() {

			public void run() {
				VSimpleErrorPreview errorView = showErrorView(pcontainer);
				errorView.setMessage(Messages.ReportControler_generatingerror);
			}
		});
	}

	protected static VSimpleErrorPreview showErrorView(PreviewJRPrint pcontainer) {
		VSimpleErrorPreview errorView = pcontainer.getErrorView();
		pcontainer.getRightContainer().switchView(null, errorView);
		return errorView;
	}

	private void refreshRightView() {
		pcontainer.switchRightView(pcontainer.getDefaultViewer(), stats, pcontainer.getRightContainer());
	}
}
