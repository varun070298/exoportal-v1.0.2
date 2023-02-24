/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portlets.content.ContentUtil;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;
import org.exoplatform.services.portal.model.Node;
/**
 * Apr 26, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentTab.java,v 1.2 2004/07/26 02:27:18 tuan08 Exp $
 **/
public class UIContentTab extends UINode { 
  protected ContentConfig config_;
    
  public UIContentTab(ContentConfig config) throws Exception {    
    config_ = config;
  }
  
  public String getName() { 
    PortalResources appres = 
      (PortalResources)SessionContainer.getComponent(PortalResources.class);
    ResourceBundle res = appres.getApplicationOwnerResource() ;
    String text2return = null;
    try {
      text2return = res.getString(config_.getTitle());
    } catch(Exception ex){ 
      text2return = config_.getTitle();
    }
    return  text2return; 
  }  
  
  public void encodeBegin(FacesContext context) throws IOException  {
  }
  
  public void encodeChildren(FacesContext context) throws IOException  {
    ResponseWriter w = context.getResponseWriter() ;
    w.write("<div class='UIContentTab'>") ;
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
    Node selectedNode = portal.getSelectedNode();
    String content = ContentUtil.resolveContent(config_, selectedNode);
    if(content == null){
      w.  write("No configuration for the content") ;
    } else {
      w.  write(content) ;
    }
    w.write("</div>") ;
  }
  
  public void encodeEnd(FacesContext context) throws IOException  {
  }
}