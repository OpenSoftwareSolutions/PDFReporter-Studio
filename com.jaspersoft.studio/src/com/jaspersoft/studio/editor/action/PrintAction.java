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
package com.jaspersoft.studio.editor.action;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.net.URISyntaxException;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleGraphics2DReportConfiguration;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PrintFigureOperation;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

public class PrintAction extends WorkbenchPartAction {

	public PrintAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		PrinterData[] printers = Printer.getPrinterList();
		return printers != null && printers.length > 0;
	}

	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
	 */
	protected void init() {
		super.init();
		setText(GEFMessages.PrintAction_Label);
		setToolTipText(GEFMessages.PrintAction_Tooltip);
		setId(ActionFactory.PRINT.getId());
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		IWorkbenchPart workbenchPart = getWorkbenchPart();
		final JasperReportsContext jrContext = (JasperReportsContext) workbenchPart.getAdapter(JasperReportsContext.class);
		final JasperPrint jrPrint = (JasperPrint) workbenchPart.getAdapter(JasperPrint.class);
		if (jrPrint != null) {
			PrintDialog dialog = new PrintDialog(UIUtils.getShell(), SWT.NULL);
			final PrinterData data = dialog.open();
			if (data != null) {
				Job job = new Job("Printing the Report") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							printUsingSWT(monitor, jrContext, jrPrint, data);
						} finally {
							monitor.done();
						}
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.setPriority(Job.LONG);
				job.schedule();

			}
			// printUsingJR(jrContext, jrPrint, data);
		} else
			UIUtils.showInformation("Printing", "There is no Report to Print, or report has not finished to execute.");
	}

	protected void printUsingSWT(final IProgressMonitor monitor, final JasperReportsContext jrContext,
			final JasperPrint jrPrint, final PrinterData data) {
		PrintFigureOperation pfo = new PrintFigureOperation(new Printer(data), new RectangleFigure()) {
			private GC gc;

			@Override
			protected void printPages() {
				final Graphics graphics = getFreshPrinterGraphics();
				UIUtils.getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						IFigure figure = getPrintSource();
						figure.setFont(UIUtils.getShell().getFont());
						setupPrinterGraphicsFor(graphics, figure);

						Rectangle bounds = getPrintRegion();
						imgPage = new Image(null, bounds.width, bounds.height);
						gc = new GC(imgPage);
						SWTGraphics g = new SWTGraphics(gc);
						g.translate(bounds.x, bounds.y);

						figure.paint(g);
					}
				});
				int nrpages = jrPrint.getPages().size();
				switch (data.scope) {
				case PrinterData.ALL_PAGES:
					monitor.beginTask("Printing the Report", nrpages);
					for (int j = 0; j < nrpages; j++) {
						printPage(monitor, jrContext, jrPrint, graphics, j);
						if (monitor.isCanceled())
							break;
						monitor.internalWorked(1);
					}
					break;
				case PrinterData.PAGE_RANGE:
					monitor.beginTask("Printing the Report", data.endPage - data.startPage);
					for (int j = data.startPage - 1; j < data.endPage && j < nrpages; j++) {
						printPage(monitor, jrContext, jrPrint, graphics, j);
						if (monitor.isCanceled())
							break;
						monitor.internalWorked(1);
					}
					break;
				case PrinterData.SELECTION:
					monitor.beginTask("Printing the Report", 1);
					printPage(monitor, jrContext, jrPrint, graphics, 0);
					break;
				}

				gc.dispose();
				gc.dispose();
				imgPage.dispose();

			}

			private Image imgPage;

			protected void printPage(final IProgressMonitor monitor, final JasperReportsContext jrContext,
					final JasperPrint jrPrint, final Graphics graphics, int j) {
				monitor.subTask("Page:" + (j + 1));
				graphics.pushState();
				getPrinter().startPage();
				final Image srcImage = new Image(null, getNewImageData(jrContext, jrPrint, j));
				graphics.drawImage(srcImage, 0, 0);

				getPrinter().endPage();
				UIUtils.getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						graphics.popState();
						srcImage.dispose();
					}
				});
			}

			private BufferedImage awtImage;
			private ImageData imageData;

			protected ImageData getNewImageData(JasperReportsContext jrContext, JasperPrint jrPrint, int page) {
				if (awtImage == null) {
					int imgW = (int) jrPrint.getPageWidth();
					int imgH = (int) jrPrint.getPageHeight();
					if (jrPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
						imgW = jrPrint.getPageHeight();
						imgH = jrPrint.getPageWidth();
					}
					awtImage = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
					imageData = new ImageData(awtImage.getWidth(), awtImage.getHeight(), 32, palette);
				}
				Graphics2D g2d = (Graphics2D) awtImage.getGraphics();
				try {
					JRGraphics2DExporter exporter = new JRGraphics2DExporter(jrContext);
					exporter.setExporterInput(new SimpleExporterInput(jrPrint));
					SimpleGraphics2DReportConfiguration grxConfiguration = new SimpleGraphics2DReportConfiguration();

					SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
					output.setGraphics2D(g2d);
					exporter.setExporterOutput(output);

					grxConfiguration.setPageIndex(page);
					exporter.setConfiguration(grxConfiguration);

					exporter.exportReport();

					int[] data = ((DataBufferInt) awtImage.getData().getDataBuffer()).getData();
					imageData.setPixels(0, 0, data.length, data, 0);
				} catch (JRException e) {
					e.printStackTrace();
				} finally {
					g2d.dispose();
				}
				return imageData;// imageData.scaledTo(imgPage.getBounds().width, imgPage.getBounds().height);
			}

		};
		pfo.run(jrPrint.getName());
	}

	private static final PaletteData palette = new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF);

	protected void printUsingJR(JasperReportsContext jrContext, JasperPrint jrPrint, PrinterData data) {
		try {
			JRPrintServiceExporter pexp = new JRPrintServiceExporter(jrContext);

			// --- Set print properties
			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
			// printRequestAttributeSet.add(MediaSizeName.ISO_A4);
			printRequestAttributeSet.add(new Copies(data.copyCount));
			printRequestAttributeSet.add(data.orientation == PrinterData.LANDSCAPE ? OrientationRequested.LANDSCAPE
					: OrientationRequested.PORTRAIT);
			printRequestAttributeSet.add(data.collate ? SheetCollate.COLLATED : SheetCollate.UNCOLLATED);
			switch (data.duplex) {
			case PrinterData.DUPLEX_LONG_EDGE:
				printRequestAttributeSet.add(Sides.TWO_SIDED_LONG_EDGE);
				break;
			case PrinterData.DUPLEX_SHORT_EDGE:
				printRequestAttributeSet.add(Sides.TWO_SIDED_SHORT_EDGE);
				break;
			case PrinterData.DUPLEX_NONE:
				printRequestAttributeSet.add(Sides.ONE_SIDED);
				break;
			}
			if (data.printToFile) {
				try {
					printRequestAttributeSet.add(new Destination(new java.net.URI(data.fileName)));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			switch (data.scope) {
			case PrinterData.ALL_PAGES:
			case PrinterData.SELECTION:
				break;
			case PrinterData.PAGE_RANGE:
				printRequestAttributeSet.add(new PageRanges(data.startPage, data.endPage));
				break;
			}

			PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
			printServiceAttributeSet.add(new PrinterName(data.name, null));

			SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
			configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
			configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
			configuration.setDisplayPageDialog(false);
			configuration.setDisplayPageDialogOnlyOnce(false);
			configuration.setDisplayPrintDialog(false);

			PrintService[] service = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.PDF,
					printRequestAttributeSet);
			if (service.length > 0)
				configuration.setPrintService(service[0]);
			pexp.setConfiguration(configuration);

			pexp.setExporterInput(new SimpleExporterInput(jrPrint));

			pexp.exportReport();
		} catch (JRException e) {
			UIUtils.showError(e);
		}
	}
}
