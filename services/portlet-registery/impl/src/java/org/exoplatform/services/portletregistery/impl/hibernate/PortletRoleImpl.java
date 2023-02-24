/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletregistery.impl.hibernate;

import org.exoplatform.services.portletregistery.PortletRole;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juin 2004
 *
 * @hibernate.class  table="PORTLET_ROLE"
 * @hibernate.generator-class  table="hilo"
 */
public class PortletRoleImpl implements PortletRole{

  private String id;
  private String roleName;
  private String description;
  private String portletId ;
  //private Portlet portlet;

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
  public String getPortletRoleName() {
    return roleName;
  }
  public void setPortletRoleName(String roleName) {
    this.roleName = roleName;
  }

  /**
   * @hibernate.many-to-one class="org.exoplatform.services.portletregistery.impl.hibernate.PortletImpl"
   *                        column="portletId"
   **/
  /*
  public Portlet getPortlet() {
    return portlet;
  }
  public void setPortlet(Portlet portlet) {
    this.portlet = portlet;
  }
  */
  
  /**
   * @hibernate.property
   **/
  public String getPortletId() {    return portletId; }
  public void setPortletId(String portletId) {
    this.portletId = portletId;
  }
}