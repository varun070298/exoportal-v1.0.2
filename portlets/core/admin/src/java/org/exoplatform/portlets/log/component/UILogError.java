/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.log.component;

import java.util.* ;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.services.log.LogService;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UILogError.java,v 1.4 2004/08/05 04:16:47 tuan08 Exp $
 */
public class UILogError extends UILog {
  private static List LOG_ERROR_LEVEL_OPTIONS ;

  static {
    LOG_ERROR_LEVEL_OPTIONS = new ArrayList(5) ;
    LOG_ERROR_LEVEL_OPTIONS.add(new SelectItem("fatal", "0")) ;
    LOG_ERROR_LEVEL_OPTIONS.add(new SelectItem("error", "1")) ;
  }
  
  public UILogError(LogService service) {
    super(service) ;
    setId("UILogError");
  }

  protected void createUIComponents() {
    uiLogMessages_ = new UILogMessages() ;
    List list = getLogMessages(1 , 50) ;
    uiLogMessages_.setLogMessages(list) ; ;
    uiNumberOfMessages_ = new UISelectBox("numberOfMessages", "50", NUM_OF_MESSAGES_OPTIONS) ;
    uiLogLevel_ = new UISelectBox("level", "1", LOG_ERROR_LEVEL_OPTIONS) ;
  }

  protected List getLogBuffer() { return service_.getErrorBuffer() ; }
}
