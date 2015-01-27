/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Samir Soyer      - Suggestion Bubble
 ******************************************************************************/
package org.eclipse.babel.editor.widgets;

import java.util.Locale;
import java.util.Stack;

import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.editor.widgets.suggestion.SuggestionBubble;
import org.eclipse.babel.editor.widgets.suggestion.provider.SuggestionProviderUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Special text control that regognized the difference between a
 * <code>null</code> values and an empty string. When a <code>null</code> value
 * is supplied, the control background is of a different color. Pressing the
 * backspace button when the field is currently empty will change its value from
 * empty string to <code>null</code>.
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class NullableText extends Composite {

    private final Text text;
    private final Color defaultColor;
    private final Color nullColor;
    private Locale locale;
    private boolean dirty;
    private boolean suggestionBubbleOn;

    private boolean isnull;

    private KeyListener keyListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (SWT.BS == e.character) {
                if (text.getText().length() == 0) {
                    renderNull();
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            if (text.getText().length() > 0) {
                renderNormal();
            }
        }
    };

    /**
     * Constructor.
     */
    public NullableText(Composite parent, int style, Locale locale) {
        super(parent, SWT.NONE);
        text = new Text(this, style);
        text.setData("UNDO", new Stack<String>());
        text.setData("REDO", new Stack<String>());
        defaultColor = text.getBackground();
        nullColor = UIUtils.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        setLayoutData(gd);

        initComponents();
		this.locale = locale;

		suggestionBubbleOn = !SuggestionProviderUtils.getSuggetionProviders()
				.isEmpty();

		if (suggestionBubbleOn) {
			if (locale != null) {
				new SuggestionBubble(text, locale.getLanguage());
			} else {
				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						SuggestionBubble.setDefaultText(text.getText());
					}
				});
			}
		}
	}

	public Text getTextBox() {
		return this.text;
	}

    public void setOrientation(int orientation) {
        text.setOrientation(orientation);
    }

    public void setText(String text) {
        isnull = text == null;

		if (locale == null && suggestionBubbleOn) {
			SuggestionBubble.setDefaultText(text);
		}

        if (isnull) {
            this.text.setText(""); //$NON-NLS-1$x
            renderNull();
        } else {
            this.text.setText(text);

            renderNormal();
        }

        Stack<String> undoCache = (Stack<String>) this.text.getData("UNDO");
        undoCache.push(this.text.getText());
    }

	/**
	 * Sets this <code>NullableText</code> to dirty or vice versa
	 *
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * This method returns whether the content of this
	 * <code> NullableText</code> have changed since the last save.
	 *
	 * @return <code>true</code> if this NullableText is dirty;
	 *         <code>false</code> otherwise.
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Applies the string to <code>NullableText</code> and makes it dirty,
	 * depending on the value of <code> dirty </code>
	 *
	 * @param text
	 *            is the string to be applied to <code> NullableText </code>
	 * @param dirty
	 *            whether setting text should make this
	 *            <code>NullableText</code> dirty.
	 */
	public void setText(String text, boolean dirty) {
		this.dirty = dirty;
		setText(text);
	}

    public String getText() {
        if (isnull) {
            return null;
        }
        return this.text.getText();
    }

    /**
     * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        text.setEnabled(enabled);
    }

    private void initComponents() {
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
                true);
        text.setLayoutData(gridData);

        text.addKeyListener(keyListener);

    }

    private void renderNull() {
        isnull = true;
        if (isEnabled()) {
            text.setBackground(nullColor);
            // try {
            // text.setBackgroundImage(UIUtils.getImage("null.bmp"));
            // } catch (Throwable t) {
            // t.printStackTrace();
            // }
        } else {
            text.setBackground(UIUtils
                    .getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
            // text.setBackgroundImage(null);
        }
    }

    private void renderNormal() {
        isnull = false;
        if (isEnabled()) {
            text.setBackground(defaultColor);
        } else {
            text.setBackground(UIUtils
                    .getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        }
        // text.setBackgroundImage(null);
    }

    /**
     * @see org.eclipse.swt.widgets.Control#addFocusListener(org.eclipse.swt.events.FocusListener)
     */
    public void addFocusListener(FocusListener listener) {
        text.addFocusListener(listener);
    }

    /**
     * @see org.eclipse.swt.widgets.Control#addKeyListener(org.eclipse.swt.events.KeyListener)
     */
    public void addKeyListener(KeyListener listener) {
        text.addKeyListener(listener);
    }

    /**
     * @see org.eclipse.swt.widgets.Control#removeFocusListener(org.eclipse.swt.events.FocusListener)
     */
    public void removeFocusListener(FocusListener listener) {
        text.removeFocusListener(listener);
    }

    /**
     * @see org.eclipse.swt.widgets.Control#removeKeyListener(org.eclipse.swt.events.KeyListener)
     */
    public void removeKeyListener(KeyListener listener) {
        text.removeKeyListener(listener);
    }

    /**
     * @param editable
     *            true if editable false otherwise. If never called it is
     *            editable by default.
     */
    public void setEditable(boolean editable) {
        text.setEditable(editable);
    }

    // private class SaveListener implements IMessagesEditorListener {
    //
    // public void onSave() {
    // Stack<String> undoCache = (Stack<String>) text.getData("UNDO");
    // undoCache.clear();
    // }
    //
    // public void onModify() {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // public void onResourceChanged(IMessagesBundle bundle) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // }

}