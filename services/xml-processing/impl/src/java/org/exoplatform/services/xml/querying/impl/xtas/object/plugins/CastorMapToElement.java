package org.exoplatform.services.xml.querying.impl.xtas.object.plugins;

/**
 * Castor map-to element's content
 * @version $Id: CastorMapToElement.java 566 2005-01-25 12:50:49Z kravchuk $
 */

public class CastorMapToElement {

    private String table;
    private String xml;
    private String nsUri;
    private String nsPrefix;
    private String ldapDn;
    private String ldapOc;

    public String getXml()
    { 
       return xml; 
    }

    public void setXml(String xml)
    { 
       this.xml = xml; 
    }


}
