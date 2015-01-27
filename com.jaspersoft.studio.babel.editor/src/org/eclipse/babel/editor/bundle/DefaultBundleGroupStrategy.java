/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - TapJI integration, messagesBundleId
 ******************************************************************************/
package org.eclipse.babel.editor.bundle;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.eclipse.babel.core.message.internal.MessageException;
import org.eclipse.babel.core.message.internal.MessagesBundle;
import org.eclipse.babel.core.message.resource.IMessagesResource;
import org.eclipse.babel.core.message.resource.internal.PropertiesIFileResource;
import org.eclipse.babel.core.message.resource.ser.PropertiesDeserializer;
import org.eclipse.babel.core.message.resource.ser.PropertiesSerializer;
import org.eclipse.babel.core.message.strategy.IMessagesBundleGroupStrategy;
import org.eclipse.babel.core.util.BabelUtils;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.preferences.MsgEditorPreferences;
import org.eclipse.babel.editor.resource.EclipsePropertiesEditorResource;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;

/**
 * MessagesBundle group strategy for standard properties file structure. That
 * is, all *.properties files of the same base name within the same directory.
 *
 * @author Pascal Essiembre
 */
public class DefaultBundleGroupStrategy implements IMessagesBundleGroupStrategy {

    /** Class name of Properties file editor (Eclipse 3.1). */
    protected static final String PROPERTIES_EDITOR_CLASS_NAME = "org.eclipse.jdt.internal.ui.propertiesfileeditor." //$NON-NLS-1$
            + "PropertiesFileEditor"; //$NON-NLS-1$

    /** Empty bundle array. */
    protected static final MessagesBundle[] EMPTY_BUNDLES = new MessagesBundle[] {};

    /** Eclipse editor site. */
    protected IEditorSite site;
    /** File being open, triggering the creation of a bundle group. */
    private IFile file;
    /** MessagesBundle group base name. */
    private final String baseName;
    /** Pattern used to match files in this strategy. */
    private final String fileMatchPattern;

    /**
     * Constructor.
     *
     * @param site
     *            editor site
     * @param file
     *            file opened
     */
    public DefaultBundleGroupStrategy(IEditorSite site, IFile file) {
        super();
        this.file = file;
        this.site = site;

        String patternCore = "((_[a-z]{2,3})|(_[a-z]{2,3}_[A-Z]{2})" //$NON-NLS-1$
                + "|(_[a-z]{2,3}_[A-Z]{2}_\\w*))?(\\." //$NON-NLS-1$
                + file.getFileExtension() + ")$"; //$NON-NLS-1$

        // Compute and cache name
        String namePattern = "^(.*?)" + patternCore; //$NON-NLS-1$
        this.baseName = file.getName().replaceFirst(namePattern, "$1"); //$NON-NLS-1$

        // File matching pattern
        this.fileMatchPattern = "^(" + baseName + ")" + patternCore; //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * @see org.eclipse.babel.core.message.internal.strategy.IMessagesBundleGroupStrategy
     *      #createMessagesBundleGroupName()
     */
    public String createMessagesBundleGroupName() {
        return getResourceBundleId(file);
    }

    public String createMessagesBundleId() {
        return getResourceBundleId(file);
    }

    public static String getResourceBundleId(IResource resource) {
        String packageFragment = "";

        try {
            IJavaElement propertyFile = JavaCore.create(resource.getParent());
            if (propertyFile != null
                    && propertyFile instanceof IPackageFragment)
                packageFragment = ((IPackageFragment) propertyFile)
                        .getElementName();
        } catch (NoClassDefFoundError e) {
            // do nothing
        }
        return (packageFragment.length() > 0 ? packageFragment + "." : "")
                + getResourceBundleName(resource);
    }

    public static String getResourceBundleName(IResource res) {
        String name = res.getName();
        String regex = "^(.*?)" //$NON-NLS-1$
                + "((_[a-z]{2,3})|(_[a-z]{2,3}_[A-Z]{2})" //$NON-NLS-1$
                + "|(_[a-z]{2,3}_[A-Z]{2}_\\w*))?(\\." //$NON-NLS-1$
                + res.getFileExtension() + ")$"; //$NON-NLS-1$
        return name.replaceFirst(regex, "$1"); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.babel.core.bundle.IMessagesBundleGroupStrategy
     *      #loadMessagesBundles()
     */
    public MessagesBundle[] loadMessagesBundles() throws MessageException {
        Collection<MessagesBundle> bundles = new ArrayList<MessagesBundle>();
        collectBundlesInContainer(file.getParent(), bundles);
        return bundles.toArray(EMPTY_BUNDLES);
    }

    protected void collectBundlesInContainer(IContainer container,
            Collection<MessagesBundle> bundlesCollector)
            throws MessageException {
        if (!container.exists()) {
            return;
        }
        IResource[] resources = null;
        try {
            resources = container.members();
        } catch (CoreException e) {
            throw new MessageException("Can't load resource bundles.", e); //$NON-NLS-1$
        }

        for (int i = 0; i < resources.length; i++) {
            IResource resource = resources[i];
            String resourceName = resource.getName();
            if (resource instanceof IFile
                    && resourceName.matches(fileMatchPattern)) {
                // Build local title
                String localeText = resourceName.replaceFirst(fileMatchPattern,
                        "$2"); //$NON-NLS-1$
                Locale locale = BabelUtils.parseLocale(localeText);
                if (UIUtils.isDisplayed(locale)) {
                    bundlesCollector.add(createBundle(locale, resource));
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.babel.core.bundle.IBundleGroupStrategy#createBundle(java.
     * util.Locale)
     */
    public MessagesBundle createMessagesBundle(Locale locale) {
        // create new empty locale file
        IFile openedFile = getOpenedFile();
        IPath path = openedFile.getProjectRelativePath();
        String localeStr = locale != null ? "_" + locale.toString() : "";
        String newFilename = getBaseName() + localeStr + "."
                + openedFile.getFileExtension();
        IFile newFile = openedFile.getProject()
                .getFile(
                        path.removeLastSegments(1).addTrailingSeparator()
                                + newFilename);

        if (!newFile.exists()) {
            try {
                // create new ifile with an empty input stream
                newFile.create(new ByteArrayInputStream(new byte[0]),
                        IResource.NONE, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return createBundle(locale, newFile);
    }

    /**
     * Creates a resource bundle for an existing resource.
     *
     * @param locale
     *            locale for which to create a bundle
     * @param resource
     *            resource used to create bundle
     * @return an initialized bundle
     */
    protected MessagesBundle createBundle(Locale locale, IResource resource)
            throws MessageException {
        try {;

            // TODO have bundleResource created in a separate factory
            // shared between strategies
            IMessagesResource messagesResource;
            if (site == null) {
                // site is null during the build.
                messagesResource = new PropertiesIFileResource(locale,
                        new PropertiesSerializer(MsgEditorPreferences.getSerializerConfig()),
                        new PropertiesDeserializer(MsgEditorPreferences.getDeserializerConfig()),
                        (IFile) resource, MessagesEditorPlugin.getDefault());
            } else {
                messagesResource = new EclipsePropertiesEditorResource(locale,
                        new PropertiesSerializer(MsgEditorPreferences.getSerializerConfig()),
                        new PropertiesDeserializer(
                        		MsgEditorPreferences.getDeserializerConfig()), createEditor(
                                resource, locale));
            }
            return new MessagesBundle(messagesResource);
        } catch (PartInitException e) {
            throw new MessageException("Cannot create bundle for locale " //$NON-NLS-1$
                    + locale + " and resource " + resource, e); //$NON-NLS-1$
        }
    }

    /**
     * Creates an Eclipse editor.
     *
     * @param site
     * @param resource
     * @param locale
     * @return
     * @throws PartInitException
     */
    protected TextEditor createEditor(IResource resource, Locale locale)
            throws PartInitException {

        TextEditor textEditor = null;
        if (resource != null && resource instanceof IFile) {
            try {
                resource.refreshLocal(IResource.DEPTH_ZERO, null);
            } catch (CoreException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            IEditorInput newEditorInput = new FileEditorInput((IFile) resource);
            textEditor = null;
            try {
                // Use PropertiesFileEditor if available
                textEditor = (TextEditor) Class.forName(
                        PROPERTIES_EDITOR_CLASS_NAME).newInstance();
            } catch (Exception e) {
                // Use default editor otherwise
                textEditor = new TextEditor();
            }
            textEditor.init(site, newEditorInput);
        }
        return textEditor;
    }

    /**
     * @return The file opened.
     */
    protected IFile getOpenedFile() {
        return file;
    }

    /**
     * @return The base name of the resource bundle.
     */
    protected String getBaseName() {
        return baseName;
    }

    public String getProjectName() {
        return ResourcesPlugin.getWorkspace().getRoot()
                .getProject(file.getFullPath().segments()[0]).getName();
    }

}
