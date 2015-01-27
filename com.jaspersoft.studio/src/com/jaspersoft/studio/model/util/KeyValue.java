/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.model.util;

public class KeyValue<T, V> {
	public T key;
	public V value;

	public KeyValue(T key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		boolean b = obj instanceof KeyValue;
		if (b) {
			KeyValue<T, V> kobj = (KeyValue<T, V>) obj;
			return key.equals(kobj.key) && value.equals(kobj.value);
		}

		return b;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + key.hashCode();
		hash = hash * 31 + value.hashCode();
		return hash;
	}
}
