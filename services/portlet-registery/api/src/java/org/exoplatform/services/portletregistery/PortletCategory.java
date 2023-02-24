package org.exoplatform.services.portletregistery;

import java.util.Date;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juin 2004
 */
public interface PortletCategory {

  public String     getId() ;

  public String getPortletCategoryName();
  public void   setPortletCategoryName(String s) ;

  public String   getDescription() ;
  public void     setDescription(String s) ;

  public Date     getCreatedDate() ;

  public Date     getModifiedDate() ;
}
