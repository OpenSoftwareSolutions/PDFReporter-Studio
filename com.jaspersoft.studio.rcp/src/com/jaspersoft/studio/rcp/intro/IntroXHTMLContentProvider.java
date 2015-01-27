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
package com.jaspersoft.studio.rcp.intro;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jaspersoft.studio.rcp.intro.action.OpenFileAction;

public class IntroXHTMLContentProvider implements IIntroXHTMLContentProvider {

	public void init(IIntroContentProviderSite site) {

	}

	public void createContent(String id, PrintWriter out) {
		out.println("HEllo world!");
	}

	public void createContent(String id, Composite parent, FormToolkit toolkit) {

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void createContent(String id, Element parent) {
		try {
			Document doc = parent.getOwnerDocument();
			PriorityQueue<IFile> queue = new PriorityQueue<IFile>(10,
					new Comparator<IFile>() {
						public int compare(IFile arg0, IFile arg1) {
							return new Long(arg1.getModificationStamp()
									- arg0.getModificationStamp()).intValue();
						}
					});
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
					.getProjects();
			for (IProject project : projects) {
				IResource[] res = project.members();
				for (IResource r : res) {
					if (r.getType() == IResource.FILE) {
						IFile file = (IFile) r;
						if (file.getFileExtension().equals("jrxml")) {
							queue.add(file);
						}
					}
				}
			}
			if (queue.isEmpty()) {
				Element div = doc.createElement("div");
				div.setAttribute("style", "color: dimgrey; font-size: 16px;");
				div.appendChild(doc.createTextNode("No recent files"));
				parent.appendChild(div);
			} else {
				Element ul = doc.createElement("ul");
				ul.setAttribute("style", "list-style-type: none");
				for (int i = 0; i < queue.size() && i < 20; i++) {
					IFile file = queue.poll();
					Element li = doc.createElement("li");
					Element a = doc.createElement("a");

					a.setAttribute("href", createOpenProcessHref(file));
					a.setAttribute("style",
							"font-size: 11px; font-weight: bold; color: dimgrey;");
					String fname = file.getName();
					if (fname.length() > 25)
						fname = fname.substring(0, 25) + "...";
					a.appendChild(doc.createTextNode(fname));
					li.appendChild(a);

					Element div = doc.createElement("div");
					div.setAttribute("style", "color: dimgrey;");
					div.appendChild(doc.createTextNode(new SimpleDateFormat("MMM d, yyyy HH:mm:ss")
									.format(new Date(file
											.getModificationStamp()))));
					li.appendChild(div);

					ul.appendChild(li);

				}
				parent.appendChild(ul);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	private String createOpenProcessHref(IFile file) {

		return "http://org.eclipse.ui.intro/runAction?pluginId=com.jaspersoft.studio.rcp&class="
				+ OpenFileAction.class.getName()
				+ "&prj="+ file.getProject().getName() + "&file="
				+ file.getProjectRelativePath().toPortableString();

	}
}
