/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms;

import org.exoplatform.services.communication.sms.common.*;
import org.exoplatform.services.communication.sms.model.*;
import org.exoplatform.services.communication.sms.provider.Provider;
import org.exoplatform.services.communication.sms.provider.Sender;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 7:51:38 PM
 */
public interface SmsService {

    public Provider createProdatProvider(String username, String password);
    
    public Sender createSender(Provider provider);

    public Messages createMessages();
    
    public Message createMessage();
    
    public Recipient createRecipient(String to);
    
    public boolean sendSms(Provider provider, Messages messages) throws CommunicationError, RequestException, ResponseException, ConvertException;

}

