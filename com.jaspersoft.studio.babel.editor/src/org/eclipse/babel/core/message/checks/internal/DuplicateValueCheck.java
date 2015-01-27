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
package org.eclipse.babel.core.message.checks.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.util.BabelUtils;

/**
 * Checks if key as a duplicate value.
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class DuplicateValueCheck implements IMessageCheck {

    private String[] duplicateKeys;

    /**
     * Constructor.
     */
    public DuplicateValueCheck() {
        super();
    }

    /**
     * Resets the collected keys to null.
     */
    public void reset() {
        duplicateKeys = null;
    }

    public boolean checkKey(IMessagesBundleGroup messagesBundleGroup,
            IMessage message) {
        Collection<String> keys = new ArrayList<String>();
        if (message != null) {
            IMessagesBundle messagesBundle = messagesBundleGroup
                    .getMessagesBundle(message.getLocale());
            for (IMessage duplicateEntry : messagesBundle.getMessages()) {
                if (!message.getKey().equals(duplicateEntry.getKey())
                        && BabelUtils.equals(message.getValue(),
                                duplicateEntry.getValue())) {
                    keys.add(duplicateEntry.getKey());
                }
            }
            if (!keys.isEmpty()) {
                keys.add(message.getKey());
            }
        }

        duplicateKeys = keys.toArray(new String[] {});
        return !keys.isEmpty();
    }

    public String[] getDuplicateKeys() {
        return duplicateKeys;
    }

}
