/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.model;

import java.io.Serializable;
import org.exoplatform.services.communication.sms.model.MessageStatus;
import org.exoplatform.services.communication.sms.model.Recipient;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 18, 2004 3:52:33 PM
 */
public class RecipientImpl implements Serializable, Recipient {
    
    public static Integer _identity = new Integer(0);
    private Integer _id;
    private String _to;
    private MessageStatus _status = MessageStatus.DRAFT;
    private String _error = "";
    
    public RecipientImpl(String to) {
        _to = to;
        synchronized(_identity) {
            int id = _identity.intValue();
            id++;
            _id = _identity = new Integer(id);
        }
    }
    
    public Integer getId() {
        return _id;
    }
    
    public String getTo() {
        return _to;
    }
    
    public Recipient setTo(String to) {
        _to = to;
        return this;
    }
    
    public MessageStatus getStatus() {
        return _status;
    }

    public Recipient setStatus(MessageStatus status) {
        _status = status;
        return this;
    }

    public String getError() {
        return _error;
    }

    public Recipient setError(String error) {
        _error = error;
        return this;
    }
}

