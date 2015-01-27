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
package com.jaspersoft.studio.editor.xml.outline;

import java.util.List;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.xml.sax.helpers.LocatorImpl;

import com.jaspersoft.studio.editor.xml.xml.XMLElement;
import com.jaspersoft.studio.editor.xml.xml.XMLParser;

public class OutlineContentProvider implements ITreeContentProvider {

	private XMLElement root = null;
	private Object input;
	private IDocumentProvider documentProvider;

	protected final static String TAG_POSITIONS = "__tag_positions";
	protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(TAG_POSITIONS);

	public OutlineContentProvider(IDocumentProvider provider) {
		super();
		this.documentProvider = provider;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement == input) {
			if (root == null)
				return new Object[0];
			List<XMLElement> childrenDTDElements = root.getChildrenDTDElements();
			if (childrenDTDElements != null)
				return childrenDTDElements.toArray();
		} else {
			XMLElement parent = (XMLElement) parentElement;
			List<XMLElement> childrenDTDElements = parent.getChildrenDTDElements();
			if (childrenDTDElements != null)
				return childrenDTDElements.toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof XMLElement)
			return ((XMLElement) element).getParent();
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element == input)
			return true;
		else {
			return ((XMLElement) element).getChildrenDTDElements().size() > 0;
		}
	}

	public Object[] getElements(Object inputElement) {
		if (root == null)
			return new Object[0];
		List<XMLElement> childrenDTDElements = root.getChildrenDTDElements();
		if (childrenDTDElements != null)
			return childrenDTDElements.toArray();
		return new Object[0];
	}

	public void dispose() {
	}
	
	/**
	 * Return the document by the type of the input
	 * 
	 * @param input the input must be a string or an IEditorInput
	 * @return an IDocument build from the input
	 */
	private IDocument getDocument(Object input){
		if (input instanceof String) return new Document((String)input);
		else return documentProvider.getDocument(input);
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		if (oldInput != null) {
			IDocument document = getDocument(oldInput);
			if (document != null) {
				try {
					document.removePositionCategory(TAG_POSITIONS);
				} catch (BadPositionCategoryException x) {
				}
				document.removePositionUpdater(positionUpdater);
			}
		}

		input = newInput;

		if (newInput != null) {
			IDocument document = getDocument(newInput);
			if (document != null) {
				document.addPositionCategory(TAG_POSITIONS);
				document.addPositionUpdater(positionUpdater);

				XMLElement rootElement = parseRootElement(document);
				if (rootElement != null) {
					root = rootElement;
				}
			}
		}
	}

	private XMLElement parseRootElement(IDocument document) {
		String text = document.get();
		XMLElement tagPositions = parseRootElements(text, document);
		return tagPositions;
	}

	private XMLElement parseRootElements(String text, IDocument document) {
		try {
			XMLParser xmlParser = new XMLParser();
			OutlineContentHandler contentHandler = new OutlineContentHandler();
			contentHandler.setDocument(document);
			contentHandler.setPositionCategory(TAG_POSITIONS);
			contentHandler.setDocumentLocator(new LocatorImpl());
			xmlParser.setContentHandler(contentHandler);
			xmlParser.doParse(text);
			XMLElement root = contentHandler.getRootElement();
			return root;
		} catch (Exception e) {
			return null;
		}
	}

}
