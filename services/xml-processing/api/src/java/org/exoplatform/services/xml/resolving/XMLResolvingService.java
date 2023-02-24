/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.resolving;

import org.xml.sax.EntityResolver;

/**
 * Created by The eXo Platform SARL        .
 *
 * XML resolving service - an abstract EntityResolver creator.
* @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: XMLResolvingService.java 565 2005-01-25 12:48:13Z kravchuk $
 */

public interface XMLResolvingService
{
  /**
   * Returns a pre-created EntityResolver
   * @return EntityResolver object
   */
   EntityResolver getEntityResolver();

}
