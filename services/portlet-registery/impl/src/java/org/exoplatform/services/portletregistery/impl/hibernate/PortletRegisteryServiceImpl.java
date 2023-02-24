/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletregistery.impl.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;
import org.exoplatform.services.portletregistery.*;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juin 2004
 */
public class PortletRegisteryServiceImpl implements PortletRegisteryService {
	
  private static String[] MAPPING =
  {
      "org/exoplatform/services/portletregistery/impl/hibernate/PortletCategoryImpl.hbm.xml",
      "org/exoplatform/services/portletregistery/impl/hibernate/PortletImpl.hbm.xml",
      "org/exoplatform/services/portletregistery/impl/hibernate/PortletRoleImpl.hbm.xml"
  };
	
	private static final String queryFindAllPortletCategories =
		"from u in class org.exoplatform.services.portletregistery.impl.hibernate.PortletCategoryImpl";
	private static final String queryPortletCategoryById =
		"from portletCategory in class org.exoplatform.services.portletregistery.impl.hibernate.PortletCategoryImpl " +
		"where portletCategory.id = ?";
	private static final String queryPortletCategoryByName =
		"from portletCategory in class org.exoplatform.services.portletregistery.impl.hibernate.PortletCategoryImpl " +
		"where portletCategory.portletCategoryName = ?";
	
	private static final String queryPortletByCategory =
		"from portlet in class org.exoplatform.services.portletregistery.impl.hibernate.PortletImpl " +
		"where portlet.portletCategoryId = ?";
	private static final String queryPortletById =
		"from portlet in class org.exoplatform.services.portletregistery.impl.hibernate.PortletImpl " +
		"where portlet.id = ?";
	private static final String queryPortletByDisplayName =
		"from p in class org.exoplatform.services.portletregistery.impl.hibernate.PortletImpl " +
		"where p.portletCategoryId = ? and p.displayName = ?";
	
	private static final String queryRoleByPortlet =
		"from pr in class org.exoplatform.services.portletregistery.impl.hibernate.PortletRoleImpl " +
		"where pr.portletId = ?";
	private static final String queryRoleById =
		"from pr in class org.exoplatform.services.portletregistery.impl.hibernate.PortletRoleImpl " +
		"where pr.id = ?";
	
	private HibernateService hibernateService_;
	private Log log;
	
	public PortletRegisteryServiceImpl(HibernateService hibernateService_,
			DatabaseService dbService, LogService logService) {
		this.hibernateService_ = hibernateService_;
		this.log = logService.getLog("org.exoplatform.services.portletregistery");
		hibernateService_.addMappingFiles(MAPPING);
		//checkDatabase(dbService);
	}
	
	public PortletCategory createPortletCategoryInstance() {
		return new PortletCategoryImpl();
	}
	
	public List getPortletCategories() throws Exception {
		Session session = hibernateService_.openSession();
		return getPortletCategories(session) ;
	}
	
	private List getPortletCategories(Session session) throws Exception {
		return session.find(queryFindAllPortletCategories);
	}
	
	public PortletCategory getPortletCategory(String id) throws Exception {
		Session session = hibernateService_.openSession();
		PortletCategory category = getPortletCategory(id, session);
		return category;
	}
	
	private PortletCategory getPortletCategory(String id, Session session) throws Exception {
		List l = session.find(queryPortletCategoryById, id, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet category " + id + " does not exist",
					PortletRegisteryException.PORTLET_CATEGORY_NOT_FOUND);
		}
		return (PortletCategory) l.get(0);
	}
	
	public PortletCategory addPortletCategory(PortletCategory portletCategory) throws Exception {
		Session session = hibernateService_.openSession();
		portletCategory = addPortletCategory(portletCategory, session);
		session.flush();
		return portletCategory;
	}
	
	private PortletCategory addPortletCategory(PortletCategory portletCategory, Session session) throws Exception {
		PortletCategoryImpl impl = (PortletCategoryImpl) portletCategory;
		Date now = new Date();
		impl.setId(IdentifierUtil.generateUUID(impl));
		impl.setCreatedDate(now);
		impl.setModifiedDate(now);
		session.save(impl);
		return impl;
	}
	
	public PortletCategory updatePortletCategory(PortletCategory portletCategory) throws Exception {
		Session session = hibernateService_.openSession();
		portletCategory = updatePortletCategory(portletCategory, session);
		session.flush();
		return portletCategory;
	}
	
	private PortletCategory updatePortletCategory(PortletCategory portletCategory, Session session) throws Exception {
		Date now = new Date();
		PortletCategoryImpl impl = (PortletCategoryImpl) portletCategory;
		impl.setModifiedDate(now);
		session.update(impl);
		return impl;
	}
	
	public PortletCategory removePortletCategory(String id) throws Exception {
		Session session = hibernateService_.openSession();
		PortletCategory category = removePortletCategory(id, session);
		session.flush();
		return category;
	}
	
	private PortletCategory removePortletCategory(String id, Session session) throws Exception {
		List l = session.find(queryPortletCategoryById, id, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet category " + id + " does not exist",
					PortletRegisteryException.PORTLET_CATEGORY_NOT_FOUND);
		}
		PortletCategory category = (PortletCategory) l.get(0);
		List portlets = getPortlets(category.getId(), session) ;
		for(int i = 0; i < portlets.size(); i++) {
			PortletImpl portlet = (PortletImpl) portlets.get(i) ;
			removePortlet(portlet.getId(), session) ;
		}
		session.delete(category);
		return category;
	}
	
	public PortletCategory removePortletCategoryByName(String name) throws Exception {
		Session session = hibernateService_.openSession();
		PortletCategory category = removePortletCategoryByName(name, session);
		session.flush();
		return category;
	}
	
	private PortletCategory removePortletCategoryByName(String name, Session session) throws Exception {
		List l = session.find(queryPortletCategoryByName, name, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet category " + name + " does not exist",
					PortletRegisteryException.PORTLET_CATEGORY_NOT_FOUND);
		}
		PortletCategory category = (PortletCategory) l.get(0) ;
    List portlets = getPortlets(category.getId(), session) ;
    for(int i = 0; i < portlets.size(); i++) {
      PortletImpl portlet = (PortletImpl) portlets.get(i) ;
    	removePortlet(portlet.getId(), session) ;
    }
		session.delete(category);
		return category;
	}
	
	public PortletCategory findPortletCategoryByName(String portletCategoryName) throws Exception {
		Session session = hibernateService_.openSession();
		PortletCategory category = findPortletCategoryByName(portletCategoryName, session);
		return category;
	}
	
	private PortletCategory findPortletCategoryByName(String portletCategoryName, Session session) throws Exception {
		List l = session.find(queryPortletCategoryByName, portletCategoryName, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet category " + portletCategoryName + " does not exist",
					PortletRegisteryException.PORTLET_CATEGORY_NOT_FOUND);
		}
		return (PortletCategory) l.get(0);
	}
	
	public List getPortlets(String portletCategoryId) throws Exception {
		Session session = hibernateService_.openSession();
		List portlets = getPortlets(portletCategoryId, session);
		return portlets;
	}
	
	private List getPortlets(String portletCategoryId, Session session) throws Exception {
		return session.find(queryPortletByCategory, portletCategoryId, Hibernate.STRING);
	}
	
	public Portlet getPortlet(String id) throws Exception {
		Session session = hibernateService_.openSession();
		Portlet portlet = getPortlet(id, session);
		return portlet;
	}
	
	private Portlet getPortlet(String id, Session session) throws Exception {
		List l = session.find(queryPortletById, id, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet " + id + " does not exist",
					PortletRegisteryException.PORTLET_NOT_FOUND);
		}
		return (Portlet) l.get(0);
	}
	
	public Portlet addPortlet(PortletCategory category, Portlet portlet) throws Exception {
		Session session = hibernateService_.openSession();
		portlet = addPortlet(category, portlet, session);
		session.flush();
		return portlet;
	}
	
	private Portlet addPortlet(PortletCategory category, Portlet portlet, Session session) throws Exception {
		Date now = new Date();
		PortletImpl impl = (PortletImpl) portlet;
		impl.setId(IdentifierUtil.generateUUID(impl));
		impl.setCreatedDate(now);
		impl.setModifiedDate(now);
		impl.setPortletCategoryId(category.getId());
		session.save(impl);
		return impl;
	}
	
	public Portlet removePortlet(String id) throws Exception {
		Session session = hibernateService_.openSession();
		Portlet portlet = removePortlet(id, session);
		session.flush();
		return portlet;
	}
	
	private Portlet removePortlet(String id, Session session) throws Exception {
		List l = session.find(queryPortletById, id, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet " + id + " does not exist",
					PortletRegisteryException.PORTLET_NOT_FOUND);
		}
		Portlet portlet = (Portlet) l.get(0);
    session.delete(queryRoleByPortlet, portlet.getId(), Hibernate.STRING);
		session.delete(portlet);
		return portlet;
	}
	
	public Portlet updatePortlet(Portlet portlet) throws Exception {
		Session session = hibernateService_.openSession();
		portlet = updatePortlet(portlet, session);
		session.flush();
		return portlet;
	}
	
	private Portlet updatePortlet(Portlet portlet, Session session) throws Exception {
		Date now = new Date();
		PortletImpl impl = (PortletImpl) portlet;
		impl.setModifiedDate(now);
		session.update(impl);
		return impl;
	}
	
	public void findPortletByDisplayName(String portletCategory, String displayName, Session session)
	throws Exception {
		Object[] args = new Object[]{portletCategory, displayName};
		Type[] types = new Type[]{Hibernate.STRING, Hibernate.STRING};
		
		List l = session.find(queryPortletByDisplayName, args, types);
		if (l.size() == 0) {
			throw new PortletRegisteryException("Portlet not found", PortletRegisteryException.PORTLET_NOT_FOUND);
		}
	}
	
	public Portlet createPortletInstance() {
		return new PortletImpl();
	}
	
	public List getPortletRoles(String portletId) throws Exception {
		Session session = hibernateService_.openSession();
		return  getPortletRoles(portletId, session);
	}
	
	private List getPortletRoles(String portletId, Session session) throws Exception {
		return session.find(queryRoleByPortlet, portletId, Hibernate.STRING);
	}
	
	public PortletRole getPortletRole(String id) throws Exception {
		Session session = hibernateService_.openSession();
		return  getPortletRole(id, session);
	}
	
	private PortletRole getPortletRole(String id, Session session) throws Exception {
		List l = session.find(queryRoleById, id, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet role " + id + " does not exist",
					PortletRegisteryException.PORTLET_ROLE_NOT_FOUND);
		}
		return (PortletRole) l.get(0);
	}
	
	public PortletRole addPortletRole(Portlet portlet, PortletRole portletRole) throws Exception {
		Session session = hibernateService_.openSession();
		portletRole = addPortletRole(portlet, portletRole, session);
		session.flush();
		return portletRole;
	}
	
	private PortletRole addPortletRole(Portlet portlet, PortletRole portletRole, Session session) throws Exception {
		PortletRoleImpl impl = (PortletRoleImpl) portletRole;
		if (portletRole.getId() == null)
			impl.setId(IdentifierUtil.generateUUID(impl));
		impl.setPortletId(portlet.getId());
		session.save(impl);
		return impl;
	}
	
	public PortletRole removePortletRole(String id) throws Exception {
		Session session = hibernateService_.openSession();
		PortletRole portletRole = removePortletRole(id, session);
		session.flush();
		return portletRole;
	}
	
	private PortletRole removePortletRole(String id, Session session) throws Exception {
		List l = session.find(queryRoleById, id, Hibernate.STRING);
		if (l.size() == 0) {
			throw new PortletRegisteryException("the portlet role " + id + " does not exist",
					PortletRegisteryException.PORTLET_ROLE_NOT_FOUND);
		}
		PortletRole portletRole = (PortletRole) l.get(0);
		session.delete(portletRole);
		return portletRole;
	}
	
	public PortletRole updatePortletRole(PortletRole portletRole) throws Exception {
		Session session = hibernateService_.openSession();
		portletRole = updatePortletRole(portletRole, session);
		session.flush();
		return portletRole;
	}
	
	private PortletRole updatePortletRole(PortletRole portletRole, Session session) throws Exception {
		session.save(portletRole);
		return portletRole;
	}
	
	public void clearPortletRoles(String portletId) throws Exception {
		Session session = hibernateService_.openSession();
		clearPortletRoles(portletId, session);
		session.flush();
	}
	
	private void clearPortletRoles(String portletId, Session session) throws Exception {
		List currentRoles = getPortletRoles(portletId, session);
		for (Iterator iterator = currentRoles.iterator(); iterator.hasNext();) {
			PortletRole portletRole = (PortletRole) iterator.next();
			removePortletRole(portletRole.getId(), session);
		}
	}
	
	public PortletRole createPortletRoleInstance() {
		return new PortletRoleImpl();
	}
	
	public void updatePortletRoles(String portletId, Collection currentRoles) throws Exception {
		Session session = hibernateService_.openSession();
		clearPortletRoles(portletId, session);
		Portlet portlet = getPortlet(portletId, session);
		for (Iterator iterator = currentRoles.iterator(); iterator.hasNext();) {
			String role = (String) iterator.next();
			PortletRole portletRole = createPortletRoleInstance();
			portletRole.setPortletRoleName(role);
			addPortletRole(portlet, portletRole, session);
		}
		session.flush();
	}
	
	public void importPortlets(Collection portletDatas) throws Exception {
		Session session = hibernateService_.openSession();
		for (Iterator iterator = portletDatas.iterator(); iterator.hasNext();) {
			PortletRuntimeData portletRuntimeData = (PortletRuntimeData) iterator.next();
			String portletCategoryName = portletRuntimeData.getPortletAppName();
			String portletName = portletRuntimeData.getPortletName();
			PortletCategory portletCategory = null;
			try {
				portletCategory = findPortletCategoryByName(portletCategoryName, session);
			} catch (Exception e) {
				portletCategory = createPortletCategoryInstance();
				portletCategory.setPortletCategoryName(portletCategoryName);
				portletCategory = addPortletCategory(portletCategory, session);
			}
			try {
				findPortletByDisplayName(portletCategory.getId(), portletName, session);
			} catch (Exception e) {
				Portlet portlet = createPortletInstance();
				portlet.setDisplayName(portletName);
				portlet.setPortletApplicationName(portletCategoryName);
				portlet.setPortletName(portletName);
				addPortlet(portletCategory, portlet, session);
				PortletRole portletRole = createPortletRoleInstance();
				portletRole.setPortletRoleName(Constants.USER_ROLE);
				addPortletRole(portlet, portletRole, session);
			}
		}
		session.flush() ;
	}
	
	public void clearRepository() throws Exception {
		Session session = hibernateService_.openSession();
    session.delete("from pr in class org.exoplatform.services.portletregistery.impl.hibernate.PortletRoleImpl") ;
    session.delete("from pr in class org.exoplatform.services.portletregistery.impl.hibernate.PortletImpl") ;
    session.delete("from pr in class org.exoplatform.services.portletregistery.impl.hibernate.PortletCategoryImpl") ;
    /*
		List categories = getPortletCategories(session);
		for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
			PortletCategory portletCategory = (PortletCategory) iterator.next();
			removePortletCategory(portletCategory.getId(), session);
		}
		session.flush();
    */
	}
}