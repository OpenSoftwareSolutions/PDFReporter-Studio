/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - TapJI integration, bug fixes & enhancements
 *                    - issue 35, 36, 48, 73
 ******************************************************************************/
package org.eclipse.babel.editor.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.internal.IMessagesBundleGroupListener;
import org.eclipse.babel.core.message.internal.MessageException;
import org.eclipse.babel.core.message.internal.MessagesBundle;
import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.babel.core.message.resource.IMessagesResource;
import org.eclipse.babel.core.message.tree.internal.AbstractKeyTreeModel;
import org.eclipse.babel.editor.IMessagesEditor;
import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.builder.ToggleNatureAction;
import org.eclipse.babel.editor.bundle.MessagesBundleGroupFactory;
import org.eclipse.babel.editor.i18n.I18NPage;
import org.eclipse.babel.editor.preferences.MsgEditorPreferences;
import org.eclipse.babel.editor.resource.EclipsePropertiesEditorResource;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.editor.views.MessagesBundleGroupOutline;
import org.eclipse.babel.messages.Messages;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Multi-page editor for editing resource bundles.
 */
public abstract class AbstractMessagesEditor extends MultiPageEditorPart
        implements IGotoMarker, IMessagesEditor {

    /** Editor ID, as defined in plugin.xml. */
    public static final String EDITOR_ID = "org.eclilpse.babel.editor.editor.MessagesEditor"; //$NON-NLS-1$

    protected String selectedKey;
    protected List<IMessagesEditorChangeListener> changeListeners = new ArrayList<IMessagesEditorChangeListener>(
            2);

    /** MessagesBundle group. */
    protected MessagesBundleGroup messagesBundleGroup;

    /** Page with key tree and text fields for all locales. */
    protected I18NPage i18nPage;
    protected final List<Locale> localesIndex = new ArrayList<Locale>();
    protected final List<ITextEditor> textEditorsIndex = new ArrayList<ITextEditor>();

    protected MessagesBundleGroupOutline outline;

    protected MessagesEditorMarkers markers;

    protected AbstractKeyTreeModel keyTreeModel;

    protected IFile file; // init

    protected boolean updateSelectedKey;

    /**
     * Creates a multi-page editor example.
     */
    public AbstractMessagesEditor() {
        super();
        outline = new MessagesBundleGroupOutline(this);
    }

    public MessagesEditorMarkers getMarkers() {
        return markers;
    }

    /**
     * The <code>MultiPageEditorExample</code> implementation of this method
     * checks that the input is an instance of <code>IFileEditorInput</code> of 
     * <code>FileStoreEditorInput</code> for the external file. In case of external
     * file a link is created into an appropriate project folder.
     */
    @Override
    public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
        file = null;
    	if (editorInput instanceof IFileEditorInput) {
            file = ((IFileEditorInput) editorInput).getFile();
            if (MsgEditorPreferences.isBuilderSetupAutomatically()) {
                IProject p = file.getProject();
                if (p != null && p.isAccessible()) {
                    ToggleNatureAction.addOrRemoveNatureOnProject(p, true, true);
                }
            }
        } else if (editorInput instanceof FileStoreEditorInput) {
        	FileStoreEditorInput input = (FileStoreEditorInput)editorInput;
        	file = createLinkedResource(input.getURI().getRawPath());
        }
    	
    	if (file != null){    	
	        try {
	            messagesBundleGroup = MessagesBundleGroupFactory.createBundleGroup(site, file);
	        } catch (MessageException e) {
	            throw new PartInitException("Cannot create bundle group.", e); //$NON-NLS-1$
	        }
	        messagesBundleGroup.addMessagesBundleGroupListener(getMsgBundleGroupListner());
	        markers = new MessagesEditorMarkers(messagesBundleGroup);
	        setPartName(messagesBundleGroup.getName());
	        setTitleImage(UIUtils.getImage(UIUtils.IMAGE_RESOURCE_BUNDLE));
	        closeIfAreadyOpen(site, file);
	        super.init(site, editorInput);
	        keyTreeModel = new AbstractKeyTreeModel(messagesBundleGroup);   
	        initRAP();
    	} else {
            throw new PartInitException("Unable to load the selected file"); //$NON-NLS-1$
        }
    }
    
    /**
     * Create a project for the external files called External Files
     * and put a link to the external resource inside there. The project
     * is created only if it dosen't exist and any previous link to the same
     * file is deleted before to create the new one
     * 
     * @param fileName Absolute path of the file on the disk
     * @return an IFile resource, type link, to the external file or null if 
     * for some reason it was not possible to create the link.
     */
    private IFile createLinkedResource(String fileName){
    	IFile file = null;
    	try {
		    IWorkspace ws = ResourcesPlugin.getWorkspace();
		    IProject project = ws.getRoot().getProject("External Files");
		    if (!project.exists())
		        project.create(null);
		    if (!project.isOpen())
		        project.open(null);
		    IPath location = new Path(fileName);
		    file = project.getFile(location.lastSegment());
		    if (file.exists()) file.delete(true, null);
			file.createLink(location, IResource.NONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
			file = null;
		}
	    return file;
    }



    /**
     * Creates the pages of the multi-page editor.
     */
    @Override
    protected void createPages() {
        // Create I18N page
        i18nPage = new I18NPage(getContainer(), SWT.NONE, this);
        int index = addPage(i18nPage);
        setPageText(index, Messages.editor_properties);
        setPageImage(index, UIUtils.getImage(UIUtils.IMAGE_RESOURCE_BUNDLE));

        // Create text editor pages for each locales
        Locale[] locales = messagesBundleGroup.getLocales();
        // first: sort the locales.
        UIUtils.sortLocales(locales);
        // second: filter+sort them according to the filter preferences.
        locales = UIUtils.filterLocales(locales);
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            MessagesBundle messagesBundle = (MessagesBundle) messagesBundleGroup.getMessagesBundle(locale);
            createMessagesBundlePage(messagesBundle);
        }
    }

    /**
     * Creates a new text editor for the messages bundle, which gets added to a new page
     */
    protected void createMessagesBundlePage(MessagesBundle messagesBundle) {
        try {
            IMessagesResource resource = messagesBundle.getResource();
            final TextEditor textEditor = (TextEditor) resource.getSource();
            int index = addPage(textEditor, textEditor.getEditorInput());
            setPageText(index,
                    UIUtils.getDisplayName(messagesBundle.getLocale()));
            setPageImage(index, UIUtils.getImage(UIUtils.IMAGE_PROPERTIES_FILE));
            localesIndex.add(messagesBundle.getLocale());
            textEditorsIndex.add(textEditor);            
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(),
                    "Error creating text editor page.", //$NON-NLS-1$
                    null, e.getStatus());
        }
    }

    /**
     * Adds a new messages bundle to an opened messages editor. Creates a new text edtor page
     * and a new entry in the i18n page for the given locale and messages bundle.
     */
    protected void addMessagesBundle(MessagesBundle messagesBundle) {
        createMessagesBundlePage(messagesBundle);
        i18nPage.addI18NEntry(messagesBundle.getLocale());
    }
    
    /**
     * Removes the text editor page + the entry from the i18n page of the given locale and messages bundle.
     */
    protected void removeMessagesBundle(MessagesBundle messagesBundle) {
        IMessagesResource resource = messagesBundle.getResource();
        final TextEditor textEditor = (TextEditor) resource.getSource();
        // index + 1 because of i18n page
        int pageIndex = textEditorsIndex.indexOf(textEditor) + 1;
        removePage(pageIndex);

        textEditorsIndex.remove(textEditor);
        localesIndex.remove(messagesBundle.getLocale());

        textEditor.dispose();

        // remove entry from i18n page
        i18nPage.removeI18NEntry(messagesBundle.getLocale());        
    }

    /**
     * Called when the editor's pages need to be reloaded. For example when the
     * filters of locale is changed.
     * <p>
     * Currently this only reloads the index page. TODO: remove and add the new
     * locales? it actually looks quite hard to do.
     * </p>
     */
    public void reloadDisplayedContents(boolean showFirstPage) {
        super.removePage(0);
        int currentlyActivePage = 0;
        if (!showFirstPage) currentlyActivePage = super.getActivePage();
        changeListeners.clear();
        i18nPage.dispose();
        i18nPage = new I18NPage(getContainer(), SWT.NONE, this);
        //keyTreeModel.dispose();
       // keyTreeModel = new AbstractKeyTreeModel(messagesBundleGroup);
        super.addPage(0, i18nPage);
        setPageText(0, Messages.editor_properties);
        setPageImage(0, UIUtils.getImage(UIUtils.IMAGE_RESOURCE_BUNDLE));
        if (currentlyActivePage == 0) {
            super.setActivePage(currentlyActivePage);
        }
    }

    /**
     * Saves the multi-page editor's document.
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        for (ITextEditor textEditor : textEditorsIndex) {
            textEditor.doSave(monitor);
        }

        try { // [alst] remove in near future
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        updateSelectedKey = true;

        RBManager instance = RBManager.getInstance(messagesBundleGroup
                .getProjectName());

        refreshKeyTreeModel(); // keeps editor and I18NPage in sync

        instance.fireEditorSaved();

        // // maybe new init?
    }

    protected void refreshKeyTreeModel() {
        String selectedKey = getSelectedKey(); // memorize

        if (messagesBundleGroup == null) {
            messagesBundleGroup = MessagesBundleGroupFactory.createBundleGroup(
                    (IEditorSite) getSite(), file);
        }

        AbstractKeyTreeModel oldModel = this.keyTreeModel;
        this.keyTreeModel = new AbstractKeyTreeModel(messagesBundleGroup);

        for (IMessagesEditorChangeListener listener : changeListeners) {
            listener.keyTreeModelChanged(oldModel, this.keyTreeModel);
        }

        i18nPage.getTreeViewer().expandAll();

        if (selectedKey != null) {
            setSelectedKey(selectedKey);
        }
    }
    
    public void updateBundleGroup(){
		messagesBundleGroup = MessagesBundleGroupFactory.createBundleGroup((IEditorSite) getSite(), file);
		AbstractKeyTreeModel oldModel = this.keyTreeModel;
		this.keyTreeModel = new AbstractKeyTreeModel(messagesBundleGroup);
		this.markers = new MessagesEditorMarkers(messagesBundleGroup);
		for (IMessagesEditorChangeListener listener : changeListeners) {
			listener.keyTreeModelChanged(oldModel, this.keyTreeModel);
		}
		i18nPage.getTreeViewer().expandAll();
    }

    /**
     * @see org.eclipse.ui.ISaveablePart#doSaveAs()
     */
    @Override
    public void doSaveAs() {
        // Save As not allowed.
    }

    /**
     * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
     * Change current page based on locale. If there is no editors associated
     * with current locale, do nothing.
     * 
     * @param locale
     *            locale used to identify the page to change to
     */
    public void setActivePage(Locale locale) {
        int index = localesIndex.indexOf(locale);
        if (index > -1) {
            setActivePage(index + 1);
        }
    }

    /**
     * @see org.eclipse.ui.ide.IGotoMarker#gotoMarker(org.eclipse.core.resources.IMarker)
     */
    public void gotoMarker(IMarker marker) {
        IResource resource = marker.getResource();
        Locale[] locales = messagesBundleGroup.getLocales();
        for (int i = 0; i < locales.length; i++) {
            IMessagesResource messagesResource = ((MessagesBundle) messagesBundleGroup
                    .getMessagesBundle(locales[i])).getResource();
            if (messagesResource instanceof EclipsePropertiesEditorResource) {
                EclipsePropertiesEditorResource propFile = (EclipsePropertiesEditorResource) messagesResource;
                if (resource.equals(propFile.getResource())) {
                    // ok we got the locale.
                    // try to open the master i18n page and select the
                    // corresponding key.
                    try {
                        String key = (String) marker
                                .getAttribute(IMarker.LOCATION);
                        if (key != null && key.length() > 0) {
                            getI18NPage().selectLocale(locales[i]);
                            setActivePage(0);
                            setSelectedKey(key);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();// something better.s
                    }
                    // it did not work... fall back to the text editor.
                    setActivePage(locales[i]);
                    IDE.gotoMarker((IEditorPart) propFile.getSource(), marker);
                    break;
                }
            }
        }
        // }
    }

    /**
     * Calculates the contents of page GUI page when it is activated.
     */
    @Override
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
        if (newPageIndex != 0) { // if we just want the default page -> == 1
            setSelection(newPageIndex);
        } else if (newPageIndex == 0 && updateSelectedKey) {
            // TODO: find better way
            for (IMessagesBundle bundle : messagesBundleGroup
                    .getMessagesBundles()) {
                RBManager.getInstance(messagesBundleGroup.getProjectName())
                        .fireResourceChanged(bundle);
            }
            updateSelectedKey = false;
        }

        // if (newPageIndex == 0) {
        // resourceMediator.reloadProperties();
        // i18nPage.refreshTextBoxes();
        // }
    }

    protected void setSelection(int newPageIndex) {
        ITextEditor editor = textEditorsIndex.get(--newPageIndex);
        String selectedKey = getSelectedKey();
        if (selectedKey != null) {
            if (editor.getEditorInput() instanceof FileEditorInput) {
                FileEditorInput input = (FileEditorInput) editor
                        .getEditorInput();
                try {
                    IFile file = input.getFile();
                    file.refreshLocal(IResource.DEPTH_ZERO, null);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(file.getContents()));
                    String line = "";
                    int selectionIndex = 0;
                    boolean found = false;

                    while ((line = reader.readLine()) != null) {
                        int index = line.indexOf('=');
                        if (index != -1) {
                            if (selectedKey.equals(line.substring(0, index)
                                    .trim())) {
                                found = true;
                                break;
                            }
                        }
                        selectionIndex += line.length() + 2; // + \r\n
                    }

                    if (found) {
                        editor.selectAndReveal(selectionIndex, 0);
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Is the given file a member of this resource bundle.
     * 
     * @param file
     *            file to test
     * @return <code>true</code> if file is part of bundle
     */
    public boolean isBundleMember(IFile file) {
        // return resourceMediator.isResource(file);
        return false;
    }

    protected void closeIfAreadyOpen(IEditorSite site, IFile file) {
        IWorkbenchPage[] pages = site.getWorkbenchWindow().getPages();
        for (int i = 0; i < pages.length; i++) {
            IWorkbenchPage page = pages[i];
            IEditorReference[] editors = page.getEditorReferences();
            for (int j = 0; j < editors.length; j++) {
                IEditorPart editor = editors[j].getEditor(false);
                if (editor instanceof AbstractMessagesEditor) {
                    AbstractMessagesEditor rbe = (AbstractMessagesEditor) editor;
                    if (rbe.isBundleMember(file)) {
                        page.closeEditor(editor, true);
                    }
                }
            }
        }
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        for (IMessagesEditorChangeListener listener : changeListeners) {
            listener.editorDisposed();
        }
        if (i18nPage != null) i18nPage.dispose();
        for (ITextEditor textEditor : textEditorsIndex) {
            textEditor.dispose();
        }

        disposeRAP();
    }

    /**
     * @return Returns the selectedKey.
     */
    public String getSelectedKey() {
        return selectedKey;
    }

    /**
     * @param selectedKey
     *            The selectedKey to set.
     */
    public void setSelectedKey(String activeKey) {
        if ((selectedKey == null && activeKey != null)
                || (selectedKey != null && activeKey == null)
                || (selectedKey != null && !selectedKey.equals(activeKey))) {
            String oldKey = this.selectedKey;
            this.selectedKey = activeKey;
            for (IMessagesEditorChangeListener listener : changeListeners) {
                listener.selectedKeyChanged(oldKey, activeKey);
            }
        }
    }

    public void addChangeListener(IMessagesEditorChangeListener listener) {
        changeListeners.add(0, listener);
    }

    public void removeChangeListener(IMessagesEditorChangeListener listener) {
        changeListeners.remove(listener);
    }

    public Collection<IMessagesEditorChangeListener> getChangeListeners() {
        return changeListeners;
    }

    /**
     * @return Returns the messagesBundleGroup.
     */
    public MessagesBundleGroup getBundleGroup() {
        return messagesBundleGroup;
    }

    /**
     * @return Returns the keyTreeModel.
     */
    public AbstractKeyTreeModel getKeyTreeModel() {
        return keyTreeModel;
    }

    /**
     * @param keyTreeModel
     *            The keyTreeModel to set.
     */
    public void setKeyTreeModel(AbstractKeyTreeModel newKeyTreeModel) {
        if ((this.keyTreeModel == null && newKeyTreeModel != null)
                || (keyTreeModel != null && newKeyTreeModel == null)
                || (!keyTreeModel.equals(newKeyTreeModel))) {
            AbstractKeyTreeModel oldModel = this.keyTreeModel;
            this.keyTreeModel = newKeyTreeModel;
            for (IMessagesEditorChangeListener listener : changeListeners) {
                listener.keyTreeModelChanged(oldModel, newKeyTreeModel);
            }
        }
    }

    public I18NPage getI18NPage() {
        return i18nPage;
    }

    /**
     * one of the SHOW_* constants defined in the
     * {@link IMessagesEditorChangeListener}
     */
    private int showOnlyMissingAndUnusedKeys = IMessagesEditorChangeListener.SHOW_ALL;

    /**
     * @return true when only unused and missing keys should be displayed. flase
     *         by default.
     */
    public int isShowOnlyUnusedAndMissingKeys() {
        return showOnlyMissingAndUnusedKeys;
    }

    public void setShowOnlyUnusedMissingKeys(int showFlag) {
        showOnlyMissingAndUnusedKeys = showFlag;
        for (IMessagesEditorChangeListener listener : getChangeListeners()) {
            listener.showOnlyUnusedAndMissingChanged(showFlag);
        }
    }

    @Override
    public Object getAdapter(Class adapter) {
        Object obj = super.getAdapter(adapter);
        if (obj == null) {
            if (IContentOutlinePage.class.equals(adapter)) {
                return (outline);
            }
        }
        return (obj);
    }

    public ITextEditor getTextEditor(Locale locale) {
        int index = localesIndex.indexOf(locale);
        return textEditorsIndex.get(index);
    }

    // Needed for RAP, otherwise super implementation of getTitleImage always
    // returns
    // same image with same device and same session context, and when this
    // session ends
    // -> NPE at org.eclipse.swt.graphics.Image.getImageData(Image.java:348)
    @Override
    public Image getTitleImage() {
        // create new image with current display
        return UIUtils.getImageDescriptor(UIUtils.IMAGE_RESOURCE_BUNDLE)
                .createImage();
    }

    public void setTitleName(String name) {
        setPartName(name);
    }

    abstract public void setEnabled(boolean enabled);

    abstract protected void initRAP();

    abstract protected void disposeRAP();

    abstract protected IMessagesBundleGroupListener getMsgBundleGroupListner();
}
