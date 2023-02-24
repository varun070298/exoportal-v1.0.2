/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.model;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 18, 2004 4:07:00 PM
 */
public interface Recipient {

    public Integer getId();

    public String getTo();

    public Recipient setTo(String to);

    public MessageStatus getStatus();

    public Recipient setStatus(MessageStatus status);

    public String getError();

    public Recipient setError(String error);

}

