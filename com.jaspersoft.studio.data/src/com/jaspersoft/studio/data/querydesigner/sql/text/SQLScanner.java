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
package com.jaspersoft.studio.data.querydesigner.sql.text;

import java.util.Arrays;
import java.util.List;

/**
 * Class implementing a simple fuzzy scanner for SQL.
 * <p>
 * Clients can easily extends this class and provide similar simple support to
 * other SQL-based language. Usually, they will only need to rewrite the methods
 * {@link #getSQLKeywords()} and {@link #getSQLSymbols()} to provide they stuff
 * additionally or instead of the current specified one.
 * <p>
 * NOTE: Re-used code and idea from JavaViewer SWT Example.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see SQLLineStyler
 */
public class SQLScanner {
	public static final int EOF_CHAR = -1;
	public static final int EOL_CHAR = 10;
	private static List<String> sqlSymbols;

	private StringBuffer fBuffer = new StringBuffer();
	private String fDoc;
	private int fPos;
	private int fEnd;
	private int fStartToken;

	public SQLScanner() {
	}

	/**
	 * Gets next token type in order to decide how to "style it".
	 * 
	 * @return the token type
	 */
	public SQLTokensType nextToken() {
		int c;
		fStartToken = fPos;
		while (true) {
			switch (c = read()) {
			case EOF_CHAR:
				return SQLTokensType.EOF;
			case '/': // comment
				c = read();
				if (c == '/') {
					while (true) {
						c = read();
						if ((c == EOF_CHAR) || (c == EOL_CHAR)) {
							unread(c);
							return SQLTokensType.COMMENT;
						}
					}
				} else {
					unread(c);
				}
				return SQLTokensType.OTHER;
			case '-': // comment
				c = read();
				if (c == '-') {
					while (true) {
						c = read();
						if ((c == EOF_CHAR) || (c == EOL_CHAR)) {
							unread(c);
							return SQLTokensType.COMMENT;
						}
					}
				} else {
					unread(c);
				}
				return SQLTokensType.OTHER;
			case '$':
				c = read();
				SQLTokensType jrbaseExprType = null;
				if (c == 'P') {
					jrbaseExprType = SQLTokensType.JRPARAMETER;
				} else if (c == 'F') {
					jrbaseExprType = SQLTokensType.JRFIELD;
				} else if (c == 'V') {
					jrbaseExprType = SQLTokensType.JRVARIABLE;
				} else {
					break;
				}
				c = read();
				if (c == '{') {
					for (;;) {
						c = read();
						switch (c) {
						case '}':
							return jrbaseExprType;
						case EOF_CHAR:
							unread(c);
							return jrbaseExprType;
						case '\\':
							c = read();
							break;
						}
					}
				}
			case '\'':
				for (;;) {
					c = read();
					switch (c) {
					case '\'':
						return SQLTokensType.QUOTED_LITERAL;
					case EOF_CHAR:
						unread(c);
						return SQLTokensType.QUOTED_LITERAL;
					case '\\':
						c = read();
						break;
					}
				}
			case '"':
				for (;;) {
					c = read();
					switch (c) {
					case '"':
						return SQLTokensType.QUOTED_LITERAL;
					case EOF_CHAR:
						unread(c);
						return SQLTokensType.QUOTED_LITERAL;
					case '\\':
						c = read();
						break;
					}
				}
			case '[':
				for (;;) {
					c = read();
					switch (c) {
					case ']':
						return SQLTokensType.QUOTED_LITERAL;
					case EOF_CHAR:
						unread(c);
						return SQLTokensType.QUOTED_LITERAL;
					case '\\':
						c = read();
						break;
					}
				}
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				do {
					c = read();
				} while (Character.isDigit((char) c));
				unread(c);
				return SQLTokensType.NUMBER;
			default:
				if (getSQLSymbols().contains(Character.toString((char) c))) {
					return SQLTokensType.SYMBOL;
				}
				if (Character.isWhitespace((char) c)) {
					do {
						c = read();
					} while (Character.isWhitespace((char) c));
					unread(c);
					return SQLTokensType.SPACE;
				}
				if (Character.isJavaIdentifierStart((char) c)) {
					fBuffer.setLength(0);
					do {
						fBuffer.append((char) c);
						c = read();
					} while (Character.isJavaIdentifierPart((char) c));
					unread(c);

					if (getSQLKeywords().contains(fBuffer.toString().toLowerCase())) {
						return SQLTokensType.KEYWORD;
					}
					return SQLTokensType.STANDARD_TEXT;
				}
				return SQLTokensType.OTHER;
			}
		}
	}

	private int read() {
		if (fPos <= fEnd) {
			return fDoc.charAt(fPos++);
		}
		return EOF_CHAR;
	}

	private void unread(int c) {
		if (c != EOF_CHAR)
			fPos--;
	}

	public void setRange(String text) {
		fDoc = text;
		fPos = 0;
		fEnd = fDoc.length() - 1;
	}

	public int getStartOffset() {
		return fStartToken;
	}

	public int getLength() {
		return fPos - fStartToken;
	}

	/**
	 * Initializes, if needed, the SQL keywords used by the scanner instance.
	 * 
	 * @return the list of SQL keywords
	 */
	protected List<String> getSQLKeywords() {
		return SQL_KEYWORDS;
	}

	public static final List<String> SQL_KEYWORDS = Arrays.asList(new String[] { "absolute", "action", "add", "after", "all", "allocate", "alter", "and", "any", "are", "array", "as", "asc",
			"asensitive", "assertion", "asymmetric", "at", "atomic", "authorization", "avg", "before", "begin", "between", "bigint", "binary", "bit", "bit_length", "blob", "boolean", "both", "breadth",
			"by", "call", "called", "cascade", "cascaded", "case", "cast", "catalog", "char", "char_length", "character", "check", "clob", "close", "coalesce", "collate", "collate", "collation", "column",
			"commit", "condition", "connect", "constraint", "constraints", "constructor", "contains", "continue", "convert", "corresponding", "count", "create", "cross", "cube", "current", "current_date",
			"current_default_transform_group", "current_path", "current_role", "current_time", "current_timestamp", "current_transform_group_for_type", "current_user", "cursor", "cycle", "data", "date",
			"day", "deallocate", "dec", "decimal", "declare", "default", "deferrable", "deferred", "delete", "depth", "deref", "desc", "describe", "descriptor", "deterministic", "diagnostics",
			"disconnect", "distinct", "do", "domain", "double", "drop", "dynamic", "each", "else", "elseif", "end", "equals", "escape", "except", "exception", "exec", "execute", "exists", "exit",
			"external", "extract", "false", "fetch", "filter", "first", "float", "for", "foreign", "found", "free", "from", "full", "function", "general", "get", "global", "go", "goto", "grant", "group",
			"grouping", "handler", "having", "hold", "hour", "identity", "if", "immediate", "in", "indicator", "initially", "inner", "inout", "input", "insensitive", "insert", "int", "integer",
			"intersect", "interval", "into", "is", "isolation", "iterate", "join", "key", "language", "large", "last", "lateral", "leading", "leave", "left", "level", "like", "local", "localtime",
			"localtimestamp", "locator", "loop", "looplower", "map", "match", "member", "merge", "method", "min", "minute", "modifies", "module", "month", "multiset", "names", "national", "natural",
			"nchar", "nclob", "new", "next", "no", "none", "not", "null", "numeric", "object", "octet_length", "of", "old", "on", "only", "open", "option", "or", "order", "ordinality", "out", "outer",
			"output", "over", "overlaps", "pad", "parameter", "partial", "partition", "path", "position", "precision", "prepare", "preserve", "primary", "prior", "privileges", "procedure", "range", "read",
			"reads", "real", "recursive", "ref", "references", "referencing", "relative", "release", "repeat", "resignal", "restrict", "result", "return", "returns", "revoke", "right", "role", "rollback",
			"rollback", "rollup", "routine", "row", "rows", "savepoint", "schema", "scope", "scroll", "search", "second", "section", "select", "sensitive", "session", "session_user", "set", "sets",
			"signal", "similar", "size", "smallint", "some", "space", "specific", "specifictype", "specifictypesql", "sql", "sqlsqlcode", "sqlerror", "sqlexception", "sqlstate", "sqlwarning", "start",
			"state", "static", "submultiset", "substring", "sum", "symmetric", "system", "system_user", "system_usertable", "table", "temporary", "then", "time", "timestamp", "timezone_hour",
			"timezone_minute", "to", "trailing", "transaction", "translate", "translation", "treat", "trigger", "trim", "true", "under", "undo", "union", "unique", "unknown", "unnest", "until", "update",
			"upper", "usage", "user", "using", "value", "values", "varchar", "varying", "view", "when", "whenever", "where", "while", "window", "with", "within", "without", "work", "write", "year", "zone"

	// until here all SQL 92, 92, 2003 keywords from:
	// http://developer.mimer.com/validator/sql-reserved-words.tml
			});

	/**
	 * Initializes, if needed, the SQL symbols used by the scanner instance.
	 * 
	 * @return the list of SQL symbols
	 */
	protected List<String> getSQLSymbols() {
		if (sqlSymbols == null) {
			sqlSymbols = Arrays.asList(new String[] { ";", "=", "+", "-", "/", "*", "&", "|", "^", "(", ")", "[", "]", ",", ".", ":", "!", "~", "<", ">", "%", "{", "}", "?", "#", "@" });
		}
		return sqlSymbols;
	}

}
