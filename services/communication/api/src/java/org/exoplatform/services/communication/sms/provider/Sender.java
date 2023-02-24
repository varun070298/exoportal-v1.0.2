/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.provider;

import org.exoplatform.services.communication.sms.common.*;
import org.exoplatform.services.communication.sms.model.Messages;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 7:58:02 PM
 */
public interface Sender {

    public Provider getProvider();
    
    public void prepare(Messages messages) throws RequestException, ConvertException;

    public Messages send() throws CommunicationError, ResponseException;

    public void clear();

}
