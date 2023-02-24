/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 3:34:04 PM
 */
public interface Operator {
    
    public SmsMethod getMethod();
    
    public void setMethod(SmsMethod method);

    public String getHost();

    public void setHost(String host);

    public String getPort();

    public void setPort(String port);

    public String getUsername();

    public void setUsername(String username);

    public String getPassword();

    public void setPassword(String password);
}