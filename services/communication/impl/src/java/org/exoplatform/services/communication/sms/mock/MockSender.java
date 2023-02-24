/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.mock;

import org.exoplatform.services.communication.sms.common.CommunicationError;
import org.exoplatform.services.communication.sms.common.ResponseException;
import org.exoplatform.services.communication.sms.model.Messages;
import org.exoplatform.services.communication.sms.provider.*;
import org.exoplatform.services.communication.sms.provider.SenderImpl;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 12:05:20 PM
 */
public class MockSender extends SenderImpl implements Sender {

    public MockSender(Provider provider) {
        super(provider);   
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Sender#send()
     */
    public Messages send() throws CommunicationError, ResponseException {
        Messenger messenger = new MockProdatMessenger(_provider);                
        messenger.service(_request, _response);
        return _response.getMessages();
    }
}
