/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.common;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 4:04:52 PM
 */
public class ConvertException extends Exception {

    /**
     * 
     */
    public ConvertException() {
        super();
    }
    /**
     * @param message
     */
    public ConvertException(String message) {
        super(message);
    }
    /**
     * @param message
     * @param cause
     */
    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param cause
     */
    public ConvertException(Throwable cause) {
        super(cause);
    }
}
