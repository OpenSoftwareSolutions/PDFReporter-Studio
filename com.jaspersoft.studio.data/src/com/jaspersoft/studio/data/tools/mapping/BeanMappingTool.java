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
package com.jaspersoft.studio.data.tools.mapping;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.data.IFieldSetter;
import com.jaspersoft.studio.data.IMappingTool;
import com.jaspersoft.studio.data.messages.Messages;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.swt.widgets.ClassType;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class BeanMappingTool implements IMappingTool {
	private JRDesignDataset dataset;
	private Composite control;
	private org.eclipse.swt.widgets.List methods;
	private PropertyDescriptor[] methodsarray;
	
	/**
	 * Map to bind a primitive type name to its wrapper class name
	 */
	private static HashMap<String, String> primitiveTypeMap = null;
	

	public String getName() {
		return Messages.BeanMappingTool_toolname;
	}

	public Control getControl() {
		return control;
	}
	
	/**
	 * Take a class and return the name of the type. If the name is 
	 * a primitive type, then the name of the wrapper is returned
	 * 
	 * @param propertyType a not null property type
	 * @return a class name
	 */
	private static String getClassName(Class<?> propertyType){
		if (primitiveTypeMap == null){
			primitiveTypeMap = new HashMap<String, String>();
			primitiveTypeMap.put("byte", "java.lang.Byte"); //$NON-NLS-1$ //$NON-NLS-2$
			primitiveTypeMap.put("short", "java.lang.Short"); //$NON-NLS-1$ //$NON-NLS-2$
			primitiveTypeMap.put("int", "java.lang.Integer"); //$NON-NLS-1$ //$NON-NLS-2$
			primitiveTypeMap.put("long", "java.lang.Long"); //$NON-NLS-1$ //$NON-NLS-2$
			primitiveTypeMap.put("float", "java.lang.Float"); //$NON-NLS-1$ //$NON-NLS-2$
			primitiveTypeMap.put("double", "java.lang.Double"); //$NON-NLS-1$ //$NON-NLS-2$
			primitiveTypeMap.put("char", "java.lang.Character"); //$NON-NLS-1$ //$NON-NLS-2$
			primitiveTypeMap.put("boolean", "java.lang.Boolean"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		String className = propertyType.getCanonicalName();
		if (primitiveTypeMap.containsKey(className)) className = primitiveTypeMap.get(className);
		return className;
	}

	public Control createControl(Composite parent) {
		control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout(3, false));

		Label label = new Label(control, SWT.NONE);
		label.setText(Messages.BeanMappingTool_labeltitle);

		final ClassType classType = new ClassType(control, ""); //$NON-NLS-1$
		classType.addListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					errMsg.setText(""); //$NON-NLS-1$
					JasperReportsConfiguration jConfig = dataQueryAdapters
							.getjConfig();

					Class<?> clazz = jConfig.getClassLoader().loadClass(
							classType.getClassType());

					methodsarray = PropertyUtils.getPropertyDescriptors(clazz);

					String[] strm = new String[methodsarray.length];
					for (int i = 0; i < methodsarray.length; i++) {
						strm[i] = methodsarray[i].getName();
					}

					methods.setItems(strm);
				} catch (ClassNotFoundException e1) {
					errMsg.setText(Messages.BeanMappingTool_errormessage
							+ e1.getMessage());
				}
			}
		});

		methods = new org.eclipse.swt.widgets.List(control, SWT.MULTI
				| SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
		methods.setItems(new String[] {});
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		methods.setLayoutData(gd);

		Composite bottomToolbar=new Composite(control,SWT.NONE);
		GridLayout btGL = new GridLayout(3,false);
		btGL.marginWidth=0;
		btGL.marginHeight=0;
		bottomToolbar.setLayout(btGL);
		bottomToolbar.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
		
		Button gfbtn = new Button(bottomToolbar, SWT.PUSH);
		gfbtn.setText(Messages.BeanMappingTool_selectfieldstitle);
		gfbtn.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		gfbtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int[] items = methods.getSelectionIndices();
				if (methodsarray != null && items != null) {
					List<JRDesignField> flist = new ArrayList<JRDesignField>();
					for (int i = 0; i < items.length; i++) {
						JRDesignField f = new JRDesignField();
						f.setName(methodsarray[items[i]].getName());
						Class<?> propertyType = methodsarray[items[i]]
								.getPropertyType();
						if (propertyType != null)
							f.setValueClassName(getClassName(propertyType));
						else
							f.setValueClass(Object.class);
						String description = methodsarray[items[i]]
								.getShortDescription();
						if (description != null)
							f.setDescription(description);
						flist.add(f);
					}
					fsetter.addFields(flist);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		Button clearBtn = new Button(bottomToolbar, SWT.PUSH);
		clearBtn.setText(Messages.BeanMappingTool_17);
		clearBtn.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		clearBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				fsetter.clearFields();
			}
		});

		errMsg = new Label(bottomToolbar, SWT.RIGHT);
		errMsg.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));

		return control;
	}

	public void setFields(List<JRDesignField> fields) {

	}

	public void setJRDataset(JRDesignDataset dataset) {
		this.dataset = dataset;
	}

	public JRDesignDataset getJRDataset() {
		return dataset;
	}

	private IFieldSetter fsetter;
	private Label errMsg;

	public void setFields(IFieldSetter fsetter) {
		this.fsetter = fsetter;
	}

	public void dispose() {

	}

	private DataQueryAdapters dataQueryAdapters;

	public void setParentContainer(DataQueryAdapters dataQueryAdapters) {
		this.dataQueryAdapters = dataQueryAdapters;
	}

}
