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
package com.jaspersoft.studio.toolbars;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.LineStyleEnum;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.action.border.TemplateBorder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElementLineBox;
import com.jaspersoft.studio.model.MLineBox;
import com.jaspersoft.studio.model.MLinePen;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * Component that represent a combo with inside the preview of some border presets. This component should 
 * be placed in the toolbar when an element that can have borders is selected
 * 
 * @author Orlandin Marco
 *
 */
public class BorderContributionItem extends CommonToolbarHandler {
		/**
		 * The composite that will displayed in the toolbar, it contains a label and the combo
		 */
		private Composite control;
		
		/**
		 * The combo with the border presets inside
		 */
		private TableCombo combo;

		
		/**
		 * The list of available presets
		 */
		private static List<TemplateBorder> exampleImages;
		
		/**
		 * Create some presets
		 */
		static{
			exampleImages = new ArrayList<TemplateBorder>();
			exampleImages.add(new TemplateBorder(null, LineStyleEnum.SOLID));
			exampleImages.add(new TemplateBorder(1f, LineStyleEnum.SOLID));
			exampleImages.add(new TemplateBorder(1f, LineStyleEnum.DASHED));
			exampleImages.add(new TemplateBorder(1f, LineStyleEnum.DOTTED));
			exampleImages.add(new TemplateBorder(1f, LineStyleEnum.DOUBLE));
			exampleImages.add(new TemplateBorder(2f, LineStyleEnum.SOLID));
			exampleImages.add(new TemplateBorder(2f, LineStyleEnum.DASHED));
			exampleImages.add(new TemplateBorder(2f, LineStyleEnum.DOTTED));
			exampleImages.add(new TemplateBorder(2f, LineStyleEnum.DOUBLE));
			exampleImages.add(new TemplateBorder(4f, LineStyleEnum.SOLID));
			exampleImages.add(new TemplateBorder(4f, LineStyleEnum.DASHED));
			exampleImages.add(new TemplateBorder(4f, LineStyleEnum.DOTTED));
			exampleImages.add(new TemplateBorder(4f, LineStyleEnum.DOUBLE));
		};
		
		/**
		 * A listener to uniform in the toolbar change done by the property tab
		 */
		private ModelListener modelListener = new ModelListener();
		
		private APropertyNode showedNode = null;
		
		private class ModelListener implements PropertyChangeListener {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setCorrectValue();
			}
		}
		
		/**
		 * When an element is selected if it use a preset it will be automatically 
		 * selected in the comb. If no preset is used for the element the Custom 
		 * image will be used
		 */
		protected void setCorrectValue(){
			if (combo != null && !combo.isDisposed()){
				TemplateBorder actualBorder = getElementAttributes();
				int index = exampleImages.indexOf(actualBorder);
				if (index != -1) combo.select(index);
				else combo.select(exampleImages.size());
			}
		}
			
		/**
		 * Create a command to change a property. The change is done if the new value of the property
		 * is different from its previous value
		 * @param property the property to change
		 * @param newValue the new value for the property
		 * @param n the element to modify
		 * @return the command
		 */
		protected Command getChangePropertyCommand(Object property, Object newValue, APropertyNode n) {
			Object oldValue = n.getPropertyValue(property);
			if (((oldValue == null && newValue != null) || (oldValue != null && newValue == null) || (newValue != null && !newValue
					.equals(oldValue))) ) {
				SetValueCommand setCommand = new SetValueCommand(n.getDisplayText());
				setCommand.setTarget(n);
				setCommand.setPropertyId(property);
				setCommand.setPropertyValue(newValue);
				return setCommand;
			}
			return null;
		}
		
		/**
		 * For an element change all the properties related to the border: color, style and width
		 * @param cc CommandCompound where all the command to change a single property are putted
		 * @param selectedElement selected preset
		 * @param lp element to change
		 */
		private void changeAllProperties(JSSCompoundCommand cc, TemplateBorder selectedElement, MLinePen lp){
			Command c = getChangePropertyCommand(JRBasePen.PROPERTY_LINE_COLOR, new AlfaRGB(selectedElement.getColor(),255), lp);
			if (c != null) cc.add(c);
			c = getChangePropertyCommand(JRBasePen.PROPERTY_LINE_STYLE, selectedElement.getStyle(), lp);
			if (c != null) cc.add(c);
			c = getChangePropertyCommand(JRBasePen.PROPERTY_LINE_WIDTH, selectedElement.getLineWidth(), lp);
			if (c != null) cc.add(c);
		} 
		
		private TemplateBorder getElementAttribute(String position, MLineBox lb){
			MLinePen lp = (MLinePen) lb.getPropertyValue(position);
			Integer lineStyleNum = ((Integer)lp.getPropertyValue(JRBasePen.PROPERTY_LINE_STYLE))-1;
			LineStyleEnum lineStyle = LineStyleEnum.getByValue(lineStyleNum.byteValue());
			Float lineWidth = (Float)lp.getPropertyValue(JRBasePen.PROPERTY_LINE_WIDTH);
			AlfaRGB lineColor = (AlfaRGB)lp.getPropertyValue(JRBasePen.PROPERTY_LINE_COLOR);
			TemplateBorder result =  new TemplateBorder(lineWidth, lineStyle, lineColor != null ? lineColor.getRgb() : null);
			return result;
		}
		
		/**
		 * Return a TemplateBorder that represent the border of the element selected. If the figure has not
		 * an unique border this method return null
		 * @return
		 */
		private TemplateBorder getElementAttributes(){
			List<Object> selection = getSelectionForType(MGraphicElementLineBox.class);
			if (selection.size() > 0){
				MGraphicElementLineBox model = (MGraphicElementLineBox)selection.get(0);
				MLineBox lb = (MLineBox) model.getPropertyValue(MGraphicElementLineBox.LINE_BOX); 
				TemplateBorder top = getElementAttribute(MLineBox.LINE_PEN_TOP, lb);
				TemplateBorder left = getElementAttribute(MLineBox.LINE_PEN_RIGHT, lb);
				if (!top.equals(left)) return null;
				TemplateBorder right = getElementAttribute(MLineBox.LINE_PEN_RIGHT, lb);
				if (!top.equals(right)) return null;
				TemplateBorder bottom = getElementAttribute(MLineBox.LINE_PEN_BOTTOM, lb);
				if (!top.equals(bottom)) return null;
				return top;
			} else return null;
	}
		
		
		/**
		 * Change the property of all the linepen of an element
		 */
		private void changeProperty() {
			  if (combo.getSelectionIndex()<exampleImages.size()){
					List<Object> selection = getSelectionForType(MGraphicElementLineBox.class);
					JSSCompoundCommand cc = new JSSCompoundCommand("Change border", selection.isEmpty() ? null : (APropertyNode)selection.get(0)); //$NON-NLS-1$
					for(Object obj : selection){
						MGraphicElementLineBox model = (MGraphicElementLineBox)obj;
						TemplateBorder selectedElement = exampleImages.get(combo.getSelectionIndex());
						MLineBox lb = (MLineBox) model.getPropertyValue(MGraphicElementLineBox.LINE_BOX);
						
						MLinePen lp = (MLinePen) lb.getPropertyValue(MLineBox.LINE_PEN_BOTTOM);
						changeAllProperties(cc,selectedElement,lp);
						
						lp = (MLinePen) lb.getPropertyValue(MLineBox.LINE_PEN_LEFT);
						changeAllProperties(cc,selectedElement,lp);
						
						lp = (MLinePen) lb.getPropertyValue(MLineBox.LINE_PEN_RIGHT);
						changeAllProperties(cc,selectedElement,lp);
						
						lp = (MLinePen) lb.getPropertyValue(MLineBox.LINE_PEN_TOP);
						changeAllProperties(cc,selectedElement,lp);
			  	}
					CommandStack cs = getCommandStack();
					cs.execute(cc);
			  }
			  setCorrectValue();
		}


		/**
		 * Crate the  control 
		 * @param parent
		 * @return a composite with a label and the combo preview inside
		 */
		protected Control createControl(Composite parent) {
			super.createControl(parent);
			control = new Composite(parent, SWT.None);
			GridLayout layout = new GridLayout(2,false);
			layout.marginHeight = 0;
			layout.verticalSpacing = 0;
			control.setLayout(layout);
			Label label = new Label(control, SWT.None);
			label.setText(Messages.ATableComboContribution_presets_label);
			combo = new TableCombo(control, SWT.BORDER | SWT.READ_ONLY);
			combo.setEditable(false);
			combo.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						changeProperty();
					}
			});
			
			GridData comboData = new GridData();
			comboData.grabExcessVerticalSpace = true;
			comboData.grabExcessHorizontalSpace = true;
			comboData.widthHint = 130;
			comboData.minimumWidth = 130;
			comboData.minimumHeight = 20;
			combo.setLayoutData(comboData);
			loadImages();
			setAllControlsData();
			return control;
		}
		
		/**
		 * Set the state of the combo according to the borders of the first element
		 * inside the selection
		 */
		protected void setAllControlsData(){
			if (combo == null || combo.isDisposed()) return;
			List<Object> selection = getSelectionForType(MGraphicElementLineBox.class);
			if (selection.size() == 1){
				APropertyNode node = (APropertyNode)selection.get(0);
				setCorrectValue();
				if (showedNode != null) showedNode.getPropertyChangeSupport().removePropertyChangeListener(modelListener);
				showedNode = node;
				showedNode.getPropertyChangeSupport().addPropertyChangeListener(modelListener);
				
			} 
		}
		
		/**
		 * Load the image for every preset and insert them into the combo element
		 */
		private void loadImages() {
			TemplateBorder.setWidth(100);
			for (TemplateBorder border: exampleImages) {
				TableItem ti = new TableItem(combo.getTable(), SWT.READ_ONLY);
				ti.setImage(border.getImage());
			}
			TableItem ti = new TableItem(combo.getTable(), SWT.NONE);
			ti.setImage(TemplateBorder.getCustomImage());
		}		
		
		@Override
		public boolean isVisible() {
			if (!super.isVisible()) return false;
			List<Object> selection = getSelectionForType(MGraphicElementLineBox.class);
			return !selection.isEmpty();
		}
		
		@Override
		public void dispose() {
			super.dispose();
			if (combo != null){
				combo.dispose();
				combo = null;
			}
			if (control != null){
				control.dispose();
				control = null;
			}
			if (showedNode != null) {
				showedNode.getPropertyChangeSupport().removePropertyChangeListener(modelListener);
				showedNode = null;
			}
		}
}
