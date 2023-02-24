/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.mock;

import org.exoplatform.services.communication.sms.SmsService;
import org.exoplatform.services.communication.sms.common.*;
import org.exoplatform.services.communication.sms.impl.SmsServiceImpl;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.provider.Provider;
import org.exoplatform.services.communication.sms.provider.Sender;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 19, 2004 2:04:12 PM
 */
public class MockSmsService extends SmsServiceImpl implements SmsService {

    public MockSmsService() {
        super();
    }

    public Messages createMessages() {
        return super.createMessages();
    }

    public Message createMessage() {
        return super.createMessage();
    }

    public Provider createProdatProvider(String username, String password) {
        return super.createProdatProvider(username, password);
    }

    public Recipient createRecipient(String to) {
        return super.createRecipient(to);
    }

    public Sender createSender(Provider provider) {
        return new MockSender(provider);
    }

    public boolean sendSms(Provider provider, Messages messages) throws CommunicationError, RequestException, ResponseException, ConvertException {
        return super.sendSms(provider, messages);
    }

}