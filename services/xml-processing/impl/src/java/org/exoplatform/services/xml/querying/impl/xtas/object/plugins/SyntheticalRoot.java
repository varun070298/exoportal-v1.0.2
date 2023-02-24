package org.exoplatform.services.xml.querying.impl.xtas.object.plugins;

import java.util.Collection;

/**
 * Root object for custom mapping.
 * (It is possible for Castor mapping only)
 * @version $Id: SyntheticalRoot.java 566 2005-01-25 12:50:49Z kravchuk $ 
 */
public class SyntheticalRoot {
    private Collection objects;

    public Collection getObjects(){ return objects; }

    public void setObjects(Collection objects){ this.objects = objects; }
}
