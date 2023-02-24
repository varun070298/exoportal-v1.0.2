/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletregistery.impl.hibernate;

import java.util.Date;
import java.util.Set;
import org.exoplatform.services.portletregistery.Portlet;


/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juin 2004
 *
 * @hibernate.class  table="PORTLET"
 */
public class PortletImpl implements Portlet{

  private String id;
  private String portletCategoryId ;
  private String portletApplicationName;
  private String portletName;
  private String description;
  private Date createdDate;
  private Date modifiedDate;
  //private PortletCategory portletCategory;
  private String displayName;
  private Set portletRoles;

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   **/
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @hibernate.property
   **/
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * @hibernate.property
   **/
  public String getPortletApplicationName() {
    return portletApplicationName;
  }
  public void setPortletApplicationName(String portletApplicationName) {
    this.portletApplicationName = portletApplicationName;
  }

  /**
   * @hibernate.property
   **/
  public String getPortletName() {
    return portletName;
  }
  public void setPortletName(String portletName) {
    this.portletName = portletName;
  }

  /**
   * @hibernate.property
   **/
  public String getDescription() {
    return description;
  }
  public void setDescription(String s) {
    description = s;
  }

  /**
   * @hibernate.property
   **/
  public Date getCreatedDate() {
    return createdDate;
  }
  public void setCreatedDate(Date d) {
    createdDate = d;
  }

  /**
   * @hibernate.property
   **/
  public Date getModifiedDate() {
    return modifiedDate;
  }
  public void setModifiedDate(Date d) {
    modifiedDate = d;
  }


  /**
   * @hibernate.many-to-one class="org.exoplatform.services.portletregistery.impl.hibernate.PortletCategoryImpl"
   *                        column="portletCategoryId"
   **/
  /*
  public PortletCategory getPortletCategory() {
    return portletCategory;
  }
  public void setPortletCategory(PortletCategory portletCategory) {
    this.portletCategory = portletCategory;
  }
  */
  
  /**
   * @hibernate.property
   **/
  public String getPortletCategoryId() {   return portletCategoryId; }
  public void setPortletCategoryId(String portletCategoryId) {
    this.portletCategoryId = portletCategoryId;
  }
  
  /**
   * @hibernate.set cascade="delete" lazy="true"
   * @hibernate.collection-key  column="portletId"
   * @hibernate.collection-one-to-many  class="org.exoplatform.services.portletregistery.impl.hibernate.PortletRoleImpl"
   **/
  /*
  public Set getPortletRoles() {
    return portletRoles;
  }

  public void setPortletRoles(Set portletRoles) {
    this.portletRoles = portletRoles;
  }
  */
}