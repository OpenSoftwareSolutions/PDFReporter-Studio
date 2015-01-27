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
package com.jaspersoft.studio.model.band.command;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.band.MBandGroup;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.MBandGroupHeader;

/**
 * Move a band next to a specific sibling. The sibling can be null
 * to move the bands on the first position
 * 
 * @author Orlandin Marco
 *
 */
public class ReorderBandCommandBySibling extends Command {

	/** The new index. */
	private JRBand oldUpperBand;
	
	private JRBand newUpperBand;

	/** The jr band. */
	private JRDesignBand jrBand;

	/** The jr design section. */
	private JRDesignSection jrDesignSection;

	/**
	 * Instantiates a new reorder band command.
	 * 
	 * @param child
	 *          the child
	 * @param newIndex
	 *          the new index
	 */
	public ReorderBandCommandBySibling(MBandGroup child, JRBand newUpperBand) {
		super(Messages.common_reorder_elements);

		this.newUpperBand = newUpperBand;
		this.jrDesignSection = (JRDesignSection) child.getSection();
		this.jrBand = (JRDesignBand) child.getValue();
	}

	/**
	 * Instantiates a new reorder band command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderBandCommandBySibling(MBand child, MReport parent, JRBand newUpperBand) {
		super(Messages.common_reorder_elements);

		this.newUpperBand = newUpperBand;
		this.jrDesignSection = (JRDesignSection) parent.getJasperDesign().getDetailSection();
		this.jrBand = (JRDesignBand) child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		doExecute();
	}

	private void doExecute() {
		int newInd = 0;
		List<JRBand> bList = jrDesignSection.getBandsList();
		int oldInd = bList.indexOf(jrBand);
		if (oldInd == 0) oldUpperBand = null;
		else oldUpperBand = bList.get(oldInd-1);
		bList.remove(jrBand);
		if (newUpperBand != null){
			newInd = bList.indexOf(newUpperBand) + 1;
		}
		if (newInd >= 0 && newInd < bList.size())
			bList.add(newInd, jrBand);
		else {
			bList.add(jrBand);
			newInd = bList.size() - 1;
		}
		//This event will not change the listener on the model, but only changes its position
		jrDesignSection.getEventSupport().fireIndexedPropertyChange(MReport.CHANGE_BAND_POSITION, newInd, oldInd, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		List<JRBand> bList = jrDesignSection.getBandsList();
		int oldInd = bList.indexOf(jrBand); 
		int newInd = 0;
		bList.remove(jrBand);
		if (oldUpperBand != null){
			newInd = bList.indexOf(oldUpperBand) + 1;
		}
	
		if (newInd >= 0 && newInd < bList.size())
			bList.add(newInd, jrBand);
		else {
			bList.add(jrBand);
			newInd = bList.size() - 1;
		}
		//This event will not change the listener on the model, but only changes its position
		jrDesignSection.getEventSupport().fireIndexedPropertyChange(MReport.CHANGE_BAND_POSITION, newInd, oldInd, -1);
	}
	
	// STATIC UTILITY METHODS
	
	/**
	 * Generate the commands to move the selected bands into a specific location
	 * 
	 * @param moved list of the bands to move. The list must be not empty and all the bands should have the same type
	 * @param location new position o the bands
	 * @param parentPart root node of the report tree, used to refresh its children
	 * @return a list of command to move the selected bands
	 */
	public static JSSCompoundCommand moveBandsCommand(List<MBand> moved, JRBand upperlocation, final EditPart parentPart) {
		final ANode report = moved.get(0).getParent();
		
		/**
		 * Customized compound command that after the execute and undo force a refresh of the editor and outline
		 */
		JSSCompoundCommand cmd = new JSSCompoundCommand(report) {

			// I create a fake command to force the refresh of the editor and outline panels
			protected void refreshVisuals() {
				PropertyChangeEvent event = new PropertyChangeEvent(report.getJasperDesign(), "refresh", null, null);
				report.propertyChange(event);
				for (Object part : parentPart.getChildren()) {
					((EditPart) part).refresh();
				}
			}

			private void refreshBandNumbers() {
			}

			@Override
			public void execute() {
				super.execute();
				refreshBandNumbers();
				refreshVisuals();
			}

			@Override
			public void undo() {
				super.undo();
				refreshBandNumbers();
				refreshVisuals();
			}
		};

		//Precondition: moved is not empty and all its contents has the same type
		MBand firstBand = moved.get(0);
		if (BandTypeEnum.GROUP_HEADER.equals(firstBand.getBandType())){
			for (MBand bandNode : moved) {
				cmd.add(new ReorderBandCommandBySibling((MBandGroup) bandNode, upperlocation));
				upperlocation = bandNode.getValue();
			}
		} else if (BandTypeEnum.GROUP_FOOTER.equals(firstBand.getBandType())){
			for (MBand bandNode : moved) {
				cmd.add(new ReorderBandCommandBySibling((MBandGroup) bandNode, upperlocation));
				upperlocation = bandNode.getValue();
			}
		} else if (BandTypeEnum.DETAIL.equals(firstBand.getBandType())){
			for (MBand bandNode : moved) {
				cmd.add(new ReorderBandCommandBySibling(bandNode, (MReport) bandNode.getParent(), upperlocation));
				upperlocation = bandNode.getValue();
			}
		}
		
		return cmd;
	}
	
	/**
	 * Check if the selected elements are valid for a drag and drop operation of the band. To be
	 * valid the selection must:
	 * -Contain only bands
	 * -The bands must be or Detail or group header or group footer
	 * -All the bands must have the same type
	 * -If the band are group header or group footer then they must be in the same group
	 * 
	 * Then return the  type of the movement if the selection is valid or null otherwise.
	 * 
	 * @param selectedElements the selected elements
	 * @param movedBands list where the models of the band will be placed, this is done to speed up 
	 * the computation since will be time loosing cast the reference of the band more then on type
	 * @return the type of the movement, it could be Detail, Group_Header or Group_footer if the 
	 * drag operation is moving this type of bands. It will be null if the selection is not valid.
	 */
	public static BandTypeEnum getMoveType(List<?> selectedElements, List<MBand> movedBands){
		BandTypeEnum lastFound = null;
		movedBands.clear();
		for(Object node : selectedElements){
			if (node instanceof EditPart) {
				node = ((EditPart) node).getModel();
			}
			if (node instanceof MBand){
				MBand band = (MBand)node;
				movedBands.add(band);
				BandTypeEnum bandType = band.getBandType();
				if (BandTypeEnum.DETAIL.equals(bandType) || BandTypeEnum.GROUP_FOOTER.equals(bandType) || BandTypeEnum.GROUP_HEADER.equals(bandType)){
					if (lastFound == null) lastFound = bandType;
					else if (!lastFound.equals(bandType)) {
						//The selection is not omogeneus, the drag can not be completed
						return null;
					}
				} else {
					//There is a not valid type selected, the drag can not be completed
					return null;
				}
			} else {
				//The selected element is null, the drag can not be completed
				return null;
			}
		}
		if (BandTypeEnum.GROUP_FOOTER.equals(lastFound)){
			JRDesignGroup lastGroup = null;
			for(MBand band : movedBands){
				MBandGroupFooter footer = (MBandGroupFooter)band;
				JRDesignGroup bandGroup = footer.getJrGroup();
				if (lastGroup == null) lastGroup = bandGroup;
				else if (lastGroup != bandGroup){
					//The selection is not in the same group, the drag can not be completed
					return null;
				}
			}
		}	else if (BandTypeEnum.GROUP_HEADER.equals(lastFound)){
			JRDesignGroup lastGroup = null;
			for(MBand band : movedBands){
				MBandGroupHeader footer = (MBandGroupHeader)band;
				JRDesignGroup bandGroup = footer.getJrGroup();
				if (lastGroup == null) lastGroup = bandGroup;
				else if (lastGroup != bandGroup){
					//The selection is not in the same group, the drag can not be completed
					return null;
				}
			}
		}
		return lastFound;
	}

}
