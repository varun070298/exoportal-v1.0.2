/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider;

import org.exoplatform.services.communication.sms.adapter.Adapter;
import org.exoplatform.services.communication.sms.encoder.Resolver;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 3:36:09 PM
 */
public interface Provider {

    public Operator getOperator();

    public void setOperator(Operator operator);

    public Adapter getAdapter();

    public void setAdapter(Adapter adapter);
    
    public Messenger getMessenger();
    
    public void setMessenger(Messenger messenger);
    
    public Resolver getResolver();
    
}