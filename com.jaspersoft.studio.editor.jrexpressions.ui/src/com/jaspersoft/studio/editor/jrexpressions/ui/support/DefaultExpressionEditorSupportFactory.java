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
package com.jaspersoft.studio.editor.jrexpressions.ui.support;

import net.sf.jasperreports.engine.JRExpression;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.jaspersoft.studio.editor.expression.ExpressionEditorSupport;
import com.jaspersoft.studio.editor.expression.IExpressionEditorSupportFactory;
import com.jaspersoft.studio.editor.jrexpressions.ui.JRExpressionsActivator;
import com.jaspersoft.studio.editor.jrexpressions.ui.JRExpressionsUIPlugin;
import com.jaspersoft.studio.editor.jrexpressions.ui.messages.Messages;

/**
 * This is the default support factory for the {@link JRExpression} editor,
 * provided by Jaspersoft Studio.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class DefaultExpressionEditorSupportFactory implements IExpressionEditorSupportFactory {
	
	public DefaultExpressionEditorSupportFactory() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.jaspersoft.studio.editor.expression.IExpressionEditorSupportFactory#getExpressionEditorSupport(java.lang.String)
	 */
	public ExpressionEditorSupport getExpressionEditorSupport(String language) {
		Assert.isNotNull(language);
		// Let's look for contributed editor support
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JRExpressionsUIPlugin.PLUGIN_ID, "jrexpressionLanguage"); //$NON-NLS-1$
		for(IConfigurationElement el : config){
			if(language.equals(el.getAttribute("languageName"))){ //$NON-NLS-1$
				Object supportClazz=null;
				try{
					supportClazz=el.createExecutableExtension("class"); //$NON-NLS-1$
					if(supportClazz instanceof ExpressionEditorSupport){
						return (ExpressionEditorSupport) supportClazz;
					}
				}
				catch(CoreException ex){
					JRExpressionsActivator.getInstance().getLog().log(
							new Status(IStatus.ERROR, JRExpressionsUIPlugin.PLUGIN_ID, Messages.DefaultExpressionEditorSupportFactory_NewClassCreationError, ex));
				}
			}
		}
		
		// TODO - Fallback solution, propose the default one.
		// For now, null will show the old text editor.
		return null;
	}

}
