/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.rss.component;

/*
 * Fri, May 30, 2003 @  
 * @author: Tuan Nguyen
 * @version: $Id: Item.java,v 1.2 2004/09/02 14:34:12 benjmestrallet Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class Item  {
  private String title_ ;
  private String description_ ;
  private String link_ ;
  
  public String getTitle() { return title_ ; }
  public void   setTitle(String title) { title_ = title ; }

  public String getLink() { return link_ ; }
  public void   setLink(String link) { link_ = link ; }

  public String getDescription() { return description_ ; }
  public void   setDescription(String desc) {description_ = desc ;}
}
