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
package org.eclipse.babel.core.message.resource.ser;

/**
 * Interface for the deserialization process.
 * 
 * @author Alexej Strelzow
 */
public interface IPropertiesDeserializerConfig {

    /**
     * Defaults true.
     * 
     * @return Returns the unicodeUnescapeEnabled.
     */
    boolean isUnicodeUnescapeEnabled();

}