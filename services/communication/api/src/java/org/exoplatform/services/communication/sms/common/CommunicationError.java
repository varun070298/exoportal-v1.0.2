/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.common;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 20, 2004 2:42:32 PM
 */
public class CommunicationError extends Exception {

    /**
     * 
     */
    public CommunicationError() {
        super();
    }
    /**
     * @param message
     */
    public CommunicationError(String message) {
        super(message);
    }
    /**
     * @param message
     * @param cause
     */
    public CommunicationError(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param cause
     */
    public CommunicationError(Throwable cause) {
        super(cause);
    }
}
