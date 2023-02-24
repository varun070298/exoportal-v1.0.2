/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider;

import org.exoplatform.services.communication.sms.common.ConvertException;
import org.exoplatform.services.communication.sms.common.RequestException;
import org.exoplatform.services.communication.sms.model.Messages;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 6:27:28 PM
 */
public interface SmsRequest {
    public Messages getMessages();
    public void setMessages(Messages messages);
    public String getPayload();
    public void setPayload(String payload);
    public void prepare() throws RequestException, ConvertException;
}
