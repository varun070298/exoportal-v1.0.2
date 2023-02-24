/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider.prodat;

import org.exoplatform.services.communication.sms.adapter.Adapter;
import org.exoplatform.services.communication.sms.adapter.prodat.ProdatAdapterImpl;
import org.exoplatform.services.communication.sms.encoder.Resolver;
import org.exoplatform.services.communication.sms.encoder.prodat.ProdatResolverImpl;
import org.exoplatform.services.communication.sms.provider.*;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 9:08:23 PM
 */
public class ProdatProviderImpl implements Provider {

    private Operator _operator;
    private Adapter _adapter;
    private Messenger _messenger;
    private Resolver _resolver;
    
    public ProdatProviderImpl() {
        _operator = new ProdatOperatorImpl();
        _adapter = new ProdatAdapterImpl(this);
        _messenger = new ProdatMessengerImpl(this);
        _resolver = ProdatResolverImpl.getInstance();
    }
    
    public ProdatProviderImpl(String username, String password) {
        _operator = new ProdatOperatorImpl();
        _adapter = new ProdatAdapterImpl(this);
        _messenger = new ProdatMessengerImpl(this);
        _resolver = ProdatResolverImpl.getInstance();
        _operator.setUsername(username);
        _operator.setPassword(password);
    }
    
    /* 
     * @see org.exoplatform.services.communication.sms.provider.Provider#getOperator()
     */
    public Operator getOperator() {
        return _operator;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Provider#setOperator(org.exoplatform.services.communication.sms.provider.Operator)
     */
    public void setOperator(Operator operator) {
        _operator = operator;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Provider#getAdapter()
     */
    public Adapter getAdapter() {
        return _adapter;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Provider#setAdapter(org.exoplatform.services.communication.sms.adapter.Adapter)
     */
    public void setAdapter(Adapter adapter) {
        _adapter = adapter;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Provider#getMessenger()
     */
    public Messenger getMessenger() {
        return _messenger;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Provider#setMessenger(org.exoplatform.services.communication.sms.provider.Messenger)
     */
    public void setMessenger(Messenger messenger) {
        _messenger = messenger;
    }

    /* 
     * @see org.exoplatform.services.communication.sms.provider.Provider#getResolver()
     */
    public Resolver getResolver() {
        return _resolver;
    }

    

}
