/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 18, 2004 12:00:24 AM
 */
public class SmsUtil {

    public static String encodeHexEncoded(Object object) {
        StringBuffer buf = new StringBuffer();
        String token = object.toString();
        char[] array = token.toCharArray();
        for (int i = 0; i < array.length; i++) {
            String hexCode = Integer.toHexString(array[i]).toUpperCase();
            buf.append(hexCode);
        }
        return buf.toString();
    }

    public static String encodeUnicodeHexEncoded(Object object) {
        StringBuffer buf = new StringBuffer();
        String token = object.toString();
        char[] array = token.toCharArray();
        for (int i = 0; i < array.length; i++) {
            String hexCode = Integer.toHexString(array[i]).toUpperCase();
            hexCode = ("000" + hexCode).substring(hexCode.length() - 1);
            buf.append(hexCode);
        }
        return buf.toString();
    }

    public static boolean hasMobileNumberPrefix(String number) {
        if (number.startsWith("+")) return true;
        else if (number.startsWith("00")) return true;
        return false;
    }
    
    public static String truncateMobileNumberPrefix(String number) {
        // Remove potential prefix
        if (number.startsWith("+")) number = number.substring(1, number.length());
        else if (number.startsWith("00")) number = number.substring(2, number.length());
        return number;
    }

    public static String prepareCellularNumber(String number) {
        number = truncateMobileNumberPrefix(number);
        StringBuffer b = new StringBuffer(number.length());
        char[] a = number.toCharArray();
        for (int n = 0; n < a.length; n++) {
            char c = a[n];
            if (c >= '0' && c <= '9') b.append(c);
        }
        return b.toString();
    }

    public static boolean isAlpha(String token) {
        Pattern p = Pattern.compile("\\p{Alpha}");
        Matcher m = p.matcher(token);        
        return m.find();        
    }

}