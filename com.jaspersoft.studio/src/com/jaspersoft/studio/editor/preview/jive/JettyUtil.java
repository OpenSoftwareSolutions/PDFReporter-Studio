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
package com.jaspersoft.studio.editor.preview.jive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.eclipse.ui.ReportPreviewUtil;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.servlets.JasperPrintAccessor;
import net.sf.jasperreports.web.servlets.ReportActionServlet;
import net.sf.jasperreports.web.servlets.ReportContextCreatorServlet;
import net.sf.jasperreports.web.servlets.ReportJiveComponentsServlet;
import net.sf.jasperreports.web.servlets.ReportOutputServlet;
import net.sf.jasperreports.web.servlets.ReportPageStatusServlet;
import net.sf.jasperreports.web.servlets.RequirejsConfigServlet;
import net.sf.jasperreports.web.servlets.ResourceServlet;
import net.sf.jasperreports.web.servlets.ViewerServlet;
import net.sf.jasperreports.web.util.WebUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.launching.SocketUtil;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.jaspersoft.studio.editor.preview.jive.servlet.SResourceServlet;
import com.jaspersoft.studio.preferences.GlobalPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperDesignPreviewView.java 27 2009-11-11 12:40:27Z teodord $
 */
public final class JettyUtil {
	private static Server server;
	private static Map<IProject, List<Handler>> hmap = new HashMap<IProject, List<Handler>>();
	private static int port = 8888;
	private static ContextHandlerCollection contextHandlerCollection;
	public static String PRM_JSSContext = "jss_context";
	public static String PRM_JRPARAMETERS = "prm_in";
	public static String PRM_JASPERREPORT = "jasperreport";

	public static void startJetty(IProject project, JasperReportsConfiguration jContext) {
		try {
			if (server == null) {
				Integer p = jContext.getPropertyInteger(GlobalPreferencePage.JSS_JETTY_PORT);
				if (p == null || p.intValue() <= 0)
					port = SocketUtil.findFreePort();
				else
					port = p;

				if (port == -1)
					port = 8888;
				server = new Server(port);
				HandlerCollection hc = new HandlerCollection();
				contextHandlerCollection = new ContextHandlerCollection();
				hc.setHandlers(new Handler[] { contextHandlerCollection });
				server.setHandler(hc);

				server.start();

			}
			if (hmap.get(project) == null) {

				// server.stop();

				List<Handler> handlers = createContext(project, jContext);
				hmap.put(project, handlers);
				for (Handler h : handlers) {
					contextHandlerCollection.addHandler(h);
					h.start();
				}
			}
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
	}

	public static String getURL(IFile file, String uuid, JasperReportsConfiguration jContext) {
		String ctxName = file.getProject().getName();
		String prjRelPath = file.getProjectRelativePath().toString();
		return String.format("http://localhost:%d/%s/servlets/viewer?" + WebUtil.REQUEST_PARAMETER_REPORT_URI
				+ "=%s&%s=%s&" + WebUtil.REQUEST_PARAMETER_ASYNC_REPORT + "=true", port, ctxName, prjRelPath, PRM_JSSContext,
				uuid);
	}

	private static List<Handler> createContext(IProject project, final JasperReportsConfiguration jContext) {
		List<Handler> handlers = new ArrayList<Handler>();
		// final String waFolder = project.getLocation().toOSString() + "/";

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/" + project.getName());
		context.setClassLoader(ReportPreviewUtil.createProjectClassLoader(project));

		// context.addServlet(new ServletHolder(DiagnosticServlet.class), "/servlets/diag");

		ServletHolder rs = new ServletHolder(new ResourceServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		});
		context.addServlet(rs, "/servlets/resources/*");

		rs = new ServletHolder(new ResourceServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		});
		context.addServlet(rs, "/servlets/image");

		context.addServlet(new ServletHolder(new SResourceServlet()), "/scripts/*");
		context.addServlet(new ServletHolder(new SResourceServlet()), "/jquery/*");
		context.addServlet(new ServletHolder(new SResourceServlet()), "/javascript/*");
		context.addServlet(new ServletHolder(new SResourceServlet()), "/images/*");

		rs = new ServletHolder(new ViewerServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		});
		rs.setInitParameter("net.sf.jasperreports.web.servlets.viewer.header.template", "viewer/CustomHeaderTemplate.vm");
		rs.setInitParameter("net.sf.jasperreports.web.servlets.viewer.body.template", "viewer/CustomBodyTemplate.vm");
		rs.setInitParameter("net.sf.jasperreports.web.servlets.viewer.footer.template", "viewer/CustomFooterTemplate.vm");
		context.addServlet(rs, "/servlets/myviewer");

		rs = new ServletHolder(new ViewerServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		});
		context.addServlet(rs, "/servlets/viewer");

		context.addServlet(new ServletHolder(new ReportContextCreatorServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}

			@Override
			protected void initWebContext(HttpServletRequest request, WebReportContext webReportContext) {
				Map<String, Object> prm = webReportContext.getParameterValues();

				String jsskey = request.getParameter(PRM_JSSContext);
				Map<String, Object> cprm = Context.getContext(jsskey);
				if (cprm != null) {
					Object das = cprm.get(PRM_JRPARAMETERS);
					if (das != null)
						prm.putAll((Map<String, Object>) das);

					JasperDesignCache cache = JasperDesignCache.getInstance(getJasperReportsContext(), webReportContext);

					JasperPrintAccessor jasperPrintAccessor = (JasperPrintAccessor) webReportContext
							.getParameterValue(WebReportContext.REPORT_CONTEXT_PARAMETER_JASPER_PRINT_ACCESSOR);

					JRPropertiesUtil propUtil = JRPropertiesUtil.getInstance(getJasperReportsContext());
					// FIXME - after JR Team refactor to JIVE use a constant in WebUtil class
					String runReportParamName = propUtil.getProperty(JRPropertiesUtil.PROPERTY_PREFIX
							+ "web.request.parameter.run.report");
					String runReport = request.getParameter(runReportParamName);
					if (jasperPrintAccessor == null || Boolean.valueOf(runReport)) {
						// FIXME - after JR Team refactor to JIVE use a constant in WebUtil
						// class
						// String reportUriParamName = propUtil.getProperty(JRPropertiesUtil.PROPERTY_PREFIX
						// + "web.request.parameter.report.uri");
						String reportUri = request.getParameter(WebUtil.REQUEST_PARAMETER_REPORT_URI);

						cache.set(reportUri, (JasperReport) cprm.get(PRM_JASPERREPORT));
					}
					// Context.unsetContext(jsskey);

				}
			}
		}), "/servlets/reportcontext");

		context.addServlet(new ServletHolder(new ReportOutputServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		}), "/servlets/reportoutput");

		context.addServlet(new ServletHolder(new ReportPageStatusServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		}), "/servlets/reportpagestatus");

		context.addServlet(new ServletHolder(new ReportActionServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		}), "/servlets/reportaction");

		context.addServlet(new ServletHolder(new ReportJiveComponentsServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		}), "/servlets/reportcomponents");

		context.addServlet(new ServletHolder(new RequirejsConfigServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		}), "/servlets/requirejsconfig");

		ServletHolder reportServletHolder = new ServletHolder(new ReportOutputServlet() {
			private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public JasperReportsContext getJasperReportsContext() {
				return jContext;
			}
		});
		context.addServlet(reportServletHolder, "/servlets/report");

		handlers.add(context);
		return handlers;
	}

	public static void stopJetty(IProject project) {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				throw new JRRuntimeException(e);
			}

			server = null;
		}
	}

	public static void restartJetty(IProject project, JasperReportsConfiguration jContext) {
		stopJetty(project);
		startJetty(project, jContext);
	}

}
