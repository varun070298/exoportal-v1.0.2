package org.exoplatform.services.portletregistery;

import java.util.Date;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juin 2004
 */
public interface Portlet {

  public String getId();

  public String getDisplayName();
  public void setDisplayName(String displayName);

  public String getPortletApplicationName();
  public void setPortletApplicationName(String portletApplicationName);

  public String getPortletName();
  void setPortletName(String name);

  public String   getDescription() ;
  public void     setDescription(String s) ;

  public Date     getCreatedDate() ;
  public void     setCreatedDate(Date d) ;

  public Date     getModifiedDate() ;
  public void     setModifiedDate(Date d) ;

  //public PortletCategory getPortletCategory();
}
