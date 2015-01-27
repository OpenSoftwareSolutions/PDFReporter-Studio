/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - TapiJI integration, fixed issues 37, 48
 ******************************************************************************/
package org.eclipse.babel.editor.i18n;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.manager.IMessagesEditorListener;
import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Internationalization page where one can edit all resource bundle entries at
 * once for all supported locales.
 * 
 * @author Pascal Essiembre
 */
public class I18NPage extends ScrolledComposite implements ISelectionProvider {

    /** Minimum height of text fields. */
    private static final int TEXT_MIN_HEIGHT = 90;

    protected final AbstractMessagesEditor editor;
    protected final SideNavComposite keysComposite;
    private final Composite valuesComposite;
    private final Map<Locale, AbstractI18NEntry> entryComposites = new HashMap<Locale, AbstractI18NEntry>();
    private Composite entriesComposite;

    // private Composite parent;
    private boolean keyTreeVisible = true;

    // private final StackLayout layout = new StackLayout();
    private final SashForm sashForm;

    /**
     * Constructor.
     * 
     * @param parent
     *            parent component.
     * @param style
     *            style to apply to this component
     * @param resourceMediator
     *            resource manager
     */
    public I18NPage(Composite parent, int style,
            final AbstractMessagesEditor editor) {
        super(parent, style);
        this.editor = editor;
        sashForm = new SashForm(this, SWT.SMOOTH);
        sashForm.setBackground(UIUtils.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
        editor.getEditorSite().getPage().addPartListener(new IPartListener() {
            public void partActivated(IWorkbenchPart part) {
                if (part == editor && !sashForm.isDisposed()) {
                    sashForm.setBackground(UIUtils.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
                }
            }

            public void partDeactivated(IWorkbenchPart part) {
                if (part == editor && !sashForm.isDisposed()) {
                    sashForm.setBackground(UIUtils.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

                }
            }

            public void partBroughtToTop(IWorkbenchPart part) {
            }

            public void partClosed(IWorkbenchPart part) {
            }

            public void partOpened(IWorkbenchPart part) {
            }
        });

        setContent(sashForm);

        keysComposite = new SideNavComposite(sashForm, editor);

        valuesComposite = createValuesComposite(sashForm);

        sashForm.setWeights(new int[] { 25, 75 });

        setExpandHorizontal(true);
        setExpandVertical(true);
        setMinWidth(400);

        RBManager instance = RBManager.getInstance(editor.getBundleGroup()
                .getProjectName());
        instance.addMessagesEditorListener(new IMessagesEditorListener() {

            public void onSave() {
                // TODO Auto-generated method stub

            }

            public void onModify() {
                // TODO Auto-generated method stub
            }

            public void onResourceChanged(IMessagesBundle bundle) {
                // [RAP] only update tree, which belongs to this UIThread
                if (!keysComposite.isDisposed()) {
                    Display display = keysComposite.getTreeViewer().getTree().getDisplay();
                    if (display.equals(Display.getCurrent())) {
                        AbstractI18NEntry i18nEntry = entryComposites.get(bundle.getLocale());
                        if (i18nEntry != null && !getSelection().isEmpty()) {
                        	i18nEntry.updateKey(String.valueOf(((IStructuredSelection) getSelection()).getFirstElement()));
                        }
                    }
                }
            }

        });
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose() {
        keysComposite.dispose();
        for (AbstractI18NEntry entry : entryComposites.values())
            entry.dispose();
        super.dispose();
    }

    public void setKeyTreeVisible(boolean visible) {
        keyTreeVisible = visible;
        if (visible) {
            sashForm.setMaximizedControl(null);
        } else {
            sashForm.setMaximizedControl(valuesComposite);
        }
        for (IMessagesEditorChangeListener listener : editor
                .getChangeListeners()) {
            listener.keyTreeVisibleChanged(visible);
        }
    }

    public boolean isKeyTreeVisible() {
        return keyTreeVisible;
    }

    private Composite createValuesComposite(SashForm parent) {
        final ScrolledComposite scrolledComposite = new ScrolledComposite(
                parent, SWT.V_SCROLL | SWT.H_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setSize(SWT.DEFAULT, 100);

        entriesComposite = new Composite(scrolledComposite, SWT.BORDER);
        scrolledComposite.setContent(entriesComposite);
        scrolledComposite.setMinSize(entriesComposite.computeSize(SWT.DEFAULT,
                editor.getBundleGroup().getLocales().length * TEXT_MIN_HEIGHT));

        entriesComposite.setLayout(new GridLayout(1, false));
        Locale[] locales = editor.getBundleGroup().getLocales();
        UIUtils.sortLocales(locales);
        locales = UIUtils.filterLocales(locales);
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            addI18NEntry(locale);
        }

        /*editor.addChangeListener(new MessagesEditorChangeAdapter() {
            public void selectedKeyChanged(String oldKey, String newKey) {
                boolean isKey = newKey != null && editor.getBundleGroup().isMessageKey(newKey);
                scrolledComposite.setBackground(isKey);
            }
        });*/

        return scrolledComposite;
    }

    public void addI18NEntry(Locale locale) {
        AbstractI18NEntry i18NEntry = null;
        try {
            Class<?> clazz = Class.forName(AbstractI18NEntry.INSTANCE_CLASS);
            Constructor<?> cons = clazz.getConstructor(Composite.class, AbstractMessagesEditor.class, Locale.class);
            i18NEntry = (AbstractI18NEntry) cons.newInstance(entriesComposite,editor, locale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // entryComposite.addFocusListener(localBehaviour);
        entryComposites.put(locale, i18NEntry);
        entriesComposite.layout();
    }

    public void removeI18NEntry(Locale locale) {
        AbstractI18NEntry i18NEntry = entryComposites.get(locale);
        if (i18NEntry != null) {
            i18NEntry.dispose();
            entryComposites.remove(locale);
            entriesComposite.layout();
        }
    }

    public void selectLocale(Locale locale) {
    	/*Collection<Locale> locales = entryComposites.keySet();
        for (Locale entryLocale : locales) {
            AbstractI18NEntry entry = entryComposites.get(entryLocale);
            // TODO add equivalent method on entry composite
            Text textBox = entry.getTextBox();
            if (BabelUtils.equals(locale, entryLocale)) {
	            textBox.selectAll();
	            textBox.setFocus();
            } else {
            	textBox.clearSelection();
            }
        }*/
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        keysComposite.getTreeViewer().addSelectionChangedListener(listener);

    }

    public ISelection getSelection() {
        return keysComposite.getTreeViewer().getSelection();
    }

    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        keysComposite.getTreeViewer().removeSelectionChangedListener(listener);
    }

    public void setSelection(ISelection selection) {
        keysComposite.getTreeViewer().setSelection(selection);
    }

    public TreeViewer getTreeViewer() {
        return keysComposite.getTreeViewer();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (AbstractI18NEntry entry : entryComposites.values())
            entry.setEnabled(enabled);
    }

    public void setEnabled(boolean enabled, Locale locale) {
        // super.setEnabled(enabled);
        for (AbstractI18NEntry entry : entryComposites.values()) {
            if (locale == entry.getLocale()
                    || (locale != null && locale.equals(entry.getLocale()))) {
                entry.setEnabled(enabled);
                break;
            }
        }
    }

    public SideNavTextBoxComposite getSidNavTextBoxComposite() {
        return keysComposite.getSidNavTextBoxComposite();
    }
}
