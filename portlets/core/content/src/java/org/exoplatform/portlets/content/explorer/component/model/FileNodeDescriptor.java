/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.exoplatform.portlets.content.MimeTypeManager;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NodeDescriptor.java,v 1.2 2004/11/02 20:02:54 geaz Exp $
 */
public class FileNodeDescriptor implements NodeDescriptor {
  private String uri_ ;
  private String parentUri_ ; 
  private String name_ ;
  private String owner_ ;
  private MimeTypeManager.MimeType mimeType_ ;
  private Date createdDate_ ;
  private Date modifiedDate_ ;
  private boolean isVersioning_ ;
  private boolean isLeafNode_ ;
  private List children_ ;
  private String cacheContent_ ;
  
  public FileNodeDescriptor(File node, String uri, String parentUri) {
    uri_ = uri ;
    parentUri_ = parentUri;
    name_ = node.getName() ;
    owner_ = "N/A" ;
    Date date =  new Date(node.lastModified()) ;
    createdDate_ =  date ;
    modifiedDate_ = date ;
    isVersioning_ = false  ;
    if (node.isFile()) {
      isLeafNode_ = true ;
      mimeType_ = MimeTypeManager.getInstance().getMimeTypeByOfFile(name_) ;
    } else {
      isLeafNode_ = false ;
      mimeType_ = MimeTypeManager.getInstance().getDirectoryType() ;
    }
  }

  public String getUri() { return uri_ ; }
  public String getParentUri() { return parentUri_ ; }
  public String getName()  { return name_ ; }
  public String getOwner()  { return owner_ ; }
  public Date getModifiedDate()  { return modifiedDate_ ; }
  public Date getCreatedDate()  { return createdDate_ ; }
  public boolean isVersioning() { return isVersioning_ ; }
  public boolean isLeafNode() { return isLeafNode_ ; }
  public String getNodeType() { return mimeType_.getMimeType() ;}
  public String getIcon() { return mimeType_.getIcon() ;} 
  
  public List getChildren() {
  	if(children_ == null) children_ = new ArrayList() ;
  	return children_ ;
  }
  
  public void setChildren(List list)  {
    children_ = list ;
  }
  
  public String getCacheContent() { return cacheContent_ ; }
  public void   setCacheContent(String s) { cacheContent_ = s ; }
}