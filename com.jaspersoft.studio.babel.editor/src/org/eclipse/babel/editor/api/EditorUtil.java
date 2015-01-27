package org.eclipse.babel.editor.api;

import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.editor.i18n.I18NPage;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Util class for editor operations. <br>
 * <br>
 * 
 * @author Alexej Strelzow
 */
public class EditorUtil {

    /**
     * @param page
     *            The {@link IWorkbenchPage}
     * @return The selected {@link IKeyTreeNode} of the page.
     */
    public static IKeyTreeNode getSelectedKeyTreeNode(IWorkbenchPage page) {
        AbstractMessagesEditor editor = (AbstractMessagesEditor) page
                .getActiveEditor();
        if (editor.getSelectedPage() instanceof I18NPage) {
            I18NPage p = (I18NPage) editor.getSelectedPage();
            ISelection selection = p.getSelection();
            if (!selection.isEmpty()
                    && selection instanceof IStructuredSelection) {
                return (IKeyTreeNode) ((IStructuredSelection) selection)
                        .getFirstElement();
            }
        }
        return null;
    }
}
