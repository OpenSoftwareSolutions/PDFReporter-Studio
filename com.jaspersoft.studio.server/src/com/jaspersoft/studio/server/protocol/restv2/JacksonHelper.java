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
package com.jaspersoft.studio.server.protocol.restv2;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.jaxb.XmlJaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class JacksonHelper {

	public static ObjectMapper getJSONMapper() {
		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector primary = new JaxbAnnotationIntrospector(mapper.getTypeFactory());
		AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
		mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(primary, secondary));
		setupMapper(mapper);
		return mapper;
	}

	public static XmlMapper getXMLMapper() {
		XmlMapper mapper = new XmlMapper();
		AnnotationIntrospector primary = new XmlJaxbAnnotationIntrospector(mapper.getTypeFactory());
		AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
		mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(primary, secondary));
		setupMapper(mapper);
		return mapper;
	}

	private static void setupMapper(ObjectMapper mapper) {
		// Serialize dates using ISO8601 format
		// Jackson uses timestamps by default, so use StdDateFormat to get ISO8601
		mapper.getSerializationConfig().with(new StdDateFormat());
		// Deserialize dates using ISO8601 format
		mapper.getDeserializationConfig().with(new StdDateFormat());
		// Prevent exceptions from being thrown for unknown properties
		// mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
		// false);
		mapper.addHandler(new DeserializationProblemHandler() {
			@Override
			public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser jp, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException,
					JsonProcessingException {
				return true;
			}
		});
		// ignore fields with null values
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

}
