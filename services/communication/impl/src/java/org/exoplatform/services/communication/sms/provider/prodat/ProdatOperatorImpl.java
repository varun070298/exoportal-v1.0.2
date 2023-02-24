/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider.prodat;

import org.exoplatform.services.communication.sms.provider.Operator;
import org.exoplatform.services.communication.sms.provider.SmsMethod;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 9:10:35 PM
 */
public class ProdatOperatorImpl implements Operator {

    private SmsMethod _method = SmsMethod.HTTP_CLIENT;
    private String _socket_host = "217.149.126.50";
    private String _http_host = "217.149.126.53";
    private String _socket_port = "1111";
    private String _http_port = "80";
    private String _username;
    private String _password;
    
    public ProdatOperatorImpl() {        
    }
    
    

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#getMethod()
     */
    public SmsMethod getMethod() {
        return _method;
    }


    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#setMethod(org.exoplatform.services.communication.sms.provider.SmsMethod)
     */
    public void setMethod(SmsMethod method) {
        _method = method;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#getIP()
     */
    public String getHost() {
        return (_method.equals(SmsMethod.SOCKET_CLIENT) ? _socket_host : _http_host);
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#setIP(java.lang.String)
     */
    public void setHost(String host) {
        if (_method.equals(SmsMethod.SOCKET_CLIENT)) {
            _socket_host = host;
        } else if (_method.equals(SmsMethod.HTTP_CLIENT)) {
            _http_host = host;
        }
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#getPort()
     */
    public String getPort() {        
        return (_method.equals(SmsMethod.SOCKET_CLIENT) ? _socket_port : _http_port);
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#setPort(java.lang.String)
     */
    public void setPort(String port) {
        if (_method.equals(SmsMethod.SOCKET_CLIENT)) {
            _socket_port = port;
        } else if (_method.equals(SmsMethod.HTTP_CLIENT)) {
            _http_port = port;
        }
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#getUsername()
     */
    public String getUsername() {
        return _username;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#setUsername(java.lang.String)
     */
    public void setUsername(String username) {
        _username = username;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#getPassword()
     */
    public String getPassword() {
        return _password;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Operator#setPassword(java.lang.String)
     */
    public void setPassword(String password) {
        _password = password;
    }

}


