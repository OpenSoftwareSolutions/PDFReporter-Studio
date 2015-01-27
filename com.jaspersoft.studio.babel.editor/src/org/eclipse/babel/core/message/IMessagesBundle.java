/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer, Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Reiterer - initial API
 *     Alexej Strelzow - extended API
 ******************************************************************************/
package org.eclipse.babel.core.message;

import java.util.Collection;
import java.util.Locale;

import org.eclipse.babel.core.message.internal.MessageException;
import org.eclipse.babel.core.message.internal.MessagesBundle;
import org.eclipse.babel.core.message.resource.IMessagesResource;

/**
 * Interface, implemented by {@link MessagesBundle}. A messages bundle is an
 * abstraction of a resource bundle (the *.properties-file). <br>
 * <br>
 * 
 * @author Martin Reiterer, Alexej Strelzow
 */
public interface IMessagesBundle {

    /**
     * Called before this object will be discarded.
     */
    void dispose();

    /**
     * Renames a message key.
     * 
     * @param sourceKey
     *            the message key to rename
     * @param targetKey
     *            the new key for the message
     * @throws MessageException
     *             if the target key already exists
     */
    void renameMessageKey(String sourceKey, String targetKey);

    /**
     * Removes a message from this messages bundle.
     * 
     * @param messageKey
     *            the key of the message to remove
     */
    void removeMessage(String messageKey);

    /**
     * Duplicates a message.
     * 
     * @param sourceKey
     *            the message key to duplicate
     * @param targetKey
     *            the new message key
     * @throws MessageException
     *             if the target key already exists
     */
    void duplicateMessage(String sourceKey, String targetKey);

    /**
     * Gets the locale for the messages bundle (<code>null</code> assumes the
     * default system locale).
     * 
     * @return Returns the locale.
     */
    Locale getLocale();

    /**
     * Gets all message keys making up this messages bundle.
     * 
     * @return message keys
     */
    String[] getKeys();

    /**
     * Returns the value to the given key, if the key exists.
     * 
     * @param key
     *            , the key of a message.
     * @return The value to the given key.
     */
    String getValue(String key);

    /**
     * Obtains the set of <code>Message</code> objects in this bundle.
     * 
     * @return a collection of <code>Message</code> objects in this bundle
     */
    Collection<IMessage> getMessages();

    /**
     * Gets a message.
     * 
     * @param key
     *            a message key
     * @return a message
     */
    IMessage getMessage(String key);

    /**
     * Adds an empty message.
     * 
     * @param key
     *            the new message key
     */
    void addMessage(IMessage message);

    /**
     * Removes messages from this messages bundle.
     * 
     * @param messageKeys
     *            the keys of the messages to remove
     */
    void removeMessages(String[] messageKeys);

    /**
     * Sets the comment for this messages bundle.
     * 
     * @param comment
     *            The comment to set.
     */
    void setComment(String comment);

    /**
     * Gets the overall comment, or description, for this messages bundle..
     * 
     * @return Returns the comment.
     */
    String getComment();

    /**
     * Gets the underlying messages resource implementation.
     * 
     * @return
     */
    IMessagesResource getResource();

    /**
     * Removes a message from this messages bundle and adds it's parent key to
     * bundle. E.g.: key = a.b.c gets deleted, a.b gets added with a default
     * message
     * 
     * @param messageKey
     *            the key of the message to remove
     */
    void removeMessageAddParentKey(String key);
}
