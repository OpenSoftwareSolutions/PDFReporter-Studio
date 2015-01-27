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
package com.jaspersoft.studio.jasper;

import java.util.HashSet;
import java.util.Set;

import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.RenderableUtil;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.component.ComponentDesignConverter;
import net.sf.jasperreports.engine.convert.ElementIconConverter;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.JRImageLoader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.jaspersoft.studio.editor.AMultiEditor;
import com.jaspersoft.studio.model.util.KeyValue;
import com.jaspersoft.studio.utils.CacheMap;
import com.jaspersoft.studio.utils.ExpressionUtil;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * 
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: MapDesignConverter.java 5877 2013-01-07 19:51:14Z teodord $
 */
public class MapDesignConverter extends ElementIconConverter implements ComponentDesignConverter {

	public static final Float DEFAULT_LONGITUDE = new Float(12.337967);
	public static final Float DEFAULT_LATITUDE = new Float(45.433967);
	/**
	 *
	 */
	private final static MapDesignConverter INSTANCE = new MapDesignConverter();

	/**
	 *
	 */
	private MapDesignConverter() {
		super(JRImageLoader.COMPONENT_IMAGE_RESOURCE);
	}

	/**
	 *
	 */
	public static MapDesignConverter getInstance() {
		return INSTANCE;
	}

	private CacheMap<JRComponentElement, Renderable> cache = new CacheMap<JRComponentElement, Renderable>(3000000);
	private CacheMap<JRElement, KeyValue<String, Long>> running = new CacheMap<JRElement, KeyValue<String, Long>>(300000);
	private static CacheMap<KeyValue<JasperReportsContext, String>, Renderable> imgCache = new CacheMap<KeyValue<JasperReportsContext, String>, Renderable>(
			300000);

	/**
	 *
	 */
	public JRPrintElement convert(final ReportConverter reportConverter, final JRComponentElement element) {
		MapComponent map = (MapComponent) element.getComponent();
		JRBasePrintImage printImage = new JRBasePrintImage(element.getDefaultStyleProvider());
		Renderable cacheRenderer = getRenderable(reportConverter, element, map, printImage);

		printImage.setUUID(element.getUUID());
		printImage.setX(element.getX());
		printImage.setY(element.getY());
		printImage.setWidth(element.getWidth());
		printImage.setHeight(element.getHeight());
		printImage.setStyle(element.getStyle());
		printImage.setMode(element.getModeValue());
		printImage.setBackcolor(element.getBackcolor());
		printImage.setForecolor(element.getForecolor());
		printImage.setLazy(false);

		// FIXMEMAP there are no scale image, alignment and onError attributes
		// defined for the map element
		printImage.setScaleImage(ScaleImageEnum.CLIP);
		printImage.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
		printImage.setVerticalAlignment(VerticalAlignEnum.TOP);
		printImage.setRenderable(cacheRenderer);
		return printImage;
	}

	protected Renderable getRenderable(final ReportConverter reportConverter, final JRComponentElement element,
			MapComponent map, final JRBasePrintImage printImage) {
		Renderable cacheRenderer = null;
		try {
			cacheRenderer = cache.get(element);
			String expr = "" + element.getWidth() + element.getHeight();
			if (map.getLongitudeExpression() != null)
				expr += map.getLongitudeExpression().getText();
			if (map.getLatitudeExpression() != null)
				expr += map.getLatitudeExpression().getText();
			if (map.getLanguageExpression() != null)
				expr += map.getLanguageExpression().getText();
			if (map.getZoomExpression() != null)
				expr += map.getZoomExpression().getText();
			if (map.getMapType() != null)
				expr += map.getMapType().getName();
			if (map.getMapScale() != null)
				expr += map.getMapScale().getName();
			if (!expr.isEmpty()) {
				KeyValue<String, Long> last = running.get(element);
				Renderable r = null;
				if (cacheRenderer == null) {
					cacheRenderer = getRenderableNoImage(reportConverter.getJasperReportsContext(), map, printImage);
					cache.put(element, cacheRenderer);
					if (last == null)
						r = doFindImage(reportConverter, element, map, expr, cacheRenderer);
				}
				if (last != null && (!last.key.equals(expr)))
					r = doFindImage(reportConverter, element, map, expr, cacheRenderer);
				if (last == null)
					r = doFindImage(reportConverter, element, map, expr, cacheRenderer);
				if (r != null)
					cacheRenderer = r;
			} else {
				running.remove(element);
				cacheRenderer = getRenderableNoImage(reportConverter.getJasperReportsContext(), map, printImage);
				cache.put(element, cacheRenderer);
			}
		} catch (Throwable e) {
			return getRenderableNoImage(reportConverter.getJasperReportsContext(), map, printImage);
		}
		return cacheRenderer;
	}

	protected Renderable doFindImage(final ReportConverter reportConverter, final JRComponentElement element,
			final MapComponent map, final String expr, Renderable cacheRenderer) {
		final JasperReportsConfiguration jrContext = (JasperReportsConfiguration) reportConverter.getJasperReportsContext();
		final KeyValue<JasperReportsContext, String> key = new KeyValue<JasperReportsContext, String>(jrContext, expr);
		Renderable r = imgCache.get(key);
		if (r != null) {
			cache.put(element, r);
			return r;
		}
		imgCache.put(key, cacheRenderer);

		final KeyValue<String, Long> kv = new KeyValue<String, Long>(null, null);
		running.put(element, kv);
		Job job = new Job("load map") {
			protected IStatus run(IProgressMonitor monitor) {
				System.out.println("loading map");
				try {
					JasperDesign jd = jrContext.getJasperDesign();
					JRDataset jrd = jd.getMainDataset();
					JRElementDataset dataset = null;
					if (ModelUtils.getSingleMarkerData(map) != null) {
						dataset = ModelUtils.getSingleMarkerData(map).getDataset();
					}
					if (dataset != null && dataset.getDatasetRun() != null) {
						String dname = dataset.getDatasetRun().getDatasetName();
						if (dname != null)
							jrd = jd.getDatasetMap().get(dname);
					}
					Float latitude = evaluate(map.getLatitudeExpression(), jrd, jrContext, DEFAULT_LATITUDE);
					Float longitude = evaluate(map.getLongitudeExpression(), jrd, jrContext, DEFAULT_LONGITUDE);

					Integer zoom = evaluate(map.getZoomExpression(), jrd, jrContext, MapComponent.DEFAULT_ZOOM);

					String mapType = map.getMapType() != null ? map.getMapType().getName() : MapTypeEnum.ROADMAP.getName();
					String mapScale = map.getMapScale() != null ? map.getMapScale().getName() : MapScaleEnum.ONE.getName();
					String mapFormat = MapImageTypeEnum.PNG.getName();
					String language = evaluate(map.getLanguageExpression(), jrd, jrContext, "");
					String markers = "";

					String imageLocation = "http://maps.google.com/maps/api/staticmap?center=" + (latitude%90) + "," + (longitude%180)
							+ "&size=" + element.getWidth() + "x" + element.getHeight() + "&zoom=" + zoom
							+ (mapType == null ? "" : "&maptype=" + mapType) + (mapFormat == null ? "" : "&format=" + mapFormat)
							+ (mapScale == null ? "" : "&scale=" + mapScale) + markers + "&sensor=false"
							+ (language == null ? "" : "&language=" + language);
					kv.key = expr;
					final Renderable r = RenderableUtil.getInstance(jrContext).getRenderable(imageLocation,
							OnErrorTypeEnum.ERROR, false);
					imgCache.put(key, r);
					r.getImageData(jrContext);
					UIUtils.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							cache.put(element, r);
							kv.value = System.currentTimeMillis();
							AMultiEditor.refresh(jrContext);
						}

					});
					Set<KeyValue<JasperReportsContext, String>> set = new HashSet<KeyValue<JasperReportsContext, String>>();
					for (KeyValue<JasperReportsContext, String> k : set)
						imgCache.get(k);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return Status.OK_STATUS;
			}
		};
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
		return null;
	}

	private <T> T evaluate(JRExpression expr, JRDataset jrd, JasperReportsConfiguration jConfig, T def) {
		if (expr != null) {
			Object l = ExpressionUtil.eval(expr, jrd, jConfig);
			if (l != null)
				try {
					return (T) l;
				} catch (Exception e) {
					return def;
				}
		}
		return def;
	}

	private static Renderable noImage;

	private Renderable getRenderableNoImage(JasperReportsContext jasperReportsContext, MapComponent map,
			JRPrintImage printImage) {
		try {
			printImage.setScaleImage(ScaleImageEnum.CLIP);
			if (noImage == null)
				noImage = RenderableUtil.getInstance(jasperReportsContext).getRenderable(JRImageLoader.NO_IMAGE_RESOURCE,
						map.getOnErrorType(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noImage;
	}
}
