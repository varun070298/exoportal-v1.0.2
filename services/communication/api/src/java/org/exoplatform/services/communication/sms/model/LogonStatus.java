/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.model;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 1:26:10 PM
 */
public class LogonStatus {
    public static final LogonStatus NONE = new LogonStatus("None");
    public static final LogonStatus SUCCESS = new LogonStatus("Success");
    public static final LogonStatus FAILED = new LogonStatus("Failed");
    
    private String _name;
    
    public LogonStatus(String name) {
        _name = name;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    public String toString() {
        return _name.toString();
    }
}
