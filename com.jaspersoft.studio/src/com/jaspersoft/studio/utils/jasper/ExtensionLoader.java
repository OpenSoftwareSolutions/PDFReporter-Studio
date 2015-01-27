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
package com.jaspersoft.studio.utils.jasper;

import java.util.HashMap;
import java.util.HashSet;

import net.sf.jasperreports.data.DataAdapterServiceFactory;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.export.GenericElementHandlerBundle;
import net.sf.jasperreports.engine.export.MatcherExportFilterMapping;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.query.QueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.scriptlets.ScriptletFactory;
import net.sf.jasperreports.engine.style.StyleProviderFactory;
import net.sf.jasperreports.functions.FunctionsBundle;
import net.sf.jasperreports.repo.PersistenceServiceFactory;
import net.sf.jasperreports.repo.RepositoryService;
import net.sf.jasperreports.util.SecretsProviderFactory;

/**
 * 
 * This class allow to preload the jr extensions to have 
 * them cached as soon as possible. Some extension are stored inside
 * the default context, so shared between reports, other are loaded for the a 
 * specified report since they are report dependent. 
 * 
 * @author Orlandin Marco
 *
 */
public class ExtensionLoader {
	
	/**
	 * Map with the common extension cached. Essentially these are the extensions from the
	 * DefaultJasperReportsContext.getInstance()
	 * 
	 */
	private static HashMap<Class<?>, Object> sharedExtensionCache = new HashMap<Class<?>, Object>();
	
	/**
	 * The extensions loaded for each report
	 */
	private static Class<?>[] reportDependentExtensionsKey = {
			FontFamily.class,
			RepositoryService.class,
			ComponentsBundle.class
	};
	
	/**
	 * The extensions loaded for the general context and cached in the sharedExtension Map
	 */
	private static Class<?>[] commonExensionKeys = {
		PersistenceServiceFactory.class,
		QueryExecuterFactoryBundle.class,
		//JRQueryExecuterFactoryBundle.class,
		MatcherExportFilterMapping.class,
		StyleProviderFactory.class,
		DataAdapterServiceFactory.class,
		ScriptletFactory.class,
		ParameterContributorFactory.class,
		SecretsProviderFactory.class,
		FunctionsBundle.class,
		FontFamily.class,
		RepositoryService.class,
		ComponentsBundle.class,
		GenericElementHandlerBundle.class
	};
	
	/**
	 * Keep track of the common extensions currently loading, if an extensions
	 * key is in the map means that its loading is started but not finished yet
	 */
	private static HashSet<Class<?>> loadingExtensions = new HashSet<Class<?>>();  

	/**
	 * Thread safe method to mark an extension key as currently loading
	 * 
	 * @param extensionKey key of the extension
	 */
	private static void setLoadingStart(Class<?> extensionKey) {
		synchronized (loadingExtensions) {
			loadingExtensions.add(extensionKey);
		}
	}
	
	/**
	 * Thread safe method to mark an extension key as not currently loading. The
	 * value of the extension is also stored inside the extension cache
	 * 
	 * @param extensionKey key of the extension
	 * @param the value of the loaded extension
	 */
	private static void setLoadingEnd(Class<?> extensionKey, Object loadedObject) {
		synchronized (loadingExtensions) {
			sharedExtensionCache.put(extensionKey, loadedObject);
			loadingExtensions.remove(extensionKey);
		}
	}
	
	/**
	 * Thread safe method to check if an extension key is currently loading
	 * 
	 * @param extensionKey key of the extension
	 * @return true if the extension is currently loading, false otherwise
	 */
	private static boolean isCurrentlyLoading(Class<?> extensionKey){
		synchronized (loadingExtensions) {
			return loadingExtensions.contains(extensionKey);
		}
	}
 
	/**
	 * Load the report dependent extensions on the specified context.
	 * Each extension is loaded inside an independent thread
	 * 
	 * @param context where the properties are loaded
	 */
 public static void loadExtension(final JasperReportsContext context){
	// timeStartLoading = System.currentTimeMillis();
	 for(Class<?> extensionKey : reportDependentExtensionsKey){
		 final Class<?> key = extensionKey;
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				context.getExtensions(key);
				//setLoadedExtenson(key);
			}
		}).start();
	 }
 }

 /**
  * Load the shared extensions inside the common context that can be 
  * Retrieved with DefaultJasperReportsContext.getInstance(). Each extension
  * is loaded inside an independent thread
  */
 public static void loadDefaultProperties(){
	 final DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
	 for(Class<?> extensionKey : commonExensionKeys){
		 final Class<?> key = extensionKey;
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				setLoadingStart(key);
				Object obj = context.getExtensions(key);
				setLoadingEnd(key, obj);
			}
		}).start();
	 }
 }
 
 /**
  * Should be called before someone load a common extension; if the extension is 
  * currently in a loading state the caller is blocked untie
  * the load is not complete. The check to see if the loading
  * is finished is done every amount of a fixed time (by default
  * 200 ms)
  * 
  * @param extensionKey the key of the extension
  */
 public static void waitIfLoading(Class<?> extensionKey){
	 int checkTime = 200;
	 while(isCurrentlyLoading(extensionKey)){
		 try {
			 //the extension is loading, wait 200ms and recheck if it was loaded
			Thread.sleep(checkTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	 }
 }
 
 /**
  * Return a value of the extension from the cache. This cached
  * value is the same returned by calling the getExtension method
  * on the DefaultJasperReportsContext.
  * The method check if the extension with the specific key is still loading, 
  * in this case wait until the loads end and then it return the result
  * 
  * @param extensionKey the key of the extension
  * @return the value of the extension or null if it isn't inside
  * the shared cache.
  */
 public static Object getSharedExtension(Class<?> extensionKey){
	 // This call avoid to load more than one time an extension because maybe the ExtensionLoader has a 
	 // thread already started for it, but still not completed and in the meantime another request for the
	 // same extensions arrive. With this code the loading of the extension is paused until the thread complete 
	 waitIfLoading(extensionKey);
	 return sharedExtensionCache.get(extensionKey);
 }
 
	//CODE USED TO TRACK THE PERFORMANCE OF THE EXTENSIONS PRECACHE
	
/*private HashMap<Class<?>, Boolean> extensionLoaded = new HashMap<Class<?>, Boolean>();

private long timeStartLoading = -1;

private void checkAllLoaded(){
	 boolean allFound = true;
	 for(Class<?> extensionKey : reportDependentExtensionsKey){
		 if (!extensionLoaded.containsKey(extensionKey)){
			 allFound = false;
			 break;
		 }
	 }
	 if (allFound){
		 long loadingTime = System.currentTimeMillis() - timeStartLoading;
		 System.out.println("time required multi thread "+ loadingTime);
	 }
}

private synchronized void setLoadedExtenson(Class<?> extensionKey){
	 extensionLoaded.put(extensionKey, true);
	 checkAllLoaded();
}*/

}
