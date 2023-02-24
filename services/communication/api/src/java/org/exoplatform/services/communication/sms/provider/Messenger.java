/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider;

import org.exoplatform.services.communication.sms.common.CommunicationError;
import org.exoplatform.services.communication.sms.common.ResponseException;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 18, 2004 10:50:16 AM
 */
public interface Messenger {

    public void service(SmsRequest request, SmsResponse response) throws CommunicationError, ResponseException;
}
