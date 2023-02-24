package org.exoplatform.services.xml.querying;

/** This exception is thrown when condition occurred 
 * @version $Id: QueryRunTimeException.java 566 2005-01-25 12:50:49Z kravchuk $*/
public class QueryRunTimeException extends Exception {
    /** Constructs an Exception without a message. */
    public QueryRunTimeException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public QueryRunTimeException(String message) {
        super(message);
    }
}
