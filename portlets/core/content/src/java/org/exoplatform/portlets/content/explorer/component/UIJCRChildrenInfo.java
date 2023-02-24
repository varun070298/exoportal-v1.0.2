/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node ;
import javax.jcr.NodeIterator ;
import org.exoplatform.portlets.content.explorer.component.model.FileNodeDescriptor;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
import org.exoplatform.portlets.content.explorer.component.model.JCRNodeDescriptor;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
public class UIJCRChildrenInfo extends UIChildrenInfo {
  
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  {
  }
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  {
  }
  
  protected List getChildren(UIExplorer uiExplorer, NodeDescriptor node) {
    UIJCRExplorer explorer = (UIJCRExplorer) uiExplorer ;
    List nodeChildren = new ArrayList() ;
    String parentUri = node.getUri() ;
    if ("/".equals(parentUri)) parentUri = "" ;
    Node jcrNode = explorer.getCurrentNode()  ;
    try {
      NodeIterator i = jcrNode.getNodes() ;
      while(i.hasNext()) {
        Node child =  i.nextNode();
        NodeDescriptor uiChild = new JCRNodeDescriptor(parentUri, child) ;
        nodeChildren.add(uiChild) ;
      }
    } catch (Exception ex) {
      ex.printStackTrace() ;
    }
    return nodeChildren ;
  }
}