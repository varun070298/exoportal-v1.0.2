/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.log.component;

import java.util.List ;
import org.exoplatform.faces.core.component.UIExoComponentBase;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UILogMessages.java,v 1.3 2004/06/02 21:45:12 tuan08 Exp $
 */
public class UILogMessages extends UIExoComponentBase {
  public static final String RENDERER_TYPE = "LogMessagesRenderer";
  
  private List logMessages_ ;
    
  public UILogMessages() {
  	setId("UILogMessages");
    setRendererType(RENDERER_TYPE) ;
  }
  
  public List getLogMessages() { return logMessages_ ; }
  public void setLogMessages(List list) { logMessages_ = list ; }
  
  public String getFamily() {
  	return "org.exoplatform.portlets.log.component.UILogMessages" ;
  }
  
}