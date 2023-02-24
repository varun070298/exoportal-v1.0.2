/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.io.File;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContentDisplayer.java,v 1.2 2004/08/07 18:11:26 tuan08 Exp $
 */
public class UIFileNodeInfo extends UIGrid implements ExplorerListener {
  private static Parameter[]  DELETE_PARAMS = {new Parameter(ACTION, DELETE_ACTION)} ;
  
  private Cell parentURI_ = new Cell("") ;
  private Cell name_ = new Cell("") ;
  private Cell type_ = new Cell("") ;
  private Cell owner_ = new Cell("") ;
  
  public UIFileNodeInfo() {
    add(new HeaderRow().
        add(new LabelCell("#{UIFileNodeInfo.header.property}")).
        add(new LabelCell("#{UIFileNodeInfo.header.value}")));
    add(new Row().
        add(new LabelCell("#{UIFileNodeInfo.label.parent-uri}")).add(parentURI_).setClazz("even"));
    add(new Row().
        add(new LabelCell("#{UIFileNodeInfo.label.local-name}")).add(name_).setClazz("odd"));
    add(new Row().
        add(new LabelCell("#{UIFileNodeInfo.label.type}")).add(type_).setClazz("even"));
    add(new Row().
        add(new LabelCell("#{UIFileNodeInfo.label.owner}")).add(owner_).setClazz("odd"));
    add(new Row().
        add(new ActionCell().
            add(new Button("#{UIFileNodeInfo.button.delete}", DELETE_PARAMS)).
            addColspan("2")));
    addActionListener(RemoveNodeActionListener.class, DELETE_ACTION) ;
  }
  
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  {
    parentURI_.setValue(node.getParentUri()) ;
    name_.setValue(node.getName()) ;
    type_.setValue(node.getNodeType()) ;
    owner_.setValue(node.getOwner()) ;
  }
  
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  {
    
  }
  
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  {}
  
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  {
    
  }
  
  static public class RemoveNodeActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIFileNodeInfo uiInfo = (UIFileNodeInfo) event.getComponent() ;
      UIFileExplorer uiExplorer = 
        (UIFileExplorer) uiInfo.getAncestorOfType(UIFileExplorer.class) ;
      NodeDescriptor node = uiExplorer.getSelectNode() ;
      InformationProvider iprovider = findInformationProvider(uiInfo) ;
      if("/".equals(node.getUri())) {
        iprovider.addMessage(new ExoFacesMessage("#{UIFileNodeInfo.msg.remove-root-node}")) ;
        return ;
      }
      String realPath = uiExplorer.getRealPathBaseDir() + node.getUri() ;
      File file = new File(realPath) ;
      if(delete(file)) {
        uiExplorer.broadcastOnRemove(node);
        uiExplorer.changeNode(node.getParentUri()) ;
        iprovider.addMessage(new ExoFacesMessage("#{UIFileNodeInfo.msg.remove-node-ok}")) ;
      } else {
        iprovider.addMessage(new ExoFacesMessage("#{UIFileNodeInfo.msg.remove-node-fail}")) ;
      }
    }
    
    public static boolean delete(File file) {
      if (file.isDirectory()) {
        File[] children = file.listFiles();
        for (int i=0; i<children.length; i++) {
          if (delete(children[i]))  return false;
        }
      }
      return file.delete();
    }
  }
}