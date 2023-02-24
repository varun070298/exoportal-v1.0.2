/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.encoder;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 18, 2004 6:12:46 PM
 */
public interface Resolver {
    public Formatter getFormatter(MessageFormat format);
    public Object getOperationCode(MessageFormat format);
}
