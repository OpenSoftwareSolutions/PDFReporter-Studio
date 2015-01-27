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
package com.jaspersoft.studio.editor.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * 
 * Serializer\Deserializer for the list of string elements. It was realized 
 * to serialize informations about the report units
 * 
 * @author Orlandin Marco
 */
public class UnitTransfer extends ByteArrayTransfer {
	private static UnitTransfer instance = new UnitTransfer();
	private static final String TYPE_NAME = "unit-transfer-format";
	private static final int TYPEID = registerType(TYPE_NAME);


	/**
	 * Return an instance of the class
	 */
	public static UnitTransfer getInstance() {
		return instance;
	}

	private UnitTransfer() {
	}
	
	protected String[] fromByteArray(byte[] bytes) {
		try {
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bytes));

			/* read number of gadgets */
			int n = in.readInt();
			/* read gadgets */
			String[] gadgets = new String[n];
			for (int i = 0; i < n; i++) {
				String gadget = readGadget(in);
				if (gadget == null) {
					return null;
				}
				gadgets[i] = gadget;
			}
			return gadgets;
		} catch (IOException e) {
			return null;
		}
	}

	/*
	 * Method declared on Transfer.
	 */
	protected int[] getTypeIds() {
		return new int[] { TYPEID };
	}

	/*
	 * Method declared on Transfer.
	 */
	protected String[] getTypeNames() {
		return new String[] { TYPE_NAME };
	}

	/*
	 * Method declared on Transfer.
	 */
	protected void javaToNative(Object object, TransferData transferData) {
		byte[] bytes = toByteArray((String[]) object);
		if (bytes != null)
			super.javaToNative(bytes, transferData);
	}

	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[]) super.nativeToJava(transferData);
		super.isSupportedType(transferData);
		if (bytes!= null) return fromByteArray(bytes);
		else return null;
	}

	private String readGadget(ObjectInputStream dataIn)
			throws IOException {
		try {
			return (String) dataIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected byte[] toByteArray(String[] nodes) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);

			/* write number of markers */
			out.writeInt(nodes.length);

			/* write markers */
			for (int i = 0; i < nodes.length; i++) {
				writeNode((String) nodes[i], out);
			}
			out.close();
			bytes = byteOut.toByteArray();
		} catch (IOException e) {
			// when in doubt send nothing
		}
		return bytes;
	}

	private void writeNode(String node, ObjectOutputStream dataOut)
			throws IOException {
		dataOut.writeObject(node);
	}
}
