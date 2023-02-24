/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component.model;

import java.util.Date;
import java.util.List;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NodeDescriptor.java,v 1.2 2004/11/02 20:02:54 geaz Exp $
 */
public interface NodeDescriptor {

  public String getUri()  ;
  public String getParentUri() ;
  public String getName()   ;
  public String getOwner() ;
  public Date getModifiedDate()   ;
  public Date getCreatedDate()   ;
  public boolean isVersioning()  ;
  public boolean isLeafNode() ;
  public String getNodeType();
  public String getIcon();
  
  public List getChildren() ;
  public void setChildren(List list) ;
}