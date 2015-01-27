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
import org.eclipse.babel.core.message.checks.proximity.IProximityAnalyzer;
import org.eclipse.babel.core.util.BabelUtils;

/**
 * Checks if key as a duplicate value.
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class SimilarValueCheck implements IMessageCheck {

    private String[] similarKeys;
    private IProximityAnalyzer analyzer;

    /**
     * Constructor.
     */
    public SimilarValueCheck(IProximityAnalyzer analyzer) {
        super();
        this.analyzer = analyzer;
    }

    /**
     * @see org.eclipse.babel.core.message.internal.checks.IMessageCheck#checkKey(org.eclipse.babel.core.message.internal.MessagesBundleGroup,
     *      org.eclipse.babel.core.message.internal.Message)
     */
    public boolean checkKey(IMessagesBundleGroup messagesBundleGroup,
            IMessage message) {
        Collection<String> keys = new ArrayList<String>();
        if (message != null) {
            // TODO have case as preference
            String value1 = message.getValue().toLowerCase();
            IMessagesBundle messagesBundle = messagesBundleGroup
                    .getMessagesBundle(message.getLocale());
            for (IMessage similarEntry : messagesBundle.getMessages()) {
                if (!message.getKey().equals(similarEntry.getKey())) {
                    String value2 = similarEntry.getValue().toLowerCase();
                    // TODO have preference to report identical as similar
                    if (!BabelUtils.equals(value1, value2)
                            && analyzer.analyse(value1, value2) >= 0.75) {
                        // TODO use preferences
                        // >= RBEPreferences.getReportSimilarValuesPrecision())
                        // {
                        keys.add(similarEntry.getKey());
                    }
                }
            }
            if (!keys.isEmpty()) {
                keys.add(message.getKey());
            }
        }
        similarKeys = keys.toArray(new String[] {});
        return !keys.isEmpty();
    }

    /**
     * Gets similar keys.
     * 
     * @return similar keys
     */
    public String[] getSimilarMessageKeys() {
        return similarKeys;
    }
}
