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
package com.jaspersoft.studio.dnd;

import org.eclipse.core.resources.IContainer;
import org.eclipse.ui.part.IDropActionDelegate;

import com.jaspersoft.studio.model.ANode;

/**
 * Performs dropping of gadgets into views that contain resources.
 */
public class NodePluginDropAdapter implements IDropActionDelegate {
	/**
	 * Method declared on IDropActionDelegate
	 */
	public boolean run(Object source, Object target) {
		if (target instanceof IContainer) {
			NodeTransfer transfer = NodeTransfer.getInstance();
			ANode[] gadgets = transfer.fromByteArray((byte[]) source);
			IContainer parent = (IContainer) target;
			for (int i = 0; i < gadgets.length; i++) {
				writeGadgetFile(parent, gadgets[i]);
			}
			return true;
		}
		// drop was not successful so return false
		return false;
	}

	private void writeGadgetFile(IContainer parent, ANode gadget) {
		// try {
		// IFile file = parent.getFile(new Path(gadget.getName()));
		// ByteArrayInputStream in = createFileContents(gadget);
		// if (file.exists()) {
		// file.setContents(in, IResource.NONE, null);
		// } else {
		// file.create(in, IResource.NONE, null);
		// }
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }
	}

//	private void writeGadgetString(ANode gadget, StringBuffer buf, int depth) {
//		// for (int i = 0; i < depth; i++)
//		// buf.append('\t');
//		// buf.append(gadget.getName());
//		// buf.append('\n');
//		// List<INode> children = gadget.getChildren();
//		// for (INode n : children)
//		// writeGadgetString((ANode) n, buf, depth + 1);
//	}
}
