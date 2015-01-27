package com.jaspersoft.studio.server;

import java.io.IOException;

import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.w3c.tools.codec.Base64Decoder;
import org.w3c.tools.codec.Base64Encoder;
import org.w3c.tools.codec.Base64FormatException;

import com.jaspersoft.studio.prm.ParameterSetProvider;
import com.jaspersoft.studio.prm.ParameterSet;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JRSBuiltInParameterProvider {
	public static final String PARAMETERSET_JRS = "Jaspersoft Server Built In Parameters";

	public static void init() {
		ScopedPreferenceStore pstore = JasperReportsConfiguration.getDefaultInstance().getPrefStore();
		ParameterSet pset = ParameterSetProvider.getParameterSet(PARAMETERSET_JRS, pstore);
		if (pset == null) {
			pset = new ParameterSet();
			pset.setName(PARAMETERSET_JRS);
			pset.setBuiltIn(true);
			JRDesignParameter p = new JRDesignParameter();
			p.setName("LoggedInUser");
			p.setDescription("Not usable in query input control, but is used as parameter to report");
			p.setValueClassName("com.jaspersoft.jasperserver.api.metadata.user.domain.client.UserImpl");
			p.setDefaultValueExpression(new JRDesignExpression("new com.jaspersoft.jasperserver.api.metadata.user.domain.client.UserImpl()"));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUsername");
			p.setDescription("Of logged in user");
			p.setValueClassName("java.lang.String");
			p.setDefaultValueExpression(new JRDesignExpression("\"joeuser\""));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserFullName");
			p.setDescription("Of logged in user");
			p.setValueClassName("java.lang.String");
			p.setDefaultValueExpression(new JRDesignExpression("\"Joe Smith\""));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserEmailAddress");
			p.setDescription("Of logged in user");
			p.setValueClassName("java.lang.String");
			p.setDefaultValueExpression(new JRDesignExpression("\"joe@some.email\""));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserEnabled");
			p.setDescription("Is logged in user enabled?");
			p.setValueClassName("java.lang.Boolean");
			p.setDefaultValueExpression(new JRDesignExpression("Boolean.TRUE"));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserExternallyDefined");
			p.setDescription("Is logged in user externally defined? ie. authenticated externally");
			p.setValueClassName("java.lang.Boolean");
			p.setDefaultValueExpression(new JRDesignExpression("Boolean.FALSE"));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserTenantId");
			p.setDescription("Of logged in user. Only relevant in Pro/Enterprise.");
			p.setValueClassName("java.lang.String");
			p.setDefaultValueExpression(new JRDesignExpression(""));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserRoles");
			p.setDescription("Current set of roles of logged in user. Useful for $X parameter");
			p.setValueClassName("java.util.Collection");
			p.setNestedTypeName("java.util.String");
			p.setDefaultValueExpression(new JRDesignExpression("new ArrayList()"));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserAttributes");
			p.setDescription("Not usable in query input control, but is used as parameter to report. Empty map if no attributes");
			p.setValueClassName("java.util.Map");
			p.setNestedTypeName("java.util.String");
			p.setDefaultValueExpression(new JRDesignExpression("new HashMap()"));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserAttributeNames");
			p.setDescription("User profile attribute names. Useful for $X parameters. Empty collection if no attributes");
			p.setValueClassName("java.util.Collection");
			p.setNestedTypeName("java.util.String");
			p.setDefaultValueExpression(new JRDesignExpression("new ArrayList()"));
			p.setForPrompting(false);

			pset.getParameters().add(p);

			p = new JRDesignParameter();
			p.setName("LoggedInUserAttributeValues");
			p.setDescription("");
			p.setValueClassName("java.util.Collection");
			p.setDefaultValueExpression(new JRDesignExpression("new ArrayList()"));
			p.setForPrompting(false);

			pset.getParameters().add(p);
			ParameterSetProvider.storeParameterSet(pset, pstore);
		}
		try {
			String str = pstore.getString(ParameterSet.PARAMETER_SETS);
			if (str != null) {
				try {
					str = new Base64Decoder(str).processString();
				} catch (Base64FormatException e) {
					e.printStackTrace();
					return;
				}
				String[] sets = str.split("\n");
				for (String key : sets) {
					if (key.equals(PARAMETERSET_JRS))
						return;
				}
			}
			str = PARAMETERSET_JRS + (str == null ? "" : "\n" + str);
			pstore.setValue(ParameterSet.PARAMETER_SETS, new Base64Encoder(str).processString());
		} finally {
			try {
				pstore.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
