/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
import org.exoplatform.commons.utils.Formater;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
abstract public class UIChildrenInfo extends UIGrid implements ExplorerListener {
  private static Formater ft_ = Formater.getDefaultFormater() ;
  
  private NodeDescriptorDataHandler dataHandler_ ;
  
  public UIChildrenInfo() {
    setClazz("UIChildrenInfo") ;
    dataHandler_ = new NodeDescriptorDataHandler() ;
    add(new Rows(dataHandler_, "even", "odd").
        add(new Column("#{UIChildrenInfo.header.icon}", "icon")).
        add(new LinkColumn("#{UIChildrenInfo.header.locale-name}", "localName", "uri").
            addParameter(UIExplorer.CHANGE_NODE).
            setHeaderClass("left-indent").setCellClass("left-indent")).
        add(new Column("#{UIChildrenInfo.header.modified-date}", "modifiedDate")).
        add(new Column("#{UIChildrenInfo.header.created-date}", "createdDate")).
        add(new Column("#{UIChildrenInfo.header.owner}", "owner")).
        add(new Column("#{UIChildrenInfo.header.versioning}", "versioning"))) ;
    addActionListener(ChangeNodeActionListener.class, UIExplorer.CHANGE_NODE_ACTION) ;
  }
  
  static public class NodeDescriptorDataHandler extends ListDataHandler {
    private NodeDescriptor node  ;
    
    public String  getData(String fieldName)   {
      if("localName".equals(fieldName)) return node.getName() ;      
      if("owner".equals(fieldName)) return node.getOwner() ;
      if("createdDate".equals(fieldName)) return ft_.format(node.getCreatedDate()) ;
      if("modifiedDate".equals(fieldName)) return  ft_.format(node.getModifiedDate()) ;
      if("versioning".equals(fieldName)) return Boolean.toString(node.isVersioning()) ;
      if("uri".equals(fieldName)) return node.getUri() ;
      if("icon".equals(fieldName)) return node.getIcon() ;
      return null ;
    }
    
    public void setCurrentObject(Object o) { node = (NodeDescriptor) o; }
  }
  
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    if(node.isLeafNode()) {
      setRendered(false) ;
      return ;
    }
    List children = getChildren(uiExplorer, node) ;
    dataHandler_.setDatas(children) ;
    node.setChildren(children) ;
    setRendered(true) ;
  }
  
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  {
    
  }
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  {
    
  }
  
  abstract protected List getChildren(UIExplorer uiExplorer, NodeDescriptor node) ;
  
  
  static public class ChangeNodeActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIChildrenInfo uicomp = (UIChildrenInfo) event.getComponent() ;
      UIExplorer uiExplorer = 
        (UIExplorer) uicomp.getAncestorOfType(UIExplorer.class) ;
      String uri = event.getParameter(OBJECTID) ;
      if("../".equals(uri))  uri = uiExplorer.getSelectNode().getParentUri() ;
      uiExplorer.changeNode(uri);
    }
  }
}