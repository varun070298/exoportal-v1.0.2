package org.exoplatform.services.xml.querying.impl.xtas;

/**
 * Factory prodused XTAS instructions executor
 * @version $Id: QueryProcessorFactory.java 566 2005-01-25 12:50:49Z kravchuk $ 
 */
public class QueryProcessorFactory {

    private static QueryProcessorFactory instance = null;

    /** @link dependency 
     * @stereotype instantiate*/
    /*# QueryProcessor lnkQueryProcessor; */

    protected QueryProcessorFactory(){}

    public QueryProcessor getProcessor()
    {
        return new QueryProcessor();
    }

    public static QueryProcessorFactory getInstance()
    {
        if (instance == null) {
            instance = new QueryProcessorFactory();
        }
        return instance;
    }
}
