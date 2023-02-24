/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

/**
 * Jul 18, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NodeNavigation.java,v 1.1 2004/07/20 12:41:07 tuan08 Exp $
 */
public class NodeNavigation {
	private String			owner;
	private PageNode	node;

	public PageNode getNode() {	return node; }
	public void setNode(PageNode node) { this.node = node;}

	public String getOwner() { return owner;}
	public void setOwner(String owner) { this.owner = owner; }
}