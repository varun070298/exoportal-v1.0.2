package org.exoplatform.services.portletregistery;

import java.util.Collection;
import java.util.List;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juin 2004
 */
public interface PortletRegisteryService {

  public PortletCategory createPortletCategoryInstance() ;
  public List getPortletCategories() throws Exception;
  public PortletCategory getPortletCategory(String id) throws Exception;
  public PortletCategory addPortletCategory(PortletCategory portletCategory) throws Exception ;
  public PortletCategory updatePortletCategory(PortletCategory portletCategory) throws Exception ;
  public PortletCategory removePortletCategory(String id) throws Exception ;
  public PortletCategory removePortletCategoryByName(String name) throws Exception;
  public PortletCategory findPortletCategoryByName(String portletCategoryName) throws Exception ;

  public List     getPortlets(String portletCategoryId) throws Exception ;
  public Portlet  getPortlet(String id) throws Exception ;
  public Portlet  addPortlet(PortletCategory category, Portlet portlet) throws Exception ;
  public Portlet  removePortlet(String id) throws Exception ;
  public Portlet  updatePortlet(Portlet portlet) throws Exception ;
  public Portlet  createPortletInstance() ;

  public List        getPortletRoles(String portletId) throws Exception ;
  public PortletRole getPortletRole(String id) throws Exception ;
  public PortletRole addPortletRole(Portlet portlet, PortletRole portletRole) throws Exception ;
  public PortletRole removePortletRole(String id) throws Exception ;
  public void clearPortletRoles(String portletId) throws Exception ;
  public PortletRole updatePortletRole(PortletRole portletRole) throws Exception ;
  public PortletRole createPortletRoleInstance() ;
  public void updatePortletRoles(String currentPortletId, Collection currentRoles) throws Exception;

  public void importPortlets(Collection portletDatas) throws Exception;
  public void clearRepository() throws Exception;

}
