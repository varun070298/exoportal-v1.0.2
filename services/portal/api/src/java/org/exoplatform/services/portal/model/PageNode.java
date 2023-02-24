/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

import java.util.* ;
/**
 * Thu, Apr 01, 2004 @ 11:02 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PageNode.java,v 1.12 2004/10/27 03:11:17 tuan08 Exp $
 */
public class PageNode  {
  protected ArrayList children ;
  protected String uri ;
  protected String name ;
  protected String label ;
  protected String icon ;
  protected String viewPermission ;
  protected String editPermission ;
  protected List pageReference ;
  protected String description ;
  
  public PageNode() {
  	pageReference = new ArrayList(3) ;
  }
  
  public String getUri() { return uri ; }
  public void   setUri(String s) { uri = s ; }

  public String getName() { return name ; }
  public void   setName(String s) { name = s ; }

  public String getLabel() { return label ; }
  public void   setLabel(String s) { label = s ; }
  
  public String getIcon() { return icon ; }
  public void   setIcon(String s) { icon = s ; }

  public String getViewPermission() { return viewPermission ; }
  public void   setViewPermission(String s) { viewPermission = s ; } 
  
  public String getEditPermission() { return editPermission ; }
  public void   setEditPermission(String s) { editPermission = s ; } 
  
  public String getDescription() { return description ; }
  public void   setDescription(String s) { description = s ; }
 
  public PageReference removePageReference(String type) {
    for(int i = 0; i < pageReference.size(); i++) {
      PageReference ref = (PageReference) pageReference.get(i) ;
      if(type.equals(ref.getType())) {
        pageReference.remove(i) ;
        return ref ;
      }
    }
    return null;
  }  
  
  public PageReference getPageReference(String type) {
    for(int i = 0; i < pageReference.size(); i++) {
      PageReference ref = (PageReference) pageReference.get(i) ;
      if(type.equals(ref.getType())) return ref ;
    }
    return null  ;
  }  
  
  public List getClonePageReference() { 
    List list = new ArrayList(3) ;
    for(int i = 0; i < pageReference.size(); i++) {
      PageReference pref = (PageReference)pageReference.get(i) ;
      list.add(new PageReference(pref)) ;
    }
    return list ;
  }
  
  public List getPageReference() { return pageReference ;}  
  public void   setPageReference(List list) { pageReference = list ;}
  
  public List getChildren() {
  	if(children == null) children = new ArrayList(3) ;
  	return children ;
  }
}