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
package org.eclipse.babel.core.refactoring;

import java.util.Locale;

import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.core.resources.IFile;

/**
 * Service class, which can be used to execute key refactorings. This can be
 * retrieved via:<br>
 * {@link RBManager#getRefactorService()}
 * 
 * @author Alexej Strelzow
 */
public interface IRefactoringService {

    /**
     * Executes following steps:<br>
     * <ol>
     * <li>Changes the {@link CompilationUnit}s, which must be changed</li>
     * <li>Changes the data mgmt. (backend -> {@link RBManager})</li>
     * <li>Displays the summary dialog</li>
     * </ol>
     * <br>
     * 
     * @param projectName
     *            The project the resource bundle is in.
     * @param resourceBundleId
     *            The resource bundle, which contains the key to be refactored.
     * @param selectedLocale
     *            The selected {@link Locale} to change.
     * @param oldKey
     *            The old key name
     * @param newKey
     *            The new key name, which should overwrite the old one
     * @param enumPath
     *            The path of the enum file to change
     */
    void refactorKey(String projectName, String resourceBundleId,
            String selectedLocale, String oldKey, String newKey, String enumName);

    /**
     * Executes following steps:<br>
     * <ol>
     * <li>Displays the initial refactoring dialog</li>
     * <li>Changes the {@link CompilationUnit}s, which must be changed</li>
     * <li>Changes the data mgmt. (backend -> {@link RBManager})</li>
     * <li>Displays the summary dialog</li>
     * </ol>
     * <br>
     * 
     * @param projectName
     *            The project the resource bundle is in.
     * @param resourceBundleId
     *            The resource bundle, which contains the key to be refactored.
     * @param selectedLocale
     *            The selected {@link Locale} to change.
     * @param oldKey
     *            The old key name
     * @param newKey
     *            The new key name, which should overwrite the old one
     * @param enumPath
     *            The path of the enum file to change
     */
    void openRefactorDialog(String projectName, String resourceBundleId,
            String oldKey, String enumName);

    /**
     * Executes following steps:<br>
     * <ol>
     * <li>Displays the initial refactoring dialog</li>
     * <li>Changes the {@link CompilationUnit}s, which must be changed</li>
     * <li>Changes the data mgmt. (backend -> {@link RBManager})</li>
     * <li>Displays the summary dialog</li>
     * </ol>
     * <br>
     * 
     * @param file
     *            The file, of the editor input
     * @param selectionOffset
     *            The position of the cursor
     */
    void openRefactorDialog(IFile file, int selectionOffset);
}
