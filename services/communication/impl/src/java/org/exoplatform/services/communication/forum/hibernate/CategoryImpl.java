/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.Date;
import org.exoplatform.services.communication.forum.Category;


/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 * @hibernate.class  table="FORUM_CATEGORY"
 */
public class CategoryImpl implements Category {
  private  String id ;
  private String owner ;
  private Date createdDate ;
  private String modifiedBy ;
  private Date modifiedDate ;
  private String name ;
  private String description ;
  private int categoryOrder ;
  
  public CategoryImpl() {
  }

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public  String  getId() { return id ; }
  public void  setId( String s) { id = s ; } 

  /**
   * @hibernate.property
   **/
  public String   getOwner() { return owner ; }
  public void     setOwner(String s) { owner = s ; }

  /**
   * @hibernate.property
   **/
  public Date     getCreatedDate() { return createdDate ; }
  public void     setCreatedDate(Date d) { createdDate = d ; }

  /**
   * @hibernate.property
   **/
  public String   getModifiedBy() { return modifiedBy ; }
  public void     setModifiedBy(String s) { modifiedBy = s ;}

  /**
   * @hibernate.property
   **/
  public Date     getModifiedDate() { return modifiedDate ; }
  public void     setModifiedDate(Date d) { modifiedDate = d ;}

  /**
   * @hibernate.property
   **/
  public String getCategoryName() { return name ; }
  public void setCategoryName(String s) { name = s ; }

  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getDescription() { return description ; }
  public void setDescription(String s) { description = s; }
  
  /**
   * @hibernate.property
   **/
  public int getCategoryOrder() { return categoryOrder ; }
  public void setCategoryOrder(int num) { categoryOrder = num ; }
}