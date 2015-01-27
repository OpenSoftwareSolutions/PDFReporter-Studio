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

import java.util.Locale;

import org.eclipse.jdt.core.dom.Message;

/**
 * Interface, implemented by {@link Message}. A message is an abstraction of a
 * key-value pair, which can be found in resource bundles (1 row). <br>
 * <br>
 * 
 * @author Martin Reiterer, Alexej Strelzow
 */
public interface IMessage {

    /**
     * Gets the message key attribute.
     * 
     * @return Returns the key.
     */
    String getKey();

    /**
     * Gets the message text.
     * 
     * @return Returns the text.
     */
    String getValue();

    /**
     * Gets the message locale.
     * 
     * @return Returns the locale
     */
    Locale getLocale();

    /**
     * Gets the comment associated with this message (<code>null</code> if no
     * comments).
     * 
     * @return Returns the comment.
     */
    String getComment();

    /**
     * Gets whether this message is active or not.
     * 
     * @return <code>true</code> if this message is active.
     */
    boolean isActive();

    /**
     * @return The toString representation
     */
    String toString();

    /**
     * Sets whether the message is active or not. An inactive message is one
     * that we continue to keep track of, but will not be picked up by
     * internationalization mechanism (e.g. <code>ResourceBundle</code>).
     * Typically, those are commented (i.e. //) key/text pairs in a *.properties
     * file.
     * 
     * @param active
     *            The active to set.
     */
    void setActive(boolean active);

    /**
     * Sets the message comment.
     * 
     * @param comment
     *            The comment to set.
     */
    void setComment(String comment);

    /**
     * Sets the actual message text.
     * 
     * @param text
     *            The text to set.
     */
    void setText(String test);
    
    public IMessage clone();

}
