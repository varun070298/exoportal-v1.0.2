/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIExplorer.java,v 1.2 2004/07/31 15:07:15 tuan08 Exp $
 */
abstract public class UIExplorer extends UIExoCommand  {
  final static public String CHANGE_NODE_ACTION = "changeNode" ;
  final static public String REMOVE_NODE = "removeNode" ;
  final static public String EDIT_NODE_CONTENT = "editContent" ;
  final static public String VIEW_NODE_CONTENT = "viewNodeContent" ;
  final static public String UPLOAD_FILE = "uploadFile" ;
  
  public static Parameter[] CHANGE_NODE = { new Parameter(ACTION , CHANGE_NODE_ACTION)} ;
  
  private NodeDescriptor selectNode_ ;
  
  public UIExplorer() {
  	setId("UIExplorer") ;
    setRendererType("ExplorerRenderer") ;
  }
   
  public NodeDescriptor getSelectNode() {  return selectNode_ ;  }
  
  protected void  setSelectNode(NodeDescriptor node) { 
    selectNode_ = node ;    
    broadcastOnChange(node) ;
  }
 
  public String getParentUri(String uri) {
    int idx = uri.lastIndexOf("/") ;
    String parentUri = "/" ;
    if(idx > 0) {
      parentUri = uri.substring(0, idx) ;
    } else if( idx == 0) {
      parentUri = "/" ;
    }
    return parentUri;
  }
  
  public abstract void changeNode(String uri) throws Exception ;
  
  public void  broadcastOnChange(NodeDescriptor node)   {
    List list = findDescendantsOfType(ExplorerListener.class) ;
    for(int i = 0 ; i < list.size(); i++) {
      ExplorerListener listener = (ExplorerListener) list.get(i) ;
      listener.onChange(this, node) ;
    }
  }
  
  public void  broadcastOnRemove(NodeDescriptor node)  {
    List list = findDescendantsOfType(ExplorerListener.class) ;
    for(int i = 0 ; i < list.size(); i++) {
      ExplorerListener listener = (ExplorerListener) list.get(i) ;
      listener.onRemove(this,node) ;
    }
  }
  
  public void  broacastOnModify(NodeDescriptor node)   {
    List list = findDescendantsOfType(ExplorerListener.class) ;
    for(int i = 0 ; i < list.size(); i++) {
      ExplorerListener listener = (ExplorerListener) list.get(i) ;
      listener.onModify(this,node) ;
    }
  }
  
  public void  broadcastOnAddChild(NodeDescriptor node)   {
    List list = findDescendantsOfType(ExplorerListener.class) ;
    for(int i = 0 ; i < list.size(); i++) {
      ExplorerListener listener = (ExplorerListener) list.get(i) ;
      listener.onAddChild(this,node) ;
    }
  }
}