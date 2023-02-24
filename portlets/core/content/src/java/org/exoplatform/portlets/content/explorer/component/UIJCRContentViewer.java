/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import javax.jcr.Node ;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
public class UIJCRContentViewer extends UIContentViewer  {
  public UIJCRContentViewer() {
    
  }
  
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    if(!node.isLeafNode())  {
      setRendered(false) ;
      return;
    }
    String content = "" ;
    String mimeType = node.getNodeType() ;
    try {
      UIJCRExplorer uiJCRExplorer = (UIJCRExplorer) uiExplorer ;
      renderer_ =   getRenderer(mimeType) ;
      if (renderer_ != null) {
        if (mimeType.startsWith("text")) {
          Node jcrnode  = uiJCRExplorer.getCurrentNode() ;
          Node jcrcontent = jcrnode.getNode("jcr:content") ;
          content = jcrcontent.getProperty("exo:content").getValue().getString() ;
        } else {
          content =  "/content/FileContentServlet?path=" + node.getUri() ;
        }
      } else {
        String relativePath = node.getUri();
        renderer_ = getRenderer("default") ;
        content =  "/content/FileContentServlet?path=" + relativePath ;
      }
    } catch (Exception ex) {
      renderer_ =   getRenderer("text/plain") ;
      content = org.exoplatform.commons.utils.ExceptionUtil.getExoStackTrace(ex) ;
    }
    setContent(content) ;
    setContentType(mimeType) ;
    setRendered(true) ;
  }
  
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  {
   
  }
}
