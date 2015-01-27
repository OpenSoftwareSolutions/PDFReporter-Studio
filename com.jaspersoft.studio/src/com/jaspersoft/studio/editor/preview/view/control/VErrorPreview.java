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
package com.jaspersoft.studio.editor.preview.view.control;

import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.xml.SourceLocation;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.editor.table.TableLabelProvider;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;
import com.jaspersoft.studio.property.SetExpressionValueCommand;
import com.jaspersoft.studio.property.descriptor.expression.dialog.JRExpressionEditor;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.utils.ErrorUtil;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class VErrorPreview extends APreview {

	private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

	public VErrorPreview(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	public void setReportContext(JasperReportsConfiguration jContext) {
		this.jContext = jContext;
	}

	private Label compilationTime;
	// private Label compilSubTime;
	private Label fillingTime;
	private Label exportTime;
	private Label execTime;
	private Label totalPages;
	private Label fillSize;
	private Label recordCount;
	private Text tmessage;
	private Text terror;
	private StackLayout stackLayout;
	private Composite body;
	private Composite errorsComposite;
	private Composite statComposite;
	private TableViewer errorViewer;
	private ArrayList<String> errorList;
	private ToolBarManager tbManager;
	private Action errAction;
	private Action statAction;
	private Action msgAction;
	private CTabFolder tabFolder;

	@Override
	public Control createControl(final Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		ToolBar topToolBar = new ToolBar(container, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		topToolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		tbManager = new ToolBarManager(topToolBar);
		msgAction = new Action("Console") { //$NON-NLS-1$
			@Override
			public void run() {
				stackLayout.topControl = tmessage;
				body.layout();
			}
		};
		tbManager.add(msgAction);
		errAction = new Action(Messages.VErrorPreview_errorsLabel) {
			@Override
			public void run() {
				stackLayout.topControl = errorsComposite;
				body.layout();
			}
		};

		tbManager.add(errAction);

		statAction = new Action(Messages.VErrorPreview_statisticsLabel) {
			@Override
			public void run() {
				stackLayout.topControl = statComposite;
				body.layout();
			}
		};
		tbManager.add(statAction);
		tbManager.update(true);
		topToolBar.pack();

		body = new Composite(container, SWT.NONE);
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
		stackLayout = new StackLayout();
		stackLayout.marginWidth = 0;
		stackLayout.marginHeight = 0;
		body.setLayout(stackLayout);

		createMessages(body);

		createErrors(body);

		createStatistics(body);

		stackLayout.topControl = tmessage;
		body.layout();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, "com.jaspersoft.studio.doc.view_reportstate"); //$NON-NLS-1$

		return container;
	}

	public void setFocus() {
		body.setFocus();
	}

	protected void createMessages(Composite composite) {
		tmessage = new Text(composite, SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		tmessage.setFont(FontUtils.getEditorsFont(jContext));
	}

	protected void createErrors(Composite composite) {
		errorsComposite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		errorsComposite.setLayout(layout);

		tabFolder = new CTabFolder(errorsComposite, SWT.BOTTOM);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		CTabItem itemTbl = new CTabItem(tabFolder, SWT.NONE);
		itemTbl.setText(Messages.VErrorPreview_tableLabel);

		final Table wtable = new Table(tabFolder, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION
				| SWT.BORDER);
		wtable.setLayoutData(new GridData(GridData.FILL_BOTH));
		wtable.setHeaderVisible(false);
		wtable.setLinesVisible(true);
		wtable.addListener(SWT.MouseDoubleClick, new Listener() {

			public void handleEvent(Event event) {
				int sindex = wtable.getSelectionIndex();
				if (sindex < 0 || sindex > errors.size())
					return;
				Object aux = auxil.get(sindex);
				if (aux != null && aux instanceof JRExpression) {
					JasperDesign jd = jContext.getJasperDesign();
					JRExpressionCollector rc = JRExpressionCollector.collector(jContext, jd);
					if (!openExpressionEditor(jContext, rc, (JRDesignDataset) jd.getMainDataset(), (JRDesignExpression) aux))
						for (JRDataset d : jd.getDatasetsList())
							if (openExpressionEditor(jContext, rc, (JRDesignDataset) d, (JRDesignExpression) aux))
								break;
				} else {
					Object e = errors.get(sindex);
					if (e instanceof Throwable)
						UIUtils.showError((Throwable) e);
				}
			}
		});

		wtable.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				int sindex = wtable.getSelectionIndex();
				if (sindex < 0 || sindex > errors.size())
					return;
				Object aux = auxil.get(sindex);
				if (aux != null) {
					if (aux instanceof JRDesignElement) {
						SelectionHelper.setSelection((JRDesignElement) aux, true);
					} else if (aux instanceof List<?>) {
						for (Object item : (List<?>) aux) {
							if (item instanceof JRDesignElement) {
								SelectionHelper.setSelection((JRDesignElement) item, true);
							}
						}
					}
				}
			}
		});

		TableColumn[] col = new TableColumn[1];
		col[0] = new TableColumn(wtable, SWT.NONE);
		col[0].setText(Messages.VErrorPreview_fieldNameLabel);
		col[0].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, true));
		wtable.setLayout(tlayout);

		errorViewer = new TableViewer(wtable);
		errorViewer.setContentProvider(new ListContentProvider());
		errorViewer.setLabelProvider(new TableLabelProvider() {
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/obj16/error_tsk.gif"); //$NON-NLS-1$
			}
		});

		CTabItem itemText = new CTabItem(tabFolder, SWT.NONE);
		itemText.setText(Messages.VErrorPreview_textLabel);
		terror = new Text(tabFolder, SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		itemText.setControl(terror);

		itemTbl.setControl(wtable);

		/**
		 * When the container is resized also the table column is resized to its maximum width
		 */
		tabFolder.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				refreshTableCellWidth();
			}
		});

		tabFolder.setSelection(itemTbl);
	}

	/**
	 * Set the width of the table column to the max width available
	 */
	private void refreshTableCellWidth() {
		Table wtable = errorViewer.getTable();
		TableColumn col = wtable.getColumns()[0];
		Rectangle area = tabFolder.getClientArea();
		Point preferredSize = wtable.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int width = area.width - 2 * wtable.getBorderWidth();
		if (preferredSize.y > area.height + wtable.getHeaderHeight()) {
			// Subtract the scrollbar width from the total column width
			// if a vertical scrollbar will be required
			Point vBarSize = wtable.getVerticalBar().getSize();
			width -= vBarSize.x;
		}
		Point oldSize = wtable.getSize();
		if (oldSize.x > area.width) {
			// table is getting smaller so make the columns
			// smaller first and then resize the table to
			// match the client area width
			col.setWidth(width);
			wtable.setSize(area.width, area.height);
		} else {
			// table is getting bigger so make the table
			// bigger first and then make the columns wider
			// to match the client area width
			wtable.setSize(area.width, area.height);
			col.setWidth(width);
		}
	}

	public static boolean openExpressionEditor(JasperReportsConfiguration jContext,
			JRExpressionCollector reportCollector, JRDesignDataset dataset, JRDesignExpression exp) {
		SelectionHelper.getActiveJRXMLEditor();

		JRExpressionCollector datasetCollector = reportCollector.getCollector(dataset);
		List<JRExpression> datasetExpressions = datasetCollector.getExpressions();
		for (JRExpression expr : datasetExpressions) {
			if (expr.getId() == exp.getId()) {
				if (!ExpressionEditorSupportUtil.isExpressionEditorDialogOpen()) {
					JRExpressionEditor wizard = new JRExpressionEditor();
					wizard.setExpressionContext(new ExpressionContext(dataset, jContext));
					wizard.setValue(exp);
					WizardDialog dialog = ExpressionEditorSupportUtil.getExpressionEditorWizardDialog(Display.getDefault()
							.getActiveShell(), wizard);
					if (dialog.open() == Dialog.OK) {
						JRExpression e = wizard.getValue();
						IEditorPart activeJRXMLEditor = SelectionHelper.getActiveJRXMLEditor();
						if (activeJRXMLEditor != null && activeJRXMLEditor instanceof JrxmlEditor) {
							JrxmlEditor editor = (JrxmlEditor) activeJRXMLEditor;
							CommandStack cs = (CommandStack) editor.getAdapter(CommandStack.class);
							if (cs != null) {
								cs.execute(new SetExpressionValueCommand((JRDesignExpression) expr, e.getText(), e.getValueClassName()));
								jContext.getJasperDesign().getEventSupport()
										.firePropertyChange(JasperDesign.PROPERTY_NAME, true, false);
							}
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	private void createStatistics(Composite parent) {
		statComposite = new Composite(parent, SWT.BORDER);
		statComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		statComposite.setBackgroundMode(SWT.INHERIT_FORCE);

		GridLayout layout = new GridLayout(3, false);
		layout.marginLeft = 20;
		layout.horizontalSpacing = 3;
		statComposite.setLayout(layout);

		// new Label(statComposite, SWT.NONE).setText("Subreport Compilation Time");
		//
		// compilSubTime = new Label(statComposite, SWT.BOLD);
		// compilSubTime.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		// com.jaspersoft.studio.utils.UIUtil.setBold(compilSubTime);
		// new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_secLabel);

		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_compilationTimeLabel);

		compilationTime = new Label(statComposite, SWT.BOLD);
		compilationTime.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		com.jaspersoft.studio.utils.UIUtil.setBold(compilationTime);
		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_secLabel);

		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_fillingTimeLabel);

		fillingTime = new Label(statComposite, SWT.BOLD);
		fillingTime.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		com.jaspersoft.studio.utils.UIUtil.setBold(fillingTime);
		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_secLabel);

		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_exectutionTimeLabel);

		execTime = new Label(statComposite, SWT.BOLD);
		execTime.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		com.jaspersoft.studio.utils.UIUtil.setBold(execTime);
		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_secLabel);

		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_exportTimeLabel);

		exportTime = new Label(statComposite, SWT.BOLD);
		exportTime.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		com.jaspersoft.studio.utils.UIUtil.setBold(exportTime);
		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_secLabel);

		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_totalPagesLabel);

		totalPages = new Label(statComposite, SWT.BOLD);
		totalPages.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		com.jaspersoft.studio.utils.UIUtil.setBold(totalPages);
		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_pagesLabel);

		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_processedRecordsLabel);

		recordCount = new Label(statComposite, SWT.BOLD);
		recordCount.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		com.jaspersoft.studio.utils.UIUtil.setBold(recordCount);
		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_recordsLabel);

		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_fillSizeLabel);

		fillSize = new Label(statComposite, SWT.BOLD);
		fillSize.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		com.jaspersoft.studio.utils.UIUtil.setBold(fillSize);
		new Label(statComposite, SWT.NONE).setText(Messages.VErrorPreview_bytesLabel);

		setStats(null);
	}

	public void setStats(Statistics stats) {
		if (compilationTime.isDisposed())
			return;
		if (stats != null) {
			// compilSubTime.setText(format(stats.getDuration(ReportControler.ST_COMPILATIONTIMESUBREPORT)));
			compilationTime.setText(format(stats.getDuration(ReportControler.ST_COMPILATIONTIME)));
			fillingTime.setText(format(stats.getDuration(ReportControler.ST_FILLINGTIME)));
			exportTime.setText(format(stats.getDuration(ReportControler.ST_EXPORTTIME)));
			execTime.setText(format(stats.getDuration(ReportControler.ST_REPORTEXECUTIONTIME)));

			totalPages.setText(Misc.nvl(stats.getValue(ReportControler.ST_PAGECOUNT), "0")); //$NON-NLS-1$
			recordCount.setText(Misc.nvl(stats.getValue(ReportControler.ST_RECORDCOUNTER), "-")); //$NON-NLS-1$
			fillSize.setText(Misc.nvl(stats.getValue(ReportControler.ST_REPORTSIZE), "0")); //$NON-NLS-1$
			statAction.run();
		} else {
			//			compilSubTime.setText("-"); //$NON-NLS-1$
			compilationTime.setText("-"); //$NON-NLS-1$
			fillingTime.setText("-"); //$NON-NLS-1$
			exportTime.setText("-"); //$NON-NLS-1$

			totalPages.setText("-"); //$NON-NLS-1$
			recordCount.setText("-"); //$NON-NLS-1$
			fillSize.setText("-"); //$NON-NLS-1$
		}
		statComposite.layout();
	}

	private static DecimalFormat df = new DecimalFormat("#.###");
	static {
		df.setRoundingMode(RoundingMode.HALF_UP);
	}

	public static String format(long time) {
		return df.format(((double) time) / 1000);
	}

	public void setMessage(String msg) {
		tmessage.setText(msg);
	}

	public void addMessage(String msg) {
		tmessage.setText(tmessage.getText() + msg + NL);
		// textSection.setText("Console: " + msg);
	}

	public void startMessage(String msg) {
		tmessage.setText(tmessage.getText() + msg); //$NON-NLS-1$
		// textSection.setText("Console: " + msg);
	}

	public void addError(Throwable t, JasperDesign design) {
		if (t != null) {
			if (t instanceof InvocationTargetException)
				t = t.getCause();
			String msg = terror.getText() + ErrorUtil.getStackTrace(t) + NL;
			terror.setText(terror.getText() + msg + NL); //$NON-NLS-1$
			// The only way we have to find a missing style error is to parse the error message for now
			String stylesErrorString = "Could not resolve style(s):";
			String m = t.getMessage();
			if (m != null && m.contains(stylesErrorString) && design != null) {
				String stylesNotFound = m.substring(m.indexOf(stylesErrorString) + stylesErrorString.length());
				String[] styleNames = stylesNotFound.split(",");
				HashSet<String> styles = new HashSet<String>();
				for (String name : styleNames)
					styles.add(name.trim());
				List<JRDesignElement> elements = getNotReferencedStyles(design.getAllBands(), styles);
				addError2List(t, m, elements);
			} else
				addError2List(t, m, null);
			// errorSection.setText("Errors: 1");
		} else
			terror.setText(""); //$NON-NLS-1$
		refreshErrorTable();
	}

	private List<JRDesignElement> getNotReferencedStyles(JRChild[] childs, HashSet<String> styles) {
		List<JRDesignElement> result = new ArrayList<JRDesignElement>();
		for (JRChild child : childs) {
			if (child instanceof JRDesignElement) {
				String styleName = getElementStyle((JRDesignElement) child);
				if (styleName != null && styles.contains(styleName)) {
					result.add((JRDesignElement) child);
				}
			}
			if (child instanceof JRElementGroup) {
				JRElementGroup group = (JRElementGroup) child;
				List<JRDesignElement> value = getNotReferencedStyles(group.getElements(), styles);
				result.addAll(value);
			}
		}
		return result;
	}

	private String getElementStyle(JRDesignElement jrElement) {
		if (jrElement.getStyleNameReference() != null)
			return jrElement.getStyleNameReference();
		JRStyle actualStyle = jrElement.getStyle();
		return actualStyle != null ? actualStyle.getName() : null;
	}

	protected void refreshErrorTable() {
		if (getErrorList().size() > 0)
			errAction.run();
		errAction.setText(Messages.VErrorPreview_errorsFoundLabel + getErrorList().size() + ")"); //$NON-NLS-2$
		errorViewer.refresh();
	}

	public void addProblem(IProblem problem, SourceLocation location) {
		addError2List(problem, problem.getMessage(), null);
		refreshErrorTable();
	}

	public void addProblem(IProblem problem, SourceLocation location, JRExpression expr) {
		addError2List(problem, problem.getMessage(), expr);
		refreshErrorTable();
	}

	public void addProblem(String message, SourceLocation location, JRDesignElement element) {
		addError2List(message, message, element);
		refreshErrorTable();
	}

	public void addProblem(String message, SourceLocation location) {
		addError2List(message, message, null);
		refreshErrorTable();
	}

	private List<Object> errors = new ArrayList<Object>();
	private List<Object> auxil = new ArrayList<Object>();

	private void addError2List(Object err, String message, Object aux) {
		errors.add(err);
		auxil.add(aux);
		if (message == null)
			message = Messages.VErrorPreview_noMessageLabel;
		String lines[] = message.split("\\r?\\n"); //$NON-NLS-1$
		if (lines.length > 0)
			message = lines[0];

		getErrorList().add(message);
	}

	private List<String> getErrorList() {
		if (errorList == null) {
			errorList = new ArrayList<String>();
			errorViewer.setInput(errorList);
		}
		return errorList;
	}

	public void clear() {
		auxil.clear();
		errors.clear();
		msgAction.run();
		tmessage.setText(""); //$NON-NLS-1$
		errorList = new ArrayList<String>();
		errorViewer.setInput(errorList);
		setStats(null);
		addError(null, null);
		refreshTableCellWidth();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
