/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.core;

import javax.jcr.Node;
import javax.jcr.Property;
import java.util.List;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeData.java,v 1.5 2004/09/03 09:58:51 geaz Exp $
 */

public interface NodeData extends Node {

  public Property getPermanentProperty(String name);

  public void addPermanentProperty(Property property);

  public List getPermanentProperties();

  public void removePermanentProperty(String name);

  public void refresh(Node withNode) throws PathNotFoundException, RepositoryException;

}
