/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.provider;

import org.exoplatform.services.communication.sms.adapter.Adapter;
import org.exoplatform.services.communication.sms.common.*;
import org.exoplatform.services.communication.sms.model.Messages;
import org.exoplatform.services.communication.sms.provider.*;

/**
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class SenderImpl implements Sender {
    
    protected Provider _provider;
    protected SmsRequest _request;
    protected SmsResponse _response;
    
    public SenderImpl(Provider provider) {
        _provider = provider;        
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Sender#getProvider()
     */
    public Provider getProvider() {
        return _provider;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Sender#clear()
     */
    public void clear() {
        _request = null;
        _response = null;
    }
    
    /* 
     * @see org.exoplatform.services.communication.sms.provider.Sender#prepare(org.exoplatform.services.communication.sms.model.Messages)
     */
    public void prepare(Messages messages) throws RequestException, ConvertException {
        Adapter adapter = _provider.getAdapter();
        _request = adapter.getRequest();
        _response = adapter.getResponse();
        _request.setMessages(messages);        
        _response.setMessages(messages);
        _request.prepare();
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Sender#send()
     */
    public Messages send() throws CommunicationError, ResponseException {
        Messenger messenger = _provider.getMessenger();                
        messenger.service(_request, _response);
        return _response.getMessages();
    }

}

