/*******************************************************************************
 *  Jaspersoft Open Studio - Eclipse-based JasperReports Designer.
 *  Copyright (C) 2005, 2010 Jaspersoft Corporation. All rights reserved.
 *  http://www.jaspersoft.com
 *  
 *  Unless you have purchased a commercial license agreement from Jaspersoft,
 *  the following license terms apply:
 *  
 *  This program is part of iReport.
 *  
 *  iReport is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  iReport is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with iReport. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
#include "com_jaspersoft_studio_editor_java2d_LinuxImageRenderer.h"
#include <gtk.h>

/*
 * Class:     com_jaspersoft_studio.designer_java2d_LinuxImageRenderer
 * Method:    renderImage
 * Signature: (IIIIIII[III)V
 */
JNIEXPORT void JNICALL Java_com_jaspersoft_studio_editor_java2d_LinuxImageRenderer_renderImage
	(JNIEnv *env, jclass c, jint hdcDest, jint xDest, jint yDest, jint width, jint height, jint xSrc, jint ySrc, jintArray data, jint imgWidth, jint imgHeight) {

	BITMAPINFOHEADER header = {
		(DWORD) sizeof(BITMAPINFOHEADER),
		(LONG) imgWidth,
		(LONG) -imgHeight,
		(WORD) 1,
		(WORD) 32,
		BI_RGB,
		(DWORD) 0,
		(LONG) 0,
		(LONG) 0,
		(DWORD) 0,
		(DWORD) 0
	};

	BITMAPINFO lpbmi = {
		header,
		0
	};

	jint* lpvBits = (*env)->GetPrimitiveArrayCritical(env, data, 0);

	// Windows is a mess trust me!
	// Even if the image is marked as 'up to bottom', the source origin is
	// always the lower-left image corner!
	// That's why we translate ySrc -> imgHeight - (ySrc + height)
	// Not doing this will give you strange effects in some area overlapping combinaisons.
	int result =
		SetDIBitsToDevice((HDC)hdcDest,xDest,yDest,width,height,xSrc,imgHeight - (ySrc + height),
			(UINT)0,(UINT)imgHeight,(void *) lpvBits,&lpbmi,DIB_RGB_COLORS);

	// Array is released without writing back data (JNI_ABORT)
	(*env)->ReleasePrimitiveArrayCritical(env, data, lpvBits, JNI_ABORT);
}
