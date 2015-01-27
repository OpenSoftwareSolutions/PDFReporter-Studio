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
package com.jaspersoft.studio.model.style;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.ExternalStylesManager;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * The Class MStyleTemplate. It will also listen on the change of the style expression to reload
 * the style children when it changes
 * 
 * @author Chicu Veaceslav & Orlandin Marco
 */
public class MStyleTemplate extends APropertyNode implements IPropertySource, ICopyable {
	
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;
	
	/**
	 * Icon used when the style can not be resolved
	 */
	private static ImageDescriptor styleNotFoundImage = JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/no_style_error.png");
	
	/**
	 * Array of the descriptors of the element
	 */
	private static IPropertyDescriptor[] descriptors;
	
	/**
	 * Default values for the element
	 */
	private static Map<String, Object> defaultsMap;

	/**
	 * Timeout time to wait between the end of an expression change and the refresh of the 
	 * element content. Used to avoid to many refresh when the user write
	 */
	private static final int UPDATE_DELAY=500;
	
	/**
	 * The job that update the styles content in background
	 */
	private UpdateStyleJob updateStyleJob;
	
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

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		JRExpressionPropertyDescriptor sourceExpression = new JRExpressionPropertyDescriptor(
				JRDesignReportTemplate.PROPERTY_SOURCE_EXPRESSION, Messages.MStyleTemplate_source_expression);
		sourceExpression.setDescription(Messages.MStyleTemplate_source_expression_description);
		desc.add(sourceExpression);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#template");
	}

	
	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("styletemplate"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m style template.
	 */
	public MStyleTemplate() {
		super();
	}

	/**
	 * Instantiates a new m style template.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrstyle
	 *          the jrstyle
	 * @param newIndex
	 *          the new index
	 */
	public MStyleTemplate(ANode parent, JRReportTemplate jrstyle, int newIndex) {
		super(parent, newIndex);
		setValue(jrstyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		JRDesignReportTemplate jt = (JRDesignReportTemplate) getValue();
		if (jt != null && jt.getSourceExpression() != null && jt.getSourceExpression().getText() != null){
			return  getIconDescriptor().getTitle() + "(" + jt.getSourceExpression().getText() + ")";
		}
		return getIconDescriptor().getTitle();
	}
	
	/**
	 * Return the image for this element, the image change if the style can not be resolved, in this 
	 * way we can show something like an error decorator if the expression of the style is not solvable
	 */
	public ImageDescriptor getImagePath() {
		JRDesignReportTemplate jt = (JRDesignReportTemplate) getValue();
		if (jt != null && jt.getSourceExpression() != null && jt.getSourceExpression().getText() != null && ExternalStylesManager.isNotValuable(this)){
			return styleNotFoundImage;
		}
		return getIconDescriptor().getIcon16();
	}

	/**
	 * Return the textual tooltip of the style. If its expression can not be solved an error message is also 
	 * shown
	 */
	@Override
	public String getToolTip() {
		JRDesignReportTemplate jt = (JRDesignReportTemplate) getValue();
		if (jt != null && jt.getSourceExpression() != null && jt.getSourceExpression().getText() != null && ExternalStylesManager.isNotValuable(this)){
			return "The resource can not be found, fix the expression and reload the style to use it";
		} else 	return getIconDescriptor().getToolTip();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignReportTemplate jrTemplate = (JRDesignReportTemplate) getValue();
		if (id.equals(JRDesignReportTemplate.PROPERTY_SOURCE_EXPRESSION))
			return ExprUtil.getExpression(jrTemplate.getSourceExpression());

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignReportTemplate jrTemplate = (JRDesignReportTemplate) getValue();
		if (id.equals(JRDesignReportTemplate.PROPERTY_SOURCE_EXPRESSION))
			jrTemplate.setSourceExpression(ExprUtil.setValues(jrTemplate.getSourceExpression(), value));
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		//If the expression change try to reload the style
		if (evt.getPropertyName().equals(JRDesignReportTemplate.PROPERTY_SOURCE_EXPRESSION)){
			performUpdate();
		}
		super.propertyChange(evt);
	}
	
	/**
	 * Since the style don't see when its children are updated (because the the relation between 
	 * style template and its inner styles is done only by our model, not by the jr structure). So
	 * when we add children to a style JR don't fire any event. Because of this to have a graphical 
	 * Refresh we must fire the event manually to have the update and see the children 
	 */
	private void fireChildrenChangeEvent(){
		//Need to be executed inside the graphic thread
		UIUtils.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				PropertyChangeEvent event = new PropertyChangeEvent(getActualStyle(), "refresh", null, null);
				getPropertyChangeSupport().firePropertyChange(event);
			}
		});

	}
	
	/**
	 * Refresh the children of a template sytle by reloading them from the external styles cache
	 */
	public void refreshChildren(){
		JasperReportsConfiguration jConf = getJasperConfiguration();
		IFile project = (IFile) jConf.get(FileUtils.KEY_FILE);
		JRDesignReportTemplate jrTemplate = (JRDesignReportTemplate) getValue();
		getChildren().clear();
		List<JRStyle> styles = ExternalStylesManager.getStyles(jrTemplate, project, jConf);
		
		for (JRStyle s : styles) {
			APropertyNode n = (APropertyNode) ReportFactory.createNode(getActualStyle(), s, -1);
			n.setEditable(false);
		}
		fireChildrenChangeEvent();
	}


	
	/**
	 * Job to update the panel UI when expression text changes or
	 * when caret is moved. This job is supposed to be delayed in order not to call
	 * UI-update events too often (avoiding flickering effects).
	 */
	private class UpdateStyleJob extends Job {
		
		public UpdateStyleJob(){
			super("RefreshStyles");
			setSystem(true);
		}
		
		@Override
		public IStatus run(IProgressMonitor monitor) {	
			refreshChildren();
			monitor.done();
			return Status.OK_STATUS;
		}
	}
	
	/**
	 * This reference, used by some inner class
	 * 
	 * @return this reference
	 */
	private MStyleTemplate getActualStyle(){
		return this;
	}
	
	/**
	 * Create and schedule the background update thread and start it. If there was another
	 * thread created it means that the old one is no more necessary, so it is cancelled
	 */
	private void performUpdate() {
		if (updateStyleJob == null){
			updateStyleJob = new UpdateStyleJob();
		}
		updateStyleJob.cancel();
		updateStyleJob.schedule(UPDATE_DELAY);
	}
	
	/**
	 * Creates the jr template.
	 * 
	 * @return the jR design report template
	 */
	public static JRDesignReportTemplate createJRTemplate() {
		JRDesignReportTemplate jrDesignReportTemplate = new JRDesignReportTemplate();
		return jrDesignReportTemplate;
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MStyles)
			return true;
		return false;
	}

}
