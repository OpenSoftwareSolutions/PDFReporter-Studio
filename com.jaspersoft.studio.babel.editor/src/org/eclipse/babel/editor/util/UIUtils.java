/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.util;

import java.awt.ComponentOrientation;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.babel.editor.compat.SwtRapCompatibilitySWT;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.preferences.MsgEditorPreferences;
import org.eclipse.babel.messages.Messages;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Utility methods related to application UI.
 * 
 * @author Pascal Essiembre
 */
public final class UIUtils {

    /** Name of resource bundle image. */
    public static final String IMAGE_RESOURCE_BUNDLE = "resourcebundle.gif"; //$NON-NLS-1$
    /** Name of properties file image. */
    public static final String IMAGE_PROPERTIES_FILE = "propertiesfile.gif"; //$NON-NLS-1$
    /** Name of new properties file image. */
    public static final String IMAGE_NEW_PROPERTIES_FILE = "newpropertiesfile.gif"; //$NON-NLS-1$
    /** Name of hierarchical layout image. */
    public static final String IMAGE_LAYOUT_HIERARCHICAL = "hierarchicalLayout.gif"; //$NON-NLS-1$
    /** Name of flat layout image. */
    public static final String IMAGE_LAYOUT_FLAT = "flatLayout.gif"; //$NON-NLS-1$

    /** Name of add icon. */
    public static final String IMAGE_ADD = "add.png"; //$NON-NLS-1$
    /** Name of edit icon. */
    public static final String IMAGE_RENAME = "rename.gif"; //$NON-NLS-1$
    /** Name of "view left" icon. */
    /** Name of refactoring icon. */
    public static final String IMAGE_REFACTORING = "refactoring.png"; //$NON-NLS-1$
    public static final String IMAGE_VIEW_LEFT = "viewLeft.gif"; //$NON-NLS-1$
    /** Name of locale icon. */
    public static final String IMAGE_LOCALE = "locale.gif"; //$NON-NLS-1$
    /** Name of new locale icon. */
    public static final String IMAGE_NEW_LOCALE = "newLocale.gif"; //$NON-NLS-1$
    /** Name of expand all icon. */
    public static final String IMAGE_EXPAND_ALL = "expandall.png"; //$NON-NLS-1$
    /** Name of collapse all icon. */
    public static final String IMAGE_COLLAPSE_ALL = "collapseall.png"; //$NON-NLS-1$

    public static final String IMAGE_KEY = "keyDefault.png"; //$NON-NLS-1$
    public static final String IMAGE_INCOMPLETE_ENTRIES = "incomplete.gif"; //$NON-NLS-1$
    public static final String IMAGE_EMPTY = "empty.gif"; //$NON-NLS-1$
    public static final String IMAGE_MISSING_TRANSLATION = "missing_translation.gif"; //$NON-NLS-1$
    public static final String IMAGE_UNUSED_TRANSLATION = "unused_translation.png"; //$NON-NLS-1$
    public static final String IMAGE_UNUSED_AND_MISSING_TRANSLATIONS = "unused_and_missing_translations.png"; //$NON-NLS-1$
    public static final String IMAGE_WARNED_TRANSLATION = "warned_translation.png"; //$NON-NLS-1$
    public static final String IMAGE_DUPLICATE = "duplicate.gif"; //$NON-NLS-1$

    public static final String IMAGE_WARNING = "warning.gif"; //$NON-NLS-1$
    public static final String IMAGE_ERROR = "error_co.gif"; //$NON-NLS-1$

    /** Image registry. */
    private static ImageRegistry imageRegistry;
    // TODO: REMOVE this comment eventually:
    // necessary to specify the display otherwise Display.getCurrent()
    // is called and will return null if this is not the UI-thread.
    // this happens if the builder is called and initialize this class:
    // the thread will not be the UI-thread.
    // new ImageRegistry(PlatformUI.getWorkbench().getDisplay());

    public static final String PDE_NATURE = "org.eclipse.pde.PluginNature"; //$NON-NLS-1$
    public static final String JDT_JAVA_NATURE = "org.eclipse.jdt.core.javanature"; //$NON-NLS-1$

    /**
     * The root locale used for the original properties file. This constant is
     * defined in java.util.Local starting with jdk6.
     */
    public static final Locale ROOT_LOCALE = new Locale(""); //$NON-NLS-1$

    /**
     * Sort the Locales alphabetically. Make sure the root Locale is first.
     * 
     * @param locales
     */
    public static final void sortLocales(Locale[] locales) {
        List<Locale> localesList = new ArrayList<Locale>(Arrays.asList(locales));
        Comparator<Locale> comp = new Comparator<Locale>() {
            public int compare(Locale l1, Locale l2) {
                if (ROOT_LOCALE.equals(l1)) {
                    return -1;
                }
                if (ROOT_LOCALE.equals(l2)) {
                    return 1;
                }
                String name1 = ""; //$NON-NLS-1$
                String name2 = ""; //$NON-NLS-1$
                if (l1 != null) {
                    name1 = l1.getDisplayName();
                }
                if (l2 != null) {
                    name2 = l2.getDisplayName();
                }
                return name1.compareTo(name2);
            }
        };
        Collections.sort(localesList, comp);
        for (int i = 0; i < locales.length; i++) {
            locales[i] = localesList.get(i);
        }
    }
    
    public static String wildcardToRegex(String wildcard){
        StringBuffer s = new StringBuffer(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch(c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                    // escape special regexp-characters
                case '(': case ')': case '[': case ']': case '$':
                case '^': case '.': case '{': case '}': case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return(s.toString());
    }

    /**
     * @param locale
     * @return true if the locale is selected by the local-filter defined in the
     *         preferences
     * @see MsgEditorPreferences#getFilterLocalesStringMatcher()
     */
    public static boolean isDisplayed(Locale locale) {
       if (ROOT_LOCALE.equals(locale) || locale == null) {
            return true;
        }
        String stringPatterns = MsgEditorPreferences.getFilterLocalesStringMatcher();
        String[] wildcards = stringPatterns.split(",");
        if (wildcards == null || wildcards.length == 0) {
            return true;
        }
        String locStr = locale.toString();
        for (int i = 0; i < wildcards.length; i++) {
            if (locStr.matches(wildcardToRegex(wildcards[i]))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads the filter of locales in the preferences and apply it to filter the
     * passed locales.
     * 
     * @param locales
     * @return The new collection of locales; removed the ones not selected by
     *         the preferences.
     */
    public static Locale[] filterLocales(Locale[] locales) {
    	String stringPatterns = MsgEditorPreferences.getFilterLocalesStringMatcher();
        Set<Locale> already = new HashSet<Locale>();
        // first look for the root locale:
        ArrayList<Locale> result = new ArrayList<Locale>();
        for (int j = 0; j < locales.length; j++) {
            Locale loc = locales[j];
            if (ROOT_LOCALE.equals(loc) || loc == null) {
                already.add(loc);
                result.add(loc);
                break;
            }
        }
        // now go through each pattern until already indexed locales found all
        // locales or we run out of locales.
        String[] wildcards = stringPatterns.split(",");
        for (int pi = 0; pi < wildcards.length; pi++) {
        	String pattern = wildcardToRegex(wildcards[pi]);
            for (int j = 0; j < locales.length; j++) {
                Locale loc = locales[j];
                if (!already.contains(loc) && loc!= null && loc.toString().matches(pattern)) {
                    already.add(loc);
                    result.add(loc);
                    if (already.size() == locales.length) {
                        for (int k = 0; k < locales.length; k++) {
                            locales[k] = (Locale) result.get(k);
                        }
                        return locales;
                    }
                }
            }
        }
        Locale[] filtered = new Locale[result.size()];
        for (int k = 0; k < filtered.length; k++) {
            filtered[k] = result.get(k);
        }
        return filtered;
    }

    /**
     * Constructor.
     */
    private UIUtils() {
        super();
    }

    /**
     * Creates a font by altering the font associated with the given control and
     * applying the provided style (size is unaffected).
     * 
     * @param control
     *            control we base our font data on
     * @param style
     *            style to apply to the new font
     * @return newly created font
     */
    public static Font createFont(Control control, int style) {
        // TODO consider dropping in favor of control-less version?
        return createFont(control, style, 0);
    }

    /**
     * Creates a font by altering the font associated with the given control and
     * applying the provided style and relative size.
     * 
     * @param control
     *            control we base our font data on
     * @param style
     *            style to apply to the new font
     * @param relSize
     *            size to add or remove from the control size
     * @return newly created font
     */
    public static Font createFont(Control control, int style, int relSize) {
        // TODO consider dropping in favor of control-less version?
        FontData[] fontData = control.getFont().getFontData();
        for (int i = 0; i < fontData.length; i++) {
            fontData[i].setHeight(fontData[i].getHeight() + relSize);
            fontData[i].setStyle(style);
        }
        return new Font(control.getDisplay(), fontData);
    }

    /**
     * Creates a font by altering the system font and applying the provided
     * style and relative size.
     * 
     * @param style
     *            style to apply to the new font
     * @return newly created font
     */
    public static Font createFont(int style) {
        return createFont(style, 0);
    }

    /**
     * Creates a font by altering the system font and applying the provided
     * style and relative size.
     * 
     * @param style
     *            style to apply to the new font
     * @param relSize
     *            size to add or remove from the control size
     * @return newly created font
     */
    public static Font createFont(int style, int relSize) {
        Display display = MessagesEditorPlugin.getDefault().getWorkbench()
                .getDisplay();
        FontData[] fontData = display.getSystemFont().getFontData();
        for (int i = 0; i < fontData.length; i++) {
            fontData[i].setHeight(fontData[i].getHeight() + relSize);
            fontData[i].setStyle(style);
        }
        return new Font(display, fontData);
    }

    /**
     * Creates a cursor matching given style.
     * 
     * @param style
     *            style to apply to the new font
     * @return newly created cursor
     */
    public static Cursor createCursor(int style) {
        Display display = MessagesEditorPlugin.getDefault().getWorkbench()
                .getDisplay();
        return new Cursor(display, style);
    }

    /**
     * Gets a system color.
     * 
     * @param colorId
     *            SWT constant
     * @return system color
     */
    public static Color getSystemColor(int colorId) {
        return MessagesEditorPlugin.getDefault().getWorkbench().getDisplay()
                .getSystemColor(colorId);
    }

    /**
     * Gets the approximate width required to display a given number of
     * characters in a control.
     * 
     * @param control
     *            the control on which to get width
     * @param numOfChars
     *            the number of chars
     * @return width
     */
    public static int getWidthInChars(Control control, int numOfChars) {
        GC gc = new GC(control);
        Point extent = gc.textExtent("W");//$NON-NLS-1$
        gc.dispose();
        return numOfChars * extent.x;
    }

    /**
     * Gets the approximate height required to display a given number of
     * characters in a control, assuming, they were laid out vertically.
     * 
     * @param control
     *            the control on which to get height
     * @param numOfChars
     *            the number of chars
     * @return height
     */
    public static int getHeightInChars(Control control, int numOfChars) {
        GC gc = new GC(control);
        Point extent = gc.textExtent("W");//$NON-NLS-1$
        gc.dispose();
        return numOfChars * extent.y;
    }

    /**
     * Shows an error dialog based on the supplied arguments.
     * 
     * @param shell
     *            the shell
     * @param exception
     *            the core exception
     * @param msgKey
     *            key to the plugin message text
     */
    public static void showErrorDialog(Shell shell, CoreException exception, String message) {
        exception.printStackTrace();
        ErrorDialog.openError(shell, message, exception.getLocalizedMessage(), exception.getStatus());
    }

    /**
     * Shows an error dialog based on the supplied arguments.
     * 
     * @param shell
     *            the shell
     * @param exception
     *            the core exception
     * @param msgKey
     *            key to the plugin message text
     */
    public static void showErrorDialog(Shell shell, Exception exception, String message) {
        exception.printStackTrace();
        IStatus status = new Status(IStatus.ERROR,
                MessagesEditorPlugin.PLUGIN_ID, 0,
                message + " " //$NON-NLS-1$
                + Messages.error_seeLogs, 
                exception);
        ErrorDialog.openError(shell, message,
                exception.getLocalizedMessage(), status);
    }

    /**
     * Gets a locale, null-safe, display name.
     * 
     * @param locale
     *            locale to get display name
     * @return display name
     */
    public static String getDisplayName(Locale locale) {
        if (locale == null || ROOT_LOCALE.equals(locale)) {
            return Messages.editor_i18nentry_rootlocale_label; 
        }
        return locale.getDisplayName();
    }

    /**
     * Gets an image descriptor.
     * 
     * @param name
     *            image name
     * @return image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String name) {
        String iconPath = "icons/"; //$NON-NLS-1$
        try {
            URL installURL = MessagesEditorPlugin.getDefault().getBundle()
                    .getEntry("/"); //$NON-NLS-1$
            URL url = new URL(installURL, iconPath + name);
            return ImageDescriptor.createFromURL(url);
        } catch (MalformedURLException e) {
            // should not happen
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }

    /**
     * Gets an image.
     * 
     * @param imageName
     *            image name
     * @return image
     */
    public static Image getImage(String imageName) {
        Image image = null;
        try {
            // [RAP] In RAP multiple displays could exist (multiple user),
            // therefore image needs to be created every time with the current
            // display
            Method getImageRAP = Class.forName(
                    "org.eclipse.babel.editor.util.UIUtilsRAP").getMethod(
                    "getImage", String.class);
            image = (Image) getImageRAP.invoke(null, imageName);
        } catch (Exception e) {
            // RAP fragment not running --> invoke rcp version
            image = getImageRCP(imageName);
        }

        return image;
    }

    /**
     * Gets an image from image registry or creates a new one if it the first
     * time.
     * 
     * @param imageName
     *            image name
     * @return image
     */
    private static Image getImageRCP(String imageName) {
        if (imageRegistry == null)
            imageRegistry = new ImageRegistry(PlatformUI.getWorkbench()
                    .getDisplay());
        Image image = imageRegistry.get(imageName);
        if (image == null) {
            image = getImageDescriptor(imageName).createImage();
            imageRegistry.put(imageName, image);
        }
        return image;
    }

    /**
     * @return Image for the icon that indicates a key with no issues
     */
    public static Image getKeyImage() {
        Image image = UIUtils.getImage(UIUtils.IMAGE_KEY);
        return image;
    }

    /**
     * @return Image for the icon which indicates a key that has missing
     *         translations
     */
    public static Image getMissingTranslationImage() {
        Image image = UIUtils.getImage(UIUtils.IMAGE_KEY);
        ImageDescriptor missing = ImageDescriptor.createFromImage(UIUtils
                .getImage(UIUtils.IMAGE_ERROR));
        image = new DecorationOverlayIcon(image, missing,
                IDecoration.BOTTOM_RIGHT).createImage();
        return image;
    }

    /**
     * @return Image for the icon which indicates a key that is unused
     */
    public static Image getUnusedTranslationsImage() {
        Image image = UIUtils.getImage(UIUtils.IMAGE_UNUSED_TRANSLATION);
        ImageDescriptor warning = ImageDescriptor.createFromImage(UIUtils
                .getImage(UIUtils.IMAGE_WARNING));
        image = new DecorationOverlayIcon(image, warning,
                IDecoration.BOTTOM_RIGHT).createImage();
        return image;
    }

    /**
     * @return Image for the icon which indicates a key that has missing
     *         translations and is unused
     */
    public static Image getMissingAndUnusedTranslationsImage() {
        Image image = UIUtils.getImage(UIUtils.IMAGE_UNUSED_TRANSLATION);
        ImageDescriptor missing = ImageDescriptor.createFromImage(UIUtils
                .getImage(UIUtils.IMAGE_ERROR));
        image = new DecorationOverlayIcon(image, missing,
                IDecoration.BOTTOM_RIGHT).createImage();
        return image;
    }

    /**
     * @return Image for the icon which indicates a key that has duplicate
     *         entries
     */
    public static Image getDuplicateEntryImage() {
        Image image = UIUtils.getImage(UIUtils.IMAGE_KEY);
        ImageDescriptor missing = ImageDescriptor.createFromImage(UIUtils
                .getImage(UIUtils.IMAGE_WARNING));
        image = new DecorationOverlayIcon(image, missing,
                IDecoration.BOTTOM_RIGHT).createImage();
        return image;
    }

    /**
     * @return Image for the icon which indicates a key that has duplicate
     *         entries and is unused
     */
    public static Image getDuplicateEntryAndUnusedTranslationsImage() {
        Image image = UIUtils.getImage(UIUtils.IMAGE_UNUSED_TRANSLATION);
        ImageDescriptor missing = ImageDescriptor.createFromImage(UIUtils
                .getImage(UIUtils.IMAGE_DUPLICATE));
        image = new DecorationOverlayIcon(image, missing,
                IDecoration.BOTTOM_RIGHT).createImage();
        return image;
    }

    /**
     * Gets the orientation suited for a given locale.
     * 
     * @param locale
     *            the locale
     * @return <code>SWT.RIGHT_TO_LEFT</code> or <code>SWT.LEFT_TO_RIGHT</code>
     */
    public static int getOrientation(Locale locale) {
        if (locale != null) {
            ComponentOrientation orientation = ComponentOrientation
                    .getOrientation(locale);
            if (orientation == ComponentOrientation.RIGHT_TO_LEFT) {
                return SwtRapCompatibilitySWT.RIGHT_TO_LEFT;
            }
        }
        return SWT.LEFT_TO_RIGHT;
    }

    /**
     * Parses manually the project descriptor looking for a nature.
     * <p>
     * Calling IProject.getNature(naturedId) throws exception if the Nature is
     * not defined in the currently executed platform. For example if looking
     * for a pde nature inside an eclipse-platform.
     * </p>
     * <p>
     * This method returns the result without that constraint.
     * </p>
     * 
     * @param proj
     *            The project to examine
     * @param nature
     *            The nature to look for.
     * @return true if the nature is defined in that project.
     */
    public static boolean hasNature(IProject proj, String nature) {
        IFile projDescr = proj.getFile(".project"); //$NON-NLS-1$
        if (!projDescr.exists()) {
            return false;// a corrupted project
        }
        // <classpathentry kind="src" path="src"/>
        InputStream in = null;
        try {
            projDescr.refreshLocal(IResource.DEPTH_ZERO, null);
            in = projDescr.getContents();
            // supposedly in utf-8. should not really matter for us
            Reader r = new InputStreamReader(in, "UTF-8");
            LineNumberReader lnr = new LineNumberReader(r);
            String line = lnr.readLine();
            while (line != null) {
                if (line.trim().equals("<nature>" + nature + "</nature>")) {
                    lnr.close();
                    r.close();
                    return true;
                }
                line = lnr.readLine();
            }
            lnr.close();
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
        return false;
    }

}
