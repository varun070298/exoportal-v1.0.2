/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider.prodat;

import java.util.Iterator;
import org.exoplatform.services.communication.sms.common.ConvertException;
import org.exoplatform.services.communication.sms.common.RequestException;
import org.exoplatform.services.communication.sms.encoder.Formatter;
import org.exoplatform.services.communication.sms.encoder.Resolver;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.provider.*;
import org.exoplatform.services.communication.sms.util.SmsUtil;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 9:39:05 PM
 */
public class ProdatRequestImpl implements SmsRequest {

    private Provider _provider;
    private Messages _messages;
    private String _payload;
    
    public ProdatRequestImpl(Provider provider) {
        _provider = provider;
    }

    /**
     * @return Returns the messages.
     */
    public Messages getMessages() {
        return _messages;
    }

    /**
     * @param messages The messages to set.
     */
    public void setMessages(Messages messages) {
        _messages = messages;
    }

    /**
     * @return Returns the payload.
     */
    public String getPayload() {
        return _payload;
    }

    /**
     * @param payload The payload to set.
     */
    public void setPayload(String payload) {
        _payload = payload;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.SenderImpl#prepare(Messages messages)
     * @see org.exoplatform.services.communication.sms.provider.SmsRequest#prepare()
     */
    public void prepare() throws RequestException, ConvertException {
        Operator operator = _provider.getOperator();        
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\""+_messages.getEncoding()+"\"?>\n");
        buffer.append("<!DOCTYPE SESSION SYSTEM \"pswincom_submit.dtd\">\n");
        buffer.append("<SESSION>\n");
        buffer.append("<CLIENT>" + operator.getUsername() + "</CLIENT>\n");
        buffer.append("<PW>" + operator.getPassword() + "</PW>\n");
        buffer.append("<AP>eXo</AP>\n"); 
        //buffer.append("<SD></SD>\n"); // Session Data Code
        buffer.append("<MSGLST>\n");
        for(Iterator i = _messages.iterator(); i.hasNext(); ) {            
            Message message = (Message) i.next();
            String text = message.getContent().toString();
            Resolver resolver = _provider.getResolver();
            Formatter formatter = resolver.getFormatter(message.getFormat());
            text = (String) formatter.convert(text);            
            Integer op = (Integer)resolver.getOperationCode(message.getFormat());
            for(Iterator j = message.getRecipients().iterator(); j.hasNext(); ) {
                Recipient recipient = (Recipient) j.next();
                String from = message.getFrom();
                // Only consider numbers with + and 00 prefix and non alpha numbers as real number
                if (!SmsUtil.isAlpha(from) || SmsUtil.hasMobileNumberPrefix(from)) 
                    from = SmsUtil.prepareCellularNumber(from);
                buffer.append("<MSG>\n");
                buffer.append("<ID>" + recipient.getId() + "</ID>\n");
                buffer.append("<TEXT>" + text + "</TEXT>\n");
                buffer.append("<RCV>" + SmsUtil.prepareCellularNumber(recipient.getTo()) + "</RCV>\n");
                buffer.append("<SND>" + from + "</SND>\n");
                buffer.append("<OP>" + op.toString() + "</OP>\n");
                buffer.append("</MSG>\n");
                recipient.setStatus(MessageStatus.PENDING);
            }
        }
        buffer.append("</MSGLST>\n");
        buffer.append("</SESSION>\n");        
        _payload = buffer.toString();        
    }

}
