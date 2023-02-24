package org.exoplatform.services.portal.model;

import java.util.* ;
import org.exoplatform.services.portal.PortalACL;
/**
 * 
 * @version $Revision: 1.9 $ $Date: 2004/09/28 15:13:52 $
 * @author Fahrid Djebbari (fdjebbari@users.sourceforge.net) 
 * 
 */
public interface Node {
	public String getUri();
  public void setUri(String s) ;
  
	public String getName();
  public void setName(String s) ;
	
	public String getLabel();
	public void   setLabel(String name);
  
  public String getResolvedLabel();
  public void   setResolvedLabel(ResourceBundle res);
	
	public String getIcon();
	public void   setIcon(String name);
  
  public String getViewPermission()  ;
  public void   setViewPermission(String s)  ; 
  
  public String getEditPermission()  ;
  public void   setEditPermission(String s)  ; 
  
	public String getDescription() ;
  public void   setDescription(String s) ;
	
  public boolean isVisible() ;
  public void    setVisible(PortalACL acl, String owner, String remoteUser) ;
  
  public Node getChild(int pos) ;
  public void addChild(Node node) ;
  public Node removeChild(int pos) ;
  public Node removeChild(String uri) ;
  public boolean hasChild(String name) ;
  public Node findNode(String uri) ;
  public int  getChildrenSize() ;
  public List  getChildren() ;
	
	public Node getParent() ;
	public void setParent(Node node) ;

  public PageReference getPageReference(String type) ;
	public List   getPageReference() ;
  public void   setPageReference(List list) ;
  public PageReference removePageReference(String type) ;
  
  public int getLevel() ;
  public void setSelectedPath(boolean b) ;
  public boolean isSelectedPath() ;
  public boolean isShare() ;
  
  public void visit(NodeVisitor visitor) ;
}