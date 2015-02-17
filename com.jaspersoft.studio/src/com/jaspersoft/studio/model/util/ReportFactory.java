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

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRScriptlet;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignBreak;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignEllipse;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignScriptlet;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.core.resources.IFile;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MBreak;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MEllipse;
import com.jaspersoft.studio.model.MLine;
import com.jaspersoft.studio.model.MRectangle;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.MBandGroupHeader;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.field.MFields;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.model.genericElement.MComponentElement;
import com.jaspersoft.studio.model.genericElement.MGenericElement;
import com.jaspersoft.studio.model.group.MGroup;
import com.jaspersoft.studio.model.group.MGroups;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.model.parameter.MParameterSystem;
import com.jaspersoft.studio.model.parameter.MParameters;
import com.jaspersoft.studio.model.scriptlet.MScriptlet;
import com.jaspersoft.studio.model.scriptlet.MScriptlets;
import com.jaspersoft.studio.model.scriptlet.MSystemScriptlet;
import com.jaspersoft.studio.model.sortfield.MSortField;
import com.jaspersoft.studio.model.sortfield.MSortFields;
import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.model.style.MStyles;
import com.jaspersoft.studio.model.style.StyleTemplateFactory;
import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.model.text.MStaticText;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.model.variable.MVariableSystem;
import com.jaspersoft.studio.model.variable.MVariables;
import com.jaspersoft.studio.plugin.ExtensionManager;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * A factory for creating Report objects.
 * 
 * @author Chicu Veaceslav
 */
public class ReportFactory {

	/**
	 * Creates a new Report object.
	 * 
	 * @param jd
	 *          the jd
	 * @return the i node
	 */
	public static INode createReport(JasperReportsConfiguration jConfig) {
		JasperDesign jd = jConfig.getJasperDesign();
		ANode node = new MRoot(null, jd);
		ANode report = new MReport(node, jConfig);
		// create first level
		// create Styles
		createStyles(jConfig, jd, report, -1);
		// create datasets
		createDataset(report, jd.getMainDesignDataset(), false);

		if (jd.getDatasetsList() != null) {
			for (JRDataset jrDataset : jd.getDatasetsList()) {
				createDataset(new MDataset(report, (JRDesignDataset) jrDataset, -1), (JRDesignDataset) jrDataset, true);
			}

		}

		MBand title = new MBand(report, jd.getTitle(), BandTypeEnum.TITLE, -1);
		if (jd.getTitle() != null)
			createElementsForBand(title, jd.getTitle().getChildren());

		MBand pageHeader = new MBand(report, jd.getPageHeader(), BandTypeEnum.PAGE_HEADER, -1);
		if (jd.getPageHeader() != null)
			createElementsForBand(pageHeader, jd.getPageHeader().getChildren());

		MBand columnHeader = new MBand(report, jd.getColumnHeader(), BandTypeEnum.COLUMN_HEADER, -1);
		if (jd.getColumnHeader() != null)
			createElementsForBand(columnHeader, jd.getColumnHeader().getChildren());

		if (jd.getGroupsList() != null) {
			for (JRGroup gr : jd.getGroupsList()) {
				MBandGroupHeader b = null;
				if (gr.getGroupHeaderSection() != null) {
					List<JRBand> grhBands = ((JRDesignSection) gr.getGroupHeaderSection()).getBandsList();
					if (grhBands != null) {
						for (Iterator<?> it = grhBands.iterator(); it.hasNext();) {
							JRDesignBand jrDB = (JRDesignBand) it.next();
							b = new MBandGroupHeader(report, (JRDesignGroup) gr, jrDB, -1);
							createElementsForBand(b, jrDB.getChildren());
						}
					}
				}
				if (b == null)
					new MBandGroupHeader(report, (JRDesignGroup) gr, null, -1);
			}
		}

		MBand detBand = null;
		if (jd.getDetailSection() != null) {
			JRBand[] bandsList = jd.getDetailSection().getBands();
			if (bandsList != null)
				for (int k = 0; k < bandsList.length; k++) {
					if (bandsList[k] != null) {
						detBand = new MBand(report, bandsList[k], BandTypeEnum.DETAIL, -1);
						createElementsForBand(detBand, bandsList[k].getChildren());
					}
				}
		}
		if (detBand == null)
			new MBand(report, null, BandTypeEnum.DETAIL, -1);

		if (jd.getGroupsList() != null) {
			for (ListIterator<JRGroup> ij = jd.getGroupsList().listIterator(jd.getGroupsList().size()); ij.hasPrevious();) {
				JRGroup gr = ij.previous();
				MBandGroupFooter b = null;
				if (gr.getGroupFooterSection() != null) {
					List<JRBand> grhBands = ((JRDesignSection) gr.getGroupFooterSection()).getBandsList();
					if (grhBands != null) {
						for (Iterator<?> it = grhBands.iterator(); it.hasNext();) {
							JRDesignBand jrDB = (JRDesignBand) it.next();
							b = new MBandGroupFooter(report, (JRDesignGroup) gr, jrDB, -1);
							createElementsForBand(b, jrDB.getChildren());
						}
					}
				}
				if (b == null)
					new MBandGroupFooter(report, (JRDesignGroup) gr, null, -1);
			}
		}

		MBand columnFooter = new MBand(report, jd.getColumnFooter(), BandTypeEnum.COLUMN_FOOTER, -1);
		if (jd.getColumnFooter() != null)
			createElementsForBand(columnFooter, jd.getColumnFooter().getChildren());

		MBand footer = new MBand(report, jd.getPageFooter(), BandTypeEnum.PAGE_FOOTER, -1);
		if (jd.getPageFooter() != null)
			createElementsForBand(footer, jd.getPageFooter().getChildren());

		MBand lastPageFooter = new MBand(report, jd.getLastPageFooter(), BandTypeEnum.LAST_PAGE_FOOTER, -1);
		if (jd.getLastPageFooter() != null)
			createElementsForBand(lastPageFooter, jd.getLastPageFooter().getChildren());

		MBand summary = new MBand(report, jd.getSummary(), BandTypeEnum.SUMMARY, -1);
		if (jd.getSummary() != null)
			createElementsForBand(summary, jd.getSummary().getChildren());

		MBand nodata = new MBand(report, jd.getNoData(), BandTypeEnum.NO_DATA, -1);
		if (jd.getNoData() != null)
			createElementsForBand(nodata, jd.getNoData().getChildren());

		MBand background = new MBand(report, jd.getBackground(), BandTypeEnum.BACKGROUND, -1);
		if (jd.getBackground() != null)
			createElementsForBand(background, jd.getBackground().getChildren());

		MCallout.createCallouts(report);

		return node;
	}

	public static void createStyles(JasperReportsConfiguration jConfig, JasperDesign jd, ANode report, int index) {
		MStyles nStyle = new MStyles(report, index);
		if (jd.getTemplates() != null)
			for (Iterator<JRReportTemplate> it = jd.getTemplatesList().iterator(); it.hasNext();)
				createNode(nStyle, it.next(), -1, (IFile) jConfig.get(FileUtils.KEY_FILE));
		if (jd.getStyles() != null) {
			for (JRStyle jrstyle : jd.getStylesList()) {
				ANode mstyle = createNode(nStyle, jrstyle, -1);
				if (((JRDesignStyle) jrstyle).getConditionalStyleList() != null)
					for (Object jrc : ((JRDesignStyle) jrstyle).getConditionalStyleList())
						createNode(mstyle, jrc, -1);
			}
		}
		nStyle.updateDefaulStyle();
	}

	/**
	 * Creates a new Report object.
	 * 
	 * @param nDataset
	 *          the n dataset
	 * @param dataSet
	 *          the data set
	 * @param showGroups
	 *          the show groups
	 */
	public static void createDataset(ANode nDataset, JRDesignDataset dataSet, boolean showGroups) {
		// create parameters
		ANode nParameters = new MParameters<JRDesignDataset>(nDataset, dataSet, JRDesignDataset.PROPERTY_PARAMETERS);
		if (dataSet.getParametersList() != null) {
			for (JRParameter jrparam : dataSet.getParametersList()) {
				createNode(nParameters, jrparam, -1);
			}
		}
		// create fields
		ANode nFields = new MFields(nDataset, dataSet);
		if (dataSet.getFieldsList() != null) {
			for (JRField jrField : dataSet.getFieldsList()) {
				createNode(nFields, jrField, -1);
			}
		}
		// create sort fields
		ANode nSortFields = new MSortFields(nDataset, dataSet);
		if (dataSet.getSortFieldsList() != null) {
			for (JRSortField sortField : dataSet.getSortFieldsList()) {
				createNode(nSortFields, sortField, -1);
			}
		}
		// create variables
		ANode nVariables = new MVariables(nDataset, dataSet);
		if (dataSet.getVariablesList() != null)
			for (JRVariable jrVar : dataSet.getVariablesList())
				// if (!jrVar.isSystemDefined())
				createNode(nVariables, jrVar, -1);

		// create scriplets
		/*
		ANode nScriptlets = new MScriptlets(nDataset, dataSet);
		if (dataSet.getParametersMap().containsKey(JRParameter.REPORT_SCRIPTLET)) {
			JRParameter prm = dataSet.getParametersMap().get(JRParameter.REPORT_SCRIPTLET);
			JRDesignScriptlet jrscriptlet = new JRDesignScriptlet();
			jrscriptlet.setName("REPORT_SCRIPTLET");
			jrscriptlet.setDescription("Default Scriptlet");
			jrscriptlet.setValueClassName(prm.getValueClassName());
			createNode(nScriptlets, jrscriptlet, -1);
		}
		if (dataSet.getScriptletClass() != null) {
			JRDesignScriptlet jrscriptlet = new JRDesignScriptlet();
			jrscriptlet.setName("DATASET_SCRIPTLET");
			jrscriptlet.setDescription("Default Scriptlet");
			jrscriptlet.setValueClassName(dataSet.getScriptletClass());
			createNode(nScriptlets, jrscriptlet, -1);
		}
		if (dataSet.getScriptletsList() != null) {
			for (JRScriptlet jrScriptlet : dataSet.getScriptletsList())
				createNode(nScriptlets, jrScriptlet, -1);
		}
    */
		if (showGroups) {
			// create scriplets
			ANode nGroups = new MGroups(nDataset, dataSet);
			if (dataSet.getGroupsList() != null) {
				for (JRGroup jrGroup : dataSet.getGroupsList()) {
					createNode(nGroups, jrGroup, -1);
				}
			}
		}
	}

	/**
	 * Creates a new Report object.
	 * 
	 * @param band
	 *          the band
	 * @param list
	 *          the list
	 */
	public static void createElementsForBand(ANode band, List<?> list) {
		for (Object element : list) {
			ANode node = createNode(band, element, -1);
			// ExtensionManager m = JaspersoftStudioPlugin.getExtensionManager();
			// List<?> children = m.getChildren4Element(element);
			// if (children != null && !children.isEmpty()) {
			// createElementsForBand(node, children);
			// } else
			if (element instanceof JRDesignFrame) {
				JRDesignFrame frame = (JRDesignFrame) element;
				createElementsForBand(node, frame.getChildren());
			} else if (element instanceof JRElementGroup) {
				JRElementGroup group = (JRElementGroup) element;
				createElementsForBand(node, group.getChildren());
			}
		}
	}

	public static ANode createNode(ANode parent, Object jrObject, int newIndex, IFile file) {
		ExtensionManager m = JaspersoftStudioPlugin.getExtensionManager();
		ANode n = m.createNode(parent, jrObject, newIndex);
		if (n != null) {
			List<?> children = m.getChildren4Element(jrObject);
			if (children != null && !children.isEmpty())
				createElementsForBand(n, children);
			return n;
		} else if (jrObject instanceof JRDesignBand) {
			return new MBand(parent, (JRDesignBand) jrObject, ((JRDesignBand) jrObject).getOrigin().getBandTypeValue(),
					newIndex);
		} else if (jrObject instanceof JRFrame) {
			return new MFrame(parent, (JRDesignFrame) jrObject, newIndex);
		} else if (jrObject instanceof JRElementGroup) {
			return new MElementGroup(parent, (JRElementGroup) jrObject, newIndex);
		} else if (jrObject instanceof JRSubreport) {
			return new MSubreport(parent, (JRDesignSubreport) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignEllipse) {
			return new MEllipse(parent, (JRDesignEllipse) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignRectangle) {
			return new MRectangle(parent, (JRDesignRectangle) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignLine) {
			return new MLine(parent, (JRDesignLine) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignImage) {
			return new MImage(parent, (JRDesignImage) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignImage) {
			return new MImage(parent, (JRDesignImage) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignStaticText) {
			return new MStaticText(parent, (JRDesignStaticText) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignBreak) {
			return new MBreak(parent, (JRDesignBreak) jrObject, newIndex);

		} else if (jrObject instanceof JRDesignTextField) {
			return new MTextField(parent, (JRDesignTextField) jrObject, newIndex);
		} else if (jrObject instanceof JRGenericElement) {
			return new MGenericElement(parent, (JRDesignGenericElement) jrObject, newIndex);

		} else if (jrObject instanceof JRDesignComponentElement) {
			return new MComponentElement(parent, (JRDesignComponentElement) jrObject, newIndex);
			// styles
		} else if (jrObject instanceof JRDesignStyle) {
			if (newIndex != -1) {
				JRReportTemplate[] templates = parent.getJasperDesign().getTemplates();
				if (templates != null && templates.length > 0)
					newIndex += templates.length;
			}
			return new MStyle(parent, (JRDesignStyle) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignConditionalStyle) {
			MConditionalStyle mConditionalStyle = new MConditionalStyle(parent, (JRDesignConditionalStyle) jrObject, newIndex);
			mConditionalStyle.setEditable(((APropertyNode) parent).isEditable());
			return mConditionalStyle;
		} else if (jrObject instanceof JRDesignReportTemplate) {
			return StyleTemplateFactory.createTemplate(parent, (JRDesignReportTemplate) jrObject, newIndex, file);
			// parameters
		} else if (jrObject instanceof JRDesignParameter) {
			JRDesignParameter jrParameter = (JRDesignParameter) jrObject;
			if (jrParameter.isSystemDefined())
				return new MParameterSystem(parent, jrParameter, newIndex);
			return new MParameter(parent, jrParameter, newIndex);
		} else if (jrObject instanceof JRDesignField) {
			return new MField(parent, (JRDesignField) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignSortField) {
			return new MSortField(parent, (JRDesignSortField) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignGroup) {
			return new MGroup(parent, (JRDesignGroup) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignVariable) {
			JRDesignVariable jrVariable = (JRDesignVariable) jrObject;
			if (jrVariable.isSystemDefined())
				return new MVariableSystem(parent, jrVariable, newIndex);
			else
				return new MVariable(parent, jrVariable, newIndex);
		} else if (jrObject instanceof JRDesignScriptlet) {
			if (parent instanceof MScriptlets) {
				MScriptlets ms = (MScriptlets) parent;
				for (INode node : ms.getChildren()) {
					JRDesignScriptlet jds = (JRDesignScriptlet) node.getValue();
					if (jds.getName().equals("REPORT_SCRIPTLET") || jds.getName().equals("DATASET_SCRIPTLET")) {
						if (newIndex < 0)
							newIndex = 0;
						newIndex++;
					}
				}
			}
			if (((JRDesignScriptlet) jrObject).getName().equals("REPORT_SCRIPTLET"))
				return new MSystemScriptlet(parent, (JRDesignScriptlet) jrObject, newIndex);
			if (((JRDesignScriptlet) jrObject).getName().equals("DATASET_SCRIPTLET"))
				return new MSystemScriptlet(parent, (JRDesignScriptlet) jrObject, newIndex);

			return new MScriptlet(parent, (JRDesignScriptlet) jrObject, newIndex);
		} else if (jrObject instanceof JRDesignDataset) {
			return new MDataset(parent, (JRDesignDataset) jrObject, newIndex);
		} else {
			newIndex++;
		}
		return null;
	}

	/**
	 * Creates a new Report object.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrObject
	 *          the jr object
	 * @param newIndex
	 *          the new index
	 * @return the a node
	 */
	public static ANode createNode(ANode parent, Object jrObject, int newIndex) {
		return createNode(parent, jrObject, newIndex, null);
	}
}
