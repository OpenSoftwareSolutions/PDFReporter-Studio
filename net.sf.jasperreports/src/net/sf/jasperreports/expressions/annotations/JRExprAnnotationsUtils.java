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
package net.sf.jasperreports.expressions.annotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.builder.jdt.JDTUtils;
import net.sf.jasperreports.eclipse.classpath.OutputFolderClassLoader;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.functions.annotations.Function;
import net.sf.jasperreports.functions.annotations.FunctionCategories;
import net.sf.jasperreports.functions.annotations.FunctionCategory;
import net.sf.jasperreports.functions.annotations.FunctionMessagesBundle;
import net.sf.jasperreports.functions.annotations.FunctionParameter;
import net.sf.jasperreports.functions.annotations.FunctionParameters;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.osgi.util.NLS;

/**
 * Support class that works with the annotated Class(es).
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public final class JRExprAnnotationsUtils 
{

	private final JasperReportsContext jasperReportsContext;
	private final MessageUtil messageUtil;

	private JRExprAnnotationsUtils(JasperReportsContext jasperReportsContext)
	{
		this.jasperReportsContext = jasperReportsContext;
		this.messageUtil = MessageUtil.getInstance(jasperReportsContext);
	}
	
	
	/**
	 *
	 */
	public static JRExprAnnotationsUtils getInstance(JasperReportsContext jasperReportsContext)
	{
		return new JRExprAnnotationsUtils(jasperReportsContext);
	}
	
	
	/**
	 * @deprecated Replaced by {@link #getFunctionsList(Class)}.
	 */
	public static List<JRExprFunctionBean> getJRFunctionsList(Class<?> clazz)
	{
		return getInstance(DefaultJasperReportsContext.getInstance()).getFunctionsList(clazz);
	}
	
	private String getFunctionMessBundle(Class clazz) {
		String functionMessBundle = "jasperreports_messages";
		if(clazz.getPackage()!=null){
			functionMessBundle = clazz.getPackage().getName() + ".jasperreports_messages";//FIXMEFUNCT use constant
		}
		return functionMessBundle;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	private JRExprFunctionBean createJRFunction(List<Method> methods, Class<?> clazz) 
	{
		JRExprFunctionBean funct = new JRExprFunctionBean(clazz.getCanonicalName());
		// The first instance is the one annotated with @JRFunction
		// that maintains all the necessary infos to prepare the skeleton of the function bean
		Method first = methods.get(0);
		funct.setId(first.getName());
		funct.setReturnType(first.getReturnType());

		String functionMessBundle = getFunctionMessBundle(clazz);
		FunctionMessagesBundle functionMessBundleAnn = first.getAnnotation(FunctionMessagesBundle.class);
		if (functionMessBundleAnn == null)
		{
			functionMessBundleAnn = clazz.getAnnotation(FunctionMessagesBundle.class);
		}
		if (functionMessBundleAnn != null)
		{
			functionMessBundle = functionMessBundleAnn.value();
		}

		String functionName = null;
		String functionDescription = null;
		
		Function newFunctionAnnotation = first.getAnnotation(Function.class);
		if (newFunctionAnnotation == null)
		{
			JRExprFunction functionAnnotation = first.getAnnotation(JRExprFunction.class);
			functionName = functionAnnotation.name();
			functionDescription = functionAnnotation.description();
		}
		else
		{
			functionName = newFunctionAnnotation.value();
			
			String name = messageUtil.getMessageProvider(functionMessBundle).getMessage(clazz.getName() + "." + funct.getId() + ".name", null, Locale.getDefault());//FIXMEFUNCT provide locale
			if (name != null)
			{
				functionName = name;
			}
			String description = messageUtil.getMessageProvider(functionMessBundle).getMessage(clazz.getName() + "." + funct.getId() + ".description", null, Locale.getDefault());//FIXMEFUNCT provide locale
			if (description != null)
			{
				functionDescription = description;
			}
		}

		funct.setName(functionName);
		funct.setDescription(functionDescription);
		
		JRExprFunctionCategories functionCategoriesAnnotation = first.getAnnotation(JRExprFunctionCategories.class);
		if (functionCategoriesAnnotation == null)
		{
			FunctionCategories newFunctionCategoriesAnnotation = first.getAnnotation(FunctionCategories.class);
			
			if (newFunctionCategoriesAnnotation == null)
			{
				newFunctionCategoriesAnnotation = clazz.getAnnotation(FunctionCategories.class);
			}
			
			if (newFunctionCategoriesAnnotation != null)
			{
				Class<?>[] categories = newFunctionCategoriesAnnotation.value();
				for (Class<?> categoryClass : categories)
				{
					String categoryId = categoryClass.getName();
					String categoryName = null;
					String categoryDescription = null;

					FunctionCategory functionCategory = categoryClass.getAnnotation(FunctionCategory.class);
					if (functionCategory != null) 
					{
						String id = functionCategory.value();
						if (id != null && id.trim().length() > 0)
						{
							categoryId = id;
						}
						categoryName = functionCategory.value();
					}
					
					String categoryMessBundle = getFunctionMessBundle(categoryClass);
					FunctionMessagesBundle categMessBundleAnn = categoryClass.getAnnotation(FunctionMessagesBundle.class);
					if (categMessBundleAnn != null)
					{
						categoryMessBundle = categMessBundleAnn.value();
					}

					String name = messageUtil.getMessageProvider(categoryMessBundle).getMessage(categoryId + ".name", null, Locale.getDefault());//FIXMEFUNCT provide locale and optimize by getting localized provider everywhere
					if (name != null)
					{
						categoryName = name;
					}
					String description = messageUtil.getMessageProvider(categoryMessBundle).getMessage(categoryId + ".description", null, Locale.getDefault());//FIXMEFUNCT provide locale
					if (description != null)
					{
						categoryDescription = description;
					}
					
					JRExprFunctionCategoryBean categDescriptor = new JRExprFunctionCategoryBean();
					categDescriptor.setId(categoryId);
					categDescriptor.setName(categoryName);
					categDescriptor.setDescription(categoryDescription);
					funct.getCategories().add(categDescriptor);
				}
			}
		}
		else
		{
			for (String category : functionCategoriesAnnotation.value())
			{
				JRExprFunctionCategoryBean categDescriptor = new JRExprFunctionCategoryBean();
				categDescriptor.setId(category);
				categDescriptor.setName(messageUtil.getMessageProvider("MessagesBundle").getMessage("Category." + category + ".display", null, Locale.getDefault()));
				categDescriptor.setDescription(messageUtil.getMessageProvider("MessagesBundle").getMessage("Category." + category + ".description", null, Locale.getDefault()));
				funct.getCategories().add(categDescriptor);
			}
		}
		
		FunctionParameters newParametersAnnotation = first.getAnnotation(FunctionParameters.class);
		if(newParametersAnnotation == null)
		{
			JRExprFunctionParameters parametersAnnotation = first.getAnnotation(JRExprFunctionParameters.class);
			if(parametersAnnotation != null)
			{
				for(JRExprFunctionParameter param : parametersAnnotation.value()){
					// Get basic info from the annotation
					JRExprFunctionParameterBean paramDescriptor=new JRExprFunctionParameterBean();
					paramDescriptor.setName(param.name());
					paramDescriptor.setDescription(param.description());
					funct.getParameters().add(paramDescriptor);
				}
			}
		}
		else
		{
			for(FunctionParameter param : newParametersAnnotation.value())
			{
				// Get basic info from the annotation
				JRExprFunctionParameterBean paramDescriptor = new JRExprFunctionParameterBean();
				String parameterId = param.value();
				String parameterName = null;
				String parameterDescription = null;
				if (parameterId != null && parameterId.trim().length() > 0)
				{
					String name = messageUtil.getMessageProvider(functionMessBundle).getMessage(clazz.getName() + "." + funct.getId() + "." + parameterId + ".name", null, Locale.getDefault());//FIXMEFUNCT provide locale
					if (name != null)
					{
						parameterName = name;
					}
					String description = messageUtil.getMessageProvider(functionMessBundle).getMessage(clazz.getName() + "." + funct.getId() + "." + parameterId + ".description", null, Locale.getDefault());//FIXMEFUNCT provide locale
					if (description != null)
					{
						parameterDescription = description;
					}
				}
				paramDescriptor.setName(parameterName);
				paramDescriptor.setDescription(parameterDescription);
				funct.getParameters().add(paramDescriptor);
			}
		}
		
		// Now computes the mandatory and cardinality of the parameters
		int paramIndex=0;
		int paramsNum=funct.getParameters().size();
		for (int i=0; i<methods.size() && paramIndex<paramsNum; i++){
			Method currMethod = methods.get(i);
			Class<?>[] parameterTypes = currMethod.getParameterTypes();
			boolean isOptional=(i==0)?false:true; // all parameters of the first method are for sure mandatory			
			for(int j=paramIndex;j<parameterTypes.length;j++,paramIndex++){
				boolean isMulti=parameterTypes[j].isArray();
				JRExprFunctionParameterBean paramFunctBean = funct.getParameters().get(paramIndex);
				if(paramFunctBean!=null){
					paramFunctBean.setOptional(isOptional);
					paramFunctBean.setMulti(isMulti);
					paramFunctBean.setParameterType(parameterTypes[j]);
				}
				else{
					// ERROR params number mismatch in descriptions/names/types
					// TODO issue Exception or log error (?!)
				}
			}
		}
		
		return funct;
	}

	/**
	 * Creates a support map that maintain for each method name (name as key) 
	 * a list of Methods found in the Class.
	 */
	@SuppressWarnings("deprecation")
	private Map<String,List<Method>> buildAnnotatedMethodsCache(Class<?> clazz) 
	{
		ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
		IProject currProj = JDTUtils.getCurrentProjectForOpenEditor();
		String classCanonicalName = clazz.getCanonicalName();
		if(currProj!=null && 
				!classCanonicalName.startsWith("net.sf.jasperreports.functions.standard")){
			// Try to reload a fresh new instance of the Class instance.
			// Useful for code being developed directly inside JSS.
			// We skip the standard ones contributed via jar.
			OutputFolderClassLoader reloaderCL = new OutputFolderClassLoader(JavaCore.create(currProj), ctxClassLoader);
			Class<?> reloadedClazz = reloaderCL.reloadClass(clazz.getCanonicalName());
			if(reloadedClazz!=null) {
				clazz = reloadedClazz;
			}
		}
		
		Map<String,List<Method>> methodsByNameMap=new HashMap<String, List<Method>>();
				
		// First round locate all methods with the JRFunction annotation
		// in order to have a list of what will be the functions
		for (Method m : clazz.getMethods()){
			JRExprFunction jrFunctionAnn = m.getAnnotation(JRExprFunction.class);
			Function jrNewFunctionAnn = m.getAnnotation(Function.class);
			if (jrNewFunctionAnn != null || jrFunctionAnn != null)
			{
				String methodName = m.getName();
				List<Method> methods=methodsByNameMap.get(methodName);
				if(methods==null){
					methods=new ArrayList<Method>();
				}
				methods.add(m);
				methodsByNameMap.put(methodName, methods);
			}
		}
		
		// After that enrich the map with the remaining methods that have the same function
		// name but a different list of parameters
		for (Method m : clazz.getMethods()){
			JRExprFunction jrFunctionAnn = m.getAnnotation(JRExprFunction.class);
			Function jrNewFunctionAnn = m.getAnnotation(Function.class);
			if (jrNewFunctionAnn == null || jrFunctionAnn == null)
			{
				String methodName = m.getName();
				List<Method> methods=methodsByNameMap.get(methodName);
				if (methods!=null){
					methods.add(m);
				}
			}
		}
		
		
		return methodsByNameMap;
	}

	/**
	 * Retrieves the list of functions contributed in the specified class reference.
	 * 
	 * <p>
	 * The method seeks for annotated methods with the annotation {@link JRExprFunction} 
	 * in order to build the basic set of functions, and then scan for similar ones to
	 * decide which parameters are mandatory and which optional. 
	 * 
	 * @param clazz the class reference that is supposed to contain expression functions
	 * @return a list of JR expression functions
	 */
	public List<JRExprFunctionBean> getFunctionsList(Class<?> clazz)
	{
		Map<String, List<Method>> methodsCache = buildAnnotatedMethodsCache(clazz);
		
		List<JRExprFunctionBean> functionsList=new ArrayList<JRExprFunctionBean>();
		for (String functionName : methodsCache.keySet()){
			try {
				JRExprFunctionBean jrFunction = createJRFunction(methodsCache.get(functionName), clazz);
				functionsList.add(jrFunction);
			}
			catch (Exception ex){
				JasperReportsPlugin.getDefault().logError(
						NLS.bind("Unable to create the function ''{0}'' from class ''{1}''. See full stacktrace.",functionName,clazz.getCanonicalName()), ex);
			}
		}
		
		return functionsList;
	}

}
