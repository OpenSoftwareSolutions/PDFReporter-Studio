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
package com.jaspersoft.studio.editor.java2d;

/*
 * A factory for creating Renderer objects.
 */
public final class RendererFactory {

	/** The image renderer. */
	private static ImageRenderer imageRenderer;

	/**
	 * A.
	 * 
	 * @return the image renderer
	 */
	public static synchronized ImageRenderer a() // getImageRenderer
	{
		if (imageRenderer == null)
			imageRenderer = createRenderer();
		return imageRenderer;
	}

	/**
	 * Creates a new Renderer object.
	 * 
	 * @return the image renderer
	 */
	private static ImageRenderer createRenderer() {
//		try {
//			if ((Platform.getOS().equals(Platform.OS_WIN32)) && (Platform.getOSArch().equals(Platform.ARCH_X86))) {
//				return new Win32ImageRenderer();
//			} 
//
//			// else if ((Platform.getOS().equals(Platform.OS_LINUX)) && (Platform.getWS().equals(Platform.WS_GTK))) {
//			// return new LinuxImageRenderer();
//			// }
//		} catch (NoClassDefFoundError e) {
//			e.printStackTrace();
//		} catch (UnsatisfiedLinkError e) {
//			e.printStackTrace();
//		}
		return new GenericImageRenderer();
	}
}
