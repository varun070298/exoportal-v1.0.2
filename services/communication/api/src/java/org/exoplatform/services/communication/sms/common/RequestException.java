/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.common;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 2:43:30 PM
 */
public class RequestException  extends Exception {

    /**
     * 
     */
    public RequestException() {
        super();
    }
    /**
     * @param message
     */
    public RequestException(String message) {
        super(message);
    }
    /**
     * @param message
     * @param cause
     */
    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param cause
     */
    public RequestException(Throwable cause) {
        super(cause);
    }
}
