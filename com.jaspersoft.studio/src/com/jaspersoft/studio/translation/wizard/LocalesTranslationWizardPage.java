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
package com.jaspersoft.studio.translation.wizard;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceCache;

import com.jaspersoft.studio.ConfigurationPathProvider;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.translation.ExtendedTranslationInformation;
import com.jaspersoft.studio.translation.FlagLocaleSelector;
import com.jaspersoft.studio.translation.ImageLocale;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;
import com.jaspersoft.translation.resources.ITranslationResource;

/**
 * In this step the user can specify the languages that the translation project
 * provide. 
 * He can also specify the path where the exported fragment will be placed. 
 * Both this fields are initialized, the first with the languages found by
 * inspecting the translation project. The second one by searching the directory
 * of Jaspersoft Studio where the files should be placed. This can be found
 * only in the rcp version of the product, so in case of the plugin version this
 * field is empty
 * 
 * @author Orlandin Marco
 *
 */
public class LocalesTranslationWizardPage extends JSSHelpWizardPage {

	/**
	 * String to identify the  default locale
	 */
	public static final String DEFAULT_LOCALE = "[Default]"; //$NON-NLS-1$

	/**
	 * Text area where the destination path can be placed
	 */
	private Text filePath;

	/**
	 * Button used to add a new locale to the exported list
	 */
	private Button addButton;

	/**
	 * Button used to remove a locale from the exported list
	 */
	private Button removeButton;

	/**
	 * List with all the exported locales
	 */
	private Table bundleLocalesList;

	/**
	 * Component used to select the locale from a combo with all the available locales
	 */
	private FlagLocaleSelector localeSelector;
	
	/**
	 * Variable where the destination path is saved before the component is disposed
	 */
	private String destinationPath = ""; //$NON-NLS-1$
	
	/**
	 * Variable where the languages selected by the user are saved before the list component is disposed
	 */
	private List<ImageLocale> selectedLanguages = new ArrayList<ImageLocale>();
	
	/**
	 * Cache where the images used by the items are stored and disposed at the end
	 */
	private ResourceCache imagesCache = new ResourceCache();
	
	public LocalesTranslationWizardPage() {
		super(Messages.LocalesTranslationWizardPage_dialogTitle);
		setTitle(Messages.LocalesTranslationWizardPage_pageTitle);
		setMessage(Messages.LocalesTranslationWizardPage_pageMessage);
	}

	/**
	 * Search for the directory where the fragments should be placed to allow JSS to load
	 * them. The path should be something like JSS installation folder\dropins\eclipse\plugins.
	 * The path can be retrieved automatically only on the rcp version of jss
	 * 
	 * @return the folder path where to place the plugins to allow jss to load them if it 
	 * can be retrieved, otherwise an empty string
	 */
	private String getPluginsFolder() {
		String separator =  System.getProperty("file.separator");//$NON-NLS-1$
		try {
			String path = new URL(ConfigurationPathProvider.getPath()).getFile();
			File destination = new File(path).getParentFile();
			destination = new File(destination.toString() + separator + "dropins" + separator + "eclipse" + separator + "plugins"); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			if (destination.exists()) return destination.getAbsolutePath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * Check if the filename has a locale as terminal part, in this 
	 * case the locale is returned. Otherwise it will return the default locale
	 * 
	 * @param fileName original filename
	 * @return a Locale that can be the one identified on the file name, or the default 
	 * one if the filename has no locale in it
	 */
	private Locale getLocaleFromFilename(String fileName){
		if (fileName.endsWith(".properties")) {//$NON-NLS-1$
			int propertiesIndex = fileName.toLowerCase().lastIndexOf(".properties");//$NON-NLS-1$
			fileName = fileName.substring(0, propertiesIndex);
		}
		for (Locale loc : Locale.getAvailableLocales()){
			if (fileName.endsWith("_"+loc.toString())) { //$NON-NLS-1$
				return loc;
			}
		}
		return Locale.getDefault();
	}
	
	
	/**
	 * Given a locale search if there is an icon image that represent it. If an image 
	 * is found it is returned, otherwise return null. the icon is searched using
	 * the locale country and language (first the language, and if it is not found it use
	 * the country)
	 * 
	 * @param loc locale used to search an associated icon
	 * @return image that represent the locale, or null if it is not found
	 */
	private ImageData getImageForLocale(Locale loc){
		String newLocaleLang = loc.getLanguage();
		String newLocaleCountry = loc.getCountry();
		String key = "icons/flags/"+newLocaleLang+".png";
		ImageDescriptor descriptor = JaspersoftStudioPlugin.getInstance().getImageDescriptor(key); //$NON-NLS-1$//$NON-NLS-2$
		if (descriptor == null) {
			key = "icons/flags/"+newLocaleCountry+".png";
			descriptor = JaspersoftStudioPlugin.getInstance().getImageDescriptor(key);
		}
		if (descriptor == null) return null;
		else {
			return descriptor.getImageData();
		}
	}
	
	/**
	 * Search recursively a resource and its children to found all the locales used inside the resources, basing the 
	 * search on the name of the file resources. It keep two has set to avoid to reanalyze files with the same name
	 * and to avoid to found more than once the same locale.
	 * 
	 * @param analyzedFiles hash set containing the analyzed filenames, a name is analyzed only if it isn't into this set
	 * @param foundedLocales locales found, using an hashset instead of a list is useful to efficiently avoid to add more times the same locale 
	 * @param actualResource resource actually analyzed, if it is a file it's name use used the find a locale, otherwise the method is called 
	 * recursively on all the children of the resource
	 */
	private void recursiveResourceExplorer(HashSet<String> analyzedFiles, HashSet<Locale> foundedLocales, ITranslationResource actualResource){
		if (actualResource.isFile() && !analyzedFiles.contains(actualResource.getResourceName())){
			Locale fileLocale = getLocaleFromFilename(actualResource.getResourceName());
			analyzedFiles.add(actualResource.getResourceName());
			if (!foundedLocales.contains(fileLocale)) foundedLocales.add(fileLocale);
		} else {
			for (ITranslationResource child : actualResource.getChildren())
				recursiveResourceExplorer(analyzedFiles, foundedLocales, child);
		}
	}
	
	/**
	 * Get a list of the image locales that are used from the resources actually selected
	 * by the user to be exported. The search of the locale is based on the filename of 
	 * the exported file resources
	 * 
	 * @return a not null list of image locale
	 */
	private List<ImageLocale> getSelectedLocales(){
		HashSet<String> analyzedFiles = new HashSet<String>();
		HashSet<Locale> foundedLocales = new HashSet<Locale>();
		List<ExtendedTranslationInformation> selectedInfos = ((GenerateFragmentWizard)getWizard()).getSelectedResources();
		//For every plugin translation
		for(ExtendedTranslationInformation info : selectedInfos){
			//and for every resource inside the plugin translation
			for(ITranslationResource resource : info.getResources()){
				recursiveResourceExplorer(analyzedFiles, foundedLocales, resource);
			}
		}
		
		List<ImageLocale> result = new ArrayList<ImageLocale>();
		for (Locale locale : foundedLocales){
			result.add(new ImageLocale(locale, getImageForLocale(locale)));
		}
		return result;	
	}
	
	/**
	 * Method used to initialize the locales list by identifying the locales from the actually
	 * selected resources. 
	 */
	public void initializeSelectedLocales(){
		bundleLocalesList.removeAll();
		List<ImageLocale> alreadySelectedLocales = getSelectedLocales();
		for(ImageLocale loc : alreadySelectedLocales){
			TableItem item = new TableItem(bundleLocalesList, SWT.NONE);
			item.setText(loc.getLocale().toString());
			item.setData(loc.getLocale());
			item.setImage(imagesCache.getImage(loc.getImage()));
		}
		setAddButtonState();
	}
	
	/**
	 * When the user advance to the next page the info into the widgets are stored 
	 * so the can be recovered by the parent wizard and used to do the finish phase, even
	 * if the widgets of the page are disposed
	 */
	@Override
	public IWizardPage getNextPage() {
		destinationPath = filePath.getText();
		selectedLanguages = new ArrayList<ImageLocale>();
		for(TableItem item : bundleLocalesList.getItems()){
			ImageData itemImageData = null;
			if (item.getImage() != null) itemImageData = item.getImage().getImageData();
			ImageLocale exportedLocale = new ImageLocale((Locale)item.getData(), itemImageData);
			selectedLanguages.add(exportedLocale);
		}
		return super.getNextPage();
	}
	
	/**
	 * Return the path where the fragment should be placed
	 * 
	 * @return a not null string representing a valid filesystem path
	 */
	public String getDestinationPath(){
		return destinationPath;
	}
	
	/**
	 * Return a list of locale with images that are the entry for the language switcher essentially
	 * 
	 * @return a not null and not void list of selected languages
	 */
	public List<ImageLocale> getSelectedLanguages(){
		return selectedLanguages;
	}
	
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_INSTALL_TRANSLATION_STEP1;
	}
	
	/**
	 * Ensures that list of locales is not void and the the path is a valid and existing path
	 */
	protected void dialogChanged() {
		String fileName = filePath.getText();
		if (fileName.length() == 0 || !(new File(fileName).exists())) {
			updateStatus(Messages.LocalesTranslationWizardPage_errorFolder, IMessageProvider.ERROR);
			return;
		}
		if (bundleLocalesList.getItems().length==0) {
			updateStatus(Messages.LocalesTranslationWizardPage_errorLocales, IMessageProvider.ERROR);  
			return;
		}
		updateStatus(Messages.LocalesTranslationWizardPage_pageMessage, IMessageProvider.NONE);
	}
	
	/**
	 * Update the page complete status only if ther'arent error messages
	 * 
	 * @param message message to set in the page
	 * @param messageType type of the message
	 */
	protected void updateStatus(String message, int messageType) {
		setMessage(message, messageType);
		setPageComplete(messageType != IMessageProvider.ERROR);
	}
	
	/**
	 * Disable the add button when the sleected locale is already on the list
	 */
	protected void setAddButtonState() {
		int index = -1;
		for(int i=0; i<bundleLocalesList.getItemCount() && index == -1; i++){
			if (bundleLocalesList.getItem(i).getText().equals(getSelectedLocaleAsString())) index = i;
		}
		addButton.setEnabled(index == -1);
	}
	
	/**
	 * Gets a string representation of selected locale.
	 * 
	 * @return string representation of selected locale
	 */
	public String getSelectedLocaleAsString() {
		Locale selectedLocale = localeSelector.getSelectedLocale();
		if (selectedLocale != null) {
			return selectedLocale.toString();
		}
		return DEFAULT_LOCALE;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createLocalesSection(container);
		createPathSection(container);
		setControl(container);
	}

	/**
	 * Check for error when the dialog became visible
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) dialogChanged();
	}

	/**
	 * Create the text for the path and the button to open 
	 * the browse dialog 
	 * 
	 */
	private void createPathSection(Composite parent){
		Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout(3,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.LocalesTranslationWizardPage_destinationLabel);
		
		filePath = new Text(container, SWT.BORDER);
		filePath.setText(getPluginsFolder());
		filePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filePath.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Button browseButton = new Button(container, SWT.NONE);
		browseButton.setText(Messages.LocalesTranslationWizardPage_browseButton);
		browseButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        DirectoryDialog dlg = new DirectoryDialog(UIUtils.getShell());
        dlg.setFilterPath(filePath.getText());
        dlg.setText(Messages.LocalesTranslationWizardPage_browseDialogTitle);
        dlg.setMessage(Messages.LocalesTranslationWizardPage_browseDialogMessage);
        String dir = dlg.open();
        //it is not necessary to call the dialog changed method since this is 
        //called by the modify listener of the file path Text are if the text is set
        if (dir != null) {
        	filePath.setText(dir);
        }
      }
    });
	}
	
	

	/**
	 * Creates the part of this page that can be used to add a locale
	 * 
	 * @param parent parent container
	 */
	private void createLocalesSection(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);

		// Available locales
		createBottomAvailableLocalesComposite(container);

		// Buttons
		createBottomButtonsComposite(container);

		// Selected locales
		createBottomSelectedLocalesComposite(container);
	}
	
	/**
	 * Creates the part of this page where there is the list where selected locales are stored.
	 * 
	 * @param parent parent container
	 */
	private void createBottomSelectedLocalesComposite(Composite parent) {
		// Selected locales Group
		Group selectedGroup = new Group(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout = new GridLayout();
		layout.numColumns = 1;
		selectedGroup.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		selectedGroup.setLayoutData(gd);
		selectedGroup.setText("Selected locales"); //$NON-NLS-1$
		bundleLocalesList = new Table(selectedGroup, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		bundleLocalesList.setLayoutData(gd);
		bundleLocalesList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				removeButton.setEnabled(bundleLocalesList.getSelectionIndices().length != 0);
				setAddButtonState();
			}
		});
	}
	

	/**
	 * Creates the bottom part of this wizard where buttons to add/remove
	 * locales are located.
	 * 
	 * @param parent
	 *            parent container
	 */
	private void createBottomButtonsComposite(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		
		addButton = new Button(container, SWT.NULL);
		addButton.setText("Add   -->"); //$NON-NLS-1$
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				TableItem item = new TableItem(bundleLocalesList, SWT.NONE);
				item.setText(getSelectedLocaleAsString());
				item.setData(localeSelector.getSelectedLocale());
				item.setImage(imagesCache.getImage(localeSelector.getActualImage()));
				setAddButtonState();
				dialogChanged();
			}
		});

		removeButton = new Button(container, SWT.NULL);
		removeButton.setText("<-- Remove"); //$NON-NLS-1$
		removeButton.setEnabled(false);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				bundleLocalesList.remove(bundleLocalesList
						.getSelectionIndices());
				removeButton.setEnabled(false);
				setAddButtonState();
				dialogChanged();
			}
		});
	}


	/**
	 * Creates the bottom part of this wizard where locales can be chosen or
	 * created
	 * 
	 * @param parent
	 *            parent container
	 */
	private void createBottomAvailableLocalesComposite(Composite parent) {
		localeSelector = new FlagLocaleSelector(parent);
		localeSelector.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		localeSelector.addModifyListener(new ModifyListener() {
			
			private String actualLocale;
			
			private ImageData getImage(String newLocaleLang, String newLocaleCountry){
				actualLocale = newLocaleLang;
				String key = "icons/flags/"+newLocaleLang+".png";
				ImageDescriptor descriptor = JaspersoftStudioPlugin.getInstance().getImageDescriptor(key); 
				if (descriptor == null) {
					actualLocale = newLocaleCountry;
					key = "icons/flags/"+newLocaleCountry+".png";
					descriptor = JaspersoftStudioPlugin.getInstance().getImageDescriptor(key);
					actualLocale = newLocaleCountry;
				}
				if (descriptor != null){
					return descriptor.getImageData();
				}
				return null;
			}
			
			public void modifyText(ModifyEvent e) {
				setAddButtonState();
				String newLocaleLang = localeSelector.getLangText();
				String newLocaleCountry = localeSelector.getCountryText();
				actualLocale = localeSelector.getActualLocaleImage();
				if (!(newLocaleLang.equals(actualLocale) || newLocaleCountry.equals(actualLocale))){
					localeSelector.updateImage(getImage(newLocaleLang, newLocaleCountry), actualLocale);
				}
			}
		});
	}
	
	
	@Override
	public void dispose() {
		super.dispose();
		imagesCache.dispose();
	}
	
}
