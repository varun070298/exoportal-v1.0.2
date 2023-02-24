/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.encoder;

import org.exoplatform.services.communication.sms.common.ConvertException;

/**
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.net
 */
public interface Formatter {

    /**
     * Formatter service identifier, like Sms operator code for different
     * formats
     */
    public MessageFormat getFormat();

    /**
     * Convert source to wanted message body format
     */
    public Object convert(Object object) throws ConvertException;

}