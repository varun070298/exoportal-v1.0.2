/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.List;
import java.util.ArrayList ;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.event.UIComponentObserver;

/**
 * Jun 2, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UINode extends UIExoComponentBase	implements Node  {
	private String name_ ;
	private String icon_ ;
	private String description_ ;
  private List observers_ ;
	
	public String getName() { return name_ ; }
	public void   setName(String s) { name_ = s ;}
	
	public String getIcon() { return icon_ ; }
	public void   setIcon(String s) { icon_ = s ; }
	
	public String getDescription() { return description_ ;}
	public void   setDescription(String s) { description_ = s ; }
	
  public void addObserver(UIComponentObserver observer) {
    if(observers_ == null) observers_ = new ArrayList(3) ;
    observers_.add(observer) ;
  }
  
  public void broadcastOnChange() throws Exception {
    if(observers_ != null) {
      for(int i = 0; i < observers_.size(); i++) {
        UIComponentObserver observer = (UIComponentObserver) observers_.get(i) ;
        observer.onChange(this) ;
      }
    }
  }
  
	public void processDecodes(FacesContext context) {
    List children = getChildren() ;
    decode(context) ;
    if (context.getRenderResponse()) return ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i);
      if (child.isRendered()) {
        child.processDecodes(context) ;
        if (context.getRenderResponse()) return ;
      }
    }
  }
  
}