/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.portlets.content.explorer.component.model.*;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
public class UIFileContentViewer extends UIContentViewer  {
  public UIFileContentViewer() {
    
  }
  
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    if(!node.isLeafNode())  {
      setRendered(false) ;
      return;
    }
    String content = "" ;
    String mimeType = node.getNodeType() ;
    try {
      UIFileExplorer uiFileExplorer = (UIFileExplorer) uiExplorer ;
      String uri = node.getUri() ;
      renderer_ =   getRenderer(mimeType) ;
      if (renderer_ != null) {
        if (mimeType.startsWith("text")) {
          FileNodeDescriptor fnode = (FileNodeDescriptor) node ;
          if(fnode.getCacheContent() == null ) {
            String realPath = uiFileExplorer.getRealPathBaseDir() + uri ;
            content = IOUtil.getFileContenntAsString(realPath) ;
            fnode.setCacheContent(content) ;
          } else {
            content = fnode.getCacheContent() ;
          }
        } else {
          String relativePath = uiFileExplorer.getRelativePathBaseDir() + uri ;
          content =  "/content/file" + relativePath ;
        }
      } else {
        String relativePath = uiFileExplorer.getRelativePathBaseDir() + uri ;
        renderer_ = getRenderer("default") ;
        content =  "/content/file" + relativePath ;
        /*
        content = 
          "\n<script language='JavaScript'>\n" +
          " myFloater = window.open('','myWindow','scrollbars=no,status=no,width=300,height=300');\n" +
          " myFloater.location = '/content/FileContentServlet?path=" + relativePath  + "';\n" +
          "</script>\n" ;
          */
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
    FileNodeDescriptor fnode = (FileNodeDescriptor) node ;
    setContent(fnode.getCacheContent()) ;
  }
}
