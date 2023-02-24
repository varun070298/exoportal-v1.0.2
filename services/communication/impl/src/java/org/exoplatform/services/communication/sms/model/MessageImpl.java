/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.exoplatform.services.communication.sms.encoder.MessageFormat;
import org.exoplatform.services.communication.sms.model.Message;
import org.exoplatform.services.communication.sms.model.Recipient;


/**
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class MessageImpl implements Serializable, Message {

    private String _from = "";
    private List _recipients = new ArrayList();
    private Object _content = "";
    private MessageFormat _format;

    public MessageImpl() {
    }

    public String getFrom() {
        return _from;
    }

    public Message setFrom(String from) {
        _from = from;
        return this;
    }

    public List getRecipients() {
        return _recipients;
    }

    public Recipient addRecipient(Recipient recipient) {
        _recipients.add(recipient);
        return recipient;
    }
    
    public Recipient addRecipient(String recipient) {
        Recipient r = new RecipientImpl(recipient);
        _recipients.add(r);
        return r;
    }
    
    public void removeRecipient(Recipient recipient) {
        _recipients.remove(recipient);
    }

    public void removeRecipient(int index) {
        _recipients.remove(index);
    }
    
    public Recipient findRecipientById(Integer id) {
        for (Iterator i = _recipients.iterator(); i.hasNext(); ) {
            Recipient r = (Recipient) i.next();
            if (id.equals(r.getId())) {
                return r;
            }
        }
        return null;
    }
    
    public void clearRecipients() {
        _recipients.clear();
    }
    
    public int countRecipients() {
        return _recipients.size();
    }
    
    public Object getContent() {
        return _content;
    }

    public Message setContent(Object content) {
        _content = content;
        return this;
    }

    public MessageFormat getFormat() {
        return _format;
    }

    public Message setFormat(MessageFormat format) {
        _format = format;
        return this;
    }

}

