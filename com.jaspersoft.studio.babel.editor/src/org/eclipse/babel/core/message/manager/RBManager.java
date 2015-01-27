/*******************************************************************************
 * Copyright (c) 2012 Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexej Strelzow - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.core.message.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.babel.core.configuration.DirtyHack;
import org.eclipse.babel.core.factory.MessagesBundleGroupFactory;
import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.internal.Message;
import org.eclipse.babel.core.message.internal.MessagesBundle;
import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.refactoring.IRefactoringService;
import org.eclipse.babel.core.refactoring.StandardRefactoring;
import org.eclipse.babel.core.util.FileUtils;
import org.eclipse.babel.core.util.NameUtils;
import org.eclipse.babel.core.util.PDEUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

/**
 * Manages all {@link MessagesBundleGroup}s. That is: <li>Hold map with projects
 * and their RBManager (1 RBManager per project)</li> <li>Hold up-to-date map
 * with resource bundles (= {@link MessagesBundleGroup})</li> <li>Hold
 * {@link IMessagesEditorListener}, which can be used to keep systems in sync</li>
 * <br>
 * <br>
 * 
 * @author Alexej Strelzow
 */
public final class RBManager {

    private static Map<IProject, RBManager> managerMap = new HashMap<IProject, RBManager>();

    /** <package>.<resourceBundleName> , IMessagesBundleGroup */
    private final Map<String, IMessagesBundleGroup> resourceBundles;

    private static RBManager INSTANCE;

    private final List<IMessagesEditorListener> editorListeners;

    private final List<IResourceDeltaListener> resourceListeners;

    private IProject project;

    private static final String TAPIJI_NATURE = "org.eclipse.babel.tapiji.tools.core.ui.nature";

    final static Logger logger = Logger.getLogger(RBManager.class.getName());

    private static IRefactoringService refactorService;

    private RBManager() {
        resourceBundles = new HashMap<String, IMessagesBundleGroup>();
        editorListeners = new ArrayList<IMessagesEditorListener>(3);
        resourceListeners = new ArrayList<IResourceDeltaListener>(2);
    }

    /**
     * @param resourceBundleId
     *            <package>.<resourceBundleName>
     * @return {@link IMessagesBundleGroup} if found, else <code>null</code>
     */
    public IMessagesBundleGroup getMessagesBundleGroup(String resourceBundleId) {
        if (!resourceBundles.containsKey(resourceBundleId)) {
            logger.log(Level.SEVERE,
                    "getMessagesBundleGroup with non-existing Id: "
                            + resourceBundleId);
            return null;
        } else {
            return resourceBundles.get(resourceBundleId);
        }
    }

    /**
     * @return All the names of the <code>resourceBundles</code> in the format:
     *         <projectName>/<resourceBundleId>
     */
    public List<String> getMessagesBundleGroupNames() {
        List<String> bundleGroupNames = new ArrayList<String>();

        for (String key : resourceBundles.keySet()) {
            bundleGroupNames.add(project.getName() + "/" + key);
        }
        return bundleGroupNames;
    }

    /**
     * @return All the {@link #getMessagesBundleGroupNames()} of all the
     *         projects.
     */
    public static List<String> getAllMessagesBundleGroupNames() {
        List<String> bundleGroupNames = new ArrayList<String>();

        for (IProject project : getAllSupportedProjects()) {
            RBManager manager = getInstance(project);
            for (String name : manager.getMessagesBundleGroupNames()) {
                if (!bundleGroupNames.contains(name)) {
                    bundleGroupNames.add(name);
                }
            }
        }
        return bundleGroupNames;
    }

    /**
     * Notification, that a {@link IMessagesBundleGroup} has been created and
     * needs to be managed by the {@link RBManager}.
     * 
     * @param bundleGroup
     *            The new {@link IMessagesBundleGroup}
     */
    public void notifyMessagesBundleGroupCreated(
            IMessagesBundleGroup bundleGroup) {
        if (resourceBundles.containsKey(bundleGroup.getResourceBundleId())) {
            IMessagesBundleGroup oldbundleGroup = resourceBundles
                    .get(bundleGroup.getResourceBundleId());

            // not the same object
            if (!equalHash(oldbundleGroup, bundleGroup)) {
                // we need to distinguish between 2 kinds of resources:
                // 1) Property-File
                // 2) Eclipse-Editor
                // When first 1) is used, and some operations where made, we
                // need to
                // sync 2) when it appears!
                boolean oldHasPropertiesStrategy = oldbundleGroup
                        .hasPropertiesFileGroupStrategy();
                boolean newHasPropertiesStrategy = bundleGroup
                        .hasPropertiesFileGroupStrategy();

                // in this case, the old one is only writing to the property
                // file, not the editor
                // we have to sync them and store the bundle with the editor as
                // resource
                if (oldHasPropertiesStrategy && !newHasPropertiesStrategy) {

                    syncBundles(bundleGroup, oldbundleGroup);
                    resourceBundles.put(bundleGroup.getResourceBundleId(),
                            bundleGroup);

                    logger.log(
                            Level.INFO,
                            "sync: " + bundleGroup.getResourceBundleId()
                                    + " with "
                                    + oldbundleGroup.getResourceBundleId());

                    oldbundleGroup.dispose();

                } else if ((oldHasPropertiesStrategy && newHasPropertiesStrategy)
                        || (!oldHasPropertiesStrategy && !newHasPropertiesStrategy)) {

                    // syncBundles(oldbundleGroup, bundleGroup); do not need
                    // that, because we take the new one
                    // and we do that, because otherwise we cache old
                    // Text-Editor instances, which we
                    // do not need -> read only phenomenon
                    resourceBundles.put(bundleGroup.getResourceBundleId(),
                            bundleGroup);

                    logger.log(
                            Level.INFO,
                            "replace: " + bundleGroup.getResourceBundleId()
                                    + " with "
                                    + oldbundleGroup.getResourceBundleId());

                    oldbundleGroup.dispose();
                } else {
                    // in this case our old resource has an EditorSite, but not
                    // the new one
                    logger.log(Level.INFO,
                            "dispose: " + bundleGroup.getResourceBundleId());

                    bundleGroup.dispose();
                }
            }
        } else {
            resourceBundles.put(bundleGroup.getResourceBundleId(), bundleGroup);

            logger.log(Level.INFO, "add: " + bundleGroup.getResourceBundleId());
        }
    }

    /**
     * Notification, that a {@link IMessagesBundleGroup} has been deleted!
     * 
     * @param bundleGroup
     *            The {@link IMessagesBundleGroup} to remove
     */
    public void notifyMessagesBundleGroupDeleted(
            IMessagesBundleGroup bundleGroup) {
        if (resourceBundles.containsKey(bundleGroup.getResourceBundleId())) {
            if (equalHash(
                    resourceBundles.get(bundleGroup.getResourceBundleId()),
                    bundleGroup)) {
                resourceBundles.remove(bundleGroup.getResourceBundleId());

                for (IResourceDeltaListener deltaListener : resourceListeners) {
                    deltaListener.onDelete(bundleGroup);
                }
            }
        }
    }

    /**
     * Notification, that a resource bundle (= {@link MessagesBundle}) have been
     * removed.
     * 
     * @param resourceBundle
     *            The removed {@link MessagesBundle}
     */
    public void notifyResourceRemoved(IResource resourceBundle) {
        String resourceBundleId = NameUtils.getResourceBundleId(resourceBundle);

        IMessagesBundleGroup bundleGroup = resourceBundles
                .get(resourceBundleId);

        if (bundleGroup != null) {
            Locale locale = NameUtils.getLocaleByName(
                    NameUtils.getResourceBundleName(resourceBundle),
                    resourceBundle.getName());
            IMessagesBundle messagesBundle = bundleGroup
                    .getMessagesBundle(locale);
            if (messagesBundle != null) {
                bundleGroup.removeMessagesBundle(messagesBundle);
            }

            for (IResourceDeltaListener deltaListener : resourceListeners) {
                deltaListener.onDelete(resourceBundleId, resourceBundle);
            }

            if (bundleGroup.getMessagesBundleCount() == 0) {
                notifyMessagesBundleGroupDeleted(bundleGroup);
            }
        }

        // TODO: maybe save and reinit the editor?

    }

    /**
     * Because BABEL-Builder does not work correctly (adds 1 x and removes 2 x
     * the SAME {@link MessagesBundleGroup}!)
     * 
     * @param oldBundleGroup
     *            {@link IMessagesBundleGroup}
     * @param newBundleGroup
     *            {@link IMessagesBundleGroup}
     * @return <code>true</code> if same {@link IMessagesBundleGroup}, else
     *         <code>false</code>
     */
    private boolean equalHash(IMessagesBundleGroup oldBundleGroup,
            IMessagesBundleGroup newBundleGroup) {
        return oldBundleGroup.hashCode() == newBundleGroup.hashCode();
    }

    /**
     * Has only one use case. If we worked with property-file as resource and
     * afterwards the messages editor pops open, we need to sync them, so that
     * the information of the property-file won't get lost.
     * 
     * @param oldBundleGroup
     *            The prior {@link IMessagesBundleGroup}
     * @param newBundleGroup
     *            The replacement
     */
    private void syncBundles(IMessagesBundleGroup oldBundleGroup,
            IMessagesBundleGroup newBundleGroup) {
        List<IMessagesBundle> bundlesToRemove = new ArrayList<IMessagesBundle>();
        List<IMessage> keysToRemove = new ArrayList<IMessage>();

        DirtyHack.setFireEnabled(false); // hebelt AbstractMessageModel aus
        // sonst m�ssten wir in setText von EclipsePropertiesEditorResource
        // ein
        // asyncExec zulassen

        for (IMessagesBundle newBundle : newBundleGroup.getMessagesBundles()) {
            IMessagesBundle oldBundle = oldBundleGroup
                    .getMessagesBundle(newBundle.getLocale());
            if (oldBundle == null) { // it's a new one
                oldBundleGroup.addMessagesBundle(newBundle.getLocale(),
                        newBundle);
            } else { // check keys
                for (IMessage newMsg : newBundle.getMessages()) {
                    if (oldBundle.getMessage(newMsg.getKey()) == null) {
                        // new entry, create new message
                        oldBundle.addMessage(new Message(newMsg.getKey(),
                                newMsg.getLocale()));
                    } else { // update old entries
                        IMessage oldMsg = oldBundle.getMessage(newMsg.getKey());
                        if (oldMsg == null) { // it's a new one
                            oldBundle.addMessage(newMsg);
                        } else { // check value
                            oldMsg.setComment(newMsg.getComment());
                            oldMsg.setText(newMsg.getValue());
                        }
                    }
                }
            }
        }

        // check keys
        for (IMessagesBundle oldBundle : oldBundleGroup.getMessagesBundles()) {
            IMessagesBundle newBundle = newBundleGroup
                    .getMessagesBundle(oldBundle.getLocale());
            if (newBundle == null) { // we have an old one
                bundlesToRemove.add(oldBundle);
            } else {
                for (IMessage oldMsg : oldBundle.getMessages()) {
                    if (newBundle.getMessage(oldMsg.getKey()) == null) {
                        keysToRemove.add(oldMsg);
                    }
                }
            }
        }

        for (IMessagesBundle bundle : bundlesToRemove) {
            oldBundleGroup.removeMessagesBundle(bundle);
        }

        for (IMessage msg : keysToRemove) {
            IMessagesBundle mb = oldBundleGroup.getMessagesBundle(msg
                    .getLocale());
            if (mb != null) {
                mb.removeMessage(msg.getKey());
            }
        }

        DirtyHack.setFireEnabled(true);

    }

    /**
     * If TapiJI needs to delete sth.
     * 
     * @param resourceBundleId
     *            The resourceBundleId
     */
    public void deleteMessagesBundleGroup(String resourceBundleId) {
        // TODO: Try to unify it some time
        if (resourceBundles.containsKey(resourceBundleId)) {
            resourceBundles.remove(resourceBundleId);
        } else {
            logger.log(Level.SEVERE,
                    "deleteMessagesBundleGroup with non-existing Id: "
                            + resourceBundleId);
        }
    }

    /**
     * @param resourceBundleId
     *            The resourceBundleId
     * @return <code>true</code> if the manager knows the
     *         {@link MessagesBundleGroup} with the id resourceBundleId
     */
    public boolean containsMessagesBundleGroup(String resourceBundleId) {
        return resourceBundles.containsKey(resourceBundleId);
    }

    /**
     * @param project
     *            The project, which is managed by the {@link RBManager}
     * @return The corresponding {@link RBManager} to the project
     */
    public static RBManager getInstance(IProject project) {
        // set host-project
        if (PDEUtils.isFragment(project)) {
            project = PDEUtils.getFragmentHost(project);
        }

        INSTANCE = managerMap.get(project);

        if (INSTANCE == null) {
            INSTANCE = new RBManager();
            INSTANCE.project = project;
            managerMap.put(project, INSTANCE);
            //INSTANCE.detectResourceBundles();

            refactorService = getRefactoringService();
        }

        return INSTANCE;
    }

    /**
     * @param projectName
     *            The name of the project, which is managed by the
     *            {@link RBManager}
     * @return The corresponding {@link RBManager} to the project
     */
    public static RBManager getInstance(String projectName) {
        for (IProject project : getAllWorkspaceProjects(true)) {
            if (project.getName().equals(projectName)) {
                // check if the projectName is a fragment and return the manager
                // for the host
                if (PDEUtils.isFragment(project)) {
                    return getInstance(PDEUtils.getFragmentHost(project));
                } else {
                    return getInstance(project);
                }
            }
        }
        return null;
    }

    /**
     * @param ignoreNature
     *            <code>true</code> if the internationalization nature should be
     *            ignored, else <code>false</code>
     * @return A set of projects, which have the nature (ignoreNature == false)
     *         or not.
     */
    public static Set<IProject> getAllWorkspaceProjects(boolean ignoreNature) {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
                .getProjects();
        Set<IProject> projs = new HashSet<IProject>();

        for (IProject p : projects) {
            try {
                if (p.isOpen() && (ignoreNature || p.hasNature(TAPIJI_NATURE))) {
                    projs.add(p);
                }
            } catch (CoreException e) {
                logger.log(Level.SEVERE,
                        "getAllWorkspaceProjects(...): hasNature failed!", e);
            }
        }
        return projs;
    }

    /**
     * @return All supported projects, those who have the correct nature.
     */
    public static Set<IProject> getAllSupportedProjects() {
        return getAllWorkspaceProjects(false);
    }

    /**
     * @param listener
     *            {@link IMessagesEditorListener} to add
     */
    public void addMessagesEditorListener(IMessagesEditorListener listener) {
        this.editorListeners.add(listener);
    }

    /**
     * @param listener
     *            {@link IMessagesEditorListener} to remove
     */
    public void removeMessagesEditorListener(IMessagesEditorListener listener) {
        this.editorListeners.remove(listener);
    }

    /**
     * @param listener
     *            {@link IResourceDeltaListener} to add
     */
    public void addResourceDeltaListener(IResourceDeltaListener listener) {
        this.resourceListeners.add(listener);
    }

    /**
     * @param listener
     *            {@link IResourceDeltaListener} to remove
     */
    public void removeResourceDeltaListener(IResourceDeltaListener listener) {
        this.resourceListeners.remove(listener);
    }

    /**
     * Fire: MessagesEditor has been saved
     */
    public void fireEditorSaved() {
        for (IMessagesEditorListener listener : this.editorListeners) {
            listener.onSave();
        }
        logger.log(Level.INFO, "fireEditorSaved");
    }

    /**
     * Fire: MessagesEditor has been modified
     */
    public void fireEditorChanged() {
        for (IMessagesEditorListener listener : this.editorListeners) {
            listener.onModify();
        }
        logger.log(Level.INFO, "fireEditorChanged");
    }

    /**
     * Fire: {@link IMessagesBundle} has been edited
     */
    public void fireResourceChanged(IMessagesBundle bundle) {
        for (IMessagesEditorListener listener : this.editorListeners) {
            listener.onResourceChanged(bundle);
        }
    }

    /**
     * Detects all resource bundles, which we want to work with.
     */
    @Deprecated
    protected void detectResourceBundles() {
        try {
            project.accept(new ResourceBundleDetectionVisitor(this));

            IProject[] fragments = PDEUtils.lookupFragment(project);
            if (fragments != null) {
                for (IProject p : fragments) {
                    p.accept(new ResourceBundleDetectionVisitor(this));
                }

            }
        } catch (CoreException e) {
            logger.log(Level.SEVERE, "detectResourceBundles: accept failed!", e);
        }
    }

    // passive loading -> see detectResourceBundles
    /**
     * Invoked by {@link #detectResourceBundles()}.
     */
    public void addBundleResource(IResource resource) {
        // create it with MessagesBundleFactory or read from resource!
        // we can optimize that, now we create a bundle group for each bundle
        // we should create a bundle group only once!

        String resourceBundleId = NameUtils.getResourceBundleId(resource);
        if (!resourceBundles.containsKey(resourceBundleId)) {
            // if we do not have this condition, then you will be doomed with
            // resource out of syncs, because here we instantiate
            // PropertiesFileResources, which have an evil setText-Method
            MessagesBundleGroupFactory.createBundleGroup(resource);

            logger.log(Level.INFO, "addBundleResource (passive loading): "
                    + resourceBundleId + " (" + resource.getName() + ")");
        }
    }

    public void writeToFile(IMessagesBundleGroup bundleGroup) {
        for (IMessagesBundle bundle : bundleGroup.getMessagesBundles()) {
            FileUtils.writeToFile(bundle);
            fireResourceChanged(bundle);
        }
    }

    private static IRefactoringService getRefactoringService() {
        IExtensionPoint extp = Platform.getExtensionRegistry().getExtensionPoint( "org.eclipse.babel.core.refactoringService");
        if (extp != null){
	        IConfigurationElement[] elements = extp.getConfigurationElements();
	
	        if (elements.length != 0) {
	            try {
	                return (IRefactoringService) elements[0]
	                        .createExecutableExtension("class");
	            } catch (CoreException e) {
	                e.printStackTrace();
	            }
	        }
        }
        return new StandardRefactoring();
    }

    public static IRefactoringService getRefactorService() {
        return refactorService;
    }
}
