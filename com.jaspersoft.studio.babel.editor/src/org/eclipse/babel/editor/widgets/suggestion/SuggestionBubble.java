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

import java.awt.Frame;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.babel.editor.widgets.NullableText;
import org.eclipse.babel.editor.widgets.suggestion.exception.SuggestionErrors;
import org.eclipse.babel.editor.widgets.suggestion.filter.SuggestionFilter;
import org.eclipse.babel.editor.widgets.suggestion.model.Suggestion;
import org.eclipse.babel.editor.widgets.suggestion.provider.ISuggestionProvider;
import org.eclipse.babel.editor.widgets.suggestion.provider.ISuggestionProviderListener;
import org.eclipse.babel.editor.widgets.suggestion.provider.SuggestionProviderUtils;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

/**
 * Auto complete pop-up dialog that displays translation suggestions from a
 * given text to a target language. Detecting the source language depends on the
 * implementation of {@link ISuggestionProvider}.
 *
 * @author Samir Soyer
 */
public class SuggestionBubble implements ISuggestionProviderListener {

	private PopupDialog dialog;
	private TableViewer tableViewer;
	private Text text;
	private Shell shell;
	private Point caret;
	private SuggestionFilter suggestionFilter;
	private Composite composite;
	private ScrolledComposite scrollComposite;
	private Label noSug;
	private PartialTranslationDialog partialTranslationDialog;
	private ArrayList<Suggestion> suggestions;
	// private static ArrayList<ISuggestionProvider> suggestionProviders;
	private String targetLanguage;
	private String oldDefaultText = "";
	private static String defaultText;
	private static boolean win;
	private String SRC_LANG = "EN";
	private static int SHELL_STYLE;
	private final String CONTENT_ASSIST;
	private final Level LOG_LEVEL = Level.INFO;

	private static final Logger LOGGER = Logger
			.getLogger(SuggestionBubble.class.getName());

	/**
	 * Constructor
	 *
	 * @param parent
	 *            is the parent {@link Text} object, to which SuggestionBubble
	 *            will be added.
	 * @param targetLanguage
	 *            is the language, to which the
	 *            {@link SuggestionBubble.defaultText} will be translated
	 */
	public SuggestionBubble(Text parent, String targetLanguage) {
		shell = parent.getShell();
		text = parent;
		this.targetLanguage = targetLanguage;

		suggestionFilter = new SuggestionFilter();
		suggestions = new ArrayList<Suggestion>();

		String srcLang = System
				.getProperty("tapiji.translator.default.language");
		if (srcLang != null) {
			SRC_LANG = srcLang.substring(0, 2).toUpperCase();
		}

		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			SHELL_STYLE = PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE;
			win = true;
		} else {
			SHELL_STYLE = PopupDialog.HOVER_SHELLSTYLE;
			win = false;
		}

		// MessagesEditorPlugin.getDefault().getBundle().
		// getEntry("glossary.xml").getPath()
		// System.out.println("install path "+MessagesEditorPlugin.getDefault().getBundle().getEntry("/").getPath()+"glossary.xml");

		SuggestionProviderUtils.addSuggestionProviderUpdateListener(this);

		/*
		 * Read shortcut of content assist (code completion) directly from
		 * org.eclipse.ui.IWorkbenchCommandConstants.EDIT_CONTENT_ASSIST and
		 * save it to CONTENT_ASSIST final variable
		 */
		IBindingService bindingService = (IBindingService) PlatformUI
				.getWorkbench().getAdapter(IBindingService.class);

		CONTENT_ASSIST = bindingService
				.getBestActiveBindingFormattedFor(IWorkbenchCommandConstants.EDIT_CONTENT_ASSIST);

		init();
	}

	/**
	 * @return default text i.e source text that is being localized
	 */
	public static String getDefaultText() {
		return defaultText;
	}

	/**
	 * @param defaultText
	 *            is the source text that is being localized.
	 */
	public static void setDefaultText(String defaultText) {
		SuggestionBubble.defaultText = defaultText;
	}

	private void updateSuggestions() {
		if (!oldDefaultText.equals(defaultText)) {

			ArrayList<ISuggestionProvider> providers = SuggestionProviderUtils
					.getSuggetionProviders();

			LOGGER.log(LOG_LEVEL, "size of suggestions: " + suggestions.size()
					+ ", size of providers: " + providers.size());

			suggestions.clear();

			final Display d = Display.getCurrent();
			for (final ISuggestionProvider provider : providers) {

				Thread fetch = new Thread() {
					Composite loadingCircle;

					@Override
					public void run() {
						if (!d.isDisposed()) {
							d.asyncExec(new Runnable() {
								@Override
								public void run() {

									// Show circle
									if (!composite.isDisposed()) {
										loadingCircle = createLoadingCircle();
									}
								}
							});
						}

						// Do the work
						suggestions.add(provider.getSuggestion(defaultText,
								targetLanguage));

						if (!d.isDisposed()) {
							d.asyncExec(new Runnable() {
								@Override
								public void run() {

									// remove laoding circle
									if (!composite.isDisposed()) {
										loadingCircle.dispose();
										tableViewer.setInput(suggestions
												.toArray());
										pack();
										composite.layout();
									}
								}
							});
						}
					}
				};
				fetch.start();
			}
			oldDefaultText = defaultText;
		} else {
			tableViewer.setInput(suggestions.toArray());
			pack();
			composite.layout();
		}
	}

	/**
	 * @return true if this SuggestionBubble is created, i.e if it is visible,
	 *         false otherwise.
	 */
	public boolean isCreated() {
		if (dialog != null && dialog.getShell() != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Disposes this SuggestionBubble.
	 */
	public void dispose() {
		if (dialog != null)
			dialog.close();
	}

	private void init() {
		// Focus handling simulation for non-Windows systems
		if (!win) {
			shell.getDisplay().addFilter(SWT.MouseDown, new Listener() {
				@Override
				public void handleEvent(Event event) {

					if (isCursorInsideTextField()
							&& text.getText().length() == 0 && !isCreated()
							&& text.isFocusControl()) {
						suggestionFilter.setSearchText("");
						createDialog();
						tableViewer.refresh();
					} else {
						if (partialTranslationDialog != null) {
							if (!partialTranslationDialog
									.isCursorInsideDialog()
									&& !isCursorInsideDialog()) {
								dispose();
							}
						} else {
							if (!isCursorInsideDialog()) {
								dispose();
							}
						}
					}
				}
			});
		}

		// shell resize listener to dispose suggestion bubble
		shell.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				if (dialog != null && dialog.getShell() != null) {
					dialog.close();
				}
			}
		});

		// shell move listener
		shell.addListener(SWT.Move, new Listener() {
			public void handleEvent(Event e) {
				if (dialog != null && dialog.getShell() != null) {
					dialog.close();
				}
			}
		});

		// get ScrolledComposite
		ScrolledComposite scrolledComposite = (ScrolledComposite) text
				.getParent().getParent().getParent().getParent();
		// scroll listener
		scrolledComposite.getVerticalBar().addSelectionListener(
				new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (dialog != null && dialog.getShell() != null) {
							dialog.close();
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}

				});

		// ModifyListener
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				recalculatePosition();

				if (dialog != null && dialog.getShell() != null
						&& !tableViewer.getControl().isDisposed()) {
					suggestionFilter.setSearchText(text.getText().trim());
					tableViewer.refresh();

					if (tableViewer.getTable().getItemCount() == 0) {
						if (noSug == null || noSug.isDisposed()) {
							noSug = new Label(composite, SWT.NONE);
							noSug.setText("No suggestions available");
							noSug.moveAbove(tableViewer.getControl());
							noSug.setBackground(new Color(shell.getDisplay(),
									255, 255, 225));
							composite.layout();
						}
					} else {
						if (noSug != null && !noSug.isDisposed()) {
							tableViewer.getTable().setSelection(0);
							noSug.dispose();
							composite.layout();
						}
					}

					suggestionFilter.setSearchText("");
				}
			}

		});

		// KeyListener
		text.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.keyCode == SWT.CR || e.keyCode == SWT.LF)
						&& (dialog != null && dialog.getShell() != null)
						&& tableViewer.getTable().getSelectionIndex() != -1) {
					e.doit = false;
				}

				int accelerator = SWTKeySupport
						.convertEventToUnmodifiedAccelerator(e);
				KeyStroke keyStroke = SWTKeySupport
						.convertAcceleratorToKeyStroke(accelerator);
				KeySequence sequence = KeySequence.getInstance(keyStroke);

				if (sequence.format().equals(CONTENT_ASSIST)) {

					if (isCreated()) {
						if (noSug != null && !noSug.isDisposed()) {
							noSug.dispose();
							composite.layout();
						}
						suggestionFilter.setSearchText("");
						tableViewer.refresh();
						tableViewer.getTable().setSelection(0);
					} else {
						createDialog();
						suggestionFilter.setSearchText(text.getText().trim());
						tableViewer.refresh();
					}
					e.doit = false;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (dialog == null || dialog.getShell() == null) {
					return;
				}

				if (e.keyCode == SWT.ESC) {
					dialog.close();
					return;
				}

				// Changing selection with keyboard arrows and applying
				// translation with enter
				int currentSelectionIndex = tableViewer.getTable()
						.getSelectionIndex();

				if (e.keyCode == SWT.ARROW_DOWN) {
					if (currentSelectionIndex >= tableViewer.getTable()
							.getItemCount() - 1) {
						tableViewer.getTable().setSelection(0);
					} else {
						tableViewer.getTable().setSelection(
								currentSelectionIndex + 1);
					}
				}

				if (e.keyCode == SWT.ARROW_UP) {
					if (currentSelectionIndex <= 0) {
						tableViewer.getTable().setSelection(
								tableViewer.getTable().getItemCount() - 1);
					} else {
						tableViewer.getTable().setSelection(
								currentSelectionIndex - 1);
					}
				}

				if (e.keyCode == SWT.CR || e.keyCode == SWT.LF) {
					applySuggestion(text);
				}
			}
		});

		// FocusListener
		text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

				if (win && !isCreated() && text.getText().length() == 0) {
					suggestionFilter.setSearchText("");
					createDialog();
					tableViewer.refresh();
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (win && dialog != null && !isCursorInsideDialog()) {
					dialog.close();
				}
			}
		});

		//MouseListener for Windows systems
		if (win) {
			text.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// Nothing to do
				}

				@Override
				public void mouseDown(MouseEvent e) {
					// Nothing to do
				}

				@Override
				public void mouseUp(MouseEvent e) {

					if (caret != null) {
						if (dialog != null
								&& !caret.equals(text.getCaretLocation())) {
							dialog.close();
							caret = text.getCaretLocation();
						}
					} else {
						caret = text.getCaretLocation();
					}

					if (partialTranslationDialog != null
							&& !partialTranslationDialog.isCursorInsideDialog()) {
						partialTranslationDialog.dispose();
					}
				}
			});
		}
	}

	private void createDialog() {
		boolean takeFocusOnOpen = false;
		boolean persistSize = false;
		boolean persistLocation = false;
		boolean showDialogMenu = false;
		boolean showPersistActions = false;
		String titleText = "Suggestions (" + SRC_LANG + " > "
				+ targetLanguage.toUpperCase() + ")";
		String infoText = "Ctrl+Space to display all suggestions";
		dialog = new PopupDialog(shell, SHELL_STYLE, takeFocusOnOpen,
				persistSize, persistLocation, showDialogMenu,
				showPersistActions, titleText, infoText) {

			@Override
			protected Control createDialogArea(Composite parent) {
				scrollComposite = new ScrolledComposite(
						(Composite) super.createDialogArea(parent),
						SWT.V_SCROLL | SWT.H_SCROLL);
				scrollComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
				scrollComposite.setExpandVertical(true);
				scrollComposite.setExpandHorizontal(true);

				GridLayout gl = new GridLayout(1, true);
				gl.verticalSpacing = 0;
				composite = new Composite(scrollComposite, SWT.NONE);
				composite.setLayout(gl);
				scrollComposite.setContent(composite);

				tableViewer = new TableViewer(composite, SWT.NO_SCROLL);
				tableViewer.getTable().setLayoutData(
						new GridData(GridData.FILL, SWT.TOP, true, false));

				tableViewer.setContentProvider(new ArrayContentProvider());
				tableViewer.setLabelProvider(new ITableLabelProvider() {

					@Override
					public Image getColumnImage(Object arg0, int arg1) {
						Suggestion s = (Suggestion) arg0;
						return s.getIcon();
					}

					@Override
					public String getColumnText(Object element, int index) {
						return ((Suggestion) element).getText();

					}

					@Override
					public void addListener(ILabelProviderListener listener) {
						// nothing to do
					}

					@Override
					public void dispose() {
						// nothing to do
					}

					@Override
					public boolean isLabelProperty(Object arg0, String arg1) {
						return true;
					}

					@Override
					public void removeListener(ILabelProviderListener arg0) {
						// nothing to do
					}
				});

				tableViewer.addFilter(suggestionFilter);

				tableViewer.addDoubleClickListener(new DoubleClickListener() {

					@Override
					public void doubleClick(DoubleClickEvent event) {
						applySuggestion(text);
					}

				});

				tableViewer
						.addSelectionChangedListener(new ISelectionChangedListener() {

							@Override
							public void selectionChanged(
									SelectionChangedEvent event) {
								if (tableViewer.getTable().getSelection().length > 0) {
									partialTranslationDialog.openDialog(
											tableViewer.getTable()
													.getSelection()[0]
													.getText(), text
													.getOrientation());
								}
							}
						});

				// For Windows 7
				// Set background color of column line
				// tableViewer.getTable().addListener(SWT.EraseItem, new
				// Listener() {
				// @Override
				// public void handleEvent(Event event) {
				// event.gc.setBackground(new Color(shell.getDisplay(), 255,
				// 255, 225));
				// event.gc.fillRectangle(event.getBounds());
				// }
				// });

				tableViewer.getTable().setSelection(0);
				return scrollComposite;
			}

			@Override
			protected void adjustBounds() {
				super.adjustBounds();

				Point point = text.getCaretLocation();

				getShell().setLocation(text.toDisplay(1, 1).x + point.x + 5,
						text.toDisplay(1, 1).y + point.y + 20);

				getShell().setSize(450, 200);
			}

		};
		dialog.open();

		partialTranslationDialog = new PartialTranslationDialog(
				dialog.getShell(), this);

		dialog.getShell().addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				partialTranslationDialog.dispose();
			}
		});

		updateSuggestions();
	}

	private void pack() {
		Point temp = new Point(0, 0);
		Point max = new Point(0, 0);

		for (TableItem item : tableViewer.getTable().getItems()) {
			temp.x = item.getBounds().width;
			temp.y = item.getBounds().height;
			if (temp.x > max.x) {
				max.x = temp.x;
			}
			max.y = max.y + temp.y;
		}
		scrollComposite.setMinSize(max);
	}

	private boolean isCursorInsideTextField() {
		if (text.isDisposed()) {
			return false;
		}

		Display d = Display.getCurrent();
		if (d == null) {
			d = Display.getDefault();
		}

		Point start = text.getLocation();
		start = text.toDisplay(start.x, start.y);
		Point size = text.getSize();
		Point end = new Point(size.x + start.x, size.y + start.y);

		if ((d.getCursorLocation().x > end.x || d.getCursorLocation().x < start.x)
				|| (d.getCursorLocation().y > end.y || d.getCursorLocation().y < start.y)) {
			return false;
		}
		return true;
	}

	private Composite createLoadingCircle() {
		// Create loading cicle
		Composite loadingCircle = new Composite(composite, SWT.EMBEDDED
				| SWT.NO_BACKGROUND);
		Frame frame = SWT_AWT.new_Frame(loadingCircle);
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(
				"/icons/ajax-loader.gif"));
		JLabel label = new JLabel(imageIcon);
		frame.add(label);
		frame.setBackground(new java.awt.Color(255, 255, 225));

		GridData gd = new GridData(GridData.BEGINNING, SWT.TOP, false, false);
		gd.heightHint = 16;
		gd.widthHint = 16;
		loadingCircle.setLayoutData(gd);
		return loadingCircle;
	}

	/**
	 * @return parent {@link Text} of this SuggestionBubble.
	 */
	public Text getTextField() {
		return text;
	}

	/**
	 * @return language of this SuggestionBubble, to which default text is being
	 *         translated
	 */
	public String getTargetLanguage() {
		return targetLanguage;
	}

	private void recalculatePosition() {
		caret = text.getCaretLocation();

		if (dialog != null && dialog.getShell() != null) {

			int oldCaretX = getCurrentLocation().x - (text.toDisplay(1, 1).x)
					- 5;
			int oldCaretY = getCurrentLocation().y - (text.toDisplay(1, 1).y)
					- 20;

			int newCaretX = caret.x;
			int newCaretY = caret.y;

			setLocation(getCurrentLocation().x + (newCaretX - oldCaretX),
					getCurrentLocation().y + (newCaretY - oldCaretY));

		}
	}

	/**
	 * @return current location of the SuggestionBubble on the screen.
	 */
	public Point getCurrentLocation() {
		if (dialog != null && dialog.getShell() != null) {
			return dialog.getShell().getLocation();
			// return dialog.getShell().toDisplay(1, 1);
		}

		return new Point(0, 0);
	}

	/**
	 * @return size of the SuggestionBubble
	 */
	public Point getSize() {
		return dialog.getShell().getSize();
	}

	private void setLocation(int x, int y) {
		if (dialog != null && dialog.getShell() != null)
			dialog.getShell().setLocation(new Point(x, y));
	}

	private void applySuggestion(Text text) {
		if (tableViewer.getTable().getSelectionIndex() == -1) {
			return;
		}
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		Suggestion suggestion = (Suggestion) selection.getFirstElement();

		String s = suggestion.getText();

		// Filter out [(].*[% match)]
		if (s.lastIndexOf("(") != -1) {
			s = s.substring(0, s.lastIndexOf("(") - 1);
		}

		if (SuggestionErrors.contains(s)) {
			// Ignore call
			return;
		}

		((NullableText) text.getParent()).setText(s, true);

		dialog.close();
	}

	private boolean isCursorInsideDialog() {
		if (dialog == null || dialog.getShell() == null) {
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
	 * @see org.eclipse.babel.editor.widgets.suggestion.provider.
	 *      ISuggestionProviderListener#suggestionProviderUpdated(org.eclipse.babel
	 *      .editor.widgets.suggestion.provider.ISuggestionProvider)
	 */
	@Override
	public void suggestionProviderUpdated(ISuggestionProvider provider) {
		LOGGER.log(LOG_LEVEL, "provider :"
				+ provider.getClass().getSimpleName()
				+ ", size of suggestions: " + suggestions.size());

		for (int i = 0; i < suggestions.size(); i++) {
			Suggestion sug = suggestions.get(i);
			if (sug.getProvider().equals(provider)) {
				suggestions.set(i,
						provider.getSuggestion(defaultText, targetLanguage));
			}
		}

		if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
			tableViewer.setInput(suggestions);
		}
	}
}

/**
 * Implements {@link IDoubleClickListener}
 *
 * @author Samir Soyer
 *
 */
abstract class DoubleClickListener implements IDoubleClickListener {
}