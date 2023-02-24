/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.exoplatform.services.communication.sms.common.CommunicationError;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jul 6, 2004 1:15:03 AM
 */
public class ClientHttpSocket {

    private String _host;
    private int _port;
    private String _username;
    private String _password;

    public ClientHttpSocket(String host, int port, String username, String password) {
        _host = host;
        _port = port;
        _username = username;
        _password = password;
    }

    /**
     * @param payload
     * @return
     */
    public String sendMessage(String payload) throws CommunicationError {
        String response = "";
        HttpClient client = new HttpClient();
        InputStream stream = new ByteArrayInputStream(payload.getBytes());
        try {
            URL url = new URL("http", _host, _port, "/sms");
            PostMethod post = new PostMethod(url.toString());
            try {
                post.addRequestHeader(new Header("Content-type", " application/xml"));
                post.addParameter(new NameValuePair("user", _username));
                post.addParameter(new NameValuePair("pw", _password));
	            post.setRequestBody(stream);
	            
	            client.executeMethod(post);
	            if (post.getStatusCode() == HttpStatus.SC_OK) {
	                response = post.getResponseBodyAsString();
	            } else {
	                throw new CommunicationError("Unexpected failure: " + post.getStatusLine().toString());
	            }
            } finally {
                post.releaseConnection();
            }
            stream.close();
        } catch (HttpException e) {
            throw new CommunicationError(e);
        } catch (IOException e) {
            throw new CommunicationError(e);
        }
        return response;
    }

}