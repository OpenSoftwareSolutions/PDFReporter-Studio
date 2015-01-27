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
package com.jaspersoft.studio.utils.encoding;

import java.util.HashMap;
import java.util.Map;

public class EncodingMap {

	/** fIANA2JavaMap */
	protected final static Map<String, String> fIANA2JavaMap = new HashMap<String, String>();

	/** fJava2IANAMap */
	protected final static Map<String, String> fJava2IANAMap = new HashMap<String, String>();

	static {

		// add IANA to Java encoding mappings.
		fIANA2JavaMap.put("BIG5", "Big5");
		fIANA2JavaMap.put("CSBIG5", "Big5");
		fIANA2JavaMap.put("CP037", "CP037");
		fIANA2JavaMap.put("IBM037", "CP037");
		fIANA2JavaMap.put("CSIBM037", "CP037");
		fIANA2JavaMap.put("EBCDIC-CP-US", "CP037");
		fIANA2JavaMap.put("EBCDIC-CP-CA", "CP037");
		fIANA2JavaMap.put("EBCDIC-CP-NL", "CP037");
		fIANA2JavaMap.put("EBCDIC-CP-WT", "CP037");
		fIANA2JavaMap.put("IBM273", "CP273");
		fIANA2JavaMap.put("CP273", "CP273");
		fIANA2JavaMap.put("CSIBM273", "CP273");
		fIANA2JavaMap.put("IBM277", "CP277");
		fIANA2JavaMap.put("CP277", "CP277");
		fIANA2JavaMap.put("CSIBM277", "CP277");
		fIANA2JavaMap.put("EBCDIC-CP-DK", "CP277");
		fIANA2JavaMap.put("EBCDIC-CP-NO", "CP277");
		fIANA2JavaMap.put("IBM278", "CP278");
		fIANA2JavaMap.put("CP278", "CP278");
		fIANA2JavaMap.put("CSIBM278", "CP278");
		fIANA2JavaMap.put("EBCDIC-CP-FI", "CP278");
		fIANA2JavaMap.put("EBCDIC-CP-SE", "CP278");
		fIANA2JavaMap.put("IBM280", "CP280");
		fIANA2JavaMap.put("CP280", "CP280");
		fIANA2JavaMap.put("CSIBM280", "CP280");
		fIANA2JavaMap.put("EBCDIC-CP-IT", "CP280");
		fIANA2JavaMap.put("IBM284", "CP284");
		fIANA2JavaMap.put("CP284", "CP284");
		fIANA2JavaMap.put("CSIBM284", "CP284");
		fIANA2JavaMap.put("EBCDIC-CP-ES", "CP284");
		fIANA2JavaMap.put("EBCDIC-CP-GB", "CP285");
		fIANA2JavaMap.put("IBM285", "CP285");
		fIANA2JavaMap.put("CP285", "CP285");
		fIANA2JavaMap.put("CSIBM285", "CP285");
		fIANA2JavaMap.put("EBCDIC-JP-KANA", "CP290");
		fIANA2JavaMap.put("IBM290", "CP290");
		fIANA2JavaMap.put("CP290", "CP290");
		fIANA2JavaMap.put("CSIBM290", "CP290");
		fIANA2JavaMap.put("EBCDIC-CP-FR", "CP297");
		fIANA2JavaMap.put("IBM297", "CP297");
		fIANA2JavaMap.put("CP297", "CP297");
		fIANA2JavaMap.put("CSIBM297", "CP297");
		fIANA2JavaMap.put("EBCDIC-CP-AR1", "CP420");
		fIANA2JavaMap.put("IBM420", "CP420");
		fIANA2JavaMap.put("CP420", "CP420");
		fIANA2JavaMap.put("CSIBM420", "CP420");
		fIANA2JavaMap.put("EBCDIC-CP-HE", "CP424");
		fIANA2JavaMap.put("IBM424", "CP424");
		fIANA2JavaMap.put("CP424", "CP424");
		fIANA2JavaMap.put("CSIBM424", "CP424");
		fIANA2JavaMap.put("IBM437", "CP437");
		fIANA2JavaMap.put("437", "CP437");
		fIANA2JavaMap.put("CP437", "CP437");
		fIANA2JavaMap.put("CSPC8CODEPAGE437", "CP437");
		fIANA2JavaMap.put("EBCDIC-CP-CH", "CP500");
		fIANA2JavaMap.put("IBM500", "CP500");
		fIANA2JavaMap.put("CP500", "CP500");
		fIANA2JavaMap.put("CSIBM500", "CP500");
		fIANA2JavaMap.put("EBCDIC-CP-CH", "CP500");
		fIANA2JavaMap.put("EBCDIC-CP-BE", "CP500");
		fIANA2JavaMap.put("IBM775", "CP775");
		fIANA2JavaMap.put("CP775", "CP775");
		fIANA2JavaMap.put("CSPC775BALTIC", "CP775");
		fIANA2JavaMap.put("IBM850", "CP850");
		fIANA2JavaMap.put("850", "CP850");
		fIANA2JavaMap.put("CP850", "CP850");
		fIANA2JavaMap.put("CSPC850MULTILINGUAL", "CP850");
		fIANA2JavaMap.put("IBM852", "CP852");
		fIANA2JavaMap.put("852", "CP852");
		fIANA2JavaMap.put("CP852", "CP852");
		fIANA2JavaMap.put("CSPCP852", "CP852");
		fIANA2JavaMap.put("IBM855", "CP855");
		fIANA2JavaMap.put("855", "CP855");
		fIANA2JavaMap.put("CP855", "CP855");
		fIANA2JavaMap.put("CSIBM855", "CP855");
		fIANA2JavaMap.put("IBM857", "CP857");
		fIANA2JavaMap.put("857", "CP857");
		fIANA2JavaMap.put("CP857", "CP857");
		fIANA2JavaMap.put("CSIBM857", "CP857");
		fIANA2JavaMap.put("IBM00858", "CP858");
		fIANA2JavaMap.put("CP00858", "CP858");
		fIANA2JavaMap.put("CCSID00858", "CP858");
		fIANA2JavaMap.put("IBM860", "CP860");
		fIANA2JavaMap.put("860", "CP860");
		fIANA2JavaMap.put("CP860", "CP860");
		fIANA2JavaMap.put("CSIBM860", "CP860");
		fIANA2JavaMap.put("IBM861", "CP861");
		fIANA2JavaMap.put("861", "CP861");
		fIANA2JavaMap.put("CP861", "CP861");
		fIANA2JavaMap.put("CP-IS", "CP861");
		fIANA2JavaMap.put("CSIBM861", "CP861");
		fIANA2JavaMap.put("IBM862", "CP862");
		fIANA2JavaMap.put("862", "CP862");
		fIANA2JavaMap.put("CP862", "CP862");
		fIANA2JavaMap.put("CSPC862LATINHEBREW", "CP862");
		fIANA2JavaMap.put("IBM863", "CP863");
		fIANA2JavaMap.put("863", "CP863");
		fIANA2JavaMap.put("CP863", "CP863");
		fIANA2JavaMap.put("CSIBM863", "CP863");
		fIANA2JavaMap.put("IBM864", "CP864");
		fIANA2JavaMap.put("CP864", "CP864");
		fIANA2JavaMap.put("CSIBM864", "CP864");
		fIANA2JavaMap.put("IBM865", "CP865");
		fIANA2JavaMap.put("865", "CP865");
		fIANA2JavaMap.put("CP865", "CP865");
		fIANA2JavaMap.put("CSIBM865", "CP865");
		fIANA2JavaMap.put("IBM866", "CP866");
		fIANA2JavaMap.put("866", "CP866");
		fIANA2JavaMap.put("CP866", "CP866");
		fIANA2JavaMap.put("CSIBM866", "CP866");
		fIANA2JavaMap.put("IBM868", "CP868");
		fIANA2JavaMap.put("CP868", "CP868");
		fIANA2JavaMap.put("CSIBM868", "CP868");
		fIANA2JavaMap.put("CP-AR", "CP868");
		fIANA2JavaMap.put("IBM869", "CP869");
		fIANA2JavaMap.put("CP869", "CP869");
		fIANA2JavaMap.put("CSIBM869", "CP869");
		fIANA2JavaMap.put("CP-GR", "CP869");
		fIANA2JavaMap.put("IBM870", "CP870");
		fIANA2JavaMap.put("CP870", "CP870");
		fIANA2JavaMap.put("CSIBM870", "CP870");
		fIANA2JavaMap.put("EBCDIC-CP-ROECE", "CP870");
		fIANA2JavaMap.put("EBCDIC-CP-YU", "CP870");
		fIANA2JavaMap.put("IBM871", "CP871");
		fIANA2JavaMap.put("CP871", "CP871");
		fIANA2JavaMap.put("CSIBM871", "CP871");
		fIANA2JavaMap.put("EBCDIC-CP-IS", "CP871");
		fIANA2JavaMap.put("IBM918", "CP918");
		fIANA2JavaMap.put("CP918", "CP918");
		fIANA2JavaMap.put("CSIBM918", "CP918");
		fIANA2JavaMap.put("EBCDIC-CP-AR2", "CP918");
		fIANA2JavaMap.put("IBM00924", "CP924");
		fIANA2JavaMap.put("CP00924", "CP924");
		fIANA2JavaMap.put("CCSID00924", "CP924");
		fIANA2JavaMap.put("EBCDIC-LATIN9--EURO", "CP924");
		fIANA2JavaMap.put("IBM1026", "CP1026");
		fIANA2JavaMap.put("CP1026", "CP1026");
		fIANA2JavaMap.put("CSIBM1026", "CP1026");
		fIANA2JavaMap.put("IBM01140", "Cp1140");
		fIANA2JavaMap.put("CP01140", "Cp1140");
		fIANA2JavaMap.put("CCSID01140", "Cp1140");
		fIANA2JavaMap.put("IBM01141", "Cp1141");
		fIANA2JavaMap.put("CP01141", "Cp1141");
		fIANA2JavaMap.put("CCSID01141", "Cp1141");
		fIANA2JavaMap.put("IBM01142", "Cp1142");
		fIANA2JavaMap.put("CP01142", "Cp1142");
		fIANA2JavaMap.put("CCSID01142", "Cp1142");
		fIANA2JavaMap.put("IBM01143", "Cp1143");
		fIANA2JavaMap.put("CP01143", "Cp1143");
		fIANA2JavaMap.put("CCSID01143", "Cp1143");
		fIANA2JavaMap.put("IBM01144", "Cp1144");
		fIANA2JavaMap.put("CP01144", "Cp1144");
		fIANA2JavaMap.put("CCSID01144", "Cp1144");
		fIANA2JavaMap.put("IBM01145", "Cp1145");
		fIANA2JavaMap.put("CP01145", "Cp1145");
		fIANA2JavaMap.put("CCSID01145", "Cp1145");
		fIANA2JavaMap.put("IBM01146", "Cp1146");
		fIANA2JavaMap.put("CP01146", "Cp1146");
		fIANA2JavaMap.put("CCSID01146", "Cp1146");
		fIANA2JavaMap.put("IBM01147", "Cp1147");
		fIANA2JavaMap.put("CP01147", "Cp1147");
		fIANA2JavaMap.put("CCSID01147", "Cp1147");
		fIANA2JavaMap.put("IBM01148", "Cp1148");
		fIANA2JavaMap.put("CP01148", "Cp1148");
		fIANA2JavaMap.put("CCSID01148", "Cp1148");
		fIANA2JavaMap.put("IBM01149", "Cp1149");
		fIANA2JavaMap.put("CP01149", "Cp1149");
		fIANA2JavaMap.put("CCSID01149", "Cp1149");
		fIANA2JavaMap.put("EUC-JP", "EUCJIS");
		fIANA2JavaMap.put("CSEUCPKDFMTJAPANESE", "EUCJIS");
		fIANA2JavaMap.put("EXTENDED_UNIX_CODE_PACKED_FORMAT_FOR_JAPANESE", "EUCJIS");
		fIANA2JavaMap.put("EUC-KR", "KSC5601");
		fIANA2JavaMap.put("CSEUCKR", "KSC5601");
		fIANA2JavaMap.put("KS_C_5601-1987", "KS_C_5601-1987");
		fIANA2JavaMap.put("ISO-IR-149", "KS_C_5601-1987");
		fIANA2JavaMap.put("KS_C_5601-1989", "KS_C_5601-1987");
		fIANA2JavaMap.put("KSC_5601", "KS_C_5601-1987");
		fIANA2JavaMap.put("KOREAN", "KS_C_5601-1987");
		fIANA2JavaMap.put("CSKSC56011987", "KS_C_5601-1987");
		fIANA2JavaMap.put("GB2312", "GB2312");
		fIANA2JavaMap.put("CSGB2312", "GB2312");
		fIANA2JavaMap.put("ISO-2022-JP", "JIS");
		fIANA2JavaMap.put("CSISO2022JP", "JIS");
		fIANA2JavaMap.put("ISO-2022-KR", "ISO2022KR");
		fIANA2JavaMap.put("CSISO2022KR", "ISO2022KR");
		fIANA2JavaMap.put("ISO-2022-CN", "ISO2022CN");

		fIANA2JavaMap.put("X0201", "JIS0201");
		fIANA2JavaMap.put("CSISO13JISC6220JP", "JIS0201");
		fIANA2JavaMap.put("X0208", "JIS0208");
		fIANA2JavaMap.put("ISO-IR-87", "JIS0208");
		fIANA2JavaMap.put("X0208dbiJIS_X0208-1983", "JIS0208");
		fIANA2JavaMap.put("CSISO87JISX0208", "JIS0208");
		fIANA2JavaMap.put("X0212", "JIS0212");
		fIANA2JavaMap.put("ISO-IR-159", "JIS0212");
		fIANA2JavaMap.put("CSISO159JISX02121990", "JIS0212");
		fIANA2JavaMap.put("GB18030", "GB18030");
		fIANA2JavaMap.put("GBK", "GBK");
		fIANA2JavaMap.put("CP936", "GBK");
		fIANA2JavaMap.put("MS936", "GBK");
		fIANA2JavaMap.put("WINDOWS-936", "GBK");
		fIANA2JavaMap.put("SHIFT_JIS", "SJIS");
		fIANA2JavaMap.put("CSSHIFTJIS", "SJIS");
		fIANA2JavaMap.put("MS_KANJI", "SJIS");
		fIANA2JavaMap.put("WINDOWS-31J", "MS932");
		fIANA2JavaMap.put("CSWINDOWS31J", "MS932");

		// Add support for Cp1252 and its friends
		fIANA2JavaMap.put("WINDOWS-1250", "Cp1250");
		fIANA2JavaMap.put("WINDOWS-1251", "Cp1251");
		fIANA2JavaMap.put("WINDOWS-1252", "Cp1252");
		fIANA2JavaMap.put("WINDOWS-1253", "Cp1253");
		fIANA2JavaMap.put("WINDOWS-1254", "Cp1254");
		fIANA2JavaMap.put("WINDOWS-1255", "Cp1255");
		fIANA2JavaMap.put("WINDOWS-1256", "Cp1256");
		fIANA2JavaMap.put("WINDOWS-1257", "Cp1257");
		fIANA2JavaMap.put("WINDOWS-1258", "Cp1258");
		fIANA2JavaMap.put("TIS-620", "TIS620");

		fIANA2JavaMap.put("ISO-8859-1", "ISO8859_1");
		fIANA2JavaMap.put("ISO-IR-100", "ISO8859_1");
		fIANA2JavaMap.put("ISO_8859-1", "ISO8859_1");
		fIANA2JavaMap.put("LATIN1", "ISO8859_1");
		fIANA2JavaMap.put("CSISOLATIN1", "ISO8859_1");
		fIANA2JavaMap.put("L1", "ISO8859_1");
		fIANA2JavaMap.put("IBM819", "ISO8859_1");
		fIANA2JavaMap.put("CP819", "ISO8859_1");

		fIANA2JavaMap.put("ISO-8859-2", "ISO8859_2");
		fIANA2JavaMap.put("ISO-IR-101", "ISO8859_2");
		fIANA2JavaMap.put("ISO_8859-2", "ISO8859_2");
		fIANA2JavaMap.put("LATIN2", "ISO8859_2");
		fIANA2JavaMap.put("CSISOLATIN2", "ISO8859_2");
		fIANA2JavaMap.put("L2", "ISO8859_2");

		fIANA2JavaMap.put("ISO-8859-3", "ISO8859_3");
		fIANA2JavaMap.put("ISO-IR-109", "ISO8859_3");
		fIANA2JavaMap.put("ISO_8859-3", "ISO8859_3");
		fIANA2JavaMap.put("LATIN3", "ISO8859_3");
		fIANA2JavaMap.put("CSISOLATIN3", "ISO8859_3");
		fIANA2JavaMap.put("L3", "ISO8859_3");

		fIANA2JavaMap.put("ISO-8859-4", "ISO8859_4");
		fIANA2JavaMap.put("ISO-IR-110", "ISO8859_4");
		fIANA2JavaMap.put("ISO_8859-4", "ISO8859_4");
		fIANA2JavaMap.put("LATIN4", "ISO8859_4");
		fIANA2JavaMap.put("CSISOLATIN4", "ISO8859_4");
		fIANA2JavaMap.put("L4", "ISO8859_4");

		fIANA2JavaMap.put("ISO-8859-5", "ISO8859_5");
		fIANA2JavaMap.put("ISO-IR-144", "ISO8859_5");
		fIANA2JavaMap.put("ISO_8859-5", "ISO8859_5");
		fIANA2JavaMap.put("CYRILLIC", "ISO8859_5");
		fIANA2JavaMap.put("CSISOLATINCYRILLIC", "ISO8859_5");

		fIANA2JavaMap.put("ISO-8859-6", "ISO8859_6");
		fIANA2JavaMap.put("ISO-IR-127", "ISO8859_6");
		fIANA2JavaMap.put("ISO_8859-6", "ISO8859_6");
		fIANA2JavaMap.put("ECMA-114", "ISO8859_6");
		fIANA2JavaMap.put("ASMO-708", "ISO8859_6");
		fIANA2JavaMap.put("ARABIC", "ISO8859_6");
		fIANA2JavaMap.put("CSISOLATINARABIC", "ISO8859_6");

		fIANA2JavaMap.put("ISO-8859-7", "ISO8859_7");
		fIANA2JavaMap.put("ISO-IR-126", "ISO8859_7");
		fIANA2JavaMap.put("ISO_8859-7", "ISO8859_7");
		fIANA2JavaMap.put("ELOT_928", "ISO8859_7");
		fIANA2JavaMap.put("ECMA-118", "ISO8859_7");
		fIANA2JavaMap.put("GREEK", "ISO8859_7");
		fIANA2JavaMap.put("CSISOLATINGREEK", "ISO8859_7");
		fIANA2JavaMap.put("GREEK8", "ISO8859_7");

		fIANA2JavaMap.put("ISO-8859-8", "ISO8859_8");
		fIANA2JavaMap.put("ISO-8859-8-I", "ISO8859_8"); // added since this encoding only differs w.r.t. presentation
		fIANA2JavaMap.put("ISO-IR-138", "ISO8859_8");
		fIANA2JavaMap.put("ISO_8859-8", "ISO8859_8");
		fIANA2JavaMap.put("HEBREW", "ISO8859_8");
		fIANA2JavaMap.put("CSISOLATINHEBREW", "ISO8859_8");

		fIANA2JavaMap.put("ISO-8859-9", "ISO8859_9");
		fIANA2JavaMap.put("ISO-IR-148", "ISO8859_9");
		fIANA2JavaMap.put("ISO_8859-9", "ISO8859_9");
		fIANA2JavaMap.put("LATIN5", "ISO8859_9");
		fIANA2JavaMap.put("CSISOLATIN5", "ISO8859_9");
		fIANA2JavaMap.put("L5", "ISO8859_9");

		fIANA2JavaMap.put("ISO-8859-13", "ISO8859_13");

		fIANA2JavaMap.put("ISO-8859-15", "ISO8859_15_FDIS");
		fIANA2JavaMap.put("ISO_8859-15", "ISO8859_15_FDIS");
		fIANA2JavaMap.put("LATIN-9", "ISO8859_15_FDIS");

		fIANA2JavaMap.put("KOI8-R", "KOI8_R");
		fIANA2JavaMap.put("CSKOI8R", "KOI8_R");
		fIANA2JavaMap.put("US-ASCII", "ASCII");
		fIANA2JavaMap.put("ISO-IR-6", "ASCII");
		fIANA2JavaMap.put("ANSI_X3.4-1968", "ASCII");
		fIANA2JavaMap.put("ANSI_X3.4-1986", "ASCII");
		fIANA2JavaMap.put("ISO_646.IRV:1991", "ASCII");
		fIANA2JavaMap.put("ASCII", "ASCII");
		fIANA2JavaMap.put("CSASCII", "ASCII");
		fIANA2JavaMap.put("ISO646-US", "ASCII");
		fIANA2JavaMap.put("US", "ASCII");
		fIANA2JavaMap.put("IBM367", "ASCII");
		fIANA2JavaMap.put("CP367", "ASCII");
		fIANA2JavaMap.put("UTF-8", "UTF8");
		fIANA2JavaMap.put("UTF-16", "UTF-16");
		fIANA2JavaMap.put("UTF-16BE", "UnicodeBig");
		fIANA2JavaMap.put("UTF-16LE", "UnicodeLittle");

		// support for 1047, as proposed to be added to the
		// IANA registry in
		// http://lists.w3.org/Archives/Public/ietf-charset/2002JulSep/0049.html
		fIANA2JavaMap.put("IBM-1047", "Cp1047");
		fIANA2JavaMap.put("IBM1047", "Cp1047");
		fIANA2JavaMap.put("CP1047", "Cp1047");

		// Adding new aliases as proposed in
		// http://lists.w3.org/Archives/Public/ietf-charset/2002JulSep/0058.html
		fIANA2JavaMap.put("IBM-37", "CP037");
		fIANA2JavaMap.put("IBM-273", "CP273");
		fIANA2JavaMap.put("IBM-277", "CP277");
		fIANA2JavaMap.put("IBM-278", "CP278");
		fIANA2JavaMap.put("IBM-280", "CP280");
		fIANA2JavaMap.put("IBM-284", "CP284");
		fIANA2JavaMap.put("IBM-285", "CP285");
		fIANA2JavaMap.put("IBM-290", "CP290");
		fIANA2JavaMap.put("IBM-297", "CP297");
		fIANA2JavaMap.put("IBM-420", "CP420");
		fIANA2JavaMap.put("IBM-424", "CP424");
		fIANA2JavaMap.put("IBM-437", "CP437");
		fIANA2JavaMap.put("IBM-500", "CP500");
		fIANA2JavaMap.put("IBM-775", "CP775");
		fIANA2JavaMap.put("IBM-850", "CP850");
		fIANA2JavaMap.put("IBM-852", "CP852");
		fIANA2JavaMap.put("IBM-855", "CP855");
		fIANA2JavaMap.put("IBM-857", "CP857");
		fIANA2JavaMap.put("IBM-858", "CP858");
		fIANA2JavaMap.put("IBM-860", "CP860");
		fIANA2JavaMap.put("IBM-861", "CP861");
		fIANA2JavaMap.put("IBM-862", "CP862");
		fIANA2JavaMap.put("IBM-863", "CP863");
		fIANA2JavaMap.put("IBM-864", "CP864");
		fIANA2JavaMap.put("IBM-865", "CP865");
		fIANA2JavaMap.put("IBM-866", "CP866");
		fIANA2JavaMap.put("IBM-868", "CP868");
		fIANA2JavaMap.put("IBM-869", "CP869");
		fIANA2JavaMap.put("IBM-870", "CP870");
		fIANA2JavaMap.put("IBM-871", "CP871");
		fIANA2JavaMap.put("IBM-918", "CP918");
		fIANA2JavaMap.put("IBM-924", "CP924");
		fIANA2JavaMap.put("IBM-1026", "CP1026");
		fIANA2JavaMap.put("IBM-1140", "Cp1140");
		fIANA2JavaMap.put("IBM-1141", "Cp1141");
		fIANA2JavaMap.put("IBM-1142", "Cp1142");
		fIANA2JavaMap.put("IBM-1143", "Cp1143");
		fIANA2JavaMap.put("IBM-1144", "Cp1144");
		fIANA2JavaMap.put("IBM-1145", "Cp1145");
		fIANA2JavaMap.put("IBM-1146", "Cp1146");
		fIANA2JavaMap.put("IBM-1147", "Cp1147");
		fIANA2JavaMap.put("IBM-1148", "Cp1148");
		fIANA2JavaMap.put("IBM-1149", "Cp1149");
		fIANA2JavaMap.put("IBM-819", "ISO8859_1");
		fIANA2JavaMap.put("IBM-367", "ASCII");

		for (String key : fIANA2JavaMap.keySet())
			fJava2IANAMap.put(fIANA2JavaMap.get(key), key);
	}

	/** Default constructor. */
	public EncodingMap() {

	}

	/**
	 * Adds an IANA to Java encoding name mapping.
	 * 
	 * @param ianaEncoding
	 *          The IANA encoding name.
	 * @param javaEncoding
	 *          The Java encoding name.
	 */
	public static void putIANA2JavaMapping(String ianaEncoding, String javaEncoding) {
		fIANA2JavaMap.put(ianaEncoding, javaEncoding);
	}

	/**
	 * Returns the Java encoding name for the specified IANA encoding name.
	 * 
	 * @param ianaEncoding
	 *          The IANA encoding name.
	 */
	public static String getIANA2JavaMapping(String ianaEncoding) {
		return (String) fIANA2JavaMap.get(ianaEncoding);
	}

	/**
	 * Removes an IANA to Java encoding name mapping.
	 * 
	 * @param ianaEncoding
	 *          The IANA encoding name.
	 */
	public static String removeIANA2JavaMapping(String ianaEncoding) {
		return (String) fIANA2JavaMap.remove(ianaEncoding);
	}

	/**
	 * Adds a Java to IANA encoding name mapping.
	 * 
	 * @param javaEncoding
	 *          The Java encoding name.
	 * @param ianaEncoding
	 *          The IANA encoding name.
	 */
	public static void putJava2IANAMapping(String javaEncoding, String ianaEncoding) {
		fJava2IANAMap.put(javaEncoding, ianaEncoding);
	}

	/**
	 * Returns the IANA encoding name for the specified Java encoding name.
	 * 
	 * @param javaEncoding
	 *          The Java encoding name.
	 */
	public static String getJava2IANAMapping(String javaEncoding) {
		return (String) fJava2IANAMap.get(javaEncoding);
	}

	/**
	 * Removes a Java to IANA encoding name mapping.
	 * 
	 * @param javaEncoding
	 *          The Java encoding name.
	 */
	public static String removeJava2IANAMapping(String javaEncoding) {
		return (String) fJava2IANAMap.remove(javaEncoding);
	}
}
