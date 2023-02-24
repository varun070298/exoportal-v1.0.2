/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.log.component;

import java.util.* ;
import javax.faces.context.FacesContext ;

import org.apache.commons.logging.Log;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.services.log.LogMessage;
import org.exoplatform.services.log.LogService;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UILog.java,v 1.7 2004/08/05 04:16:47 tuan08 Exp $
 */
public class UILog extends UISimpleForm {
  protected static Log log_ = getLog("org.exoplatform.portal.portlets.log") ;

  protected static List NUM_OF_MESSAGES_OPTIONS ;
  private static List LOG_LEVEL_OPTIONS ;

  static {
    NUM_OF_MESSAGES_OPTIONS = new ArrayList(10) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 50", "50")) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 100", "100")) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 200", "200")) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 300", "300")) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 500", "500")) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 750", "750")) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 1000", "1000")) ;
    NUM_OF_MESSAGES_OPTIONS.add(new SelectItem("last 1500", "1500")) ;

    LOG_LEVEL_OPTIONS = new ArrayList(5) ;
    LOG_LEVEL_OPTIONS.add(new SelectItem("fatal", "0")) ;
    LOG_LEVEL_OPTIONS.add(new SelectItem("error", "1")) ;
    LOG_LEVEL_OPTIONS.add(new SelectItem("warn", "2")) ;
    LOG_LEVEL_OPTIONS.add(new SelectItem("info", "3")) ;
    LOG_LEVEL_OPTIONS.add(new SelectItem("debug", "4")) ;
    LOG_LEVEL_OPTIONS.add(new SelectItem("trace", "5")) ;
  }
   
  protected LogService service_ ;
  protected UILogMessages uiLogMessages_ ;
  protected UISelectBox uiNumberOfMessages_ ;
  protected UISelectBox uiLogLevel_ ;

  public UILog(LogService service) {
    super("logForm", "post", null) ;
    setId("UILog");
    service_ = service ;
    createUIComponents() ;
    add(new Row().
        add(new ComponentCell(this, uiLogMessages_)));
    add(new Row().
        add(new ListComponentCell().
            add(this, uiLogLevel_).
            add(this, uiNumberOfMessages_).
            add(new FormButton("#{UILog.button.refresh}", "refresh")).
            addAlign("center").addHeight("30"))) ;
  }
  
  protected void createUIComponents() {
    uiLogMessages_ = new UILogMessages() ;
    List list = getLogMessages(4 , 50) ;
    uiLogMessages_.setLogMessages(list) ; 
    uiNumberOfMessages_ = new UISelectBox("numberOfMessages", "50", NUM_OF_MESSAGES_OPTIONS) ;
    uiLogLevel_ = new UISelectBox("level", "4", LOG_LEVEL_OPTIONS) ;
  }

  public void decode(FacesContext context) {
    try {
      int number = Integer.parseInt(uiNumberOfMessages_.getValue()) ;
      int level = Integer.parseInt(uiLogLevel_.getValue()) ;
      List list = getLogMessages(level , number) ;
      uiLogMessages_.setLogMessages(list) ; 
    } catch (Exception ex) {
    }
  }

  protected List getLogMessages(int level, int number) {
    LinkedList list = new LinkedList() ;
    List logBuffer = getLogBuffer() ;
    int counter = 0;
    for (int i = logBuffer.size() - 1; i >= 0 ; i--) {
      LogMessage lm = (LogMessage) logBuffer.get(i) ;
      if (lm.getType() <= level) {
        list.addFirst(lm) ;
        counter++ ;
        if (counter == number) break ;
      }
    }
    return list ;
  }

  protected List getLogBuffer() { return service_.getLogBuffer() ; }
}