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
package com.jaspersoft.studio.data.querydesigner.plsql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jaspersoft.studio.data.querydesigner.sql.SimpleSQLQueryDesigner;
import com.jaspersoft.studio.data.querydesigner.sql.text.SQLLineStyler;
import com.jaspersoft.studio.data.querydesigner.sql.text.SQLScanner;

/**
 * Query designer for PL/SQL language, that simply provides syntax coloring support.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class PLSQLQueryDesigner extends SimpleSQLQueryDesigner {

	private static List<String> plsqlKeywords;
	
	@Override
	protected SQLLineStyler getSQLBasedLineStyler() {
		return new PLSQLLineStyler();
	}

	/*
	 * Class implementing a simple fuzzy scanner for PL/SQL.
	 */
	private class PLSQLScanner extends SQLScanner {
		
		@Override
		protected List<String> getSQLKeywords() {
			if (plsqlKeywords == null) {
				plsqlKeywords=new ArrayList<String>();
				// PL/SQL reserved keywords 
				// see http://download-uk.oracle.com/docs/cd/B10501_01/appdev.920/a96624/f_words.htm#LNPLS019
				plsqlKeywords.addAll(Arrays.asList(new String[] { "zone",
						"year", "write", "work", "with", "while", "where",
						"whenever", "when", "view", "varchar2", "varchar",
						"values", "validate", "use", "update", "unique",
						"union", "type", "true", "trigger", "to",
						"timezone_region", "timezone_minute", "timezone_hour",
						"timezone_abbr", "timestamp", "time", "then", "table",
						"synonym", "successful", "subtype", "start", "sqlerrm",
						"sqlcode", "sql", "space", "smallint", "share", "set",
						"separate", "select", "second", "savepoint", "rowtype",
						"rownum", "rowid", "row", "rollback", "reverse",
						"return", "release", "record", "real", "raw", "range",
						"raise", "public", "procedure", "private", "prior",
						"pragma", "positiven", "positive", "pls_integer",
						"pctfree", "partition", "package", "out", "others",
						"organization", "order", "or", "option", "operator",
						"open", "opaque", "on", "of", "ocirowid",
						"number_base", "number", "null", "nowait", "not",
						"nocopy", "nextval", "new", "naturaln", "natural",
						"month", "mode", "mlslabel", "minute", "minus", "loop",
						"long", "lock", "limited", "like", "level", "java",
						"isolation", "is", "into", "interval", "intersect",
						"interface", "integer", "insert", "indicator", "index",
						"in", "immediate", "if", "hour", "heap", "having",
						"group", "goto", "function", "from", "forall", "for",
						"float", "fetch", "false", "extends", "exit", "exists",
						"execute", "exclusive", "exception", "end", "elsif",
						"else", "drop", "do", "distinct", "desc", "delete",
						"default", "declare", "decimal", "day", "date",
						"cursor", "currval", "current", "create", "constant",
						"connect", "compress", "commit", "comment", "collect",
						"cluster", "close", "check", "char_base", "char",
						"case", "by", "bulk", "boolean", "body",
						"binary_integer", "between", "begin", "authid", "at",
						"asc", "as", "array", "any", "and", "alter", "all" }));

				// SQL functions 
				// see http://download-uk.oracle.com/docs/cd/B10501_01/server.920/a96540/toc.htm  
				// see http://download-uk.oracle.com/docs/cd/B10501_01/server.920/a96540/functions101a.htm#85925
				plsqlKeywords.addAll(Arrays.asList(new String[] {
						"xmltransform", "xmlsequence", "xmlforest",
						"xmlelement", "xmlconcat", "xmlcolattval", "xmlagg",
						"width_bucket", "vsize", "variance", "var_samp",
						"var_pop", "value", "userenv", "user", "upper",
						"updatexml", "unistr", "uid", "tz_offset", "trunc",
						"trim", "treat", "translate", "to_yminterval",
						"to_timestamp_tz", "to_timestamp", "to_single_byte",
						"to_number", "to_nclob", "to_nchar", "to_multi_byte",
						"to_lob", "to_dsinterval", "to_date", "to_clob",
						"to_char", "tanh", "tan", "systimestamp", "sysdate",
						"sys_xmlgen", "sys_xmlagg", "sys_typeid", "sys_guid",
						"sys_extract_utc", "sys_dburigen", "sys_context",
						"sys_connect_by_path", "sum", "substr", "stddev_samp",
						"stddev_pop", "stddev", "sqrt", "soundex", "sinh",
						"sin", "sign", "sessiontimezone", "rtrim", "rpad",
						"rowidtonchar", "rowidtochar", "row_number", "round",
						"replace", "regr_syy", "regr_sxy", "regr_sxx",
						"regr_slope", "regr_r2", "regr_intercept",
						"regr_count", "regr_avgy", "regr_avgx", "reftohex",
						"ref", "rawtonhex", "rawtohex", "ratio_to_report",
						"rank", "power", "percentile_disc", "percentile_cont",
						"percent_rank", "path", "nvl2", "nvl",
						"numtoyminterval", "numtodsinterval", "nullif",
						"ntile", "nlssort", "nls_upper", "nls_lower",
						"nls_initcap", "nls_charset_name", "nls_charset_id",
						"nls_charset_decl_len", "next_day", "new_time", "nchr",
						"months_between", "mod", "min", "max", "make_ref",
						"ltrim", "lpad", "lower", "log", "localtimestamp",
						"ln", "length", "least", "lead", "last_value",
						"last_day", "last", "lag", "instr", "initcap",
						"hextoraw", "grouping_id", "grouping", "group_id",
						"greatest", "from_tz", "floor", "first_value", "first",
						"extractvalue", "extract", "exp", "existsnode",
						"empty_clob", "empty_blob", "dump", "deref", "depth",
						"dense_rank", "decompose", "decode", "dbtimezone",
						"current_timestamp", "current_date", "cume_dist",
						"covar_samp", "covar_pop", "count", "cosh", "cos",
						"corr", "convert", "concat", "compose", "coalesce",
						"chr", "chartorowid", "ceil", "cast", "bitand",
						"bin_to_num", "bfilename", "avg", "atan2", "atan",
						"asin", "asciistr", "ascii", "add_months", "acos",
						"abs" }));

		        // PL/SQL packages 
				// see http://download-uk.oracle.com/docs/cd/B10501_01/appdev.920/a96612/intro2.htm#1025672
				plsqlKeywords.addAll(Arrays.asList(new String[] { "utl_url",
						"utl_tcp", "utl_smtp", "utl_ref", "utl_raw", "utl_pg",
						"utl_inaddr", "utl_http", "utl_file", "utl_encode",
						"utl_coll", "sdo_util", "sdo_tune", "sdo_migrate",
						"sdo_lrs", "sdo_geom", "sdo_cs", "dmbs_xmlquery",
						"dmbs_flashback", "dmbs_defer_sys", "debug_extproc",
						"dbms_xslprocessor", "dbms_xplan", "dbms_xmlschema",
						"dbms_xmlsave", "dbms_xmlparser", "dbms_xmlgen",
						"dbms_xmldom", "dbms_xdbt", "dbms_xdb_version",
						"dbms_xdb", "dbms_wm", "dbms_utility", "dbms_types",
						"dbms_tts", "dbms_transform", "dbms_transaction",
						"dbms_trace", "dbms_strm_a", "dbms_strm",
						"dbms_storage_map", "dbms_stats", "dbms_sql",
						"dbms_space_admin", "dbms_space", "dbms_shared_pool",
						"dbms_session", "dbms_rule_adm", "dbms_rule",
						"dbms_rowid", "dbms_rls", "dbms_resumable",
						"dbms_resource_manager_privs", "dbms_resource_manager",
						"dbms_reputil", "dbms_repcat_rgt",
						"dbms_repcat_instatiate", "dbms_repcat_admin",
						"dbms_repcat", "dbms_repair", "dbms_refresh",
						"dbms_redefinition", "dbms_rectifier_diff",
						"dbms_random", "dbms_propagation_adm", "dbms_profiler",
						"dbms_pipe", "dbms_pclxutil", "dbms_output",
						"dbms_outln_edit", "dbms_outln",
						"dbms_oracle_trace_user", "dbms_oracle_trace_agent",
						"dbms_olap", "dbms_offline_snapshot",
						"dbms_offline_og", "dbms_odci",
						"dbms_obfuscation_toolkit", "dbms_mview",
						"dbms_mgwmsg", "dbms_mgwadm", "dbms_metadata",
						"dbms_logstdby", "dbms_logmnr_d",
						"dbms_logmnr_cdc_subscribe", "dbms_logmnr_cdc_publish",
						"dbms_logmnr", "dbms_lock", "dbms_lob",
						"dbms_libcache", "dbms_ldap", "dbms_job", "dbms_iot",
						"dbms_hs_passthrough", "dbms_fga",
						"dbms_distributed_trust_admin", "dbms_describe",
						"dbms_defer_query", "dbms_defer", "dbms_debug",
						"dbms_ddl", "dbms_capture_adm", "dbms_aw",
						"dbms_aqelm", "dbms_aqadm", "dbms_aq",
						"dbms_apply_adm", "dbms_application_info",
						"dbms_alert", "cwm2_olap_aw_access" }));
				
		        // PL/SQL predefined exceptions 
				// see http://download-uk.oracle.com/docs/cd/B10501_01/appdev.920/a96624/07_errs.htm#784
				plsqlKeywords.addAll(Arrays.asList(new String[] {
						"zero_divide", "value_error", "too_many_rows",
						"timeout_on_resource", "sys_invalid_rowid",
						"subscript_outside_limit", "subscript_beyond_count",
						"storage_error", "self_is_null", "rowtype_mismatch",
						"program_error", "not_logged_on", "no_data_found",
						"login_denied", "invalid_number", "invalid_cursor",
						"dup_val_on_index", "cursor_already_open",
						"collection_is_null", "case_not_found",
						"access_into_null" }));
				
				// Static data dictionary views 
				// see http://download-uk.oracle.com/docs/cd/B10501_01/server.920/a96536/ch2.htm
				plsqlKeywords.addAll(Arrays.asList(new String[] {
				"user_repsites", "user_repschema",
						"user_represolution_statistics",
						"user_represolution_method", "user_represolution",
						"user_represol_stats_control", "user_repprop",
						"user_reppriority_group", "user_reppriority",
						"user_repparameter_column", "user_repobject",
						"user_repkey_columns", "user_repgrouped_column",
						"user_repgroup_privileges", "user_repgroup",
						"user_repgenobjects", "user_repgenerated",
						"user_repflavors", "user_repflavor_objects",
						"user_repflavor_columns", "user_repddl",
						"user_repconflict", "user_repcolumn_group",
						"user_repcolumn", "user_repcatlog",
						"user_repcat_user_parm_values",
						"user_repcat_user_authorizations",
						"user_repcat_template_sites",
						"user_repcat_template_parms",
						"user_repcat_template_objects",
						"user_repcat_refresh_templates", "user_repcat",
						"user_repaudit_column", "user_repaudit_attribute",
						"dba_repsites_new", "dba_repsites", "dba_repschema",
						"dba_represolution_statistics",
						"dba_represolution_method", "dba_represolution",
						"dba_represol_stats_control", "dba_repprop",
						"dba_reppriority_group", "dba_reppriority",
						"dba_repparameter_column", "dba_repobject",
						"dba_repkey_columns", "dba_repgrouped_column",
						"dba_repgroup_privileges", "dba_repgroup",
						"dba_repgenobjects", "dba_repgenerated",
						"dba_repflavors", "dba_repflavor_objects",
						"dba_repflavor_columns", "dba_repextensions",
						"dba_repddl", "dba_repconflict", "dba_repcolumn_group",
						"dba_repcolumn", "dba_repcatlog",
						"dba_repcat_user_parm_values",
						"dba_repcat_user_authorizations",
						"dba_repcat_template_sites",
						"dba_repcat_template_parms",
						"dba_repcat_template_objects",
						"dba_repcat_refresh_templates",
						"dba_repcat_exceptions", "dba_repcat",
						"dba_repaudit_column", "dba_repaudit_attribute",
						"all_repsites", "all_repschema",
						"all_represolution_statistics",
						"all_represolution_method", "all_represolution",
						"all_represol_stats_control", "all_repprop",
						"all_reppriority_group", "all_reppriority",
						"all_repparameter_column", "all_repobject",
						"all_repkey_columns", "all_repgrouped_column",
						"all_repgroup_privileges", "all_repgroup",
						"all_repgenobjects", "all_repgenerated",
						"all_repflavors", "all_repflavor_objects",
						"all_repflavor_columns", "all_repddl",
						"all_repconflict", "all_repcolumn_group",
						"all_repcolumn", "all_repcatlog",
						"all_repcat_user_parm_values",
						"all_repcat_user_authorizations",
						"all_repcat_template_sites",
						"all_repcat_template_parms",
						"all_repcat_template_objects",
						"all_repcat_refresh_templates", "all_repcat",
						"all_repaudit_column", "all_repaudit_attribute"
				}));
			}
			return plsqlKeywords;
		}
		
	}
	
	/*
	 * Line style for PL/SQL language.
	 */
	private class PLSQLLineStyler extends SQLLineStyler {
		@Override
		protected SQLScanner getSQLBasedScanner() {
			return new PLSQLScanner();
		}
	}
	
}
