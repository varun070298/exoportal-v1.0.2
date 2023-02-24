package org.exoplatform.services.portletregistery;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juin 2004
 */
public interface PortletRole {

  public String getId();

  public String getPortletRoleName();
  public void setPortletRoleName(String roleName);

  //public Portlet getPortlet();
}