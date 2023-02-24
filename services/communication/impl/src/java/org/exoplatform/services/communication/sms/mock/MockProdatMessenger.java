/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.mock;

import org.exoplatform.services.communication.sms.common.ResponseException;
import org.exoplatform.services.communication.sms.provider.*;
import org.exoplatform.services.communication.sms.provider.prodat.ProdatMessengerImpl;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 8:09:31 PM
 */
public class MockProdatMessenger extends ProdatMessengerImpl implements Messenger {

    public MockProdatMessenger(Provider provider) {
        super(provider);
    }

    public void service(SmsRequest request, SmsResponse response) throws ResponseException {
        String ip = _provider.getOperator().getHost();
        int port = Integer.parseInt(_provider.getOperator().getPort());
        String payload = request.getPayload();
        statusSending(request.getMessages());
        response.setResult(MockResponse.RESPONSE);
        response.translate();

    }

}

