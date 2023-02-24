/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.ldap.test;

import java.util.Collection;
import java.util.Iterator;
import net.sf.hibernate.Transaction;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.organization.*;
import org.exoplatform.services.organization.impl.GroupImpl;
import org.exoplatform.test.BasicTestCase;
/**
 * Created by the eXo platform team
 * User: Daniel Summer
 * Date: 28 august 2004
 *
 */
public class TestLDAPOrganizationService extends BasicTestCase {

	private static OrganizationService service;
	private static HibernateService hservice_;
	private static LDAPService lservice_;

	public TestLDAPOrganizationService(String s) {
		super(s);
	}

	protected String getDescription() {
		return "test ldap organization service";
	}

	public void setUp() throws Exception {
		if (service == null) {
			PortalContainer manager = PortalContainer.getInstance();
			service =
				(OrganizationService) manager.getComponentInstanceOfType(
					OrganizationService.class);
			LogUtil.setLevel("org.exoplatform.services.organization",	LogService.DEBUG,true);
			LogUtil.setLevel("org.exoplatform.services.database", LogService.DEBUG, true);
			hservice_ =
				(HibernateService) manager.getComponentInstanceOfType(
					HibernateService.class);
			lservice_ = (LDAPService) manager.getComponentInstanceOfType(LDAPService.class);

		}
	}

	public void tearDown() throws Exception {
		hservice_.closeSession();
	}

	public void testUser() throws Exception {
		net.sf.hibernate.Session session = hservice_.openSession();
		String USER = "test";
		Transaction tx = session.beginTransaction();
		User user = service.createUserInstance();
		user.setUserName(USER);
		user.setPassword("exo");
		user.setFirstName("Exo");
		user.setLastName("Platform");
		user.setEmail("exo@exoportal.org");
		service.createUser(user);
		tx.commit();
		User u = service.findUserByName(USER);
		assertEquals(u.getFirstName(), "Exo");
		UserProfile up = service.findUserProfileByName(USER);
		assertTrue("Expect user profile is found: ", up != null);

		Query query = new Query();
		PageList users = service.findUsers(query);
		assertTrue("Expect 1 user found ", users.getAvailable() >= 1);

		u.setFirstName("Exo(Update)");
		service.saveUser(u);
		up.getUserInfoMap().put("user.gender", "male");
		service.saveUserProfile(up);
		up = service.findUserProfileByName(USER);
		String male = (String) up.getUserInfoMap().get("user.gender");
		assertEquals("Expect user profile is updated: ", "male", male);

		PageList piterator = service.getUserPageList(10);
		assertTrue(piterator.currentPage().size() > 1);

		service.removeUser(USER);
	}

	public void testGroup() throws Exception {
		Group groupParent = service.createGroupInstance();
		groupParent.setGroupName("GroupParent");
		service.createGroup(groupParent);
		assertTrue(((GroupImpl) groupParent).getId() != null);

		Group groupChild = service.createGroupInstance();
		groupChild.setGroupName("GroupChild");

		service.addChild(groupParent, groupChild);

		groupParent = service.findGroupById(groupParent.getId());
		assertEquals(groupParent.getGroupName(), "GroupParent");

		groupChild = service.findGroupById(groupChild.getId());
		assertEquals(groupChild.getParentId(), groupParent.getId());

		groupChild.setLabel("GroupRenamed");
		service.saveGroup(groupChild);
		assertEquals(
			service.findGroupById(groupChild.getId()).getLabel(),
			"GroupRenamed");

		service.removeGroup(groupChild);
		assertNotNull(service.findGroupById(groupParent.getId()));

		service.addChild(groupParent, groupChild);
		Collection groups = service.findGroups(groupParent);
		assertEquals(1, groups.size());

		service.removeGroup(groupParent);
		assertNull(service.findGroupById(groupParent.getId()));
		assertNull(service.findGroupById(groupChild.getId()));
	}

	public void testMembership() throws Exception {
		MembershipType mt = service.createMembershipTypeInstance();
		mt.setName("Hierarchical");
		mt.setDescription("This is a test");
		mt.setOwner("exo");
		service.createMembershipType(mt);
		mt = service.findMembershipType("Hierarchical");
		assertTrue("Membership is found", mt != null);
		mt.setDescription("This is a test (update)");
		service.saveMembershipType(mt);
		mt = service.findMembershipType("Hierarchical");
		assertTrue("Membership is found", mt != null);

		User user = service.createUserInstance();
		user.setUserName("benj");
		user.setPassword("benj");
		user.setFirstName("benjamin");
		user.setLastName("mestrallet");
		user.setEmail("benjmestrallet@users.sourceforge.net");
		service.createUser(user);

		Group group = service.createGroupInstance();
		group.setGroupName("Group");
		service.createGroup(group);

		Membership membership = service.createMembershipInstance();		
		membership.setMembershipType("Hierarchical");
		//service.createMembership(membership);
		//assertTrue(((MembershipImpl) membership).getId() != null);
		service.linkMembership("benj", group, membership);
		Collection groups =
			service.findGroupByMembership("benj", "Hierarchical");
		assertEquals("expect 1 group ", 1, groups.size());

		Collection c =
			service.findMembershipsByUserAndGroup("benj", group.getId());
		assertTrue(c.size() > 0);

		mt = service.removeMembershipType("Hierarchical");
		mt = service.findMembershipType("Hierarchical");
		assertTrue("Membership is not found", mt == null);
		//test cascade delete
		Collection memberships =
			service.findMembershipsByUserAndGroup("benj", group.getId());
		assertTrue(memberships.size() == 0);

		membership = service.createMembershipInstance();		
		membership.setMembershipType("Hierarchical");
		//service.createMembership(membership);
		service.linkMembership("benj", group, membership);

		groups = service.findGroupsOfUser("benj");
		assertEquals("expect 2 group: ", 2, groups.size());
		groups = service.findGroupByMembership("benj", "Hierarchical");
		Iterator i = groups.iterator();
		while (i.hasNext()) {
			Group g = (Group) i.next();
			System.out.println("-----------------------" + group.getId());
		}
		assertEquals("expect one group: ", 1, groups.size());
		group = (Group) groups.iterator().next();
		assertEquals(
			"find group with group name 'Group'",
			"Group",
			group.getGroupName());

		service.removeGroup(group);
		memberships = service.findMembershipsByUserAndGroup("benj", "Group");
		assertTrue(memberships.size() == 0);
		//need to delete the roles belong to the user as well
		service.removeUser("benj");
		memberships = service.findMembershipsByUser("benj");
		assertEquals("Check system role: ", 0, memberships.size());
	}
}