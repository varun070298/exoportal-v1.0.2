/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.exoplatform.services.communication.sms.model.*;

/**
 * 
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class MessagesImpl extends ArrayList implements Serializable, Messages {
    
    private LogonStatus _logonStatus;
    private String _encoding;
    private String _reason;
    private boolean _gotError;

    public MessagesImpl() {
        clear();
    }
    
    public void clear() {
        super.clear();
        _logonStatus = LogonStatus.NONE;
        _encoding = "UTF-8";
        _reason = "";
        _gotError = false;
    }
    
    public LogonStatus getLogonStatus() {
        return _logonStatus;
    }
    
    public void setLogonStatus(LogonStatus logonStatus) {
        _logonStatus = logonStatus;
    }
    
    public String getEncoding() {
        return _encoding;
    }
    
    public void setEncoding(String encoding) {
        _encoding = encoding;
    }
    
    public String getReason() {
        return _reason;
    }
    
    public void setReason(String reason) {
        _reason = reason;
    }
    
    public boolean hasErrorOccured() {
        return _gotError;
    }
    
    public void errorOccured() {
        _gotError = true;
    }
    
    public Iterator iterator() {
        return super.iterator();
    }
    
    public Message getMessage(int index) {
        return (Message) get(index);
    }

    public Message addMessage() {
        Message message = new MessageImpl();
        add( message );
        return message;
    }
    
    public Message addMessage(Message message) {
        add(message);
        return message;
    }
    
    public void removeMessage(Message message) {
        remove(message);
    }
    
    public Message findMessageById(Integer id) {
        for(Iterator i = iterator(); i.hasNext(); ) {
            Message m = (Message) i.next();            
            for(Iterator j = m.getRecipients().iterator(); j.hasNext(); ) {
                Recipient r = (Recipient) j.next();
                if (id.equals(r.getId())) {
                    return m;
                }
            }
        }
        return null;
    }
    
    public int count() {
        return size();
    }

}
