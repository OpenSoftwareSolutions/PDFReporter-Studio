/*******************************************************************************
 * Copyright (c) 2012 Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexej Strelzow - initial API and implementation
 ******************************************************************************/

package org.eclipse.babel.core.configuration;

import org.eclipse.babel.core.message.internal.AbstractMessageModel;

/**
 * Contains following two <b>dirty</b> workaround flags: <li><b>fireEnabled:</b>
 * deactivates {@link PropertyChangeEvent}-fire in {@link AbstractMessageModel}</li>
 * <li><b>editorModificationEnabled:</b> prevents
 * <code>EclipsePropertiesEditorResource#setText</code></li> <br>
 * <br>
 * <b>We need to get rid of this somehow!!!!!!!!!</b> <br>
 * <br>
 * 
 * @author Alexej Strelzow
 */
public final class DirtyHack {

    private static boolean fireEnabled = true; // no property-fire calls in
                                               // AbstractMessageModel

    private static boolean editorModificationEnabled = true; // no setText in
                                                             // EclipsePropertiesEditorResource

    // set this, if you serialize!

    public static boolean isFireEnabled() {
        return fireEnabled;
    }

    public static void setFireEnabled(boolean fireEnabled) {
        DirtyHack.fireEnabled = fireEnabled;
    }

    public static boolean isEditorModificationEnabled() {
        return editorModificationEnabled;
    }

    public static void setEditorModificationEnabled(
            boolean editorModificationEnabled) {
        DirtyHack.editorModificationEnabled = editorModificationEnabled;
    }

}
