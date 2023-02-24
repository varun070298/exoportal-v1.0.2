/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import org.exoplatform.text.template.DataHandler ;
import org.exoplatform.text.template.DataHandlerManager ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public interface XhtmlDataHandlerManager extends DataHandlerManager {
  public String getBaseURL() ;
  public DataHandler getDataHandler(Class dataType) ;
}
