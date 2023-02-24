/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.Date;
import org.exoplatform.services.communication.forum.Topic;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="FORUM_TOPIC"
 */
public class TopicImpl implements Topic {
  
  private String id ;
  private String forumId ;
  private String owner ;
  private Date createdDate ;
  private String modifiedBy ;
  private Date modifiedDate ;
  private String lastPostBy ;
  private Date lastPostDate ;
  private String topic ;
  private String description ;
  private int postCount ;

  public TopicImpl() {
  }

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String  getId() { return id ; }
  public void  setId(String s) { id = s ; } 

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
  public void     setModifiedDateINT11(long d) { modifiedDate = new Date(d * 1000)  ;}

  /**
   * @hibernate.property
   **/
  public String   getLastPostBy() { return lastPostBy ; }
  public void     setLastPostBy(String s) { lastPostBy = s ;}
  /**
   * @hibernate.property
   **/
  public Date     getLastPostDate() { return lastPostDate ; }
  public void     setLastPostDate(Date d) { lastPostDate = d ;}

  /**
   * @hibernate.property
   **/
  public String getTopic() { return topic ; }
  public void   setTopic(String s) { topic = s ; }

  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getDescription() { return description ; }
  public void setDescription(String s) { description = s; }

  /**
   * @hibernate.property
   **/
  public int getPostCount() { return postCount ; }
  public void setPostCount(int num) { postCount = num ; }
  void addPostCount(int num) { postCount += num; }
}
