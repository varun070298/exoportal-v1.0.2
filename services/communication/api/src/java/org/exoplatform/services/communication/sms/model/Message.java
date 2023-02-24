/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.model;

import java.util.List;
import org.exoplatform.services.communication.sms.encoder.MessageFormat;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 4:05:27 PM
 */
public interface Message {

    public String getFrom();

    public Message setFrom(String from);

    public List getRecipients();

    public Recipient addRecipient(Recipient recipient);

    public Recipient addRecipient(String recipient);

    public void removeRecipient(Recipient recipient);

    public void removeRecipient(int index);

    public Recipient findRecipientById(Integer id);
    
    public void clearRecipients();

    public int countRecipients();
    
    public Object getContent();

    public Message setContent(Object content);

    public MessageFormat getFormat();

    public Message setFormat(MessageFormat format);

}
