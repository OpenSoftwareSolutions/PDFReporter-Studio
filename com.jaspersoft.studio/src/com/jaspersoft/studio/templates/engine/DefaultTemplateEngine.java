/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.templates.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.templates.ReportBundle;
import com.jaspersoft.templates.TemplateBundle;
import com.jaspersoft.templates.TemplateEngine;
import com.jaspersoft.templates.TemplateEngineException;

/**
 * A default template Engine which expect in the settings a dataset with the key "main_dataset", and few other optional
 * informations. It will then elaborate the input template with the basic rules used with iReport templates.
 * 
 * 
 * @author gtoffoli
 * 
 */
public class DefaultTemplateEngine implements TemplateEngine {

	final static public String DATASET = "main_dataset"; //$NON-NLS-1$
	final static public String FIELDS = "main_fields"; //$NON-NLS-1$
	final static public String GROUP_FIELDS = "main_group_fields"; //$NON-NLS-1$
	final static public String DATA_ADAPTER = "data_adapter"; //$NON-NLS-1$
	final static public String OTHER_PARAMETERS = "parameters"; //$NON-NLS-1$
	final static public String ORDER_GROUP = "create_sort_fields"; //$NON-NLS-1$

	protected boolean createSortFields = false;

	@SuppressWarnings("unchecked")
	@Override
	public ReportBundle generateReportBundle(TemplateBundle template, Map<String, Object> settings,
			JasperReportsContext jContext) throws TemplateEngineException {

		JasperDesign jdCopy = null;
		try {
			// N.B: We need a fresh new copy of the jasper design!
			jdCopy = ModelUtils.copyJasperDesign(jContext, template.getJasperDesign());
		} catch (JRException e) {
			UIUtils.showError(e);
			return null;
		}

		List<Object> fields = (List<Object>) settings.get(FIELDS);
		List<Object> groupFields = (List<Object>) settings.get(GROUP_FIELDS);

		JRDesignDataset dataset = (JRDesignDataset) settings.get(DATASET);
		createSortFields = (Boolean) settings.get(ORDER_GROUP);
		if (dataset != null) {
			jdCopy.getMainDesignDataset().setQuery((JRDesignQuery) dataset.getQuery());

			System.out.println("Query: " + dataset.getQuery().getText()); //$NON-NLS-1$

			for (JRField f : dataset.getFields()) {
				try {
					jdCopy.getMainDesignDataset().addField(f);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
			for (JRParameter p : dataset.getParameters()) {
				try {
					jdCopy.getMainDesignDataset().addParameter(p);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		}

		processTemplate(jdCopy, fields, groupFields);

		/*
		 * Check if there are some extra parameters to add to the default ones, then add them
		 */
		Object subreportParams = settings.get(OTHER_PARAMETERS);
		if (subreportParams != null) {
			JRSubreportParameter[] otherParamters = (JRSubreportParameter[]) subreportParams;
			for (JRSubreportParameter param : otherParamters) {
				if (!jdCopy.getParametersMap().containsKey(param.getName())) {
					JRDesignParameter newParam = new JRDesignParameter();
					newParam.setName(param.getName());
					// newParam.setDefaultValueExpression(param.getExpression());
					try {
						jdCopy.addParameter(newParam);
					} catch (JRException e) {
						e.printStackTrace();
					}
				}
			}
		}

		ReportBundle reportBundle = new ReportBundle(template);

		reportBundle.setJasperDesign(jdCopy);

		return reportBundle;
	}

	protected void processTemplate(JasperDesign jd, List<Object> fields, List<Object> groupFields) {
		String reportType = Misc.nvl(jd.getProperty("template.type"), "tabular"); //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$

		boolean keepExtraGroups = false;
		boolean noLayoutChanges = false;

		if (jd.getProperty("template.keepExtraGroups") != null && jd.getProperty("template.keepExtraGroups").equals("true")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ $NON-NLS-2$ $NON-NLS-3$
			keepExtraGroups = true;
		}

		// Adjusting groups
		if (groupFields != null) {
			for (int i = 0; i < groupFields.size(); ++i) {
				try {
					String name = ((JRField) groupFields.get(i)).getName();
					if (jd.getGroupsList().size() <= i) {
						// Add a new group on the fly...
						JRDesignGroup g = new JRDesignGroup();
						g.setName(name);
						JRDesignExpression jre = new JRDesignExpression();
						jre.setText("$F{" + name + "}"); //$NON-NLS-1$ //$NON-NLS-2$
						g.setExpression(jre);
						jd.addGroup(g);
					}
					if (createSortFields) {
						JRDesignSortField sortfield = new JRDesignSortField();
						sortfield.setType(SortFieldTypeEnum.FIELD);
						sortfield.setOrder(SortOrderEnum.DESCENDING);
						sortfield.setName(name);
						jd.addSortField(sortfield);
					}
				} catch (JRException e) {
				}
				JRField gr = (JRField) groupFields.get(i);
				JRDesignGroup group = (JRDesignGroup) jd.getGroupsList().get(i);

				// find the two elements having as expression: G1Label and G1Field
				if (group.getGroupHeaderSection() != null && group.getGroupHeaderSection().getBands().length > 0) {
					JRBand groupHeaderSection = group.getGroupHeaderSection().getBands()[0];
					JRDesignExpression groupExpression = ExprUtil.setValues(new JRDesignExpression(),
							"$F{" + gr.getName() + "}", gr.getValueClassName()); //$NON-NLS-1$ //$NON-NLS-2$
					group.setExpression(groupExpression);
					JRDesignStaticText st = findStaticTextElement(groupHeaderSection, "G" + (i + 1) + "Label"); //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "GroupLabel"); //$NON-NLS-1$ 
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "Group Label"); //$NON-NLS-1$ 
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "Label"); //$NON-NLS-1$ 
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "Group name"); //$NON-NLS-1$ 
					if (st != null)
						st.setText(gr.getName());

					JRDesignTextField tf = findTextFieldElement(groupHeaderSection, "G" + (i + 1) + "Field"); //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
					if (tf == null)
						tf = findTextFieldElement(groupHeaderSection, "GroupField"); //$NON-NLS-1$ 
					if (tf == null)
						tf = findTextFieldElement(groupHeaderSection, "Group Field"); //$NON-NLS-1$ 
					if (tf == null)
						tf = findTextFieldElement(groupHeaderSection, "Field"); //$NON-NLS-1$ 

					if (tf != null) {
						JRDesignExpression expression = ExprUtil.setValues(new JRDesignExpression(), "$F{" + gr.getName() + "}", //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
								gr.getValueClassName());
						tf.setExpression(expression);
					}
				}
			}
		}
		// Remove extra groups...
		if (!keepExtraGroups && !noLayoutChanges && groupFields != null) {
			while (groupFields.size() < jd.getGroupsList().size()) {
				jd.removeGroup((JRDesignGroup) jd.getGroupsList().get(groupFields.size()));
			}
		}

		JRElementGroup detailBand = (jd.getDetailSection() != null && jd.getDetailSection().getBands() != null && jd
				.getDetailSection().getBands().length > 0) ? jd.getDetailSection().getBands()[0] : null;

		// Adjusting detail...
		if (!noLayoutChanges && reportType != null && reportType.equals("tabular")) { //$NON-NLS-1$ 
			// Add the labels to the column header..
			JRElementGroup columnHeaderBand = (JRDesignBand) jd.getColumnHeader();

			// Find the label template...
			JRDesignStaticText labelElement = null;
			if (columnHeaderBand != null) {
				labelElement = findStaticTextElement(columnHeaderBand, "DetailLabel"); //$NON-NLS-1$ 
				if (labelElement == null)
					labelElement = findStaticTextElement(columnHeaderBand, "Label"); //$NON-NLS-1$ 
				if (labelElement == null)
					labelElement = findStaticTextElement(columnHeaderBand, "Header"); //$NON-NLS-1$ 
			}

			JRDesignTextField fieldElement = null;
			if (detailBand != null) {
				fieldElement = findTextFieldElement(detailBand, "DetailField"); //$NON-NLS-1$ 
				if (fieldElement == null)
					fieldElement = findTextFieldElement(detailBand, "Field"); //$NON-NLS-1$ 
			}

			if (labelElement != null) {
				columnHeaderBand = labelElement.getElementGroup();
				removeElement(columnHeaderBand, labelElement);
			}
			if (fieldElement != null) {
				detailBand = fieldElement.getElementGroup();
				removeElement(detailBand, fieldElement);
			}

			int width = jd.getPageWidth() - jd.getRightMargin() - jd.getLeftMargin();
			if (detailBand != null && detailBand instanceof JRDesignFrame) {
				width = ((JRDesignFrame) detailBand).getWidth();
			}
			int cols = (fields != null ? fields.size() : 0) - (groupFields != null ? groupFields.size() : 0);
			if (cols > 0) {
				width /= cols;
				int currentX = 0;
				if (fields != null)
					for (Object obj : fields) {
						JRDesignField f = (JRDesignField) obj;
						if (groupFields != null && groupFields.contains(f))
							continue;
						if (labelElement != null && columnHeaderBand != null) {
							JRDesignStaticText newLabel = (JRDesignStaticText) labelElement.clone();
							newLabel.setText(f.getName());
							newLabel.setX(currentX);
							newLabel.setWidth(width);
							addElement(columnHeaderBand, newLabel);
						}
						if (fieldElement != null && detailBand != null) {
							JRDesignTextField newTextField = (JRDesignTextField) fieldElement.clone();
							// Fix the class (the Textfield has a limited set of type options...)
							newTextField.setExpression(ExprUtil.setValues(new JRDesignExpression(), "$F{" + f.getName() + "}", //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
									f.getValueClassName()));
							newTextField.setX(currentX);
							newTextField.setWidth(width);
							addElement(detailBand, newTextField);
						}

						currentX += width;
					}
			}

		} else if (!noLayoutChanges && reportType != null && reportType.equals("columnar") && detailBand != null) { //$NON-NLS-1$
			// Add the labels to the column header..
			JRElementGroup detailBandField = (JRDesignBand) jd.getDetailSection().getBands()[0];
			// Find the label template...

			JRDesignStaticText labelElement = findStaticTextElement(detailBand, "DetailLabel"); //$NON-NLS-1$
			if (labelElement == null)
				labelElement = findStaticTextElement(detailBand, "Label"); //$NON-NLS-1$
			if (labelElement == null)
				labelElement = findStaticTextElement(detailBand, "Header"); //$NON-NLS-1$

			JRDesignTextField fieldElement = findTextFieldElement(detailBandField, "DetailField"); //$NON-NLS-1$
			if (fieldElement == null)
				fieldElement = findTextFieldElement(detailBandField, "Field"); //$NON-NLS-1$

			if (labelElement != null) {
				detailBand = labelElement.getElementGroup();
				removeElement(detailBand, labelElement);
			}

			if (fieldElement != null) {
				detailBandField = fieldElement.getElementGroup();
				removeElement(detailBandField, fieldElement);
			}

			int currentY = 0;
			int rowHeight = 0; // Just to set a default...
			if (labelElement != null)
				rowHeight = labelElement.getHeight();
			if (fieldElement != null)
				rowHeight = Math.max(rowHeight, fieldElement.getHeight());
			// if rowHeight is still 0... no row will be added...
			if (fields != null)
				for (Object obj : fields) {
					JRDesignField f = (JRDesignField) obj;
					if (groupFields != null && groupFields.contains(f))
						continue;
					if (labelElement != null) {
						JRDesignStaticText newLabel = (JRDesignStaticText) labelElement.clone();
						newLabel.setText(f.getName());
						newLabel.setY(currentY);
						addElement(detailBand, newLabel);
					}
					if (fieldElement != null) {
						JRDesignTextField newTextField = (JRDesignTextField) fieldElement.clone();
						JRDesignExpression expression = ExprUtil.setValues(new JRDesignExpression(), "$F{" + f.getName() + "}", //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
								f.getValueClassName());

						newTextField.setExpression(expression);
						newTextField.setY(currentY);
						addElement(detailBandField, newTextField);
					}
					currentY += rowHeight;
				}

			setGroupHeight(detailBand, currentY);
			setGroupHeight(detailBandField, currentY);
		}
	}

	/**
	 * Find a JRDesignStaticText element having exp as text.
	 * 
	 * @param band
	 * @param exp
	 * @return the first matching element or null.
	 */
	public static JRDesignStaticText findStaticTextElement(JRElementGroup parent, String exp) {
		JRElement[] elements = parent.getElements();
		for (int i = 0; i < elements.length; ++i) {
			JRElement ele = elements[i];
			if (ele instanceof JRDesignStaticText) {
				JRDesignStaticText st = (JRDesignStaticText) ele;
				if (st.getText() != null && st.getText().equalsIgnoreCase(exp)) {
					return st;
				}
			} else if (ele instanceof JRElementGroup) {
				JRDesignStaticText ele2 = findStaticTextElement((JRElementGroup) ele, exp);
				if (ele2 != null)
					return ele2;
			}
		}
		return null;
	}

	/**
	 * Find a JRDesignTextField element having exp as expression value.
	 * 
	 * @param band
	 * @param exp
	 * @return the first matching element or null.
	 */
	public static JRDesignTextField findTextFieldElement(JRElementGroup band, String exp) {
		JRElement[] elements = band.getElements();
		for (int i = 0; i < elements.length; ++i) {
			JRElement ele = elements[i];
			if (ele instanceof JRDesignTextField) {
				String s = ExprUtil.getExpressionText(((JRDesignTextField) ele).getExpression());
				if (s.startsWith("\"")) { //$NON-NLS-1$
					s = s.substring(1);
				}
				if (s.endsWith("\"")) { //$NON-NLS-1$
					s = s.substring(0, s.length() - 1);
				}
				if (s.equalsIgnoreCase(exp))
					return (JRDesignTextField) ele;
			} else if (ele instanceof JRElementGroup) {
				JRDesignTextField ele2 = findTextFieldElement((JRElementGroup) ele, exp);
				if (ele2 != null)
					return ele2;
			}
		}
		return null;
	}

	/**
	 * Remove an element from its container. This method checks if the container is a frame or an element groups (like a
	 * band or a cell);
	 * 
	 * @param container
	 * @param element
	 */
	public void removeElement(JRElementGroup container, JRDesignElement element) {
		if (container instanceof JRDesignElementGroup) {
			((JRDesignElementGroup) container).removeElement(element);
		}
		if (container instanceof JRDesignFrame) {
			((JRDesignFrame) container).removeElement(element);
		}
	}

	/**
	 * Add an element to a container. This method checks if the container is a frame or an element groups (like a band or
	 * a cell);
	 * 
	 * @param container
	 * @param element
	 */
	public void addElement(JRElementGroup container, JRDesignElement element) {
		if (container instanceof JRDesignElementGroup) {
			((JRDesignElementGroup) container).addElement(element);
		}
		if (container instanceof JRDesignFrame) {
			((JRDesignFrame) container).addElement(element);
		}
	}

	/**
	 * Set the height of a container (which could be a band or a frame). If the height of the container is already bigger
	 * than the passed in value, the container is left unchanged.
	 * 
	 * @param container
	 * @param minHeight
	 *          - The minimum height of the container
	 */
	private void setGroupHeight(JRElementGroup container, int minHeight) {
		if (container instanceof JRDesignBand) {
			((JRDesignBand) container).setHeight(Math.max(minHeight, ((JRDesignBand) container).getHeight()));
		}
		if (container instanceof JRDesignFrame) {
			((JRDesignFrame) container).setHeight(Math.max(minHeight, ((JRDesignFrame) container).getHeight()));
		}
	}

	/**
	 * Get a JasperDesign and check if that JasperDesign can be used as Template and processed by this engine.
	 * 
	 * @param design
	 *          the design to check
	 * @return a List of founded error, the list is void if no error are found
	 */
	public static List<String> validateJasperDesig(JasperDesign design) {
		List<String> errorsList = new ArrayList<String>();

		int groupIndex = 0;
		for (net.sf.jasperreports.engine.JRGroup jrGroup : design.getGroupsList()) {
			JRDesignGroup group = (JRDesignGroup) jrGroup;
			// find the two elements having as expression: G1Label and G1Field
			if (group.getGroupHeaderSection() != null && group.getGroupHeaderSection().getBands().length > 0) {
				JRBand groupHeaderSection = group.getGroupHeaderSection().getBands()[0];
				JRDesignStaticText st = findStaticTextElement(groupHeaderSection, "G" + (groupIndex + 1) + "Label"); //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
				if (st == null)
					st = findStaticTextElement(groupHeaderSection, "GroupLabel"); //$NON-NLS-1$ 
				if (st == null)
					st = findStaticTextElement(groupHeaderSection, "Group Label"); //$NON-NLS-1$ 
				if (st == null)
					st = findStaticTextElement(groupHeaderSection, "Label"); //$NON-NLS-1$ 
				if (st == null)
					st = findStaticTextElement(groupHeaderSection, "Group name"); //$NON-NLS-1$ 

				JRDesignTextField tf = findTextFieldElement(groupHeaderSection, "G" + (groupIndex + 1) + "Field"); //$NON-NLS-1$ //$NON-NLS-2$ $NON-NLS-2$
				if (tf == null)
					tf = findTextFieldElement(groupHeaderSection, "GroupField"); //$NON-NLS-1$ 
				if (tf == null)
					tf = findTextFieldElement(groupHeaderSection, "Group Field"); //$NON-NLS-1$ 
				if (tf == null)
					tf = findTextFieldElement(groupHeaderSection, "Field"); //$NON-NLS-1$ 

				if (st == null && tf == null)
					errorsList.add(Messages.DefaultTemplateEngine_missingGroupFiledStatic + (groupIndex + 1));
			}
			groupIndex++;
		}

		String reportType = Misc.nvl(design.getProperty("template.type"), "tabular"); //$NON-NLS-1$ //$NON-NLS-2$

		JRElementGroup detailBand = (design.getDetailSection() != null && design.getDetailSection().getBands() != null && design
				.getDetailSection().getBands().length > 0) ? design.getDetailSection().getBands()[0] : null;

		// Adjusting detail...
		if (reportType != null && reportType.equals("tabular")) { //$NON-NLS-1$ 
			// Add the labels to the column header..
			JRElementGroup columnHeaderBand = (JRDesignBand) design.getColumnHeader();

			// Find the label template...
			JRDesignStaticText labelElement = null;
			if (columnHeaderBand != null) {
				labelElement = findStaticTextElement(columnHeaderBand, "DetailLabel"); //$NON-NLS-1$ 
				if (labelElement == null)
					labelElement = findStaticTextElement(columnHeaderBand, "Label"); //$NON-NLS-1$ 
				if (labelElement == null)
					labelElement = findStaticTextElement(columnHeaderBand, "Header"); //$NON-NLS-1$ 
			}

			JRDesignTextField fieldElement = null;
			if (detailBand != null) {
				fieldElement = findTextFieldElement(detailBand, "DetailField"); //$NON-NLS-1$ 
				if (fieldElement == null)
					fieldElement = findTextFieldElement(detailBand, "Field"); //$NON-NLS-1$ 
			}

			if (labelElement == null) {
				errorsList.add(Messages.DefaultTemplateEngine_missingStaticTextCH);
			}
			if (fieldElement == null) {
				errorsList.add(Messages.DefaultTemplateEngine_missingTextFieldD);
			}

		} else if (reportType.equals("columnar") && detailBand != null) { //$NON-NLS-1$
			// Add the labels to the column header..
			JRElementGroup detailBandField = (JRDesignBand) design.getDetailSection().getBands()[0];
			// Find the label template...

			JRDesignStaticText labelElement = findStaticTextElement(detailBand, "DetailLabel"); //$NON-NLS-1$
			if (labelElement == null)
				labelElement = findStaticTextElement(detailBand, "Label"); //$NON-NLS-1$
			if (labelElement == null)
				labelElement = findStaticTextElement(detailBand, "Header"); //$NON-NLS-1$

			JRDesignTextField fieldElement = findTextFieldElement(detailBandField, "DetailField"); //$NON-NLS-1$
			if (fieldElement == null)
				fieldElement = findTextFieldElement(detailBandField, "Field"); //$NON-NLS-1$

			if (labelElement == null) {
				errorsList.add("Missing Static Text placeholder in the detail band");; //$NON-NLS-1$
			}

			if (fieldElement != null) {
				errorsList.add(Messages.DefaultTemplateEngine_missingTextFieldD);
			}
		}

		return errorsList;
	}

	@Override
	public void setReportDataAdapter(ReportBundle bundle, DataAdapterDescriptor dataadapter, JRPropertiesMap properties) {
		JasperDesign jd = bundle.getJasperDesign();
		for (String key : properties.getPropertyNames())
			jd.setProperty(key, properties.getProperty(key));
		jd.setProperty(DataQueryAdapters.DEFAULT_DATAADAPTER, dataadapter.getName());
	}

}
