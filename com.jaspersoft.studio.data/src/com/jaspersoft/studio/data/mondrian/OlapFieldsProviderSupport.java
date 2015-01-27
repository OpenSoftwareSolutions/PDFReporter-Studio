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
package com.jaspersoft.studio.data.mondrian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;
import net.sf.jasperreports.olap.result.JROlapMember;
import net.sf.jasperreports.olap.result.JROlapMemberTuple;
import net.sf.jasperreports.olap.result.JROlapResult;
import net.sf.jasperreports.olap.result.JROlapResultAxis;

/**
 * Offers the support methods that using the result of a mondrian or xmla query generate 
 * a set of JRDesignFields
 * 
 * @author Sherman Wood & Marco Orlandin
 *
 */
public class OlapFieldsProviderSupport {

	private static final String PREFIX_COLUMNS = "Columns";
	
	private static final String PREFIX_ROWS = "Rows";
	
	private static final String PREFIX_PAGES = "Pages";
	
	private static final String PREFIX_CHAPTERS = "Chapters";
	
	private static final String PREFIX_SECTIONS = "Sections";
	
	public static List<JRDesignField> getFieldsFromResult(JROlapResult result) {
		Map<String, JRDesignField> fields = new HashMap<String, JRDesignField>();
		/*
		 * Generate fields for all members in all axes at the lowest level
		 * 
		 * <field name="StoreState" class="java.lang.String">
		 * 		<fieldDescription><![CDATA[Rows[Store][Store State]]]></fieldDescription>
		 * </field>
		 * 
		 * In the CellSet (columns)
		 * 
		 * <field name="SalesAmount" class="java.lang.Number">
		 * 		<fieldDescription><![CDATA[Data([Measures].[Sales Amount],?)]]></fieldDescription>
		 * </field>
		 * 
		 * We are not mapping.....
		 * 
		 * .....Properties of members - only works in Mondrian results
		 * 
		 * <field name="Street" class="java.lang.String">
		 * 		<fieldDescription><![CDATA[Rows[Address][Person](Person Address)]]></fieldDescription>
		 *  </field>
		 * 
		 * .....values at different summarization levels
		 * 
		 * <field name="StoreStateUnitSales" class="java.lang.Number">
		 * 		<fieldDescription><![CDATA[Data(Rows[Store][Store State],
		 * 										Rows[Time][Year],
		 * 										Rows[Customers][(All)])
		 * 							([Measures].[Unit Sales],?)]]></fieldDescription>
		 * </field>
		 * 
		 * .........Members: only in Mondrian results
		 * 
		 * <field name="AddressMember" class="mondrian.olap.Member">
		 * 		<fieldDescription><![CDATA[Rows[Address]]]></fieldDescription>
		 * </field>
		 * 
		 */
		Set<String> measureNames = new HashSet<String>();
		
		int axisCount = 0;
		
		int measureLevelAxisIndex = 0;
		
		for (JROlapResultAxis axis : result.getAxes()) {
			String prefix = null;
			switch (axisCount) {
				case 0: prefix = PREFIX_COLUMNS; break;
				case 1: prefix = PREFIX_ROWS; break;
				case 2: prefix = PREFIX_PAGES; break;
				case 3: prefix = PREFIX_CHAPTERS; break;
				case 4: prefix = PREFIX_SECTIONS; break;
				default:
					throw new JRRuntimeException("Unknown axis #: " + axisCount);
			}

			for (JROlapHierarchy hier : axis.getHierarchiesOnAxis()) {

				/*
				 * create a "Rows/Columns" for each level - but not Measures
				 */
				boolean foundMeasuresLevel = false;

				for (JROlapHierarchyLevel level : hier.getLevels()) {
					if (level != null) {
						if (level.getName().equalsIgnoreCase("MeasuresLevel")) {
							foundMeasuresLevel = true;
							measureLevelAxisIndex = axisCount;
						} else {
							String rowsExpression = prefix
									+ makeOlapExpression(hier
											.getDimensionName())
									+ "[" + level.getName() + "]";
							
							addField(makeJRFieldName(hier.getDimensionName()
									+ level.getName()), rowsExpression, "java.lang.String",
									fields);
						}
					}
				}

				/*
				 * Pick up the measure names.
				 * There should always be a measuresLevel.
				 */
				if (foundMeasuresLevel){
					for (int i = 0; i < axis.getTupleCount(); i++) {
						JROlapMemberTuple memberTuple = axis.getTuple(i);
						for (int j = 0; j < memberTuple.getMembers().length; j++) {
							JROlapMember member = memberTuple.getMembers()[j];
							if (isMeasureMember(member.getUniqueName()) && !measureNames.contains(member.getUniqueName())) {
								measureNames.add(member.getUniqueName());
							}
						}
					}
				}
			}
			axisCount++;
		}

		/*
		 * This assumes measures will be only on only rows and columns.
		 * will fail for pages, chapters and sections
		 */
		for (String measureName : measureNames) {
			addField(makeJRFieldName(measureName), generateMeasureAxisExpression(measureLevelAxisIndex, axisCount-1, measureName), "java.lang.Number", fields);
		}

		// preserve the order of field creation
		
		List<JRDesignField> returnedList = new ArrayList<JRDesignField>(fields.size());
		
		for (String key : fields.keySet()) {
			returnedList.add(fields.get(key));
		}
		return returnedList;
	}
	
	private static String generateMeasureAxisExpression(int measureLevelIndex, int axisNumber, String measureName){
		StringBuilder result = new StringBuilder();
		result.append("Data(");
		for(int i=0; i<=axisNumber; i++){
			if (i == measureLevelIndex) result.append(measureName);
			else result.append("?");
			
			if (i != axisNumber) result.append(",");
		}
		result.append(")");
		return result.toString();
	}

		
	private static void addField(String name, String description, String type, Map<String, JRDesignField> fieldsMap) {
		JRDesignField f = new JRDesignField();
		f.setName(name);
		f.setDescription(description);

		f.setValueClassName(type);

		fieldsMap.put(name, f);
	}


	private static String makeJRFieldName(String s) {
		String out = s.replace(" ", "");
		out = out.replace("[", "");
		out = out.replace("]", "");
		out = out.replace("(", "");
		out = out.replace(")", "");
		out = out.replace(".", "");
		return out;
	}

	private static String makeOlapExpression(String s) {
		/*
		 * Could be: - "hierarchy name" - "[hierarchy name]" -
		 * "[dimension].[hierarchy name]"
		 * 
		 * want "[hierarchy name]"
		 */
		String out = s.trim();
		int pos = out.indexOf("].[");
		if (pos != -1) {
			out = out.substring(pos + 2);
		}
		if (out.charAt(0) != '[') {
			out = "[" + out;
		}
		if (out.charAt(out.length() - 1) != ']') {
			out = out + "]";
		}
		return out;
	}

	private static boolean isMeasureMember(String uniqueName) {
		/*
		 * should be "[Measures].[measure name]"
		 */
		return uniqueName.startsWith("[Measures].");
	}
}
