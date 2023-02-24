/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import java.util.*;

import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.portal.model.NodeVisitor;
import org.exoplatform.services.portal.model.PageNode;

/**
 * Thu, Apr 01, 2004 @ 11:02
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: NodeImpl.java,v 1.8 2004/10/27 03:11:17 tuan08 Exp $
 */
public class NodeImpl extends PageNode implements Node {
	
	transient private Node parent;

	transient String resolvedLabel_;

	transient private int level;

	transient boolean selected;

	transient boolean share = false;

	transient boolean visible = true;

	public NodeImpl() {

	}

	public NodeImpl(String uri, String name, String label) {
		this.uri = uri;
		this.name = name;
		this.label = label;
	}

	public NodeImpl(Node nodeImpl, Node parent, int level) {
		copyPageNode((PageNode) nodeImpl);
		this.parent = parent;
		this.level = level;
		this.selected = nodeImpl.isSelectedPath();
		this.share = nodeImpl.isShare();
		this.visible = nodeImpl.isVisible();
		List list = nodeImpl.getChildren();
		if (list != null) {
			children = new ArrayList(3);
			for (int i = 0; i < list.size(); i++) {
				nodeImpl = (NodeImpl) list.get(i);
				Node childNode = new NodeImpl(nodeImpl, this, level + 1);
				children.add(childNode);
			}
		}
	}

	public NodeImpl(PageNode pageNode, Node parent, int level) {
		copyPageNode(pageNode);
		this.parent = parent;
		this.level = level;
		selected = false;
		List list = pageNode.getChildren();
		if (list != null) {
			children = new ArrayList(3);
			for (int i = 0; i < list.size(); i++) {
				pageNode = (PageNode) list.get(i);
				Node childNode = new NodeImpl(pageNode, this, level + 1);
				children.add(childNode);
			}
		}
	}

	private void copyPageNode(PageNode node) {
		this.uri = node.getUri();
		this.name = node.getName();
		this.label = node.getLabel();
		this.viewPermission = node.getViewPermission();
		this.editPermission = node.getEditPermission();
		this.icon = node.getIcon();
		this.pageReference = node.getClonePageReference();
		this.description = node.getDescription();
	}

	public void setLabel(String s) {
		super.setLabel(s);
		resolvedLabel_ = s;
	}

	public String getResolvedLabel() {
		return resolvedLabel_;
	}

	public void setResolvedLabel(ResourceBundle res) {
		resolvedLabel_ = ExpressionUtil.getExpressionValue(res, label);
	}

	public boolean isShare() {
		return share;
	}

	public void setShare(boolean b) {
		share = b;
		if (children == null)
			return;
		for (int i = 0; i < children.size(); i++) {
			NodeImpl child = (NodeImpl) children.get(i);
			child.setShare(b);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(PortalACL acl, String owner, String remoteUser) {
		if (PortalACL.ANY_PERMISSION.equals(getViewPermission()))
			visible = true;
		if (!visible)
			visible = owner.equals(remoteUser);
		if (children == null)
			return;
		for (int i = 0; i < children.size(); i++) {
			NodeImpl child = (NodeImpl) children.get(i);
			child.setVisible(acl, owner, remoteUser);
		}
	}

	public void setParent(Node node) {
		parent = node;
	}

	public Node getParent() {
		return parent;
	}

	public int getLevel() {
		return level;
	}

	public void setSelectedPath(boolean b) {
		selected = b;
		if (parent != null) {
			parent.setSelectedPath(b);
		}
	}

	public boolean isSelectedPath() {
		return selected;
	}

	public Node getChild(int pos) {
		if (pos >= children.size())
			return null;
		return (Node) children.get(pos);
	}

	public void addChild(Node node) {
		node.setParent(this);
		if (children == null)
			children = new ArrayList(3);
		children.add(node);
	}

	public Node removeChild(int pos) {
		if (pos >= children.size())
			return null;
		NodeImpl node = (NodeImpl) children.remove(pos);
		node.setParent(null);
		return node;
	}

	public Node removeChild(String uri) {
		if (children == null)
			return null;
		for (Iterator i = children.iterator(); i.hasNext();) {
			NodeImpl child = (NodeImpl) i.next();
			if (child.getUri().equals(uri)) {
				i.remove();
				child.setParent(null);
				return child;
			}
		}
		return null;
	}

	public int getChildrenSize() {
		if (children == null)
			return 0;
		return children.size();
	}

	public Node findNode(String uri) {
		if (this.uri.equals(uri))
			return this;
		if (children == null)
			return null;
		for (int i = 0; i < children.size(); i++) {
			Node child = (Node) children.get(i);
			Node result = child.findNode(uri);
			if (result != null)
				return result;
		}
		return null;
	}

	public boolean hasChild(String name) {
		if (children == null)
			return false;
		for (int i = 0; i < children.size(); i++) {
			NodeImpl child = (NodeImpl) children.get(i);
			if (name.equals(child.getName()))
				return true;
		}
		return false;
	}

	public NodeImpl getThisNode() {
		return this;
	}

	public void visit(NodeVisitor visitor) {
		visitor.visit(this);
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				NodeImpl child = (NodeImpl) children.get(i);
				child.visit(visitor);
			}
		}
	}

	public static Comparator NameComparator = new Comparator() {
		
		public int compare(Object a, Object b) {

			if (!(a instanceof NodeImpl) || !(b instanceof NodeImpl))
				throw new ClassCastException("Two NodeImpl objects expected.");

			return (((NodeImpl) a).name).compareToIgnoreCase(((NodeImpl) b).name);
		}

	};	
	
}
