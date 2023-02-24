/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.model;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 18, 2004 3:56:19 PM
 */
public class MessageStatus {
    public static final MessageStatus DRAFT = new MessageStatus("DRAFT");
    public static final MessageStatus PENDING = new MessageStatus("PENDING");
    public static final MessageStatus SENDING = new MessageStatus("SENDING");
    public static final MessageStatus COMPLETE = new MessageStatus("COMPLETE");
    public static final MessageStatus FAILURE = new MessageStatus("FAILURE");

    private String _status;
    
    public MessageStatus(String status) {
        _status = status;
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }
    public String toString() {
        return _status;
    }
}
