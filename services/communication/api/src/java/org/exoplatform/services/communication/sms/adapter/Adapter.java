/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.adapter;

import org.exoplatform.services.communication.sms.provider.*;

/**
 * 
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.net
 */
public interface Adapter {

    public Provider getProvider();

    public SmsRequest getRequest();

    public SmsResponse getResponse();

}