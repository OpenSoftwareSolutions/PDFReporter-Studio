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
package com.jaspersoft.studio.preferences.util;

import java.util.Properties;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.SafeRunnable;

import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JRContextPrefStore extends EventManager implements IPreferenceStore {
	private JasperReportsConfiguration jConfig;
	private Properties defaultProperties;
	private boolean dirty = false;

	public JRContextPrefStore(JasperReportsConfiguration jConfig) {
		this.jConfig = jConfig;
		defaultProperties = new Properties();
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		addListenerObject(listener);
	}

	@Override
	public boolean contains(String name) {
		return jConfig.getProperties().containsKey(name) || defaultProperties.containsKey(name);
	}

	@Override
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		final Object[] finalListeners = getListeners();
		// Do we need to fire an event.
		if (finalListeners.length > 0 && (oldValue == null || !oldValue.equals(newValue))) {
			final PropertyChangeEvent pe = new PropertyChangeEvent(this, name, oldValue, newValue);
			for (int i = 0; i < finalListeners.length; ++i) {
				final IPropertyChangeListener l = (IPropertyChangeListener) finalListeners[i];
				SafeRunnable.run(new SafeRunnable(JFaceResources.getString("PreferenceStore.changeError")) { //$NON-NLS-1$
							public void run() {
								l.propertyChange(pe);
							}
						});
			}
		}
	}

	@Override
	public boolean getBoolean(String name) {
		Boolean pboolean = jConfig.getPropertyBoolean(name);
		if (pboolean != null)
			return pboolean;
		return BOOLEAN_DEFAULT_DEFAULT;
	}

	@Override
	public boolean getDefaultBoolean(String name) {
		Object p = defaultProperties.get(name);
		if (p != null)
			if (p instanceof String)
				return Boolean.parseBoolean((String) p);
			else if (p instanceof Boolean)
				return (Boolean) p;
		return BOOLEAN_DEFAULT_DEFAULT;
	}

	@Override
	public double getDefaultDouble(String name) {
		Object p = defaultProperties.get(name);
		if (p != null)
			if (p instanceof String)
				try {
					return Double.parseDouble((String) p);
				} catch (NumberFormatException e) {
					;
				}
			else if (p instanceof Double)
				return (Double) p;
		return DOUBLE_DEFAULT_DEFAULT;
	}

	@Override
	public float getDefaultFloat(String name) {
		Object p = defaultProperties.get(name);
		if (p != null)
			if (p instanceof String)
				try {
					return Float.parseFloat((String) p);
				} catch (NumberFormatException e) {
					;
				}
			else if (p instanceof Float)
				return (Float) p;
		return FLOAT_DEFAULT_DEFAULT;
	}

	@Override
	public int getDefaultInt(String name) {
		Object p = defaultProperties.get(name);
		if (p != null)
			if (p instanceof String)
				try {
					return Integer.parseInt((String) p);
				} catch (NumberFormatException e) {
					;
				}
			else if (p instanceof Integer)
				return (Integer) p;
		return INT_DEFAULT_DEFAULT;
	}

	@Override
	public long getDefaultLong(String name) {
		Object p = defaultProperties.get(name);
		if (p != null)
			if (p instanceof String)
				try {
					return Long.parseLong((String) p);
				} catch (NumberFormatException e) {
					;
				}
			else if (p instanceof Long)
				return (Long) p;
		return LONG_DEFAULT_DEFAULT;
	}

	@Override
	public String getDefaultString(String name) {
		Object p = defaultProperties.get(name);
		if (p != null) {
			if (p instanceof String)
				return (String) p;
			return p.toString();
		}
		return null;
	}

	@Override
	public double getDouble(String name) {
		if (name == null)
			return DOUBLE_DEFAULT_DEFAULT;
		Double val = jConfig.getPropertyDouble(name);
		if (val != null)
			return val;
		return DOUBLE_DEFAULT_DEFAULT;
	}

	@Override
	public float getFloat(String name) {
		if (name == null)
			return FLOAT_DEFAULT_DEFAULT;
		Float val = jConfig.getPropertyFloat(name);
		if (val != null)
			return val;
		return FLOAT_DEFAULT_DEFAULT;
	}

	@Override
	public int getInt(String name) {
		if (name == null)
			return INT_DEFAULT_DEFAULT;
		Integer val = jConfig.getPropertyInteger(name);
		if (val != null)
			return val;
		return INT_DEFAULT_DEFAULT;
	}

	@Override
	public long getLong(String name) {
		if (name == null)
			return LONG_DEFAULT_DEFAULT;
		Long val = jConfig.getPropertyLong(name);
		if (val != null)
			return val;
		return LONG_DEFAULT_DEFAULT;
	}

	@Override
	public String getString(String name) {
		if (name == null)
			return null;
		return Misc.nvl(jConfig.getProperty(name));
	}

	@Override
	public boolean isDefault(String name) {
		return (!jConfig.getProperties().containsKey(name) && defaultProperties.containsKey(name));
	}

	@Override
	public boolean needsSaving() {
		return dirty;
	}

	@Override
	public void putValue(String name, String value) {
		String oldValue = getString(name);
		if (oldValue == null || !oldValue.equals(value)) {
			setValue(name, value);
			dirty = true;
		}
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		removeListenerObject(listener);
	}

	@Override
	public void setDefault(String name, double value) {
		Assert.isTrue(name != null);
		defaultProperties.put(name, Double.toString(value));
	}

	@Override
	public void setDefault(String name, float value) {
		Assert.isTrue(name != null);
		defaultProperties.put(name, Float.toString(value));
	}

	@Override
	public void setDefault(String name, int value) {
		Assert.isTrue(name != null);
		defaultProperties.put(name, Integer.toString(value));
	}

	@Override
	public void setDefault(String name, long value) {
		Assert.isTrue(name != null);
		defaultProperties.put(name, Long.toString(value));
	}

	@Override
	public void setDefault(String name, String defaultObject) {
		Assert.isTrue(name != null);
		if (defaultObject != null)
			defaultProperties.put(name, defaultObject.toString());
	}

	@Override
	public void setDefault(String name, boolean value) {
		Assert.isTrue(name != null);
		defaultProperties.put(name, Boolean.toString(value));
	}

	@Override
	public void setToDefault(String name) {
		if (!jConfig.getProperties().containsKey(name))
			return;
		Object oldValue = jConfig.getProperties().get(name);
		jConfig.getProperties().remove(name);
		dirty = true;
		Object newValue = null;
		if (defaultProperties != null) {
			newValue = defaultProperties.get(name);
		}
		firePropertyChangeEvent(name, oldValue, newValue);
	}

	@Override
	public void setValue(String name, double value) {
		double oldValue = getDouble(name);
		if (oldValue != value) {
			jConfig.setProperty(name, Double.toString(value));
			dirty = true;
			firePropertyChangeEvent(name, new Double(oldValue), new Double(value));
		}
	}

	@Override
	public void setValue(String name, float value) {
		float oldValue = getFloat(name);
		if (oldValue != value) {
			jConfig.setProperty(name, Float.toString(value));
			dirty = true;
			firePropertyChangeEvent(name, new Float(oldValue), new Float(value));
		}
	}

	@Override
	public void setValue(String name, int value) {
		int oldValue = getInt(name);
		if (oldValue != value) {
			jConfig.setProperty(name, Integer.toString(value));
			dirty = true;
			firePropertyChangeEvent(name, new Integer(oldValue), new Integer(value));
		}
	}

	@Override
	public void setValue(String name, long value) {
		long oldValue = getLong(name);
		if (oldValue != value) {
			jConfig.setProperty(name, Long.toString(value));
			dirty = true;
			firePropertyChangeEvent(name, new Long(oldValue), new Long(value));
		}
	}

	@Override
	public void setValue(String name, String value) {
		String oldValue = getString(name);
		if (oldValue != value) {
			jConfig.setProperty(name, value);
			dirty = true;
			firePropertyChangeEvent(name, oldValue, value);
		}
	}

	@Override
	public void setValue(String name, boolean value) {
		boolean oldValue = getBoolean(name);
		if (oldValue != value) {
			jConfig.setProperty(name, Boolean.toString(value));
			dirty = true;
			firePropertyChangeEvent(name, new Boolean(oldValue), new Boolean(value));
		}
	}

}
