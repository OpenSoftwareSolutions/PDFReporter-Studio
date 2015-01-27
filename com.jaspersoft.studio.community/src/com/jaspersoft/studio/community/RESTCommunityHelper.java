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
package com.jaspersoft.studio.community;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaspersoft.studio.community.issues.IssueField;
import com.jaspersoft.studio.community.messages.Messages;
import com.jaspersoft.studio.community.requests.FileUploadRequest;
import com.jaspersoft.studio.community.requests.IssueRequest;
import com.jaspersoft.studio.community.utils.CommunityAPIException;

/**
 * Helper class to manager REST operations towards the community site. 
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public final class RESTCommunityHelper {
	
	private static final Log log = LogFactory.getLog(RESTCommunityHelper.class);

	private RESTCommunityHelper(){
		// prevent instantiation
	}
	
	/**
	 * Executes the authentication to the Jaspersoft community in order to
	 * retrieve the session cookie to use later for all other operations.
	 * 
	 * @param client
	 *            the http client to use
	 * @param username
	 *            the community user name (or email)
	 * @param password
	 *            the community user password
	 * @return the authentication cookie if able to retrieve it,
	 *         <code>null</code> otherwise
	 * @throws CommunityAPIException
	 */
	public static Cookie getAuthenticationCookie(
			HttpClient client, String username, String password) throws CommunityAPIException{

		try {
			PostMethod loginPost = new PostMethod(CommunityConstants.LOGIN_URL);
			loginPost.setRequestEntity(
					new StringRequestEntity(
							"{ \"username\": \""+username+"\", \"password\":\""+password+"\" }",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							CommunityConstants.JSON_CONTENT_TYPE,CommunityConstants.REQUEST_CHARSET));
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			int httpRetCode = client.executeMethod(loginPost);
			String responseBodyAsString = loginPost.getResponseBodyAsString();
			
			if(log.isDebugEnabled()){
				displayCookiesAndResponseBody("====== LOGIN ======",client,loginPost); //$NON-NLS-1$
			}
			
			if(HttpStatus.SC_OK == httpRetCode) {
				// Can proceed
				Cookie[] cookies = client.getState().getCookies();
				Cookie authCookie = null;
				for(Cookie cookie : cookies){
					if(cookie.getName().startsWith("SESS")){ //$NON-NLS-1$
						authCookie = cookie;
						break;
					}
				}
				releaseConnectionAndClearCookies(loginPost, client);
				return authCookie;
			}
			else if(HttpStatus.SC_UNAUTHORIZED == httpRetCode){
				releaseConnectionAndClearCookies(loginPost, client);
				// Unauthorized... wrong username or password
				CommunityAPIException unauthorizedEx = new CommunityAPIException(Messages.RESTCommunityHelper_WrongUsernamePasswordError);
				unauthorizedEx.setHttpStatusCode(httpRetCode);
				unauthorizedEx.setResponseBodyAsString(responseBodyAsString);
				throw unauthorizedEx;
			}
			else {
				releaseConnectionAndClearCookies(loginPost, client);
				// Some other problem occurred
				CommunityAPIException generalEx = new CommunityAPIException(Messages.RESTCommunityHelper_AuthInfoProblemsError);
				generalEx.setHttpStatusCode(httpRetCode);
				generalEx.setResponseBodyAsString(responseBodyAsString);
				throw generalEx;				
			}
		} catch (UnsupportedEncodingException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_EncodingNotValidError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_AuthenticationError, e);
		} catch (HttpException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_PostMethodError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_AuthenticationError, e);
		} catch (IOException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_PostMethodIOError,e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_AuthenticationError, e);
		}
	}
	
	
	/**
	 * Uploads the specified file to the community site. The return identifier
	 * can be used later when composing other requests.
	 * 
	 * @param client
	 *            the http client to use
	 * @param attachment
	 *            the file to attach
	 * @param authCookie
	 *            the session cookie to use for authentication purpose
	 * @return the identifier of the file uploaded, <code>null</code> otherwise
	 * @throws CommunityAPIException
	 */
	public static String uploadFile(
			HttpClient client, File attachment, Cookie authCookie) throws CommunityAPIException{
		try {
			FileInputStream fin = new FileInputStream(attachment);
			byte fileContent[] = new byte[(int)attachment.length()];
			fin.read(fileContent);
			fin.close();
			
			byte[] encodedFileContent = Base64.encodeBase64(fileContent);
			FileUploadRequest uploadReq = new FileUploadRequest(attachment.getName(),encodedFileContent);
	
			PostMethod fileupload = new PostMethod(CommunityConstants.FILE_UPLOAD_URL);
			fileupload.setRequestEntity(new StringRequestEntity(
					uploadReq.getAsJSON(),CommunityConstants.JSON_CONTENT_TYPE,CommunityConstants.REQUEST_CHARSET));
			fileupload.setRequestHeader(new Header("Cookie", authCookie.toExternalForm())); //$NON-NLS-1$
			
			int httpRetCode = client.executeMethod(fileupload);
			String responseBodyAsString = fileupload.getResponseBodyAsString();
			if(log.isDebugEnabled()){
				displayCookiesAndResponseBody("====== FILE UPLOAD ======",client,fileupload); //$NON-NLS-1$
			}

			if(HttpStatus.SC_OK == httpRetCode){
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
				mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
				JsonNode jsonRoot = mapper.readTree(responseBodyAsString);
				String fid = jsonRoot.get("fid").asText(); //$NON-NLS-1$
				//String uri = jsonRoot.get("uri").asText(); //$NON-NLS-1$
				releaseConnectionAndClearCookies(fileupload, client);
				return fid;
			}
			else {
				CommunityAPIException ex = new CommunityAPIException(Messages.RESTCommunityHelper_FileUploadError);
				ex.setHttpStatusCode(httpRetCode);
				ex.setResponseBodyAsString(responseBodyAsString);
				throw ex;
			}
			
		} catch (FileNotFoundException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_FileNotFoundError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_FileUploadError,e);
		} catch (UnsupportedEncodingException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_EncodingNotValidError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_FileUploadError,e);
		} catch (HttpException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_PostMethodError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_FileUploadError,e);
		} catch (IOException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_PostMethodIOError,e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_FileUploadError,e);
		}
	}
	
	/**
	 * Creates a new issue in the community tracker.
	 * 
	 * @param client
	 *            the http client to use
	 * @param newIssue
	 *            the new issue to create on the community tracker
	 * @param attachmentsIds
	 *            the list of file identifiers that will be attached to the
	 *            final issue
	 * @param authCookie
	 *            the session cookie to use for authentication purpose
	 * @return the tracker URL of the newly created issue
	 * @throws CommunityAPIException
	 */
	public static String createNewIssue(
			HttpClient client, IssueRequest newIssue, List<String> attachmentsIds, Cookie authCookie) throws CommunityAPIException{
		try {
			// Add attachments if any
			if (!attachmentsIds.isEmpty()){
				IssueField attachmentsField = new IssueField(){
					@Override
					protected String getValueAttributeName() {
						return "fid"; //$NON-NLS-1$
					}

					@Override
					public boolean isArray() {
						return true;
					}
				};
				attachmentsField.setName("field_bug_attachments"); //$NON-NLS-1$
				attachmentsField.setValues(attachmentsIds);
				newIssue.setAttachments(attachmentsField);
			}
			
			PostMethod issueCreation = new PostMethod(CommunityConstants.ISSUE_CREATION_URL);
			issueCreation.setRequestEntity(new StringRequestEntity(
					newIssue.getAsJSON(), CommunityConstants.JSON_CONTENT_TYPE, CommunityConstants.REQUEST_CHARSET));
			issueCreation.setRequestHeader(new Header("Cookie", authCookie.toExternalForm())); //$NON-NLS-1$
			int httpRetCode = client.executeMethod(issueCreation);
			String responseBodyAsString = issueCreation.getResponseBodyAsString();
			if(log.isDebugEnabled()){
				displayCookiesAndResponseBody("====== ISSUE CREATION ======",client,issueCreation); //$NON-NLS-1$
			}
			releaseConnectionAndClearCookies(issueCreation, client);
			
			if(HttpStatus.SC_OK != httpRetCode){
				CommunityAPIException ex = new CommunityAPIException(Messages.RESTCommunityHelper_IssueCreationError);
				ex.setHttpStatusCode(httpRetCode);
				ex.setResponseBodyAsString(responseBodyAsString);
				throw ex;
			}
			else {
				// extract the node ID information in order
				// to retrieve the issue URL available on the tracker
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
				mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
				JsonNode jsonRoot = mapper.readTree(responseBodyAsString);
				String nodeID = jsonRoot.get("nid").asText(); //$NON-NLS-1$
				JsonNode jsonNodeContent = retrieveNodeContentAsJSON(client, nodeID, authCookie);
				return jsonNodeContent.get("path").asText(); //$NON-NLS-1$
			}
						
		} catch (UnsupportedEncodingException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_EncodingNotValidError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_IssueCreationError,e);
		} catch (HttpException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_PostMethodError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_IssueCreationError,e);
		} catch (IOException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_PostMethodIOError,e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_IssueCreationError,e);
		}
	}
	
	/**
	 * Tries to retrieve the content for the specified node ID.
	 * 
	 * @param client
	 *            the http client to use
	 * @param nodeID
	 *            the node ID
	 * @param authCookie
	 *            the session cookie to use for authentication purpose
	 * @return the node content as JSON
	 * @throws CommunityAPIException
	 */
	public static JsonNode retrieveNodeContentAsJSON(
			HttpClient client, String nodeID,Cookie authCookie) throws CommunityAPIException{
		try {
			GetMethod retrieNodeContent = 
					new GetMethod(CommunityConstants.NODE_CONTENT_URL_PREFIX + nodeID + ".json"); //$NON-NLS-1$
			retrieNodeContent.setRequestHeader(new Header("Cookie", authCookie.toExternalForm())); //$NON-NLS-1$
			int httpRetCode = client.executeMethod(retrieNodeContent);
			String responseBodyAsString = retrieNodeContent.getResponseBodyAsString();
			if(log.isDebugEnabled()){
				displayCookiesAndResponseBody("====== NODE CONTENT RETRIEVE ======",client,retrieNodeContent); //$NON-NLS-1$
			}
			
			if(HttpStatus.SC_OK == httpRetCode){
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
				mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
				JsonNode jsonRoot = mapper.readTree(responseBodyAsString);
				releaseConnectionAndClearCookies(retrieNodeContent, client);
				return jsonRoot;
			}
			else {
				CommunityAPIException ex = new CommunityAPIException(Messages.RESTCommunityHelper_NodeContentRetrieveError);
				ex.setHttpStatusCode(httpRetCode);
				ex.setResponseBodyAsString(responseBodyAsString);
				throw ex;
			}
		} catch (HttpException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_GetMethodError, e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_NodeContentRetrieveError,e);
		} catch (IOException e) {
			JSSCommunityActivator.getDefault().logError(
					Messages.RESTCommunityHelper_GetMethodIOError,e);
			throw new CommunityAPIException(Messages.RESTCommunityHelper_NodeContentRetrieveError,e);
		}
	}

	/*
	 * Logs debug information if enabled. 
	 */
	private static void displayCookiesAndResponseBody(
			String title, HttpClient client, HttpMethod method) throws IOException {
		log.debug(title);

		// Get all the cookies
        Cookie[] cookies = client.getState().getCookies();
        // Display the cookies
        log.debug("Actual cookies: "); //$NON-NLS-1$
        for (int i = 0; i < cookies.length; i++) {
            log.debug(" - " + cookies[i].toExternalForm()); //$NON-NLS-1$
        }
        
        // Response body
        log.debug("Response content: "); //$NON-NLS-1$
		log.debug(method.getResponseBodyAsString());
	}
	
	private static void releaseConnectionAndClearCookies(HttpMethod method, HttpClient client) {
		method.releaseConnection();
		client.getState().clearCookies();
	}
	
}
