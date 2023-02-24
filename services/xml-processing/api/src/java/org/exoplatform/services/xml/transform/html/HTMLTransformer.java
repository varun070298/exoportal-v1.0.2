/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform.html;

import org.exoplatform.services.xml.transform.AbstractTransformer;
import java.util.Properties;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: HTMLTransformer.java 565 2005-01-25 12:48:13Z kravchuk $
 */

public interface HTMLTransformer extends AbstractTransformer {
    /**
     * Sets properties for Tidy parser
     * See Tidy properties
     */
    public void setOutputProperties(Properties props);

    public Properties getOutputProperties();

}
