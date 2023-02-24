package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
 * Compiled (by XSLT) XTAS instruction
 * @version $Id: Command.java 566 2005-01-25 12:50:49Z kravchuk $
 */
public class Command
{
    private byte[] buf;

    public Command(byte[] bytes)
    {
        this.buf = bytes;
    }

    public InputStream getAsInputStream()
    {
        return new ByteArrayInputStream( buf );
    }

    public String toString()
    {
        return new String( buf );
    }

}
