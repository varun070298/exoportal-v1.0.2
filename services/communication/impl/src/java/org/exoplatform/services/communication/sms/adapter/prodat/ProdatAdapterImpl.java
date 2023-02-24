/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.adapter.prodat;

import org.exoplatform.services.communication.sms.adapter.Adapter;
import org.exoplatform.services.communication.sms.provider.*;
import org.exoplatform.services.communication.sms.provider.prodat.ProdatRequestImpl;
import org.exoplatform.services.communication.sms.provider.prodat.ProdatResponseImpl;

/**
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.net
 */
public class ProdatAdapterImpl implements Adapter {

    private Provider _provider;
    private SmsRequest _request;
    private SmsResponse _response;

    public ProdatAdapterImpl(Provider provider) {
        _provider = provider;
        _request = new ProdatRequestImpl(provider);
        _response = new ProdatResponseImpl(provider);
    }

    public Provider getProvider() {
        return _provider;
    }

    /*
     * @see org.exoplatform.services.communication.sms.adapter.Adapter#getRequest()
     */
    public SmsRequest getRequest() {
        return _request;
    }

    /*
     * @see org.exoplatform.services.communication.sms.adapter.Adapter#getResponse()
     */
    public SmsResponse getResponse() {
        return _response;
    }

}