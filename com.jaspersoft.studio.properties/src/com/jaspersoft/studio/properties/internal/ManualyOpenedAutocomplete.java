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

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.widgets.Control;

/**
 * Autocomplete manager used for the properties of an element. Can be also opened
 * and closed manually
 * 
 * @author Orlandin Marco
 *
 */
public class ManualyOpenedAutocomplete{

	/**
	 * Provider for the element to propose
	 */
	private PropertiesProposalProvider proposalProvider;
	
	/**
	 * Dialog for the content
	 */
	private OpenableContentProposal adapter;
	
	/**
	 * Dialog for the content, expose the methods to open and close the 
	 * dialog manually
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class OpenableContentProposal extends ContentProposalAdapter{

		public OpenableContentProposal(Control control,
				IControlContentAdapter controlContentAdapter,
				IContentProposalProvider proposalProvider, KeyStroke keyStroke,
				char[] autoActivationCharacters) {
			super(control, controlContentAdapter, proposalProvider, keyStroke,
					autoActivationCharacters);
		}
		
		/**
		 * Open the proposal popup and display the proposals provided by the
		 * proposal provider. This method returns immediately. That is, it does not
		 * wait for a proposal to be selected. This method is used by subclasses to
		 * explicitly invoke the opening of the popup. If there are no proposals to
		 * show, the popup will not open and a beep will be sounded.
		 */
		public void openProposalPopup(){
			super.openProposalPopup();
		}
		
		/**
		 * Close the proposal popup without accepting a proposal. This method
		 * returns immediately, and has no effect if the proposal popup was not
		 * open. This method is used by subclasses to explicitly close the popup
		 * based on additional logic.
		 * 
		 * @since 3.3
		 */
		public void closeProposalPopup(){
			super.closeProposalPopup();
		}
	}

	/**
	 * Construct an AutoComplete field on the specified control, whose
	 * completions are characterized by the specified array of Strings.
	 * 
	 * @param control
	 *            the control for which autocomplete is desired. May not be
	 *            <code>null</code>.
	 * @param controlContentAdapter
	 *            the <code>IControlContentAdapter</code> used to obtain and
	 *            update the control's contents. May not be <code>null</code>.
	 * @param proposals
	 *            the container with all the properties of the element
	 */
	public ManualyOpenedAutocomplete(Control control, IControlContentAdapter controlContentAdapter, PropertiesContainer proposals) {
		proposalProvider = new PropertiesProposalProvider(proposals);
		proposalProvider.setFiltering(true);
		adapter = new OpenableContentProposal(control, controlContentAdapter, proposalProvider, null, null);
		adapter.setPropagateKeys(true);
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
	}

	/**
	 * Set the properties to be used as content proposals.
	 * 
	 * @param proposals
	 *            properties to be used as proposals.
	 */
	public void setProposals(PropertiesContainer proposals) {
		proposalProvider.setProposals(proposals);
	}
	
	/**
	 * Open the proposal popup and display the proposals provided by the
	 * proposal provider. This method returns immediately. That is, it does not
	 * wait for a proposal to be selected. This method is used by subclasses to
	 * explicitly invoke the opening of the popup. If there are no proposals to
	 * show, the popup will not open and a beep will be sounded.
	 */
	public void openProposalPopup(){
		adapter.openProposalPopup();
	}
	
	/**
	 * Answers a boolean indicating whether the main proposal popup is open.
	 * 
	 * @return <code>true</code> if the proposal popup is open, and
	 *         <code>false</code> if it is not.
	 * 
	 */
	public boolean isProposalOpened(){
		return adapter.isProposalPopupOpen();
	}
	
	/**
	 * Close the proposal popup without accepting a proposal. This method
	 * returns immediately, and has no effect if the proposal popup was not
	 * open. This method is used by subclasses to explicitly close the popup
	 * based on additional logic.
	 * 
	 */
	public void closeProposalPopup(){
		adapter.closeProposalPopup();
	}
	
	/**
	 * Add the specified listener to the list of content proposal listeners that
	 * are notified when content proposals are chosen.
	 * </p>
	 * 
	 * @param listener
	 *            the IContentProposalListener to be added as a listener. Must
	 *            not be <code>null</code>. If an attempt is made to register
	 *            an instance which is already registered with this instance,
	 *            this method has no effect.

	 */
	public void addProposalSelectedListener(IContentProposalListener listener){
		adapter.addContentProposalListener(listener);
	}
	
}
