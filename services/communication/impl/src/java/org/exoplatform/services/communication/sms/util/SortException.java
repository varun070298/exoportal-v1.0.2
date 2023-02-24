/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.sms.util;

/**
 * 
 * @author: Ove Ranheim
 * @email:  oranheim@users.sourceforge.net
 */
public class SortException extends RuntimeException {

    public SortException() {
    }

    public SortException(String message) {
        super(message);
    }

    public SortException(String message, Throwable cause) {
        super(message, cause);
    }

    public SortException(Throwable cause) {
        super(cause);
    }
}