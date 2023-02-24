/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;

import java.util.ArrayList;

import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.StringIterator;
import javax.jcr.Ticket;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.access.AccessDeniedException;
import javax.jcr.lock.Lock;

import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.impl.util.EntityCollection;
import org.exoplatform.services.log.LogUtil;


/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: ItemImpl.java,v 1.14 2004/09/16 15:26:53 geaz Exp $
 */

abstract public class ItemImpl implements Item {

  protected Log log;

  protected ItemLocation location;
  protected TicketImpl ticket;


  /**
   * Level1 constructor
   */
  public ItemImpl(String absPath) throws PathNotFoundException {
    log = LogUtil.getLog("org.exoplatform.services.jcr");

    if (absPath == null || absPath.length() == 0 || !(absPath.startsWith("/")))
      throw new PathNotFoundException("ItemImpl() Invalid Path '" + absPath + "'!");

    this.location = new ItemLocation(absPath);
  }

  /**
   * 6.2.4
   * Returns the path to this Item.
   * In level 2: If this Item has multiple paths, this method
   * returns one of those paths. How the implementation
   * chooses which path to return is left unspecified, the only
   * requirement is that for a given Item this method always
   * returns the same path. To get all paths to this Item use getPaths().
   */
  public String getPath() {
    return location.getPath();
  }

  /**
   * 7.1.1
   * Returns all paths to this Item.
   * In level 1: Returns the path to this Item, wrapped in an
   * array. This is the same path returned by getPath().
   */
/*  public StringIterator getPaths() {
    ArrayList list = new ArrayList();
    list.addAll(ticket.getNodesManager().getPaths(getPath()));
    EntityCollection paths = new EntityCollection(list);
    return paths;
  }
*/
  /**
   * 6.2.4
   * Returns the name of this Item. The name is the last item
   * in the path. If this Item is the root node of the repository
   * (i.e. if this.getDepth() == 0), an empty string will be returned.
   * In level 2: Returns one of the names of this Item. The
   * name returned is the last item in the path returned by getPath().
   */
  public String getName() {
    return location.getName();
  }


  /**
   * 6.2.4
   * Returns the ancestor of the specified absolute degree.
   * An ancestor of absolute degree x is the Item that is x
   * levels down along the path from the root node to this Item.
   * degree = 0 returns the root node.
   * degree = 1 returns the child of the root node along the path to this Item.
   * degree = 2 returns the grandchild of the root node along the path to this Item.
   * And so on to degree = n, where n is the depth of this Item,
   * which returns this Item itself. If degree > n is specified then an ItemNotFoundException
   * is thrown.
   * In level 2: Same behavior as level 1, but if multiple
   * paths exist to this Item then the path used is the same
   * one that is returned by getPath().
   */
  public Item getAncestor(int degree) throws ItemNotFoundException, AccessDeniedException, RepositoryException {
    try {
      log.debug("getAncestor(" + degree + ") " + ticket);
      int n = getDepth() - degree;
      if(n==0)
        return this;
      else if (n < 0)
        throw new ItemNotFoundException("Workspace.getAncestor() ancestor's degree > depth of this item.");
      else {
        ItemImpl item = (ItemImpl)ticket.getNodesManager().getNodeByPath(location.getAncestorPath(n));
        item.setTicket(ticket);
        return item;
      }
    } catch (PathNotFoundException e) {
      throw new ItemNotFoundException(e.getMessage(), e);
    }
  }


  /**
   * 6.2.4
   * Returns the parent of this Item.
   * In level 2: If this Item has multiple paths then this
   * method returns the parent along the path returned by getPath().
   */
  public Node getParent() throws ItemNotFoundException, AccessDeniedException, RepositoryException {
    try {
      return (Node) getAncestor(getDepth() - 1);
    } catch (PathNotFoundException e) {
      throw new ItemNotFoundException(e.getMessage(), e);
    }
  }

  /**
   * 7.1.1
   * Returns all the parents of this Item.
   * In level 1: Returns the parent of this Item, wrapped in a NodeIterator.
   */
  public NodeIterator getParents() throws ItemNotFoundException, AccessDeniedException, RepositoryException {

    ArrayList list = new ArrayList();
    StringIterator paths = getPaths();
    while(paths.hasNext()) {
        String path = new ItemLocation(paths.nextString()).getParentPath();
        Node node = ticket.getNodeByAbsPath(path);
        list.add(node);
    }
    return new EntityCollection(list);

  }

  /**
   * Returns the <code>Ticket</code> through which this <code>Item</code>
   * was acquired.
   * Every <code>Item</code> can ultimately be traced back through a series
   * of method calls to the call <code>{@link Ticket#getRootNode}</code>,
   * <code>{@link Ticket#getNodeByAbsPath}</code> or
   * <code>{@link Ticket#getNodeByUUID}</code>. This method returns that
   * <code>Ticket</code> object.
   *
   * @return the <code>Tickete</code> through which this <code>Item</code> was
   *         acquired.
   */
  public Ticket getTicket() {
    return ticket; 
  }


  /**
   * 6.2.4
   * Returns the depth below the root node of this Item (counting this Item itself):
   * The root node returns 0.
   * A property or child node of the root node returns 1.
   * A property or child node of a child node of the root returns 2.
   * And so on to this Item.
   * In Level 2: Same behavior as level 1, but if multiple
   * paths exist to this Item then the path used to determine
   * the depth is the same one that is returned by getPath().
   */
  public int getDepth() {
    return location.getDepth();
  }

  /**
   * 6.5
   * In some implementations, the identity of Node or Property Java
   * objects may not correspond to the identity of nodes or properties
   * as entities within the repository. For example, it may be possible
   * for two Java objects A and B to both represent the same actual
   * node in the repository such that any changes to A are reflected in
   * subsequent reads from B, but that nonetheless, A.equals(B)==false.
   * <p/>
   * Returns true if this Item Java object represents the same
   * repository item as the Java object otherItem.
   */
  public boolean isIdentical(Item otherItem) {
    if (otherItem == null)
      return false;

    if (!this.getClass().getName().equals(otherItem.getClass().getName()))
      return false;

    return isItemIdentical(otherItem);
  }


  protected abstract boolean isItemIdentical(Item otherItem);

  /* In level 1: Always returns true. */
  public boolean isGranted(long permissions) throws UnsupportedRepositoryOperationException, RepositoryException {
    return true;
  }

  /* level2 */
  public Lock lock(boolean recurse, boolean shared, int lockType) throws UnsupportedRepositoryOperationException, AccessDeniedException, RepositoryException {
    throw new UnsupportedRepositoryOperationException("Workspace.lock() is not supported by Level 1 of JCR.");
  }

  /* level2 */
  public void unlock(Lock lock) throws UnsupportedRepositoryOperationException, AccessDeniedException {
    throw new UnsupportedRepositoryOperationException("Workspace.unlock() is not supported by Level 1 of JCR.");
  }

  /* level2 */
  public Lock[] getLocks() throws UnsupportedRepositoryOperationException {
    throw new UnsupportedRepositoryOperationException("Workspace.getLocks() is not supported by Level 1 of JCR.");
  }

  /**
   * Returns <code>true</code> if there is a lock on this node, false
   * otherwise.
   *
   * @return a boolean.
   */
  public boolean hasLocks() {
    return false;
  }

  public void setTicket(TicketImpl ticket) {
    this.ticket = ticket;
  }

  void setLocation(ItemLocation loc) {
    this.location = loc;
  }
/*
  public boolean equals(Object obj) {
    if (!(obj instanceof ItemImpl))
      return false;
    return ((ItemImpl) obj).getPath() == this.getPath();
  }
*/
}
