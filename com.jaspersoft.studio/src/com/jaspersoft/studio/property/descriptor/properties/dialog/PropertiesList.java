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
package com.jaspersoft.studio.property.descriptor.properties.dialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PropertiesList {

	private static List<PropertyDTO> props;
	private static String[] names;
	private static String[] sortedProperties;

	public static String[] getPropertiesNames() {
		if (names != null)
			return names;
		getJRProperties();
		String[] res = new String[props.size()];
		for (int i = 0; i < props.size(); i++)
			res[i] = props.get(i).getProperty();
		names = res;
		return res;
	}
	
	public static String[] getSortedProperitesNames() {
		if (sortedProperties != null) 
			return sortedProperties;
		getJRProperties();
		List<String> res = new ArrayList<String>();
		HashSet<String> alreadyAdded = new HashSet<String>();
		for (int i = 0; i < props.size(); i++){
			String actualProp = props.get(i).getProperty();
			if (!alreadyAdded.contains(actualProp)){
				alreadyAdded.add(actualProp);
				res.add(actualProp);
			}
		}
		sortedProperties = res.toArray(new String[res.size()]);
		return sortedProperties;
	}

	public static PropertyDTO getDTO(String name) {
		//Called to initialize the properties list
		getJRProperties();
		for (PropertyDTO dto : props)
			if (dto.getProperty().equalsIgnoreCase(name))
				return dto;
		return null;
	}

	public static List<PropertyDTO> getJRProperties() {
		if (props != null)
			return props;
		props = new ArrayList<PropertyDTO>();
		for (int i = 0; i < 20; i++) {
			props.add(new PropertyDTO("net.sf.jasperreports.text.truncate.at.char", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.text.truncate.suffix", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.print.keep.full.text", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.text.measurer.factory", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.chart.theme", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.h1", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.h2", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.h3", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.table", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.tr", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.th", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.td", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.colspan", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.rowspan", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.html.id", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.character.encoding", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.graphics2d.min.job.size", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.html.frames.as.nested.tables", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.html.remove.empty.space.between.rows", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.html.white.page.background", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.html.wrap.break.word", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.html.size.unit", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.html.using.images.to.align", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.force.svg.shapes", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.force.linebreak.policy", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.create.batch.mode.bookmarks", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.compressed", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.encrypted", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.128.bit.key", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.create.custom.palette", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.one.page.per.sheet", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.remove.empty.space.between.rows", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.remove.empty.space.between.columns", "Property", "true"));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.white.page.background", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.detect.cell.type", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.font.size.fix.enabled", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.ignore.graphics", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.collapse.row.span", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.ignore.cell.border", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xls.max.rows.per.sheet", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.xml.validation", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.csv.field.delimiter", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.csv.record.delimiter", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tagged", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			props.add(new PropertyDTO("net.sf.jasperreports.export.pdf.tag.language", "Property", "true")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return props;
	}
}
