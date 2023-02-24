package org.exoplatform.services.xml.querying;

/** This exception is thrown when 
 * invalid data for the statement occurs
 * @version $Id: InvalidStatementException.java 566 2005-01-25 12:50:49Z kravchuk $*/
public class InvalidStatementException extends Exception {
    /** Constructs an Exception without a message. */
    public InvalidStatementException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public InvalidStatementException(String message) {
        super(message);
    }
}
