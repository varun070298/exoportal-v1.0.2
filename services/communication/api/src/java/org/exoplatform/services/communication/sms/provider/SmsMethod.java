/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jul 6, 2004 10:00:11 AM
 */
final public class SmsMethod {
    
    final static public SmsMethod SOCKET_CLIENT = new SmsMethod("SOCKET_CLIENT");
    final static public SmsMethod HTTP_CLIENT = new SmsMethod("HTTP_CLIENT");
    
    private String _name;
    
    public SmsMethod(String name) {
        _name = name;
    }
    
    public String toString() {
        return _name;
    }

}
