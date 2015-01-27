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
package org.eclipse.babel.core.util;

import java.util.Locale;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;

/**
 * Contains methods, which return names/IDs or Objects by name.
 *
 * @author Alexej Strelzow
 */
public class NameUtils {

    public static String getResourceBundleId(IResource resource) {
        String packageFragment = "";

        try {
            IJavaElement propertyFile = JavaCore.create(resource.getParent());
            if (propertyFile != null
                    && propertyFile instanceof IPackageFragment)
                packageFragment = ((IPackageFragment) propertyFile)
                        .getElementName();
        } catch (NoClassDefFoundError e) {
            // do nothing
        }
        return (packageFragment.length() > 0 ? packageFragment + "." : "")
                + getResourceBundleName(resource);
    }

    public static String getResourceBundleName(IResource res) {
        String name = res.getName();
        String regex = "^(.*?)" //$NON-NLS-1$
                + "((_[a-z]{2,3})|(_[a-z]{2,3}_[A-Z]{2})" //$NON-NLS-1$
                + "|(_[a-z]{2,3}_[A-Z]{2}_\\w*))?(\\." //$NON-NLS-1$
                + res.getFileExtension() + ")$"; //$NON-NLS-1$
        return name.replaceFirst(regex, "$1"); //$NON-NLS-1$
    }

    public static Locale getLocaleByName(String bundleName, String localeID) {
        String theBundleName = bundleName;
        if (theBundleName.contains(".")) {
            // we entered this method with the rbID and not the name!
            theBundleName = theBundleName
                    .substring(theBundleName.indexOf(".") + 1);
        }

        // Check locale
        Locale locale = null;
        localeID = localeID.substring(0,
                localeID.length() - "properties".length() - 1);
        if (localeID.length() == theBundleName.length()) {
            // default locale
            return null;
        } else {
            localeID = localeID.substring(theBundleName.length() + 1);
            String[] localeTokens = localeID.split("_");

            switch (localeTokens.length) {
            case 1:
                locale = new Locale(localeTokens[0]);
                break;
            case 2:
                locale = new Locale(localeTokens[0], localeTokens[1]);
                break;
            case 3:
                locale = new Locale(localeTokens[0], localeTokens[1],
                        localeTokens[2]);
                break;
            default:
                locale = null;
                break;
            }
        }

        return locale;
    }
}
