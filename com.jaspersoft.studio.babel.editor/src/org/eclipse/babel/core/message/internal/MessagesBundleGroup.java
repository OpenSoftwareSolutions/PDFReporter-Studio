/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - TapJI integration
 *    Matthias Lettmayer - added removeMessagesAddParentKey() (fixed issue 41)
 ******************************************************************************/
package org.eclipse.babel.core.message.internal;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.babel.core.message.resource.IMessagesResource;
import org.eclipse.babel.core.message.strategy.IMessagesBundleGroupStrategy;
import org.eclipse.babel.core.message.strategy.PropertiesFileGroupStrategy;
import org.eclipse.babel.core.util.BabelUtils;

/**
 * Grouping of all messages bundle of the same kind.
 *
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class MessagesBundleGroup extends AbstractMessageModel implements
        IMessagesBundleGroup {

    private String projectName;

    private static final IMessagesBundleGroupListener[] EMPTY_GROUP_LISTENERS = new IMessagesBundleGroupListener[] {};
    private static final Message[] EMPTY_MESSAGES = new Message[] {};

    public static final String PROPERTY_MESSAGES_BUNDLE_COUNT = "messagesBundleCount"; //$NON-NLS-1$
    public static final String PROPERTY_KEY_COUNT = "keyCount"; //$NON-NLS-1$

    /** For serialization. */
    private static final long serialVersionUID = -1977849534191384324L;
    /** Bundles forming the group (key=Locale; value=MessagesBundle). */
    private final Map<Locale, IMessagesBundle> localeBundles = new HashMap<Locale, IMessagesBundle>();
    private final Set<String> keys = new TreeSet<String>();
    private final IMessagesBundleListener messagesBundleListener = new MessagesBundleListener();

    private final IMessagesBundleGroupStrategy groupStrategy;
    private static final Locale[] EMPTY_LOCALES = new Locale[] {};
    private final String name;
    private final String resourceBundleId;

    /**
     * Creates a new messages bundle group.
     *
     * @param groupStrategy
     *            a IMessagesBundleGroupStrategy instance
     */
    public MessagesBundleGroup(IMessagesBundleGroupStrategy groupStrategy) {
        super();
        this.groupStrategy = groupStrategy;
        this.name = groupStrategy.createMessagesBundleGroupName();
        this.resourceBundleId = groupStrategy.createMessagesBundleId();

        this.projectName = groupStrategy.getProjectName();

        MessagesBundle[] bundles = groupStrategy.loadMessagesBundles();
        if (bundles != null) {
            for (int i = 0; i < bundles.length; i++) {
                addMessagesBundle(bundles[i]);
            }
        }

        if (this.projectName != null) {
            RBManager.getInstance(this.projectName)
                    .notifyMessagesBundleGroupCreated(this);
        }
    }

    /**
     * Called before this object will be discarded. Disposes the underlying
     * MessageBundles
     */
    @Override
    public void dispose() {
        for (IMessagesBundle mb : getMessagesBundles()) {
            try {
                mb.dispose();
            } catch (Throwable t) {
                // FIXME: remove debug:
                System.err.println("Error disposing message-bundle "
                        + ((MessagesBundle) mb).getResource()
                                .getResourceLocationLabel());
                // disregard crashes: this is a best effort to dispose things.
            }
        }

        RBManager.getInstance(this.projectName)
                .notifyMessagesBundleGroupDeleted(this);
    }

    /**
     * Gets the messages bundle matching given locale.
     *
     * @param locale
     *            locale of bundle to retreive
     * @return a bundle
     */
    @Override
    public IMessagesBundle getMessagesBundle(Locale locale) {
        return localeBundles.get(locale);
    }

    /**
     * Gets the messages bundle matching given source object. A source object
     * being a context-specific concrete underlying implementation of a
     * <code>MessagesBundle</code> as per defined in
     * <code>IMessageResource</code>.
     *
     * @param source
     *            the source object to match
     * @return a messages bundle
     * @see IMessagesResource
     */
    public MessagesBundle getMessagesBundle(Object source) {
        for (IMessagesBundle messagesBundle : getMessagesBundles()) {
            if (equals(source, ((MessagesBundle) messagesBundle).getResource()
                    .getSource())) {
                return (MessagesBundle) messagesBundle;
            }
        }
        return null;
    }

    /**
     * Adds an empty <code>MessagesBundle</code> to this group for the given
     * locale.
     *
     * @param locale
     *            locale for the new bundle added
     */
    public void addMessagesBundle(Locale locale) {
        addMessagesBundle(groupStrategy.createMessagesBundle(locale));
    }
    
    public IMessagesBundle copyMessagesBundle(Locale locale, IMessagesBundle source) {
    	IMessagesBundle destination = groupStrategy.createMessagesBundle(locale);
    	if (source != null){
	    	for(String key : source.getKeys()){
	    		IMessage message = source.getMessage(key);
	    		destination.addMessage(message != null ? message.clone() : null);
	    	}
    	}
        addMessagesBundle(destination);
        return destination;
    }

    /**
     * Gets all locales making up this messages bundle group.
     */
    public Locale[] getLocales() {
        return localeBundles.keySet().toArray(EMPTY_LOCALES);
    }

    /**
     * Gets all messages associated with the given message key.
     *
     * @param key
     *            a message key
     * @return messages
     */
    @Override
    public IMessage[] getMessages(String key) {
        List<IMessage> messages = new ArrayList<IMessage>();
        for (IMessagesBundle messagesBundle : getMessagesBundles()) {
            IMessage message = messagesBundle.getMessage(key);
            if (message != null) {
                messages.add(message);
            }
        }
        return messages.toArray(EMPTY_MESSAGES);
    }

    /**
     * Gets the message matching given key and locale.
     *
     * @param locale
     *            locale for which to retrieve the message
     * @param key
     *            key matching entry to retrieve the message
     * @return a message
     */
    @Override
    public IMessage getMessage(String key, Locale locale) {
        IMessagesBundle messagesBundle = getMessagesBundle(locale);
        if (messagesBundle != null) {
            return messagesBundle.getMessage(key);
        }
        return null;
    }

    /**
     * Adds a messages bundle to this group.
     *
     * @param messagesBundle
     *            bundle to add
     * @throws MessageException
     *             if a messages bundle for the same locale already exists.
     */
    public void addMessagesBundle(IMessagesBundle messagesBundle) {
        addMessagesBundle(messagesBundle.getLocale(), messagesBundle);
    }

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
    @Override
    public void addMessagesBundle(Locale locale, IMessagesBundle messagesBundle) {
        MessagesBundle mb = (MessagesBundle) messagesBundle;
        if (localeBundles.get(mb.getLocale()) != null) {
            throw new MessageException(
                    "A bundle with the same locale already exists."); //$NON-NLS-1$
        }

        int oldBundleCount = localeBundles.size();
        localeBundles.put(mb.getLocale(), mb);

        firePropertyChange(PROPERTY_MESSAGES_BUNDLE_COUNT, oldBundleCount,
                localeBundles.size());
        fireMessagesBundleAdded(mb);

        String[] bundleKeys = mb.getKeys();
        for (int i = 0; i < bundleKeys.length; i++) {
            String key = bundleKeys[i];
            if (!keys.contains(key)) {
                int oldKeyCount = keys.size();
                keys.add(key);
                firePropertyChange(PROPERTY_KEY_COUNT, oldKeyCount, keys.size());
                fireKeyAdded(key);
            }
        }
        mb.addMessagesBundleListener(messagesBundleListener);

    }

    /**
     * Removes the {@link IMessagesBundle} from the group.
     *
     * @param messagesBundle
     *            The bundle to remove.
     */
    @Override
    public void removeMessagesBundle(IMessagesBundle messagesBundle) {
        Locale locale = messagesBundle.getLocale();

        if (localeBundles.containsKey(locale)) {
            localeBundles.remove(locale);
        }

        // which keys should I not remove?
        Set<String> keysNotToRemove = new TreeSet<String>();

        for (String key : messagesBundle.getKeys()) {
            for (IMessagesBundle bundle : localeBundles.values()) {
                if (bundle.getMessage(key) != null) {
                    keysNotToRemove.add(key);
                }
            }
        }

        // remove keys
        for (String keyToRemove : messagesBundle.getKeys()) {
            if (!keysNotToRemove.contains(keyToRemove)) { // we can remove
                keys.remove(keyToRemove);
            }
        }
    }

    public void removeMessagesBundle(Locale locale) {
        removeMessagesBundle(getMessagesBundle(locale));
    }

    /**
     * Gets this messages bundle group name. That is the name, which is used for
     * the tab of the MultiPageEditorPart
     *
     * @return bundle group name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Adds an empty message to every messages bundle of this group with the
     * given.
     *
     * @param key
     *            message key
     */
    @Override
    public void addMessages(String key) {
        for (IMessagesBundle msgBundle : localeBundles.values()) {
            ((MessagesBundle) msgBundle).addMessage(key);
        }
    }

    /**
     * Renames a key in all messages bundles forming this group.
     *
     * @param sourceKey
     *            the message key to rename
     * @param targetKey
     *            the new message name
     */
    @Override
    public void renameMessageKeys(String sourceKey, String targetKey) {
        for (IMessagesBundle msgBundle : localeBundles.values()) {
            msgBundle.renameMessageKey(sourceKey, targetKey);
        }
    }

    /**
     * Removes messages matching the given key from all messages bundle.
     *
     * @param key
     *            key of messages to remove
     */
    @Override
    public void removeMessages(String key) {
        for (IMessagesBundle msgBundle : localeBundles.values()) {
            msgBundle.removeMessage(key);
        }
    }

    /**
     * Removes messages matching the given key from all messages bundle and add
     * it's parent key to bundles.
     *
     * @param key
     *            key of messages to remove
     */
    @Override
    public void removeMessagesAddParentKey(String key) {
        for (IMessagesBundle msgBundle : localeBundles.values()) {
            msgBundle.removeMessageAddParentKey(key);
        }
    }

    /**
     * Sets whether messages matching the <code>key</code> are active or not.
     *
     * @param key
     *            key of messages
     */
    public void setMessagesActive(String key, boolean active) {
        for (IMessagesBundle msgBundle : localeBundles.values()) {
            IMessage entry = msgBundle.getMessage(key);
            if (entry != null) {
                entry.setActive(active);
            }
        }
    }

    /**
     * Duplicates each messages matching the <code>sourceKey</code> to the
     * <code>newKey</code>.
     *
     * @param sourceKey
     *            original key
     * @param targetKey
     *            new key
     * @throws MessageException
     *             if a target key already exists
     */
    public void duplicateMessages(String sourceKey, String targetKey) {
        if (sourceKey.equals(targetKey)) {
            return;
        }
        for (IMessagesBundle msgBundle : localeBundles.values()) {
            msgBundle.duplicateMessage(sourceKey, targetKey);
        }
    }

    /**
     * Returns a collection of all bundles in this group.
     *
     * @return the bundles in this group
     */
    @Override
    public Collection<IMessagesBundle> getMessagesBundles() {
        return localeBundles.values();
    }

    /**
     * Gets all keys from all messages bundles.
     *
     * @return all keys from all messages bundles
     */
    @Override
    public String[] getMessageKeys() {
        return keys.toArray(BabelUtils.EMPTY_STRINGS);
    }

    /**
     * Whether the given key is found in this messages bundle group.
     *
     * @param key
     *            the key to find
     * @return <code>true</code> if the key exists in this bundle group.
     */
    @Override
    public boolean isMessageKey(String key) {
        return keys.contains(key);
    }

    /**
     * Gets the number of messages bundles in this group.
     *
     * @return the number of messages bundles in this group
     */
    @Override
    public int getMessagesBundleCount() {
        return localeBundles.size();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MessagesBundleGroup)) {
            return false;
        }
        MessagesBundleGroup messagesBundleGroup = (MessagesBundleGroup) obj;
        return equals(localeBundles, messagesBundleGroup.localeBundles);
    }

    public final synchronized void addMessagesBundleGroupListener(
            final IMessagesBundleGroupListener listener) {
        addPropertyChangeListener(listener);
    }

    public final synchronized void removeMessagesBundleGroupListener(
            final IMessagesBundleGroupListener listener) {
        removePropertyChangeListener(listener);
    }

    public final synchronized IMessagesBundleGroupListener[] getMessagesBundleGroupListeners() {
        // TODO find more efficient way to avoid class cast.
        return Arrays.asList(getPropertyChangeListeners()).toArray(
                EMPTY_GROUP_LISTENERS);
    }

    private void fireKeyAdded(String key) {
        IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
        for (int i = 0; i < listeners.length; i++) {
            IMessagesBundleGroupListener listener = listeners[i];
            listener.keyAdded(key);
        }
    }

    private void fireKeyRemoved(String key) {
        IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
        for (int i = 0; i < listeners.length; i++) {
            IMessagesBundleGroupListener listener = listeners[i];
            listener.keyRemoved(key);
        }
    }

    private void fireMessagesBundleAdded(MessagesBundle messagesBundle) {
        IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
        for (int i = 0; i < listeners.length; i++) {
            IMessagesBundleGroupListener listener = listeners[i];
            listener.messagesBundleAdded(messagesBundle);
        }
    }

    private void fireMessagesBundleRemoved(MessagesBundle messagesBundle) {
        IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
        for (int i = 0; i < listeners.length; i++) {
            IMessagesBundleGroupListener listener = listeners[i];
            listener.messagesBundleRemoved(messagesBundle);
        }
    }

    /**
     * Returns true if the supplied key is already existing in this group.
     *
     * @param key
     *            The key that shall be tested.
     *
     * @return true <=> The key is already existing.
     */
    @Override
    public boolean containsKey(String key) {
        for (Locale locale : localeBundles.keySet()) {
            IMessagesBundle messagesBundle = localeBundles.get(locale);
            for (String k : messagesBundle.getKeys()) {
                if (k.equals(key)) {
                    return true;
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    /**
     * Is the given key found in this bundle group.
     *
     * @param key
     *            the key to find
     * @return <code>true</code> if the key exists in this bundle group.
     */
    @Override
    public boolean isKey(String key) {
        return keys.contains(key);
    }

    /**
     * Gets the unique id of the bundle group. That is usually:
     * <directory>"."<default-filename>. The default filename is without the
     * suffix (e.g. _en, or _en_GB).
     *
     * @return The unique identifier for the resource bundle group
     */
    @Override
    public String getResourceBundleId() {
        return resourceBundleId;
    }

    /**
     * Gets the name of the project, the resource bundle group is in.
     *
     * @return The project name
     */
    @Override
    public String getProjectName() {
        return this.projectName;
    }

    /**
     * Class listening for changes in underlying messages bundle and relays them
     * to the listeners for MessagesBundleGroup.
     */
    private class MessagesBundleListener implements IMessagesBundleListener {
        @Override
        public void messageAdded(MessagesBundle messagesBundle, Message message) {
            int oldCount = keys.size();
            IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
            for (int i = 0; i < listeners.length; i++) {
                IMessagesBundleGroupListener listener = listeners[i];
                listener.messageAdded(messagesBundle, message);
                if (getMessages(message.getKey()).length == 1) {
                    keys.add(message.getKey());
                    firePropertyChange(PROPERTY_KEY_COUNT, oldCount,
                            keys.size());
                    fireKeyAdded(message.getKey());
                }
            }
        }

        @Override
        public void messageRemoved(MessagesBundle messagesBundle,
                Message message) {
            int oldCount = keys.size();
            IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
            for (int i = 0; i < listeners.length; i++) {
                IMessagesBundleGroupListener listener = listeners[i];
                listener.messageRemoved(messagesBundle, message);
                int keyMessagesCount = getMessages(message.getKey()).length;
                if (keyMessagesCount == 0 && keys.contains(message.getKey())) {
                    keys.remove(message.getKey());
                    firePropertyChange(PROPERTY_KEY_COUNT, oldCount,
                            keys.size());
                    fireKeyRemoved(message.getKey());
                }
            }
        }

        @Override
        public void messageChanged(MessagesBundle messagesBundle,
                PropertyChangeEvent changeEvent) {
            IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
            for (int i = 0; i < listeners.length; i++) {
                IMessagesBundleGroupListener listener = listeners[i];
                listener.messageChanged(messagesBundle, changeEvent);
            }
        }

        // MessagesBundle property changes:
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            MessagesBundle bundle = (MessagesBundle) evt.getSource();
            IMessagesBundleGroupListener[] listeners = getMessagesBundleGroupListeners();
            for (int i = 0; i < listeners.length; i++) {
                IMessagesBundleGroupListener listener = listeners[i];
                listener.messagesBundleChanged(bundle, evt);
            }
        }
    }

    /**
     * @return <code>true</code> if the bundle group has
     *         {@link PropertiesFileGroupStrategy} as strategy, else
     *         <code>false</code>. This is the case, when only TapiJI edits the
     *         resource bundles and no have been opened.
     */
    @Override
    public boolean hasPropertiesFileGroupStrategy() {
        return groupStrategy instanceof PropertiesFileGroupStrategy;
    }
}
