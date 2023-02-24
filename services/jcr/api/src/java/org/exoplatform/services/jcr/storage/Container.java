/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.storage;

import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import org.exoplatform.services.jcr.core.NodeData;


/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: Container.java,v 1.10 2004/11/02 18:29:51 geaz Exp $
 */

public interface Container {

  public static final String DEFAULT_WORKSPACE = "dafault_workspace";

  public NodeData getRootNode() throws RepositoryException;

  public List getChildren(String absPath) throws RepositoryException;

  public NodeData getNodeByPath(String absPath) throws RepositoryException;

  public void add(Node node) throws ItemExistsException, RepositoryException;

  public void update(Node node) throws RepositoryException;

  public void delete(String absPath) throws RepositoryException;

}
