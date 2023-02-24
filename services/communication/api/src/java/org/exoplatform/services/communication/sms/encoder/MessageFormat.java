/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.encoder;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 17, 2004 12:54:22 PM
 */
public class MessageFormat {

    public static final MessageFormat PLAIN_TEXT = new MessageFormat("Text");
    public static final MessageFormat RINGTONE = new MessageFormat("Ringtone");
    public static final MessageFormat OPERATOR_LOGO = new MessageFormat("Operator Logo");
    public static final MessageFormat CALLER_GROUP_GRAPHIC = new MessageFormat("Caller Group Graphic");
    public static final MessageFormat PICTURE = new MessageFormat("Picture");
    public static final MessageFormat VCARD = new MessageFormat("vCard");
    public static final MessageFormat VCALENDAR = new MessageFormat("vCalender");
    public static final MessageFormat RAW_BINARY_UDH = new MessageFormat("RawBinaryUDH");
    public static final MessageFormat UNICODE_TEXT = new MessageFormat("Unicode");

    private String p_format;

    public MessageFormat(String format) {
        p_format = format;
    }

    public String toString() {
        return p_format;
    }

    public static MessageFormat parse(String format) {
        MessageFormat mf = null;
        if (format.equals(PLAIN_TEXT.toString())) mf = PLAIN_TEXT;
        else if (format.equals(RINGTONE.toString())) mf = RINGTONE;
        else if (format.equals(OPERATOR_LOGO.toString())) mf = OPERATOR_LOGO;
        else if (format.equals(CALLER_GROUP_GRAPHIC.toString())) mf = CALLER_GROUP_GRAPHIC;
        else if (format.equals(PICTURE.toString())) mf = PICTURE;
        else if (format.equals(VCARD.toString())) mf = VCARD;
        else if (format.equals(VCALENDAR.toString())) mf = VCALENDAR;
        else if (format.equals(RAW_BINARY_UDH.toString())) mf = RAW_BINARY_UDH;
        else if (format.equals(UNICODE_TEXT.toString())) mf = UNICODE_TEXT;
        return mf;
    }
        
}

