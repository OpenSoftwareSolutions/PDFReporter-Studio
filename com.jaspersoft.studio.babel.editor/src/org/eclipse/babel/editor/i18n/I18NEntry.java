/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre, Alexej Strelow, Matthias Lettmayer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - updateKey
 *    Matthias Lettmayer - extracted I18NEntry into own class for SWT specific implementation
 ******************************************************************************/
package org.eclipse.babel.editor.i18n;

import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.util.BabelUtils;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;

public class I18NEntry extends AbstractI18NEntry {

    public I18NEntry(Composite parent, AbstractMessagesEditor editor,
            Locale locale) {
        super(parent, editor, locale);
    }

    @Override
    void updateKey(String key) {
        IMessagesBundleGroup messagesBundleGroup = editor.getBundleGroup();
        boolean isKey = key != null && messagesBundleGroup.isMessageKey(key);
        textBox.setEnabled(isKey);
        if (isKey) {
            IMessage entry = messagesBundleGroup.getMessage(key, locale);
            if (entry == null || entry.getValue() == null) {
                textBox.setText(null);
                // commentedCheckbox.setSelection(false);
            } else {
                // commentedCheckbox.setSelection(bundleEntry.isCommented());
                textBox.setText(entry.getValue());
            }
        } else {
            textBox.setText(null);
        }
    }

    @Override
    KeyListener getKeyListener() {
        return new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                // Text field has changed: make editor dirty if not already
                if (!BabelUtils.equals(focusGainedText, textBox.getText())) {
                    // Make the editor dirty if not already. If it is,
                    // we wait until field focus lost (or save) to
                    // update it completely.
                    if (!editor.isDirty()) {
                        // textEditor.isDirty();
                        updateModel();
                        // int caretPosition = eventBox.getCaretPosition();
                        // updateBundleOnChanges();
                        // eventBox.setSelection(caretPosition);
                    }
                    // autoDetectRequiredFont(eventBox.getText());
                }
            }
        };
        // Eric Fettweis : new listener to automatically change the font
        // textBox.addModifyListener(new ModifyListener() {
        //
        // public void modifyText(ModifyEvent e) {
        // String text = textBox.getText();
        // Font f = textBox.getFont();
        // String fontName = getBestFont(f.getFontData()[0].getName(), text);
        // if(fontName!=null){
        // f = getSWTFont(f, fontName);
        // textBox.setFont(f);
        // }
        // }
        //
        // });
        // }
    }
}
