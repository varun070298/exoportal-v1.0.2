/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.wsrp.component;

import java.util.* ;
import javax.faces.context.FacesContext ;
import javax.faces.component.UIComponentBase;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.services.wsrp.consumer.*;


/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIProducerNode.java,v 1.2 2004/06/07 01:42:44 benjmestrallet Exp $
 */
public class UIProducerNode extends UINode {
  public static final String VIEW_ID = "producer-node";
  private String tabTitle_;
  private UIProducerMenu uiProducerMenu;

  public UIProducerNode(UIProducerMenu uiProducerMenu,
                        UIProducerInfo uiProducerInfo ,
                        UIOfferedPortlet uiOfferedPortlet) {
    setId(VIEW_ID) ;
    setRendererType("ProducerNodeRenderer");
    tabTitle_ = "Producers";
    List children = getChildren() ;
    this.uiProducerMenu = uiProducerMenu;
    uiProducerMenu.setRendered(true);
    children.add(uiProducerMenu);
    uiProducerInfo.setRendered(true) ;
    children.add(uiProducerInfo) ;
    uiOfferedPortlet.setRendered(false) ;
    children.add(uiOfferedPortlet);
  }

  public String getName() {
    return tabTitle_;
  }

  public String getIcon() { return "no-icon" ;}

  public void decode(FacesContext context) {
  }

	public String getFamily() {
		return "org.exoplatform.portlets.wsrp.component.UIProducerNode" ;
	}

  public boolean hasProducer(){
    if(uiProducerMenu.getProducers().isEmpty())
      return false;
    else
      return true;
  }

}