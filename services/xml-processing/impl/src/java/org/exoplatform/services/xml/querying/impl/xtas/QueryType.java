package org.exoplatform.services.xml.querying.impl.xtas;

import java.util.HashMap;

/**
 * Query types constants 
 * @version $Id: QueryType.java 566 2005-01-25 12:50:49Z kravchuk $
 */
public class QueryType {

    public final static String UPDATE = "update";
    public final static String SELECT = "select";
    public final static String DELETE = "delete";
    public final static String APPEND = "append";
    public final static String CREATE = "create";
    public final static String DROP   = "drop";
    private final static String XML   = "xml";
    private final static String RESOURCE   = "resource";
    private String type;
    private static HashMap types = new HashMap();

    static {

        types.put( UPDATE, XML );
        types.put( SELECT, XML );
        types.put( DELETE, XML );
        types.put( APPEND, XML );
        types.put( DROP, RESOURCE );
        types.put( CREATE, RESOURCE );

    }

    public static String[] getAll()
    {
        String s[]= {SELECT, UPDATE, DELETE, APPEND, CREATE, DROP};
        return s;
    }

    public static boolean isAtResource( String type )
    {
        return types.get( type ).equals( RESOURCE );
    }

    public static boolean isAtXml( String type )
    {
        return types.get( type ).equals( XML );
    }

    public static boolean exists( String type )
    {
        return types.get( type ) != null;
    }
}
