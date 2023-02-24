/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.encoder;

import org.exoplatform.services.communication.sms.common.ConvertException;
import org.exoplatform.services.communication.sms.encoder.Formatter;
import org.exoplatform.services.communication.sms.encoder.MessageFormat;
import org.exoplatform.services.communication.sms.util.SmsUtil;

/**
 * 
 * @author: Ove Ranheim
 * @email: oranheim@users.sourceforge.net
 */
public class UnicodeFormatter implements Formatter {
    
    private static Formatter _me;
    
    public UnicodeFormatter() {
    }

    public MessageFormat getFormat() {
        return MessageFormat.UNICODE_TEXT;
    }

    public Object convert(Object object) throws ConvertException{
        return SmsUtil.encodeUnicodeHexEncoded(object);
    }
    
    public static Formatter getInstance() {
        if (_me == null) _me = new UnicodeFormatter();
        return _me;
    }

}

