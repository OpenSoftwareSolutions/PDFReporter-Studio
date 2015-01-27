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
package com.jaspersoft.studio.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.xml.SourceLocation;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.jaspersoft.studio.editor.preview.ReportStateView;
import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.control.VErrorPreview;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class Console {
	public static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	public static Console showConsole(String name, JasperReportsConfiguration jConfig) {
		MessageConsole myConsole = findConsole(name);
		final Console c = new Console(myConsole, jConfig);
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				c.showConsole();
			}
		});
		// ConsolePlugin.getDefault().getConsoleManager().showConsoleView(myConsole);
		return c;
	}

	public void showConsole() {
		// ConsolePlugin.getDefault().getConsoleManager().showConsoleView(myConsole);
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ReportStateView.ID);
		} catch (PartInitException e) {
			UIUtils.showError(e);
		}
	}

	private MessageConsole console;
	private List<VErrorPreview> ePreviews = new ArrayList<VErrorPreview>();
	private JasperReportsConfiguration jConfig;

	private Console(MessageConsole console, JasperReportsConfiguration jConfig) {
		this.console = console;
		this.jConfig = jConfig;
	}

	public void addErrorPreview(VErrorPreview ep) {
		if (ep != null) {
			ep.setReportContext(jConfig);
			ePreviews.add(ep);
		}
	}

	public void removeErrorPreview(VErrorPreview ep) {
		ePreviews.remove(ep);
	}

	public static Color REDCOLOR = Display.getDefault().getSystemColor(SWT.COLOR_RED);

	public void addError(final Throwable e, final JasperDesign design) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				MessageConsoleStream out = console.newMessageStream();
				Color color = out.getColor();
				out.setColor(color);
				out.println(ErrorUtil.getStackTrace(e) + "\n\r");
				out.setColor(REDCOLOR);
				for (VErrorPreview vep : ePreviews)
					vep.addError(e, design);
			}
		});
	}

	public void addMessage(final String message) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				MessageConsoleStream out = console.newMessageStream();
				out.println(message);
				for (VErrorPreview vep : ePreviews)
					vep.addMessage(message);
			}
		});
	}

	public void startMessage(final String message) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				MessageConsoleStream out = console.newMessageStream();
				out.print(message);
				for (VErrorPreview vep : ePreviews)
					vep.startMessage(message);
			}
		});
	}

	public void addProblem(final IProblem problem, final SourceLocation location) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				for (VErrorPreview vep : ePreviews)
					vep.addProblem(problem, location);
			}
		});
	}

	public void addProblem(final IProblem problem, final SourceLocation location, final JRExpression expr) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				for (VErrorPreview vep : ePreviews)
					vep.addProblem(problem, location, expr);
			}
		});
	}
	
	public void addProblem(final String message, final SourceLocation location, final JRDesignElement element) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				for (VErrorPreview vep : ePreviews)
					vep.addProblem(message, location, element);
			}
		});
	}

	public void addProblem(final String message, final SourceLocation location) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				for (VErrorPreview vep : ePreviews)
					vep.addProblem(message, location);
			}
		});
	}

	public void setStatistics(final Statistics stats) {
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				for (VErrorPreview vep : ePreviews)
					vep.setStats(stats);
			}
		});
	}

	public void clearConsole() {
		for (VErrorPreview vep : ePreviews)
			vep.setStats(null);
		console.clearConsole();
		for (VErrorPreview vep : ePreviews)
			vep.clear();
	}
}
