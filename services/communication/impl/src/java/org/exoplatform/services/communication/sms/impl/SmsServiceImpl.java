/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.impl;

import org.exoplatform.services.communication.sms.SmsService;
import org.exoplatform.services.communication.sms.common.*;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.provider.*;
import org.exoplatform.services.communication.sms.provider.SenderImpl;
import org.exoplatform.services.communication.sms.provider.prodat.ProdatProviderImpl;

/**
 * 
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.net
 */
public class SmsServiceImpl implements SmsService {

    public SmsServiceImpl() {
    }

    /*
     * @see org.exoplatform.services.communication.sms.SmsService#createProdatProvider(java.lang.String,
     *      java.lang.String)
     */
    public Provider createProdatProvider(String username, String password) {
        Provider provider = new ProdatProviderImpl();
        provider.getOperator().setUsername(username);
        provider.getOperator().setPassword(password);
        return provider;
    }

    /*
     * @see org.exoplatform.services.communication.sms.SmsService#createMessages()
     */
    public Messages createMessages() {
        return new MessagesImpl();
    }

    /*
     * @see org.exoplatform.services.communication.sms.SmsService#createMessaage()
     */
    public Message createMessage() {
        return new MessageImpl();
    }

    /*
     * @see org.exoplatform.services.communication.sms.SmsService#createRecipient()
     */
    public Recipient createRecipient(String to) {
        return new RecipientImpl(to);
    }

    /*
     * @see org.exoplatform.services.communication.sms.SmsService#getSender()
     */
    public Sender createSender(Provider provider) {
        Sender sender = new SenderImpl(provider);
        return sender;
    }

    /*
     * @see org.exoplatform.services.communication.sms.SmsService#sendSms(org.exoplatform.services.communication.sms.provider.Provider,
     *      org.exoplatform.services.communication.sms.model.Messages)
     */
    public boolean sendSms(Provider provider, Messages messages) throws CommunicationError, RequestException, ResponseException, ConvertException {
        Sender sender = new SenderImpl(provider);
        sender.prepare(messages);
        sender.send();
        return messages.hasErrorOccured();
    }

}