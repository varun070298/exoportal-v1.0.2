/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import javax.jcr.Node ;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
public class UIJCRNodeInfo extends UIExoCommand implements ExplorerListener {
  private static Parameter[]  DELETE_PARAMS = {new Parameter(ACTION, DELETE_ACTION)} ;
  
  private Node currentNode_ ;
  
  public UIJCRNodeInfo() {
    setRendererType("JCRNodeInfoRenderer") ;
    addActionListener(RemoveNodeActionListener.class, DELETE_ACTION) ;
  }
  
  public Node getCurrentNode() { return currentNode_ ; }
  
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    UIJCRExplorer uiJCRExplorer = (UIJCRExplorer) uiExplorer ;
    currentNode_ = uiJCRExplorer.getCurrentNode() ;
  }
  
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  {
    
  }
  
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  {}
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  {
    
  }
  
  public String getFamily() {
    return "org.exoplatform.portlets.content.explorer.component.UIJCRNodeInfo" ;
  }
  
  static public class RemoveNodeActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
     
    }
  }
}