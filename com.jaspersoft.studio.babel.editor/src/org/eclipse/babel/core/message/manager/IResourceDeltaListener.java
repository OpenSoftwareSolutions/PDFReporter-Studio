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

import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.core.resources.IResource;

/**
 * Used to update TapiJI (ResourceBundleManager) when bundles have been removed. <br>
 * <br>
 * 
 * @author Alexej Strelzow
 */
public interface IResourceDeltaListener {

    /**
     * Called when a resource (= bundle) has been removed
     * 
     * @param resourceBundleId
     *            The {@link IMessagesBundleGroup} which contains the bundle
     * @param resource
     *            The resource itself
     */
    public void onDelete(String resourceBundleId, IResource resource);

    /**
     * Called when a {@link IMessagesBundleGroup} has been removed
     * 
     * @param bundleGroup
     *            The removed bundle group
     */
    public void onDelete(IMessagesBundleGroup bundleGroup);

}
