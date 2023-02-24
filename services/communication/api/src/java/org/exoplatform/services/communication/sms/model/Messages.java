/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.model;

import java.util.Iterator;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 4:10:47 PM
 */
public interface Messages {

    public LogonStatus getLogonStatus();
    
    public void setLogonStatus(LogonStatus logonStatus);
    
    public String getEncoding();

    public void setEncoding(String encoding);

    public String getReason();
    
    public void setReason(String reason);
    
    public boolean hasErrorOccured();

    public void errorOccured();
    
    public Iterator iterator();

    public Message getMessage(int index);

    public Message addMessage();

    public Message addMessage(Message message);

    public void removeMessage(Message message);

    public Message findMessageById(Integer id);
    
    public int count();

    public void clear();

}