package org.eclipse.babel.core.refactoring;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.eclipse.babel.core.configuration.DirtyHack;
import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.babel.core.refactoring.KeyRefactoringDialog.DialogConfiguration;
import org.eclipse.babel.core.util.PDEUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

public class StandardRefactoring implements IRefactoringService {
	
	public static String defaultLocaleTag = "[default]"; 
	
	public static IProject getProject(String projectName){
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject p : projects) {
			if (p.getName().equalsIgnoreCase(projectName)) {
				// check if the projectName is a fragment and return the manager
				// for the host
				if (PDEUtils.isFragment(p)) {
					return PDEUtils.getFragmentHost(p);
				} else {
					return p;
				}
			}
		}
		return null;
	}
	

	public Set<Locale> getProvidedLocales(String bundleName, IProject project) {
		RBManager instance = RBManager.getInstance(project);

		Set<Locale> locales = new HashSet<Locale>();
		IMessagesBundleGroup group = instance
				.getMessagesBundleGroup(bundleName);
		if (group == null) {
			return locales;
		}

		for (IMessagesBundle bundle : group.getMessagesBundles()) {
			locales.add(bundle.getLocale());
		}
		return locales;
	}
	
    public static Locale getLocaleByDisplayName(Set<Locale> locales,
            String displayName) {
        for (Locale l : locales) {
            String name = l == null ? defaultLocaleTag
                    : l.getDisplayName();
            if (name.equals(displayName) || (name.trim().length() == 0 && displayName.equals(defaultLocaleTag))) {
                return l;
            }
        }

        return null;
    }

	
	@Override
	public void refactorKey(String projectName, String resourceBundleId, String selectedLocale, String oldKey, String newKey, String enumName) {
		// contains file and line

		IProject project = getProject(projectName);

		// change backend
		RBManager rbManager = RBManager.getInstance(projectName);
		IMessagesBundleGroup messagesBundleGroup = rbManager.getMessagesBundleGroup(resourceBundleId);

		DirtyHack.setFireEnabled(false);
		if (KeyRefactoringDialog.ALL_LOCALES.equals(selectedLocale)) {
			messagesBundleGroup.renameMessageKeys(oldKey, newKey);

		} else {
			IMessagesBundle messagesBundle = messagesBundleGroup.getMessagesBundle(getLocaleByDisplayName(getProvidedLocales(resourceBundleId, project),selectedLocale));
			messagesBundle.renameMessageKey(oldKey, newKey);
		}
		DirtyHack.setFireEnabled(true);
		rbManager.writeToFile(rbManager.getMessagesBundleGroup(resourceBundleId));

		//rbManager.fireEditorChanged(); // notify Resource Bundle View
		
		// show the summary dialog
		//List<String> changeSet = new ArrayList<String>();
		//KeyRefactoringSummaryDialog summaryDialog = new KeyRefactoringSummaryDialog(Display.getDefault().getActiveShell());
		//DialogConfiguration config = summaryDialog.new DialogConfiguration();
		//config.setPreselectedKey(oldKey);
		//config.setNewKey(newKey);
		//config.setPreselectedBundle(resourceBundleId);
		//config.setProjectName(projectName);

		//summaryDialog.setDialogConfiguration(config);
		//summaryDialog.setChangeSet(changeSet);

		//summaryDialog.open();
	}


	@Override
	public void openRefactorDialog(String projectName, String resourceBundleId,String oldKey, String enumName) {
		KeyRefactoringDialog dialog = new KeyRefactoringDialog(Display.getDefault().getActiveShell());

		DialogConfiguration config = dialog.new DialogConfiguration();
		config.setPreselectedKey(oldKey);
		config.setPreselectedBundle(resourceBundleId);
		config.setProjectName(projectName);

		dialog.setDialogConfiguration(config);

		if (dialog.open() != InputDialog.OK) {
			return;
		}

		refactorKey(projectName, resourceBundleId, config.getSelectedLocale(),oldKey, config.getNewKey(), enumName);
	}

	@Override
	public void openRefactorDialog(IFile file, int selectionOffset) {
	}
}
