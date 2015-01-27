/*******************************************************************************
 * Copyright (c) 2013 Samir Soyer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Samir Soyer - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.widgets.suggestion;

import org.eclipse.babel.editor.widgets.NullableText;
import org.eclipse.babel.editor.widgets.suggestion.exception.SuggestionErrors;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Child dialog of {@link SuggestionBubble}, which lets user to mark a part of
 * the suggestion and apply it to {@link Text}
 *
 * @author Samir Soyer
 *
 */
public class PartialTranslationDialog {

	private PopupDialog dialog;
	private Shell shell;
	private SuggestionBubble parent;
	private Composite composite;
	private Text textField;
	private final String FOOT_NOTE_1 = "Click for focus";
	private final String FOOT_NOTE_2 = "Mark the text, which will be used as translation, then click on 'Apply' button";
	private String infoText;
	private String text;
	private int orientation;
	private boolean win;
	private static int SHELL_STYLE;

	/**
	 * The constructor
	 *
	 * @param shell
	 *            is the shell of the SuggestionBubble that is parent of this
	 *            dialog
	 * @param parent
	 *            is the parent of this dialog.
	 */
	public PartialTranslationDialog(Shell shell, SuggestionBubble parent) {
		this.parent = parent;
		this.shell = shell;

		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			SHELL_STYLE = PopupDialog.INFOPOPUP_SHELLSTYLE;
			win = true;
		} else {
			SHELL_STYLE = PopupDialog.HOVER_SHELLSTYLE;
			win = false;
		}

	}

	private void createDialog(final int shellStyle) {
		// int shellStyle = PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE;
		boolean takeFocusOnOpen = false;
		boolean persistSize = false;
		boolean persistLocation = false;
		boolean showDialogMenu = false;
		boolean showPersistActions = false;
		String titleText = null;
		dialog = new PopupDialog(shell, shellStyle, takeFocusOnOpen,
				persistSize, persistLocation, showDialogMenu,
				showPersistActions, titleText, infoText) {

			@Override
			protected Control createDialogArea(Composite parent) {
				composite = (Composite) super.createDialogArea(parent);

				composite.setLayout(new GridLayout(2, false));

				final Button button = new Button(composite, SWT.PUSH);
				button.setText("Apply");
				button.setEnabled(false);
				button.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						NullableText nText = (NullableText) PartialTranslationDialog.this.parent
								.getTextField().getParent();

						nText.setText(
								PartialTranslationDialog.this.parent
										.getTextField().getText()
										+ textField.getSelectionText(), true);

						PartialTranslationDialog.this.parent.dispose();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}

				});

				Label label = new Label(composite, SWT.NONE);
				label.setText("Selected translation");

				FontData fontData = label.getFont().getFontData()[0];
				Font font = new Font(label.getDisplay(), new FontData(
						fontData.getName(), fontData.getHeight(), SWT.BOLD));
				label.setFont(font);

				// Invisible separator
				new Label(composite, SWT.NONE);

				textField = new Text(composite, SWT.V_SCROLL | SWT.WRAP
						| SWT.MULTI | SWT.READ_ONLY | orientation);
				textField.setText(text);
				textField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						true, 2, 1));
				textField.addListener(SWT.MouseUp, new Listener() {
					@Override
					public void handleEvent(Event event) {
						Text text = (Text) event.widget;

						String selection = text.getSelectionText();

						if (selection.length() > 0
								&& !SuggestionErrors.contains(textField
										.getText())) {
							button.setEnabled(true);
						} else {
							button.setEnabled(false);
						}
					}
				});

				Listener scrollBarListener = new Listener() {
					@Override
					public void handleEvent(Event event) {
						Text t = (Text) event.widget;
						Rectangle r1 = t.getClientArea();
						Rectangle r2 = t.computeTrim(r1.x, r1.y, r1.width,
								r1.height);
						Point p = t.computeSize(composite.getSize().x,
								SWT.DEFAULT, true);
						t.getVerticalBar().setVisible(r2.height <= p.y);
					}
				};

				textField.addListener(SWT.Resize, scrollBarListener);

				return composite;
			}

			@Override
			protected void adjustBounds() {
				super.adjustBounds();

				Point start = parent.getCurrentLocation();
				Point size = parent.getSize();

				int x = start.x + size.x;
				int y = start.y;
				int screenWidth = Display.getCurrent().getBounds().width;

				if (screenWidth - x <= 200) {
					x = start.x - 450;
				}

				getShell().setLocation(x, y);

				if (screenWidth - x < 450) {
					getShell().setSize(screenWidth - x, 200);
				} else {
					getShell().setSize(450, 200);
				}
			}

			@Override
			protected void configureShell(Shell shell) {
				super.configureShell(shell);

				if (win) {
					shell.addFocusListener(new FocusListener() {

						@Override
						public void focusGained(FocusEvent e) {
							if (shellStyle == INFOPOPUPRESIZE_SHELLSTYLE
									|| dialog == null) {
								return;
							}
							dialog.close();
							infoText = FOOT_NOTE_2;
							createDialog(PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE);
							dialog.open();
							dialog.getShell().setFocus();
						}

						@Override
						public void focusLost(FocusEvent e) {
						}
					});
				}
			}
		};
	}

	/**
	 * @return location of this dialog relative to display
	 */
	public Point getLocation() {
		return dialog.getShell().toDisplay(1, 1);
	}

	/**
	 * @return size if this dialog
	 */
	public Point getSize() {
		return dialog.getShell().getSize();
	}

	/**
	 * Creates a new dialog. If it is already created, it updates its text.
	 *
	 * @param text
	 *            is the string to displayed in the dialog
	 * @param orientation
	 *            is the text alignment for the specific language/locale, which
	 *            should be either <code>SWT.LEFT_TO_RIGHT</code> or
	 *            <code>SWT.RIGHT_TO_LEFT</code>.
	 */
	public void openDialog(String text, int orientation) {
		if (SuggestionErrors.contains(text)) {
			return;
		}

		this.text = text;
		this.orientation = orientation;
		if (dialog != null && dialog.getShell() != null) {
			textField.setText(text);
		} else {
			infoText = FOOT_NOTE_1;
			createDialog(SHELL_STYLE);
			dialog.open();
		}

	}

	/**
	 * Disposes this dialog.
	 */
	public void dispose() {
		if (dialog != null) {
			dialog.close();
		}
	}

	/**
	 * @return true if mouse cursor is in the bounds of dialog, otherwise false.
	 */
	public boolean isCursorInsideDialog() {
		if (dialog == null || dialog.getShell() == null
				|| dialog.getShell().isDisposed()) {
			return false;
		}

		Display d = Display.getCurrent();
		if (d == null) {
			d = Display.getDefault();
		}

		Point start = dialog.getShell().getLocation();
		Point size = dialog.getShell().getSize();
		Point end = new Point(size.x + start.x, size.y + start.y);

		if ((d.getCursorLocation().x > end.x || d.getCursorLocation().x < start.x)
				|| (d.getCursorLocation().y > end.y || d.getCursorLocation().y < start.y)) {
			return false;
		}
		return true;
	}

	/**
	 * @return true if dialog is already created and visible, otherwise false.
	 */
	public boolean isVisible() {
		if (dialog != null && dialog.getShell() != null) {
			return true;
		}
		return false;
	}
}
