/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.exoplatform.portlets.content.explorer.component.model.FileNodeDescriptor;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
import org.exoplatform.services.cache.SimpleExoCache ;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
public class UIFileChildrenInfo extends UIChildrenInfo {
  private static SimpleExoCache cache_ = new SimpleExoCache(200) ;
  
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  {
    UIFileExplorer explorer = (UIFileExplorer) uiExplorer ;
    if(!node.isLeafNode()) {
      cache_.remove(explorer.getRelativePathBaseDir() + node.getUri()) ;
    }
    cache_.remove(explorer.getRelativePathBaseDir() + node.getParentUri()) ;
  }
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  {
    UIFileExplorer explorer = (UIFileExplorer) uiExplorer ;
    cache_.remove(explorer.getRelativePathBaseDir() + node.getUri()) ;
    onChange(uiExplorer, node) ;
  }
  
  protected List getChildren(UIExplorer uiExplorer, NodeDescriptor node) {
    List nodeChildren = (List) cache_.get(node.getUri()) ;
    if(nodeChildren != null) return nodeChildren;
    UIFileExplorer explorer = (UIFileExplorer) uiExplorer ;
    String realPath = explorer.getRealPathBaseDir() + node.getUri() ;
    File file = new File(realPath) ;
    File[] children = file.listFiles() ;
    nodeChildren = new ArrayList() ;
    String parentUri = node.getUri() ;
    if ("/".equals(parentUri)) parentUri = "" ;
    for (int i = 0 ; i < children.length ; i++) {
      File child = children[i] ;
      String childUri = parentUri + "/"  + child.getName() ;
      NodeDescriptor uiChild = new FileNodeDescriptor(child, childUri, parentUri) ;
      nodeChildren.add(uiChild) ;
    }
    cache_.put(explorer.getRelativePathBaseDir() + node.getUri(), nodeChildren) ;
    return nodeChildren ;
  }
}