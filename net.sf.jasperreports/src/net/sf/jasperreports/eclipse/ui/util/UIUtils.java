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
package net.sf.jasperreports.eclipse.ui.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.messages.Messages;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class UIUtils {

	// Placeholder for the SWT.SPACE constant, since it is not available in 3.6.x
	public static final char SWT_SPACE = ' ';

	public static void showError(final String message, final Throwable t) {
		t.printStackTrace();
		getDisplay().asyncExec(new Runnable() {
			public void run() {

				showErrorDialog(message, t);
			}

		});

	}

	public static void showErrorDialog(final String message, final Throwable t) {
		IStatus status = new OperationStatus(IStatus.ERROR, JasperReportsPlugin.getDefault().getPluginID(), OperationStatus.NOTHING_TO_REDO, message, t);
		ExceptionDetailsErrorDialog exceptionDialog = new ExceptionDetailsErrorDialog(getShell(), Messages.UIUtils_ExceptionTitle, Messages.UIUtils_ExceptionDetailsMsg, status, IStatus.OK | IStatus.INFO
				| IStatus.WARNING | IStatus.ERROR) {
			protected void setShellStyle(int newShellStyle) {
				super.setShellStyle(newShellStyle | SWT.SHEET);
			}

			@Override
			protected void populateList(Text listToPopulate, IStatus buildingStatus, int nesting, boolean includeStatus) {
				super.populateList(listToPopulate, buildingStatus, nesting, includeStatus);
				Throwable t = buildingStatus.getException();
				// Try to print the cause also in cases of CoreException when there is
				// not details text
				if (listToPopulate.getText().isEmpty() && t instanceof CoreException) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < nesting; i++) {
						sb.append(" ");
					}

					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					t.printStackTrace(pw);

					String message = sw.getBuffer().toString();
					if (message == null) {
						message = t.toString();
					}
					int causeIndex = message.indexOf("Caused by:");
					if (causeIndex != -1) {
						sb.append(message.substring(causeIndex));
						listToPopulate.append(sb.toString());
					}
				}
			}
		};
		exceptionDialog.open();
	}

	public static void showError(Throwable t) {
		showError(t.getMessage(), t);
	}

	public static void showWarning(final String message) {
		getDisplay().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.open(MessageDialog.WARNING, getShell(), Messages.UIUtils_Warning, message, SWT.SHEET);
			}
		});
	}

	/**
	 * @return true if yes
	 */
	public static boolean showConfirmation(String title, String message) {
		MessageDialog dialog = new MessageDialog(getShell(), title, null, message, MessageDialog.QUESTION, new String[] { Messages.UIUtils_AnswerYes, Messages.UIUtils_AnswerNo }, 0) {

			@Override
			protected void setShellStyle(int newShellStyle) {
				super.setShellStyle(newShellStyle | SWT.SHEET);
			}
		};
		return dialog.open() == 0;
	}

	/**
	 * @return true if yes
	 */
	public static boolean showDeleteConfirmation() {
		return showConfirmation(Messages.UIUtils_DeleteConfirmation.replace("&", ""), //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				Messages.UIUtils_ResourceDeleteConfirmationMsg);
	}

	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	public static void showInformation(final String message) {
		showInformation(Messages.UIUtils_InformationTitle, message);
	}

	public static void showInformation(final String title, final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.open(MessageDialog.INFORMATION, getShell(), title, message, SWT.SHEET);
			}
		});
	}

	/**
	 * Gets a valid {@link Display} instance trying the following steps:
	 * <ol>
	 * <li>get the current display from the UI thread if any;</li>
	 * <li>get the display from the running workbench;</li>
	 * <li>get a default display instance;</li>
	 * </ol>
	 * 
	 * @return a valid {@link Display} instance
	 */
	public static Display getDisplay() {
		// If we are in the UI Thread use that
		Display d = Display.getCurrent();
		if (d != null)
			return d;
		if (PlatformUI.isWorkbenchRunning())
			return PlatformUI.getWorkbench().getDisplay();
		d = Display.getDefault();
		if (d != null)
			return d;

		// Invalid thread access if it is not the UI Thread
		// and the workbench is not created.
		throw new SWTError(SWT.ERROR_THREAD_INVALID_ACCESS);
	}

	/**
	 * Gets a valid {@link Shell} instance trying the following steps:
	 * <ol>
	 * <li>get shell from the current active workbench window;</li>
	 * <li>get active shell from the display instance returned by
	 * {@link getDisplay};</li>
	 * </ol>
	 * 
	 * @return a valid {@link Shell} instance
	 */
	public static Shell getShell() {
		Shell shell = null;

		IWorkbenchWindow window = null;
		if (PlatformUI.isWorkbenchRunning()) {
			window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		}

		if (window != null) {
			shell = window.getShell();
		} else {
			shell = getDisplay().getActiveShell();
		}

		return shell;
	}

	/**
	 * Truncates a string to the specified max number of characters. Useful when
	 * composing a human-readable text for a {@link LabelProvider}.
	 * 
	 * @param str
	 *          the string to truncate
	 * @param maxChars
	 *          max chars
	 * @param suffix
	 *          the suffix to add if any, can be <code>null</code>
	 * @return
	 */
	public static String truncateStringForLabel(String str, int maxChars, String suffix) {
		Assert.isNotNull(str);
		String result = str.substring(0, Math.min(str.length(), maxChars));
		if (str.length() > maxChars) {
			result += (suffix != null) ? suffix : ""; //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * Resize the input shell and re-locates it on the center of the screen.
	 * 
	 * @param shell
	 *          the shell instance
	 * @param newWidth
	 *          the new width in px
	 * @param newHeight
	 *          the new height in px
	 */
	public static void resizeAndCenterShell(Shell shell, int newWidth, int newHeight) {
		shell.setSize(newWidth, newHeight);
		Rectangle bounds = shell.getDisplay().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	public static void relayoutDialog(Shell shell, int maxWidth, int maxHeight) {
		Point oldsize = shell.getSize();
		shell.layout();
		shell.pack();
		Point sizeAfterPack = shell.getSize();
		Point sizeToSet = new Point(sizeAfterPack.x, sizeAfterPack.y);

		Rectangle r = Display.getCurrent().getClientArea();
		sizeToSet.x = getMaxSize(maxWidth, oldsize.x, r.width, sizeAfterPack.x);
		sizeToSet.y = getMaxSize(maxHeight, oldsize.y, r.height, sizeAfterPack.y);

		shell.setSize(sizeToSet.x, sizeToSet.y);
		shell.layout();
	}

	private static int getMaxSize(int max, int old, int val, int afterPack) {
		switch (max) {
		case -1:
			return afterPack > val ? val : afterPack;
		case 0:
			return old;
		default:
			return afterPack > max ? max : afterPack;
		}
	}

	private static final PaletteData palette = new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF);

	public static Image awt2Swt(BufferedImage img) {
		DataBuffer buffer = img.getData().getDataBuffer();
		if (buffer instanceof DataBufferInt) {
			int[] data = ((DataBufferInt) buffer).getData();
			ImageData imageData = new ImageData(img.getWidth(), img.getHeight(), 32, palette);
			imageData.setPixels(0, 0, data.length, data, 0);
			return new Image(getDisplay(), imageData);
		} else if (buffer instanceof DataBufferByte) {
			byte[] data = ((DataBufferByte) buffer).getData();
			return new Image(getDisplay(), new ImageData(img.getWidth(), img.getHeight(), 32, palette, 4, data));
		}
		return null;
	}

}
