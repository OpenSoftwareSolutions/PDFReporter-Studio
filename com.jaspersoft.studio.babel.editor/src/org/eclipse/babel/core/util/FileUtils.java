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

import java.io.ByteArrayInputStream;
import java.io.File;

import org.eclipse.babel.core.configuration.ConfigurationManager;
import org.eclipse.babel.core.configuration.DirtyHack;
import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.resource.internal.PropertiesFileResource;
import org.eclipse.babel.core.message.resource.ser.PropertiesSerializer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * Util class for File-I/O operations.
 * 
 * @author Alexej Strelzow
 */
public class FileUtils {

    public static void writeToFile(IMessagesBundle bundle) {
        DirtyHack.setEditorModificationEnabled(false);

        PropertiesSerializer ps = new PropertiesSerializer(ConfigurationManager.getInstance().getSerializerConfig());
        String editorContent = ps.serialize(bundle);
        IFile file = getFile(bundle);
        try {
            file.refreshLocal(IResource.DEPTH_ZERO, null);
            file.setContents(
                    new ByteArrayInputStream(editorContent.getBytes()), false,
                    true, null);
            file.refreshLocal(IResource.DEPTH_ZERO, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DirtyHack.setEditorModificationEnabled(true);
        }
    }

    public static IFile getFile(IMessagesBundle bundle) {
        if (bundle.getResource() instanceof PropertiesFileResource) { // different
            // ResourceLocationLabel
            String path = bundle.getResource().getResourceLocationLabel(); // P:\Workspace\AST\TEST\src\messages\Messages_de.properties
            int index = path.indexOf("src");
            String pathBeforeSrc = path.substring(0, index - 1);
            int lastIndexOf = pathBeforeSrc.lastIndexOf(File.separatorChar);
            String projectName = path.substring(lastIndexOf + 1, index - 1);
            String relativeFilePath = path.substring(index, path.length());

            return ResourcesPlugin.getWorkspace().getRoot()
                    .getProject(projectName).getFile(relativeFilePath);
        } else {
            String location = bundle.getResource().getResourceLocationLabel(); // /TEST/src/messages/Messages_en_IN.properties
            String projectName = location
                    .substring(1, location.indexOf("/", 1));
            location = location.substring(projectName.length() + 1,
                    location.length());
            return ResourcesPlugin.getWorkspace().getRoot()
                    .getProject(projectName).getFile(location);
        }
    }

}
