package org.eclipse.babel.core.configuration;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.babel.editor.preferences.MsgEditorPreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;


public class BasePreferences implements IConfiguration {

    public static final String AUDIT_SAME_VALUE = "auditSameValue";
    public static final String AUDIT_UNSPEZIFIED_KEY = "auditMissingValue";
    public static final String AUDIT_MISSING_LANGUAGE = "auditMissingLanguage";
    public static final String AUDIT_RB = "auditResourceBundle";
    public static final String AUDIT_RESOURCE = "auditResource";

    public static final String NON_RB_PATTERN = "NoRBPattern";
    
    private static final String DELIMITER = ";";
    private static final String ATTRIBUTE_DELIMITER = ":";

    private static final IEclipsePreferences PREF = MsgEditorPreferences.getEclipsePreferenceStore();
    
    private static class CheckItem {
        boolean checked;
        String name;

        public CheckItem(String item, boolean checked) {
            this.name = item;
            this.checked = checked;
        }

        public String getName() {
            return name;
        }
    }
    
    @Override
    public boolean getAuditSameValue() {
        return PREF.getBoolean(AUDIT_SAME_VALUE, false);
    }

    @Override
    public boolean getAuditMissingValue() {
        return PREF.getBoolean(AUDIT_UNSPEZIFIED_KEY, true);
    }

    @Override
    public boolean getAuditMissingLanguage() {
        return PREF.getBoolean(AUDIT_MISSING_LANGUAGE, true);
    }

    @Override
    public boolean getAuditRb() {
        return PREF.getBoolean(AUDIT_RB, true);
    }

    @Override
    public boolean getAuditResource() {
        return PREF.getBoolean(AUDIT_RESOURCE, true);
    }

    @Override
    public String getNonRbPattern() {
        return PREF.get(NON_RB_PATTERN, getDefaultNonRbPattern());
    }  
    
    public static String convertListToString(List<CheckItem> patterns) {
        StringBuilder sb = new StringBuilder();
        int tokenCount = 0;

        for (CheckItem s : patterns) {
            sb.append(s.getName());
            sb.append(ATTRIBUTE_DELIMITER);
            if (s.checked) {
                sb.append("true");
            } else {
                sb.append("false");
            }

            if (++tokenCount != patterns.size()) {
                sb.append(DELIMITER);
            }
        }
        return sb.toString();
    }
    
    private String getDefaultNonRbPattern(){
    	List<CheckItem> patterns = new LinkedList<CheckItem>();
    	patterns.add(new CheckItem("^(.)*/build\\.properties", true));
    	patterns.add(new CheckItem("^(.)*/config\\.properties", true));
    	patterns.add(new CheckItem("^(.)*/targetplatform/(.)*", true));
    	return convertListToString(patterns);
    }
    
    // ResourceBundle-Settings
   
}
