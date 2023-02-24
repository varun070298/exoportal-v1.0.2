/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.session.component;

import java.util.List ;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortalMonitor.java,v 1.3 2004/08/02 12:04:26 tuan08 Exp $
 */
public class UIActionHistory extends UIExoCommand {
  final public static Parameter[] BACK_PARAMS = {new Parameter(ACTION , CANCEL_ACTION) } ;

  private List actionHistory_ ;
  private Class backModule_ ;
  
  public UIActionHistory() {
    setRendererType("ActionHistoryRenderer");
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
	}
  
  public List getActionHistory() { return actionHistory_ ; }
  public void setActionHistory(Class backModule, List history) { 
    actionHistory_ = history ; 
    backModule_ = backModule ;
  }
  
  public String getFamily() { 
    return "org.exoplatform.portlets.monitor.session.component.UIActionHistory"; 
  }
  
  static public class CancelActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIActionHistory uiComp = (UIActionHistory) event.getSource() ;
      uiComp.setRenderedSibling(uiComp.backModule_) ;
    }
  }
}