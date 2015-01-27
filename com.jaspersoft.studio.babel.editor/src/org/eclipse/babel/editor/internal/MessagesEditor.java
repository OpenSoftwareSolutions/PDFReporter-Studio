/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre, Alexej Strelzow, Matthias Lettmayer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - TapJI integration, bug fixes & enhancements
 *                    - issue 35, 36, 48, 73
 *    Matthias Lettmayer - extracted messages editor into own class for SWT specific implementation
 ******************************************************************************/

package org.eclipse.babel.editor.internal;

import org.eclipse.babel.core.message.internal.IMessagesBundleGroupListener;
import org.eclipse.babel.core.message.internal.MessagesBundle;
import org.eclipse.babel.core.message.internal.MessagesBundleGroupAdapter;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.messages.Messages;


public class MessagesEditor extends AbstractMessagesEditor {

    /** New locale page. */
    private NewLocalePage newLocalePage;
	
    @Override
    protected IMessagesBundleGroupListener getMsgBundleGroupListner() {
        return new MessagesBundleGroupAdapter() {
            @Override
            public void messagesBundleAdded(MessagesBundle messagesBundle) {
                addMessagesBundle(messagesBundle);
            }
        };
    }

    @Override
    protected void initRAP() {
        // nothing to do
    }

    @Override
    protected void disposeRAP() {
        // nothing to do
    }
    
    @Override
    protected void createPages() {
    	super.createPages();
    	createNewLocalePage();
    }

    @Override
    public void setEnabled(boolean enabled) {
        i18nPage.setEnabled(enabled);
        if (newLocalePage != null){
    		newLocalePage.setEnabled(enabled);
    	}
    }
    
    @Override
    public void dispose() {
    	super.dispose();
    	disposeNewLocalePage();
    }
    
    private void disposeNewLocalePage(){
    	if (newLocalePage != null){
    		newLocalePage.dispose();
    	}	
    }
    
    private void createNewLocalePage(){
    	newLocalePage = new NewLocalePage(getContainer(), this);
    	int index = addPage(newLocalePage);
        setPageText(index, Messages.editor_new_tab);
        setPageImage(index, UIUtils.getImage(UIUtils.IMAGE_NEW_PROPERTIES_FILE));
    }
    
    @Override
    protected void addMessagesBundle(MessagesBundle messagesBundle) {
    	removePage(getPageCount()-1);
    	disposeNewLocalePage();
    	super.addMessagesBundle(messagesBundle);
    	createNewLocalePage();
    }
    
    @Override
    protected void setSelection(int newPageIndex) {
    	if (getPageCount()-1 != newPageIndex)
    		super.setSelection(newPageIndex);
    }

}
