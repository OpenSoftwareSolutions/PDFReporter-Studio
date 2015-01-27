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
package com.jaspersoft.studio.wizards.report;

import java.util.List;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.utils.Misc;

public class ReportGenerator {
	public void processTemplate(JasperDesign jd, List<Object> fields, List<Object> groupFields) {
		String reportType = Misc.nvl(jd.getProperty("template.type"), "tabular");

		boolean keepExtraGroups = false;
		boolean noLayoutChanges = false;

		if (jd.getProperty("template.keepExtraGroups") != null && jd.getProperty("template.keepExtraGroups").equals("true")) {
			keepExtraGroups = true;
		}

		// Adjusting groups
		if (groupFields != null)
			for (int i = 0; i < groupFields.size(); ++i) {
				JRField gr = (JRField) groupFields.get(i);
				JRDesignGroup group = (JRDesignGroup) jd.getGroupsList().get(i);

				// find the two elements having as expression: G1Label and G1Field
				if (group.getGroupHeaderSection() != null && group.getGroupHeaderSection().getBands().length > 0) {
					JRBand groupHeaderSection = group.getGroupHeaderSection().getBands()[0];
					JRDesignStaticText st = findStaticTextElement(groupHeaderSection, "G" + (i + 1) + "Label");
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "GroupLabel");
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "Group Label");
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "Label");
					if (st == null)
						st = findStaticTextElement(groupHeaderSection, "Group name");
					if (st != null)
						st.setText(gr.getName());

					JRDesignTextField tf = findTextFieldElement(groupHeaderSection, "G" + (i + 1) + "Field");
					if (tf == null)
						tf = findTextFieldElement(groupHeaderSection, "GroupField");
					if (tf == null)
						tf = findTextFieldElement(groupHeaderSection, "Group Field");
					if (tf == null)
						tf = findTextFieldElement(groupHeaderSection, "Field");

					if (tf != null) {
						JRDesignExpression expression = ExprUtil.setValues(new JRDesignExpression(), "$F{" + gr.getName() + "}",
								gr.getValueClassName());
						tf.setExpression(expression);
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
		if (!noLayoutChanges && reportType != null && reportType.equals("tabular")) {
			// Add the labels to the column header..
			JRElementGroup columnHeaderBand = (JRDesignBand) jd.getColumnHeader();

			// Find the label template...
			JRDesignStaticText labelElement = null;
			if (columnHeaderBand != null) {
				labelElement = findStaticTextElement(columnHeaderBand, "DetailLabel");
				if (labelElement == null)
					labelElement = findStaticTextElement(columnHeaderBand, "Label");
				if (labelElement == null)
					labelElement = findStaticTextElement(columnHeaderBand, "Header");
			}

			JRDesignTextField fieldElement = null;
			if (detailBand != null) {
				fieldElement = findTextFieldElement(detailBand, "DetailField");
				if (fieldElement == null)
					fieldElement = findTextFieldElement(detailBand, "Field");
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
							newTextField.setExpression(ExprUtil.setValues(new JRDesignExpression(), "$F{" + f.getName() + "}",
									f.getValueClassName()));
							newTextField.setX(currentX);
							newTextField.setWidth(width);
							addElement(detailBand, newTextField);
						}

						currentX += width;
					}
			}

		} else if (!noLayoutChanges && reportType != null && reportType.equals("columnar") && detailBand != null) {
			// Add the labels to the column header..
			JRElementGroup detailBandField = (JRDesignBand) jd.getDetailSection().getBands()[0];
			// Find the label template...

			JRDesignStaticText labelElement = findStaticTextElement(detailBand, "DetailLabel");
			if (labelElement == null)
				labelElement = findStaticTextElement(detailBand, "Label");
			if (labelElement == null)
				labelElement = findStaticTextElement(detailBand, "Header");

			JRDesignTextField fieldElement = findTextFieldElement(detailBandField, "DetailField");
			if (fieldElement == null)
				fieldElement = findTextFieldElement(detailBandField, "Field");

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
						JRDesignExpression expression = ExprUtil.setValues(new JRDesignExpression(), "$F{" + f.getName() + "}",
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
	 * Find in band a JRDesignTextField element having exp as expression value.
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
				if (s.startsWith("\"")) {
					s = s.substring(1);
				}
				if (s.endsWith("\"")) {
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

	public void removeElement(JRElementGroup container, JRDesignElement element) {
		if (container instanceof JRDesignElementGroup) {
			((JRDesignElementGroup) container).removeElement(element);
		}
		if (container instanceof JRDesignFrame) {
			((JRDesignFrame) container).removeElement(element);
		}
	}

	public void addElement(JRElementGroup container, JRDesignElement element) {
		if (container instanceof JRDesignElementGroup) {
			((JRDesignElementGroup) container).addElement(element);

		}
		if (container instanceof JRDesignFrame) {
			((JRDesignFrame) container).addElement(element);
		}
	}

	private void setGroupHeight(JRElementGroup container, int currentY) {
		if (container instanceof JRDesignBand) {
			((JRDesignBand) container).setHeight(Math.max(currentY, ((JRDesignBand) container).getHeight()));

		}
		if (container instanceof JRDesignFrame) {
			((JRDesignFrame) container).setHeight(Math.max(currentY, ((JRDesignFrame) container).getHeight()));
		}
	}
}
