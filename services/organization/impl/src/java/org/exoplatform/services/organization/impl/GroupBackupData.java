/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.impl;
/**
 * @author Tuan Nguyen
 * @hibernate.class  table="EXO_GROUP"
 */
public class GroupBackupData {
  
  private String id = null;
  private String groupName = null;
  private String type = null;
  private String parent = null;
  
  public GroupBackupData()  {
  
  }
  
  public GroupBackupData(String name) {
    groupName = name ;
  }
  
  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  /**
   * @hibernate.property
   **/
  public String getGroupName() { return groupName; }
  public void setGroupName(String name) { this.groupName = name; }

  /**
   * @hibernate.property
   **/
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  
  /**
   * @hibernate.property
   **/
  public String getParent() { return parent; }
  public void   setParent(String parent) { this.parent = parent; }
}