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
package org.eclipse.babel.editor.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.babel.core.message.internal.AbstractIFileChangeListener;
import org.eclipse.babel.core.message.internal.AbstractIFileChangeListener.IFileChangeListenerRegistry;
import org.eclipse.babel.editor.builder.ToggleNatureAction;
import org.eclipse.babel.editor.preferences.MsgEditorPreferences;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.pde.nls.internal.ui.model.ResourceBundleModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class MessagesEditorPlugin extends AbstractUIPlugin implements
        IFileChangeListenerRegistry {

    // TODO move somewhere more appropriate
    public static final String MARKER_TYPE = "org.eclipse.babel.editor.nlsproblem"; //$NON-NLS-1$

    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.babel.editor";

    // The shared instance
    private static MessagesEditorPlugin plugin;

    // The resource change litener for the entire plugin.
    // objects interested in changes in the workspace resources must
    // subscribe to this listener by calling subscribe/unsubscribe on the
    // plugin.
    private IResourceChangeListener resourceChangeListener;

    // The map of resource change subscribers.
    // The key is the full path of the resource listened. The value as set of
    // SimpleResourceChangeListners
    // private Map<String,Set<SimpleResourceChangeListners>>
    // resourceChangeSubscribers;
    private Map<String, Set<AbstractIFileChangeListener>> resourceChangeSubscribers;

    private ResourceBundleModel model;

    /**
     * The constructor
     */
    public MessagesEditorPlugin() {
        resourceChangeSubscribers = new HashMap<String, Set<AbstractIFileChangeListener>>();
        plugin = this;
    }
    
    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        // make sure the rbe nature and builder are set on java projects
        // if that is what the users prefers.
        if (MsgEditorPreferences.isBuilderSetupAutomatically()) {
            ToggleNatureAction.addOrRemoveNatureOnAllJavaProjects(true);
        }

        // the unique file change listener
        resourceChangeListener = new IResourceChangeListener() {
            public void resourceChanged(IResourceChangeEvent event) {
                IResource resource = event.getResource();
                if (resource != null) {
                    String fullpath = resource.getFullPath().toString();
                    Set<AbstractIFileChangeListener> listeners = resourceChangeSubscribers.get(fullpath);
                    if (listeners != null) {
                        AbstractIFileChangeListener[] larray = listeners .toArray(new AbstractIFileChangeListener[0]);
                        for (int i = 0; i < larray.length; i++) {
                            larray[i].listenedFileChanged(event);
                        }
                    }
                }
            }
        };
        ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);
        try {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                    Display.getDefault().addFilter(SWT.KeyUp, new UndoKeyListener());

            }
        });
        } catch (NullPointerException e) {
            // TODO [RAP] Non UI-Thread, no default display available, in RAP
            // multiple clients and displays
        }
    }

    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
        super.stop(context);
    }


    private static class UndoKeyListener implements Listener {

        public void handleEvent(Event event) {
            Control focusControl = event.display.getFocusControl();
            if (event.stateMask == SWT.CONTROL && focusControl instanceof Text) {

                Text txt = (Text) focusControl;
                String actText = txt.getText();
                Stack<String> undoStack = (Stack<String>) txt.getData("UNDO");
                Stack<String> redoStack = (Stack<String>) txt.getData("REDO");

                if (event.keyCode == 'z' && undoStack != null
                        && redoStack != null) { // Undo
                    event.doit = false;

                    if (undoStack.size() > 0) {
                        String peek = undoStack.peek();
                        if (actText != null && !txt.getText().equals(peek)) {
                            String pop = undoStack.pop();
                            txt.setText(pop);
                            txt.setSelection(pop.length());
                            redoStack.push(actText);
                        }
                    }
                } else if (event.keyCode == 'y' && undoStack != null
                        && redoStack != null) { // Redo

                    event.doit = false;

                    if (redoStack.size() > 0) {
                        String peek = redoStack.peek();

                        if (actText != null && !txt.getText().equals(peek)) {
                            String pop = redoStack.pop();
                            txt.setText(pop);
                            txt.setSelection(pop.length());
                            undoStack.push(actText);
                        }
                    }
                }
            }
        }

    }

   
    /**
     * @param rcl
     *            Adds a subscriber to a resource change event.
     */
    public void subscribe(AbstractIFileChangeListener fileChangeListener) {
        synchronized (resourceChangeListener) {
            String channel = fileChangeListener.getListenedFileFullPath();
            Set<AbstractIFileChangeListener> channelListeners = resourceChangeSubscribers
                    .get(channel);
            if (channelListeners == null) {
                channelListeners = new HashSet<AbstractIFileChangeListener>();
                resourceChangeSubscribers.put(channel, channelListeners);
            }
            channelListeners.add(fileChangeListener);
        }
    }

    /**
     * @param rcl
     *            Removes a subscriber to a resource change event.
     */
    public void unsubscribe(AbstractIFileChangeListener fileChangeListener) {
        synchronized (resourceChangeListener) {
            String channel = fileChangeListener.getListenedFileFullPath();
            Set<AbstractIFileChangeListener> channelListeners = resourceChangeSubscribers
                    .get(channel);
            if (channelListeners != null
                    && channelListeners.remove(fileChangeListener)
                    && channelListeners.isEmpty()) {
                // nobody left listening to this file.
                resourceChangeSubscribers.remove(channel);
            }
        }
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static MessagesEditorPlugin getDefault() {
        return plugin;
    }

   

    // Stefan's activator methods:

    /**
     * Returns an image descriptor for the given icon filename.
     * 
     * @param filename
     *            the icon filename relative to the icons path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String filename) {
        String iconPath = "icons/"; //$NON-NLS-1$
        return imageDescriptorFromPlugin(PLUGIN_ID, iconPath + filename);
    }

    public static ResourceBundleModel getModel(IProgressMonitor monitor) {
        if (plugin.model == null) {
            plugin.model = new ResourceBundleModel(monitor);
        }
        return plugin.model;
    }

    public static void disposeModel() {
        if (plugin != null) {
            plugin.model = null;
        }
    }

    // Logging

    /**
     * Adds the given exception to the log.
     * 
     * @param e
     *            the exception to log
     * @return the logged status
     */
    public static IStatus log(Throwable e) {
        return log(new Status(IStatus.ERROR, PLUGIN_ID, 0, "Internal error.", e));
    }

    /**
     * Adds the given exception to the log.
     * 
     * @param exception
     *            the exception to log
     * @return the logged status
     */
    public static IStatus log(String message, Throwable exception) {
        return log(new Status(IStatus.ERROR, PLUGIN_ID, -1, message, exception));
    }

    /**
     * Adds the given <code>IStatus</code> to the log.
     * 
     * @param status
     *            the status to log
     * @return the logged status
     */
    public static IStatus log(IStatus status) {
        getDefault().getLog().log(status);
        return status;
    }

}
