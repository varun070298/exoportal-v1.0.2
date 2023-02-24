/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Ticket;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.explorer.component.model.JCRNodeDescriptor;
import org.exoplatform.services.jcr.RepositoryService;
/**
 * Jun 22, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIFileExplorer.java,v 1.3 2004/08/17 13:07:02 tuan08 Exp $
 */
public class UIJCRExplorer extends UIExplorer {   
	private Ticket ticket_ ;
  private Node currentNode_ ;
  
	public UIJCRExplorer(RepositoryService repositoryService) throws Exception {
    Repository repository = repositoryService.getRepository();
    ticket_ = repository.login(null, "ws");//annonymous ticket    
    UINode uiDetail = (UINode)addChild(UINode.class) ;
    uiDetail.setRendererType("SimpleTabRenderer") ;
    UINode uiNodeContent = (UINode)uiDetail.addChild(UINode.class) ;
    UINode uiAdmin = (UINode)uiDetail.addChild(UINode.class) ;
    
    uiNodeContent.setRendererType("ChildrenRenderer") ;
    uiNodeContent.setName("View") ;
    uiNodeContent.setId("UINodeContent") ;
    uiNodeContent.addChild(UIJCRChildrenInfo.class) ;
    uiNodeContent.addChild(UIJCRContentViewer.class).setRendered(false) ;
    
    uiAdmin.setRendered(false) ;
    uiAdmin.setId("UIAdmin") ;
    uiAdmin.setName("Admin") ;
    uiAdmin.setRendererType("SimpleTabRenderer") ;
    uiAdmin.addChild(UIJCRNodeInfo.class) ;
    
    addActionListener(ChangeNodeActionListener.class, CHANGE_NODE_ACTION) ;
    
    changeNode("/") ;
  }
  
  public void changeNode(String uri) throws Exception {
    if(uri == null) uri = "/" ;
    currentNode_ = ticket_.getNodeByAbsPath(uri) ;
    setSelectNode(new JCRNodeDescriptor(currentNode_.getParent().getPath(), currentNode_));
  }
  
  public Node getCurrentNode() { return currentNode_ ; }
  
  public Ticket getTicket() { return ticket_ ; } 
  
  static public class ChangeNodeActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIJCRExplorer uiExplorer = (UIJCRExplorer) event.getComponent() ;
      String uri = event.getParameter("uri") ;
      if("../".equals(uri))  uri = uiExplorer.getSelectNode().getParentUri() ;
      uiExplorer.changeNode(uri);
    }
  }
}