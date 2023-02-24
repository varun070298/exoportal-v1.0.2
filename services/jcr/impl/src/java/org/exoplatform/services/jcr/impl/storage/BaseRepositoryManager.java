/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage;


import java.util.Calendar;
import java.util.Date;
import org.exoplatform.services.jcr.storage.RepositoryManager;


/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: BaseRepositoryManager.java,v 1.1 2004/08/23 10:31:38 geaz Exp $
 */

abstract public class BaseRepositoryManager implements RepositoryManager {

  public Calendar getCurrentTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    return cal;
  }

}
