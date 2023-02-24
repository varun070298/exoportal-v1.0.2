/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.common;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 2:43:08 PM
 */
public class ResponseException  extends Exception {

    /**
     * 
     */
    public ResponseException() {
        super();
    }
    /**
     * @param message
     */
    public ResponseException(String message) {
        super(message);
    }
    /**
     * @param message
     * @param cause
     */
    public ResponseException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param cause
     */
    public ResponseException(Throwable cause) {
        super(cause);
    }
}
