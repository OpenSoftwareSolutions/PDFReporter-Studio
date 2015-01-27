package org.eclipse.babel.core.message.internal;

import java.beans.PropertyChangeEvent;

/**
 * An adapter class for a {@link IMessagesBundleGroupListener}. Methods
 * implementation do nothing.
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class MessagesBundleGroupAdapter implements IMessagesBundleGroupListener {
    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleGroupListener#
     *      keyAdded(java.lang.String)
     */
    public void keyAdded(String key) {
        // do nothing
    }

    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleGroupListener#
     *      keyRemoved(java.lang.String)
     */
    public void keyRemoved(String key) {
        // do nothing
    }

    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleGroupListener#
     *      messagesBundleAdded(org.eclipse.babel.core.message.internal.MessagesBundle)
     */
    public void messagesBundleAdded(MessagesBundle messagesBundle) {
        // do nothing
    }

    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleGroupListener#
     *      messagesBundleChanged(org.eclipse.babel.core.message.internal.MessagesBundle,
     *      java.beans.PropertyChangeEvent)
     */
    public void messagesBundleChanged(MessagesBundle messagesBundle,
            PropertyChangeEvent changeEvent) {
        // do nothing
    }

    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleGroupListener
     *      #messagesBundleRemoved(org.eclipse.babel.core.message.internal.MessagesBundle)
     */
    public void messagesBundleRemoved(MessagesBundle messagesBundle) {
        // do nothing
    }

    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleListener#messageAdded(org.eclipse.babel.core.message.internal.MessagesBundle,
     *      org.eclipse.babel.core.message.internal.Message)
     */
    public void messageAdded(MessagesBundle messagesBundle, Message message) {
        // do nothing
    }

    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleListener#
     *      messageChanged(org.eclipse.babel.core.message.internal.MessagesBundle,
     *      java.beans.PropertyChangeEvent)
     */
    public void messageChanged(MessagesBundle messagesBundle,
            PropertyChangeEvent changeEvent) {
        // do nothing
    }

    /**
     * @see org.eclipse.babel.core.message.internal.IMessagesBundleListener#
     *      messageRemoved(org.eclipse.babel.core.message.internal.MessagesBundle,
     *      org.eclipse.babel.core.message.internal.Message)
     */
    public void messageRemoved(MessagesBundle messagesBundle, Message message) {
        // do nothing
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // do nothing
    }
}
