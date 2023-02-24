/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.log.component;

import java.util.*;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.UIRadioBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.log.impl.ExoLog;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIConfig.java,v 1.7 2004/06/30 19:54:39 tuan08 Exp $
 */
public class UILogConfig extends UISimpleForm {
  protected static Log log_ = getLog("org.exoplatform.portal.portal.portlets.log") ;
  private static List LEVELS ;
 
  static {
    LEVELS = new ArrayList(6) ;
    LEVELS.add(new SelectItem("fatal", "0")) ;
    LEVELS.add(new SelectItem("error", "1")) ;
    LEVELS.add(new SelectItem("warn", "2")) ;
    LEVELS.add(new SelectItem("info", "3")) ;
    LEVELS.add(new SelectItem("debug", "4")) ;
    LEVELS.add(new SelectItem("trace", "5")) ;
  }
   
  private LogService service_ ;
  private String name_ ;
  private int availableLog_ ;
  private boolean admin_ ;
  
  public UILogConfig(LogService service) {
    super("configForm", "post", null) ;
    service_ = service ;
    name_ = "Configuration" ;
    setId("UILogConfig");
    admin_ = hasRole(CheckRoleInterceptor.ADMIN);
    update() ;
  }

  private void update() {
    clear() ;
    Collection logs = service_.getLogs() ;
    availableLog_ = logs.size() ;
    add(new HeaderRow().
        add(new Cell("#{UIConfig.header.log-name}")).
        add(new Cell("#{UIConfig.header.log-level}")));
    Iterator i = logs.iterator() ;
    while(i.hasNext()) {
      ExoLog log = (ExoLog) i.next() ;
      String category = log.getLogCategory() ;
      String level = Integer.toString(log.getLevel()) ;
      UIRadioBox uiRadio =  new UIRadioBox(category,level, LEVELS); 
      uiRadio.setClazz("radio") ;
      add(new Row().
          add(new LabelCell(category)).
          add(new ComponentCell(this, uiRadio).
              addAlign("center")));
    }
    add(new Row().
        add(new ListComponentCell().
        		add(new FormButton("#{UIConfig.button.save}", SAVE_ACTION)).
            addColspan("2").addAlign("center").addHeight("30"))) ;
  }
  
  public String getComponentType() { return COMPONENT_TYPE; }

  public void decode(FacesContext context) {
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String action = (String) paramMap.get(FacesConstants.ACTION) ;
    if (SAVE_ACTION.equals(action)) {
      if (!admin_) return ;
      List children = getChildren() ;
      try {
        for(int i = 0; i < children.size(); i++) {
          Object o = children.get(i) ;
          if (o instanceof UIRadioBox) {
            UIRadioBox child = (UIRadioBox) o ;
            int level = Integer.parseInt(child.getValue()) ;
            service_.setLogLevel(child.getName(), level, false ) ;
          }
        }
        update() ;
      } catch (Exception ex) {
        log_.error("Error: ", ex) ;
      }
    } else {
      Collection logs = service_.getLogs() ;
      if(availableLog_ < logs.size()) {
        update() ;
      }
    }
  }
}