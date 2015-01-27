/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - moved code to here
 ******************************************************************************/
package org.eclipse.babel.editor.preferences;

import org.eclipse.babel.core.message.resource.ser.IPropertiesDeserializerConfig;

/**
 * The concrete implementation of {@link IPropertiesDeserializerConfig}.
 * 
 * @author Alexej Strelzow
 */
public class PropertiesDeserializerConfig implements
        IPropertiesDeserializerConfig { // Moved from MsgEditorPreferences, to
                                        // make it more flexible.


    /**
     * Gets whether to convert encoded strings to unicode characters when
     * reading file.
     * 
     * @return <code>true</code> if converting
     */
    public boolean isUnicodeUnescapeEnabled() {
        return MsgEditorPreferences.getUnicodeUnescapeEnabled();
    }

}
