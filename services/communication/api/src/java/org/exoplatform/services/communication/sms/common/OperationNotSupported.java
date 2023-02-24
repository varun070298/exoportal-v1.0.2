/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.communication.sms.common;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 18, 2004 6:03:18 PM
 */
public class OperationNotSupported extends RuntimeException {

    /**
     * 
     */
    public OperationNotSupported() {
        super();
    }
    /**
     * @param message
     */
    public OperationNotSupported(String message) {
        super(message);
    }
    /**
     * @param message
     * @param cause
     */
    public OperationNotSupported(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param cause
     */
    public OperationNotSupported(Throwable cause) {
        super(cause);
    }
}
