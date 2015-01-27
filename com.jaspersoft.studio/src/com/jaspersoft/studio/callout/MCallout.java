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
package com.jaspersoft.studio.callout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.callout.pin.MPin;
import com.jaspersoft.studio.callout.pin.MPinConnection;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Misc;

/**
 * 
 * @author sanda zaharia
 * 
 */
public class MCallout extends APropertyNode implements IGraphicElement {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final int DEFAULT_HEIGHT = 200;
	public static final int DEFAULT_WIDTH = 200;
	public static final String PROP_CALLOUT = "ireport.callouts";

	public MCallout() {
		super();
	}

	public MCallout(ANode parent, Properties properties, int icallout) {
		super(parent, -1);
		this.properties = properties;
		parseCallout(icallout);

		setValue(getJasperDesign());
	}

	public MCallout(ANode parent) {
		super(parent, -1);
		setValue(getJasperDesign());
	}

	public void deleteCallout() {
		ANode parent = getParent();
		if (parent != null) {
			properties = getProperities(parent);
			properties.remove("callouts." + i + ".bounds");
			properties.remove("callouts." + i + ".pins");
			properties.remove("callouts." + i + ".text");
			properties.remove("callouts." + i + ".pins");
			properties.remove("callouts." + i + ".bg");
			properties.remove("callouts." + i + ".fg");

			String calloutInfoStr = FileUtils.getPropertyAsString(properties);
			if(calloutInfoStr==null || calloutInfoStr.isEmpty()){
				getPropertiesHolder(parent).getPropertiesMap().removeProperty(PROP_CALLOUT);
			}
			else{
				getPropertiesHolder(parent).getPropertiesMap().setProperty(PROP_CALLOUT,
						calloutInfoStr);	
			}
			
			removeChild(this);

			getPropertyChangeSupport().fireIndexedPropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, -1, true, false);
		}
	}

	public static MCallout createCallout(ANode parent, Rectangle location) {
		Properties properties = getProperities(parent);
		Set<Object> keySet = properties.keySet();
		int max = 0;
		for (Object key : keySet) {
			String k = (String) key;
			if (k.matches("callouts\\.[0-9]\\.bounds")) {
				int tmp = Integer.parseInt(k.replaceAll("callouts\\.", "").replaceAll("\\.bounds", ""));
				max = Math.max(tmp, max);
			}
		}
		MCallout mCallout = new MCallout(parent, properties, max + 1);
		mCallout.setPropertyValue(JRDesignElement.PROPERTY_X, location.x);
		mCallout.setPropertyValue(JRDesignElement.PROPERTY_Y, location.y);
		mCallout.setPropertyValue(JRDesignElement.PROPERTY_WIDTH, location.width);
		mCallout.setPropertyValue(JRDesignElement.PROPERTY_HEIGHT, location.height);

		parent.getPropertyChangeSupport()
				.fireIndexedPropertyChange(JRDesignElementGroup.PROPERTY_CHILDREN, -1, true, false);
		return mCallout;
	}

	public static Properties getProperities(ANode parent) {
		try {
			// JasperDesign jd = parent.getJasperDesign();
			// String pcallout = jd.getProperty(MCallout.PROP_CALLOUT);
			JRPropertiesHolder pholder = getPropertiesHolder(parent);
			String pcallout = pholder.getPropertiesMap().getProperty(MCallout.PROP_CALLOUT);
			if (pcallout != null) {
				pcallout = pcallout.replaceAll("callouts.", "\ncallouts.");
				return FileUtils.load(pcallout);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Properties();
	}

	private static JRPropertiesHolder getPropertiesHolder(ANode parent) {
		JRPropertiesHolder pholder = null;
		if (parent instanceof IContainerLayout) {
			JRPropertiesHolder[] pholders = ((IContainerLayout) parent).getPropertyHolder();
			if (pholders != null)
				pholder = pholders[pholders.length - 1];
		}
		if (pholder == null)
			pholder = parent.getJasperDesign();
		return pholder;
	}

	public static void createCallouts(ANode parent) {
		Properties properties = getProperities(parent);
		if (properties != null) {
			Set<Object> keySet = properties.keySet();
			Set<Integer> indexset = new HashSet<Integer>();
			for (Object key : keySet) {
				String k = (String) key;
				if (k.matches("callouts\\.[0-9]\\.bounds"))
					indexset.add(Integer.parseInt(k.replaceAll("callouts\\.", "").replaceAll("\\.bounds", "")));
			}
			for (Integer ind : indexset)
				new MCallout(parent, properties, ind);
		}
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("note"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getImagePath()
	 */
	@Override
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

	@Override
	public Map<String, Object> getDefaultsMap() {
		return defaultsMap;
	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		NTextPropertyDescriptor textD = new NTextPropertyDescriptor(PROP_TEXT, "Text", SWT.MULTI);
		desc.add(textD);

		ColorPropertyDescriptor backcolorD = new ColorPropertyDescriptor(PROP_BACKGROUND, Messages.common_backcolor,
				NullEnum.NOTNULL,false);
		backcolorD.setDescription("Background color of the callout");
		desc.add(backcolorD);

		ColorPropertyDescriptor foreColorD = new ColorPropertyDescriptor(PROP_FOREGROUND, Messages.common_forecolor,
				NullEnum.NOTNULL,false);
		foreColorD.setDescription("Foreground color of the callout");
		desc.add(foreColorD);

		defaultsMap.put(PROP_BACKGROUND, ColorConstants.yellow);
		defaultsMap.put(PROP_FOREGROUND, ColorConstants.black);
	}

	public void parseCallout(int icallout) {
		if (properties == null)
			properties = new Properties();
		i = icallout;
		String b = properties.getProperty("callouts." + i + ".bounds");
		if (b != null) {
			String[] coords = b.split(",");
			x = getInt(coords[0]);
			y = getInt(coords[1]);
			w = getInt(coords[2]);
			h = getInt(coords[3]);
		}
		b = properties.getProperty("callouts." + i + ".fg");
		if (b != null)
			fg = getColor(b, fg);
		b = properties.getProperty("callouts." + i + ".bg");
		if (b != null)
			bg = getColor(b, bg);

		removeChildren();
		String pins = properties.getProperty("callouts." + i + ".pins");
		if (pins != null) {
			String[] ps = pins.trim().split(";");
			for (String pin : ps) {
				String[] p = pin.split(",");

				MPin mpin = new MPin(this, new Point(getInt(p[0]), getInt(p[1])));
				new MPinConnection(this, mpin);
			}
		}
		text = Misc.nvl(properties.getProperty("callouts." + i + ".text"), text);
	}

	private List<MPinConnection> pinConnections = new ArrayList<MPinConnection>();

	public void addPinConnection(MPinConnection conn) {
		pinConnections.add(conn);
	}

	public void removePinConnection(MPinConnection conn) {
		pinConnections.remove(conn);
	}

	public List<MPinConnection> getTargetConnections() {
		return pinConnections;
	}

	private Color getColor(String str, Color def) {
		try {
			return SWTResourceManager.getColor(StringConverter.asRGB(str));
		} catch (Exception e) {
			e.printStackTrace();
			return def;
		}
	}

	private int getInt(String str) {
		try {
			return Integer.parseInt(str.trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public final static String PROP_TEXT = "TEXT";
	public final static String PROP_BACKGROUND = "BACKGROUND";
	public final static String PROP_FOREGROUND = "FOREGROUND";

	private int x, y, w, h, i = 100;
	private Color fg = ColorConstants.black;
	private Color bg = ColorConstants.yellow;

	private String text = System.getProperty("user.name") + " " + (new SimpleDateFormat()).format(new Date());
	private Properties properties;

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(JRDesignElement.PROPERTY_X))
			return x;
		if (id.equals(JRDesignElement.PROPERTY_Y))
			return y;
		if (id.equals(JRDesignElement.PROPERTY_WIDTH))
			return w;
		if (id.equals(JRDesignElement.PROPERTY_HEIGHT))
			return h;
		if (id.equals(PROP_TEXT))
			return text;
		if (id.equals(PROP_FOREGROUND))
			return AlfaRGB.getFullyOpaque(fg.getRGB());
		if (id.equals(PROP_BACKGROUND))
			return AlfaRGB.getFullyOpaque(bg.getRGB());

		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		Object oldValue = getPropertyValue(id);
		if (id.equals(JRDesignElement.PROPERTY_X))
			x = (Integer) value;
		else if (id.equals(JRDesignElement.PROPERTY_Y))
			y = (Integer) value;
		else if (id.equals(JRDesignElement.PROPERTY_WIDTH))
			w = (Integer) value;
		else if (id.equals(JRDesignElement.PROPERTY_HEIGHT))
			h = (Integer) value;
		else if (id.equals(PROP_TEXT))
			text = Misc.nvl(value, "");
		else if (id.equals(PROP_FOREGROUND))
			fg = SWTResourceManager.getColor(AlfaRGB.safeGetRGB((AlfaRGB) value));
		else if (id.equals(PROP_BACKGROUND))
			bg = SWTResourceManager.getColor(AlfaRGB.safeGetRGB((AlfaRGB) value));

		properties = getProperities(getParent());

		properties.setProperty("callouts." + i + ".bounds", x + "," + y + "," + w + "," + h);
		properties.setProperty("callouts." + i + ".text", text);// .replace("\n", "\\n"));
		properties.setProperty("callouts." + i + ".bg", StringConverter.asString(bg.getRGB()));
		properties.setProperty("callouts." + i + ".fg", StringConverter.asString(fg.getRGB()));

		String pins = "";
		List<INode> children = getChildren();
		for (INode ch : children) {
			if (ch instanceof MPin) {
				MPin pin = (MPin) ch;
				if (!pins.isEmpty())
					pins += ";";
				pins += pin.getPropertyValue(JRDesignElement.PROPERTY_X) + ","
						+ pin.getPropertyValue(JRDesignElement.PROPERTY_Y);
			}
		}
		if (!pins.isEmpty()) {
			properties.setProperty("callouts." + i + ".pins", pins);
		}
		else{ 
			properties.remove("callouts." + i + ".pins");
		}
		
		getPropertiesHolder(getParent()).getPropertiesMap().setProperty(PROP_CALLOUT,
				FileUtils.getPropertyAsString(properties).replace("\n", "\\n"));
		
		getPropertyChangeSupport().firePropertyChange((String)id, oldValue, value);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, w, h);
	}

	@Override
	public int getDefaultWidth() {
		return DEFAULT_WIDTH;
	}

	@Override
	public int getDefaultHeight() {
		return DEFAULT_HEIGHT;
	}

	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		return null;
	}

}
