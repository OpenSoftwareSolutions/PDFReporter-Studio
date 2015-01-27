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
package com.jaspersoft.studio.properties.internal;

import java.util.ArrayList;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

/**
 * Content provider for the proposals starting extracted from a PropertiesContainer object
 * 
 * @author Orlandin Marco
 *
 */
public class PropertiesProposalProvider implements IContentProposalProvider {

	/**
	 * The proposals provided.
	 */
	private PropertiesContainer proposals;

	/**
	 * The proposals mapped to IContentProposal. Cached for speed in the case
	 * where filtering is not used.
	 */
	private IContentProposal[] contentProposals;

	/**
	 * Boolean that tracks whether filtering is used.
	 */
	private boolean filterProposals = false;

	/**
	 * Construct a PropertiesProposalProvider whose content proposals are
	 * always the specified by a PropertiesContainer
	 * 
	 * @param proposals container of the properties to handle
	 */
	public PropertiesProposalProvider(PropertiesContainer proposals) {
		super();
		this.proposals = proposals;
	}

	/**
	 * Return an array of Objects representing the valid content proposals for a
	 * field. 
	 * 
	 * @param contents
	 *            the current contents of the field (only consulted if filtering
	 *            is set to <code>true</code>)
	 * @param position
	 *            the current cursor position within the field (ignored)
	 * @return the array of Objects that represent valid proposals for the field
	 *         given its current content.
	 */
	public IContentProposal[] getProposals(String contents, int position) {
		if (filterProposals) {
			ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
			for (int i = 0; i < proposals.getSize(); i++) {
				PropertyContainer actualContainer = proposals.getPrperties()[i];
				String actualName = actualContainer.getName();
				String searchString = contents.toLowerCase().trim();
				if (actualName.length() >= contents.length() && actualName.toLowerCase().contains(searchString)) {
					Object id = actualContainer.getId();
					Class<?> sectionType = actualContainer.getSectionType();
					list.add(new PropertyContentProposal(actualName, id, sectionType));
				}
			}
			return (IContentProposal[]) list.toArray(new IContentProposal[list.size()]);
		}
		if (contentProposals == null) {
			contentProposals = new IContentProposal[proposals.getSize()];
			for (int i = 0; i < proposals.getSize(); i++) {
				PropertyContainer actualContainer = proposals.getPrperties()[i];
				contentProposals[i] = new PropertyContentProposal(actualContainer.getName(), actualContainer.getId(), actualContainer.getSectionType());
			}
		}
		return contentProposals;
	}

	/**
	 * Set the properties to be used as content proposals.
	 * 
	 * @param items container of the properties to handle
	 */
	public void setProposals(PropertiesContainer items) {
		this.proposals = items;
		contentProposals = null;
	}

	/**
	 * Set the boolean that controls whether proposals are filtered according to
	 * the current field content.
	 * 
	 * @param filterProposals
	 *            <code>true</code> if the proposals should be filtered to
	 *            show only those that match the current contents of the field,
	 *            and <code>false</code> if the proposals should remain the
	 *            same, ignoring the field content.
	 * @since 3.3
	 */
	public void setFiltering(boolean filterProposals) {
		this.filterProposals = filterProposals;
		// Clear any cached proposals.
		contentProposals = null;
	}
}
