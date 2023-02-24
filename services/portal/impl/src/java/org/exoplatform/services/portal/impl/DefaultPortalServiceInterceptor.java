/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.portal.Interceptor;
import org.exoplatform.services.portal.community.CommunityConfigService;
import org.exoplatform.services.portal.community.CommunityNavigation;
import org.exoplatform.services.portal.community.CommunityPortal;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.portal.model.Page;
import org.exoplatform.services.portal.model.PageNode;
import org.exoplatform.services.portal.model.PortalConfig;

/**
 * Jul 15, 2004
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: DefaultInterceptor.java,v 1.6 2004/09/28 18:19:24 tuan08 Exp $
 */
public class DefaultPortalServiceInterceptor implements Interceptor {
	
	private HibernateService hservice_;

	private CommunityConfigService communityService_;

	public DefaultPortalServiceInterceptor(HibernateService hservice,
			CommunityConfigService communityService) throws Exception {
		hservice_ = hservice;
		communityService_ = communityService;
	}

	public PortalConfig getPortalConfig(String owner) throws Exception {
		CommunityPortal cp = communityService_.findCommunityPortal(owner);
		boolean shared = false;
		if (cp != null) {
			if (!cp.getPortal().equals("#{owner}")) {
				owner = cp.getPortal();
				shared = true;
			}
		}
		PortalConfigData data = (PortalConfigData) hservice_.findOne(
				PortalConfigData.class, owner);
		if (data == null)
			return null;
		PortalConfig config = data.getPortalConfig();
		if (shared) {
			config = (PortalConfig) config.softCloneObject();
			config.setOwner(owner);
			config.setEditPermission("noone");
		}
		return config;
	}

	public void savePortalConfig(PortalConfig config) throws Exception {
		hservice_.save(new PortalConfigData(config));
	}

	public Page getPage(String pageId) throws Exception {
		PageData data = (PageData) hservice_.findOne(PageData.class, pageId);
		if (data != null)
			return data.getPage();
		return null;
	}

	public void savePage(Page page) throws Exception {
		hservice_.save(new PageData(page));
	}

	public Node getNodeNavigation(String owner) throws Exception {
		NodeNavigationData data = (NodeNavigationData) hservice_.findOne(
				NodeNavigationData.class, owner);
		if (data == null)
			return null;
		PageNode pnode = data.getNodeNavigation().getNode();
		NodeImpl root = new NodeImpl(pnode, null, 0);
		List list = communityService_.findCommunityNavigation(owner);
				
		List sharedChildrenList = new ArrayList();
		
		for (int i = 0; i < list.size(); i++) {
			CommunityNavigation cn = (CommunityNavigation) list.get(i);
			data = (NodeNavigationData) hservice_.findOne(
					NodeNavigationData.class, cn.getNavigation());
			PageNode sharedNode = data.getNodeNavigation().getNode();
			List children = sharedNode.getChildren();
		
			for (int j = 0; j < children.size(); j++) {
				PageNode pageChild = (PageNode) children.get(j);
				NodeImpl shareChild = new NodeImpl(pageChild, root, 1);
				
				shareChild.setShare(true);
				
				sharedChildrenList.add(shareChild);
			}
		}
		
		Collections.sort(sharedChildrenList, NodeImpl.NameComparator);
		
		for (int j = 0; j < sharedChildrenList.size(); j++) {
		    root.addChild((NodeImpl) sharedChildrenList.get(j));
		}	
		
		return root;
	}

	public void saveNodeNavigation(String owner, Node node) throws Exception {
		NodeImpl root = (NodeImpl) node;
		// remove shared node
		PageNode prootNode = createPageNode(root);
		hservice_.save(new NodeNavigationData(owner, prootNode));
	}

	private PageNode createPageNode(NodeImpl node) {
		PageNode pnode = new PageNode();
		pnode.setUri(node.getUri());
		pnode.setName(node.getName());
		pnode.setViewPermission(node.getViewPermission());
		pnode.setEditPermission(node.getEditPermission());
		pnode.setIcon(node.getIcon());
		pnode.setLabel(node.getLabel());
		pnode.setDescription(node.getDescription());
		pnode.setPageReference(node.getPageReference());
		for (int i = 0; i < node.getChildrenSize(); i++) {
			NodeImpl child = (NodeImpl) node.getChild(i);
			if (!child.isShare()) {
				pnode.getChildren().add(createPageNode(child));
			}
		}
		return pnode;
	}
}
