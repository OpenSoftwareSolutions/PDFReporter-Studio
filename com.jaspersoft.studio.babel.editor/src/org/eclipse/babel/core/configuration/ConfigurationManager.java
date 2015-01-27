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

package org.eclipse.babel.core.configuration;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.resource.ser.IPropertiesDeserializerConfig;
import org.eclipse.babel.core.message.resource.ser.IPropertiesSerializerConfig;
import org.eclipse.babel.core.message.resource.ser.PropertiesDeserializer;
import org.eclipse.babel.core.message.resource.ser.PropertiesSerializer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

/**
 * Singelton, which provides information regarding the configuration of: <li>
 * TapiJI Preference Page: interface is {@link IConfiguration}</li> <li>
 * Serializing {@link MessagesBundleGroup} to property file</li> <li>
 * Deserializing {@link MessagesBundleGroup} from property file</li> <br>
 * <br>
 * 
 * @author Alexej Strelzow
 */
public class ConfigurationManager {

    private static ConfigurationManager INSTANCE;

    private IConfiguration config;

    private IPropertiesSerializerConfig serializerConfig;

    private IPropertiesDeserializerConfig deserializerConfig;

    private ConfigurationManager() {
        config = getConfig();
    }

    private IConfiguration getConfig() {
        IExtensionPoint extp = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.babel.core.babelConfiguration");
        if (extp == null) return new BasePreferences();
        IConfigurationElement[] elements = extp.getConfigurationElements();
        if (elements.length != 0) {
            try {
                return (IConfiguration) elements[0]
                        .createExecutableExtension("class");
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @return The singleton instance
     */
    public static ConfigurationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigurationManager();
        }
        return INSTANCE;
    }

    /**
     * @return TapiJI configuration
     */
    public IConfiguration getConfiguration() {
        return this.config;
    }

    /**
     * @return Config needed for {@link PropertiesSerializer}
     */
    public IPropertiesSerializerConfig getSerializerConfig() {
        return serializerConfig;
    }

    /**
     * @param serializerConfig
     *            The config for serialization
     */
	public void setSerializerConfig(IPropertiesSerializerConfig serializerConfig) {
        this.serializerConfig = serializerConfig;
    }

    /**
     * @return Config needed for {@link PropertiesDeserializer}
     */
    public IPropertiesDeserializerConfig getDeserializerConfig() {
        return deserializerConfig;
    }

    /**
     * @param serializerConfig
     *            The config for deserialization
     */
    public void setDeserializerConfig(
            IPropertiesDeserializerConfig deserializerConfig) {
        this.deserializerConfig = deserializerConfig;
    }

}
