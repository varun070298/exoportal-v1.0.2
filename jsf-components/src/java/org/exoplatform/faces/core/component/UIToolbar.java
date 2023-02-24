 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.faces.core.component;

import java.util.ArrayList;
import java.util.List ;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.model.Button;
import org.exoplatform.faces.core.event.ExoActionEvent;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class UIToolbar extends UIExoCommand {
  
  public static final String  COMPONENT_FAMILY = "org.exoplatform.faces.core.component.UIToolbar" ;
  public static final String  BUTTON_PARAM = "button" ;
  
  private List leftButtons_ = new ArrayList();
  private List rightButtons_ = new ArrayList();

  public UIToolbar(){ 
    this("UIToolbar");
  }
  
  public UIToolbar(String id){             
    setId(id);
    setRendererType("ToolbarRenderer") ;
    setRendered(true);        
  }
  
  public void addLeftButton(Button button){
    leftButtons_.add(button);
  }

  public void addRightButton(Button button){
    rightButtons_.add(button);
  }  
  

  public List getLeftButtons() {  return leftButtons_; }

  public List getRightButtons() {  return rightButtons_; }
  
  public void decode(FacesContext context) {
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String comp = (String) paramMap.get(UICOMPONENT) ;
    if(getId().equals(comp)) {
      UIComponent parent = getParent() ;
      try{
        String action = (String) paramMap.get(ACTION) ;                
        parent.broadcast(new ExoActionEvent(parent, action, paramMap)) ;
      } catch (Exception ex) {
        ex.printStackTrace() ;
      }
    }
  }
  
  public void processDecodes(FacesContext context) {
    decode(context) ;
  }
  
  public String getFamily() {  return COMPONENT_FAMILY ; }  
  
}
