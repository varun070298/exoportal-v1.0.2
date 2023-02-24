package org.exoplatform.services.xml.querying;

/** This exception is thrown when 
 * something wrong with destination resource
 * @version $Id: InvalidDestinationException.java 566 2005-01-25 12:50:49Z kravchuk $*/
public class InvalidDestinationException extends Exception {
    /** Constructs an Exception without a message. */
    public InvalidDestinationException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public InvalidDestinationException(String message) {
        super(message);
    }
}
