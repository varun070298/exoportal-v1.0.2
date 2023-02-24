package org.exoplatform.services.xml.querying;

/** This exception is thrown when something 
 * wrong with source resource
 * @version $Id: InvalidSourceException.java 566 2005-01-25 12:50:49Z kravchuk $*/
public class InvalidSourceException extends Exception {
    /** Constructs an Exception without a message. */
    public InvalidSourceException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public InvalidSourceException(String message) {
        super(message);
    }
}
