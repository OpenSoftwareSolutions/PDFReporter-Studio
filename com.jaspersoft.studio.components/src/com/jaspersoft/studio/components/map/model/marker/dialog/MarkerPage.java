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
package com.jaspersoft.studio.components.map.model.marker.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.map.Item;
import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.StandardItem;
import net.sf.jasperreports.components.map.StandardItemProperty;
import net.sf.jasperreports.components.map.StandardMapComponent;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;

import com.buzzcoders.yasw.widgets.map.core.LatLng;
import com.buzzcoders.yasw.widgets.map.core.MapType;
import com.buzzcoders.yasw.widgets.map.ui.MarkersPickupDialog;
import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.MMap;
import com.jaspersoft.studio.components.map.model.marker.MarkersDTO;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.EditButton;
import com.jaspersoft.studio.swt.widgets.table.IEditElement;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.ExpressionInterpreter;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;

public class MarkerPage extends WizardPage {
	private final class EditElement implements IEditElement<Item> {
		@Override
		public void editElement(List<Item> input, int pos) {
			Item v = (Item) input.get(pos);
			if (v == null)
				return;
			v = (Item) v.clone();
			MarkerDialog dialog = new MarkerDialog(Display.getDefault().getActiveShell());
			dialog.setValue((StandardItem) v, expContext);
			if (dialog.open() == Window.OK)
				input.set(pos, v);
		}
	}

	private MarkersDTO value;
	private Table table;
	private TableViewer tableViewer;
	private EditButton<Item> editButton;
	private BasicMapInfo mapInfo;
	private ExpressionContext expContext;

	public MarkersDTO getValue() {
		return new MarkersDTO(value.getMarkers(), value.getPnode());
	}

	@Override
	public void dispose() {
		// clear all properties
		List<Item> props = (List<Item>) tableViewer.getInput();
		value.setMarkers(props);
		super.dispose();
	}

	public void setValue(MarkersDTO value) {
		this.value = value;
		if (table != null)
			fillTable(table);
	}

	protected MarkerPage(String pageName) {
		super(pageName);
		setTitle(Messages.MarkerPage_Title);
		setDescription(Messages.MarkerPage_Description);
	}

	public void createControl(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		setControl(composite);
		
		FormText mapPickSuggestion=new FormText(composite, SWT.NONE);
		mapPickSuggestion.setText(Messages.MarkerPage_0, true, false);
		mapPickSuggestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,2,1));
		mapPickSuggestion.setWhitespaceNormalized(true);
		mapPickSuggestion.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				MarkersPickupDialog staticMarkersDialog = new MarkersPickupDialog(getShell()){
					@Override
					protected void configureShell(Shell newShell) {
						super.configureShell(newShell);
						UIUtils.resizeAndCenterShell(newShell, 800, 600);
					}
				};
				if(mapInfo==null){
					mapInfo=getBasicMapInformation();
				}
				if(mapInfo.getLatitude()!=null && mapInfo.getLongitude()!=null){
					staticMarkersDialog.setInitialPosition(new LatLng(mapInfo.getLatitude(), mapInfo.getLongitude(), true));
				}
				if(mapInfo.getMapType()!=null) {
					staticMarkersDialog.setType(
							MapType.fromStringID(mapInfo.getMapType().getName()));
				}
				if(mapInfo.getZoom()!=0){
					staticMarkersDialog.setZoom(mapInfo.getZoom());
				}
				if(staticMarkersDialog.open()==Window.OK) {
					List<LatLng> markersList = staticMarkersDialog.getMarkersList();
					for(LatLng m : markersList) {
						StandardItem newMarker = new StandardItem();
						newMarker.addItemProperty(new StandardItemProperty("latitude", null, new JRDesignExpression(m.getLat().floatValue()+"f"))); //$NON-NLS-1$ //$NON-NLS-2$
						newMarker.addItemProperty(new StandardItemProperty("longitude", null, new JRDesignExpression(m.getLng().floatValue()+"f"))); //$NON-NLS-1$ //$NON-NLS-2$
						value.getMarkers().add(newMarker);
					}
					tableViewer.setInput(value.getMarkers());
				}
			}
		});
		
		buildTable(composite);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		table.setLayoutData(gd);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				ArrayList<ItemProperty> props = new ArrayList<ItemProperty>();
				props.add(new StandardItemProperty("id", "CHANGE_ME_ID", null)); //$NON-NLS-1$ //$NON-NLS-2$
				props.add(new StandardItemProperty("label", "CHANGE_ME_LABEL", null)); //$NON-NLS-1$ //$NON-NLS-2$
				props.add(new StandardItemProperty("latitude", "0", null)); //$NON-NLS-1$ //$NON-NLS-2$
				props.add(new StandardItemProperty("longitude", "0", null)); //$NON-NLS-1$ //$NON-NLS-2$

				StandardItem v = new StandardItem(props);
				MarkerDialog dialog = new MarkerDialog(Display.getDefault().getActiveShell());
				dialog.setValue(v, expContext);
				if (dialog.open() == Window.OK)
					return v;
				return null;
			}
		});

		editButton = new EditButton<Item>();
		editButton.createEditButtons(bGroup, tableViewer, new EditElement());
		new DeleteButton().createDeleteButton(bGroup, tableViewer);
		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);

		table.setFocus();
		
		if (value.getPnode() instanceof MMap) {
			expContext = ((MMap)value.getPnode()).getMarkersExpressionContext();
		}
	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editButton.push();
			}
		});

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setLabelProvider(new TMarkerLabelProvider());
		// attachCellEditors(tableViewer, table);

		TableColumn[] column = new TableColumn[4];

		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.MarkerPage_IdColumn);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.MarkerPage_LabelColumn);
		
		column[2] = new TableColumn(table, SWT.NONE);
		column[2].setText(Messages.MarkerPage_LatitudeColumn);

		column[3] = new TableColumn(table, SWT.NONE);
		column[3].setText(Messages.MarkerPage_LongitudeColumn);

		fillTable(table);
		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(20, true));
		tlayout.addColumnData(new ColumnWeightData(30, true));
		tlayout.addColumnData(new ColumnWeightData(25, true));
		tlayout.addColumnData(new ColumnWeightData(25, true));
		table.setLayout(tlayout);
	}

	private void fillTable(Table table) {
		List<Item> props = new ArrayList<Item>();
		props.addAll(value.getMarkers());
		tableViewer.setInput(props);
	}

	/*
	 * Gets the basic information: map center, zoom and type.
	 */
	private BasicMapInfo getBasicMapInformation() {
		MMap mapRef = (MMap) value.getPnode();
		BasicMapInfo info = new BasicMapInfo();
		JRDesignDataset dataset = ModelUtils.getDataset(mapRef);
		if(dataset==null){
			dataset = (JRDesignDataset) mapRef.getJasperDesign().getMainDataset();
		}
		ExpressionInterpreter expIntr = 
				new ExpressionInterpreter(dataset, mapRef.getJasperDesign(), mapRef.getJasperConfiguration());
		// Center
		JRDesignExpression latitudeExpr = (JRDesignExpression) mapRef.getPropertyValue(StandardMapComponent.PROPERTY_LATITUDE_EXPRESSION);
		JRDesignExpression longitudeExpr = (JRDesignExpression) mapRef.getPropertyValue(StandardMapComponent.PROPERTY_LONGITUDE_EXPRESSION);
		if(latitudeExpr!=null && longitudeExpr!=null) {
			Object latObj = expIntr.interpretExpression(Misc.nvl(latitudeExpr.getText()));
			Object lngObj = expIntr.interpretExpression(Misc.nvl(longitudeExpr.getText()));
			if(latObj instanceof Number && lngObj instanceof Number) {
				info.setLatitude(((Number)latObj).doubleValue());
				info.setLongitude(((Number)lngObj).doubleValue());
			}
		}
		// Zoom
		JRDesignExpression zoomExpr = (JRDesignExpression) mapRef.getPropertyValue(StandardMapComponent.PROPERTY_ZOOM_EXPRESSION);
		if(zoomExpr!=null) {
			Object zoomObj = expIntr.interpretExpression(zoomExpr.getText());
			if(zoomObj instanceof Number) {
				info.setZoom(((Number) zoomObj).intValue());
			}
			else {
				info.setZoom(MapComponent.DEFAULT_ZOOM);
			}
		}		
		// Map Type
		Integer type = (Integer) mapRef.getPropertyValue(StandardMapComponent.PROPERTY_MAP_TYPE);
		if(type!=null){
			MapTypeEnum typeVal = MapTypeEnum.getByValue(type.byteValue());
			info.setMapType(typeVal);
		}
		return info;
	}
	
	class BasicMapInfo {

		private Double latitude;
		private Double longitude;
		private MapTypeEnum mapType;
		private int zoom;

		public Double getLatitude() {
			return latitude;
		}

		public Double getLongitude() {
			return longitude;
		}

		public MapTypeEnum getMapType() {
			return mapType;
		}

		public int getZoom() {
			return zoom;
		}

		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}

		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}

		public void setMapType(MapTypeEnum mapType) {
			this.mapType = mapType;
		}

		public void setZoom(int zoom) {
			this.zoom = zoom;
		}
	}
}
