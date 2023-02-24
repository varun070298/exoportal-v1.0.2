/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

import java.util.* ;

/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PageSet.java,v 1.2 2004/06/11 02:42:22 tuan08 Exp $
 **/
public class PageSet {
  private List pages = new ArrayList() ;
  
  public List getPages() { return pages ; }
  public void setPages(List list) { pages = list ; }
}