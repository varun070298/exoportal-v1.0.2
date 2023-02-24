/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.Date;
import org.exoplatform.services.communication.forum.Post;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="FORUM_POST"
 */
public class PostImpl implements Post {
  private String id ;
  private String topicId ;
  private String forumId ;
  private String owner ;
  private Date createdDate ;
  private String modifiedBy ;
  private Date modifiedDate ;
  private String subject ;
  private String message ;
  private String remoteAddr ;
 
  public PostImpl() {
  }

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String  getId() { return id ; }
  public void  setId(String s) { id = s ; } 

  /**
   * @hibernate.property
   **/
  public String  getTopicId() { return topicId ; }
  public void  setTopicId(String id) { topicId = id ; }
  
  /**
   * @hibernate.property
   **/
  public String  getForumId() { return forumId ; }
  public void  setForumId(String id) { forumId = id ; }
  
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

  public long     getCreatedDateINT11() { return createdDate.getTime() ; }
  public void     setCreatedDateINT11(long d) { createdDate =  new Date(d * 1000) ; }

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

  public long     getModifiedDateINT11() { return modifiedDate.getTime() ; }
  public void     setModifiedDateINT11(long d) { modifiedDate = new Date(d * 1000) ; }
  
  /**
   * @hibernate.property
   **/
  public String getSubject() { return subject ;} 
  public void setSubject(String s) { subject = s ; }

  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getMessage() { return message  ; }
  public void setMessage(String s) { message = s ; }

  /**
   * @hibernate.property
   **/
  public String getRemoteAddr() { return remoteAddr ;} 
  public void setRemoteAddr(String s) { remoteAddr = s ;}
}
