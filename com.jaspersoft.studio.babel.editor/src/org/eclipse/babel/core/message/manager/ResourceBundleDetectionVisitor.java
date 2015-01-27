/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer, Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Reiterer - initial API and implementation
 *     Alexej Strelzow - detection if isResourceBundleFile
 ******************************************************************************/

package org.eclipse.babel.core.message.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.babel.core.configuration.ConfigurationManager;
import org.eclipse.babel.core.configuration.IConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class ResourceBundleDetectionVisitor implements IResourceVisitor,
        IResourceDeltaVisitor {

    private RBManager manager = null;

    public ResourceBundleDetectionVisitor(RBManager manager) {
        this.manager = manager;
    }

    public boolean visit(IResource resource) throws CoreException {
        try {
            if (isResourceBundleFile(resource)) {
                manager.addBundleResource(resource);
                return false;
            } else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean visit(IResourceDelta delta) throws CoreException {
        IResource resource = delta.getResource();

        if (isResourceBundleFile(resource)) {
            // ResourceBundleManager.getManager(resource.getProject()).bundleResourceModified(delta);
            return false;
        }

        return true;
    }

    private final String RB_MARKER_ID = "org.eclipse.babel.tapiji.tools.core.ResourceBundleAuditMarker";

    private boolean isResourceBundleFile(IResource file) {
        boolean isValied = false;

        if (file != null && file instanceof IFile && !file.isDerived()
                && file.getFileExtension() != null
                && file.getFileExtension().equalsIgnoreCase("properties")) {
            isValied = true;

            List<CheckItem> list = getBlacklistItems();
            for (CheckItem item : list) {
                if (item.getChecked()
                        && file.getFullPath().toString()
                                .matches(item.getName())) {
                    isValied = false;

                    // if properties-file is not RB-file and has
                    // ResouceBundleMarker, deletes all ResouceBundleMarker of
                    // the file
                    if (hasResourceBundleMarker(file))
                        try {
                            file.deleteMarkers(RB_MARKER_ID, true,
                                    IResource.DEPTH_INFINITE);
                        } catch (CoreException e) {
                        }
                }
            }
        }

        return isValied;
    }

    private List<CheckItem> getBlacklistItems() {
        IConfiguration configuration = ConfigurationManager.getInstance()
                .getConfiguration();
        if (configuration != null) {
            return convertStringToList(configuration.getNonRbPattern());
        } else {
            return new ArrayList<CheckItem>();
        }
    }

    private static final String DELIMITER = ";";
    private static final String ATTRIBUTE_DELIMITER = ":";

    private List<CheckItem> convertStringToList(String string) {
        StringTokenizer tokenizer = new StringTokenizer(string, DELIMITER);
        int tokenCount = tokenizer.countTokens();
        List<CheckItem> elements = new LinkedList<CheckItem>();

        for (int i = 0; i < tokenCount; i++) {
            StringTokenizer attribute = new StringTokenizer(
                    tokenizer.nextToken(), ATTRIBUTE_DELIMITER);
            String name = attribute.nextToken();
            boolean checked;
            if (attribute.nextToken().equals("true"))
                checked = true;
            else
                checked = false;

            elements.add(new CheckItem(name, checked));
        }
        return elements;
    }

    /**
     * Checks whether a RB-file has a problem-marker
     */
    public boolean hasResourceBundleMarker(IResource r) {
        try {
            if (r.findMarkers(RB_MARKER_ID, true, IResource.DEPTH_INFINITE).length > 0)
                return true;
            else
                return false;
        } catch (CoreException e) {
            return false;
        }
    }

    private class CheckItem {
        boolean checked;
        String name;

        public CheckItem(String item, boolean checked) {
            this.name = item;
            this.checked = checked;
        }

        public String getName() {
            return name;
        }

        public boolean getChecked() {
            return checked;
        }
    }

}
