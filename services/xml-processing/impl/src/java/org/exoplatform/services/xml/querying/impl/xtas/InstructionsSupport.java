package org.exoplatform.services.xml.querying.impl.xtas;
import org.exoplatform.services.xml.querying.InvalidStatementException;

/**
 * XTAS Statement's instructions 
 * life-cycle supported operations.
 * @version $Id: InstructionsSupport.java 566 2005-01-25 12:50:49Z kravchuk $ 
 */
public interface InstructionsSupport {
    Instruction pickNextInstruction() throws ForbiddenOperationException, InvalidStatementException;

    void addInstruction (String type, String match, UniFormTree newValue) throws InvalidStatementException;

    Instruction[] getInstructions( );
}
