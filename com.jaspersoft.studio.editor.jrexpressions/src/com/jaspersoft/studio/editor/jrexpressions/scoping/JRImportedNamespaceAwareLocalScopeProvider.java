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
package com.jaspersoft.studio.editor.jrexpressions.scoping;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRClassGenerator;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;

/**
 * Custom local scope provider that provides implicit imports related to JasperReports.
 * <p>
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see JRClassGenerator
 *
 */
public class JRImportedNamespaceAwareLocalScopeProvider extends	ImportedNamespaceAwareLocalScopeProvider {
	
	// List of JR imports
	private List<ImportNormalizer> jrImports;
	
	@Override
	  protected List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
		if(jrImports==null){
		    jrImports=new ArrayList<ImportNormalizer>();
		    // import net.sf.jasperreports.engine.*
		    jrImports.add(new ImportNormalizer(
		  	      QualifiedName.create("net","sf","jasperreports","engine"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		  	      true, ignoreCase));
		    // import net.sf.jasperreports.engine.fill.*
		    jrImports.add(new ImportNormalizer(
		  	      QualifiedName.create("net","sf","jasperreports","engine","fill"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		  	      true, ignoreCase));
		    // import java.lang.*
		    jrImports.add(new ImportNormalizer(
		  	      QualifiedName.create("java","lang"), //$NON-NLS-1$ //$NON-NLS-2$
		  	      true, ignoreCase));
		    // import java.util.*
		    jrImports.add(new ImportNormalizer(
		      QualifiedName.create("java","util"), //$NON-NLS-1$ //$NON-NLS-2$
		      true, ignoreCase));
		    // import java.math.*
		    jrImports.add(new ImportNormalizer(
		  	      QualifiedName.create("java","math"), //$NON-NLS-1$ //$NON-NLS-2$
		  	      true, ignoreCase));
		    // import java.text.*
		    jrImports.add(new ImportNormalizer(
		  	      QualifiedName.create("java","text"), //$NON-NLS-1$ //$NON-NLS-2$
		  	      true, ignoreCase));
		    // import java.io.*
		    jrImports.add(new ImportNormalizer(
		  	      QualifiedName.create("java","io"), //$NON-NLS-1$ //$NON-NLS-2$
		  	      true, ignoreCase));
		    // import java.net.*
		    jrImports.add(new ImportNormalizer(
		  	      QualifiedName.create("java","net"), //$NON-NLS-1$ //$NON-NLS-2$
		  	      true, ignoreCase));
		}
	    return jrImports;
	  }
}
