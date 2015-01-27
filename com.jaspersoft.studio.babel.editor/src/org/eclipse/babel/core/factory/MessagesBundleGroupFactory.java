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

package org.eclipse.babel.core.factory;

import java.io.File;

import org.eclipse.babel.core.configuration.ConfigurationManager;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.strategy.PropertiesFileGroupStrategy;
import org.eclipse.core.resources.IResource;

/**
 * Factory class for creating a {@link MessagesBundleGroup} with a
 * {@link PropertiesFileGroupStrategy}. This is in use when we work with TapiJI
 * only and not with <code>EclipsePropertiesEditorResource</code>. <br>
 * <br>
 * 
 * @author Alexej Strelzow
 */
public class MessagesBundleGroupFactory {

    public static IMessagesBundleGroup createBundleGroup(IResource resource) {
    	
        File ioFile = new File(resource.getRawLocation().toFile().getPath());

        return new MessagesBundleGroup(new PropertiesFileGroupStrategy(ioFile,
                ConfigurationManager.getInstance().getSerializerConfig(),
                ConfigurationManager.getInstance().getDeserializerConfig()));
    }
}
