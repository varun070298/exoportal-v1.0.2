/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;


import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.exoplatform.services.jcr.core.NodeChange;
import org.exoplatform.services.jcr.core.NodeData;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeChangeImpl.java,v 1.3 2004/09/16 15:26:53 geaz Exp $
 */

public class NodeChangeImpl implements NodeChange {

  private NodeData node;
  private int state;
  private String realPath;

//  public NodeChangeImpl(NodeData node, int state) {
//    this(node, state, node.getPath());
//  }

  public NodeChangeImpl(NodeData node, int state, String path) {
    this.node = node;
    this.state = state;
    this.realPath = path;
  }

  public NodeData getNode() {
    return node;
  }

  public int getState() {
    return state;
  }

  public String getPath() {
    return realPath;
  }

  public void setState(int state) {
    this.state = state;
  }

  public NodeData refreshNode(NodeData withNode) throws RepositoryException, PathNotFoundException {
    node.refresh(withNode);
    return node;
  }


  public String toString() {
    return "NODE CHANGE: state =" + NodeChangeState.getStateName(state) + " " + node;
  }
}
