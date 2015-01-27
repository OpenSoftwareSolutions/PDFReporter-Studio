/*******************************************************************************
 * Copyright (c) 2012 Matthias Lettmayer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthias Lettmayer - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.compat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SwtRapCompatibilityFormToolkit extends FormToolkit {

    public SwtRapCompatibilityFormToolkit(FormColors colors) {
        super(colors);
    }

    public SwtRapCompatibilityFormToolkit(Display display) {
        super(display);
    }
}
