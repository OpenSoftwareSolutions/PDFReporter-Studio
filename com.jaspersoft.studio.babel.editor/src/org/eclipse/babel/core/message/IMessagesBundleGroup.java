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
import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.strategy.PropertiesFileGroupStrategy;

/**
 * Interface, implemented by {@link MessagesBundleGroup}. A messages bundle
 * group is an abstraction of a group of resource bundles. <br>
 * <br>
 * 
 * @author Martin Reiterer, Alexej Strelzow
 */
public interface IMessagesBundleGroup {

    /**
     * Returns a collection of all bundles in this group.
     * 
     * @return the bundles in this group
     */
    Collection<IMessagesBundle> getMessagesBundles();

    /**
     * Returns true if the supplied key is already existing in this group.
     * 
     * @param key
     *            The key that shall be tested.
     * @return true <=> The key is already existing.
     */
    boolean containsKey(String key);

    /**
     * Gets all messages associated with the given message key.
     * 
     * @param key
     *            a message key
     * @return messages
     */
    IMessage[] getMessages(String key);

    /**
     * Gets the message matching given key and locale.
     * 
     * @param locale
     *            The locale for which to retrieve the message
     * @param key
     *            The key matching entry to retrieve the message
     * @return a message
     */
    IMessage getMessage(String key, Locale locale);

    /**
     * Gets the messages bundle matching given locale.
     * 
     * @param locale
     *            The locale of bundle to retreive
     * @return a bundle
     */
    IMessagesBundle getMessagesBundle(Locale locale);

    /**
     * Removes messages matching the given key from all messages bundle.
     * 
     * @param key
     *            The key of messages to remove
     */
    void removeMessages(String messageKey);

    /**
     * Is the given key found in this bundle group.
     * 
     * @param key
     *            The key to find
     * @return <code>true</code> if the key exists in this bundle group.
     */
    boolean isKey(String key);

    /**
     * Adds a messages bundle to this group.
     * 
     * @param locale
     *            The locale of the bundle
     * @param messagesBundle
     *            bundle to add
     * @throws MessageException
     *             if a messages bundle for the same locale already exists.
     */
    void addMessagesBundle(Locale locale, IMessagesBundle messagesBundle);

    /**
     * Gets all keys from all messages bundles.
     * 
     * @return all keys from all messages bundles
     */
    String[] getMessageKeys();

    /**
     * Adds an empty message to every messages bundle of this group with the
     * given.
     * 
     * @param key
     *            The message key
     */
    void addMessages(String key);

    /**
     * Gets the number of messages bundles in this group.
     * 
     * @return the number of messages bundles in this group
     */
    int getMessagesBundleCount();

    /**
     * Gets this messages bundle group name. That is the name, which is used for
     * the tab of the MultiPageEditorPart.
     * 
     * @return bundle group name
     */
    String getName();

    /**
     * Gets the unique id of the bundle group. That is usually:
     * <directory>"."<default-filename>. The default filename is without the
     * suffix (e.g. _en, or _en_GB).
     * 
     * @return The unique identifier for the resource bundle group
     */
    String getResourceBundleId();

    /**
     * @return <code>true</code> if the bundle group has
     *         {@link PropertiesFileGroupStrategy} as strategy, else
     *         <code>false</code>. This is the case, when only TapiJI edits the
     *         resource bundles and no have been opened.
     */
    boolean hasPropertiesFileGroupStrategy();

    /**
     * Whether the given key is found in this messages bundle group.
     * 
     * @param key
     *            The key to find
     * @return <code>true</code> if the key exists in this bundle group.
     */
    public boolean isMessageKey(String key);

    /**
     * Gets the name of the project, the resource bundle group is in.
     * 
     * @return The project name
     */
    public String getProjectName();

    /**
     * Removes the {@link IMessagesBundle} from the group.
     * 
     * @param messagesBundle
     *            The bundle to remove.
     */
    public void removeMessagesBundle(IMessagesBundle messagesBundle);

    /**
     * Called before this object will be discarded. Disposes the underlying
     * MessageBundles
     */
    public void dispose();

    /**
     * Removes messages matching the given key from all messages bundle and add
     * it's parent key to bundles.
     * 
     * @param key
     *            The key of messages to remove
     */
    void removeMessagesAddParentKey(String key);

    /**
     * Renames a key in all messages bundles forming this group.
     * 
     * @param sourceKey
     *            the message key to rename
     * @param targetKey
     *            the new message name
     */
    void renameMessageKeys(String sourceKey, String targetKey);
}
