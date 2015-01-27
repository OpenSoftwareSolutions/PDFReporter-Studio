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
package com.jaspersoft.studio.components.crosstab.model.dialog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.ModeEnum;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.style.ApplyStyleAction;
import com.jaspersoft.studio.model.style.command.CreateStyleCommand;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * This class can be used to apply a TemplateStyle (must be a crosstabStyle) to
 * a crosstab.
 * 
 * 
 * @author Orlandin Marco
 *
 */
public class ApplyCrosstabStyleAction extends ApplyStyleAction {

	/**
	 * The list of style to apply to the crosstab
	 */
	private List<JRDesignStyle> styles = null;
	
	/**
	 * Build the class
	 * 
	 * @param style the crosstab style used to generate the styles
	 * @param element the crosstab to witch the styles will be applied
	 */
	public ApplyCrosstabStyleAction(CrosstabStyle style, JRElement element) {
		super(style, element);
	}
	
	/**
	 * Build the class, instead to build the styles from a CrosstabStyle
	 * it receive directly the list of styles that applied to the crosstab
	 * 
	 * @param styles list of styles that will be applied on the table, the order is important
	 * and it should be: crosstab header, group, total and detail
	 * @param crosstab the crosstab to witch the styles will be applied
	 */
	public ApplyCrosstabStyleAction(List<JRDesignStyle> styles, JRElement crosstab){
		super(null, crosstab);
		this.styles = styles;
	}

	
	/**
	 * Return the correct style for a cell, checking if it the detail cell, an header cell, or a total cell
	 * @param crosstab the crosstab
	 * @param origin the cell origin
	 * @param styleList the list of the available styles
	 * @return the style to apply to the cell
	 */
    private JRDesignStyle getCellBackgroundColor(JRDesignCrosstab crosstab, JRCrosstabOrigin origin, List<JRDesignStyle> styleList) {
        
        int c_index = -1;
        int r_index = -1;
        
        if (origin.getColumnGroupName() != null)
        {
            c_index = (Integer)crosstab.getColumnGroupIndicesMap().get(origin.getColumnGroupName());
            //c_index = (crosstab.getColumnGroupsList().size()-1) - c_index;
        }
        if (origin.getRowGroupName() != null)
        {
            r_index = (Integer)crosstab.getRowGroupIndicesMap().get(origin.getRowGroupName());
            //r_index = (crosstab.getRowGroupsList().size()-1) - r_index;
        }
        
        int groupIndex = Math.max(c_index, r_index);
        
        //groupRowName and groupColName are both null, so it is a detail cell
        if (groupIndex < 0) 
        	return styleList.get(3);
        groupIndex = (c_index == 0) || (r_index == 0) ? 1 : 0;
        switch (origin.getType())
        {
        	
            case JRCrosstabOrigin.TYPE_DATA_CELL:
            {
                return styleList.get(groupIndex+1);
            }
            case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
            {
                return styleList.get(0);
            }
            case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
            {
                return styleList.get(groupIndex+1);
            }
            
        }    
        return null;
    }
    
    /**
     * Return the index of the style that will be applied to a cell of the crosstab
     * 
     * @param crosstab the crosstab
     * @param origin the origin
     * @return the index of the style for the cell where 0=crosstab header, 1=group, 2=total and 3=detail
     */
    private int getBackgroundIndex(JRDesignCrosstab crosstab, JRCrosstabOrigin origin) {
        
        int c_index = -1;
        int r_index = -1;
        
        if (origin.getColumnGroupName() != null)
        {
            c_index = (Integer)crosstab.getColumnGroupIndicesMap().get(origin.getColumnGroupName());
            //c_index = (crosstab.getColumnGroupsList().size()-1) - c_index;
        }
        if (origin.getRowGroupName() != null)
        {
            r_index = (Integer)crosstab.getRowGroupIndicesMap().get(origin.getRowGroupName());
            //r_index = (crosstab.getRowGroupsList().size()-1) - r_index;
        }
        
        int groupIndex = Math.max(c_index, r_index);
        
        //groupRowName and groupColName are both null, so it is a detail cell
        if (groupIndex < 0) 
        	return 3;
        //groupIndex = Math.min(groupIndex, 1);
        groupIndex = (c_index == 0) || (r_index == 0) ? 1 : 0;
        switch (origin.getType())
        {
        	
            case JRCrosstabOrigin.TYPE_DATA_CELL:
            {
                return groupIndex+1;
            }
            case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
            {
                return 0;
            }
            case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
            case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
            {
                return groupIndex+1;
            }
            
        }    
        return -1;
    }
    
	/**
	 * Use the crosstab to rebuild the styles list from it
	 */
    public void rebuildStylesFromCrosstab(){
	    styles = new ArrayList<JRDesignStyle>(Arrays.asList(getStylesFromCrosstab()));
    }
    
	/**
	 * Extract the list of styles actually used on the crosstab 
	 * 
	 * @return the list of styles actually used in the cells of the crosstab in this order 
	 * crosstab header, group, total and detail
	 */
	public JRDesignStyle[] getStylesFromCrosstab(){
		JRDesignCrosstab crosstab = (JRDesignCrosstab)getElement();
    	List<JRDesignCellContents> contents = ModelUtils.getAllCells(crosstab);
    	JRDesignStyle[] stylesArray = new JRDesignStyle[4];
	    for (JRDesignCellContents content : contents)
	    {
	        if (content == null) continue;
	        JRStyle actualStyle = content.getStyle();
	        if (actualStyle != null && actualStyle instanceof JRDesignStyle){
	        	int index = getBackgroundIndex(crosstab, content.getOrigin());
	        	if (index != -1) stylesArray[index] = (JRDesignStyle)actualStyle;
	        }
	    }
	    return stylesArray;
	}
	
    /**
     * Apply the correct style to every cell in the crosstab
     * 
     * @param design the jasper design
     */
	@Override
	public void applayStyle(JasperDesign design) {
		List<JRDesignStyle> styleList = createStyles(design);
		setCellStyles(styleList);
	}
	
	/**
	 * 
	 * Apply the list of styles to the cell of the crosstab. The styles are first set to null and then at
	 * the style value, to force a graphical update (the style are not update if the name is the same)
	 * 
	 * @param styleList list of styles that will be applied on the crosstab, the order is important
	 * and it should be: crosstab header, group, total and detail
	 */
	private void setCellStyles(List<JRDesignStyle> styleList) {
		JRDesignCrosstab crosstab = (JRDesignCrosstab) getElement();
		List<JRDesignCellContents> contents = ModelUtils.getAllCells(crosstab);
		for (JRDesignCellContents content : contents) 
		{
			if (content == null) continue;
			JRDesignStyle style = getCellBackgroundColor(crosstab, content.getOrigin(), styleList);
			if (style != null) {
				try {
					content.setStyle(null);
					content.setStyle(style);
					content.setMode(ModeEnum.OPAQUE);
					// Set the text white if the background color its color is
					// too similar to the background
					Color backGround = style.getBackcolor();
					int luminance = (30 * backGround.getRed() + 59
							* backGround.getGreen() + 11 * backGround.getBlue()) / 255;
					if (luminance < 50) {
						JRElement[] elements = content.getElements();
						for (int i = 0; i < elements.length; ++i) {
							if (elements[i] instanceof JRDesignTextElement) {
								((JRDesignTextElement) elements[i])
										.setForecolor(Color.WHITE);
							}
						}
					}
				} catch (NullPointerException e) {
				}
			}
		}
	}
	
	/**
	 * Update the style of the crosstab with a new CrosstabStyle
	 * 
	 * @param design the JasperDesign of the report
	 * @param newStyles the new style template for the crosstab
	 * @param updateOldStyles true if the new styles will overwrite the old ones, false if the old ones will keep and 
	 * the new ones will have a different name and will be applied to the table with the different name
	 * @param removeOldStyles if updateOldStyles is false, after the new styles are created the old one are deleted. 
	 * if updateOldStyles is true this attribute is ignored
	 */
	public void updateStyle(JasperDesign design, CrosstabStyle newStyles, boolean updatOldStyles, boolean removeOldStyles){
		updateStyle(design, createStyles(design, false), updatOldStyles, removeOldStyles);
	}
	
	/**
	 * Update the style of the crosstab with a new list of styles
	 * 
	 * @param design the JasperDesign of the report
	 * @param newStyles list of styles that will be applied on the crosstab, the order is important
	 * and it should be: crosstab header, group, total and detail.
	 * @param updateOldStyles true if the new styles will overwrite the old ones, false if the old ones will keep and 
	 * the new ones will have a different name and will be applied to the table with the different name
	 * @param removeOldStyles if updateOldStyles is false, after the new styles are created the old one are deleted
	 */
	public void updateStyle(JasperDesign design, List<JRDesignStyle> newStyles, boolean updatOldStyles, boolean removeOldStyles){
		JSSCompoundCommand commands = new JSSCompoundCommand(null);
		if (updatOldStyles){
			JRDesignStyle[] actualStyles = getStylesFromCrosstab();
			for(int i=0; i<actualStyles.length; i++){
				JRDesignStyle style = actualStyles[i];
				if (style != null){
					JRDesignStyle updatedStyle = newStyles.get(i);
					updatedStyle.setName(style.getName());
					design.removeStyle(style.getName());
					commands.add(new CreateStyleCommand(design, updatedStyle));
				}
			}
		} else {
			styles = newStyles;
			Map<String,JRStyle> stylesMap = design.getStylesMap();
			if (removeOldStyles){
				JRDesignStyle[] oldStyles = getStylesFromCrosstab();
				for(JRDesignStyle style : oldStyles){
					if (style != null) design.removeStyle(style);
				}
			}
			for(JRDesignStyle style : newStyles){
				if (style != null && !stylesMap.containsKey(style.getName())) commands.add(new CreateStyleCommand(design, style));
			}
		}
		commands.execute();
		setCellStyles(newStyles);
	}
	
	
	/**
	 * Get a base name and check if  one the composed names of the single styles (basename + _TH or _CD or _TD) are already used
	 * 
	 * @param styleMap the style map
	 * @param baseName the base name
	 * @return true if all the composed names are available, false otherwise
	 */
	private boolean stylePresent(Map<String,JRStyle> styleMap, String baseName){
		return (styleMap.containsKey(baseName+"_CH") || styleMap.containsKey(baseName+"_CG") || styleMap.containsKey(baseName+"_CT") || styleMap.containsKey(baseName+"_CD"));
	}
	

	/**
	 * Starting from a CrosstabStyle it generate a list of styles that will be applied to the crosstab.
	 * It can also add them to the report
	 * 
	 * @param jd the jasperdesign
	 * @param addStylesToReport true if the generated styles will also be added to the report, otherwise false
	 * @return a list of style that can be applied to the crosstab
	 */
	public List<JRDesignStyle> createStyles(JasperDesign jd, boolean addStylesToReport) 
	{
		JSSCompoundCommand commands = new JSSCompoundCommand(null);
		
		CrosstabStyle style = (CrosstabStyle)getStyle();
		String baseName = "Crosstab";
		for (int i = 0;; i++) {
			String name = baseName;
			if (i > 0) {
				name = baseName + " " + i;
			}
	
			if (!(stylePresent(jd.getStylesMap(),name))) {
				baseName = name;
				break;
			}
		}
		
		float gridSize = style.isShowGrid() ? 0.5f : 0f;
		
		List<JRDesignStyle> result = new ArrayList<JRDesignStyle>();
	    JRDesignStyle tableHeaderStyle=  new JRDesignStyle();
	    tableHeaderStyle.setName(baseName + "_CH");
	    setBorderWidth(tableHeaderStyle, gridSize);
	    
	    if (style.getWhiteGrid()) setBorderColor(tableHeaderStyle,Color.white);
	    else setBorderColor(tableHeaderStyle,Color.black);
	    
	    tableHeaderStyle.setMode(ModeEnum.OPAQUE);
	    tableHeaderStyle.setBackcolor(style.getColorValue(CrosstabStyle.COLOR_MEASURES));
	
	    commands.add(new CreateStyleCommand(jd, tableHeaderStyle));
	    result.add(tableHeaderStyle);
	    
	    JRDesignStyle groupStyle =  new JRDesignStyle();
	    groupStyle.setName(baseName + "_CG");
	    setBorderWidth(groupStyle, gridSize);
	
	    if (style.getWhiteGrid()) setBorderColor(groupStyle,Color.white);
	    else setBorderColor(groupStyle,Color.black);
	
	    groupStyle.setMode(ModeEnum.OPAQUE);
	    groupStyle.setBackcolor(style.getColorValue(CrosstabStyle.COLOR_GROUP));
	
	    commands.add(new CreateStyleCommand(jd, groupStyle));
	    result.add(groupStyle);
	
	    JRDesignStyle columnHeaderStyle=  new JRDesignStyle();
	    columnHeaderStyle.setName(baseName + "_CT");
	    setBorderWidth(columnHeaderStyle, gridSize);
	
	    if (style.getWhiteGrid()) setBorderColor(columnHeaderStyle,Color.white);
	    else setBorderColor(columnHeaderStyle,Color.black);
	
	    columnHeaderStyle.setMode(ModeEnum.OPAQUE);
	    columnHeaderStyle.setBackcolor(style.getColorValue(CrosstabStyle.COLOR_TOTAL));
	
	    commands.add(new CreateStyleCommand(jd, columnHeaderStyle));
	    result.add(columnHeaderStyle);
	    
	    JRDesignStyle cellStyle=  new JRDesignStyle();
	    cellStyle.setName(baseName + "_CD");
	    setBorderWidth(cellStyle, gridSize);
	
	    if (style.getWhiteGrid()) setBorderColor(cellStyle,Color.white);
	    else setBorderColor(cellStyle,Color.black);
	
	    cellStyle.setMode(ModeEnum.OPAQUE);
	    cellStyle.setBackcolor(style.getColorValue(CrosstabStyle.COLOR_DETAIL));
	
	    commands.add(new CreateStyleCommand(jd, cellStyle));
	    result.add(cellStyle);
	   
	    if (addStylesToReport) commands.execute();
	    return result;
	}
	
	/**
	 * Starting from a TableStyle it generate a list of styles that will be applied to the table.
	 * For every style generated will be executed an addCommand to add them to the report
	 * 
	 * @param jd the jasperdesign
	 * @return a list of style that can be applied to the table
	 */
	@Override
	public List<JRDesignStyle> createStyles(JasperDesign jd) {
		if (styles == null) return createStyles(jd, true);
		return styles;
	}

}
