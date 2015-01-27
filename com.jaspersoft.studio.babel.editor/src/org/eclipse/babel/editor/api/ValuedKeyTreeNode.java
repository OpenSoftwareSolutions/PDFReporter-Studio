/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Reiterer - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;

public class ValuedKeyTreeNode extends KeyTreeNode implements
        IValuedKeyTreeNode {

    public ValuedKeyTreeNode(IKeyTreeNode parent, String name,
            String messageKey, IMessagesBundleGroup messagesBundleGroup) {
        super(parent, name, messageKey, messagesBundleGroup);
    }

    private Map<Locale, String> values = new HashMap<Locale, String>();
    private Object info;

    public void initValues(Map<Locale, String> values) {
        this.values = values;
    }

    public void addValue(Locale locale, String value) {
        values.put(locale, value);
    }

    public String getValue(Locale locale) {
        return values.get(locale);
    }

    public void setValue(Locale locale, String newValue) {
        if (values.containsKey(locale))
            values.remove(locale);
        addValue(locale, newValue);
    }

    public Collection<String> getValues() {
        return values.values();
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public Object getInfo() {
        return info;
    }

    public Collection<Locale> getLocales() {
        List<Locale> locs = new ArrayList<Locale>();
        for (Locale loc : values.keySet()) {
            locs.add(loc);
        }
        return locs;
    }

}
