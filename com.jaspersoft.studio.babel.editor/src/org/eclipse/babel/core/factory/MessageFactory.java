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

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.internal.Message;

/**
 * Factory class for creating a {@link IMessage}
 * 
 * @author Alexej Strelzow
 */
public class MessageFactory {

    static Logger logger = Logger.getLogger(MessageFactory.class
            .getSimpleName());

    /**
     * @param key
     *            The key of the message
     * @param locale
     *            The {@link Locale}
     * @return An instance of {@link IMessage}
     */
    public static IMessage createMessage(String key, Locale locale) {
        String l = locale == null ? "[default]" : locale.toString();
        logger.log(Level.INFO, "createMessage, key: " + key + " locale: " + l);

        return new Message(key, locale);
    }

}
