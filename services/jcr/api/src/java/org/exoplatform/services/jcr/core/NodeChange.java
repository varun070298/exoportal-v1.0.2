package org.exoplatform.services.jcr.core;


import javax.jcr.Property;
import java.util.Collection;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 20 juil. 2004
 * @version $Id: NodeChange.java 548 2005-01-24 11:04:03Z geaz $
 */
public interface NodeChange {

  public NodeData getNode();

  public int getState();

  public void setState(int state);

  public NodeData refreshNode(NodeData withNode) throws RepositoryException, PathNotFoundException;

  public String getPath();
}
