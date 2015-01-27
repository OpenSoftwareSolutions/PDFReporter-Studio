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
package net.sf.jasperreports.eclipse.builder.jdt;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

public class NameEnvironmentAnswerFactory {
	// static {
	// boolean success;
	// try // FIXME remove support for pre 3.1 jdt
	// {
	// Class<?> classAccessRestriction =
	// Class.forName("org.eclipse.jdt.internal.compiler.env.AccessRestriction");
	// constrNameEnvAnsBin2Args = NameEnvironmentAnswer.class.getConstructor(new
	// Class[] { IBinaryType.class, classAccessRestriction });
	// constrNameEnvAnsCompUnit2Args =
	// NameEnvironmentAnswer.class.getConstructor(new Class[] {
	// ICompilationUnit.class, classAccessRestriction });
	// success = true;
	// } catch (NoSuchMethodException e) {
	// success = false;
	// } catch (ClassNotFoundException ex) {
	// success = false;
	// }
	//
	// if (!success) {
	// try {
	// constrNameEnvAnsBin = NameEnvironmentAnswer.class.getConstructor(new
	// Class[] { IBinaryType.class });
	// constrNameEnvAnsCompUnit = NameEnvironmentAnswer.class.getConstructor(new
	// Class[] { ICompilationUnit.class });
	// } catch (NoSuchMethodException ex) {
	// throw new JRRuntimeException("Not able to load JDT classes", ex);
	// }
	// }
	// }
	// private static Constructor<? extends NameEnvironmentAnswer>
	// constrNameEnvAnsBin;
	// private static Constructor<? extends NameEnvironmentAnswer>
	// constrNameEnvAnsCompUnit;
	//
	// private static Constructor<? extends NameEnvironmentAnswer>
	// constrNameEnvAnsBin2Args;
	// private static Constructor<? extends NameEnvironmentAnswer>
	// constrNameEnvAnsCompUnit2Args;

	public static NameEnvironmentAnswer getCompilationUnit(ICompilationUnit compilationUnit) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return new NameEnvironmentAnswer(compilationUnit, null);
		//
		// if (constrNameEnvAnsCompUnit2Args != null)
		// return constrNameEnvAnsCompUnit2Args.newInstance(new Object[] {
		// compilationUnit, null });
		// return constrNameEnvAnsCompUnit.newInstance(new Object[] {
		// compilationUnit });
	}

	public static NameEnvironmentAnswer getClassFileReader(ClassFileReader classFileReader) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return new NameEnvironmentAnswer(classFileReader, null);
		// if (constrNameEnvAnsBin2Args != null)
		// return constrNameEnvAnsBin2Args.newInstance(new Object[] {
		// classFileReader, null });
		// return constrNameEnvAnsBin.newInstance(new Object[] { classFileReader });
	}
}
