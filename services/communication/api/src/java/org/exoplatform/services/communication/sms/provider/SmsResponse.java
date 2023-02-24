/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider;

import org.exoplatform.services.communication.sms.common.ResponseException;
import org.exoplatform.services.communication.sms.model.Messages;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 6:29:33 PM
 */
public interface SmsResponse {
    public Messages getMessages();
    public void setMessages(Messages messages);
    public String getResult();
    public void setResult(String payload);
    public void translate() throws ResponseException;
}
