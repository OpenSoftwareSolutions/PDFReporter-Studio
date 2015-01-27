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
package com.jaspersoft.studio.components.table.model.dialog;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.components.table.model.dialog.TableStyle.BorderStyleEnum;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.figures.borders.ShadowBorder;
import com.jaspersoft.studio.editor.java2d.J2DLightweightSystem;
import com.jaspersoft.studio.property.color.ColorSchemaGenerator;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * Generate a preview of a table with a table style applied on it
 * 
 * @author Orlandin Marco & Giulio Toffoli
 *
 */
public class TableStylePreview extends Composite {
	
	/**
	 * The style of the table
	 */
	private TableStyle tableStyle;
	
	/**
	 * The parent figure
	 */
	private Figure parentFigure;
	
	/**
	 * The area where the table will be inserted
	 */
	private Canvas square;
	
	/**
	 * Figure where the table will be painted
	 */
	private RectangleFigure borderPreview;
	
	private J2DLightweightSystem lws;
	
	/**
	 * Create a preview with a default table style
	 *
	 * @param parent
	 * @param style
	 */
	public TableStylePreview(Composite parent, int style){
		super(parent, style);
		AlfaRGB baseColor = AlfaRGB.getFullyOpaque(ColorConstants.lightBlue.getRGB());
		AlfaRGB borderColor = AlfaRGB.getFullyOpaque(ColorConstants.black.getRGB());
		tableStyle = new TableStyle(baseColor, ColorSchemaGenerator.SCHEMAS.DEFAULT,BorderStyleEnum.FULL,borderColor ,false);
		createFigure();
	}
	
	public TableStylePreview(Composite parent, int style, TableStyle tableStyle) {
		super(parent, style);
		this.tableStyle = tableStyle;
		createFigure();
	}
	
	/**
	 * Set the table style and redraw the preview image
	 * 
	 * @param style the new table style
	 */
	public void setTableStyle(TableStyle style){
		tableStyle = style;
		setTBounds();
	}
	


	private void createFigure(){
		setLayout(new GridLayout(1,false));
		lws = new J2DLightweightSystem();
		square = new Canvas(this, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		square.setLayoutData(gd);
		lws.setControl(square);
		parentFigure = new Figure();
		parentFigure.setLayoutManager(new XYLayout());
		lws.setContents(parentFigure);
		
		borderPreview = new RectangleFigure() {

			@Override
			public void paint(Graphics graphics) {
				int y = 5;
			    int x = 5;
			    int w = getBounds().width -10;
			    int h = getBounds().height-10;
		        int rowHeight = h/7;
		        Rectangle row_bounds = new Rectangle(x,y + rowHeight*2, w, rowHeight);
		        Graphics2D g = ComponentFigure.getG2D(graphics);
		        
		        Color colorDetail =  tableStyle.getColorValue(TableStyle.STANDARD_COLOR_DETAIL);
		        
		        g.setColor(colorDetail);
			    g.fillRect(row_bounds.x, row_bounds.y, row_bounds.width, row_bounds.height);
			    Color c = null;
			    row_bounds = new Rectangle(x,y + rowHeight*3, w, rowHeight);
			    g.setColor(colorDetail);
			    if (tableStyle.hasAlternateColor())
			    {
			    	c = tableStyle.getColorValue(TableStyle.COLOR_DETAIL);
			        g.setColor(c);
			    }
			    g.fill(row_bounds);
			    row_bounds = new Rectangle(x,y + rowHeight*4, w, rowHeight);
			    g.setColor(colorDetail);
			    g.fill(row_bounds);

			    // TABLE HEADER
			    row_bounds = new Rectangle(x,y + rowHeight*0, w, rowHeight);
			    c = tableStyle.getColorValue(TableStyle.COLOR_TABLE_HEADER);
			    g.setColor(c);
			    g.fill(row_bounds);

			    // TABLE FOOTER
			    row_bounds = new Rectangle(x,y + rowHeight*6, w, rowHeight);
			    g.setColor(c);
			    g.fill(row_bounds);


			    // COLUMN HEADER
			    row_bounds = new Rectangle(x,y + rowHeight*1, w, rowHeight);
			    c = tableStyle.getColorValue(TableStyle.COLOR_COL_HEADER);
			    g.setColor(c);
			    g.fill(row_bounds);

			    // COLUMN FOOTER
			    row_bounds = new Rectangle(x,y + rowHeight*5, w, rowHeight);
			    g.setColor(c);
			    g.fill(row_bounds);


			    g.setColor(tableStyle.getBorderColor());
			    // Draw border...
			    for (int i=0; i<8; ++i)
			    {
			    	g.drawLine(x, y+rowHeight*i, x+w, y+rowHeight*i);
			    }

			    h = rowHeight*7;
			    if (tableStyle.getBorderStyle() == BorderStyleEnum.FULL)
			    {
			        for (int i=0; i<3; ++i)
			        {
			            g.drawLine(x+(i*(w/3)), y, x+(i*(w/3)), y+h);
			        }
			        g.drawLine(x+w, y, x+w, y+h-1);
			    }
			    if (tableStyle.getBorderStyle() == BorderStyleEnum.ONLY_HORIZONTAL)
			    {
			        g.drawLine(x, y, x, y+h);
			        g.drawLine(x+w, y, x+w, y+h-1);
			    }
			}
		};
		borderPreview.setBorder(new ShadowBorder());
		parentFigure.add(borderPreview);	
		addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				setTBounds();
			}
		});
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				setTBounds();
			}
		});
	}
	
	/**
	 * Set the size of the preview area and request a redraw
	 */
	public void setTBounds() {
		if (!isDisposed()) {
			Dimension psize = parentFigure.getSize();
			borderPreview.setSize(psize);
			borderPreview.setLocation(new Point(0,0));
			parentFigure.invalidate();

			square.redraw();
			lws.getUpdateManager().performUpdate();
		}
	}

}
