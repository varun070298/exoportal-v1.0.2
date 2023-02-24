/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import java.io.File;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import org.exoplatform.faces.FacesUtil;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.content.ACL;
import org.exoplatform.portlets.content.FileACL;
import org.exoplatform.portlets.content.explorer.component.model.FileNodeDescriptor;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * Jun 22, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIFileExplorer.java,v 1.3 2004/08/17 13:07:02 tuan08 Exp $
 */
public class UIFileExplorer extends UIExplorer {
  private String reposistory_ ;
	private String relativePathBaseDir_ ;
  private String realPathBaseDir_ ;
	private ACL acl_ ;   
	
	public UIFileExplorer() throws Exception {
    init() ;
	  UINode uiDetail = (UINode)addChild(UINode.class) ;
    uiDetail.setRendererType("PyramidTabBarRenderer") ;
    UINode uiNodeContent = (UINode)uiDetail.addChild(UINode.class) ;
    
    uiNodeContent.setRendererType("ChildrenRenderer") ;
    uiNodeContent.setName("View") ;
    uiNodeContent.setId("UINodeContent") ;
    uiNodeContent.addChild(UIFileChildrenInfo.class) ;
    uiNodeContent.addChild(UIFileContentViewer.class).setRendered(false) ;
    
    if(acl_.hasWriteRole(null)) {
      UINode uiAdmin = (UINode)uiDetail.addChild(UINode.class) ;
      uiAdmin.setRendered(false) ;
      uiAdmin.setId("UIAdmin") ;
      uiAdmin.setName("Admin") ;
      uiAdmin.setRendererType("SimpleTabRenderer") ;
      uiAdmin.addChild(UIFileNodeInfo.class) ;
      UINode uiEditor = (UINode) uiAdmin.addChild(UINode.class) ;
      uiEditor.setId("UINodeEditor") ;
      uiEditor.setRendered(false) ;
      uiEditor.setRendererType("ChildrenRenderer") ;
      
      UIGrid grid = (UIGrid) uiEditor.addChild(UIGrid.class);
      grid.setId("UICreateOrUpload");
      
      UIFileUpload uiUpload = new UIFileUpload();
      uiUpload.setRendered(true) ;
      uiUpload.setClazz("UIUpload");
      uiUpload.addFileSystemActionListener() ;    
      
      grid.add(new Row().
               add(new ComponentCell(grid, new UIFileNodeEditor())));
      grid.add(new Row().
               add(new ComponentCell(grid, new UIChoiceLabel())));    
      grid.add(new Row().    
               add(new ComponentCell(grid, uiUpload)));
      uiEditor.addChild(UIFileContentEditor.class) ;
    }
    
	  addActionListener(ChangeNodeActionListener.class, CHANGE_NODE_ACTION) ;
    changeNode("/") ;
	}
	
	public void init() throws Exception {
    PortletPreferences prefs = FacesUtil.getPortletPreferences() ;
    String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() ;
    if(user == null) user = "" ;
    reposistory_ = FacesUtil.getServletContext().getInitParameter("file.portlet.reposistory") ;
    if(reposistory_ == null || reposistory_.length() ==0 || reposistory_.equals("default")) {
      reposistory_=  FacesUtil.getServletContext().getRealPath("/") ;
    }
    if(reposistory_.endsWith("/")) {
      reposistory_ = reposistory_.substring(0, reposistory_.length() - 1) ;
    }
    relativePathBaseDir_ = prefs.getValue("base-dir", "default") ;
    if(relativePathBaseDir_ == null || 
       relativePathBaseDir_.length() ==0 ||
       relativePathBaseDir_.equals("default")) {
      relativePathBaseDir_ =  "" ;
    }
    if(relativePathBaseDir_.endsWith("/")) {
      relativePathBaseDir_ = relativePathBaseDir_.substring(0, relativePathBaseDir_.length() - 1) ;
    }
    realPathBaseDir_ = reposistory_ + relativePathBaseDir_ ;
    String readRole = prefs.getValue("read-role", "any") ;
    String writeRole = prefs.getValue("write-role", "admin") ;
    acl_ = new FileACL(user, readRole, writeRole) ;
	}
	
  public String getRealPathBaseDir() { return realPathBaseDir_ ; }
  
  public String getRelativePathBaseDir() { return relativePathBaseDir_ ; }
  
  final public void changeNode(String uri) throws Exception {
    setSelectNode(createNodeDescriptor(uri)) ;
  }
	
  public NodeDescriptor createNodeDescriptor(String uri) {
    String parentUri = getParentUri(uri) ;
    String realPath = realPathBaseDir_ + uri ;
    File file = new File(realPath) ;
    NodeDescriptor node = new FileNodeDescriptor(file, uri, parentUri) ;
    return node ;
  }
  
	static public class ChangeNodeActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIFileExplorer uiExplorer = (UIFileExplorer) event.getComponent() ;
			String uri = event.getParameter("uri") ;
 		  if("../".equals(uri))  uri = uiExplorer.getSelectNode().getParentUri() ;
			uiExplorer.changeNode(uri);
		}
	}
	
}