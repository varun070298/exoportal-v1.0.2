package org.exoplatform.services.xml.querying.impl.xtas;

/** This exception is thrown when 
 * with object's state is unappropriate
 * @version $Id: ForbiddenOperationException.java 566 2005-01-25 12:50:49Z kravchuk $*/
public class ForbiddenOperationException extends Exception {
    /** Constructs an Exception without a message. */
    public ForbiddenOperationException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
