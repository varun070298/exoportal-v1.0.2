/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.communication.forum.Forum;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="FORUM"
 */
public class ForumImpl implements Forum {
  private String id ;
  private String owner ;
  private Date createdDate ;
  private String modifiedBy ;
  private Date modifiedDate ;
  private String lastPostBy ;
  private Date lastPostDate ;
  private String name ;
  private String description ;
  private int postCount ;
  private int topicCount ;
  private String viewForumRole = "guest";
  private String createTopicRole ; 
  private String replyTopicRole ; 
  private String moderators ; 
  private String categoryId ;
  private int forumOrder ;
  
  private transient Map moderatorMap_ ;

  public ForumImpl() {
  }

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public  String  getId() { return id ; }
  public void  setId( String s) { id = s ; } 
  
  /**
   * @hibernate.property
   **/
  public  String  getCategoryId() { return categoryId ; }
  public void  setCategoryId( String id) { categoryId = id ;} 

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
  public String getForumName() { return name ; }
  public void   setForumName(String s) { name = s ; }

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

  /**
   * @hibernate.property
   **/
  public int getTopicCount() { return  topicCount; }
  public void setTopicCount(int num) { topicCount = num; }
  void addTopicCount(int num) { topicCount += num; }
  
  /**
   * @hibernate.property
   **/
  public String getViewForumRole()  { return viewForumRole ; }
  public void   setViewForumRole(String s) { viewForumRole = s ; }

  /**
   * @hibernate.property
   **/
  public String getCreateTopicRole() { return createTopicRole ; }
  public void   setCreateTopicRole(String s) {createTopicRole = s ;}

  /**
   * @hibernate.property
   **/
  public String getReplyTopicRole() { return replyTopicRole ; }
  public void   setReplyTopicRole(String role) { replyTopicRole = role; }

  /**
   * @hibernate.property
   **/
  public String getModerators() { return moderators ; }
  public void   setModerators(String s) { moderators = s ; }
    
  /**
   * @hibernate.property
   **/
  public int getForumOrder() { return forumOrder ; }
  public void setForumOrder(int num) { forumOrder = num ; }
  
  public boolean isModerator(String user) {
    if(owner.equals(user)) return true ;
    if(moderatorMap_ == null) getAllModerators() ;
    if(moderatorMap_.containsKey(user)) return true ;
    return false ;
  }
  
  public Collection getAllModerators()  {
    if(moderatorMap_ == null) {
      moderatorMap_ = new HashMap() ;
      if (moderators != null) {
        String[] users = StringUtils.split(moderators, ",") ;
        for(int i = 0; i < users.length; i++) {
          String user = users[i].trim() ;
          moderatorMap_.put(user, user) ;
          
        }
      }
    }
    return moderatorMap_.values() ;
  }
}