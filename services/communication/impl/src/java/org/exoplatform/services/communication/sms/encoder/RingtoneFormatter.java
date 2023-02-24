/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.encoder;

import org.exoplatform.services.communication.sms.common.ConvertException;
import org.exoplatform.services.communication.sms.common.OperationNotSupported;
import org.exoplatform.services.communication.sms.encoder.Formatter;
import org.exoplatform.services.communication.sms.encoder.MessageFormat;

/**
 * 
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class RingtoneFormatter implements Formatter {

    private static Formatter _me;
    
    public RingtoneFormatter() {
    }

    public MessageFormat getFormat() {
        return MessageFormat.RINGTONE;
    }

    public Object convert(Object object) throws ConvertException {
        throw new OperationNotSupported("Not yet implemented!");
    }
    
    public static Formatter getInstance() {
        if (_me == null) _me = new RingtoneFormatter();
        return _me;
    }
    

}

