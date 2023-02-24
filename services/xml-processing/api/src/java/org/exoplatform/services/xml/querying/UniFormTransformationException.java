package org.exoplatform.services.xml.querying;

/** This exception is thrown when UniFormTree object
 * can not be created
 * @version $Id: UniFormTransformationException.java 566 2005-01-25 12:50:49Z kravchuk $*/
public class UniFormTransformationException extends Exception {
    /** Constructs an Exception without a message. */
    public UniFormTransformationException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public UniFormTransformationException(String message) {
        super(message);
    }
}
